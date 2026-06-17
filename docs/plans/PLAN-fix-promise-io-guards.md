# Implementation Plan: fix/promise-io-guards

## Goal
Add try/catch guards and `.catch()` handlers to all unguarded async IO operations (CLUSTER 5 — R-11 violations) to prevent unhandled promise rejections that cause silent data loss in the Crew/Cloud layer.

## Source Analysis Link
[System Audit Report — CLUSTER 5](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) lines 133–149

## Files to Modify

| # | File | Line(s) | Issue |
|---|------|---------|-------|
| 1 | `src/context/AppConfigContext.tsx` | 24–27 | `await fetchAllSettings()` — no try/catch |
| 2 | `src/services/appLogger/AppLoggerStorage.ts` | 64–71 | `await AsyncStorage.setItem()` in `executePersist` — no try/catch |
| 3 | `src/services/LocationService.ts` | 122–129 | Supabase public sessions query — no try/catch |
| 4 | `src/services/CrewService/CrewRealtime.ts` | 47–51 | `AsyncStorage.multiRemove` not awaited (floating promise inside try) |
| 5 | `src/services/CrewService/CrewService.ts` | 90–95 | `supabase.removeChannel` — floating promise, no `.catch()` |
| 6 | `src/services/CrewService/CrewSessionManager.ts` | 360–364 | Floating `supabase.from('crew_members').delete()` chain |
| 7 | `src/services/CrewService/CrewSessionManager.ts` | 372–375 | Floating `supabase.removeChannel` inside `setTimeout` |

## Steps

### Step 1 — AppConfigContext.tsx: Wrap `refresh` in try/catch
**What:** Wrap the `await AppSettingsService.fetchAllSettings()` call at line 25 in a try/catch.
**Source:** `AppConfigContext.tsx:24–27` — `refresh` is an async function with no error boundary.
**How:** Add try/catch around lines 25–26. Catch block logs via `AppLogger.warn('[AppConfigContext] fetchAllSettings failed', { error, payload_size: 0, ssi: 0 })`. On failure, `setSettings({})` to maintain a safe fallback (already the default state).
- **Verify:** `npm run verify` passes. Grep the file for the new catch block.

### Step 2 — AppLoggerStorage.ts: Guard `executePersist`
**What:** Wrap the `AsyncStorage.setItem` call at line 68 in a try/catch.
**Source:** `AppLoggerStorage.ts:64–71` — `executePersist` is already called with `.catch()` by callers at lines 49 and 58, but the method itself has no guard around the `setItem`. If `JSON.stringify` throws (circular ref) or `setItem` rejects, the promise propagates up raw.
**How:** Wrap lines 65–70 in try/catch. Catch block: `if (__DEV__) console.warn('[AppLogger] executePersist setItem failed', e instanceof Error ? e.message : String(e));` — mirrors the existing pattern at lines 50 and 59. Do NOT use `AppLogger.warn` here to avoid re-entrant logging loops.
- **Verify:** `npm run verify` passes. Confirm the catch exists and does NOT call `AppLogger` (re-entrancy guard).

### Step 3 — LocationService.ts: Guard public sessions query
**What:** Wrap the unguarded Supabase query at lines 123–129 in a try/catch.
**Source:** `LocationService.ts:122–129` — the `publicData` query has no error handling. If Supabase returns an error or throws, the entire `getNearbyPublicSessions` crashes.
**How:** Wrap lines 123–129 in try/catch. On error, set `publicData` to `[]` and log: `AppLogger.warn('[LocationService] Public session query failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 })`. The method already handles `publicData ?? []` downstream at line 182, so empty array is safe.
- **Verify:** `npm run verify` passes. Confirm the new catch doesn't shadow the existing `privateData` try/catch at line 135.

### Step 4 — CrewRealtime.ts: Await floating `AsyncStorage.multiRemove`
**What:** Add `await` to the `AsyncStorage.multiRemove` call at line 48.
**Source:** `CrewRealtime.ts:47–51` — the call is inside a try/catch but NOT awaited, making it a floating promise. The catch at line 49 will never fire because `multiRemove` returns a Promise that is never awaited.
**How:** Change line 48 from `AsyncStorage.multiRemove(...)` to `await AsyncStorage.multiRemove(...)`. The enclosing `session_ended` handler callback on line 40 is not async, so we must also make the handler body an async IIFE or convert the `.on()` callback to async. Safest pattern: wrap lines 42–52 in a self-invoking async arrow `(async () => { ... })().catch(...)` to avoid changing the Supabase `.on()` signature.
- **Verify:** `npm run verify` passes. Confirm `multiRemove` is now `await`-ed. Confirm the `.on('broadcast', ...)` callback signature is not broken.

### Step 5 — CrewService.ts: Add `.catch()` to `removeChannel`
**What:** Add `.catch()` to the `supabase.removeChannel` call at line 92.
**Source:** `CrewService.ts:90–95` — `_ensureUnsubscribed` calls `supabase.removeChannel(this.channel)` as a fire-and-forget. If `removeChannel` rejects (network error, stale channel), it becomes an unhandled rejection.
**How:** Append `.catch((err: unknown) => AppLogger.warn('[CrewService] removeChannel failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }))` to line 92. Note: `removeChannel` returns a `Promise` per the Supabase JS v2 API.
- **Verify:** `npm run verify` passes. Confirm the `.catch()` is chained directly on the `removeChannel` call.

### Step 6 — CrewSessionManager.ts: Add `.catch()` to delete chain
**What:** Add `.catch()` to the floating `supabase.from('crew_members').delete()` chain at lines 360–364.
**Source:** `CrewSessionManager.ts:360–364` — the `.then()` fires on success but there is no `.catch()` for failure. A Supabase delete failure becomes an unhandled rejection.
**How:** Append `.catch((err: unknown) => AppLogger.warn('[CrewService] crew_members delete failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }))` after the `.then()` at line 364.
- **Verify:** `npm run verify` passes. Confirm the chain is `supabase...delete()...then(...)...catch(...)`.

### Step 7 — CrewSessionManager.ts: Guard `removeChannel` in setTimeout
**What:** Add try/catch and `.catch()` around the `supabase.removeChannel` call at line 373 inside the `setTimeout` callback.
**Source:** `CrewSessionManager.ts:372–375` — the `setTimeout` callback fires `supabase.removeChannel(channelRef)` with no error handling. If it rejects, the promise is unhandled inside a timer callback (no caller to propagate to).
**How:** Wrap the `setTimeout` body (lines 373–374) in a try/catch. Catch: `AppLogger.warn('[CrewService] deferred removeChannel failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 })`. Additionally chain `.catch()` on `removeChannel` itself since the method is async.
- **Verify:** `npm run verify` passes. Confirm both try/catch AND `.catch()` are present (belt-and-suspenders for timer callbacks).

## Verification Plan
1. `npm run verify` — TSC + Jest + AST + TypeSafety + WorkflowValidator must all pass.
2. Manual grep for `unhandled` or naked `await` without try/catch in the 7 target files — zero results expected.
3. Confirm every new catch block includes `AppLogger.warn` with `payload_size` and `ssi` metadata fields (except Step 2 which uses `console.warn` to prevent re-entrancy).
4. `git diff HEAD` — confirm diff touches ONLY the 5 files listed above and only the lines specified.

## Out of Scope
- **Notification service changes** — LOW severity, separate task.
- **BluetoothGuard changes** — LOW severity, separate task.
- **App.tsx changes** — LOW severity, separate task.
- **Re-entrancy guards (CLUSTER 6)** — separate task `fix/reentrant-handler-guards`.
- **PII scrubbing (CLUSTER 7)** — separate task `fix/pii-scrub-sweep`.
- **Any `as any` casts found in these files** — separate task `fix/ble-core-type-safety`. Boy Scout exemption: collision risk in multi-concern files.

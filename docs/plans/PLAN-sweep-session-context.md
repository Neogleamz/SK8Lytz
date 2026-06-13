# Implementation Plan

## Task: sweep-session-context
**Slug:** sweep-session-context
**Wave:** [WAVE:4] — Prerequisite: Wave 3 fully merged
**Size:** [Meal] — 6 files
**Risk:** [H-RISK] — SessionContext is the app's session state authority; re-entrancy bugs here corrupt session data
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_SESSION_TRACKING_findings.json`
**Prerequisite:** Wave 3 fully merged

## Goal
Fix 20 findings in the session tracking and context layer. Critical: 3 `flushSyncQueue` functions (`SpeedTrackingService`, `ScenesService`, `GradientsService`) have no re-entrancy guard — concurrent callers (e.g. a timer + a foreground trigger) will read the same queue, double-upload, and then write an empty queue, silently deleting pending sync data. Add `isFlushInProgress` guards to all three. Fix 3 unawaited AsyncStorage calls in `SessionContext`. Register all undocumented `@sk8lytz_*` AsyncStorage keys into the central registry. Replace hardcoded timeout delays with named constants.

## Decision Log
- **Re-entrancy on `flushSyncQueue` (CONFIRMED — R-26 HIGH on all 3 services)**: The flush reads the local queue, POSTs to Supabase, then clears the queue. If two concurrent callers enter simultaneously, both read the same queue items, both POST duplicates, and then both clear the queue (even items the other didn't process). A simple `isFlushInProgress` boolean ref solves this.
- **Unawaited AsyncStorage in `SessionContext` (R-11 MEDIUM × 3)**: `AsyncStorage.getItem` at L51, `AsyncStorage.removeItem` at L301, and the entire `recover()` function at L226 are all called without try/catch or `.catch()`. A storage failure silently drops session state.
- **Hardcoded `notifee.onForegroundEvent` re-subscription (R-12 MEDIUM)**: At L367, the listener is re-subscribed inside a `useEffect` that depends on `endSession`, `pauseSession` — these are recreated every render, causing the listener to mount/unmount continuously. Must wrap callbacks in `useCallback` first.
- **Undocumented keys (R-24)**: `@sk8lytz_session_phase`, `@sk8lytz_session_pause_time`, `@sk8lytz_telemetry_buffer`, `@SK8Lytz_DeviceState_v2_` prefix — none are in the Master Reference key registry. Register all.

## Files to Create/Modify

### [MODIFY] src/services/SpeedTrackingService.ts
- Add `isFlushInProgress` boolean flag to `flushPendingSessionQueue` at L243 — check at entry, release in `finally`
- Wrap AsyncStorage write at L268 in try/catch (R-11)

### [MODIFY] src/services/ScenesService.ts
- Add `isFlushInProgress` boolean flag to `flushSyncQueue` at L258 — check at entry, release in `finally`

### [MODIFY] src/services/GradientsService.ts
- Add `isFlushInProgress` boolean flag to `flushSyncQueue` at L161 — check at entry, release in `finally`
- Wrap AsyncStorage write at L186 in try/catch (R-11)
- Wrap AsyncStorage read at L83 in try/catch (R-11)

### [MODIFY] src/context/SessionContext.tsx
- L51: Add try/catch to `AsyncStorage.getItem(STORAGE_AUTO_PAUSE_ENABLED)` (R-11)
- L226: Wrap entire `recover()` function body in try/catch (R-11)
- L301: Wrap `AsyncStorage.removeItem('@sk8lytz_session_active')` in try/catch (R-11)
- L367: Wrap `endSession` and `pauseSession` passed to `notifee.onForegroundEvent` in `useCallback` to prevent continuous re-subscription (R-12)
- L212: Replace hardcoded `10000` (10s auto-pause) with named constant `SESSION_AUTO_PAUSE_DELAY_MS` (R-16)
- L341: Replace hardcoded `10000` (10s watch sync delay) with named constant `SESSION_WATCH_SYNC_DELAY_MS` (R-16)
- L32: Register `@sk8lytz_session_phase`, `@sk8lytz_session_pause_time`, `@sk8lytz_session_active` in `STORAGE_KEYS` and import (R-24)

### [MODIFY] src/hooks/useTelemetryLedger.ts
- L7: Register `@sk8lytz_telemetry_buffer` in `STORAGE_KEYS` and import (R-24)
- L168: Fix `AppLogger.warn` — add `payload_size` and `ssi` context (R-04)

### [MODIFY] src/hooks/useDeviceStateLedger.ts
- L27: Register `@SK8Lytz_DeviceState_v2_` prefix in `STORAGE_KEYS` and import (R-24)
- L140: Replace raw MAC in persistence warning with `'[REDACTED]'` (R-09)
- L199: Replace raw MAC in ledger clear warning with `'[REDACTED]'` (R-09)
- L95, L140, L199: Fix `AppLogger.warn` — add `payload_size` and `ssi` context (R-04)

## Out of Scope
- `SessionContext.tsx` full XState migration (that is `refactor/session-context-xstate` on the board)
- No UI component changes

## Verification Plan
- `npm run verify` — TSC + Jest must pass (especially `SessionMachine.test.ts`)
- Verify `grep 'isFlushInProgress' src/services/SpeedTrackingService.ts src/services/ScenesService.ts src/services/GradientsService.ts` returns matches in all 3
- Verify `grep '@sk8lytz_session\|@sk8lytz_telemetry\|@SK8Lytz_Device' src/` shows only registry imports

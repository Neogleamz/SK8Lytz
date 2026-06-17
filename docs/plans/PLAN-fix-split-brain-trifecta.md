# Implementation Plan: fix/split-brain-trifecta

> **Risk:** H-RISK | **Size:** Feast | **Rule:** R-21
> **Source Analysis:** [system_audit_report.md — CLUSTER 2](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md)

## Goal

Resolve 3 confirmed split-brain architectural defects where disjoint read/write paths cause silent state divergence:

- **(A)** `useDashboardGroups` derives groups from `registeredDevices` string props, bypassing `GroupRepository.getGroups()` — making GroupRepository a write-only sink.
- **(B)** `useControllerDispatch` hardcodes `ZenggeProtocol.setMultiColor` in fallback branches instead of routing through the adapter registry — non-Zengge hardware is silently ignored.
- **(C)** `useCrewSession.executeEndSession` issues raw Supabase queries to `user_profiles.lifetime_distance_miles` — competing with `SpeedTrackingService.saveSession` for the same row.

## Source Analysis Link

- Audit CLUSTER 2: [system_audit_report.md:69-91](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md)

## Files to Create/Modify

| # | File | Action |
|---|------|--------|
| 1 | `src/hooks/useDashboardGroups.ts` | Replace group derivation `useEffect` (lines 114–138) to read from `GroupRepository.getGroups()` |
| 2 | `src/services/GroupRepository.ts` | Add `subscribe()` method + ensure `getGroups()` stays canonical read path |
| 3 | `src/hooks/useControllerDispatch.ts` | Remove all `ZenggeProtocol.*` fallback branches; require adapter via `getAdapterForDevice` |
| 4 | `src/services/SpeedTrackingService.ts` | Extract inline lifetime-stats update into public `updateLifetimeStats(userId, distanceMiles, topSpeedMph)` method |
| 5 | `src/hooks/useCrewSession.ts` | Replace raw Supabase telemetry block (lines 74–95) with call to `SpeedTrackingService.updateLifetimeStats()` |

## Steps

### Step 1 — Split-Brain A: GroupRepository Read Path

**1.1** Add a `subscribe(listener)` / `notifyGroupSubscribers()` mechanism to `GroupRepository.ts`.
- Source: `GroupRepository.ts` already has `notifySubscribers()` on its delegate ([GroupRepository.ts:106](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts#L106)), but no direct group-subscriber list for React hooks.
- Add a `private groupListeners: Set<() => void>` + `subscribeGroups(cb): () => void` + call listeners from `setGroups`, `deleteGroup`, `saveGroupTransactional`.

**1.2** Refactor `useDashboardGroups.ts` group derivation `useEffect` (lines 114–138).
- Source: Current derivation loops `registeredDevices.forEach` to build `groupMap` from string props ([useDashboardGroups.ts:116-138](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts#L116)).
- Replace with: initialize from `GroupRepository.getInstance().getGroups()` on mount, subscribe to `GroupRepository.subscribeGroups()` for live updates.
- Keep Pass 2 (hardware config merge, lines 140–193) unchanged — it reads from `registeredDevices` which is correct for device-level config.

**Verify:** `setCustomGroups` is never called with data derived from `registeredDevices` string props. All group reads flow through `GroupRepository.getGroups()`. `GroupRepository.subscribeGroups` fires on every group mutation.

---

### Step 2 — Split-Brain B: Protocol Dispatch Hardcode Removal

**2.1** In `useControllerDispatch.ts`, the `getAdapterForDevice` param is already optional ([useControllerDispatch.ts:39](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts#L39)). Make it **required** (non-optional) in the `UseControllerDispatchParams` interface.

**2.2** Remove all `else` fallback branches that call `ZenggeProtocol.*` directly:
- `sendColor` line 101-102: `ZenggeProtocol.setMultiColor(...)` fallback
- `applyStaticModePattern` lines 216-218, 226-228: `ZenggeProtocol.setCustomModeCompact(...)` fallbacks
- `applyEmergencyPattern` line 286: `ZenggeProtocol.setMultiColor(...)` fallback
- `handleMusicChange` lines 343-351: `ZenggeProtocol.setMusicConfig(...)` fallback
- `setPower` line 374: `ZenggeProtocol.turnOn/turnOff()` fallback
- `setMultiColor` line 396: `ZenggeProtocol.setMultiColor(...)` fallback

Replace each `else` branch with an `AppLogger.error` + early return. The adapter is guaranteed to exist when `getAdapterForDevice` is required.

**2.3** Remove the now-unused `import { ZenggeProtocol }` from line 17 (Boy Scout).

**2.4** Verify all callers of `useControllerDispatch` already pass `getAdapterForDevice`. Source: The hook receives it as a prop from DockedController which gets it from BLEContext ([useControllerDispatch.ts:39](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts#L39)).

**Verify:** `findstr /s "ZenggeProtocol" src\hooks\useControllerDispatch.ts` returns zero matches. All dispatch routes through `adapter.build*()`. Callers that don't supply `getAdapterForDevice` get a TypeScript compilation error (caught by `npm run verify`).

---

### Step 3 — Split-Brain C: Crew Stats Race Condition

**3.1** Extract the duplicated lifetime-stats update logic from `SpeedTrackingService.saveSession` (lines 232–252) into a new public method:
```typescript
async updateLifetimeStats(userId: string, addedDistanceMiles: number, sessionTopSpeedMph: number): Promise<void>
```
Source: The identical pattern exists in `saveSession` ([SpeedTrackingService.ts:232-252](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts#L232)) and `flushPendingSessionQueue` ([SpeedTrackingService.ts:357-380](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts#L357)).

**3.2** Refactor `saveSession` (lines 232–252) and `flushPendingSessionQueue` (lines 357–380) to call `this.updateLifetimeStats()` instead of inline Supabase queries.

**3.3** In `useCrewSession.ts`, replace the raw Supabase block (lines 74–95) with:
```typescript
await SpeedTrackingService.updateLifetimeStats(
  user.id,
  crewService.sessionTelemetry.distanceMiles,
  crewService.sessionTelemetry.topSpeedMph
);
```
Source: The existing TODO at [useCrewSession.ts:72-73](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewSession.ts#L72) explicitly calls for this consolidation.

**3.4** Remove the `import { supabase }` from `useCrewSession.ts` line 4 if no other usages remain (Boy Scout).

**Verify:** `findstr "supabase" src\hooks\useCrewSession.ts` returns zero matches. All lifetime stats writes route through `SpeedTrackingService.updateLifetimeStats()`. The TODO comment at line 72 is resolved and removed.

---

## Verification Plan

1. **TypeScript compilation**: `npm run verify` passes with zero errors.
2. **No ZenggeProtocol in dispatch**: `findstr /s "ZenggeProtocol" src\hooks\useControllerDispatch.ts` → 0 matches.
3. **No raw Supabase in crew hook**: `findstr "supabase" src\hooks\useCrewSession.ts` → 0 matches.
4. **No group derivation from registeredDevices**: `useDashboardGroups.ts` group `useEffect` reads `GroupRepository.getGroups()`, not `registeredDevices.forEach → groupMap`.
5. **Single write path for lifetime stats**: `findstr /s "lifetime_distance_miles" src\hooks\useCrewSession.ts` → 0 matches.
6. **Post-edit diff**: `git diff HEAD` after every file edit — no unrelated changes.

## Out of Scope

- AsyncStorage migration or schema changes
- New protocol adapters (only removing hardcoded Zengge fallbacks)
- UI component changes (DashboardScreen, DockedController JSX)
- Test files
- `useProtocolDispatch.ts` changes (API surface is already correct — [useProtocolDispatch.ts:36](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProtocolDispatch.ts#L36) uses `getAdapterForDevice` correctly)
- Refactoring the `Promise.all` concurrent write pattern (covered by CLUSTER 1 / `fix/group-concurrent-write`)
- `flushPendingSessionQueue` inline stats block refactor is IN scope (same pattern, same method extraction)

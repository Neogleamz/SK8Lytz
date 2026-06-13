# Implementation Plan

## fix/session-machine-actor-types

### Goal
Harden the session XState engine's 4 actor services and 2 machine behaviors that were flagged in the post-merge audit:
1. `syncWatchStopped` 10-second deferred fire creates a race condition with rapid new-session starts
2. All 4 `fromCallback` actors use `fromCallback<any, InputType>` — suppresses type checking on `sendBack` events
3. `NotificationService` shows PAUSE+END action buttons during ENDING phase (should show no actions)

### Source of Truth
- `src/services/session/SessionMachine.ts` — `syncWatchStopped` action L127–L131
- `src/services/session/AutoPauseService.ts` — `fromCallback<any, ...>` L9
- `src/services/session/SensorService.ts` — `fromCallback<any, ...>` L18
- `src/services/session/HealthService.ts` — `fromCallback<any, ...>` L12
- `src/services/session/NotificationService.ts` — `fromCallback<any, ...>` L18; ENDING branch L42–L71
- `src/services/session/SessionMachine.types.ts` — defines `SessionMachineEvent` (the correct type for sendBack)

---

## Files to Create/Modify

### [MODIFY] `src/services/session/SessionMachine.ts`
**Step 1 — Remove `syncWatchStopped` setTimeout delay**
- Locate the `syncWatchStopped` action at L127–L131
- Current: `setTimeout(() => WatchBridge.syncSessionState({status:'STOPPED',...}), 10000)`
- Change to: call `WatchBridge.syncSessionState({status:'STOPPED',...})` synchronously
- Verify: `git diff HEAD src/services/session/SessionMachine.ts` shows only the setTimeout removal

### [MODIFY] `src/services/session/AutoPauseService.ts`
**Step 2 — Type sendBack event parameter**
- Line 9: `fromCallback<any, AutoPauseServiceInput>` → `fromCallback<SessionMachineEvent, AutoPauseServiceInput>`
- Import `SessionMachineEvent` from `'./SessionMachine.types'`
- Verify: TSC emits no errors for this file

### [MODIFY] `src/services/session/SensorService.ts`
**Step 3 — Type sendBack event parameter**
- Line 18: `fromCallback<any, SensorServiceInput>` → `fromCallback<SessionMachineEvent, SensorServiceInput>`
- Import `SessionMachineEvent` from `'./SessionMachine.types'`
- Verify: TSC emits no errors

### [MODIFY] `src/services/session/HealthService.ts`
**Step 4 — Type sendBack event parameter**
- Line 12: `fromCallback<any, HealthServiceInput>` → `fromCallback<SessionMachineEvent, HealthServiceInput>`
- Import `SessionMachineEvent` from `'./SessionMachine.types'`
- Verify: TSC emits no errors

### [MODIFY] `src/services/session/NotificationService.ts`
**Step 5 — Type sendBack + fix ENDING action buttons**
- Line 18: `fromCallback<any, NotificationServiceInput>` → `fromCallback<SessionMachineEvent, NotificationServiceInput>`
- Import `SessionMachineEvent` from `'./SessionMachine.types'`
- In the `buildActions` function (or wherever ENDING is detected, L42–L71):
  - ADD: `if (isEnding) return [];` — return empty actions array when phase is ENDING
  - Position: before the isPaused branch so ENDING is handled first
- Verify: TSC clean; ENDING phase returns empty actions array

---

## Verification Plan

### Automated
```
npm run verify
```
TSC must be clean (0 errors). Jest 28/28 suites must pass.

### Manual Verification
- All 4 actor files compile without `any`-type suppression errors
- `SessionMachine.ts` `syncWatchStopped` action contains no `setTimeout`
- `NotificationService.ts` has explicit ENDING branch returning `[]` for actions

### Out of Scope
- No changes to `SessionContext.tsx`
- No changes to `SessionBridge.ts`
- No changes to `SessionCommitService.ts` (already fixed in hotfix worktree)
- No test file changes in this plan

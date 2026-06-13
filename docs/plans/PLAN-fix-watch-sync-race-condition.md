# Implementation Plan: Fix Watch Sync Race Condition

## Objective
Prevent the watch from being permanently desynced if the Bluetooth link is congested exactly when the session ends.

## Source of Truth
- `src/services/session/SessionMachine.ts#L128`
- `src/services/session/SessionMachine.ts#L289-L290`

## Steps
1. **Implement ACK Timeout for Watch Sync**
   - In `SessionMachine.ts`, modify the `ENDING` state. Instead of immediately transitioning to `IDLE` after `sessionCommitService` finishes, implement a new intermediate service/promise that calls `WatchBridge.syncSessionState({ status: 'STOPPED' })`.
   - Wrap the call in a Promise race with a 3000ms timeout.
   - Alternatively, since `syncWatchStopped` is just an action, convert the teardown to wait on the WatchBridge promise, or retry once if the promise rejects.
   - If modifying `SessionMachine.ts` state structure is too invasive, update `sessionCommitService` (which is already a Promise) to await the `WatchBridge.syncSessionState` with a short timeout *before* it resolves.
2. **Verify**
   - Run `npm run verify`.
   - Assert that `ENDING` state cleanly transitions to `IDLE` even if WatchBridge throws.

## Files to Create/Modify
- `src/services/session/SessionMachine.ts` or `src/services/session/SessionCommitService.ts`

## Out of Scope
- Rewriting the WatchBridge native code.

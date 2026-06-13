# Implementation Plan: ble-t1-machine-tests

## Goal
Write unit tests for `BleMachine.ts` covering all state transitions and — critically — the organic disconnect → RECOVERY_START wiring that was a silent HIGH-severity bug (fixed in commit 2276ac8a).

## Source Audit Finding
- **Audit:** `tools/BLE_AUDIT_2/01_bleMachine.md` — full state machine map documented
- **Bug history:** `tools/SESSION_LOG.md` [DECISION] 2026-06-10T08:38 — organic disconnect was a logging-only no-op. No test existed to prevent this. Must not recur.

## Source of Truth
- `src/services/ble/BleMachine.ts`
- `src/services/ble/BleMachine.types.ts`
- `src/hooks/useBLE.ts` L182–187 (onOrganicDisconnect wiring)
- `tools/BLE_AUDIT_2/01_bleMachine.md`

## Test File
`src/services/ble/__tests__/BleMachine.test.ts`

## Test Cases (minimum — ship all of these)

### Group A — State Transitions
1. `IDLE → SCANNING` on `SCAN_START`
2. `IDLE → CONNECTING` on `CONNECT_REQUEST`
3. `IDLE → RECOVERING` on `RECOVERY_START`
4. `SCANNING → IDLE` on `SCAN_STOP`
5. `SCANNING → CONNECTING` on `CONNECT_REQUEST`
6. `CONNECTING → READY` on connectService `onDone`
7. `CONNECTING → IDLE` on connectService `onError`
8. `READY → RECOVERING` on `HEARTBEAT_FAIL`
9. `READY → DISCONNECTING` on `DISCONNECT_REQUEST`
10. `RECOVERING → READY` on `RECOVERY_COMPLETE`
11. `RECOVERING → IDLE` on `RECOVERY_FAIL`
12. `DISCONNECTING → IDLE` on `DISCONNECT_COMPLETE`
13. `ANY → IDLE` on `FORCE_IDLE` (does NOT clear connectedDevices)

### Group B — Organic Disconnect Regression Guard (P0)
14. **THE CRITICAL TEST:** Mock `onOrganicDisconnect` callback in machine input. Fire it. Assert `RECOVERY_START` event reaches the machine. Assert machine enters `RECOVERING`.
15. Organic disconnect during `DISCONNECTING` state: assert `RECOVERY_START` is suppressed (the guard in useBLE.ts).

### Group C — Context Assertions
16. `ghostedDeviceIds` populated correctly on `RECOVERY_START`
17. `connectedDevices` cleared only on `DISCONNECT_COMPLETE`, not on `FORCE_IDLE`
18. `targetMacs` set correctly on `CONNECT_REQUEST`

## Implementation Notes
- Use XState `createActor` with mocked service input
- Mock `connectService`, `recoveryService`, `heartbeatService` as `fromPromise(() => Promise.resolve({ devices: [] }))` stubs
- Use `actor.send(event)` + `actor.getSnapshot().value` assertions
- Test `onOrganicDisconnect` by calling it directly from the mock input and asserting snapshot

## Verification
`npm run verify` — all 116+ tests must pass

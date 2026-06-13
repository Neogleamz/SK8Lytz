# Implementation Plan: ble-t4-recovery-service-tests

## Goal
Write unit tests for `RecoveryService.ts` covering Phase 1/2/3 backoff, clearWriteQueue call, RECOVERY_COMPLETE/FAIL events, and GATT notification re-registration after successful reconnect.

## Source Audit Finding
- **Audit:** `tools/BLE_AUDIT_2/03_recoveryService.md` — full behavioral spec with exact backoff formulas
- **Gap:** `ble_test_gap_analysis.md` — "RecoveryService.ts — NONE"
- **Key finding from audit:** "RecoveryService does NOT clear the write queue" — fixed in 2276ac8a. Test must guard this.

## Source of Truth
- `src/services/ble/RecoveryService.ts` (230 lines)
- `tools/BLE_AUDIT_2/03_recoveryService.md`

## Test File
`src/services/ble/__tests__/RecoveryService.test.ts`

## Test Cases

### Group A — Write Queue Clear (regression guard for fix in 2276ac8a)
1. `clearWriteQueue()` called immediately when `run()` starts, before first GATT attempt

### Group B — RECOVERY_COMPLETE Path
2. First GATT attempt succeeds → `sendBack({ type: 'RECOVERY_COMPLETE', devices: [conn] })`
3. Adapter re-mapped in `adapterMapRef` after reconnect
4. `monitorCharacteristicForService` re-registered after reconnect
5. `onOrganicDisconnect` wired into new disconnect subscription (regression guard)
6. MTU re-negotiated and stored in `mtuMapRef`

### Group C — RECOVERY_FAIL Path
7. Empty `ghostedDeviceIds` → immediate `sendBack({ type: 'RECOVERY_FAIL' })`
8. All Phase 1+2 attempts exhausted (mock `createGattSession` to always throw) → `RECOVERY_FAIL`
9. Phase 3: sweeper never returns device → `RECOVERY_FAIL` after poll exhaustion

### Group D — Phase 3 Happy Path
10. Phase 1+2 fail → Phase 3 entered. `getSweepedDevice` returns device on poll 3 → `RECOVERY_COMPLETE`
11. Phase 3: `createGattSession` fails → immediately sends `RECOVERY_FAIL` (no further retries)

### Group E — Cancellation
12. `cancel()` called during Phase 1 backoff delay → service exits cleanly, no events sent
13. `cancel()` called during Phase 3 poll → service exits cleanly

## Mock Strategy
- Mock `clearWriteQueue` from `BleWriteQueue` — use `jest.mock('../BleWriteQueue')` and assert it was called
- Mock `createGattSession` with `jest.fn()` — control success/failure per test
- Mock `bleManager.onDeviceDisconnected` to capture the callback
- Use `jest.useFakeTimers()` to fast-forward backoff delays without actual waiting
- Invoke the `fromCallback` function directly: `recoveryService.create({ input, sendBack })` and call the returned `cancel` for cleanup tests

## Verification
`npm run verify` — all tests pass

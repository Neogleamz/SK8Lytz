# Implementation Plan: ble-t6-interrogator-service-tests

## Goal
Write unit tests for `InterrogatorService.ts` covering interrogateDevice probe lifecycle, createProbeQueue FTUE vs standard delay, loadHWCacheFromStorage parse error handling, and cancelDeviceConnection in finally.

## Source Audit Finding
- **Audit:** `tools/BLE_AUDIT_2/06_interrogatorService.md` — full behavioral spec with exact FTUE logic
- **Gap:** `ble_test_gap_analysis.md` — "InterrogatorService.ts — NONE"

## Source of Truth
- `src/services/ble/InterrogatorService.ts`
- `tools/BLE_AUDIT_2/06_interrogatorService.md`

## Test File
`src/services/ble/__tests__/InterrogatorService.test.ts`

## Test Cases

### Group A — interrogateDevice Happy Path
1. Connects, sends 0x63 HW query via `enqueueWrite`, receives notification → `onDeviceInterrogated` called with parsed data
2. `cancelDeviceConnection` called in `finally` block (success path)
3. MAC removed from `probingMacsRef` in `finally` block

### Group B — interrogateDevice Error Path
4. `createGattSession` throws → `cancelDeviceConnection` still called in `finally`
5. `enqueueWrite` (0x63 write) throws → `cancelDeviceConnection` still called, probe exits gracefully
6. Notification timeout (no response) → `cancelDeviceConnection` called, probe exits

### Group C — FTUE vs Standard Delay
7. `getRegisteredMacsCount()` returns 0 → probe queue delay is 500ms (FTUE)
8. `getRegisteredMacsCount()` returns 3 → probe queue delay is 2000ms (standard)

### Group D — loadHWCacheFromStorage
9. Valid JSON in AsyncStorage → parsed and returned correctly
10. Malformed JSON for one MAC → that entry skipped, other MACs loaded successfully (no crash)
11. AsyncStorage throws → error caught, empty cache returned

### Group E — Duplicate Guard
12. MAC already in `probingMacsRef` → `interrogateDevice` returns early, no GATT connection attempted

## Mock Strategy
- Mock `AsyncStorage` from `@react-native-async-storage/async-storage`
- Mock `createGattSession` to return mock connection + adapter
- Mock `enqueueWrite` from `BleWriteQueue`
- Mock `bleManager.cancelDeviceConnection`
- Use `jest.useFakeTimers()` for queue delay assertions
- Import and call `interrogateDevice`, `createProbeQueue`, `loadHWCacheFromStorage` directly

## Verification
`npm run verify` — all tests pass

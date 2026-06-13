# Implementation Plan: ble-t5-heartbeat-service-tests

## Goal
Write unit tests for `HeartbeatService.ts` covering 45s tick, 0x63 opcode ping via enqueueWrite, HEARTBEAT_FAIL event on GATT error, RSSI fallback for non-Zengge devices, and cleanup on exit.

## Source Audit Finding
- **Audit:** `tools/BLE_AUDIT_2/04_heartbeatService.md` — full behavioral spec including exact opcode bytes
- **Gap:** `ble_test_gap_analysis.md` — "HeartbeatService.ts — NONE"

## Source of Truth
- `src/services/ble/HeartbeatService.ts`
- `tools/BLE_AUDIT_2/04_heartbeatService.md`

## Test File
`src/services/ble/__tests__/HeartbeatService.test.ts`

## Test Cases

### Group A — Tick Interval
1. `setInterval` called with 45,000ms interval
2. Heartbeat does NOT fire immediately — waits for first interval

### Group B — Zengge Device Happy Path
3. Zengge adapter present → `enqueueWrite('normal', ...)` called with 0x63 opcode payload
4. Exact bytes verified: inner payload `[0x63, 0x12, 0x21, 0x0F, 0xA5]` wrapped in V2 transport

### Group C — Fallback (BanlanX / Unknown)
5. No adapter in `adapterMap` → `bleManager.readRSSIForDevice(mac)` called directly (NOT via enqueueWrite)

### Group D — HEARTBEAT_FAIL
6. `enqueueWrite` throws GATT error → `sendBack({ type: 'HEARTBEAT_FAIL', deviceId: mac })`
7. `bleManager.cancelDeviceConnection(mac)` called after GATT error before sending HEARTBEAT_FAIL
8. `readRSSIForDevice` throws → `sendBack({ type: 'HEARTBEAT_FAIL', deviceId: mac })`

### Group E — Multi-device
9. Two connected devices → heartbeat sent to both sequentially in same tick
10. First device GATT fails → HEARTBEAT_FAIL for that device, second device still receives heartbeat

### Group F — Cleanup
11. Returned cleanup function calls `clearInterval` — timer does not fire after cleanup
12. No `sendBack` called after cleanup

## Mock Strategy
- `jest.useFakeTimers()` to control the 45s interval
- Mock `enqueueWrite` from `BleWriteQueue`
- Mock `bleManager.readRSSIForDevice`, `bleManager.cancelDeviceConnection`
- Mock `adapterMap` as a `Map` with/without entries per test case
- Invoke `heartbeatService` as `fromCallback` directly with mocked input and `sendBack`
- Call `jest.advanceTimersByTime(45_000)` to trigger ticks

## Verification
`npm run verify` — all tests pass

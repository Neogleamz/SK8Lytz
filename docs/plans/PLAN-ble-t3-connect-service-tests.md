# Implementation Plan: ble-t3-connect-service-tests

## Goal
Write unit tests for `ConnectService.ts` covering group connect, GATT retry, stale device flush, MTU negotiation, adapter mapping, and GATT notification registration.

## Source Audit Finding
- **Audit:** `tools/BLE_AUDIT_2/02_connectService.md` — full Q&A behavioral spec documented
- **Gap:** `ble_test_gap_analysis.md` — "ConnectService.ts — NONE"

## Source of Truth
- `src/services/ble/ConnectService.ts` (288 lines)
- `tools/BLE_AUDIT_2/02_connectService.md` (exact behavioral spec)

## Test File
`src/services/ble/__tests__/ConnectService.test.ts`

## Test Cases

### Group A — Happy Path
1. Single device connect → returns `{ devices: [device] }`
2. Group connect (2 MACs) → sequential, returns both devices
3. Already-connected device cache hit → skips connectToDevice, returns from connectedDevicesRef

### Group B — GATT Retry (GATT 133)
4. GATT 133 on attempt 1 → retries with `refreshGatt: 'OnConnected'` on attempt 2, succeeds
5. GATT 133 on all 3 attempts → device omitted from result, other devices still connect
6. All devices fail → throws `'all_connections_failed'`

### Group C — Stale Device Flush
7. `connectedDevicesRef` has device NOT in `targetMacs` → `cancelDeviceConnection` called, `disconnectListeners` cleaned up
8. Stale flush settle delay respected

### Group D — MTU Negotiation (Android)
9. MTU negotiation succeeds → stored in `mtuMapRef`
10. MTU glitch (returns 23) → retries, stores 186 on second glitch
11. MTU request throws → stores 186 fallback

### Group E — Adapter + Notification
12. `adapterMapRef.current.set(conn.id, adapter)` called after handshake
13. `onDeviceDisconnected` subscription registered
14. `onOrganicDisconnect` called when disconnect fires (THE REGRESSION GUARD)
15. `monitorCharacteristicForService` called with correct UUIDs

### Group F — Hardware Blacklist
16. MAC in `blacklistedMacsRef` → throws `'hardware_blacklist'`, Alert shown

### Group G — Abort Signal
17. `signal.aborted` before connect → throws `'connect_aborted'`
18. `signal.aborted` during handshake → throws `'connect_aborted'`

## Mock Strategy
- Mock `bleManager` with `jest.fn()` stubs for all methods
- Mock `createGattSession` to return `{ conn, adapter }` stubs
- Mock `onDeviceDisconnected` to capture the callback and fire it in tests
- Use `fromPromise` actor resolver directly (call the async function with mocked input)

## Verification
`npm run verify` — all tests pass

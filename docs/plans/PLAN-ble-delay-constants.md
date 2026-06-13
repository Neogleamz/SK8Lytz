# Implementation Plan — `refactor/ble-delay-constants`

## Goal
Extract all hardcoded millisecond delay values in the BLE pipeline into named constants in a `BLE_TIMING_CONSTANTS` file, making timing tunable without hunting magic numbers across 6 files.

## Source of Truth
- `artifacts/deepdive_raw/R-16_findings.json` — full setTimeout inventory
- `artifacts/system_audit_report.md` §R-16
- `tools/ZENGGE_PROTOCOL_BIBLE.md` — inter-packet delay specifications

## Out of Scope
- Changing any delay values (only naming them)
- Touching UI debounce timers (LocationPicker, VerticalPatternDrum — already scrubbed as acceptable)
- Test files

---

## Step 1 — Create `src/constants/bleTimingConstants.ts`
- **Action:** Create file with named exports for all confirmed BLE pipeline delays:
```typescript
export const BLE_TIMING = {
  GATT_SETTLE_MS: 600,        // BleConnectionManager - post-connect settle
  WRITE_LAND_MS: 400,         // Sk8LytzProgrammer - write settle
  INTER_PACKET_GAP_MS: 50,    // BleWriteDispatcher - between packets
  SESSION_CONNECT_DELAY_MS: 100, // BleConnectionManager - pre-connect
  RECONNECT_BACKOFF_INITIAL_MS: 600, // useBLEAutoRecovery - Phase 2 backoff
  KEEPALIVE_INTERVAL_MS: 250, // BleLifecycleManager - keepalive ping
  MTU_SETTLE_MS: 200,         // BleConnectionManager - post-MTU settle
} as const;
```
- **Verify:** File compiles. All keys documented with inline comments citing the consumer file.

## Step 2 — Update `src/services/BleConnectionManager.ts`
- **Lines:** 108 (100ms), 175 (variable), 214/216 (200ms), 259 (interPacketDelayMs)
- **Action:** Import `BLE_TIMING` and replace magic numbers with named constants where values match. Leave parameterized delays (e.g. `interPacketDelayMs` from config) as-is.
- **Verify:** `grep "setTimeout.*[0-9][0-9][0-9]" src/services/BleConnectionManager.ts` returns zero hardcoded ms values.

## Step 3 — Update `src/services/BleWriteDispatcher.ts`
- **Lines:** 153 (50ms), 248 (50ms), 250 (8ms), 252 (50ms), 354 (interPacketDelayMs), 363 (50ms)
- **Action:** Replace literal 50ms values with `BLE_TIMING.INTER_PACKET_GAP_MS`. Leave `interPacketDelayMs` from preparedResult config as-is.
- **Verify:** No magic ms numbers remain in BleWriteDispatcher's setTimeout calls.

## Step 4 — Update `src/services/BleSessionFactory.ts`
- **Action:** Replace hardcoded delay with named constant
- **Verify:** Confirmed.

## Step 5 — Update `src/hooks/ble/useBLEAutoRecovery.ts`
- **Lines:** 264 (backoff variable — keep), 366 (600ms), 416 (PHASE_3_POLL_INTERVAL_MS — already named, keep)
- **Action:** Replace the 600ms hardcoded value with `BLE_TIMING.RECONNECT_BACKOFF_INITIAL_MS`
- **Verify:** Only named constants or named variables remain in this file's setTimeout calls.

## Step 6 — Update `src/services/BleLifecycleManager.ts`
- **Line:** 45 (250ms)
- **Action:** Replace with `BLE_TIMING.KEEPALIVE_INTERVAL_MS`
- **Verify:** Confirmed.

## Step 7 — Update `src/services/BlePingService.ts`
- **Action:** Replace hardcoded ms values with named constants
- **Verify:** No magic ms numbers in ping service timeouts.

## Step 8 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors.

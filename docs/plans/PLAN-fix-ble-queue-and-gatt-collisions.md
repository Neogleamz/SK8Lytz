# Implementation Plan: Fix BLE Queue and GATT Collisions

## Goal
Resolve critical GATT collisions and queue bypassing issues identified in the QA audit across BLE Core protocols.

## Source of Truth
📖 [system_audit_report.md](../../C:/Users/Magma/.gemini/antigravity/brain/79cf6856-67a1-49d0-aadc-9079eee6c7ae/system_audit_report.md)

## Identified Issues
1. **[R-01] Queue Enforcement:** `useBLEAutoRecovery.ts` bypasses `BleWriteQueue` for recovery pings.
2. **[R-13] GATT Collisions:** `Promise.all` used for `handshakeDevice` in `BleConnectionManager.ts` and `executeWriteChunked` in `BleWriteDispatcher.ts`.
3. **[R-03] Missing Jitter:** Hardcoded reconnect backoffs in `useDashboardAutoConnect.ts` cause herd storms.
4. **[R-10] Sequential Group Writes:** `BleWriteDispatcher.ts` loops with 50ms delays instead of concurrent queues.
5. **[R-16] Hardcoded Delays:** `setTimeout` used globally instead of `BleWriteQueue` delay handling.

## Proposed Changes
- **`src/hooks/ble/useBLEAutoRecovery.ts`**: Replace raw `writeCharacteristic` with `enqueueWrite`.
- **`src/services/BleConnectionManager.ts`**: Refactor `Promise.all` into sequential `for...of` loops during device discovery.
- **`src/services/BleWriteDispatcher.ts`**: Migrate chunked writes into a controlled concurrent mapped structure governed by a token bucket or queue lock.
- **`src/hooks/useDashboardAutoConnect.ts`**: Add `Math.random() * 500` jitter to backoff calculations.

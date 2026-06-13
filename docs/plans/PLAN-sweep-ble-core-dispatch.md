# Implementation Plan

## Task: sweep-ble-core-dispatch
**Slug:** sweep-ble-core-dispatch
**Wave:** [WAVE:2] — Prerequisite: Wave 1 fully merged
**Size:** [Feast] — 11 files
**Risk:** [H-RISK] — Core BLE write pipeline; protocol correctness
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_BLE_CORE_findings.json` + `docs/ZENGGE_PROTOCOL_BIBLE.md`
**Prerequisite:** Wave 1 fully merged (confirm via `git log --oneline -5` on master)

## Goal
Fix 42 findings in the BLE core and write dispatcher. Critical priorities: (1) Replace `Promise.all` concurrent writes in `BleWriteDispatcher` with properly serialized sequential dispatch — this is a **protocol correctness bug** that can corrupt controller state. (2) Add re-entrancy guards to `processQueue` in `InterrogatorService` and the battery sweep async callback. (3) PII-scrub all 9 raw MAC address leaks to telemetry logs. (4) Add `.catch()` to all unawaited `AsyncStorage` calls.

## Decision Log
- **`Promise.all` on BLE writes (CONFIRMED — 2 agents, R-13 HIGH)**: The Zengge controller has a single GATT characteristic. Concurrent parallel writes cause GATT collision errors and undefined controller state. The Protocol Bible mandates strictly sequential write delivery. `Promise.all` at L164 and L228 of `BleWriteDispatcher.ts` must be replaced with sequential `for...of` + `await` loops.
- **Re-entrancy on `processQueue` (R-26 HIGH)**: Without an `isProcessing` boolean lock, rapid successive calls to `processQueue` in `InterrogatorService` will process the same queue item twice, sending duplicate interrogation packets to the controller.
- **MAC address PII (R-09 MEDIUM — 9 locations)**: Raw MAC addresses logged to Supabase telemetry are PII. All must be replaced with `'[REDACTED]'` or a hashed/truncated form.
- **`useControllerDispatch` forEach (R-10 HIGH)**: Group BLE dispatch uses `.forEach` synchronously — must be converted to a `for...of` loop with `await` to serialize delivery across multiple connected devices.

## Files to Create/Modify

### [MODIFY] src/services/BleWriteDispatcher.ts
- L164: Replace `Promise.all(targets.map(t => writeChar(t)))` with sequential `for...of` + `await`
- L228: Replace `Promise.all(chunks.map(c => writeChar(c)))` with sequential `for...of` + `await`
- L333: Replace hardcoded `50` delay magic number with named constant `BLE_INTER_CHUNK_DELAY_MS`
- L122: Replace raw `targetDeviceId` / `device.id` in AppLogger warn with `'[REDACTED]'`

### [MODIFY] src/services/ble/InterrogatorService.ts
- L174: Add `isProcessing` boolean ref guard to `processQueue` — check at entry, release in `finally`
- L164: Add cleanup callback to `createProbeQueue` return value to clear scheduled intervals on teardown
- L46: Replace raw MAC address in AppLogger.warn with `'[REDACTED]'`
- L181: Replace hardcoded `500` stagger delay with named constant `BLE_INTERROGATION_STAGGER_MS`

### [MODIFY] src/services/ble/ConnectService.ts
- L65: Replace raw MAC list in AppLogger.warn with `'[REDACTED]'`
- L229: Replace raw MAC in AppLogger.log with `'[REDACTED]'`
- L112: Wrap `AsyncStorage.getItem` in try/catch block

### [MODIFY] src/services/ble/HeartbeatService.ts
- L60: Replace raw MAC in AppLogger.warn with `'[REDACTED]'`

### [MODIFY] src/services/ble/RecoveryService.ts
- L84: Replace raw MAC in AppLogger warning with `'[REDACTED]'`
- L160: Replace raw MAC in AppLogger warning with `'[REDACTED]'`

### [MODIFY] src/services/ble/RSSIService.ts
- L37: Replace raw MAC in RSSI poll logs with `'[REDACTED]'`

### [MODIFY] src/hooks/useBLE.ts
- L306: Replace raw MAC in disconnect warning with `'[REDACTED]'`
- L396: Replace raw MAC in warning log with `'[REDACTED]'`
- L217: Add `.catch()` to `AsyncStorage.getItem(STORAGE_DEMO_MODE)`

### [MODIFY] src/hooks/useControllerDispatch.ts
- L90: Convert `.forEach(device => { ... })` to `for...of` + `await` for sequential group BLE dispatch (R-10)

### [MODIFY] src/hooks/ble/useBLEScanner.ts
- L230: Replace raw MAC in scan rejection logs with `'[REDACTED]'`
- L77: Add `.catch()` to `AsyncStorage.getItem(STORAGE_APP_SETTINGS)`
- L325: Track mock sandbox timers in a ref; clear on unmount

### [MODIFY] src/hooks/ble/useBLEBatterySweep.ts
- L76: Add guard in `.then` callback to check if sweeper was stopped before proceeding with next queued item

### [MODIFY] src/hooks/useOptimisticBLE.ts
- L108: Track confirmation/reconciliation UI reset timeouts in a ref; clear on unmount

## Out of Scope
- `BleWriteDispatcher` chunking logic changes (those belong in `sweep-protocol-core`, Wave 1)
- `BleMachine.ts` state machine logic
- No UI component changes

## Verification Plan
- `npm run verify` — TSC + Jest must pass
- BLE simulator test must continue to pass with sequential write order
- Manual verify: `grep -r "device\.id\|deviceId" src/services/ble/ src/hooks/` should show no raw MAC in AppLogger calls

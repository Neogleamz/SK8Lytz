# AUDIT: BLE Hardcoded Delays

**Date:** 2026-06-10
**Task:** `chore/hardcoded-delay-audit`
**Auditor:** ⚒️ Dev — Sage

## Overview
This audit explicitly documents the `setTimeout` instances identified across the BLE domain. The objective was to migrate physical BLE GATT staggered write delays into the priority `BleWriteQueue` to prevent queue-jumping and race conditions, while preserving UI and network logic debouncers that are intentionally time-based.

## Migrated Delays (Now using `BleWriteQueue.enqueueDelay`)
These delays were refactored because they pace strictly ordered BLE writes. Using raw `setTimeout` inside a queue executor caused the queue to resolve early, allowing interleaving writes to corrupt the strict timing sequence. 

1. **ConnectService Handshake Gap**
   - **Location:** `src/services/ble/ConnectService.ts`
   - **Before:** `await new Promise(res => setTimeout(res, handshake.interPacketDelayMs))`
   - **After:** `await enqueueDelay('critical', handshake.interPacketDelayMs)`
   - **Reason:** Ensures the queue fully blocks any normal/bulk writes during the 200ms power-on to time-sync gap required by Zengge hardware.

2. **BlePingService HW Query Setup Delay (400ms)**
   - **Location:** `src/services/BlePingService.ts`
   - **Before:** `setTimeout(..., 400)`
   - **After:** `enqueueDelay('critical', 400)` followed by synchronous execution mapping to the queue.
   - **Reason:** Physical separation between GATT notification subscription and the HW query dispatch.

3. **BlePingService RF Query Stagger (200ms)**
   - **Location:** `src/services/BlePingService.ts`
   - **Before:** Nested `setTimeout(..., 200)`
   - **After:** `enqueueDelay('critical', 200)` followed by synchronous execution mapping to the queue.
   - **Reason:** The BLE hardware drops the RF query if it receives it while still parsing the HW query.

4. **InterrogatorService HW Query Setup Delay (400ms)**
   - **Location:** `src/services/ble/InterrogatorService.ts`
   - **Before:** `setTimeout(..., 400)`
   - **After:** `enqueueDelay('normal', 400)`
   - **Reason:** Same logic as Ping Service but mapped to 'normal' priority for background interrogation.

5. **InterrogatorService RF Query Stagger (200ms)**
   - **Location:** `src/services/ble/InterrogatorService.ts`
   - **Before:** Nested `setTimeout(..., 200)`
   - **After:** `enqueueDelay('normal', 200)`
   - **Reason:** Prevents controller saturation during background interrogation.

## Intentionally Preserved `setTimeout` Instances
These remain as `setTimeout` because they govern React Component lifecycles, network debouncing, connection recovery state machines, or UI presentation — NOT serial GATT writes.

1. **Connection Backoff Jitter**
   - **Location:** `src/services/ble/ConnectService.ts` and `RecoveryService.ts`
   - **Role:** Backoff intervals (`jitteredDelay`) during GATT 133 connection retries. Since no GATT session exists yet, `BleWriteQueue` is not applicable.

2. **Probe Timeout Watches (3500ms)**
   - **Location:** `src/services/BlePingService.ts` and `src/services/ble/InterrogatorService.ts`
   - **Role:** Fallback watchdog timers that resolve the Promise to `null` if the hardware fails to respond to a query.

3. **Dashboard Auto-Connect Debouncers**
   - **Location:** `src/hooks/useDashboardAutoConnect.ts`
   - **Role:** Prevents the React hook from triggering a connection flood when Bluetooth state rapidly toggles or dependencies update.

4. **Crew Broadcast Debouncers**
   - **Location:** `src/services/CrewService.ts`
   - **Role:** Batches rapid real-time presence updates (e.g. 10 users dragging color wheels) into a single Supabase broadcast every few hundred milliseconds to prevent WebSocket rate-limiting.

5. **Visual Blink Hold Duration**
   - **Location:** `src/services/BlePingService.ts`
   - **Role:** Pauses JS execution for X milliseconds so the user can physically see the hardware blink before the script powers it down.

## Conclusion
The `BleWriteQueue` now possesses the capability to natively inject delays via `enqueueDelay`. All payload-separating timeouts have been flattened and ported, ensuring robust queue backpressure during complex initialization sequences.

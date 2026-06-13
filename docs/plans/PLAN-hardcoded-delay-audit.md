# Implementation Plan: Hardcoded Delay Audit & Migration

This plan addresses the `chore/hardcoded-delay-audit` task from Cluster G. The goal is to document the 8 `setTimeout` delays found in the BLE services and migrate the BLE-specific payload delays into the `BleWriteQueue` serialization system.

## Proposed Changes

### 1. Introduce `enqueueDelay` to `BleWriteQueue`

#### [MODIFY] [src/services/BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
Add an exported `enqueueDelay` function that allows us to inject explicit delays directly into the priority FIFO queue. This ensures that when a delay is needed between two writes (like the 200ms between time-sync and power-on), the queue actually pauses and blocks interleaving writes from jumping ahead.

### 2. Refactor `ConnectService.ts` Handshake Delay

#### [MODIFY] [src/services/ble/ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- Replace the raw `await new Promise(res => setTimeout(res, handshake.interPacketDelayMs))` with `await enqueueDelay('critical', handshake.interPacketDelayMs)`.
- This guarantees no other critical writes can be processed during the handshake delay window.

### 3. Flatten Callbacks in `BlePingService.ts` & `InterrogatorService.ts`

#### [MODIFY] [src/services/BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- Remove the nested `setTimeout` callbacks (400ms and 200ms delays) used for staggering the HW and RF queries.
- Replace them with synchronous calls to `enqueueDelay('critical', ...)` followed by `enqueueWrite('critical', ...)`. Because the queue guarantees strictly ordered sequential execution, the writes will physically execute with the correct delays without needing ugly JS callback nesting.

#### [MODIFY] [src/services/ble/InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- Apply the exact same flattening using `enqueueDelay('normal', ...)` and `enqueueWrite('normal', ...)` for the background interrogator sequence.

### 4. Documentation

#### [NEW] [docs/audits/AUDIT-hardcoded-delays.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/audits/AUDIT-hardcoded-delays.md)
- Create a markdown file explicitly documenting the 8 MEDIUM `setTimeout` delays.
- Outline which ones were migrated to `BleWriteQueue` (the GATT write stagger delays).
- Outline which ones remain as `setTimeout` intentionally (e.g., Connection Backoff Jitter, Dashboard Auto-Connect Debouncers, and Crew Broadcast Debouncers) because they govern UI or network logic rather than BLE GATT physical write separation.

## Verification Plan
- Run `npm run verify` to ensure TypeScript compilation passes.
- Run `npm run test -- BleMachine.test.ts` and `RecoveryService.test.ts` to ensure the timing modifications don't break the mocked BLE timelines.

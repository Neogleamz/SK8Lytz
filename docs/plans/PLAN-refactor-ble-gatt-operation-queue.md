# Implementation Plan: refactor/ble-gatt-operation-queue

## Problem
[BleWriteQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts) (232 lines) is a well-designed priority FIFO queue, but it only serializes **write** operations. Heartbeat pings (HeartbeatService), recovery GATT handshakes (RecoveryService), and user color writes all go through it — but **reads** (e.g., `readCharacteristicForService`) and **descriptor operations** bypass the queue entirely.

Android's BLE stack is **single-threaded for GATT**. Concurrent operations cause silent failures (GATT error 133/8). Nordic's gold standard: serialize ALL GATT operations through a single FIFO.

Source: [BleWriteQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts#L1-L21) — header says "Priority FIFO Write Queue" but Android needs ALL ops queued.

## Files to Create/Modify

#### [MODIFY] [BleWriteQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts) → rename to BleOperationQueue.ts
- Generalize `enqueueWrite` to `enqueueOperation` (keep `enqueueWrite` as alias for backward compat)
- Add `enqueueRead` for read operations with the same priority/drain logic
- Add `OperationType: 'write' | 'read' | 'descriptor' | 'delay'` to QueueEntry for logging
- Keep all existing write logic intact — this is additive

#### [NEW] [BleOperationQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleOperationQueue.ts)
- If rename is too risky (many imports), create new file that re-exports BleWriteQueue + adds read/descriptor wrappers

#### [MODIFY] [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- Any raw `readCharacteristicForService` calls → route through the queue

#### [MODIFY] [HeartbeatService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)
- Verify heartbeat pings already go through BleWriteQueue (they likely do)

#### [MODIFY] [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- Verify recovery handshakes go through queue

#### [MODIFY] All consumers if renaming: BleMachine.types.ts, BlePingService.ts, BleWriteDispatcher.ts, DockedController.tsx, useBLE.ts

## Steps

### Step 1: Generalize BleWriteQueue
- Add `enqueueOperation(type, priority, execute, generation)` as the universal entry point
- Keep `enqueueWrite` as a convenience wrapper
- Verify: TSC clean, all existing tests pass

### Step 2: Add enqueueRead
- Same drain logic as writes — priority + generation + retry
- Verify: Unit test for read serialization

### Step 3: Route ConnectService reads through queue
- Find any direct `readCharacteristicForService` calls
- Wrap in `enqueueRead('critical', ...)`
- Verify: git diff shows only read calls wrapped

### Step 4: Verify all services use queue
- Grep for direct GATT calls that bypass the queue
- Verify: No raw `.writeCharacteristic` or `.readCharacteristic` outside the queue

## Out of Scope
- Background mode
- UI components
- Scan filter
- Connection parameters

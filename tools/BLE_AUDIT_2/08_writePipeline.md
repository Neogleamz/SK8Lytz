# BLE Audit Report: Write Pipeline Analysis

This report documents the architectural design, priority queueing, backpressure mechanics, and error recovery behavior of the SK8Lytz BLE write pipeline, based on a code review of [BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts) and [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts).

---

## 1. 3 Priority Tiers and Opcode Mapping

The write queue defines three priority tiers (`critical`, `normal`, and `bulk`) to serialize operations. Opcodes are mapped to these tiers as follows:

*   **`critical`**: Used for command payloads that require immediate delivery (e.g., state toggles or control queries).
    *   `0xCC`: Power ON/OFF.
    *   `0x71`: Advanced Power payload.
    *   `0x63`: Hardware query / recovery ping.
*   **`bulk`**: Used for large or segmented data sets where throughput is maximized but pacing is non-critical.
    *   `0x51`: Scene Builder payloads.
    *   `0x40`: Chunked frame segments.
*   **`normal`**: The default fallback category.
    *   Includes opcodes such as `0x56` (static color), `0x59` (multi-color/colorful pattern), and `0x61` (built-in effect mode).

Opcode classification is performed by `resolveWritePriority(opcodeByte: number)` inside `BleWriteQueue.ts`.

---

## 2. MAX_QUEUE_DEPTH and Drop Strategy

To prevent memory bloat and write lag during high-frequency interactions (e.g., color wheel dragging), the queue restricts outstanding operations:

*   **`MAX_QUEUE_DEPTH`**: Hardcoded to `8` in `BleWriteQueue.ts`.
*   **Drop Strategy**: Enforced inside `enqueueWrite` when the queue length reaches `MAX_QUEUE_DEPTH`:
    1.  **Bulk Drop**: The queue scans from the tail to find the last `bulk` priority entry. If found, it is spliced out and silently resolved (`resolve(true)`).
    2.  **Normal Drop**: If no bulk entry exists, it scans from the head to find the first `normal` priority entry. If found, it is spliced out and silently resolved (`resolve(true)`).
    3.  **Critical Drop**: If the queue is saturated entirely with critical writes, the *incoming* entry is dropped immediately (`resolve(true)`).
    4.  **Preservation**: Already queued `critical` writes are never dropped.

---

## 3. Sequential 1-at-a-time Execution Gate (`_drain()`)

To accommodate Android's hardware constraint (where concurrent GATT writes cause collisions and `status 133` disconnects), the queue executes writes serially:

*   **Mutex Gate**: `_drain()` utilizes a module-scoped boolean lock `_isRunning`. If `_isRunning` is true, subsequent calls return early.
*   **Sequential Loop**: Once locked, `_drain()` executes a `while (_queue.length > 0)` loop:
    ```typescript
    const entry = _queue.shift()!;
    // ... Stale pruning check ...
    try {
      const result = await entry.execute();
      entry.resolve(result);
    } catch (err: unknown) {
      // ... Error handling & retry ...
    }
    ```
*   **Release**: The lock is set back to false (`_isRunning = false`) only when the queue is completely empty. This guarantees that only one BLE write operation is active at any time.

---

## 4. Transient GATT Error Retries

The queue automatically handles common transient Android BLE stack issues to prevent disconnect cascades:

*   **Detected Errors**: Checked via `isTransientGattError` against message substrings: `"status 8"`, `"status 133"`, `"gatt_error"`, `"disconnected"`, `"not connected"`, and `"connection reset"`.
*   **Retry Protocol**:
    1.  On catching a transient error, it delays retry using a jittered delay:
        ```typescript
        const delay = jitteredDelay(100, 50); // 100ms base with ±50ms random jitter
        ```
        This random jitter prevents synchronized retry storms if multiple devices hit errors simultaneously.
    2.  It executes the callback a second time (`await entry.execute()`).
    3.  If the retry succeeds, the promise resolves. If the retry fails, it catches the second error and resolves the queue entry with `false` (capping retries at **exactly 1**).

---

## 5. `clearWriteQueue` Promise Resolution

When the write queue is cleared (e.g., upon device disconnect or recovery cancellations), pending entries are handled gracefully:

*   **Silent Resolve**: `clearWriteQueue()` iterates over all remaining queue entries and calls `entry.resolve(true)`.
*   **Rationale**: Resolving with `true` (indicating a no-op silent drop) rather than rejecting avoids unhandled promise rejections or cascading error-handling crashes in UI components and caller states.

---

## 6. Stale-Write Pruning via Generations

Rapid user gestures (e.g., continuous dragging of a color slider) generate a queue of updates. Generational pruning discards intermediate states:

*   **Generation Tracking**: Pattern/color writes increment a module-level variable `_currentGeneration`. The dispatcher calls `setWriteQueueGeneration(thisGeneration)` to update the queue's boundary.
*   **Pruning Condition**: When a write is dequeued inside `_drain()`, its generation is checked:
    ```typescript
    if (entry.generation !== 0 && entry.generation < _currentGeneration) {
      entry.resolve(true); // pruned silently
      continue;
    }
    ```
*   **Exception**: Critical writes use a generation tag of `0` which is explicitly ignored by the pruning check, ensuring they always execute.

---

## 7. Exclusive Queue Routing

All writes originating from the `BleWriteDispatcher` route through the `BleWriteQueue`:

1.  **Single Writes**: `executeWriteToDevice` routes normal and critical writes to `_executeWriteToDeviceInternal`, which returns the promise from `enqueueWrite(...)`.
2.  **Chunked Writes**: `executeWriteChunked` wraps the entire sequence of ZENGGE 0x40 chunked packets into a single `bulk` execution callback passed to `enqueueWrite(...)`.
3.  **Polymorphic Writes**: `executeProtocolResults` processes adapter results through `_executeProtocolResultsInternal`, which calls `enqueueWrite('normal', ...)` at the end.

This ensures unified backpressure management and serialization.

---

## 8. `resolveWritePriority` Usage

`resolveWritePriority` is invoked in `BleWriteDispatcher.ts` at line 187:

```typescript
const priority = capturedGeneration === 0
  ? resolveWritePriority(payload[0])   // critical path: use opcode classification
  : 'normal';                           // pattern write: always normal tier
```

*   **Critical Path**: If a write is NOT a pattern write (represented by `capturedGeneration === 0`), the dispatcher calls `resolveWritePriority(payload[0])` to classify the write priority based on the first byte of the payload.
*   **Pattern Path**: If it is a generation-tracked pattern write, it is automatically assigned `'normal'` priority to ensure it is governed by normal backpressure constraints.

---

## 9. Imports Tracking

### `BleWriteQueue.ts`
*   `AppLogger` from `./AppLogger`
*   `jitteredDelay` from `../utils/backoff`

### `BleWriteDispatcher.ts`
*   `Platform` from `'react-native'`
*   `Buffer` from `'buffer'`
*   `AppLogger` from `./AppLogger`
*   `resolveProtocolForDevice` from `../protocols/ControllerRegistry`
*   `IControllerProtocol`, `ProtocolResult` from `../protocols/IControllerProtocol`
*   `enqueueWrite`, `resolveWritePriority`, `setWriteQueueGeneration` from `./BleWriteQueue`
*   `BLE_TIMING` from `../constants/bleTimingConstants`
*   `ZenggeProtocol` from `../protocols/ZenggeProtocol`

---

## 10. Type Safety Audit: `any` Casts

A strict syntax scan was performed across both files:
*   **No `any` or `@ts-ignore` casts** were found. Both files fully comply with the project's strict type-safety standards (Fix 1: The No `any` Cast Law).
*   The only type cast in these files is a safe signature cast in `BleWriteDispatcher.ts` at line 365, converting a `Promise<boolean>` returning function to match `() => Promise<boolean | 'partial'>` for queue insertion:
    ```typescript
    executeWrite as () => Promise<boolean | 'partial'>
    ```

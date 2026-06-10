# BLE Write Pipeline Audit (BleWriteQueue.ts & BleWriteDispatcher.ts)

## 1. Opcodes and Priority Tiers
What are the 3 priority tiers and which opcodes map to each?

**Priority Tiers:**
- `critical`
- `normal`
- `bulk`

**Opcode Mapping (defined in `resolveWritePriority(opcodeByte)` in `BleWriteQueue.ts` L50-62):**
- **`critical`:** 
  - `0xCC` — Power ON/OFF
  - `0x71` — Advanced Power payload
  - `0x63` — Hardware config query / recovery ping
- **`bulk`:**
  - `0x51` — Scene Builder (large, chunked animations)
  - `0x40` — Chunked frame segment
- **`normal`:**
  - Default fallback for all other opcodes (e.g., `0x56` static color, `0x59` multi-color spatial patterns, `0x61` effect animations).

*Note:* In `BleWriteDispatcher.ts` (L213-215), when enqueuing write operations, if a pattern write is being debounced with a non-zero write generation (`capturedGeneration !== 0`), it is explicitly queued as `'normal'` tier rather than utilizing the opcode byte mapping. Chunked sequences in `executeWriteChunked` are always enqueued as `'bulk'` tier (L287).

---

## 2. Bounded Queue & Drop Strategy
What is MAX_QUEUE_DEPTH? What is the drop strategy when full?

- **`MAX_QUEUE_DEPTH`:** `8` (defined in `BleWriteQueue.ts` L36)
- **Drop Strategy (L108-133):**
  When a write is enqueued and the queue depth has reached/exceeded `MAX_QUEUE_DEPTH` (`_queue.length >= MAX_QUEUE_DEPTH`), it enforces backpressure:
  1. **Drop Bulk First:** It pops the last enqueued `'bulk'` priority entry from the tail of the queue. If found, it splices it out and silently resolves its promise with `true` to notify completion without blocking the caller.
  2. **Drop Normal Second:** If no bulk entries exist in the queue, it shifts the first enqueued `'normal'` priority entry from the head of the queue. If found, it splices it out and silently resolves its promise with `true`.
  3. **Drop Incoming (Critical Saturation):** If the queue is filled entirely with `'critical'` writes (and no bulk or normal entries are present to drop), the incoming new write operation is dropped immediately. Its promise is resolved with `true`, and a warning is logged: `"Queue saturated with critical writes — dropping incoming entry"`.
  *Note:* Critical entries already in the queue are never dropped.

---

## 3. Concurrency & Serialization
Does _drain() process one write at a time? (CRITICAL — Android BLE stack constraint)

**Yes.** `BleWriteQueue.ts` is explicitly designed to process exactly one write at a time to satisfy the Android BLE stack's single outstanding write constraint:
1. **Re-entrancy Guard (L157-158):** It uses a module-level lock `_isRunning`. If `_isRunning` is already `true`, `_drain()` aborts early. Otherwise, it sets `_isRunning = true`.
2. **Sequential Queue Draining (L160-161):** It processes entries one-by-one in a `while (_queue.length > 0)` loop, shifting the head item of the queue (`_queue.shift()!`).
3. **Awaiting Completion (L178):** It calls `await entry.execute()` (the async wrapper performing the physical BLE write). Because of the `await` statement, `_drain()` blocks and will not fetch the next entry in the queue until the active BLE write completes or fails.
4. **Unlocking (L199):** Once all queued entries have finished processing, `_isRunning` is reset to `false`.

---

## 4. Transient Error Retries
Does it retry on transient GATT errors? Which error strings trigger retry? How many retries?

- **Yes.** `BleWriteQueue` automatically handles retries on transient GATT errors.
- **Error Strings (L39-44):** Match is performed case-insensitively using `includes()` against the error message:
  - `'status 8'`
  - `'status 133'`
  - `'gatt_error'`
  - `'disconnected'`
  - `'not connected'`
  - `'connection reset'`
- **Number of Retries (L182-192):** It attempts exactly **1 retry** (a single retry) after a **100ms** delay.
  ```typescript
  if (isTransientGattError(err)) {
    AppLogger.warn('[BleWriteQueue] Transient GATT error — retrying in 100ms', { error: String(err) });
    await new Promise(r => setTimeout(r, 100));
    try {
      const retryResult = await entry.execute();
      entry.resolve(retryResult);
    } catch (retryErr: unknown) {
      const safeErr = retryErr instanceof Error ? retryErr : new Error(String(retryErr));
      AppLogger.warn('[BleWriteQueue] Retry failed', { error: String(retryErr) });
      entry.resolve(false);
    }
  }
  ```

---

## 5. Queue Clearing Behavior
Does clearWriteQueue() resolve all pending promises (not reject them)?

**Yes.**
`clearWriteQueue()` (L83-91) iterates over the queue and resolves all pending promises with `true` (making them silent no-ops for the callers) instead of rejecting them:
```typescript
export function clearWriteQueue(): void {
  const dropped = _queue.length;
  // Resolve all pending promises as true (no-op drops, not errors)
  _queue.forEach(entry => entry.resolve(true));
  _queue = [];
  if (dropped > 0) {
    AppLogger.log('BLE_WRITE_QUEUE', { event: 'queue_cleared', dropped });
  }
}
```

---

## 6. Stale-Write Pruning
Does setWriteQueueGeneration exist and is it used for stale-write pruning?

**Yes.**
- **`setWriteQueueGeneration` (L70-72):** Exists and updates a module-level variable `_currentGeneration`:
  ```typescript
  export function setWriteQueueGeneration(gen: number): void {
    _currentGeneration = gen;
  }
  ```
- **Stale-Write Pruning (L165-169):** Inside the `_drain()` loop, any entry with a non-zero generation that is older than `_currentGeneration` is skipped (pruned) and silently resolved:
  ```typescript
  // generation=0 means critical (never prune). Otherwise skip if superseded.
  if (entry.generation !== 0 && entry.generation < _currentGeneration) {
    AppLogger.log('BLE_WRITE_QUEUE', { event: 'stale_pruned', generation: entry.generation, current: _currentGeneration });
    entry.resolve(true);
    continue;
  }
  ```
- **Dispatcher Usage:** In `BleWriteDispatcher.ts`, `setWriteQueueGeneration(thisGeneration)` is called for non-low-priority pattern writes in `executeWriteToDevice` (L93) and `executeProtocolResults` (L332). This advances the module's generation counter and triggers pruning of outdated/superseded writes when the queue drains.

---

## 7. Write Dispatch Routing
Does BleWriteDispatcher.ts always route writes through enqueueWrite? Or does it ever call bleManager.writeCharacteristic... directly?

**Yes, it always routes writes through `enqueueWrite`.**
In `BleWriteDispatcher.ts`, there are zero instances where a BLE write is dispatched directly without `enqueueWrite`. Every write operation is wrapped in a callback function (such as `executeWrite`) and passed into `enqueueWrite`:
- `_executeWriteToDeviceInternal` -> L216: `return enqueueWrite(priority, executeWrite, capturedGeneration);`
- `executeWriteChunked` -> L287: `await enqueueWrite('bulk', async () => { ... });`
- `_executeProtocolResultsInternal` -> L421: `const result = await enqueueWrite('normal', executeWrite as () => Promise<boolean | 'partial'>, capturedGeneration);`

---

## 8. Opcode Classification
Does BleWriteDispatcher use resolveWritePriority to classify writes?

**Yes.**
In `BleWriteDispatcher.ts` (L213-215) inside `_executeWriteToDeviceInternal`:
```typescript
const priority = capturedGeneration === 0
  ? resolveWritePriority(payload[0])   // critical path: use opcode classification
  : 'normal';                           // pattern write: always normal tier
```
It utilizes `resolveWritePriority(payload[0])` to dynamically classify the priority tier by examining the first byte (opcode) of the payload, but only when `capturedGeneration` is `0` (the critical path). For pattern writes with a non-zero generation, the priority tier defaults to `'normal'`.

---

## 9. Write Queue Imports
List every file that imports from BleWriteQueue. Are any of them dead/deleted files?

The active files that contain imports from `BleWriteQueue` are:
1. `src/hooks/useBLE.ts`
2. `src/services/BlePingService.ts`
3. `src/services/BleWriteDispatcher.ts`
4. `src/services/ble/HeartbeatService.ts`
5. `src/services/ble/InterrogatorService.ts`
6. `src/services/ble/RecoveryService.ts`

**Are any of them dead/deleted files?**
**No.** All of these 6 files are active and exist in the current codebase under `src/`.
*Note:* A previously active connection file (`BleConnectionManager.ts`) was recently deleted (clean-up in `SESSION_LOG.md` on 2026-06-10), but it is no longer on the filesystem and is not returned by active imports.

---

## 10. `any` Casts
Are there any any casts in either file?

- **`BleWriteQueue.ts`:**
  **No.** There are absolutely no `any` casts (neither `as any` nor `: any`) in `BleWriteQueue.ts`. It strictly uses typed definitions and `unknown` for errors.
- **`BleWriteDispatcher.ts`:**
  **No `as any` or `<any>` type assertions (casts) exist.**
  However, the file does declare parameters and variables with loose types (`any` / `any[]`) to facilitate legacy compatibility with the device structures:
  - `bleManager: any` (L22, L139, L315)
  - `connectedDevices: any[]` (L23, L140, L225, L316, L373)
  - `adapterMap: Map<string, any>` (L26, L143, L228, L319, L376)

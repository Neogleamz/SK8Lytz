# BLE Write Pipeline Audit

**1. 3 priority tiers and opcode mapping?**
- Tiers: `critical`, `normal`, `bulk`.
- Opcodes:
  - **Critical**: `0xCC` (Power ON/OFF), `0x71` (Advanced Power), `0x63` (Hardware query/ping)
  - **Bulk**: `0x51` (Scene Builder chunked), `0x40` (Chunked frame segment)
  - **Normal**: All other opcodes (e.g., `0x56`, `0x59`, `0x61`).

**2. MAX_QUEUE_DEPTH and drop strategy?**
- `MAX_QUEUE_DEPTH` is `8`.
- Drop Strategy: When the queue reaches 8 items, it drops `bulk` entries from the tail first (LIFO). If no bulk entries exist, it drops `normal` entries from the front (FIFO). Dropped entries resolve silently as `true`. `critical` entries are never dropped. If the queue is entirely saturated with critical entries, the new incoming entry is rejected and resolves `true`.

**3. _drain() sequential 1-at-a-time gate?**
- `_drain()` enforces sequential 1-at-a-time execution using an `_isRunning` boolean flag. It loops `while (_queue.length > 0)`, shifting one entry at a time and `await`ing its execution before proceeding.

**4. Transient GATT error retries?**
- The system checks for transient error strings (`'status 8'`, `'status 133'`, `'gatt_error'`, `'disconnected'`, `'not connected'`, `'connection reset'`).
- For these errors, it applies a `jitteredDelay(100, 50)` (100ms ±50ms) to prevent retry storms, and retries exactly once. If the retry fails, it resolves `false`.

**5. clearWriteQueue promise resolution?**
- Iterates over all pending queue entries, resolving each as `true` (treated as a silent no-op drop, not an error), and empties the queue array.

**6. Stale-write pruning via generations?**
- Entries are enqueued with a `generation` tag. Inside `_drain()`, right before execution, if `entry.generation !== 0 && entry.generation < _currentGeneration`, the write is considered stale, pruned, and resolved as `true`. Generation `0` (used for critical writes) is bypassed and never pruned.

**7. enqueueWrite routing exclusively?**
- Yes, all device write operations in `BleWriteDispatcher.ts` (`executeWriteToDevice`, `executeWriteChunked`, `executeProtocolResults`) wrap their actual Bluetooth native calls and route them exclusively through `enqueueWrite`.

**8. resolveWritePriority usage?**
- Used in `BleWriteDispatcher.ts` by inspecting the first byte (`payload[0]`) to determine the queue priority. It's also used to determine if a write should be debounced (only `normal` tier writes are debounced).

**9. Imports tracking?**
- `BleWriteQueue.ts`: `AppLogger`, `jitteredDelay`.
- `BleWriteDispatcher.ts`: `Platform`, `Buffer`, `scrubPII`, `AppLogger`, `resolveProtocolForDevice`, `IControllerProtocol`, `ProtocolResult`, `enqueueWrite`, `resolveWritePriority`, `setWriteQueueGeneration`, `BLE_TIMING`, `ZenggeProtocol`.

**10. Any `any` casts?**
- No `any` casts or `@ts-ignore` in either file. Both use `unknown` for errors with proper `instanceof Error` type guarding. There is one specific function type assertion (`resolve as (result: boolean | 'partial') => void`) in the dispatcher, but strictly zero uses of the `any` type.

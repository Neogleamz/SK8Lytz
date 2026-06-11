/**
 * BleWriteQueue.ts — Priority FIFO Write Queue with Backpressure
 *
 * Replaces the unbounded promise-chain mutex in BleWriteDispatcher with a
 * bounded, priority-ordered FIFO queue. Prevents memory pressure and multi-
 * second write lag caused by rapid user input (e.g., color wheel drags).
 *
 * Design principles:
 *   - 3-tier priority: critical > normal > bulk
 *   - MAX_QUEUE_DEPTH=8: bounded backpressure, bulk entries dropped first
 *   - 1 outstanding write at a time (Android BLE stack hardware constraint)
 *   - 1 retry with 100ms delay for transient GATT errors (status 8, 133)
 *   - Stale write pruning: generation-tagged entries skipped if superseded
 *   - Module-level singleton: shared across all BleWriteDispatcher calls
 *     without React context overhead (same pattern as useBLEGattMutex.ts)
 *
 * Priority classification (by payload[0] opcode):
 *   critical — 0xCC (power), 0x63 (hw query), recovery pings
 *   normal   — 0x56 (static color), 0x59 (multi-color), 0x61 (effect)
 *   bulk     — 0x51 (scene builder chunks), 0x40 (chunked frame)
 */
import { AppLogger } from './AppLogger';
import { jitteredDelay } from '../utils/backoff';

export type WritePriority = 'critical' | 'normal' | 'bulk';

interface QueueEntry {
  priority: WritePriority;
  /** Optional generation tag — entry is skipped if currentGeneration has advanced */
  generation: number;
  execute: () => Promise<boolean | 'partial'>;
  resolve: (result: boolean | 'partial') => void;
  reject: (err: unknown) => void;
  enqueuedAt: number;
}

const MAX_QUEUE_DEPTH = 8;

/** Transient GATT error strings that warrant a single retry */
const TRANSIENT_GATT_ERRORS = ['status 8', 'status 133', 'gatt_error', 'disconnected', 'not connected', 'connection reset'];

function isTransientGattError(err: unknown): boolean {
  const msg = err instanceof Error ? err.message : String(err);
  return TRANSIENT_GATT_ERRORS.some(t => msg.toLowerCase().includes(t));
}

/**
 * Resolve the write priority for a given BLE opcode byte.
 * Used by the dispatcher to classify writes at enqueue time.
 */
export function resolveWritePriority(opcodeByte: number): WritePriority {
  switch (opcodeByte) {
    case 0xCC: // Power ON/OFF
    case 0x71: // Advanced Power payload
    case 0x63: // Hardware query / recovery ping
      return 'critical';
    case 0x51: // Scene Builder (large, chunked)
    case 0x40: // Chunked frame segment
      return 'bulk';
    default:
      return 'normal';
  }
}

// ── Module-level singleton state ──────────────────────────────────────────────
let _queue: QueueEntry[] = [];
let _isRunning = false;
let _currentGeneration = 0;

/** Update the current generation so stale queued writes are pruned on drain. */
export function setWriteQueueGeneration(gen: number): void {
  _currentGeneration = gen;
}

/** Returns the current number of pending entries in the queue. */
export function getQueueDepth(): number {
  return _queue.length;
}

/** Returns true if any operation is currently executing or pending in the queue. */
export const isWriteQueueActive = (): boolean => {
  return _isRunning || _queue.length > 0;
};

/**
 * Flush all pending writes. Call on disconnect or cancelAllRecoveries
 * to prevent stale writes firing after teardown.
 */
export function clearWriteQueue(): void {
  const dropped = _queue.length;
  // Resolve all pending promises as true (no-op drops, not errors)
  _queue.forEach(entry => entry.resolve(true));
  _queue = [];
  if (dropped > 0) {
    AppLogger.log('BLE_WRITE_QUEUE', { event: 'queue_cleared', dropped });
  }
}

/**
 * Enqueue a BLE write operation. Returns a Promise that resolves when
 * the write completes (or is pruned/dropped).
 *
 * @param priority   Write priority tier
 * @param execute    Async function performing the actual BLE write
 * @param generation Optional generation tag for stale-write pruning (0 = critical, never pruned)
 */
export function enqueueWrite(
  priority: WritePriority,
  execute: () => Promise<boolean | 'partial'>,
  generation: number = 0
): Promise<boolean | 'partial'> {
  return new Promise<boolean | 'partial'>((resolve, reject) => {
    // ── BACKPRESSURE: enforce MAX_QUEUE_DEPTH ─────────────────────────────
    if (_queue.length >= MAX_QUEUE_DEPTH) {
      // Drop strategy: remove bulk entries from the tail first, then normal.
      // Critical entries are NEVER dropped.
      const bulkIdx = _queue.map((e, i) => ({ e, i }))
        .filter(({ e }) => e.priority === 'bulk')
        .pop();
      if (bulkIdx !== undefined) {
        const [dropped] = _queue.splice(bulkIdx.i, 1);
        dropped.resolve(true); // silent drop
        AppLogger.log('BLE_WRITE_QUEUE', { event: 'backpressure_drop_bulk', depth: _queue.length });
      } else {
        const normalIdx = _queue.map((e, i) => ({ e, i }))
          .filter(({ e }) => e.priority === 'normal')
          .shift();
        if (normalIdx !== undefined) {
          const [dropped] = _queue.splice(normalIdx.i, 1);
          dropped.resolve(true);
          AppLogger.log('BLE_WRITE_QUEUE', { event: 'backpressure_drop_normal', depth: _queue.length });
        } else {
          // Queue is full of critical writes — reject the new entry
          AppLogger.warn('BLE_WRITE_QUEUE', 'Queue saturated with critical writes — dropping incoming entry');
          resolve(true);
          return;
        }
      }
    }

    // ── PRIORITY INSERT: maintain critical > normal > bulk ordering ────────
    const entry: QueueEntry = { priority, generation, execute, resolve, reject, enqueuedAt: Date.now() };
    if (priority === 'critical') {
      // Insert after the last critical entry (or at front if none)
      const lastCritIdx = _queue.map((e, i) => ({ e, i }))
        .filter(({ e }) => e.priority === 'critical')
        .pop();
      _queue.splice(lastCritIdx !== undefined ? lastCritIdx.i + 1 : 0, 0, entry);
    } else if (priority === 'normal') {
      // Insert after all critical entries, before any bulk
      const firstBulkIdx = _queue.findIndex(e => e.priority === 'bulk');
      _queue.splice(firstBulkIdx === -1 ? _queue.length : firstBulkIdx, 0, entry);
    } else {
      _queue.push(entry); // bulk goes to tail
    }

    AppLogger.log('BLE_WRITE_QUEUE', { event: 'enqueued', priority, depth: _queue.length });
    _drain();
  });
}

/**
 * Enqueue an explicit delay into the priority queue.
 * Useful for pacing sequential writes that require a physical gap (e.g., handshake, query staggered delays).
 */
export function enqueueDelay(
  priority: WritePriority,
  delayMs: number,
  generation: number = 0
): Promise<boolean | 'partial'> {
  return enqueueWrite(
    priority,
    () => new Promise(res => setTimeout(() => res(true), delayMs)),
    generation
  );
}

async function _drain(): Promise<void> {
  if (_isRunning) return;
  _isRunning = true;

  while (_queue.length > 0) {
    const entry = _queue.shift()!;

    // ── STALE WRITE PRUNING ───────────────────────────────────────────────
    // generation=0 means critical (never prune). Otherwise skip if superseded.
    if (entry.generation !== 0 && entry.generation < _currentGeneration) {
      AppLogger.log('BLE_WRITE_QUEUE', { event: 'stale_pruned', generation: entry.generation, current: _currentGeneration });
      entry.resolve(true);
      continue;
    }

    const queueLatencyMs = Date.now() - entry.enqueuedAt;
    if (queueLatencyMs > 2000) {
      AppLogger.warn('BLE_WRITE_QUEUE', `High queue latency: ${queueLatencyMs}ms for ${entry.priority} write`);
    }

    // ── EXECUTE WITH 1 TRANSIENT RETRY ────────────────────────────────────
    try {
      const result = await entry.execute();
      entry.resolve(result);
    } catch (err: unknown) {
      const safeErr = err instanceof Error ? err : new Error(String(err));
      if (isTransientGattError(err)) {
        const delay = jitteredDelay(100, 50);
        AppLogger.warn(`[BleWriteQueue] Transient GATT error — retrying in ${delay}ms`, { error: String(err) });
        await new Promise(r => setTimeout(r, delay));
        try {
          const retryResult = await entry.execute();
          entry.resolve(retryResult);
        } catch (retryErr: unknown) {
      const safeErr = retryErr instanceof Error ? retryErr : new Error(String(retryErr));
          AppLogger.warn('[BleWriteQueue] Retry failed', { error: String(retryErr) });
          entry.resolve(false);
        }
      } else {
        entry.resolve(false);
      }
    }
  }

  _isRunning = false;
}

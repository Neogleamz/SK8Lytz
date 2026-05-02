/**
 * useBLEGattMutex.ts — GATT Traffic Cop (Module-Level Async Mutex)
 *
 * Serializes all BLE GATT operations across the SK8Lytz engine to prevent
 * the Android "GATT 133" congestion error caused by concurrent connections.
 *
 * Priority Tiers:
 *   Priority 1 (USER)         — pingDevice, connectToDevices (manual user actions)
 *   Priority 2 (INTERROGATOR) — background EEPROM probes from useBLESweeper
 *   Priority 3 (SWEEPER)      — passive scan (no GATT connection, lowest priority)
 *
 * P1 Preemption:
 *   When P1 arrives while P2 is in-flight, P1 signals the P2 holder via an
 *   AbortController. P2's Interrogator checks the signal at each async boundary
 *   and cancels its in-flight GATT connection, immediately releasing the lock.
 *   P1 then acquires without waiting the full 3.5s probe timeout.
 *
 * Usage:
 *   const { release, signal } = await acquireGattLock(1);
 *   try {
 *     if (signal.aborted) return; // preemption check
 *     ... your GATT operation ...
 *   }
 *   finally { release(); }
 *
 * Design: Module-level (not a React hook) so the same lock instance is
 * shared across all hook instances without React context overhead.
 */

export type GattPriority = 1 | 2 | 3;

export interface GattLockHandle {
  /** Call in finally{} to release the lock */
  release(): void;
  /** AbortSignal — check signal.aborted at async boundaries to support P1 preemption */
  signal: AbortSignal;
}

/** How long a P2/P3 waiter will poll before giving up and skipping */
const LOCK_POLL_INTERVAL_MS = 100;
const LOCK_MAX_WAIT_MS = 8000; // 8s max wait for background ops

/**
 * Internal lock state — module-level singleton.
 */
let _lockChain: Promise<void> = Promise.resolve();
let _currentPriority: GattPriority = 3;
let _isLocked = false;

/**
 * The AbortController for the current P2 holder.
 * When P1 arrives, it calls _currentHolderAbort?.abort() to signal P2 to stop.
 */
let _currentHolderAbortController: AbortController | null = null;

/**
 * Acquire the GATT lock for a given priority operation.
 *
 * - P1 callers acquire immediately AND preempt any in-flight P2 via AbortController.
 * - P2 callers wait for any in-flight P1 operation to complete.
 * - P3 callers are advisory — they don't block, they check and bail.
 *
 * @param priority  1=User Action, 2=Background Probe, 3=Passive Sweep
 * @param timeoutMs Max wait time before giving up (P2/P3 only). Default: 8000ms.
 * @returns         A GattLockHandle { release, signal } or null if timed out (P2/P3).
 */
export async function acquireGattLock(
  priority: GattPriority,
  timeoutMs: number = LOCK_MAX_WAIT_MS
): Promise<GattLockHandle | null> {
  // P3 (Sweeper): never blocks, never waits — advisory check only
  if (priority === 3) {
    if (_isLocked && _currentPriority <= 2) return null; // yield to P1/P2
    // P3 doesn't actually lock — it's just a passive scan, no GATT
    return { release: () => {}, signal: new AbortController().signal };
  }

  const waitStart = Date.now();
  const ownAbortController = new AbortController();

  // P1: Preempt any running P2 immediately
  if (priority === 1 && _isLocked && _currentPriority === 2) {
    _currentHolderAbortController?.abort();
  }

  // P2: Poll until the lock is free (or P1 releases it) or timeout
  if (priority === 2) {
    while (_isLocked && _currentPriority <= 1) {
      if (Date.now() - waitStart > timeoutMs) {
        return null; // give up
      }
      await new Promise(r => setTimeout(r, LOCK_POLL_INTERVAL_MS));
    }
  }

  // Chain onto existing lock promise
  let release!: () => void;
  const prevChain = _lockChain;
  _lockChain = new Promise<void>(res => { release = res; });
  _isLocked = true;
  _currentPriority = priority;

  // Register the AbortController so P1 can preempt this holder later
  _currentHolderAbortController = priority === 2 ? ownAbortController : null;

  // Wait for previous operation to complete
  await prevChain.catch(() => {});

  return {
    signal: ownAbortController.signal,
    release: () => {
      _isLocked = false;
      _currentPriority = 3;
      _currentHolderAbortController = null;
      release();
    },
  };
}

/** Returns true if a P1 user action is currently holding the GATT lock */
export function isGattBusy(): boolean {
  return _isLocked;
}

/** Returns the priority of the current GATT lock holder */
export function currentGattPriority(): GattPriority {
  return _currentPriority;
}

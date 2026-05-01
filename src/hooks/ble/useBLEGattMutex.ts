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
 * Usage:
 *   const release = await acquireGattLock(1); // P1 = user action
 *   try { ... your GATT operation ... }
 *   finally { release(); }
 *
 * Design: Module-level (not a React hook) so the same lock instance is
 * shared across all hook instances without React context overhead.
 */

export type GattPriority = 1 | 2 | 3;

/** How long a P2/P3 waiter will poll before giving up and skipping */
const LOCK_POLL_INTERVAL_MS = 100;
const LOCK_MAX_WAIT_MS = 8000; // 8s max wait for background ops

/**
 * Internal lock state — module-level singleton.
 * Access is serialized via promise chaining so no race conditions are possible
 * even in concurrent async contexts.
 */
let _lockChain: Promise<void> = Promise.resolve();
let _currentPriority: GattPriority = 3;
let _isLocked = false;

/**
 * Acquire the GATT lock for a given priority operation.
 *
 * - P1 callers acquire immediately (they preempt everything).
 * - P2 callers wait for any in-flight P1 operation to complete.
 * - P3 callers are advisory — they don't block, they check and bail.
 *
 * @param priority  1=User Action, 2=Background Probe, 3=Passive Sweep
 * @param timeoutMs Max wait time before giving up (P2/P3 only). Default: 8000ms.
 * @returns         A `release()` function to call in `finally{}`.
 *                  Returns null if the lock could not be acquired (P2/P3 timeout).
 */
export async function acquireGattLock(
  priority: GattPriority,
  timeoutMs: number = LOCK_MAX_WAIT_MS
): Promise<(() => void) | null> {
  // P3 (Sweeper): never blocks, never waits — advisory check only
  if (priority === 3) {
    if (_isLocked && _currentPriority <= 2) return null; // yield to P1/P2
    // P3 doesn't actually lock — it's just a passive scan, no GATT
    return () => {}; // no-op release
  }

  // P1/P2: serialize via promise chaining
  let release!: () => void;

  const waitStart = Date.now();

  // For P2: poll until the lock is free or timeout
  if (priority === 2) {
    while (_isLocked && _currentPriority <= 1) {
      if (Date.now() - waitStart > timeoutMs) {
        return null; // give up — P1 is holding too long
      }
      await new Promise(r => setTimeout(r, LOCK_POLL_INTERVAL_MS));
    }
  }

  // Chain onto existing lock promise (P1 always wins by acquiring immediately)
  const prevChain = _lockChain;
  _lockChain = new Promise<void>(res => { release = res; });
  _isLocked = true;
  _currentPriority = priority;

  // Wait for previous operation to complete
  await prevChain.catch(() => {}); // swallow previous errors

  return () => {
    _isLocked = false;
    _currentPriority = 3;
    release();
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

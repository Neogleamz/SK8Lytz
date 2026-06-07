/**
 * useBLEGattMutex.ts — GATT Traffic Cop (Module-Level Async Mutex)
 *
 * Serializes all BLE GATT operations across the SK8Lytz engine to prevent
 * the Android "GATT 133" congestion error caused by concurrent connections.
 *
 * Priority Tiers:
 *   Priority 1 (USER)         — pingDevice, connectToDevices (manual user actions)
 *   Priority 2 (RECOVERY)     — auto-recovery reconnection (time-sensitive, user sees dimmed card)
 *   Priority 3 (INTERROGATOR) — background EEPROM probes from useBLESweeper (deferrable)
 *   Priority 4 (SWEEPER)      — passive scan (no GATT connection, lowest priority)
 *
 * Preemption Chain (P1 > P2 > P3 > P4):
 *   When a higher-priority caller arrives while a lower-priority holder is in-flight,
 *   the higher-priority caller signals the holder via AbortController. The holder
 *   checks signal.aborted at each async boundary and cancels its GATT operation.
 *   P1 preempts P2 and P3. P2 (recovery) preempts P3 (interrogation).
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
import { AppLogger } from '../../services/AppLogger';

export type GattPriority = 1 | 2 | 3 | 4;

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
let _currentPriority: GattPriority = 4;
let _isLocked = false;

/**
 * The AbortController for the current P2 or P3 holder.
 * When a higher-priority caller arrives, it calls abort() to signal the holder to stop.
 */
let _currentHolderAbortController: AbortController | null = null;

/**
 * Deadlock watchdog timer. If the lock is held for >15s, auto-release and log.
 * Protects against unhandled exceptions that skip finally{release()} and Hot Reload
 * orphaning the lock chain (module re-evaluates, old lock never resolves).
 */
const DEADLOCK_WATCHDOG_MS = 15_000;
let _deadlockWatchdog: ReturnType<typeof setTimeout> | null = null;

/**
 * Hot Reload generation counter. Increments every time the module re-evaluates.
 * Locks acquired by a previous generation are considered orphaned and released
 * immediately on the next acquire attempt, avoiding the 15s watchdog stall.
 */
let _generation = 0;
_generation++;

// Hot Reload cleanup: wrapped in a function so TypeScript's control-flow analysis
// cannot statically determine that _isLocked is always false here. Without the
// function wrapper, TSC treats the if-body as dead code (since _isLocked=false at init)
// and narrows all variables inside it to 'never', causing a type error on .abort().
function _hotReloadCleanup(): void {
  if (_isLocked) {
    AppLogger.warn('[Mutex] Hot Reload detected with lock held — force-releasing orphaned lock');
    _isLocked = false;
    _currentPriority = 4;
    if (_currentHolderAbortController) {
      _currentHolderAbortController.abort();
    }
    _currentHolderAbortController = null;
    if (_deadlockWatchdog) {
      clearTimeout(_deadlockWatchdog);
      _deadlockWatchdog = null;
    }
  }
}
_hotReloadCleanup();

/**
 * Acquire the GATT lock for a given priority operation.
 *
 * - P1 callers acquire immediately AND preempt any in-flight P2/P3 via AbortController.
 * - P2 (recovery) callers preempt P3 (interrogation), wait for P1 to release.
 * - P3 (interrogation) callers wait for P1/P2 to release, can be preempted by P1/P2.
 * - P4 (sweeper) callers are advisory — they don't block, they check and bail.
 *
 * @param priority  1=User Action, 2=Auto-Recovery, 3=Interrogation, 4=Passive Sweep
 * @param timeoutMs Max wait time before giving up (P2/P3 only). Default: 8000ms.
 * @returns         A GattLockHandle { release, signal } or null if timed out.
 */
export async function acquireGattLock(
  priority: GattPriority,
  timeoutMs: number = LOCK_MAX_WAIT_MS
): Promise<GattLockHandle | null> {
  // P4 (Sweeper): never blocks, never waits — advisory check only
  if (priority === 4) {
    if (_isLocked && _currentPriority <= 3) return null; // yield to P1/P2/P3
    // P4 doesn't actually lock — it's just a passive scan, no GATT
    return { release: () => {}, signal: new AbortController().signal };
  }

  const waitStart = Date.now();
  const ownAbortController = new AbortController();

  // P1: Preempt any running P2 (recovery) or P3 (interrogation) immediately
  if (priority === 1 && _isLocked && (_currentPriority === 2 || _currentPriority === 3)) {
    _currentHolderAbortController?.abort();
  }

  // P2 (Recovery): Preempt any running P3 (interrogation) immediately
  if (priority === 2 && _isLocked && _currentPriority === 3) {
    _currentHolderAbortController?.abort();
  }

  // P2 (Recovery): Poll until the lock is free (or P1 releases it) or timeout
  if (priority === 2) {
    while (_isLocked && _currentPriority <= 1) {
      if (Date.now() - waitStart > timeoutMs) {
        return null; // give up
      }
      await new Promise(r => setTimeout(r, LOCK_POLL_INTERVAL_MS));
    }
  }

  // P3 (Interrogation): Poll until P1/P2 releases or timeout
  if (priority === 3) {
    while (_isLocked && _currentPriority <= 2) {
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
  const acquiredGeneration = _generation;

  // Register the AbortController so P1 can preempt this holder later
  // P2 (recovery) and P3 (interrogation) can both be preempted — register their AbortController
  _currentHolderAbortController = (priority === 2 || priority === 3) ? ownAbortController : null;

  // Wait for previous operation to complete.
  // Race with a 200ms timeout: if the previous chain is orphaned (Hot Reload),
  // resolve quickly instead of waiting for the full 15s deadlock watchdog.
  try {
    await Promise.race([
      prevChain,
      new Promise<void>(r => setTimeout(r, 200)),
    ]);
  } catch (e: unknown) {
    AppLogger.warn('[Mutex] Previous operation in GATT lock chain rejected', e);
  }

  // If a Hot Reload happened between acquiring and waiting, skip the stale lock
  if (acquiredGeneration !== _generation) {
    AppLogger.warn('[Mutex] Stale lock detected after Hot Reload — releasing immediately');
    _isLocked = false;
    _currentPriority = 4;
    _currentHolderAbortController = null;
    release();
    return null;
  }

  const waitTime = Date.now() - waitStart;
  if (waitTime > 2000) {
    AppLogger.warn('GATT_LOCK_CONTENTION', { waitTime, priority });
  }

  // Start deadlock watchdog — if the lock is held for >15s, something is stuck.
  if (_deadlockWatchdog) clearTimeout(_deadlockWatchdog);
  _deadlockWatchdog = setTimeout(() => {
    AppLogger.error('GATT_DEADLOCK_DETECTED', {
      priority,
      heldForMs: DEADLOCK_WATCHDOG_MS,
      action: 'auto_release',
    });
    // Force-abort the current holder if it's listening
    _currentHolderAbortController?.abort();
    // Force-release the lock
    _isLocked = false;
    _currentPriority = 4;
    _currentHolderAbortController = null;
    _deadlockWatchdog = null;
    release();
  }, DEADLOCK_WATCHDOG_MS);

  return {
    signal: ownAbortController.signal,
    release: () => {
      if (_deadlockWatchdog) {
        clearTimeout(_deadlockWatchdog);
        _deadlockWatchdog = null;
      }
      _isLocked = false;
      _currentPriority = 4;
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
// Blast radius verification anchor

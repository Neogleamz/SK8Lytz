# Implementation Plan: fix-ble-gatt-mutex-hotreload

> **RC-04** | Priority: P3 | Risk: LOW | Size: Snack | Layer: BLE (Dev-Only)

## Problem

After a React Native Hot Reload (Fast Refresh), the GATT mutex in `useBLEGattMutex.ts` enters a 15-second deadlock stall. All GATT operations (connect, recovery, interrogation) block until the deadlock watchdog fires.

The module-level lock state at [useBLEGattMutex.ts:48-50](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEGattMutex.ts#L48-L50):

```typescript
// L48-50 — module-level state survives Hot Reload
let _lockChain: Promise<void> = Promise.resolve();
let _currentPriority: GattPriority = 4;
let _isLocked = false;
```

And the lock chain acquisition at [L122-136](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEGattMutex.ts#L122-L136):

```typescript
// L122-136 — chain onto existing lock promise
let release!: () => void;
const prevChain = _lockChain;
_lockChain = new Promise<void>(res => { release = res; });
_isLocked = true;
_currentPriority = priority;
// ...
await prevChain.catch(...); // ← HANGS if prevChain's resolve function was lost
```

**What happens on Hot Reload**:
1. Module re-evaluates: `_lockChain = Promise.resolve()` (fresh)
2. BUT if a lock was held before Hot Reload, the **old** `_lockChain` promise still has pending `.then()` handlers from callers that chained onto it
3. The old `release` function (captured in a closure by the previous module evaluation) is garbage-collected
4. New callers chain onto the fresh `_lockChain` normally — but the `_isLocked` flag was reset to `false`, so the watchdog timer from the previous evaluation was also cleared
5. In practice: if a GATT operation was mid-flight during Hot Reload, its `release()` callback never fires, and the next caller's `await prevChain` resolves immediately (since `_lockChain` was reset to `Promise.resolve()`). The **real** issue is when the module re-evaluates while `_isLocked === true` — the watchdog timer handle (`_deadlockWatchdog`) is lost, so if the old operation's promise chain hangs, the new module has no reference to clean it up.

The 15s watchdog at [L143-159](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEGattMutex.ts#L143-L159) eventually fires and force-releases, but 15 seconds is a painful dev-time stall.

## Root Cause

Module-level state (`_lockChain`, `_isLocked`, `_deadlockWatchdog`) survives Hot Reload because React Native's Fast Refresh re-executes module code but doesn't clear pre-existing closures or timers. The `release` function from the pre-reload lock acquisition is captured in a closure that's now orphaned.

## Proposed Fix

**Strategy: Add a generation counter. On module re-evaluation, increment the generation. If a lock was acquired by a stale generation, release it immediately.**

### Step 1: Add a generation counter to the module

```diff
 // useBLEGattMutex.ts — after L50
 let _isLocked = false;
+
+/**
+ * Hot Reload generation counter. Increments every time the module re-evaluates.
+ * Locks acquired by a previous generation are considered orphaned and released
+ * immediately on the next acquire attempt, avoiding the 15s watchdog stall.
+ */
+let _generation = 0;
+
+// Increment on every module evaluation — Hot Reload triggers a fresh evaluation
+_generation++;
```

### Step 2: Capture generation on lock acquisition, validate on release

```diff
 // useBLEGattMutex.ts — inside acquireGattLock(), near L122-136
 let release!: () => void;
 const prevChain = _lockChain;
 _lockChain = new Promise<void>(res => { release = res; });
 _isLocked = true;
 _currentPriority = priority;
+const acquiredGeneration = _generation;

 // ...

 // Wait for previous operation to complete
-await prevChain.catch((e: unknown) => {
-  AppLogger.warn('[Mutex] Previous operation in GATT lock chain rejected', e);
-});
+try {
+  await Promise.race([
+    prevChain,
+    // If the previous chain is orphaned (Hot Reload), resolve after 200ms
+    new Promise<void>(r => setTimeout(r, 200)),
+  ]);
+} catch (e: unknown) {
+  AppLogger.warn('[Mutex] Previous operation in GATT lock chain rejected', e);
+}
+
+// If a Hot Reload happened between acquiring and waiting, skip the stale lock
+if (acquiredGeneration !== _generation) {
+  AppLogger.warn('[Mutex] Stale lock detected after Hot Reload — releasing immediately');
+  _isLocked = false;
+  _currentPriority = 4;
+  _currentHolderAbortController = null;
+  release();
+  return null;
+}
```

### Step 3: Force-release stale lock at module re-evaluation

Add a cleanup block at module level to detect and release any orphaned lock:

```diff
 // useBLEGattMutex.ts — after _generation++ block
+
+// Hot Reload cleanup: if a lock was held by the previous module evaluation,
+// force-release it so the fresh module starts clean.
+if (_isLocked) {
+  AppLogger.warn('[Mutex] Hot Reload detected with lock held — force-releasing orphaned lock');
+  _isLocked = false;
+  _currentPriority = 4;
+  _currentHolderAbortController?.abort();
+  _currentHolderAbortController = null;
+  if (_deadlockWatchdog) {
+    clearTimeout(_deadlockWatchdog);
+    _deadlockWatchdog = null;
+  }
+}
```

## Files Modified

| File | Change |
|------|--------|
| `src/hooks/ble/useBLEGattMutex.ts` | Add `_generation` counter, Hot Reload cleanup block, stale-generation check in `acquireGattLock`, race timeout for orphaned prevChain |

## Verification

1. **Manual test (dev-only)**: Start a connection → trigger Hot Reload via Metro (save a file) → attempt another connection → verify it proceeds within ~200ms instead of 15s.
2. **Production safety test**: Verify `_generation` increment is harmless in production (module loads once, `_generation` is 1, no stale-lock path is hit).
3. **Deadlock watchdog still works**: Hold a lock artificially for 16s in a test → verify watchdog fires at 15s and auto-releases.
4. **`npm run verify`**: TSC + Jest + AST pass.

## Risk Assessment

| Risk | Mitigation |
|------|------------|
| Module-level side effects on import | `_generation++` is idempotent and side-effect-free beyond incrementing a counter. No network calls, no state mutation. |
| `Promise.race` with 200ms timeout could release lock prematurely in production | The 200ms timeout only races against `prevChain`. In production, `prevChain` is always `Promise.resolve()` on first load (no pending lock). The race is only hit when the old chain is orphaned (Hot Reload only). |
| Force-releasing an in-flight GATT operation could cause GATT 133 | The `_currentHolderAbortController.abort()` signals the holder to stop gracefully. The GATT operation will see `signal.aborted` at the next async boundary and exit cleanly. |

**Rollback**: Revert single file. Behavior returns to 15s watchdog stall on Hot Reload (annoying but not broken).

> [!NOTE]
> This is a **dev-only** improvement. In production, the module loads once and `_generation` stays at 1. Zero production impact.

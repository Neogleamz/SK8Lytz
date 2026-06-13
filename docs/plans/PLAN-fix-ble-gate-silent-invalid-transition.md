# Implementation Plan: fix-ble-gate-silent-invalid-transition

> **RC-05** | Priority: P1 | Risk: LOW | Size: Snack | Layer: BLE

## Problem

`BleStateMachine.transitionTo()` returns `false` on invalid transitions, but **no caller checks the return value**. The gate silently stays in the wrong state while downstream code assumes the transition succeeded.

In [BleStateMachine.ts:35-57](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleStateMachine.ts#L35-L57):

```typescript
// L35-57 — returns boolean, but callers ignore it
transitionTo(nextPhase: BLEPhase, reason: string): boolean {
  const from = this._phase.tag;
  const to = nextPhase.tag;
  if (from === to) return true;
  const isValid = this.validateTransition(from, to);
  if (!isValid) {
    AppLogger.warn(`[BleStateMachine] INVALID TRANSITION: ${from} -> ${to} (Reason: ${reason})`);
    return false;  // ← caller never checks this
  }
  // ... transition happens ...
  return true;
}
```

The sole consumer, `setGate` in [useBLE.ts:142-144](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L142-L144):

```typescript
// L142-144 — return value discarded
const setGate = useCallback((phase: BLEPhaseTag) => {
  bleGateRef.current.transitionTo({ tag: phase }, 'Manual Phase Change');
}, []);
```

`setGate` is called from:
- [BleConnectionManager.ts:110](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L110): `setGate('CONNECTING')` — could fail if gate is `SCANNING` and `SCANNING → CONNECTING` is valid, but `SCANNING → DISCONNECTING` is not.
- [BleConnectionManager.ts:132, 292, 302](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L132): `setGate('IDLE')` — always valid from any state, so safe.
- [BleLifecycleManager.ts:20](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleLifecycleManager.ts#L20): `setGate('DISCONNECTING')` — could fail from `SCANNING` (valid transitions: `SCANNING → IDLE | CONNECTING`).

**Concrete failure scenario**: If the sweeper is actively scanning (`SCANNING`) and the user taps disconnect, `setGate('DISCONNECTING')` silently fails. The gate stays in `SCANNING` while `executeRealDisconnect` tears down GATT connections. The sweeper still thinks it owns the radio.

## Root Cause

The `transitionTo()` API was designed as a "try and report" pattern, but the callers were written as "fire and forget." The warning log exists but is easy to miss in a noisy BLE log stream.

## Proposed Fix

**Strategy: Throw on invalid transitions in dev mode, return a typed result in production, and check the result at all call sites.**

### Step 1: Change `transitionTo` to throw in `__DEV__` and always log a structured error

```diff
 // BleStateMachine.ts L35-57
-transitionTo(nextPhase: BLEPhase, reason: string): boolean {
+transitionTo(nextPhase: BLEPhase, reason: string): boolean {
   const from = this._phase.tag;
   const to = nextPhase.tag;

   if (from === to) return true;

   const isValid = this.validateTransition(from, to);
   if (!isValid) {
-    AppLogger.warn(`[BleStateMachine] INVALID TRANSITION: ${from} -> ${to} (Reason: ${reason})`);
+    const msg = `[BleStateMachine] INVALID TRANSITION: ${from} -> ${to} (Reason: ${reason})`;
+    AppLogger.error(msg);
+    AppLogger.log('BLE_STATE_CHANGE', {
+      event: 'fsm_invalid_transition',
+      from,
+      to,
+      reason,
+    });
+    if (__DEV__) {
+      throw new Error(msg);
+    }
     return false;
   }
```

### Step 2: Update `setGate` to check the return value and log

```diff
 // useBLE.ts L142-144
 const setGate = useCallback((phase: BLEPhaseTag) => {
-  bleGateRef.current.transitionTo({ tag: phase }, 'Manual Phase Change');
+  const success = bleGateRef.current.transitionTo({ tag: phase }, 'Manual Phase Change');
+  if (!success) {
+    AppLogger.error('[BLE] setGate failed — gate stuck in ' + bleGateRef.current.tag, {
+      attemptedPhase: phase,
+      currentPhase: bleGateRef.current.tag,
+    });
+  }
 }, []);
```

### Step 3: Add a `forceTransitionTo` escape hatch for legitimate resets

Some call sites legitimately need to force the gate to `IDLE` regardless of current state (error recovery paths). Add a dedicated method:

```diff
 // BleStateMachine.ts — after transitionTo()
+/**
+ * Force-transitions to the given phase WITHOUT validation.
+ * Use ONLY in error recovery paths where the gate is known to be in an
+ * inconsistent state (e.g., connection failure cleanup).
+ */
+forceTransitionTo(nextPhase: BLEPhase, reason: string): void {
+  const from = this._phase.tag;
+  AppLogger.warn(`[BleStateMachine] FORCE TRANSITION: ${from} -> ${nextPhase.tag} (Reason: ${reason})`);
+  AppLogger.log('BLE_STATE_CHANGE', {
+    event: 'fsm_force_transition',
+    from,
+    to: nextPhase.tag,
+    reason,
+  });
+  this._phase = nextPhase;
+  this.notify();
+}
```

### Step 4: Use `forceTransitionTo` in error recovery paths

The `setGate('IDLE')` calls in catch blocks of BleConnectionManager ([L292, L302](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L292-L302)) should use `forceTransitionTo` since the gate may be in an unexpected state after an error:

```diff
 // BleConnectionManager.ts L292 (inner catch) and L302 (outer catch)
-setGate('IDLE');
+bleGateRef.current.forceTransitionTo({ tag: 'IDLE' }, 'Error recovery — connection failed');
```

This requires passing `bleGateRef` to these catch blocks (it's already available as a parameter at L25).

### Step 5: Add the missing `SCANNING → DISCONNECTING` transition

Looking at the transition table at [BleStateMachine.ts:74-75](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleStateMachine.ts#L74-L75):

```typescript
// L74-75
case 'SCANNING':
  return to === 'IDLE' || to === 'CONNECTING';
```

`SCANNING → DISCONNECTING` is missing. If the user taps disconnect while scanning, the transition is rejected. This is actually a valid edge case — add it:

```diff
 // BleStateMachine.ts L74-75
 case 'SCANNING':
-  return to === 'IDLE' || to === 'CONNECTING';
+  return to === 'IDLE' || to === 'CONNECTING' || to === 'DISCONNECTING';
```

### Step 6: Update existing tests

The existing test at `BleStateMachine.test.ts` tests that `SCANNING → RECOVERING` returns `false`. Ensure the new `__DEV__` throw behavior is accounted for in tests by wrapping in try/catch or mocking `__DEV__`.

## Files Modified

| File | Change |
|------|--------|
| `src/services/BleStateMachine.ts` | Add `__DEV__` throw, structured error logging, `forceTransitionTo` method, add `SCANNING → DISCONNECTING` transition |
| `src/hooks/useBLE.ts` | Check `transitionTo` return value in `setGate` |
| `src/services/BleConnectionManager.ts` | Use `forceTransitionTo` in error-recovery catch blocks (L292, L302) |
| `src/services/__tests__/BleStateMachine.test.ts` | Add test for `__DEV__` throw, test `forceTransitionTo`, test `SCANNING → DISCONNECTING` |

## Verification

1. **Unit test**: `SCANNING → DISCONNECTING` transition now returns `true`.
2. **Unit test**: Invalid transition (`SCANNING → RECOVERING`) throws in `__DEV__`, returns `false` in prod.
3. **Unit test**: `forceTransitionTo` always succeeds regardless of current state.
4. **Integration test**: Start scan → tap disconnect → verify gate transitions to `DISCONNECTING` cleanly.
5. **`npm run verify`**: TSC + Jest + AST pass.

## Risk Assessment

| Risk | Mitigation |
|------|------------|
| `__DEV__` throw could crash the app during development | This is intentional — surfaces invalid transitions as dev-time crashes rather than silent state bugs. The crash message includes the from/to/reason for immediate diagnosis. |
| `forceTransitionTo` could be misused to bypass validation | Named clearly as "force" and documented as error-recovery-only. Code review catches misuse. |
| Adding `SCANNING → DISCONNECTING` could allow unintended transitions | This is a legitimate user flow — the user can disconnect while scanning. The transition was missing, not intentionally excluded. |

**Rollback**: Revert 4 files. Gate returns to silent `warn` on invalid transitions.

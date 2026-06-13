# Implementation Plan: fix-ble-ghost-state-flicker

> **RC-03** | Priority: P3 | Risk: LOW | Size: Snack | Layer: BLE/UI

## Problem

During the Group Dropout Coordinator flow in `useBLEAutoRecovery.ts`, ghost state is **cleared before** `connectToDevices()` completes. If the reconnect fails, devices are re-ghosted in the `.catch()`. During the gap, the UI briefly shows the device as "healthy" (no ghost overlay).

In [useBLEAutoRecovery.ts:485-507](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts#L485-L507):

```typescript
// L485-489 — ghost state cleared BEFORE connectToDevices resolves
if (batch.length >= 2) {
  // GROUP DROPOUT — clear ghost state for the batch then fire group reconnect.
  ghostedRefs.current = ghostedRefs.current.filter(id => !batch.some(d => d.id === id));
  setGhostedDeviceIds([...ghostedRefs.current]);
  // ... then at L497-507:
  onGroupDropout(batch).catch((e: unknown) => {
    // Fallback: re-ghost and spawn individual loops
    batch.forEach(d => {
      if (!ghostedRefs.current.includes(d.id)) {
        ghostedRefs.current = [...ghostedRefs.current, d.id];
        spawnRecoveryLoop(d.id);
      }
    });
    setGhostedDeviceIds([...ghostedRefs.current]);
  });
}
```

**Timeline of the flicker**:
1. `t0`: Devices ghosted (dimmed card overlay) ✅
2. `t1`: Coordinator fires → ghost cleared → UI shows healthy ❌ (premature)
3. `t2a`: `connectToDevices` succeeds → devices actually healthy ✅
4. `t2b`: `connectToDevices` fails → `.catch()` re-ghosts → UI shows ghost again ✅

The flicker between `t1` and `t2b` is a visual glitch. Users see cards flash from ghost → healthy → ghost within ~2-5s.

## Root Cause

The coordinator clears ghost state optimistically before the connection result is known. The original design intent was to hand ownership of the device state to `connectToDevices`, which sets `connectedDevices` on success. But the ghost overlay is driven by `ghostedDeviceIds`, which is a separate state — clearing it early creates a visual desync.

## Proposed Fix

**Strategy: Keep ghost state until connection is confirmed successful. Clear ghost in `.then()`, not before dispatch.**

### Step 1: Move ghost-clear from pre-dispatch to post-success

In [useBLEAutoRecovery.ts L485-507](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts#L485-L507):

```diff
 // useBLEAutoRecovery.ts L485-507
 if (batch.length >= 2) {
-  // GROUP DROPOUT — clear ghost state for the batch then fire group reconnect.
-  // connectToDevices will re-add devices to connectedDevices on success.
-  ghostedRefs.current = ghostedRefs.current.filter(id => !batch.some(d => d.id === id));
-  setGhostedDeviceIds([...ghostedRefs.current]);
-
   AppLogger.log('AUTO_RECOVERY_GROUP_COORDINATOR', {
     event: 'group_dropout_detected',
     count: batch.length,
     devices: batch.map(d => d.id),
   });

-  onGroupDropout(batch).catch((e: unknown) => {
-    // Fallback: if group reconnect fails, re-ghost and spawn individual loops
-    AppLogger.warn('[AutoRecovery] Group dropout reconnect failed — falling back to individual loops', e);
-    batch.forEach(d => {
-      if (!ghostedRefs.current.includes(d.id)) {
-        ghostedRefs.current = [...ghostedRefs.current, d.id];
-        spawnRecoveryLoop(d.id);
-      }
+  onGroupDropout(batch)
+    .then(() => {
+      // SUCCESS: clear ghost state now that devices are confirmed reconnected
+      ghostedRefs.current = ghostedRefs.current.filter(id => !batch.some(d => d.id === id));
+      setGhostedDeviceIds([...ghostedRefs.current]);
+    })
+    .catch((e: unknown) => {
+      // FAILURE: ghost state was never cleared — devices stay dimmed.
+      // Fall back to individual recovery loops for each failed device.
+      AppLogger.warn('[AutoRecovery] Group dropout reconnect failed — falling back to individual loops', e);
+      batch.forEach(d => {
+        spawnRecoveryLoop(d.id);
+      });
     });
-    setGhostedDeviceIds([...ghostedRefs.current]);
-  });
 }
```

### Step 2: Clean up the `.catch()` fallback

The original `.catch()` re-checks `if (!ghostedRefs.current.includes(d.id))` before re-ghosting. Since we no longer clear ghost state before dispatch, the devices are already in `ghostedRefs.current`. The fallback just needs to spawn individual recovery loops — no need to re-add to ghost list.

## Files Modified

| File | Change |
|------|--------|
| `src/hooks/ble/useBLEAutoRecovery.ts` | Move ghost-clear from pre-dispatch (L488-489) to `.then()` callback, simplify `.catch()` fallback |

## Verification

1. **Visual test**: Connect 2+ devices → power off all → observe cards stay dimmed (ghost overlay) continuously through the reconnect attempt — no healthy flash.
2. **Success path test**: Power off → power on within 30s → verify ghost clears only after `connectToDevices` resolves.
3. **Failure path test**: Power off all devices permanently → verify ghost state persists, individual recovery loops start, no premature healthy flash.
4. **`npm run verify`**: TSC + Jest + AST pass.

## Risk Assessment

| Risk | Mitigation |
|------|------------|
| `connectToDevices` may succeed but not resolve the promise (hangs) | `connectToDevices` has internal timeouts (3500ms per device via BleSessionFactory). Ghost stays visible during the hang — acceptable UX. |
| Ghost state stays visible ~2-5s longer on success path | Acceptable trade-off: 2s of ghost overlay vs. a confusing flash. Users won't notice a brief delay in un-ghosting. |
| `.then()` fires but `connectToDevices` only partially succeeded (some devices connected, some didn't) | `connectToDevices` succeeds atomically per batch. Partially connected devices are in `connectedDevices` but still ghosted — individual recovery loops will handle stragglers. The ghost-clear in `.then()` only fires on full success. |

**Rollback**: Revert single file change. Ghost clears pre-dispatch (original behavior with flicker).

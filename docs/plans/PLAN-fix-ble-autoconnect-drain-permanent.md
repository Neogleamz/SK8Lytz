# Implementation Plan: fix-ble-autoconnect-drain-permanent

> **RC-02** | Priority: P1 | Risk: MEDIUM | Size: Meal | Layer: BLE

## Problem

When the auto-connect observer dispatches a batch of devices to `connectToDevices()`, those MACs are **permanently drained** from `autoConnectIdsRef` regardless of connection outcome.

In [useDashboardAutoConnect.ts:164-170](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L164-L170):

```typescript
// L164-170 — drain happens BEFORE connection result is known
const batch = [...pendingBatchRef.current];
pendingBatchRef.current = [];

// Drain queue for this batch to prevent reconnection loop
autoConnectIdsRef.current = autoConnectIdsRef.current.filter(
  id => !batch.some(p => p.id === id)
);
```

Then at [L177-182](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L177-L182):

```typescript
// L177-182 — fire-and-forget: no error handling for failed connections
connectToDevicesRef.current(batch).finally(() => {
  if (autoConnectIdsRef.current.length > 0) {
    AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_resume_scan' });
    scanForPeripheralsRef.current({ disableProbing: true });
  }
});
```

**Impact**: If `connectToDevices()` fails (GATT 133, device out of range, BLE stack glitch on cold boot), the MACs are gone forever from the auto-connect queue. The user must manually force close and reopen the app, or call `retriggerAutoConnect()` — which only happens on AppState wake or wizard completion, not on connection failure.

## Root Cause

The drain-before-dispatch pattern was intentionally designed to prevent reconnection loops (a device appearing in allDevices would re-trigger the observer endlessly). But it doesn't distinguish between "connection dispatched" and "connection succeeded."

## Proposed Fix

**Strategy: Drain on dispatch (keep current loop prevention), but re-queue on failure with exponential backoff.**

### Step 1: Add a retry counter and pending set

At the top of `useDashboardAutoConnect`, add refs to track retry state:

```diff
 // useDashboardAutoConnect.ts — near L110-120
 const autoConnectIdsRef = useRef<string[]>([]);
+/** Tracks retry counts per MAC for failed auto-connect attempts. */
+const autoConnectRetriesRef = useRef<Map<string, number>>(new Map());
+/** Max retries before permanently abandoning a MAC. */
+const MAX_AUTO_CONNECT_RETRIES = 3;
+/** Base backoff in ms between retries. */
+const AUTO_CONNECT_RETRY_BACKOFF_MS = 3000;
```

### Step 2: Re-queue failed MACs in the `.finally()` / `.catch()` handler

Replace the fire-and-forget pattern at L177-182 with failure-aware re-queueing:

```diff
 // useDashboardAutoConnect.ts — replace L177-182
-connectToDevicesRef.current(batch).finally(() => {
-  if (autoConnectIdsRef.current.length > 0) {
-    AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_resume_scan' });
-    scanForPeripheralsRef.current({ disableProbing: true });
-  }
-});
+connectToDevicesRef.current(batch)
+  .then(() => {
+    // On success: clear retry counters for devices that connected
+    for (const d of batch) {
+      autoConnectRetriesRef.current.delete(d.id);
+    }
+  })
+  .catch((e: unknown) => {
+    AppLogger.warn('[AutoConnect] Batch connection failed — re-queueing', {
+      macs: batch.map(d => d.id),
+      error: String(e),
+    });
+
+    // Re-queue MACs that aren't already connected and haven't exceeded retries
+    const failedIds = batch
+      .filter(d => !connectedDevices.some(c => c.id === d.id))
+      .map(d => d.id);
+
+    for (const id of failedIds) {
+      const retries = (autoConnectRetriesRef.current.get(id) ?? 0) + 1;
+      autoConnectRetriesRef.current.set(id, retries);
+
+      if (retries <= MAX_AUTO_CONNECT_RETRIES) {
+        const backoff = AUTO_CONNECT_RETRY_BACKOFF_MS * retries;
+        AppLogger.log('BLE_STATE_CHANGE', {
+          event: 'auto_connect_requeued',
+          mac: id,
+          retry: retries,
+          backoffMs: backoff,
+        });
+        setTimeout(() => {
+          if (!autoConnectIdsRef.current.includes(id)) {
+            autoConnectIdsRef.current = [...autoConnectIdsRef.current, id];
+          }
+        }, backoff);
+      } else {
+        AppLogger.warn('[AutoConnect] Max retries exceeded — abandoning', { mac: id, retries });
+        autoConnectRetriesRef.current.delete(id);
+      }
+    }
+  })
+  .finally(() => {
+    if (autoConnectIdsRef.current.length > 0) {
+      AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_resume_scan' });
+      scanForPeripheralsRef.current({ disableProbing: true });
+    }
+  });
```

### Step 3: Clear retry counters on retrigger

In the `retriggerAutoConnect` function at [L339-355](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L339-L355), clear the retry map:

```diff
 // useDashboardAutoConnect.ts L347-348
 hasAutoConnectedRef.current = false;
 autoConnectIdsRef.current = [];
+autoConnectRetriesRef.current.clear();
```

### Step 4: Capture `connectedDevices` in the observer effect's closure

The `.catch()` handler needs to check which devices actually connected. The observer effect already has `connectedDevices` in its dependency array ([L187](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L187)), so the closure is fresh. No additional change needed — the `connectedDevices` variable at L98 is available.

## Files Modified

| File | Change |
|------|--------|
| `src/hooks/useDashboardAutoConnect.ts` | Add retry refs, replace fire-and-forget with failure-aware re-queue, clear retries on retrigger |

## Verification

1. **Simulated failure test**: Mock `connectToDevices` to reject on first call, resolve on second. Assert MACs are re-queued with increasing backoff and eventually connect.
2. **Max retry test**: Mock `connectToDevices` to always reject. Assert MACs are abandoned after 3 retries with a log entry.
3. **Happy path test**: Mock `connectToDevices` to resolve. Assert retry map is empty after success and no re-queue occurs.
4. **Manual test**: Power on app with fleet devices out of range → wait 15s → power on devices → verify auto-connect retries and eventually connects.
5. **`npm run verify`**: TSC + Jest + AST pass.

## Risk Assessment

| Risk | Mitigation |
|------|------------|
| Re-queued MACs could create a reconnection loop if device stays visible in allDevices | The drain-on-dispatch pattern is preserved. Re-queue happens after a backoff delay (3s, 6s, 9s). The gate check at L153 prevents connection spam. |
| setTimeout-based re-queue could fire after component unmount | The observer effect checks `autoConnectIdsRef.current.length === 0` early, and the re-queue only adds to the ref — no state updates happen in the timeout callback. Safe for unmount. |
| `connectedDevices` closure may be stale by the time `.catch()` fires | The `.catch()` fires synchronously after the connection attempt. React state will be current because the observer effect re-runs on `[allDevices, connectedDevices]` changes. |

**Rollback**: Revert single file. The drain-before-dispatch pattern is the fallback behavior.

# Implementation Plan: fix-ble-state-ref-lag

> **RC-01** | Priority: P2 | Risk: LOW | Size: Snack | Layer: BLE

## Problem

`connectedDevices` (React state) and `connectedDevicesRef` (ref) can diverge for one render frame.

In [useBLE.ts:188-190](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L188-L190), the ref is synced **reactively** via `useEffect`:

```typescript
// useBLE.ts L188-190
useEffect(() => { 
  connectedDevicesRef.current = connectedDevices; 
}, [connectedDevices]);
```

This means after `setConnectedDevices(...)` fires, the ref is stale for the **entire render** until the effect runs post-commit. During that window:

1. **useBLEHeartbeat** ([useBLEHeartbeat.ts:102](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEHeartbeat.ts#L102)) reads `connectedDevicesRef.current` in its 45s interval — could ping a device that was just disconnected, triggering a spurious recovery.
2. **useBLERSSIMonitor** ([useBLERSSIMonitor.ts:84](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLERSSIMonitor.ts#L84)) reads `connectedDevicesRef.current` in its 30s interval — RSSI read on a dead GATT handle triggers GATT 133.
3. **BleConnectionManager** ([BleConnectionManager.ts:46](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L46)) checks `connectedDevicesRef.current` for connection caching — could skip a reconnect that should happen.
4. **useBLEAutoRecovery** group dropout coordinator ([useBLEAutoRecovery.ts:471](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts#L471)) reads `connectedDevicesRef.current` to find Device objects — could see a stale ghost.

## Root Cause

React's `useState` setter queues a batched re-render. The `useEffect` that syncs the ref fires **after** the commit phase (post-paint). Any imperative code reading the ref between `setState` and the effect execution sees the previous value.

## Proposed Fix

**Strategy: Make the ref authoritative, sync to state for React renders.**

Create a wrapper function `updateConnectedDevices` that writes to the ref **synchronously** and also calls `setConnectedDevices` for React re-renders. Replace all direct `setConnectedDevices` calls that touch `connectedDevicesRef`.

### Step 1: Create the synchronized setter in `useBLE.ts`

Replace the useEffect sync pattern with a write-through wrapper.

```diff
 // useBLE.ts — near L161
 const connectedDevicesRef = useRef<Device[]>([]);
+
+// Write-through setter: updates ref synchronously, then schedules React state update.
+// Eliminates the 1-frame ref-vs-state lag that caused phantom heartbeat pings (RC-01).
+const updateConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>> = useCallback(
+  (action) => {
+    const prev = connectedDevicesRef.current;
+    const next = typeof action === 'function' ? action(prev) : action;
+    connectedDevicesRef.current = next;
+    setConnectedDevices(next);
+  },
+  [],
+);
```

### Step 2: Remove the stale useEffect sync

```diff
 // useBLE.ts L188-190 — DELETE this effect entirely
-useEffect(() => { 
-  connectedDevicesRef.current = connectedDevices; 
-}, [connectedDevices]);
```

### Step 3: Replace `setConnectedDevices` with `updateConnectedDevices` in useBLE.ts

All internal usages within `useBLE.ts` that pass `setConnectedDevices` as a prop should pass `updateConnectedDevices` instead:

| Call Site | Line | Change |
|-----------|------|--------|
| `useBLEAutoRecovery` props | L300 | `setConnectedDevices` → `updateConnectedDevices` |
| `useBLEHeartbeat.onStaleLinkDetected` | L398 | `setConnectedDevices` → `updateConnectedDevices` |
| `executeConnectToDevices` props | L438 | `setConnectedDevices` → `updateConnectedDevices` |
| `realDisconnect` call | L509 | `setConnectedDevices` → `updateConnectedDevices` |
| AppState wake audit | L250 | `setConnectedDevices` → `updateConnectedDevices` |

### Step 4: Update callee signatures

`BleConnectionManager.ts`, `BleLifecycleManager.ts`, and `useBLEAutoRecovery.ts` all accept `setConnectedDevices` as a parameter. Their type signatures don't change (still `React.Dispatch<React.SetStateAction<Device[]>>`) — they just receive the write-through wrapper at runtime.

No signature changes needed in downstream files.

## Files Modified

| File | Change |
|------|--------|
| `src/hooks/useBLE.ts` | Add `updateConnectedDevices` wrapper, delete useEffect sync (L188-190), replace all `setConnectedDevices` prop-passing with `updateConnectedDevices` |

## Verification

1. **Unit test**: Write a synchronous test that calls `updateConnectedDevices`, then immediately reads `connectedDevicesRef.current` — assert they match without waiting for a render cycle.
2. **Integration test**: Connect 2 devices → disconnect 1 → immediately check that `connectedDevicesRef.current.length === 1` (no stale ghost).
3. **Manual test**: Connect fleet → power off one skate → verify heartbeat doesn't trigger a spurious "stale link detected" log within the first render frame.
4. **`npm run verify`**: TSC + Jest + AST pass.

## Risk Assessment

| Risk | Mitigation |
|------|------------|
| `updateConnectedDevices` reads stale `connectedDevicesRef.current` when two setters race | The functional form `action(prev)` uses `connectedDevicesRef.current` as `prev`, which is always the latest synchronous value — no React batching delay. |
| Downstream callers that capture `setConnectedDevices` in a closure won't see the wrapper | All callers receive the wrapper as a prop — they don't import `setConnectedDevices` directly. No risk. |
| React state and ref could diverge if React batches the `setConnectedDevices(next)` call | Acceptable — the ref is authoritative for imperative reads. React state is only for triggering re-renders. The ref is always ≥ as fresh as state. |

**Rollback**: Revert the single file change. Re-add the useEffect sync. Zero downstream impact.

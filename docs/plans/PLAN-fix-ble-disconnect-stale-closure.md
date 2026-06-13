# Implementation Plan: fix-ble-disconnect-stale-closure

> **RC-06** | Priority: P2 | Risk: LOW | Size: Snack | Layer: BLE

## Problem

`handleOrganicDisconnect` is defined as a plain function (not `useCallback`) in [useBLE.ts:286-290](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L286-L290):

```typescript
// L286-290 — re-created every render, but captured by value in listeners
const handleOrganicDisconnect = (error: any, deviceId: string) => {
  AppLogger.warn(`[BLE] Organic disconnect/dropout for ${deviceId}`);
  AppLogger.log('DEVICE_DISCONNECTED', { id: deviceId, reason: 'dropout', error: error?.message });
  autoRecovery.initiateRecovery(deviceId);
};
```

This function closes over `autoRecovery.initiateRecovery`. It is passed **by value** to:

1. **BleConnectionManager.ts** at [L229-231](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L229-L231) — registered in `onDeviceDisconnected`:
   ```typescript
   // L229-231 — callback captured once, never refreshed
   disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
     handleOrganicDisconnect(error, conn.id);
   });
   ```

2. **useBLEAutoRecovery.ts** at [L319-321](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts#L319-L321) — re-registered after recovery:
   ```typescript
   // L319-321 — also captured once per recovery cycle
   disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: Error | null) => {
     onOrganicDisconnect(error, conn.id);
   });
   ```

The `onDeviceDisconnected` listener is registered **once** during `handshakeDevice()` or recovery and **never refreshed** on re-render. If `autoRecovery.initiateRecovery` changes identity after a re-render (e.g., if `useBLEAutoRecovery` returns a new `initiateRecovery` due to dependency array changes), the listener holds a stale reference.

**Current mitigation**: `initiateRecovery` is wrapped in `useCallback` at [useBLEAutoRecovery.ts:456](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts#L456) with a dependency array at [L523](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts#L523):
```typescript
}, [bleManager, disconnectListeners, handleNotification, onOrganicDisconnect, setConnectedDevices, bleGateRef, onGroupDropout]);
```

If **any** of those 7 dependencies changes identity, `initiateRecovery` gets a new reference. The closure in `onDeviceDisconnected` will call the **old** `initiateRecovery`, which references the old `handleNotification`, old `onOrganicDisconnect`, etc.

## Root Cause

Classic React stale closure: a BLE event listener is registered once and captures a function reference that may become stale on re-render. The pattern works by accident today because most dependencies are stable refs or memoized callbacks, but it's structurally fragile.

## Proposed Fix

**Strategy: Use a ref pattern — store `handleOrganicDisconnect` in a ref that always points to the latest version. Pass a stable wrapper to the listener.**

### Step 1: Create a stable ref-wrapper for `handleOrganicDisconnect`

```diff
 // useBLE.ts — after L290
 const handleOrganicDisconnect = (error: any, deviceId: string) => {
   AppLogger.warn(`[BLE] Organic disconnect/dropout for ${deviceId}`);
   AppLogger.log('DEVICE_DISCONNECTED', { id: deviceId, reason: 'dropout', error: error?.message });
   autoRecovery.initiateRecovery(deviceId);
 };
+
+// Stable ref-forwarder: the BLE disconnect listener captures this ref once,
+// but it always delegates to the latest handleOrganicDisconnect. Same pattern
+// as handleNotificationRef (L224-225). Eliminates RC-06 stale closure risk.
+const handleOrganicDisconnectRef = useRef(handleOrganicDisconnect);
+handleOrganicDisconnectRef.current = handleOrganicDisconnect;
```

### Step 2: Pass the ref-forwarder to BleConnectionManager

Update the `connectToDevices` call at [useBLE.ts:421-448](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L421-L448):

```diff
 // useBLE.ts L437
-handleOrganicDisconnect,
+handleOrganicDisconnect: (error: any, deviceId: string) =>
+  handleOrganicDisconnectRef.current(error, deviceId),
```

### Step 3: Pass the ref-forwarder to useBLEAutoRecovery

Update the auto-recovery props at [useBLE.ts:298-327](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L298-L327):

```diff
 // useBLE.ts L303
-onOrganicDisconnect: handleOrganicDisconnect,
+onOrganicDisconnect: (error: any, deviceId: string) =>
+  handleOrganicDisconnectRef.current(error, deviceId),
```

### Step 4: No changes needed in BleConnectionManager or useBLEAutoRecovery

Both files receive `handleOrganicDisconnect` / `onOrganicDisconnect` as a parameter and register it in `onDeviceDisconnected`. Since the parameter is now a stable wrapper (arrow function that reads from the ref), the listener always calls the latest version. No structural changes needed in those files.

## Files Modified

| File | Change |
|------|--------|
| `src/hooks/useBLE.ts` | Add `handleOrganicDisconnectRef`, pass ref-forwarding wrappers to `executeConnectToDevices` and `useBLEAutoRecovery` props |

## Verification

1. **Manual test**: Connect fleet → trigger a Hot Reload → disconnect one device → verify auto-recovery starts (proving the listener calls the latest `initiateRecovery`, not a stale one).
2. **Code audit**: Confirm this follows the same pattern as `handleNotificationRef` at [useBLE.ts:224-225](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L224-L225), which is already proven stable.
3. **`npm run verify`**: TSC + Jest + AST pass.

## Risk Assessment

| Risk | Mitigation |
|------|------------|
| Ref update on every render is expensive | `handleOrganicDisconnectRef.current = handleOrganicDisconnect` is a single assignment — zero allocation overhead. Same pattern used for `handleNotificationRef` at L225. |
| The wrapper arrow function creates a new closure on each render | The wrapper is only created once when passed as a prop (it's inside `useCallback` or `useMemo`). The ref read (`handleOrganicDisconnectRef.current`) happens at **call time**, not at creation time. |
| Breaking the `connectToDevices` `useCallback` dependency array | `handleOrganicDisconnect` is already in the dependency array as a plain function (new on every render). Replacing it with a stable wrapper actually **improves** memoization — `connectToDevices` won't re-create on every render anymore. |

**Rollback**: Revert single file. The stale closure risk returns but is mitigated by stable `useCallback` dependencies (current behavior).

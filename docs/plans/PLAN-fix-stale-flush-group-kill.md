# Implementation Plan: fix/stale-flush-group-kill

## Problem

ConnectService.ts has a "stale flush" mechanism (L77-106) that disconnects any device in `connectedDevicesRef.current` that is NOT in the current `targetMacs` array. This was designed for fleet switching (disconnect old fleet → connect new fleet), but it catastrophically breaks **incremental group assembly**.

**Reproduction Scenario:**
When auto-connect fires in two separate batches (Device A appears in BLE scan first, Device B appears 600ms+ later):

1. Batch 1: `connectToDevices([A])` → ConnectService connects A → XState sets `connectedDevices = [A]`
2. Batch 2: `connectToDevices([B])` → ConnectService sees A in `connectedDevicesRef.current` but NOT in `targetMacs = [B]` → A is classified as "stale" → `cancelDeviceConnection(A)` called → only B is returned → `connectedDevices = [B]`
3. Result: Only 1 of 2 group devices is connected. User sees single-device control.

**User Impact:** Group connection fundamentally broken. Only one skate lights up despite both having strong RSSI and being visible during setup.

## Root Cause

[ConnectService.ts L77](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L77):
```ts
const staleDevices = connectedDevicesRef.current.filter(c => !targetMacs.includes(c.id));
```
This line marks **every currently-connected device NOT in the current batch** as stale. During incremental assembly, this kills Device A when Device B's batch arrives.

## Proposed Changes

### BLE Connection Layer

#### [MODIFY] [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)

**Change 1: Remove the stale flush entirely (L77, L88-107)**

Replace the stale flush logic with a retain-all approach. Connected devices that are not in the target list are simply retained (carried forward in the final merge at L304-306). They are NOT disconnected.

The stale flush was originally intended for fleet switching, but fleet switching is already handled correctly by the `DISCONNECT_REQUEST` → `CONNECTING` flow in BleMachine.ts. The user taps "Disconnect" before connecting a different group. The auto-connect observer never intends to switch fleets — it only adds devices incrementally.

**Before (L77-107):**
```ts
const staleDevices = connectedDevicesRef.current.filter(c => !targetMacs.includes(c.id));

if (allRequestedAlreadyConnected && staleDevices.length === 0) {
  // cache hit skip...
}

const retainedDevices = connectedDevicesRef.current.filter(c => targetMacs.includes(c.id));

// Stale flush logic
if (staleDevices.length > 0) {
  for (const stale of staleDevices) { ... cancel ... }
  await settle delay...
}
```

**After:**
```ts
// Check if all requested devices are already connected (no work needed)
if (allRequestedAlreadyConnected) {
  AppLogger.log('BLE_STATE_CHANGE', { event: 'connectToDevices_cached_hit_skip' });
  return { devices: connectedDevicesRef.current };
}

// Retain ALL currently connected devices — incremental assembly never flushes.
// Fleet switching is handled by DISCONNECT_REQUEST → CONNECTING, not here.
const retainedDevices = [...connectedDevicesRef.current];
```

Key behavioral changes:
- **Stale flush loop removed entirely** — no more `cancelDeviceConnection()` on non-target devices
- **`retainedDevices` now includes ALL connected devices**, not just those in `targetMacs`
- **Cache-hit check simplified** — `staleDevices.length === 0` guard removed since stale concept is eliminated
- **Final merge at L304-306 still works correctly** — `retainedDevices` + `freshlyConnected` deduplication is unchanged

**Verify:** 
- Connect 2 devices in a group → both should remain connected after incremental auto-connect
- `connectedDevices.length` should be 2 after both devices are assembled
- `isGrouped` should resolve to `true`
- Color dispatch should iterate both devices

**Change 2: Update existing tests (L325-360)**

Update test cases 7 and 8 in `ConnectService.test.ts` that validate the old stale flush behavior. The tests should now verify that non-target connected devices are **retained**, not cancelled.

#### [MODIFY] [ConnectService.test.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/ConnectService.test.ts)

Update:
- Test 7: "Non-target connected device is retained during incremental assembly" (was: "Stale connected device is cancelled")
- Test 8: "No stale flush settle delay when all devices are retained" (was: "Stale flush respects settle delay timing")

**Verify:** `npm run verify` passes with updated tests.

## Files to Create/Modify

| Action | File | Lines |
|--------|------|-------|
| MODIFY | `src/services/ble/ConnectService.ts` | L73-107 |
| MODIFY | `src/services/ble/__tests__/ConnectService.test.ts` | L325-360 |

## Out of Scope

- `useDashboardAutoConnect.ts` — the batch observer debounce logic is fine; the fix is entirely in ConnectService
- `BleMachine.ts` — `setConnectedDevices` full-replacement action is correct because ConnectService now returns the merged array
- `useControllerDispatch.ts` — dispatch iteration is already correct
- `BleWriteDispatcher.ts` — write routing is already correct
- Any UI component changes

## Verification Plan

### Automated Tests
```
npm run verify
```
TSC compilation + Jest test suite including updated ConnectService tests.

### Manual Verification
1. Build release APK via `/deploy-device`
2. Open app with 2 registered devices in a group (both powered on, strong RSSI)
3. Verify both connect during auto-connect (Dashboard should show 2 connected devices)
4. Tap a color → both skates should change simultaneously
5. Apply a pattern → both skates should animate identically

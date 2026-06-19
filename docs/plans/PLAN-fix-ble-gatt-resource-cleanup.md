# Implementation Plan: fix/ble-gatt-resource-cleanup

## Problem
After `cancelDeviceConnection`, we don't explicitly destroy/close the GATT client handle. Over many connect/disconnect cycles (common for skaters), Android can exhaust its limited GATT connection slots (~7-8 total), causing all future connections to fail silently.

Source: [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L20-L32) — disconnectService calls `cancelDeviceConnection(device.id)` but no explicit `close()`.

## Files to Create/Modify

#### [MODIFY] [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
- In disconnectService (L20-32): after `cancelDeviceConnection`, call `bleManager.destroyClient?.()` or device-level cleanup if available in ble-plx API
- Note: In react-native-ble-plx, `cancelDeviceConnection` is the primary disconnect method. Verify if `.destroy()` on BleManager is the correct additional cleanup (it is — for per-device GATT handle release)

#### [MODIFY] [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- In error catch paths where connection fails: ensure any partial GATT handle from `connectToDevice` is cleaned up

## Steps

### Step 1: Research ble-plx GATT cleanup API
- Check if `cancelDeviceConnection` also closes the GATT handle, or if additional cleanup is needed
- Source: react-native-ble-plx docs
- Verify: Document the correct API

### Step 2: Add cleanup after cancel in disconnectService
- Source: BleMachine.ts L20-32
- Verify: git diff shows only the cleanup addition

### Step 3: Add cleanup in ConnectService error paths
- Source: ConnectService.ts catch blocks
- Verify: git diff shows only error path changes

## Out of Scope
- Background mode
- GATT operation queue
- Connection parameters
- UI components

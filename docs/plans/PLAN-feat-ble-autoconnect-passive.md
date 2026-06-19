# Implementation Plan: feat/ble-autoconnect-passive

## Problem
We always use direct connect (`autoConnect: false`) via `bleManager.connectToDevice(mac)` at [ConnectService.ts L154](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L154). This requires active scanning to find the device first.

After initial connection succeeds, switching to `autoConnect: true` for **subsequent reconnections** would let the Android OS reconnect passively when the device comes back in Bluetooth range — without our app needing to actively scan.

Industry standard: Direct connect for first pairing (faster), autoConnect for subsequent reconnections (fire-and-forget).

## Files to Create/Modify

#### [MODIFY] [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- At L154: `bleManager.connectToDevice(mac, ...)` — add `autoConnect` parameter
- Determine if this is a reconnect (device MAC is in registered MACs from context) vs first-time connect
- First connect: `autoConnect: false` (faster, current behavior)
- Reconnect: `autoConnect: true` (OS-managed passive reconnect)
- Accept `registeredMacs: string[]` in the service invoke input to distinguish

#### [MODIFY] [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
- Pass `registeredMacs` context into ConnectService invoke input

#### [MODIFY] [BleMachine.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts)
- Add `registeredMacs: string[]` to machine context type if not already present

## Steps

### Step 1: Add isReconnect detection to ConnectService
- Check if target MAC exists in registeredMacs list
- Source: ConnectService.ts L154
- Verify: AppLogger logs `autoConnect: true/false` per device

### Step 2: Wire registeredMacs into invoke input
- Source: BleMachine.ts connect service invoke
- Verify: TSC clean

### Step 3: Test autoConnect behavior
- Verify: Device reconnects passively when it comes back in range (manual test)

## Out of Scope
- Background mode (that's a separate plan)
- GATT operation queue
- Connection parameters
- UI components

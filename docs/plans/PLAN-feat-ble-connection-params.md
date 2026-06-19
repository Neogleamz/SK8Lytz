# Implementation Plan: feat/ble-connection-params

## Problem
After connecting to a device, we use default OS-negotiated connection parameters. Active LED control needs low latency (~15ms interval) for responsive color changes, but the same low latency during idle periods wastes battery on both the phone and the LED controller.

Industry standard (Nordic SDK): request `ConnectionPriority.High` during active control, switch to `ConnectionPriority.Balanced` during idle.

Source: [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L154-L158) — `connectToDevice` at L154 uses default params. MTU negotiation happens after connect but no connection priority request.

## Files to Create/Modify

#### [MODIFY] [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- After MTU negotiation succeeds, call `conn.requestConnectionPriority(ConnectionPriority.High)` (or react-native-ble-plx equivalent: `1` = High, `0` = Balanced)
- Add a helper function `requestHighPriority(deviceId)` and `requestBalancedPriority(deviceId)`

#### [MODIFY] [bleTimingConstants.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/bleTimingConstants.ts)
- Add `CONNECTION_IDLE_TIMEOUT_MS: 30_000` — time after last user command before switching to Balanced

#### [MODIFY] [BleWriteQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts) (or BleOperationQueue)
- Add idle timeout tracking: after last `enqueueWrite`, start idle timer
- On idle timeout, emit event or callback to switch to Balanced priority
- On next enqueue, switch back to High priority

## Steps

### Step 1: Add connection priority request after MTU negotiation
- Source: ConnectService.ts MTU section
- Verify: ADB logcat shows priority request log

### Step 2: Add idle timeout for priority downgrade
- Track last write timestamp in BleWriteQueue
- After 30s idle → callback → priority switch to Balanced
- Verify: Unit test for idle detection

### Step 3: Re-elevate on next user command
- On next `enqueueWrite` with priority 'normal', switch back to High
- Verify: Priority oscillation test

## Out of Scope
- Background mode
- GATT queue refactor (structural changes — this only adds priority switching)
- UI components
- Scan filter

# Implementation Plan — Resolve BLE Connection Hang

## Goal Description
Fix the connection hang when attempting to connect to a controller device. The app gets stuck at the blue screen because the `BleMachine` state remains in `CONNECTING` indefinitely. This happens because `ConnectService.ts` calls `bleManager.connectedDevices([mac])` passing the MAC address instead of service UUIDs, causing it to return `[]` even if the device is already connected. As a result, the code falls back to calling `connectToDevice` on an already connected device, which hangs or fails.

## User Review Required
None. This is a surgical bug fix.

## Open Questions
None.

## Proposed Changes

### BLE Service Layer

#### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- Replace the buggy `connectedDevices([mac])` query with `connectedDevices([])` and then find the device with matching MAC address.
- Ensure that if the device is already connected, it returns the existing device object correctly and bypasses re-connection.

```typescript
// Proposed replacement at lines 147-151:
          const isConnected = await bleManager.isDeviceConnected(mac);
          if (isConnected) {
            const devicesList = await bleManager.connectedDevices([]).catch(() => []);
            conn = devicesList.find(d => d.id === mac) || null;
          } else {
            conn = null;
          }
```

## Verification Plan

### Automated Tests
- Run the Jest unit test suite:
  ```powershell
  npm run verify
  ```
  This will execute all Jest tests (including `ConnectService.test.ts` and `BleMachine.test.ts`) to ensure no regressions are introduced.

### Manual Verification
- Deploy the updated code to the physical Android device.
- Verify that opening the controller screen successfully connects and transitions from the blue screen to the controller interface.

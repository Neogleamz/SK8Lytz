# Epic: Connection Reliability & BLE Teardown Stability

Fixes intermittent GATT 133 errors (connection dropouts), UI thread lockups when entering/leaving the DockedController, and abrupt teardown behavior upon hardware disconnection. 

## Design Decisions & Rationale
Android's native BLE stack (and the React Native BLE PLX layer) penalizes un-awaited or overlapping connection/disconnection promises, resulting in GATT 133 exceptions and frozen BLE adapters. We will solve this by transforming `disconnectFromDevice` into a strictly awaited async sequence. Additionally, the UI currently violently unmounts `DockedController` before the hardware teardown is complete. By introducing an `isDisconnecting` state into the Dashboard, we can lock navigation and prevent unmount races, ensuring robust state parity between the physical connection layer and the UI representation.

## Proposed Changes

### Configuration / Core Services

#### [MODIFY] src/hooks/useBLE.ts
- Refactor `disconnectFromDevice` to be `async`.
- `await` every call to `bleManager.cancelDeviceConnection(device.id)` in the teardown loop sequentially to prevent GATT 133 stack overflows.
- Add a tiny 250ms buffer delay after the disconnect loop to let the Android BluetoothAdapter settle before returning control to the UI.
- Wrap dropout listener (`bleManager.onDeviceDisconnected`) so that it gracefully updates `connectedDevices` without triggering nested race conditions.

### User Interface / Screens

#### [MODIFY] src/screens/DashboardScreen.tsx
- Add a new `isDisconnecting` React state to block rapid double-tapping on the back/disconnect handlers.
- Refactor `handleDisconnect` to `async`, setting `isDisconnecting(true)`, awaiting `disconnectFromDevice()`, and then resetting state.
- Ensure the hardware back button logic `handleBackPress` does not blindly return `true` continuously if a disconnect is already in progress.
- Prevent `DockedController` from instantly unmounting on press by letting it fade out or locking the UI while `isDisconnecting` is active.

#### [MODIFY] src/components/DockedController.tsx
- Add an `isDisconnecting` prop (or short-circuit internal effects) so that if the controller is being torn down, it immediately aborts any debounced BLE writes (`setTimeout`s for color/speed changes). This prevents writes from occurring on a dead connection, which locks the Android JS thread bridging queue.

## UI Craftsmanship
- The user will now see a brief, elegant loading spinner or "Disconnecting..." state when hitting back or exiting, rather than an instant glitch back to the setup scan state. 
- 4-State handling will safely handle the transition from connected -> disconnecting -> clean empty state.

## Verification Plan

### Automated Tests
- Validate no TypeScript errors (`npx tsc --noEmit`) post refactoring regarding the new `async` signatures on `useBLE`.

### Manual Verification
- **Rapid Disconnect**: Use the physical back button and the UI disconnect swipe rapidly. Verify the app does not lock up or hang the JS thread.
- **Sequential Drop**: Fully connect to the hardware array, disconnect, and attempt to reconnect immediately. Verify we get a clean connection instead of a GATT error.

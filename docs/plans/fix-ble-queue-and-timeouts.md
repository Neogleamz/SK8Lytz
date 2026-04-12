# BLE Connection Stability Implementation

### Design Decisions & Rationale
We are implementing a globally shared Mutex Queue for BLE writes, adding explicit timeouts on all connection promises, and introducing an `isDisconnecting` latch to prevent the Android GATT 133 exception and related UI lockups. The Mutex ensures that no matter how rapidly the UI fires `writeToDevice` (e.g., during slider drags), operations execute strictly sequentially without overloading the BLE stack, while the hard timeouts protect the app state machine from indeterminate BLE Promise hangs.

## Proposed Changes

### `src/hooks/useBLE.ts`

#### [MODIFY] `useBLE.ts`
*   **Global Write Queue**:
    *   Initialize a simple Promise-based Mutex queue at the module level (outside the `useBLE` function) to sequence all writes across the app.
    *   Inside `writeToDevice`, wrap the `await device.writeCharacteristicWithoutResponseForService(...)` loop block in an enqueue function.
*   **Hard Connection Timeouts**:
    *   Update `connectToDevice` to pass a strict timeout: `const deviceConnection = await bleManager.connectToDevice(device.id, { timeout: 5000 });`
    *   Update `connectToDevices` (the group connection loop) to use the same timeout logic.
*   **The `isDisconnecting` Latch**:
    *   Introduce `const [isDisconnecting, setIsDisconnecting] = useState(false);` and export it in the hook return interface.
    *   In `disconnectFromDevice`, toggle `setIsDisconnecting(true)` at the start and back to `false` when teardown is finished.
    *   In `writeToDevice`, add an early return guard: `if (isDisconnecting) return;`.

# BLE Machine Audit Findings

## 1. Machine States
The machine consists of the following states:
*   `IDLE`
*   `SCANNING`
*   `CONNECTING`
*   `READY`
*   `DISCONNECTING`
*   `RECOVERING`

---

## 2. State Actions, Events, and Transitions

### State: `IDLE`
*   **Invoked Actors:** None
*   **Events Accepted & Transitions:**
    *   `SCAN_START`: Transitions to `SCANNING` and triggers actions `setSweeperId` and `logTransition` (from 'IDLE' to 'SCANNING').
    *   `CONNECT_REQUEST`: Transitions to `CONNECTING` and triggers actions `setTargetMacs` and `logTransition` (from 'IDLE' to 'CONNECTING').
    *   `DISCONNECT_REQUEST`: Transitions to `DISCONNECTING` and triggers action `logTransition` (from 'IDLE' to 'DISCONNECTING').
    *   `RECOVERY_START`: Transitions to `RECOVERING` and triggers actions `setGhostedMacs` and `logTransition` (from 'IDLE' to 'RECOVERING').
    *   `UPDATE_CONNECTED_DEVICES`: Does not transition (stays in `IDLE`). Triggers action `setConnectedDevices`.

### State: `SCANNING`
*   **Invoked Actors:** None (performs manual device scan start on entry and stop on exit/pause)
*   **Events Accepted & Transitions:**
    *   `SCAN_STOP`: Transitions to `IDLE` and triggers actions `clearSweeperId` and `logTransition` (from 'SCANNING' to 'IDLE').
    *   `CONNECT_REQUEST`: Transitions to `CONNECTING` and triggers actions `clearSweeperId`, `setTargetMacs`, and `logTransition` (from 'SCANNING' to 'CONNECTING').
    *   `SCAN_PAUSE`: Stays in `SCANNING`. Action: stops device scanning (`context.bleManager.stopDeviceScan()`).
    *   `SCAN_RESUME`: Stays in `SCANNING`. Action: starts device scanning (`context.bleManager.startDeviceScan(...)`).
    *   `DISCONNECT_REQUEST`: Transitions to `DISCONNECTING` and triggers actions `clearSweeperId` and `logTransition` (from 'SCANNING' to 'DISCONNECTING').
    *   `RECOVERY_START`: Transitions to `RECOVERING` and triggers actions `setGhostedMacs` and `logTransition` (from 'SCANNING' to 'RECOVERING').

### State: `CONNECTING`
*   **Invoked Actors:** `connectService`
*   **Events Accepted & Transitions:**
    *   `onDone` (on actor success): Transitions to `READY` and triggers actions `setConnectedDevices` and `logTransition` (from 'CONNECTING' to 'READY').
    *   `onError` (on actor error): Transitions to `IDLE` and triggers action `logTransition` (from 'CONNECTING' to 'IDLE', reason: 'connect_failed').
    *   `RECOVERY_START`: Transitions to `RECOVERING` and triggers actions `setGhostedMacs` and `logTransition` (from 'CONNECTING' to 'RECOVERING').
    *   `DISCONNECT_REQUEST`: Transitions to `DISCONNECTING` and triggers action `logTransition` (from 'CONNECTING' to 'DISCONNECTING').

### State: `READY`
*   **Invoked Actors:** `heartbeatService`
*   **Events Accepted & Transitions:**
    *   `HEARTBEAT_FAIL`: Transitions to `RECOVERING` and triggers actions `setGhostedMacs` and `logTransition` (from 'READY' to 'RECOVERING', reason: 'heartbeat_fail').
    *   `DISCONNECT_REQUEST`: Transitions to `DISCONNECTING` and triggers action `logTransition` (from 'READY' to 'DISCONNECTING').
    *   `RECOVERY_START`:
        *   *Guard:* `event.type === 'RECOVERY_START' && (event.ghostedMacs?.length ?? 0) >= 2`: Transitions to `CONNECTING` and triggers actions `setTargetMacs` and `logTransition` (from 'READY' to 'CONNECTING', reason: 'group_recovery').
        *   *Fallback (Default):* Transitions to `RECOVERING` and triggers actions `setGhostedMacs` and `logTransition` (from 'READY' to 'RECOVERING').
    *   `UPDATE_CONNECTED_DEVICES`:
        *   *Guard:* `event.type === 'UPDATE_CONNECTED_DEVICES' && event.devices.length === 0`: Transitions to `IDLE` and triggers actions `setConnectedDevices` and `logTransition` (from 'READY' to 'IDLE', reason: 'zero_devices').
        *   *Fallback (Default):* Stays in `READY`. Action: triggers `setConnectedDevices`.

### State: `DISCONNECTING`
*   **Invoked Actors:** None
*   **Events Accepted & Transitions:**
    *   `DISCONNECT_COMPLETE`: Transitions to `IDLE` and triggers actions `clearConnectedDevices`, `clearGhostedMacs`, and `logTransition` (from 'DISCONNECTING' to 'IDLE').

### State: `RECOVERING`
*   **Invoked Actors:** `recoveryService`
*   **Events Accepted & Transitions:**
    *   `RECOVERY_COMPLETE`: Transitions to `READY`. Action: updates the `connectedDevices` in context by merging the recovered devices into the existing connected devices list (filtering out duplicates by ID), calls `clearGhostedMacs`, and triggers `logTransition` (from 'RECOVERING' to 'READY').
    *   `CONNECT_REQUEST`: Transitions to `CONNECTING` and triggers actions `setTargetMacs` and `logTransition` (from 'RECOVERING' to 'CONNECTING').
    *   `RECOVERY_FAIL`: Transitions to `IDLE` and triggers actions `clearGhostedMacs` and `logTransition` (from 'RECOVERING' to 'IDLE').
    *   `DISCONNECT_REQUEST`: Transitions to `DISCONNECTING` and triggers action `logTransition` (from 'RECOVERING' to 'DISCONNECTING').

---

## 3. Heartbeat Invocation in READY
Yes, the `READY` state invokes `heartbeatService`.
It receives the following input object constructed from `context`:
```typescript
{
  bleManager: context.bleManager,
  connectedDevices: context.connectedDevices,
  adapterMap: context.adapterMapRef.current,
}
```

---

## 4. Connect Invocation in CONNECTING
Yes, the `CONNECTING` state invokes `connectService`.
It receives the following input object constructed from `context`:
```typescript
{
  bleManager: context.bleManager,
  targetMacs: context.targetMacs ?? [],
  connectedDevicesRef: { current: context.connectedDevices },
  adapterMapRef: context.adapterMapRef,
  mtuMapRef: context.mtuMapRef,
  disconnectListeners: context.disconnectListeners,
  blacklistedMacsRef: context.blacklistedMacsRef,
  handleOrganicDisconnect: context.handleOrganicDisconnect,
  handleNotification: context.handleNotification,
  enqueueWrite: context.enqueueWrite,
}
```

---

## 5. Recovery Invocation in RECOVERING
Yes, the `RECOVERING` state invokes `recoveryService`.
It receives the following input object constructed from `context`:
```typescript
{
  bleManager: context.bleManager,
  ghostedDeviceIds: context.ghostedDeviceIds ?? [],
  adapterMapRef: context.adapterMapRef,
  mtuMapRef: context.mtuMapRef,
  disconnectListeners: context.disconnectListeners,
  handleOrganicDisconnect: context.handleOrganicDisconnect,
  handleNotification: context.handleNotification,
}
```

---

## 6. RSSI and Interrogator Services
No. Neither `rssiService` nor `interrogatorService` is invoked anywhere inside `BleMachine.ts` or `BleMachine.types.ts`. As expected, they function at the React hook level rather than as actors inside the state machine.

---

## 7. Presence of `any` Casts
There are **no `any` casts** (such as `as any` or `<any>`) in either file.
There are, however, type definitions and parameters typed as `any` in callback signatures and type declarations:
*   `adapterMapRef: { current: Map<string, any> }` in `BleMachine.types.ts`
*   `handleOrganicDisconnect: (error: any, deviceId: string) => void` in `BleMachine.types.ts`
*   `handleNotification: (error: any, characteristic: any, deviceId: string) => void` in `BleMachine.types.ts`
*   `context: ({ input }: { input: any }) => ...` in `BleMachine.ts`
*   `const existingIds = newDevices.map((d: any) => d.id)` in `BleMachine.ts`

---

## 8. Machine Context Fields
The machine context (`BleMachineContext`) in `BleMachine.types.ts` contains all of the requested fields:
*   `bleManager` (present)
*   `scanCallback` (present)
*   `adapterMapRef` (present)
*   `mtuMapRef` (present)
*   `disconnectListeners` (present)
*   `blacklistedMacsRef` (present)
*   `handleOrganicDisconnect` (present)
*   `handleNotification` (present)
*   `enqueueWrite` (present)

No fields from this list are missing.

---

## 9. Handling of `SCAN_PAUSE` & `SCAN_RESUME`
Yes, `SCAN_PAUSE` and `SCAN_RESUME` are handled.
Only the **`SCANNING`** state accepts and handles these events:
*   `SCAN_PAUSE` stops scanning via `context.bleManager.stopDeviceScan()`
*   `SCAN_RESUME` starts scanning again via `context.bleManager.startDeviceScan(...)`

---

## 10. `FORCE_IDLE` Behavior
On `FORCE_IDLE`, the machine transitions to the `.IDLE` state and executes the following actions:
*   `clearSweeperId`
*   `clearGhostedMacs`
*   `logTransition` (from 'ANY' to 'IDLE', reason: 'forced')

It **does not** clear `connectedDevices`. The `clearConnectedDevices` action is intentionally omitted here to prevent resetting the device connection list on an activity reset, which avoids known screen flickers or blank blue screen issues on state resets. Devices are only cleared on `DISCONNECT_COMPLETE`.

# BleMachine Audit Findings

## 1. States in the Machine
The `BleMachine` defines the following states:
- `IDLE`
- `SCANNING`
- `RESTORING`
- `CONNECTING`
- `READY`
- `DISCONNECTING`
- `RECOVERING`

*(Note: There is a global transition `FORCE_IDLE` that targets `.IDLE` from any state)*

## 2. Actors, Events, and Transitions per State

### `IDLE`
- **Actors Invoked:** None
- **Events & Transitions:**
  - `SCAN_START` -> `SCANNING`
  - `CONNECT_REQUEST` -> `CONNECTING`
  - `DISCONNECT_REQUEST` -> `DISCONNECTING`
  - `RECOVERY_START` -> `RECOVERING`
  - `RESTORE_PERIPHERALS` -> `RESTORING`
  - `UPDATE_CONNECTED_DEVICES` -> internal transition (updates context via `setConnectedDevices`)

### `SCANNING`
- **Actors Invoked:** None (Uses `entry` and `exit` actions to start/stop device scan on `bleManager`)
- **Events & Transitions:**
  - `SCAN_STOP` -> `IDLE`
  - `CONNECT_REQUEST` -> `CONNECTING`
  - `SCAN_PAUSE` -> internal transition (calls `stopDeviceScan`)
  - `SCAN_RESUME` -> internal transition (calls `startDeviceScan`)
  - `DISCONNECT_REQUEST` -> `DISCONNECTING`
  - `RECOVERY_START` -> `RECOVERING`

### `RESTORING`
- **Actors Invoked:** None
- **Events & Transitions:**
  - `after 1000` (ms) -> `CONNECTING`

### `CONNECTING`
- **Actors Invoked:** `connectService`
- **Events & Transitions:**
  - `onDone` (of invoke) -> `READY`
  - `onError` (of invoke) -> `IDLE`
  - `RECOVERY_START` -> `RECOVERING`
  - `DISCONNECT_REQUEST` -> `DISCONNECTING`

### `READY`
- **Actors Invoked:** `heartbeatService`
- **Events & Transitions:**
  - `HEARTBEAT_FAIL` -> `RECOVERING`
  - `CONNECT_REQUEST` -> `CONNECTING`
  - `DISCONNECT_REQUEST` -> `DISCONNECTING`
  - `RECOVERY_START` -> `CONNECTING` (guarded: if `ghostedMacs >= 2`) OR `RECOVERING` (fallback)
  - `UPDATE_CONNECTED_DEVICES` -> `IDLE` (guarded: if `devices.length === 0`) OR internal transition (updates context)

### `DISCONNECTING`
- **Actors Invoked:** None
- **Events & Transitions:**
  - `DISCONNECT_COMPLETE` -> `IDLE`

### `RECOVERING`
- **Actors Invoked:** `recoveryService`
- **Events & Transitions:**
  - `RECOVERY_COMPLETE` -> `READY`
  - `CONNECT_REQUEST` -> `CONNECTING`
  - `RECOVERY_PERMANENTLY_FAILED` -> `IDLE`
  - `RECOVERY_FAIL` -> `IDLE`
  - `DISCONNECT_REQUEST` -> `DISCONNECTING`

## 3. Does READY invoke heartbeatService?
**Yes.** The `READY` state invokes `heartbeatService`.

## 4. Does CONNECTING invoke connectService?
**Yes.** The `CONNECTING` state invokes `connectService`.

## 5. Does RECOVERING invoke recoveryService?
**Yes.** The `RECOVERING` state invokes `recoveryService`.

## 6. Is rssiService or interrogatorService invoked anywhere in the machine?
**No.** Neither `rssiService` nor `interrogatorService` are invoked in the machine, nor are they defined in the setup `actors` object.

## 7. Any `any` casts?
**Yes.** In `BleMachine.ts`, there are `any` casts in the `setTargetMacs` action:
- `setTargetMacs: assign({ targetMacs: ({ event }: any) => {`
- `if (event.type === 'RESTORE_PERIPHERALS') return event.peripherals.map((p: any) => p.id);`

## 8. Does the context contain specific fields?
**Yes.** The `BleMachineContext` interface in `BleMachine.types.ts` contains all the requested fields:
- `bleManager`
- `scanCallback`
- `adapterMapRef`
- `mtuMapRef`
- `disconnectListeners`
- `blacklistedMacsRef`
- `handleOrganicDisconnect`
- `handleNotification`
- `enqueueWrite`

## 9. Is SCAN_PAUSE / SCAN_RESUME handled? Which states?
**Yes.** They are handled exclusively in the `SCANNING` state. `SCAN_PAUSE` calls `stopDeviceScan()` and `SCAN_RESUME` calls `startDeviceScan()`.

## 10. What happens on FORCE_IDLE? Does it clear connectedDevices?
On `FORCE_IDLE`, the machine transitions globally to the `.IDLE` state.
**No, it does not clear `connectedDevices`.** There is an explicit code comment stating that `clearConnectedDevices` is intentionally omitted here because `FORCE_IDLE` resets the activity gate (SCANNING/CONNECTING) but not the device connection list, and clearing it would cause bugs such as a "blank blue screen" and breaking the keepalive cache-hit path.

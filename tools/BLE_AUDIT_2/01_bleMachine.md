# BLE Machine Audit Findings

## Overview
This document logs the audit findings for the BLE XState machine (`BleMachine.ts` and `BleMachine.types.ts`) in the SK8Lytz project.

---

## 1. Machine States
The machine defines the following 6 states in `BleMachineState`:
1. `IDLE`
2. `SCANNING`
3. `CONNECTING`
4. `READY`
5. `DISCONNECTING`
6. `RECOVERING`

---

## 2. State-by-State Analysis (Actors, Events, and Transitions)

### 2.1. `IDLE`
* **Invoked Actors**: None.
* **Events & Transitions**:
  * `SCAN_START` ➡️ `SCANNING`
    * Actions: `setSweeperId`, `logTransition`
  * `CONNECT_REQUEST` ➡️ `CONNECTING`
    * Actions: `setTargetMacs`, `logTransition`
  * `DISCONNECT_REQUEST` ➡️ `DISCONNECTING`
    * Actions: `logTransition`
  * `RECOVERY_START` ➡️ `RECOVERING`
    * Actions: `setGhostedMacs`, `logTransition`
  * `UPDATE_CONNECTED_DEVICES` ➡️ Internal Transition (stays in `IDLE`)
    * Actions: `setConnectedDevices`

### 2.2. `SCANNING`
* **Invoked Actors**: None.
  * *Entry Action*: Starts scanning using `context.bleManager.startDeviceScan(...)`.
  * *Exit Action*: Stops scanning using `context.bleManager.stopDeviceScan()`.
* **Events & Transitions**:
  * `SCAN_STOP` ➡️ `IDLE`
    * Actions: `clearSweeperId`, `logTransition`
  * `CONNECT_REQUEST` ➡️ `CONNECTING`
    * Actions: `clearSweeperId`, `setTargetMacs`, `logTransition`
  * `SCAN_PAUSE` ➡️ Internal Transition
    * Actions: Stops device scan via `context.bleManager.stopDeviceScan()`
  * `SCAN_RESUME` ➡️ Internal Transition
    * Actions: Resumes device scan via `context.bleManager.startDeviceScan(...)`
  * `DISCONNECT_REQUEST` ➡️ `DISCONNECTING`
    * Actions: `clearSweeperId`, `logTransition`
  * `RECOVERY_START` ➡️ `RECOVERING`
    * Actions: `setGhostedMacs`, `logTransition`

### 2.3. `CONNECTING`
* **Invoked Actors**: `connectService` (ID: `connectService`).
  * *Input Map*:
    * `bleManager`: `context.bleManager`
    * `targetMacs`: `context.targetMacs`
    * `connectedDevicesRef`: `{ current: context.connectedDevices }`
    * `adapterMapRef`: `context.adapterMapRef`
    * `mtuMapRef`: `context.mtuMapRef`
    * `disconnectListeners`: `context.disconnectListeners`
    * `blacklistedMacsRef`: `context.blacklistedMacsRef`
    * `handleOrganicDisconnect`: `context.handleOrganicDisconnect`
    * `onOrganicDisconnect`: `context.onOrganicDisconnect`
    * `handleNotification`: `context.handleNotification`
    * `enqueueWrite`: `context.enqueueWrite`
* **Events & Transitions**:
  * `xstate.done.actor.connectService` (`onDone`) ➡️ `READY`
    * Actions: `setConnectedDevices`, `logTransition`
  * `xstate.error.actor.connectService` (`onError`) ➡️ `IDLE`
    * Actions: `logTransition` (reason: `connect_failed`)
  * `RECOVERY_START` ➡️ `RECOVERING`
    * Actions: `setGhostedMacs`, `logTransition`
  * `DISCONNECT_REQUEST` ➡️ `DISCONNECTING`
    * Actions: `logTransition`

### 2.4. `READY`
* **Invoked Actors**: `heartbeatService` (invoked anonymously in array format).
  * *Input Map*:
    * `bleManager`: `context.bleManager`
    * `connectedDevices`: `context.connectedDevices`
    * `adapterMap`: `context.adapterMapRef.current`
* **Events & Transitions**:
  * `HEARTBEAT_FAIL` ➡️ `RECOVERING`
    * Actions: `setGhostedMacs`, `logTransition` (reason: `heartbeat_fail`)
  * `CONNECT_REQUEST` ➡️ `CONNECTING`
    * Actions: `setTargetMacs`, `logTransition`
  * `DISCONNECT_REQUEST` ➡️ `DISCONNECTING`
    * Actions: `logTransition`
  * `RECOVERY_START` (Conditional Target):
    * Target 1: `CONNECTING` (if guard returns true: `event.ghostedMacs?.length >= 2`)
      * Actions: `setTargetMacs`, `logTransition` (reason: `group_recovery`)
    * Target 2: `RECOVERING` (default fallback)
      * Actions: `setGhostedMacs`, `logTransition`
  * `UPDATE_CONNECTED_DEVICES` (Conditional Target):
    * Target 1: `IDLE` (if guard returns true: `event.devices.length === 0`)
      * Actions: `setConnectedDevices`, `logTransition` (reason: `zero_devices`)
    * Target 2: Internal Transition (default fallback)
      * Actions: `setConnectedDevices`

### 2.5. `DISCONNECTING`
* **Invoked Actors**: None.
* **Events & Transitions**:
  * `DISCONNECT_COMPLETE` ➡️ `IDLE`
    * Actions: `clearConnectedDevices`, `clearGhostedMacs`, `logTransition`

### 2.6. `RECOVERING`
* **Invoked Actors**: `recoveryService` (invoked anonymously).
  * *Input Map*:
    * `bleManager`: `context.bleManager`
    * `ghostedDeviceIds`: `context.ghostedDeviceIds`
    * `adapterMapRef`: `context.adapterMapRef`
    * `mtuMapRef`: `context.mtuMapRef`
    * `disconnectListeners`: `context.disconnectListeners`
    * `handleOrganicDisconnect`: `context.handleOrganicDisconnect`
    * `onOrganicDisconnect`: `context.onOrganicDisconnect`
    * `handleNotification`: `context.handleNotification`
  * *Configuration*: `onDone` and `onError` are set to `undefined`.
* **Events & Transitions**:
  * `RECOVERY_COMPLETE` ➡️ `READY`
    * Actions: `assign({ connectedDevices: ... })`, `clearGhostedMacs`, `logTransition`
  * `CONNECT_REQUEST` ➡️ `CONNECTING`
    * Actions: `setTargetMacs`, `logTransition`
  * `RECOVERY_PERMANENTLY_FAILED` ➡️ `IDLE`
    * Actions: `setDeviceUnreachable`, `notifyUserDeviceFailed`, `clearGhostedMacs`, `logTransition` (reason: `permanent_fail`)
  * `RECOVERY_FAIL` ➡️ `IDLE`
    * Actions: `clearGhostedMacs`, `logTransition`
  * `DISCONNECT_REQUEST` ➡️ `DISCONNECTING`
    * Actions: `logTransition`

---

## 3. Global Events
* `FORCE_IDLE` ➡️ `.IDLE` (transition from any state back to IDLE)
  * Actions: `clearSweeperId`, `clearGhostedMacs`, `logTransition` (reason: `forced`)
  * *Note*: This intentionally does not clear `connectedDevices` to avoid clearing connection list state or breaking keepalive cache-hit paths.

---

## 4. Specific Audit Questionnaire Responses

### Q1: List every state in the machine (IDLE, SCANNING, CONNECTING, READY, DISCONNECTING, RECOVERING).
* **Response**: Yes, the states are: `IDLE`, `SCANNING`, `CONNECTING`, `READY`, `DISCONNECTING`, and `RECOVERING`.

### Q2: For each state: what actors are invoked? What events does it accept? What transitions exist?
* **Response**: Refer to Section 2 for a detailed state-by-state transition map, actors invoked, and action arrays.

### Q3: Does READY invoke heartbeatService?
* **Response**: **Yes**. `READY` invokes `heartbeatService` to monitor device connectivity.

### Q4: Does CONNECTING invoke connectService?
* **Response**: **Yes**. `CONNECTING` invokes `connectService` to handle the connection setup (MTU negotiation, adapter pairing, etc.).

### Q5: Does RECOVERING invoke recoveryService?
* **Response**: **Yes**. `RECOVERING` invokes `recoveryService` to handle connection recovery for ghosted devices.

### Q6: Is rssiService or interrogatorService invoked anywhere in the machine?
* **Response**: **No**. Neither `rssiService` nor `interrogatorService` is imported or invoked inside `BleMachine.ts`.

### Q7: Any `any` casts?
* **Response**: **Yes**. There is one `any` cast/type definition in the types file (`BleMachine.types.ts` line 18):
  * `handleOrganicDisconnect: (error: any, deviceId: string) => void;`
  There are no raw `as any` casts or `@ts-ignore` in `BleMachine.ts`, though `as` is used for XState context/event type definitions during machine setup.

### Q8: Does the context contain bleManager, scanCallback, adapterMapRef, mtuMapRef, disconnectListeners, blacklistedMacsRef, handleOrganicDisconnect, handleNotification, enqueueWrite?
* **Response**: **Yes**. All nine parameters are declared in `BleMachineContext` inside `BleMachine.types.ts` and passed appropriately into services.

### Q9: Is SCAN_PAUSE / SCAN_RESUME handled? Which states?
* **Response**: **Yes**. Both are handled inside the `SCANNING` state.
  * `SCAN_PAUSE` invokes `bleManager.stopDeviceScan()`.
  * `SCAN_RESUME` invokes `bleManager.startDeviceScan(...)` to resume.
  * Both are internal transitions that keep the state as `SCANNING`.

### Q10: What happens on FORCE_IDLE? Does it clear connectedDevices?
* **Response**: On `FORCE_IDLE`, the machine transitions to the `IDLE` state and runs `clearSweeperId`, `clearGhostedMacs`, and the `logTransition` action. It **does NOT** clear `connectedDevices`. This design prevents breaking UI components that rely on mock/existing connections and keeps keepalive cache-hit paths working.

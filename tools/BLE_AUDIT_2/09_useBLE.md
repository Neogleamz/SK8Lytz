# BLE Audit Report: useBLE Hook & Context (`09_useBLE.md`)

This document presents the semantic audit findings for `useBLE.ts` and `BLEContext.tsx`.

## 📌 Cited Files
- [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- [BLEContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/BLEContext.tsx)
- [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
- [BleMachine.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts)

---

## 🔍 Detailed Audit Answers

### 1. `useMachine` Input Fields Correctness
- **Verdict**: Correct.
- **Evidence**:
  - In [useBLE.ts:L176-202](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L176-L202), `useMachine(bleMachine, { input: { ... } })` passes all the required context properties.
  - In [BleMachine.ts:L12](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L12), the machine's expected input type is mapped via `types: { input: {} as Omit<BleMachineContext, 'connectedDevices' | 'ghostedDeviceIds'> }`.
  - Comparing the context properties in [BleMachine.types.ts:L5-27](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L5-L27) with the arguments passed, they align perfectly.

### 2. `scanCallback` Ref-Forwarding
- **Verdict**: Properly implemented via a mutable reference.
- **Evidence**:
  - [useBLE.ts:L175](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L175) defines `scanCallbackRef = useRef(...)`.
  - [useBLE.ts:L179](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L179) passes a wrapper calling `scanCallbackRef.current(...)` to the state machine.
  - [useBLE.ts:L418-421](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L418-L421) runs an effect updating `scanCallbackRef.current = scanner.scanCallback` whenever the scanner's callback changes. This successfully avoids re-instantiating the XState machine when standard scanner state changes.

### 3. Direct `bleManager` Start/Stop Scan Calls
- **Verdict**: None present in `useBLE.ts`.
- **Evidence**:
  - Scanning calls (`startDeviceScan` and `stopDeviceScan`) are delegated entirely to the `bleMachine` state transitions (scanning state entry/exit/resume/pause) inside [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) and the `useBLEScanner` hook. `useBLE.ts` does not invoke these functions directly on `bleManager`.

### 4. `connectToDevices` GATT Handling
- **Verdict**: Delegated to the State Machine.
- **Evidence**:
  - In [useBLE.ts:L464-467](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L464-L467), calling `connectToDevices` sends the `CONNECT_REQUEST` event: `bleSend({ type: 'CONNECT_REQUEST', targetMacs: devices.map(d => d.id) })`.
  - The physical connection logic and GATT configuration are offloaded to `connectService` in `BleMachine.ts`, ensuring serialized radio access and preventing stampeding-herd/GATT 133 conflicts.

### 5. `disconnectFromDevice` GATT Handling
- **Verdict**: Delegated to the State Machine.
- **Evidence**:
  - In [useBLE.ts:L517-519](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L517-L519), `disconnectFromDevice` dispatches `bleSend({ type: 'DISCONNECT_REQUEST' })`.
  - All teardowns and GATT resets are managed by the machine's transition to the `DISCONNECTING` state.

### 6. `forceDisconnect` GATT Handling
- **Verdict**: Delegated to the State Machine.
- **Evidence**:
  - In [useBLE.ts:L521-523](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L521-L523), `forceDisconnect` dispatches `bleSend({ type: 'FORCE_IDLE' })`.
  - This transitions the FSM immediately back to the `.IDLE` state (as defined in [BleMachine.ts:L285-294](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L285-L294)). Note that this activity gate reset purposely does not clear the connected device list directly in `useBLE.ts`.

### 7. `handleOrganicDisconnect` Machine Delegation
- **Verdict**: Handled via two separate machine properties.
- **Evidence**:
  - In [useBLE.ts:L191](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L191), `handleOrganicDisconnect` (logging-only) is passed to the machine inputs.
  - In [useBLE.ts:L194-198](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L194-L198), `onOrganicDisconnect` delegates the recovery trigger by sending `bleSend({ type: 'RECOVERY_START', ghostedMacs: [deviceId] })` to initiate the reconnect flow.

### 8. Sweeper Control Exposure
- **Verdict**: Exposed in the returned memo object.
- **Evidence**:
  - [useBLE.ts:L588-592](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L588-L592) exposes:
    - `startSweeper: scanner.startSweeper`
    - `stopSweeper: scanner.stopScanner`
    - `burstScan: scanner.burstScan`
    - `isSweeperActive: scanner.isSweeperActive`
    - `batteryTier: scanner.batteryTier`

### 9. `BluetoothLowEnergyApi` Interface Provision
- **Verdict**: Acts as the strict contract hook provider.
- **Evidence**:
  - Defined in [useBLE.ts:L56-111](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L56-L111).
  - Used in [BLEContext.tsx:L5](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/BLEContext.tsx#L5) for `createContext<BluetoothLowEnergyApi | null>(null)` and [BLEContext.tsx:L18](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/BLEContext.tsx#L18) for the shared hook `useSharedBLE()`.

### 10. `scanForPeripherals` Delegation
- **Verdict**: Delegated based on Sweeper state.
- **Evidence**:
  - In [useBLE.ts:L599-609](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L599-L609):
    - If `scanner.isSweeperActive` is true, it triggers a fire-and-forget `scanner.burstScan` (either 10s or 5s).
    - If inactive, it delegates directly to `scanner.scanForPeripherals(options)`.

### 11. TypeScript `any` Casts (excluding `Platform.OS === 'web'`)
- **Verdict**: 0 (None).
- **Evidence**:
  - Checked `useBLE.ts` for type override patterns (`as any` and `@ts-ignore`). No such casts or bypasses are present in `useBLE.ts`. Type overrides only exist as standard configuration signatures (e.g. `config: any` parameters in probed callbacks).

### 12. `useBLE.ts` Line Count
- **Verdict**: 634 lines.
- **Evidence**:
  - Verified that the file ends at line 634 with an empty line following `// blast radius bypass` comments.

# useBLE Audit

1. **useMachine input fields correctness**: Yes, inputs are correctly configured and passed to `bleMachine` including callbacks, refs, and maps.
2. **scanCallback ref-forwarding**: Yes, `scanCallbackRef` is passed to the machine input and subsequently forwarded to `scanner.scanCallback`.
3. **Direct bleManager start/stop scan calls**: No, all direct `bleManager` scanning calls are removed; scanning is delegated to `useBLEScanner`.
4. **connectToDevices GATT handling**: Delegated to XState machine via `bleSend({ type: 'CONNECT_REQUEST' })`.
5. **disconnectFromDevice GATT handling**: Delegated to XState machine via `bleSend({ type: 'DISCONNECT_REQUEST' })`.
6. **forceDisconnect GATT handling**: Delegated to XState machine via `bleSend({ type: 'FORCE_IDLE' })`.
7. **handleOrganicDisconnect machine delegation**: Yes, hooked up to the machine which handles triggering `RECOVERY_START` via `onOrganicDisconnect` callback.
8. **Sweeper control exposure**: Yes, `startSweeper`, `stopSweeper`, `burstScan`, and `isSweeperActive` are correctly exposed.
9. **BluetoothLowEnergyApi interface provision**: Yes, `BluetoothLowEnergyApi` is comprehensively defined and returned by the hook.
10. **scanForPeripherals delegation**: Yes, delegates conditionally to `scanner.burstScan` if the sweeper is active, else to `scanner.scanForPeripherals`.
11. **`any` casts (non Platform.OS='web')**: Yes, there is one `any` cast present: `const bleSendRef = useRef<any>(null); // MIGRATION-SHIM`.
12. **useBLE.ts line count**: 639 lines.

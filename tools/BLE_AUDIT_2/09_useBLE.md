# 09_useBLE.ts Audit Findings

## 1. instantiation of `useMachine(bleMachine, ...)`
`useBLE.ts` instantiates `useMachine(bleMachine, ...)` correctly, providing the following input fields:
* `bleManager`
* `scanCallback`
* `scanMode`
* `scanServiceUUIDs`
* `adapterMapRef`
* `mtuMapRef`
* `disconnectListeners`
* `blacklistedMacsRef`
* `handleOrganicDisconnect`
* `handleNotification`
* `enqueueWrite`

Source snippet (lines 171-185):
```typescript
  const [bleSnapshot, bleSend, bleActorRef] = useMachine(bleMachine, {
    input: {
      bleManager,
      scanCallback: (error: BleError | null, device: Device | null) => scanCallbackRef.current(error, device),
      scanMode: 1,
      scanServiceUUIDs: [ZENGGE_SERVICE_UUID, BANLANX_SERVICE_UUID],
      adapterMapRef,
      mtuMapRef,
      disconnectListeners,
      blacklistedMacsRef,
      handleOrganicDisconnect: (error: any, deviceId: string) => handleOrganicDisconnectRef.current(error, deviceId),
      handleNotification: (error: any, characteristic: any, deviceId: string) => handleNotificationRef.current(error, characteristic, deviceId),
      enqueueWrite,
    }
  });
```

---

## 2. `scanCallback` Ref-Forwarding Pattern
Yes, `scanCallback` is passed as a ref-forwarded function rather than a direct closure to prevent stale closure issues.

**Cited Pattern:**
```typescript
scanCallback: (error: BleError | null, device: Device | null) => scanCallbackRef.current(error, device)
```
Where `scanCallbackRef` is defined as:
```typescript
const scanCallbackRef = useRef<(error: BleError | null, device: Device | null) => void>(() => {});
```
And dynamically wired in `useEffect` (lines 388-391):
```typescript
  useEffect(() => {
    pendingRegistrationsSetterRef.current = scanner.setPendingRegistrations;
    scanCallbackRef.current = scanner.scanCallback;
  }, [scanner.setPendingRegistrations, scanner.scanCallback]);
```

---

## 3. Direct calls to `startDeviceScan()` or `stopDeviceScan()`
**No.** `useBLE.ts` does not call `bleManager.startDeviceScan()` or `bleManager.stopDeviceScan()` directly anywhere in the file. These interactions are entirely managed by the state machine and the `useBLEScanner` hook.

---

## 4. `connectToDevices` Behavior
`connectToDevices` does **not** perform any GATT work directly. It only dispatches the `CONNECT_REQUEST` event containing the target device MAC addresses to the state machine.

Source snippet (lines 433-436):
```typescript
  const connectToDevices = useCallback(async (devices: Device[]) => {
    if (devices.length === 0) return;
    bleSend({ type: 'CONNECT_REQUEST', targetMacs: devices.map(d => d.id) });
  }, [bleSend]);
```

---

## 5. `disconnectFromDevice` Behavior
`disconnectFromDevice` does **not** perform direct GATT teardown. It only dispatches the `DISCONNECT_REQUEST` event to the machine.

Source snippet (lines 486-488):
```typescript
  const disconnectFromDevice = useCallback(() => {
    bleSend({ type: 'DISCONNECT_REQUEST' });
  }, [bleSend]);
```

---

## 6. `forceDisconnect` Behavior
`forceDisconnect` does **not** call GATT teardown directly. It only sends the `FORCE_IDLE` event to the state machine.

Source snippet (lines 490-492):
```typescript
  const forceDisconnect = useCallback(() => {
    bleSend({ type: 'FORCE_IDLE' });
  }, [bleSend]);
```

---

## 7. `handleOrganicDisconnect` Mechanism
`handleOrganicDisconnect` logs the warning and telemetry, but does not send a machine event or call GATT methods within its block. The XState machine intercepts the disconnect organically and manages the recovery lifecycle (`RECOVERY_START`) internally.

Source snippet (lines 365-370):
```typescript
  const handleOrganicDisconnect = (error: any, deviceId: string) => {
    AppLogger.warn(`[BLE] Organic disconnect/dropout for ${deviceId}`);
    AppLogger.log('DEVICE_DISCONNECTED', { id: deviceId, reason: 'dropout', error: error instanceof Error ? error.message : String(error) });
    // The machine handles this organically via handleOrganicDisconnect callback in bleMachine input.
    // The machine handles RECOVERY_START internally for organic drops.
  };
```

---

## 8. Exposure of Sweeper and Scan Controls
Yes, `useBLE` exposes the following scanner functions from `useBLEScanner`:
* `startSweeper` mapped to `scanner.startSweeper`
* `stopSweeper` mapped to `scanner.stopScanner`
* `burstScan` mapped to `scanner.burstScan`
* `isSweeperActive` mapped to `scanner.isSweeperActive`

---

## 9. `BLEContext.tsx` Integration
`BLEContext.tsx` provides the full `BluetoothLowEnergyApi` interface:
```typescript
export const BLEContext = createContext<BluetoothLowEnergyApi | null>(null);
```
`useBLE` is wired into `BLEProvider` by calling the hook with the list of registered device MAC addresses (obtained from `useRegistration()`) and providing the resulting object directly to the React Context:
```typescript
export const BLEProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { registeredDevices } = useRegistration();
  const registeredMacs = useMemo(
    () => registeredDevices.map(d => d.device_mac || ''),
    [registeredDevices]
  );
  const ble = useBLE(registeredMacs);

  return <BLEContext.Provider value={ble}>{children}</BLEContext.Provider>;
};
```

---

## 10. `scanForPeripherals` Delegation
Yes, `scanForPeripherals` is present in the returned API and delegates correctly depending on the sweeper state:
* If the sweeper is active (`scanner.isSweeperActive` is true), it calls `scanner.burstScan` as a fire-and-forget call to avoid locking `bleState` to `SCANNING`.
* Otherwise, it delegates directly to `scanner.scanForPeripherals(options)`.

Source snippet (lines 567-577):
```typescript
    scanForPeripherals: (options?: { keepAlive?: boolean; disableProbing?: boolean }) => {
      if (scanner.isSweeperActive) {
        // FIX: Fire-and-forget — do NOT await burstScan.
        // Awaiting it locked bleState='SCANNING' for the full 5s burst duration,
        // blocking the Wizard from ever showing discovered devices.
        // burstScan handles its own timer + revert internally.
        scanner.burstScan(options?.keepAlive ? 10000 : 5000);
      } else {
        scanner.scanForPeripherals(options);
      }
    },
```

---

## 11. Type Casting and `any` Usage
There are **no** type-bypassing casts (e.g., `as any`) or `@ts-ignore` comments in `useBLE.ts`.

However, the `any` type is used for:
* Parameters in callback registrations (e.g., `error: any`, `characteristic: any`, `config: any`, `state: any`).
* Lazy-loaded/conditional class refs (e.g., `let BleManager: any;` and `let State: any;` on lines 45-46, which are safely instantiated inside a web platform guard).

---

## 12. Line Count
The total line count of `useBLE.ts` is **602 lines** (slightly over the 600-line threshold).

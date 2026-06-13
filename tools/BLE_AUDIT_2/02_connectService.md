# BLE Audit Findings: ConnectService

**File Path:** `src/services/ble/ConnectService.ts`  
**Auditor:** 🕵️ Scout — Reyes  
**Date:** 2026-06-13  

---

## Findings Summary & Questionnaire Answers

### 1. What XState actor type does it use?
`ConnectService.ts` defines `connectService` as a promise actor using the `fromPromise` creator from `xstate`:
```typescript
export const connectService = fromPromise<
  { devices: Device[] },
  ConnectServiceInput
>(async ({ input, signal }) => { ... })
```

### 2. Does it call bleManager.connectToDevice() or bleManager.connectToDeviceById()?
It calls `bleManager.connectToDevice(mac, attempt > 1 ? { refreshGatt: 'OnConnected' } : undefined)` (lines 157-160). It does not call `connectToDeviceById()`.

### 3. Group connect handling? Sequential or parallel?
Connection is handled **sequentially** for the requested devices.
- It iterates through the target MACs via a sequential `for (const mac of targetMacs)` loop (line 134).
- Handshaking is also done sequentially on the successfully connected devices in a sequential `for (const conn of rawConns)` loop (line 293).

### 4. Service/Characteristic discovery logic?
The service does not call service/characteristic discovery methods directly. Instead, it delegates this process to `createGattSession` from the session factory (line 197):
```typescript
const { adapter } = await createGattSession(bleManager, conn.id, {
  timeout: 6000,
  retries: 1,
  context: 'handshakeDevice',
  manufacturerData: conn.manufacturerData ?? undefined,
  signal,
});
```

### 5. GATT notification registration method?
It registers for notifications using the device's `monitorCharacteristicForService` method (lines 242-246):
```typescript
conn.monitorCharacteristicForService(
  adapter.serviceUUID,
  adapter.notifyCharacteristicUUID,
  (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
);
```

### 6. Protocol adapter mapping (adapterMapRef)?
When a device is successfully connected and handshaked, the adapter returned from `createGattSession` is stored in the referenced Map (line 232):
```typescript
adapterMapRef.current.set(conn.id, adapter);
```

### 7. MTU Negotiation (mtuMapRef)?
MTU Negotiation logic is platform-dependent:
- **iOS:** Directly reads the MTU from the device object:
  ```typescript
  mtuMapRef.current.set(conn.id, conn.mtu > 23 ? conn.mtu : 186);
  ```
- **Android:** Executes a retry loop (up to 2 attempts) to request an MTU of `512` (lines 207-220):
  ```typescript
  let negotiatedMtu = 23;
  for (let mtuAttempt = 1; mtuAttempt <= 2; mtuAttempt++) {
    try {
      const negotiated = await conn.requestMTU(512);
      negotiatedMtu = negotiated.mtu;
      if (negotiatedMtu > 23) break;
      // Delay and retry on glitch (23) or error
    } catch { ... }
  }
  mtuMapRef.current.set(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
  ```

### 8. Success transmission payload?
Upon successful connection/negotiation, the service fetches handshake payloads from the protocol adapter and enqueues them via `enqueueWrite` (lines 248-265):
```typescript
const handshake = adapter.getHandshakePayloads();
for (let i = 0; i < handshake.packets.length; i++) {
  const b64 = Buffer.from(handshake.packets[i]).toString('base64');
  const writeOp = async () => {
    await conn.writeCharacteristicWithoutResponseForService(
      adapter.serviceUUID, adapter.writeCharacteristicUUID, b64
    );
    return true;
  };
  await enqueueWrite('critical', writeOp);
  if (i < handshake.packets.length - 1 && handshake.interPacketDelayMs > 0) {
    await enqueueDelay('critical', handshake.interPacketDelayMs);
  }
}
```

### 9. Failure/cleanup flow?
- **Stale Device Flush:** Prior to connection, devices currently connected but not requested in `targetMacs` are flushed (lines 89-107):
  - Any active disconnect listeners are removed/deleted.
  - Connection is cancelled: `await bleManager.cancelDeviceConnection(stale.id)`.
  - A settle delay of `BLE_TIMING.STALE_FLUSH_SETTLE_MS` is applied.
- **Connection Failures:** Catches errors during individual device connections. On transient errors (GATT 133, timeouts, etc.), it retries up to 3 times with exponential backoff and calls `bleManager.cancelDeviceConnection(mac)` before the next attempt.
- **Handshake Failures:** If a device's handshake fails, it logs a warning/error and returns `null`, omitting it from the active session.
- **Termination/Abort:** If `signal.aborted` is detected at checkpoints, `Error('connect_aborted')` is thrown.
- **All Failures:** If zero connections succeed and target MACs were provided, it throws `Error('all_connections_failed')`.

### 10. Any `any` casts?
Yes, the file uses `any` in callback parameters:
- Line 27: `handleOrganicDisconnect: (error: any, deviceId: string) => void;`
- Line 236: `bleManager.onDeviceDisconnected(conn.id, (error: any) => {`
- Line 245: `(error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)`
There are no explicit Type Assertion casts (e.g. `as any`), but parameter type annotations are set to `any`.

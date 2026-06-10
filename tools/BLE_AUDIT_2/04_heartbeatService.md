# BLE Audit: HeartbeatService

This document contains the read-only audit findings for the `HeartbeatService.ts` file in the SK8Lytz codebase.

---

## Code Reference
- **File:** [HeartbeatService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)

---

## 🔍 Audit Answers

### 1. What actor type?
The service is implemented as an **XState callback actor** using `fromCallback` from the `'xstate'` package. It runs indefinitely upon activation, dispatching periodic heartbeats.

```typescript
export const heartbeatService = fromCallback<any, HeartbeatServiceInput>(({ input, sendBack }) => {
```

---

### 2. What is the heartbeat interval?
The heartbeat interval is **45,000 milliseconds (45 seconds)**. It is configured via the file-level constant `HEARTBEAT_INTERVAL_MS`:

```typescript
const HEARTBEAT_INTERVAL_MS = 45_000;
```

---

### 3. What payload does it send to the device? Which opcode?
- **Zengge/MagicHome Adapter Devices:**
  - It builds a settings query payload by calling the adapter's `buildQuerySettings(false)` method.
  - The inner payload returned by `queryHardwareSettings(false)` is:
    ```
    [0x63, 0x12, 0x21, 0x0F, checksum]
    ```
    Where `checksum` is `0xA5` (the simple sum `0x63 + 0x12 + 0x21 + 0x0F = 165 = 0xA5`).
  - After being wrapped with the V2 transport packet framing `wrapCommand`, the sent bytes are:
    ```
    [0x00, seq & 0xFF, 0x80, 0x00, 0x00, 0x05, 0x06, 0x0b, 0x63, 0x12, 0x21, 0x0F, 0xA5]
    ```
    Where `seq` represents the independent sequence number of the adapter.
  - **Opcode:** The primary opcode is **`0x63`** (IC Config Query / EEPROM read).
- **BanlanX / Unknown Fallback Devices:**
  - No write payload or opcode is sent. Instead, the service uses `bleManager.readRSSIForDevice(mac)` as a liveness probe.

---

### 4. How does it detect a failed heartbeat? Timeout? GATT error?
The heartbeat loop evaluates each connected device in a nested `try/catch` block:
1. If the connection fails or drops, calling `enqueueWrite` (which executes `writeCharacteristicWithoutResponseForDevice`) or `readRSSIForDevice` will throw a **GATT or BLE error** (propagated by the underlying `react-native-ble-plx` manager).
2. The `catch (err: unknown)` block catches this error, logs a warning via `AppLogger.warn`, cancels the stale GATT connection cleanly using `bleManager.cancelDeviceConnection(mac)`, and dispatches a failure event back to the machine.
3. No custom JS-level `setTimeout` based liveness timer is implemented inside the service itself; it depends entirely on the GATT/BLE library error handling.

---

### 5. What event does it send to the machine on failure?
On failure, it dispatches the following event back to the parent XState machine:
- **Event:** `HEARTBEAT_FAIL`
- **Payload:** `{ type: 'HEARTBEAT_FAIL', deviceId: mac }` (where `mac` is the MAC address of the failed device)

```typescript
sendBack({ type: 'HEARTBEAT_FAIL', deviceId: mac });
```

---

### 6. Does it use enqueueWrite or call bleManager.writeCharacteristic... directly?
- **Zengge Devices:** It uses `enqueueWrite` with priority `'normal'`. The actual write command `bleManager.writeCharacteristicWithoutResponseForDevice` is wrapped in an async callback passed to the queue:
  ```typescript
  await enqueueWrite('normal', async () => {
    await bleManager.writeCharacteristicWithoutResponseForDevice(
      mac,
      adapter.serviceUUID,
      adapter.writeCharacteristicUUID,
      b64,
    );
    return true;
  });
  ```
- **Fallback (BanlanX/Unknown):** It calls `bleManager.readRSSIForDevice(mac)` **directly** without going through the `enqueueWrite` queue.

---

### 7. Does it clean up (clear timers) when the machine exits READY state?
Yes. The `fromCallback` generator returns a cleanup function that clears the periodic timer:
```typescript
return () => clearInterval(interval);
```
This is automatically run by XState when the machine exits the state hosting the service.

---

### 8. Does it handle multiple connected devices simultaneously?
Yes. It loops through the `connectedDevices` array sequentially inside the interval callback:
```typescript
for (const device of connectedDevices) {
  const mac = device.id;
  // ... check liveness ...
}
```

---

### 9. Any `any` casts? List them.
The service uses the `any` type in two places (exclusively for parameter definitions, no inline `as any` or `@ts-ignore` bypasses are used):
1. **Type Parameter in `fromCallback`:**
   ```typescript
   export const heartbeatService = fromCallback<any, HeartbeatServiceInput>(...)
   ```
2. **Interface parameter `bleManager`:**
   ```typescript
   export interface HeartbeatServiceInput {
     bleManager: any;
     ...
   }
   ```

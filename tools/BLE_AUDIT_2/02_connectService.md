# BLE Audit Findings: ConnectService

This document contains the read-only audit findings for `src/services/ble/ConnectService.ts`.

---

### 1. What XState actor type does it use? (fromPromise, fromCallback, or custom?)
It uses the **`fromPromise`** actor creator from XState to encapsulate the asynchronous connection logic.
```typescript
export const connectService = fromPromise<
  { devices: Device[] },
  ConnectServiceInput
>(async ({ input, signal }) => {
```

---

### 2. Does it call bleManager.connectToDevice() or bleManager.connectToDeviceById()? Quote the exact line.
It calls **`bleManager.connectToDevice()`**.
* **Exact Lines (132-135):**
```typescript
             conn = await bleManager.connectToDevice(
              mac,
              attempt > 1 ? { refreshGatt: 'OnConnected' } : undefined,
            );
```

---

### 3. How does it handle multiple devices (group connect)? Sequential or parallel?
It handles multiple devices **sequentially** using a `for...of` loop that awaits each connection and handshake step before proceeding to the next device.
* The connection phase loops over `targetMacs` sequentially (Lines 114-162).
* The handshake phase loops over `rawConns` sequentially (Lines 263-266).

---

### 4. Does it call bleManager.discoverAllServicesAndCharacteristicsForDevice() after connect?
No, it does not call `bleManager.discoverAllServicesAndCharacteristicsForDevice()` or `conn.discoverAllServicesAndCharacteristics()` directly in `ConnectService.ts`. 

Instead, it delegates this step to the centralized GATT session factory:
* **ConnectService.ts (Lines 172-178):**
```typescript
        const { adapter } = await createGattSession(bleManager, conn.id, {
          timeout: 6000,
          retries: 1,
          context: 'handshakeDevice',
          manufacturerData: conn.manufacturerData ?? undefined,
          signal,
        });
```
* **BleSessionFactory.ts (Line 152):**
```typescript
  await conn.discoverAllServicesAndCharacteristics();
```

---

### 5. Does it register GATT notifications? Via which method? For which characteristics?
Yes.
* **Method:** `conn.monitorCharacteristicForService()`
* **Characteristics:** `adapter.notifyCharacteristicUUID` under service `adapter.serviceUUID`
* **Exact Lines (212-216):**
```typescript
        conn.monitorCharacteristicForService(
          adapter.serviceUUID,
          adapter.notifyCharacteristicUUID,
          (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
        );
```

---

### 6. Does it map the protocol adapter (adapterMapRef)? When?
Yes. It maps the protocol adapter by storing it in `adapterMapRef.current` using the device ID as the key.
* **When:** It maps the adapter during the `handshakeDevice` flow, right after resolving the adapter and negotiating the MTU, but prior to performing the handshake write payloads.
* **Exact Line (205):**
```typescript
        adapterMapRef.current.set(conn.id, adapter);
```

---

### 7. Does it negotiate MTU (mtuMapRef)? What's the target MTU?
Yes, on Android.
* **Target MTU:** **`512`**
* **Fallback MTU:** **`186`** (used if the negotiated MTU is `<= 23` or if the request fails).
* **Exact Lines (180-197):**
```typescript
        if (Platform.OS === 'android') {
          let negotiatedMtu = 23;
          for (let mtuAttempt = 1; mtuAttempt <= 2; mtuAttempt++) {
            if (signal.aborted) throw new Error('connect_aborted');
            try {
              const negotiated = await conn.requestMTU(512);
              negotiatedMtu = negotiated.mtu;
              if (negotiatedMtu > 23) break;
              AppLogger.warn(`[BLE] MTU glitch (23) for ${conn.id}. Retrying...`);
              await new Promise(res => setTimeout(res, BLE_TIMING.MTU_RETRY_SETTLE_MS));
            } catch {
              await new Promise(res => setTimeout(res, BLE_TIMING.MTU_RETRY_SETTLE_MS));
            }
          }
          mtuMapRef.current.set(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
        } else {
          mtuMapRef.current.set(conn.id, conn.mtu > 23 ? conn.mtu : 186);
        }
```

---

### 8. What does it send on success? (CONNECT_SUCCESS with devices array?)
Since it is a `fromPromise` actor type, it does not dispatch events directly. Instead, on successful completion, it resolves the promise by returning an object containing the combined array of retained and freshly connected devices.
* **Returned Output (Line 281):**
```typescript
    return { devices: finalGroup };
```

---

### 9. What does it do on failure? Does it clean up GATT sessions before failing?
* **On individual device failure:** In the sequential connection/handshake loops, errors are caught within try-catch blocks, logged, and the device is omitted from the resulting connection list (returning `null`).
* **On entire process failure:** If no devices succeed in connecting (when target devices were requested) or if an outer exception is caught, it logs the exception via `AppLogger.error()` and throws the error (e.g. `'all_connections_failed'`, `'connect_aborted'`).
* **GATT session cleanup:** It **does not** clean up GATT sessions or call cancel connection on the main failure pathways (such as when throwing `all_connections_failed` or `connect_aborted`). The only cancellation calls are restricted to the transient connection retry error path and the stale device flush path.

---

### 10. Does it call cancelDeviceConnection anywhere on error paths?
Yes, in two specific scenarios:
1. **Stale Device Flush (Line 83):** When removing previously connected devices that are no longer requested in `targetMacs`.
```typescript
          await bleManager.cancelDeviceConnection(stale.id);
```
2. **Transient Connection Retries (Line 149):** Inside the connection retry loop when encountering a transient error (such as GATT 133 or connection timeouts) before retrying the connection attempt.
```typescript
            await bleManager.cancelDeviceConnection(mac).catch(() => {});
```

---

### 11. Any any casts? List them.
There are no `as any` casts or `@ts-ignore` comments in `ConnectService.ts`. 

However, there are:
* **`any` type annotations in parameter signatures:**
  * `error: any` (Line 22, 23, 209, 215)
  * `characteristic: any` (Line 23, 215)
* **Specific type casts using `as`:**
  * `as unknown as Device` (Line 105) for generating mock devices in DEMO mode:
    ```typescript
    } as unknown as Device));
    ```
  * `as Device` (Line 129) for an empty object fallback when checking connected devices:
    ```typescript
    conn = isConnected ? (await bleManager.connectedDevices([mac]))[0] || ({} as Device) : null;
    ```

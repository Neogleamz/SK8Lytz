# ConnectService.ts Audit

1. **What XState actor type does it use?**
   It uses the `fromPromise` actor logic creator.

2. **Does it call bleManager.connectToDevice() or bleManager.connectToDeviceById()?**
   It calls `bleManager.connectToDevice(mac, ...)`.

3. **Group connect handling? Sequential or parallel?**
   Sequential. It iterates through the target MACs using a `for...of` loop, awaiting the connection for each before moving to the next. The subsequent handshaking is also performed sequentially in a `for...of` loop.

4. **Service/Characteristic discovery logic?**
   Discovery is delegated to `createGattSession()`, which returns the appropriate protocol adapter.

5. **GATT notification registration method?**
   It uses `conn.monitorCharacteristicForService(adapter.serviceUUID, adapter.notifyCharacteristicUUID, ...)` passing the adapter's UUIDs and the `handleNotification` callback.

6. **Protocol adapter mapping (adapterMapRef)?**
   The resolved `adapter` returned by `createGattSession` is stored in the map via `adapterMapRef.current.set(conn.id, adapter)`.

7. **MTU Negotiation (mtuMapRef)?**
   On Android, it attempts `conn.requestMTU(512)` with up to 1 retry if it glitches and returns 23. It falls back to `186` if the resulting MTU is still not > 23. On iOS, it checks `conn.mtu` and falls back to `186` if it's not > 23. The result is stored in `mtuMapRef`.

8. **Success transmission payload?**
   It retrieves the handshake payloads via `adapter.getHandshakePayloads()` and writes each packet sequentially using `conn.writeCharacteristicWithoutResponseForService()`, respecting any `interPacketDelayMs` with `enqueueDelay`.

9. **Failure/cleanup flow?**
   - Stale connections are flushed with `cancelDeviceConnection` and their disconnect listeners are removed.
   - Connection attempts feature a 3-try retry loop with a jittered backoff for transient errors (like GATT 133). 
   - A disconnect listener is registered on successful connection which triggers both a legacy `handleOrganicDisconnect` and the XState `onOrganicDisconnect` to start recovery.
   - If the handshake fails, the individual device connection returns `null`. If all requested devices fail, it throws `all_connections_failed`.

10. **Any `any` casts?**
    Yes, two explicit `any` typings were found in the callbacks:
    - `(error: any)` in the `onDeviceDisconnected` callback.
    - `(error: any, characteristic: any)` in the `monitorCharacteristicForService` callback.

# Recovery Service Audit
1. **What actor type?**
   XState `fromCallback` actor (`fromCallback<any, RecoveryInput>`).

2. **Recovery attempts count and backoff delays?**
   Phase 1 & 2 loop limits max attempts to 5 (`MAX_RECOVERY_ATTEMPTS = 5` via `hasExceededMaxRecovery(attempts)` check overrides `PHASE_1_MAX_ATTEMPTS` and `PHASE_2_MAX_ATTEMPTS`). Backoff is exponential: `1500 * (1.5 ^ attempts)` capped at `30_000ms` for Phase 1. Phase 3 allows up to 120 attempts with a 5000ms fixed polling interval.

3. **JitteredDelay vs fixed?**
   Jitter is applied. Phase 1 adds random jitter (`Math.random() * exponential * 0.3`). Phase 2 backoff adds random jitter (`20_000 + Math.random() * 1500`). Phase 3 uses a fixed `5000ms` poll interval.

4. **Reconnect mechanism (bleManager or event)?**
   Uses `bleManager` explicitly via `createGattSession(bleManager, deviceId, ...)` for active reconnection in Phase 1 & 2, and polls `getSweepedDevice?.(deviceId)` before attempting `createGattSession` in Phase 3.

5. **Exhaustion event?**
   Sends `{ type: 'RECOVERY_PERMANENTLY_FAILED', deviceId }` if attempts exceed max. Sends `{ type: 'RECOVERY_FAIL' }` on standard failure where attempts haven't exceeded maximum limits.

6. **Success event?**
   Sends `{ type: 'RECOVERY_COMPLETE', devices: [reconnectedDevice] }`.

7. **GATT re-registration logic?**
   Yes. It negotiates MTU for Android, re-establishes disconnect listeners (`bleManager.onDeviceDisconnected`), reconnects notification monitors (`conn.monitorCharacteristicForService`), and dispatches a setup ping write characteristic.

8. **Protocol adapter re-mapping?**
   Yes. Resolves new adapter and re-maps it via `adapterMapRef.current.set(conn.id, recoveryAdapter);`, and appropriately updates MTU in `mtuMapRef`.

9. **Write queue clearing?**
   Yes. Calls `clearWriteQueue();` upon start to drop stale pre-disconnect writes.

10. **Any `any` casts?**
    No explicit `as any` casts. There are `any` usage in type signatures (e.g. `Map<string, any>`, `fromCallback<any, ...>`) and some unknown object casting like `(error as {errorCode: unknown})`, but it avoids direct `as any` casting.

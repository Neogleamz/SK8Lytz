# BLE Audit Findings: RecoveryService

This document contains the audit findings for `RecoveryService.ts`.

## 1. Actor Type
The `recoveryService` is defined as a **fromCallback** actor type from XState.
```typescript
import { fromCallback } from 'xstate';
...
export const recoveryService = fromCallback<any, RecoveryInput>(({ input, sendBack }) => {
```

## 2. Recovery Attempts & Backoff Delays
The service utilizes a phased recovery mechanism with different backoff strategies:

*   **Phase 1 & 2 GATT Hammering**:
    *   **Max attempts**: The loop runs while `attempts <= PHASE_2_MAX_ATTEMPTS` (which is `35`), meaning a maximum of **36 attempts** (0 through 35).
    *   **Phase 1 backoff** (for `attempts <= PHASE_1_MAX_ATTEMPTS`, which is `12`): Uses `getRecoveryBackoffMs(attempts)`, which calculates:
        $$\text{exponential} = \min(\text{RECOVERY\_BASE\_MS} \times 1.5^{\text{attempts}}, \text{RECOVERY\_MAX\_MS})$$
        $$\text{jitter} = \text{random}() \times \text{RECOVERY\_BASE\_MS}$$
        $$\text{delay} = \text{exponential} + \text{jitter}$$
        With $\text{RECOVERY\_BASE\_MS} = 1500\text{ms}$ and $\text{RECOVERY\_MAX\_MS} = 30000\text{ms}$.
    *   **Phase 2 backoff** (for `attempts` from `13` to `35`): Uses a fixed base delay of `PHASE_2_BACKOFF_MS` ($20000\text{ms}$) plus a random jitter up to `RECOVERY_BASE_MS` ($1500\text{ms}$).
*   **Phase 3 Passive Sweeper-Watch Mode**:
    *   If Phase 1 & 2 fail, it enters Phase 3, running up to `PHASE_3_MAX_POLLS` (`120` polls).
    *   Each poll waits `PHASE_3_POLL_INTERVAL_MS` ($5000\text{ms}$) and checks if the device's MAC is found via the sweeper.
    *   If the device is found, it attempts a single reconnection. If that reconnection fails, it logs a warning and exits/breaks the Phase 3 loop immediately.

## 3. Jittered vs. Fixed Delays
The service uses a combination of both:
*   **Phase 1**: Jittered exponential delay (exponential backoff capped at 30,000ms plus a random jitter up to 1,500ms).
*   **Phase 2**: Jittered fixed delay (20,000ms fixed delay plus a random jitter up to 1,500ms).
*   **Phase 3**: Fixed poll interval delay (5,000ms).

## 4. Reconnection Logic
The service **does not** call `bleManager.connectToDevice()` directly, nor does it send a machine event to trigger a reconnection.
Instead, it calls the helper function `createGattSession` (imported from `../BleSessionFactory`):
```typescript
const { conn, adapter: recoveryAdapter } = await createGattSession(bleManager, deviceId, {
  timeout: 3500,
  retries: 1,
  signal,
  context: 'autoRecovery', // 'autoRecoveryPhase3' in Phase 3
});
```

## 5. All Retries Exhausted Behavior
When all attempts/retries fail, or if no device IDs are supplied at startup, the service calls `sendBack` to dispatch a `'RECOVERY_FAIL'` event:
```typescript
sendBack({ type: 'RECOVERY_FAIL' });
```

## 6. Successful Reconnect Behavior
Upon a successful reconnection, the service calls `sendBack` to dispatch a `'RECOVERY_COMPLETE'` event containing the reconnected device:
```typescript
sendBack({ type: 'RECOVERY_COMPLETE', devices: [reconnectedDevice] });
```

## 7. GATT Notification Re-Registration
**Yes**, it re-registers GATT notifications after a successful reconnect by calling `monitorCharacteristicForService` on the connection object:
```typescript
conn.monitorCharacteristicForService(
  recoveryAdapter.serviceUUID,
  recoveryAdapter.notifyCharacteristicUUID,
  (error: Error | null, characteristic: import('react-native-ble-plx').Characteristic | null) =>
    handleNotification(error, characteristic, conn.id)
);
```

## 8. Protocol Adapter Re-Mapping
**Yes**, it maps/re-maps the protocol adapter to the connection ID inside `adapterMapRef`:
```typescript
adapterMapRef.current.set(conn.id, recoveryAdapter);
```

## 9. Write Queue Clearing
**No**, the service does not call `clearWriteQueue()` or make any reference to clearing the write queue before or during recovery. It only imports `enqueueWrite` to send a recovery ping.

## 10. `any` Casts
There are **no direct `as any` type casts** in `RecoveryService.ts`. 
However:
*   `any` is used as a type parameter/argument in the type signatures of:
    *   `adapterMapRef: { current: Map<string, any> };` (Line 32)
    *   `handleOrganicDisconnect: (error: any, deviceId: string) => void;` (Line 35)
    *   `handleNotification: (error: any, characteristic: any, deviceId: string) => void;` (Line 36)
    *   `fromCallback<any, RecoveryInput>` (Line 40)
*   There are type assertions/casts to other types (but not to `any`):
    *   `error as {errorCode: unknown}` (Line 119)
    *   `error as {status: unknown}` (Line 119)

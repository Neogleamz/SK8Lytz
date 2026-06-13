# BLE Audit Findings: RecoveryService

This document contains the audit findings for `RecoveryService.ts` (`file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts`).

---

## 1. Actor Type
The `recoveryService` is defined as an XState **callback actor** using the `fromCallback` helper.
```typescript
// Line 1
import { fromCallback } from 'xstate';
// Line 45
export const recoveryService = fromCallback<any, RecoveryInput>(({ input, sendBack }) => {
```

---

## 2. Recovery Attempts & Backoff Delays

### Recovery Attempts
- **Maximum Connection Attempts**: The service makes exactly **5 recovery attempts** in the main loop before breaking and failing.
- **Loop Logic Analysis**:
  - The loop condition is `attempts <= PHASE_2_MAX_ATTEMPTS && !hasExceededMaxRecovery(attempts)` (Line 73).
  - `PHASE_2_MAX_ATTEMPTS` is `5` (Line 14).
  - `MAX_RECOVERY_ATTEMPTS` is `5` (Line 10).
  - `hasExceededMaxRecovery(attempts)` returns `attempts > 5` (Line 25).
  - Inside the loop, the delay is applied, `attempts` is incremented, and then `hasExceededMaxRecovery(attempts)` is checked. If it returns `true`, it breaks the loop *before* attempting the connection (Line 83).
  - **Trace**:
    1. **Iteration 1**: Starts with `attempts = 0`. Condition `0 <= 5 && !(0 > 5)` passes. Sleep occurs. `attempts` becomes `1`. `hasExceededMaxRecovery(1)` is false. Connects (Attempt 1).
    2. **Iteration 2**: Starts with `attempts = 1`. Condition `1 <= 5 && !(1 > 5)` passes. Sleep occurs. `attempts` becomes `2`. `hasExceededMaxRecovery(2)` is false. Connects (Attempt 2).
    3. **Iteration 3**: Starts with `attempts = 2`. Condition `2 <= 5 && !(2 > 5)` passes. Sleep occurs. `attempts` becomes `3`. `hasExceededMaxRecovery(3)` is false. Connects (Attempt 3).
    4. **Iteration 4**: Starts with `attempts = 3`. Condition `3 <= 5 && !(3 > 5)` passes. Sleep occurs. `attempts` becomes `4`. `hasExceededMaxRecovery(4)` is false. Connects (Attempt 4).
    5. **Iteration 5**: Starts with `attempts = 4`. Condition `4 <= 5 && !(4 > 5)` passes. Sleep occurs. `attempts` becomes `5`. `hasExceededMaxRecovery(5)` is false. Connects (Attempt 5).
    6. **Iteration 6**: Starts with `attempts = 5`. Condition `5 <= 5 && !(5 > 5)` passes. Sleep occurs. `attempts` becomes `6`. `hasExceededMaxRecovery(6)` is `true`. Logs warning and breaks loop *without* attempting connection.

### Backoff Delays
- **Phase 1 Delays**: Since `attempts` is always $\le 5$ (which is $\le$ `PHASE_1_MAX_ATTEMPTS` of `12`), it always calculates delay using `getRecoveryBackoffMs(attempts)` (Lines 19-23):
  $$\text{exponential} = \min(\text{RECOVERY\_BASE\_MS} \times 1.5^{\text{attempts}}, \text{RECOVERY\_MAX\_MS})$$
  $$\text{jitter} = \text{random}() \times \text{exponential} \times 0.3$$
  $$\text{delay} = \text{exponential} + \text{jitter}$$
  With $\text{RECOVERY\_BASE\_MS} = 1500\text{ms}$ and $\text{RECOVERY\_MAX\_MS} = 30000\text{ms}$.
  - **Attempt 1** (`attempts` input = 0): base 1500ms, total delay 1500ms to 1950ms.
  - **Attempt 2** (`attempts` input = 1): base 2250ms, total delay 2250ms to 2925ms.
  - **Attempt 3** (`attempts` input = 2): base 3375ms, total delay 3375ms to 4387.5ms.
  - **Attempt 4** (`attempts` input = 3): base 5062.5ms, total delay 5062.5ms to 6581.25ms.
  - **Attempt 5** (`attempts` input = 4): base 7593.75ms, total delay 7593.75ms to 9871.875ms.
- **Phase 2 Delays**: Unreachable in the main loop since it breaks at 5 attempts, but defined as a fixed backoff of `PHASE_2_BACKOFF_MS` ($20000\text{ms}$) plus a randomized jitter up to `RECOVERY_BASE_MS` ($1500\text{ms}$) (Lines 76-77).
- **Phase 3 Sweeper-Watch Delays**: Uses a fixed polling interval `PHASE_3_POLL_INTERVAL_MS` ($5000\text{ms}$) for up to `PHASE_3_MAX_POLLS` (`120` polls, approx 10 minutes) (Lines 167-170).

---

## 3. Jittered Delay vs. Fixed Delay
- **Phase 1**: Jittered exponential delay (base exponential backoff capped at 30,000ms plus up to 30% randomized jitter, Lines 20-22).
- **Phase 2**: Jittered fixed delay (20,000ms fixed plus random jitter up to 1,500ms, Lines 76-77).
- **Phase 3**: Fixed delay (5,000ms poll interval, Line 170).

---

## 4. Reconnection Mechanism
The service reconnects by directly calling the factory function `createGattSession` from `BleSessionFactory` with the `bleManager` instance passed in via inputs (Lines 93, 181). It does not send an XState machine event to coordinate reconnection:
```typescript
const { conn, adapter: recoveryAdapter } = await createGattSession(bleManager, deviceId, {
  timeout: 3500,
  retries: 1,
  signal,
  context: 'autoRecovery', // 'autoRecoveryPhase3' in Phase 3
});
```

---

## 5. Exhaustion Event
- If connection attempts exceed limits, it dispatches `'RECOVERY_PERMANENTLY_FAILED'` containing the `deviceId` (Line 234):
  ```typescript
  sendBack({ type: 'RECOVERY_PERMANENTLY_FAILED', deviceId });
  ```
- If recovery fails for other reasons (e.g. cancelled/empty inputs), it dispatches `'RECOVERY_FAIL'` (Line 236):
  ```typescript
  sendBack({ type: 'RECOVERY_FAIL' });
  ```

---

## 6. Success Event
On a successful reconnection, the service dispatches `'RECOVERY_COMPLETE'` alongside an array containing the reconnected device (Line 232):
```typescript
sendBack({ type: 'RECOVERY_COMPLETE', devices: [reconnectedDevice] });
```

---

## 7. GATT Re-registration Logic
Yes. After establishing `conn`, the service executes the following re-registration steps:
1. **MTU Negotiation** (Lines 103-115): Requests 512 MTU on Android (stored in `mtuMapRef`), defaults to 186 on iOS/errors.
2. **Disconnect Listener** (Lines 118-132): Removes the existing disconnect subscriber for the device and registers a new handler using `bleManager.onDeviceDisconnected(...)`.
3. **Notify Characteristics Monitor** (Lines 134-139): Re-attaches listener on the notify characteristic.
4. **Recovery Settle Ping** (Lines 141-152): Dispatches a settings query packet via `enqueueWrite('critical', ...)` to settle the link state.

---

## 8. Protocol Adapter Re-mapping
Yes. It updates the adapter map references with the newly created adapter instance (Lines 101, 186):
```typescript
adapterMapRef.current.set(conn.id, recoveryAdapter);
```

---

## 9. Write Queue Clearing
Yes. The service calls `clearWriteQueue()` at the very beginning of the `run()` execution block (Line 61) to remove pending commands so they do not block recovery diagnostic pings.

---

## 10. `any` Casts
There are **no `as any` type casts** or `@ts-ignore` workarounds in `RecoveryService.ts`.

However, the `any` type is used as:
- A generic parameter: `fromCallback<any, RecoveryInput>` (Line 45).
- Input reference types (Lines 32, 35, 41):
  - `adapterMapRef: { current: Map<string, any> };`
  - `handleOrganicDisconnect: (error: any, deviceId: string) => void;`
  - `handleNotification: (error: any, characteristic: any, deviceId: string) => void;`

Type-casting is used safely with `{errorCode: unknown}` and `{status: unknown}` (Line 128) rather than `any`.

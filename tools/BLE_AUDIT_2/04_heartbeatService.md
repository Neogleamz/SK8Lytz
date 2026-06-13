# BLE Audit: HeartbeatService

This document contains the audit findings for [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts).

## 1. Actor Type
The `heartbeatService` is an **XState callback actor** created via `fromCallback` from the `'xstate'` package (line 17).

## 2. Heartbeat Interval
The heartbeat interval is **45 seconds** (45,000 ms), defined by the constant `HEARTBEAT_INTERVAL_MS = 45_000` on line 9.

## 3. Payload Sent / Opcode
* **Zengge Devices**: The service calls `adapter.buildQuerySettings(false)` (line 36) to construct a query settings packet. If packets exist, it takes the first packet (`queryResult.packets[0]`), converts it to base64, and sends it to the device using `writeCharacteristicWithoutResponseForDevice` (line 40).
* **BanlanX / Unknown Fallback**: If there is no adapter or no query packets, it issues a **liveness probe** via `bleManager.readRSSIForDevice(mac)` (line 54). No BLE payload/opcode is sent in this fallback case.

## 4. Failure Detection Method
Failure detection relies on catching errors/exceptions thrown during the GATT operations:
* If the `writeCharacteristicWithoutResponseForDevice` or `readRSSIForDevice` call throws an error (e.g., GATT write/read failure, timeout, or disconnect), it enters the `catch (err: unknown)` block (line 58).

## 5. Failure Event
Inside the catch block, the service:
1. Logs a warning using `AppLogger.warn`.
2. Cancels the stale GATT connection handle immediately to release it: `await bleManager.cancelDeviceConnection(mac).catch(() => undefined)` (line 66).
3. Dispatches the **`HEARTBEAT_FAIL`** event back to the parent XState machine using `sendBack({ type: 'HEARTBEAT_FAIL', deviceId: mac })` (line 69).

## 6. enqueueWrite vs Direct Write
The service **always uses `enqueueWrite`** (from `BleWriteQueue`) with `'normal'` priority for both characteristic writes (line 39) and RSSI reads (line 53). It never executes direct writes or direct reads.
Additionally, it skips the heartbeat cycle entirely if any GATT operation is currently active or pending in the queue (line 25):
```typescript
if (isWriteQueueActive()) return;
```

## 7. Cleanup Mechanism
The callback actor returns a cleanup function (line 78) that calls `clearInterval(interval)`. This clears the heartbeat interval timer when the actor is stopped/unmounted (e.g. when the machine transitions out of the `READY` state).

## 8. Simultaneous Device Handling
The service handles multiple devices **sequentially in series** using a `for...of` loop over `connectedDevices` (line 30). For each device, it awaits the write/read operation inside the queue before moving to the next. It does not handle them concurrently or in parallel.

## 9. Any Casts
There are no type-laundering assertions (such as `as any` or `as unknown as any`) or compiler bypasses (`@ts-ignore`) in this file. The only use of `any` is in the XState actor declaration's generic type parameter:
```typescript
export const heartbeatService = fromCallback<any, HeartbeatServiceInput>(...)
```
This is a standard generic parameter for event types in `fromCallback` when not strictly typed.

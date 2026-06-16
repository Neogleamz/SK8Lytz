# BLE Audit: HeartbeatService.ts

1. **Actor type?**
   `fromCallback` (XState v5 callback actor).
2. **Heartbeat interval?**
   45 seconds (`45_000` ms).
3. **Payload sent/opcode?**
   Uses `adapter.buildQuerySettings(false)` (usually `0x81` query) if an adapter is available. If no adapter is found, it falls back to an RSSI read (`readRSSIForDevice`).
4. **Failure detection method?**
   Checks for a rejected promise from the GATT write or RSSI read inside a `try/catch` block.
5. **Failure event?**
   Sends back `{ type: 'HEARTBEAT_FAIL', deviceId: mac }`.
6. **enqueueWrite vs direct write?**
   Uses `enqueueWrite` for both the GATT write and the RSSI read. It also skips the entire heartbeat cycle if `isWriteQueueActive()` returns true.
7. **Cleanup mechanism?**
   Returns `() => clearInterval(interval)` for XState to clear the timer on exit. On failure, it also actively calls `bleManager.cancelDeviceConnection(mac)` to release the broken GATT handle immediately.
8. **Simultaneous device handling?**
   Yes, it maps over `currentDevices` and executes them using `Promise.all` so all devices are pinged in the same interval cycle.
9. **Any `any` casts?**
   Yes, there is one `any` type argument provided to `fromCallback` at line 20: `fromCallback<any, HeartbeatServiceInput>`.

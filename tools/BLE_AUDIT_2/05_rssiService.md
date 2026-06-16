# RSSI Service & Monitor Audit

1. **React hook imports in RSSIService?**
   None. `RSSIService.ts` is a pure TS file without any React imports.

2. **Structural interface vs direct BleManager import?**
   It uses a structural interface (`RSSIBleManager` with `readRSSIForDevice`) to allow for partial mocks in tests, rather than directly importing and depending on `BleManager`.

3. **Poll interval?**
   30,000 ms (30 seconds) via `RSSI_POLL_INTERVAL_MS`.

4. **Cleanup function return?**
   Yes, `startRSSIPolling` returns a cleanup function `() => clearInterval(intervalId);`.

5. **Polling logic ownership?**
   `RSSIService.ts` owns the polling loop (`startRSSIPolling`). `useBLERSSIMonitor.ts` acts as a thin wrapper that manages the React state (`rssiMap`).

6. **useBLERSSIMonitor re-exports?**
   It re-exports `RSSI_WEAK_THRESHOLD`, `RSSI_CRITICAL_THRESHOLD`, `RSSI_POLL_INTERVAL_MS`, and `readDeviceRSSI` so that existing test imports don't break.

7. **Stale MAC pruning?**
   Yes. `useBLERSSIMonitor.ts` contains a `useEffect` dependent on `connectedDeviceIds` that iterates over the `rssiMap` and deletes entries for any MAC addresses that are no longer in the connected list.

8. **onCriticalSignal RECOVERY_START wiring?**
   `useBLERSSIMonitor.ts` accepts `onCriticalSignal` as an optional callback prop and passes it to `startRSSIPolling`. When `rssi < RSSI_CRITICAL_THRESHOLD`, the service invokes the callback. The hook itself does not directly dispatch a `RECOVERY_START` event; that wiring is delegated to the hook's consumer.

9. **Any `any` casts?**
   None. It properly uses `unknown` in catch blocks (e.g., `catch (err: unknown)`).

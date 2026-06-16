# Scanner Hooks Audit

1. **`bleManager.startDeviceScan()` direct calls?**
   None. Neither file directly calls `bleManager.startDeviceScan()`. They both delegate to the XState state machine via `bleSend({ type: 'SCAN_START' })`, `bleSend({ type: 'SCAN_PAUSE' })`, or `bleSend({ type: 'SCAN_RESUME' })`.

2. **`SCAN_STOP` events?**
   - In `useBLEScanner.ts`, `SCAN_STOP` is dispatched after a 5000ms timeout for manual, non-sweeper scans or sandbox mocking paths.
   - In `useBLEBatterySweep.ts`, `SCAN_STOP` is dispatched when `stopSweeper()` is explicitly called (or when battery becomes `PAUSED`). For throttling and bursts, it dispatches `SCAN_PAUSE` and `SCAN_RESUME` instead.

3. **THROTTLED battery duty-cycle logic?**
   - Active when battery is between 15% and 30% (`BATTERY_TIER_THROTTLED_THRESHOLD = 0.15`).
   - Duty cycle: `THROTTLE_SCAN_ON_MS` (10,000ms) active, followed by `THROTTLE_SCAN_OFF_MS` (20,000ms) paused. 
   - It cycles by sending `SCAN_RESUME` and `SCAN_PAUSE` recursively using `setTimeout`.

4. **Android scan budget handling?**
   - Handled in `startSweeper` for Android 12+ (API 31+).
   - `SCAN_BUDGET_MAX = 4` starts within a `SCAN_BUDGET_WINDOW_MS = 30_000` (30 seconds) window.
   - If the budget is exhausted, it sets a `budgetResetTimerRef` timeout for the remaining milliseconds until the oldest scan ages out of the 30s window, temporarily deactivating the sweeper until it resets.

5. **`scanCallback` wiring?**
   - The `scanCallback` function is defined internally inside `useBLEScanner.ts` to process device discovery, RSSI filtering, telemetry batching, and `queueDeviceForInterrogation`.
   - It is not passed directly to `bleManager` in this file; rather, it is returned from the `useBLEScanner` hook to be consumed by the parent component (likely to be passed to `BleMachine`). 

6. **`scanForPeripherals` non-sweeper paths?**
   - If FTUE (`registeredMacs.length === 0` and no `keepAlive`), it directly invokes `startSweeper()` for a persistent scan.
   - If `isSweeperActive` is true, it triggers `burstScan()` for 5s (or 10s if `keepAlive` is set).
   - If neither (manual scan), it dispatches `SCAN_START` and sets a 5000ms timeout to dispatch `SCAN_STOP`.

7. **`useBLEInterrogator` imports?**
   - Imported in `useBLEScanner.ts` as: `import { useBLEInterrogator } from './useBLEInterrogator';`
   - Initialized to get `hwCache`, `hwCacheRef`, and `queueDeviceForInterrogation`, passing down `bleManager`, `registeredMacs`, and an `onDeviceInterrogated` callback.

8. **`useBLEBatterySweep` imports?**
   - Imported in `useBLEScanner.ts` as: `import { useBLEBatterySweep } from './useBLEBatterySweep';`
   - Initialized to get `isSweeperActive`, `startSweeper`, `stopSweeper`, `burstScan`, and `batteryTier`, passing down `bleManager` and `bleSend`.

9. **Any `any` casts?**
   - Zero `any` casts found in either file.
   - Error catches correctly use `unknown` (e.g., `catch (e: unknown)`).
   - Some explicit type assertions exist (e.g., `as Device`, `as TelemetryInsert[]`), but no `as any` or `@ts-ignore` bypasses.

# BLE Audit Report: Scanner & Battery Sweep Hook analysis
**Files Evaluated:**
- [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- [useBLEBatterySweep.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts)

---

## 1. Direct Calls to `bleManager.startDeviceScan()`
No direct calls to `bleManager.startDeviceScan()` exist in either file. The `BleManager` is only imported as a type for parameter typing. The scanning lifecycle is controlled entirely by sending event dispatches to the XState state machine (`bleSend`) with events such as `SCAN_START`, `SCAN_STOP`, `SCAN_PAUSE`, and `SCAN_RESUME`. The actual invocation of the native BLE manager scan methods is encapsulated inside the `bleMachine` service/actor.

---

## 2. SCAN_STOP Events
`SCAN_STOP` events are dispatched via `bleSend({ type: 'SCAN_STOP' })` in the following locations:
* **`useBLEScanner.ts`:**
  * **Sandbox Mocking Path:** Dispatched inside a `setTimeout` callback after 5000ms to stop the simulated scan (line 339).
  * **Non-Sweeper Manual Path:** Dispatched inside a fallback `setTimeout` callback after 5000ms (line 362).
* **`useBLEBatterySweep.ts`:**
  * **Stop Sweeper Action:** Dispatched in the `stopSweeper` callback when turning off the scanner sweeper (line 128).

---

## 3. THROTTLED Battery Duty-Cycle Logic
The THROTTLED battery duty-cycle logic resides in `useBLEBatterySweep.ts`:
* **Thresholds:** Classified when the battery level is between `0.15` (15%) and `0.30` (30%).
* **Duty-Cycle Constants:** 
  * `THROTTLE_SCAN_ON_MS = 10_000` (10s scanning)
  * `THROTTLE_SCAN_OFF_MS = 20_000` (20s paused)
* **Execution Flow (`startThrottleCycle`):**
  * It starts a recursive cycle via `throttleCycleTimerRef` utilizing `setTimeout`.
  * In the active phase, it calls `bleSend({ type: 'SCAN_RESUME' })` and logs a `sweeper_throttle_scan_on` event.
  * After 10 seconds, it triggers `bleSend({ type: 'SCAN_PAUSE' })`, logs `sweeper_throttle_scan_off`, and sets a 20-second timeout to repeat the cycle.
* **State Listener Transitions:**
  * On transition from `FULL` to `THROTTLED`: It dispatches `SCAN_PAUSE`, clears any existing throttle cycle timer, and invokes `startThrottleCycle()`.
  * On transition from `THROTTLED` to `FULL`: It clears the throttle cycle timer and dispatches `SCAN_RESUME`.

---

## 4. Android Scan Budget Handling
Android scan budget deferral is implemented in `useBLEBatterySweep.ts` inside `startSweeper`:
* **Limits:** `SCAN_BUDGET_MAX = 4` starts within `SCAN_BUDGET_WINDOW_MS = 30_000` (30 seconds).
* **Logic:**
  * If the platform is Android and API version is >= 31, it checks the timestamps stored in `scanStartTimestampsRef.current`.
  * It filters out timestamps older than 30 seconds.
  * If the size of the array is `>= 4`, it identifies the oldest timestamp and calculates `msUntilBudgetResets` (the remaining time before that oldest scan expires from the 30-second window plus a 100ms safety margin).
  * It logs a `sweeper_start_deferred_budget` event, resets `isSweeperActive` state to `false`, and schedules a deferral `setTimeout` to retry starting the sweeper once the oldest window budget slot clears.
  * If the budget is not exceeded, the current timestamp `now` is appended to `scanStartTimestampsRef.current` and scanning proceeds.

---

## 5. scanCallback Wiring
* **Definition:** `scanCallback` is defined as a `useCallback` inside `useBLEScanner.ts` (lines 204-294).
* **Purpose:** It processes BleErrors and discovered Devices, parses advertisement manufacturer data for Zengge/BanlanX device signatures, validates RSSI thresholds (filtering based on whether the device MAC is registered or new), updates the last seen time, records telemetry statistics, and pushes valid devices to the staging queue for interrogation.
* **Wiring:** The callback is returned from the `useBLEScanner` hook, but it is **not** registered directly with `bleManager` inside either hook file. The hook caller is responsible for passing or wiring this callback to the native BLE scanner registration mechanism or XState service.

---

## 6. scanForPeripherals Non-Sweeper Paths
In `useBLEScanner.ts`, the `scanForPeripherals` function executes several logical branches:
1. **Cleanup Phase:** Clears all search-related state (pending registrations, rejected and seen MACs, device lists) unless `options?.keepAlive` is true.
2. **Sandbox Mocking Path:** If `isSandboxEnabled` and the app is running in DEV/Web mode:
   * Dispatches `SCAN_START`.
   * Sets a 1-second timeout to mock the discovery of four virtual devices (`VIRTUAL-HALOZ-L`, `VIRTUAL-HALOZ-R`, `VIRTUAL-SOULZ-L`, `VIRTUAL-SOULZ-R`) by calling `scanCallback` for each.
   * Sets a 5-second timeout to dispatch `SCAN_STOP`.
   * Early returns if on the Web platform.
3. **First-Time User Experience (FTUE) Path:** If no devices are registered (`registeredMacs.length === 0`) and `keepAlive` is false:
   * Bypasses sweeper state checks and directly calls `startSweeper()` to start scanning persistently.
4. **Sweeper Burst Path:** If the sweeper is already active:
   * Calls `burstScan()` for 10 seconds (if keepAlive is true) or 5 seconds.
5. **Non-Sweeper Manual Scan Fallback:** If the sweeper is not active and sandbox mode is disabled:
   * Dispatches `SCAN_START`.
   * Sets a 5-second fallback timer that dispatches `SCAN_STOP`.

---

## 7. useBLEInterrogator Imports
`useBLEInterrogator` is imported on line 19:
```typescript
import { useBLEInterrogator } from './useBLEInterrogator';
```
It is called on line 142 inside the hook to query details from discovered hardware:
```typescript
  const { hwCache, hwCacheRef, queueDeviceForInterrogation } = useBLEInterrogator({
    bleManager,
    registeredMacs,
    onDeviceInterrogated: () => {
      setAllDevices(current => {
        classifyProbeResults(current);
        return current;
      });
    }
  });
```

---

## 8. useBLEBatterySweep Imports
`useBLEBatterySweep` is imported on line 18:
```typescript
import { useBLEBatterySweep } from './useBLEBatterySweep';
```
It is called on line 296 to manage battery-aware sweeps:
```typescript
  const { isSweeperActive, startSweeper, stopSweeper: _stopSweeper, burstScan: _burstScan, batteryTier } = useBLEBatterySweep({
    bleManager,
    bleSend
  });
```

---

## 9. Presence of `any` Casts
There are absolutely **no** `any` casts (e.g., `as any`, typed variables using `any`), nor any `@ts-ignore` comments in either file. Both files are strictly typed and pass static compilation checks cleanly.

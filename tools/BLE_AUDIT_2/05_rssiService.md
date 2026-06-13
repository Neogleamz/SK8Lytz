# BLE Audit Report: RSSIService and useBLERSSIMonitor

This report documents findings from an architectural and implementation audit of `RSSIService.ts` and `useBLERSSIMonitor.ts`.

---

## 🔍 Codebase Audited Files
1. [RSSIService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RSSIService.ts)
2. [useBLERSSIMonitor.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLERSSIMonitor.ts)

---

## 📝 Audit Answers & Findings

### 1. React Hook Imports in RSSIService
* **Findings:** There are **no** React hook imports (such as `useState`, `useEffect`, or `useRef`) in `RSSIService.ts`. 
* **Details:** It strictly imports:
  ```typescript
  import type { Device } from 'react-native-ble-plx';
  import { AppLogger } from '../AppLogger';
  ```
  This keeps `RSSIService.ts` as a pure, React-independent utility service file that can be run and tested in environments outside of React (e.g., node test runners).

### 2. Structural Interface vs Direct BleManager Import
* **Findings:** `RSSIService.ts` declares a minimal structural interface `RSSIBleManager` rather than importing `BleManager` directly from `react-native-ble-plx`.
* **Details:**
  ```typescript
  interface RSSIBleManager {
    readRSSIForDevice(deviceIdentifier: string): Promise<Device>;
  }
  ```
  This is a clean-architecture best practice that decouples the service from the library's concrete implementation and allows for simple, lightweight mocking in unit tests.

### 3. Poll Interval
* **Findings:** The poll interval is defined as **30,000 milliseconds** (30 seconds).
* **Details:**
  ```typescript
  export const RSSI_POLL_INTERVAL_MS = 30_000;
  ```

### 4. Cleanup Function Return
* **Findings:** 
  * `startRSSIPolling` returns a function that clears the interval timer:
    ```typescript
    return () => clearInterval(intervalId);
    ```
  * In `useBLERSSIMonitor.ts`, this returned function is captured as `stopPolling` and returned directly in the hook's `useEffect` clean-up phase. This ensures that when the component unmounts (or dependency changes), the timer is cleaned up properly.

### 5. Polling Logic Ownership
* **Findings:** The ownership is split as follows:
  * **Core Polling Logic**: Resides in `RSSIService.ts` within the `startRSSIPolling` loop function. It manages the `setInterval` interval, handles overlapping execution prevention (idempotency via `_isRunning`), loops over currently connected devices, performs `readDeviceRSSI` calls, and dispatches callbacks.
  * **UI/State Management**: Resides in the React hook `useBLERSSIMonitor.ts`. It acts as a thin wrapper that stores the RSSI values in React state (`rssiMap`), triggers local state clean-up when devices disconnect, and provides callbacks to the caller.

### 6. useBLERSSIMonitor Re-exports
* **Findings:** `useBLERSSIMonitor.ts` re-exports the constants and helper functions from `RSSIService.ts` so that existing consumers/test files do not break.
* **Details:**
  ```typescript
  export { RSSI_WEAK_THRESHOLD, RSSI_CRITICAL_THRESHOLD, RSSI_POLL_INTERVAL_MS, readDeviceRSSI };
  ```

### 7. Stale MAC Pruning
* **Findings:** Stale MAC address pruning is handled by a secondary `useEffect` hook that observes changes in the list of active connected device IDs (`connectedDeviceIds`).
* **Details:**
  ```typescript
  useEffect(() => {
    setRssiMap(prev => {
      let changed = false;
      const pruned = { ...prev };
      Object.keys(pruned).forEach(mac => {
        if (!connectedDeviceIds.includes(mac)) {
          delete pruned[mac];
          changed = true;
        }
      });
      return changed ? pruned : prev;
    });
  }, [connectedDeviceIds]);
  ```
  Whenever `connectedDeviceIds` updates, any device identifier in the state's `rssiMap` that is no longer in the array is deleted to prevent stale badges or disconnected status showing incorrect signal strength.

### 8. onCriticalSignal RECOVERY_START Wiring
* **Findings:** Yes, critical signal events trigger the XState recovery pipeline.
* **Details:**
  * **Service Detection:** `RSSIService.ts` checks `if (rssi < RSSI_CRITICAL_THRESHOLD)` during its polling loop, firing `callbacks.onCriticalSignal?.(mac, rssi)`.
  * **Hook Propagation:** `useBLERSSIMonitor` receives `onCriticalSignal` parameters and forwards it to the service.
  * **XState Trigger:** In `useBLE.ts`, `useBLERSSIMonitor` is invoked with `onCriticalSignal: handleCriticalSignal`. Inside `handleCriticalSignal`, if the device is not already in the recovery queue, it dispatches the `RECOVERY_START` action to the XState engine:
    ```typescript
    bleSend({ type: 'RECOVERY_START', ghostedMacs: [mac] });
    ```

### 9. Any `any` Casts
* **Findings:** There are no `any` casts inside `RSSIService.ts`. However, there is exactly one `any` type declaration inside `useBLERSSIMonitor.ts`'s parameters.
* **Details:** In `useBLERSSIMonitor.ts` at line 29:
  ```typescript
  export interface UseBLERSSIMonitorParams {
    bleManager: any;
    ...
  ```
  No other usages of `as any` or `@ts-ignore` are present in either file.

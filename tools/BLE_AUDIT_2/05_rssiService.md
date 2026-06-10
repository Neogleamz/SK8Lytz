# BLE Audit Findings: RSSIService and useBLERSSIMonitor

This document compiles the read-only audit findings for the Bluetooth Low Energy (BLE) RSSI service and its associated React hook wrapper.

- **Audited Files:**
  - `src/services/ble/RSSIService.ts`
  - `src/hooks/ble/useBLERSSIMonitor.ts`
  - `src/hooks/useBLE.ts` (partial verification of hook integration)

---

## 1. Does `RSSIService.ts` import any React hooks?
**No.** `RSSIService.ts` is a pure TypeScript service file and does not import or use any React hooks. Its imports are:
```typescript
import type { Device } from 'react-native-ble-plx';
import { AppLogger } from '../AppLogger';
```

---

## 2. Does `RSSIService.ts` import `BleManager` from `react-native-ble-plx`, or a structural interface? Quote the type.
It does **not** import `BleManager` from `react-native-ble-plx`. Instead, it defines a local structural interface named `RSSIBleManager` to achieve loose coupling and simplify testing:
```typescript
interface RSSIBleManager {
  readRSSIForDevice(deviceIdentifier: string): Promise<Device>;
}
```

---

## 3. What is the poll interval?
The poll interval is **30,000 milliseconds (30 seconds)**, defined by the constant:
```typescript
export const RSSI_POLL_INTERVAL_MS = 30_000;
```

---

## 4. Does `startRSSIPolling` return a cleanup function?
**Yes.** `startRSSIPolling` returns a closure that clears the polling interval:
```typescript
return () => clearInterval(intervalId);
```

---

## 5. Does `useBLERSSIMonitor.ts` own any polling logic itself, or does it fully delegate to `RSSIService`?
It **fully delegates** the polling logic to `RSSIService`. The hook uses a React `useEffect` to invoke `startRSSIPolling` and returns the returned function to clear the interval on unmount or dependency changes:
```typescript
    const stopPolling = startRSSIPolling(bleManager, {
      getConnectedDevices: () => connectedDevicesRef.current,
      onRssiUpdated: (mac, rssi) => setRssiMap(prev => ({ ...prev, [mac]: rssi })),
      onWeakSignal,
      onCriticalSignal,
    });

    return stopPolling;
```

---

## 6. Is `readDeviceRSSI` re-exported from `useBLERSSIMonitor.ts`?
**Yes.** For backward compatibility with existing tests and imports, `readDeviceRSSI` and the associated thresholds/constants are explicitly re-exported:
```typescript
// Re-export constants AND readDeviceRSSI so existing test imports don't break
export { RSSI_WEAK_THRESHOLD, RSSI_CRITICAL_THRESHOLD, RSSI_POLL_INTERVAL_MS, readDeviceRSSI };
```

---

## 7. Does `useBLERSSIMonitor` correctly prune stale MAC entries from `rssiMap` on device disconnect?
**Yes.** It implements a dedicated `useEffect` listening to the `connectedDeviceIds` array. When a device disconnects (its MAC is removed from `connectedDeviceIds`), the hook prunes the stale entries from the local `rssiMap` state:
```typescript
  // Clear rssiMap when devices disconnect to avoid stale entries in the badge.
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

---

## 8. Does `useBLE.ts` wire `onCriticalSignal` to send `RECOVERY_START` to the machine? Quote the relevant lines.
**Yes.** `useBLE.ts` defines a `handleCriticalSignal` callback which dispatches the `RECOVERY_START` event to the `bleMachine` state machine, and passes this callback to `useBLERSSIMonitor`.

Here are the relevant lines from `src/hooks/useBLE.ts` (lines 418–424):
```typescript
  const handleCriticalSignal = useCallback((mac: string) => {
    // Only reconnect if device is not already in the recovery queue.
    if (!bleSnapshot.context.ghostedDeviceIds.includes(mac)) {
      AppLogger.warn('[BLE RSSI] Critical signal — proactive reconnect', { deviceId: scrubPII(mac) });
      bleSend({ type: 'RECOVERY_START', ghostedMacs: [mac] });
    }
  }, [bleSnapshot.context.ghostedDeviceIds, bleSend]);
```

---

## 9. Are there any `any` casts in either file? List them.
- **In `RSSIService.ts`:** No `any` casts or declarations exist.
- **In `useBLERSSIMonitor.ts`:** There are no `any` casts (such as `as any`), but there is one type declaration using `any` within the parameter interface:
  ```typescript
  export interface UseBLERSSIMonitorParams {
    bleManager: any;
    // ...
  }
  ```

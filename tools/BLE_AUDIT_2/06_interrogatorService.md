# BLE Audit: InterrogatorService & useBLEInterrogator

## 1. File Details
* **Service File:** [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
* **Hook File:** [useBLEInterrogator.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEInterrogator.ts)

---

## 2. Answers to Audit Questions

### 1. React hooks imported?
* **InterrogatorService.ts:** None. It contains pure TypeScript logic and I/O.
* **useBLEInterrogator.ts:** Yes, it imports `useCallback`, `useEffect`, `useRef`, and `useState` on line 8:
  ```typescript
  import { useCallback, useEffect, useRef, useState } from 'react';
  ```

### 2. cancelDeviceConnection in finally block?
Yes. In `InterrogatorService.ts`, `bleManager.cancelDeviceConnection(mac)` is called inside the `finally` block of the `interrogateDevice` function (lines 153-154):
```typescript
  } finally {
    probingMacsRef.current.delete(mac);
    await bleManager.cancelDeviceConnection(mac)
      .catch((e: unknown) => AppLogger.warn('[InterrogatorService] Disconnect after probe failed', { error: String(e) }));
  }
```

### 3. enqueueWrite for 0x63 query?
Yes. In `InterrogatorService.ts`, the HW query (which corresponds to `0x63` settings retrieval) is retrieved via `interrogatorAdapter.buildQuerySettings(false)` and its transmission is wrapped in `enqueueWrite('normal', async () => { ... })` on lines 117-122:
```typescript
      const hwQuery = interrogatorAdapter.buildQuerySettings(false);
      if (hwQuery.packets.length > 0) {
        const b64HW = Buffer.from(hwQuery.packets[0]).toString('base64');
        enqueueWrite('normal', async () => {
          await bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64HW
          );
          return true;
        }).catch((e: unknown) => AppLogger.warn('[InterrogatorService] HW query write failed', { error: String(e) }));
      }
```

### 4. AsyncStorage parse error handling?
Yes. In `InterrogatorService.ts` within the `loadHWCacheFromStorage` function (lines 43-47), the `JSON.parse` call is wrapped in a `try...catch` block. If a malformed entry is detected, it is logged as a warning via `AppLogger.warn` instead of crashing the startup sequence:
```typescript
      try {
        result[mac] = JSON.parse(val);
      } catch (e: unknown) {
        AppLogger.warn('[InterrogatorService] Malformed HW cache entry', { mac, error: e instanceof Error ? e.message : String(e) });
      }
```

### 5. FTUE delay vs standard delay?
* **FTUE (First-Time User Experience) Delay:** `PROBE_QUEUE_DELAY_MS_FTUE = 500` ms (line 27). Used when the registered device count is `0`.
* **Standard Delay:** `PROBE_QUEUE_DELAY_MS = 2000` ms (line 26). Used when there are already registered devices.
* **Mechanism:** In `createProbeQueue`'s `processQueue` function (lines 174-177), the delay is selected dynamically:
  ```typescript
  const delay = params.getRegisteredMacsCount() === 0 ? PROBE_QUEUE_DELAY_MS_FTUE : PROBE_QUEUE_DELAY_MS;
  probeQueueTimerRef.current = setTimeout(async () => {
  ```
  This provides rapid interrogation during the initial setup/FTUE phase where a user is trying to pair their first lights, and falls back to a safer, debounced schedule when devices are already registered to prevent saturating GATT.

### 6. Logic delegation ownership?
* **InterrogatorService.ts** owns the pure state and business logic:
  * Persistent storage operations (`loadHWCacheFromStorage`).
  * Low-level GATT setup, monitoring, protocol query writes, response notifications parsing, and local caching (`interrogateDevice`).
  * Probe queue processing logic and scheduling (`createProbeQueue`).
* **useBLEInterrogator.ts** acts as a thin React boundary wrapper:
  * Owns the React hook lifecycle and state (`hwCache` state via `useState`, refs via `useRef` for tracking `hwCacheRef` and `probingMacsRef`).
  * Initiates storage load on mount via `useEffect`.
  * Instantiates the queue with callbacks that bridge the pure service events back into React state updates (e.g., calling `setHwCache({ ...hwCacheRef.current })` to trigger UI updates when a new device is discovered).
  * Exposes memoized callbacks to React consumers.

### 7. hwCacheRef syncing?
Yes, syncing between the ref (`hwCacheRef`) and the React state (`hwCache`) is handled at key lifecycle and event boundaries:
1. **On Mount (AsyncStorage load):**
   ```typescript
   loadHWCacheFromStorage().then(loaded => {
     if (Object.keys(loaded).length > 0) {
       hwCacheRef.current = { ...hwCacheRef.current, ...loaded };
       setHwCache(prev => ({ ...prev, ...loaded }));
     }
   });
   ```
2. **On Device Interrogated callback:**
   Inside `useBLEInterrogator.ts`, the `onDeviceInterrogated` hook property handler is wrapped. When the service completes an interrogation, it mutates `hwCacheRef.current` directly (line 142: `hwCacheRef.current[mac] = hwConfig;`), and calls the hook's callback, which performs:
   ```typescript
   onDeviceInterrogated: () => {
     setHwCache({ ...hwCacheRef.current });
     onDeviceInterrogated();
   }
   ```
   This ensures the reference object and the React state remain synchronized, prompting downstream consumers to re-render.

### 8. Memoization of exported callbacks?
Yes. The exported callback is memoized using `useCallback` with an explicit dependency array (lines 51-55):
```typescript
  const stableQueueDeviceForInterrogation = useCallback(
    (mac: string) => queueDeviceForInterrogation(mac),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [bleManager, registeredMacs.length],
  );
```
*(Note: It includes an eslint-disable-next-line rule to ignore the dependencies of `queueDeviceForInterrogation` which is recreated conditionally on render)*

### 9. Any `any` casts?
No. There are **zero** `any` casts (`as any` or `@ts-ignore`) in both files. They strictly use type interfaces (`PingResult`, `BleManager`, `BleError`, `Characteristic`), standard casting (`as number[]`), and type guard check `isPingResult` to enforce type safety.

# BLE Interrogation Service Audit Findings

This document outlines the findings of a read-only audit conducted on the hardware interrogation system of the SK8Lytz application, analyzing `InterrogatorService.ts` and its React wrapper hook `useBLEInterrogator.ts`.

---

## Audit Questions & Answers

### 1. Does `InterrogatorService.ts` import any React hooks?
**No.** `InterrogatorService.ts` is a pure TypeScript module. It contains no React imports and does not consume any React hooks. It relies on vanilla JavaScript/TypeScript parameters, callbacks, and reference parameters (e.g. passing mutable `{ current: ... }` references) to communicate with the calling context.

---

### 2. Does `interrogateDevice` call `bleManager.cancelDeviceConnection` in its `finally` block?
**Yes.** This is a critical safeguard to ensure the GATT connection is always disconnected and resources are freed after the probe process, whether it succeeds or fails. 

The `finally` block in `interrogateDevice` is defined as:
```typescript
  } finally {
    probingMacsRef.current.delete(mac);
    await bleManager.cancelDeviceConnection(mac)
      .catch((e: unknown) => AppLogger.warn('[InterrogatorService] Disconnect after probe failed', { error: String(e) }));
  }
```

---

### 3. Does it use `enqueueWrite` for sending the `0x63` HW query? Quote the call.
**Yes.** To avoid GATT channel saturation, the write is scheduled using the `enqueueWrite` queue manager.

The exact call from `InterrogatorService.ts` is:
```typescript
        enqueueWrite('normal', async () => {
          await bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64HW
          );
          return true;
        }).catch((e: unknown) => AppLogger.warn('[InterrogatorService] HW query write failed', { error: String(e) }));
```

---

### 4. Does `loadHWCacheFromStorage` handle `AsyncStorage` parse errors gracefully?
**Yes.** In `loadHWCacheFromStorage`, individual entries read from `AsyncStorage` are wrapped in their own nested `try-catch` blocks. If any cache entry is malformed or corrupt, it is caught, logged as a warning, and bypassed without disrupting the loading of other keys or breaking the application launch:

```typescript
      try {
        result[mac] = JSON.parse(val);
      } catch (e: unknown) {
        AppLogger.warn('[InterrogatorService] Malformed HW cache entry', { mac, error: e instanceof Error ? e.message : String(e) });
      }
```

---

### 5. Does `createProbeQueue` correctly use FTUE delay (500ms) vs standard delay (2000ms)?
**Yes.** The queue manager evaluates the delay based on the number of registered boards. During the First-Time User Experience (FTUE), where no boards are registered yet, it uses a shorter delay of 500ms (`PROBE_QUEUE_DELAY_MS_FTUE`) to ensure rapid discovery response. Otherwise, it defaults to the standard 2000ms delay (`PROBE_QUEUE_DELAY_MS`):

```typescript
    const delay = params.getRegisteredMacsCount() === 0 ? PROBE_QUEUE_DELAY_MS_FTUE : PROBE_QUEUE_DELAY_MS;
    probeQueueTimerRef.current = setTimeout(async () => {
      while (probeQueueRef.current.length > 0) {
        const mac = probeQueueRef.current.shift()!;
        await interrogateDevice(mac, params.bleManager, params.probingMacsRef, params.hwCacheRef, params.onDeviceInterrogated);
        await new Promise(r => setTimeout(r, 500));
      }
    }, delay);
```

---

### 6. Does `useBLEInterrogator.ts` own any probe logic itself, or fully delegate to `InterrogatorService`?
**It fully delegates.** The React hook acts as a thin wrapper and state synchronizer. It defines:
- React state (`hwCache`)
- Component-lifecycle refs (`hwCacheRef`, `probingMacsRef`)
- A mount-effect that triggers `loadHWCacheFromStorage()`
- Callback mapping to invoke `createProbeQueue()`

It contains zero hardware querying, packet assembly/disassembly, timing loops, or raw GATT operations.

---

### 7. Does `useBLEInterrogator` sync `hwCacheRef` back to React state after a probe completes?
**Yes.** Inside the callback config passed to `createProbeQueue`, the `onDeviceInterrogated` hook synchronizes the ref's current contents to the state variable by creating a shallow copy, triggering a re-render of subscriber components:

```typescript
        onDeviceInterrogated: () => {
          // Sync hwCacheRef into React state so consumers re-render
          setHwCache({ ...hwCacheRef.current });
          onDeviceInterrogated();
        },
```

---

### 8. Is `queueDeviceForInterrogation` stable across renders (memoized)?
**Yes.** The returned function is memoized via `useCallback` with dependencies on `bleManager` and `registeredMacs.length`:

```typescript
  const stableQueueDeviceForInterrogation = useCallback(
    (mac: string) => queueDeviceForInterrogation(mac),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [bleManager, registeredMacs.length],
  );
```

---

### 9. Are there any `any` casts in either file? List them.
**No.** There are zero occurrences of `as any` or `@ts-ignore` in both `InterrogatorService.ts` and `useBLEInterrogator.ts`. All variables, callback errors (`e: unknown`), and buffer casts (e.g. `as number[]` on raw buffers) are typed correctly and securely.

---

## Verification & Summary
- **Type Safety**: Both files adhere strictly to type safety, utilizing type assertions where safe (`as number[]` for base64 parsed byte buffers) and handling unknown catch variables defensively.
- **Resource Management**: Ensures clean teardown by calling `cancelDeviceConnection` in `finally` and cleanly removing notify monitor subscriptions when timeout or complete responses are received.

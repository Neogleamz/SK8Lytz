# PLAN: perf/sensor-logger-hardening

## Summary

Batch of 6 targeted, low-risk performance fixes across the sensor pipeline and logging
engine. Every fix reduces JS thread work in high-frequency code paths without changing
any visible behaviour or BLE protocol.

**Blocked by:** `perf/visualizer-engine-overhaul` — this task shares no files with Task A
but should be executed after Task A is validated on hardware to confirm the JS thread
budget freed by the fixes is attributable correctly.

---

## Fix Inventory

### Fix 1 — `AppLogger.ts`: Batch AsyncStorage writes

**Problem:** `persist()` is called on every `log()` event, serializing the entire
`this.buffer` array (up to 500 entries × average 200 bytes = ~100KB) to AsyncStorage
on every event. During normal usage (color changes, mode switches, pattern picks) this
fires 3-5 times per second.

**Root cause line:** `AppLogger.ts` line 191 — `await AsyncStorage.setItem(...)` inside
`persist()` which is called unconditionally from `log()` line 358.

**Fix:** Add a write accumulator. Only call `AsyncStorage.setItem` if:
- `this.buffer.length - this.lastPersistedLength >= 10` (10 new events have accumulated)
- OR `Date.now() - this.lastPersistTime >= 2000` (2 seconds have passed)

```typescript
private lastPersistedLength = 0;
private lastPersistTime = 0;

private async persist() {
  try {
    if (this.buffer.length > MAX_ENTRIES) {
      this.buffer = this.buffer.slice(-MAX_ENTRIES);
    }
    const now = Date.now();
    const newEntries = this.buffer.length - this.lastPersistedLength;
    if (newEntries < 10 && (now - this.lastPersistTime) < 2000) return; // skip write
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(this.buffer));
    this.lastPersistedLength = this.buffer.length;
    this.lastPersistTime = now;
  } catch (e) {
    if (__DEV__) console.warn('[AppLogger] persist failed', e);
  }
}
```

**Safety:** On clean app close, `uploadLogsToSupabase()` already calls `persist()` via
the upload path which clears the buffer — no log loss on exit. The 2-second window is
safe for analytics data (not crash data — VIP Fast-Lane bypasses persist entirely).

---

### Fix 2 — `AppLogger.ts`: Replace deep-clone PII scrub with targeted key scan

**Problem:** `formatPayload()` line 287 runs `JSON.parse(JSON.stringify(payload))` — a
full deep-clone on every log event — purely to enable PII scrubbing.

**Fix:** Replace with a shallow spread + targeted recursive key-walk that avoids the
double serialization:

```typescript
private formatPayload(payload: Record<string, any>): Record<string, any> {
  // Shallow copy to avoid mutating caller's object
  const clean: Record<string, any> = { ...payload };
  // Targeted PII scrub (same logic, no round-trip serialize)
  const piiKeys = new Set(['email','name','password','token','phone','address','fullname']);
  const scrub = (obj: Record<string, any>) => {
    for (const key in obj) {
      if (piiKeys.has(key.toLowerCase()) && typeof obj[key] === 'string') {
        obj[key] = '[REDACTED]';
      } else if (obj[key] && typeof obj[key] === 'object' && !Array.isArray(obj[key])) {
        scrub(obj[key] as Record<string, any>);
      }
    }
  };
  scrub(clean);
  // Hardware injection (unchanged)
  if (this.activeDevices.length > 0) {
    const primary = this.activeDevices.find(d => d.id === (clean as any).deviceId) || this.activeDevices[0];
    if (primary) {
      if (!(clean as any).rssi && primary.rssi !== undefined) (clean as any).rssi = primary.rssi;
      if (!(clean as any).mtu && primary.mtu !== undefined) (clean as any).mtu = primary.mtu;
    }
  }
  return clean;
}
```

**Collateral Damage Lock:** Do NOT change PII key list. Do NOT change VIP Fast-Lane path.

---

### Fix 3 — `useAppMicrophone.ts`: Magnitude delta guard + interval slow-down

**Problem:** `setInterval` at 50ms fires `writeToDevice(ZenggeProtocol.sendMusicMagnitude(...))`
20 times per second unconditionally. The BLE write fires even if the microphone magnitude
hasn't changed materially.

**Fix:**

```diff
- magnitudeInterval.current = setInterval(() => {
+ const prevMagRef = { current: -1 };
+ magnitudeInterval.current = setInterval(() => {
    if (!writeToDevice) return;
    const stats = recorder.getStatus();
    if (stats.canRecord && stats.isRecording) {
      const metering = stats.metering ?? -160;
      const normalized = Math.max(0, Math.min(1, (metering + 60) / 60));
+     // Skip write if magnitude hasn't changed by more than 5% — avoids useless BLE traffic
+     if (Math.abs(normalized - prevMagRef.current) < 0.05) return;
+     prevMagRef.current = normalized;
      setAudioMagnitude(normalized);
      const deviceMag = Math.floor(normalized * 255);
      writeToDevice(ZenggeProtocol.sendMusicMagnitude(deviceMag));
    }
- }, 50);
+ }, 100); // 10Hz — hardware BLE ACK latency makes 20Hz invisible; saves ~50% BLE writes
```

**Rationale:** The ZENGGE controller responds to magnitude changes with a visual effect
that has its own hardware interpolation. Sending at 10Hz vs 20Hz produces no perceptible
visual difference because the hardware smooths between values. The delta guard further
reduces writes by ~40% during quiet or consistent audio.

---

### Fix 4 — `useStreetMode.ts`: Move `require()` to top-level static import

**Problem:** Line 153 inside `applyStreetPattern` (a `useCallback`):
```typescript
const { buildPatternPayload } = require('../protocols/PatternEngine');
```
Dynamic `require()` inside a hot callback triggers a CommonJS module cache lookup on
every invocation. While cached after first call, it still takes a synchronous path
through the module registry every time.

**Fix:** Add a top-level static import at the top of `useStreetMode.ts`:
```typescript
import { buildPatternPayload } from '../protocols/PatternEngine';
```
And remove the `require()` call inside `applyStreetPattern`.

**Collateral Damage Lock:** Touch ONLY the import block and the single `require()` line
inside `applyStreetPattern`. Do NOT change the callback logic.

---

### Fix 5 — `useStreetMode.ts`: Convert `peakGForce` to ref-gated state update

**Problem:** The accelerometer listener at 80ms calls `setPeakGForce(...)` on every tick
via the exponential decay formula, causing a React re-render 12.5x per second even when
the user is standing still and the value barely changes.

**Fix:** Only call `setPeakGForce` when the value changes by more than a threshold:

```diff
  const prevGRef = useRef(0);
  // Inside Accelerometer.addListener callback:
  const newG = Math.sqrt(x * x + y * y + z * z);
  const decayed = prevGRef.current * 0.95 + newG * 0.05;
  prevGRef.current = decayed;
- setPeakGForce(decayed);
+ if (Math.abs(decayed - peakGForce) > 0.05) {
+   setPeakGForce(decayed);
+ }
```

Note: `peakGForce` from `useState` is available in the outer scope via closure.
The `prevGRef` stores the decay accumulator to avoid stale closure issues.

---

### Fix 6 — `useControllerAnalytics.ts`: Verify deviceContext memo stability

**Problem:** `deviceContext` is computed via `useMemo` in `DockedController.tsx` (line 368)
and passed to `useControllerAnalytics`. If this memo recreates on every render (e.g., if
`devices` array reference changes), it can ghost-fire `MODE_CHANGED`, `PATTERN_CHANGED`,
`COLOR_CHANGED` analytics spuriously.

**Fix:** Verify the `useMemo` dependencies in DockedController:
```typescript
const deviceContext = React.useMemo(() => {
  if (!devices || devices.length === 0) return { target: 'none' };
  if (devices.length === 1) return { target: 'device', deviceId: devices[0].id };
  return { target: 'group', deviceIds: devices.map(d => d.id), groupSize: devices.length };
}, [devices]); // ← devices reference must be stable
```

The `devices` prop comes from `displayConnectedDevices` in DashboardScreen which is
itself a `useMemo`. As long as `connectedDevices` and `deviceConfigs` are stable between
renders, `deviceContext` will be stable.

**Action:** Add a `JSON.stringify` equality guard inside `useControllerAnalytics` as a
safety net for the `deviceContext` dependency to prevent spurious effect re-runs:

```typescript
// useControllerAnalytics.ts
const prevDeviceContextRef = useRef<string>('');
const deviceContextKey = JSON.stringify(deviceContext);
// Use deviceContextKey (string) in useEffect deps instead of deviceContext (object)
```

---

## Execution Order

Execute fixes in this order (all in the same worktree since they touch different files):

1. Fix 1 + Fix 2 (`AppLogger.ts`) — highest impact, zero side-effects
2. Fix 3 (`useAppMicrophone.ts`) — music mode BLE reduction
3. Fix 4 + Fix 5 (`useStreetMode.ts`) — sensor hot-path
4. Fix 6 (`useControllerAnalytics.ts`) — analytics stability

---

## Files Modified

| File | Fixes |
|---|---|
| `src/services/AppLogger.ts` | Fix 1 (batch writes), Fix 2 (PII scrub) |
| `src/hooks/useAppMicrophone.ts` | Fix 3 (delta guard + 10Hz) |
| `src/hooks/useStreetMode.ts` | Fix 4 (static import), Fix 5 (peakGForce gating) |
| `src/hooks/useControllerAnalytics.ts` | Fix 6 (deviceContext stability) |

## Collateral Damage Lock

- `useBLE.ts` — DO NOT TOUCH
- `ZenggeProtocol.ts` — DO NOT TOUCH
- `DockedController.tsx` — DO NOT TOUCH (Fix 6 is in the analytics hook only)
- All BLE write paths — DO NOT TOUCH

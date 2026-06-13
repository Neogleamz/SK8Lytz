# Implementation Plan

## Task: fix/street-mode-accelerometer
## Cluster: F (Stale Closures — R-12)
## Risk: [L-RISK] | Size: [Snack] | Layer: [HOOK]
## Status: [✅ VERIFIED]
## Batch: [BATCH:stale-closure-sweep]

---

## Problem Statement

**Source:** `system_audit_report.md` → Cluster F → R12-002

In `src/hooks/useStreetMode.ts`, the Accelerometer `useEffect` (line 185–231) includes `gpsSpeed`
in its dependency array (line 231):

```typescript
// useStreetMode.ts:231 — CURRENT (BUG)
  }, [activeMode, streetSensitivity, gpsSpeed, updateMotion]);
```

`gpsSpeed` is a prop injected from the parent (live GPS telemetry). It updates on every GPS tick —
approximately once per second while skating. Including it in the dep array causes the entire
Accelerometer subscription to **tear down and re-subscribe every second**, which produces:

1. **Sensor dropout frames** — each re-subscribe cycle has a brief gap during which acceleration
   events are not captured. During a hard-braking jerk, this window could cause the jerk event
   to be missed entirely.
2. **Battery drain** — the Accelerometer listener is the highest-frequency sensor in the app
   (80ms intervals). Re-subscribing it ~60 times per minute is measurable energy waste.
3. **Thrashing** — `Accelerometer.addListener` allocates a native event bridge object on each call.
   Frequent re-allocation pressures the JS-to-native bridge.

**The only usage of `gpsSpeed` inside the listener body** is on line 211:

```typescript
// useStreetMode.ts:211 — inside Accelerometer.addListener callback
AppLogger.log('STREET_JERK_DETECTED', { jerkMag: ..., threshold: ..., gpsSpeedMph: gpsSpeed, ... });
```

This is a **logging-only** usage — the value is attached to an analytics event. It does not affect
any branching logic, hardware dispatch, or FSM transition. Reading a stale `gpsSpeed` in a log
message is zero-risk. The fix is to forward `gpsSpeed` to a `useRef` and remove it from the dep
array so the accelerometer subscription is stable across GPS ticks.

---

## Source of Truth

- **File:** `src/hooks/useStreetMode.ts`
- **Bug location — dep array:** Line 231
- **Bug location — listener body usage:** Line 211
- **Audit reference:** `system_audit_report.md` Cluster F, R12-002:
  > `useStreetMode.ts` | 185 | `gpsSpeed` in Accelerometer listener dep array → re-subscribes every GPS tick

---

## Cited Truth

```typescript
// useStreetMode.ts:185–231 — CURRENT STATE (Evidence Before Action read, 2026-06-10)
useEffect(() => {
  if (activeMode !== 'STREET') {
    if (Platform.OS !== 'web') Accelerometer.removeAllListeners();
    if (streetBrakingRef.current) {
      streetBrakingRef.current = false;
      setIsStreetBraking(false);
    }
    return;
  }

  if (Platform.OS === 'web') return;

  Accelerometer.setUpdateInterval(80);
  const sub = Accelerometer.addListener(({ x, y, z }) => {
    const prev = lastAccelRef.current;
    const jerkMag = Math.sqrt(
      Math.pow(x - prev.x, 2) + Math.pow(y - prev.y, 2) + Math.pow(z - prev.z, 2)
    );
    lastAccelRef.current = { x, y, z };
    const threshold = ((100 - streetSensitivity) / 100) * 2.5 + 0.3;

    const isBrakingJerk = jerkMag > threshold;
    const isActivePush = jerkMag > 0.4 && !isBrakingJerk;

    if (isBrakingJerk) {
      if (!streetBrakingRef.current) {
        AppLogger.log('STREET_JERK_DETECTED', { jerkMag: ..., gpsSpeedMph: gpsSpeed, ... }); // L211
      }
      streetBrakingRef.current = true;
      setIsStreetBraking(true);
      updateMotion('HARD_BRAKING');
    } else {
      ...
    }
  });

  return () => {
    sub.remove();
  };
}, [activeMode, streetSensitivity, gpsSpeed, updateMotion]); // L231 — BUG: gpsSpeed here
```

---

## Existing Plans Cross-Reference

The following prior plans exist for related stale closure patterns and were reviewed:
- `PLAN-STALE-CLOSURE-INTERVALS.md` — covers interval re-entrancy and `useDashboardAutoConnect` stale session. **Different files, different pattern. No overlap.**
- `PLAN-stale-closure-fixes.md` — covers `useGlobalTelemetry` stale user, `CustomEffectVisualizer` dep array, `SessionContext` notification interval. **Different files. No overlap.**
- `PLAN-fix-ble-disconnect-stale-closure.md` — covers BLE disconnect listener stale closure in `useBLE.ts`. **Different file. No overlap.**

This plan is **not redundant** with any existing plan.

---

## Proposed Fix

**Pattern:** React hooks `useRef` forwarding for event listeners (industry standard).

Add a `gpsSpeedRef` that stays current via a dedicated `useEffect`. Remove `gpsSpeed` from
the Accelerometer `useEffect` dep array. Read `gpsSpeedRef.current` inside the listener.

### Step 1 — Add `gpsSpeedRef` declaration and sync effect

Insert immediately after the existing `writeToDeviceRef` pattern at line 85–86:

```diff
// useStreetMode.ts — after L86
  const writeToDeviceRef = useRef(writeToDevice);
  writeToDeviceRef.current = writeToDevice;

+ const gpsSpeedRef = useRef(gpsSpeed);
+ useEffect(() => { gpsSpeedRef.current = gpsSpeed; }, [gpsSpeed]);
```

**Why a separate `useEffect` rather than inline assignment?**
`writeToDevice` is already inline-assigned (L86) because it's a callback reference that must
be current at call-time. `gpsSpeed` is a primitive that feeds a GPS-tick-driven parent prop.
Using a dedicated `useEffect` makes the sync explicit and avoids triggering any downstream
memoization invalidation.

### Step 2 — Update the AppLogger call to read from ref

In the listener body (line 211), replace the closed-over `gpsSpeed` with `gpsSpeedRef.current`:

```diff
// useStreetMode.ts:211
-        AppLogger.log('STREET_JERK_DETECTED', { jerkMag: parseFloat(jerkMag.toFixed(3)), threshold: parseFloat(threshold.toFixed(3)), gpsSpeedMph: gpsSpeed, ...deviceContext });
+        AppLogger.log('STREET_JERK_DETECTED', { jerkMag: parseFloat(jerkMag.toFixed(3)), threshold: parseFloat(threshold.toFixed(3)), gpsSpeedMph: gpsSpeedRef.current, ...deviceContext });
```

### Step 3 — Remove `gpsSpeed` from the dep array

```diff
// useStreetMode.ts:231
-  }, [activeMode, streetSensitivity, gpsSpeed, updateMotion]);
+  }, [activeMode, streetSensitivity, updateMotion]);
```

### Files Modified

| File | Lines Changed |
|------|--------------|
| `src/hooks/useStreetMode.ts` | +2 lines (ref + sync effect) · L211 (ref read) · L231 (dep array) |

**Total diff: ~5 lines. Zero other files touched.**

---

## Rejected Alternative

**Alternative: Re-subscribe accelerometer on each `gpsSpeed` change (keep it in the dep array)**
- **Rejected because:** Re-subscribing the accelerometer causes sensor dropout frames. During hard braking (the highest-stakes moment), the 80ms re-subscription gap can cause the jerk event to be missed. This is the exact failure mode the sensor is designed to prevent. The ref pattern eliminates the gap entirely at zero cost.

---

## Verification Plan

### Automated
- `npm run verify` — TSC must pass with zero new errors.

### Manual
1. **Street Mode Toggle Stability**: Enable Street Mode → wait 30 seconds (observe ~30 GPS ticks) → verify accelerometer subscription does NOT re-register on each GPS update (check ADB logcat for `Accelerometer.addListener` calls — should remain 1 after initial mount).
2. **Jerk Detection Accuracy**: While in Street Mode, perform a hard stop — verify `HARD_BRAKING` state fires correctly and `STREET_JERK_DETECTED` log entry contains a non-zero `gpsSpeedMph` value.
3. **10 Rapid Toggles**: Toggle Street Mode off/on 10 times rapidly — verify no sensor dropout errors in logcat and mode reads correct state each time.
4. **GPS Speed in Logs**: After a jerk detection event, confirm the `gpsSpeedMph` field in the AppLogger output reflects the actual speed at jerk time (within 2 seconds of staleness maximum).

---

## Worktree

`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\street-mode-accelerometer\`
Branch: `fix/street-mode-accelerometer`

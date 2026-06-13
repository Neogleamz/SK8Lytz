# Implementation Plan: fix-session-idle-race-summary

## Problem

**BUG-S6 LOW-MED** — In `endSession()` at [SessionContext.tsx L309–350](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L309-L350), the phase is set to `IDLE` at L321 **immediately**, then the SUMMARY push to the watch fires at L334 as fire-and-forget:

```typescript
// L321 — state goes IDLE immediately
setSessionPhase('IDLE');

// ... AsyncStorage write, FGS stop ...

// L329-330 — Android FGS stopped
if (Platform.OS === 'android') {
  notifee.stopForegroundService();
}

// L334-343 — SUMMARY push (fire-and-forget .catch())
WatchBridge.syncSessionState({
  status: 'SUMMARY',
  totalDuration: finalDurationSec,
  // ...
}).catch(/* ... */);
```

**Race condition chain:**
1. `setSessionPhase('IDLE')` → triggers `useGlobalTelemetry` cleanup (refs zeroed, `commitSession()` called)
2. `notifee.stopForegroundService()` at L330 → Android may begin killing background work
3. The SUMMARY push at L334 may not complete before Android reclaims resources
4. The 10-second STOPPED timeout at L346–349 fires against zeroed telemetry refs
5. Watch may never receive the SUMMARY card or show a zeroed-out summary

## Root Cause

`setSessionPhase('IDLE')` is called eagerly to give instant UI feedback on the phone, but it triggers a cascade of side effects (FGS teardown, ref zeroing) that race against the watch data delivery. There's no intermediate "ending" state.

## Proposed Fix

Add an `ENDING` transitional phase. The lifecycle becomes: `ACTIVE` → `ENDING` → `IDLE`. During `ENDING`, the phone UI shows idle-like state but the FGS stays alive and telemetry refs remain valid until the SUMMARY push completes.

### Step 1: Extend the SessionPhase type (L13, L26)

Update the type union everywhere it appears:

```typescript
// L13
sessionPhase: 'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING';

// L26
const [sessionPhase, setSessionPhase] = useState<'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING'>('IDLE');
```

Also update the `useGlobalTelemetry` parameter type at [useGlobalTelemetry.ts L32](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGlobalTelemetry.ts#L32):

```typescript
export function useGlobalTelemetry(
  sessionPhase: 'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING',
```

### Step 2: Update `isSkateSessionActive` (L30)

`ENDING` should still keep the FGS alive but NOT accumulate new GPS data:
```typescript
const isSkateSessionActive = sessionPhase === 'ACTIVE' || sessionPhase === 'PAUSED' || sessionPhase === 'ENDING';
```

### Step 3: Rewrite `endSession` to use ENDING → IDLE two-phase teardown

Replace L309–350:
```typescript
  const endSession = useCallback(async () => {
    // ── 1. Capture final metrics before state resets ──────────────────────────
    const finalDurationSec = telemetry.sessionDurationSec ?? 0;
    const finalDistanceMiles = telemetry.sessionDistanceMiles ?? 0;
    const finalAvgSpeed =
      finalDistanceMiles > 0 && finalDurationSec > 0
        ? finalDistanceMiles / (finalDurationSec / 3600)
        : 0;
    const finalCalories = health.activeCalories ?? 0;
    const finalPeakHR = health.peakBpm ?? 0;

    // ── 2. Transition to ENDING — phone HUD clears, but FGS stays alive ──────
    setSessionPhase('ENDING');
    setRecoveredStartTimeMs(null);
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'false');
    } catch (err) {
      AppLogger.error('Failed to save session state to AsyncStorage', err);
    }
    AppLogger.log('APP_LOG', { event: 'session_ending' });

    // ── 3. Push SUMMARY → wait for delivery before tearing down ──────────────
    try {
      await WatchBridge.syncSessionState({
        status: 'SUMMARY',
        totalDuration: finalDurationSec,
        distance: finalDistanceMiles,
        avgSpeed: finalAvgSpeed,
        calories: finalCalories,
        peakHR: finalPeakHR,
      });
    } catch (err: unknown) {
      AppLogger.warn('WATCH_BRIDGE', { event: 'summary_push_failed', error: String(err) });
    }

    // ── 4. NOW transition to IDLE — safe to tear down FGS ────────────────────
    setSessionPhase('IDLE');
    AppLogger.log('APP_LOG', { event: 'session_ended' });
    if (Platform.OS === 'android') {
      notifee.stopForegroundService();
    }

    // ── 5. Push STOPPED after 10s (matches watch card auto-dismiss timer) ────
    summaryTimeoutRef.current = setTimeout(() => {
      WatchBridge.syncSessionState({ status: 'STOPPED' }).catch(() => {});
      summaryTimeoutRef.current = null;
    }, 10000);
  }, [telemetry, health]);
```

**Key changes:**
- SUMMARY push is now `await`ed (not fire-and-forget)
- `setSessionPhase('IDLE')` moved **after** SUMMARY delivery confirms
- FGS `stopForegroundService()` moved **after** IDLE transition

### Step 4: Update `useGlobalTelemetry` to NOT zero refs during ENDING

In [useGlobalTelemetry.ts L284–291](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGlobalTelemetry.ts#L284-L291), the cleanup fires when `isSkateSessionActive` goes false. Since we updated `isSkateSessionActive` to include `ENDING`, refs won't zero until `IDLE` is reached. No code change needed here — the type change propagates automatically.

### Step 5: Update notification display to treat ENDING like IDLE (L219)

```typescript
title: sessionPhase === 'PAUSED' ? 'Skate Session Paused ⏸'
     : sessionPhase === 'ENDING' ? 'Saving Session...'
     : 'Skate Session Active 🟢',
```

## Files Modified

| File | Change |
|------|--------|
| `src/context/SessionContext.tsx` | Add `ENDING` to phase type; update `isSkateSessionActive`; rewrite `endSession` with 2-phase teardown; update notification title |
| `src/hooks/useGlobalTelemetry.ts` | Add `ENDING` to `sessionPhase` parameter type |

## Verification

1. Start session → accumulate real telemetry → end session
2. Verify SUMMARY card appears on watch with **correct** non-zero values
3. Verify phone notification briefly shows "Saving Session..." then disappears
4. Verify STOPPED arrives on watch 10 seconds after SUMMARY
5. Check AppLogger for `session_ending` followed by `session_ended` (not simultaneous)
6. Android: verify FGS is NOT killed before SUMMARY delivery (check logcat for `summary_push_failed`)

## Risk Assessment

| Risk | Severity | Mitigation |
|------|----------|------------|
| SUMMARY push hangs (watch unreachable) | Medium | WatchBridge native module has a built-in timeout (5s). If it fails, the `catch` logs and we still transition to IDLE. |
| `ENDING` state visible in UI | Low | Notification shows "Saving Session..." — intentional. Phone HUD should treat ENDING like IDLE. |
| Type change propagation | Low | Only `useGlobalTelemetry` accepts the phase type directly. All other consumers use `isSkateSessionActive` boolean. |
| `ENDING` state persisted to AsyncStorage | None | We write `'false'` to `@sk8lytz_session_active` during ENDING. If app crashes during ENDING, restart reads `'false'` → IDLE. Correct. |

**Rollback**: `git checkout -- src/context/SessionContext.tsx src/hooks/useGlobalTelemetry.ts`

# Implementation Plan: fix-session-watch-stale-closure

## Problem

**BUG-S1 CRITICAL** — The watch command listener in [SessionContext.tsx L60–91](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L60-L91) subscribes to `WatchBridge.addWatchCommandListener` inside a `useEffect` with an **empty `[]` dependency array** (L91). The callback captures `startSession` and `endSession` by value at mount time and never updates.

`startSession` (L279–307) and `endSession` (L309–350) are `useCallback` functions whose closures capture `telemetry` and `health`. When the watch sends `STOP_SESSION`, the `endSession` in the stale closure reads the **initial** `telemetry` and `health` values — all zeros.

**Concrete data path:**
1. User starts session from phone → `telemetry` accumulates real speed/distance/duration
2. Watch sends `STOP_SESSION` → listener at L60 fires `endSession()`
3. The `endSession` captured at mount reads `telemetry.sessionDurationSec = 0`, `telemetry.sessionDistanceMiles = 0`, `health.activeCalories = 0`, etc.
4. Watch receives a SUMMARY card with **all zeros**

**Contrast:** The notification bar's "END SESSION" button works correctly because its listener at L353–360 has `[endSession]` in its dependency array.

## Root Cause

React's `useCallback` returns a new function identity when its dependency array changes. The `useEffect` at L43–91 captures the initial `startSession`/`endSession` references and never re-subscribes because its dependency array is `[]`. Adding `startSession`/`endSession` to the deps would cause listener churn (unsubscribe → resubscribe on every telemetry tick). The correct pattern is to use **refs** that always point to the latest function.

## Proposed Fix

Use mutable refs for `startSession` and `endSession`. Update the refs on every render. The listener callback reads from the refs, ensuring it always calls the latest version without needing to re-subscribe.

### Step 1: Add refs for the session functions (after L32)

```typescript
// Refs to always-current session functions — avoids stale closures in stable listeners
const startSessionRef = React.useRef<(externalStartTimeMs?: number) => void>(() => {});
const endSessionRef = React.useRef<() => void>(() => {});
```

### Step 2: Sync refs after `startSession` and `endSession` definitions (after L307 and L350)

After the `startSession` useCallback (currently ending at L307):
```typescript
  }, []);
  startSessionRef.current = startSession;
```

After the `endSession` useCallback (currently ending at L350):
```typescript
  }, [telemetry, health]);
  endSessionRef.current = endSession;
```

### Step 3: Update the watch listener to read from refs (L60–64)

Replace:
```typescript
    const unsubscribeCmd = WatchBridge.addWatchCommandListener((command: WatchCommand) => {
      AppLogger.log('APP_LOG', { event: 'watch_command_received', command });
      if (command === 'START_SESSION') startSession();
      else if (command === 'STOP_SESSION') endSession();
    });
```

With:
```typescript
    const unsubscribeCmd = WatchBridge.addWatchCommandListener((command: WatchCommand) => {
      AppLogger.log('APP_LOG', { event: 'watch_command_received', command });
      if (command === 'START_SESSION') startSessionRef.current();
      else if (command === 'STOP_SESSION') endSessionRef.current();
    });
```

### Step 4: Update the health listener auto-recovery (L82)

Replace:
```typescript
        startSession(update.startTimeMs);
```

With:
```typescript
        startSessionRef.current(update.startTimeMs);
```

### Step 5: eslint-disable comment stays valid

The `[]` dependency array at L91 is now **correct** — the effect truly has no reactive dependencies because it only reads from refs. The `eslint-disable-next-line` comment remains appropriate.

## Files Modified

| File | Change |
|------|--------|
| `src/context/SessionContext.tsx` | Add `startSessionRef` + `endSessionRef` refs; sync after useCallback definitions; update listener to dereference refs |

## Verification

1. Start a session from the **phone**
2. Skate for ≥30 seconds (accumulate real telemetry)
3. Tap **Stop** on the **watch**
4. Verify the SUMMARY card on the watch shows **non-zero** duration, distance, and calories
5. Verify `AppLogger` output for `session_ended` event contains non-zero `finalDurationSec`
6. Verify phone-side notification "END SESSION" button still works correctly
7. Verify watch-initiated START_SESSION still works

## Risk Assessment

| Risk | Severity | Mitigation |
|------|----------|------------|
| Ref not updated before listener fires | Very Low | Refs are updated synchronously during render, before any async event can fire |
| Memory leak from refs | None | Refs are owned by the component and GC'd on unmount |
| Regression to notification button | None | Notification listener at L353–360 uses `[endSession]` in deps — unaffected |

**Rollback**: `git checkout -- src/context/SessionContext.tsx`

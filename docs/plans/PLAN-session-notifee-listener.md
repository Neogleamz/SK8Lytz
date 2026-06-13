# Implementation Plan

## Task: fix/session-notifee-listener
## Cluster: F (Stale Closures — R-12)
## Risk: [L-RISK] | Size: [Snack] | Layer: [CLOUD]
## Status: [✅ VERIFIED]
## Batch: [BATCH:stale-closure-sweep]

---

## Problem Statement

**Source:** `system_audit_report.md` → Cluster F → R12-003

In `src/context/SessionContext.tsx`, the `notifee.onForegroundEvent` listener is registered in a
`useEffect` that includes `endSession` in its dependency array (line 427):

```typescript
// SessionContext.tsx:420–427 — CURRENT (BUG)
useEffect(() => {
  return notifee.onForegroundEvent(({ type, detail }) => {
    if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
      AppLogger.log('APP_LOG', { event: 'end_session_from_notification' });
      endSession();           // <-- closed-over endSession
    }
  });
}, [endSession]);             // <-- L427: endSession in dep array
```

`endSession` is defined as a `useCallback` at line 371 with the dependency array `[telemetry, health]`
(line 416). Both `telemetry` and `health` are live objects updated frequently during an active skate
session. As a result:

1. `endSession` gets a new reference on every telemetry update.
2. The `useEffect` at line 420 tears down and re-registers the `notifee.onForegroundEvent` listener
   on every telemetry update.
3. This creates **two risks**:
   - **Listener accumulation**: If `notifee.onForegroundEvent` does not properly clean up the old
     listener when the effect cleanup runs, each re-registration adds another listener. Tapping
     "END SESSION" on the notification could fire `endSession` multiple times, causing duplicate
     session-end events, duplicate Supabase writes, and watch SUMMARY pushes.
   - **Session-end races**: In the brief teardown/re-registration window, a notification tap
     could be missed entirely.

**The fix already exists 3 lines above the bug.** At line 59, `endSessionRef` is declared:
```typescript
// SessionContext.tsx:59 — ALREADY EXISTS
const endSessionRef = React.useRef<() => void>(() => {});
```

And at line 417, it is kept current after every `endSession` recreation:
```typescript
// SessionContext.tsx:417 — ALREADY EXISTS
endSessionRef.current = endSession;
```

The correct fix is to make the `notifee.onForegroundEvent` `useEffect` depend on `[]` (registered
once on mount) and call `endSessionRef.current()` instead of the closed-over `endSession()`.

---

## Source of Truth

- **File:** `src/context/SessionContext.tsx`
- **Bug location — dep array:** Line 427
- **Bug location — stale call:** Line 424
- **Fix anchor — existing ref:** Line 59 (`endSessionRef` declaration)
- **Fix anchor — ref sync:** Line 417 (`endSessionRef.current = endSession`)
- **Audit reference:** `system_audit_report.md` Cluster F, R12-003:
  > `SessionContext.tsx` | 420 | `notifee.onForegroundEvent` depends on `endSession` which changes every render

---

## Cited Truth

```typescript
// SessionContext.tsx:59 — endSessionRef already declared
const endSessionRef = React.useRef<() => void>(() => {});

// SessionContext.tsx:416–417 — endSession useCallback + ref kept current
  }, [telemetry, health]);       // endSession dep array — changes every telemetry tick
  endSessionRef.current = endSession;  // already synced here

// SessionContext.tsx:420–427 — CURRENT BUG
useEffect(() => {
  return notifee.onForegroundEvent(({ type, detail }) => {
    if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
      AppLogger.log('APP_LOG', { event: 'end_session_from_notification' });
      endSession();           // closes over stale endSession reference
    }
  });
}, [endSession]);             // triggers re-registration every telemetry update
```

---

## Existing Plans Cross-Reference

The following prior plans exist for related stale closure patterns and were reviewed:
- `PLAN-STALE-CLOSURE-INTERVALS.md` — references `SessionContext.tsx:313` for a setInterval
  notification update pattern. **Different line, different issue (interval re-entrancy guard vs.
  onForegroundEvent dep array). No overlap.**
- `PLAN-stale-closure-fixes.md` — references `SessionContext.tsx:319` for the notification
  interval thrashing issue. **Different line (319 vs. 420), different pattern. No overlap.**
- `PLAN-fix-ble-disconnect-stale-closure.md` — covers BLE disconnect listener in `useBLE.ts`.
  **Different file. No overlap.**

This plan is **not redundant** with any existing plan.

---

## Proposed Fix

**Pattern:** React hooks `useRef` forwarding for stable event listeners (industry standard).
`endSessionRef` already exists and is already kept current. The fix is a 2-line change to the
`notifee.onForegroundEvent` `useEffect` only.

### Step 1 — Update the listener to call via ref

Change line 424 to call `endSessionRef.current()` instead of `endSession()`:

```diff
// SessionContext.tsx:420–427
  useEffect(() => {
    return notifee.onForegroundEvent(({ type, detail }) => {
      if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
        AppLogger.log('APP_LOG', { event: 'end_session_from_notification' });
-       endSession();
+       endSessionRef.current();
      }
    });
- }, [endSession]);
+ }, []);
```

### Step 2 — Remove `endSession` from the ESLint exhaustive-deps

The `eslint-disable-next-line react-hooks/exhaustive-deps` comment is **not needed** here because
`endSessionRef` is a stable ref (never changes identity). The dep array `[]` is correct and will
not trigger an ESLint warning. No suppression comment is required.

### Files Modified

| File | Lines Changed |
|------|--------------|
| `src/context/SessionContext.tsx` | L424 (call site) · L427 (dep array) |

**Total diff: 2 lines. Zero other files touched. `endSessionRef` is already in place — no new
infrastructure required.**

---

## Rejected Alternative

**Alternative: Re-register the `notifee.onForegroundEvent` listener using a `useEffect` with
`endSession` in the dep array and ensuring proper teardown on each cycle.**
- **Rejected because:** `notifee.onForegroundEvent` returns an unsubscribe function. React's
  `useEffect` cleanup calls the unsubscribe on each re-run, which means the old listener IS
  removed before a new one is registered. However, the teardown/re-registration introduces a
  window where a notification tap can be missed. Additionally, re-registering on every telemetry
  tick (once per second during a session) is measurable overhead on the Notifee native bridge.
  The ref pattern eliminates both risks entirely at zero cost, using infrastructure that already
  exists in the file.

---

## Verification Plan

### Automated
- `npm run verify` — TSC must pass with zero new errors.

### Manual
1. **Notification Tap After Session Change**: Start a skate session → wait 15 seconds for telemetry
   to generate several `endSession` recreations → tap "END SESSION" on the persistent notification →
   verify the session ends exactly once (check `AppLogger` for a single `end_session_from_notification`
   event and a single `session_ended` event).
2. **Listener Registration Frequency**: In ADB logcat, confirm `notifee.onForegroundEvent` is NOT
   re-registering on every telemetry update (log should show exactly one registration on mount).
3. **Session Phase Correctness**: After tapping notification, confirm `sessionPhase` transitions
   `ACTIVE → ENDING → IDLE` in the correct sequence (not fired twice or skipped).
4. **Watch Sync**: After notification tap end, confirm WatchBridge receives exactly one `SUMMARY`
   push followed by one `STOPPED` push after 10 seconds.

---

## Worktree

`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\session-notifee-listener\`
Branch: `fix/session-notifee-listener`

# Implementation Plan
# refactor/session-context-xstate — SessionContext XState Rewrite

**Wave:** 2 (requires Wave 1 merged)
**Worktree:** `session-context-xstate`
**Type:** Single file rewrite — `SessionContext.tsx` only

## Source of Truth
- `src/services/session/SessionMachine.ts` (Wave 1 output — must be merged)
- `src/services/session/SessionBridge.ts` (Wave 1 output — must be merged)
- `src/hooks/useBLE.ts:177` — `useMachine(machine, { input })` wiring pattern
- `src/context/SessionContext.tsx` — current 474-line implementation (READ IN FULL before touching)
- `src/constants/storageKeys.ts` — `STORAGE_PENDING_BG_END`, `STORAGE_AUTO_PAUSE_ENABLED` keys

## Steps

### Step 1 — Read current SessionContext.tsx in full
- `view_file` lines 1–474 before writing a single character
- Verify: No line in the existing file is unknown before proceeding

### Step 2 — Replace SessionProvider internals with useMachine
Keep:
- `SessionContext` createContext definition (line 24)
- `NOTIFICATION_CHANNEL_ID`, `NOTIFICATION_ID` constants (lines 26–27)
- `SESSION_PHASE_KEY`, `SESSION_PAUSE_TIME_KEY` constants (lines 31–32)
- `persistSessionPhase` function (lines 34–49) — still used by machine actions
- `useSession()` export (lines 469–473) — identical, untouched

Remove:
- All `useState` FSM calls
- All `useEffect` chains for auto-pause, watch sync, notification, crash recovery
- `useGlobalTelemetry` import and call (line 6, 66–70)
- `useHealthTelemetry` import and call (line 7, 65)
- `summaryTimeoutRef` (line 58) — moved into machine's ENDING state
- `startSession` / `endSession` useCallback implementations — replaced by `send({ type: 'START/END' })`

Add:
```tsx
const [snapshot, send] = useMachine(sessionMachine, {
  input: {
    gpsSpeedRef,
    onTelemetryUpdate: (t) => { telemetryRef.current = t; gpsSpeedRef.current = t.gpsSpeed; },
    onHealthUpdate:    (h) => { healthRef.current = h; },
    onSessionSaved:    () => invalidateStatsCache(),
  }
});
useEffect(() => { SessionBridge.register(send); return () => SessionBridge.unregister(); }, [send]);
```

Crash recovery (replaces `SessionContext.tsx:128–182`):
```tsx
useEffect(() => {
  const recover = async () => {
    const pendingEnd = await AsyncStorage.getItem(STORAGE_PENDING_BG_END);
    if (pendingEnd === 'true') {
      await AsyncStorage.removeItem(STORAGE_PENDING_BG_END);
      if (!snapshot.matches('IDLE')) SessionBridge.send({ type: 'END' });
      return;
    }
    const phase = await AsyncStorage.getItem(SESSION_PHASE_KEY);
    if (phase === 'active' && snapshot.matches('IDLE'))  SessionBridge.send({ type: 'START' });
    if (phase === 'paused' && snapshot.matches('IDLE'))  {
      SessionBridge.send({ type: 'START' });
      SessionBridge.send({ type: 'PAUSE' });
    }
  };
  recover();
  const sub = AppState.addEventListener('change', s => { if (s === 'active') recover(); });
  return () => sub.remove();
}, []);
```

Watch listeners (replaces `SessionContext.tsx:93–124`):
```tsx
useEffect(() => {
  const unsubCmd = WatchBridge.addWatchCommandListener((command: WatchCommand) => {
    if (command === 'START_SESSION') SessionBridge.send({ type: 'START' });
    if (command === 'STOP_SESSION')  SessionBridge.send({ type: 'END' });
  });
  const unsubHealth = WatchBridge.addWatchHealthListener((update: WatchHealthUpdate) => {
    if (update.status === 'ACTIVE' && snapshot.matches('IDLE')) {
      SessionBridge.send({ type: 'START', externalStartTimeMs: update.startTimeMs });
    }
  });
  return () => { unsubCmd(); unsubHealth(); };
}, []);
```

1s ticker (replaces telemetry useState churn):
```tsx
useEffect(() => {
  if (!snapshot.matches('ACTIVE') && !snapshot.matches('PAUSED')) return;
  const id = setInterval(() => {
    const elapsed = snapshot.context.startTimeMs
      ? Math.floor((Date.now() - snapshot.context.startTimeMs - snapshot.context.pausedMsAccum) / 1000)
      : 0;
    setTelemetry({ ...telemetryRef.current, sessionDurationSec: elapsed });
    setHealth({ ...healthRef.current });
  }, 1000);
  return () => clearInterval(id);
}, [snapshot.value]);
```

### Step 3 — Verify notifee iOS category registration still present
- `notifee.setNotificationCategories` must still be called on iOS mount
- Add `PAUSE` / `RESUME` action IDs to the iOS category (alongside existing `end-session`)
- Source: `SessionContext.tsx:78–90`
- Verify: iOS category registration fires on mount

### Step 4 — Post-edit diff check
```powershell
git diff HEAD src/context/SessionContext.tsx
```
- Verify: No `useGlobalTelemetry` or `useHealthTelemetry` imports remain
- Verify: `useSession()` return shape is unchanged
- Verify: `SessionContext.Provider value=` prop shape is unchanged

### Step 5 — Run verify
```powershell
npm run verify
```
- Verify: TSC 0 errors, Jest passes

## Out of Scope
- Deletion of `useGlobalTelemetry.ts` / `useHealthTelemetry.ts` (Wave 3A — separate worktree)
- UI badge changes (Wave 3B, 3C)
- `App.tsx` background handler wiring (Wave 3A)

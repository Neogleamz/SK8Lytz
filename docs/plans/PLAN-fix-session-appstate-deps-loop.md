# Implementation Plan: fix-session-appstate-deps-loop

## Problem

**BUG-S2 HIGH** — The AppState sync effect in [SessionContext.tsx L94–124](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L94-L124) has `sessionPhase` in its dependency array (L124). Inside the effect, `syncSessionState()` reads `sessionPhase` (L100, L104) and conditionally calls `setSessionPhase()` (L101, L105).

**Effect chain:**
1. Mount → effect runs → reads AsyncStorage → may call `setSessionPhase('ACTIVE')`
2. `sessionPhase` changes → **effect re-runs** → registers a **new** AppState listener
3. Previous listener is cleaned up (L121–123), but this is wasteful churn
4. Every `setSessionPhase()` call from *anywhere* (auto-pause, start, end) triggers this effect to re-run and re-register the AppState listener

The guards at L100/L104 prevent true infinite loops, but the effect still fires 3× during a normal session lifecycle (IDLE → ACTIVE → IDLE) when it only needs to fire once.

## Root Cause

The effect needs to **read** `sessionPhase` to decide whether to update it, but it also **writes** `sessionPhase`. Including it in deps creates a feedback loop. The existing `sessionPhaseRef` (L28–29) already tracks the latest value but isn't used here.

## Proposed Fix

Use `sessionPhaseRef.current` inside the effect body instead of the `sessionPhase` state variable. Remove `sessionPhase` from the dependency array. The effect subscribes once on mount and the AppState listener always reads the latest phase from the ref.

### Step 1: Replace the entire effect body (L94–124)

Replace:
```typescript
  useEffect(() => {
    const syncSessionState = async () => {
      try {
        const val = await AsyncStorage.getItem('@sk8lytz_session_active');
        const isActive = val === 'true';
        if (isActive) {
          if (sessionPhase === 'IDLE') {
            setSessionPhase('ACTIVE');
          }
        } else {
          if (sessionPhase !== 'IDLE') {
            setSessionPhase('IDLE');
          }
        }
      } catch (err) {
        AppLogger.error('Failed to sync session state from AsyncStorage', err);
      }
    };

    syncSessionState();

    const subscription = AppState.addEventListener('change', (nextAppState) => {
      if (nextAppState === 'active') {
        syncSessionState();
      }
    });

    return () => {
      subscription.remove();
    };
  }, [sessionPhase]);
```

With:
```typescript
  // 3. Synchronize React state with AsyncStorage on mount & App foreground transitions
  useEffect(() => {
    const syncSessionState = async () => {
      try {
        const val = await AsyncStorage.getItem('@sk8lytz_session_active');
        const isActive = val === 'true';
        if (isActive) {
          if (sessionPhaseRef.current === 'IDLE') {
            setSessionPhase('ACTIVE');
          }
        } else {
          if (sessionPhaseRef.current !== 'IDLE') {
            setSessionPhase('IDLE');
          }
        }
      } catch (err) {
        AppLogger.error('Failed to sync session state from AsyncStorage', err);
      }
    };

    syncSessionState();

    const subscription = AppState.addEventListener('change', (nextAppState) => {
      if (nextAppState === 'active') {
        syncSessionState();
      }
    });

    return () => {
      subscription.remove();
    };
  }, []);
```

**Key changes:**
- L100: `sessionPhase === 'IDLE'` → `sessionPhaseRef.current === 'IDLE'`
- L104: `sessionPhase !== 'IDLE'` → `sessionPhaseRef.current !== 'IDLE'`
- L124: `[sessionPhase]` → `[]`

## Files Modified

| File | Change |
|------|--------|
| `src/context/SessionContext.tsx` | Replace `sessionPhase` reads with `sessionPhaseRef.current` inside AppState sync effect; remove `sessionPhase` from deps |

## Verification

1. Kill app completely, start fresh
2. Verify AsyncStorage sync on mount still works (start session → kill app → relaunch → session resumes)
3. Background the app → foreground it → verify `syncSessionState` fires once (check AppLogger)
4. Start/pause/end a session — verify the AppState listener is NOT torn down and re-registered (no `subscription.remove()` calls between phase transitions)
5. Verify no double ACTIVE flicker on app launch with an existing session

## Risk Assessment

| Risk | Severity | Mitigation |
|------|----------|------------|
| Ref lags behind state by 1 tick | Very Low | `sessionPhaseRef` is updated in a `useEffect([sessionPhase])` at L29 — fires synchronously in the same commit batch |
| Missing AppState re-sync after phase change | None | The AppState listener fires on foreground transitions, not phase changes. Phase-driven re-sync was never intentional. |

**Rollback**: `git checkout -- src/context/SessionContext.tsx`

# Implementation Plan: fix-session-paused-persistence

## Problem

**BUG-S5 MEDIUM** — AsyncStorage key `@sk8lytz_session_active` only stores `'true'` or `'false'`. There is no `'paused'` value. If the app crashes or is killed by the OS while the session is in `PAUSED` phase:

1. On restart, [SessionContext.tsx L97–107](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L97-L107) reads `'true'` → sets phase to `ACTIVE`
2. The session was paused but now shows active — **wrong state**
3. No pause anchor is recorded, so `useGlobalTelemetry` has no `sessionPauseTimeRef` to shift against
4. Duration calculation includes the paused time — **inflated stats**

**Current writes:**
- `startSession()` at L290: `AsyncStorage.setItem('@sk8lytz_session_active', 'true')`
- `endSession()` at L324: `AsyncStorage.setItem('@sk8lytz_session_active', 'false')`
- Auto-pause at L149: `setSessionPhase('PAUSED')` — **no AsyncStorage write** ❌

## Root Cause

The persistence layer was designed as a simple boolean before the PAUSED state was added. The auto-pause feature at [L127–174](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L127-L174) writes to React state but never persists the paused status to AsyncStorage.

## Proposed Fix

Store a 3-state string value in AsyncStorage: `'active'`, `'paused'`, or `'idle'`. Include the pause timestamp so duration correction can be applied on recovery.

### Step 1: Define a storage type (add after L23)

```typescript
/** Persisted session phase for crash recovery. */
type PersistedSessionPhase = 'active' | 'paused' | 'idle';
const SESSION_PHASE_KEY = '@sk8lytz_session_phase';
const SESSION_PAUSE_TIME_KEY = '@sk8lytz_session_pause_time';
```

### Step 2: Create a helper to persist phase (add after the new constants)

```typescript
async function persistSessionPhase(phase: PersistedSessionPhase, pauseTimeMs?: number): Promise<void> {
  try {
    const pairs: [string, string][] = [
      [SESSION_PHASE_KEY, phase],
      ['@sk8lytz_session_active', phase === 'active' || phase === 'paused' ? 'true' : 'false'],
    ];
    if (phase === 'paused' && pauseTimeMs) {
      pairs.push([SESSION_PAUSE_TIME_KEY, String(pauseTimeMs)]);
    } else if (phase !== 'paused') {
      pairs.push([SESSION_PAUSE_TIME_KEY, '']);
    }
    await AsyncStorage.multiSet(pairs);
  } catch (err) {
    AppLogger.error('Failed to persist session phase', err);
  }
}
```

> [!NOTE]
> The legacy `@sk8lytz_session_active` key is maintained as `'true'`/`'false'` for backward compatibility with the background handler in `index.ts` (BUG-S4 fix reads this key).

### Step 3: Update `startSession` (L289–293)

Replace:
```typescript
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'true');
    } catch (err) {
      AppLogger.error('Failed to save session state to AsyncStorage', err);
    }
```

With:
```typescript
    await persistSessionPhase('active');
```

### Step 4: Update `endSession` (L323–327)

Replace:
```typescript
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'false');
    } catch (err) {
      AppLogger.error('Failed to save session state to AsyncStorage', err);
    }
```

With:
```typescript
    await persistSessionPhase('idle');
```

### Step 5: Persist PAUSED state in auto-pause effect (after L149)

After `setSessionPhase('PAUSED')` at L149, add:
```typescript
              setSessionPhase('PAUSED');
              AppLogger.log('APP_LOG', { event: 'auto_pause_triggered' });
              await persistSessionPhase('paused', Date.now());
```

### Step 6: Update AppState sync effect to read 3-state value

Replace the existing `syncSessionState` logic (L95–111) with:
```typescript
    const syncSessionState = async () => {
      try {
        // ... pending bg end check from BUG-S4 (if applied) ...

        const phase = await AsyncStorage.getItem(SESSION_PHASE_KEY);
        if (phase === 'active') {
          if (sessionPhaseRef.current === 'IDLE') {
            setSessionPhase('ACTIVE');
          }
        } else if (phase === 'paused') {
          if (sessionPhaseRef.current === 'IDLE') {
            setSessionPhase('PAUSED');
            // Recover pause timestamp for duration correction
            const pauseTimeStr = await AsyncStorage.getItem(SESSION_PAUSE_TIME_KEY);
            if (pauseTimeStr) {
              AppLogger.log('APP_LOG', { event: 'recovered_paused_session', pauseTimeMs: Number(pauseTimeStr) });
            }
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
```

### Step 7: Persist on auto-resume too

When auto-resume fires at L160:
```typescript
          if (sessionPhase === 'PAUSED') {
            setSessionPhase('ACTIVE');
            AppLogger.log('APP_LOG', { event: 'auto_resume_triggered' });
            persistSessionPhase('active');
          }
```

## Files Modified

| File | Change |
|------|--------|
| `src/context/SessionContext.tsx` | Add `PersistedSessionPhase` type, constants, `persistSessionPhase` helper; update all phase transitions to persist; update sync effect to read 3-state |

## Verification

1. Start session → skate → auto-pause triggers (stop moving for 10s)
2. Force-kill the app while in PAUSED state
3. Relaunch → verify session resumes in **PAUSED** state (not ACTIVE)
4. Resume skating → verify duration does NOT include time spent killed
5. Start session → force-kill while ACTIVE → relaunch → verify ACTIVE
6. End session normally → force-kill → relaunch → verify IDLE

## Risk Assessment

| Risk | Severity | Mitigation |
|------|----------|------------|
| Backward compatibility with `@sk8lytz_session_active` | None | `persistSessionPhase` writes both keys — old background handler and new sync both work |
| First launch after update — no `SESSION_PHASE_KEY` in storage | Low | `phase` will be `null`, falls through to the `else` branch → sets IDLE. The legacy key will still be checked if needed. |
| AsyncStorage write latency during auto-pause | Very Low | `persistSessionPhase` is fire-and-forget async — doesn't block UI |

**Rollback**: `git checkout -- src/context/SessionContext.tsx`

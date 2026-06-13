# Implementation Plan: fix-session-background-end-data-loss

## Problem

**BUG-S4 MEDIUM** — When the user taps "End Session" from the notification bar while the app is **backgrounded**, the handler in [index.ts L9–22](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/index.ts#L9-L22) only performs 3 actions:

```typescript
notifee.onBackgroundEvent(async ({ type, detail }) => {
  if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'false');  // ✅
    } catch (e) {
      console.error('...', e);
    }
    if (detail.notification?.id) {
      await notifee.cancelNotification(detail.notification.id);        // ✅
    }
    await notifee.stopForegroundService();                             // ✅
  }
});
```

**Missing actions:**

| Action | Status | Consequence |
|--------|--------|-------------|
| Watch notified (STOPPED/SUMMARY) | ❌ MISSING | Watch shows ACTIVE forever |
| Session saved to Supabase | ❌ MISSING | Session data silently lost |
| GPS/accel subscriptions cleaned | ❌ MISSING | Battery drain until app killed |
| HealthKit workout finalized | ❌ MISSING | No workout in Apple Health |

The background handler **cannot** access React state (`telemetry`, `health`, `sessionPhase`) because it runs outside the React tree. This is the core challenge.

## Root Cause

The background event handler in `index.ts` was implemented as a minimal "stop the notification" handler. It was never connected to the full session teardown pipeline that lives in `SessionContext.tsx`'s `endSession()` callback. The React context and hooks don't exist in the background handler's execution context.

## Proposed Fix

**Queue a "pending end" flag** in AsyncStorage that `SessionContext` picks up on the next foreground transition. Also push `STOPPED` to the watch directly from the background handler (WatchBridge works outside React — it's a native module).

### Strategy: Hybrid Background + Deferred Foreground

1. **Background (immediate):** Set AsyncStorage flag, push STOPPED to watch, stop FGS
2. **Foreground (deferred):** When app comes to foreground, SessionContext detects the pending-end flag and runs the full teardown (Supabase save, GPS cleanup, etc.)

### Step 1: Update the background handler in `index.ts`

Replace L8–23:
```typescript
// Register Notifee background event handler
notifee.onBackgroundEvent(async ({ type, detail }) => {
  if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'false');
    } catch (e) {
      console.error('Failed to save session active state in background handler', e);
    }

    if (detail.notification?.id) {
      await notifee.cancelNotification(detail.notification.id);
    }

    await notifee.stopForegroundService();
  }
});
```

With:
```typescript
import { WatchBridge } from 'sk8lytz-watch-bridge';

// Register Notifee background event handler
notifee.onBackgroundEvent(async ({ type, detail }) => {
  if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
    try {
      // Mark session as ended + flag for deferred full teardown on foreground
      await AsyncStorage.multiSet([
        ['@sk8lytz_session_active', 'false'],
        ['@sk8lytz_pending_bg_end', 'true'],
      ]);
    } catch (e) {
      console.error('Failed to save session state in background handler', e);
    }

    // Push STOPPED to watch immediately — WatchBridge works outside React
    WatchBridge.syncSessionState({ status: 'STOPPED' }).catch(() => {});

    if (detail.notification?.id) {
      await notifee.cancelNotification(detail.notification.id);
    }

    await notifee.stopForegroundService();
  }
});
```

### Step 2: Handle the pending-end flag in SessionContext's AppState sync effect

In the `syncSessionState` function inside the AppState effect (currently [L95–111](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L95-L111)), add a check for the pending background end flag **before** the existing sync logic:

```typescript
    const syncSessionState = async () => {
      try {
        // Check if a background end was queued while we were backgrounded
        const pendingBgEnd = await AsyncStorage.getItem('@sk8lytz_pending_bg_end');
        if (pendingBgEnd === 'true') {
          await AsyncStorage.removeItem('@sk8lytz_pending_bg_end');
          if (sessionPhaseRef.current !== 'IDLE') {
            AppLogger.log('APP_LOG', { event: 'deferred_bg_end_session' });
            // Fire the full endSession teardown (Supabase save, GPS cleanup, etc.)
            endSessionRef.current();
          }
          return; // Skip normal sync — endSession handles everything
        }

        const val = await AsyncStorage.getItem('@sk8lytz_session_active');
        // ... existing sync logic unchanged ...
      } catch (err) {
        AppLogger.error('Failed to sync session state from AsyncStorage', err);
      }
    };
```

> [!NOTE]
> This depends on PLAN-fix-session-watch-stale-closure (BUG-S1) being applied first, since it introduces `endSessionRef`. If implemented independently, add the ref inline.

### Step 3: Dependency ordering

This plan has a **soft dependency** on BUG-S1 (stale closure fix) because the deferred `endSessionRef.current()` call must capture fresh telemetry. If BUG-S1 is not yet merged, the deferred end will still set IDLE + clear AsyncStorage correctly, but the Supabase save may capture stale/zero telemetry. Apply BUG-S1 first.

## Files Modified

| File | Change |
|------|--------|
| `index.ts` | Add WatchBridge import; set pending-end flag in AsyncStorage; push STOPPED to watch |
| `src/context/SessionContext.tsx` | Add pending-bg-end check in AppState sync effect |

## Verification

1. Start a session → background the app
2. Tap "🛑 END SESSION" from the notification bar
3. Verify watch immediately shows STOPPED/idle state (not stuck on ACTIVE)
4. Foreground the app
5. Verify AppLogger shows `deferred_bg_end_session` event
6. Verify session was committed to Supabase (check `SpeedTrackingService.saveSession` call)
7. Verify GPS subscription was cleaned up (check battery usage, no location icon persisting)
8. Verify notification is gone and FGS is stopped

**Edge case:** App killed by OS while backgrounded before foreground resume → on next cold start, `syncSessionState` fires on mount, detects `@sk8lytz_pending_bg_end = true`, and runs deferred end. Telemetry will be zeros (refs were cleared by OS kill), but at minimum the session state is cleaned up.

## Risk Assessment

| Risk | Severity | Mitigation |
|------|----------|------------|
| Deferred end fires with stale telemetry after OS kill | Medium | Acceptable — better than silent data loss. The session save will have partial data vs. no data. |
| Race between background handler and foreground sync | Low | AsyncStorage operations are serialized. The `pendingBgEnd` flag is checked first. |
| WatchBridge call fails in background | Low | `.catch(() => {})` — watch may stay ACTIVE until the deferred foreground end pushes STOPPED via `endSession()` |
| Double STOPPED push (background + deferred foreground) | None | STOPPED is idempotent on both watchOS and WearOS companions |

**Rollback**: `git checkout -- index.ts src/context/SessionContext.tsx`

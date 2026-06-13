# Implementation Plan: Auto-Pause

## Goal
Automatically pause session tracking when speed = 0 for >10 seconds, auto-resume on movement. Both Strava and Nike Run Club implement this — it prevents idle time from inflating session duration and deflating average speed.

## Proposed Changes

### Phone — Session State Model

#### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)

**Step 1:** Add PAUSED state to session model. Change the boolean to a union type:

```typescript
// Replace boolean with FSM state
type SessionPhase = 'IDLE' | 'ACTIVE' | 'PAUSED';

// In SessionProvider:
const [sessionPhase, setSessionPhase] = useState<SessionPhase>('IDLE');
const isSkateSessionActive = sessionPhase === 'ACTIVE' || sessionPhase === 'PAUSED';
```

**Step 2:** Add auto-pause effect. New `useEffect` after the existing effects:

```typescript
// Auto-pause: pause when speed = 0 for >10 seconds
useEffect(() => {
    if (sessionPhase !== 'ACTIVE' && sessionPhase !== 'PAUSED') return;

    let zeroSpeedTimer: ReturnType<typeof setTimeout> | null = null;

    // Check if auto-pause is enabled (read from settings)
    const checkAutoPause = async () => {
        const setting = await AsyncStorage.getItem('@sk8lytz_auto_pause_enabled');
        if (setting === 'false') return; // Disabled by user

        if (telemetry.gpsSpeed < 0.5) {
            // Speed is ~0 — start countdown if not already paused
            if (sessionPhase === 'ACTIVE' && !zeroSpeedTimer) {
                zeroSpeedTimer = setTimeout(() => {
                    setSessionPhase('PAUSED');
                    AppLogger.log('APP_LOG', { event: 'auto_pause_triggered' });
                    WatchBridge.syncSessionState({ status: 'PAUSED' }).catch(() => {});
                }, 10000);
            }
        } else {
            // Moving — cancel countdown and resume if paused
            if (zeroSpeedTimer) {
                clearTimeout(zeroSpeedTimer);
                zeroSpeedTimer = null;
            }
            if (sessionPhase === 'PAUSED') {
                setSessionPhase('ACTIVE');
                AppLogger.log('APP_LOG', { event: 'auto_resume_triggered' });
                WatchBridge.syncSessionState({ status: 'ACTIVE' }).catch(() => {});
            }
        }
    };

    checkAutoPause();

    return () => {
        if (zeroSpeedTimer) clearTimeout(zeroSpeedTimer);
    };
}, [sessionPhase, telemetry.gpsSpeed]);
```

### Phone — Telemetry Accumulation

#### [MODIFY] [useGlobalTelemetry.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGlobalTelemetry.ts)

Pass `sessionPhase` instead of `isSkateSessionActive` boolean. When phase is `PAUSED`:
- **Stop accumulating distance** (don't add GPS deltas to `sessionDistanceMiles`)
- **Stop incrementing duration** (freeze `durationSec`)
- **Continue GPS polling** (so we can detect movement to auto-resume)
- **Continue health tracking** (HealthKit/ExerciseClient should keep running)

```typescript
// In the GPS location callback, guard distance accumulation:
if (sessionPhase === 'ACTIVE') {
    // Only accumulate distance when actively moving
    sessionDistanceMiles += deltaDistanceMiles;
    durationSec += deltaTimeSec;
}
// Speed updates always flow regardless of phase
```

### Phone — Settings Toggle

#### [MODIFY] Settings component (wherever app settings are rendered)

Add a toggle for auto-pause:

```typescript
const [autoPauseEnabled, setAutoPauseEnabled] = useState(true);

// On mount:
AsyncStorage.getItem('@sk8lytz_auto_pause_enabled').then(val => {
    setAutoPauseEnabled(val !== 'false'); // Default true
});

// Toggle handler:
const toggleAutoPause = async (value: boolean) => {
    setAutoPauseEnabled(value);
    await AsyncStorage.setItem('@sk8lytz_auto_pause_enabled', String(value));
};
```

### Watch — PAUSED State Display

#### [MODIFY] [SessionState.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/SessionState.kt)

Add PAUSED to enum:

```kotlin
enum class SessionState {
    IDLE,
    ACTIVE,
    PAUSED
}
```

#### [MODIFY] [DashboardScreen.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/DashboardScreen.kt)

Add PAUSED case to the `when` block:

```kotlin
when (sessionState) {
    SessionState.IDLE -> IdleView(onStart = { ... })
    SessionState.ACTIVE -> ActiveView(...)
    SessionState.PAUSED -> ActiveView(
        // Same as ACTIVE but with "PAUSED" indicator
        speed = speed, heartRate = heartRate, calories = calories,
        isPaused = true,
        onStop = { ... }
    )
}
```

Add `isPaused` param to `ActiveView` and show pulsing "PAUSED" indicator:

```kotlin
if (isPaused) {
    Text(
        text = "⏸ PAUSED",
        color = Color(0xFFFFAA00), // Amber
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
    )
} else {
    Text(text = "SESSION ACTIVE", ...)
}
```

#### [MODIFY] [ContentView.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/ContentView.swift)

Add PAUSED state handling. In `WatchConnectivityManager`, parse `status: "PAUSED"` and set a `isPaused` published property. In `ContentView.activeSessionView`, conditionally show "⏸ PAUSED" instead of "ACTIVE SESSION".

#### [MODIFY] [WearableCommunicationService.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt)

Parse `"PAUSED"` status in `onDataChanged()`:

```kotlin
currentState = when (status) {
    "ACTIVE" -> SessionState.ACTIVE
    "PAUSED" -> SessionState.PAUSED
    else -> SessionState.IDLE
}
```

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest pass
- Add unit test: simulate 10s of zero-speed GPS readings → verify phase transitions

### Manual Verification
- Start session → skate → stop moving for 10s → watch should show "PAUSED"
- Start moving again → should auto-resume to "SESSION ACTIVE"
- Verify distance/duration do NOT accumulate while paused
- Verify HR continues tracking while paused
- Toggle auto-pause OFF in settings → verify no pause behavior

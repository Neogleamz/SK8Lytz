# PLAN-session-telemetry-auto-start.md — Skate Session Telemetry and Health Sync Fixes

Implement automatic session tracking upon landing on the dashboard, construct a cross-platform OS background notification widget allowing session control/app reopening, resolve mismatched health database category mappings, and sync background actions to local state.

## User Review Required

> [!IMPORTANT]
> The default Android Health permission check has been updated to query both Activity Recognition AND Google Health Connect permissions. Toggling the HEALTH permission in the app's settings will now present the standard Google Health Connect permission page, allowing the user to select heart rate and calorie read permissions.
> 
> A session will now automatically start as soon as the user enters the Dashboard screen to ensure HUD, pill, and street metrics are continuously active. Manual end session from the notification or UI remains fully functional.

## Proposed Changes

### Core Session Lifecycle

#### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
- Persist the logical tracking session `isSkateSessionActive` in `AsyncStorage` under key `@sk8lytz_session_active`.
- Enable cross-platform background notifications via `Notifee` (iOS and Android).
- Set up an iOS notification category `session-actions` with action `end-session` (foreground: true) to allow ending sessions from lock screen.
- On Android, add `pressAction` to open the app on tapping the notification itself, and set Android foreground color to `#00F0FF`.
- Synchronize React state with AsyncStorage on app foreground transitions so changes made in the background are reactively applied.

### Root Initialization

#### [MODIFY] [index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/index.ts)
- Register `notifee.onBackgroundEvent` handler at the app entry point.
- When an action with id `end-session` is pressed in the background, set `@sk8lytz_session_active` to `'false'`, cancel the notification, and stop the foreground service.

### Dashboard Auto-Start

#### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- Add a `useEffect` that listens for `viewState === 'DASHBOARD'`.
- On entering the dashboard state, automatically invoke `startSession()` if the session is not already active.
- Utilize a ref `hasAutoStartedSessionRef` to ensure auto-start is only triggered once on launch/mount, preventing loops when the user manually ends the session.

### Permission Mapping and SDK Sync

#### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- On Android case `'HEALTH'`, request both `PermissionsAndroid.PERMISSIONS.ACTIVITY_RECOGNITION` and Health Connect permissions (`HeartRate`, `ActiveCaloriesBurned` for read; `ExerciseSession`, `TotalCaloriesBurned`, `Distance` for write) using `react-native-health-connect`'s `requestPermission`.
- On Android checking `'HEALTH'`, verify that both Activity Recognition and Health Connect read permissions for Heart Rate and Calories are granted.

### Telemetry & Health Alignment

#### [MODIFY] [HealthSyncService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/HealthSyncService.ts)
- Correct the Android `exerciseType` mapping for Roller Skating from `71` (which is `Stretching`) to `60` (which is `Skating` in `react-native-health-connect`).
- Update session validation: allow syncing workouts if `snapshot.distanceMiles > 0.1` or `snapshot.durationSec > 60` to ensure indoor/rink skaters get credit.

#### [MODIFY] [SpeedTrackingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts)
- Correct the Android `exerciseType` mapping for Roller Skating from `64` (which is `Soccer`) to `60` (which is `Skating` in `react-native-health-connect`).
- Delegate the workout recording and health sync directly to `HealthSyncService.saveWorkout` to eliminate redundant sync implementations and double-writes.

#### [MODIFY] [useGlobalTelemetry.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGlobalTelemetry.ts)
- Change the save threshold from `distanceMiles > 0.2` to `distanceMiles > 0.1 || durationSec > 60` to match the updated rink-skater validation.
- Remove duplicate/redundant inline call to `HealthSyncService.saveWorkout` since it is now delegated to `SpeedTrackingService.saveSession`.

---

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure TypeScript compilation passes and verifiable check runners complete successfully.

### Manual Verification
- Launch the application, navigate past loading screen to the Dashboard, and verify that the session timer and background notification immediately kick off.
- Bring the app to the background, check that the OS-level notification displays speed and distance.
- Press the "End Session" action button on the notification and verify the notification disappears and the session stops.
- Open the application again and verify that the session status is correctly updated to inactive.
- Verify Health Connect sync registers Skating (ID 60) instead of Soccer/Stretching on Android emulator/device.

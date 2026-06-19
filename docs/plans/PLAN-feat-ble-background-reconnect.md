# Implementation Plan: feat/ble-background-reconnect

## Problem
When the app backgrounds, BLE connections drop. Skaters put their phone in their pocket and the lights stop responding. Every competitor (Govee, Hue, LIFX) maintains connection in the background.

This is the **#1 UX gap** identified in the industry comparison.

Source: No background mode configuration exists. BleManager is initialized without `restoreStateIdentifier`. No foreground service on Android.

## Prerequisites
- **PLAN-feat-ble-scan-filter-uuid** MUST be merged first — iOS background scanning requires Service UUID filters
- **PLAN-refactor-ble-gatt-operation-queue** should be merged — background operations need queue serialization

## Files to Create/Modify

#### [NEW] [BackgroundBLEService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BackgroundBLEService.ts)
- Central service managing background BLE lifecycle
- iOS: State restoration handler
- Android: Foreground service notification manager
- Both: AppState listener (background/foreground transitions), auto-reconnect on wake

#### [MODIFY] [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- BleManager initialization: add `restoreStateIdentifier: 'sk8lytz-ble'` (iOS)
- Add `restoreStateFunction` callback to handle state restoration on iOS wake
- Wire AppState listener for background/foreground transitions

#### [MODIFY] [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts)
- Add foreground re-entry logic: when app comes to foreground, check connection state and trigger reconnect if needed

#### [MODIFY] iOS Info.plist
// SKIPPED: Already configured in app.config.js (UIBackgroundModes includes bluetooth-central)

#### [MODIFY] AndroidManifest.xml
// SKIPPED: Already configured in app.config.js (FOREGROUND_SERVICE and FOREGROUND_SERVICE_CONNECTED_DEVICE)

#### [MODIFY] app.json / app.config.ts (Expo config)
// SKIPPED: Already configured in app.config.js

#### [DEPENDENCY] react-native-background-actions (or expo-task-manager)
- Android foreground service — requires dependency proposal approval
- Alternative: expo-task-manager (already in Expo ecosystem, may be lighter)

## Steps

### Step 1: iOS State Restoration Setup
- Add `restoreStateIdentifier` to BleManager constructor
- Add `restoreStateFunction` to handle restored peripherals
- Add `bluetooth-central` to Info.plist UIBackgroundModes
- Verify: iOS simulator shows BLE background mode enabled

### Step 2: Android Foreground Service
- Dependency proposal for background task library (user approval required)
- Create persistent notification: "SK8Lytz — Connected to your skates"
- Start foreground service when devices are connected
- Stop foreground service when all devices disconnect
- Verify: Android notification appears, BLE stays alive in background

### Step 3: AppState transition handler
- On background entry: start keepalive (iOS: nothing extra needed, Android: foreground service)
- On foreground entry: check connection state → auto-reconnect if stale
- Wire with useDashboardAutoConnect
- Verify: Background → Foreground reconnects within 2s

### Step 4: Integration testing
- Connect to device → background app → wait 30s → foreground → verify still connected
- Verify: Connection survives background on both platforms

## Out of Scope
- GATT operation queue internals (separate plan)
- Connection parameters (separate plan)
- UI state badges (separate plan)
- Smart group health (separate plan)

## Risk Assessment
- **iOS**: State restoration is well-documented by Apple. react-native-ble-plx supports it natively.
- **Android**: Foreground services require careful notification management. Android 14+ requires specific foreground service type declaration.
- **Expo**: May require config plugins for native Info.plist/AndroidManifest changes. Need to verify Expo compatibility.

# Implementation Plan: Watch Bridge Native Modules

## Goal
Implement the React Native native modules (Swift for iOS, Kotlin for Android) required to bridge communication between the JS thread in SK8Lytz and the native watchOS/Wear OS companion apps.

## Architectural Constraints
- **Library Choice:** Due to Expo's managed workflow, we must use `expo-modules-core` to build a local Expo module (`sk8lytz-watch-bridge`) rather than a traditional React Native native module. This provides a modern Swift/Kotlin API and automatic config plugin generation.
- **Interfaces:** We need methods to `sendStateToWatch(state)` and events for `onWatchCommandReceived(command)`.

## Proposed Changes
### [NEW] `modules/sk8lytz-watch-bridge/`
- `index.ts`: The JS API surface (`export async function syncSessionState(...)`, etc.)
- `ios/Sk8lytzWatchBridgeModule.swift`: Uses `WCSession.default` to send `updateApplicationContext` to the Apple Watch.
- `android/src/main/java/expo/modules/sk8lytzwatchbridge/Sk8lytzWatchBridgeModule.kt`: Uses `Wearable.getDataClient(context)` to push `PutDataRequest` to Wear OS.

## Open Questions / User Review Required
> [!WARNING]
> Maintaining custom native modules adds significantly to technical debt. 
- Should we evaluate `react-native-watch-connectivity` (iOS only) before rolling our own custom bridge to save time, even if it means writing our own for Android?

## Verification Plan
- Build locally using `npx expo run:ios` and `npx expo run:android` to ensure the module compiles.
- Write Jest mock for the `sk8lytz-watch-bridge` module so CI tests (`npm run verify`) do not fail.

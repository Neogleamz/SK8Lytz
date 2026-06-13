# Implementation Plan: watchOS Companion App

## Goal
Build a standalone native watchOS app (SwiftUI) that acts as a remote control and real-time telemetry display for the SK8Lytz iOS app.

## Architectural Constraints
- **Framework:** SwiftUI & WatchKit.
- **Communication:** `WatchConnectivity` framework (specifically `WCSession`).
- **Health:** Integrate `HKHealthStore` natively on the watch to read heart rate directly from the optical sensor during a live session.
- **Standalone Mode vs. Proxied:** Phase 1 will be strictly Proxied (phone must be present). The watch sends "START_SESSION" to the phone, phone handles BLE to the skates.

## Proposed Changes
### [NEW] `ios/sk8lytzWatch/`
- `sk8lytzWatchApp.swift`: Main entry point.
- `ContentView.swift`: Main dashboard showing Start/Stop button, Current Speed, and Active Cals.
- `WatchConnectivityManager.swift`: Singleton managing `WCSession` state and message passing with the iOS host app.
- `HealthManager.swift`: Handles requesting HealthKit permissions and starting an `HKWorkoutSession` on the watch to keep the app active and screen on.

## Open Questions / User Review Required
> [!WARNING]
> Building a watchOS app requires modifying the Xcode project directly (adding a new Target). Since we are using Expo, we MUST use a custom Expo Config Plugin to inject this target, otherwise `npx expo prebuild` will wipe the watch app out of the `ios/` folder.
- Are we comfortable ejecting to bare workflow or maintaining a complex config plugin?
- Do we want the watch to control colors/patterns, or just start/stop the workout?

## Verification Plan
- Requires physical Apple Watch for testing HealthKit background execution.
- Validate `WCSession.isReachable` state transitions.

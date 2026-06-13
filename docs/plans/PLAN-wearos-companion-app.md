# Implementation Plan: Wear OS Companion App

## Goal
Build a standalone native Wear OS app (Kotlin/Jetpack Compose) that acts as a remote control and real-time telemetry display for the SK8Lytz Android app.

## Architectural Constraints
- **Framework:** Jetpack Compose for Wear OS (Kotlin).
- **Communication:** Google Play Services `Wearable Data Layer API` (DataClient & MessageClient).
- **Health:** Health Services on Wear OS (WHS) to capture high-frequency heart rate data and keep the app active via an `ExerciseClient`.
- **Standalone Mode vs. Proxied:** Phase 1 will be Proxied. Wear OS acts as a UI for the phone.

## Proposed Changes
### [NEW] `android/sk8lytzWear/`
- `MainActivity.kt`: Wear OS entry point.
- `presentation/theme/Theme.kt`: SK8Lytz Dark Mode theme for Wear OS.
- `presentation/DashboardScreen.kt`: Jetpack Compose UI with Start/Stop, Speed (received from phone), and HR.
- `services/WearableCommunicationService.kt`: Listens for messages from the phone app.
- `services/HealthTracker.kt`: Initializes the `ExerciseClient` for background HR polling.

## Open Questions / User Review Required
> [!WARNING]
> Similar to watchOS, modifying the Android project to include a wear module must be done via an Expo Config Plugin to survive `npx expo prebuild`.
- Do we want to support standalone BLE connections (Wear OS connecting directly to the skate controller) in the future? If so, we need to port the `ZENGGE_PROTOCOL_BIBLE.md` hex builder into Kotlin.

## Verification Plan
- Requires physical Wear OS device (e.g. Pixel Watch or Galaxy Watch) for testing Health Services.
- Verify `MessageClient` latency between Phone and Watch.

# [PLAN] fix/voice-mic-access

### Design Decisions & Rationale

The `VoiceService` currently attempts to start the microphone without verifying that the app has system-level permissions. This results in immediate errors on most modern mobile devices (iOS/Android) where runtime permission requests are mandatory. We will implement a robust permission guard using `react-native-permissions` and ensure the `Voice` dependency is correctly initialized before use.

## User Review Required

> [!IMPORTANT]
> **Android Manifest / iOS Info.plist**: This fix requires native configuration changes. If the user is running in a managed Expo environment, we need to ensure the correct plugins are added to `app.json`.

## Proposed Changes

### [Services]

#### [MODIFY] [VoiceService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/VoiceService.ts)

- Import `check`, `request`, and `PERMISSIONS` from `react-native-permissions`.
- Implement a `checkPermissions()` helper method.
- Update `startListening()` to wait for permission approval before calling `Voice.start()`.
- Add a `isAvailable()` check to prevent crashes on unsupported devices.

### [Config/Native]

#### [MODIFY] [app.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.json) (if applicable)

- Ensure `@react-native-voice/voice` plugin is present and `NSMicrophoneUsageDescription` is set.

## Open Questions

- Are we using a Managed Expo flow or Bare Workflow? (Affects how we handle native permissions).

## Verification Plan

### Automated Tests

- Since hardware permissions are hard to test in CI, we will use a mock for `Voice` and verify that the `checkPermissions` logic is triggered before `start()`.

### Manual Verification

1. Open the app on a physical device.
2. Trigger the Voice FAB.
3. Verify that the System Permission Dialog for "Microphone" appears.
4. Verify that the app transitions to "Listening" state ONLY after permission is granted.

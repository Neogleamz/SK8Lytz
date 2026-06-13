# Fix Voice Engine Integration

## Target

`main`

## Objective

Repair the native Voice Command Engine. The `@react-native-voice/voice` bridge appears to be throwing null references on startup. This task includes auditing the native module configuration, verifying OS permissions logic (iOS/Android), and updating native handlers.

## Design Decisions & Rationale

The voice engine relies on an outdated Expo `config-plugin` architecture, which caused our recent dependency vulnerability. Repairing this requires a deep audit into how Expo injects the iOS and Android permission manifest entries and whether the native code needs to be recompiled under the current React Native version. We will decouple the UI components from the native service so it fails gracefully going forward.

## Proposed Changes

### Configuration Updates

- **[MODIFY] app.json / app.config.js**
  - Verify microphone and speech recognition permissions are correctly declared.

### Service Updates

- **[MODIFY] src/services/VoiceService.ts**
  - Implement strict null checks and fallback mechanisms when the native module fails to initialize, preventing the crash from propagating.

### Test and Verification

- Compile native application to physical device (Android `run:android` / iOS `run:ios`).
- Verify voice permissions prompt.
- Confirm speech recognition output logs correctly without crashing.

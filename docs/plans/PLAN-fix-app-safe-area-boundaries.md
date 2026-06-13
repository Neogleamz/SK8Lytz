# Implementation Plan: Fix App SafeArea Boundaries

Provide a brief description of the problem, any background context, and what the change accomplishes.
The core `SafeAreaView` from `react-native` has been deprecated in favor of `react-native-safe-area-context`. Furthermore, the core `SafeAreaView` does not properly apply safe area insets on many Android devices, leading to the UI bleeding into the device notch or notification bar. This plan migrates all remaining instances of the core `SafeAreaView` to the community package.

## User Review Required

No breaking changes. This is a drop-in replacement that specifically addresses the Android UI bleed and the console warnings.

## Open Questions

None.

## Proposed Changes

### `src/components/GlobalErrorBoundary.tsx`
#### [MODIFY] [GlobalErrorBoundary.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/GlobalErrorBoundary.tsx)
- Remove `SafeAreaView` from `react-native` import.
- Import `SafeAreaView` from `react-native-safe-area-context`.
- *Source*: `src/components/GlobalErrorBoundary.tsx:2`

### `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
#### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- Remove `SafeAreaView` from `react-native` import.
- Import `SafeAreaView` from `react-native-safe-area-context`.
- *Source*: `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:11`

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure TypeScript compilation and no test failures.

### Manual Verification
- Deploy to Android device and verify that `HardwareSetupWizardScreen` and `GlobalErrorBoundary` no longer bleed into the notification bar.
- Verify `adb logcat` no longer outputs the `SafeAreaView` deprecation warning.

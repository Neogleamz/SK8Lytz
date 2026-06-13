# Implementation Plan

Fix global header spacing issue causing large blank space at the top of the app.

## Proposed Changes

### src/screens/DashboardScreen.tsx
- **Goal:** Remove double padding applied by nested `SafeAreaView` and `insetTop` combinations.
- **[MODIFY] src/screens/DashboardScreen.tsx**
  - Change the root `<SafeAreaView>` (around line 964) wrapping the `DashboardScreen` to a standard `<View>`.
  - Import `View` from `react-native` instead if `SafeAreaView` was exclusively used there.
  - Retain `insetTop={insets.top}` being passed to `DashboardHeader` so the header correctly pads itself against the physical notch/status bar, rather than the entire screen being pushed down and then the header padding itself again.

## Verification Plan
### Manual Verification
- Run `/dev-server`, open the app.
- Ensure the `DashboardHeader` sits flush against the top of the screen on devices with notches and without notches.
- Ensure there is no duplicated blank space above the telemetry HUD or header.

## Out of Scope
- Modifying `DashboardHeader.tsx` internal styling.

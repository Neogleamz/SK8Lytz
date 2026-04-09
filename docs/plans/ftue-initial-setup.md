# FTUE Phase 1 - Hardware Setup Wizard

This module implements Phase 1 of the new Device Registration Flow. We are refactoring the temporary `FirstTimeSetupModal` into a strict, unskippable full-screen wizard.

## Proposed Changes

### 1. New Screen Architecture
- **Create**: `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
- We will migrate the UI from the old `FirstTimeSetupModal` but reconstruct it as a standalone, unskippable view.
- The UI will perfectly match the existing dark premium theme using `useTheme()` contexts.

### 2. Integration with DashboardScreen
- **Modify**: `src/screens/DashboardScreen.tsx`
- `DashboardScreen` currently spawns `FirstTimeSetupModal` if `!hasAny` registrations. We will replace this. If `!hasAny` registrations, `DashboardScreen` will instead absolutely block rendering the main dashboard and ONLY render `HardwareSetupWizardScreen`.

### 3. Probe Logic Wiring
- Provide a "Tap to Scan" button if the auto-scan doesn't trigger immediately.
- Integrate the `useBLE()` probe routines to actively scan and pull down the hardware signatures (led_points, ic_type) directly into this wizard.

## Open Questions

> [!IMPORTANT]
> - Do you want the `HardwareSetupWizardScreen` to be a full React component that completely hides the Dashboard, or just a Modal that covers 100% of the screen with no close button? (A full component is cleaner for performance and prevents accidental dismissals).

## Verification Plan
1. Reset local `AsyncStorage` to clear all registrations.
2. Sign in or load the app.
3. Verify the completely unskippable `HardwareSetupWizardScreen` is rendered.

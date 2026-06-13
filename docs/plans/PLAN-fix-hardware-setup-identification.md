# Implementation Plan

Fix hardware setup identification logic, blink button colors, and device name persistence.

## Proposed Changes

### src/screens/Onboarding/HardwareSetupWizardScreen.tsx
- **Goal:** Prevent duplicate position assignment, make Blink button match orientation color, and persist `custom_name` so it sticks globally.
- **[MODIFY] src/screens/Onboarding/HardwareSetupWizardScreen.tsx**
  - **Color Swap Logic Guard:** In Step 2's "ASSIGN SKATE SETTINGS" press handler, if both devices heuristically resolve to the same position (e.g. both 'Left'), force the second one to the opposite position.
  - **Blink Button Color:** In Step 3 (`renderStep3`), dynamically set the Blink button icon and text color to Red (`#ff4444`) if position is 'Left', Green (`#4ade80`) if 'Right', and fallback to Cyan (`#00f0ff`) if unknown.
  - **Device Name Persistence:** In `finalizedDevices` mapping (line 687), assign the user's typed name to `custom_name` IN ADDITION to `device_name` so it permanently overrides the generic BLE broadcast name across the app.

## Verification Plan
### Manual Verification
- Run `/dev-server`, open app, simulate Hardware Setup.
- Verify devices default to Left and Right correctly even if named identically.
- Verify Blink buttons in Step 3 are colored Red and Green respectively.
- Complete setup and verify the custom names appear in the Dashboard and Registered Fleet lists.

## Out of Scope
- Modifying the underlying BLE ping mechanism.

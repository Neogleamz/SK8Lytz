# Implementation Plan

## hardware-setup-batch

This batch resolves the UI/UX issues encountered during the initial hardware setup wizard flow, as well as fixing the global header double-padding bug across the app.

### Proposed Changes

#### [MODIFY] [src/screens/Onboarding/HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Goal:** Fix color swap logic, blink button matching colors, name persistence, and brand colors.
- **Color Swap Logic Guard (Step 2 Press Handler):** In the "ASSIGN SKATE SETTINGS" block, if two devices heuristically resolve to the same position (e.g. both 'Left' due to identical names), force the second one to the opposite position.
- **Device Name Persistence:** When mapping the `finalizedDevices` before returning to the parent, explicitly set the `custom_name` property alongside `device_name` to ensure it is correctly stored in the Supabase database.
- **Brand Colors Update:** The Setup Wizard will use the official Neogleamz brand colors: Blue (`#1B4279`) for Left, and Orange (`#F79320`) for Right, replacing the generic Red/Green scheme.
  - **Blink Button Colors:** The Blink buttons will dynamically adopt Blue (`#1B4279`) or Orange (`#F79320`) depending on the assigned position.
  - **Orientation Test Ping:** The initial `fireOrientationTest` will ping Blue (`{r: 27, g: 66, b: 121}`) for Left, and Orange (`{r: 247, g: 147, b: 32}`) for Right.

#### [MODIFY] [src/screens/DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Goal:** Eliminate the double padding (blank space) at the top of the app header.
- **SafeAreaView Removal:** Replace `<SafeAreaView testID="dashboard-screen" style={styles.safeArea}>` with a standard `<View testID="dashboard-screen" style={styles.safeArea}>`. 
- **Reasoning:** `App.tsx` already wraps the app in a `SafeAreaProvider`, and `DashboardHeader` explicitly uses `insets.top` to pad the header. Nesting a `SafeAreaView` inside the screen creates double padding. 

## Verification Plan

### Automated Tests
- `npm run verify` to ensure no TypeScript compilation errors.

### Manual Verification
- **Setup Wizard:** Navigate to the wizard, select two identically named devices, and verify they automatically split into Left and Right.
- **Colors:** Verify the Left skate blinks Blue and the Right skate blinks Orange, and the UI buttons reflect these brand colors.
- **Spacing:** Return to the Dashboard and verify the blank space above the header has been eliminated.

## Out of Scope
- Modifying the underlying BLE ping mechanism.
- Modifying the `DashboardHeader` component internals.

# Complete Remaining Main Issues

This plan addresses three high-priority items from the `main` backlog to stabilize the FTUE and UI experience before shipping.

## User Review Required

> [!IMPORTANT]
> **Task 1: Username Fallback.** I will change the fallback from "Skater" to "GUEST" when no profile is available. Please confirm if you prefer a different label.
>
> **Task 2: Post-Setup Behavior.** Currently, the app auto-connects to new devices which forces the controller view. I will disable this auto-connect to ensure the user stays on the Dashboard as requested.

## Proposed Changes

---

### [Component] DashboardScreen.tsx

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

- **Task 1: Dynamic Username**
  - Improve `authUsername` initialization.
  - Change JSX fallback from `'Skater'` to `'GUEST'`.
- **Task 2: Setup Finish Destination**
  - Modify `handleRegistrationComplete`.
  - REASON: Removing `connectToDevices(devicesToConnect)` will prevent the `isActuallyConnected` state from flipping, which keeps the user on the Dashboard.

---

### [Component] CameraTracker.tsx

#### [MODIFY] [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)

- **Task 3: Camera Mode Layout**
  - **Maximize Camera**: Increase `flex` of `cameraBox` and remove excessive padding in `container`.
  - **Minimize Color Bar**: Reduce vertical padding and font size in `statusBox`.
  - **Instruction Tooltip**: Enhance `instructionOverlay` to be more prominent and use `Righteous` font (per brand standard).
  - **Permissions**: Ensure the "GRANT PERMISSION" flow follows the app's modern UI theme.

## Open Questions

1. **Task 3 (Permissions)**: Is there a specific "permissions prompt system" (e.g., a custom modal) used elsewhere in the app that should be implemented here, or is the current inline full-screen view (lines 138-155) acceptable?
2. **Task 1 (Username)**: Should we attempt to use the device's local brand name (e.g., "SK8Lytz User") if offline, or is "GUEST" the universal target?

## Verification Plan

### Automated Tests

- `npx tsc --noEmit`: Verify no new type regressions in Dashboard or Camera components.

### Manual Verification

1. **Username**: Sign in and verify name displays; sign out and verify "GUEST" displays.
2. **Setup Wizard**: Complete a mock registration and verify it stays on the Dashboard "My Skates" view.
3. **Camera Mode**: Open camera mode, check layout proportions on web/native, and verify the pick instructions are clear.

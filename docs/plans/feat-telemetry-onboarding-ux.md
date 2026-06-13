# [PLAN] feat/telemetry-onboarding-ux (Permissions Hub)

### Design Decisions & Rationale

After the "Rock Solid" EULA, the user might feel overwhelmed by legal prose. We need to pivot to a **Casual, High-Trust Onboarding Screen** that explains _why_ we need permissions in a skater-friendly way.

- **Persona**: Friendly and transparent ("We just make cool skate apps").
- **Strategy**: Instead of separate OS popups, we show a unified "Dashboard" of permissions where the user can choose to enable features they care about.
- **Copy**: Focus on the _benefit_ (e.g., "See your speed" instead of "Enable GPS").

## User Review Required

> [!IMPORTANT]
> **Consent Model**: Should some permissions be "Non-Optional" for the core app (like Bluetooth), while others (Mic, Camera) are "Choose to Enable"?

## Proposed Changes

### UI & Components

#### [NEW] [PermissionsOnboardingScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/onboarding/PermissionsOnboardingScreen.tsx)

- Premium, illustrative screen appearing immediately after `EulaModal` acceptance.
- **Micro-Toggles**: A list of feature-centric permissions:
  - **"Eyes in the Dark" (Camera)**: For Camera Mode effects.
  - **"Feel the Beat" (Mic)**: For Music Mode syncing to the DJ.
  - **"Performance HUD" (GPS/Accel)**: For Street Mode speed tracking.
  - **"The Roll Call" (Location)**: For Crew Hub discovery.
  - **"Heartbeat" (Bluetooth)**: Core connection (Required).
  - **"Stay Connected" (Notifications)**: For crew alerts.
- **Privacy Hero Section**: A prominent badge saying: _"No data sold. No trackers. Just SK8Lytz. We only use telemetry to make your lights smarter."_

#### [MODIFY] [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)

- Update the onboarding navigation flow: `EULA` -> `Permissions Hub` -> `Hardware Setup`.

### Services

#### [NEW] [PermissionService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)

- Wrapper for `react-native-permissions` or Expo's `DevicePermissions`.
- Handles the state of "Deferred" vs. "Granted" permissions.

## Open Questions

1. **Bluetooth Enforcement**: Since Bluetooth is required for 99% of the app, should we allow the user to continue if they deny it, or loop them back with a friendly explanation?

## Verification Plan

1. **Navigation Flow**: Verify that accepting the EULA correctly transitions the user to the Permissions Hub.
2. **Permission Request**: Verify that tapping a toggle triggers the standard iOS/Android system permission prompt.
3. **Persisted State**: Ensure that even if a user skips a permission (e.g., Camera), they can still reach the dashboard but see the "Gated" state when entering Camera Mode.

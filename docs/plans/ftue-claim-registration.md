# FTUE Phase 3 - Claim Registration

Wire the offline/online claiming via useRegistration.ts and UI portal integration.

### Design Decisions & Rationale
We will modify the `HardwareSetupWizardScreen` to directly invoke the `onSetupComplete` prop with the `pendingRegistrations` payload. By updating `DashboardScreen.tsx` to wire `handleRegistrationComplete` into this prop, we ensure the existing `useRegistration` hook (which is already instantiated in the Dashboard) correctly persists the discovered devices to local offline storage and queues them for Supabase sync. This approach centralizes the device provisioning business logic in the root Dashboard component and accurately fulfills the offline-first requirement.

### UI & Platform Strategy
The claiming process will trigger an `ActivityIndicator` spinner within the primary action button to provide immediate, responsive visual feedback on touch. Utilizing the existing Flexbox container classes will ensure scaling remains identical across iOS and Android safe-areas.

## Proposed Changes

### `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
- Update `HardwareSetupWizardScreenProps` so that `onSetupComplete` passes an array of pending registrations: `onSetupComplete: (devices: any[]) => Promise<void> | void;`
- Introduce a local `isClaiming` boolean state.
- Update the mock `onPress` action at the bottom of the component to `await onSetupComplete(pendingRegistrations)` and trigger the local spinner until it completes.

### `src/screens/DashboardScreen.tsx`
- In the `isSetupWizardVisible` rendering block, replace the mock arrow function with the existing `handleRegistrationComplete` logic: `onSetupComplete={(devices) => handleRegistrationComplete(devices)}`

## Verification Plan

### Automated Tests
- Check TypeScript build integrity (`npx tsc --noEmit`) to verify the new prop signatures match correctly.

### Manual Verification
- Render the Demo devices, trigger the setup wizard, click the Claim button, and observe the loading spinner toggle.
- Verify `handleRegistrationComplete` fires and populates `registeredDevices` seamlessly without an unhandled promise rejection.

# FTUE Phase 2 - Device Discovery & Identification

This module implements Phase 2 of the Device Registration Flow, focusing on displaying discovered hardware and allowing users to physically identify them via a "Blink Test".

## Proposed Changes

### 1. State Management in HardwareSetupWizardScreen

- **Modify**: `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
- Implement an internal `step` state (e.g., `1` for instructions/scanning, `2` for discovery list).
- Once `pendingRegistrations` is populated by `useBLE()`, transition the UI to `step === 2`.

### 2. Device Discovery List UI

- **Modify**: `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
- Build a new sub-view for Step 2 that renders the list of discovered devices.
- Group the devices logically by `product_type` (HALOZ vs SOULZ) using the auto-classified data from `useBLE`.
- Display hardware telemetry: signal strength (RSSI bars), LED points, IC type.
- Retain the dark premium theme with correct accent colors (`#00f0ff` for HALOZ, `#a855f7` for SOULZ).

### 3. "Blink Test" Functionality

- **Modify**: `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
- Add a "Blink" button next to each discovered device.
- Tapping "Blink" will dispatch a `ZenggeProtocol.setSymphonyColor(0, 255, 0)` (Pure Green) to the specific `device_mac` using the `writeToDevice` function exposed by `useBLE()`.
- After 500ms, dispatch `ZenggeProtocol.turnOff()` or `ZenggeProtocol.setSymphonyColor(0,0,0)` to conclude the blink.
- This allows users to physically differentiate between Left and Right skates for the next phase.

## Open Questions

None at this time. The flow is well defined by the `FirstTimeSetupModal` precedent and the auto-classification logic is already fully implemented within `useBLE.ts`.

## Verification Plan

1. Simulate a BLE scan in `HardwareSetupWizardScreen`.
2. Confirm the UI successfully transitions from "Scanning" to "Discovered Devices".
3. Verify the HALOZ and SOULZ grouping headers dynamically appear based on mocked `pendingRegistrations`.
4. Validate the "Blink" `writeToDevice` dispatch sends the correct targeted payload.

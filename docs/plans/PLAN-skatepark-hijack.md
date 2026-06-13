# Implementation Plan

## Goal Description
Resolve the "Skatepark Hijack" (Proximity Blindness) without breaking the new `HardwareSetupWizardScreen` flow. 

**Background:** 
Historically, the `runAutoProvisioning` function in `useDashboardGroups.ts` was designed to automatically and blindly group any unregistered BLE devices it saw after a scan. At a public skatepark, this meant your phone could hijack another skater's devices simply by running a scan, as it would pair them up and claim them without any user intervention.

**Analysis of the Current State & Setup Wizard:**
We recently introduced the `HardwareSetupWizardScreen` (which relies on an explicit human-in-the-loop BLINK and assignment flow). Because the Setup Wizard now handles all device claiming with explicit user confirmation, `runAutoProvisioning` was bypassed in `DashboardScreen.tsx` and is now **dead, orphaned code**. 

The Setup Wizard itself prevents the hijacking because the user must tap a device, see it physically blink green, and manually assign it a port/starboard position. However, to further protect the user from seeing 50 different skates in their wizard list at a busy skatepark, we can tighten the RSSI (signal strength) requirement for *unregistered* devices so that you must hold your phone close to your skates to pair them.

> [!IMPORTANT]
> **User Review Required**
> - Are you comfortable with completely deleting `runAutoProvisioning` from `useDashboardGroups.ts` since it is entirely replaced by the Setup Wizard?
> - For the RSSI Gating in the scanner: I propose setting a threshold of `-70 dBm` (approx. 3-5 feet away) for discovering unregistered devices. This will force users to hold their phone near their skates during the Setup Wizard, hiding everyone else's skates at the skatepark. Does `-70 dBm` sound good to you?

## Proposed Changes

### Core Logic (State Management)

#### [MODIFY] [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts)
- Delete the legacy `runAutoProvisioning` function entirely.
- Remove `isProvisioningTriggered` state ref.
- Remove these from the hook's returned interface.

### Scanner (Hardware)

#### [MODIFY] [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- Currently, there is an ambient `RSSI_THRESHOLD = -80` that applies to all scans (to drop OS-cache ghosts).
- We will add a secondary check: if the scanned device is NOT already registered in `hwCache` or the global catalog, enforce a stricter `SETUP_RSSI_THRESHOLD = -70`.
- This ensures registered devices can connect from far away, but new devices must be right next to the phone to appear in the Setup Wizard.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure no TypeScript or Jest errors are introduced by removing `runAutoProvisioning`.

### Manual Verification
- Launch the Setup Wizard and verify that only devices physically near the phone (strong RSSI) populate the "Found Skates" list.
- Ensure the app builds and `DashboardScreen.tsx` does not crash due to missing exports.

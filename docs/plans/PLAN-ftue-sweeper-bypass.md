# Implementation Plan: Fix FTUE Scanner Lockout

The hardware setup wizard is failing to discover physical devices because the First-Time User Experience (FTUE) scan is improperly routed through the background Battery Sweeper service. The background Sweeper relies on a potentially hanging battery promise and enforces a restrictive 10s ON / 20s OFF duty cycle designed for background Overwatch, which is inappropriate for active foreground device discovery.

## Proposed Changes

### [Component Name] BLE Discovery & Onboarding

#### [MODIFY] `src/hooks/ble/useBLEScanner.ts`
- Remove the early-return trap that routes `registeredMacs.length === 0` to `startSweeper()`.
- Allow the FTUE scan to fall through to the manual `bleSend({ type: 'SCAN_START' })` block.
- This guarantees an instant, synchronous command to the native Bluetooth scanner without awaiting the battery API, bypassing the Sweeper's silent lockouts.

#### [MODIFY] `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
- Fix the logic bug in the `keepAlive` polling loop. Currently, it requires `step === 2 && pendingRegistrations.length === 0` (which is logically impossible).
- Update the condition to run on `step === 1` when `scanStage === 'STARTED'` and no devices are found.
- This restores the "continuous scan" behavior from the legacy architecture, where the scanner actively retries every few seconds until the hardware is powered on and discovered.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure TypeScript compliance and that no hooks or context dependencies were broken.

### Manual Verification
- Ask the user to run the app on the physical device.
- Navigate to the Hardware Setup Wizard.
- Observe the "SEARCHING FOR SKATES..." UI indicator.
- Verify that turning on a physical SK8Lytz controller instantly results in a discovery, rather than being missed by a 20-second sleep window.

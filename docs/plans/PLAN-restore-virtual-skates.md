# Implementation Plan: Restore Virtual Skates Sandbox

## Goal
Restore the "Virtual Skates" Dev Sandbox feature by injecting mock devices (HALOZ and SOULZ) into the BLE scanning engine when `STORAGE_DEMO_MODE` is active.

## User Review Required
> [!IMPORTANT]
> The previous implementation in `AuthFooterActions.tsx` set `STORAGE_DEMO_HALO` and `STORAGE_DEMO_SOUL` keys, while `AuthSandboxToggle.tsx` sets `STORAGE_DEMO_MODE`. I propose we deprecate the split halo/soul keys and consolidate everything under `STORAGE_DEMO_MODE` to prevent state fragmentation. Let me know if you approve this simplification.

## Proposed Changes

### [MODIFY] src/hooks/useBLE.ts
- Wait for `STORAGE_DEMO_MODE` resolution in the initial `useEffect` and store `isSandboxEnabled` in state.
- Pass `isSandboxEnabled` down into `useBLEScanner`.

### [MODIFY] src/hooks/ble/useBLEScanner.ts
- Accept `isSandboxEnabled: boolean`.
- Update `scanForPeripherals`: If `__DEV__ && isSandboxEnabled` is true, bypass `bleManager?.startDeviceScan`. 
- Instead, use `setTimeout` to simulate discovery and immediately yield two mock objects (implementing the `Device` interface with fake MACs, RSSIs, and Manufacturer Data) to the `scanCallback` function.

### [MODIFY] src/components/auth/AuthFooterActions.tsx
- Remove `STORAGE_DEMO_HALO` and `STORAGE_DEMO_SOUL` setting, replace with `STORAGE_DEMO_MODE` to unify the Sandbox behavior.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure no TypeScript compilation errors (especially regarding the Mock Device interfaces).

### Manual Verification
1. Boot the app in Dev Mode.
2. Toggle "DEV SANDBOX" or "DEV MODE: VIRTUAL SKATES" on the auth screen.
3. Refresh the app.
4. On the scanner screen, verify that "VIRTUAL HALOZ" and "VIRTUAL SOULZ" immediately appear without actual hardware powered on.
5. Tap "Connect" and verify they successfully bind and appear in the Dashboard.

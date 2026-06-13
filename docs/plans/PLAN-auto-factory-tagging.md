# Implementation Plan: Auto Factory Tagging

The BLE `manufacturer_data` payload is a raw base64 string and does not contain human-readable factory names. Currently, the `factory_name` field in `registered_devices` remains `null`. This task introduces BLE signature fingerprinting during the discovery scan to automatically populate `factory_name` with values like `'Zengge'` or `'BanlanX'`.

## Proposed Changes

### [MODIFY] src/hooks/ble/useBLEScanner.ts
- Create a `determineFactoryName(device)` helper function within the scanner hook.
- **Zengge Logic**: Check if `device.serviceUUIDs` includes `0000ffe5-0000-1000-8000-00805f9b34fb` or `0000ffd5...`.
  Source: `tools/ZENGGE_PROTOCOL_BIBLE.md`
- **BanlanX Logic**: Check if `device.serviceUUIDs` includes `0000ffe0...` and if `manufacturerData` decodes to a hex string containing the Manufacturer ID `5053` ("PS").
  Source: `tools/BANLANX_PROTOCOL_BIBLE.md`:30
- Map the output of `determineFactoryName` to the `factory_name` property when creating the `scannedDevice` object on line 93.

### [MODIFY] src/services/DeviceRepository.ts
- Verify that `saveDevice` handles the populated `factory_name` accurately. (Code audit confirms it already maps `device.factory_name ?? null`, so no direct edits are strictly necessary here unless type fixes are needed).

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure strict TypeScript compilation.

### Manual Verification
- Deploy to a physical Android device.
- Scan for nearby skates.
- Verify in the Redux state (via React Native Debugger or console logs) that `factory_name` populates with `'Zengge'` or `'BanlanX'` before the user clicks "Register".

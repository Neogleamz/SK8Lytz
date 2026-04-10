# FTUE & Telemetry Hardware Sync Strategy

This plan finalizes the database synchronization requirements for the Sk8Lytz hardware registration flow and introduces a robust telemetry pipeline for discovering and logging anonymous/unregistered hardware.

## Proposed Changes

### 1. Finalizing FTUE Device Sync (`src/hooks/useRegistration.ts` & `src/screens/DashboardScreen.tsx`)
Currently, upon finishing the `HardwareSetupWizardScreen`, the system invokes `saveAllRegisteredDevices()` which issues an `upsert` to Supabase `registered_devices`. 
* I will verify that the database upsert properly unpacks and saves all detailed properties injected by the FTUE (such as `product_type`, `led_points`, `firmware_ver`, `group_name`, and `position`).
* Wait states will be tightened to ensure the app doesn't leave the completion wizard until Supabase returns a 200 OK. 

### 2. Live Scanned Hardware Telemetry (`src/hooks/useBLE.ts`)
We will intercept the raw raw Bluetooth LE advertising data from any discovered Zengge controller and buffer it.
* Instead of only parsing "connectable" or "claimed" devices, `useBLE` will log the full MAC address, RSSI, and payload data of *every* detected SK8Lytz-compatible board.
* This raw data will be synced to Supabase.
* **[NEW] `src/services/DeviceTelemetryService.ts`**: A dedicated background service to batch these raw scanned devices and push them to Supabase (e.g., to a `device_diagnostics` or new `scanned_hardware` table) so the database maintains a live topological layout of the environment without polluting the user's `registered_devices` ownership table.

### 3. LogParser / Diagnostic Lab Integration (`src/components/Sk8LytzDiagnosticLab.tsx`)
The Diagnostic Lab (Logparser) currently tracks BLE GATT data, but does not have a dedicated UI for the raw environment slice.
* **[MODIFY]** `Sk8LytzDiagnosticLab.tsx`: We will add logic to the `DEVICES` tab that parses the `allDevices` array from `useBLE`. 
* It will explicitly tag whether a device is *Registered to You*, *Unregistered*, or *Claimed by Someone Else*, mirroring the data synced to Supabase.

> [!IMPORTANT]
> ## User Review Required
> 1. **Supabase Target Table**: Where exactly in Supabase do you want **unregistered/scanned-only** devices logged? Should I use the existing `device_diagnostics` table, or do you have a specific table like `scanned_devices` ready for this telemetry?
> 2. **Registration Overwrite**: Right now, if a user skips the FTUE, the background engine might try to auto-add discovered boards to their local storage. Are we strictly relying on the FTUE to serve as the only gateway for permanent Database Ownership?

## Verification Plan

### Manual Verification
1. I will factory-reset the app via the Nuke Button.
2. We will run the FTUE, claim a device, and verify it populates the `registered_devices` Supabase table.
3. We will open the "Sniffer Lab" (Diagnostic Lab) and verify that all surrounding BLE boards (even unowned ones) populate the Devices Tab and push their data correctly to Supabase.

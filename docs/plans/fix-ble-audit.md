# BLE Implementation Audit & Remediation Plan

This document outlines the findings and proposed patches from a comprehensive audit of the SK8Lytz BLE engine (`useBLE.ts`), Registration Engine (`useRegistration.ts`), and the Dashboard connection logic.

## 🔴 User Review Required

The following fixes address the regression where only 2 out of 4 devices were being successfully detected and connected. Please review the underlying causes and proposed patches. If you approve, type 'proceed' to execute.

## Proposed Changes

### `src/hooks/useBLE.ts` [BLE Discovery & Connectivity]

1. **Bug: Scanner Filter Rejects Custom Devices (Phantom Skates)**
   - **Root Cause**: The physical BLE scanner drops devices if they don't explicitly broadcast `isSymphony=true`, `hasZenggeService=true`, or lack an expected default name (`lednet`, `sk8`, `halo`, `soul`). If a user customizes their skate name *and* Android's OS truncates the `ServiceUUID` from the background broadcast (common on Android 12+), `useBLE.ts` immediately classifies the skate as a "ghost" and silently rejects it.
   - **Fix**: We will inject a preemptive `knownMacs` bypass list into `scanForPeripherals()`. By quickly loading `ng_registered_devices` from `AsyncStorage` when a scan starts, we can guarantee that ANY device MAC address the user previously registered is **always accepted**, entirely bypassing the strict name/service filter.

2. **Bug: Setup Wizard Device Slice (Left/Right Grouping)**
   - **Root Cause**: The `mapToRegistration` parser forces the first two discovered devices sequentially into `position = 'Left'` and `position = 'Right'`. Any additional devices of the exact same product type (i.e., a 3rd and 4th `HALOZ`) are assigned a position of `null`. This breaks `useRegistration` down the line if the user tries to claim 4 of the same hardware type.
   - **Fix**: Re-write `mapToRegistration` to dynamically handle multiple pairs. It will assign `Left, Right, Left 2, Right 2, Left 3, …` based on the sequence index, ensuring infinite scalability of identical devices within the Setup Wizard.

### `src/screens/DashboardScreen.tsx` [Auto-Connect Sequence]

3. **Gating Issue: Auto-Connect Timeout Race Condition**
   - **Root Cause**: The Dashboard's `syncCloudAndAutoConnect` waits exactly 7000ms (`setTimeout`) to gather `allDevicesRef.current` before firing the connection sequence. If a specific controller takes 8 seconds to randomly broadcast its UUID from sleep, it misses the connection train entirely and is incorrectly flagged as offline until the user taps on it.
   - **Fix**: Replace the hard 7-second cutoff with a continuous connection queue observer. As soon as a known tracked device is discovered by the persistent scanner loop, the Dashboard will queue it for connection immediately rather than batch-firing everything once at launch.

## Verification Plan

### Automated Tests
- Inject `[EMERGENCY-DEBUG]` telemetry to confirm `isMatch` correctly accepts previously dropped MAC addresses.

### Manual Verification
- Compile and install the test APK to the Android hardware.
- Observe whether all 4 previously registered devices automatically appear on the Dashboard without restrictive truncation.

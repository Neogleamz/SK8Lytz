# Implementation Plan: feat/ble-scan-filter-uuid

> ⛔ **HARMFUL — DO NOT RE-APPLY (reverted 2026-07-01, VS-013).**
> This plan's premise is WRONG. Applying an OS-level Service-UUID scan filter drops fresh
> Zengge/FCF1 controllers, which advertise their ID in `mServiceData` (NOT `mServiceUuids`)
> and only expose `FFFF` post-GATT. The result: hardware setup finds zero devices and spins
> forever ("searching for skates"). The scan MUST stay unfiltered (`scanServiceUUIDs: null`);
> device filtering happens in `scanCallback`, not at the OS layer. See KNOWN_ISSUES.md VS-013
> and VS-006. If iOS background scanning ever needs a filter, set it conditionally via the
> `scanServiceUUIDs` context input in `useBLE.ts` — never hardcode it in `BleMachine.ts`.

## Problem
The BleMachine's SCANNING state passes `null` as the first argument to `startDeviceScan()`, meaning no Service UUID filter is applied. This scans for ALL BLE devices, which is:
1. **Wasteful** — we process every nearby BLE device even though we only care about Zengge/BanlanX controllers
2. **iOS background blocking** — iOS REQUIRES Service UUIDs for background scanning. Without this, Gap 1 (background reconnect) cannot work on iOS

Source: [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L6) — `ZENGGE_SERVICE_UUID` is already imported but only used for post-scan filtering at L236, not as a pre-scan filter.

## Files to Create/Modify

#### [MODIFY] [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
- Find the SCANNING state's `startDeviceScan` call (invoked actor or inline action)
- Change the first argument from `null` to `[ZENGGE_SERVICE_UUID, BANLANX_SERVICE_UUID]`
- Import both UUIDs from their respective protocol files

#### [MODIFY] [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- If `startDeviceScan` is called from the hook rather than the machine, apply the same UUID filter there
- Already imports `ZENGGE_SERVICE_UUID` at L6 and `BANLANX_SERVICE_UUID` at L7

## Steps

### Step 1: Find the actual `startDeviceScan` call site
- Source: BleMachine.ts SCANNING state or useBLE.ts scan wiring
- Verify: Grep confirms the call site and its current arguments

### Step 2: Add UUID filter array
- Pass `[ZENGGE_SERVICE_UUID, BANLANX_SERVICE_UUID]` as first arg
- Verify: `npm run verify` — TSC clean, Jest passes

### Step 3: Verify scan still discovers Zengge devices
- Verify: Manual test — scan should still find HALOZ/SOULZ controllers

## Out of Scope
- Background mode (Info.plist, BleManager restoreState) — that's PLAN-feat-ble-background-reconnect
- Scan callback logic changes
- BleWriteQueue changes

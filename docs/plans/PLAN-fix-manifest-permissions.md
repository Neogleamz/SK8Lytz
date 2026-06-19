# Implementation Plan: fix/manifest-permissions

## Goal
Fix the `neverForLocation` manifest regression that silently drops BLE scan results on Android 12+.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Deep-dive code synthesis, Cluster 1 CRITICAL

## Files to Create/Modify

### [MODIFY] [app.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js)
- Locate the `react-native-ble-plx` plugin configuration inside the `plugins` array.
- Remove the `neverForLocation: true` flag from the configuration.
- Verify `ACCESS_FINE_LOCATION` is configured correctly if needed.

## Verification
- `npm run verify`
- Manual: confirm BLE scan discovers FCF1 devices on Android 12+ test device

## Out of Scope
- All files not listed above
- iOS permissions

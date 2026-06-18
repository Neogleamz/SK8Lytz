# Implementation Plan: fix/manifest-permissions

## Goal
Fix the `neverForLocation` manifest regression that silently drops BLE scan results on Android 12+.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Deep-dive code synthesis, Cluster 1 CRITICAL

## Files to Create/Modify

### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
- Remove `android:usesPermissionFlags="neverForLocation"` from `BLUETOOTH_SCAN` permission
- Verify `ACCESS_FINE_LOCATION` is declared

## Verification
- `npm run verify`
- Manual: confirm BLE scan discovers FCF1 devices on Android 12+ test device

## Out of Scope
- All files not listed above
- iOS permissions

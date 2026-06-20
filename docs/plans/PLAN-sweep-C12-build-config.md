# Implementation Plan: C12 — Build Config Permissions Fix

## Goal
Add missing Android 14+ foreground service permissions and fix stale web shim targets.

## Rules Addressed
- R-20: OS Variance Parity (3 findings, 2 HIGH)

## Files to Create/Modify
- `app.config.js` — Add FOREGROUND_SERVICE_HEALTH, FOREGROUND_SERVICE_DATA_SYNC, ACCESS_BACKGROUND_LOCATION
- `metro.config.js` — Fix react-native-worklets shim to react-native-worklets-core

## Implementation Steps
1. View app.config.js permissions section.
2. Add missing permissions to android.permissions array.
3. View metro.config.js web shim section.
4. Fix stale module reference.
5. Verify: git diff shows only config changes.
6. Run npm run verify.

## Out of Scope
- All src/ files

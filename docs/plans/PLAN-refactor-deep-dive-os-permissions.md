# Implementation Plan: refactor/deep-dive-os-permissions

## Goal
Fix missing Android 14+ FOREGROUND_SERVICE flags and conflicting location permissions in `AndroidManifest.xml`.

## Source of Truth
`android/app/src/main/AndroidManifest.xml`

## Proposed Changes

### OS_PERMISSIONS
- **[MODIFY]** `android/app/src/main/AndroidManifest.xml`: Add missing Android 14+ specific foreground service type permissions (e.g., `FOREGROUND_SERVICE_LOCATION`, `FOREGROUND_SERVICE_CONNECTED_DEVICE`). Resolve the conflicting location permission declarations on line 2.
- **[MODIFY]** `src/services/PermissionService.ts`: Add `try/catch` wrapper around `AsyncStorage.setItem()` on line 47 to prevent offline telemetry drops if storage fails.

## Verification Plan
1. **Automated Tests**: Run `npm run verify` to ensure TypeScript compilation succeeds.
2. **Manual Verification**: Verify Android build passes without manifest merge conflicts.

# Implementation Plan - Fix Device Group FK Integrity

Resolve `registered_devices_group_id_fkey` foreign key constraint violations during device registration.

## Proposed Changes

### Audit Registration Logic

- Inspect `src/services/useRegistration.ts` and `src/screens/HardwareSetupWizardScreen.tsx`.
- Identify why `group_id` might be null or pointing to a non-existent group during the final sync step.

### Fix

- Ensure groups are created and returned before devices are linked.
- Add additional validation in `useRegistration.ts` to prevent syncing devices with invalid/stale local group IDs.

## Verification Plan

- Run a full "New Device Setup" in the app and check Supabase logs for FK errors.

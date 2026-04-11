# Goal Description
Fix the hardware setup logic loop that repeatedly launches the Hardware Setup Wizard on app reopen, trapping offline users and failing cloud syncs.

### Design Decisions & Rationale
We are refactoring `hasCloudRegistrations` in `useRegistration.ts` to execute a local-first check before demanding network consensus. By aggressively checking `getLocalDevices()` first, any user who possesses registered devices locally (even those pending cloud sync or in offline mode) will immediately bypass the Setup Wizard. This prevents users from getting soft-locked in the onboarding wizard if their Supabase connection drops, if they clicked "Continue Offline", or if their initial cloud sync failed.

## Proposed Changes

### Core Registration Hook

#### [MODIFY] `useRegistration.ts`
- Refactor `hasCloudRegistrations()` to immediately query `getLocalDevices()`.
- Return `true` if `local.length > 0` regardless of "pending sync" status.
- Only fall back to `supabase.from('registered_devices').select()` if the local cache is empty, ensuring cross-device cloud sync properly triggers when a user installs the app on a new phone.

## Open Questions
None.

## Verification Plan

### Automated Tests
- Test TypeScript constraints to ensure the Promise correctly returns a boolean without rejecting on missing database tables.

### Manual Verification
1. Launch app offline or skip login.
2. Register a device.
3. Close app completely and reopen.
4. Verify the Hardware Setup wizard does not forcefully take over the screen.

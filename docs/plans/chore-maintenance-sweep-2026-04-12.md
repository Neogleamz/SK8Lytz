# Maintenance Sweep: 2026-04-12

This is a consolidated maintenance sweep to address 3 urgent `[BATCH]` tasks from our Bucket List. By batching them together, we eliminate redundant branch switching and testing cycles.

## Design Decisions & Rationale

1. **Device Group FK Integrity:** The Supabase registration upsert is failing because we are using hardcoded string identifiers (e.g. `'default-fleet'`) for `group_id` which violate foreign key constraints in the DB (assuming `device_groups` expects actual UUIDs). We will alter `useRegistration.ts` to execute a pre-flight UPSERT against the `device_groups` table so that a valid `group_id` is created and retrieved before committing to `registered_devices`.
2. **Voice Mic Access:** Mobile platforms require explicit user consent. We will implement `react-native-permissions` or standard `PermissionsAndroid`/`expo-av` permission calls in `useVoiceControl.ts` to request and verify mic access _before_ calling `Voice.start()`, avoiding silent initialization failures and app crashes.
3. **TSC Errors Audit:** Resolving dangling TypeScript drift caused by recent UI/Component migrations, specifically fixing the undefined `Audio` namespace, the missing `BUILDER_PRESET_SAVED` union string in our EventBus/Types, and broken imports in `DashboardScreen.tsx`.

## Proposed Changes

### Database Sync & Integrity

#### [MODIFY] `src/hooks/useRegistration.ts`

- **What**: Update the `uploadDeviceConfig` function.
- **Why**: Before uploading to `registered_devices`, we must ensure the `group_id` exists.
- **Change**: Add a pre-flight database call: `await supabase.from('device_groups').upsert({ id: ..., name: ... })` or adjust the mapping logic to match the existing schema perfectly.

---

### UI & Platform Strategy (Microphone)

#### [MODIFY] `src/hooks/useVoiceControl.ts` (or `src/services/VoiceService.ts`)

- **What**: Add strict OS-level permissions gating.
- **Why**: iOS and Android 11+ immediately crash or suppress streams if the microphone permissions are accessed without prior consent.
- **Change**: Integrate standard `PermissionsAndroid` / native checks into the `startListening` function. It will actively prompt the user if they haven't granted recording access yet.

---

### Code Cleanliness (TypeScript)

#### [MODIFY] `src/screens/DashboardScreen.tsx`

- **Change**: Fix `IVoiceAction` import from `VoiceService` and resolve `Typography` subheader issues.

#### [MODIFY] `src/components/DockedController.tsx`

- **Change**: Add `BUILDER_PRESET_SAVED` to the local EventType definitions to resolve compiler warnings.

#### [MODIFY] Selected files with `Audio` context (e.g., `useVoiceControl.ts`)

- **Change**: Fix missing `import { Audio } from 'expo-av'` namespace where necessary.

## Verification Plan

### Automated Tests

- Run `npm run tsc` locally to ensure zero compiler errors remain in the `Dashboard` and `DockedController` files.

### Manual Verification

- **Test 1:** Attempt to save a new hardware profile via the setup wizard and verify it syncs to Supabase without triggering a 409/500 FK constraint violation.
- **Test 2:** Tap the core Voice FAB feature to confirm we are successfully greeted by an OS-level prompt requesting Audio Recording permissions (if not previously granted).

# Fix Account Avatar Data Loss & Polish Sweep

This plan addresses a critical data loss bug during profile updates and sweeps 10 remaining cosmetic/UX issues from the recent account audit.

## Proposed Changes

### [UI / Services] Account Profile & Avatar Fixes

#### [MODIFY] `src/services/AuthProfileService.ts`
- **Change:** Change `.upsert({ user_id: userId, ...cleanFields }, { onConflict: 'user_id' })` to `.update(cleanFields).eq('user_id', userId)` in `updateProfile()`.
- **Why:** Supabase `upsert` replaces the entire row, nullifying omitted fields. This caused avatar photo uploads to delete the avatar color, and vice versa. `update` only modifies the provided fields.
- **Change:** Catch the `23505` unique constraint violation and throw a clean "Username already taken" error (fixes L-08).

#### [MODIFY] `src/components/AccountModal.tsx`
- **Change (L-01):** Improve email regex in `handleChangeEmail` to `const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;`.
- **Change (L-02):** Update the delete account confirmation copy from "Type 'DELETE' to confirm" to "Tap 'Confirm Delete' to permanently erase your account."

#### [MODIFY] `src/hooks/useAccountOverview.ts`
- **Change (L-03):** Add an `Alert.alert('Settings Error', ...)` to the `.catch()` block in `saveNotifPrefs`.
- **Change (L-04):** Add `Alert.alert('Health Access Denied', 'Update in device Settings.')` when health permission is rejected and the toggle flips back.

#### [MODIFY] `App.tsx` & `src/components/auth/AuthFormSignIn.tsx`
- **Change (L-05):** Replace duplicated `STORAGE_OFFLINE_SKIP` strings with imports from `src/constants/storageKeys.ts`.
- **Change (L-06):** Remove the dead `onAuthSuccess` prop from `App.tsx` and `AuthFormSignIn.tsx`.

#### [MODIFY] `src/constants/storageKeys.ts`
- **Change (L-05):** Centralize `STORAGE_OFFLINE_SKIP = '@Sk8lytz_offline_skip'`.

#### [MODIFY] `src/services/ProfileService.types.ts`
- **Change (L-07):** Add `accepted_eula_version?: string`, `lifetime_distance_miles?: number`, and `lifetime_top_speed_mph?: number` to `UserProfile`.

#### [MODIFY] `src/services/SpeedTrackingService.ts` & `src/services/ScenesService.ts`
- **Change (L-09, L-10):** Update `fetchRecentSessions()`, `fetchLifetimeStats()`, and `getPublicScenes()` to fall back to `AsyncStorage` caches when offline.

## Verification Plan
- **Verify:** `npm run verify` passes with no TSC errors.
- **Verify:** Updating avatar photo does not nullify avatar color.
- **Verify:** Offline stats display cache instead of blank.

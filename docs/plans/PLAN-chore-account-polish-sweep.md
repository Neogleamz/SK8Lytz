# Implementation Plan

## Goal
Fix 10 low-severity account system polish items in a single sweep: email validation, DELETE confirm copy, silent error toasts, health toggle feedback, storage key constants, dead onAuthSuccess prop, stale UserProfile types, username uniqueness error, offline stats cache, and offline scenes cache.

## Source of Truth
- `account_audit.md` L-01 through L-10 — canonical source for all 10 items in this sweep
- `src/components/account/AccountModal.tsx`:350 — L-01 email regex, L-02 DELETE copy
- `src/components/account/AccountModal.tsx`:338-345 — L-02 DELETE alert copy
- `src/hooks/useAccountOverview.ts`:208 — L-03 silent catch; L-04 health toggle silent fail; L-219
- `App.tsx`, `src/components/auth/AuthFormSignIn.tsx`, `src/components/auth/AuthFooterActions.tsx` — L-05 inline storage key strings to extract
- `App.tsx` + `AuthFormSignIn.tsx` — L-06 dead `onAuthSuccess` prop
- `src/types/ProfileService.types.ts` — L-07 stale UserProfile interface
- `src/services/AuthProfileService.ts`:96 — L-08 username uniqueness error
- `src/services/SpeedTrackingService.ts` — L-09 offline stats fallback (depends on PLAN-fix-offline-session-persistence-queue)
- `src/services/ScenesService.ts` — L-10 offline scenes cache

## Steps

### Step 1 — Create storageKeys.ts constant file (L-05)
- Action: Create `src/constants/storageKeys.ts` — extract every `'@Sk8lytz_*'` and `'STORAGE_*'` string literal found in `App.tsx`, `AuthFormSignIn.tsx`, and `AuthFooterActions.tsx` into named exports (e.g., `export const STORAGE_OFFLINE_SKIP = 'STORAGE_OFFLINE_SKIP'`); then replace all inline string literals in those 3 files with imports from this constants file
- Source: `App.tsx`, `src/components/auth/AuthFormSignIn.tsx`, `src/components/auth/AuthFooterActions.tsx` — grep for `'@Sk8lytz_'` and `'STORAGE_'` literals
- Verify: `grep -rn "@Sk8lytz_\|STORAGE_OFFLINE" src/components/auth/ App.tsx` returns zero inline string literals; all references import from `storageKeys.ts`

### Step 2 — Fix email validation regex (L-01)
- Action: `view_file src/components/account/AccountModal.tsx L345-360` — locate `email.includes('@')`; replace with `/.+@.+\..+/.test(email)` for proper format validation
- Source: `src/components/account/AccountModal.tsx`:350
- Verify: `git diff` shows only the regex replacement; `'notanemail'` fails validation; `'a@b.c'` passes; `'@missing.com'` fails

### Step 3 — Fix DELETE confirm copy (L-02)
- Action: `view_file src/components/account/AccountModal.tsx L395-415` — locate the Alert copy containing "Type 'DELETE' to confirm"; replace with "Tap Delete to permanently remove your account." — update both the alert message and any instruction label if present
- Source: `src/components/account/AccountModal.tsx`:402
- Verify: `git diff` shows only the string replacement; no logic altered

### Step 4 — Fix silent notif pref save error (L-03)
- Action: `view_file src/hooks/useAccountOverview.ts L205-215` — locate `.catch(() => {})`; replace with `.catch(e => Alert.alert('Settings Error', 'Failed to save notification preference.'))`
- Source: `src/hooks/useAccountOverview.ts`:208
- Verify: `git diff` shows only catch body replacement; Alert is imported (add import if missing)

### Step 5 — Add health toggle denial feedback (L-04)
- Action: `view_file src/hooks/useAccountOverview.ts L215-228` — locate the `setHealthSync(false)` call on permission denied; add `Alert.alert('Health Access Required', 'Please enable Health access in your device Settings.')` immediately after the `setHealthSync(false)` call
- Source: `src/hooks/useAccountOverview.ts`:219
- Verify: `git diff` shows only the Alert insertion; existing `setHealthSync(false)` call unchanged

### Step 6 — Remove dead onAuthSuccess prop (L-06)
- Action: `view_file App.tsx` — locate `onAuthSuccess` prop being passed to `AuthFormSignIn` or `AuthScreen`; `view_file src/components/auth/AuthFormSignIn.tsx` — locate `onAuthSuccess` parameter; remove the prop from the parent call site and remove the parameter declaration and any `onAuthSuccess()` call from `AuthFormSignIn`
- Source: `App.tsx` — prop call site; `src/components/auth/AuthFormSignIn.tsx` — parameter declaration
- Verify: `grep -rn "onAuthSuccess" src/` returns zero results after removal; TypeScript compiles

### Step 7 — Add missing fields to UserProfile interface (L-07)
- Action: `view_file src/types/ProfileService.types.ts` fully — locate the `UserProfile` interface; add `accepted_eula_version?: number`, `lifetime_distance_miles?: number`, and `lifetime_top_speed_mph?: number` as optional fields
- Source: `src/types/ProfileService.types.ts` — `UserProfile` interface block
- Verify: `git diff` shows only the 3 field additions; no existing fields removed or renamed; TypeScript compiles

### Step 8 — Handle username uniqueness error (L-08)
- Action: `view_file src/services/AuthProfileService.ts L90-105` — locate the `upsert` call; wrap it in try/catch; in the catch, check `if ((e as any).code === '23505' || error?.code === '23505')` → `throw new Error('Username already taken')`; caller hook should surface this message to the user via Alert
- Source: `src/services/AuthProfileService.ts`:96
- Verify: Simulating a `23505` Postgres error in the upsert path produces `'Username already taken'` error message in the UI

### Step 9 — Add offline stats cache fallback (L-09)
- Action: `view_file src/services/SpeedTrackingService.ts` — locate `fetchRecentSessions()`; at the end of the function, if the result array is empty (network failure / offline), read from `PENDING_SESSION_QUEUE_KEY` AsyncStorage key and return those locally queued sessions instead (requires PLAN-fix-offline-session-persistence-queue to be merged first — add note in file comment)
- Source: `src/services/SpeedTrackingService.ts` — `fetchRecentSessions()` function
- Verify: In offline mode with queued sessions, `fetchRecentSessions()` returns the locally cached records instead of an empty array

### Step 10 — Add offline scenes cache (L-10)
- Action: `view_file src/services/ScenesService.ts` — locate `getPublicScenes()`; after a successful fetch, write results to `AsyncStorage.setItem('@SK8Lytz_PublicScenes_Cache', JSON.stringify(scenes))`; if the fetch returns an empty array (network failure), read `AsyncStorage.getItem('@SK8Lytz_PublicScenes_Cache')` and return the cached array; add the key to `storageKeys.ts`
- Source: `src/services/ScenesService.ts` — `getPublicScenes()` function
- Verify: After one successful fetch, killing network and re-fetching returns cached scenes; `@SK8Lytz_PublicScenes_Cache` constant exported from `storageKeys.ts`

### Step 11 — Final verify
- Action: Run `npm run verify`; spot-check each fix: email validation, DELETE copy, error toast, health alert, storage constants, no dead prop, UserProfile types, username error, offline stats, offline scenes
- Source: All files touched in Steps 1-10
- Verify: `npm run verify` exits 0; all 10 items visually confirmed or unit-testable

## Out of Scope
- New features beyond the 10 listed items
- BLE protocol or device layer changes
- Supabase schema migrations (this plan does not add columns)
- EULA flow (see PLAN-fix-offline-eula-bypass)
- Crew or auth flow changes

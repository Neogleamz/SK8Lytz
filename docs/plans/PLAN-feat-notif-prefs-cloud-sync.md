# Implementation Plan

## Goal
Sync notification preferences (Crew Invites, Session Reminders, Leadership Changes) to Supabase `user_profiles` so they persist across devices and reinstalls.

## Source of Truth
- `src/hooks/useAccountOverview.ts`:207 — `AsyncStorage.setItem(NOTIF_PREF_KEY, ...)` is local-only; no Supabase write
- `src/types/supabase.ts`:2931-2978 — `user_profiles` table definition — confirm `notif_preferences` column is absent before migration
- `src/services/AuthProfileService.ts` — `updateProfile()` method that wraps the Supabase `user_profiles` upsert

## Steps

### Step 1 — Read useAccountOverview and AuthProfileService
- Action: `view_file src/hooks/useAccountOverview.ts L195-240`; then `view_file src/services/AuthProfileService.ts` fully — map `saveNotifPrefs()`, `loadData()`, `NOTIF_PREF_KEY`, the prefs object shape, and `updateProfile()` signature
- Source: `src/hooks/useAccountOverview.ts`:195-240, `src/services/AuthProfileService.ts`:1-end
- Verify: Know the exact prefs object shape (keys and value types) and the `updateProfile()` parameter type before writing migration SQL

### Step 2 — Confirm column absence in supabase.ts types
- Action: `view_file src/types/supabase.ts L2931-2978` — confirm `notif_preferences` is not already in `user_profiles` table definition
- Source: `src/types/supabase.ts`:2931-2978
- Verify: `notif_preferences` column is definitively absent; proceeding with migration is safe

### Step 3 — Write and run Supabase migration
- Action: Via Supabase MCP `apply_migration`: SQL = `ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS notif_preferences JSONB DEFAULT '{}';` — name the migration `add_notif_preferences_to_user_profiles`
- Source: `src/types/supabase.ts`:2931-2978 — confirms absence; migration adds the column
- Verify: Migration applies without error; Supabase Studio shows `notif_preferences JSONB` column on `user_profiles`

### Step 4 — Regenerate TypeScript types
- Action: Run `/db-sync` workflow to regenerate `src/types/supabase.ts` from the live schema
- Source: `src/types/supabase.ts` — file to be regenerated
- Verify: `src/types/supabase.ts` now contains `notif_preferences?: Json | null` in the `user_profiles` Row type

### Step 5 — Update saveNotifPrefs() to also write to Supabase
- Action: In `useAccountOverview.ts` `saveNotifPrefs()` — after the existing `AsyncStorage.setItem(NOTIF_PREF_KEY, ...)` call, add a fire-and-forget background Supabase write: `profileService.updateProfile({ notif_preferences: prefs }).catch(e => AppLogger.warn('[NotifPrefs] cloud sync failed', e))`. Do NOT await or block the UI on this call
- Source: `src/hooks/useAccountOverview.ts`:207
- Verify: `git diff` shows only the addition of the background write; existing AsyncStorage write is unchanged; UI does not block

### Step 6 — Update loadData() to merge cloud prefs over local
- Action: In `useAccountOverview.ts` `loadData()` — after fetching the user profile, if `profile.notif_preferences` is non-null and non-empty, merge cloud values over AsyncStorage values: `const merged = { ...localPrefs, ...profile.notif_preferences }`. Use `merged` to set state and write back to AsyncStorage
- Source: `src/hooks/useAccountOverview.ts` — `loadData()` function
- Verify: On fresh sign-in with pre-existing cloud prefs, local state reflects cloud values; no override of keys absent from cloud

### Step 7 — Verify cross-device sync
- Action: Run `npm run verify`; manual test: set notification prefs on device/account A → sign out → sign in on a second device or simulator → confirm prefs match
- Source: `src/hooks/useAccountOverview.ts` — full notif prefs flow
- Verify: `npm run verify` exits 0; cross-device pref sync confirmed

## Out of Scope
- Push notification delivery pipeline
- FCM or APNS token management
- Notification permission prompts
- Notification preference UI redesign
- Any BLE or device layer changes

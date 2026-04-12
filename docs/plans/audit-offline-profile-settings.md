# Plan: `audit/offline-profile-settings`

### Design Decisions & Rationale
User profile settings (display name, avatar color) are stored in Supabase but must be readable offline via `AsyncStorage`. Any write while offline should be queued and synced on reconnect — the same "shadow write" pattern already used by device registration sync.

---

## Proposed Changes

### [MODIFY] `src/components/AccountModal.tsx`
- On load, read `@Sk8lytz_auth_username` from AsyncStorage as the fallback display name (this key is already in the AsyncStorage Registry).
- On display name change: write to AsyncStorage immediately, then attempt Supabase update. If Supabase fails (offline), queue the update in a new `@sk8lytz_pending_profile_sync` AsyncStorage key.

### [NEW] `src/services/ProfileSyncService.ts`
- `syncPendingProfileUpdates()`: reads `@sk8lytz_pending_profile_sync`, attempts each queued update against Supabase, clears on success.
- Called on app foreground and on Supabase session restore.

### [MODIFY] `App.tsx` (or auth context)
- On Supabase `onAuthStateChange` to SIGNED_IN, call `ProfileSyncService.syncPendingProfileUpdates()`.

---

## Open Questions
- **Q:** Is avatar color stored in Supabase `users` metadata, or only locally? Need to audit `AccountModal.tsx` save logic.

## Verification Plan
1. Enable airplane mode.
2. Change display name in Account settings.
3. Disable airplane mode.
4. Verify the new name syncs to Supabase and appears on other devices.

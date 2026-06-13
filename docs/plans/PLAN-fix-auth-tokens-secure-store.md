# Implementation Plan

## Goal
Replace AsyncStorage with expo-secure-store as the Supabase auth token storage adapter to protect tokens on rooted/jailbroken devices.

## Source of Truth
- `src/services/supabaseClient.ts`:11-17 — `storage: AsyncStorage` passed to `createClient` — the exact adapter being replaced
- `package.json` — dependency registry to check for existing `expo-secure-store` before any install
- Supabase JS v2 docs — `SupportedStorage` interface: `getItem(key): Promise<string | null>`, `setItem(key, value): Promise<void>`, `removeItem(key): Promise<void>`

## Steps

### Step 1 — Read supabaseClient.ts fully
- Action: `view_file src/services/supabaseClient.ts` — map the entire `createClient` call, all options passed, the current `storage: AsyncStorage` line, and any other auth options set
- Source: `src/services/supabaseClient.ts`:1-end
- Verify: Know exact line numbers for the storage adapter before writing any replacement

### Step 2 — Check package.json for expo-secure-store
- Action: `view_file package.json` — search for `expo-secure-store` in dependencies and devDependencies
- Source: `package.json`:dependencies block
- Verify: Definitively know whether the package is already installed; proceed to Step 3 only if absent

### Step 3 — Dependency Diet check (if not installed)
- Action: If `expo-secure-store` is absent: present Dependency Proposal to user — Weight: ~52KB unpacked; Activity: actively maintained by Expo team (check last commit); Necessity: AsyncStorage is unencrypted plaintext on Android/iOS filesystem, SecureStore uses Keychain (iOS) and EncryptedSharedPreferences (Android). Await explicit user approval before running `npx expo install expo-secure-store`
- Source: `package.json` — confirming absence
- Verify: User has approved; `package.json` shows `expo-secure-store` after install

### Step 4 — Implement SecureStoreAdapter
- Action: In `src/services/supabaseClient.ts`, above the `createClient` call, add `SecureStoreAdapter` class implementing Supabase `SupportedStorage`: `getItem(key) → SecureStore.getItemAsync(key)`, `setItem(key, value) → SecureStore.setItemAsync(key, value)`, `removeItem(key) → SecureStore.deleteItemAsync(key)`. All methods `async`, all return correct types with no `any` casts
- Source: `src/services/supabaseClient.ts`:11-17 — insertion point
- Verify: Class implements all 3 required methods; TypeScript infers correct return types

### Step 5 — Replace storage adapter in createClient
- Action: Replace `storage: AsyncStorage` with `storage: new SecureStoreAdapter()` in the `createClient` options object
- Source: `src/services/supabaseClient.ts`:11-17
- Verify: `git diff HEAD -- src/services/supabaseClient.ts` shows only the storage line changed; no other lines touched

### Step 6 — Write one-time migration on first launch
- Action: Create `src/utils/migrateAuthTokens.ts` — async function `migrateAuthTokensToSecureStore()`: reads `AsyncStorage.getItem('supabase.auth.token')`; if value found AND `SecureStore.getItemAsync('supabase.auth.token')` returns null, writes value to SecureStore then deletes from AsyncStorage. Call this function once in `App.tsx` before the `init()` auth check, guarded by a migration-complete flag in AsyncStorage (`@Sk8lytz_auth_migration_v1`)
- Source: `App.tsx`:151 — `init()` call site for insertion point
- Verify: On simulated upgrade (pre-existing AsyncStorage token), token moves to SecureStore and is deleted from AsyncStorage; migration flag is set; function does not run again on next launch

### Step 7 — Verify end-to-end session restore
- Action: Fresh install test — sign in → kill app → relaunch → session restored without re-authentication prompt. Run `npm run verify`
- Source: `src/services/supabaseClient.ts` — integration under test
- Verify: `npm run verify` exits 0; app relaunch restores session from SecureStore successfully

## Out of Scope
- Other AsyncStorage keys (`@SK8Lytz_*` device/scene/queue keys) — only auth tokens
- Biometric unlock layer over SecureStore
- Multi-account token storage
- Any BLE or device layer changes

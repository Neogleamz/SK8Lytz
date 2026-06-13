# Implementation Plan — `refactor/storage-key-registry-v2`

## Goal
Audit the AsyncStorage key ecosystem: fix 4 raw string key usages (bypassing constants), document all undocumented keys in Master Reference §A.2, and ensure `storageKeys.ts` is the single source of truth for all 20+ keys.

## Source of Truth
- `artifacts/deepdive_raw/R-24_findings.json` — full key inventory (9 keys, 30+ usages)
- `artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json` — undocumented key flags
- `src/constants/storageKeys.ts` (or equivalent) — current key registry
- `tools/SK8Lytz_App_Master_Reference.md` §A.2 (AsyncStorage Key Registry)

**Note:** `fix/async-storage-key-registry` was previously merged (b707386d). This task addresses the **remaining violations** found by the new deep-dive R-24 sweep.

## Out of Scope
- Migrating existing stored data (keys not changing value)
- `src/types/supabase.ts`

---

## Step 1 — Audit existing `storageKeys.ts`
- **Action:** View `src/constants/storageKeys.ts` (or wherever constants are defined). List all defined constants and their string values.
- **Verify:** Inventory matches or exceeds what R-24 found.

## Step 2 — Fix raw string in `AdminToolsModal.tsx:104`
- **Finding:** Uses `'@Sk8lytz_device_configs'` as raw string instead of `CONFIGS_KEY` constant
- **Action:** Import `CONFIGS_KEY` from storageKeys and replace raw string
- **Verify:** Zero raw `@Sk8lytz` strings in AdminToolsModal.

## Step 3 — Fix raw string in `useBLEScanner.ts:56`
- **Finding:** Uses `'@sk8lytz_app_settings'` instead of `STORAGE_APP_SETTINGS` constant
- **Action:** Import and replace
- **Verify:** Zero raw storage strings in useBLEScanner.

## Step 4 — Fix inconsistent key in `useAccountOverview.ts:82`
- **Finding:** Uses `'@sk8lytz_auto_pause_enabled'` raw string in one place, but `STORAGE_AUTO_PAUSE_ENABLED` constant elsewhere in the same file
- **Action:** Replace the raw string with the constant
- **Verify:** All auto_pause_enabled references use the constant.

## Step 5 — Fix inconsistent key in `AuthContext.tsx:136`
- **Finding:** Uses `'@Sk8lytz_auth_last_email'` raw string while `AuthScreen.tsx` uses `STORAGE_LAST_EMAIL` constant
- **Action:** Import and replace in AuthContext
- **Verify:** Both files use the same constant.

## Step 6 — Register undocumented keys in Master Reference
- **Undocumented keys to add:** `@Sk8lytz_offline_skip`, `@Sk8lytz_auth_last_email`, `@sk8lytz_auto_pause_enabled`, `CACHE_KEY` (SkaterStatsPanel), `STORAGE_DEMO_HALO`, `STORAGE_DEMO_SOUL`
- **Action:** View `tools/SK8Lytz_App_Master_Reference.md` §A.2 and add entries for each undocumented key: key name, constant name, writers, readers, TTL/expiry if applicable.
- **Verify:** §A.2 covers all keys found in R-24 inventory.

## Step 7 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors.

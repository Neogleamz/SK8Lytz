# Implementation Plan

## Task: fix/storage-key-centralization
## Cluster: TC-08
## Risk: [M-RISK] | Size: [Meal] | Layer: [SERVICE]
## Status: [⚪ READY]

---

## Problem Statement

8 AsyncStorage key violations found across the codebase:

**Type 1 — Active Split-Brain (H-RISK):**
- `@Sk8lytz_skate_spots_cache` written independently by both `LocationService` AND `SkateSpotsService` → uncontrolled cache overwrites
- `@Sk8lytz_custom_groups` read by `useRegistration` (migration) while `GroupRepository` continues writing to it → destructive feedback loop
- `@Sk8lytz_registered_devices` read directly by `useDashboardAutoConnect` bypassing `DeviceRepository` (the SoT owner)

**Type 2 — Decentralized Key Definitions (M-RISK):**
- `@Sk8lytz_remember_creds` redefined locally in `AuthScreen.tsx` (already in `storageKeys.ts`)
- `@Sk8lytz_demo_mode` hardcoded in `useBLE.ts`, `BleConnectionManager.ts`, `AuthScreen.tsx` (constant exists: `STORAGE_DEMO_MODE`)
- `@sk8lytz_app_settings` hardcoded in `AppLogger.ts` bypassing `AppSettingsService`

---

## Source of Truth

- **`artifacts/deepdive_raw/R-24_findings.json`** → R-24-001 through R-24-008
- **`src/constants/storageKeys.ts`** — canonical key constants file
- **`src/services/LocationService.ts:232`** — `const CACHE_KEY = '@Sk8lytz_skate_spots_cache';`
- **`src/services/SkateSpotsService.ts`** — independently owns same key
- **`src/hooks/useRegistration.ts:182`** — `AsyncStorage.getItem('@Sk8lytz_custom_groups')`
- **`src/hooks/useDashboardAutoConnect.ts:303`** — direct `@Sk8lytz_registered_devices` read

---

## Proposed Changes

### [NEW] `src/constants/storageKeys.ts` additions
Add any missing keys:
```typescript
export const STORAGE_SKATE_SPOTS_CACHE = '@Sk8lytz_skate_spots_cache';
export const STORAGE_APP_SETTINGS = '@sk8lytz_app_settings';
// Verify STORAGE_REMEMBER_CREDS and STORAGE_DEMO_MODE already present
```

### Fix Type 1 — Active Split-Brain

**`src/services/SkateSpotsService.ts`:** Import `STORAGE_SKATE_SPOTS_CACHE` from `storageKeys.ts` and use it. Remove local definition.

**`src/services/LocationService.ts:232`:** Same — import and use the shared constant. Then trace who is the SoT owner for this cache and document it. If `SkateSpotsService` owns it, `LocationService` should call `SkateSpotsService.getCachedSpots()` instead of reading directly.

**`src/hooks/useRegistration.ts:182`:** Add a migration completion flag key (e.g., `STORAGE_GROUPS_MIGRATED_V2`). Check flag before migrating. After migration, mark the flag so it never re-runs and doesn't collide with `GroupRepository`'s ongoing writes.

**`src/hooks/useDashboardAutoConnect.ts:303`:** Replace `AsyncStorage.getItem('@Sk8lytz_registered_devices')` with `DeviceRepository.getInstance().getDevices()`.

### Fix Type 2 — Decentralized Definitions

**`src/screens/AuthScreen.tsx:26`:** Remove local `const STORAGE_REMEMBER_CREDS = ...` and import from `storageKeys.ts`.

**`src/hooks/useBLE.ts:181`:** Replace hardcoded `'@Sk8lytz_demo_mode'` with `STORAGE_DEMO_MODE` import.

**`src/services/BleConnectionManager.ts:117`:** Same as above.

**`src/screens/AuthScreen.tsx:56`:** Same as above.

**`src/services/AppLogger.ts:566`:** Replace hardcoded `'@sk8lytz_app_settings'` with `STORAGE_APP_SETTINGS` import.

---

## Verification Plan

### Automated
- `npm run verify` — TSC clean (all keys typed, no magic strings)

### Manual
1. Open skate spots map → spots load from cache → navigate away and back → cache still valid (no overwrite)
2. Toggle demo mode in Settings → restart app → demo mode persists correctly
3. Register a device → confirm `DeviceRepository.getDevices()` reflects it in auto-connect

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\storage-key-centralization\`
Branch: `fix/storage-key-centralization`

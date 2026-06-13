# Implementation Plan

## Task: sweep-storage-keys
**Slug:** sweep-storage-keys
**Wave:** [WAVE:2] — Prerequisite: Wave 1 fully merged
**Size:** [Snack] — 4 files
**Risk:** [H-RISK] — AsyncStorage key registry is a data integrity contract; wrong keys = silent data loss
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_DATA_LAYER_findings.json` + `artifacts/deepdive_raw/R-24_findings.json`
**Prerequisite:** Wave 1 fully merged

## Goal
Fix 16 findings related to AsyncStorage key management. Critical: there are 3 key collision bugs where different features use the same or conflicting AsyncStorage keys. `DashboardScreen` hardcodes `'@Sk8lytz_Favorites'` instead of importing from the key registry. `AppSettingsService` blocks its local cache update behind a synchronous Supabase network call — violating the offline-first mandate. Consolidate all stray key literals into the central `STORAGE_KEYS` registry.

## Decision Log
- **Key collision (`useFavorites` + `QuickPresetModal`, R-24 HIGH)**: Both resolve a favorites key independently, with `QuickPresetModal` using `'@Sk8lytz_QuickPresets'` in a way that overlaps with the favorites registry key resolution logic. Must audit both against the registry and fix to use the canonical key.
- **Hardcoded `'@Sk8lytz_Favorites'` in `DashboardScreen` (R-24 HIGH)**: `DashboardScreen` bypasses `useFavorites` and reads AsyncStorage directly with a hardcoded string. This is a ticking time bomb — if the key is ever renamed in the registry, this silent read will silently stop working.
- **`AppSettingsService` blocking Supabase call (R-05 HIGH)**: The local cache write is gated behind a `.then()` on the Supabase call. If the user is offline, the local cache is never written. Must flip the order: write local first, sync remote asynchronously.
- **`DeviceRepository` local key definitions (R-24 MEDIUM)**: `'@Sk8lytz_deleted_macs'` and `'@Sk8lytz_pending_sync'` are defined as local consts rather than imported from the registry. Register them.

## Files to Create/Modify

### [MODIFY] src/hooks/useFavorites.ts
- Fix key collision at L33 — resolve canonical key from `STORAGE_KEYS` registry, not locally
- Fix duplicate `instanceof Error` ternary at L178 (R-06)
- Fix direct Supabase write at L187 — ensure optimistic local write happens first, Supabase sync is async background (R-05)

### [MODIFY] src/services/AppSettingsService.ts
- Fix L91 — write to AsyncStorage locally first (optimistic), then fire Supabase sync in the background without blocking (R-05)

### [MODIFY] src/services/DeviceRepository.ts
- Register `'@Sk8lytz_deleted_macs'` (L126) and `'@Sk8lytz_pending_sync'` (L715) into the central `STORAGE_KEYS` registry and import from there

### [MODIFY] src/components/docked/QuickPresetModal.tsx
- Fix `'@Sk8lytz_QuickPresets'` key at L48 — import from `STORAGE_KEYS` registry

## Out of Scope
- `DashboardScreen.tsx` hardcoded key fix is handled in `sweep-ui-screens-dashboard` (Wave 1)
- No BLE changes
- No UI component restructuring

## Verification Plan
- `npm run verify` — TSC must pass
- Verify all key literals are eliminated from non-registry files via `grep '@Sk8lytz' src/`
- Manual verify: offline scenario — `AppSettingsService` must update local cache when Supabase is unreachable

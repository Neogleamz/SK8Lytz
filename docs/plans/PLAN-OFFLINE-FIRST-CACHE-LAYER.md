# Implementation Plan
# PLAN-OFFLINE-FIRST-CACHE-LAYER

## Summary
6 critical data fetching paths violate the offline-first mandate by hitting Supabase network before checking AsyncStorage cache. This causes UI hangs on slow networks and complete failures when the device is offline (skating condition). The pattern fix is consistent: return cached data immediately, then refresh from network asynchronously.

**Batch:** `BATCH:offline-first-sweep`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/hooks/useBLE.ts` — Line 199 (R-05-001): hardware_blacklist
- `src/services/AppSettingsService.ts` — Line 32 (R-05-002): app settings
- `src/services/GradientsService.ts` — Line 18 (R-05-003): gradients
- `src/services/ScenesService.ts` — Line 198 (R-05-006): scenes
- `src/services/LocationService.ts` — Line 231 (R-05-004): skate spots
- `src/services/SkateSpotsService.ts` — Line 21 (R-05-005): skate spots
- Raw audit: `R-05_findings.json`

---

## Findings

| ID | File | Line | Network Call | Severity |
|----|------|------|-------------|----------|
| R-05-001 | `src/hooks/useBLE.ts` | 199 | `hardware_blacklist` | HIGH |
| R-05-002 | `src/services/AppSettingsService.ts` | 32 | `sk8lytz_app_settings` | MEDIUM |
| R-05-003 | `src/services/GradientsService.ts` | 18 | `custom_builder_presets` | MEDIUM |
| R-05-006 | `src/services/ScenesService.ts` | 198 | `custom_builder_presets` scenes | MEDIUM |
| R-05-004 | `src/services/LocationService.ts` | 231 | `skate_spots` | LOW |
| R-05-005 | `src/services/SkateSpotsService.ts` | 21 | `skate_spots` | LOW |

---

## Offline-First Pattern (Apply to All)

```typescript
// BEFORE — blocks on network
const { data, error } = await supabase.from('table').select('*');
return data;

// AFTER — cache-first + background sync
const CACHE_KEY = '@Sk8lytz_<data_name>_cache';

// 1. Return cache immediately
const cached = await AsyncStorage.getItem(CACHE_KEY);
if (cached) setState(JSON.parse(cached)); // or return immediately

// 2. Background network sync (non-blocking)
supabase.from('table').select('*').then(({ data, error }) => {
  if (data && !error) {
    setState(data);
    AsyncStorage.setItem(CACHE_KEY, JSON.stringify(data)).catch(() => {});
  }
});
```

---

## Implementation Steps

### Step 1 — hardware_blacklist cache (useBLE.ts — HIGHEST PRIORITY)
**File:** `src/hooks/useBLE.ts:199`
- Add cache key `@Sk8lytz_hardware_blacklist`
- Read from AsyncStorage first → populate blacklist Set immediately
- Fire Supabase fetch in background → update cache and in-memory Set when resolved
- Fail open: if both cache and network fail, use empty Set (keep existing behavior)

### Step 2 — App settings cache (AppSettingsService.ts)
**File:** `src/services/AppSettingsService.ts:32`
- Add cache key `@Sk8lytz_app_settings`  
- Return cached `settingsMap` immediately on startup
- Background-sync from Supabase and merge into cache

### Step 3 — Gradients cache (GradientsService.ts)
**File:** `src/services/GradientsService.ts:18`
- `LOCAL_GRADIENTS_KEY` already exists in this file — use it
- Return local AsyncStorage data first, then merge cloud data behind the scenes

### Step 4 — Scenes cache (ScenesService.ts)
**File:** `src/services/ScenesService.ts:198`
- `LOCAL_SCENES_KEY` already exists — same pattern as Step 3

### Step 5 — Skate spots cache (LocationService + SkateSpotsService)
**File:** `src/services/LocationService.ts:231` and `src/services/SkateSpotsService.ts:21`
- Cache key: `@Sk8lytz_skate_spots_cache`
- TTL: 24 hours (static data doesn't change often)
- Return cached spots when offline; refresh in background when online

---

## Cache Key Registry Update Required
After implementation, add all new cache keys to `tools/SK8Lytz_App_Master_Reference.md` §2 AsyncStorage Key Registry (VS-003 compliance).

---

## Verification
- `npm run verify`
- Airplane mode test: kill network → open app → verify gradients, settings, and BLE blacklist load from cache
- Background sync test: restore network → verify cache updates within 30s

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Services]`
- `[Risk: M-RISK]`
- `[Size: Banquet]`
- `[Cognitive Load: Medium]`
- `[BATCH: offline-first-sweep]`

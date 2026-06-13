# Implementation Plan
# PLAN-ASYNC-STORAGE-KEY-REGISTRY

## Summary
8 AsyncStorage keys are either undocumented, duplicated, or use the banned `ng_` legacy namespace. Two keys using the `ng_` prefix are particularly urgent — they were explicitly banned due to prior split-brain bugs and must be migrated. All keys must be registered in the Master Reference §2 Key Registry.

**Batch:** `BATCH:storage-key-hygiene`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/components/admin/tools/Sk8LytzProgrammer.tsx` — Line 47 (R-24-004): `ng_programmer_profiles` → BANNED
- `src/hooks/useProductCatalog.ts` — Line 25 (R-24-005): `ng_product_catalog` → BANNED  
- `src/services/SpeedTrackingService.ts` — Line 56 (R-24-003): `PENDING_SESSION_QUEUE_KEY` duplication
- `src/components/auth/AuthSandboxToggle.tsx` — Line 23 (R-24-001): `@Sk8lytz_demo_mode` undocumented
- `src/context/SessionContext.tsx` — Line 180 (R-24-002): `@sk8lytz_auto_pause_enabled` undocumented
- `src/hooks/useDashboardGroups.ts` — Line 244 (R-24-006): `@Sk8lytz_last_group_patterns` undocumented
- `src/context/SessionContext.tsx` — Line 124 (R-24-007): `@sk8lytz_pending_bg_end` undocumented
- `src/hooks/ble/useBLEScanner.ts` — Line 105 (R-24-008): `@sk8lytz_scanner_telemetry_queue` undocumented
- Raw audit: `R-24_findings.json`

---

## Findings

### HIGH — Banned `ng_` Namespace (Must Migrate)

| ID | File | Line | Current Key | New Key |
|----|------|------|-------------|---------|
| R-24-004 | `Sk8LytzProgrammer.tsx` | 47 | `ng_programmer_profiles` | `@Sk8lytz_programmer_profiles` |
| R-24-005 | `useProductCatalog.ts` | 25 | `ng_product_catalog` | `@Sk8lytz_product_catalog` |

> [!CAUTION]
> Key migration requires a one-time data migration: on first run after upgrade, read from the old `ng_` key, write to the new `@Sk8lytz_` key, then delete the old key. Failure to migrate = data loss for existing users.

### HIGH — Key Duplication (Split-Brain Risk)

| ID | File | Line | Issue |
|----|------|------|-------|
| R-24-003 | `SpeedTrackingService.ts` | 56 | Local `PENDING_SESSION_QUEUE_KEY` duplicates `storageKeys.ts` |
| R-24-001 | `AuthSandboxToggle.tsx` | 23 | `@Sk8lytz_demo_mode` used in 4 files as string literal |
| R-24-002 | `SessionContext.tsx` | 180 | `@sk8lytz_auto_pause_enabled` duplicated in SessionContext + useAccountOverview |

### MEDIUM — Undocumented Keys

| ID | File | Key |
|----|------|-----|
| R-24-006 | `useDashboardGroups.ts` | `@Sk8lytz_last_group_patterns` |
| R-24-007 | `SessionContext.tsx` | `@sk8lytz_pending_bg_end` |
| R-24-008 | `useBLEScanner.ts` | `@sk8lytz_scanner_telemetry_queue` |

---

## Implementation Steps

### Step 1 — Fix banned `ng_` keys (data migration)

**`Sk8LytzProgrammer.tsx:47`**
```typescript
// Migration on component mount
const OLD_KEY = 'ng_programmer_profiles';
const NEW_KEY = '@Sk8lytz_programmer_profiles';

const migrateKey = async () => {
  try {
    const old = await AsyncStorage.getItem(OLD_KEY);
    if (old) {
      await AsyncStorage.setItem(NEW_KEY, old);
      await AsyncStorage.removeItem(OLD_KEY);
    }
  } catch (error) {
    getAppLogger().error(`[Migration] Failed to migrate ${OLD_KEY}`, error);
  }
};
```
Run migration in `useEffect(migrateKey, [])` on first mount.

Same pattern for `ng_product_catalog` → `@Sk8lytz_product_catalog`.

### Step 2 — Consolidate duplicate keys to storageKeys.ts
**`src/hooks/useProductCatalog.ts`**: Remove local constant, import from `storageKeys.ts`
**`src/services/SpeedTrackingService.ts`**: Remove `PENDING_SESSION_QUEUE_KEY` local export, import from `storageKeys.ts`
**`src/components/auth/AuthSandboxToggle.tsx`**: Extract `@Sk8lytz_demo_mode` to `storageKeys.ts`, import it everywhere it's used

### Step 3 — Register all 8 keys in Master Reference
**File:** `tools/SK8Lytz_App_Master_Reference.md` §2 AsyncStorage Key Registry
Add entries for all 8 keys with their owning file, type, and TTL/update strategy.

### Step 4 — Normalize key casing
Note: mixed casing exists (`@Sk8lytz_` vs `@sk8lytz_`). Do NOT normalize in this task — the data migration risk is too high. Log a follow-up task for casing normalization after registry is complete.

---

## Verification
- `npm run verify`
- `grep -rn "ng_programmer\|ng_product" src/` → should return 0
- `grep -rn "PENDING_SESSION_QUEUE_KEY" src/services/SpeedTrackingService.ts` → should import, not define
- Confirm Master Reference §2 has all 8 key entries

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Services]`
- `[Risk: M-RISK]`
- `[Size: Meal]`
- `[Cognitive Load: Low]`
- `[BATCH: storage-key-hygiene]`

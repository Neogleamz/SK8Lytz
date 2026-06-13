# Implementation Plan
# PLAN-ASYNC-ERROR-HARDENING

## Summary
Two complementary error handling deficiencies exist across the codebase:
- **R-11**: 120+ async `await` operations missing `try/catch` (naked awaits)
- **R-06**: 72+ catch blocks missing `e instanceof Error` unwrapping, logging raw unknown objects

These two rules are batched together because they are always co-located: every naked await that gains a try/catch also needs proper error unwrapping in its new catch block.

**Batch:** `BATCH:error-handling-sweep`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/services/CrewProfileService.ts` — Lines 31–461 (R-11, 55 instances — highest density)
- `src/services/CrewService.ts` — Lines 76–597 (R-11, 20+ instances)
- `src/services/AuthProfileService.ts` — Lines 24–113 (R-11)
- `src/components/auth/AuthFooterActions.tsx` — Lines 31–91 (R-11)
- `src/services/DeviceRepository.ts` — Lines 140–724 (R-06, 8 instances)
- `src/services/GradientsService.ts` — Lines 26–132 (R-06, 7 instances)
- `src/hooks/useGlobalTelemetry.ts` — Lines 126–254 (R-06)
- Raw audit: `R-11_findings.json` (120+ findings), `R-06_findings.json` (72+ findings)

---

## Findings Summary

### R-11 — Naked Awaits (Top Priority Files by Density)
| File | Instance Count | Priority |
|------|---------------|----------|
| `src/services/CrewProfileService.ts` | ~55 | P1 |
| `src/services/CrewService.ts` | ~20 | P1 |
| `src/services/AuthProfileService.ts` | ~6 | P2 |
| `src/components/auth/AuthFooterActions.tsx` | 5 | P2 |
| `src/hooks/useDashboardAutoConnect.ts` | 3 | P2 |
| `src/hooks/useFavorites.ts` | 3 | P2 |
| `src/components/auth/AuthSandboxToggle.tsx` | 1 | P3 |
| `src/screens/AuthScreen.tsx` | 1 | P3 |
| `src/providers/ComplianceGate.tsx` | 1 | P3 |

> [!NOTE]
> Test files (`ble-simulator.test.ts`) flagged by R-11 are false positives (HIGH risk). Skip them.

### R-06 — Untyped Error Catch Blocks (Standard Fix Pattern)
Same files as R-11 plus: `GradientsService.ts`, `GroupRepository.ts`, `SpeedTrackingService.ts`, `SkateSpotsService.ts`, `HealthSyncService.ts`, `LocationService.ts`, `BlePingService.ts`

---

## Implementation Steps

### Step 1 — Establish the error unwrap pattern (Boy Scout setup)
All catch blocks should use this pattern:
```typescript
} catch (e: unknown) {
  const msg = e instanceof Error ? e.message : String(e);
  AppLogger.error('[Context] Operation failed', { error: msg });
}
```

### Step 2 — Sweep CrewProfileService (highest density — 55 instances)
**Strategy:** Wrap entire public methods that do not already have try/catch. Example:
```typescript
// BEFORE — naked await at line 31
const { data: { user } } = await supabase.auth.getUser();

// AFTER — method-level try/catch
async createCrew(...) {
  try {
    const { data: { user } } = await supabase.auth.getUser();
    // ... rest of method
  } catch (e: unknown) {
    const msg = e instanceof Error ? e.message : String(e);
    AppLogger.error('[CrewProfileService] createCrew failed', { error: msg });
    throw new Error(msg);
  }
}
```

### Step 3 — Sweep CrewService (20 instances)
Same pattern as Step 2 — method-level try/catch wrapping.

### Step 4 — Sweep AuthProfileService, AuthFooterActions, useDashboardAutoConnect, useFavorites
Smaller scope — wrap individual async operations.

### Step 5 — Fix R-06 catch blocks in all remaining files
For every existing catch block missing proper unwrapping:
```typescript
// BEFORE
} catch (e) {
  AppLogger.error('Failed', e);
}

// AFTER
} catch (e: unknown) {
  const msg = e instanceof Error ? e.message : String(e);
  AppLogger.error('Failed', { error: msg });
}
```

### Step 6 — Sweep `err: any` catch declarations
Files using `catch (err: any)` also violate the No-Any rule. Fix:
```typescript
// BEFORE
} catch (err: any) {
  AppLogger.log('ERROR_CAUGHT', { error: err.message });
// AFTER
} catch (err: unknown) {
  const msg = err instanceof Error ? err.message : String(err);
  AppLogger.log('ERROR_CAUGHT', { error: msg });
```

---

## Batching Strategy
Due to size (~120 R-11 + ~72 R-06 = 192 total changes across ~30 files), execute as sequential file-by-file sweep:
1. `CrewProfileService.ts` + `CrewService.ts` (highest risk, must go first)
2. `AuthProfileService.ts` + Auth UI files
3. `DeviceRepository.ts` + Service layer sweep
4. Hooks layer sweep

Each sub-batch gets its own worktree: `error-handling-sweep-phase-N`

---

## Verification
- `npm run verify` (TSC — all `catch (e)` must become `catch (e: unknown)`)
- `grep -rn "catch (e)" src/ | grep -v "e instanceof\|String(e)"` → should return 0
- `grep -rn ": any\b" src/services/Crew` → should return 0

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Services]`
- `[Risk: M-RISK]`
- `[Size: Banquet]`
- `[Cognitive Load: Low]`
- `[BATCH: error-handling-sweep]`

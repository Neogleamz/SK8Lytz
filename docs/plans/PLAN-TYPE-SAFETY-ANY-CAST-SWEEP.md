# Implementation Plan
# PLAN-TYPE-SAFETY-ANY-CAST-SWEEP

## Summary
294 `any` cast violations found across the codebase, concentrated in admin tools (`ProductManager.tsx`, `AdminToolsModal.tsx`), modal components (`AccountModal.tsx`, `CommunityModal.tsx`), and RPC call patterns. The critical pattern is `supabase as unknown as { rpc: ... }` — a chain cast that entirely disables type checking on database calls. This must be the first category fixed to unlock strict TSC.

**Batch:** `BATCH:type-safety-sweep`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/components/admin/tools/ProductManager.tsx` — Lines 15–85 (R-08-013 to R-08-018): 6 `any` violations
- `src/components/admin/AdminToolsModal.tsx` — Lines 46–129 (R-08-006 to R-08-008): 3 violations
- `src/components/AccountModal.tsx` — Lines 310–544 (R-08-001 to R-08-005): 5 violations
- `src/components/CommunityModal.tsx` — Lines 22–278 (R-08-022 to R-08-025): 4 violations
- `src/components/admin/tools/GlobalAnalyticsPanel.tsx` — Line 21 (R-08-010 to R-08-012): RPC chain cast
- Raw audit: `R-08_findings.json` (294 total, 25 shown in findings file)

---

## Findings

### Priority 1 — Supabase RPC Chain Casts (Structural Risk)

**Pattern found in AccountModal.tsx:410 and GlobalAnalyticsPanel.tsx:21:**
```typescript
// BROKEN — bypasses all type safety
const { error } = await (supabase as unknown as { rpc: (fn: string) => Promise<{ error: any }> }).rpc('delete_account');
```

**Fix:** Use Supabase typed RPC:
```typescript
// src/types/supabase.ts already has typed RPCs via /db-sync
const { error } = await supabase.rpc('delete_account');
```
If `delete_account` is not in the generated types, run `/db-sync` first, then use the generated type.

### Priority 2 — ProductManager interface (most `any` per file)

**File:** `src/components/admin/tools/ProductManager.tsx`
```typescript
// BEFORE — 6 any violations
interface ProductEditorState {
  allProfiles: any[];
  editingProfile: any;
  startEditing: (p: any) => void;
  patchEdit: (d: any) => void;
}

// AFTER — define proper Product type
interface Product { id: string; name: string; price: number; /* etc */ }
interface ProductEditorState {
  allProfiles: Product[];
  editingProfile: Product | null;
  startEditing: (p: Product) => void;
  patchEdit: (patch: Partial<Product>) => void;
}
```

### Priority 3 — `createStyles(Colors: any)` pattern
Found in AccountModal.tsx:544, CommunityModal.tsx:278. Fix by importing the theme type:
```typescript
import type { ThemeColors } from '../../theme/theme';
const createStyles = (Colors: ThemeColors) => StyleSheet.create({ ... });
```

### Priority 4 — `as unknown as ViewStyle` workaround
Found in `Sk8LytzProgrammer.tsx:489`:
```typescript
// BEFORE
? { boxShadow: '0px 4px 8px rgba(0,0,0,0.3)' } as unknown as import('react-native').ViewStyle

// AFTER — Use Platform.select or a proper typed object
const shadowStyle: ViewStyle = Platform.OS === 'web' 
  ? ({ boxShadow: '0px 4px 8px rgba(0,0,0,0.3)' } as ViewStyle)
  : { elevation: 4 };
```

---

## Implementation Steps

### Phase 1 — Supabase RPC chain casts (2 files, highest structural risk)
1. Run `/db-sync` to get fresh Supabase TypeScript types
2. Replace chain casts in `AccountModal.tsx:410` and `GlobalAnalyticsPanel.tsx:21` with typed `.rpc()` calls
3. `npm run verify` — confirm TSC passes on these files

### Phase 2 — ProductManager interface (6 violations, self-contained)
1. Read `src/components/admin/tools/ProductManager.tsx`
2. Define `Product` interface from Supabase types (`Tables<'products'>` or equivalent)
3. Replace all 6 `any` with typed interface

### Phase 3 — Sweep `createStyles(Colors: any)` pattern
1. `grep -rn "createStyles = (Colors: any)" src/`
2. Import `ThemeColors` type and replace
3. Estimated: 5-10 files

### Phase 4 — AdminToolsModal + CommunityModal prop types
Replace `hwSettings?: any` with `HardwareSettings | undefined`, `allDevices?: any[]` with `Device[]`, `onApplyScene: (payload: any)` with typed scene payload.

### Phase 5 — Full remaining sweep (defer to subsequent task)
294 total - ~30 in phases 1-4 = ~264 remaining across the codebase. File a separate `[BATCH: type-safety-sweep-phase-2]` task after Phase 1-4 are merged and TSC is cleaner.

---

## Verification
- `npm run verify` — TSC error count must decrease after each phase
- `grep -rn "as any\|: any\b" src/components/admin/tools/ProductManager.tsx` → 0
- `grep -rn "as unknown as" src/components/AccountModal.tsx` → 0

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: UI]`
- `[Risk: M-RISK]`
- `[Size: Banquet]`
- `[Cognitive Load: Medium]`
- `[BATCH: type-safety-sweep]`

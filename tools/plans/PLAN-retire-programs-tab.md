# PLAN: Retire Programs Tab & 0x42 RBM Architecture

**Slug**: `refactor/retire-programs-tab`
**Created**: 2026-04-22
**Status**: 🔲 BLOCKED — cannot execute until replacement is live
**Risk**: M-RISK (removes active user-facing feature)
**Hard Gate**: `feat/pattern-engine-expansion-group7/8/9` ✅ + `feat/unified-pattern-picker-ui` ✅ MUST be shipped first
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Why

**Architecture decision 2026-04-22**: The Programs tab exposes 100 baked-in hardware RBM effects via `0x42`. The hardware runs the animation internally — the ProductVisualizer cannot show what the hardware is doing. This breaks parity.

Our Unified Pattern Picker (PatternEngine, 45+ patterns) replaces Programs entirely with full visualizer parity. Once the replacement ships, Programs is dead weight.

---

## The Gate

```
Phase 1 (Groups 7/8/9 — 45 total patterns) ✅
       +
Unified Pattern Picker with 3 tabs ✅
       ↓
Programs tab retirement UNLOCKS
```

Do not start this task until both gates are green. Users must have access to the replacement before we remove the old system.

---

## What To Remove

### Step 1: Audit What Exists (grep first)
```powershell
Get-ChildItem -Path src -Recurse -Include *.ts,*.tsx |
  Select-String "programs|Programs|PROGRAMS|setCustomRbm|RbmPattern|0x42" |
  Select-Object Filename, LineNumber, Line
```

### Step 2: Remove Programs UI
- `src/components/ProgramsPanel.tsx` or equivalent — DELETE
- Any Programs sub-components — DELETE
- Remove Programs tab entry from `DockedController.tsx` or tab bar

### Step 3: Remove Programs Hook/Logic
- `src/hooks/usePrograms.ts` or `useProgramsMode.ts` — DELETE
- Remove `'PROGRAMS'` from `activeMode` FSM string union in `useControllerDispatch.ts`
- Remove all `case 'PROGRAMS':` branches from mode switch logic

### Step 4: Remove RBM Data & Protocol
- `src/constants/RbmPatterns.ts` — DELETE (100 program label strings, not needed)
- `src/protocols/ZenggeProtocol.ts` — add `@DEPRECATED` JSDoc to `setCustomRbm()`, do NOT delete method (Protocol Bible completeness)
- Note: `RbmSimulator.ts` and `RbmDictionary.ts` are already deleted in `refactor/retire-rbm-simulator`

### Step 5: Update Navigation & Tab Bar
- Remove Programs icon/label from bottom navigation
- If Programs was a named route, remove from router config
- Verify no dead links remain in navigation

### Step 6: Update Master Reference & Test Plan
- `tools/SK8Lytz_App_Master_Reference.md` — remove Programs tab from features section
- `tools/SK8Lytz_TEST_PLAN.md` — remove any Programs tab test cases (replaced by PatternEngine test cases)

---

## Migration Note for Users

When this ships, users who had Programs patterns saved as favorites will see them broken. Migration path:
1. On app launch AFTER this update, detect any favorites with `activeMode: 'PROGRAMS'`
2. Auto-convert them to the closest PatternEngine equivalent (e.g., first pattern in the library)
3. Show a one-time banner: "Programs tab has been retired. Your patterns are now in the new Patterns library with full visualizer sync."

---

## Files To Touch

| File | Action |
|------|--------|
| `src/components/ProgramsPanel.tsx` | DELETE |
| `src/hooks/usePrograms.ts` | DELETE |
| `src/constants/RbmPatterns.ts` | DELETE |
| `src/hooks/useControllerDispatch.ts` | Remove PROGRAMS from FSM, remove case branch |
| `src/components/DockedController.tsx` | Remove Programs tab entry |
| `src/protocols/ZenggeProtocol.ts` | @DEPRECATED JSDoc on setCustomRbm() |
| `src/services/FavoritesService.ts` | Add PROGRAMS→PATTERN migration on load |
| `tools/SK8Lytz_App_Master_Reference.md` | Remove Programs from features |
| `tools/SK8Lytz_TEST_PLAN.md` | Remove Programs test cases |

---

## Test Criteria

- [ ] Programs tab is gone from the bottom nav / tab bar
- [ ] No PROGRAMS references in `useControllerDispatch.ts` FSM
- [ ] `npx tsc --noEmit` — zero errors
- [ ] Favorites with old PROGRAMS state gracefully migrate to PATTERN mode
- [ ] Unified Pattern Picker accessible from where Programs tab was
- [ ] All 45+ patterns accessible and hardware-confirmed on HALOZ

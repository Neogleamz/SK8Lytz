# Implementation Plan: chore/teardown-dead-code-sweep

## Goal
Clear the dead-code / dead-type / stale-doc residue the C2/C3/C4/C14 monolith teardowns left behind, in one surgical pass. Zero behavior change. Runs in **Wave 2** (after the Wave 1 bug fixes merge) so it operates on the final master state.

## Source Analysis
Reyes C2/C3/C4/C14 wiring audits + Q1/Q2 trace verdicts (docs/SESSION_LOG.md, 2026-06-25/26). All items are file:line-confirmed and verified runtime-inert.

## Files to Create/Modify
- `src/protocols/handlers/staticColorHandler.ts`
- `src/protocols/handlers/stateHandler.ts`
- `src/protocols/handlers/legacyHandler.ts`
- `src/components/DockedController.tsx`
- `src/screens/DashboardScreen.tsx`
- `src/screens/Dashboard/DashboardHeader.tsx`
- `src/screens/Dashboard/DashboardCrewHub.tsx`
- `src/services/GroupRepository.ts`
- `src/hooks/useTelemetryLedger.ts`
- `src/styles/DashboardStyles.ts`

> ⚠️ Line numbers below are from the pre-Wave-1 audit. Wave 1 edits will shift them. READ each file fresh and locate the symbol before editing — do NOT trust the cited line blindly.

## Implementation Steps

### C3 handlers — dead imports + No-`any` Law
1. `staticColorHandler.ts:1` — remove unused named imports (`HardwareSettings`, `IC_TYPES`, `COLOR_SORTING_RGB`, `icTypeIndex`, `colorSortingIndex`); keep `ZenggeProtocol`, `HW_CONSTRAINTS`. Verify: grep the file body confirms each removed symbol is unreferenced.
2. `stateHandler.ts:1` — remove the same unused imports; keep only what the body uses. `stateHandler.ts:3` — replace `let _appLogger: any;` with the `AppLoggerLike` interface pattern already used in `staticColorHandler.ts:3-7`. Verify: no `any` remains; `getAppLogger()` still assigns on first call.
3. `legacyHandler.ts:1` — remove all unused imports (functions are pure; even `ZenggeProtocol` is unused per audit — confirm). `legacyHandler.ts:3` — replace `let _appLogger: any;` with `AppLoggerLike`. Verify: file compiles, no `any`.

### C4 — DockedController dead surface
4. `DockedController.tsx` — remove dead imports: `StyleSheet`, `Text` (L22), `LinearGradient` (L49), `Layout/Spacing/Typography` (L50), `LOCAL_PRODUCT_CATALOG` (L59) — each confirmed used only by extracted sub-files. Remove dead `const gaugeSize` (was L184). Remove dead destructures `activeQuickPresetIndex`, `saveQuickPreset` from `useDockedState()` (was L167,179). Verify: grep confirms each is unreferenced post-removal; `git diff` shows only deletions.

### C2 — Dashboard residue
5. `DashboardScreen.tsx` — remove the dead `useProtocolDispatch()` call (was L743, var `dispatch`). **Q1 VERIFIED SAFE: the hook is a pure factory, no side effects.** Remove the `useProtocolDispatch` import if now unused. Verify: grep `dispatch.` returns zero; `git diff` shows only the removed call + import.
6. `DashboardScreen.tsx` — delete the stale TODO (was L23) re `STORAGE_CREW_HUB_COLLAPSED` (already defined + used). Verify: TODO gone.
7. `DashboardStyles.ts` — **Q2 VERIFIED INERT: createDashboardStyles ignoring `_Colors` has no visual effect (all 4 consumers theme via live `Colors` prop).** Remove the `@deprecated createDashboardStyles` shim (was L398-413) and update its sole caller `DashboardScreen.tsx` (was L97) to consume the base `DashboardStyles` / `getDimensionStyles` directly. Verify: `npm run verify` clean; no visual-logic change (styles object shape preserved for the `styles` prop).
8. `Dashboard/DashboardHeader.tsx` — add a top-of-file clarifying comment documenting that this exports `DashboardHeaderBanners` (NOT the main header, which lives at `src/components/dashboard/DashboardHeader.tsx`). **Do NOT rename the file** (avoids import churn during the sweep). Verify: comment present.
9. `Dashboard/DashboardCrewHub.tsx` — remove the dead `export { DashboardCrewPanel }` re-export (was L61) — nothing imports it from this path. Verify: grep confirms no importer of `DashboardCrewPanel` from `./Dashboard/DashboardCrewHub`.

### C14 — service residue
10. `GroupRepository.ts` — remove the dead `getGroupCount()` method (was L133, zero callers). Verify: grep `getGroupCount` returns zero outside the definition.
11. `useTelemetryLedger.ts` — add a guard comment above `injectStreetSummary` (was L197) noting it writes `user_lifetime_stats` (a DIFFERENT table from `user_profiles`, owned by `SpeedTrackingService`) and is currently dormant (0 callers); wiring a caller alongside `SpeedTrackingService.updateLifetimeStats` would create cross-table semantic drift. Do NOT remove the method (it has a designed purpose) — just document the boundary. Verify: comment present.

### Final
12. After all edits: `git diff HEAD` review — every change is a deletion or a comment; zero behavior change. `npm run verify` passes clean (TSC + Jest + no-any guard MUST pass given the two `any` removals).

## Out of Scope
- Renaming `Dashboard/DashboardHeader.tsx` (comment only this pass).
- The 4 Wave 1 bug fixes (already merged before this runs).
- `refactor/break-circular-deps` files unless they overlap (they don't — different files).
- Any functional/logic change. Deletions + comments ONLY.

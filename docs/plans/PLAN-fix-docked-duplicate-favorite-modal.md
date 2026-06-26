# Implementation Plan: fix/docked-duplicate-favorite-modal

## Goal
Eliminate the duplicate `FavoritePromptModal` so exactly one modal owns favorite-naming, with zero data loss for BUILDER-mode saves.

## Source Analysis
Reyes C4 audit + Q3 evidence trace (docs/SESSION_LOG.md, 2026-06-25/26).
- Two modals both render on `visible={promptState === 'NAMING_FAVORITE'}`: `DockedController.tsx:1214-1224` and `BuilderPanel.tsx:219-229`.
- `promptState`/`promptName`/`openFavoritePrompt`/`saveFavorite`/`closePrompt` are SINGLE-instance state from `useFavorites` via `FavoritesContext` — one state object, two consumers. When `activeMode === 'BUILDER'`, both hearts are tappable and both modals mount on the shared bit.
- **DockedController's `handleConfirmSaveFavorite` (L513-539) is a strict SUPERSET** of BuilderPanel's `handleConfirmFavorite` (L124-136): it already captures `builderNodes/builderFillMode/builderTransitionType/builderDirection` (L534-535) plus the full controller snapshot. BuilderPanel's handler captures nothing DockedController's doesn't.
- DockedController's modal also wires `favPromptTargetId` (edit-existing flow); BuilderPanel always passes `favPromptTargetId={null}`.
- Only feature unique to BuilderPanel's modal: `suggestionChips` (cosmetic hint array).
- `saveFavorite` (useFavorites.ts:122) already calls `closePrompt()`; BuilderPanel's explicit `closePrompt()` at L135 is a redundant double-call.

## Files to Create/Modify
- `src/components/docked/BuilderPanel.tsx` — remove the `<FavoritePromptModal>` render (L219-229) and its now-unused local handler `handleConfirmFavorite` (L124-136) if nothing else references it. Keep the heart button + its `openFavoritePrompt(...)` trigger (L119-121) — that correctly drives the shared prompt state.
- `src/components/DockedController.tsx` — (only if `suggestionChips` is worth preserving) pass BuilderPanel's suggestion chips to the surviving modal at L1214-1224. If chips are not readily available at that scope, drop them (cosmetic; document the drop). NO change to `handleConfirmSaveFavorite`.

## Implementation Steps
1. Read `src/components/docked/BuilderPanel.tsx:110-140,215-235` and `src/components/DockedController.tsx:475-540,1210-1230` in full. Confirm the Q3 facts.
2. Confirm BuilderPanel's heart (`handleHeartClick` L119) calls `openFavoritePrompt` on the shared context (it does) — so removing the modal does NOT remove the trigger; the DockedController modal will display.
3. Remove the `<FavoritePromptModal>` JSX block from BuilderPanel (L219-229). `git diff HEAD src/components/docked/BuilderPanel.tsx` — confirm only the modal block removed.
4. Remove BuilderPanel's now-orphaned `handleConfirmFavorite` (L124-136) and its `closePrompt` import if unused. Re-diff.
5. Verify the BUILDER-mode default name: DockedController's `openFavoritePrompt` default name (L510) vs BuilderPanel's (L121, "Custom Gradient"). Since BuilderPanel's heart still calls `openFavoritePrompt` with its own default name, the name is preserved. Confirm no regression.
6. (Optional) If preserving `suggestionChips`: thread them into the DockedController `FavoritePromptModal` at L1214-1224. Else document the cosmetic drop in the final report.
7. `npm run verify` passes clean. Manually reason through: BUILDER mode → tap builder heart → single modal opens with builder default name → save → `handleConfirmSaveFavorite` captures builder fields → `saveFavorite` closes prompt. No second modal.

## Out of Scope
- The `useImperativeHandle` stale-closure fix (separate task fix/docked-stale-imperative-handle — SAME unified worktree, different region ~L448; apply both, diff after each).
- Dead-import/destructure cleanup in DockedController (Wave 2 chore/teardown-dead-code-sweep).
- `useFavorites.ts` / `FavoritesContext.tsx` internals — HARD boundary.

## Coordination note
Runs in the SAME unified worktree as `fix/docked-stale-imperative-handle` (both edit DockedController.tsx). Modal fix touches ~L1214 + BuilderPanel; handle fix touches ~L448. Non-overlapping regions — apply both, `git diff HEAD <file>` after every edit.

# Implementation Plan: fix/docked-stale-imperative-handle

## Goal
Fix the `useImperativeHandle` dependency array in `DockedController` so the externally-exposed `applyCloudScene` and `loadFavorite` no longer capture stale closures.

## Source Analysis
Reyes C4 wiring audit (docs/SESSION_LOG.md, 2026-06-25).
- Source: `src/components/DockedController.tsx:448-469` — `useImperativeHandle` factory closes over `applyCloudScene` (L449) and `loadFavorite` (L456) but its dep array is `[speed, brightness, writeToDevice, optimisticWrite]`.
- `applyCloudScene` is a `useCallback` with deps `[baseApplyCloudScene, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor]`.
- `loadFavorite` comes from `useLoadFavorite.ts:81` (a `useCallback` with ~20 deps).
- Consequence: parent callers (crew scene-apply, crew loadout sync, voice commands) invoke a stale ref handle. The BLE reconcile path is unaffected (uses `onReconcileRef`, refreshed every render at L395).

## Files to Create/Modify
- `src/components/DockedController.tsx` — add `applyCloudScene` and `loadFavorite` (and `applySpatialSegments` if exposed via the handle) to the `useImperativeHandle` dep array at L448-469.

## Implementation Steps
1. Read `src/components/DockedController.tsx:440-475` in full — confirm exactly which callbacks the handle factory references vs what is in the dep array.
2. Confirm `applyCloudScene` (L449) and `loadFavorite` (L456) are themselves `useCallback`-wrapped (stable-ish) so adding them to deps does not cause an infinite re-create loop. If either is NOT memoized, note it — but do not refactor the source callback (out of scope); just include it in deps (React handles ref re-creation correctly).
3. Add the missing callbacks to the `useImperativeHandle` dependency array. Keep the existing entries.
4. `git diff HEAD src/components/DockedController.tsx` — confirm ONLY the dep array changed; the handle factory body and all other code untouched.
5. Verify: `npm run verify` passes clean.

## Out of Scope
- The duplicate FavoritePromptModal fix (separate task fix/docked-duplicate-favorite-modal — same file, executed in the SAME unified worktree; coordinate edits, do not collide).
- `useLoadFavorite.ts` internals and the `applyCloudScene` source callback's own deps — HARD boundary.
- Any dead-import / dead-destructure cleanup in this file (belongs to chore/teardown-dead-code-sweep, Wave 2).

## Coordination note
This task shares `DockedController.tsx` with `fix/docked-duplicate-favorite-modal` and runs in the SAME worktree. Apply both fixes; they touch different regions (modal ~L1214, handle ~L448). Run `git diff` after each edit.

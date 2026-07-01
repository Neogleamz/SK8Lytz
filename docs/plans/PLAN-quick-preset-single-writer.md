# Implementation Plan — chore/quick-preset-dead-writer-cleanup

> **Slug:** `chore/quick-preset-dead-writer-cleanup`
> **Author:** 🎯 Jordan (intake), plan drafted for 📐 Quinn / ⚒️ Sage
> **Date:** 2026-06-30
> **Classification:** `[🕵️ SPIKE]` `[✅ VERIFIED]` `[UI]` `[LOW-RISK]` `[🍪 Snack]` `[LOW]`

## Context (corrected from the intake report)

The intake reported R-21-004 as a *live* dual-writer race between `QuickPresetModal.tsx`
and `useFavorites.saveQuickPreset`, and proposed passing `onPresetsChanged={setQuickPresets}`
in `DockedController.tsx`.

Both claims were falsified by evidence (P1):

1. **The proposed fix is a data-loss regression.**
   `QuickPresetModal.persistPresets` (`src/components/docked/QuickPresetModal.tsx:L76-89`)
   already calls `setQuickPresets(presets)` unconditionally at **L77**, then branches:
   - if `onPresetsChanged` is provided → delegate to it (L78-79), NO AsyncStorage write
   - else → `AsyncStorage.setItem(STORAGE_QUICK_PRESETS, ...)` (L81)

   Passing `setQuickPresets` as `onPresetsChanged` means a save calls `setQuickPresets`
   twice (L77 + L79) and writes storage **zero times**. DockedController quick presets
   would live only in memory and be lost on app restart.

2. **The race is not live — the second writer is dead code.**
   `grep saveQuickPreset src/` returns only its definition (`useFavorites.ts:L133`) and its
   place in the return object (`:L165`). **Zero call sites.** The only live writer to
   `@Sk8lytz_QuickPresets` is `QuickPresetModal.tsx:L81`.

## Cited Truth

- `src/components/docked/QuickPresetModal.tsx:L48-49` — `onPresetsChanged?` typed as delegated persist callback.
- `src/components/docked/QuickPresetModal.tsx:L76-89` — `persistPresets`: `setQuickPresets` at L77, then delegate-or-write branch.
- `src/components/DockedController.tsx:L1195-1212` — `<QuickPresetModal>` call site; passes `setQuickPresets` (L1202), NOT `onPresetsChanged`.
- `src/hooks/useFavorites.ts:L133-144` — `saveQuickPreset`, writes `STORAGE_QUICK_PRESETS` at L138. **Dead — no callers.**
- `src/hooks/useFavorites.ts:L165` — `saveQuickPreset` exported in return object.
- `src/constants/storageKeys.ts:L24` — `STORAGE_QUICK_PRESETS = '@Sk8lytz_QuickPresets'`.

## The honest fix

Delete the dead writer so no latent second writer to the key can ever exist. Leave
`QuickPresetModal.tsx:L81` as the single, documented writer. Do NOT wire `onPresetsChanged`.

## Files to Create/Modify

| File | Change |
| ---- | ------ |
| `src/hooks/useFavorites.ts` | Delete `saveQuickPreset` (L133-144). Remove `saveQuickPreset` from the return object (L165). Remove `IQuickPreset` from the type import **only if** no other usage remains after deletion (Boy Scout — verify by grep, do not assume). |

## Steps

### Step 0 — Confirm zero callers (SPIKE gate — HARD STOP)
- Run: `Grep "saveQuickPreset" src/` (or ripgrep).
- **Expected:** matches ONLY at `useFavorites.ts:L133` and `:L165`.
- If ANY other match appears → **HALT**. The dead-code premise is wrong; re-open as a real
  single-writer wiring task and revise this plan. Do not delete.

### Step 1 — Delete the dead writer
- Read `src/hooks/useFavorites.ts:L133-144` immediately before editing.
- Delete the `saveQuickPreset` `useCallback` block (L133-144).
- **Verify:** `git diff HEAD src/hooks/useFavorites.ts` shows only that block removed.

### Step 2 — Remove the dead export
- Read the return object around `:L165`.
- Delete the `saveQuickPreset,` line.
- **Verify:** `git diff HEAD src/hooks/useFavorites.ts` shows only that line removed.

### Step 3 — Boy Scout unused import (conditional)
- Grep `IQuickPreset` within `useFavorites.ts`. If still used (it is — L20 `useState<IQuickPreset[]>`),
  leave the import. Do NOT remove imports that remain in use.

### Verify
- `npm run verify` clean (TSC will fail loudly if any missed `saveQuickPreset` reference exists —
  this is the safety net for Step 0).
- Manual: open DockedController quick-preset flow → save a preset → kill and relaunch the app →
  confirm the preset persists (proves `QuickPresetModal.tsx:L81` remains the working single writer).

## Out of Scope (HARD BOUNDARY)
- `src/components/DockedController.tsx` — do NOT open. Do NOT pass `onPresetsChanged`.
- `src/components/docked/QuickPresetModal.tsx` — do NOT modify `persistPresets` or the
  `onPresetsChanged` prop signature. It is intentional future-proofing for a builder flow that
  may need delegated persistence later.
- Adding any new bulk storage-writer to `useFavorites`.
- `FavoritesService`, DB schema, RLS.

# Implementation Plan: sweep-src-components-ProductVisualizer.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-ProductVisualizer.tsx` domain cluster.

## Batch & Wave
- **Wave:** 2
- **Prerequisite:** Wave 1 fully merged

## Proposed Changes
### [MODIFY] ProductVisualizer.tsx
- Line 153: Fix `R-21` violation. Dead/unused stylesheet classes (haloBase, soulBase, railBase) duplicated from VisualizerUnit.tsx. (Suggested: Remove the unused stylesheet properties to clean up code bloat.)
- Line 103: Fix `R-07` violation. Inline static style values (backgroundColor and borderColor) redundant with styles.container styles. (Suggested: Remove the inline object and rely solely on styles.container which already specifies these exact colors.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

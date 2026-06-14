# Implementation Plan: sweep-src-components-VerticalPatternDrum.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-VerticalPatternDrum.tsx` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] VerticalPatternDrum.tsx
- Line 121: Fix `R-25` violation. Custom drum pattern wheel picker has no accessibility roles or instructions for screen reader users. (Suggested: Add accessible={true}, accessibilityRole="adjustable", and custom descriptions to represent the picker scroll/wheel.)
- Line 146: Fix `R-28` violation. Inline anonymous function passed to FlatList's getItemLayout causes reference re-allocation on every render. (Suggested: Extract the getItemLayout logic to a class static function or a memoized callback using useCallback.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

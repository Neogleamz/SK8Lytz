# Implementation Plan: sweep-src-components-LocationPicker.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-LocationPicker.tsx` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] LocationPicker.tsx
- Line 124: Fix `R-20` violation. Call to Location.getLastKnownPositionAsync is unguarded for Web platform availability. Location.getLastKnownPositionAsync is not supported on all platforms (e.g. Web) and will crash if called in a Web browser environment. (Suggested: Add a Platform guard before calling Location.getLastKnownPositionAsync(): if (Platform.OS !== 'web') { ... }.)
- Line 178: Fix `R-20` violation. There is a complete lack of localization framework/support (i18n.t()) across components, with raw hardcoded UI label strings used globally. (Suggested: Introduce an internationalization framework (e.g., react-i18next or similar i18n utility) and wrap all UI text strings in i18n.t() translations.)
- Line 185: Fix `R-25` violation. Smart chips and text inputs inside the LocationPicker component lack accessibility roles and labels, causing screen readers to miss essential interactive feedback. (Suggested: Add accessibilityRole="button" and accessibilityLabel for the history chips, and add accessibilityLabel for the text inputs.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

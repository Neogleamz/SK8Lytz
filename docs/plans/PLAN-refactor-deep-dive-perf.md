# Implementation Plan: refactor/deep-dive-perf

## Goal
Resolve performance leaks caused by inline functions and styles in FlatLists across `UI_CONTROLS` and `GROUP_SYNC`.

## Source of Truth
`src/components/DockedController.tsx`

## Proposed Changes

### UI_CONTROLS
- **[MODIFY]** `src/components/DockedController.tsx`: Move inline styles to `StyleSheet.create`. Extract inline arrow functions from `renderItem` blocks into `useCallback` hooks.
- **[MODIFY]** `src/screens/DashboardScreen.tsx`: Move massive inline styles to `StyleSheet.create`. Extract render items.

### GROUP_SYNC
- **[MODIFY]** `src/components/crew/*`: Identify mapping and FlatList functions. Wrap with `useCallback` or `useMemo`. Move inline styles to `StyleSheet.create`.

## Verification Plan
1. **Automated Tests**: Run `npm run verify` to ensure TypeScript compilation succeeds.
2. **Manual Verification**: Launch app, open Crew and Dashboard screens, and monitor frame rates (FPS) using React Native performance monitor to ensure 60FPS on scroll.

# Implementation Plan: sweep-src-components-CommunityModal.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-CommunityModal.tsx` domain cluster.

## Batch & Wave
- **Wave:** 4
- **Prerequisite:** Wave 3 fully merged

## Proposed Changes
### [MODIFY] CommunityModal.tsx
- Line 372: Fix `R-07, R-28` violation. An inline arrow function rendering a component is passed directly to the `ListEmptyComponent` prop of `FlatList`. This causes the component to unmount and remount on every render cycle instead of updating. (Suggested: Define a memoized empty state renderer using `useCallback`: `const renderEmpty = useCallback(() => <EmptyState message={activeTab === 'COMMUNITY' ? '...' : '...'} />, [activeTab]);`)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

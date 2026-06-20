# Implementation Plan: C15 — FlatList Performance

## Goal
Extract inline FlatList header/footer/empty components to stable references.

## Rules Addressed
- R-28: FlatList Bottlenecks (3 findings)

## Files to Create/Modify
- `src/components/admin/AdminToolsModal.tsx`
- `src/components/VerticalPatternDrum.tsx`

## Implementation Steps
1. View each file. Find inline arrow functions in renderItem, ListHeaderComponent, etc.
2. Extract to useCallback/useMemo wrapped references.
3. Verify: git diff shows only extraction.
4. Run npm run verify.

## Out of Scope
- All other FlatList usages

# Implementation Plan

## PLAN-flatlist-render-sweep — FlatList Inline Props Performance
*Source: `/deepdive-code-hunt` fleet | Rules: R-07 | Date: 2026-06-10*

### Problem
17 FlatList/SectionList `renderItem` and `keyExtractor` callbacks are defined as inline arrow functions in JSX props. React re-creates these functions on every parent render, defeating the FlatList's `PureComponent` optimization and causing full list re-renders for unrelated state changes.

### Fix Pattern
```typescript
// Before (causes FlatList re-render on every parent render)
<FlatList renderItem={({ item }) => <ItemCard item={item} />} keyExtractor={item => item.id} />

// After (stable references, list only re-renders when items change)
const renderItem = useCallback(({ item }: ListRenderItemInfo<MyType>) => (
  <ItemCard item={item} />
), [/* deps */]);
const keyExtractor = useCallback((item: MyType) => item.id, []);
<FlatList renderItem={renderItem} keyExtractor={keyExtractor} />
```

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-05

### Affected Files
| File | Line | Violation | Severity |
|---|---|---|---|
| `src/components/docked/FavoritesPanel.tsx` | 79-80, 89-94 | Two inline keyExtractors (CONFIRMED) | MEDIUM |
| `src/components/VerticalPatternDrum.tsx` | 113-116 | Inline fn in drum list (CONFIRMED) | MEDIUM |
| `src/components/patterns/GradientLibraryTab.tsx` | 66, 114-115, 127 | Inline renderItem + keyExtractor (CONFIRMED) | MEDIUM |
| `src/components/patterns/PatternPickerTab.tsx` | 72-73, 115, 134, 155-157 | Multiple inline fns (CONFIRMED) | MEDIUM |
| `src/components/CommunityModal.tsx` | 272-276 | Inline keyExtractor (CONFIRMED) | MEDIUM |
| `src/components/admin/tools/tabs/DiagnosticLabSnifferTab.tsx` | 30 | Inline renderItem | HIGH |
| `src/components/admin/tools/AdminAuditLogViewer.tsx` | 116 | renderItem not useCallback | HIGH |
| `src/components/admin/tools/AdminRosterPanel.tsx` | 102 | renderItem not useCallback | HIGH |
| `src/components/admin/tools/FeatureFlagsPanel.tsx` | 148 | renderItem not useCallback | HIGH |
| `src/components/admin/tools/HardwareBlacklistPanel.tsx` | 138 | renderItem not useCallback | HIGH |
| `src/components/admin/tools/UserManagementPanel.tsx` | 244 | renderItem not useCallback | HIGH |

### Implementation Steps
1. Fix all CONFIRMED violations first (highest confidence)
2. Extract each inline `renderItem` into a `useCallback` at the top of the component body
3. Extract each inline `keyExtractor` into a `useCallback` (or a stable module-level function if deps are empty)
4. Add proper TypeScript types to all `renderItem` and `keyExtractor` signatures
5. Verify dependency arrays are complete (no stale closures introduced)

### Verify
- `npm run verify`
- React DevTools Profiler: confirm FlatList re-render count drops on parent state change

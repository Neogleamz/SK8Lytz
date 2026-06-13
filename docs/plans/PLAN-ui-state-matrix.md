# Implementation Plan

## Task: fix/ui-state-matrix
**Cluster:** J — 4-State UI Matrix Gaps  
**Batch:** `[BATCH:state-matrix-sweep]`  
**Rule Violated:** R-14  
**Severity:** MEDIUM × 8, LOW × 1  
**Risk:** M-RISK  
**Size:** Feast (9 screens, each needing Error + Empty state implementation)

## Cited Truth
- Audit R14: 9 files confirmed missing Error or Empty state rendering
- Source: `artifacts/system_audit_report.md` Cluster J lines 333–357

| File | Missing State |
|---|---|
| `src/components/admin/tools/AdminAuditLogViewer.tsx` | Error card + `ListEmptyComponent` |
| `src/components/admin/tools/AdminPicksScheduler.tsx` | Error card + CTA empty state |
| `src/components/admin/tools/GlobalAnalyticsPanel.tsx` | Error/retry panel |
| `src/components/admin/tools/UserManagementPanel.tsx` | Error card + empty search results |
| `src/components/CommunityModal.tsx` | Error state (silently falls back to empty) |
| `src/components/AccountModal.tsx` | Error fallback after skeleton |
| `src/screens/CrewJoinScreen.tsx` (approx path) | Empty search + network error |
| `src/screens/CrewLandingScreen.tsx` (approx path) | Persistent error card |
| `src/components/SkateSpotBottomSheet.tsx` (approx path) | Failure toast + keep modal open |

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster J lines 333–357
- Rule: `.agents/rules/agent-behavior.md` Rule 4 (UI Safety — 4-state matrix)
- Each file must be `view_file`'d to confirm exact data fetch pattern before editing

## 4-State Pattern (Apply to All 9 Files)

### Required State Variables
```ts
const [data, setData] = useState<T[]>([]);
const [loading, setLoading] = useState(true);
const [error, setError] = useState<string | null>(null);
// data.length === 0 && !loading && !error → empty state
```

### Required Rendering
```tsx
if (loading) return <ActivityIndicator />;
if (error) return (
  <View style={styles.errorCard}>
    <Text style={styles.errorText}>{error}</Text>
    <TouchableOpacity onPress={retry}><Text>Retry</Text></TouchableOpacity>
  </View>
);
if (data.length === 0) return (
  <View style={styles.emptyState}>
    <Text>No items found.</Text>
  </View>
);
// Success state: render list
```

## Implementation Steps

### Step 1 — Confirm file paths (P1)
`grep -rn "AdminAuditLogViewer\|AdminPicksScheduler\|GlobalAnalyticsPanel\|UserManagementPanel\|CommunityModal\|AccountModal\|CrewJoinScreen\|CrewLandingScreen\|SkateSpotBottomSheet" src/ --include="*.tsx" -l`

### Step 2 — Read each file
For each file: `view_file` to understand current data fetch pattern and existing state variables.

### Step 3 — Add Error State (per file)
Each file needs:
1. `const [error, setError] = useState<string | null>(null);`
2. In the `catch` block of the data fetch: `setError('Failed to load. Tap to retry.')`
3. Render: `{error && <ErrorCard message={error} onRetry={refetch} />}`

### Step 4 — Add Empty State (per file where applicable)
Where `FlatList` is used: add `ListEmptyComponent`:
```tsx
ListEmptyComponent={<EmptyState message="No items found" />}
```
Where data renders directly (not FlatList): add conditional render.

### Step 5 — Create shared components (if not already present)
Check `src/components/ErrorCard.tsx` and `src/components/EmptyState.tsx` exist. If not, create them as simple, styled, reusable components.

## Batch Strategy
**This Feast task should be split into 2 worktrees at execution time:**
- Worktree 1: Admin tools (4 files: AuditLogViewer, PicksScheduler, AnalyticsPanel, UserManagementPanel)
- Worktree 2: Modals + screens (5 files: CommunityModal, AccountModal, CrewJoinScreen, CrewLandingScreen, SkateSpotBottomSheet)
- Merge worktree 1 before creating worktree 2 (VS-001 parallel worktree safety)

## Verification Plan
- TSC no-emit passes for each worktree
- Manual: For each screen, simulate error state (airplane mode / bad network) → confirm error card with retry button appears
- Manual: For each list screen, clear all data → confirm empty state renders (not blank)
- `npm run verify` after each worktree merge

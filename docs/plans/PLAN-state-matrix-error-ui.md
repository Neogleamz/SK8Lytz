# Implementation Plan — `refactor/state-matrix-error-ui`

## Goal
Add explicit Error UI state to ~18 data-driven components that silently render stale/empty data on fetch failure, completing the 4-state matrix (Loading, Error, Empty, Success) per the Offline-First mandate.

## Source of Truth
- `artifacts/deepdive_raw/R-14_findings.json`
- `artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json` line 116 (SkaterStatsPanel)
- `artifacts/system_audit_report.md` §R-14

## Out of Scope
- Changing fetch logic or data sources
- Redesigning loading states that already exist
- Any component without data fetching (utility components)

## Fix Pattern
```typescript
// ADD error state
const [error, setError] = useState<string | null>(null);

// IN catch block
setError(e instanceof Error ? e.message : 'Failed to load');

// IN render
if (error) return <ErrorState message={error} onRetry={refetch} />;
```

---

## Step 1 — Fix `src/components/account/SkaterStatsPanel.tsx`
- **Finding:** `fetchStats` error is swallowed — component renders zeros on failure
- **Action:** Add `error` state, set in catch, render `<Text>Failed to load stats. Tap to retry.</Text>` with an `onPress={fetchStats}` retry
- **Verify:** Error state renders correctly in isolation (can test by temporarily making Supabase call fail).

## Step 2 — Fix `src/hooks/useScenes.ts`
- **Finding:** No error state for scene fetch failures — UI shows empty list with no feedback
- **Action:** Add `error: string | null` to hook return, set in catch block, consumers check `error` before rendering list
- **Verify:** Consumers updated. Zero silent failures.

## Step 3 — Fix `src/hooks/useGradients.ts`
- **Finding:** `isLoading` boolean flag but no error state
- **Action:** Add error state alongside existing loading state
- **Verify:** Hook returns `{ presets, isLoading, error }`.

## Step 4 — Scan R-14 for remaining instances
- **Action:** Read full `R-14_findings.json` for additional components not covered above
- **Verify:** All R-14 HIGH/MEDIUM instances addressed.

## Step 5 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors.

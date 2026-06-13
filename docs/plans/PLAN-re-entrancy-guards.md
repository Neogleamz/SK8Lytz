# Implementation Plan — `refactor/re-entrancy-guards`

## Goal
Add `isMountedRef` / `isRunningRef` boolean guards to 5 confirmed async functions called from `useEffect` or event listeners without re-entrancy protection, preventing duplicate async calls and state updates after unmount.

## Source of Truth
- `artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json` lines 192–211
- `artifacts/deepdive_raw/R-26_findings.json`
- `artifacts/system_audit_report.md` §R-26

## Fix Pattern
```typescript
// BEFORE (vulnerable)
useEffect(() => {
  loadData(); // can fire multiple times, no cleanup
}, [dep]);

// AFTER (guarded)
useEffect(() => {
  let isActive = true;
  const run = async () => {
    if (!isActive) return;
    await loadData();
  };
  run();
  return () => { isActive = false; };
}, [dep]);
```

## Out of Scope
- Refactoring loadData internals
- Changing dependency arrays (except to fix stale closures where required)

---

## Step 1 — Fix `src/hooks/useRegistration.ts:81`
- **Action:** View file, wrap `boot()` call in useEffect with `isActive` guard and cleanup return
- **Source:** `DOMAIN_IDENTITY_findings.json` line 195
- **Verify:** useEffect has a return function that sets `isActive = false`. No state update without `if (isActive)` guard.

## Step 2 — Fix `src/components/account/SkaterStatsPanel.tsx:24`
- **Action:** View file, wrap `fetchStats()` in useEffect with `isActive` guard
- **Source:** `DOMAIN_IDENTITY_findings.json` line 202
- **Verify:** useEffect cleanup cancels pending fetch. Verified `setData` is not called after unmount.

## Step 3 — Fix `src/context/AuthContext.tsx:102`
- **Action:** View file. The `handleDeepLink` function is called from an event listener. Add an `isHandling` ref to prevent duplicate concurrent executions.
- **Source:** `DOMAIN_IDENTITY_findings.json` line 209
- **Pattern:** `const isHandlingRef = useRef(false);` — check and set at function entry, clear in finally block.
- **Verify:** Concurrent deep link events are safely queued/dropped. No state corruption on rapid deep links.

## Step 4 — Check R-26 sniper findings for additional instances
- **Action:** Read `R-26_findings.json` for any instances beyond the 3 confirmed in DOMAIN_IDENTITY
- **Verify:** All additional instances addressed or triaged.

## Step 5 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors. No unused variable warnings from `isActive`.

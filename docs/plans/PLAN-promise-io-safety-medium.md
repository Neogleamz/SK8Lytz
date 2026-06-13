# Implementation Plan

## Task: fix/promise-io-safety-medium
**Cluster:** C — Promise & IO Safety (MEDIUM severity subset)  
**Batch:** `[BATCH:promise-io-safety-sweep]`  
**Rule Violated:** R-11  
**Severity:** MEDIUM × 7  
**Risk:** L-RISK  
**Size:** Meal (7 fire-and-forget AsyncStorage write fixes)

## Cited Truth
- Audit report: `artifacts/system_audit_report.md` Cluster C lines 194 (MEDIUM description)
- Affected files: ThemeContext, QuickPresetModal, AuthScreen, and other async write call sites
- R-11 Raw: `artifacts/deepdive_raw/R-11_findings.json`

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster C, MEDIUM findings
- Rule: `.agents/rules/prime-directive.md` Process Gates §1 (SoT PRIME)

## Problem
7 `AsyncStorage.setItem()` / `removeItem()` calls are fire-and-forget (no `.catch()`). On device storage write failures (disk full, keychain errors on iOS), the app silently drops theme, preset, or auth state — user loses preference data with no feedback.

## Pattern (Apply to All 7 Instances)

**Before:**
```ts
AsyncStorage.setItem('key', value);
```

**After:**
```ts
AsyncStorage.setItem('key', value).catch((err: unknown) => {
  AppLogger.warn('[ContextName] Failed to persist key', err instanceof Error ? err.message : String(err));
});
```

## Implementation Steps

### Step 1 — Discovery
Run `grep -rn "AsyncStorage\.setItem\|AsyncStorage\.removeItem" src/ --include="*.ts" --include="*.tsx" | grep -v "\.catch\|await\|try"` to enumerate all unguarded fire-and-forget calls.

### Step 2 — Apply Pattern
For each result: `view_file` the line → verify it has no existing `.catch()` or `await` → add `.catch()` inline.

### Step 3 — ThemeContext (confirm)
ThemeContext is the highest-traffic persistence path (theme changes on every theme toggle). Verify the write is guarded.

### Step 4 — AuthScreen & QuickPresetModal
These are user-action driven (explicit button taps). If they fail silently the user doesn't know their action was dropped. Add `.catch()` + optional user-facing toast.

## Verification Plan
- `npm run verify` — no TSC errors
- grep confirms 0 unguarded AsyncStorage writes remain in the 7 targeted files
- `git diff HEAD` — only `.catch()` additions, no logic changes

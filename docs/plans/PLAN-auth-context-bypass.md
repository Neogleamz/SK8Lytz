# Implementation Plan

## Task: fix/auth-context-bypass
## Cluster: TC-03
## Risk: [M-RISK] | Size: [Meal] | Layer: [SERVICE]
## Status: [⚪ READY]

---

## Problem Statement

`CrewProfileService.ts` calls `supabase.auth.getUser()` directly in 8 separate methods as a fallback to resolve the current user. `GroupRepository.ts` and `ScenesService.ts` each call `supabase.auth.getSession()` directly for cloud operations.

This bypasses `AuthContext` (the canonical source of auth state) and introduces:
1. **Silent stale auth**: `getUser()` makes a network call each time — if the token is expired, it silently returns null instead of the cached context state
2. **Untraceable auth flows**: Auth state changes in `AuthContext` are not observed by these services
3. **Architectural inconsistency**: Every other service correctly receives `userId` as a parameter

---

## Source of Truth

- **`artifacts/deepdive_raw/R-15_findings.json`** → R-15-001 through R-15-010
- **`src/services/CrewProfileService.ts`** — lines 32, 76, 128, 162, 196, 266, 309, 330
- **`src/services/GroupRepository.ts:154`** — `const { data: { session } } = await supabase.auth.getSession();`
- **`src/services/ScenesService.ts:360`** — `const { data: session } = await supabase.auth.getSession();`
- **`tools/SK8Lytz_App_Master_Reference.md`** — AuthContext as the canonical auth state source

---

## Proposed Changes

### [MODIFY] `src/services/CrewProfileService.ts` (8 occurrences)
For each method that currently calls `supabase.auth.getUser()`:
1. Remove the internal `getUser()` call
2. Add `userId: string` as a required parameter to the method signature
3. Use the passed `userId` directly

**Callers to update**: Wherever these `CrewProfileService` methods are called (search for each method name) — the caller must pass the `userId` from the `AuthContext` hook.

### [MODIFY] `src/services/GroupRepository.ts:154`
```diff
- const { data: { session } } = await supabase.auth.getSession();
- const userId = session?.user?.id;
+ // Caller must pass userId
```
Change `deleteGroup(groupId: string)` → `deleteGroup(groupId: string, userId: string)`.

### [MODIFY] `src/services/ScenesService.ts:360`
```diff
- const { data: session } = await supabase.auth.getSession();
- const userId = session?.user?.id;
+ // Caller must pass userId
```
Change `flushSyncQueue()` → `flushSyncQueue(userId: string)`.

### [MODIFY] Callers
Search for all call sites of the modified methods and pass `user?.id` from their `AuthContext`-sourced state.

---

## Verification Plan

### Automated
- `npm run verify` — TSC will catch any missing `userId` argument at call sites

### Manual
1. Login → open Crew Profile → verify crew data loads correctly
2. Delete a group → verify Supabase delete executes (check network tab or Supabase logs)
3. Go offline → come back online → verify scene sync queue flushes correctly

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\auth-context-bypass\`
Branch: `fix/auth-context-bypass`

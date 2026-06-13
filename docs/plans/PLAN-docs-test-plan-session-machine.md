# Implementation Plan

## Task: `docs/test-plan-session-machine`
**Goal:** Add a new section to `tools/SK8Lytz_TEST_PLAN.md` documenting the sessionMachine test coverage added in Waves 1 and 2 of the session-xstate-engine batch (28 suites / 218 tests green).

---

## Files to Create/Modify

- **[MODIFY]** `tools/SK8Lytz_TEST_PLAN.md` — append new section: "Session Machine Test Coverage (XState v5)"

---

## Execution Steps

**Step 1 — Read Existing Test Plan Structure**
- `view_file` `tools/SK8Lytz_TEST_PLAN.md` (39KB) — read structure and find correct insertion point
- Source: `tools/SK8Lytz_TEST_PLAN.md`

**Step 2 — Read Existing Session Test Files**
- `list_dir` `src/hooks/ble/__tests__/` and `src/__tests__/` for session-related test files
- `view_file` each session-related test file to document what is actually covered
- Source: Wave 1 + Wave 2 test files (read before documenting)
- Verify source: `jest --listTests | grep session` output equivalent

**Step 3 — Document Coverage**
Add new section to TEST_PLAN.md:
```
## Session Machine Test Coverage (XState v5)
Added: Wave 1 (feat/session-services-layer @ b9c7baa9) + Wave 2 (refactor/session-context-xstate @ 4df46b81)
Suite count: 28 total suites / 218 total tests

### Covered States
- IDLE → WARMING_UP transition
- WARMING_UP → ACTIVE on GPS + timer ready
- ACTIVE → PAUSED on user action / watch command
- PAUSED → ACTIVE on resume
- ACTIVE → COMMITTED on end session
- COMMITTED → IDLE on cleanup

### Covered Edge Cases
- BLE disconnect during ACTIVE state → session NOT ended (product invariant)
- App backgrounded during ACTIVE state → session persists via STORAGE_PENDING_BG_END
- Watch START_SESSION command → fires START event to machine
- Watch STOP_SESSION command → fires END event to machine
- Auto-pause trigger → ACTIVE → PAUSED transition
- Crash recovery on app restart → STORAGE_PENDING_BG_END recovery path

### Not Yet Covered (Known Gaps)
- Network failure during COMMITTED → Supabase sync retry behavior
- Concurrent watch + notification END triggers
```

**Step 4 — Commit**
- `git add tools/SK8Lytz_TEST_PLAN.md`
- `git commit -m "docs(test-plan): add sessionMachine XState v5 test coverage section"`
- Write SESSION_LOG [ARTIFACT] entry

---

## Source of Truth
- `src/hooks/ble/__tests__/` — existing BLE test suite location
- `b9c7baa9` — Wave 1 merge commit (session services layer + tests)
- `4df46b81` — Wave 2 merge commit (SessionContext XState rewrite + tests)

## Out of Scope
- Writing new tests (that's a `/tdd` task)
- Updating coverage for Wave 3 tasks (not yet merged)
- Any changes to `.ts`/`.tsx` source files

---
name: qa-cases
description: "Use when code has just been written or changed and is about to be committed, or when asked to harden, stress-test or find edge cases: five case groups with a pack for the repo's stack and a PASS or NEEDS FIX verdict."
---
<!-- generated from HQ playbook qa-cases v1, do not edit. Run `npm run gen:playbooks`. -->

# QA edge cases: five case groups before a commit

**Role:** reviewer (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use when code has just been written or changed and is about to be committed, or when asked to harden, stress-test or find edge cases: five case groups with a pack for the repo's stack and a PASS or NEEDS FIX verdict.

## Steps
1. Known issues first: read the 'Known issues that touch these files' section of the brief and search_brain for earlier failures in this area. Any relevant one becomes case 1 and is tested explicitly.
   - Verify: A cross-reference table: known issue, relevant or not, action.
2. Read the diff (git diff HEAD) and name the domains it touches. If it spans several unrelated domains, give each domain to its own read-only reviewer and merge the answers.
   - Gate: Read only: this playbook finds gaps, it does not fix them.
   - Verify: The files and domains under review are listed.
3. For each of the five case groups below, state what could go wrong, how the code handles it (cite file:line), and, if it does not, mark the gap before the commit. Use the stack pack for this repo (stack: react-native, ble, supabase).
   - Verify: All five groups have an answer with a file:line, or a flagged gap.
4. Case group 1, loss and failure: the dependency, service or connection drops halfway through the operation. Are locks, timers and subscriptions cleaned up? Does it recover or fail silently?
5. Case group 2, lifecycle and resume: the tab or app is backgrounded, reloaded or the session expires between two steps. Is the state still right when it comes back?
6. Case group 3, null and empty states: loading, empty and missing optional data. Can null or undefined reach a crash (a map on null, a missing optional chain)?
7. Case group 4, repeats and concurrency: a double click, two writers, a retry, a burst. Is there a guard (a mutex, a disabled button, an idempotent write) and does the order hold?
8. Case group 5, the task's own edge: choose one that is specific to this change (a boundary value, a tiny screen, a zero or a thousand items, an offline backend) and test it.
   - Verify: The chosen case is named and tested.
9. Give the verdict: PASS when all five hold, NEEDS FIX when any has a gap (with file:line). Fix the gaps before you commit. If a failure was a new pattern, write it as a known issue (write_kb kind known-issue).
   - Gate: Any gap blocks the commit.

## Stack pack: ble
- A connection drop in the middle of a write or read: locks, timers and subscriptions are released and recovery runs (or the failure is shown).
- Concurrent writes: a GATT write mutex, a write queue that keeps order under a burst, a debounce on double taps.
- Hardware boundaries: MTU overflow, an opcode timeout, zero devices and five or more devices.

## Stack pack: react-native
- App backgrounded and resumed: cleanup in the effect return and on AppState change; iOS background rules respected.
- Smallest screen, landscape, large font scaling and the safe area.
- A null device or user in state reaches no crash.

## Stack pack: supabase
- Row level security: a person who is not an admin, and a signed-out request, are refused with a clear message (not an empty list that looks like success).
- A database guard exists as well as the screen guard: try the forbidden change in SQL inside a transaction that rolls back.
- A migration is safe to run twice, and the code works before and after it is applied.

## Output

## QA edge-case report
| # | Case group | Status | Notes (file:line) |
|---|------------|--------|-------------------|
| 1 | Loss and failure | Handled / Gap | |
| 2 | Lifecycle and resume | Handled / Gap | |
| 3 | Null and empty states | Handled / Gap | |
| 4 | Repeats and concurrency | Handled / Gap | |
| 5 | Task-specific | Handled / Gap | |

**Verdict: PASS or NEEDS FIX**

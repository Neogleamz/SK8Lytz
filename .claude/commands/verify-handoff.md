---
description: "Use when someone says 'test this', 'verify the change' or 'does this work', or when a task is finished and needs its verify steps: a check scoped to the diff, a curl or SQL check for backend work, a…"
argument-hint: "[task id]"
---
<!-- generated from HQ playbook verify-handoff v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Verify handoff: an isolated check of exactly what changed

**Role:** reviewer (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use when someone says 'test this', 'verify the change' or 'does this work', or when a task is finished and needs its verify steps: a check scoped to the diff, a curl or SQL check for backend work, a strict three-step manual script for UI; it stops and never verifies your own work.

## Steps
1. Scope to the diff: run git diff HEAD (or the task's branch against origin/main) and read exactly which lines changed. You test only that.
   - Gate: Do not evaluate or refactor anything outside the diff during this check.
   - Verify: The changed behaviours are listed in a line each.
2. Start what you need: the dev server for a screen, or nothing for a function. Run the repo's test command (get_repo, test_cmd) and the repo's verify command (get_repo, verify_cmd) and keep the output.
   - Verify: Both commands' results are quoted.
3. Backend, API, database or script change: write a temporary isolated check (a curl call, a SQL query inside a transaction that rolls back, a small script) against exactly what changed, run it, and keep the output as proof.
   - Verify: The output shows the changed behaviour working, and the failing case failing.
4. UI change: write a strict three-step manual script a person can follow: 1. where to go, 2. what to do, 3. what they must see. Give the exact URL or path.
   - Verify: Three steps, each with a visible result.
5. Write the checks as the task's verify steps with update_task (kind cmd, sql, url or manual, with the expected result), so the Verify screen shows them.
   - Verify: The task lists the steps you ran or wrote.
6. Stop. Hand the result to the person or the other agent who verifies: say what passed, what you could not check, and what only a person can judge.
   - Gate: Never verify your own work or your own agents' (gate G-4). Do not mark it verified, and do not commit on a failed or unchecked step.
   - Verify: The report names what is left for a person.
7. If it fails, fix only the lines found in step 1, then repeat from step 3. Do not rewrite the file or add architecture to fix a typo.
   - Verify: The second run passes with only the diff's lines touched.

## Output

## Verify handoff
**Changed:** one line per behaviour
**Ran:** each command and its result
**Manual script (UI):** 1. ... 2. ... 3. ...
**Not checked / needs a person:** ...
**Verify steps written to the task:** yes or no

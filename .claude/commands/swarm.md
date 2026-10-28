---
description: "Use when asked to clear low-hanging fruit, run a swarm, or work many small tasks in parallel: HQ hands out a batch of small, cheap tasks that touch different files, one sub-agent each on its own mode…"
argument-hint: "[plan id, optional: run that plan's next wave instead]"
---
<!-- generated from HQ playbook swarm v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Swarm: many cheap sub-agents run a file-disjoint batch of small tasks at once

**Role:** builder (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use when asked to clear low-hanging fruit, run a swarm, or work many small tasks in parallel: HQ hands out a batch of small, cheap tasks that touch different files, one sub-agent each on its own model, merged one at a time.

## Steps
1. Check in and read the limits: report_session with your plan usage, then get_session_policy. Stop at the wind-down point.
   - Gate: At the wind-down point start nothing new.
   - Verify: You know your limit and how many tasks you hold (my_claims {session_id}).
2. Get the batch: list_ready_tasks { swarm: true } (or { plan_id, autopilot: true } for a plan's next wave). It returns tasks whose files clash with neither each other nor claimed work, capped by your limit, and left says why the rest wait.
   - Verify: A batch of task ids, each with its exec_model and files.
3. Claim each task with claim_task (your session_id and plan usage), then get_task_brief for each.
   - Gate: A refused claim stays unclaimed: drop it from the batch.
   - Verify: my_claims {session_id} lists the batch.
4. One sub-agent per task, at once, each on the task's exec_model, each in its own worktree from origin/main (with the node_modules junction), each given the whole brief. They edit, run the task's verify steps and return a summary and their branch.
   - Gate: Sub-agents never push, merge, log_run or mark_done: you do, once per task.
   - Verify: Every sub-agent returned a branch and its verify results.
5. Merge one at a time onto the latest main: rebase the task branch, run npm test and npm run build, push to main (fetch, rebase and retry when another session wins the race; confirm the commit is on origin/main). Deploy changed functions with node scripts/deploy-functions.mjs changed.
   - Gate: A task whose tests fail is not merged: log_run partial with the next step and release_task.
   - Verify: Each merged commit is on origin/main and its checks passed.
6. Close each merged task: log_run (changed_files), write_kb with its lessons, mark_done. Then take the next batch (step 2) until none is left or you reach the wind-down point.
   - Verify: Every task you claimed is done, or released with a partial run.
7. Before you stop: write_handoff with what ran, what was left and why.
   - Verify: The handoff names every task of the run.

## Output

## Swarm run
**Batches:** N
**Done:** task ids (merged commits)
**Released:** task ids, each with why
**Left:** what waits for a person, from list_ready_tasks left
**Usage:** 5-hour and weekly percent at the end

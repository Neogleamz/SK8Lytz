---
description: "Use before you stop, when you reach the wind-down point, or when asked to wrap up: a completeness audit of uncommitted work, unpushed branches, runs, decisions, research and friction, then write_hand…"
---
<!-- generated from HQ playbook wind-down v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Wind down: audit what is left, then write the handoff

**Role:** overseer (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use before you stop, when you reach the wind-down point, or when asked to wrap up: a completeness audit of uncommitted work, unpushed branches, runs, decisions, research and friction, then write_handoff.

## Steps
1. Work left in the checkout: git status in the main folder and in every worktree for sk8lytz. List uncommitted changes.
   - Verify: Each worktree is listed with clean or dirty.
2. Branches that exist only here: list the t/ branches that are not on origin (git branch -vv, git log --not --remotes). Push them, or say why not.
   - Gate: Commits start with the repo's commit rule (none).
   - Verify: Every t/ branch is pushed or accounted for.
3. Trace each commit of this session to a run: every commit has a log_run with its hash (or a [chore] reason). Close what you hold: mark_done, or release_task with a partial log_run and a next_step.
   - Verify: my_claims shows nothing held that you are not working.
4. Decisions: every decision you opened or saw answered has its rejected options and a 'Don't re-derive:' line. Note their ids.
   - Verify: The decision ids are listed.
5. Uncaptured research: anything you learned from the web or the code that no one can find yet goes into record_research or write_kb now.
   - Verify: Nothing learned this session lives only in the chat.
6. Friction audit: list this session's corrections, workarounds and rules you missed, and for each what you filed (report_friction). A mistake the owner corrected twice is reported even if it looks small.
   - Verify: Each item has a friction report or a reason.
7. Call write_handoff once: session_id, a short summary, the next steps, the tasks you touched, the decisions opened and answered, what_burned_us and user_pattern. docs/HANDOFF.md is generated from it: never edit it by hand.
   - Gate: Do not stop before the handoff is written.
   - Verify: write_handoff returned an id.

## Output

## Wind-down audit
| Check | Status |
|---|---|
| Uncommitted work | none / listed |
| Unpushed t/ branches | none / listed |
| Commits traced to runs | all / gaps |
| Decisions with don't-re-derive | all / gaps |
| Research captured | yes / gaps |
| Friction filed | yes / n items |

**Handoff id:** ...

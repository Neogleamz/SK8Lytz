---
description: "Use when the owner says run this plan, run the waves, go and walk away, or launch everything after answering decisions, or when the run-waves-queue scheduled task picks up a run-waves job: you are th…"
argument-hint: "<plan id>"
---
<!-- generated from HQ playbook run-waves v5, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Run waves: the Conductor runs a whole approved plan, wave by wave, with a sub-agent per task on its model

**Role:** conductor (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use when the owner says run this plan, run the waves, go and walk away, or launch everything after answering decisions, or when the run-waves-queue scheduled task picks up a run-waves job: you are the Conductor, sub-agents do the tasks, you check, land, close and move to the next wave until the plan is done or you reach the wind-down point.

## Steps
1. Start: report_session with your plan usage, get_session_policy for your limits (wind-down, hard stop, stale_claim_hours), and get the plan (its id is the argument). It must be approved or in progress and have tasks with plan waves; if it has none, say so and stop. Heartbeat: every step below starts with report_session (your session_id, usage and the label run-waves <plan id>), so HQ and the Waves board see the run is alive. Heartbeat note: one line (wave, tasks, state). Status: post at most 5 plain lines at least every 5 minutes (building, landed, next, waits on Andy, or "no change since HH:MM"); sub-agents in background; re-arm */5 timer (CronCreate) after each post, CronDelete at run end.
   - Gate: At the wind-down point start nothing new; at the hard stop save and release now.
   - Verify: You know the plan, who you run as, your limit and your usage.
2. Who you run as: a run the run-waves-queue scheduled task starts (a queued run-waves job, decision q-mcp-btoq590r) runs as the Conductor agent, agent:conductor, with its own HQ_BRAIN_MCP_TOKEN (docs/runbooks/conductor.md): my_claims or get_session_policy answers as conductor. A person who types /run-waves in their own session runs as themselves.
   - Gate: A queued run that does not answer as conductor stops before any claim and says why (the Conductor token is missing or refused), so the run-waves-queue task fails the job with that reason.
   - Verify: The run's agent is conductor for a queued run, or the person's own agent for a run by hand.
3. Heartbeat, then get the next wave: list_ready_tasks { plan_id, autopilot: true }. It returns the wave, a batch of its ready tasks that touch different files, capped by your limit, and left: each task that waits and why (a person's gate, a dependency, a file clash, the cap). Budget rule: heartbeat, keep the 5-hour percent; before any claim call budgetGate({budgets: parseBudget(args), runReadings, waveReadings}) from public/js/budgetModel.js; if go is false, start nothing new, land in-flight work, put words in the heartbeat note, job summary and handoff, then end.
   - Gate: A task that needs a person is never claimed: it stays in left and goes in the handoff.
   - Verify: A wave name and a batch of task ids, or a note that nothing can start.
4. Heartbeat, then claim each task of the batch with claim_task (your session_id and usage), then get_task_brief for each.
   - Gate: A refused claim is dropped from the batch, never forced.
   - Verify: my_claims {session_id} lists the batch.
5. Heartbeat, then plan the personas for the batch: run node scripts/persona-step.mjs wave --in <file>, the file holding { tasks: [{ id, effort, exec_model, files, tags, lane, lessons }], focus: get_session_policy focus.line, lanes: { <lane>: its focus } }. It says whether the Architect and the Overseer run, and gives each task its builder kind (ui, database, edge-function, docs or general: from its files, else its layer: tag), model and preamble: persona line, kind notes and the context stack (soul, vision, focus, lane, the task's lessons), capped. Every sub-agent below is launched with subagent_type set to its persona, so its hq_subagent_runs row shows the persona of each step.
   - Gate: The Architect and the Overseer are skipped only when every task is XS or S (persona-step.mjs says so); the Builder, the Reviewer and the Debugger are never skipped. A persona step skipped for any other reason is named, with the reason, in that task's log_run.
   - Verify: Each task of the batch has its builder kind and preamble, and you know whether the Architect and the Overseer run.
6. Heartbeat, then the Architect checks the wave before it starts (unless skipped): one sub-agent, subagent_type architect, model opus, given only the wave list (each task's id, title, effort, files and dependencies) and the preamble of node scripts/persona-step.mjs architect, never the full briefs. It answers GO, or a one-line concern per task (a file clash, a missing dependency, a wrong order, a design that breaks a rule).
   - Gate: A task with a concern is not launched: log_run partial with the concern as the next step, then release_task. The Architect decides nothing; a design question goes in the handoff for a person.
   - Verify: The Architect said GO for every task you launch, or it was skipped for an XS/S-only wave.
7. Heartbeat, then launch one builder per task at once (the Agent tool, subagent_type builder), each with model set to the task's exec_model, each told to work in its own worktree from origin/main with the node_modules junction on branch t/<task id> (the SubagentStop hook records its run against that task), its prompt the task's preamble from the persona plan followed by the whole brief, and told to run the task's verify steps and return its branch, commit and results.
   - Gate: Sub-agents never push, merge, log_run, write_kb or mark_done: you do, once per task.
   - Verify: Every sub-agent returned a branch and its verify results.
8. Heartbeat, then the Debugger on a failure, before any release: a task whose verify steps, tests or build failed gets one Debugger attempt (subagent_type debugger, model from node scripts/persona-step.mjs debug: the exec_model, at least sonnet) in its worktree, given only the failing command, its last 80 output lines, git diff --stat origin/main...HEAD and the preamble; it fixes and commits, or says why not. Then release stuck work: still failing after the Debugger, no branch or no commit, or no progress past stale_claim_hours (my_claims, no run logged): log_run partial with what was done, the Debugger's finding and the exact next step, then release_task, and go on with the others.
   - Gate: A failed test or build is never released before its Debugger attempt (one per task per run). Never leave a task in doing under your session, and never land half work from a stuck sub-agent.
   - Verify: Every claim you hold has a passing result ready to check, or it is released with a partial run after its Debugger attempt.
9. Heartbeat, then read each sub-agent's run record before you merge: its hq_subagent_runs rows (Supabase connector, read only: select subagent_id, subagent_type, requested_model, actual_model, model_mismatch, minutes, requests, result from public.hq_subagent_runs where task_id = '<task id>' order by created_at desc) and the model check: the builder's actual model must carry the task's exec_model (public/js/modelCheckModel.js checkModel; the Agents roster flags a mismatch). subagent_type shows the persona of each row (builder, and debugger where one ran).
   - Gate: A mismatch is not landed: log_run partial naming the exec_model and the actual model, release_task, so it runs again on its model. No row yet (the hook can lag): look once more; still none, land it and write model check unknown in its log_run.
   - Verify: Every task you merge has its run record read and its model check (match, mismatch or unknown) noted for its log_run.
10. Heartbeat, then the Reviewer checks each change before it lands: one sub-agent per task, subagent_type reviewer, model sonnet, in the task's worktree, given only the diff (git diff origin/main...HEAD), the task's title, files and verify steps and the preamble of node scripts/persona-step.mjs review, never the full brief. PASS goes on to the landing. NEEDS FIX goes back to a builder once (the same preamble, with the Reviewer's file:line list), then to the Reviewer once more.
   - Gate: A change without a PASS is never landed: a second NEEDS FIX is saved with log_run partial (the findings as the next step) and release_task. The Reviewer never edits, and its PASS is evidence, not a verification (gate G-4).
   - Verify: Every task you land has a Reviewer PASS on its final diff.
11. Heartbeat, then land one task at a time through the merge queue: node scripts/hq-land.mjs --cwd <its worktree>. It takes the per-machine lock, rebases on origin/main, pushes once with the pre-push checks (tests and build), retries a lost race and says landed only when the commit is on origin/main; keep the JSON it prints (its log_run object). Then apply its migrations on HQ through the Supabase connector and read them back; deploy changed functions with node scripts/deploy-functions.mjs changed (health check and rollback built in).
   - Gate: Never push by hand, set HQ_SKIP_PREPUSH, use --no-verify or force-push (decision q-mcp-6klpqw2p). Not landed because the pre-push tests or build failed: the Debugger attempt first if the task has not had one, then the lander once more. Not landed otherwise (exit 1) or a deploy rolls back: do not retry blindly; log_run partial with the lander's next_step, release_task, go on with the others.
   - Verify: Each landed commit is on origin/main (landed: true in the lander's JSON), migrations read back, functions healthy.
12. Heartbeat, then close each landed task: log_run from the lander's log_run object plus changed_files, what passed, the model check (exec_model and actual model) and the personas that ran (builder kind, the Reviewer's verdict, the Debugger if one ran), write_kb with its lessons, mark_done. It then waits for a person or another person's agent to verify it.
   - Verify: Every task of the wave is done or released with a partial run.
13. Next: heartbeat (report_session with your usage), then back to step 3 for the next batch of this wave or the next wave, until list_ready_tasks returns nothing that can start. Budget rule: heartbeat, keep the 5-hour percent; before any claim call budgetGate({budgets: parseBudget(args), runReadings, waveReadings}) from public/js/budgetModel.js; if go is false, start nothing new, land in-flight work, put words in the heartbeat note, job summary and handoff, then end.
   - Gate: At the wind-down point stop taking batches: finish or save what you hold.
   - Verify: The plan has no ready task left, or you stopped at the wind-down point.
14. End-of-run sweep, before you write the handoff or finish: re-read every task you worked in this run (my_claims, then get_task_brief for each, which lists its runs). A task whose latest run passed with its commit on origin/main (git fetch, then git branch -r --contains <sha> names origin/main) and that is not done is closed now: write_kb if it has no reasoning record, then mark_done (claim_task it first if it was released to todo and no decision is open). A task released by the 3-strikes rule has an open decision you cannot claim: leave it and name it in the summary with the decision and the sha its work landed at; a person closes it with one click. Never answer that decision yourself.
   - Gate: Never leave a task whose work landed as todo, released or doing under your session, and never list a task as done in the summary unless mark_done succeeded for it. You never verify your own work (gate G-4): mark_done only waits for verification.
   - Verify: For every task of the run: it is done (mark_done returned), or it is released with a partial run, or it is named as waiting on a decision with the sha its work landed at.
15. Overseer reviews the run, before the handoff (unless every task of the run was XS or S): one sub-agent, subagent_type overseer, model haiku, given only the run summary (per task: outcome, commit, model check, the personas that ran from hq_subagent_runs subagent_type, the release reason) and the preamble of node scripts/persona-step.mjs oversee. It reports patterns: repeated failures, model mismatches, a persona step missing (personaRecordProblems in public/js/personaStepModel.js), stuck claims.
   - Gate: The Overseer verifies, decides and edits nothing; its findings go in the handoff for a person.
   - Verify: The handoff carries the Overseer's findings, or says it was skipped for an XS/S-only run.
16. End: write_handoff with the waves run, the tasks done (commits), the tasks released and why, every model mismatch, the personas per task and every persona step skipped with its reason, the Overseer's findings, and everything in left that waits for a person; tell the owner in plain words what is done and what needs them. A queued run returns that summary to the run-waves-queue task, which completes the job (complete_job) or fails it with the reason (fail_job). Archive, last after the handoff: tell your parent the run is finished (it calls archive_session on you), or with no parent call archive_session on yourself.
   - Verify: No task is left in doing under your session; the handoff names every task of the run.

## Output

## Run waves: <plan id>
**Ran as:** conductor (queued run) or your own agent (by hand)
**Waves run:** names
**Done:** task ids with commits (landed through hq-land.mjs)
**Model check:** task ids with exec_model and actual model; every mismatch
**Personas:** per task the builder kind, the Reviewer's verdict and the Debugger if one ran; the Architect and the Overseer ran or skipped (XS/S only); every persona step skipped and why
**Overseer:** its findings, or skipped
**Released:** task ids, each with the next step (stuck, model mismatch, not landed)
**Waiting on a person:** task ids and what each needs
**Usage:** 5-hour and weekly percent at the end

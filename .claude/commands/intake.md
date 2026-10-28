---
description: "Use when someone describes a feature, a fix or an idea casually, before any work starts: it turns the request into a classified, evidence-backed, planned task that passes the Definition of Ready."
argument-hint: "[the idea or request]"
---
<!-- generated from HQ playbook intake v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Intake: from an idea to a ready task

**Role:** planner (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use when someone describes a feature, a fix or an idea casually, before any work starts: it turns the request into a classified, evidence-backed, planned task that passes the Definition of Ready.

## Steps
1. Board first: list_ready_tasks, my_claims and search_brain. Is it already a task, an idea or a decision? If yes, work that record instead of making a new one.
   - Gate: Intercept a casual request: classify it before you do any work.
   - Verify: The existing record is named, or none was found.
2. Knowledge first: search_brain, related_lessons and the KB, then the research library (search_brain for existing research). Reuse beats a duplicate.
   - Verify: What was found is stated, with its ids.
3. Benchmark: look at how three to five strong products or teams solve this exact problem (skip a topic the KB already holds and is current). Record the finding with record_research and name the approach to adopt.
   - Verify: Three to five named examples and the chosen approach.
4. Create the record through the tools, never raw SQL: create_idea for a new idea, create_task for a ready piece of work. Classify it: area, layer, size (XS to XL), model, risks and gates.
   - Verify: The record exists and the classification is complete.
5. Write the why: one sentence of evidence (update_task why), a confidence (verified, inferred or hypothesis) and at least one rejected alternative with its reason. A large task (L or XL) needs these before it can be claimed.
   - Gate: Definition of Ready: verify steps, a why, a design or plan for L and XL, done dependencies, confidence not hypothesis.
   - Verify: The task passes the ready check at claim.
6. Write the plan with propose_plan: steps small enough for one session each, every step with its files, a size, a model and a Verify line, and the plan ends with an Out of scope section.
   - Verify: Every step has files and a Verify line; Out of scope is present.
7. Order the work by file overlap: tasks that edit the same file go in different waves or depend on each other (depends_on); tasks with no shared files may run in parallel.
   - Verify: No two tasks in one wave share a file.
8. Stop at the decision that is a person's: the plan's approval waits in Decisions. Do not start the work until it is approved, and do not build in the same pass.
   - Gate: A person approves the plan.
   - Verify: The approval decision is open with its packet.

## Output

## Intake
**Request:** ...
**Existing record:** id or none
**Benchmarks:** 3 to 5, and the approach adopted
**Classification:** area, layer, size, model, risks, gates
**Why / confidence / rejected alternative:** ...
**Plan:** id, steps, Out of scope
**Waves:** by file overlap

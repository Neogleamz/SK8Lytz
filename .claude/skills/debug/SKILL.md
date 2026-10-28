---
name: debug
description: "Use whenever you investigate a bug, a crash, an exception, a failed test or 'why is this broken': known issues first, three theories before any code, a strike count and a post-mortem."
---
<!-- generated from HQ playbook debug v1, do not edit. Run `npm run gen:playbooks`. -->

# Debug: theory first, then a fix, then a post-mortem

**Role:** debugger (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use whenever you investigate a bug, a crash, an exception, a failed test or 'why is this broken': known issues first, three theories before any code, a strike count and a post-mortem.

## Steps
1. Known issues and lessons first: search_brain for the error text and the symptom, read the 'Known issues that touch these files' section of your brief, and call related_lessons for the task. If one matches, test its prior fix as Theory 1.
   - Verify: Say which known issue or lesson matched, or that none did.
2. Reproduce before you touch code: read the stack trace or error text, find the exact line, and run the failing check (the repo's test command (get_repo, test_cmd)). Keep the output.
   - Gate: No code changes yet. No diagnosis without the error text in front of you.
   - Verify: The failure reproduces and you can quote the error.
3. If the cause is not obvious, instrument: add a temporary log or trace at the suspect spot and run again. Remove it before you commit.
   - Verify: The log shows which branch or value is wrong.
4. Write three theories before any fix. Theory 1 is the most likely and names the mechanism; Theory 2 is the next candidate; Theory 3 is an edge case. For each, the evidence for and against.
   - Gate: Do not edit code until the evidence picks one theory.
   - Verify: Three theories are written with evidence, and the one chosen says why.
5. Fix the theory the evidence supports with the smallest change that works, and leave a source comment saying why. Run the repo's test command (get_repo, test_cmd).
   - Verify: The reproduction now passes and the rest of the suite still does.
6. Count strikes: every fix attempt that fails is a log_run with result fail, naming the theory you tried and what it showed.
   - Gate: The third failed log_run in a row releases the task and opens a decision for a person. Do not try a fourth variation. Nothing is reset: branch t/<task id> keeps the work.
   - Verify: Each failed attempt is logged with the theory and the outcome.
7. When it is fixed, write the post-mortem into the task's reasoning record (write_kb, lessons and deviations) with the four fields in the output template.
   - Verify: Root cause, ruled out, don't re-derive and source are all filled in.
8. If the pattern is new, file it as a known issue so the next brief shows it: write_kb kind known-issue with repo sk8lytz, symptom, root_cause, fix and the files it touches. If the mistake was in how you worked (a skipped check, a repeated error), report_friction.
   - Verify: The known issue exists, or you say why the pattern is not new.

## Output

## Post-mortem
**Root cause:** the exact cause, one or two sentences
**Ruled out:** each rejected theory and why
**Don't re-derive:** the settled cause, in a line a later session can trust
**Source:** file:line of the fix
**Known issue filed:** the KB article id, or none and why

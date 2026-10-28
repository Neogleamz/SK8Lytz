---
description: "Use when the owner says everything is broken, asks to revert, or reports an emergency: enter read-only triage, assess, and propose the safe escape commands for the owner to run."
argument-hint: "[what is broken]"
---
<!-- generated from HQ playbook panic v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Panic: read-only incident mode

**Role:** debugger (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use when the owner says everything is broken, asks to revert, or reports an emergency: enter read-only triage, assess, and propose the safe escape commands for the owner to run.

## Steps
1. Go read-only: write no feature code, no quick patch and no state change.
   - Gate: Strictly read-only until the owner says 'Crisis averted'.
   - Verify: No file or record was changed.
2. Assess the situation in sk8lytz: git status and git log -3 --oneline, and ask for the exact error text or what looks broken.
   - Verify: The repo state and the symptom are quoted.
3. Check known issues and recent changes: search_brain for the symptom and read the latest handoff, so you know what changed last.
   - Verify: The most likely recent cause is named, or none found.
4. Propose the safe escape: the exact commands to abort or return to the last stable state (for example git merge --abort, git revert of one commit, or checking out the last good tag). Explain what each does before the owner runs it. Prefer a revert over a reset, and never propose a force push.
   - Gate: You propose; the owner runs. Do not run a destructive command yourself.
   - Verify: Each command has a plain-words effect.
5. Hold the state. When the owner says 'Crisis averted', run the debug playbook on what happened and file report_friction if a rule would have prevented it.
   - Verify: The post-mortem is queued.

## Output

## Triage
**State:** git status and last three commits
**Symptom:** quoted
**Likely cause:** ...
**Safe escape (you run these):**
1. command: what it does
2. command: what it does

Holding in read-only mode.

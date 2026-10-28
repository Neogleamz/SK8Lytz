---
description: "Use before you claim or start an L or XL task, or whenever you are asked 'are you following' or 'do you understand': restate the intent, the assumptions, what is out of scope, the measurable done and…"
argument-hint: "[what to restate]"
---
<!-- generated from HQ playbook echo v1, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Echo: restate the work before you start a big task

**Role:** researcher (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use before you claim or start an L or XL task, or whenever you are asked 'are you following' or 'do you understand': restate the intent, the assumptions, what is out of scope, the measurable done and the gaps, then wait for a yes.

## Steps
1. Halt: write no code, plan no steps and run no commands that change anything until the echo is confirmed.
   - Gate: Read only until the person says aligned.
   - Verify: Nothing was changed.
2. Restate the request in two or three sentences that prove you grasp the intent and the mechanics, not just the words.
   - Verify: A person could correct it.
3. State one to three assumptions you are holding (for example: this reads existing data, it needs no new table).
   - Verify: Each assumption is testable.
4. State what you will not do: the boundaries that stop scope creep.
   - Verify: At least one exclusion is named.
5. State the measurable done: exactly what passes or shows, so it can be tested.
   - Verify: Done is a result you can check, not a feeling.
6. Name the gaps: anything vague, contradictory or missing. If everything is clear, say 'Context is 100% clear'.
   - Verify: Gaps are listed or declared absent.
7. Ask: 'Is this alignment correct? Tell me where I am off, or say aligned.' If it is wrong, ask specific questions until it is right; do not guess again.
   - Gate: Do not claim the task or start until aligned.
   - Verify: The answer was 'aligned' or a correction you then restated.

## Output

## Echo
**Understanding:** 2 to 3 sentences
**Assumptions:** 1 to 3
**Out of scope:** ...
**Done means:** a checkable result
**Gaps:** ... or 'Context is 100% clear'

Is this alignment correct?

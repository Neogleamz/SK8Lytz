---
trigger: always_on
---

# Agent Cognitive Behaviors & Learning Directives

---
trigger: always_on
---

## Anti-Hallucination & First Principles

Eliminate guesses by anchoring decisions to First Principles.

1. **Deny Assumptions**: Do not guess root causes based on generic knowledge.
2. **First-Principles Audit**: Check `tools/SK8Lytz_App_Master_Reference.md` first. If missing, read the actual `src/` codebase.
3. **Cite Truth**: Cite the exact line of your documentation or code source when diagnosing.
4. **Math Mandate**: For BLE payloads or SQL/schema logic, output the hex array math/SQL clearly and verify before editing.
5. **Conflict Halt**: If the live codebase contradicts the Master Reference, HALT immediately and ask the user to decide the source of truth.
6. **Discovery Mode**: If answers are missing entirely, explicitly announce entering "Discovery Mode" before researching via external tools.


---
trigger: always_on
---

## Meta-Evolution & Self-Correction

1. **Friction Detection**: If corrected twice on the same topic, pause and propose updating the rules to prevent repeats.
2. **Victory Snapshot**: After struggling to find a correct syntax/pattern/MCP sequence (e.g., >2 retries), proactively offer a "Victory Snapshot". If approved, append the pattern into `RULE[tool-playbook.md]` so the agent "learns" from success.


---
trigger: always_on
---

## Corporate Memory & Test Sync

1. **Keep Specs Synced**: `tools/SK8Lytz_App_Master_Reference.md` (Architecture/Hardware) and `tools/SK8Lytz_TEST_PLAN.md` (Testing).
2. **Search Before Write**: Do not append blindly. Update existing sections.
3. **The QA Mandate**: If you build a new UI feature, you must add a corresponding manual test case to the TEST_PLAN.


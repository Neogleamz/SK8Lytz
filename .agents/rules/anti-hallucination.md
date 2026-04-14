---
trigger: always_on
---

# Anti-Hallucination & First Principles Rule

Eliminate guesses by anchoring decisions to First Principles.

1. **Deny Assumptions**: Do not guess root causes based on generic knowledge.
2. **First-Principles Audit**: Check `tools/SK8Lytz_App_Master_Reference.md` first. If missing, read the actual `src/` codebase.
3. **Cite Truth**: Cite the exact line of your documentation or code source when diagnosing.
4. **Math Mandate**: For BLE payloads or SQL/schema logic, output the hex array math/SQL clearly and verify before editing.
5. **Conflict Halt**: If the live codebase contradicts the Master Reference, HALT immediately and ask the user to decide the source of truth.
6. **Discovery Mode**: If answers are missing entirely, explicitly announce entering "Discovery Mode" before researching via external tools.

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
2. **First-Principles Audit**: You MUST read `tools/SK8Lytz_App_Master_Reference.md` AND `tools/ZENGGE_PROTOCOL_BIBLE.md` BEFORE generating any implementation plan, bucket list task, or writing code. These are the absolute sources of truth.
3. **Continuous Sync**: If you discover a protocol truth (like an opcode meaning) during development, you MUST update both the Master Reference and the ZENGGE_PROTOCOL_BIBLE before proceeding with code changes.
4. **Cite Truth**: When generating an Implementation Plan that touches BLE payloads, opcodes, or architecture, you MUST include a `# Cited Truth` section quoting the exact line number from the Master Reference or Protocol Bible that justifies your code. If you cannot cite it, you cannot write it.
5. **The No `any` Cast Law**: You are strictly forbidden from fixing a TypeScript error by casting to `any` (e.g. `as any`, `@ts-ignore`). If the compiler says a method doesn't exist, you must find the correct import or fix the interface. `any` is a lazy bandage that causes runtime crashes.
6. **Math Mandate**: For BLE payloads or SQL/schema logic, output the hex array math/SQL clearly and verify before editing.
7. **Conflict Halt**: If the live codebase contradicts the Master Reference, HALT immediately and ask the user to decide the source of truth.
8. **Discovery Mode**: If answers are missing entirely, explicitly announce entering "Discovery Mode" before researching via external tools.


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
4. **The TDD Mandate**: If you modify ANY file that has a corresponding `.test.ts` file (or if you extract a new pure-math core logic hook), you MUST run `npm test` and update the unit test to verify your exact byte-math matches expected outputs. Never merge a broken test.


---
trigger: always_on
---

## No Pre-Existing Excuses

**"Pre-existing" is not a valid reason to leave a defect in the codebase.**

1. **Own Every Problem**: If you discover a bug, dead import, stale reference, or broken pattern during ANY phase (audit, QA, Release Manager review), you fix it — regardless of whether you introduced it. It is now our problem.
2. **Never Say "Not My Problem"**: Do NOT flag something as "pre-existing" and leave it for later unless it is a MAJOR architectural risk that requires its own plan and worktree. A dead import, a missing null guard, a stale variable — fix it on the spot and include it in the current commit.
3. **The Boy-Scout Distinction**: This rule does NOT override `Zero-Collateral Damage` (RULE[preferences.md] #4). The distinction is:
   - **Boy-Scout refactors** = unsolicited architectural rewrites, hook extractions, and renames during active development → **FORBIDDEN**
   - **Discovered defects** = dead imports, unused variables, stale references, broken patterns found during QA/audit → **ALWAYS FIX**
4. **Commit It With Context**: When fixing a pre-existing defect, include it in the current commit with a `chore(cleanup):` prefix or fold it into the nearest `fix:` commit. Never leave the repo in a state where you said "I saw a bug but it wasn't my task."

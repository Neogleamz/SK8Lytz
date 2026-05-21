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

## Self-Doubt Protocol (Hallucination Elimination Layer)

This protocol wraps around the existing Anti-Hallucination & First Principles rules as a mandatory enforcement layer. It applies to ALL assertions — technical, architectural, conversational, and inferential.

### Layer 1: Confidence Classification (Mandatory)

Before delivering ANY assertion of fact, you MUST internally classify your confidence level and attach the appropriate structured block to your response.

**VERIFIED** — You read the exact line in a source-of-truth file, the live codebase, or a verifiable external source.

> [!CONFIDENCE: VERIFIED]
> Source: [file or URL, with line number if applicable]
> Cross-checked: [secondary source or "N/A"]

**INFERRED** — You pieced it together from multiple signals, but no single authoritative line exists. You MUST NOT deliver inferred claims as facts.

> [!CONFIDENCE: INFERRED]
> Reasoning: [explain the logical chain]
> Gap: [what specific evidence is missing]
> Recommendation: [suggest Discovery Mode, user confirmation, or testing]

**UNVERIFIED** — You have no file-backed or source-backed evidence. You are FORBIDDEN from delivering this as a factual statement. You MUST halt and either enter Discovery Mode or ask the user.

> [!CONFIDENCE: UNVERIFIED]
> Admission: "I do not have a verified answer for this."
> Action: [Entering Discovery Mode / Asking user for clarification]

### Layer 2: Contradiction Scanner (Mandatory)

After forming an answer but BEFORE delivering it, you MUST perform an adversarial self-check:

1. Ask yourself: "What evidence would prove me wrong?"
2. Actively search for contradicting information in the codebase, docs, or prior conversation context.
3. If a contradiction is found: HALT. Present both sides to the user and let them decide. Do NOT silently pick one.
4. If no contradiction is found: Note the files/sources you scanned in the confidence block under a `Contradiction Check` field:

> Contradiction Check: Scanned [list of files/sources]. No conflicts found.

### Layer 3: Staleness Guard (Mandatory)

Before citing ANY document, file, or prior conversation as truth:

1. Consider the last-modified context of the source. If the source is a living document (e.g., a protocol bible, bucket list, or config file), verify its current content — do not rely on cached memory from earlier in the conversation.
2. Cross-reference the cited document against the LIVE codebase. If the code has diverged from the documentation, flag the discrepancy:

> [!WARNING]
> Staleness Risk: [document] claims X, but the live code at [file:line] shows Y. Flagging for user resolution.

3. If you cannot verify freshness (e.g., external URL with no date), explicitly state: "I cannot verify the freshness of this source."

### Enforcement Rules

- **No Silent Gap-Filling**: You are NEVER allowed to silently bridge a knowledge gap with plausible-sounding but unverified information. Every gap must be visible.
- **Confidence Blocks Are Not Optional**: If a response contains a factual claim and lacks a confidence block, the response is NON-COMPLIANT.
- **Cascading with Existing Rules**: This protocol does NOT replace `Cite Truth`, `Discovery Mode`, or `Conflict Halt`. It wraps around them as a verification and formatting enforcement layer. Those rules define WHAT to do; this protocol defines HOW to prove you did it.
- **Proportional Application**: For trivial or conversational statements (e.g., "sure, I can help with that"), a confidence block is not required. The trigger is any statement that asserts a FACT about code, architecture, hardware, protocols, APIs, databases, or system behavior.


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

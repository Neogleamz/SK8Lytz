---
description: Structured BLE protocol testing — read Master Reference, build hex payload, verify byte math, and log results
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/team-roster.md
---

> **🕵️ Scout — Reyes | BLE Lab Active**
> *Reyes treats every opcode like a lab specimen. Cite Master Reference line numbers. No assertions without hex math to back them up.*

---

### ⚡ Step 0 — Reyes Knowledge-First (MANDATORY, NO SKIP)
Before any protocol investigation, Reyes checks what the team already knows:

Announce: *"Checking what we already know about this command..."*

1. Read `tools/SESSION_LOG.md` — search for prior [DECISION] or [ARTIFACT] entries mentioning the target opcode (e.g., `0x62`, `0x59`, `0x74`).
2. Read `tools/SK8Lytz_App_Master_Reference.md` §3 BLE Protocol Library — find the command entry.
3. Read `tools/ZENGGE_PROTOCOL_BIBLE.md` §3 Opcode Command Map — find the command specification.

**If prior lab findings exist in SESSION_LOG:**
> *"Found prior lab results for [opcode] in SESSION_LOG [date]: [finding]. Verifying current state matches."*

**If no prior findings:**
> *"No prior findings for [opcode]. Proceeding with fresh lab session. Will write results to SESSION_LOG after."*

---

1. **Parse the command** from the user prompt (e.g., "ble lab 0x62 color write" → target command `0x62`).

2. **Read the Master Reference** — open and search `tools/SK8Lytz_App_Master_Reference.md` for the target command byte to retrieve:
   - Command structure and byte layout
   - Expected payload format (Big-Endian/Little-Endian)
   - Expected notification response format

3. **Build the hex payload** — physically write out the complete byte array in chat:
   ```
   Payload: [0x7E, 0x07, 0x05, 0x03, <R>, <G>, <B>, 0x00, 0xEF]
   ```
   Show the math for each calculated byte. Verify checksum if applicable.

4. **Cross-reference with source code** — use `grep_search` to find where this command is constructed in the codebase (likely in `src/protocols/` or `src/services/`). Verify the implementation matches the Master Reference.

5. **Report discrepancies** — if the source code and Master Reference disagree, **HALT** and flag the anomaly per the Anti-Hallucination Rule. Do not proceed until the user declares the Source of Truth.

6. **Log the test** — output a structured lab report:
   ```
   ## 🧪 BLE Lab Report
   - Command: 0x<XX>
   - Purpose: <description>
   - Payload: [<hex bytes>]
   - Source File: <file path>
   - Master Ref Match: ✅/❌
   - Notes: <any observations>
   ```

7. If the user confirms the test was successful, offer to update the Master Reference with any new findings.

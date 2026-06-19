# /ble-lab — BLE Protocol Lab

**Description:** Structured BLE protocol testing — read Master Reference, build hex payload, verify byte math, and log results.
**Persona:** 🕵️ Scout — Reyes

> **🕵️ Scout — Reyes | BLE Lab Active**
> *Reyes treats every opcode like a lab specimen. Cite Master Reference line numbers. No assertions without hex math to back them up.*

---

### Phase 0 — Reyes KB Hardware Check (MANDATORY, NO SKIP)
Before any protocol investigation, Reyes checks the KB for existing hardware captures:

Announce: *"Checking KB for hardware entries..."*

Read `tools/knowledge-base/INDEX.md` — look for entries with `[HARDWARE]` or `[BLE]` domain tags matching the target chipset/opcode.
- **CURRENT entry found** → cite it: *"KB entry found: [slug]. Key fact: [relevant fact]. Verifying current state matches."* Skip re-research of already-known facts.
- **STALE/CRITICAL entry found** → flag: *"⚠️ KB STALE: [slug]. RE findings may be outdated if firmware was updated. Proceeding with caution."*
- **No entry** → *"No KB entry for this hardware topic. Will capture findings at session end."*

---

### Step 0 — Reyes Knowledge-First (MANDATORY, NO SKIP)
Before any protocol investigation, Reyes checks what the team already knows:

Announce: *"Checking what we already know about this command..."*

1. Read `docs/SESSION_LOG.md` — search for prior [DECISION] or [ARTIFACT] entries mentioning the target opcode (e.g., `0x62`, `0x59`, `0x74`).
2. Read `docs/SK8Lytz_App_Master_Reference.md` §3 BLE Protocol Library — find the command entry.
3. Read `docs/ZENGGE_PROTOCOL_BIBLE.md` §3 Opcode Command Map — find the command specification.

**If prior lab findings exist in SESSION_LOG:**
> *"Found prior lab results for [opcode] in SESSION_LOG [date]: [finding]. Verifying current state matches."*

**If no prior findings:**
> *"No prior findings for [opcode]. Proceeding with fresh lab session. Will write results to SESSION_LOG after."*

---

1. **Parse the command** from the user prompt (e.g., "ble lab 0x62 color write" → target command `0x62`).

2. **Read the Master Reference** — open and search `docs/SK8Lytz_App_Master_Reference.md` for the target command byte to retrieve:
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

7. If the user confirms the test was successful, offer to update the Master Reference with any new findings. **Additionally, if any new opcode behavior or hardware constraint was discovered during this session, Reyes MUST run `/kb-capture` targeting `knowledge-base/hardware/` before the session ends.**

8. **Write SESSION_LOG [ARTIFACT] entry (mandatory — P3 compliance)**:
   After every BLE lab session, append to `docs/SESSION_LOG.md`:
   ```markdown
   ### [ARTIFACT] YYYY-MM-DDTHH:MM — BLE Lab: 0x<XX> <command-name>
   **Payload tested:** [hex bytes]
   **Result:** Master Ref match: ✅/❌ | Discrepancies: (list or "none")
   **KB captured:** ✅/❌
   **Source file verified:** <file path>
   ```

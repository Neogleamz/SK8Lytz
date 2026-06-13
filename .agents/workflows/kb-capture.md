---
description: Capture external knowledge into the Knowledge Base — triggered after any research session with external findings
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/rules/team-roster.md
---

> **🕵️ Scout — Reyes | KB Capture Active**
> *Every external fact discovered this session gets a permanent home before the session ends.
> The KB is the only reason future Reyes doesn't pay for today's research twice.*

// turbo-all

---

### ⚡ KB Capture — Trigger Conditions

Run `/kb-capture` when ANY of the following occurred this turn or session:
- A `search_web` call returned API guidance, pattern analysis, or competitor insights
- A GitHub README, docs page, or npm package API was read
- A reverse engineering session produced hardware findings
- `add-dep` research established library behavior facts
- `ble-lab` discovered new opcode behavior not in the Protocol Bible
- Morgan completed a Giants-First benchmarking exercise

---

### 📋 Phase 1 — Classification (Reyes)

Answer these 4 questions before writing anything:

**1. Source Type?**
- URL (API docs, GitHub README, blog post) → `raw-captures/` or appropriate subfolder
- Reverse Engineering session → `hardware/`
- Competitor/pattern analysis → `patterns/`
- Library API behavior → `react-native/` or domain-appropriate subfolder

**2. Domain Tags?**
Assign from: `[BLE]` `[UI]` `[DB]` `[PATTERNS]` `[HARDWARE]` `[SCRAPER]`

**3. Staleness Window?**
Default by source type (see INDEX.md staleness table). Override only if the source has explicit versioning.

**4. Does this promote to a Tier-2 doc?**
- Hardware/BLE protocol finding → `docs/ZENGGE_PROTOCOL_BIBLE.md` or `docs/BANLANX_PROTOCOL_BIBLE.md`
- App architecture pattern → `docs/SK8Lytz_App_Master_Reference.md`
- If YES → flag for Avery handoff after capture

---

### ✍️ Phase 2 — Write the Capture File (Reyes)

Create or update `tools/knowledge-base/<subfolder>/<topic-slug>.md`:

```markdown
# <Topic Name> — Knowledge Capture

**Source:** <URL | "Reverse Engineering — <description>" | "GitHub: org/repo @ SHA">
**Captured:** YYYY-MM-DD
**Captured By:** <persona>
**Domain:** <tags>
**Staleness Window:** <Xd | never>

---

## Key Facts

(The extracted, team-relevant content. Be specific. Include:)
- Method signatures with types where applicable
- Edge cases and gotchas discovered
- Explicit version/context notes ("as of v3.1, this behavior changed from...")
- Any contradictions with our existing code or Master Reference

## Excerpts (Verbatim)

(Paste critical verbatim text — method signatures, code samples, protocol spec lines.
Keep it short — only the lines we'll actually cite in a plan or DECISION entry.)

## Team Notes

(What WE need to know about this. Not the library's intent — our specific use case context.)
- How we use this in SK8Lytz: ...
- Known quirks in our implementation: ...
- Do NOT do: ... (anti-patterns we've observed)

## History

(Record superseded versions here when the entry is re-validated and updated.)
| Date | What Changed |
|---|---|
| YYYY-MM-DD | Initial capture |
```

---

### 📇 Phase 3 — Update INDEX.md (Reyes)

Add or update the entry in `tools/knowledge-base/INDEX.md` following the exact schema:

```markdown
## <topic-slug>
- **File**: `knowledge-base/<path>/<filename>.md`
- **Source**: <URL or description>
- **Version/Commit**: <version or "N/A">
- **Captured**: YYYY-MM-DD
- **Staleness Window**: <window>
- **Re-Validate By**: YYYY-MM-DD (= Captured + Window, or "N/A")
- **Captured By**: <persona>
- **Domain Tags**: `[tags]`
- **Key Facts Stored**: (3-5 bullets)
- **Last Validated**: YYYY-MM-DD
- **Status**: ✅ CURRENT
- **Feeds Into**: <Tier-2 doc or "standalone">
```

Also update the **Staleness Summary table** at the bottom of INDEX.md.

---

### ⬆️ Phase 4 — Tier Promotion Check (Reyes → Avery)

If the captured finding belongs in a Tier-2 doc:

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🕵️ Scout — Reyes → 📋 Docs — Avery
Completed: KB capture complete. Entry written to INDEX.md and knowledge-base/<path>.
Picking up: Tier-2 promotion — write the finding to the appropriate Master Reference section.
Context: Finding: [one sentence]. Target section: [§X of file]. INDEX.md Feeds Into field already set.
─────────────────────────────────────────────────────────────────────
```

Avery writes the finding to the Tier-2 doc and updates the `Feeds Into:` field in INDEX.md to confirm promotion.

If NOT a Tier-2 promotion → skip this phase. Reyes proceeds directly to Phase 5.

---

### 📝 Phase 5 — SESSION_LOG Write-Back (Reyes)

Append a `[ARTIFACT]` entry to `docs/SESSION_LOG.md`:

```markdown
### [ARTIFACT] YYYY-MM-DDTHH:MM — KB Capture: <topic-slug>
**Type:** Knowledge Base Capture
**File:** `tools/knowledge-base/<path>/<filename>.md`
**Source:** <source>
**Key Finding:** <one sentence — the single most important fact captured>
**Feeds Into:** <Tier-2 doc or "standalone">
**Re-Validate By:** YYYY-MM-DD (or "never")
```

---

> **🕵️ Scout — Reyes | KB Capture Complete.**
> *External knowledge is now persistent. Future Reyes will find it in INDEX.md before going to the web.*

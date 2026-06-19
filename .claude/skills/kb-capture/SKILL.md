---
name: kb-capture
description: Capture external knowledge into the Knowledge Base. Use this WHENEVER a research session produced external findings — a web search returned API guidance, a GitHub README/npm API was read, a reverse-engineering session found hardware facts, a dependency's behavior was established, or a new BLE opcode was discovered. Auto-activate to persist findings before the session ends so they're never re-derived.
---

# Knowledge Base Capture (🕵️ Scout — Reyes)

Every external fact discovered this session gets a permanent home before the session ends. The KB is the only reason future Reyes doesn't pay for today's research twice.

> Open responses while this is active with `[🕵️ Reyes | kb-capture | {topic} | {cold/warm}]`.

## Trigger conditions
Run when ANY of these occurred this turn/session:
- A web search returned API guidance, pattern analysis, or competitor insights
- A GitHub README, docs page, or npm package API was read
- A reverse-engineering session produced hardware findings
- `add-dep` research established library-behavior facts
- `ble-lab` discovered new opcode behavior not in the Protocol Bible
- A Giants-First benchmarking exercise completed

## Phase 1 — Classification
1. **Source Type?** URL → `raw-captures/` (or domain subfolder); reverse engineering → `hardware/`; competitor/pattern → `patterns/`; library API → `react-native/` or domain subfolder.
2. **Domain Tags?** From `[BLE] [UI] [DB] [PATTERNS] [HARDWARE] [SCRAPER]`.
3. **Staleness Window?** Default by source type (see INDEX.md table); override only with explicit versioning.
4. **Tier-2 promotion?** Hardware/BLE → `docs/ZENGGE_PROTOCOL_BIBLE.md` or `docs/BANLANX_PROTOCOL_BIBLE.md`; app pattern → `docs/SK8Lytz_App_Master_Reference.md`. If YES → flag for Avery handoff.

## Phase 2 — Write the capture file
Create/update `tools/knowledge-base/<subfolder>/<topic-slug>.md`:
```markdown
# <Topic Name> — Knowledge Capture
**Source:** <URL | "Reverse Engineering — <desc>" | "GitHub: org/repo @ SHA">
**Captured:** YYYY-MM-DD
**Captured By:** <persona>
**Domain:** <tags>
**Staleness Window:** <Xd | never>
---
## Key Facts
- Method signatures w/ types; edge cases & gotchas; version notes; contradictions with our code/Master Reference
## Excerpts (Verbatim)
(Only the lines we'll actually cite — signatures, code samples, spec lines.)
## Team Notes
- How WE use this in SK8Lytz; known quirks in our implementation; anti-patterns (Do NOT do …)
## History
| Date | What Changed |
|---|---|
| YYYY-MM-DD | Initial capture |
```

## Phase 3 — Update INDEX.md
Add/update `tools/knowledge-base/INDEX.md` (full schema): File, Source, Version/Commit, Captured, Staleness Window, Re-Validate By (= Captured + Window), Captured By, Domain Tags, Key Facts Stored (3–5 bullets), Last Validated, Status ✅ CURRENT, Feeds Into. Also update the Staleness Summary table at the bottom.

## Phase 4 — Tier promotion check (Reyes → Avery)
If the finding belongs in a Tier-2 doc, hand off to Avery to write it and set INDEX.md `Feeds Into:` to `PROMOTED → §[section] on YYYY-MM-DD`. Otherwise skip to Phase 5.

## Phase 5 — SESSION_LOG write-back
Append to `docs/SESSION_LOG.md`:
```markdown
### [ARTIFACT] YYYY-MM-DDTHH:MM — KB Capture: <topic-slug>
**Type:** Knowledge Base Capture
**File:** `tools/knowledge-base/<path>/<filename>.md`
**Source:** <source>
**Key Finding:** <one sentence>
**Feeds Into:** <Tier-2 doc or "standalone">
**Re-Validate By:** YYYY-MM-DD (or "never")
```

External knowledge is now persistent. Future Reyes will find it in INDEX.md before going to the web.

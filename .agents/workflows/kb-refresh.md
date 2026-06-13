---
description: Knowledge Base Staleness Sweep — surface expired KB entries as warnings and guide re-validation
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/rules/team-roster.md
---

> **🕵️ Scout — Reyes | KB Refresh Active**
> *Stale knowledge is silent debt. This sweep surfaces what's expired so we can decide — with full context —
> whether to re-validate before using it in a sprint. Warnings only. The user decides what matters.*

// turbo-all

---

### ⚡ Trigger Conditions

`/kb-refresh` runs when:
- User types `/kb-refresh` directly
- `/hello` Step 0.7b surfaces 🔴 CRITICAL entries and user wants to address them now
- `/intake` Step 0.5a finds a stale KB entry for the task's domain

---

### 📊 Phase 1 — Staleness Scan (Reyes)

Run the validator:
```powershell
node tools/kb-validator.js
```

The validator outputs JSON:
```json
{
  "current": [...],
  "stale": [{ "slug": "...", "daysPastExpiry": N, "reValidateBy": "...", "domain": [...] }],
  "critical": [{ "slug": "...", "daysPastExpiry": N, ... }],
  "neverExpire": [...]
}
```

Output the human-readable staleness report:

```
🗄️ KB Staleness Report — <YYYY-MM-DD>
─────────────────────────────────────────────────────────────────────
✅ CURRENT  (<N> entries): [list slugs]
♾️  NEVER    (<N> entries): [list slugs — RE sessions]

⚠️  STALE    (<N> entries):
  - <slug> — expired <N> days ago | Domain: <tags> | Source: <URL>
    Last used for: (grep SESSION_LOG for this slug — cite the task)

🔴 CRITICAL (<N> entries — expired >30 days):
  - <slug> — expired <N> days ago | Domain: <tags> | Source: <URL>
    Last used for: (grep SESSION_LOG for this slug — cite the task)
    Risk: If a sprint task uses this as reference, findings may be outdated.
─────────────────────────────────────────────────────────────────────
```

---

### 🤔 Phase 2 — User Decision Gate (WARNING — Not a Hard Block)

Present each stale/critical entry to the user with a clear choice:

```
For each stale entry:
→ Re-validate now? [yes / skip / remove]
  - yes    → proceed to Phase 3 re-fetch for this entry
  - skip   → leave as STALE, note in SESSION_LOG
  - remove → delete the capture file and remove from INDEX.md
```

**CRITICAL entries get a stronger prompt:**
```
🔴 CRITICAL: <slug> is <N> days past expiry.
The following active sprint tasks reference this entry: [list or "none found"]
Recommend re-validating before using in sprint work.
→ Re-validate now? [yes / skip / note-risk]
  - note-risk → add ⚠️ WARNING comment to the task's Source of Truth field
```

**The agent MUST NOT auto-re-fetch without explicit user selection.** Present and wait.

---

### 🔄 Phase 3 — Re-Validation (Reyes, per approved entry)

For each entry approved for re-validation:

1. **Re-fetch the source**:
   - URL source → `read_url_content` or `search_web` for the specific page
   - GitHub README → read the raw README at the current HEAD
   - Forum/blog → `read_url_content`

2. **Diff against stored content**: Compare new content to the existing capture file.
   - **No significant changes** → update `Last Validated` and `Re-Validate By` only. Note: "No breaking changes found."
   - **Breaking changes found** → update the capture file with new facts. Move old content to `## History` section with date. Flag any facts that changed.
   - **Source gone / URL dead** → mark entry as `🔴 DEAD SOURCE`. Ask user whether to find a replacement or remove.

3. **Update INDEX.md**:
   - `Last Validated`: today
   - `Re-Validate By`: today + staleness window
   - `Status`: ✅ CURRENT
   - Update the Staleness Summary table

4. **If breaking changes were found** → check whether Tier-2 docs need updating:
   - Trigger Avery parity check if the entry `Feeds Into` a Master Reference section
   - If yes → handoff to Avery for Tier-2 update

---

### 📝 Phase 4 — Log (Reyes)

Append a `[ARTIFACT]` entry to `docs/SESSION_LOG.md`:

```markdown
### [ARTIFACT] YYYY-MM-DDTHH:MM — KB Refresh Sweep
**Entries scanned:** <N>
**Re-validated:** <N> entries: [list slugs]
**Skipped:** <N> entries: [list slugs]
**Removed:** <N> entries: [list slugs]
**Breaking changes found:** <yes/no — if yes, list affected slugs and what changed>
**Tier-2 updates required:** <yes/no — if yes, Avery notified>
```

---

> **🕵️ Scout — Reyes | KB Refresh Complete.**
> *Staleness report resolved. The knowledge base reflects the current state of approved re-validations.*

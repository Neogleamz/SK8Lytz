---
description: Fire up the environment, launch web demo, open it, and provide a status update
persona_entry: "📋 Scrum — Casey"
team_roster: .agents/team-roster.md
---

> **📋 Scrum — Casey | Session Kickoff Active**
> *Casey opens the session: environment up, memory loaded, sprint state reviewed, top priorities surfaced. You know exactly where we are before writing a single line.*

// turbo-all

---

### ⚡ Step 0 — Casey Worktree-First Check (MANDATORY, NO SKIP)
Before any environment launch, Casey checks the house:

```powershell
git worktree list
```

- Any orphaned worktrees (branch exists but task is `[x]`)? → Surface immediately, ask user to resolve.
- Any active worktrees with uncommitted changes? → Flag before proceeding.
- Output: *"Worktree state: [CLEAN / ISSUES FOUND]"*

---

### ⚡ Step 0.5 — Jordan Board-First Check (MANDATORY, NO SKIP)
Before reading anything else, Jordan reads `docs/SK8Lytz_Bucket_List.md` ACTIVE SPRINT section and outputs sprint health:
- Any zombie tasks (`[/]` with no active worktree)? → Flag and propose resolution.
- Any completed tasks not yet archived? → Archive them now before continuing.
- Any ON DECK tasks missing `Decision Log:` field? → Flag for return to TRIAGE.
- Output: *"Sprint health: [CLEAN / ISSUES: (list)]"*

---

### ⚡ Step 0.7 — Reyes Session Health Checks (MANDATORY, NO SKIP)

**0.7a. Friction Ledger Review:**
Reyes reads `docs/FRICTION_LEDGER.md` — specifically `🔴 Active Friction Events`:
- **0 active events** → *"Friction Ledger: CLEAN ✅. No recurring patterns to watch."*
- **Any event at 1–2 occurrences** → *"Watching for: [pattern name] ([X] occurrences). I'll flag if it recurs this session."*
- **Any event at 3 occurrences** → IMMEDIATELY generate a Rule Evolution Proposal before proceeding. Do not continue the session until the proposal is surfaced to the user.

**0.7b. KB Staleness Sweep (NEW — Knowledge Retention System):**
```powershell
node tools/kb-validator.js --summary
```
- **All current** → *"KB Health: CURRENT ✅. All [N] knowledge entries within staleness windows."*
- **⚠️ STALE entries** → List each: *"⚠️ KB STALE: [slug] — expired [N] days ago. Domain: [tags]. Consider /kb-refresh before using in sprint."*
- **🔴 CRITICAL entries** → Escalate: *"🔴 KB CRITICAL: [slug] — expired [N] days ago. Recommend /kb-refresh before sprint work that references this entry."*
- **INDEX.md not found** → *"KB not yet seeded. Run /kb-capture to initialize the knowledge base."*

Output: *"KB Health: [N stale / M critical / all current] | [/kb-refresh to address | no action needed]"*

Output combined: *"Session health: Friction Ledger: [status] | KB: [status]"*

---

1. Verify or Start the Docker Stack (Scraper Stack & Web Demo)
```powershell
docker compose up -d
```

2. Synchronize Supabase TypeScript definitions
   - Execute the `mcp_supabase-mcp-server_generate_typescript_types` MCP tool using project ID `qefmeivpjyaukbwadgaz`.
   - Overwrite `src/types/supabase.ts` with the new definitions using `write_to_file`.

3. Verify the Docker Stack is online
```powershell
docker compose ps
```

4. **Session Memory Read — Reyes Knowledge-First Protocol**: Read `docs/SESSION_LOG.md` — most recent entry only. Announce: *"Checking what we already know..."* then verbally summarize:
   - What burned us last session (AI failure pattern)
   - What the user pattern was
   - What architecture decisions are locked (don't re-derive these)
   This is the memory bridge. Never skip it.

5. **Bucket List Status**: The AI must read `docs/SK8Lytz_Bucket_List.md` to determine the current project status.

6. Verify Database Backup Status
   - Use PowerShell to check the `backups/` directory for the most recent `.sql` file: `Get-ChildItem -Path C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\backups -Filter "data_*.sql" | Sort-Object LastWriteTime -Descending | Select-Object -First 1`
   - Note the timestamp and file size of the last backup.

7. Provide a friendly greeting ("Hello!"), summarize the top 3 open items from the Bucket List, report the status of the database type sync and backup, and ask the user what they would like to tackle next.

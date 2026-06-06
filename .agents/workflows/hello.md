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
Before reading anything else, Jordan reads `tools/SK8Lytz_Bucket_List.md` ACTIVE SPRINT section and outputs sprint health:
- Any zombie tasks (`[/]` with no active worktree)? → Flag and propose resolution.
- Any completed tasks not yet archived? → Archive them now before continuing.
- Any ON DECK tasks missing `Decision Log:` field? → Flag for return to TRIAGE.
- Output: *"Sprint health: [CLEAN / ISSUES: (list)]"*

---

### ⚡ Step 0.7 — Reyes Friction Ledger Review (MANDATORY, NO SKIP)
Reyes reads `tools/FRICTION_LEDGER.md` — specifically `🔴 Active Friction Events`:
- **0 active events** → *"Friction Ledger: CLEAN ✅. No recurring patterns to watch."*
- **Any event at 1–2 occurrences** → *"Watching for: [pattern name] ([X] occurrences). I'll flag if it recurs this session."*
- **Any event at 3 occurrences** → IMMEDIATELY generate a Rule Evolution Proposal before proceeding. Do not continue the session until the proposal is surfaced to the user.

Output: *"Friction Ledger: [X] active patterns | [Y] resolved victories | Watching: [list of active patterns or 'none']"*

---

1. Launch the Scraper Stack (CCTower, Dashboard, Discord Bridge via PM2 — no popup windows)
```powershell
powershell.exe -ExecutionPolicy Bypass -File "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\scraper\start-scraper-stack.ps1"
```

2. Synchronize Supabase TypeScript definitions
   - Execute the `mcp_supabase-mcp-server_generate_typescript_types` MCP tool using project ID `qefmeivpjyaukbwadgaz`.
   - Overwrite `src/types/supabase.ts` with the new definitions using `write_to_file`.

3. Launch the Expo web server
```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"; npx expo start --web
```

4. **Session Memory Read — Reyes Knowledge-First Protocol**: Read `tools/SESSION_LOG.md` — most recent entry only. Announce: *"Checking what we already know..."* then verbally summarize:
   - What burned us last session (AI failure pattern)
   - What the user pattern was
   - What architecture decisions are locked (don't re-derive these)
   This is the memory bridge. Never skip it.

5. **Bucket List Status**: The AI must read `tools/SK8Lytz_Bucket_List.md` to determine the current project status.

6. Verify Database Backup Status
   - Use PowerShell to check the `backups/` directory for the most recent `.sql` file: `Get-ChildItem -Path C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\backups -Filter "data_*.sql" | Sort-Object LastWriteTime -Descending | Select-Object -First 1`
   - Note the timestamp and file size of the last backup.

7. Provide a friendly greeting ("Hello!"), summarize the top 3 open items from the Bucket List, report the status of the database type sync and backup, and ask the user what they would like to tackle next.

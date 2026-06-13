---
description: The Midnight Oil Protocol (Wind Down Workflow)
trigger: always_on
persona_entry: "🌙 Ops — Alex"
team_roster: .agents/team-roster.md
---

# The Midnight Oil Protocol (Wind Down Workflow)

// turbo-all

> **🌙 Ops — Alex | Wind Down Active**
> *Alex runs the shutdown checklist. Nothing gets left uncommitted, undocumented, or unnotified before this session closes. Methodical and complete.*

---

### ⚡ Step 0 — Alex Completeness-First (MANDATORY, NO SKIP)
Alex does NOT start the shutdown checklist until the workspace is in a known-good state.

**0a. Git Status Sweep (master + all worktrees):**
```powershell
# Master branch
git status --short

# All active worktrees
git worktree list
```
- Uncommitted changes on master? → HALT. Stage and commit before proceeding: `git add . && git commit -m "chore: end-of-session sync"`
- Any active worktrees with uncommitted changes? → FLAG to user: *"Worktree [name] has uncommitted changes. Commit or stash before wind-down."*
- Orphaned worktrees (branch exists, task is `[x]`)? → FLAG: *"Orphaned worktree detected: [name]. Route to Casey for cleanup."*

**0b. SESSION_LOG Decision Log Completeness Check:**
Read `docs/SESSION_LOG.md`. Find all `[DECISION]` entries written during this session (today's date).
For each entry, verify the `Don't re-derive:` field is filled. If missing:
> Fill it now: *"What is the one sentence the next agent must NOT re-derive about this decision?"*

**0c. Output Readiness Status:**
```
🌙 Alex Pre-Shutdown State:
- Master git status: [CLEAN ✅ / COMMITTED ✅ / ISSUES: (list)]
- Worktree state: [CLEAN ✅ / ORPHANS FOUND: (list)]
- Decision logs complete: [YES ✅ / GAPS FILLED ✅]
→ Proceeding with shutdown checklist.
```

---

When my prompt includes "good night", "wrapping up", "wind down", "done for the day", or "Run nightly testing", execute the following shutdown sequence:


1. **Workspace Sanitization (The Big Sync)**:
   - Check `git status`. If there are uncommitted changes, execute `git add .` and `git commit -m "chore: end of session sync and corporate memory update"`.
   - If a remote is configured, `git push`.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🌙 Ops — Alex → 🩺 SRE — River
Completed: Workspace committed and pushed.
Picking up: Production health check — Supabase log audit for 4xx/5xx errors and RLS violations.
Context: This is a read-only diagnostic pass. Any critical errors found go directly to TRIAGE QUEUE.
─────────────────────────────────────────────────────────────────────
```

1.5. **🩺 SRE — River | Production Health Check (Supabase Log Audit)**:

- Use `get_logs` for `api`, `auth`, and `postgres` services for the last 2 hours.
- Analyze logs specifically for 4xx/5xx errors, RLS violations, or foreign key constraints.
- If critical production errors are found, autonomously add them as prioritized `fix/...` entries to the top of the `docs/SK8Lytz_Bucket_List.md` in the `🚑 TRIAGE QUEUE`, strictly following the nested multi-line Task Schema defined in the AI AGENT DIRECTIVES.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🩺 SRE — River → 🌙 Ops — Alex
Completed: Production health check complete. Critical errors triaged or none found.
Picking up: Nightly testing execution and knowledge persistence.
Context: Production log status: [CLEAN / ISSUES TRIAGED]. Resuming shutdown checklist.
─────────────────────────────────────────────────────────────────────
```

1. **🌙 Ops — Alex | Nightly Testing Execution**:
   - Explicitly spawn the `browser_subagent` and instruct it to execute `docs/SK8Lytz_TEST_PLAN.md` against the local expo server.
   - If regressions are found by the agent, you MUST autonomously add them to `docs/SK8Lytz_Bucket_List.md` as new `fix/...` tasks under the `🚑 TRIAGE QUEUE`, strictly following the nested multi-line Task Schema defined in the AI AGENT DIRECTIVES.
   - Explicitly list them in the "Traps & Landmines" section of the Final SITREP.

2. **Knowledge Persistence (Master Reference Sync)**:
   - Review all implementation plans and terminal logs from the current session.
   - Identify any new architectural patterns, hardware protocol discoveries, or database schema changes.
   - Update `docs/SK8Lytz_App_Master_Reference.md` strictly following the **Corporate Memory Synchronization Rule**.

2.5. **KB Capture Sweep (Reyes — NEW)**:
   Reyes reviews the session’s research activity and verifies all external knowledge was captured:
   - Did this session involve reading external library docs, GitHub READMEs, or web searches for API guidance? → KB entry required
   - Did this session involve RE findings, hardware discoveries, or constraint analysis? → KB entry required (`hardware/`)
   - Did Morgan run any Giants-First benchmarking? → KB entry required (`patterns/`)
   - Did Blake discover a novel library behavior during QA? → KB entry required (`raw-captures/`)

   For each YES: verify a KB capture was already done (grep SESSION_LOG for `[ARTIFACT]` entries with `KB Capture` in the type line).
   For any uncaptured finding: run `/kb-capture` NOW before committing.

   Output: *"🗋️ KB Capture Sweep: [N] captures already logged | [M] new captures run this sweep | KB is up to date ✅"*

   After sweep, run the staleness validator one final time:
   ```powershell
   node tools/kb-validator.js --summary
   ```
   Include the KB health line in the Final SITREP.

3. **Bucket List Grooming & Archiving**:
   - Parse `docs/SK8Lytz_Bucket_List.md`.
   - Ensure every task completed during the session is marked with `[x]`.
   - **Cleanup & Archiving**: Physically move all `[x]` items from their active Epic sections into the `## ✅ Completed Previously` archive at the bottom of the file to maintain a lean active backlog.
   - **Icebox Sweep (Guillotine Protocol)**: Scan `🏗️ ROADMAP` and `🧹 TECH DEBT` sections. Any task older than 30 days that is still `[ ]` MUST be moved to the `❄️ ICEBOX` section. (This is the `/groom-backlog` protocol — running it inline here so it doesn't require a manual trigger.)
   - **Progress Sync**: Update the Mermaid `pie` charts in the `## 📊 Global System Readiness` and category headers to reflect the new completion counts.
   - **Prioritization**: Identify the next logical Task/Epic. Ask: "What is the absolute #1 priority for our next session?" and move that item to the top of the active list.

4. **The State of the Union (Final SITREP)**:
   - Generate a concise summary of today's achievements.
   - List any "Traps & Landmines": Technical debt, half-finished refactors, or bugs encountered that need immediate attention next time.
   - State the current active branch and the last commit hash.

4.5. **Database Backup**:
   - Execute `powershell.exe -ExecutionPolicy Bypass -File .\tools\backup_database.ps1` via the terminal.
   - Verify the script completes successfully and report the backup file sizes in the Final SITREP.

4.6. **Satellite Doc Completeness Check (Alex — Mandatory)**:
   Before the Hard Freeze, Alex MUST verify that all 3 Tier-3 satellite docs have been updated if their domains changed today:
   - Check `git log --oneline --since="today" --name-status`
   - If XState or session machines changed → verify `docs/State_Charts_UX.md` changed.
   - If WatchBridge or flow changed → verify `docs/User_Journey_Maps.md` changed.
   - If new hardware/API added → verify `docs/System_Context_Diagram.md` changed.
   - If ANY required satellite doc is missing an update: HALT wind-down and trigger `/audit-codebase` or `/deepdive-docs` to sync them first.


4.75. **Evolution Audit (Alex — Mandatory)**:
   Alex audits `docs/FRICTION_LEDGER.md` before the Hard Freeze:
   1. **New Friction Events This Session**: Did any rule get forgotten? Did any behavior need correcting? Was the user forced to remind the team of anything? If yes → file a new `[FRICTION-XXX]` entry for each pattern.
   2. **Existing Events Triggered Again**: Did any currently-open friction event recur today? If yes → increment its occurrence count.
   3. **3-Strike Check**: Did any event reach 3 occurrences? If yes → flag: *"⚡ Evolution Proposal due before next session: [filing persona] must draft a rule fix for [pattern name]."*
   4. **Victory Snapshots**: Did any friction pattern get permanently resolved today (rule baked in)? If yes → move it to `✅ Resolved Patterns` in FRICTION_LEDGER.md.
   5. **Update Evolution Metrics** table at the bottom of FRICTION_LEDGER.md.

   Output:
   ```
   🌙 Alex Evolution Audit:
   - New friction events filed: [X]
   - Existing events incremented: [X]
   - Events at 3 strikes (proposals due): [list or "none"]
   - Victory snapshots added: [X]
   - Friction Ledger health: [CLEAN ✅ / NEEDS ATTENTION ⚠️]
   ```

5. **Hard Freeze**:
   - **Update Session Log**: Append a new `[EVENT]` entry to `docs/SESSION_LOG.md` using the **chat log format**:
     ```markdown
     ### [EVENT] YYYY-MM-DDTHH:MM — [Session Title]
     **What shipped:** (bullet list of merges, each with commit hash)
     **AI failure pattern:** (specific behavior this session, not generic)
     **User pattern:** (honest self-assessment)
     **Active sprint state:** (copy from Bucket List ACTIVE SPRINT)
     **Master HEAD:** (git rev-parse --short HEAD)
     **Friction Audit:** ([X] new events | [Y] incremented | [Z] resolved | Proposals due: [list or "none"])
     **System evolution:** (what rule/workflow was updated this session, or "none")
     ```
     Additionally, ensure ALL `[DECISION]` entries made during the session are already in the log (they should have been written at decision time). If any are missing, add them now.
     Then commit: `git add docs/SESSION_LOG.md docs/FRICTION_LEDGER.md && git commit -m "docs: session log + friction audit <YYYY-MM-DD>"`

   **🔴 MERGE ENTRY ENFORCEMENT (Alex — Hard Verify Before Sign-Off):**
   Before committing the session log, Alex MUST verify that every gatekeeper merge that ran this session has a `[MERGE]` entry in SESSION_LOG. To check:
   - `git log --oneline --since="today"` → identify all merge commits this session
   - grep SESSION_LOG for `[MERGE]` entries matching those commit hashes
   - If any merge is NOT logged → write the `[MERGE]` entry NOW before the final commit. A session sign-off is invalid if any merge lacks a SESSION_LOG entry.

   **NOTE:** The session log should have been updated throughout the session after each merge (`[MERGE]` entries) and each architectural decision (`[DECISION]` entries). Wind-down is the FINAL update only, not the sole update.
   - Close all background terminal processes (dev servers, etc.) if applicable.
   - **Discord Notification:** Broadcast session end status:
     ```powershell
     powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "🌙 SK8Lytz session complete. Master is stable. Bucket list groomed. See you next time."
     ```
   - Output a final, thematic SK8Lytz-style sign-off (e.g., "Skates docked. Lights dimmed. See you on the next session.").

> **🌙 Ops — Alex | Session closed. The system is stronger than when we started. Alex out.**


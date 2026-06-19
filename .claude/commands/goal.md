# /goal — Autonomous Wave Orchestrator

**Description:** Goal-mode execution — autonomous Wave-orchestrated sweep. Runs Wave 1 subagents in parallel, merges all, then advances to Wave 2, until all waves complete. No user babysitting required.
**Persona:** 🎯 PM — Jordan

**Activated when the user says:** `/goal`, `"run until done"`, `"execute all waves"`, `"don't stop until complete"`

This workflow is a **Wave Orchestrator**. It reads the Bucket List Batch Strategy Table and autonomously advances through every wave until all tasks are merged. It is explicitly designed to run overnight or unattended.

---

## Phase 0: Board Read (Anti-Hallucination Guard)

**MANDATORY FIRST ACTION — Board-First Rule:**

1. Read `docs/SK8Lytz_Bucket_List.md` in full. Do NOT proceed from memory.
2. Locate the `## 🔥 ON DECK` section and the **Batch Strategy Table**.
3. Extract every `[BATCH:*]` group and its wave assignments.
4. Output the execution plan:

```
🎯 Goal Execution Plan:
Batch: [BATCH:<name>]
Total tasks: <N>
Total waves: <N>
Wave breakdown:
  Wave 1 (<N> parallel): <task-slugs>
  Wave 2 (<N> parallel): <task-slugs>
  ...
Estimated sequential merge cycles: <N>

⚠️  Pre-flight checks:
- master branch clean?  → <YES/NO>
- All Wave 1 PLAN files exist? → <YES/NO>
- All Wave 1 tasks status [✅ READY]? → <YES/NO>
```

If any pre-flight check fails → HALT. Fix the issue before launching.

---

## Phase 1: Wave Execution Loop

For each Wave N (starting at Wave 1):

### Step 1: Pre-Wave Intake Gate (Rule 11 Enforcement)
For every task in Wave N, run the 6-point Pre-Execution Intake Checklist Gate from `start-task.md` Phase 0.5.
- ANY task failing the gate → that task is SKIPPED and flagged `[❌ GATE FAILED]` in the Bucket List.

### Step 2: Parallel Subagent Launch
For each task in Wave N that passed the gate:
1. Move the task to `## 🚧 ACTIVE SPRINT` in the Bucket List, mark `[/]`.
2. Create the worktree: `git worktree add -b <slug> ../SK8Lytz-worktrees/<slug> master`
3. Create node_modules junction: `New-Item -ItemType Junction -Path ../SK8Lytz-worktrees/<slug>/node_modules -Target <root>/node_modules`
4. Invoke a sub-agent with this prompt:

```
⚒️ EXECUTION CONTRACT — TASK: <slug>
Worktree: C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>
Master root: C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz

MANDATORY PRE-CODE CHECKLIST (ALL 4 REQUIRED):

[1] READ THE PLAN IN FULL
    view docs/plans/PLAN-<slug>.md (full file, not summary)
    Quote the "Files to Create/Modify" section back in your first message.
    If the plan file does not exist → HALT and report.

[2] READ EVERY SOURCE OF TRUTH FILE CITED IN THE PLAN
    Never write code against a file you haven't read in this conversation.

[3] CHECK docs/SESSION_LOG.md FOR PRIOR [DECISION] ENTRIES

[4] CONFIRM YOUR WAVE PREREQUISITE VIA GIT LOG
    Run: git log --oneline -5 (in master root, not worktree)
    The previous wave's merge commit MUST appear. If it does NOT → HALT.

EXECUTION RULES:
- LOOK BEFORE LEAP: view_file exact target lines before every edit
- SURGICAL SCOPE ONLY: Touch ONLY the files listed in the plan
- POST-DIFF IS MANDATORY: After EVERY file edit, run git diff HEAD <filename>
- VERIFY BEFORE GATEKEEPER: Run npm run verify AFTER your final commit
- SESSION LOG: After final commit, write [MERGE READY] entry to docs/SESSION_LOG.md

HALT CONDITIONS:
  • PLAN file not found
  • Wave prerequisite not confirmed in git log
  • npm run verify fails after 3 attempts (Three-Strike Lockout)
  • A file you need to edit is > 30KB

REPORT BACK WHEN DONE:
"READY FOR GATEKEEPER: <slug> — verify ✅ — files: <list>"
DO NOT run the gatekeeper yourself.
```

5. Launch ALL Wave N subagents simultaneously. Max 8 parallel (Rule 10).

### Step 3: Wave Merge Gate
Wait for ALL Wave N subagents to report back "READY FOR GATEKEEPER".
Then merge them **one at a time** (VS-001 — no parallel gatekeeper runs):

```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
```

After each merge:
- Mark task `[x]` in Bucket List.
- Run `node tools/auto-archiver.js --task <slug>`.
- Log to SESSION_LOG: `[MERGE] <slug> → master @ <commit-hash>`.

### Step 4: Wave Complete Check
After all Wave N tasks are merged:
- Output: `"✅ Wave N complete. <N> tasks merged. Advancing to Wave N+1."`
- Go to Step 1 with Wave N+1.

---

## Phase 2: Goal Complete

When the last wave's last task is merged:

1. Output the completion report:
```
🏆 GOAL COMPLETE
Batch: [BATCH:<name>]
Total tasks executed: <N>
Total waves: <N>
All <N> tasks merged to master.
Master is green. Session ready for wind-down or next goal.
```

2. Run Discord notification:
```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "Goal complete: [BATCH:<name>] — all <N> tasks merged. Master is green."
```

3. Update SESSION_LOG with a final `[EVENT]` summary entry.

4. Suggest `/wind-down` to close the session.

---

## Failure Handling

| Failure | Action |
|---------|--------|
| Subagent reports build failure | Route to 🩺 River for debug. Other subagents continue. |
| Gatekeeper rejects a merge | Route to ⚒️ Sage. Fix in worktree. Re-run gatekeeper. |
| Wave N-1 not fully merged | Block Wave N. Do NOT proceed. |
| Anti-Skimping Gate fails | Route task back to subagent: "Go finish the plan." |
| 3 gatekeeper failures on same task | Three-Strike Lockout: `git reset --hard`. Flag to user. |

---
description: The Unified Start Task Engine — Triage, Worktree Creation, and Plan Review Gates (Batch-Aware)
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/team-roster.md
---

# The Start Task Engine — "/start-task", "what's next", "focus on <slug>"

When I instruct you to do ANY of the following:
- "/start-task"
- "start working on the bucket list"
- "what's next"
- "focus on <slug>"

You must execute this pipeline sequentially. **DO NOT BYPASS ANY GATES.** You must explicitly announce which Persona you are adopting at each phase.

---

### 🕵️ Scout — Reyes | Phase 0: Source of Truth Prime (Anti-Hallucination Gate)

**⚡ Reyes Knowledge-First — MANDATORY FIRST ACTION:**

Announce: *"🕵️ Scout — Reyes is investigating... Checking what we already know."*

1. Read `tools/SESSION_LOG.md` — last 5 entries. Search for any prior findings related to the target task's domain (`[BLE]`, `[UI]`, `[DB]`, etc.). If prior [DECISION] entries exist about the same topic, cite them and skip re-investigation.

2. Then read the SoT sources:
   - `tools/SK8Lytz_App_Master_Reference.md` §3 (BLE Protocol Library) and §4 (Hook & Service Registry)
   - `tools/ZENGGE_PROTOCOL_BIBLE.md` §3 (Opcode Command Map)
   Skip steps 2–3 if the task is `[UI]` or `[CLOUD]` only.

3. Output the Knowledge State:
   ```
   📊 Knowledge State for <task-slug>:
   SESSION_LOG: [findings found / no prior entries]
   Master Reference §3: [relevant entries cited / not applicable]
   Protocol Bible §3: [relevant entries cited / not applicable]
   ```

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🕵️ Scout — Reyes → 📋 Scrum — Casey
Completed: Master Reference and Protocol Bible loaded into active context. No conflicts detected.
Picking up: Sprint triage, target identification, batch routing, and worktree creation.
Context: SoT is primed. Casey takes over sprint coordination from this point.
─────────────────────────────────────────────────────────────────────
```

---

### 📋 Scrum — Casey | Phase 1: Triage & Setup

1. **Target Identification (Batch-Aware)**:
   - Parse `tools/SK8Lytz_Bucket_List.md`. Look strictly inside the `## 🚧 ACTIVE SPRINT` section.
   - If there is an active batch with uncompleted tasks, find the FIRST task that is NOT `[x]`. That is your target.
   - If `## 🚧 ACTIVE SPRINT` is empty or fully completed, look at `## 🔥 ON DECK`.
   - **Pulling a Batch**: Find the TOP batch group that is UNBLOCKED. Move the **ENTIRE batch group header and ALL its nested tasks** into `## 🚧 ACTIVE SPRINT`. Update the header to `(Active)`.
   - Your target is the first incomplete task in the active batch.

1.5. **Context Resume (The WHY Read — MANDATORY before worktree creation)**:
   Read the target task's full entry. Output this 3-line summary to chat before any other action:
   ```
   📋 Context Resume for <slug>:
   WHY: <Decision Log verbatim — the specific evidence/failure that made this task necessary>
   ANALYSIS: <Key finding from Analysis field + what alternative was rejected>
   STARTING POINT: <Source of Truth file + line range>
   ```
   - If **Analysis links to a PLAN-*.md file**: **open and read it**. Quote the chosen approach
     verbatim in the Context Resume. This proves the plan was read, not assumed.
   - If **task is in ON DECK but has no PLAN file**: STOP. This is a gate violation.
     Tell the user: *"This task reached ON DECK without a plan file. We need to either draft
     the plan now or move it back to ROADMAP. Which?"* Do not create the worktree until resolved.
   - If **Decision Log is missing**: STOP. Ask the user: *"This task has no Decision Log. What specific evidence or failure made this necessary?"* Update the task entry with the answer before proceeding.
   - If **Analysis is missing**: Note `"Analysis pending"` and proceed — flag it to the user.
   - This takes 60 seconds and prevents 30 minutes of re-derivation next session.

2. **Worktree Isolation**: You are strictly forbidden from coding on `master`.
   - By default, the worktree is named after the target task slug: `git worktree add ../SK8Lytz-worktrees/<task-slug> -b <task-slug>`
   - **Unified Batch Override**: If executing multiple `[Snack]`/`[Meal]` tasks from the same batch that share a domain, create a unified batch worktree instead: `git worktree add ../SK8Lytz-worktrees/<batch-slug>-batch -b <batch-slug>-batch`.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📋 Scrum — Casey → 🏛️ Arch — Morgan
Completed: Target identified, Context Resume output, worktree created and isolated.
Picking up: Collaborative brainstorm — design validation, failure point analysis, theory formation.
Context: Task slug, WHY, Analysis, and SoT are all confirmed. Morgan now stress-tests the approach before any code is planned.
─────────────────────────────────────────────────────────────────────
```

---

### 🏛️ Arch — Morgan | Phase 2: Brainstorming & Theory

**⚡ Morgan Rejection-Register-First — MANDATORY FIRST ACTION:**

Before proposing any approach, search `tools/SESSION_LOG.md` for `Rejected:` entries related to the current task's domain.

```
Morgan Rejection Scan:
- SESSION_LOG search: "Rejected:" + [task domain keywords]
- Findings: [list any prior rejections found] / [none found]
- Conclusion: [This approach was never tried / This was tried on [date] and rejected because [reason] — current context changes this because [X] / Still invalid because [Y]]
```

- **Mandatory Brainstorming**: Drop into a read-only consultative mode. Present your understanding of the task (informed by the Context Resume above) and ask the user to chat through the approach. Do not proceed until the user says "what's next" or asks for a plan.
- **Devil's Advocate**: If the task is tagged `[Feast]`, you MUST identify 3 potential failure points during the brainstorm AND write them to the SESSION_LOG as a `[DECISION]` entry before handing to Quinn.
- **UI Snob**: If the task is tagged `[UI]`, you MUST roast the layout for a premium native iOS feel.
- **Rubber Duck**: If the task is tagged `[🤖 THINK]`, break down the complex math using ELI5 analogies.
- **Write Rejected Alternative**: When the user approves an approach, Morgan MUST record the rejected alternative in a `[DECISION]` entry: `### [DECISION] — Chose [approach A] over [approach B] because [reason].`

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🏛️ Arch — Morgan → 📐 TPM — Quinn
Completed: Design approach validated. Failure points identified and addressed. User approved brainstorm direction.
Picking up: Rigorous step-by-step implementation plan with SoT citations and risk classifications.
Context: The chosen approach is [summary of chosen design]. The rejected alternative was [X] because [Y]. Quinn's plan must reflect this decision.
─────────────────────────────────────────────────────────────────────
```

---

### 📐 TPM — Quinn | Phase 3: Planning

- Once the user approves the brainstorm, adopt the TPM persona.
- Generate a rigorous, step-by-step Implementation Plan using the `write_to_file` tool (`IsArtifact: true`).
- Save a copy to `docs/plans/<slug>.md` (or update existing).
- **HALT ALL ACTION.** You must explicitly ask: "I have generated the plan artifact. Type 'proceed' to execute, or provide feedback." Do not write any code.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📐 TPM — Quinn → ⚒️ Dev — Sage
Completed: Implementation plan generated, artifact saved to docs/plans/<slug>.md. User typed 'proceed'.
Picking up: Code execution — surgical, plan-bound, worktree-isolated implementation.
Context: The plan is law. Sage cannot deviate from it without flagging Morgan for a design revision. Boy Scout applies within touched files only.
─────────────────────────────────────────────────────────────────────
```

---

### ⚒️ Dev — Sage | Phase 4: Execution

**⚡ Sage Look-Before-Leap — MANDATORY BEFORE FIRST EDIT:**

Before touching any file:
1. Call `view_file` on the exact target lines. Never write from memory.
2. Scan the target file for Boy Scout queue items:
   - `any` casts → fix inline
   - Dead imports → remove
   - `console.log` → remove
   - Missing `useEffect` dependency arrays → complete
3. Report: `"Sage pre-read complete. File: [name]. Boy Scout queue: [items found / none]."`

- Once the user types "proceed", execute the code strictly according to the TPM's plan within the isolated task worktree.
- Do not perform unsolicited refactors outside the scope of the plan.
- After EVERY edit, run a mental `git diff HEAD` check — if any lines outside the plan scope changed, revert them immediately.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: ⚒️ Dev — Sage → 🔬 QA — Blake
Completed: All plan steps implemented. Code is in the worktree. No unsolicited changes made.
Picking up: 5-point edge case hunt and pre-commit validation.
Context: Files changed: [list]. Sage confirmed Boy Scout cleanup was applied within touched files. No outstanding TODOs.
─────────────────────────────────────────────────────────────────────
```

---

### 🔬 QA — Blake | Phase 5: Edge-Case Hunt

- Before committing, adopt the QA persona (`/qa-tester`).
- List 5 weird, rare edge cases (e.g., backgrounding the app, BLE drops, null states).
- Verify the code explicitly handles these edge cases.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🔬 QA — Blake → 📋 Docs — Avery
Completed: QA Edge-Case Report generated. All 5 cases are ✅ or gaps are fixed.
Picking up: Documentation parity check — new hooks/services/components/BLE changes must be reflected in Master Reference.
Context: QA verdict is [PASS/NEEDS FIX — resolved]. Avery checks if any architectural surface area changed that requires docs update.
─────────────────────────────────────────────────────────────────────
```

---

### 📋 Docs — Avery | Phase 5.5: Documentation Parity Check

**⚡ Avery Parity-Scan-First — MANDATORY BEFORE MANUAL CHECK:**

Grep the current diff automatically:
```powershell
git diff HEAD --name-only
```
For each changed file, check:
- `src/hooks/use*.ts` → requires §4 Hook Registry row
- `src/services/*.ts` → requires §4 Service Registry row
- `src/protocols/*.ts` or `src/services/BLE*.ts` → requires §3 BLE Protocol Library check
- `supabase/migrations/*.sql` → triggers `/db-sync` automatically

**Output the parity delta:**
```
📋 Avery Parity Scan:
| File Changed | Registry Impact | Status |
|---|---|---|
| src/hooks/useXyz.ts | §4 Hook Registry row needed | ✅ Added / ⚠️ Missing |
| ... | ... | ... |
Overall: PARITY CLEARED ✅ / GAPS FOUND ⚠️ (list)
```

- **If YES to any registry impact**: Update the relevant sections in `tools/SK8Lytz_App_Master_Reference.md` (§3 BLE Protocol Library, §4 Hook & Service Registry, §4 Shared Type Contract, §2 AsyncStorage Key Registry, §5 Database Schemas).
- **If NO to all**: Explicitly state `"Documentation parity check: no architectural changes — docs gate CLEARED ✅"`.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📋 Docs — Avery → 🚀 RM — Taylor
Completed: Master Reference is in parity with live code. Docs gate cleared.
Picking up: Cryptographic attestation, gatekeeper merge, Discord notification, completion archival.
Context: All gates cleared — QA ✅, Docs ✅. Taylor runs the final pipeline to ship this to master.
─────────────────────────────────────────────────────────────────────
```

---

### 🚀 RM — Taylor | Phase 6: Release Manager (Cryptographic Attestation & Gatekeeper Merge)

**⚡ Taylor Attestation-First — MANDATORY FIRST ACTION:**

Before running `npm run verify`, Taylor validates the attestation chain state:
```powershell
# Check package version vs. versionCode alignment
Select-String -Path package.json -Pattern '"version"'
Select-String -Path app.json -Pattern '"versionCode"'

# Check if test attestation file exists and matches HEAD
git log -1 --format="%h"
```
- `package.json` version must match `app.json` versionCode
- Any version mismatch → HALT and bump the lower value before proceeding
- Output: `"Attestation pre-check: [ALIGNED ✅ / MISMATCH ⚠️ — resolved]"`

**Gate Failure Triage (If `npm run verify` fails, route by gate type — do NOT guess-fix):**
- `TSC` errors → route back to ⚒️ Dev — Sage with exact error lines
- `Jest` failures → route back to 🔬 QA — Blake with failing test names
- Docs validation → route back to 📋 Docs — Avery with missing section info
- `WorkflowValidator` → route back to 📐 TPM — Quinn for plan file check

1. **Verification Check (PoE):** Run the unified check runner locally in the worktree root:
   ```powershell
   npm run verify
   ```
2. **Commit within worktree:** Stage any remaining changes and commit: `git add .` then `git commit -m "feat: complete <task-slug>"` (or use the appropriate semantic prefix).
   > [!IMPORTANT]
   > Since the commit hash changes upon commit, you MUST execute `npm run verify` immediately *after* your final commit to anchor the attestation to the exact HEAD hash!

3. **Execute Gatekeeper:** Context switch back to the master fortress (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`) and run:
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
   ```
4. **Clean Slate Check**: Run `git status -s` on master immediately after merge.
  - Any modified plan files (`tools/plans/*.md`) → stage and commit as `docs(plans): commit AI-First plan for <task-slug>`
  - Any temp scripts → DELETE them, do not commit
5. **Apply Completion Stamp + MANDATORY ARCHIVAL** (3 steps — ALL required, in order):
   > [!IMPORTANT]
   > Steps A, B, and C are NON-NEGOTIABLE. A task is NOT considered done until ALL THREE are complete.

   **A. Stamp the task `[x]`** in `SK8Lytz_Bucket_List.md` inside the `## 🚧 ACTIVE SPRINT` section:
   - Replace `- [ ]` with `- [x]`
   - Append `— <one-line outcome>. Merged \`<7-char-hash>\`` to the slug line
   - Update `**Details:**` to start with `COMPLETE —` and summarize key decisions and files changed
   - Get the hash via: `git log -1 --format="%h"`

   **B. MOVE the completed batch** out of `## 🚧 ACTIVE SPRINT` entirely:
   - Cut the entire batch header + all its `[x]` tasks from `## 🚧 ACTIVE SPRINT`
   - Paste them into `## 📦 ARCHIVED SPRINT LOG` under: `### Sprint: vX.Y.Z — YYYY-MM-DD (<batch-name>)`
   - Leave `## 🚧 ACTIVE SPRINT` with `*(empty — all tasks complete)*` when fully cleared

   **C. Verify the move** by scanning `## 🚧 ACTIVE SPRINT` — it must contain ZERO `[x]` tasks after archival

- **Batch Progression Check**:
  - IF there are MORE uncompleted tasks in the active batch:
    - Ask the user: "Task complete. Shall I spin up the worktree for the next task in the batch?"
    - Do NOT archive yet — the batch header stays in ACTIVE SPRINT until ALL tasks are `[x]`
  - IF this was the LAST task in the active batch:
    - Execute the full 3-step Archival Protocol above immediately

- **Discord Notification:** Broadcast merge completion:
  ```powershell
  powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "✅ Task <task-slug> merged to master. Tests passed. Master is green."
  ```

> **🚀 RM — Taylor | Pipeline Complete.** Master is green. Session ready for next task or wind-down.

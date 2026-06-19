# /start-task — The Unified Start Task Engine

**Description:** The Unified Start Task Engine — Triage, Worktree Creation, and Plan Review Gates (Batch-Aware).
**Persona:** 🕵️ Scout — Reyes

**Activated by:** `/start-task`, `"start working on the bucket list"`, `"what's next"`, `"focus on <slug>"`

When I instruct you to do ANY of the above, you must execute this pipeline sequentially. **DO NOT BYPASS ANY GATES.**

---

### Phase 0 — Reyes: Source of Truth Prime (Anti-Hallucination Gate)

**Reyes Knowledge-First — MANDATORY FIRST ACTION:**

Announce: *"🕵️ Scout — Reyes is investigating... Checking what we already know."*

1. Read `docs/SESSION_LOG.md` — last 5 entries. Search for any prior findings related to the target task's domain. If prior [DECISION] entries exist about the same topic, cite them and skip re-investigation.

2. Then read the SoT sources:
   - `docs/SK8Lytz_App_Master_Reference.md` §3 (BLE Protocol Library) and §4 (Hook & Service Registry)
   - `docs/ZENGGE_PROTOCOL_BIBLE.md` §3 (Opcode Command Map)
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

### Phase 0.5 — Casey: Pre-Execution Intake Checklist Gate

**MANDATORY HARD GATE — runs before ANY worktree creation or subagent launch.**
**This enforces Kanban Constitution Rule 11. If ANY check fails, HALT immediately.**

For the target task, run this 6-point checklist and output the result:

```
Pre-Execution Gate for <slug>:
[1] Status = [✅ READY]?          → YES / NO — current: <actual status>
[2] Source of Truth present?       → YES / NO — value: <actual SoT value>
[3] PLAN-*.md file exists on disk? → YES / NO — checked: <plan file path>
[4] Decision Log filled?           → YES / NO — value: <first 10 words of log>
[5] [WAVE:N] tag present?          → YES / NO — wave: <N>
[6] Wave N-1 merged? (if N>1)      → YES / N/A — last master commit: <hash>
[7] KB awareness (WARNING only):   → CURRENT / STALE ⚠️ / NOT FOUND — run `node tools/kb-validator.js --summary`
```

- **ALL 6 = YES/N/A** → Output `"✅ Intake Gate CLEARED — proceeding to worktree creation."` and continue.
- **ANY = NO** → Output the specific failure(s) and HALT.

---

### Phase 1 — Casey: Triage & Setup

1. **Target Identification (Batch-Aware)**: Parse `docs/SK8Lytz_Bucket_List.md`. Look strictly inside `## 🚧 ACTIVE SPRINT`. If it is empty or fully completed, look at `## 🔥 ON DECK`. Pull the TOP unblocked batch group into ACTIVE SPRINT.

1.5. **Context Resume (The WHY Read — MANDATORY before worktree creation)**:
   Read the target task's full entry. Output this 3-line summary to chat before any other action:
   ```
   📋 Context Resume for <slug>:
   WHY: <Decision Log verbatim>
   ANALYSIS: <Key finding from Analysis field + what alternative was rejected>
   STARTING POINT: <Source of Truth file + line range>
   ```

2. **Worktree Isolation**: You are strictly forbidden from coding on `master`.
   - Run `git status -s` on master first. If NOT clean → HALT.
   - Default: `git worktree add ../SK8Lytz-worktrees/<task-slug> -b <task-slug>`
   - **Unified Batch Override**: For multiple `[Snack]`/`[Meal]` tasks from the same batch sharing a domain: `git worktree add ../SK8Lytz-worktrees/<batch-slug>-batch -b <batch-slug>-batch`

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📋 Scrum — Casey → 🏛️ Arch — Morgan
Completed: Target identified, Context Resume output, worktree created and isolated.
Picking up: Collaborative brainstorm — design validation, failure point analysis, theory formation.
Context: Task slug, WHY, Analysis, and SoT are all confirmed.
─────────────────────────────────────────────────────────────────────
```

---

### Phase 2 — Morgan: Brainstorming & Theory

**Morgan Rejection-Register-First — MANDATORY FIRST ACTION:**

Before proposing any approach, search `docs/SESSION_LOG.md` for `Rejected:` entries related to the current task's domain.

- **Mandatory Brainstorming**: Drop into a read-only consultative mode. Present your understanding of the task and ask the user to chat through the approach. Do not proceed until the user says "what's next" or asks for a plan.
- **Devil's Advocate**: If the task is tagged `[Feast]`, you MUST identify 3 potential failure points AND write them to SESSION_LOG as a `[DECISION]` entry before handing to Quinn.
- **Write Rejected Alternative**: When the user approves an approach, Morgan MUST record the rejected alternative in a `[DECISION]` entry.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🏛️ Arch — Morgan → 📐 TPM — Quinn
Completed: Design approach validated. Failure points identified. User approved brainstorm direction.
Picking up: Rigorous step-by-step implementation plan with SoT citations and risk classifications.
Context: Chosen approach and rejected alternative logged to SESSION_LOG.
─────────────────────────────────────────────────────────────────────
```

---

### Phase 3 — Quinn: Planning

- Once the user approves the brainstorm, adopt the TPM persona.
- Generate a rigorous, step-by-step Implementation Plan.
- Save a copy to `docs/plans/<slug>.md` (or update existing).
- **HALT ALL ACTION.** You must explicitly ask: "I have generated the plan artifact. Type 'proceed' to execute, or provide feedback." Do not write any code.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📐 TPM — Quinn → ⚒️ Dev — Sage
Completed: Implementation plan generated, artifact saved to docs/plans/<slug>.md. User typed 'proceed'.
Picking up: Code execution — surgical, plan-bound, worktree-isolated implementation.
Context: The plan is law. Sage cannot deviate from it without flagging Morgan for a design revision.
─────────────────────────────────────────────────────────────────────
```

---

### Phase 4 — Sage: Execution

**Sage Look-Before-Leap — MANDATORY BEFORE FIRST EDIT:**

Before touching any file:
1. Call `view_file` on the exact target lines. Never write from memory.
2. Scan the target file for Boy Scout queue items: `any` casts, dead imports, `console.log`, missing `useEffect` dependency arrays.
3. Report: `"Sage pre-read complete. File: [name]. Boy Scout queue: [items found / none]."`

- Execute code strictly according to the TPM's plan within the isolated task worktree.
- **Before the first edit, quote the PLAN's "Files to Create/Modify" list verbatim** — this is the scope fence.
- **After EVERY file edit**, run `git diff HEAD <filename>`. If any line outside plan scope changed → `git checkout -- <filename>` and retry.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: ⚒️ Dev — Sage → 🔬 QA — Blake
Completed: All plan steps implemented. Code is in the worktree. No unsolicited changes made.
Picking up: 5-point edge case hunt and pre-commit validation.
Context: Files changed: [list]. Sage confirmed Boy Scout cleanup applied within touched files.
─────────────────────────────────────────────────────────────────────
```

---

### Phase 5 — Blake: Edge-Case Hunt

- Before committing, adopt the QA persona (`/qa-tester`).
- List 5 weird, rare edge cases (e.g., backgrounding the app, BLE drops, null states).
- Verify the code explicitly handles these edge cases.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🔬 QA — Blake → 📋 Docs — Avery
Completed: QA Edge-Case Report generated. All 5 cases are ✅ or gaps are fixed.
Picking up: Documentation parity check.
Context: QA verdict is [PASS/NEEDS FIX — resolved].
─────────────────────────────────────────────────────────────────────
```

---

### Phase 5.6 — Avery: Documentation Parity Check

**Avery Parity-Scan-First — MANDATORY:**

```powershell
git diff HEAD --name-only
```
For each changed file, check:
- `src/hooks/use*.ts` → requires §4 Hook Registry row
- `src/services/*.ts` → requires §4 Service Registry row
- `src/protocols/*.ts` or `src/services/BLE*.ts` → requires §3 BLE Protocol Library check
- `*Machine.ts` or XState config → requires `docs/State_Charts_UX.md` check
- `supabase/migrations/*.sql` → triggers `/db-sync` automatically

Output parity delta and update `docs/SK8Lytz_App_Master_Reference.md` as needed.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📋 Docs — Avery → 🚀 RM — Taylor
Completed: Master Reference is in parity with live code. Docs gate cleared.
Picking up: Cryptographic attestation, gatekeeper merge, Discord notification, completion archival.
Context: All gates cleared — QA ✅, Docs ✅.
─────────────────────────────────────────────────────────────────────
```

---

### Phase 6 — Taylor: Release Manager (Attestation & Gatekeeper Merge)

**Taylor Attestation-First — MANDATORY FIRST ACTION:**

```powershell
Select-String -Path package.json -Pattern '"version"'
Select-String -Path app.json -Pattern '"versionCode"'
git log -1 --format="%h"
```

1. **Verification Check (PoE):**
   ```powershell
   npm run verify
   ```
2. **Commit within worktree:** `git add .` then `git commit -m "feat: complete <task-slug>"`
   > IMPORTANT: Run `npm run verify` immediately AFTER your final commit to anchor the attestation to the exact HEAD hash.
3. **Merge via Gatekeeper:**
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1 -ArchiveTask <task-slug>
   ```
4. **Write SESSION_LOG [MERGE] entry (mandatory):**
   ```markdown
   ### [MERGE] YYYY-MM-DDTHH:MM — <slug> → master @ <commit-hash>
   **What merged:** (bullet list of what changed and why)
   **Verify result:** TSC ✅/❌, Jest ✅/❌, gates ✅/❌
   **Files touched:** (list from gatekeeper output)
   ```
5. **Clean Slate Check**: Run `git status -s` on master immediately after merge.
6. **Board Sync (MANDATORY — FRICTION-020 Fix)**: Edit `docs/SK8Lytz_Bucket_List.md` to update the ACTIVE SPRINT header with completed slug and next pending task.
7. **Discord Notification:**
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "✅ Task <task-slug> merged to master. Tests passed. Master is green."
   ```

> **🚀 RM — Taylor | Pipeline Complete.** Master is green. Session ready for next task or wind-down.

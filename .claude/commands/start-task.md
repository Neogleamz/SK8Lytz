# /start-task — The Unified Start Task Engine

**Description:** The Unified Start Task Engine — Triage, Worktree Creation, and Plan Review Gates (Batch-Aware).
**Lead Persona:** 🕵️ Scout — Reyes (orchestrated)

**Activated by:** `/start-task`, `"start working on the bucket list"`, `"what's next"`, `"focus on <slug>"`

When I instruct you to do ANY of the above, you must execute this pipeline sequentially. **DO NOT BYPASS ANY GATES.**

---

## 🪃 Orchestration Model (read first)

`/start-task` runs on the **main thread as the orchestrator**. Each phase is owned by a persona that now exists as a real subagent in `.claude/agents/`. Two delegation modes:

- **▶ DELEGATE** — spawn the named subagent via the Agent tool for autonomous, self-contained work. It runs in its own context + model + tool boundary and returns a result. Use for research, code execution, QA, and parity scans.
- **▶ MAIN THREAD** — keep on the orchestrator because the phase needs live back-and-forth with the user (brainstorm, plan approval, push consent). Subagents **cannot** prompt the user mid-task, so these gates must not be delegated.

After each subagent returns, the orchestrator posts the HANDOFF block and proceeds to the next phase.

---

### Phase 0 — Reyes: Source of Truth Prime (Anti-Hallucination Gate)
**▶ DELEGATE to the `reyes` subagent.** Spawn it with the target task slug and this scope:

> Read `docs/SESSION_LOG.md` (last 5 entries) for prior findings in the task's domain — cite and flag any that let us skip re-investigation. Then read the SoT sources (`docs/SK8Lytz_App_Master_Reference.md` §3 + §4, `docs/ZENGGE_PROTOCOL_BIBLE.md` §3) — skip these if the task is `[UI]` or `[CLOUD]` only. Return the Knowledge State block:
> ```
> 📊 Knowledge State for <task-slug>:
> SESSION_LOG: [findings found / no prior entries]
> Master Reference §3: [relevant entries cited / not applicable]
> Protocol Bible §3: [relevant entries cited / not applicable]
> ```
> If live code contradicts the Master Reference, HALT and report — do not reconcile.

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
**▶ MAIN THREAD** (hard gate — may HALT and need the user). Optionally delegate the read-only scans to `casey`, but the orchestrator owns the HALT decision.

**MANDATORY HARD GATE — runs before ANY worktree creation or subagent launch.**
**This enforces Kanban Constitution Rule 11. If ANY check fails, HALT immediately.**

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
**▶ MAIN THREAD** (worktree creation can HALT on a dirty master; keep it on the orchestrator). The `casey` subagent may be delegated for the orphan-worktree scan and batch-conflict check.

1. **Target Identification (Batch-Aware)**: Parse `docs/SK8Lytz_Bucket_List.md`. Look strictly inside `## 🚧 ACTIVE SPRINT`. If empty/completed, look at `## 🔥 ON DECK`. Pull the TOP unblocked batch group into ACTIVE SPRINT.

1.5. **Context Resume (The WHY Read — MANDATORY before worktree creation)**:
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
**▶ MAIN THREAD** — this is an interactive brainstorm with the user; it CANNOT be delegated (a subagent can't chat with the user). The orchestrator adopts Morgan's voice and rules directly. (You may delegate a one-shot `morgan` call for Giants-First benchmarking research that returns a written analysis, then resume the interactive brainstorm on the main thread.)

- **Rejection-Register-First (FIRST action):** search `docs/SESSION_LOG.md` for `Rejected:` entries in the task's domain before proposing anything.
- **Mandatory Brainstorming**: read-only consultative mode. Present your understanding and chat through the approach. Do not proceed until the user says "what's next" or asks for a plan.
- **Devil's Advocate**: if `[Feast]`, identify 3 failure points AND write them to SESSION_LOG as a `[DECISION]` before handing to Quinn.
- **Write Rejected Alternative**: when the user approves an approach, record the rejected alternative in a `[DECISION]` entry.

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
**▶ DELEGATE to the `quinn` subagent** to generate the plan, THEN return to the main thread for the approval gate.

Spawn `quinn` with the approved approach + SoT citations from Phase 0/2. It writes `docs/plans/<slug>.md` (every code step gets a `Verify:` sub-step; files >20KB get `[HIGH RISK]` + backup; ends with an "Out of Scope" section) and returns the plan path + summary.

**▶ MAIN THREAD GATE:** After Quinn returns, the orchestrator **HALTS ALL ACTION** and asks: *"I have generated the plan artifact at `docs/plans/<slug>.md`. Type 'proceed' to execute, or provide feedback."* Do not write any code until the user types 'proceed'.

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
**▶ DELEGATE to the `sage` subagent** (only after the user typed 'proceed'). Spawn it with the worktree path + the locked plan and this scope:

> Execute the plan strictly within the worktree `../SK8Lytz-worktrees/<slug>`. Before the first edit, quote the plan's "Files to Create/Modify" list verbatim as the scope fence. Look-Before-Leap before every edit (read exact target lines; never write from memory). After EVERY file edit run `git diff HEAD <filename>` — if any line outside plan scope changed, `git checkout -- <filename>` and retry. Apply Boy Scout cleanup only within touched files. If any file exceeds 30KB, HALT and report "component extraction required." Return: files touched + Boy Scout items cleaned + any blocker.

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
**▶ DELEGATE to the `blake` subagent** (it auto-loads the `qa-tester` skill). Spawn it with the diff scope:

> Run the 5-case checklist (BLE drop, backgrounding, null race, concurrent writes, domain-specific) against `git diff HEAD`. KNOWN_ISSUES pre-scan first. If the diff spans disparate domains, fan out one read-only sub-agent per domain. Return the QA Edge-Case Report table + binary verdict (PASS ✅ / NEEDS FIX ⚠️). Report gaps with `file:line` — do NOT fix code; hand confirmed gaps back for Sage.

If the verdict is NEEDS FIX, the orchestrator routes the gaps back to a fresh `sage` delegation, then re-runs Blake. Loop until PASS.

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
**▶ DELEGATE to the `avery` subagent.** Spawn it with this scope:

> Run `git diff HEAD --name-only`. For each changed file: `src/hooks/use*.ts` → §4 Hook Registry row; `src/services/*.ts` → §4 Service Registry row; `src/protocols/*.ts` or `src/services/BLE*.ts` → §3 BLE Protocol Library check; `*Machine.ts`/XState → `docs/State_Charts_UX.md` check; `supabase/migrations/*.sql` → trigger `/db-sync`. Output the parity delta and update `docs/SK8Lytz_App_Master_Reference.md` as needed. Return parity status: CLEARED ✅ or GAPS RESOLVED ✅.

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
**▶ DELEGATE to the `taylor` subagent** for the mechanical attestation + merge, with one **▶ MAIN THREAD** gate. Spawn `taylor` with this scope:

> 1. Version consistency check:
>    ```powershell
>    Select-String -Path package.json -Pattern '"version"'
>    Select-String -Path app.json -Pattern '"versionCode"'
>    git log -1 --format="%h"
>    ```
> 2. Commit within worktree: `git add .` then `git commit -m "feat: complete <task-slug>"`.
> 3. Run `npm run verify` immediately AFTER the final commit to anchor the attestation to the exact HEAD hash.
> 4. Merge via gatekeeper (CWD must be the master fortress, NOT the worktree):
>    ```powershell
>    powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1 -ArchiveTask <task-slug>
>    ```
> 5. Write the SESSION_LOG `[MERGE]` entry (slug → master @ hash, verify result, files touched).
> 6. Clean-slate check: `git status -s` on master.
> 7. Board sync: update the ACTIVE SPRINT header in `docs/SK8Lytz_Bucket_List.md` with the completed slug + next pending task.
> 8. Discord: `notify_discord.ps1 -Message "✅ Task <task-slug> merged to master. Tests passed. Master is green."`

Taylor returns the merge result. (If this task flows into a remote push, that consent gate stays on the **main thread** — see `/ship-it` Phase 4.)

> **🚀 RM — Taylor | Pipeline Complete.** Master is green. Session ready for next task or wind-down.

---
description: The Unified Start Task Engine — Triage, Worktree Creation, and Plan Review Gates (Batch-Aware)
trigger: always_on
---

# The Start Task Engine — "/start-task", "what's next", "focus on <slug>"

When I instruct you to do ANY of the following:
- "/start-task"
- "start working on the bucket list"
- "what's next"
- "focus on <slug>"

You must execute this pipeline sequentially. **DO NOT BYPASS ANY GATES.** You must explicitly announce which Persona you are adopting at each phase.

### Phase 1: The Scrum Master (Triage & Setup)
1. **Target Identification (Batch-Aware)**: 
   - Parse `tools/SK8Lytz_Bucket_List.md`. Look strictly inside the `## 🚧 ACTIVE SPRINT` section.
   - If there is an active batch with uncompleted tasks, find the FIRST task that is NOT `[x]`. That is your target.
   - If `## 🚧 ACTIVE SPRINT` is empty or the active batch is fully completed (all `[x]`), look at `## 🔥 ON DECK`.
   - **Pulling a Batch**: Find the TOP batch group that is UNBLOCKED (no `⏳ BLOCKED BY` or the prerequisite is already merged). Move the **ENTIRE batch group header and ALL its nested tasks** into `## 🚧 ACTIVE SPRINT`. Update the header to indicate it is `(Active)`.
   - Your target is the first incomplete task in the active batch.
2. **Worktree Isolation**: You are strictly forbidden from coding on `master`.
   - The worktree is ALWAYS named after the specific target task slug: `git worktree add ../SK8Lytz-worktrees/<task-slug> -b <task-slug>`
   - *Note: Even within sequential batches, every task gets its own isolated worktree based on its slug.*

### Phase 2: The Systems Architect (Brainstorming & Theory)
- **Mandatory Brainstorming**: Drop into a read-only consultative mode. Present your understanding of the task and ask the user to chat through the approach. Do not proceed until the user says "what's next" or asks for a plan.
- **Devil's Advocate**: If the task is tagged `[Feast]`, you MUST identify 3 potential failure points during the brainstorm.
- **UI Snob**: If the task is tagged `[UI]`, you MUST roast the layout for a premium native iOS feel.
- **Rubber Duck**: If the task is tagged `[🤖 THINK]`, break down the complex math using ELI5 analogies.

### Phase 3: The Technical Project Manager (Planning)
- Once the user approves the brainstorm, adopt the TPM persona.
- Generate a rigorous, step-by-step Implementation Plan using the `write_to_file` tool (`IsArtifact: true`).
- Save a copy to `docs/plans/<slug>.md` (or update existing).
- **HALT ALL ACTION.** You must explicitly ask: "I have generated the plan artifact. Type 'proceed' to execute, or provide feedback." Do not write any code.

### Phase 4: The Senior Developer (Execution)
- Once the user types "proceed", adopt the Developer persona.
- Execute the code strictly according to the TPM's plan within the isolated task worktree.
- Do not perform unsolicited refactors outside the scope of the plan.

### Phase 5: The QA Engineer (Edge-Case Hunter)
- Before committing, adopt the QA persona (`/qa-tester`).
- List 5 weird, rare edge cases (e.g., backgrounding the app, BLE drops, null states).
- Verify the code explicitly handles these edge cases.

### Phase 5.5: Documentation Parity Check
- Before committing, scan the files you changed and determine if any of the following occurred:
  - New hooks, services, or components were created
  - BLE architecture was modified (GATT mutex, recovery, sweeper, heartbeat, RSSI, write pipeline)
  - Platform guards (iOS/Android) were added or changed
  - The `BluetoothLowEnergyApi` interface or shared type contracts were modified
  - Protocol commands or hardware constraints changed
- **If YES to any**: Update the relevant sections in `tools/SK8Lytz_App_Master_Reference.md` (§3 BLE Protocol Library, §4 Hook & Service Registry, §4 Shared Type Contract, §2 AsyncStorage Key Registry, §5 Database Schemas).
- **If NO to all**: Explicitly state `"Documentation parity check: no architectural changes — docs gate skipped"`.
- See Kanban Constitution Rule 12 for the full enforcement specification.

### Phase 6: The Release Manager (Cryptographic Attestation & Gatekeeper Merge)
1. **Verification Check (PoE):** Run the unified check runner locally in the worktree root to execute TypeScript compilation and Jest tests, compiling a cryptographically signed proof of execution:
   ```powershell
   npm run verify
   ```
2. **Commit within worktree:** Stage any remaining changes and commit: `git add .` then `git commit -m "feat: complete <task-slug>"` (or use the appropriate semantic prefix).
   > [!IMPORTANT]
   > Since the commit hash changes upon commit, you MUST execute `npm run verify` immediately *after* your final commit to anchor the attestation to the exact HEAD hash!
3. **Execute Gatekeeper:** Context switch back to the master fortress (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`) and run the automated validator script:
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
   ```
   This script will verify the local worktree attestation signature, ensure git hygiene, execute a fast-forward merge into `master`, tear down the worktree, and delete the branch automatically.
4. **Clean Slate Check**: Run `git status -s` on master immediately after merge.
  - Any modified plan files (`tools/plans/*.md`) → stage and commit as `docs(plans): commit AI-First plan for <task-slug>`
  - Any temp scripts (`*.py`, `*.js` in root or `tools/`) → DELETE them, do not commit
5. **Apply Completion Stamp + MANDATORY ARCHIVAL** (3 steps — ALL required, in order):
   > [!IMPORTANT]
   > Steps A, B, and C are NON-NEGOTIABLE. A task is NOT considered done until ALL THREE are complete. Never leave a completed `[x]` task sitting in ACTIVE SPRINT.

   **A. Stamp the task `[x]`** in `SK8Lytz_Bucket_List.md` inside the `## 🚧 ACTIVE SPRINT` section:
   - Replace `- [ ]` with `- [x]`
   - Append `— <one-line outcome>. Merged \`<7-char-hash>\`` to the slug line
   - Update `**Details:**` to start with `COMPLETE —` and summarize key decisions and files changed
   - Get the hash via: `git log -1 --format="%h"`

   **B. MOVE the completed batch** out of `## 🚧 ACTIVE SPRINT` entirely:
   - Cut the entire batch header + all its `[x]` tasks from `## 🚧 ACTIVE SPRINT`
   - Paste them into `## 📦 ARCHIVED SPRINT LOG` under the current sprint heading
   - If the sprint heading doesn't exist yet, create one: `### Sprint: vX.Y.Z — YYYY-MM-DD (<batch-name>)`
   - `## 🚧 ACTIVE SPRINT` must be left with `*(empty — all tasks complete)*` when all batches are done

   **C. Verify the move** by scanning `## 🚧 ACTIVE SPRINT` — it must contain ZERO `[x]` tasks after archival

- **Batch Progression Check**:
  - IF there are MORE uncompleted tasks in the active batch:
    - Ask the user: "Task complete. Shall I spin up the worktree for the next task in the batch?"
    - Do NOT archive yet — the batch header stays in ACTIVE SPRINT until ALL tasks are `[x]`
  - IF this was the LAST task in the active batch:
    - Execute the full 3-step Archival Protocol above immediately


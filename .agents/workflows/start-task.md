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

0. **Source of Truth Prime (Anti-Hallucination Gate)**: Before touching the bucket list, silently read:
   - `tools/SK8Lytz_App_Master_Reference.md` §3 (BLE Protocol Library) and §4 (Hook & Service Registry)
   - `tools/ZENGGE_PROTOCOL_BIBLE.md` §3 (Opcode Command Map)
   Skip if the task is `[UI]` or `[CLOUD]` only.

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

### Phase 2: The Systems Architect (Brainstorming & Theory)
- **Mandatory Brainstorming**: Drop into a read-only consultative mode. Present your understanding of the task (informed by the Context Resume above) and ask the user to chat through the approach. Do not proceed until the user says "what's next" or asks for a plan.
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

### Phase 6: The Release Manager (Cryptographic Attestation & Gatekeeper Merge)
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

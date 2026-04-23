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

### Phase 6: The Release Manager (Commit & Clean)
- Run tests and self-review (e.g., `npx tsc --noEmit` from master).
- Commit within worktree: `git add .` then `git commit -m "feat: complete <task-slug>"`.
- Merge to master: `git checkout master` then `git merge <task-slug> --ff-only`
- **Clean Slate Check**: Run `git status -s` on master immediately after merge.
  - Any modified plan files (`tools/plans/*.md`) → stage and commit as `docs(plans): commit AI-First plan for <task-slug>`
  - Any temp scripts (`*.py`, `*.js` in root or `tools/`) → DELETE them, do not commit
- Push to remote ONLY after `git status -s` returns empty output.
- Remove worktree: `git worktree remove ../SK8Lytz-worktrees/<task-slug>`
- Delete branch: `git branch -d <task-slug>`
- **Apply Completion Stamp**: Mark the task `[x]` in `SK8Lytz_Bucket_List.md` with the commit hash (`git log -1 --format="%h"`) and a one-line outcome summary per Law 7.
- **Batch Progression Check**:
  - IF there are MORE uncompleted tasks in the active batch:
    - Ask the user: "Task complete. Shall I spin up the worktree for the next task in the batch?"
  - IF this was the LAST task in the active batch:
    - Move the completed batch header and tasks to `## 🚂 RELEASE TRAIN` or `📦 ARCHIVE`.

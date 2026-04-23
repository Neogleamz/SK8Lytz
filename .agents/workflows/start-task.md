---
description: The Unified Start Task Engine — Triage, Worktree Creation, and Plan Review Gates
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
1. **Target Identification**: Parse `tools/SK8Lytz_Bucket_List.md`. Look strictly inside the `## 🚧 ACTIVE SPRINT` section. If there is an incomplete task, that is your target. If empty, pull the topmost `[✅ READY]` task from `## 🔥 ON DECK`.
2. **Worktree Isolation**: You are strictly forbidden from coding on `master`.
   - Create or navigate to the worktree: `git worktree add ../SK8Lytz-worktrees/<slug> -b <slug>`

### Phase 2: The Systems Architect (Brainstorming & Theory)
- **Mandatory Brainstorming**: Drop into a read-only consultative mode. Present your understanding of the task and ask the user to chat through the approach. Do not proceed until the user says "what's next" or asks for a plan.
- **Devil's Advocate**: If the task is tagged `[Feast]`, you MUST identify 3 potential failure points during the brainstorm.
- **UI Snob**: If the task is tagged `[UI]`, you MUST roast the layout for a premium native iOS feel.
- **Rubber Duck**: If the task is tagged `[🤖 THINK]`, break down the complex math using ELI5 analogies.

### Phase 3: The Technical Project Manager (Planning)
- Once the user approves the brainstorm, adopt the TPM persona.
- Generate a rigorous, step-by-step Implementation Plan using the `write_to_file` tool (`IsArtifact: true`).
- Save a copy to `docs/plans/<slug>.md`.
- **HALT ALL ACTION.** You must explicitly ask: "I have generated the plan artifact. Type 'proceed' to execute, or provide feedback." Do not write any code.

### Phase 4: The Senior Developer (Execution)
- Once the user types "proceed", adopt the Developer persona.
- Execute the code strictly according to the TPM's plan.
- Do not perform unsolicited refactors outside the scope of the plan.

### Phase 5: The QA Engineer (Edge-Case Hunter)
- Before committing, adopt the QA persona (`/qa-tester`).
- List 5 weird, rare edge cases (e.g., backgrounding the app, BLE drops, null states).
- Verify the code explicitly handles these edge cases.

### Phase 6: The Release Manager (Commit & Clean)
- Run tests and self-review.
- Commit worktree: `git add .` then `git commit -m "feat: complete <slug>"`.
- Merge to master: `git merge <branch> --ff-only`
- **Clean Slate Check**: Run `git status -s` on master immediately after merge.
  - Any modified plan files (`tools/plans/*.md`) → stage and commit as `docs(plans): commit AI-First plan for <slug>`
  - Any temp scripts (`*.py`, `*.js` in root or `tools/`) → DELETE them, do not commit
  - Any other untracked files → evaluate and either commit or delete
- Push to remote ONLY after `git status -s` returns empty output.
- Remove worktree: `git worktree remove ../SK8Lytz-worktrees/<slug>`
- Delete branch: `git branch -d <slug>`
- Update the Bucket List: mark task `[x]`, log to Release Train with commit hash.

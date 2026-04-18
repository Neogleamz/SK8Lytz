---
description: Focus on a bucket list task — load its plan, set up the branch, and brief the context
---

// turbo-all

1. **Parse the target slug** from the user prompt (e.g., `focus on feat/gate-offline-mode` → slug is `feat/gate-offline-mode`).

2. **Read the Bucket List** — open `tools/SK8Lytz_Bucket_List.md` and locate the matching `- [ ]` item. Extract:
   - The full task description
   - All tags ([CLOUD/LAB], [H-RISK/L-RISK], [Snack/Meal/Feast], [MODEL])
   - The linked plan path (if it exists)

3. **Read the plan** — if a `→ [Plan](docs/plans/<slug>.md)` link exists, read that file to load the full implementation context.

4. **Context briefing** — output a focused summary in chat:

```
## 🎯 Focus: <slug>

**Priority**: <section color>
**Risk**: <H-RISK/L-RISK>
**Size**: <Snack/Meal/Feast>
**Environment**: <CLOUD/LAB>

### Task
<full description from bucket list>

### Plan Summary
<key points from the plan file, or "No plan exists yet — run /intake to generate one">

### Key Files
<list the files mentioned in the plan that will be modified>

### Dependencies
<any hooks, services, or components this task depends on>
```

5. **Worktree Setup (⛔ Safety Rule 6 — Worktree Isolation)**:
   - The `/focus` command IS an explicit execution trigger — the user is saying "I want to work on this now."
   - Check if a worktree already exists for this slug:
     ```powershell
     git worktree list
     ```
   - If the worktree **already exists**, just inform the user: "Worktree for `<slug>` already exists at `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>`. Work should continue there."
   - If it **does not exist**, create it:
     ```powershell
     cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
     git worktree add ../SK8Lytz-worktrees/<slug> -b <slug>
     ```
   - **NEVER run `git checkout` or `git switch` in the main repo directory.**

6. **Halt** — inform the user the worktree is ready and ask if they are ready to proceed. Remind them all work for this task happens in `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>`.

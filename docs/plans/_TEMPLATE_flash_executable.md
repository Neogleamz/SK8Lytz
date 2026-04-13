# ⚡ Flash-Executable Implementation Plan: [Task Name]

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors** (e.g. `Find the entire useBLE hook`, `Search for the exact phrase: return <View>`).
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

Before executing any file modifications, execute the following strict checks using `view_file` or `grep_search`. Do NOT guess if the file looks different.

- [ ] **Check 1:** Open `src/path/to/file.tsx`. Search for semantic anchor: `export const TargetFunction = () => {`.
  - _Expected state:_ The function should exist and currently take X arguments.
  - _Abort Condition:_ If the function has been renamed or heavily refactored into a hook, **HALT** and instruct the user: _"Codebase has drifted. This plan is stale and must be recompiled by a THINK model."_

_(Add as many pre-flight checks as necessary to ensure the environment is safe)._

---

## 2. Step-by-Step Execution Strict Instructions

_(Break the task down into atomic, copy-paste ready tool operations)._

### Step 2.1: [Action Description]

- **Target File:** `src/path/to/target.tsx`
- **Semantic Anchor / Target Content:**
  _(Describe exactly what the executing model should search for using `view_file` or `grep_search` before running `replace_file_content`. e.g., "The entire `return` block of the `renderItem` function")._

**Exact Replacement Snippet:**

```typescript
// Provide the 100% complete replacement code here. No // ... existing code comments.
// It must be a complete drop-in replacement payload for the replace_file_content tool.
```

### Step 2.2: [Next Action]

...

---

## 3. Post-Execution Verification

_(Provide exact terminal commands and expected output strings to prove the execution worked)._

- [ ] **Command:** `npx tsc --noEmit`
  - _Expected Output:_ Clean exit (0 errors) relating to the modified files.
- [ ] **Manual Step:** Ask user to load `localhost:8081` and verify [Specific UI state or behavior].

---

**Completion:** Once all checks pass, proceed to Commit Phase using the semantic message: `[type]([scope]): [message]`

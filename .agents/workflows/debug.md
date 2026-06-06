---
description: Diagnostic drill for stack traces or unexpected crashes
---

1. **Strike Counter (Three-Strike Lockout — Safety Rule 10)**:
   - On FIRST invocation for a bug: Create/update `.debug-strikes.json` in the worktree root:
     ```json
     { "bug": "<brief description>", "attempts": 1, "startedAt": "<ISO timestamp>" }
     ```
   - On each subsequent invocation for the SAME bug: increment `attempts`.
   - **If `attempts` reaches 3**: HALT immediately. Run `git reset --hard`. Tell the user: "Three-Strike Lockout triggered. Reverting and entering consultative mode." Delete `.debug-strikes.json`. Do NOT attempt a 4th fix.

2. **Isolation**: When asked to "debug this" or "fix this error", do NOT guess-fix or modify production code immediately.
3. **Analysis**: Analyze the stack trace / codebase using `grep_search` and `view_file` to locate the exact crash line.
4. **Instrumentation**: If the cause isn't obvious, use code-editing tools to inject highly specific `AppLogger.log`, `console.warn`, or `console.trace` calls within the component.
5. **Theory Formulation**: Output three distinct, highly technical theories explaining the root cause.
6. **Approval Gate**: Wait for the user to select a theory or authorize the fix before altering logic.
7. **Execution**: Once approved, apply the fix and add a source comment explaining the patch. Increment the strike counter.
8. **Success Reset**: If the fix works and tests pass, delete `.debug-strikes.json` to reset the counter.

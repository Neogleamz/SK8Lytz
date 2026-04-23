---
description: Diagnostic drill for stack traces or unexpected crashes
---

1. **Isolation**: When asked to "debug this" or "fix this error", do NOT guess-fix or modify production code immediately.
2. **Analysis**: Analyze the stack trace / codebase using `grep_search` and `view_file` to locate the exact crash line.
3. **Instrumentation**: If the cause isn't obvious, use code-editing tools to inject highly specific `AppLogger.log`, `console.warn`, or `console.trace` calls within the component.
4. **Theory Formulation**: Output three distinct, highly technical theories explaining the root cause.
5. **Approval Gate**: Wait for the user to select a theory or authorize the fix before altering logic.
6. **Execution**: Once approved, apply the fix and add a source comment explaining the patch.

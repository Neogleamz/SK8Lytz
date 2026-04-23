---
trigger: always_on
---

# The Turbo & Autopilot Protocols (Velocity Rules)

1. **The // turbo Annotation**: If a step in an Implementation Plan or Workflow is annotated with `// turbo`, you are authorized to set `SafeToAutoRun` to `true` for those specific `run_command` calls to bypass manual user confirmation.
2. **The Snack Autopilot**: Any task tagged `[L-RISK]` AND `[Snack]` AND `[BATCH]` is authorized for "Zero-Gate Execution." You may implement the change, verify it, and commit it in a single turn WITHOUT waiting for a formal plan approval or a separate branch if it is part of a maintenance sweep.

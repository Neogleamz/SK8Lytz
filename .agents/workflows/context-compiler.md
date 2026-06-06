---
description: Context Memory Compiler — sync codebase architecture into SK8Lytz_App_Master_Reference.md
---

# Context Memory Compiler — "sync architecture", "update memory", "compile context"

When prompted with "sync architecture", "update memory", or "compile context", execute the following workflow to synchronize `tools/SK8Lytz_App_Master_Reference.md` with the current codebase reality:

1. **Codebase Scan**: Use `view_file` and `list_dir` to survey:
   - `src/hooks/` — count all hooks, identify new ones not in the Registry
   - `src/hooks/ble/` — count all BLE sub-hooks
   - `src/services/` — count all services
   - `src/components/` — count all components
   - `src/screens/` — list all screens

2. **Hook & Service Registry Diff**: Compare the live file count against the Hook & Service Registry table in `tools/SK8Lytz_App_Master_Reference.md` §4. List every hook/service that is missing from the Registry.

3. **Update the Master Reference**: For each missing entry, add it to the appropriate Registry table in §4 with:
   - Name, file path, domain responsibility, and status
   Update §2 System Architecture with any directory-level changes (new `src/` subdirectories, renamed modules, etc.)

4. **Commit the Sync**:
   ```powershell
   Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
   git add tools/SK8Lytz_App_Master_Reference.md
   git commit -m "docs: sync Master Reference architecture map with live codebase"
   ```

5. **Halt**: Report how many hooks/services were added to the Registry and confirm the Master Reference is now current.

> Note: This workflow supersedes the old `architecture-map.md` approach. The Master Reference IS the architecture map.

---
description: Context Memory Compiler — sync codebase architecture into SK8Lytz_App_Master_Reference.md
persona_entry: "📋 Docs — Avery"
team_roster: .agents/team-roster.md
---

# Context Memory Compiler — "sync architecture", "update memory", "compile context"

> **📋 Docs — Avery | Context Compiler Active**
> *Avery does not tolerate documentation drift. Every hook, service, and component in the live codebase gets a row in the Master Reference. No exceptions. No lazy "TODO: document later".*

---

### ⚡ Step 0 — Avery Parity-Scan-First (MANDATORY, NO SKIP)
Before any manual sync, Avery runs an automated parity delta:

```powershell
# Count live hooks vs. registered hooks
$liveHooks = (Get-ChildItem -Path src/hooks -Recurse -Filter "*.ts" | Measure-Object).Count
Write-Host "Live hooks: $liveHooks"

# Find new files not yet in registry (proxy: files newer than last commit to Master Reference)
git log --oneline -1 -- tools/SK8Lytz_App_Master_Reference.md
```

Then grep the `src/` diff for unregistered exports:
- New `export function use*` in `src/hooks/` → potential missing Hook Registry rows
- New files in `src/services/` → potential missing Service Registry rows
- New files in `src/protocols/` → potential missing BLE Protocol Library entries

**Output the delta before proceeding:**
```
## 📋 Parity Pre-Scan
| Category | Live Count | Registered Count | Delta |
|---|---|---|---|
| Hooks | X | Y | Z unregistered |
| Services | X | Y | Z unregistered |
| BLE Commands | X | Y | Z undocumented |
```

---

When prompted with "sync architecture", "update memory", or "compile context", execute the following workflow to synchronize `tools/SK8Lytz_App_Master_Reference.md` with the current codebase reality:

1. **Codebase Scan**: Leverage the Sub-Agent Swarm Protocol. Invoke parallel `research` sub-agents using `invoke_subagent` to survey distinct directories simultaneously:
   - Sub-agent 1: `src/hooks/` and `src/hooks/ble/` — count all hooks, identify new ones not in the Registry
   - Sub-agent 2: `src/services/` — count all services
   - Sub-agent 3: `src/components/` and `src/screens/` — count all components and screens

2. **Hook & Service Registry Diff**: Compare the live file count against the Hook & Service Registry table in `tools/SK8Lytz_App_Master_Reference.md` §4. List every hook/service that is missing from the Registry.

3. **Update the Master Reference**: For each missing entry, add it to the appropriate Registry table in §4 with:
   - Name, file path, domain responsibility, and status
   Update §2 System Architecture with any directory-level changes (new `src/` subdirectories, renamed modules, etc.)

4. **Commit the Sync**:
   ```powershell
   Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
   npm run verify
   ```
   If verify fails → HALT. Fix the type error before committing the sync.
   ```powershell
   git add tools/SK8Lytz_App_Master_Reference.md
   git commit -m "docs: sync Master Reference architecture map with live codebase"
   ```

5. **Write SESSION_LOG [ARTIFACT] entry (mandatory — P3)**:
   After the commit, append to `tools/SESSION_LOG.md`:
   ```markdown
   ### [ARTIFACT] YYYY-MM-DDTHH:MM — Master Reference Sync
   **What changed:** N hooks / N services registered | N BLE entries updated
   **Commit:** <commit-hash>
   **Triggered by:** context-compiler (manual sync)
   ```

6. **Halt**: Report how many hooks/services were added to the Registry and confirm the Master Reference is now current.

> Note: This workflow supersedes the old `architecture-map.md` approach. The Master Reference IS the architecture map.


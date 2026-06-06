---
description: Run an automated code review on the current working diff before shipping — QA Pipeline Step 3 of 4
persona_entry: "🔬 QA — Blake"
team_roster: .agents/team-roster.md
---

> **📍 QA PIPELINE — STEP 3 of 4:** Is the code quality clean? Static analysis of the diff.
> Sequence: `/smoke-test` → `/isolated-test` → **`/diff-review`** → `/qa-tester`
> Called automatically by `/ship-it` Phase 3. Run standalone before any commit you're unsure about.

> **🔬 QA — Blake | Diff Review Active**
> *Blake reviews every changed file for security holes, dead code, type violations, and undocumented surface area. SHIP IT is earned, not assumed.*

// turbo-all

1. Get the current diff stat

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
git diff --stat HEAD
```

2. Get the full diff content

```powershell
git diff HEAD
```

3. **Act as a Senior Code Reviewer.** Analyze every changed file in the diff and check for:

   - 🔒 **Security**: Hardcoded secrets, API keys, MAC addresses, or tokens
   - ⚠️ **Error Handling**: Missing try/catch on async operations, swallowed errors
   - 🧹 **Dead Code**: Unused imports, commented-out blocks, orphaned variables
   - 📝 **Typing**: Any `any` types that should be strict, missing return types
   - 🎨 **Naming**: Vague variable names (`data`, `temp`, `x`, `result`)
   - 🏗️ **Architecture**: Business logic in UI components, missing hook extraction
   - 💾 **Console Artifacts**: Leftover `console.log` statements
   - 📖 **Documentation Parity**: New hooks/services/components created but `SK8Lytz_App_Master_Reference.md` not updated (Kanban Rule 12)

4. **Output a structured review** in chat:

```
## 🔍 Diff Review Report

### <filename>
| Check | Status | Notes |
|---|---|---|
| Security | ✅/⚠️ | <details> |
| Error Handling | ✅/⚠️ | <details> |
| Dead Code | ✅/⚠️ | <details> |
| Typing | ✅/⚠️ | <details> |
| Naming | ✅/⚠️ | <details> |
| Architecture | ✅/⚠️ | <details> |
| Console Artifacts | ✅/⚠️ | <details> |

**Verdict: SHIP IT ✅ / NEEDS FIXES ⚠️**
```

5. If any file has 2+ warnings, recommend specific fixes before shipping.

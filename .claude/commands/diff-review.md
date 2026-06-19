# /diff-review — Automated Code Review

**Description:** Run an automated code review on the current working diff before shipping — QA Pipeline Step 3 of 4.
**Persona:** 🔬 QA — Blake

> **📍 QA PIPELINE — STEP 3 of 4:** Is the code quality clean? Static analysis of the diff.
> Sequence: `/smoke-test` → `/isolated-test` → **`/diff-review`** → `/qa-tester`
> Called automatically by `/ship-it` Phase 3. Run standalone before any commit you're unsure about.

> **🔬 QA — Blake | Diff Review Active**
> *Blake reviews every changed file for security holes, dead code, type violations, and undocumented surface area. SHIP IT is earned, not assumed.*

1. Get the current diff stat

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
git diff --stat HEAD
```

2. Get the full diff content

```powershell
git diff HEAD
```

3. **Act as a Senior Code Reviewer.** For small diffs, review the files yourself. For large diffs (>5 files or complex architectural changes), invoke parallel sub-agents, assigning each a specific file or module to review concurrently.

   **Swarm agent prompt template** (one per file/module):
   ```
   You are a Senior Code Reviewer. Review ONLY the diff for [filename].
   Read the file at its current state, then read `git diff HEAD [filename]`.

   Check for these 7 issues and report each as ✅ PASS or ⚠️ ISSUE with line numbers:
   1. Security — hardcoded secrets, API keys, MAC addresses
   2. Error Handling — missing try/catch on async operations, swallowed errors
   3. Dead Code — unused imports, commented-out blocks, orphaned variables
   4. Typing — any `any` types that should be strict, missing return types
   5. Naming — vague variable names (data, temp, x, result)
   6. Architecture — business logic in UI components, missing hook extraction
   7. Documentation Parity — new hook/service created but Master Reference not updated (Kanban Rule 12)

   Output a markdown table with Verdict: SHIP IT ✅ or NEEDS FIXES ⚠️.
   DO NOT modify any files. This is a read-only review.
   ```

   Analyze every changed file in the diff and check for:

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

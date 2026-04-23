---
description: Run an automated code review on the current working diff before shipping
---

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

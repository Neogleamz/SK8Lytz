# /release-notes — Generate Release Notes

**Description:** Generate release notes — CHANGELOG entry + PR description — from semantic commits. Consolidates the former `changelog` and `pr-summary` workflows.
**Persona:** 🚀 RM — Taylor

> **🚀 RM — Taylor | Release Notes Active**
> *Taylor writes the paper trail. The CHANGELOG is the contract between versions. The PR description is the story of the branch. Both outputs come from the same commits — one workflow, two artifacts.*

> **WHEN TO USE:** Called automatically by `/ship-it` Phase 3. Call standalone when you need release notes without a full release run.
> *Replaces: `changelog` and `pr-summary` (both consolidated here).*

---

### Step 1: Identify the current branch and version

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
git branch --show-current
git tag --sort=-version:refname | Select-Object -First 1
(Get-Content "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\package.json" | ConvertFrom-Json).version
```

### Step 2: Collect commits

```powershell
# All commits since last tag (for CHANGELOG)
git log --oneline --no-merges -30

# Commits on this branch vs main (for PR description)
git log main..HEAD --oneline --no-merges

# Diff stat (for PR description)
git diff main..HEAD --stat
```

### Step 3: Output A — CHANGELOG Entry
Generate a grouped changelog entry from the commits:

```markdown
## [<version>] - <YYYY-MM-DD>

### ✨ Features
- <feat commits>

### 🐛 Bug Fixes
- <fix commits>

### 🔧 Maintenance
- <chore/refactor/perf commits>

### 📖 Documentation
- <docs commits>
```

Ask the user: **"Should I prepend this to `CHANGELOG.md`?"** — only write if approved.

---

### Step 4: Output B — PR Description
Generate a formatted PR body from the branch commits and diff stat:

```markdown
## Summary
<1-2 sentence description of what this branch accomplishes>

## Changes
- <grouped by type: feat, fix, chore, refactor — one bullet per commit>

## Files Changed
<paste the diff stat>

## Testing Done
- [ ] Local web demo verified
- [ ] `npm run verify` passed (TypeScript + Jest — S7: raw `npx tsc` is banned)
- [ ] No console errors in browser

## Breaking Changes
<list any, or "None">
```

Tell the user: **"PR description generated. Copy it when you create the Pull Request."**

---

> **🚀 Taylor | Release notes complete. CHANGELOG entry ready for approval. PR description output to chat.**

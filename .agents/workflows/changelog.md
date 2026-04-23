---
description: Generate a CHANGELOG entry from semantic commits since the last version tag
---

// turbo-all

1. Find the latest version tag

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
git tag --sort=-version:refname | Select-Object -First 1
```

2. Get all commits since that tag (or last 30 if no tags exist)

```powershell
git log --oneline --no-merges -30
```

3. Read the current version from package.json

```powershell
(Get-Content "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\package.json" | ConvertFrom-Json).version
```

4. Using the commits, generate a changelog entry grouped by type:

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

5. Ask the user: "Should I prepend this to `CHANGELOG.md`?" — only write if approved.

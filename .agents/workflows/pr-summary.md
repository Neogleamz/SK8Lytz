---
description: Auto-generate a formatted Pull Request description from semantic commits on the current branch
---

// turbo-all

1. Identify the current branch name

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
git branch --show-current
```

2. Collect all semantic commits since divergence from main

```powershell
git log main..HEAD --oneline --no-merges
```

3. Get the diff stat summary

```powershell
git diff main..HEAD --stat
```

4. Using the commit log and diff stat, generate a formatted PR description in the chat using this template:

```
## Summary
<1-2 sentence description of what this branch accomplishes>

## Changes
- <grouped by type: feat, fix, chore, refactor — one bullet per commit>

## Files Changed
<paste the diff stat>

## Testing Done
- [ ] Local web demo verified
- [ ] TypeScript compilation clean (`npx tsc --noEmit`)
- [ ] No console errors in browser

## Breaking Changes
<list any, or "None">
```

5. Tell the user: "PR summary generated. Copy it when you create the Pull Request."

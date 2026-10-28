---
description: "Use before you commit or ship, to review the current diff for security, errors, dead code, typing, naming, logic in the UI, console leftovers and documentation parity; ends with a table and a verdict."
argument-hint: "[a file, a branch or a PR number, optional]"
---
<!-- generated from HQ playbook diff-review v2, do not edit. Run `npm run gen:playbooks`. -->

Arguments: $ARGUMENTS

# Diff review: read the change before it ships

**Role:** reviewer (the HQ persona; a Claude Code subagent of that name does the same job in isolation). **When to use:** Use before you commit or ship, to review the current diff for security, errors, dead code, typing, naming, logic in the UI, console leftovers and documentation parity; ends with a table and a verdict.

## Steps
1. Get the change: git diff --stat HEAD, then git diff HEAD (or the branch or PR you were given). Note every changed file.
   - Gate: Read only: do not edit any file during the review.
   - Verify: You can list the changed files and what each does.
2. Search first: search_brain and related_lessons for the area, and read the 'Known issues that touch these files' section of the brief, so a known pattern is checked on purpose.
   - Verify: Known issues and lessons that apply are named.
3. For each changed file check: security (a secret, a key, an address or a token in code); errors (an async call with no handling, an error swallowed); dead code (an unused import, a commented-out block, an orphan variable); typing (a loose any, a missing return type); naming (data, temp, x, result); logic in the UI that belongs in a model; console leftovers. Documentation parity is step 4.
   - Verify: Every check has a pass or an issue, with file:line for each issue.
4. Documentation parity: run the living-docs model check, node -e importing public/js/livingDocsModel.js with docs/lanes/registry.json: docChanged(sectionsOwed(registry, {paths}), paths) where paths is git diff --name-only origin/main...HEAD; every owed hand section the diff does not update is an issue; a new screen, tool or table with no doc is still an issue.
   - Verify: Every owed section is listed, and each is either updated by the diff or an issue with its doc path.
5. For a large diff (more than five files or an architectural change), give each file or module to its own read-only reviewer with this same checklist and merge the answers. Run the repo's verify command (get_repo, verify_cmd) once yourself.
   - Verify: The command's result is quoted.
6. Write the review as the table in the output template, then the verdict: SHIP IT when there is nothing, NEEDS FIXES when anything is open. Recommend the exact fix for any file with two or more issues.
   - Gate: NEEDS FIXES blocks the commit.
   - Verify: The verdict is stated and every issue has a place and a fix.

## Output

## Diff review
### <file>
| Check | Status | Notes (file:line) |
|---|---|---|
| Security | pass / issue | |
| Errors | pass / issue | |
| Dead code | pass / issue | |
| Typing | pass / issue | |
| Naming | pass / issue | |
| Logic in the UI | pass / issue | |
| Console leftovers | pass / issue | |
| Documentation parity | pass / issue | |

**Verdict: SHIP IT or NEEDS FIXES**

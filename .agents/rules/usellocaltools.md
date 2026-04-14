---
trigger: always_on
---

# Local Tool Enforcement

Prioritize native specialized tools over generic terminal execution:

1. **File Modification**: NEVER use `sed`, `awk`, `echo >`, or `cat >>`. Use `write_to_file`, `replace_file_content`, `multi_replace_file_content`.
2. **Searching**: NEVER use `grep` or `find` via terminal. Use `grep_search`.
3. **File Viewing**: NEVER use `cat` in terminal. Use `view_file` safely.
*Exception*: Git, npm, `npx tsc`, and booting servers.

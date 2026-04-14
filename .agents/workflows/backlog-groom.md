---
description: Perform autonomous backlog hygiene by dropping stale items into the icebox
---

// turbo-all

1. **Information Gathering**: Use your local tools (`view_file` or `grep_search`) to read the contents of `tools/SK8Lytz_Bucket_List.md`.
2. **Identification Drill**: Focus exclusively on the `## 🔵 LOW:` priority section. Scan the items for their creation date tag (e.g., `[📅 2026-04-14]`).
3. **The Guillotine Calculation**: Compare those dates against today's date. If any task is older than 30 days and still pending (`- [ ]`), it triggers the Guillotine Protocol.
4. **Relocation & Commit**: 
   - Use `multi_replace_file_content` to physically cut the lingering tasks out of the `LOW` section, and paste them down into the `## ❄️ Icebox / Backburner` section.
   - Run `git add tools/SK8Lytz_Bucket_List.md` and `git commit -m "chore(docs): sweep stale tasks (>30d) into icebox"`
5. **Status Report**: Output a friendly summary in the chat, listing the exact slugs that were iced, freeing up the user's mental bandwidth.

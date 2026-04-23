---
description: Perform autonomous backlog hygiene by dropping stale items into the icebox
---

// turbo-all

1. **Information Gathering**: Use your local tools (`view_file` or `grep_search`) to read the contents of `tools/SK8Lytz_Bucket_List.md`.
2. **Identification Drill**: Focus exclusively on the `🏗️ ROADMAP` and `🧹 TECH DEBT` sections. Scan the items for their creation date tag (e.g., `[📅 2026-04-14]`) or lack of recent activity.
3. **The Guillotine Calculation**: Compare those dates against today's date. If any task is older than 30 days and still pending (`- [ ]`), it triggers the Guillotine Protocol.
4. **Relocation Protocol**: 
   - Use `multi_replace_file_content` or a surgical script to physically cut the entire multi-line block of lingering stale tasks out, and paste them down into the `❄️ ICEBOX` section.
   - Scan ALL priority sections (`TRIAGE QUEUE`, `ON DECK`, `ROADMAP`, `TECH DEBT`) for any tasks marked completed (`- [x]`).
   - Cut the entire multi-line block of those completed tasks and paste them into the `📦 ARCHIVED SPRINT LOG` at the very bottom of the file.
5. **Status Report**: Output a friendly summary in the chat, listing the exact slugs that were iced or archived, freeing up the user's mental bandwidth.

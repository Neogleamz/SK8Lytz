---
description: "⚠️ DEPRECATED — Consolidated into /audit-codebase. This file is kept for reference only."
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/team-roster.md
---

> ⚠️ **THIS WORKFLOW HAS BEEN CONSOLIDATED**
>
> `/bundle-audit` no longer exists as a standalone workflow.
>
> **All bundle-audit functionality** (largest src files, heavy node_modules scan, God Object scan) has been folded into **`/audit-codebase`** as Step 6: "Bundle & Dependency Weight Check".
>
> **Use instead:**
> - For a full sweep: `/audit-codebase`
> - For pre-release only: `/ship-it` calls `/audit-codebase` automatically in Phase 1
>
> This file will be deleted in the next cleanup sprint.

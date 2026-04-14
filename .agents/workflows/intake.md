---
description: Intake an idea, draft a plan, generate the slug, and append to bucket list
---

1. **The Intercept**: If the user asks for a feature/fix casually, halt and route it through intake: "Intercepting request. Routing through formal intake..."
2. **Generate Slug**: e.g., `feat/` for new, `fix/` for bugs, `chore/` for updates.
3. **Determine Classification**: Rank: [☁️ CLOUD|🧪 LAB], [⚠️ H-RISK|✅ L-RISK], [🍪 Snack|🍱 Meal|🥩 Feast], recommended [🤖 MODEL], [⏱️ TIME], and projected token spend [🪙 XXk].
4. **Draft the Plan**: Autonomously write a markdown plan and save it to `docs/plans/<generated-slug>.md`. 
5. **Format the Item**:
   `- [ ] <slug> : [tags...] <description> → [Plan](docs/plans/<slug>.md)`
6. **Determine Placement**: Append the item to `tools/SK8Lytz_Bucket_List.md` based on priority:
   - 🔴 CRITICAL: Crashes, Security
   - 🟠 HIGH: Tech Debt
   - 🟡 MEDIUM: Admin
   - 🔵 LOW: New Features
7. **The Priority Override (Zero-Bypass Integration)**: If the user explicitly says "up next", "bump", or "priority", immediately execute `git checkout -b <slug>` (use `// turbo` to auto-create the branch silently in the background), draft the plan, and HALT for approval before execution. Wait for the word "approved".

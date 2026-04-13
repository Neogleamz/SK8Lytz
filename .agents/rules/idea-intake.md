---
trigger: always_on
---

# Idea Intake Workflow Rule -- "add to:", "new task:", or "idea:"

When my prompt starts with "add to:", "new task:", or "idea:", you must act as the Project Manager and execute the following workflow:

1. **Analyze the Request**: Extract the core requirement from my natural language prompt.
2. **Generate Branch Slug**: Create a standard, git-friendly branch slug for the task using these prefixes:
   - fix/... for bugs, errors, or broken features.
   - feat/... for new modules, UI elements, or logic.
   - chore/... for updates, refactoring, or maintenance.
   - hw-test/... for hardware/Bluetooth experiments.
3. **Determine Classification**: Analyze the task to determine its tags: [☁️ CLOUD|🧪 LAB], [⚠️ H-RISK|✅ L-RISK], [🍪 Snack|🍱 Meal|🥩 Feast], the recommended [🤖 MODEL], the [Ready-State], and the estimated [⏱️ TIME].
   - The **[🤖 MODEL]** tag must be autonomously recommended:
     - [🤖 FLASH] (Gemini 3 Flash): UI tweaks, CSS spacing, boilerplate refactors ([✅ L-RISK], [🍪 Snack]).
     - [🤖 PRO-LOW] (Gemini 3.1 Pro Low): Standard isolated logic, simple DB queries.
     - [🤖 PRO-HIGH] (Gemini 3.1 Pro High): Complex logic crossing multiple files, DB mutations ([🍱 Meal]).
     - [🧠 THINK] (Claude 4.6 Thinking): Reverse engineering, complex architecture, deep hardware debugging ([⚠️ H-RISK], [🥩 Feast]).
   - The **[Ready-State]** tag tracks execution readiness (ONLY required for [✅ L-RISK] Snacks/Meals):
     - [📝️ NEEDS-PLAN]: Task requires a THINK model to compile the Flash-Executable plan first.
     - [⚡ FLASH-READY]: A THINK model has pre-compiled the semantic plan; ready for Flash/Pro-Low pure execution.
   - The **[⏱️ TIME]** tag provides an estimated completion time based on rapid AI Pair-Programming velocity (e.g. [⏱️ 15m], [⏱️ 45m], [⏱️ 2h]).
4. **Draft the Plan**: Autonomously generate a markdown implementation plan for the task and save it at docs/plans/<generated-slug>.md.
   - **Crucial Branching:** If the task is evaluated as [✅ L-RISK] AND ([🍪 Snack] OR [🍱 Meal]), you MUST format the plan using the strict docs/plans/_TEMPLATE_flash_executable.md standard. This creates a "Drift-Tolerant, Flash-Executable" script.
   - For [⚠️ H-RISK] or [🥩 Feast] tasks, use the standard high-level design rationale, file changes, and verification steps format.
5. **Format the Item**: Draft the new bucket list item exactly like this, explicitly linking the plan: - [ ] <generated-slug> : [☁️ CLOUD|🧪 LAB] [📦 BATCH?] [⚠️ H-RISK|✅ L-RISK] [🍪 Snack|🍱 Meal|🥩 Feast] [🤖 MODEL] [Ready-State] [⏱️ TIME] <Clear, professional description of the task> → [Plan](docs/plans/<generated-slug>.md)
   *(Note: ONLY append [Ready-State] if the task is [✅ L-RISK] AND ([🍪 Snack] OR [🍱 Meal]))*
6. **Determine Placement**: Read tools/SK8Lytz_Bucket_List.md and determine the correct section based on the **Stability-First Hierarchy**:
   - **🔴 CRITICAL**: Bugs, Crashes, RLS Blocks, Registry Data loss.
   - **🟠 HIGH**: Engineering Excellence, Tech Debt, Refactoring.
   - **🟡 MEDIUM**: EULA, Governance, Admin Hub.
   - **🔵 LOW**: New Features, UI Polish, Social.
   - Insert the item at the bottom of its respective category.
7. **Update File**: Use your code-editing tools to insert the newly formatted item into the correct section of tools/SK8Lytz_Bucket_List.md.
8. **Confirm & Halt**: Output a message confirming the addition and that the plan was generated. For example: "Added \fix/rgn-slider\ to the **main** target section and wrote plan to \docs/plans/fix-rgn-slider.md\." Do not start working on the task. Wait for me to say "what's next".
9. **Override (Zero-Bypass Integration)**: If my prompt includes an urgent directive along with the idea (e.g. "idea: change the font right now" or "add to: fix spacing up next"), you must skip the Halt step and immediately trigger the **Zero-Bypass Protocol** to checkout the branch and draft the plan.

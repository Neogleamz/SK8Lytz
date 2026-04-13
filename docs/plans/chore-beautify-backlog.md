# Implementation Plan - Beautify Backlog Tags & Rule Synchronization

This plan outlines the updates to the `SK8Lytz_Bucket_List.md` and the associated agentic rules to enhance visual clarity and aesthetic appeal using thematic icons and structured formatting.

## User Review Required

> [!IMPORTANT]
> The selection of icons for tags should be validated to ensure they align with the project's visual language. The proposed set is:
> - `[CLOUD]` -> `[☁️ CLOUD]`
> - `[LAB]` -> `[🧪 LAB]`
> - `[H-RISK]` -> `[⚠️ H-RISK]`
> - `[L-RISK]` -> `[✅ L-RISK]`
> - `[Snack]` -> `[🍪 Snack]`
> - `[Meal]` -> `[🍱 Meal]`
> - `[Feast]` -> `[🥩 Feast]`
> - `[BATCH]` -> `[📦 BATCH]`

## Proposed Changes

### Engineering Governance & PM Tools

#### [MODIFY] [SK8Lytz_Bucket_List.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_Bucket_List.md)
- Batch update all existing tags to include their respective icons.
- Update section headers with enhanced iconography (e.g., `## 🟠 HIGH: 🛠️ Engineering Excellence`).
- Refine Mermaid diagram themes for higher contrast and matching the new palette.

#### [MODIFY] [idea-intake.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/idea-intake.md)
- Update the "Determine Classification" and "Format the Item" sections to mandate the use of icons in all future bucket list additions.
- Synchronize descriptions to match the new visual tokens.

## Visual Enhancement Proposals (Feedback Requested)

1. **Model Color Tokens**: Mandate specific icons for models:
   - `[🤖 FLASH]`
   - `[🤖 PRO-LOW]`
   - `[🤖 PRO-HIGH]`
   - `[🧠 THINK]` (Changing from 🤖 to 🧠 for Thinking models)
2. **Pillar Iconography**: Map the "Pillars" (1-12) to specific icons (e.g., `[🏛️ Pillar X]`).
3. **Status Intensity**: Use colored dots or squares (🔴, 🟠, 🟡, 🔵, ⚪) next to task slugs to indicate priority at a glance.

## Verification Plan

### Manual Verification
1. Open `SK8Lytz_Bucket_List.md` and verify all existing tasks have the correct icons.
2. Simulate a new task entry to verify the updated rule correctly generates an icon-rich item.

---
trigger: always_on
---

# The Zero-Bypass Protocol (Strict Intake Enforcement)

Whenever the user issues a command that requires modifying logic, UI, or architecture—even casually, without an explicit intake trigger—you must **intercept** the request and route it through the formal system.

1. **The Intercept**: Halt execution and state: *"Intercepting request. Routing through formal intake..."*
2. **Triaging & Injection**:
   - Read the user's request and categorize it contextually.
   - If it relates to an active Epic (e.g., UI changes might belong in `epic/ui-refinement`), assign it there. If it is a disconnected bug or chore, assign it to `### Target: main`.
   - Generate a slug and physically inject the `- [ ]` markdown item into `tools/SK8Lytz_Bucket_List.md`.
3. **The Priority Override ("Up Next" / "Bump")**:
   - If the user uses the trigger phrase `"up next"`, `"bump"`, or `"priority"` alongside their request, bypass the bottom of the Epic list.
   - Physically move (or insert) the ticket to the absolute top of the `## 🔴 High Priority / Next Up` section.
4. **Quarantine & Plan (THE HARD STOP)**:
   - Execute `git checkout -b <slug>`.
   - Generate your Implementation Plan.
   - **HALT ALL ACTION.** You are strictly **FORBIDDEN** from executing any code-editing tools (`replace_file_content`, `write_to_file`, etc.) until the user physically types the word "approved" or "proceed". End your turn immediately after presenting the plan.

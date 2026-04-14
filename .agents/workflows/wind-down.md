---
trigger: always_on
---

# The Midnight Oil Protocol (Wind Down Workflow)

// turbo-all

When my prompt includes "good night", "wrapping up", "wind down", "done for the day", or "Run nightly testing", you must execute the following shutdown sequence:

1. **Workspace Sanitization (The Big Sync)**:
   - Check `git status`. If there are uncommitted changes, execute `git add .` and `git commit -m "chore: end of session sync and corporate memory update"`.
   - If a remote is configured, `git push`.

1.5. **Production Health Check (Supabase Log Audit)**:

- Use `get_logs` for `api`, `auth`, and `postgres` services for the last 2 hours.
- Analyze logs specifically for 4xx/5xx errors, RLS violations, or foreign key constraints.
- If critical production errors are found, autonomously add them as prioritized `fix/...` entries to the top of the `tools/SK8Lytz_Bucket_List.md`.

1. **Nightly Testing Execution**:
   - Explicitly spawn the `browser_subagent` and instruct it to execute `tools/SK8Lytz_TEST_PLAN.md` against the local expo server.
   - If regressions are found by the agent, you MUST autonomously add them to `tools/SK8Lytz_Bucket_List.md` as new `fix/...` tasks under the **🔴 CRITICAL** section.
   - Explicitly list them in the "Traps & Landmines" section of the Final SITREP.

2. **Knowledge Persistence (Master Reference Sync)**:
   - Review all implementation plans and terminal logs from the current session.
   - Identify any new architectural patterns, hardware protocol discoveries, or database schema changes.
   - Update `tools/SK8Lytz_App_Master_Reference.md` strictly following the **Corporate Memory Synchronization Rule**.

3. **Bucket List Grooming & Archiving**:
   - Parse `tools/SK8Lytz_Bucket_List.md`.
   - Ensure every task completed during the session is marked with `[x]`.
   - **Cleanup & Archiving**: Physically move all `[x]` items from their active Epic sections into the `## ✅ Completed Previously` archive at the bottom of the file to maintain a lean active backlog.
   - **Progress Sync**: Update the Mermaid `pie` charts in the `## 📊 Global System Readiness` and category headers to reflect the new completion counts.
   - **Prioritization**: Identify the next logical Task/Epic. Ask: "What is the absolute #1 priority for our next session?" and move that item to the top of the active list.

4. **The State of the Union (Final SITREP)**:
   - Generate a concise summary of today's achievements.
   - List any "Traps & Landmines": Technical debt, half-finished refactors, or bugs encountered that need immediate attention next time.
   - State the current active branch and the last commit hash.

5. **Hard Freeze**:
   - Close all background terminal processes (dev servers, etc.) if applicable.
   - Output a final, thematic SK8Lytz-style sign-off (e.g., "Skates docked. Lights dimmed. See you on the next session.").

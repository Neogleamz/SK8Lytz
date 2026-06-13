---
description: Unified Maintenance Sweep — npm audit, TODO hunt, bundle weight, component sizing, security advisors. Replaces /bundle-audit (consolidated here).
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/rules/team-roster.md
---

# The Audit Codebase Engine

> ⛔ **DEPRECATED — HARD REDIRECT**: This workflow has been superseded by `/self-heal`.
> Run `/self-heal` instead. All audit-codebase functionality is now part of the observatory pipeline.
> **If invoked directly**: Output *"⚠️ /audit-codebase is deprecated. Running /self-heal instead."* and switch to the /self-heal workflow immediately. Only continue with this file if the user explicitly responds with "proceed with audit-codebase directly".

> **📍 WHEN TO USE:** Periodic "clean the house" sweep — weekly, or whenever things feel messy.
> Also called by `/ship-it` Phase 1 for the bundle check portion.
> For a fast pre-release security-only check, use `/health-sweep` instead.

// turbo-all

> **🕵️ Scout — Reyes | Codebase Audit Active** *(Docs — Avery co-pilots on bucket list injection)*
> *Reyes finds the rot. Avery logs it properly. Together they keep the codebase honest. Nothing gets swept under the rug — it goes to TECH DEBT with a proper schema entry.*

---

### ⚡ Step 0 — Reyes Knowledge-First (MANDATORY, NO SKIP)
Before running a full sweep, check what the last audit found:

Announce: *"Checking prior audit findings..."*

Read `docs/SESSION_LOG.md` — search for `[ARTIFACT]` entries mentioning `audit` or `TODO sweep`. If found, output:
> *"Last audit: [date]. Findings: [summary]. Checking if those issues are resolved before flagging new ones."*

This prevents filing the same TECH DEBT tasks every session.

---

When I say "run health check", "audit codebase", or "clean the house", execute this sweep by leveraging the Sub-Agent Swarm Protocol (`invoke_subagent`):

### ⚡ Phase 1 — The Fleet Launch
Invoke 3 parallel sub-agents (Role: "Auditor", TypeName: "self") to partition the work:

- **Sub-Agent 1 (Dependencies & Cloud)**: Runs `npm outdated`, `npm audit --audit-level=moderate`, bundle/dependency weight checks (using powershell to find top 15 largest source files and node_modules >5MB), and calls `mcp_supabase-mcp-server_get_advisors`.
- **Sub-Agent 2 (Code Maintenance)**: Searches `src/` for `TODO:`, `FIXME:`, or `HACK:`, and flags God Objects (components with >15 hook calls) and Architectural Smells (files >30KB).
- **Sub-Agent 3 (Telemetry Sync)**: Runs `node tools/sync_remote_errors.mjs --hours 48 --limit 50 --json` to fetch live production errors from Supabase.

*Note: You do NOT need to wait or poll. Spawn them concurrently via a single `invoke_subagent` call and stop calling tools. The system will wake you when they message back.*

### ⚡ Phase 2 — Synthesis
1. **Bucket List Integration**:
   - Pipe all findings into `docs/SK8Lytz_Bucket_List.md` under the `🧹 TECH DEBT` queue.
   - You MUST strictly format the injected tasks according to the nested multi-line Task Schema defined in the AI AGENT DIRECTIVES.
2. **Write SESSION_LOG [ARTIFACT] entry (mandatory — P3)**:
   Append to `docs/SESSION_LOG.md`:
   ```markdown
   ### [ARTIFACT] YYYY-MM-DDTHH:MM — Codebase Audit
   **Vulnerabilities:** N found | N new TECH DEBT tasks filed
   **TODOs found:** N | **God Objects:** N | **Bundle >5MB packages:** N
   **Production errors fetched:** N
   ```
3. **Report**: Output a summary to the chat detailing vulnerabilities, crashes, TODOs, smells, and bundle weight.


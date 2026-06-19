# /audit-codebase — Unified Maintenance Sweep

**Description:** Unified Maintenance Sweep — npm audit, TODO hunt, bundle weight, component sizing, security advisors. Replaces /bundle-audit (consolidated here).
**Persona:** 🕵️ Scout — Reyes

> ⛔ **DEPRECATED — HARD REDIRECT**: This workflow has been superseded by `/self-heal`.
> Run `/self-heal` instead. All audit-codebase functionality is now part of the observatory pipeline.
> **If invoked directly**: Output *"⚠️ /audit-codebase is deprecated. Running /self-heal instead."* and switch to the /self-heal workflow immediately. Only continue with this file if the user explicitly responds with "proceed with audit-codebase directly".

> **📍 WHEN TO USE:** Periodic "clean the house" sweep — weekly, or whenever things feel messy.
> Also called by `/ship-it` Phase 1 for the bundle check portion.
> For a fast pre-release security-only check, use `/health-sweep` instead.

> **🕵️ Scout — Reyes | Codebase Audit Active** *(Docs — Avery co-pilots on bucket list injection)*
> *Reyes finds the rot. Avery logs it properly. Together they keep the codebase honest. Nothing gets swept under the rug — it goes to TECH DEBT with a proper schema entry.*

---

### Step 0 — Reyes Knowledge-First (MANDATORY, NO SKIP)
Before running a full sweep, check what the last audit found:

Announce: *"Checking prior audit findings..."*

Read `docs/SESSION_LOG.md` — search for `[ARTIFACT]` entries mentioning `audit` or `TODO sweep`. If found, output:
> *"Last audit: [date]. Findings: [summary]. Checking if those issues are resolved before flagging new ones."*

This prevents filing the same TECH DEBT tasks every session.

---

### Phase 1 — The Fleet Launch
Invoke 3 parallel sub-agents to partition the work:

- **Sub-Agent 1 (Dependencies & Cloud)**: Runs `npm outdated`, `npm audit --audit-level=moderate`, bundle/dependency weight checks (using powershell to find top 15 largest source files and node_modules >5MB), and calls `mcp__supabase__get_advisors`.
- **Sub-Agent 2 (Code Maintenance)**: Searches `src/` for `TODO:`, `FIXME:`, or `HACK:`, and flags God Objects (components with >15 hook calls) and Architectural Smells (files >30KB).
- **Sub-Agent 3 (Telemetry Sync)**: Runs `node tools/sync_remote_errors.mjs --hours 48 --limit 50 --json` to fetch live production errors from Supabase.

### Phase 2 — Synthesis
1. **Bucket List Integration**:
   - Pipe all findings into `docs/SK8Lytz_Bucket_List.md` under the `🧹 TECH DEBT` queue.
   - Format injected tasks strictly according to the nested multi-line Task Schema.
2. **Write SESSION_LOG [ARTIFACT] entry (mandatory — P3)**:
   Append to `docs/SESSION_LOG.md`:
   ```markdown
   ### [ARTIFACT] YYYY-MM-DDTHH:MM — Codebase Audit
   **Vulnerabilities:** N found | N new TECH DEBT tasks filed
   **TODOs found:** N | **God Objects:** N | **Bundle >5MB packages:** N
   **Production errors fetched:** N
   ```
3. **Report**: Output a summary to the chat detailing vulnerabilities, crashes, TODOs, smells, and bundle weight.

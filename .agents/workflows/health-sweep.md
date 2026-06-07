---
description: Pre-release health sweep — npm audit + Supabase DB security advisors before any push
persona_entry: "🩺 SRE — River"
team_roster: .agents/team-roster.md
---

# Health Sweep Engine — "/health-sweep"

// turbo-all

> **🩺 SRE — River | Health Sweep Active**
> *River runs the pre-flight checklist. A critical vulnerability in npm audit is a NO-GO for release, full stop. Security advisors on Supabase get the same treatment.*

When invoked via `/health-sweep` or as Phase 1 Step 1 of `/ship-it`, leverage the Sub-Agent Swarm Protocol to run all security and dependency health checks concurrently. This must pass before ANY release merge.

### Phase 1: Swarm Execution

Invoke 2 parallel `self` sub-agents using `invoke_subagent`:

**Sub-agent 1 (NPM Health):**
- Runs `npm audit --audit-level=moderate 2>&1 | Select-Object -Last 20`
- Runs `npm outdated 2>&1 | Select-Object -First 20`
- Reports back the exact results.

**Sub-agent 2 (Cloud Security):**
- Runs the MCP tool `mcp_supabase-mcp-server_get_advisors` with `type: 'security'`.
- Reports back any security advisors flagged.

### Phase 2: Triage & Synthesis

Once the swarm reports back, synthesize the results:

- **npm audit**: If ANY **critical** or **high** vulnerabilities are found: **HALT. Do NOT proceed to release.** If only **moderate**: Document them, proceed with user approval.
- **npm outdated**: Log any packages >2 major versions behind into the `🧹 TECH DEBT` section of `tools/SK8Lytz_Bucket_List.md`.
- **Supabase Security**: If ANY security advisors are flagged: **HALT. Log to TRIAGE QUEUE and do NOT push.**

### Step 4: Report

Output a structured summary:

```
## 🛡️ Health Sweep Report

| Check | Status | Notes |
|-------|--------|-------|
| npm audit | ✅ Clean / 🔴 CRITICAL / ⚠️ Moderate | <vuln count> |
| npm outdated | ✅ Current / ⚠️ <N> packages behind | <list> |
| Supabase Security | ✅ Clean / 🔴 Advisors found | <details> |

**Verdict: PROCEED ✅ / BLOCKED 🔴**
```

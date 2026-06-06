---
description: Unified Maintenance Sweep — npm audit, TODO hunt, bundle weight, component sizing, security advisors. Replaces /bundle-audit (consolidated here).
persona_entry: "🕵️ Scout — Reyes"
team_roster: .agents/team-roster.md
---

# The Audit Codebase Engine

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

Read `tools/SESSION_LOG.md` — search for `[ARTIFACT]` entries mentioning `audit` or `TODO sweep`. If found, output:
> *"Last audit: [date]. Findings: [summary]. Checking if those issues are resolved before flagging new ones."*

This prevents filing the same TECH DEBT tasks every session.

---

When I say "run health check", "audit codebase", or "clean the house", execute this sweep:

1. **Dependency Audit**: Run `npm outdated` and `npm audit --audit-level=moderate 2>&1 | Select-Object -Last 20`.
2. **The TODO Hunt**: Search `src/` for `TODO:`, `FIXME:`, or `HACK:`.
3. **Database Security Audit**: Run `mcp_supabase-mcp-server_get_advisors` tool with `type: 'security'`.
4. **Architectural Smell Scan**: Scan `src/` for files exceeding 30KB.
5. **God Object Scan**: Flag components with excessive hook usage (> 15 hook calls).
6. **Bundle & Dependency Weight Check** *(consolidated from /bundle-audit)*:
   ```powershell
   Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"

   # Top 15 largest source files (proxy for bundle weight)
   Get-ChildItem -Path src -Recurse -File -Include *.ts,*.tsx |
     Sort-Object Length -Descending |
     Select-Object -First 15 |
     Select-Object Name, @{Name="SizeKB";Expression={ [math]::Round($_.Length/1KB, 1) }}, DirectoryName |
     Format-Table -AutoSize

   # Flag any single package directory over 5MB
   Get-ChildItem -Path node_modules -Directory |
     Where-Object { (Get-ChildItem $_.FullName -Recurse -File | Measure-Object -Property Length -Sum).Sum -gt 5MB } |
     Select-Object Name, @{Name="SizeMB";Expression={
       [math]::Round((Get-ChildItem $_.FullName -Recurse -File | Measure-Object -Property Length -Sum).Sum/1MB, 1)
     }} |
     Sort-Object SizeMB -Descending |
     Select-Object -First 10 |
     Format-Table -AutoSize
   ```
   - Flag any source file over 100KB as a TECH DEBT item.
   - Flag any single node_module over 5MB in the report.
   - Do NOT block release for bundle size alone unless a single source file exceeds 200KB.
7. **Telemetry Sync**: Run `node tools/sync_remote_errors.mjs` to query the Supabase `telemetry_errors` table and print a triage summary. Flags: `--hours 48` (window), `--limit 50`, `--json` (raw output). Non-fatal if Supabase is offline.
8. **Bucket List Integration**:
   - Pipe all findings into `tools/SK8Lytz_Bucket_List.md` under the `🧹 TECH DEBT` queue.
   - You MUST strictly format the injected tasks according to the nested multi-line Task Schema defined in the AI AGENT DIRECTIVES.
9. **Report**: Output a summary to the chat detailing vulnerabilities, crashes, TODOs, smells, and bundle weight.


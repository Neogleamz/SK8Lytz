---
description: Unified Maintenance Sweep — npm audit, TODO hunt, component sizing, bundle checking
---

# The Audit Codebase Engine

// turbo-all

When I say "run health check", "audit codebase", or "clean the house", execute this sweep:

1. **Dependency Audit**: Run `npm outdated` and `npm audit --audit-level=moderate 2>&1 | Select-Object -Last 20`.
2. **The TODO Hunt**: Search `src/` for `TODO:`, `FIXME:`, or `HACK:`.
3. **Database Security Audit**: Run `mcp_supabase-mcp-server_get_advisors` tool with `type: 'security'`.
4. **Architectural Smell Scan**: Scan `src/` for files exceeding 30KB.
5. **God Object Scan**: Flag components with excessive hook usage (> 15 hook calls).
6. **Telemetry Sync**: Run `node tools/sync_remote_errors.mjs` to fetch crash telemetry.
7. **Bucket List Integration**: 
   - Pipe all findings into `tools/SK8Lytz_Bucket_List.md` under the `🧹 TECH DEBT` queue.
   - You MUST strictly format the injected tasks according to the nested multi-line Task Schema defined in the AI AGENT DIRECTIVES.
8. **Report**: Output a summary to the chat detailing vulnerabilities, crashes, TODOs, and smells.

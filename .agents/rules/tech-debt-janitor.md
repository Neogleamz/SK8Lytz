---
trigger: always_on
---

# Tech Debt Janitor Workflow

When I say "run health check" or "clean the house", you must execute the following maintenance sweep:

1. **Dependency Audit**: 
   - Open the terminal and run `npm outdated` and `npm audit`. 
   - Note any critical vulnerabilities or major version updates needed.
2. **The TODO Hunt**: 
   - Use your tools to search the entire codebase for comments containing `TODO:`, `FIXME:`, or `HACK:`. 
3. **Global Telemetry Sync**:
   - Run `node tools/sync_remote_errors.mjs` to fetch recent unhandled crash telemetry from Supabase and automatically pipe them into the priority bucket list.
4. **Architectural Smell Scan**:
   - Scan `src/` for files exceeding 30KB or 500 lines.
   - Specifically flag components that are growing into "God Objects" (too many responsibilities/hooks).
5. **Bucket List Integration**: 
   - Open `tools/SK8Lytz_Bucket_List.md`.
   - Take the findings from the Dependency Audit, TODO Hunt, and Smell Scan and format them into proper `- [ ]` tasks (e.g., `- [ ] \`chore/refactor-monolith\` : Refactor large monolithic component to meet modularity standards`).
   - Add these items to the very bottom of the `### Target: main` section (or appropriate Epic if identified).
6. **Report**: 
   - Output a summary to the chat detailing vulnerabilities, crashes, TODOs, and any new architectural smells discovered. Wait for my next command.

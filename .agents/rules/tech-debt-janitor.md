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
4. **Bucket List Integration**: 
   - Open `tools/SK8Lytz_Bucket_List.md`.
   - Take the findings from the Dependency Audit and the TODO Hunt and format them into proper `- [ ]` tasks (e.g., `- [ ] \`chore/update-noble-package\` : Update noble Bluetooth library to fix security vulnerability`).
   - Add these items to the very bottom of the `### Target: main` section.
5. **Report**: 
   - Output a summary to the chat detailing how many vulnerabilities were found, how many new crashes were synced from production, and how many TODOs were converted. Wait for my next command.

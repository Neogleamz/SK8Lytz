# SK8Lytz: rules for Claude Code sessions

SK8Lytz is a React Native (Expo) app that controls SK8Lytz roller-skate LEDs over BLE (Zengge/BanlanX controllers), with a Supabase backend.
Run it: `npm install` (legacy-peer-deps, see .npmrc), then `npm start` (`npm run android` / `npm run ios` for a device build). Tests: `npm test`. Pre-push gate: `npm run verify`.

## Consult the Brain first
- The `hq-brain` MCP server (.mcp.json, needs `HQ_BRAIN_MCP_TOKEN` in your environment) searches every doc and HQ record. Use `search_brain` before researching or designing.
- Work HQ tasks: `list_ready_tasks` -> `get_task_brief` -> `claim_task` -> work -> `log_run` -> `write_kb` -> `mark_done`. Never verify your own work.
- The repo rules come from HQ: `get_rules` (repo sk8lytz). Playbooks in .claude/commands and .claude/skills and the agents in .claude/agents are generated from HQ: run `npm run gen:playbooks -- --repo sk8lytz --out C:/Neogleamz/SK8Lytz` in neogleamz-hq. Do not edit them here.
- Commits start `[t-<task id>]` or `[chore]` (checked by .husky/commit-msg).
- Never touch production Supabase `qefmeivpjyaukbwadgaz` (including type generation) without the owner.

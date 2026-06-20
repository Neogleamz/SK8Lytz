# SK8Lytz — Claude Code Project Rules

> A BLE-controlled smart-skate-light app (React Native / Expo + Supabase + Zengge/BanlanX controllers).
> This project runs as an 11-persona development team with a strict, evidence-first constitution.
> **Priority order: P1 (Evidence) > P2 (Identity) > P3 (System) > P4 (Surgical) > P5 (Grow).**

All rules are always-active and split into focused files under `.claude/rules/` (imported below). Custom subagents in `.claude/agents/` inherit all of it automatically.

## Rules (imported)

- @.claude/rules/constitution.md — the 5 core principles (P1–P5) + priority conflict resolution. The final fallback when no procedure matches.
- @.claude/rules/prime-directive.md — the S1–S9 hard stops, process gates, micro-checkpoints, key file locations.
- @.claude/rules/safety-protocol.md — git/branch/push gates, three-strike lockout, machine constants, tool playbook, Victory Snapshots, Discord bridge.
- @.claude/rules/agent-behavior.md — anti-hallucination, surgical/Boy-Scout, dependency diet, self-doubt protocol, session-state header, knowledge retention.
- @.claude/rules/kanban.md — task/sprint/wave management constitution.
- @.claude/rules/sub-agent-behavior.md — rules for delegated subagents.
- @.claude/rules/team-roster.md — the 11 personas, default-persona rules, handoff format.

## How this project is wired (Claude Code)

- **Personas** → `.claude/agents/*.md` (11 subagents, tool-boundaried, per-persona model). Delegate to them by name; `/start-task` and `/ship-it` orchestrate them through their phases.
- **Workflows** → `.claude/commands/*.md` (invoked with `/name`).
- **Auto-activating skills** → `.claude/skills/` — `debug`, `qa-tester`, `kb-capture` fire on matching context.
- **Enforcement** → `.claude/settings.json` hooks (these are HARD GATES, not guidance):
  - `guard-push` — blocks `git push` without a fresh attestation (Safety Rule 6).
  - `guard-fortress` — blocks master `src/` edits while a worktree is active (Rules 1–2).
  - `guard-strikes` — blocks edits once debug attempts ≥ 3 (Rule 10).
  - `check-any-cast` — flags `as any` / `@ts-ignore` after edits (No-`any` Law).
- **Persona identity** → the `SK8Lytz Personas` output style enforces the `[badge | … ]` header. Enable once with `/output-style SK8Lytz Personas`.

## Natural-language routing

Slash commands do NOT auto-fire in Claude Code (only skills do). When the user speaks naturally, recognize these phrases and invoke the matching command:

| The user says… | Run |
|---|---|
| "what's next" / "start working on the bucket list" / "focus on <slug>" | `/start-task` |
| a new feature idea / "I want to add…" / "can we build…" | `/intake` |
| "wind down" / "end of session" / "let's wrap up" | `/wind-down` |
| "hello" / session kickoff / cold start | `/hello` |
| "ship it" / "release this" / "merge and push" | `/ship-it` |
| "groom the backlog" / "clean up the board" | `/groom-backlog` |

This is guidance, not automation — recognize the intent and run the command.

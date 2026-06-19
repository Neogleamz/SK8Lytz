# SK8Lytz — Antigravity → Claude Code Migration & Gap Analysis

> Reference doc for the migration of the SK8Lytz agent system from Google Antigravity ADE to Claude Code (June 2026).
> Covers: what maps, what regresses, the gaps, best-practice enhancements, the enforcement design, and scoping.

---

## 1. Project layout after migration

Everything lives under the **real project root** `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz` (NOT the parent `AG_SK8Lytz_App`). Claude Code must be launched from `SK8Lytz\` or none of this loads.

```
SK8Lytz/
├── CLAUDE.md                 ← lean entry point; @-imports the rules below (always-on)
├── .claude/
│   ├── rules/                ← split, path-scoped rule files (auto-inherited by subagents)
│   ├── commands/  (37)       ← workflows, invoked with /name
│   ├── skills/    (3)        ← debug, qa-tester, kb-capture (auto-activate on context)
│   ├── agents/    (11)       ← the persona roster as subagents
│   ├── hooks/                ← enforcement scripts (PENDING consent — see §6)
│   ├── output-styles/        ← persona-header enforcement (PENDING consent — see §6)
│   └── settings.json         ← permissions + hooks + statusline (PENDING consent — see §6)
└── .agents/                  ← ORIGINAL Antigravity source (kept as reference; see §7)
```

## 2. Capability mapping (what became what)

| Antigravity | Claude Code | Notes |
|---|---|---|
| `~/.antigravity` rules (`trigger: always_on`) | `CLAUDE.md` + `.claude/rules/` | Auto-loaded every session. **Guidance, not enforced** (see §3). |
| Workflows (`.agents/workflows/*.md`) | `.claude/commands/*.md` | Invoked with `/name`. |
| Personas (team roster) | `.claude/agents/*.md` (11) | Real subagents with tool boundaries + per-persona model. |
| Auto-triggered workflows | **Skills** (`.claude/skills/`) | Only skills auto-activate; commands do not (see §3). |
| `// turbo` / `SafeToAutoRun` | `settings.json` permissions allow-list | Inline turbo markers are dead. |
| Rule enforcement | **Hooks** (`settings.json`) | The only mechanism that can *block* an action (see §5). |

## 3. Regressions — what does NOT work the same

1. **Always-on session-state header `[badge | activity | task | cold/warm]` (P2 Identity).** Antigravity injected rules every turn; Claude Code treats CLAUDE.md as *context the model can drift from*. **Fix:** an **output style** (modifies the system prompt directly — the only reliable enforcement) + optional statusline display.
2. **Natural-language workflow triggers.** Phrases like "what's next" / "start working on the bucket list" auto-fired workflows in Antigravity. **Claude Code slash commands never auto-fire** — only **skills** auto-activate (via their `description`). **Fix:** a "Natural-language routing" section in CLAUDE.md maps the phrases → slash commands so the agent recognizes and invokes them.
3. **`trigger: always_on` enforcement.** Rules are now context, not enforced config. Under pressure the model can deprioritize them. **Fix:** move the must-never-break rules into **hooks** (§5).
4. **`// turbo` auto-run.** Replaced by `settings.json` permission allow-lists.
5. **Antigravity tool names** in command bodies (`view_file`, `search_web`, `grep_search`, `replace_file_content`, `read_url_content`, `send_message`, `mcp_<server>-mcp-server_*`). These don't exist in Claude Code. **Fix:** mechanical replacement (Read, WebSearch, Grep, Edit, WebFetch, auto-return, `mcp__<server>__*`).
6. **Gemini model assumptions** in 2 commands → re-pointed to Claude models.

## 4. Gaps identified

- **Enforcement gap (biggest):** safety-critical rules (Zero-Bypass Push Gate, Three-Strike Lockout, Master Fortress Lock, No-`any` Law) were prose the model *hopes* to follow — nothing physically prevented a violation.
- **~22 Antigravity tool references** across 15 command files + CLAUDE.md.
- **Machine-specific path** `D:\graphrag-brain\…` hard-coded in `graphrag-db-exporter.md` → replaced with a `<GRAPHRAG_BRAIN_DIR>` placeholder.
- **Rule duplication:** each of the 11 agent files restated the Constitution under a (false) premise that subagents don't inherit CLAUDE.md. **Reality: custom subagents DO inherit CLAUDE.md + `.claude/rules/`** (only built-in Explore/Plan skip it). Agents slimmed to persona-specific emphasis.
- **CLAUDE.md bloat:** 636 lines (docs recommend <~200). Split into `.claude/rules/` + `@`-imports.
- **Unused Claude Code features:** hooks, output styles, statusline, `.claude/rules/` path-scoping, `settings.json` permissions, `@`-imports.

## 5. Enforcement design (hooks) — turning prose rules into hard gates

| Rule (was prose) | Hook | Mechanism |
|---|---|---|
| Zero-Bypass Push Gate (Safety Rule 6/7) | `guard-push.ps1` — PreToolUse on Bash/PowerShell | Blocks `git push` unless `.test-attestation.json` exists AND is newer than HEAD. Exit 2 = deny. |
| Master Fortress Lock (Rules 1-2) | `guard-fortress.ps1` — PreToolUse on Edit/Write | Blocks edits to `src/`,`android/`,`ios/` on master while a worktree is active. |
| Three-Strike Lockout (Rule 10) | `guard-strikes.ps1` — PreToolUse on Edit/Write | Blocks edits once `.debug-strikes.json` attempts ≥ 3 (root or any worktree). |
| No-`any` Law (agent-behavior) | `check-any-cast.ps1` — PostToolUse on Edit/Write | Warns (exit 2 → fed back to Claude) when a `.ts/.tsx` edit contains `as any`/`@ts-ignore`. |

All scripts use `$PSScriptRoot`-relative resolution (portable), fail **open** on parse/git errors (never block legitimate work by accident), and emit a clear reason on stderr. Wired via `.claude/settings.json` `hooks` + a `permissions` allow/deny list (turbo allow-list for safe commands; deny `rm -rf` and `.git/hooks/` edits) + a `statusLine` showing 🛹 persona / model / worktree / context%.

> **Reliability note:** CLAUDE.md = best-effort adherence. Hooks = deterministic enforcement (exit code 2 denies the call). For "must never happen" rules, hooks are the only guarantee.

## 6. Status — done vs. pending consent

**Done / in progress (content & structure):**
- ✅ Command files cleaned of Antigravity tool names + Gemini refs + machine path.
- ✅ CLAUDE.md split into `.claude/rules/` with `@`-imports; NL-routing section added.
- ✅ 11 agent files slimmed (de-duplicated constitutional boilerplate; persona specifics kept).
- ✅ 3 skills + 11 subagents + 37 commands in place.
- ✅ This document.

**Pending explicit user consent (self-modifying / auto-executing config):**
- ⏳ `.claude/hooks/*.ps1` enforcement scripts (auto-run shell on every matching tool call).
- ⏳ `.claude/settings.json` (activates hooks + permissions + statusline).
- ⏳ `.claude/output-styles/` persona-header enforcement.

These were intentionally deferred: hooks execute shell commands on every tool call and change agent behavior at startup, so they warrant a conscious opt-in. See the active session for the approval step.

## 7. Open decisions

- **Fate of `.agents/` + original `docs/` Antigravity content:** the pre-migration source still exists and is saturated with Antigravity-isms. Keep as reference, archive, or delete? (Confusion/drift risk if left in place.)
- **Global vs project scope:** all of the above is project-scoped (SK8Lytz only). Universal preferences (no `as any`, evidence-first, surgical edits) could be promoted to `~/.claude/CLAUDE.md` to apply to every session if desired.
- **Natural-language triggers:** recovered via CLAUDE.md routing guidance (not auto-firing). Converting heavy pipelines (`/start-task`) to auto-activating skills is possible but risky (unwanted auto-fire) — left as a documented option.

## 8. Scope summary

| Artifact | Scope | Applies to other projects? |
|---|---|---|
| `SK8Lytz/CLAUDE.md`, `.claude/**` | Project | ❌ SK8Lytz only |
| `~/.claude/CLAUDE.md`, `~/.claude/**` | User | ✅ All sessions |
| Managed/enterprise settings | Org | ✅ All, non-overridable |

**To launch correctly:** `cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz` then `claude`. Hooks, commands, skills, agents, and CLAUDE.md all load relative to that directory.

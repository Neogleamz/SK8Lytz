---
description: "Self-Healing Audit System — Unified error observatory, analyzer, and auto-triage pipeline"
persona_entry: "🩺 SRE — River"
team_roster: .agents/rules/team-roster.md
trigger: "/self-heal"
model_requirement: "Reasoning-heavy model (Claude Sonnet 4 / Gemini 2.5 Pro)"
estimated_duration: "10-20 minutes depending on fleet size"
---

# 🛡️ Self-Healing Observatory Pipeline (/self-heal)

This workflow acts as the Error Observatory and Auto-Triage Pipeline.

## Phase 0 — Institutional Memory Pre-Check (River)
> 🩺 SRE — River activates. Reads institutional memory BEFORE any collection.

1. Read `docs/KNOWN_ISSUES.md` → load resolved patterns for regression detection
2. Read `docs/FRICTION_LEDGER.md` → load active friction events
3. Read last 10 entries of `docs/SESSION_LOG.md` → check for recent error context
4. Read `docs/SK8Lytz_Bucket_List.md` TRIAGE QUEUE → check for existing error tasks (avoid duplicates)
5. Announce findings — "Observatory pre-check complete. X known issues loaded, Y friction events active, Z existing error tasks in triage."

**Gate:** If ACTIVE SPRINT contains incomplete tasks, warn user: *"⚠️ Active sprint has in-progress work. Observatory findings will be queued to TRIAGE, not injected into the sprint."*

## Phase 1 — Collection Fleet (River → Blake)
> 🔬 QA — Blake takes over collection. Spawns parallel collector agents.

─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🩺 River → 🔬 Blake
Completed: Institutional memory pre-check — X known issues, Y frictions, Z existing triage items loaded
Picking up: Error collection from all 12 sources
Context: Regression detection baseline loaded. No sprint conflicts.
─────────────────────────────────────────────────────────────────────

**Fleet Architecture:** Launch sub-agents to run collectors (Remote, Build, Device, Local). Write findings to `tools/observatory/raw/<agent_id>_findings.json`.

## Phase 2 — Normalization & Dedup (Blake)
> 🔬 QA — Blake processes raw findings.

1. Read all `tools/observatory/raw/*_findings.json`
2. Validate schema conformance (spot-check 3 random files)
3. Run deduplication pipeline
4. Run cross-reference engine
5. Run regression detection
6. Calculate urgency scores
7. Output → `tools/observatory/processed/YYYY-MM-DD_analyzed.json`

**Quality Gate:** If >50% of findings are false positives → halt and report: *"⚠️ False positive rate too high. Check collector configurations."*

## Phase 3 — Triage Report Generation (Blake → Reyes)
> 🕵️ Scout — Reyes takes over for the intelligence synthesis.

─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🔬 Blake → 🕵️ Reyes
Completed: X errors collected, Y after dedup, Z regressions detected, W auto-heal candidates identified
Picking up: Triage report generation and priority ranking
Context: Analyzed JSON at tools/observatory/processed/YYYY-MM-DD_analyzed.json. Urgency scores computed.
─────────────────────────────────────────────────────────────────────

1. Group errors into task clusters
2. Rank clusters by urgency
3. Generate task entries
4. Check auto-heal library
5. Generate triage report: `tools/observatory/reports/YYYY-MM-DD/report.md`
6. Write [ARTIFACT] entry to SESSION_LOG

## Phase 4 — User Review Gate (Reyes → Jordan)
> 🎯 PM — Jordan presents the triage report to the user.

─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🕵️ Reyes → 🎯 Jordan
Completed: Triage report generated with X task proposals, Y auto-heal candidates
Picking up: User review and approval
Context: Report at tools/observatory/reports/YYYY-MM-DD/report.md. All tasks have full kanban schema.
─────────────────────────────────────────────────────────────────────

1. Present triage report to user
2. User reviews and makes decisions
3. For approved tasks → Jordan routes through /intake Step 6-8
4. For auto-heal candidates → present separately

## Phase 5 — Institutional Memory Write-Back (Jordan → Alex)
> 🌙 Ops — Alex handles the write-back to all institutional memory files.

─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🎯 Jordan → 🌙 Alex
Completed: User approved X tasks, rejected Y, Z auto-heal candidates queued
Picking up: Institutional memory updates and session close
Context: Approved task list + observatory report location
─────────────────────────────────────────────────────────────────────

1. Write [ARTIFACT] entry to SESSION_LOG
2. Append novel error patterns to KNOWN_ISSUES.md
3. Increment occurrences in FRICTION_LEDGER
4. Trigger Evolution Proposal if needed
5. Archive raw findings to tools/observatory/archive/YYYY-MM-DD/
6. Output final summary

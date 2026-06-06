# Friction Ledger
> **Owned by:** The entire team — any persona may file. Reyes reviews at session start. Alex reviews at wind-down.
>
> **The Evolution Loop:**
> `Observe → Document here → 3 strikes → Auto-propose rule fix → User approves → Implement → Victory Snapshot`
>
> **Why this file exists:** Every time the team has to re-correct the same behavior, it costs time and trust.
> This ledger closes that loop permanently. No pattern should appear here more than 3 times without a rule being updated.

---

## ⚡ How to File a Friction Event (Any Persona)

When you observe a recurring problem, add or increment an entry here:
```markdown
### [FRICTION-XXX] <short pattern name>
- **First Observed:** YYYY-MM-DD
- **Observed By:** [persona name]
- **Occurrences:** X / 3
- **Trigger:** [what user said or what happened]
- **Pattern:** [what behavior went wrong — be specific]
- **Root Cause Theory:** [why this keeps happening mechanically]
- **Impact:** [what it cost — time, re-work, confusion]
- **Status:** MONITORING | PROPOSAL SENT | RESOLVED
```

**At 3 occurrences → AUTO-PROPOSAL TRIGGERED:**
The observing persona immediately drafts a Rule Evolution Proposal and presents it to the user.

---

## 🔴 Active Friction Events (Open — Under Monitoring)

> *No active friction events. Add entries here as patterns emerge.*

---

## ✅ Resolved Patterns (Victory Snapshots)

> *Victory Snapshots are baked into the rules and moved here. The problem cannot recur.*

### [VICTORY-001] Documentation Drift (VS-003)
- **Pattern:** New hooks/services added to codebase without updating Master Reference §3/§4.
- **Occurrences Before Fix:** 16 commits of drift (2026-06-06)
- **Root Cause:** No automated parity check gate before merge.
- **Fix Applied:** Added Avery's Parity-Scan-First to Phase 5.5 of `/start-task` + `agent-behavior.md` Rule 12 Documentation Parity Gate.
- **Filed To:** `start-task.md`, `agent-behavior.md`, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-002] Session Re-Derivation Loops
- **Pattern:** Agent re-investigates decisions already made in prior sessions, wasting 30–60 min per session.
- **Root Cause:** SESSION_LOG was only written at wind-down, not after each decision in real-time.
- **Fix Applied:** Reyes Knowledge-First Protocol (check SESSION_LOG BEFORE investigating), plus Rule 11 mandating live SESSION_LOG updates after every merge/decision.
- **Filed To:** `agent-behavior.md` Rule 11, `team-roster.md`, all research workflows.
- **Date Resolved:** 2026-06-06

### [VICTORY-003] Nameless Analysis Sessions
- **Pattern:** Agent reads files and researches without declaring which persona is active, causing session drift and inconsistent behavior.
- **Root Cause:** No enforcement that free-form research requires a persona declaration.
- **Fix Applied:** Rule 12 Free-Form Research Binding — any file read must be preceded by "🕵️ Scout — Reyes is investigating..."
- **Filed To:** `agent-behavior.md` Rule 12
- **Date Resolved:** 2026-06-06

### [VICTORY-004] Persona Stale Labeling
- **Pattern:** Personas existed as labels (a badge at the top) but had no proactive behaviors or owned artifacts, so the "team" feeling was absent.
- **Root Cause:** team-roster.md only defined voice/style, not Superpower, Owns, Proactive Behaviors, or Elite Standard.
- **Fix Applied:** Full elite profile upgrade for all 11 personas. Rule 13 Proactive Persona Protocol (Behavior #1 is mandatory on activation). All workflows now have embedded proactive Step 0 checks.
- **Filed To:** `team-roster.md`, `agent-behavior.md` Rule 13, all 34 workflows.
- **Date Resolved:** 2026-06-06

### [VICTORY-005] No Constitutional Fallback
- **Pattern:** When a situation matched no procedure in the workflows, agent reverted to generic behavior instead of reasoning from first principles.
- **Root Cause:** Rules were all procedures ("in situation X, do Y") with no underlying principles for the agent to extrapolate from in novel situations.
- **Fix Applied:** Created `.agents/rules/CONSTITUTION.md` — 5 priority-ordered principles (P1 Evidence > P2 Identity > P3 System > P4 Surgical > P5 Grow). Added as preamble to `agent-behavior.md`. Referenced in `prime-directive.md` and `team-roster.md`.
- **Filed To:** `.agents/rules/CONSTITUTION.md` (new), `agent-behavior.md` preamble, `prime-directive.md`, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-006] Invisible Session State (Identity Decay)
- **Pattern:** Persona badges appeared at workflow start, then disappeared after 2–5 turns. Agent drifted to anonymous generic responses mid-workflow.
- **Root Cause:** No enforcement mechanism for continuous identity declaration. Persona was declared once and never re-affirmed.
- **Fix Applied:** Session State Header rule (Rule 0, Fix 2) added to `agent-behavior.md`. Every response during active workflow MUST begin with `[{badge} | {activity} | {task} | {cold/warm}]`. Added to `team-roster.md` header.
- **Filed To:** `agent-behavior.md` Rule 0, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-007] Cold-Start Blindness
- **Pattern:** New conversations started without onboarding — no SESSION_LOG read, no persona declared, no sprint context. Agent went straight to task execution in a context vacuum.
- **Root Cause:** `/hello` was opt-in (user had to type it). Cold-start had no automatic trigger.
- **Fix Applied:** Cold-Start Auto-Detection (Rule 0, Fix 3) added to `agent-behavior.md`. Agent now scans every first message for cold-start signals and auto-executes the hello protocol before responding.
- **Filed To:** `agent-behavior.md` Rule 0
- **Date Resolved:** 2026-06-06

### [VICTORY-008] Placeholder Handoffs (Context Leakage Between Personas)
- **Pattern:** Handoff blocks shipped with template placeholder text (`[list]`, `[summary]`, `[TBD]`). Receiving persona had no real context and re-derived it from scratch.
- **Root Cause:** Handoff block format was a template but there was no enforcement that it was filled with real content before activation.
- **Fix Applied:** Handoff Completeness Gate (Rule 0, Fix 4) added to `agent-behavior.md`. Any handoff with placeholder text is REJECTED — outgoing persona must fill it. Also added to `team-roster.md`.
- **Filed To:** `agent-behavior.md` Rule 0, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-009] Rules Loaded Once, Never Re-Applied (JIT Drift)
- **Pattern:** Rules read at session start faded mid-conversation. By turn 8, agent had stopped applying surgical bounds, QA cross-refs, and attestation checks.
- **Root Cause:** Rules were session-start documents, not point-of-action triggers. No mechanism re-enforced them at the moment of the specific action they governed.
- **Fix Applied:** Just-In-Time Micro-Read protocol (Fix 5) added to `prime-directive.md`. 5 personas (Sage, Blake, Taylor, Reyes, Jordan) each have a 3-point "recite before action" micro-check that fires RIGHT before their critical action, not just at session start.
- **Filed To:** `prime-directive.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-010] Rules Without Reasoning (Mechanical Compliance Without Understanding)
- **Pattern:** Agent followed rules in matched situations but couldn't extrapolate to similar-but-not-identical situations because the WHY was never documented.
- **Root Cause:** Rules were stated as mandates without explaining the failure mode they prevented. The agent had no basis for reasoning about edge cases.
- **Fix Applied:** "Because" annotations added to Rules 1–5 in `agent-behavior.md`. "Why It Exists" column added to all Hard Stops in `prime-directive.md`. Each rule now explains the specific failure mode it was designed to prevent.
- **Filed To:** `agent-behavior.md` Rules 1–5, `prime-directive.md` Hard Stops table
- **Date Resolved:** 2026-06-06

### [VICTORY-011] Flat Workflow List (No Hierarchy = Apparent Duplication)
- **Pattern:** 34 workflows presented as a flat list made unrelated tools look equivalent (e.g., /tdd and /start-stack appearing side-by-side). Users couldn't tell when to reach for which tool.
- **Occurrences Before Fix:** Flagged by user 2026-06-06: "i feel like health sweep - smoke test - product alignment... ARE ALL VERY SIMILAR and confusing"
- **Root Cause:** Cheat sheet used one flat alphabetical table. No tier grouping, no lifecycle position, no sequence context.
- **Fix Applied:** Cheat sheet rebuilt with 7 color-coded tier groups (Session/Task/Dev/QA/Release/Maintenance/Infra). QA pipeline sequence visual added (smoke-test→isolated-test→diff-review→qa-tester). Each QA workflow now has a lifecycle position header (Step N of 4 + sequence breadcrumb).
- **Filed To:** `tools/cheat-sheet.html`, `smoke-test.md`, `isolated-test.md`, `diff-review.md`, `qa-tester.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-012] Redundant Standalone Workflows (bundle-audit, changelog, pr-summary)
- **Pattern:** `bundle-audit` was 80% duplicate of `audit-codebase`. `changelog` and `pr-summary` ran the same git commands against the same data for two different outputs but had to be called separately. `ship-it` called all three independently.
- **Root Cause:** Workflows added incrementally over time without a consolidation review. No taxonomy existed to catch when a new workflow was subsumed by an existing one.
- **Fix Applied:** `bundle-audit` folded into `audit-codebase` Step 6 (Bundle & Dependency Weight Check). `changelog` + `pr-summary` merged into new `release-notes.md` (two outputs, one workflow). `ship-it` updated to call consolidated workflows. Old files redirected with deprecation notices.
- **Filed To:** `audit-codebase.md`, `release-notes.md` (new), `ship-it.md`, `bundle-audit.md`, `changelog.md`, `pr-summary.md`
- **Date Resolved:** 2026-06-06

---

## 📊 Evolution Metrics

| Metric | Value |
|---|---|
| Total Friction Events Filed | 12 |
| Resolved (Victory Snapshots) | 12 |
| Open / Monitoring | 0 |
| Rules Updated This Session | Rules 0–5, 12–14 + 34 workflows + team-roster.md + prime-directive.md |
| New Files Created | `CONSTITUTION.md`, `FRICTION_LEDGER.md`, `release-notes.md`, `cheat-sheet.html` |
| Workflows Consolidated | `bundle-audit` → `audit-codebase` · `changelog`+`pr-summary` → `release-notes` |
| System Capability Delta | +Constitution (P1-P5 fallback) · +JIT re-reads · +cold-start detection · +handoff gate · +state header · +7-tier cheat sheet · +QA pipeline sequence |
| Re-Derivation Loops Prevented | ∞ (Reyes Knowledge-First + SESSION_LOG live updates) |
| Hounding Incidents Expected Going Forward | 0 (The No-Hounding Compact + P5 Grow the System) |


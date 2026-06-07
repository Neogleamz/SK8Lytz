# Friction Ledger
> **Owned by:** The entire team â€” any persona may file. Reyes reviews at session start. Alex reviews at wind-down.
>
> **The Evolution Loop:**
> `Observe â†’ Document here â†’ 3 strikes â†’ Auto-propose rule fix â†’ User approves â†’ Implement â†’ Victory Snapshot`
>
> **Why this file exists:** Every time the team has to re-correct the same behavior, it costs time and trust.
> This ledger closes that loop permanently. No pattern should appear here more than 3 times without a rule being updated.

---

## âš¡ How to File a Friction Event (Any Persona)

When you observe a recurring problem, add or increment an entry here:
```markdown
### [FRICTION-XXX] <short pattern name>
- **First Observed:** YYYY-MM-DD
- **Observed By:** [persona name]
- **Occurrences:** X / 3
- **Trigger:** [what user said or what happened]
- **Pattern:** [what behavior went wrong â€” be specific]
- **Root Cause Theory:** [why this keeps happening mechanically]
- **Impact:** [what it cost â€” time, re-work, confusion]
- **Status:** MONITORING | PROPOSAL SENT | RESOLVED
```

**At 3 occurrences â†’ AUTO-PROPOSAL TRIGGERED:**
The observing persona immediately drafts a Rule Evolution Proposal and presents it to the user.

---

## ðŸ”´ Active Friction Events (Open â€” Under Monitoring)

### [FRICTION-015] Cowboy Hotfixes on Master
- **First Observed:** 2026-06-07
- **Observed By:** River
- **Occurrences:** 1 / 3
- **Trigger:** User: "why are we stashing this??? what changes did we fucking loose???"
- **Pattern:** Agent fixed an organically discovered bug directly on `master` without creating a formal task worktree, violating Safety Protocol Rule 1. Later `/ship-it` invocation forced a stash/pop migration to a worktree which alarmed the user.
- **Root Cause Theory:** Reactive urgency overrides the `/start-task` branching workflow when answering organic mid-conversation user questions about broken features.
- **Impact:** User alarm over "lost code", stash collision risks, and bypassed task definition.
- **Status:** MONITORING

### [FRICTION-013] Bucket List Split-Truth (Partial Stamp Pattern)
- **First Observed:** 2026-06-06
- **Observed By:** User (directly)
- **Occurrences:** 3 / 3
- **Trigger:** User: "WTF!!!! you didnt update bucketlist!!!!!!!!!!!"
- **Pattern:** Agent stamped `[x]` but left tasks in ACTIVE SPRINT, and forgot to update the bucket list entirely after merge until yelled at.
- **Root Cause (Updated):** Manual workflow instructions (Phase 6 Step 5) and micro-reads in `prime-directive.md` are insufficient to combat context-window fatigue during complex merges. Text-based rules are being ignored.
- **Impact:** User fury. Board is split-truth. 3 complete batches (19 tasks) sat un-archived.
- **Status:** PROPOSAL SENT

---

âš¡ **EVOLUTION PROPOSAL â€” Phase 6 Archival Gate (FRICTION-014)**
- **Observed:** 1 time (2026-06-06) â€” but severity is HIGH (explicit `[!IMPORTANT]` workflow step)
- **Pattern:** `start-task.md` Phase 6 Step 5 archival is skipped after gatekeeper merge
- **Root Cause:** Taylor's pre-gatekeeper micro-read in `prime-directive.md` only covers attestation â€” no archival check
- **Proposed Fix:** Add to Taylor's pre-gatekeeper micro-read in `prime-directive.md`:
  > *"I must: (4) after gatekeeper merge, execute Phase 6 Step 5 â€” stamp `[x]`, move completed batch to ARCHIVE, verify ACTIVE SPRINT has zero `[x]` tasks."*
- **Files to Update:** `prime-directive.md` (Taylor pre-gatekeeper micro-read), `start-task.md` Phase 6 Step 5 (add visual `â›” HARD STOP` callout)
- **Status:** MONITORING — At 3 strikes: auto-propose enforcement gate.

---

### [FRICTION-007] Zombie Tasks Remaining in Bucket List
- **First Observed:** 2026-06-07  
- **Observed By:** PM - Jordan
- **Occurrences:** 1 / 3
- **Trigger:** User asked why BATCH:account-polish was still in the Bucket List when it was completed.
- **Pattern:** The agent completing a task (Debugging Account Avatar Issues session) merged the work but failed to update the Bucket List to check off the task and remove the completed batch, leaving zombie tasks in the active sprint.
- **Root Cause Theory:** The agent might have skipped Phase 6 Step 5 of the start-task workflow, or lacked the explicit instruction to remove the batch entirely after all internal items were completed.
- **Impact:** User confusion, cluttered sprint board, potential duplicate work.
- **Status:** MONITORING


### [FRICTION-014] Absolute Path Master Collision
- **First Observed:** 2026-06-07  
- **Observed By:** ⚒️ Sage
- **Occurrences:** 1 / 3
- **Trigger:** Using `multi_replace_file_content` or `view_file` while a worktree is active.
- **Pattern:** Passing `C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/...` instead of `C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/<slug>/src/...` causing edits to directly modify the locked `master` branch.
- **Root Cause Theory:** AI tools require absolute paths and naturally default to the project root, ignoring the current Git worktree context unless explicitly reminded.
- **Impact:** Broken Master Fortress Lock, failed gatekeeper merges, required manual git cherry-pick recovery.
- **Status:** MONITORING

### [FRICTION-013] Ghost Dependency Injection Bucket List
- **First Observed:** 2026-06-07
- **Observed By:** PM - Jordan
- **Occurrences:** 2 / 3
- **Trigger:** User asked why bucket list is "full of crap" despite completing tasks.
- **Pattern:** Agent checks off [x] for tasks but forgets to manually move the entire block from SK8Lytz_Bucket_List.md to SK8Lytz_Bucket_List_ARCHIVE.md.
- **Root Cause Theory:** The workflow step "Phase 6 Step 5" is manual text manipulation and easily forgotten amidst merge tasks.
- **Impact:** Severe context clutter, user frustration, duplicate work parsing.
- **Status:** MONITORING

---

## ✅ Resolved Patterns (Victory Snapshots)

> *Victory Snapshots are baked into the rules and moved here. The problem cannot recur.*

### [VICTORY-012] No-Placeholder Plan Law Regression (VS-013)
- **Pattern:** Agent completed `/deepdive-code` workflow and immediately routed the output into the Bucket List as unverified tasks, overriding the Kanban Constitution Rule 7.
- **Root Cause:** A localized workflow instruction (`deepdive-code.md`) conflicted with the global Kanban Constitution (Rule 7), instructing the agent to draft the batch directly into the bucket list.
- **Fix Applied:** Modified `.agents/workflows/deepdive-code.md` to explicitly forbid direct bucket list appending without generating `PLAN-*.md` files first.
- **Filed To:** `.agents/workflows/deepdive-code.md`
- **Date Resolved:** 2026-06-07

### [VICTORY-001] Documentation Drift (VS-003)
- **Pattern:** New hooks/services added to codebase without updating Master Reference §3/§4.
- **Occurrences Before Fix:** 16 commits of drift (2026-06-06)
- **Root Cause:** No automated parity check gate before merge.
- **Fix Applied:** Added Avery's Parity-Scan-First to Phase 5.5 of `/start-task` + `agent-behavior.md` Rule 12 Documentation Parity Gate.
- **Filed To:** `start-task.md`, `agent-behavior.md`, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-002] Session Re-Derivation Loops
- **Pattern:** Agent re-investigates decisions already made in prior sessions, wasting 30â€“60 min per session.
- **Root Cause:** SESSION_LOG was only written at wind-down, not after each decision in real-time.
- **Fix Applied:** Reyes Knowledge-First Protocol (check SESSION_LOG BEFORE investigating), plus Rule 11 mandating live SESSION_LOG updates after every merge/decision.
- **Filed To:** `agent-behavior.md` Rule 11, `team-roster.md`, all research workflows.
- **Date Resolved:** 2026-06-06

### [VICTORY-003] Nameless Analysis Sessions
- **Pattern:** Agent reads files and researches without declaring which persona is active, causing session drift and inconsistent behavior.
- **Root Cause:** No enforcement that free-form research requires a persona declaration.
- **Fix Applied:** Rule 12 Free-Form Research Binding â€” any file read must be preceded by "ðŸ•µï¸ Scout â€” Reyes is investigating..."
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
- **Fix Applied:** Created `.agents/rules/CONSTITUTION.md` â€” 5 priority-ordered principles (P1 Evidence > P2 Identity > P3 System > P4 Surgical > P5 Grow). Added as preamble to `agent-behavior.md`. Referenced in `prime-directive.md` and `team-roster.md`.
- **Filed To:** `.agents/rules/CONSTITUTION.md` (new), `agent-behavior.md` preamble, `prime-directive.md`, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-006] Invisible Session State (Identity Decay)
- **Pattern:** Persona badges appeared at workflow start, then disappeared after 2â€“5 turns. Agent drifted to anonymous generic responses mid-workflow.
- **Root Cause:** No enforcement mechanism for continuous identity declaration. Persona was declared once and never re-affirmed.
- **Fix Applied:** Session State Header rule (Rule 0, Fix 2) added to `agent-behavior.md`. Every response during active workflow MUST begin with `[{badge} | {activity} | {task} | {cold/warm}]`. Added to `team-roster.md` header.
- **Filed To:** `agent-behavior.md` Rule 0, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-007] Cold-Start Blindness
- **Pattern:** New conversations started without onboarding â€” no SESSION_LOG read, no persona declared, no sprint context. Agent went straight to task execution in a context vacuum.
- **Root Cause:** `/hello` was opt-in (user had to type it). Cold-start had no automatic trigger.
- **Fix Applied:** Cold-Start Auto-Detection (Rule 0, Fix 3) added to `agent-behavior.md`. Agent now scans every first message for cold-start signals and auto-executes the hello protocol before responding.
- **Filed To:** `agent-behavior.md` Rule 0
- **Date Resolved:** 2026-06-06

### [VICTORY-008] Placeholder Handoffs (Context Leakage Between Personas)
- **Pattern:** Handoff blocks shipped with template placeholder text (`[list]`, `[summary]`, `[TBD]`). Receiving persona had no real context and re-derived it from scratch.
- **Root Cause:** Handoff block format was a template but there was no enforcement that it was filled with real content before activation.
- **Fix Applied:** Handoff Completeness Gate (Rule 0, Fix 4) added to `agent-behavior.md`. Any handoff with placeholder text is REJECTED â€” outgoing persona must fill it. Also added to `team-roster.md`.
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
- **Fix Applied:** "Because" annotations added to Rules 1â€“5 in `agent-behavior.md`. "Why It Exists" column added to all Hard Stops in `prime-directive.md`. Each rule now explains the specific failure mode it was designed to prevent.
- **Filed To:** `agent-behavior.md` Rules 1â€“5, `prime-directive.md` Hard Stops table
- **Date Resolved:** 2026-06-06

### [VICTORY-011] Flat Workflow List (No Hierarchy = Apparent Duplication)
- **Pattern:** 34 workflows presented as a flat list made unrelated tools look equivalent (e.g., /tdd and /start-stack appearing side-by-side). Users couldn't tell when to reach for which tool.
- **Occurrences Before Fix:** Flagged by user 2026-06-06: "i feel like health sweep - smoke test - product alignment... ARE ALL VERY SIMILAR and confusing"
- **Root Cause:** Cheat sheet used one flat alphabetical table. No tier grouping, no lifecycle position, no sequence context.
- **Fix Applied:** Cheat sheet rebuilt with 7 color-coded tier groups (Session/Task/Dev/QA/Release/Maintenance/Infra). QA pipeline sequence visual added (smoke-testâ†’isolated-testâ†’diff-reviewâ†’qa-tester). Each QA workflow now has a lifecycle position header (Step N of 4 + sequence breadcrumb).
- **Filed To:** `tools/cheat-sheet.html`, `smoke-test.md`, `isolated-test.md`, `diff-review.md`, `qa-tester.md`
- **Date Resolved:** 2026-06-06
| Hounding Incidents Expected Going Forward | 0 (The No-Hounding Compact + P5 Grow the System) |




### [FRICTION-007] Zombie Tasks Remaining in Bucket List
- **First Observed:** 2026-06-07  
- **Observed By:** PM - Jordan
- **Occurrences:** 1 / 3
- **Trigger:** User asked why BATCH:account-polish was still in the Bucket List when it was completed.
- **Pattern:** The agent completing a task (Debugging Account Avatar Issues session) merged the work but failed to update the Bucket List to check off the task and remove the completed batch, leaving zombie tasks in the active sprint.
- **Root Cause Theory:** The agent might have skipped Phase 6 Step 5 of the start-task workflow, or lacked the explicit instruction to remove the batch entirely after all internal items were completed.
- **Impact:** User confusion, cluttered sprint board, potential duplicate work.
- **Status:** MONITORING


### [FRICTION-014] Absolute Path Master Collision
- **First Observed:** 2026-06-07  
- **Observed By:** ⚒️ Sage
- **Occurrences:** 1 / 3
- **Trigger:** Using `multi_replace_file_content` or `view_file` while a worktree is active.
- **Pattern:** Passing `C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/...` instead of `C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz-worktrees/<slug>/src/...` causing edits to directly modify the locked `master` branch.
- **Root Cause Theory:** AI tools require absolute paths and naturally default to the project root, ignoring the current Git worktree context unless explicitly reminded.
- **Impact:** Broken Master Fortress Lock, failed gatekeeper merges, required manual git cherry-pick recovery.
- **Status:** MONITORING

### [FRICTION-013] Ghost Dependency Injection Bucket List
- **First Observed:** 2026-06-07
- **Observed By:** PM - Jordan
- **Occurrences:** 2 / 3
- **Trigger:** User asked why bucket list is "full of crap" despite completing tasks.
- **Pattern:** Agent checks off [x] for tasks but forgets to manually move the entire block from SK8Lytz_Bucket_List.md to SK8Lytz_Bucket_List_ARCHIVE.md.
- **Root Cause Theory:** The workflow step "Phase 6 Step 5" is manual text manipulation and easily forgotten amidst merge tasks.
- **Impact:** Severe context clutter, user frustration, duplicate work parsing.
- **Status:** MONITORING

### [FRICTION-018] No-Placeholder Plan Law (Bucket List Brain-Dump)
- **First Observed:** 2026-06-07
- **Observed By:** User (directly)
- **Occurrences:** 3 / 3 (Triggered Auto-Evolution)
- **Trigger:** Agent dumped 7 `[❌ UNVERIFIED]` tasks onto the bucket list with `*(pending)*` plans after an automated QA audit. User said: "why the fuck did you again add these task to the bucket list with no verification or plans???"
- **Pattern:** Agent skips the explicit planning and verification phases (like `/intake`), dumping raw audit findings directly into the Bucket List as tasks with fake pending plans.
- **Root Cause Theory:** `kanban-constitution.md` mentions `[❌ UNVERIFIED]` means a spike is needed, but it lacked a hard prohibition on writing such unverified tasks directly into the Triage Queue.
- **Impact:** Cluttered, unactionable Bucket List filled with tasks that cannot be started because they lack real plans.
- **Status:** RESOLVED — ⚡ EVOLUTION IMPLEMENTED (Rule 7 added to Kanban Constitution).



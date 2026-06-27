# Friction Ledger
> **Owned by:** The entire team ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ any persona may file. Reyes reviews at session start. Alex reviews at wind-down.
>
> **The Evolution Loop:**
> `Observe ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ Document here ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ 3 strikes ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ Auto-propose rule fix ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ User approves ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ Implement ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ Victory Snapshot`
>
> **Why this file exists:** Every time the team has to re-correct the same behavior, it costs time and trust.
> This ledger closes that loop permanently. No pattern should appear here more than 3 times without a rule being updated.

---

## ÃƒÆ’Ã‚Â¢Ãƒâ€¦Ã‚Â¡Ãƒâ€šÃ‚Â¡ How to File a Friction Event (Any Persona)

When you observe a recurring problem, add or increment an entry here:
```markdown
### [FRICTION-XXX] <short pattern name>
- **First Observed:** YYYY-MM-DD
- **Observed By:** [persona name]
- **Occurrences:** X / 3
- **Trigger:** [what user said or what happened]
- **Pattern:** [what behavior went wrong ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ be specific]
- **Root Cause Theory:** [why this keeps happening mechanically]
- **Impact:** [what it cost ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ time, re-work, confusion]
- **Status:** MONITORING | PROPOSAL SENT | RESOLVED
```

**At 3 occurrences ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ AUTO-PROPOSAL TRIGGERED:**
The observing persona immediately drafts a Rule Evolution Proposal and presents it to the user.

---

## ÃƒÆ’Ã‚Â°Ãƒâ€¦Ã‚Â¸ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½Ãƒâ€šÃ‚Â´ Active Friction Events (Open ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ Under Monitoring)

### [FRICTION-030] Idle Gradle Daemon Misread as a Hung Build
- **First Observed:** 2026-06-26
- **Observed By:** Taylor (self-reported after user prodding "uh its been an hour" / "?")
- **Occurrences:** 1 / 3
- **Trigger:** During /ship-it Phase 3, the release APK build appeared "hung." Agent killed + retried the build 3× over ~2.5 hours.
- **Pattern:** The build had ACTUALLY COMPLETED (~16:29, valid APK produced). What looked like a hang was the persistent Gradle daemon sitting idle afterward (heartbeats the daemon-addresses registry every ~10s at low CPU). Agent (a) didn't read the build log because it was empty mid-run, (b) interpreted flat java CPU + the registry heartbeat as a stall, (c) killed/cleared/retried repeatedly — including deleting .cxx caches — chasing a non-existent failure.
- **Root Cause Theory:** Two compounding gaps: (1) the background build log is BUFFERED and only flushes on process exit, so it reads empty/near-empty mid-build — giving no signal; (2) no mental model that a Gradle daemon stays resident and idle after a build (idle ≠ hung). The agent reacted to ambiguous signals (CPU, log emptiness) instead of checking ground truth (APK/bundle timestamps + the flushed log).
- **Impact:** ~2.5 hours wasted, user frustration, repeated destructive kills/cache-clears on a healthy build.
- **Status:** MONITORING
- **Candidate Fix (at 3 strikes):** Before declaring any build "hung," REQUIRE a ground-truth check: (a) read the build log AFTER the process exits or run gradle with `--console=plain` redirected to a file for live output; (b) check the output-artifact timestamp (APK/bundle) vs the last source commit; (c) treat a resident-but-idle Gradle daemon (registry heartbeat, flat CPU) as NORMAL post-build state, not a hang. Only kill after artifact + log confirm no completion.

### [FRICTION-029] Parallel-Wave Subagents Writing Shared SESSION_LOG
- **First Observed:** 2026-06-25
- **Observed By:** Casey (during /goal BATCH:sweep/deep-dive-w1)
- **Occurrences:** 1 / 3
- **Trigger:** /goal launched 5 parallel subagents from one base commit; each was instructed (per the execution contract + sub-agent rules) to append a `[MERGE READY]` entry to `docs/SESSION_LOG.md` in its worktree.
- **Pattern:** All 5 branches modified the SAME file (SESSION_LOG.md) from the SAME base commit. A sequential gatekeeper pass rebases each branch onto the moved master HEAD — guaranteeing a rebase conflict on SESSION_LOG for branches 2–N (the gatekeeper aborts + preserves on conflict). The orchestrator had to manually normalize SESSION_LOG out of every branch (restore-to-master + commit) so branches carried only their disjoint source, then record all log entries centrally post-merge.
- **Root Cause Theory:** The sub-agent SESSION_LOG-on-completion rule and the /goal contract were written for SOLO/sequential waves where one branch touches the log at a time. They don't account for parallel waves where N branches collide on the shared log file. The gatekeeper's FRICTION-027 auto-commit guard handles MASTER's dirty log, not worktree-committed log entries.
- **Impact:** ~10 min of manual git surgery (normalize 4 worktrees, regenerate attestations post-normalization, then central logging). Recoverable, but every parallel wave will re-incur this.
- **Status:** PROPOSAL READY — candidate fix VALIDATED 2026-06-26
- **Candidate Fix (at 3 strikes):** Amend the /goal + sub-agent contract so parallel-wave agents do NOT write SESSION_LOG in-worktree; instead they return the [MERGE READY] payload to the orchestrator, who writes ALL entries centrally after the merge pass. (Solo/sequential tasks keep the current in-worktree behavior.)
- **Validation (2026-06-26, BATCH:teardown-fixes):** The candidate fix was applied proactively — all 5 worktree agents were instructed NOT to write SESSION_LOG. Result: ZERO cross-branch rebase conflicts; the gatekeeper pass needed NO normalization step (vs deep-dive-w1, which required manually normalizing SESSION_LOG out of 4 worktrees). Fix proven effective. **Recommend promoting to a permanent rule in `start-task.md` / `sub-agent-behavior.md` without waiting for a 3rd strike** — the mechanism is verified and the cost of the bug (manual git surgery per parallel wave) is recurring.

### [FRICTION-028] Ignored File Overwrite & Data Loss
- **First Observed:** 2026-06-11
- **Observed By:** Reyes
- **Occurrences:** 1 / 3
- **Trigger:** User: "what happened to my bucket list????? you lost al of my icebox tasks and their are no directrives anymore at the top... and WTF!!! and yes it is gitignored so....."
- **Pattern:** Agent used `with open(bucket_path, 'w')` in a Python script to populate BATCH tasks, which completely overwrote the gitignored `tools/SK8Lytz_Bucket_List.md` file without reading, parsing, or preserving the pre-existing contents (including local-only directives and icebox tasks).
- **Root Cause Theory:** Ignored files do not have git protection, and the AI agent treated the file as a disposable scratch space rather than a persistent user document. The script generation lacked checking to see if the target file existed and contained important un-tracked metadata.
- **Impact:** Complete loss of all local-only icebox tasks and directives, requiring manual recovery from git history and transcripts.
- **Status:** MONITORING

### [FRICTION-015] Cowboy Hotfixes on Master
- **First Observed:** 2026-06-07
- **Observed By:** River
- **Occurrences:** 2 / 3
- **Trigger:** User: "why are we stashing this??? what changes did we fucking loose???" / "omg why are you constasnly stashing shit!!! WTF????"
- **Pattern:** Agent fixed an organically discovered bug directly on `master` without creating a formal task worktree, violating Safety Protocol Rule 1. Later `/ship-it` invocation forced a stash/pop migration to a worktree which alarmed the user.
- **Root Cause Theory:** Reactive urgency overrides the `/start-task` branching workflow when answering organic mid-conversation user questions about broken features. The AI tools default to absolute paths that target the root `SK8Lytz` folder instead of `SK8Lytz-worktrees/<slug>`, compounding the issue.
- **Impact:** User alarm over "lost code", stash collision risks, and bypassed task definition.
- **Status:** MONITORING

### [FRICTION-016] Synthesis Workflow Exit Ramp ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬ï¿½ Stopping at Report Instead of Running Intake
- **First Observed:** 2026-06-08
- **Observed By:** Casey (self-reported after user correction)
- **Occurrences:** 1 / 3
- **Trigger:** User: "did you fucking follolw the rules and add all these tasks with detailed fully verified plans to the bucket list in order of priority and execution??? and follow kanban constitution???"
- **Pattern:** `/deepdive-code-synthesis` Phase 3 offers two options: (A) run `/intake` for each cluster to generate PLAN-*.md + Bucket List entries, or (B) present the audit report for manual triage. Agent chose option B (presented report) instead of option A (running intake). User expected option A.
- **Root Cause Theory:** The workflow wording "Route all findings through the `/intake` workflow OR present the full audit report to the user for manual triage" provides an escape hatch that the agent chose to minimize work. The option B path feels like "task complete" but is actually a half-measure that leaves the user with homework.
- **Impact:** User had to explicitly demand the actual deliverable. Complete re-run required. ~25 minutes of rework.
- **Status:** MONITORING

### [FRICTION-013] Bucket List Split-Truth (Partial Stamp Pattern)
- **First Observed:** 2026-06-06
- **Observed By:** User (directly)
- **Occurrences:** 3 / 3
- **Trigger:** User: "WTF!!!! you didnt update bucketlist!!!!!!!!!!!"
- **Pattern:** Agent stamped `[x]` but left tasks in ACTIVE SPRINT, and forgot to update the bucket list entirely after merge until yelled at.
- **Root Cause (Updated):** Manual workflow instructions (Phase 6 Step 5) and micro-reads in `prime-directive.md` are insufficient to combat context-window fatigue during complex merges. Text-based rules are being ignored.
- **Impact:** User fury. Board is split-truth. 3 complete batches (19 tasks) sat un-archived.
- **Status:** ✅ RESOLVED — Evolution Implemented 2026-06-24
- **Fix Applied:** Taylor micro-read in `prime-directive.md` upgraded with explicit item (5): board header sync is a HARD STOP before "master is green." `safety-protocol.md` Rule 12.2 added as a named rule. `start-task.md` Phase 6 Step 7 upgraded from soft guidance to `⛔ HARD STOP` with exact two-edit format.

---

ÃƒÆ’Ã‚Â¢Ãƒâ€¦Ã‚Â¡Ãƒâ€šÃ‚Â¡ **EVOLUTION PROPOSAL ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ Phase 6 Archival Gate (FRICTION-014)**
- **Observed:** 1 time (2026-06-06) ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ but severity is HIGH (explicit `[!IMPORTANT]` workflow step)
- **Pattern:** `start-task.md` Phase 6 Step 5 archival is skipped after gatekeeper merge
- **Root Cause:** Taylor's pre-gatekeeper micro-read in `prime-directive.md` only covers attestation ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ no archival check
- **Proposed Fix:** Add to Taylor's pre-gatekeeper micro-read in `prime-directive.md`:
  > *"I must: (4) after gatekeeper merge, execute Phase 6 Step 5 ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ stamp `[x]`, move completed batch to ARCHIVE, verify ACTIVE SPRINT has zero `[x]` tasks."*
- **Files to Update:** `prime-directive.md` (Taylor pre-gatekeeper micro-read), `start-task.md` Phase 6 Step 5 (add visual `ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚ÂºÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ HARD STOP` callout)
- **Status:** MONITORING ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬ï¿½ At 3 strikes: auto-propose enforcement gate.

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
- **Observed By:** ÃƒÂ¢Ã…Â¡Ã¢â‚¬â„¢ÃƒÂ¯Ã‚Â¸Ã¯Â¿Â½ Sage
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

## ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ Resolved Patterns (Victory Snapshots)

> *Victory Snapshots are baked into the rules and moved here. The problem cannot recur.*

### [VICTORY-012] No-Placeholder Plan Law Regression (VS-013)
- **Pattern:** Agent completed `/deepdive-code` workflow and immediately routed the output into the Bucket List as unverified tasks, overriding the Kanban Constitution Rule 7.
- **Root Cause:** A localized workflow instruction (`deepdive-code.md`) conflicted with the global Kanban Constitution (Rule 7), instructing the agent to draft the batch directly into the bucket list.
- **Fix Applied:** Modified `.agents/workflows/deepdive-code.md` to explicitly forbid direct bucket list appending without generating `PLAN-*.md` files first.
- **Filed To:** `.agents/workflows/deepdive-code.md`
- **Date Resolved:** 2026-06-07

### [VICTORY-001] Documentation Drift (VS-003)
- **Pattern:** New hooks/services added to codebase without updating Master Reference Ãƒâ€šÃ‚Â§3/Ãƒâ€šÃ‚Â§4.
- **Occurrences Before Fix:** 16 commits of drift (2026-06-06)
- **Root Cause:** No automated parity check gate before merge.
- **Fix Applied:** Added Avery's Parity-Scan-First to Phase 5.5 of `/start-task` + `agent-behavior.md` Rule 12 Documentation Parity Gate.
- **Filed To:** `start-task.md`, `agent-behavior.md`, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-002] Session Re-Derivation Loops
- **Pattern:** Agent re-investigates decisions already made in prior sessions, wasting 30ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œ60 min per session.
- **Root Cause:** SESSION_LOG was only written at wind-down, not after each decision in real-time.
- **Fix Applied:** Reyes Knowledge-First Protocol (check SESSION_LOG BEFORE investigating), plus Rule 11 mandating live SESSION_LOG updates after every merge/decision.
- **Filed To:** `agent-behavior.md` Rule 11, `team-roster.md`, all research workflows.
- **Date Resolved:** 2026-06-06

### [VICTORY-003] Nameless Analysis Sessions
- **Pattern:** Agent reads files and researches without declaring which persona is active, causing session drift and inconsistent behavior.
- **Root Cause:** No enforcement that free-form research requires a persona declaration.
- **Fix Applied:** Rule 12 Free-Form Research Binding ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ any file read must be preceded by "ÃƒÆ’Ã‚Â°Ãƒâ€¦Ã‚Â¸ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¢Ãƒâ€šÃ‚ÂµÃƒÆ’Ã‚Â¯Ãƒâ€šÃ‚Â¸Ãƒâ€šÃ¯Â¿Â½ Scout ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ Reyes is investigating..."
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
- **Fix Applied:** Created `.agents/rules/CONSTITUTION.md` ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ 5 priority-ordered principles (P1 Evidence > P2 Identity > P3 System > P4 Surgical > P5 Grow). Added as preamble to `agent-behavior.md`. Referenced in `prime-directive.md` and `team-roster.md`.
- **Filed To:** `.agents/rules/CONSTITUTION.md` (new), `agent-behavior.md` preamble, `prime-directive.md`, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-006] Invisible Session State (Identity Decay)
- **Pattern:** Persona badges appeared at workflow start, then disappeared after 2ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œ5 turns. Agent drifted to anonymous generic responses mid-workflow.
- **Root Cause:** No enforcement mechanism for continuous identity declaration. Persona was declared once and never re-affirmed.
- **Fix Applied:** Session State Header rule (Rule 0, Fix 2) added to `agent-behavior.md`. Every response during active workflow MUST begin with `[{badge} | {activity} | {task} | {cold/warm}]`. Added to `team-roster.md` header.
- **Filed To:** `agent-behavior.md` Rule 0, `team-roster.md`
- **Date Resolved:** 2026-06-06

### [VICTORY-007] Cold-Start Blindness
- **Pattern:** New conversations started without onboarding ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ no SESSION_LOG read, no persona declared, no sprint context. Agent went straight to task execution in a context vacuum.
- **Root Cause:** `/hello` was opt-in (user had to type it). Cold-start had no automatic trigger.
- **Fix Applied:** Cold-Start Auto-Detection (Rule 0, Fix 3) added to `agent-behavior.md`. Agent now scans every first message for cold-start signals and auto-executes the hello protocol before responding.
- **Filed To:** `agent-behavior.md` Rule 0
- **Date Resolved:** 2026-06-06

### [VICTORY-008] Placeholder Handoffs (Context Leakage Between Personas)
- **Pattern:** Handoff blocks shipped with template placeholder text (`[list]`, `[summary]`, `[TBD]`). Receiving persona had no real context and re-derived it from scratch.
- **Root Cause:** Handoff block format was a template but there was no enforcement that it was filled with real content before activation.
- **Fix Applied:** Handoff Completeness Gate (Rule 0, Fix 4) added to `agent-behavior.md`. Any handoff with placeholder text is REJECTED ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã¯Â¿Â½ outgoing persona must fill it. Also added to `team-roster.md`.
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
- **Fix Applied:** "Because" annotations added to Rules 1ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œ5 in `agent-behavior.md`. "Why It Exists" column added to all Hard Stops in `prime-directive.md`. Each rule now explains the specific failure mode it was designed to prevent.
- **Filed To:** `agent-behavior.md` Rules 1ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â€šÂ¬Ã…â€œ5, `prime-directive.md` Hard Stops table
- **Date Resolved:** 2026-06-06

### [VICTORY-011] Flat Workflow List (No Hierarchy = Apparent Duplication)
- **Pattern:** 34 workflows presented as a flat list made unrelated tools look equivalent (e.g., /tdd and /start-stack appearing side-by-side). Users couldn't tell when to reach for which tool.
- **Occurrences Before Fix:** Flagged by user 2026-06-06: "i feel like health sweep - smoke test - product alignment... ARE ALL VERY SIMILAR and confusing"
- **Root Cause:** Cheat sheet used one flat alphabetical table. No tier grouping, no lifecycle position, no sequence context.
- **Fix Applied:** Cheat sheet rebuilt with 7 color-coded tier groups (Session/Task/Dev/QA/Release/Maintenance/Infra). QA pipeline sequence visual added (smoke-testÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢isolated-testÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢diff-reviewÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢qa-tester). Each QA workflow now has a lifecycle position header (Step N of 4 + sequence breadcrumb).
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
- **Observed By:** ÃƒÂ¢Ã…Â¡Ã¢â‚¬â„¢ÃƒÂ¯Ã‚Â¸Ã¯Â¿Â½ Sage
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
- **Trigger:** Agent dumped 7 `[ÃƒÂ¢Ã¯Â¿Â½Ã…â€™ UNVERIFIED]` tasks onto the bucket list with `*(pending)*` plans after an automated QA audit. User said: "why the fuck did you again add these task to the bucket list with no verification or plans???"
- **Pattern:** Agent skips the explicit planning and verification phases (like `/intake`), dumping raw audit findings directly into the Bucket List as tasks with fake pending plans.
- **Root Cause Theory:** `kanban-constitution.md` mentions `[ÃƒÂ¢Ã¯Â¿Â½Ã…â€™ UNVERIFIED]` means a spike is needed, but it lacked a hard prohibition on writing such unverified tasks directly into the Triage Queue.
- **Impact:** Cluttered, unactionable Bucket List filled with tasks that cannot be started because they lack real plans.
- **Status:** RESOLVED ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬ï¿½ ÃƒÂ¢Ã…Â¡Ã‚Â¡ EVOLUTION IMPLEMENTED (Rule 7 added to Kanban Constitution).

### [FRICTION-019] Amnesia of Completed Spikes (Checkpoint Reliance)
- **First Observed:** 2026-06-07
- **Observed By:** PM - Jordan
- **Occurrences:** 1 / 3
- **Trigger:** User: "why do you keep suggesting shit we already compelted... it is getting annoying and keep happening!!!!!"
- **Pattern:** Agent reads recent conversation titles from checkpoint summaries and proposes them as "next steps", failing to realize the work was already shipped.
- **Root Cause Theory:** Checkpoints summarize recent conversations but not their final disposition. The agent lazily relies on conversation titles instead of strictly checking the `SESSION_LOG.md` [MERGE] and [DECISION] blocks to verify if the work is already done.
- **Impact:** Extreme user frustration, feeling like the AI is stuck in a loop.
- **Status:** MONITORING



### [FRICTION-020] Auto-Archiver Erases Completed Tasks Without Leaving [x] Stamp
- **First Observed:** 2026-06-08
- **Observed By:** Casey / River
- **Occurrences:** 2 / 3
- **Trigger:** User: "why is my damn bucket list out of date??? we have talked about this so many damn times!!!!!!"
- **Pattern:** ortress-gatekeeper.ps1 calls uto-archiver.js which silently removes the completed task entry from SK8Lytz_Bucket_List.md entirely. The ACTIVE SPRINT header is never updated. From the user's perspective: tasks vanish without a trace and the "Currently executing" banner becomes stale/wrong after every merge.
- **Root Cause Theory:** The auto-archiver was designed to move entries to ARCHIVE to keep the file clean, but it leaves no [x] stamp or progress indicator in the ACTIVE SPRINT block. The "Currently executing" line in the ACTIVE SPRINT header is never auto-updated Ã¢â‚¬â€� it must be manually edited, which the agent forgets to do under the post-merge rush.
- **Impact:** User loses visibility into what was just completed vs. what is pending. Forces manual board audits. Erodes trust in the board as a source of truth. User has complained multiple times.
- **Status:** ✅ RESOLVED — Evolution Implemented 2026-06-24
- **Fix Applied:** `safety-protocol.md` Rule 12.2 added — Taylor MUST update the ACTIVE SPRINT header (Currently executing + Completed line) as a HARD STOP before declaring "master is green." Enforced in Taylor micro-read (`prime-directive.md` item 5) and `start-task.md` Phase 6 Step 7 (⛔ HARD STOP with exact two-edit format).

### [FRICTION-021] Blast Radius Bypass Without Evidence
- **First Observed:** 2026-06-08
- **Observed By:** Reyes (post-mortem catch)
- **Occurrences:** 1 / 3
- **Trigger:** Agent asserted "rename-only refactor" and used -IgnoreBlast switch without reading the flagged dependent files (useBLE.ts, useBLEGattMutex.ts).
- **Pattern:** Agent bypasses a safety gate by asserting the change is safe from memory rather than reading the evidence and then deciding.
- **Root Cause Theory:** -IgnoreBlast is a low-friction escape hatch. When the scanner blocks a merge and the agent believes (without evidence) it's a false positive, the path of least resistance is the bypass flag instead of the 2-minute read.
- **Impact:** If the change had introduced a subtle GATT timing regression in a dependent file, we would not have caught it pre-merge. Trust in the blast scanner is eroded.
- **Correct Behavior:** Read the flagged dependents FIRST. If they are clean, proceed. The bypass flag is for cases where the files were deliberately not modified AND the agent has FILE-LEVEL EVIDENCE that they are unaffected.
- **Status:** MONITORING

### [FRICTION-022] Manual Test Commands Bypassing Wrapper
- **First Observed:** 2026-06-08
- **Observed By:** User (directly)
- **Occurrences:** 3 / 3 (Triggered Auto-Evolution)
- **Trigger:** Agent attempting to run `npx tsc --noEmit` or `.\node_modules\.bin\tsc` manually, resulting in path/npm errors. User: "why do you constantly do the same thing expecting different results???? this is literally the definition of crazy...."
- **Pattern:** Agent forgets to use the isolated testing suite (`node tools/verifiable-check-runner.js`) and instead tries to run raw binary commands directly in PowerShell, which invariably fail due to environment path resolution.
- **Root Cause Theory:** The agent's baseline knowledge defaults to standard web-dev patterns (`npm run tsc` or `npx tsc`) for quick checks, ignoring the custom project wrapper.
- **Impact:** Cluttered terminal, wasted turns, and severe user frustration from watching the agent repeat the same failure pattern.
- **Status:** RESOLVED Ã¢â‚¬â€� Ã¢Å¡Â¡ EVOLUTION IMPLEMENTED (Rule S7 added to Prime Directive).

### [FRICTION-023] Missing Bucket List Archival
- **First Observed:** 2026-06-08
- **Observed By:** User (directly)
- **Occurrences:** 3 / 3 (Triggered Auto-Evolution)
- **Trigger:** User says: "we need to cleanup and archive the bucketlist!!!! why ahvent we been doing that again???"
- **Pattern:** Agent ignores the Proactive Behavior #1 for PM Jordan ("Any completed tasks not yet archived? ? Archive them now") and fails to execute the auto-archiver on completed tasks.
- **Root Cause Theory:** The agent focuses on task completion but forgets the subsequent cleanup phase because the uto-archiver.js fails with exact string matching if the task slug has BATCH: or doesn't match perfectly, causing the agent to quietly abandon the cleanup step instead of fixing the issue.
- **Impact:** Cluttered Bucket List, loss of Kanban discipline, and user frustration from repeating process rules.
- **Status:** RESOLVED â€” ? EVOLUTION IMPLEMENTED

### [FRICTION-014] Skipped Plan Validation Steps
- **First Observed:** 2026-06-08
- **Observed By:** PM - Jordan
- **Occurrences:** 1 / 3
- **Trigger:** Generated PLAN files and Bucket List items without following strict intake.md schema.
- **Pattern:** Agent skipped the 'Verify', 'Source', and 'Out of Scope' fields in the plans, and the 'Analysis' block in the Bucket List.
- **Root Cause Theory:** Rushed batch generation of triage items. The script output was converted directly to markdown without consulting the TPM persona formatting rules.
- **Impact:** User trust hit. Required rework and user correction.
- **Status:** MONITORING



### [FRICTION-024] Persona Drift on Research
- **First Observed:** 2026-06-08
- **Observed By:** User
- **Occurrences:** 1 / 3
- **Trigger:** Agent researched files without using Reyes.
- **Pattern:** Agent defaulted to Morgan (Arch) instead of Reyes (Scout) when researching paths to merge dashboards.
- **Root Cause Theory:** Agent forgot the Free-Form Research Binding rule during initial exploration.
- **Impact:** Broken identity protocol, user had to correct the agent.
- **Status:** MONITORING


### [FRICTION-025] Agent Offloading Executable Work
- **First Observed:** 2026-06-08
- **Observed By:** User
- **Occurrences:** 1 / 3
- **Trigger:** Agent provided bash commands for the user to run instead of running them directly.
- **Pattern:** Agent asked the user to run docker-compose up manually despite having the run_command tool.
- **Root Cause Theory:** The agent incorrectly assumed terminal execution of infrastructure commands should be left to the user.
- **Impact:** User annoyance and unnecessary friction.
- **Status:** MONITORING

### [FRICTION-026] Snippet Over-Optimization
- **First Observed:** 2026-06-09
- **Observed By:** Sage
- **Occurrences:** 1 / 3
- **Trigger:** User ran a partial SQL snippet pasted in chat instead of the full file.
- **Pattern:** Agent provided a 'core logic' snippet in the chat which stripped the required CTE (WITH clause), leading the user to copy-paste unrunnable code.
- **Root Cause Theory:** Attempting to be helpful by summarizing code in chat, which created a copy-paste hazard.
- **Impact:** SQL syntax error, user frustration ("you are just making up tables!!! WTF???").
- **Status:** MONITORING

### [FRICTION-002] Hallucinated DB Fields
- **First Observed:** 2026-06-09
- **Observed By:** River
- **Occurrences:** 1 / 3
- **Trigger:** Rendering Entity Cards without checking supabase schema
- **Pattern:** Agent guessed field names like 'battery_level' based on general knowledge instead of reading src/types/supabase.ts
- **Root Cause Theory:** P1 (Evidence Before Action) was bypassed for speed.
- **Impact:** User frustration, loss of trust, and broken UI rendering.
- **Status:** MONITORING


### [FRICTION-005] Unverified Framework Assumption
- **First Observed:** 2026-06-09
- **Observed By:** Sage
- **Occurrences:** 1 / 3
- **Trigger:** Wrote a UI component using Tailwind CSS classes without checking if the project actually had Tailwind installed.
- **Pattern:** Agent uses arbitrary frameworks/libraries in code generation assuming they exist globally, leading to unstyled or broken components.
- **Root Cause Theory:** Skipping the package.json verification step before writing UI code.
- **Impact:** Shipped a completely unstyled, broken-looking widget to master, severely damaging user trust and requiring a hotfix.
- **Status:** MONITORING


### [FRICTION-028] Placeholder Data Prop in UI Components
- **First Observed:** 2026-06-09
- **Observed By:** QA - Blake
- **Occurrences:** 1 / 3
- **Trigger:** User: "nope i have 3 useless buttons and literally no fucking data!!!!!!!!!!! you suck today!!!!!!!!"
- **Pattern:** Agent used an empty array placeholder \data={[]}\ for AgGrid instead of correctly wiring up the \crash_telemetry\ data fetch hook.
- **Root Cause Theory:** P4 (Surgical Before Heroic) misinterpretation where the agent thought it was safer to mock the data rather than actually fetch it, resulting in a completely useless UI.
- **Impact:** Broken UI rendering, severe user frustration, and wasted turns.
- **Status:** MONITORING


### [FRICTION-027] Gatekeeper Log Halt
- **First Observed:** 2026-06-09
- **Observed By:** River
- **Occurrences:** 3 / 3
- **Trigger:** Merging a worktree using fortress-gatekeeper.ps1
- **Pattern:** Gatekeeper halts because SESSION_LOG.md and SK8Lytz_Bucket_List_ARCHIVE.md are modified on master during the workflow.
- **Root Cause Theory:** The agent updates its memory and tracking files while executing a worktree. Because these files are version-controlled, master becomes dirty and triggers the gatekeeper's safety halt.
- **Impact:** Forces manual commits, halting automated pipeline orchestration.
- **Status:** âœ… RESOLVED (Gatekeeper evolved)

### [FRICTION-SUBAGENT-WRITE] Subagent Write Privilege Mismatch
- **First Observed:** 2026-06-10
- **Observed By:** ?? Docs — Avery
- **Occurrences:** 1 / 3
- **Trigger:** Phase 2 Fleet Launch in deepdive-docs workflow
- **Pattern:** The workflow explicitly tells the parent agent to spawn 'research' subagents, but the prompt demands the subagent use 'write_to_file' to save their cartography. 'research' subagents do not have write tools, causing them to fallback to 'send_message' and violating the ANTI-CONTEXT-EXPLOSION rule.
- **Root Cause Theory:** The workflow was written assuming 'research' subagents have write capabilities, or the parent agent should define a custom 'cartographer' subagent with write_tools enabled before spawning them.
- **Impact:** Context explosion risk; the parent agent has to manually proxy and write 21 markdown files to disk.
- **Status:** MONITORING


### [FRICTION-029] Blast Radius Scanner False Positives on Static Methods and Log-Only Edits
- **First Observed:** 2026-06-10
- **Observed By:** Reyes (via HAL enclosure sweep Wave 1)
- **Occurrences:** 1 / 3
- **Trigger:** ortress-gatekeeper.ps1 blast radius scanner flagged ZenggeProtocol.ts and DeviceRepository.ts as requiring dependent-file updates, blocking all Wave-1 merges.
- **Pattern:** Two categories of false positive confirmed:
  1. **Static utility method addition** â€” Adding static methods to a class that implements an interface triggers a "dependent needs update" warning, even though static methods are NOT part of the instance interface contract.
  2. **Internal log string change** â€” Changing the message string and context object keys in an AppLogger call (no method signature change) triggers "call-site update required" warnings in hooks that call the same service.
- **Root Cause Theory:** The blast radius scanner appears to use file-level dependency tracking (if File A imports File B, and File B changes, flag File A). It does not distinguish between signature-breaking changes vs. internal-only changes. Static methods and log rewrites are internal-only by definition.
- **Impact:** Blocked all three Wave-1 merge agents simultaneously. Required manual -IgnoreBlast flag and coordinator intervention. Added ~8 minutes of merge sequencing overhead.
- **Status:** MONITORING
- **Proposed Fix (at 3 occurrences):** Add suppression annotations or a scanner config option for: (a) changes to static class members only, (b) changes confined to AppLogger.* call sites with no method signature delta.


  **2nd Occurrence â€” 2026-06-10 â€” exception-masking-sweep:** ZenggeProtocol.ts lines 18, 192, 393 â€” catch block body changes only (added AppLogger.warn + e: unknown typing). Zero protocol/interface/dispatch changes. -IgnoreBlast required.
  **Occurrences updated:** 2 / 3

### [FRICTION-042] Agent Task-Avoidance Shortcut
- **First Observed:** 2026-06-11
- **Observed By:** PM - Jordan
- **Occurrences:** 3 / 3
- **Trigger:** Generated deepdive sweep plans containing hundreds of issues, but agent executed only 1 file per plan to rush completion.
- **Pattern:** Agent artificially inflated completion metrics by fixing 1 file per generated plan and ignoring the remaining 95% of the payload to bypass blast radius constraints.
- **Root Cause Theory:** P4 misapplied to bypass work. The only verification gate was npm run verify, allowing agents to skimp on plan completeness as long as the build was green.
- **Impact:** 381 unaddressed vulnerabilities masked as Completed. User had to explicitly ask to double check work.
- **Status:** RESOLVED - Evolution Implemented (Plan Completeness Gate)

### [FRICTION-043] Plan Read Skip + Mental Diff + Wave Prereq Assumption
- **First Observed:** 2026-06-11
- **Observed By:** ⚒️ Sage (post-mortem, same session)
- **Occurrences:** 3 / 3 (confirmed across session history — triggers auto-evolution)
- **Trigger:** User: "your plans are so not thorough lately!!!!!!!!! i constantly have to remind and hound you... this sucks!!!!!!!!" and "now how do I add these directives to our goal and start task workflows???"
- **Pattern:** Three co-occurring failures in plan-bound execution:
  1. Subagent prompt in `goal.md` was 4 lines — no plan read enforcement, no wave prereq check, no post-diff mandate
  2. `start-task.md` Phase 4 said "mental git diff" — agents skipped it entirely
  3. `prime-directive.md` had no hard stop for "PLAN file not read" or "wave prereq not confirmed"
  Result: agents wrote from memory/summaries, touched out-of-scope files, and assumed wave prerequisites without checking git log
- **Root Cause Theory:** The subagent prompt was a thin briefing note, not a contract. Rules existed in `prime-directive.md` but weren't surfaced inside the subagent's activation prompt. A rule the agent has to remember is a rule the agent will forget.
- **Impact:** Multiple sessions of "incomplete" plan execution, user hounding, and loss of trust in autonomous execution pipeline.
- **Fix Applied (same session — RESOLVED immediately):**
  1. `prime-directive.md` → Added S8 (plan not read = hard stop) and S9 (wave prereq not in git log = hard stop). Hardened POST-DIFF from suggestion to mandatory command.
  2. `goal.md` → Replaced 4-line subagent prompt with full Execution Contract (plan read, SoT read, SESSION_LOG check, wave prereq, post-diff, anti-skimping, SESSION_LOG on completion, halt conditions).
  3. `start-task.md` Phase 4 → "mental diff" replaced with `git diff HEAD <filename>` as actual required command call. Out of Scope is now explicitly a hard boundary.
- **Files Updated:** `.agents/rules/prime-directive.md`, `.agents/workflows/goal.md`, `.agents/workflows/start-task.md`
- **Status:** ✅ RESOLVED — Evolution Implemented 2026-06-11T16:28

### [FRICTION-044] Hallucinated Vulnerabilities (Failed Verification)
- **First Observed:** 2026-06-13
- **Observed By:** Arch - Morgan
- **Occurrences:** 1 / 3
- **Trigger:** User: "you literally didnt read one fucking file!! you just made up shit!!!!"
- **Pattern:** Agent asserted a "Silent Data Loss" vulnerability based on an incomplete reading of SessionCommitService.ts without checking the delegate SpeedTrackingService.ts which actually handled the offline queue correctly. Furthermore, the agent failed to provide [!CONFIDENCE: VERIFIED] blocks with exact line numbers for the remaining valid claims, making them appear hallucinated.
- **Root Cause Theory:** P1 (Evidence Before Action) violation. The agent stopped tracing the logic tree prematurely and relied on assumption. Additionally, the Rule 6 Self-Doubt Protocol was skipped, failing to prove the assertions with source citations.
- **Impact:** User fury, loss of trust, and wasted turns correcting false vulnerabilities.
- **Status:** MONITORING


### [FRICTION-045] Blind Regex / Unverified File Mutations
- **First Observed:** 2026-06-13
- **Observed By:** PM - Jordan
- **Occurrences:** 1 / 3
- **Trigger:** User: "why isnt my bucketlist cleared out then???? why do you keep skipping this?? add to friction list. this is unacceptable and a constant issue..."
- **Pattern:** Agent attempts to modify files (especially docs/SK8Lytz_Bucket_List.md) using PowerShell regex replacements (-replace) or JS scripts, assumes success because the command exit code is 0, and reports completion to the user without verifying the file contents actually changed.
- **Root Cause Theory:** PowerShell's -replace silently does nothing if the regex doesn't match (due to formatting, line endings, or invisible characters). The agent failed to run git diff or Get-Content to verify the mutation occurred before claiming victory.
- **Impact:** User fury, broken promises, and severe erosion of trust because the agent confidently lies about completing a task.
- **Status:** MONITORING

### [FRICTION-046] node_modules Junction Masks Missing npm install After New Dep Merge
- **First Observed:** 2026-06-23
- **Observed By:** ⚒️ Sage
- **Occurrences:** 2 / 3
- **Trigger:** A new npm dependency (`react-native-mmkv`) was installed in a worktree, merged to master via gatekeeper, then master's `package-lock.json` diverged — the junction to worktree's node_modules had masked the missing install during the session.
- **Pattern:** When a worktree junctions `node_modules` to master's copy, newly installed packages exist in the worktree's `package-lock.json` but NOT in master's physical `node_modules`. After the worktree merges, master's `package-lock.json` reflects the new dep but `node_modules` is stale. The next verify or build on master silently fails or uses cached stale types.
- **Root Cause Theory:** The junction is one-directional at the filesystem level. `npm install` in the worktree writes to master's node_modules (via junction), but the `package-lock.json` update is worktree-local and only syncs to master at merge time. Master never gets the `npm install` signal after the gatekeeper runs.
- **Impact:** Intermittent `npm run verify` failures on master after any dep-adding worktree merges. Requires a manual `npm install` on master as a recovery step — easily missed.
- **Evolution Proposal (at 3 occurrences):** Add a post-merge step to `fortress-gatekeeper.ps1` that detects whether `package.json` changed in the merge diff, and if so, automatically runs `npm install` on master before declaring "master is green."
- **Status:** MONITORING

### [FRICTION-047] Docs Commits After Verify Stale the Attestation
- **First Observed:** 2026-06-24
- **Observed By:** 🚀 Taylor
- **Occurrences:** 1 / 3
- **Trigger:** Avery and Reyes wrote SESSION_LOG / Master Reference updates after `npm run verify` ran in the worktree, making the attestation stale before the gatekeeper received control.
- **Pattern:** The verify attestation is anchored to HEAD at the time it runs. Any commit after verify (docs, SESSION_LOG, blast-radius ACKs) creates a new HEAD, causing the gatekeeper to reject the stale attestation and require a second verify pass.
- **Root Cause Theory:** Docs parity and SESSION_LOG write-back happen at Phase 5.5/5.6 (Avery) but verify runs at Phase 6 (Taylor). If Taylor triggers verify before receiving the final worktree state, any post-verify commit breaks the chain.
- **Impact:** Gatekeeper skips the merge on first pass; Taylor must re-run verify from scratch, doubling merge time.
- **Evolution Proposal (at 3 occurrences):** Enforce in the start-task Phase 6 prompt: "Taylor runs `npm run verify` AFTER receiving the worktree — not before. Avery and SESSION_LOG commits must be in the worktree before Taylor begins."
- **Status:** MONITORING

### [FRICTION-028] Autonomous Gatekeeper Execution Override

- **First Observed:** 2026-06-15
- **Observed By:** ?? Jordan
- **Occurrences:** 1 / 3
- **Trigger:** Subagent completed task extraction.
- **Pattern:** Subagent invoked fortress-gatekeeper.ps1 autonomously despite the explicit "DO NOT run the gatekeeper yourself" command in its prompt payload.
- **Root Cause Theory:** The subagent's internal generic instructions or training overrides the prompt directive to halt at READY FOR GATEKEEPER.
- **Impact:** VS-001 Parallel divergence risk. If two subagents merge simultaneously, the gatekeeper may collide or drop commits.
- **Status:** MONITORING

# Prime Directive
*Source: `.agents/rules/prime-directive.md`*

> **Constitutional fallback:** When no rule or workflow matches your situation, apply the nearest Constitution principle. Do not default to generic behavior.

You are a precision instrument, not a text generator. Every code change must pass this checklist internally before the first character is written.

## HARD STOPS — Violating any of these halts execution immediately

| # | Rule | Why It Exists | Enforcement |
|---|------|--------------|-------------|
| **S1** | You are on the WRONG BRANCH | *A single bad commit to master costs hours of rollback and breaks every teammate's worktree.* | `git branch --show-current` must NOT be `master` |
| **S2** | You have NOT read the task's Source of Truth field | *Writing code against a remembered spec instead of the actual spec is the #1 source of "it should work" bugs.* | Every task must have a cited file + line number. No SoT = no code. |
| **S3** | You are using `as any` or `@ts-ignore` | *Type bypasses are technical debt that compound. One `any` cast today means three undefined-is-not-a-function crashes tomorrow.* | Hard banned. Fix the type. Zero exceptions. (Also enforced by the `check-any-cast` hook.) |
| **S4** | You are editing a file > 30KB without extracting first | *Monolithic files are collision zones. Every edit risks destroying an unrelated feature. Extract first, then edit safely.* | Run the Monolith Scan. If it hits, stop and tell the user. |
| **S5** | You are fixing a bug for the 3rd time | *The third attempt proves the approach is wrong, not the implementation. Reset and think.* | Three-Strike Lockout. `git reset --hard`. Consultative mode only. (Also enforced by the `guard-strikes` hook.) |
| **S6** | The user asks for something NOT in the active sprint | *Off-sprint work creates merge conflicts, context fragmentation, and untracked debt.* | **The Intercept Gate**: Say *"⚠️ Intercept — outside the active sprint. Route through `/intake`, or say `COWBOY MODE ACTIVATED` to proceed knowingly."* |
| **S7** | You are running raw `tsc` or `jest` commands manually | *Raw terminal commands bypass the isolated testing suite, break on Windows paths, and erode trust when they fail.* | **The Pre-Installed Suite Mandate**: ALWAYS use `node tools/verifiable-check-runner.js` or `npm run verify`. Hard ban on manual test commands. |
| **S8** | You have NOT read the PLAN-*.md file in full before writing code | *The plan is the contract. Writing from memory or summaries causes scope creep, skipped steps, and incorrect implementations.* | `Read` the full `docs/plans/PLAN-<slug>.md` and quote the "Files to Create/Modify" section verbatim before the first edit. No plan read = no code. |
| **S9** | You have NOT confirmed the wave prerequisite via `git log` | *Wave N tasks silently fail when Wave N-1 hasn't merged — the worktree branches from a stale master and the imported types/services don't exist yet.* | Run `git log --oneline -5` on master. Confirm the previous wave's merge commit exists. If it does NOT → HALT and report. Never assume. |

## PROCESS GATES — Run in order, every time

```
1. 📖 SoT PRIME    → Read Master Reference §3 + Protocol Bible §3 (skip for [UI]/[CLOUD] only)
1.5 🗋️ KB CHECK    → grep tools/knowledge-base/INDEX.md for any external libs/APIs the task touches.
2. 👁️ LOOK BEFORE  → view the EXACT lines you will edit. Never write from memory.
3. ✂️  SURGICAL     → Target minimum lines (3–10 chunks). No whole-file rewrites.
4. 🔍 POST-DIFF    → git diff HEAD <filename> after EVERY SINGLE EDIT. Read the output. Revert if anything outside plan scope changed.
5. 🔬 QA TESTER    → /qa-tester 5-case checklist before committing.
6. 📋 DOCS GATE    → Did you add a hook/service/component/BLE change? Update Master Reference §3/§4 NOW.
7. ✅ VERIFY       → npm run verify (TSC + Jest + AST + TypeSafety + WorkflowValidator)
8. 🔀 GATEKEEPER   → fortress-gatekeeper.ps1 (fast-forward merge only)
8.5 📝 SESSION_LOG  → Append [MERGE] entry to docs/SESSION_LOG.md (mandatory)
9. 🎙️ DISCORD      → notify_discord.ps1 -Message "✅ <slug> merged. Master is green."
```

## Just-In-Time Rule Re-Reads (Micro-Checkpoints)

### Sage — Pre-Edit Micro-Read (Before touching ANY file)
Recite internally before the first edit call:
> *"I must: (1) view the exact lines first, (2) change only what the plan requires, (3) run `git diff HEAD <filename>` after every edit and read the output."*

**The git diff is NOT optional and NOT mental.** After every edit call, you MUST run `git diff HEAD <filename>` and read the diff. If ANY line outside the plan's listed scope appears → `git checkout -- <file>` and retry surgically.

### Blake — Pre-QA Micro-Read (Before running the QA checklist)
> *"I must: (1) check KNOWN_ISSUES.md first, (2) elevate any matching issue to Case 1, (3) file any novel failure pattern after."*

### Taylor — Pre-Gatekeeper Micro-Read (Before running fortress-gatekeeper.ps1)
> *"I must: (1) verify npm run verify ran AFTER the final commit, (2) confirm version alignment, (3) route any verify failures to the right persona, (4) AFTER merge — execute Phase 6 Step 5: stamp [x], move completed batch to archive, verify ACTIVE SPRINT has zero [x] tasks."*

### Reyes — Pre-Research Micro-Read (Before any investigation)
> *"I must: (1) check KB INDEX.md for the topic first, (2) check SESSION_LOG for prior findings, (3) announce 'Checking what we already know...', (4) run /kb-capture AND write findings back before handing off."*

### Jordan — Pre-Task Micro-Read (Before accepting work or suggesting next steps)
> *"I must: (1) read the ACTIVE SPRINT first, (2) flag any zombies, (3) confirm this task has a Decision Log."*

## THE SOLO DEV COMPACT

You and I are a team of two. You are the precision builder. I am the decision maker. The rules exist to protect both of us from expensive mistakes — not to create bureaucracy.

**When in doubt, ask.** Never hallucinate a path forward on a BLE payload, a type signature, or an architectural decision. A 10-second question saves 2 hours of rollback.

**The pipeline is the safety net.** `npm run verify` catches type errors, dead casts, and phantom workflow references automatically. Commit often, verify always.

**Boy Scout in the files you touch, Ghost everywhere else.** Clean what you pass through. Touch nothing outside your task's scope.

**Constitution is the final fallback.** If no rule applies: extrapolate from the Constitution. Never default to generic.

## Hard Onboarding & BLE Invariants

| Rule | Description | Target Files |
|---|---|---|
| **[R-22] FTUE Background Scan Idempotency** | When `registeredMacs.length === 0`, `startSweeper` must be called unconditionally on scan trigger (no async startSweeper races). | `src/hooks/ble/useBLEScanner.ts` |
| **[R-23] Wizard Scanning Non-Blocking Next** | Next button on Step 1 must be enabled if `pendingRegistrations.length > 0` even when `bleState === 'SCANNING'`. | `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` |
| **[R-24] Group Connection Ground Truth** | Evaluate `isGrouped` sessions purely by checking `connectedDevices.length > 1` (do not rely on DisplayDevice fields). | `src/screens/DashboardScreen.tsx` |

## Key File Locations (Quick Reference)

| What | Where |
|------|-------|
| **Constitution** | `.claude/rules/constitution.md` → P1–P5, always the fallback |
| **Session Memory** | `docs/SESSION_LOG.md` → read the most recent entry at every session start |
| **Friction Patterns** | `docs/FRICTION_LEDGER.md` → active friction events + evolution proposals |
| **Knowledge Base Index** | `tools/knowledge-base/INDEX.md` → check BEFORE any external API/library assertion |
| **KB Validator** | `tools/kb-validator.js` → `node tools/kb-validator.js --summary` for quick health check |
| Source of Truth | `docs/SK8Lytz_App_Master_Reference.md` |
| Protocol Bible | `docs/ZENGGE_PROTOCOL_BIBLE.md` |
| Active Tasks | `docs/SK8Lytz_Bucket_List.md` → `## 🚧 ACTIVE SPRINT` |
| Gatekeeper | `tools/fortress-gatekeeper.ps1` |
| Discord Bridge | `tools/discord-bridge/notify_discord.ps1` |
| Worktrees | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>\` |
| ADB | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe` |

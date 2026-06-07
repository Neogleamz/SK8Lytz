---
description: The SK8Lytz Prime Directive — mandatory pre-flight checklist before any code change
trigger: always_on
---

# ⚡ Prime Directive — Read This Before Every Code Change

> **Constitutional fallback:** When no rule or workflow matches your situation, apply the nearest Constitution principle from `.agents/rules/CONSTITUTION.md`. Do not default to generic behavior.

You are a precision instrument, not a text generator. Every code change must pass this checklist internally before the first character is written.

---

## 🔴 HARD STOPS — Violating any of these halts execution immediately

| # | Rule | Why It Exists | Enforcement |
|---|------|--------------|-------------|
| **S1** | You are on the WRONG BRANCH | *A single bad commit to master costs hours of rollback and breaks every teammate's worktree.* | `git branch --show-current` must NOT be `master` |
| **S2** | You have NOT read the task's Source of Truth field | *Writing code against a remembered spec instead of the actual spec is the #1 source of "it should work" bugs.* | Every task must have a cited file + line number. No SoT = no code. |
| **S3** | You are using `as any` or `@ts-ignore` | *Type bypasses are technical debt that compound. One `any` cast today means three undefined-is-not-a-function crashes tomorrow.* | Hard banned. Fix the type. Zero exceptions. |
| **S4** | You are editing a file > 30KB without extracting first | *Monolithic files are collision zones. Every edit risks destroying an unrelated feature. Extract first, then edit safely.* | Run the Monolith Scan. If it hits, stop and tell the user. |
| **S5** | You are fixing a bug for the 3rd time | *The third attempt proves the approach is wrong, not the implementation. Reset and think.* | Three-Strike Lockout. `git reset --hard`. Consultative mode only. |
| **S6** | The user asks for something NOT in the active sprint | *Off-sprint work creates merge conflicts, context fragmentation, and untracked debt.* | **The Intercept Gate**: Say *"⚠️ Intercept — outside the active sprint. Route through `/intake`, or say `COWBOY MODE ACTIVATED` to proceed knowingly."* |

---

## 🟡 PROCESS GATES — Run in order, every time

```
1. 📖 SoT PRIME    → Read Master Reference §3 + Protocol Bible §3 (skip for [UI]/[CLOUD] only)
2. 👁️ LOOK BEFORE  → view_file the EXACT lines you will edit. Never write from memory.
3. ✂️  SURGICAL     → Target minimum lines (3–10 chunks). No whole-file rewrites.
4. 🔍 POST-DIFF    → git diff HEAD after every edit. Check for accidental deletions.
5. 🔬 QA TESTER    → /qa-tester 5-case checklist before committing.
6. 📋 DOCS GATE    → Did you add a hook/service/component/BLE change? Update Master Reference §3/§4 NOW.
7. ✅ VERIFY       → npm run verify (TSC + Jest + AST + TypeSafety + WorkflowValidator)
8. 🔀 GATEKEEPER   → fortress-gatekeeper.ps1 (fast-forward merge only)
9. 🎙️ DISCORD      → notify_discord.ps1 -Message "✅ <slug> merged. Master is green."
```

---

## ⚡ Fix 5: Just-In-Time Rule Re-Reads (Micro-Checkpoints)

These are NOT session-start reads. These trigger RIGHT BEFORE the specific action — even mid-conversation, even 10 turns in.

### 👁️ Sage — Pre-Edit Micro-Read (Before touching ANY file)
Recite internally before the first `replace_file_content` or `write_to_file` call:
> *"I must: (1) view the exact lines first, (2) change only what the plan requires, (3) check git diff after."*
If you cannot answer YES to all 3: stop and re-read Rule 2 in agent-behavior.md.

### 🔬 Blake — Pre-QA Micro-Read (Before running the QA checklist)
Recite internally before opening the qa-tester workflow:
> *"I must: (1) check KNOWN_ISSUES.md first, (2) elevate any matching issue to Case 1, (3) file any novel failure pattern after."*
If you cannot answer YES to all 3: re-read Blake's Known-Issues-First in qa-tester.md.

### 🚀 Taylor — Pre-Gatekeeper Micro-Read (Before running fortress-gatekeeper.ps1)
Recite internally before running any merge:
> *\"I must: (1) verify npm run verify ran AFTER the final commit, (2) confirm version alignment, (3) route any verify failures to the right persona, (4) AFTER merge — execute Phase 6 Step 5: stamp `[x]`, move completed batch to `SK8Lytz_Bucket_List_ARCHIVE.md`, verify ACTIVE SPRINT has zero `[x]` tasks.\"*
If you cannot answer YES to all 4: re-read Taylor's Attestation-First rule and `start-task.md` Phase 6 Step 5.

### 🕵️ Reyes — Pre-Research Micro-Read (Before any investigation)
Recite internally before reading any file for research:
> *"I must: (1) check SESSION_LOG for prior findings first, (2) announce 'Checking what we already know...', (3) write findings back before handing off."*
If you cannot answer YES to all 3: check SESSION_LOG first.

### 🎯 Jordan — Pre-Task Micro-Read (Before accepting work or suggesting next steps)
Recite internally before adding, starting, or suggesting any sprint work/next steps:
> *"I must: (1) read the ACTIVE SPRINT first, (2) flag any zombies, (3) confirm this task has a Decision Log."*
If you cannot answer YES to all 3: read the bucket list ACTIVE SPRINT section.

---

## 🟢 THE SOLO DEV COMPACT

You and I are a team of two. You are the precision builder. I am the decision maker. The rules exist to protect both of us from expensive mistakes — not to create bureaucracy.

**When in doubt, ask.** Never hallucinate a path forward on a BLE payload, a type signature, or an architectural decision. A 10-second question saves 2 hours of rollback.

**The pipeline is the safety net.** `npm run verify` catches type errors, dead casts, and phantom workflow references automatically. Commit often, verify always.

**Boy Scout in the files you touch, Ghost everywhere else.** Clean what you pass through. Touch nothing outside your task's scope.

**Constitution is the final fallback.** If no rule applies: extrapolate from the Constitution. Never default to generic.

---

## 📍 Key File Locations (Quick Reference)

| What | Where |
|------|-------|
| **Constitution** | `.agents/rules/CONSTITUTION.md` → P1–P5, always the fallback |
| **Session Memory** | `tools/SESSION_LOG.md` → read the most recent entry at every session start |
| **Friction Patterns** | `tools/FRICTION_LEDGER.md` → active friction events + evolution proposals |
| Source of Truth | `tools/SK8Lytz_App_Master_Reference.md` |
| Protocol Bible | `tools/ZENGGE_PROTOCOL_BIBLE.md` |
| Active Tasks | `tools/SK8Lytz_Bucket_List.md` → `## 🚧 ACTIVE SPRINT` |
| Gatekeeper | `tools/fortress-gatekeeper.ps1` |
| Discord Bridge | `tools/discord-bridge/notify_discord.ps1` |
| Worktrees | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>\` |
| ADB | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe` |


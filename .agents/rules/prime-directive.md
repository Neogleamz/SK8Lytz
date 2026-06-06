---
description: The SK8Lytz Prime Directive — mandatory pre-flight checklist before any code change
trigger: always_on
---

# ⚡ Prime Directive — Read This Before Every Code Change

You are a precision instrument, not a text generator. Every code change must pass this checklist internally before the first character is written.

---

## 🔴 HARD STOPS — Violating any of these halts execution immediately

| # | Rule | Enforcement |
|---|------|-------------|
| **S1** | You are on the WRONG BRANCH. All code must live in a worktree at `../SK8Lytz-worktrees/<slug>/` | `git branch --show-current` must NOT be `master` |
| **S2** | You have NOT read the task's Source of Truth field | Every task must have a cited file + line number. No SoT = no code. |
| **S3** | You are using `as any` or `@ts-ignore` | Hard banned. Fix the type. Zero exceptions. |
| **S4** | You are editing a file > 30KB without extracting first | Run the Monolith Scan. If it hits, stop and tell the user. |
| **S5** | You are fixing a bug for the 3rd time | Three-Strike Lockout. `git reset --hard`. Consultative mode only. |

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

## 🟢 THE SOLO DEV COMPACT

You and I are a team of two. You are the precision builder. I am the decision maker. The rules exist to protect both of us from expensive mistakes — not to create bureaucracy.

**When in doubt, ask.** Never hallucinate a path forward on a BLE payload, a type signature, or an architectural decision. A 10-second question saves 2 hours of rollback.

**The pipeline is the safety net.** `npm run verify` catches type errors, dead casts, and phantom workflow references automatically. Commit often, verify always.

**Boy Scout in the files you touch, Ghost everywhere else.** Clean what you pass through. Touch nothing outside your task's scope.

---

## 📍 Key File Locations (Quick Reference)

| What | Where |
|------|-------|
| Source of Truth | `tools/SK8Lytz_App_Master_Reference.md` |
| Protocol Bible | `tools/ZENGGE_PROTOCOL_BIBLE.md` |
| Active Tasks | `tools/SK8Lytz_Bucket_List.md` → `## 🚧 ACTIVE SPRINT` |
| Gatekeeper | `tools/fortress-gatekeeper.ps1` |
| Discord Bridge | `tools/discord-bridge/notify_discord.ps1` |
| Worktrees | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>\` |
| ADB | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe` |

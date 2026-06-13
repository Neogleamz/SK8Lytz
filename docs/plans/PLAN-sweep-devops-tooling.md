# Implementation Plan

## Task: sweep-devops-tooling
**Slug:** sweep-devops-tooling  
**Wave:** [WAVE:1] — Parallel-safe with other Wave 1 clusters  
**Size:** [Snack] — 5 files  
**Risk:** [H-RISK] — Core tooling (gatekeeper, auto-archiver, sentinel healer)  
**Status:** [✅ READY]  
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_DEVOPS_AND_TOOLING_findings.json`

## Goal
Fix 10 defects in the DevOps toolchain. Critical fixes: add `$LASTEXITCODE` check after `git rebase` in the gatekeeper (corrupted rebase state risk), add a branch guard to the regression healer (can commit to master), fix the auto-archiver regex substring collision, fix PowerShell newline escaping in sentinel-rules-guard, update the stale ARCH_DEPENDENCY_MAP.json, and replace raw `npx tsc` / `npx jest` in the husky pre-commit hook.

## Decision Log
- **Gatekeeper rebase**: PowerShell `try/catch` does NOT catch non-zero exit codes from external processes — must use `$LASTEXITCODE` explicitly.
- **Regression healer**: The autonomous healer must never commit to master — add `git branch --show-current` guard as first step.
- **Auto-archiver regex**: Use word-boundary regex `(^|,\s*)taskSlug($|,|\s)` to prevent substring collision.
- **Sentinel PS1 newlines**: Use backtick-n (`` `n ``) not `\n` for newlines in PowerShell double-quoted strings.

## Files to Create/Modify

### [MODIFY] tools/fortress-gatekeeper.ps1
- After each `git rebase` call, check `if ($LASTEXITCODE -ne 0)` → run `git rebase --abort`, log error, and `exit 1`
- Target lines around L93

### [MODIFY] tools/auto-archiver.js
- Replace `new RegExp(`${taskSlug},? ?`)` with a boundary-aware regex
- Fix multi-line task block parser to use next-task-header detection instead of blank-line detection
- Target lines around L44 and L77

### [MODIFY] tools/sync_remote_errors.mjs
- Wrap IIFE in an `else` block when Supabase env vars are missing (prevent crash with TypeError)
- Target lines around L68

### [MODIFY] tools/sentinel-rules-guard.ps1
- Fix `\n` to `` `n `` in the `$injected` string at L61
- Expand `$rules` array to include all 5 rules files: `agent-behavior.md`, `safety-protocol.md`, `prime-directive.md`, `kanban-constitution.md`, `sub-agent-behavior.md`
- Target lines L10 and L61

### [MODIFY] tools/sentinel/regression_healer.py
- Add branch guard at start of `_commit_repair`: run `subprocess.run(["git", "branch", "--show-current"])`, abort if output is `master`
- Fix `original_text = sub_parts[0].strip()` to preserve leading whitespace for indented code blocks
- Target lines L165 and L188

### [MODIFY] tools/ARCH_DEPENDENCY_MAP.json
- Replace obsolete trigger paths (`BleStateMachine.ts`, `useBLEAutoRecovery.ts`, `useBLEGattMutex.ts`) with current live paths (`src/services/ble/BleMachine.ts`, `src/services/ble/RecoveryService.ts`)

### [MODIFY] .husky/pre-commit
- Replace `npx tsc --noEmit` and `npx jest` with `npm run verify`
- Target lines around L59

## Out of Scope
- No changes to app source code (src/)
- No changes to supabase/ functions
- No changes to package.json scripts

## Verification Plan
- Manual diff review of each file
- `npm run verify` must pass
- Test auto-archiver regex against known slug collision cases

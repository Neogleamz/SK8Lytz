# Implementation Plan: fix/ble-disconnect-service

## Goal
Port the DisconnectService GATT teardown actor and FEF3 UUID scan detection from the salvaged `temp-troubleshoot-backup` branch into master. Fixes orphaned GATT connections (VS-005) that leave the stack in a zombie state, and fixes silent Zengge device drops on fresh install where no OS GATT cache exists.

## Source of Truth
- `temp-troubleshoot-backup` commit `a3146b5f` — DisconnectService.ts + BleMachine.ts wiring + useBLEScanner FEF3 detection
- `src/services/ble/BleMachine.ts:1-end` — current DISCONNECTING state (inline disconnect logic to replace)
- `src/hooks/ble/useBLEScanner.ts:1-end` — current scan UUID filter handling
- `docs/ZENGGE_PROTOCOL_BIBLE.md` §3 — FEF3 is the canonical Zengge GATT service UUID

## Prerequisite — WAVE:2 (Hard Stop)
**`refactor/burn-down-audit-failures` (Wave 1) MUST be merged into master before this worktree is created.**
Both tasks modify `src/hooks/ble/useBLEScanner.ts` — concurrent edits produce a merge conflict at gatekeeper time.
Verify: `git log --oneline -5` on master must show the burn-down merge commit before proceeding.

## Files to Create / Modify

### [CREATE] `src/services/ble/DisconnectService.ts`
Source: `git show temp-troubleshoot-backup:src/services/ble/DisconnectService.ts`

Port the GATT teardown XState actor verbatim (with these exceptions):
- Strip any `console.error` debug lines — use `AppLogger.warn` only
- Verify no `any` casts — fix if present
- Actor responsibilities: cancel active connection, unsubscribe disconnect listener, log teardown outcome

### [MODIFY] `src/services/ble/BleMachine.ts`
Source: `git diff master...temp-troubleshoot-backup -- src/services/ble/BleMachine.ts`

- Import `DisconnectService` from `./DisconnectService`
- Wire the actor in the DISCONNECTING state (replaces any inline disconnect invocation)
- Surgical: only the actor wiring lines — touch nothing else

Verify: `git diff HEAD src/services/ble/BleMachine.ts` — only actor import + DISCONNECTING state change visible.

### [MODIFY] `src/hooks/ble/useBLEScanner.ts`
Source: `git diff master...temp-troubleshoot-backup -- src/hooks/ble/useBLEScanner.ts`

Add FEF3 UUID detection only:
- Add `const FEF3_UUID = '0000fef3-0000-1000-8000-00805f9b34fb'` near existing UUID constants
- Add `hasFef3Service` predicate to the scan signature filter (fixes: devices dropped on fresh install with no OS GATT cache)

**Do NOT port:**
- `console.error('[SCAN_CALLBACK_RAW]', ...)` debug line
- RSSI threshold changes (`-95`/`-90`) — separate evaluation needed, not part of this task
- Any crash-mode unfiltered scan fallback logic

## Verify Steps
1. After DisconnectService creation: `cat src/services/ble/DisconnectService.ts` — file exists, typed, no `any`
2. After BleMachine.ts edit: `git diff HEAD src/services/ble/BleMachine.ts` — only actor import + DISCONNECTING state line(s) changed
3. After useBLEScanner.ts edit: `grep -n "FEF3_UUID" src/hooks/ble/useBLEScanner.ts` — present, used in filter
4. After all edits: `npm run verify` — TSC clean, no regressions
5. Physical QA: pair a fresh Zengge device (Settings → Bluetooth → Forget), scan → device appears in list

## Out of Scope
- RSSI threshold tuning — evaluate in a separate fix after observing connection stability
- `PermissionService.ts` — already addressed in master's permission audit sweep
- `.old.ts` / `.old.tsx` backup files from temp-troubleshoot-backup — debug artifacts, do not ship
- Audit pipeline workflow expansion (9→17 agents) — separate docs task if needed
- Any crew, auth, or telemetry files

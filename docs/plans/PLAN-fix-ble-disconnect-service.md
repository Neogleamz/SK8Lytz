# Implementation Plan: fix/ble-disconnect-service

## Goal
Extract the inline `disconnectService` XState actor from `BleMachine.ts` into a standalone `DisconnectService.ts` module, and add FEF3 UUID detection to `useBLEScanner.ts`. Fixes orphaned GATT connections (VS-005) and silent Zengge device drops on fresh install where no OS GATT cache has enumerated the FFFF service UUID yet.

## Source of Truth
- `src/services/ble/BleMachine.ts:10-53` — inline `disconnectService` actor to extract (CURRENT MASTER, verified by Reyes Phase 0)
- `src/hooks/ble/useBLEScanner.ts:238-253` — current UUID filter block where FEF3 detection slots in (CURRENT MASTER)
- `temp-troubleshoot-backup` commit `a3146b5f` — FEF3_UUID value `'0000fef3-0000-1000-8000-00805f9b34fb'` and `hasFef3Service` predicate (verified by Morgan Phase 2: `git show temp-troubleshoot-backup:src/hooks/ble/useBLEScanner.ts | grep fef3`)
- NOTE: `DisconnectService.ts` does NOT exist in `temp-troubleshoot-backup`. The source for the extract is `BleMachine.ts:10-53` in master.

## Prerequisite — WAVE:2
Wave 1 (`refactor/burn-down-audit-failures`) was SUPERSEDED — work already in master. No prerequisite action required. Worktree created 2026-06-23.

## Files to Create / Modify

### [CREATE] `src/services/ble/DisconnectService.ts`
Source: Extract verbatim from `src/services/ble/BleMachine.ts:10-53` (current master).

Content to extract:
- `interface DestroyableClient { destroyClient: () => void; }`
- `function isDestroyable(manager: unknown): manager is DestroyableClient`
- `const disconnectService = fromPromise<{ success: boolean }, {...}>(async ({ input }) => { ... })`

Required additions:
- Add `import { fromPromise } from 'xstate';` at top
- Add `import { AppLogger } from '../appLogger';`
- Add `import type { BleMachineContext } from './BleMachine.types';`
- Add `export { disconnectService };` at bottom (or inline `export const`)

After extraction: `BleMachine.ts:10-53` must be DELETED and replaced with `import { disconnectService } from './DisconnectService';` on one line.

Verify: `cat src/services/ble/DisconnectService.ts` — file exists, exports `disconnectService`, no `any` casts, properly typed.

### [MODIFY] `src/services/ble/BleMachine.ts`
- DELETE lines 10-53 (the interface, type guard, and disconnectService const — all of it)
- ADD `import { disconnectService } from './DisconnectService';` in the imports block (line ~5-9)
- All other lines untouched — `actors: { ..., disconnectService }` at line 61 stays as-is

Verify: `git diff HEAD src/services/ble/BleMachine.ts` — only: one added import line, lines 10-53 removed.

### [MODIFY] `src/hooks/ble/useBLEScanner.ts`
Add FEF3 detection inside the scan callback, immediately after the `hasFcf1Service` block (currently around line 240):

```typescript
// FEF3 is the advertisement-layer UUID Zengge controllers broadcast in scan packets.
// FFFF (ZENGGE_SERVICE_UUID) is only visible post-GATT-connection. Without this,
// fresh-install scans (no OS GATT cache) silently drop all Zengge devices.
const FEF3_UUID = '0000fef3-0000-1000-8000-00805f9b34fb';
const hasFef3Service = device.serviceUUIDs?.includes(FEF3_UUID)
  || !!(device.serviceData?.[FEF3_UUID]);
```

Then update the filter guard (currently line 253):
```typescript
// BEFORE:
if (!isSymphony && !isKnownPrefix && !hasZenggeService && !hasBanlanxService && !hasFcf1Service) {
// AFTER:
if (!isSymphony && !isKnownPrefix && !hasZenggeService && !hasBanlanxService && !hasFcf1Service && !hasFef3Service) {
```

**Do NOT port from the backup branch:**
- `console.error('[SCAN_CALLBACK_RAW]', ...)` — debug artifact
- RSSI threshold changes (`-95`/`-90`) — separate evaluation needed
- The commented-out CRISIS OVERRIDE block — do not carry forward

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

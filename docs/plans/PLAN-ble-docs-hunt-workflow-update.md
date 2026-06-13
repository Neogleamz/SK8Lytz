# Implementation Plan: ble-docs-hunt-workflow-update

## Goal
Update `.agents/workflows/deepdive-code-hunt.md` to fix the BLE domain agent file targets. Currently, the hunt agents target deleted files and miss all 5 new XState service actors. Running `/deepdive-code-hunt` today would produce an audit of ghost files.

## Source Audit Finding
- **Gap report:** `ble_test_gap_analysis.md` §"deepdive-code-hunt workflow agent file lists are stale"
- **Deleted files confirmed:** `BleConnectionManager.ts`, `BleLifecycleManager.ts` — confirmed missing from filesystem
- **New files confirmed:** All 5 actors present and audited in `tools/BLE_AUDIT_2/`

## Source of Truth
- `.agents/workflows/deepdive-code-hunt.md` (current — needs updating)
- `src/services/ble/` directory listing (ground truth of what exists)

## Pre-Implementation Read
Run before touching the file:
```powershell
Get-ChildItem -Path "src/services/ble" -Include "*.ts" -Recurse | Select-Object Name
```

## Changes Required

### BLE Domain Agent file lists — replace stale targets

**DELETE from agent targets:**
- `src/services/BleConnectionManager.ts` → DELETED
- `src/services/BleLifecycleManager.ts` → DELETED
- Any reference to `useBLEAutoRecovery.ts`, `useBLEHeartbeat.ts`, `useBLEGattMutex.ts`

**ADD to agent targets:**
- `src/services/ble/BleMachine.ts`
- `src/services/ble/BleMachine.types.ts`
- `src/services/ble/ConnectService.ts`
- `src/services/ble/RecoveryService.ts`
- `src/services/ble/HeartbeatService.ts`
- `src/services/ble/RSSIService.ts`
- `src/services/ble/InterrogatorService.ts`
- `src/services/BleSessionFactory.ts` (if not already present)
- `src/services/BleWriteQueue.ts` (if not already present)
- `src/services/BleWriteDispatcher.ts` (if not already present)

### Update BLE audit question banks
The hunt agents likely have hardcoded questions about `BleConnectionManager`. Replace with questions about:
- XState actor invocation and input shape
- `onOrganicDisconnect` wiring pattern
- `clearWriteQueue` on recovery start
- Service actor type (`fromPromise` vs `fromCallback`)

## Implementation Notes
- Read `.agents/workflows/deepdive-code-hunt.md` fully before editing (P1 rule)
- Use `multi_replace_file_content` — do NOT rewrite the whole workflow
- Only touch the BLE domain agent section(s)
- Verify the workflow still passes the Workflow Reference Validator after edits (`npm run verify`)

## Verification
- Grep for `BleConnectionManager` in deepdive-code-hunt.md → zero results
- Grep for `ConnectService` in deepdive-code-hunt.md → at least 1 result
- `npm run verify` → Workflow Reference Validator green

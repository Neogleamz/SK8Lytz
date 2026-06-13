# Implementation Plan: ble-docs-master-ref-update

## Goal
Fix the VS-003 documentation parity violation. Update `tools/SK8Lytz_App_Master_Reference.md` BLE architecture section to reflect the fully-migrated XState pipeline. Remove all references to deleted files. Document all 5 new XState actors and the organic disconnect pattern.

## Source Audit Finding
- **VS-003 violation:** Master Reference not updated before merge (commit 2276ac8a)
- **Gap report:** `ble_test_gap_analysis.md` §"DOCUMENTATION — Master Reference not updated"
- **Audit basis:** `tools/BLE_AUDIT_2/` — all 9 files serve as the source of truth for what actually exists

## Source of Truth
- `tools/SK8Lytz_App_Master_Reference.md` (current — needs updating)
- `tools/BLE_AUDIT_2/01_bleMachine.md` through `09_useBLE.md` (verified architecture)
- `src/services/ble/BleMachine.ts`, `ConnectService.ts`, `RecoveryService.ts`, `HeartbeatService.ts`
- `src/services/ble/RSSIService.ts`, `InterrogatorService.ts`

## Sections to Update

### §3 — BLE Architecture (full rewrite of BLE subsection)

**DELETE:**
- Any reference to `BleConnectionManager.ts` (DELETED in Phase 3)
- Any reference to `BleLifecycleManager.ts` (DELETED in Phase 6)
- Any reference to `useBLEAutoRecovery.ts` (DELETED in Phase 4)
- Any reference to `useBLEHeartbeat.ts` (DELETED in Phase 5)
- Any reference to `useBLEGattMutex.ts` (DELETED in Phase 6)

**ADD — XState Actor Architecture:**
```
BLE State Machine: src/services/ble/BleMachine.ts
States: IDLE → SCANNING → CONNECTING → READY → RECOVERING → DISCONNECTING

Invoked Actors:
  CONNECTING → connectService (fromPromise)  src/services/ble/ConnectService.ts
  RECOVERING → recoveryService (fromCallback) src/services/ble/RecoveryService.ts
  READY     → heartbeatService (fromCallback) src/services/ble/HeartbeatService.ts

Hook-level services (NOT XState actors):
  src/services/ble/RSSIService.ts — pure RSSI polling, 30s interval
  src/services/ble/InterrogatorService.ts — hardware probe via 0x63 opcode

Organic Disconnect Pattern:
  ConnectService registers bleManager.onDeviceDisconnected for each device.
  Fires TWO callbacks:
    1. handleOrganicDisconnect(error, deviceId) — logging/telemetry only
    2. onOrganicDisconnect(deviceId) — sends RECOVERY_START to machine
  useBLE.ts wires onOrganicDisconnect with a DISCONNECTING guard.
  DO NOT merge these callbacks — they serve different purposes.
```

### §4 — Hooks (update descriptions)
- `useBLERSSIMonitor` — thin React wrapper around `RSSIService.startRSSIPolling`. Owns no polling logic.
- `useBLEInterrogator` — thin React wrapper around `InterrogatorService`. Owns no probe logic.
- Remove `useBLEAutoRecovery` entry

## Implementation Notes
- Read the current §3 and §4 sections first with `view_file` before editing
- Use `multi_replace_file_content` — do NOT rewrite the entire file
- Surgical: only touch BLE-related sections

## Verification
- Grep for `BleConnectionManager` in Master Reference → zero results
- Grep for `onOrganicDisconnect` in Master Reference → at least 1 result
- `npm run verify` passes (Workflow Reference Validator checks for dead references)

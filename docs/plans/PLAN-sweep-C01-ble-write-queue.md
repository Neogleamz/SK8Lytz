# Implementation Plan: C1 — BLE Write Queue Centralization

## Goal
Route all direct writeCharacteristicWithoutResponseForDevice calls through BleWriteDispatcher.enqueue().

## Source Analysis
[system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md)

## Rules Addressed
- R-01: BLE Write Queue Bypass (7 HIGH findings)

## Files to Create/Modify
- `src/services/ble/HeartbeatService.ts` — Replace direct write at L46 with BleWriteDispatcher.enqueue()
- `src/services/ble/InterrogatorService.ts` — Replace direct writes at L119, L132 with BleWriteDispatcher.enqueue()
- `src/services/BlePingService.ts` — Replace direct writes at L57, L123, L136, L157 with BleWriteDispatcher.enqueue()

## Implementation Steps
1. View HeartbeatService.ts L40-55. Replace writeCharacteristicWithoutResponseForDevice with BleWriteDispatcher.enqueue(). Verify: git diff shows only the write call changed.
2. View InterrogatorService.ts L115-140. Replace both direct writes. Verify: git diff shows only write calls changed.
3. View BlePingService.ts L50-165. Replace all 4 direct writes. Verify: git diff shows only write calls changed.
4. Run npm run verify. Verify: TSC + Jest pass.

## Out of Scope
- BleWriteDispatcher.ts (no changes needed)
- BleMachine.ts, ConnectService.ts, RecoveryService.ts

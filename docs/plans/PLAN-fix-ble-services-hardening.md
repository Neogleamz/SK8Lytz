# Implementation Plan: fix/ble-services-hardening

## Goal
Purge `any` types, fix error swallowing, and resolve hardcoded delays across the BLE service layer.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters BLE_CORE + R-08 + R-16

## Findings to Resolve
1. R-08: BleMachine.ts L47,50 — `any` type annotations in XState actions
2. R-08: BleMachine.types.ts L18 — `any` in handleOrganicDisconnect signature
3. R-08: ConnectService.ts L27,240,249 — `any` in disconnect/notification handlers
4. R-08: RecoveryService.ts L36,42 — `any` in handler signatures
5. R-16: BleWriteDispatcher.ts L79,191,260,275,317,388,407 — Hardcoded setTimeout delays
6. R-16: ConnectService.ts L106,179,219,222 — Hardcoded timeouts
7. R-16: HeartbeatService.ts L24 — Hardcoded setInterval
8. R-16: InterrogatorService.ts L87,180,186 — Hardcoded timeouts
9. R-16: RecoveryService.ts L79,142,171 — Hardcoded timeouts
10. R-16: BlePingService.ts L71,148 — Hardcoded timeouts
11. R-16: BleSessionFactory.ts L130 — Hardcoded timeout
12. R-16: BleWriteQueue.ts L174,215 — Internal timer management

## Files to Create/Modify

### [MODIFY] [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
### [MODIFY] [BleMachine.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts)
### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
### [MODIFY] [RecoveryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
### [MODIFY] [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)
### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
### [MODIFY] [RSSIService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RSSIService.ts)
### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
### [MODIFY] [BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
### [MODIFY] [BleSessionFactory.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleSessionFactory.ts)

## Verification
- `npm run verify`
- Grep for remaining `any` in target files

## Out of Scope
- Protocol files (Wave 1)
- Scanner hooks (Wave 4)

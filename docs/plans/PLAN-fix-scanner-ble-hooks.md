# Implementation Plan: fix/scanner-ble-hooks

## Goal
Fix error swallowing, type safety, and hardcoded delays in BLE scanner and device state hooks.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters BLE_CORE scanner + R-16

## Findings to Resolve
1. R-07: useBLEScanner.ts L38,84 — Empty catch blocks
2. R-08: useBLE.ts L144 — `useRef<any>` MIGRATION-SHIM
3. R-16: useBLEScanner.ts L107,224,309,348,361,384 — Hardcoded scan window timeouts (many are legitimate — review each)
4. R-16: useBLEBatterySweep.ts L53,58,105,159 — Hardcoded timeouts
5. R-16: useOptimisticBLE.ts L86,111,135 — Hardcoded timeouts
6. R-16: useDeviceStateLedger.ts L150 — Hardcoded timeout

## Files to Create/Modify

### [MODIFY] [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
### [MODIFY] [useBLEBatterySweep.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts)
### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
### [MODIFY] [useOptimisticBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useOptimisticBLE.ts)
### [MODIFY] [useDeviceStateLedger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDeviceStateLedger.ts)

## Verification
- `npm run verify`

## Out of Scope
- BleMachine.ts, ConnectService.ts (Wave 2)
- DockedController hooks (Wave 1/3)

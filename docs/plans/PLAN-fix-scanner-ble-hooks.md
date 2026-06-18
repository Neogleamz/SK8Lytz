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

## Execution Results

### useBLEScanner.ts
- R-07 L38: ✅ Fixed — empty catch → AppLogger.error
- R-07 L84: ✅ Fixed — empty catch → AppLogger.error
- R-16 L107,224,309,348,361,384: // SKIPPED: All timeouts already use BLE_TIMING.* constants — no raw literals found
- R-22 invariant: ✅ Verified — startSweeper() called unconditionally at registeredMacs.length===0 (L372-376), not modified

### useBLEBatterySweep.ts
// SKIPPED: All R-16 timeouts at L53,58,105,159 already use BLE_TIMING.* constants — no raw literals found

### useBLE.ts
- R-08 L144: ✅ Fixed — useRef<any> → useRef<((event: EventFrom<typeof bleMachine>) => void) | null>

### useOptimisticBLE.ts
- R-16 L111: ✅ Fixed — 300 → BLE_TIMING.OPTIMISTIC_CONFIRM_RESET_MS
- R-16 L135: ✅ Fixed — 1000 → BLE_TIMING.OPTIMISTIC_RECONCILE_RESET_MS
- R-07 L138-142: ✅ Fixed — catch block was swallowing error silently → AppLogger.error added
- R-16 L86: // SKIPPED: setTimeout(async ()=>{}) is not a raw BLE timing literal — debounceMs is a parameter passed from caller

### useDeviceStateLedger.ts
- R-16 L150: ✅ Fixed — inline LEDGER_WRITE_DEBOUNCE_MS=500 removed, replaced with BLE_TIMING.LEDGER_WRITE_DEBOUNCE_MS

### bleTimingConstants.ts [IMPLICIT MODIFY]
- Added: OPTIMISTIC_CONFIRM_RESET_MS, OPTIMISTIC_RECONCILE_RESET_MS, LEDGER_WRITE_DEBOUNCE_MS

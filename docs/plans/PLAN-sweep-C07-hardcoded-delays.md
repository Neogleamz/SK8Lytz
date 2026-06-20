# Implementation Plan: C7 — R-16 Hardcoded Delay Migration

## Goal
Replace inline setTimeout/delay() calls with named constants or queue-managed timing.

## Source Analysis
[system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md)

## Rules Addressed
- R-16: Hardcoded Delays (10 MEDIUM findings)

## Files to Create/Modify
- `src/services/BleWriteDispatcher.ts`
- `src/components/admin/tools/tabs/Sk8LytzProgrammer.tsx`
- `src/hooks/useControllerDispatch.ts`

## Implementation Steps
1. View each file. Identify setTimeout calls with magic numbers.
2. Extract delay values to named constants (e.g., BLE_WRITE_SETTLE_MS = 200).
3. Replace inline numbers with constants.
4. Verify: git diff shows only constant extraction.
5. Run npm run verify.

## Out of Scope
- UI animation setTimeout (acceptable), ConnectService.ts (C5)

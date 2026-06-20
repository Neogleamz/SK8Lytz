# Implementation Plan: C13 — R-08 Type Safety Hardening

## Goal
Replace as unknown as double casts and JSON.parse(JSON.stringify(...)) as Type with proper type guards.

## Rules Addressed
- R-08: Type Safety Bypass (18 findings, 10 HIGH)

## Files to Create/Modify
- `src/services/ble/BleMachine.ts`
- `src/services/BlePingService.ts`
- `src/services/appLogger/AppLoggerCloud.ts`
- `src/services/ScenesService.ts`
- `src/services/GradientsService.ts`
- `src/hooks/useBLE.ts`
- `src/hooks/ble/useBLEBatterySweep.ts`
- `src/hooks/ble/useBLEScanner.ts`

## Implementation Steps
1. For each as unknown as cast, create a properly typed interface or use structuredClone().
2. For JSON.parse(JSON.stringify()) laundering, use structuredClone() with proper generics.
3. Verify: git diff shows type improvements, no as any introduced.
4. Run npm run verify.

## Out of Scope
- Test files, ZenggeProtocol.ts (C3)

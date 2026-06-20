# Implementation Plan: C5 — R-06 Error Unwrapping Sweep

## Goal
Add e instanceof Error unwrapping to all catch blocks missing it.

## Source Analysis
[system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md)

## Rules Addressed
- R-06: Error Handling (~28 MEDIUM findings)

## Files to Create/Modify
- `src/services/ble/ConnectService.ts`
- `src/services/ble/RecoveryService.ts`
- `src/services/ble/RSSIService.ts`
- `src/services/appLogger/AppLoggerCloud.ts`
- `src/services/CrewService/CrewSessionManager.ts`
- `src/protocols/ControllerRegistry.ts`
- `src/protocols/BanlanxAdapter.ts`
- `src/context/SessionContext.tsx`
- Additional files as found by grep for bare catch blocks

## Implementation Steps
1. Grep for catch blocks: `catch\\s*\\(` without `instanceof Error`.
2. For each, add: `const msg = e instanceof Error ? e.message : String(e);`
3. Verify each file: git diff shows only catch block changes.
4. Run npm run verify.

## Out of Scope
- Test files, DashboardScreen.tsx (C2), DockedController.tsx (C4)

# Implementation Plan: C6 — R-04 Telemetry Context Sweep

## Goal
Add { payload_size, ssi } context to all AppLogger.error/warn calls missing it.

## Source Analysis
[system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md)

## Rules Addressed
- R-04: Missing Telemetry Context (~24 MEDIUM findings)

## Files to Create/Modify
- `src/components/admin/tools/tabs/oracle/Oracle53LiveStream.tsx`
- `src/components/CameraTracker.tsx`
- `src/components/PositionalGradientBuilder.tsx`
- `src/context/SessionContext.tsx`
- `src/services/CrewService/CrewSessionManager.ts`
- `src/services/CrewProfileService.ts`
- `src/services/SpeedTrackingService.ts`

## Implementation Steps
1. For each AppLogger.error/warn call, add payload_size: 0, ssi: 0 to context object.
2. Where actual payload_size is available (BLE calls), use real value.
3. Verify each file: git diff shows only logger context additions.
4. Run npm run verify.

## Out of Scope
- Test files, files handled in other clusters

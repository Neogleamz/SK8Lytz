# Implementation Plan: fix/controller-dispatch-safety

## Goal
Fix PII telemetry leaks, re-entrancy races, and type safety violations in the controller dispatch pipeline.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster UI_DOCKED_CONTROLLER + R-09

## Findings to Resolve
1. R-09: useControllerDispatch.ts L100 — PII Leak: device.id (MAC address) passed unscrubbed to AppLogger.error
2. R-10: useControllerDispatch.ts L98 — Sequential awaited group writes instead of concurrent
3. R-16: useControllerDispatch.ts L335,340,352 — Hardcoded setTimeout for BLE write gaps
4. R-26: DockedController.tsx L771 — Re-entrancy race on handleMusicChange/applyFixedPattern (note: fix the hook call sites in dispatch, NOT the DockedController component itself)
5. R-08: useDockedControllerState.ts L61 — `as ModeType` cast instead of type guard

## Files to Create/Modify

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- Scrub device.id from all AppLogger.error calls (hash or truncate MAC)
- Replace sequential for...of group writes with Promise.all
- Replace raw setTimeout with BleWriteQueue.enqueueDelay where applicable

### [MODIFY] [useControllerAnalytics.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerAnalytics.ts)
- Replace hardcoded setTimeout delays at L101, L109, L118

### [MODIFY] [useDockedControllerState.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDockedControllerState.ts)
- Replace `as ModeType` cast with proper type guard

## Verification
- `npm run verify`
- Grep for device.id in AppLogger calls to confirm scrubbing

## Out of Scope
- DockedController.tsx component body (Wave 3)
- BleWriteDispatcher.ts (Wave 2)

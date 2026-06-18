# Implementation Plan: fix/logger-telemetry-hardening

## Goal
Fix type laundering, error swallowing, floating promises, and AsyncStorage key drift in the logger pipeline.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters ADMIN_&_TELEMETRY + R-07 + R-11

## Findings to Resolve
1. R-08: AppLoggerCloud.ts L55,61 — `as unknown as Json` type laundering
2. R-08: AppLoggerCloud.ts L150 — `any[]` parameter type
3. R-08: AppLoggerService.ts L11,23 — `any[]` for activeDevices
4. R-07: AppLoggerCloud.ts L93, AppLoggerService.ts L42,L323, AppLoggerStorage.ts L101 — Empty catch blocks
5. R-11: AppLoggerCloud.ts L24,L52 — Fire-and-forget telemetry inserts
6. R-24: AppLoggerCloud.ts L85 — Direct AsyncStorage.getItem bypasses AppSettingsService
7. R-26: useAdminTelemetry.ts L89 — Async load without re-entrancy guard

## Files to Create/Modify

### [MODIFY] [AppLoggerCloud.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/AppLoggerCloud.ts)
- Fix type laundering with proper Supabase Json typing
- Add proper error handling to catch blocks
- Add .catch() to fire-and-forget inserts
- Replace direct AsyncStorage access with AppSettingsService

### [MODIFY] [AppLoggerService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/AppLoggerService.ts)
- Replace `any[]` with proper DisplayDevice[] or typed arrays
- Fix empty catch blocks

### [MODIFY] [AppLoggerStorage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/AppLoggerStorage.ts)
- Fix empty catch block at L101

### [MODIFY] [useAdminTelemetry.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAdminTelemetry.ts)
- Add useRef boolean re-entrancy guard to load()

## Verification
- `npm run verify`
- Grep for remaining `any` in target files

## Out of Scope
- All other services

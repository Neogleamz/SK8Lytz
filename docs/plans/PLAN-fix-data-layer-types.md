# Implementation Plan: fix/data-layer-types

## Goal
Fix type safety violations, error swallowing, offline-first violations, and AsyncStorage key drift in data layer services.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters DATA_LAYER + R-05

## Findings to Resolve
1. R-08: ScenesService.ts, GradientsService.ts, SkateSpotsService.ts, DeviceRepositoryService.ts — `as unknown as` casts on Supabase query results
2. R-05: SpeedTrackingService.ts L427,L527 — Cloud-first reads for fetchRecentSessions/fetchLifetimeStats
3. R-06: Multiple services — Missing `instanceof Error` unwrapping in catch blocks
4. R-07: SkateSpotsService.ts L39 — Empty catch block
5. R-24: Key registry mismatches for gradients and scenes storage keys
6. R-08: HealthSyncService.ts L46 — `as Record<string, unknown>` cast chain

## Files to Create/Modify

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
// SKIPPED: no violations found — all catch blocks use instanceof Error pattern, storage keys use STORAGE_* constants, no as-unknown-as casts present.

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
// SKIPPED: no violations found — instanceof Error pattern in place, STORAGE_LOCAL_GRADIENTS constant used, .returns<CustomBuilderPreset[]>() used instead of casts.

### [MODIFY] [SkateSpotsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SkateSpotsService.ts)
// FIXED R-07: Empty catch block at L39 — added AppLogger.error call with instanceof Error unwrapping.

### [MODIFY] [DeviceRepositoryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/deviceRepository/DeviceRepositoryService.ts)
// SKIPPED: no violations found — all catch blocks use `const msg = e instanceof Error ? e.message : String(e)` pattern throughout. No as-unknown-as casts.

### [MODIFY] [SpeedTrackingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts)
// SKIPPED: no violations found — offline-first pattern already implemented (getCachedRecentSessions/getCachedLifetimeStats fallbacks), storage keys use constants from storageKeys.ts, instanceof Error checks throughout.

### [MODIFY] [HealthSyncService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/HealthSyncService.ts)
// SKIPPED: no violations found — Record<string, unknown> cast at L59 already correct (R-08 fix comment present), instanceof Error checks in all catch blocks.

### [MODIFY] [GlobalForegroundService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GlobalForegroundService.ts)
// SKIPPED: no violations found — instanceof Error checks in all catch blocks, no AsyncStorage usage, no as-unknown-as casts.

## Verification
- `npm run verify`

## Out of Scope
- CrewService (Wave 4)
- AppLogger services (same wave, different cluster)

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
### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
### [MODIFY] [SkateSpotsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SkateSpotsService.ts)
### [MODIFY] [DeviceRepositoryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepositoryService.ts)
### [MODIFY] [SpeedTrackingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts)
### [MODIFY] [HealthSyncService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/HealthSyncService.ts)
### [MODIFY] [GlobalForegroundService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GlobalForegroundService.ts)

## Verification
- `npm run verify`

## Out of Scope
- CrewService (Wave 4)
- AppLogger services (same wave, different cluster)

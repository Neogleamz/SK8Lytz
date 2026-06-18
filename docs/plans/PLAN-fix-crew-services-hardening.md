# Implementation Plan: fix/crew-services-hardening

## Goal
Fix circular dependencies, error swallowing, floating promises, memory leaks, offline-first violations, and telemetry context gaps across crew services.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters CREW_SERVICES + R-29

## Findings to Resolve
1. R-29: CrewService ↔ CrewSessionManager circular dependency
2. R-29: CrewService ↔ CrewRealtime circular dependency
3. R-29: CrewAutoRejoin ↔ CrewService circular dependency
4. R-04: CrewSessionManager.ts — 9 instances missing payload_size/ssi context
5. R-11: CrewSessionManager.ts L360 — Unawaited crew member deletion
6. R-07: CrewRealtime.ts L99 — Comment-only catch block
7. R-05: useCrewSession.ts L85 — Synchronous cloud write without optimistic UI
8. R-22: CrewMemberDashboard.tsx L103 — useEffect without cleanup
9. R-05: CrewMemberDashboard.tsx L178 — Direct Supabase UI query without cache
10. R-16: Multiple files — Hardcoded timeouts

## Files to Create/Modify

### [MODIFY] [CrewRealtime.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService/CrewRealtime.ts)
### [MODIFY] [CrewSessionManager.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService/CrewSessionManager.ts)
### [MODIFY] [CrewProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
### [MODIFY] [useCrewHub.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewHub.ts)
### [MODIFY] [useCrewLeaderBroadcast.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewLeaderBroadcast.ts)
### [MODIFY] [useCrewManage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
### [MODIFY] [useCrewSession.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewSession.ts)
### [MODIFY] [CrewMemberDashboard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewMemberDashboard.tsx)

## Verification
- `npm run verify`
- Confirm no circular import cycles via ast-parser

## Out of Scope
- CrewCard, CrewLandingMap (Wave 3)
- LocationService (Wave 2)

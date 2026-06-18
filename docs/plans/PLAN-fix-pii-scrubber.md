# Implementation Plan: fix/pii-scrubber

## Goal
Scrub all PII (MAC addresses, user names) from telemetry logging calls across crew and admin components.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster R-09 PII Leaks

## Findings to Resolve
1. R-09: Sk8LytzProgrammer.tsx — deviceId (MAC) in AppLogger calls
2. R-09: CrewDetailScreen.tsx — crewName in telemetry
3. R-09: CrewLandingScreen.tsx — crewName in telemetry (2x)
4. R-09: CrewManageScreen.tsx — crewName in telemetry

## Files to Create/Modify

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
### [MODIFY] [CrewDetailScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewDetailScreen.tsx)
### [MODIFY] [CrewLandingScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingScreen.tsx)
### [MODIFY] [CrewManageScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewManageScreen.tsx)

For each file: hash or truncate MAC addresses, redact user names from telemetry payloads.

## Verification
- `npm run verify`
- Grep for `device.id` and `crewName` in AppLogger calls across these files

## Out of Scope
- useControllerDispatch PII (Wave 1)
- RecoveryService PII (Wave 2)

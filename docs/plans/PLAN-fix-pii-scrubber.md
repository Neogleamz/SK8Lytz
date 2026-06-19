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
- **FIXED** — Line 222: `unit: id` → `unit: scrubPII(id)`. Raw MAC was leaking via `unit` field while `deviceId` was already scrubbed.
// SKIPPED: Already fixed on master in a prior wave.

### [MODIFY] [CrewDetailScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewDetailScreen.tsx)
- **SKIPPED** — Line 122 already uses `crewName: scrubPII(editCrewName.trim())`. No PII violation found.
// SKIPPED: False positive, no PII violation found.

### [MODIFY] [CrewLandingScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingScreen.tsx)
- **SKIPPED** — Lines 124, 145 already use `crewName: scrubPII(crew.name)`. No PII violation found.
// SKIPPED: False positive, no PII violation found.

### [MODIFY] [CrewManageScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewManageScreen.tsx)
- **SKIPPED** — Line 67 already uses `crewName: scrubPII(crew.name)`. `city` is location data, not PII. No violation found.
// SKIPPED: False positive, no PII violation found.

For each file: hash or truncate MAC addresses, redact user names from telemetry payloads.

## Verification
- `npm run verify`
- Grep for `device.id` and `crewName` in AppLogger calls across these files

## Out of Scope
- useControllerDispatch PII (Wave 1)
- RecoveryService PII (Wave 2)

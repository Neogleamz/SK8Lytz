# Implementation Plan: fix/crew-ui-types

## Goal
Replace all `any` type annotations in crew UI components with proper types.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster R-08 crew UI

## Findings to Resolve
1. R-08: CrewCard.tsx L11,12,24,30 — `styles: any`, `Colors: any`, `setStep: any`, `profileService: any`
2. R-08: CrewLandingMap.web.tsx L11 — `[key: string]: any` index signature
3. R-08: CrewScheduleScreen.tsx L134,146 — `_evt: any` in date picker handlers
4. R-08: MapFiltersTray.tsx L44,47 — `opt: any`, `Colors: any`

## Files to Create/Modify

### [MODIFY] [CrewCard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewCard.tsx)
### [MODIFY] [CrewLandingMap.web.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingMap.web.tsx)
### [MODIFY] [CrewScheduleScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewScheduleScreen.tsx)
### [MODIFY] [MapFiltersTray.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/MapFiltersTray.tsx)

## Verification
- `npm run verify`
- Grep for `any` in target files — must be zero

## Out of Scope
- CrewDetailScreen, CrewLandingScreen, CrewManageScreen (Wave 2 PII)

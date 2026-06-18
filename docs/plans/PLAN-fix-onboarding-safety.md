# Implementation Plan: fix/onboarding-safety

## Goal
Fix error swallowing, hardcoded delays, OS variance, and re-entrancy races in onboarding screens.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster UI_SCREENS onboarding

## Findings to Resolve
1. R-07: AuthScreen.tsx L71 — Empty catch block
2. R-16: HardwareSetupWizardScreen.tsx L160 — Hardcoded setTimeout
3. R-20: HardwareSetupWizardScreen.tsx L556,771 — Platform.OS ternary instead of Platform.select
4. R-20: AuthScreen.tsx L98 — Platform.OS ternary instead of Platform.select

## Files to Create/Modify

### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
### [MODIFY] [AuthScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)

## Verification
- `npm run verify`

## Out of Scope
- DashboardScreen (Wave 2)

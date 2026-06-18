# Implementation Plan: fix/dashboard-styles-perf

## Goal
Fix StyleSheet.create firing on every render cycle (up to 20Hz) and resolve theme token bypass.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster THEME_&_ASSETS

## Findings to Resolve
1. THEME-001: DashboardStyles.ts L26-365 — CRITICAL: createDashboardStyles wraps StyleSheet.create in factory function called every render
2. THEME-002: DashboardStyles.ts L45-119 — Cross-platform shadow assumptions without Platform.select()
3. THEME-003: DashboardStyles.ts L8-24 — Business logic (getPatternColors) misplaced in styles file
4. THEME-004: DashboardStyles.ts L112,165-173 — Hardcoded hex colors bypass ThemePalette
5. THEME-005: theme.ts L76,82,88,95 — Explicit ViewStyle/TextStyle casts

## Files to Create/Modify

### [MODIFY] [DashboardStyles.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/styles/DashboardStyles.ts)
- Convert createDashboardStyles from factory function to static StyleSheet.create call
- Replace raw shadow properties with Shadows.soft/Shadows.glow from theme.ts
- Move getPatternColors to a utility file
- Replace hardcoded hex colors with Colors.primary/secondary tokens

### [MODIFY] [theme.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/theme/theme.ts)
- Remove unnecessary ViewStyle/TextStyle casts by typing config objects strictly

## Verification
- `npm run verify`

## Out of Scope
- All other style files
- DashboardScreen.tsx (Wave 2)

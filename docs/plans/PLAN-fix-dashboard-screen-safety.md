# Implementation Plan: fix/dashboard-screen-safety

## Goal
Fix type laundering, OS variance violations, event listener leaks, and FlatList bottlenecks in DashboardScreen.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters UI_SCREENS + R-08

## Findings to Resolve
1. R-08: DashboardScreen.tsx L251,252,527,842-844,981,1111,1157,1273 — Multiple `as unknown as` type laundering and `any` casts
2. R-20: DashboardScreen.tsx L397,584,955,1003 — Platform.OS blind cross-platform assumptions
3. R-17: DashboardScreen.tsx L694 — Event listener leak (DeviceEventEmitter inside Promise.then)
4. R-25: DashboardScreen.tsx L746 — BackHandler without Platform.OS guard
5. R-28: DashboardScreen.tsx — Inline FlatList components
6. R-16: useDashboardAutoConnect.ts L191,245,266,426,456 — Hardcoded timeouts

## Files to Create/Modify

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
### [MODIFY] [useDashboardAutoConnect.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts)
### [MODIFY] [useDashboardController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardController.tsx)

## Verification
- `npm run verify`

## Out of Scope
- DashboardStyles.ts (Wave 1)
- DockedController.tsx (Wave 3)

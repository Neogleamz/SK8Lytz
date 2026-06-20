# Implementation Plan: C2 — DashboardScreen Monolith Extraction

## Goal
Extract DashboardScreen.tsx (57.16KB, 17 findings) into sub-components below 30KB.

## Source Analysis
[system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md)

## Rules Addressed
- R-23: Monolith (57.16KB), R-26: Missing re-entrancy guards, R-25: Unguarded BackHandler
- R-27: 4 context consumers, R-24: Undocumented storage key, R-18: Boolean traps
- R-17: Listener leak, R-14: Missing state matrix

## Files to Create/Modify
- `src/screens/DashboardScreen.tsx` — Reduce to orchestrator (<25KB)
- `src/screens/Dashboard/DashboardHeader.tsx` [NEW]
- `src/screens/Dashboard/DashboardDeviceList.tsx` [NEW]
- `src/screens/Dashboard/DashboardCrewHub.tsx` [NEW]
- `src/screens/Dashboard/DashboardPowerControls.tsx` [NEW]
- `src/screens/Dashboard/useDashboardState.ts` [NEW] — consolidated FSM state hook
- `src/screens/Dashboard/index.tsx` [NEW] — re-export

## Implementation Steps
1. Read DashboardScreen.tsx in full. Identify component boundaries.
2. Extract DashboardHeader (top bar, device name, battery). Verify: renders identically.
3. Extract DashboardDeviceList (FlatList, device cards). Verify: scroll behavior preserved.
4. Extract DashboardCrewHub (crew section). Verify: collapse toggle works.
5. Extract DashboardPowerControls (power toggle, quick actions). Verify: BLE dispatch works.
6. Create useDashboardState hook consolidating boolean traps into FSM. Verify: type-safe.
7. Add Platform.OS guard around BackHandler. Verify: no crash on iOS.
8. Fix DeviceEventEmitter cleanup in useEffect return. Verify: no listener leak.
9. Centralize AsyncStorage key to storageKeys.ts. Verify: key imported, not inline.
10. Run npm run verify.

## Out of Scope
- DockedController.tsx (C4), ZenggeProtocol.ts (C3), any services

# [PLAN] chore/refactor-god-object-dashboard — Dashboard God Object Surgery

> See full plan in the active implementation_plan.md artifact.

## Summary

Surgical extraction of ~900 remaining lines of business logic from DashboardScreen.tsx (2,332 → ~1,200 lines).

### Phases
1. [NEW] `src/hooks/useDashboardAutoConnect.ts` — Extract `syncCloudAndAutoConnect` + auto-connect observer
2. [MODIFY] `src/hooks/useDashboardGroups.ts` — Migrate `runAutoProvisioning`
3. [MODIFY] `src/hooks/useDashboardGroups.ts` — Migrate `saveGroup`, `handleGroupDelete`, `saveSettings`
4. Fix 4 pre-existing TS2307/TS2322 compiler errors (`AdminToolsModal`, `DockedController`, `Sk8LytzProgrammerModal`, `LabProps`)
5. [NEW] `src/hooks/useHardwareNotifications.ts` — Extract `setOnDataReceived` callback + diagnostics upload
6. `npx tsc --noEmit` exits 0. DashboardScreen ≤1,200 lines.

## Target
- `src/screens/DashboardScreen.tsx`: ~1,200 lines (down from 2,332)
- All TS errors resolved
- BLE co-location constraint maintained: `useBLE`, crew state, `writeToDevice` remain in DashboardScreen

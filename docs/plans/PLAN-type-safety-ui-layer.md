# Implementation Plan — `refactor/type-safety-ui-layer`

## Goal
Fix `: any` prop type annotations across Dashboard UI, Admin Diagnostic Lab, and Docked components — primarily prop interface definitions that need explicit types derived from the existing domain model.

## Source of Truth
- `artifacts/deepdive_raw/R-08_findings.json` — UI layer entries (R-08-001 through ~R-08-200+)
- `artifacts/system_audit_report.md` §R-08 UI Layer cluster
- `src/types/` — existing domain types to import from

## Out of Scope
- Data layer service any-casts (separate PLAN-type-safety-data-layer)
- Protocol layer (ZenggeProtocol.ts)
- Test files

## Batch Strategy
This is a large sweep. Execute in domain sub-batches, one file cluster at a time, committing after each cluster passes TSC.

---

## Sub-batch A — Diagnostic Lab Tabs (~25 instances)
Files: `DiagnosticLabBuilderTab.tsx`, `DiagnosticLabColorTab.tsx`, `DiagnosticLabDevicesTab.tsx`, `DiagnosticLabHwBadge.tsx`, `DiagnosticLabOracleTab.tsx`, `DiagnosticLabSnifferTab.tsx`, `DiagnosticLabTransitionTab.tsx`

- **Step A1:** For each file, identify what the `any` props represent (packet arrays, device configs, etc.)
- **Step A2:** Import the matching type from `src/types/ble.types.ts` or `src/protocols/ZenggeProtocol.ts` and replace `: any`
- **Verify:** `grep ": any" src/components/admin/tools/tabs/*.tsx` returns zero.

## Sub-batch B — Dashboard Slabs (~15 instances)
Files: `DashboardCrewPanel.tsx`, `RegisteredFleetSlab.tsx`, `SkateGroupCard.tsx`, `HardwareStatusPills.tsx`, `DashboardHeader.tsx`, `DashboardTelemetryHero.tsx`, `MySkatesSlab.tsx`

- **Step B1:** Identify prop types for each `any` — most are device/group/crew types already defined in `src/types/`
- **Step B2:** Replace each `: any` with the correct import
- **Verify:** `grep ": any" src/components/dashboard/*.tsx` returns zero.

## Sub-batch C — Docked Components (~10 instances)
Files: `DockedDock.tsx`, `FavoritesPanel.tsx`, `MusicPanel.tsx`, `PresetCard.tsx`, `UniversalSlidersFooter.tsx`

- **Step C1:** Same pattern — trace what props are passed and import matching types
- **Verify:** `grep ": any" src/components/docked/*.tsx` returns zero (or near-zero for legitimate edge cases with comment justification).

## Sub-batch D — Remaining Components
Files: `AccountModal.tsx`, `CrewMemberDashboard.tsx`, `CustomSlider.tsx`, `DeviceSettingsModal.tsx`, `account/types.ts`, `ProfileService.types.ts`

- **Step D1:** Fix `account/types.ts:101` and `ProfileService.types.ts:21` first (type files cascade to consumers)
- **Verify:** No `: any` in type declaration files.

## Final Step — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors. Error count equal or reduced vs baseline.

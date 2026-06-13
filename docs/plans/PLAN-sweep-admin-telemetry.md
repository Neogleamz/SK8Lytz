# Implementation Plan

## Task: sweep-admin-telemetry
**Slug:** sweep-admin-telemetry
**Wave:** [WAVE:2] — Prerequisite: Wave 1 fully merged
**Size:** [Meal] — 6 files
**Risk:** [M-RISK] — Admin UI only; no BLE or data layer changes
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_ADMIN_AND_TELEMETRY_findings.json`
**Prerequisite:** Wave 1 fully merged

## Goal
Fix 12 findings in the admin tools and telemetry panel layer. Focus: extract all inline `keyExtractor` and `renderItem` arrow functions in FlatLists to stable `useCallback` references, add missing 4-state UI matrices (loading/error/empty/success) to panels that currently skip the error and empty states, and fix the 3 AppLogger telemetry calls with incorrectly structured context objects.

## Decision Log
- **Inline FlatList callbacks (R-28)**: Inline arrow functions for `keyExtractor` cause `FlatList` to treat every render as a full re-render, defeating its virtualization. Every admin panel has at least one — all must be extracted.
- **4-state matrix gaps (R-14)**: `AdminRosterPanel`, `HardwareBlacklistPanel`, and `FeatureFlagsPanel` all have FlatLists with no `ListEmptyComponent` or persistent error state. These are visible to admins who need reliable feedback.
- **AppLogger telemetry context (R-04)**: `DiagnosticLabOracleTab` and `Sk8LytzProgrammer` call `AppLogger.error` but pass `payload_size` and `ssi` inside the message string instead of as top-level context object fields. These never reach the Supabase telemetry schema correctly.

## Files to Create/Modify

### [MODIFY] src/components/admin/AdminToolsModal.tsx
- Extract inline `keyExtractor` at L207 to a stable `useCallback` (R-28)

### [MODIFY] src/components/admin/tools/AdminAuditLogViewer.tsx
- Extract inline `keyExtractor` at L178 to a stable `useCallback` (R-28)
- Add `instanceof Error` check to catch variable `e` at L100 (R-06)

### [MODIFY] src/components/admin/tools/GlobalAnalyticsPanel.tsx
- Add `instanceof Error` check to catch variable `e` at L34 (R-06)

### [MODIFY] src/components/admin/tools/AdminRosterPanel.tsx
- Extract inline `keyExtractor` at L178 to a stable `useCallback` (R-28)
- Add `ListEmptyComponent` prop to the FlatList at L172 (R-14)

### [MODIFY] src/components/admin/tools/HardwareBlacklistPanel.tsx
- Extract inline `keyExtractor` at L255 to a stable `useCallback` (R-28)
- Add persistent error state rendering to the FlatList at L249 (R-14)

### [MODIFY] src/components/admin/tools/FeatureFlagsPanel.tsx
- Extract inline `keyExtractor` at L273 to a stable `useCallback` (R-28)
- Add persistent error state UI when `fetchFlags` fails at L267 (R-14)

### [MODIFY] src/components/admin/tools/DiagnosticLabOracleTab.tsx
- Fix `AppLogger.error` at L214 — move `payload_size` and `ssi` to top-level context object (R-04)

### [MODIFY] src/components/admin/tools/Sk8LytzProgrammer.tsx
- Fix `AppLogger.error` at L78 — move `payload_size` and `ssi` to top-level context object (R-04)

## Out of Scope
- `AdminPicksScheduler.tsx` ScrollView→FlatList refactor (LOW — deferred)
- `UserManagementPanel.tsx` renderItem extraction (LOW — deferred)
- No BLE or service layer changes

## Verification Plan
- `npm run verify` — TSC must pass
- `git diff HEAD` after each file

# [FIX] TSC Errors Audit

## Goal
Fix TypeScript errors remaining from dynamic-arch-regressions (Audio namespace, missing EventType for 'BUILDER_PRESET_SAVED' in DockedController, IVoiceAction/Typography Subheader in DashboardScreen).

## Proposed Changes
- Resolve `Audio` namespace ambiguity in `CrewModal`.
- Add `BUILDER_PRESET_SAVED` to `EventType` enum.
- Fix `IVoiceAction` interface in `DashboardScreen`.
- Fix `Typography` subheader props.

## Verification Plan
- Run `npx tsc`.
- Confirm 0 errors in target files.

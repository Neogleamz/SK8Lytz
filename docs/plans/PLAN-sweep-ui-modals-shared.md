# Implementation Plan

## Task: sweep-ui-modals-shared
**Slug:** sweep-ui-modals-shared
**Wave:** [WAVE:2] — Prerequisite: Wave 1 fully merged
**Size:** [Snack] — 8 files
**Risk:** [M-RISK] — Modal UI layer; no BLE or data layer changes
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_UI_MODALS_findings.json`
**Prerequisite:** Wave 1 fully merged

## Goal
Fix 10 findings in the modal and shared component layer. Key fixes: replace hardcoded static color imports in `DeviceSettingsModal`, `GroupSettingsModal`, and `SessionSummaryModal` with `useTheme()` dynamic theme hook. Fix the `any` typed props in `CommunityModal`, `GroupSettingsModal`, and `MarqueeText`. Resolve the circular dependency in `account/types.ts`. Note: `AccountModal.tsx` (~35KB monolith) extraction is deferred — only surgical prop type fixes inside it.

## Decision Log
- **Static color imports (R-21 MEDIUM)**: `DeviceSettingsModal` and `GroupSettingsModal` import colors statically from `theme/theme`, bypassing the `useTheme()` hook. This means they ignore dark mode / theme switching. Must refactor to use the hook.
- **`SessionSummaryModal` hardcoded overlay (R-21 LOW)**: The overlay color is hardcoded despite `useTheme` being used elsewhere in the component — inconsistency.
- **Circular dependency (R-29 MEDIUM)**: `account/types.ts` creates a circular import chain with `AccountModal.tsx` and `AccountTab*.tsx`. Must break by moving shared types to a standalone file that none of those components import from each other.
- **`AccountModal.tsx` monolith**: Flagged at ~35KB but extraction is out of scope. Only the `any` prop type for `styles` at L344/362 Supabase auth calls are in scope.

## Files to Create/Modify

### [MODIFY] src/components/DeviceSettingsModal.tsx
- Replace static `Colors` import at L7 with `const { colors } = useTheme()` hook

### [MODIFY] src/components/GroupSettingsModal.tsx
- Replace static `Colors` import at L4 with `const { colors } = useTheme()` hook
- Type `allDevices` prop at L14 with correct `DisplayDevice[]` instead of `any[]`

### [MODIFY] src/components/SessionSummaryModal.tsx
- Fix hardcoded overlay color at L207 — use `colors.overlay` from `useTheme()` (which is already consumed in the component)
- Remove duplicated `estimateCalories` helper at L51 — import from `src/services/SpeedTrackingService.ts` (R-21)

### [MODIFY] src/components/CommunityModal.tsx
- Type `styles` prop in `SceneCard` subcomponent at L101 — replace `any` with `StyleProp<ViewStyle>` (R-08)
- Extract inline `styles` object at L266 from inside the component to `useMemo` or static `StyleSheet` (R-07)

### [MODIFY] src/components/MarqueeText.tsx
- Type `containerStyle` prop at L7 with `StyleProp<ViewStyle>` instead of `any` (R-08)

### [MODIFY] src/components/CustomSlider.tsx
- Remove unused `step` and `thumbTintColor` props from destructuring at L18 (R-21)

### [MODIFY] src/components/account/types.ts
- Move shared account types into a new `src/components/account/account.types.ts` to break the circular dependency chain (R-29)
- Update imports in affected files

## Out of Scope
- `AccountModal.tsx` component extraction (monolith — separate task)
- `AccountModal.tsx` L344/362 direct Supabase auth calls (handled in `sweep-identity-auth`, Wave 3)
- No BLE changes

## Verification Plan
- `npm run verify` — TSC must pass; circular dependency should not appear in import graph
- Verify dark mode: `DeviceSettingsModal` and `GroupSettingsModal` must respect theme switching

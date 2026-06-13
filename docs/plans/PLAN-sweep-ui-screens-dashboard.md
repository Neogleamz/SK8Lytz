# Implementation Plan

## Task: sweep-ui-screens-dashboard
**Slug:** sweep-ui-screens-dashboard
**Wave:** [WAVE:1] — Parallel-safe with other Wave 1 clusters
**Size:** [Meal] — 8 files
**Risk:** [H-RISK] — DashboardScreen is the app's root screen; monolith
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_UI_SCREENS_findings.json`

## Goal
Fix 16 findings in the Dashboard and Onboarding screen layer. Critical: eliminate `as any` / `any[]` props on all 5 dashboard sub-components, fix the `Animated.Value` memory leak instantiated inside render, remove the leaked duplicate `DashboardGroupList.tsx`, and fix the `Platform.OS` ternary that doesn't account for Web. Note: the monolith flag on `DashboardScreen.tsx` (~54KB) and `HardwareSetupWizardScreen.tsx` (~42KB) is logged but **component extraction is out of scope** for this sweep — the extraction itself is a separate [Feast] task.

## Decision Log
- **Monolith extraction deferred**: Per P4, extraction requires its own plan. This sweep targets only the surgical fixes inside those files — type safety, the Animated.Value leak, and the Platform ternary.
- **`Animated.Value` in render**: Must be moved to `useRef` or `useMemo` to prevent re-instantiation and memory accumulation on every render cycle.
- **Duplicate DashboardGroupList.tsx**: `src/components/DashboardGroupList.tsx` is an empty legacy file; `src/components/dashboard/DashboardGroupList.tsx` is also empty (verification anchor). Both should be deleted.

## Files to Create/Modify

### [MODIFY] src/components/dashboard/DashboardTelemetryHero.tsx
- Fix `forwardRef` generic parameters — replace `any` with correct element and props types (L12)

### [MODIFY] src/components/dashboard/HardwareStatusPills.tsx
- Type `device` prop with the correct `DisplayDevice` interface instead of `any` (L8)

### [MODIFY] src/components/dashboard/MySkatesSlab.tsx
- Type `allDevices`, `connectedDevices`, `registeredDevices` props with `DisplayDevice[]` instead of `any[]` (L21)

### [MODIFY] src/components/dashboard/SkateGroupCard.tsx
- Type `userProfile` prop with the correct profile type instead of `any` (L18)
- Fix type laundering `as unknown as` on colors coercion (L65) — use proper readonly tuple type

### [MODIFY] src/components/dashboard/CrewHubSlab.tsx
- Move `new Animated.Value(1)` from render body into `useRef` (L181)

### [MODIFY] src/components/LocationPickerMap.web.tsx
- Replace `any` index signature in props type with explicit typed interface (L11)

### [MODIFY] src/screens/DashboardScreen.tsx
- Fix `as unknown as DisplayDevice[]` type laundering at L809 — use proper typed assertion or fix upstream type
- Fix `Platform.OS === 'ios' ? 'padding' : undefined` ternary at L90 — add explicit `'web'` case
- Fix power-toggle loop at L755 — wrap in queue-serialized dispatch (R-10)
- Fix hardcoded `'@Sk8lytz_Favorites'` AsyncStorage key at L648 — import from key registry (R-24)
- Remove scattered boolean flags `isTestModeActive`/`isDiagnosticsMode` at L183 — replace with string union FSM state (R-18)

### [DELETE] src/components/DashboardGroupList.tsx
- Empty legacy file — delete

## Out of Scope
- Component extraction of `DashboardScreen.tsx` or `HardwareSetupWizardScreen.tsx`
- No changes to `HardwareSetupWizardScreen.tsx` (no actionable surgical fixes from this sweep)
- No changes to BLE hooks

## Verification Plan
- `npm run verify` — TSC must pass (all `any` props replaced with real types)
- `git diff HEAD` after each edit

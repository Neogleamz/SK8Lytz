# Plan: `fix/typescript-debt-audit`

### Design Decisions & Rationale
This is a pure type-safety sweep. All errors are pre-identified in the Bucket List description. We fix them one file at a time in order of risk (lowest-risk dead state vars first, most complex type drift last). No logic changes — strictly type annotations and removing stale state.

---

## Prioritized Fix List

### `src/screens/DashboardScreen.tsx`
- Remove dead state variables: `setDemoHaloQueued` / `setDemoSoulQueued` — these are never read after being set.
- Add missing `IVoiceAction` import.
- Fix `Typography.Subheader` reference (likely a missing export from the theme file).
- Fix missing `EventType` entry for `'BUILDER_PRESET_SAVED'`.

### `src/types/` (CustomGroup type)
- Audit and update `CustomGroup` type to match actual `ng_custom_groups` AsyncStorage shape.

### `src/components/HardwareSetupWizardScreen.tsx`
- Resolve `'UNKNOWN'` product type overlap — ensure the union type `'HALOZ' | 'SOULZ' | 'RAILZ' | 'UNKNOWN'` is declared in a shared type file and imported consistently.

### `src/hooks/useRegistration.ts`
- Add missing `group_id` field to the legacy migration helper object.

### `src/components/PositionalGradientBuilder.tsx`
- Add missing `EventType` entries for any builder events that are logged but not typed.

### `src/services/LocationService.ts`
- Replace all `any` param types with proper typed interfaces.

### `src/components/Sk8LytzDiagnosticLab.tsx`
- Fix missing `useRegistration` import (the old file reference).

### `src/components/DeviceSettingsModal.tsx`
- Narrow `any` prop types to the actual `IDevice` or `PendingRegistration` shape.

---

## Open Questions
- None — all errors are pre-identified. This is a straight execution task.

## Verification Plan
- Run `npx tsc --noEmit` after each file fix.
- Target: zero TypeScript errors in the entire workspace.

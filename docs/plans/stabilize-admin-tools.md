# Phase 6: Stabilization Pass (Admin Hub)

Stabilize the recent domain-driven refactor by fixing performance bottlenecks in `AdminToolsModal.tsx` and hardening the TypeScript type safety in the new domain hooks.

## User Review Required

> [!IMPORTANT]
> **Performance Shift**: I will be moving several sub-render functions (`renderLogItem`, `renderDeviceTab`, etc.) out of the main `AdminToolsModal` component. This is a non-breaking but significant refactor to prevent "Re-render Storms" in the virtualized lists.

## Proposed Changes

### [Admin UI] Performance Optimization

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AdminToolsModal.tsx)

- Move `renderLogItem` outside the component or wrap in `useMemo`.
- Move sub-tabs (`Stats`, `Device`, `Tools`) to dedicated internal sub-components to isolate their state/re-renders.
- Use `useMemo` for filtering `timelineLogs` to avoid array slicing/filtering on every render.
- Fix inline style definitions (e.g., `fieldStyle`) by moving them to the stylesheet or a stable object.

---

### [Domain Hooks] Hardening & Completion

#### [MODIFY] [useAdminTelemetry.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAdminTelemetry.ts)

- Replace `as any` in `setStats` with proper typing.
- Tighten error handling in `uploadLogs`.

#### [MODIFY] [useAdminSettings.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAdminSettings.ts)

- Implement the actual persistence call in `updateSetting` (calling `AppSettingsService.saveSettingToServer`).

#### [MODIFY] [useProtocolBuilder.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProtocolBuilder.ts)

- Add guard logic for `parseInt` to ensure zero-value fallbacks instead of `NaN`.

## Open Questions

- None at this stage. Logic is well-understood.

## Verification Plan

### Automated Tests

- `npx tsc --noEmit` to verify type safety across the new hook signatures.

### Manual Verification

1. Open Admin Tools → Verify Timeline scrolls smoothly.
2. Open Stats → Verify device/OS info displays correctly.
3. Modify an Product Category → Save → Verify it persists to Supabase.
4. Check app logs for any `NaN` payload warnings in the protocol builder.

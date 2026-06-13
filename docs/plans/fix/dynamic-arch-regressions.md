# Sanitization Audit: Dynamic Product Architecture (Updated)

This plan aims to resolve remaining ReferenceErrors and strip out hardcoded "binary" product logic (HALOZ vs SOULZ) that persists after the dynamic architecture migration. We will unify all hardware behavioral logic under the `LOCAL_PRODUCT_CATALOG` definitions.

## Phase 4: Sanitization Regressions & Final Polish

### [Component Name] Type System & Registry

#### [MODIFY] [ProductCatalog.ts (Types)](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/ProductCatalog.ts)

- Add `vizThemeColor: string` to `ProductProfile`.
- Add `allowsLedCountTrim: boolean` to `ProductProfile`.

#### [MODIFY] [useRegistration.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useRegistration.ts)

- Add `custom_name?: string` to `RegisteredDevice` interface.

### [Component Name] Onboarding & Dashboard UI

#### [MODIFY] [ProductCatalog.ts (Constants)](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/ProductCatalog.ts)

- Populate `vizThemeColor` for HALOZ (#00F0FF), SOULZ (#FFAA00), and RAILZ (#FF00FF).
- Populate `allowsLedCountTrim`.

#### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)

- Refactor the conditional LED trim UI to use `allowsLedCountTrim` from the matched profile.

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

- Fix the `AccountModal` mapping to safely handle optional `id` and include `custom_name`.

---

## Open Questions

- **RAILZ Color**: I've assumed Magenta (#FF00FF) for RAILZ as a placeholder. Does this match the intended brand identity?
- **RAILZ Trimming**: Confirming that RAILZ (strips) allow LED count trimming in the UI.

---

## Verification Plan

### Automated Tests

- `npx tsc --noEmit` must return 0 errors.

### Manual Verification

- Open the Hardware Setup Wizard and verify colors match the product type.
- Verify that LED trimming is ONLY visible for SOULZ and RAILZ.
- Open Account Settings to verify registered devices display correctly.

# Sanitization Audit: Dynamic Product Architecture

This plan aims to resolve remaining ReferenceErrors and strip out hardcoded "binary" product logic (HALOZ vs SOULZ) that persists after the dynamic architecture migration. We will unify all hardware behavioral logic under the `LOCAL_PRODUCT_CATALOG` definitions.

## User Review Required

> [!IMPORTANT]
> **RAILZ Support**: This audit will officially enable RAILZ as a first-class citizen in the UI, replacing heuristic "is it Haloz?" checks with catalog-driven properties.

## Proposed Changes

### [Core] Product Catalog Enhancements

#### [MODIFY] [ProductCatalog.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/ProductCatalog.ts) (or its type)
- Add `vizIsMirrored: boolean` to the `ProductProfile` interface. This replaces `isHaloz` for driving the "Seg2 mirrors Seg1" visualizer logic.
- Ensure RAILZ has correct defaults (2000mAh, 0x51 support).

---

### [UI] Visualizer Sanitization

#### [MODIFY] [ProductVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/ProductVisualizer.tsx)
- **Fix ReferenceError**: Verify all usages of `isHaloz` are within scope.
- **Remove isHaloz**: Replace `const isHaloz = productProfile.id === 'HALOZ'` with references to `productProfile.vizIsMirrored` and `productProfile.vizShape`.
- **RAILZ Geometry**: Ensure `vizShape === 'DUAL_STRIP'` handles 2000mAh/high-density rendering correctly.

---

### [UI] Controller Sanitization

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Update Types**: Update `ProductType` to include `'RAILZ'`.
- **Remove Heuristics**: Delete `const isHalozRing = segs === 2 && pts === 16;`.
- **Dynamic Mirroring**: Replace `if (isHalozRing)` with `if (productProfile.vizIsMirrored)`.
- **Dynamic Descriptions**: Update comment blocks to reference the catalog rather than specific hardcoded models.

---

### [UI] Setup Wizard Sanitization

#### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Remove Fallbacks**: Replace `|| 'SOULZ'` and hardcoded `43` points with `LOCAL_PRODUCT_CATALOG[0].id` and the corresponding profile defaults.
- **Dynamic Grouping**: Update naming heuristics to be more generic or catalog-aware.

## Open Questions

- None at this time. The data requirements for the catalog were confirmed in the previous session (SOULZ/RAILZ 2000mAh, HALOZ 1200mAh).

## Verification Plan

### Automated Tests
- `run_command`: `npx tsc` to verify no new type errors were introduced during the refactor.
- `browser_subagent`: Open the dashboard and verify the `ProductVisualizer` renders without crashing.

### Manual Verification
- Verify that HALOZ still displays its mirrored ring geometry.
- Verify that SOULZ still displays its U-shaped oval geometry.
- Verify that the Setup Wizard correctly pulls default LED counts from the catalog.

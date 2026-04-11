# [PLAN] Post-Migration Bug Fix & Sanitization Audit

## Goal
The goal of this task is to:
1. **Critical Repair**: Resolve the `isHaloz is not defined` ReferenceError in `ProductVisualizer.tsx` that crashed the app.
2. **Sanitization Audit**: Remove remaining hardcoded "binary" logic (`isHaloz`, `isSoulz`, or hardcoded point counts) in `DockedController`, `ZenggeProtocol`, and `HardwareSetupWizardScreen` to ensure the new catalog-driven architecture is universally applied.

### Design Decisions & Rationale
We are moving from a "binary" hardware model (Haloz vs. Soulz) to a "catalog" model. To prevent regressions, we must define the `isHaloz` flag as a derived property of the `ProductProfile` instead of a prop. I'm choosing to keep the `isHaloz` naming internally within the visualizer to minimize risk while technically grounding it in the catalog lookup.

## User Review Required
> [!IMPORTANT]
> This plan includes a minor refactor of the `ZenggeProtocol` constants to remove `SK8_DEFAULTS`. This is a low-risk cleanup that ensures we don't have dual sources of truth for hardware specs.

## Proposed Changes

### 1. Product Visualizer (Critical Repair)
Fix the `ReferenceError` caused by missing variable declarations within the `leds` useMemo.

#### [MODIFY] [ProductVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/ProductVisualizer.tsx)
- Define `const isHaloz = productProfile.id === 'HALOZ'` at the top of the `leds` useMemo block.
- Define `const isHalozSeg2 = isHaloz && deviceSegments > 1 && i >= (renderLeds / 2)` inside the `renderLeds` loop.
- **Boy Scout Cleanup**: Remove unused `simMode` and `_simMode` variables/props if they are orphaned.

### 2. Zengge Protocol (Consistency Audit)
Remove the redundant `SK8_DEFAULTS` to ensure the `ProductCatalog` is the single source of truth.

#### [MODIFY] [ZenggeProtocol.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- Delete the `SK8_DEFAULTS` constant.
- Redirect any protocol-level defaults to use `LOCAL_PRODUCT_CATALOG`.

### 3. Hardware Setup Wizard (Modernization)
Bring the setup wizard into the dynamic product architecture.

#### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- Dynamically render discovered device groups based on all entries in `LOCAL_PRODUCT_CATALOG` (removing the hardcoded HALOZ/SOULZ/UNKNOWN filter logic).
- Replace hardcoded `43` points in `handleBlinkDevice` with a lookup to the catalog for that product's default points.

### 4. Controller Review (Surgical Strike)
Check for any remaining binary checks in the main controller.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Ensure all `isHalozRing` checks are properly scoped or derived from the catalog.

## Open Questions
- **None**. The path forward is clear.

## Verification Plan

### Automated Tests
- None.

### Manual Verification
1. **Visualizer Test**: Load the controller. Ensure the `isHaloz is not defined` crash is gone and the visualizer renders correctly for HALOZ, SOULZ, and RAILZ.
2. **Setup Wizard Test**: Open Setup Wizard. Ensure RAILZ devices (if simulated) appear in the discovery list.
3. **Blink Test**: Trigger a "Blink" in the wizard and verify (via console logs) that the payload uses the correct points for the selected product.

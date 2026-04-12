# [FIX] Dynamic Architecture Regressions

## Goal
Resolve 'isHaloz' ReferenceError in ProductVisualizer and perform a sanitization audit (DockedController, ZenggeProtocol, Setup Wizard) to remove remaining hardcoded binary logic.

## Proposed Changes
- Audit `ProductVisualizer.tsx` for `isHaloz` / `isSk8lytz` hardcoded checks.
- Replace with `ProductCatalog` heuristics.
- Audit `ZenggeProtocol.ts` for hardcoded 0x51/0x73 logic specific to legacy builds.
- Sanitize `HardwareSetupWizardScreen.tsx`.

## Verification Plan
- Build app.
- Open Setup Wizard.
- Verify no 'ReferenceError' in logs.

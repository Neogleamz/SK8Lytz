# Dynamic Architecture Regressions & Sanitization Audit

### Design Decisions & Rationale
We are fully deprecating the legacy `if HALOZ` / `if SOULZ` binary checks. By mapping the UI and logical components explicitly loop over the `LOCAL_PRODUCT_CATALOG`, we ensure that any new products (like RAILZ) added into the generic offline catalog will instantly propagate through the Setup Wizard, Modals, and Docked Controller without needing to hunt down and update a dozen files. Geometry checks will be refactored to look at the profile properties like `vizShape` instead of the raw brand name.

## Background Context
During the transition to the Dynamic Product Catalog (`LOCAL_PRODUCT_CATALOG`), some components were updated to use product profiles to derive metadata (like `vizShape`, `vizThemeColor`, etc), but a lot of the UI layer still relies on the legacy binary heuristic (`if (type === 'HALOZ') { ... } else { ... }`). This prevents the seamless addition of new products like `RAILZ` because the UI expects exactly two shapes. Additionally, there are mentions of a `isHaloz` ReferenceError which appears to be partially resolved in `ProductVisualizer`, but its ghost variables still persist elsewhere.

## Proposed Changes

### Component: `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` (Setup Wizard)
- **[MODIFY] HardwareSetupWizardScreen.tsx**
  - **Dynamic Grouping**: We currently use `hCount` and `sCount` to generate default names ("My SK8Lytz HALOZ" vs "My... SOULZ"). We will modify this to select the most frequently observed `product.displayName` in the selection.
  - **Color Syncing**: Replace hardcoded hex values with the product's `vizThemeColor` dynamically looked up via `getLocalProfileById(pType)`.
  - **Product Mapping**: Replace `as 'HALOZ' | 'SOULZ' | 'RAILZ'` explicit narrowings with the dynamic string IDs.

### Component: `src/components/FirstTimeSetupModal.tsx` (Setup Modal)
- **[MODIFY] FirstTimeSetupModal.tsx**
  - **Dynamic Map**: Instead of hardcoding `<Text style={{ color: '#00f0ff' }}>HALOZ</Text>` and `<Text style={{ color: '#a855f7' }}>SOULZ</Text>`, group the scanned devices by their `product_type` using `LOCAL_PRODUCT_CATALOG`. 
  - Render lists dynamically so if a `RAILZ` device is detected, it automatically spins up a RAILZ-branded section with its respective `vizThemeColor`.
  
### Component: `src/components/DockedController.tsx`
- **[MODIFY] DockedController.tsx**
  - **Constant Cleanup**: Rename the remaining legacy `isHalozRing` to `isRingShape` so it reflects geometry logic rather than product branding.
  - **Dynamic Tabs**: Lines 1494-1510 hardcode the "HALOZ" and "SOULZ" product selector tabs. We will replace this with a `map()` loop over `LOCAL_PRODUCT_CATALOG` to map `Product Selector` tabs with dynamic gradient colors based on `vizThemeColor`.

### Component: `src/screens/DashboardScreen.tsx`
- **[MODIFY] DashboardScreen.tsx**
  - **Type Loosening**: Remove the type cast `type: getLocalProfileByPoints(...).id as 'HALOZ' | 'SOULZ'` near line 1874 and let it use string so it is fully dynamic.

### Component: `src/protocols/ZenggeProtocol.ts`
- **[MODIFY] ZenggeProtocol.ts**
  - Correct any leftover comments referencing "HALOZ/SOULZ" binary splits specifically regarding the `hasMic` boolean. We will update the docs/comments to specify that we refer to device capabilities rather than specific product subsets. (e.g., `// always false for HALOZ/SOULZ` becomes `// depends on product capabilities`). 

### Component: `src/components/Sk8LytzProgrammerModal.tsx`
- **[MODIFY] Sk8LytzProgrammerModal.tsx**
  - **Active Profile**: `const [activeProfile, setActiveProfile] = useState<ActiveProfileType>('HALOZ');`. Update to map across the catalog and dynamically select UI accent colors (`activeProfile === 'HALOZ' ? cyan : amber`) using `getLocalProfileById(activeProfile)?.vizThemeColor`.

## UI/UX & Platform Strategy
- **Scalability**: By rendering arrays using `LOCAL_PRODUCT_CATALOG.map`, any future hardware added to the catalog (e.g. RAILZ, WHEELZ) will automatically populate their tabs, section headers, and default colors without needing code modifications in 5 different files.
- **Micro-Interactions**: The tab components in `DockedController` will continue to use LinearGradients, dynamically fetching colors from the new catalog. 

## Open Questions
- The `isHaloz` ReferenceError in `ProductVisualizer` appears to have already been patched natively in a previous commit (the variable is missing from the file), but I am ready to resolve the remaining legacy variables. 

## Verification Plan
1. **Automated Tests**: Run TypeScript compilation `npx tsc --noEmit` and check that `ProductVisualizer` has no ReferenceErrors, and that type casting does not fail natively inside the components.
2. **Setup UI Test**: View the `HardwareSetupWizardScreen` dynamically generating groups. 
3. **Controller Test**: Ensure `DockedController` successfully maps HALOZ, SOULZ, and RAILZ in the tabs visually across the top correctly natively fetching `vizThemeColor`.

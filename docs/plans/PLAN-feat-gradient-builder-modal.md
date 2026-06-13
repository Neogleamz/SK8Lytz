# Gradient Builder Modal & Library Refactor

Provide a dedicated, 80% screen-height modal workspace for creating and editing custom gradients, while converting the main BUILDER tab into a library for saved custom gradients.

## User Review Required

> [!WARNING]
> **Data Persistence Schema**
> Currently, we save preset combinations to `user_saved_modes` in Supabase. We will inject custom gradients into this exact same table by using a new `mode_type = 'GRADIENT'` flag, instead of creating a brand new table. The payload will be stored in a JSON column. 

## Resolved Decisions

> [!NOTE]
> **Starter Templates (Built-ins)**
> The Gradient Library will come pre-loaded with four built-in presets that cannot be deleted:
> 1. Rainbow
> 2. USA
> 3. Cyberpunk (Neon Pink/Cyan/Purple)
> 4. Fire (Red/Orange/Yellow)

## Proposed Changes

### [UI Components]

#### [MODIFY] [UnifiedPatternPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx)
- Re-wire the `BUILDER` tab to render `GradientLibraryTab` instead of the raw `GradientBuilderTab`.
- Add state for `isGradientModalVisible`.

#### [NEW] [GradientLibraryTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/GradientLibraryTab.tsx)
- A new tab view that displays a massive `+ Create Custom Gradient` button.
- Renders a grid of `GradientCard` components mapping over the user's saved gradients AND the 4 hardcoded built-in gradients (Rainbow, USA, Cyberpunk, Fire).

#### [NEW] [GradientBuilderModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/GradientBuilderModal.tsx)
- A transparent `<Modal animationType="slide">`.
- Outer container has `paddingTop: '20%'` to leave the `LEDStripPreview` visible.
- Inner container has dark backdrop blur and takes up the bottom 80%.
- Move all existing gradient logic (color nodes, transition types, sliders) from `GradientBuilderTab` into this modal.
- Add a sticky `Save to Library` button at the bottom.

#### [DELETE] [GradientBuilderTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/GradientBuilderTab.tsx)
- This file is retired. Its UI is moved into the Modal, and its root placement is replaced by the Library tab.

---

### [State & Persistence]

#### [NEW] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- A service class mapping directly to `user_saved_modes` where `type = 'GRADIENT'`.
- Implements Local `AsyncStorage` caching first, with background sync to Supabase.
- Handles CRUD operations for Custom Gradients.

#### [NEW] [useGradients.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGradients.ts)
- React hook to subscribe to the custom gradients list.

## Verification Plan

### Automated Tests
- `npx tsc --noEmit` from master fortress to ensure no broken imports from the deleted `GradientBuilderTab`.

### Manual Verification
1. Navigate to BUILDER tab. Confirm it shows the library and the big "Create" button.
2. Tap "Create". Confirm the 80% modal slides up and the top 20% remains transparent, showing the live LED preview.
3. Add a color node. Confirm the live LED preview instantly reflects the new color.
4. Tap Save. Confirm the modal dismisses and the new gradient appears in the Library list.
5. Close the app completely, turn off WiFi, reopen the app. Confirm the saved gradient still loads from Local Storage.

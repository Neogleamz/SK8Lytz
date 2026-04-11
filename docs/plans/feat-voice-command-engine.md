# Voice Engine Integration Plan

This plan outlines the final steps to integrate the offline voice command engine into the SK8Lytz application, bridging natural language commands to hardware execution.

## Design Decisions & Rationale
- **Imperative Bridge**: We use `useImperativeHandle` on `DockedController` to allow the `DashboardScreen` (where the Voice Engine lives) to trigger complex lighting state changes without forcing a massive props drilling or global store refactor.
- **Top-Level Entry**: The `VoiceFAB` is placed on the `DashboardScreen` to satisfy the requirement of "always" being available while skating, providing high-visibility access for hands-free control.
- **Spatial Mapping**: Voice-parsed spatial segments ("red in the back") are mapped to the `PositionalMathBuffer` by generating temporary `BuilderNode` configurations, ensuring the "build your own" vision is fulfilled.

## Proposed Changes

### [DockedController](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
-   Extend `DockedControllerHandle` to include:
    -   `loadFavorite(fav: IFavoriteState)`
    -   `setActiveMode(mode: ModeType)`
    -   `setBrightness(val: number)`
    -   `setSpeed(val: number)`
    -   `handleRbmChange(id: number)`
    -   `applySpatialSegments(segments: ISpatialSegment[])`
-   Implement `applySpatialSegments` internal handler:
    -   Switches mode to `MULTIMODE` / `BUILDER`.
    -   Maps `BACK`, `MIDDLE`, `FRONT` to specific node positions (0, 50, 100).
    -   Updates `builderNodes` and triggers hardware sync.

---

### [Dashboard](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
-   Import `VoiceFAB` and `VoiceCommandModal`.
-   Update `dockedControllerRef` type from `any` to `DockedControllerHandle`.
-   Implement `handleVoiceAction(action: IVoiceAction)`:
    -   Dispatches to `dockedControllerRef.current` based on action type.
    -   Handles `FAVORITE`, `FIXED_PATTERN`, `SPEED`, `BRIGHTNESS`, `MODE`, and `SPATIAL_COLORS`.
-   Render `VoiceFAB` and `VoiceCommandModal` within the screen container.

## Verification Plan

### Automated Tests
-   Verify `resolveCommand` in `VoiceService.ts` via temporary unit test to ensure spatial strings map to correct segments.

### Manual Verification
-   **Voice Entry**: Tap FAB -> Confirm Modal appears with Pulse animation.
-   **Command Execution**: Say "Street mode" -> Verify `DockedController` switches to Street tab.
-   **Spatial Testing**: Say "Red in the back, white in the front" -> Verify `BuilderNodes` update correctly in the UI and hardware (if connected).
-   **Fuzzy Matching**: Say a favorite name (e.g., "Rainbow") -> Verify it selects the correct preset.

# Camera Mode UX & Color Capture Overhaul

## Objective
Improve the "Camera Mode" in `DockedController.tsx` (or whatever component handles it) to accurately sample colors from the camera feed, expand the viewfinder to fill vertical space, and reduce the height of the selected color bar.

## Background
Users report that the camera mode color extraction is inaccurate. Additionally, the UI layout for the Camera Panel is poorly proportioned—the camera preview does not fill all available vertical space, and the "Selected Color" bar is unnecessarily tall, wasting screen real estate.

## Proposed Strategy
1. **Color Extraction Algorithm:** Investigate the underlying color sampling (e.g., using `expo-camera` or `expo-image-manipulator` or a fast-average algorithm over the center pixels) and replace it with a more robust dominant-color or center-reticle average algorithm. 
2. **Layout Expansion:** Update the Flexbox constraints on the Camera viewfinder container so it uses `flex: 1` properly, scaling to vertical bounds without hardcoded heights.
3. **Color Bar Reduction:** Inspect the padding and absolute height of the `SelectedColorBar` within the camera panel and slim it down to a dense, compact indicator.

## Risk Assessment
- **Risk Level:** L-RISK
- **Impact:** Medium (UI/UX enhancement)
- **Complexity:** Meal (Requires tweaking layout constraints and optimizing a hot-path color sampling algorithm).

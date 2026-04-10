# Implementation Plan: Camera Touch Precision Fix (fix-camera-touch)

### Design Decisions & Rationale
To fix the imprecise color selection in the Camera Mode, we will reduce the image manipulation crop size from a 20x20 area down to an exact 1x1 pixel (`cropSize = 1`). By applying `Math.floor()` to the mapped touch coordinates, we ensure the exact pixel under the user's finger is extracted. The existing Vividness Normalization Algorithm will remain unchanged, as it perfectly supports evaluating a single pixel's RGB channel balance.

### Proposed Changes

#### `src/components/CameraTracker.tsx`
- **Modify:** Change `cropSize` from `20` to `1`.
- **Modify:** Update the `originX` and `originY` calculation to use `Math.floor()` and center strictly on the exact touched coordinate (`x_p` and `y_p`) instead of buffering out an arbitrary bounding box.

### UI & Platform Strategy
React Native's `nativeEvent.locationX` and `locationY` provide the exact point of contact relative to the `CameraView`. By converting this directly as a percentage of the exact `photo.width` and `photo.height`, we guarantee 1:1 cross-platform parity on both iOS and Android regardless of their physical camera resolutions, ensuring the visual crosshair perfectly matches the mathematical pixel sampled.

### Verification Plan
- **Manual Verification:** Build the app on a physical device, open Camera Mode, and tap highly specific intersecting points (like the edge of a screen) to verify the sampled color hex precisely mirrors the single pixel targeted rather than bleeding into surrounding averages.

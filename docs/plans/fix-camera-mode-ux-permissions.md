# Fix Camera Mode Permissions & Layout

The current Camera Mode requires the user to manually dive into their phone's settings instead of prompting directly, and the viewport is constrained with padding that wastes precious screen real estate. The bottom color bar is also floating instead of docked.

## Proposed Changes

### 1. Fix Camera Permissions

There is an issue where `permission.canAskAgain` evaluates poorly on some Android devices, leading to an immediate "Open Settings" block instead of displaying the native permission prompt.

- We will update `CameraTracker.tsx` to automatically request permissions via an effect if the status is undetermined.
- We will replace the conditional button with a "Click to Request" button that relies entirely on `expo-camera`'s `requestPermission()` method before resorting to OS settings.

### 2. Expand Layout Constraints

The camera view should be edge-to-edge (or as close to it as the container allows).

- **DockedController.tsx**: Remove the horizontal padding (`paddingHorizontal: Layout.padding`) on the `CAMERA` mode wrapper.
- **CameraTracker.tsx**:
  - Remove the bounding `borderWidth` and `borderRadius` constraints to make it completely flush.
  - Redesign the `statusBox` so it is no longer a floating oval, but a 100% width docked bar at the absolute bottom of the viewport with no margins.

## User Review Required

Please review the plan above. Type **'proceed'** or **'approved'** to execute the changes!

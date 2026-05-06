# Sniper Dropper UX (Vision Camera Redesign)

This is the revised architectural plan to build the "Sniper Dropper" feature with **true 60fps real-time video frame processing**, completely eliminating the photo shutter logic.

## Goal
Swap out the standard `expo-camera` for `react-native-vision-camera` to gain access to C++ Native Frame Processors. Extract the center pixel color in real-time as the user pans their phone, rendering the live color into a new Sniper Reticle UI.

## User Review Required

> [!WARNING]
> **Native Dependency Swap:** This plan introduces `react-native-vision-camera` and `react-native-worklets-core`. Because these contain custom native C++ and Kotlin/Swift code, **your current Android build will be invalidated.** After I merge this code, I will need to trigger a fresh `gradle assembleRelease` to compile the new native libraries into the `.apk`. You will then need to reinstall the APK on the device.

## Proposed Changes

### 1. Dependency Management
- **[NEW]** Install `react-native-vision-camera` and `react-native-worklets-core`.
- **[DELETE]** Uninstall `expo-camera`.
- **[MODIFY]** Create a `babel.config.js` (if absent) to inject the `react-native-worklets-core/plugin` required for frame processors.

### 2. Component Refactoring: `CameraTracker.tsx`
- **[MODIFY]** Completely rewrite `CameraTracker.tsx` to use `<Camera />` from `react-native-vision-camera`.
- **[NEW]** Implement a `useFrameProcessor` worklet that grabs the current frame buffer and calculates the RGB values of the center matrix of pixels, sending it to the JS thread synchronously.
- **[MODIFY]** Add the frosted-glass Sniper Reticle UI and a massive "CAPTURE" FAB at the bottom of the screen.

### 3. Permissions
- **[MODIFY]** Update `PermissionService.ts` to use Vision Camera's `Camera.requestCameraPermission()` instead of Expo's permission hooks.

## Verification Plan

### Automated / Build
1. Execute `npx tsc --noEmit` from the master fortress to ensure Vision Camera types are correctly resolved.
2. Run `gradle assembleRelease` to confirm the new native C++ dependencies compile successfully for Android.

### Manual Verification
1. Install the new APK on the device.
2. Navigate to Camera Mode and accept the new Vision Camera permission prompt.
3. Verify the video feed is smooth at 60fps.
4. Verify the center reticle instantly changes color as you pan the phone across different colored objects.
5. Tap "CAPTURE" and verify the skates update immediately.

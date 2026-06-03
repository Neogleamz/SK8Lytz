# Fix Camera Vibe Sliders Interaction

Expose the Speed slider in Camera Vibe Mode and update both the Speed and Brightness sliders to correctly scale and dispatch the custom 3-color Vibe gradient instead of resetting to a solid color.

## User Review Required

> [!NOTE]
> This change ensures that when you create and apply a custom color palette in Camera Vibe Mode, adjusting the Brightness or Speed slider will modify that custom gradient in real-time and send it to the skates, rather than overwriting the pattern with a solid color.

## Open Questions

None.

## Proposed Changes

### UI Components

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L1262-L1325)
Pass the `cameraSubMode` state variable to the `<UniversalSlidersFooter>` element:
```typescript
             builderTransitionType={builderTransitionType}
             builderDirection={builderDirection}
+            cameraSubMode={cameraSubMode}
           />
```

#### [MODIFY] [UniversalSlidersFooter.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- Add `cameraSubMode?: 'SNIPER' | 'VIBE'` to `UniversalSlidersFooterProps`.
- Update the SPEED slider visibility logic to show it when `activeMode === 'CAMERA'` and `cameraSubMode === 'VIBE'`:
  ```typescript
  - {!(activeMode === 'MUSIC' || activeMode === 'CAMERA') && (
  + {!(activeMode === 'MUSIC' || (activeMode === 'CAMERA' && cameraSubMode === 'SNIPER')) && (
  ```
- Update the BRIGHTNESS and SPEED sliders' `onSlidingComplete` handlers to support `(activeMode === 'CAMERA' && cameraSubMode === 'VIBE')`. Group it with the `BUILDER` condition so that it processes the vibe nodes (`builderNodes`), scales the brightness, and dispatches the multi-color array:
  ```typescript
  - } else if ((activeMode === 'MULTIMODE' && fixedSubMode === 'BUILDER') || activeMode === 'BUILDER') {
  + } else if ((activeMode === 'MULTIMODE' && fixedSubMode === 'BUILDER') || activeMode === 'BUILDER' || (activeMode === 'CAMERA' && cameraSubMode === 'VIBE')) {
  ```

## Verification Plan

### Automated Tests
- Run `npm run verify` to confirm typescript compilation.

### Manual Verification
- Open the controller, navigate to the Camera tab, and switch to Vibe Sub-Mode.
- Capture/Apply a 3-color palette to the skates.
- Verify that the Speed slider is visible.
- Move the Brightness or Speed slider and release.
- Verify that the skates are updated with the correct brightness/speed for the 3-color gradient, rather than resetting to a solid color.

# Implementation Plan: Camera Mode v2 — Sniper + Vibe Catcher

> **Slug**: `feat/camera-vibe-catcher-v2`
> **Approved**: 2026-05-27 — All design decisions and architectural safety gates verified.
> **Batch**: `[BATCH:camera-v2]`

---

## 1. Goal

Completely replace the broken native camera snapshot/crop pipeline with a cross-platform VisionCamera v5 Frame Processor + `vision-camera-resize-plugin` approach. Integrate the planned `vibe-catcher-palette-extractor` into a unified **SNIPER / VIBE** dual sub-mode camera system.

---

## 2. User Review Required (Safety & Architectural Gates)

### 🚨 Safety Gate 1: EEPROM Buffer Overflow Defense (12-Pixel Minimum)
*   **The Hazard**: Payloads sent via `0x59` Static Colorful must have $\ge 12$ RGB pixels. If we send a 3-color palette directly as a 3-pixel array, the `0xA3` controller suffers physical EEPROM buffer lockout and state crashes.
*   **The Solution**: We will map the 3 extracted VIBE colors (FG, BG, ACCENT) to a full canvas of size `ledPoints` (defaults: HALOZ = 8 segments of 2, SOULZ = 43). For `0x59` dispatches, if `ledPoints < 12` (such as HALOZ which has `ledPoints = 8`), we will pad/duplicate the pixel array up to **12** to prevent hardware crashes, and set the payload strip length bytes to 12. For SOULZ, we will interpolate the colors to fill all 43 slots (or repeat them smoothly), satisfying the 12-pixel minimum.

### 🚨 Safety Gate 2: CPU Worklet Rate-Limiting (5Hz Throttle)
*   **The Hazard**: VisionCamera processes frames at 30fps. Running Euclidean distance clustering on 2,500 pixels at 30fps consumes too much CPU, causing JS thread lockups.
*   **The Solution**: Frame processor runs at 30fps but K-Means/center-read execution is hard-throttled to **5fps (every 200ms)** using a worklet timestamp check.

### 🚨 Safety Gate 3: BLE Mutex Preservation (Hybrid Dispatch)
*   **The Hazard**: Continuous BLE writes of scanned color frames saturate the Bluetooth radio and cause disconnects.
*   **The Solution**: Active camera frame processing only updates UI states (ProductVisualizer preview, viewfinder overlays, swatches). **BLE payloads are only sent on manual taps** ("Capture Color" in SNIPER, "APPLY VIBE" in VIBE).

---

## 3. Proposed Changes

### [A] Color Utility

#### [NEW] [kMeansPalette.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/kMeansPalette.ts)
Implement a pure, type-safe JS K-Means clustering utility with zero external dependencies:
- Euclidean RGB distance calculation:
  $$d(p_1, p_2) = \sqrt{(r_1 - r_2)^2 + (g_1 - g_2)^2 + (b_1 - b_2)^2}$$
- Initial centroids: picked randomly or spread across extreme color spaces (white, red, black).
- 5 clustering iterations max.
- Output: Array of 3 dominant RGB colors sorted from most to least dominant.

### [B] Camera Processing Core

#### [NEW] [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- Replaces platform-split `CameraTracker.android.tsx` and `CameraTracker.ios.tsx`.
- Uses `useFrameProcessor` from `react-native-vision-camera` + `vision-camera-resize-plugin`.
- Viewport size: GPU-scaled down to 50x50 pixels.
- Dynamic Sub-mode routing:
  - **SNIPER**: Sample center pixel index `(25, 25) * 3` in the RGB array. Run `runOnJS(onSniperColor)(r, g, b)`.
  - **VIBE**: Feed 2,500 RGB pixels to K-Means worklet. Run `runOnJS(onVibePalette)(colors)`.

#### [DELETE] [CameraTracker.android.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.android.tsx)
#### [DELETE] [CameraTracker.ios.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.ios.tsx)

### [C] View Layer & Controller Wiring

#### [MODIFY] [CameraPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/CameraPanel.tsx)
- Add glassmorphic camera frame.
- Add concentric circle shutter button with spring haptics on tap.
- Add iOS-style SNIPER / VIBE sub-mode sliding switch.
- **SNIPER Control View**: Viewfinder with crosshair reticle, capture trigger, and 5-swatch color history.
- **VIBE Control View**: Smoothly interpolated liquid gradient preview, 3 giant swatches showing hex values, Static / Flow toggle, and "APPLY VIBE" button.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Connect sub-mode toggling, and wire the K-Means color array to `ProductVisualizer`.
- **`handleVibeApply(colors: RGB[], isFlow: boolean)`**:
  - Auto-generate the `BuilderNode[]` array using:
    ```typescript
    const vibeNodes: BuilderNode[] = [
      { id: 'vibe_fg',     position: 0,   colorHex: rgbToVividHex(colors[0]) },
      { id: 'vibe_bg',     position: 50,  colorHex: rgbToVividHex(colors[1]) },
      { id: 'vibe_accent', position: 100, colorHex: rgbToVividHex(colors[2]) },
    ];
    setBuilderNodes(vibeNodes);
    ```
  - Map transition type: `Static` (0x01) vs `Flow` (0x03).
  - Enforce the 12-pixel minimum. Form an array of `max(12, ledPoints)` by repeating or interpolating colors.
  - Dispatch: Fire `setMultiColor(paddedColors, points, speed, 1, transitionType)`.
- Re-enable `UniversalSlidersFooter` for the camera, restricting it to the global brightness controller.

#### [MODIFY] [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json)
- Add dependency `"vision-camera-resize-plugin": "^5.0.0"` (or user-approved version).

---

## 5. Verification Plan

### Automated Verification
- **Compilation**: `npm run verify` (runs strict `tsc --noEmit` and Jest tests).
- **Unit Tests**: Add tests to `src/utils/__tests__/kMeansPalette.test.ts` to assert deterministic color extraction.

### Manual Smoke-Testing (LAB Layer)
- **Logcat Ble Sniffing**: Run `adb logcat -s "ReactNativeJS:V"` to confirm exact hex packet composition.
  - Assert that SNIPER captures dispatch solid colors via `0x56` or `0x59`.
  - Assert that VIBE apply dispatches gradient arrays via `0x59` containing $\ge 12$ RGB nodes.
- **Visual Smoke Test**: Verify transition toggle spring physics and concentric shutter tap state.

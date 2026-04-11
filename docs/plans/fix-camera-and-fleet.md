# Goal Description

Restore accuracy and usability to the Camera Mode's color picker by eliminating noise susceptibility and replacing the destructive color normalization algorithm with an intelligent HSL-based vividness boost.

### Design Decisions & Rationale

We are increasing the sampled crop from 1 pixel to a 10x10 area to average out camera noise and give a much more stable color reading. For vividness, we'll swap out the raw RGB min/max subtraction—which currently turns pastels into neon colors—with a smart HSL (Hue, Saturation, Lightness) conversion that bumps up Saturation by 20% to help LED vibrancy without permanently warping the actual color picked.

## Proposed Changes

### Camera Mode Logic

#### [MODIFY] `src/components/CameraTracker.tsx`

- **Sampling Grid (Lines 76-80):** Increase `cropSize` from `1` to `10`. Ensure the touch boundary constraints account for the grid size so panning at the edge of the screen doesn't run out of bounds.
- **Normalization Update (Lines 104-115):** RIP the old `avgR - min / max - min` logic. Implement an RGB-to-HSL algorithm, inflate the `s` (saturation) channel by +0.2 (maxing at 1.0), and convert HSL back to RGB. This achieves the vivid look without destroying whites and grays.
- **Performance consideration (Line 66):** Lower image quality (`quality: 0.1` instead of `0.2`) as we are taking a much larger crop, this offsets memory pressure and ensures fast response times.

## Verification Plan

### Automated Tests
- N/A

### Manual Verification
1. Navigate to Camera Mode in the app.
2. Tap a pure white object or light gray object and verify the color returned is white/gray, not a random neon hue.
3. Tap a colorful object (e.g., green apple) and verify a robust, highly saturated green is returned.
4. Verify tapping along the extreme edges of the camera view does not crash the device or throw an out-of-bounds error.

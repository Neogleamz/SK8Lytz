# Fix Camera White Color Capture (Neutral Gate)

Increase the saturation threshold in the camera color boost function to properly classify warm/muted whites and grays as achromatic white instead of boosting them to yellow.

## User Review Required

> [!NOTE]
> Raising the neutral gate threshold from `0.05` to `0.20` makes white balancing on real-world surfaces far more forgiving. Achromatic objects (paper, white walls, gray asphalt) under typical warm indoor light or shaded outdoor light will correctly register as white/gray on the LEDs instead of triggering a saturated yellow or blue boost.

## Open Questions

None.

## Proposed Changes

### Core Color Utilities

#### [MODIFY] [ColorUtils.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/ColorUtils.ts#L94-L99)
- Modify the neutral gate check threshold in `boostForLED()` from `0.05` to `0.20` (or `0.18`).
- Update comments to document the new threshold choice.

```typescript
-  // NEUTRAL GATE: Only truly achromatic pixels (S < 0.05) pass through.
-  // Pastels, tinted grays, and muted colors all get boosted to vivid.
-  if (s < 0.05) {
+  // NEUTRAL GATE: Achromatic pixels (S < 0.20) pass through as white/gray.
+  // This prevents camera noise and warm indoor lighting from forcing off-whites to yellow.
+  if (s < 0.20) {
     const gray = Math.round(cMax * 255);
     return { r: gray, g: gray, b: gray };
   }
```

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure tests compile.
- Create or check if we can add a test case in a test script.

### Manual Verification
- Launch the camera view, point the sniper/reticle at a white sheet of paper or a white wall under warm lighting.
- Verify that the target color registers as white/light-gray and doesn't boost to a saturated yellow.
- Point the camera at a strongly colored object (e.g. red book, blue shirt) and verify that color boost still functions correctly.

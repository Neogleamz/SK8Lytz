### Design Decisions & Rationale
To ensure the `NeonHueStrip` slider dynamically stays in sync with the currently selected active foreground/background layer in "Pro Effects" (MULTIMODE Pattern) mode, we will introduce a `hexToHue` helper inside `DockedController.tsx`. When the user toggles between the FOREGROUND and BACKGROUND chips, we will instantly compute the `hue` value from the selected layer's stored hex color and invoke `setFixedHue()` to physically snap the slider handle to the correct position.

### Proposed Changes

#### [MODIFY] src/components/DockedController.tsx
- **Add Hex-to-Hue Math**: Introduce a lightweight `hexToHue(hex: string): number` pure function outside the main component.
- **Update Foreground Toggle**: Update the `onPress` handler for the FOREGROUND chip to calculate and set the exact hue from the stored `fixedFgColor` state.
- **Update Background Toggle**: Update the `onPress` handler for the BACKGROUND chip to calculate and set the exact hue from the stored `fixedBgColor` state.

### Verification Plan
**Automated Tests**:
N/A (UI-driven component)

**Manual Verification**:
1. Open up the Docked Controller and navigate to a mode using the Multi/Pattern builder (or Pro Effects).
2. Adjust the Foreground color slider to Red.
3. Tap the Background tab. Observe the color slider physically snap to match the Background's current hue.
4. Tap the Foreground tab again. The slider should snap back accurately to Red.

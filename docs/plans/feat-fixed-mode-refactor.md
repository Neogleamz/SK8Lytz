# Implementation Plan: Fixed Mode & Pattern Refactor

This plan replaces the legacy "Solid" mode with a professional "Fixed" mode that supports pattern selection (Strobe, Blink, Static) and restores the broken music color slider.

## Design Rationale
Parity with the Zengge APK "Fixed" mode. The current "Solid" mode is too simplistic. By adding pattern selection to the fixed color tab, we allow users to create "Stationary FX" (e.g. constant strobing red) without needing to use the complex multi-segment pattern engine.

## Proposed Changes

### [DockedController]
Adding the pattern selector to the Fixed/Solid tab.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/DockedController.tsx)
- Re-label "Solid" tab to "Fixed".
- Add a Horizontal pattern selector below the color wheel.
- Patterns: `Static`, `Strobe (Slow/Med/Fast)`, `Blink`.
- Wired to `applyFixedPattern(color, patternId)`.

#### [MODIFY] [MusicController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/MusicController.tsx)
- Restore the `colorSlider` logic that was accidentally disconnected during the last refactor.
- Ensure the selected music color is passed to the FFT engine during `Music Mode` execution.

### [Zengge Protocol]
Mapping fixed patterns to the 0x51/0x73 packet structures.

#### [MODIFY] [ZenggeProtocol.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/protocols/ZenggeProtocol.ts)
- Add `applyFixedPattern(color, patternId)` helper.
- Static = 0x51 with zero speed.
- Strobe = 0x51 with speed mapping.

## Verification Plan

### Manual Verification
1. Open Fixed Tab -> Select Blue -> Skates turn solid blue.
2. Select "Strobe" Pattern -> Skates strobe blue.
3. Open Music Tab -> Change Color Slider -> Verify visualizer color updates.

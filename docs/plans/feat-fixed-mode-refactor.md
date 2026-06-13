# Implementation Plan: Fixed Mode & Pattern Refactor

This plan replaces the legacy "Solid" concepts to fit seamlessly into our newly decomposed `DockedController` architecture. The goal is to elevate basic solid colors into a professional "Fixed Mode" tier supporting specific static hardware effects (Static, Strobe, Blink).

## Design Rationale

Parity with the Zengge APK "Fixed" mode. By adding pattern selection to a new FIXED tab, we allow users to create "Stationary FX" (e.g. constant strobing red) without needing to use the complex multi-segment pattern engine.

## Proposed Changes

### [DockedController]
Inject a new `FIXED` tab into the Floating Dock array, positioned between `FAVORITES` and `MULTI`.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Active Mode State: Add `'FIXED'` to the activeMode union and default handling. Add supporting `fixedModePattern` state (`'STATIC' | 'STROBE' | 'BLINK'`).
- Footer UI Integration: When `activeMode === 'FIXED'`, the universal footer renders the Color Grid. Tapping a color triggers action based on `fixedModePattern`.

### [FixedPanel]
A highly focused extraction specifically for the new feature.

#### [NEW] [FixedPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/FixedPanel.tsx)
- Pattern Selector: A horizontal segmented toggle (`STATIC`, `STROBE`, `BLINK`) to let users easily govern the LED behavior.
- Speed Control: If `STROBE` or `BLINK` is selected, display a localized Speed slider.

### [Zengge Protocol]
Mapping fixed patterns to the 0x51/0x73 packet structures.

#### [MODIFY] [ZenggeProtocol.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- Add `applyFixedPattern(color, patternId)` helper.
- Static = sendColor(r,g,b).
- Strobe = 0x51 with speed mapping.
- Blink = 0x51 with step jump mapping.

## Verification Plan

### Manual Verification
1. Open Fixed Tab -> Select Blue -> Skates turn solid blue.
2. Select "Strobe" Pattern -> Skates strobe blue.
3. Open Music Tab -> Change Mic Sensitivity Slider -> Verify visualizer reactivity updates.

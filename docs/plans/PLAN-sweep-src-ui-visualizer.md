# Implementation Plan: sweep-src-ui-visualizer

This is a synthesized sweep plan addressing all rule violations identified in the **UI_VISUALIZER** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### UI_VISUALIZER Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- **Line 49 [MEDIUM]:** requestPermission('CAMERA') returns a Promise but is called inside useEffect without catch() or error handling. (Rule: R-11)
- **Line 88 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 150 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)

#### [MODIFY] [CustomEffectVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomEffectVisualizer.tsx)
- **Line 18 [MEDIUM]:** hexToRgb helper is duplicated in CustomEffectVisualizer.tsx and protocols/SymphonyEngine.ts, which matches hexToRgb already defined in src/utils/ColorUtils.ts. (Rule: R-21)

#### [MODIFY] [NeonHueStrip.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/NeonHueStrip.tsx)
- **Line 99 [MEDIUM]:** Passes web-specific style parameters ('touchAction' and 'userSelect') directly to standard React Native 'View' component styles without checking if Platform.OS is web. (Rule: R-20)

#### [MODIFY] [GradientLibraryTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/GradientLibraryTab.tsx)
- **Line 45 [MEDIUM]:** Inline arrow functions inside FlatList item renderer 'renderGradientCard' recreate callbacks on every render. (Rule: R-28)
- **Line 132 [LOW]:** FlatList uses inline style objects for contentContainerStyle and columnWrapperStyle props, which are recreated on every render. (Rule: R-07)

#### [MODIFY] [PatternPickerTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/PatternPickerTab.tsx)
- **Line 66 [LOW]:** Casting 'string[]' array colors to tuple 'readonly [string, string, ...string[]]' required by expo-linear-gradient using 'as unknown as'. (Rule: R-08)
- **Line 115 [MEDIUM]:** The renderItem callback returns a View wrapper with an inline style definition (style={{ width: '48%' }}). Since renderItem runs for each item in the list, it allocates a new style object for every item, leading to memory thrashing and layout recalculations. The FlatList component also uses inline style objects for style, contentContainerStyle, and columnWrapperStyle. (Rule: R-07, R-28)
- **Line 160 [LOW]:** Inline style objects passed to FlatList 'contentContainerStyle' and 'columnWrapperStyle' props. (Rule: R-07)

#### [MODIFY] [UnifiedPatternPicker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx)
- **Line 62 [HIGH]:** writeToDeviceRef.current(payload) returns a Promise but is called as a floating promise without catch() or try/catch, risking unhandled rejection on connection failure. (Rule: R-11)

#### [MODIFY] [PositionalGradientBuilder.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/PositionalGradientBuilder.tsx)
- **Line 44 [MEDIUM]:** Hardcoded setTimeout delay utilized directly in a component to handle BLE write dispatch throttling instead of using a queue-managed or debounced hook structure. (Rule: R-16)
- **Line 55 [MEDIUM]:** Type safety violation where err in the promise catch handler is implicitly typed as any, and console.warn is logged without strict type enforcement or checking. (Rule: R-08, R-06)
- **Line 56 [LOW]:** Bypasses AppLogger telemetry context and formatting by using raw console.warn (Rule: R-04)

#### [MODIFY] [ProductVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/ProductVisualizer.tsx)
- **Line 29 [MEDIUM]:** Uses 'any' type in the 'onLongPressDevice' prop parameter 'device'. (Rule: R-08)

#### [MODIFY] [VerticalPatternDrum.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VerticalPatternDrum.tsx)
- **Line 144 [LOW]:** FlatList uses inline JSX with inline style objects for ListHeaderComponent and ListFooterComponent, leading to new element creation and style reconciliation on every render. (Rule: R-07)

#### [MODIFY] [VisualizerUnit.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VisualizerUnit.tsx)
- **Line 1 [LOW]:** File size is very close to the 30KB limit (30645 bytes / 29.93 KB), creating risk of exceeding the monolith boundary if extended. (Rule: R-23)
- **Line 32 [LOW]:** toHex / rgbToHex conversion code is duplicated locally inside VisualizerUnit.tsx (HSLToHex conversion function) and DockedController.tsx. (Rule: R-21)
- **Line 45 [MEDIUM]:** Uses 'any' type in the 'onLongPress' prop parameter 'device'. (Rule: R-08)
- **Line 488 [MEDIUM]:** Iterates over up to 86 LEDs and creates multiple nested Views/Animated.Views with inline dynamic styles. This causes heavy rendering overhead during animation ticks. (Rule: R-07)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

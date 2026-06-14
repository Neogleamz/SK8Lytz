# Implementation Plan: sweep-src-components-docked

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-docked` domain cluster.

## Batch & Wave
- **Wave:** 6
- **Prerequisite:** Wave 5 fully merged

## Proposed Changes
### [MODIFY] QuickPresetModal.tsx
- Line 75: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 89: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 90: Fix `R-09` violation. PII key 'name' passed with unscrubbed value 'safeName' in AppLogger call. (Suggested: Wrap resource name in scrubPII(safeName) or log the unique ID instead of name.)
### [MODIFY] PresetCard.tsx
- Line 53: Fix `R-07` violation. An inline arrow function is passed directly to the `onPress` prop of `TouchableOpacity` in `PresetCard` which is rendered inside a horizontal `FlatList` rendering hotpath. This causes new function instances to be created for each item card on every list render, bypassing child component rendering optimizations. (Suggested: Define a local handler within `PresetCard` using `useCallback`: `const handlePress = useCallback(() => { onPress(preset, onPressContext); }, [onPress, preset, onPressContext]);`)
- Line 56: Fix `R-08` violation. Type assertion to tuple 'readonly [string, string, ...string[]]'. The LinearGradient component requires a tuple of at least 2 colors, while the helper returns a standard array 'string[]'. (Suggested: Refactor the color resolution helper ('resolveGradientColors') to return a tuple type '[string, string, ...string[]]' since it guarantees at least 2 colors, removing the need for a cast.)
### [MODIFY] BuilderPanel.tsx
- Line 109: Fix `R-09` violation. PII key 'name' passed with unscrubbed value 'presetNameInput' in AppLogger call. (Suggested: Wrap resource name in scrubPII(presetNameInput) or log the unique ID instead of name.)
### [MODIFY] CameraPanel.tsx
- Line 60: Fix `R-20` violation. Calls 'expo-haptics' API functions (impactAsync, selectionAsync, notificationAsync) unconditionally without check gates for Web or catch handlers. Web does not support standard haptics, leading to unhandled promise rejections or device runtime crashes. (Suggested: Gate the haptic dispatches with Platform.OS !== 'web' and attach a dummy catch handler: if (Platform.OS !== 'web') { Haptics.impactAsync(...).catch(() => {}); })
### [MODIFY] UniversalColorGrid.tsx
- Line 5: Fix `R-29` violation. UniversalColorGrid.tsx imports constants PRESET_COLORS, PRESET_HUE_MAP and helper function hueToHex from UniversalSlidersFooter.tsx as value imports, while UniversalSlidersFooter.tsx imports the UniversalColorGrid component from UniversalColorGrid.tsx. This creates a concrete circular value dependency cycle between the two files. (Suggested: Extract the helper functions (hueToHex, hueToHexUpper) and configuration constants (PRESET_COLORS, PRESET_HUE_MAP) out of UniversalSlidersFooter.tsx and place them in a dedicated shared utility/constants file (e.g., src/utils/ColorPresetUtils.ts). Update both UniversalSlidersFooter.tsx and UniversalColorGrid.tsx to import these from the new file, breaking the value circular dependency.)
### [MODIFY] UniversalHueStripSlider.tsx
- Line 6: Fix `R-29` violation. UniversalHueStripSlider.tsx imports helper functions hueToHex and hueToHexUpper from UniversalSlidersFooter.tsx as value imports, while UniversalSlidersFooter.tsx imports the UniversalHueStripSlider component from UniversalHueStripSlider.tsx. This creates a concrete circular value dependency cycle between the two files. (Suggested: Extract the helper functions (hueToHex, hueToHexUpper) out of UniversalSlidersFooter.tsx and place them in a dedicated shared utility/constants file (e.g., src/utils/ColorPresetUtils.ts). Update both UniversalSlidersFooter.tsx and UniversalHueStripSlider.tsx to import these from the new file, breaking the value circular dependency.)
### [MODIFY] UniversalTacticalSliders.tsx
- Line 9: Fix `R-29` violation. UniversalTacticalSliders.tsx imports type UniversalSlidersFooterProps from components/docked/UniversalSlidersFooter.tsx via 'import type'. UniversalSlidersFooter.tsx imports UniversalTacticalSliders.tsx as a component. This creates a type-level circular dependency cycle. (Suggested: No action is strictly required because this is a type-only import that compiles away at runtime. If decoupling is desired, move UniversalSlidersFooterProps definition to a shared types file.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

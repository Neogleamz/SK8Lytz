# Implementation Plan: sweep-components-docked

## Goal
Fix static audit findings for the `sweep-components-docked` domain cluster.

## Proposed Changes

### [MODIFY] [BuilderPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/BuilderPanel.tsx)
- **Line:** 77
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [FavoritesPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/FavoritesPanel.tsx)
- **Line:** 50
- **Rule:** R-07
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The FlatList renderItem callbacks (renderYoursItem and renderPicksItem) define inline arrow function handlers for onPress and onEdit callbacks, which bypass memoization and cause child components to re-render.
- **Suggested Fix:** Extract the preset rendering logic or avoid inline arrows by having PresetCard take fav, onLoadFavorite, and onEditFavorite directly, and let the child wrap its own callbacks with useCallback using the item's properties.

### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line:** 401
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line:** 412
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line:** 501
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line:** 509
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line:** 520
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DockedDock.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/DockedDock.tsx)
- **Line:** 164
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders non-standard style properties (boxShadow) to React Native standard ViewStyle.
- **Suggested Fix:** Separate web styles or use style extensions/custom wrappers instead of double-casting ViewStyle.

### [MODIFY] [DockedDock.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/DockedDock.tsx)
- **Line:** 191
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders non-standard style properties (boxShadow) to React Native standard ViewStyle.
- **Suggested Fix:** Separate web styles or use style extensions/custom wrappers instead of double-casting ViewStyle.

### [MODIFY] [FavoritesPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/FavoritesPanel.tsx)
- **Line:** 52
- **Rule:** R-28
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** FlatList rendering user and curated style cards uses inline arrow functions for keyExtractor and renderItem. Additionally, it lacks optimization parameters such as initialNumToRender or windowSize, causing unnecessary item re-renders and memory allocations on scroll.
- **Suggested Fix:** Extract keyExtractor and renderItem into useCallback handlers or static helpers, and define initialNumToRender={8} and windowSize={5} configurations.

### [MODIFY] [PresetCard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/PresetCard.tsx)
- **Line:** 54
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders color array into a strict non-empty tuple array to fit visual representation constraint.
- **Suggested Fix:** Enforce tuple structure from upstream hooks or update consumer components to handle arrays safely.

### [MODIFY] [SpectrumAnalyzer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/SpectrumAnalyzer.tsx)
- **Line:** 83
- **Rule:** R-22
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The audio visualizer spectrum loop starts a requestAnimationFrame rendering loop but fails to clear it when the component unmounts. This results in memory leaks and background CPU consumption when switching away from music mode.
- **Suggested Fix:** Invoke cancelAnimationFrame(animationRef.current) in the useEffect cleanup return function.

### [MODIFY] [StreetModeDistributionSlider.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/StreetModeDistributionSlider.tsx)
- **Line:** 44
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The PanResponder initialization uses inline functions for touch events, resulting in PanResponder re-creation on every render. This introduces garbage collection thrashing during active distribution adjustments.
- **Suggested Fix:** Wrap the PanResponder initialization in a useMemo block, or pass callbacks from stable helper references.

### [MODIFY] [StreetPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/StreetPanel.tsx)
- **Line:** 71
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** StreetPanel displays high-frequency speed and telemetry updates. However, it recreates several inline styling objects in render, creating garbage collection overhead and potential UI micro-stutters during high-speed rides.
- **Suggested Fix:** Move all inline styles to a StyleSheet.create declaration outside the component body.

### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Extracted lighting parameters control sliders panel rendering below active mode views, including brightness, speed, and audio sensitivity controls.
- **Suggested Fix:** Separate the hue strip slider, color grids, and tactile range adjustment inputs into distinct modular components.

### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line:** 393
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Error telemetry calls inside sliding completion handlers (lines 393, 401, 412, 501, 509, 520) invoke AppLogger.error('writeToDevice error', e) but fail to provide the mandatory payload_size and ssi properties in the context object as required by R-04.
- **Suggested Fix:** Provide standard telemetry context details, e.g.: AppLogger.error('writeToDevice error', e.message, { payload_size: scaledRgb.length, ssi: 0 });

### [MODIFY] [QuickPresetModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/QuickPresetModal.tsx)
- **Line:** 75
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [QuickPresetModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/QuickPresetModal.tsx)
- **Line:** 89
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

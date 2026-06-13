# Implementation Plan: sweep-src-ui-docked-controller

This is a synthesized sweep plan addressing all rule violations identified in the **UI_DOCKED_CONTROLLER** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### UI_DOCKED_CONTROLLER Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [BuilderPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/BuilderPanel.tsx)
- **Line 38 [MEDIUM]:** Performance / Unmemoized component: BuilderPanel is a React.FC that does not implement React.memo. It will perform full re-renders and re-create all handlers (handleSavePreset, handleConfirmFavorite, openBuilder) when parent state elements trigger changes. (Rule: R-07)
- **Line 77 [HIGH]:** writeToDevice is called synchronously without being awaited or having a catch handler. writeToDevice returns a Promise under the hood, and unhandled rejections will occur if BLE write fails. (Rule: R-11)
- **Line 109 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)

#### [MODIFY] [CameraPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/CameraPanel.tsx)
- **Line 22 [MEDIUM]:** rgbToHexStr utility is defined locally but is functionally identical to rgbToHex in src/utils/ColorUtils.ts. (Rule: R-21)
- **Line 175 [LOW]:** Performance: Declares inline arrow functions in the swatches map list loop, causing recreation on every camera layout frame refresh. (Rule: R-07)

#### [MODIFY] [DockedDock.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/DockedDock.tsx)
- **Line 164 [LOW]:** Type laundering to coerce custom web style property 'boxShadow' to standard React Native 'ViewStyle'. (Rule: R-08)
- **Line 191 [LOW]:** Type laundering to coerce custom web style property 'boxShadow' to standard React Native 'ViewStyle'. (Rule: R-08)

#### [MODIFY] [FavoritesPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/FavoritesPanel.tsx)
- **Line 55 [HIGH]:** FlatList Performance Bottleneck: FlatList renderItems pass inline arrow functions (onPress and onEdit) to the PresetCard child elements. This breaks PresetCard's React.memo optimization by changing properties on every render. (Rule: R-28)
- **Line 89 [LOW]:** Casting 'null' value to 'IFavoriteState' using double cast 'as unknown as' to serve as a FlatList empty placeholder card item. (Rule: R-08)
- **Line 100 [HIGH]:** The curated picks list ('SK8Lytz Picks') and user favorites sections completely ignore their respective fetch/load error states. When useCuratedPicks or useFavorites fails to load from the server, the UI displays no error visualizer or retry action, rendering empty slots or default blank lists instead. (Rule: R-14)
- **Line 106 [LOW]:** Casting 'null' value to 'IFavoriteState' using double cast 'as unknown as' to serve as a FlatList empty placeholder card item. (Rule: R-08)

#### [MODIFY] [MusicPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/MusicPanel.tsx)
- **Line 76 [MEDIUM]:** Performance: The onMatrixSwitch callback depends on volatile slider states (micSensitivity, brightness), causing it to drop and recreate its instance reference on every drag event. (Rule: R-07)
- **Line 93 [LOW]:** Performance: Recreates inline arrow functions in the render block for matrix selector toggles. (Rule: R-07)

#### [MODIFY] [PresetCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/PresetCard.tsx)
- **Line 54 [LOW]:** Casting 'string[]' array colors to tuple 'readonly [string, string, ...string[]]' required by expo-linear-gradient using 'as unknown as'. (Rule: R-08)

#### [MODIFY] [QuickPresetModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/QuickPresetModal.tsx)
- **Line 48 [HIGH]:** AsyncStorage key collision and duplicate usage. The key '@Sk8lytz_QuickPresets' is dynamically resolved using local variables, bypasses centralized storageKeys.ts registration, and is concurrently accessed in both useFavorites.ts and QuickPresetModal.tsx. (Rule: R-24)

#### [MODIFY] [UniversalSlidersFooter.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- **Line 1 [MEDIUM]:** Component Monolithic Size: File is 33.7KB, exceeding the 30KB threshold. It merges color preset grid, sliders, and mode adapters, violating single-responsibility and component size limits. (Rule: R-23)
- **Line 127 [MEDIUM]:** Implements sliding controllers for fine-tuning speed, brightness, and color settings. Its size is 33.7KB, violating R-23. (Rule: R-23)
- **Line 393 [HIGH]:** Promise / IO Safety: writeToDevice is typed as returning void in props, but the underlying implementation returns a Promise. The callbacks inside onSlidingComplete fire this promise without any .catch() or await, resulting in unhandled rejection issues when a BLE write fails. (Rule: R-11)
- **Line 401 [MEDIUM]:** Unawaited writeToDevice call with no catch handler when updating multi-color presets. (Rule: R-11)
- **Line 412 [MEDIUM]:** Unawaited writeToDevice call with no catch handler in Camera Vibe mode color dispatch. (Rule: R-11)
- **Line 435 [LOW]:** Performance Guardrails: Inline arrow functions are declared inside the render tree, creating new function instances on every refresh cycle. (Rule: R-07)
- **Line 501 [MEDIUM]:** Unawaited writeToDevice call in slider change handler with no catch handler. (Rule: R-11)
- **Line 509 [MEDIUM]:** Unawaited writeToDevice call inside multi-color slider update with no catch handler. (Rule: R-11)
- **Line 520 [MEDIUM]:** Unawaited writeToDevice call inside Camera Vibe slider change handler with no catch handler. (Rule: R-11)

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line 1 [MEDIUM]:** Component Monolithic Size: The file size is 68.7KB (1,481 lines), exceeding the 30KB limit. It contains large sub-views like FixedPatternPreviewRow and excessive hook allocations, making it a high-risk collision zone. (Rule: R-23)
- **Line 151 [LOW]:** Type Safety / any cast: Uses 'any' inside generic type annotations and reference definitions, bypassing compiler warnings. (Rule: R-08)
- **Line 157 [LOW]:** AppState change listener is registered but its removal uses the newer React Native subscription pattern (`sub.remove()`) instead of the deprecated `AppState.removeEventListener()`. Removal is executed correctly inside the useEffect cleanup return, presenting zero leak risk on modern React Native versions. (Rule: R-22)
- **Line 192 [HIGH]:** This React component handles the docked bottom control panel for the skates, including BLE connection details, battery stats, active group status, and multiple rendering sub-panels. The file size is 68.7KB, violating R-23. (Rule: R-23)
- **Line 194 [MEDIUM]:** Context Consumer Depth: The component consumes 4 contexts directly (ThemeContext, AppConfigContext, FavoritesContext, BLEContext), leading to unnecessary re-renders on any context slice update. (Rule: R-27)
- **Line 450 [MEDIUM]:** AppLogger.warn for BLE_TRANSPORT context does not contain payload_size and/or ssi parameters (Rule: R-04)
- **Line 469 [HIGH]:** Stale Closures in Imperative Handle: useImperativeHandle exposes functions (applyCloudScene, loadFavorite, setActiveMode, applySpatialSegments) but doesn't include them in its dependency array. If these functions are updated when hooks change, the parent component will hold stale references. (Rule: R-12)
- **Line 482 [MEDIUM]:** AppLogger.warn for BLE_TRANSPORT context does not contain payload_size and/or ssi parameters (Rule: R-04)
- **Line 900 [HIGH]:** Stale Closures in Pattern Changed effect: The useEffect body references several local state variables (activeMode, fixedPatternId, fixedFgColor, selectedColor, fixedBgColor, builderNodes, lastSentPayload) but only lists [currentStatusText, onPatternChanged] as dependencies. If these state variables update without changing status text, stale snapshots will be sent. (Rule: R-12)

#### [MODIFY] [useControllerAnalytics.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerAnalytics.ts)
- **Line 91 [MEDIUM]:** Split-Brain Telemetry / Redundant Logging: useControllerAnalytics dispatches debounced logging events for brightness, speed, and sensitivity updates. However, the exact same telemetry logging dispatches are called instantly inside UniversalSlidersFooter's callbacks, polluting the AppLogger event logs. (Rule: R-21)

#### [MODIFY] [useControllerDispatch.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line 54 [LOW]:** safeWrite passes override (a Record<string, unknown> containing e.g. { micSource: src }) as the third parameter to writeToDevice. However, in useBLE.ts, the third parameter of writeToDevice is typed as opts?: { lowPriority?: boolean, writeType?: 'Response' | 'NoResponse' }. This structural mismatch compiles only because the WriteFn type in useControllerDispatch.ts casts the third parameter as override?: Record<string, unknown>, bypassing strict option typing. (Rule: R-02)
- **Line 67 [LOW]:** Bypasses AppLogger telemetry context and formatting by using raw console.warn (Rule: R-04)
- **Line 86 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 90 [HIGH]:** The hook loops over a collection of connected target devices using 'targets.forEach(device => { ... })' and dispatches writes via 'safeWrite(..., device.id)' sequentially. Because these writes are sent individually, they are queued as separate serial actions in the BleWriteQueue instead of leveraging the concurrent 'Promise.all' mapped dispatch mechanism available at the lower level, which violates concurrent group BLE write requirements and leads to Android GATT 133 or queue delays. (Rule: R-10)
- **Line 98 [HIGH]:** Hardware Limit Bypass: In sendColor (and emergency pattern dispatches), the ledPoints are queried directly from hwSettings without a maximum cap. If the value exceeds 300 (SOULZ maximum support), it will bypass the 300 LED hardware point constraint and cause EEPROM memory overflow lockouts. (Rule: R-01)
- **Line 119 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 190 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 245 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 301 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 355 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 377 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)

#### [MODIFY] [useDashboardController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardController.tsx)
- **Line 245 [HIGH]:** High-Frequency Telemetry Re-render Storms: MemoizedSk8lytzController depends directly on high-frequency telemetry states (gpsSpeed, peakGForce, sessionDurationSec). Any change to these (which happens 1-10 times per second during sessions) completely destroys the memoization, triggering React reconciliation for the entire DockedController tree. (Rule: R-21)

#### [MODIFY] [useDockedControllerState.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDockedControllerState.ts)
- **Line 201 [HIGH]:** Performance / Unstable React Keys: applySpatialSegments generates dynamic IDs using Date.now() for items in the builder nodes array. This causes mapped elements in child lists to completely lose their identity, prompting React to do a full unmount and remount on every update. (Rule: R-07)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

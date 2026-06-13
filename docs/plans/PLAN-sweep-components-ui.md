# Implementation Plan: BATCH:sweep-components-ui

## Proposed Changes

### Domain: components-ui

#### [MODIFY] src/components/CrewMemberDashboard.tsx
- Line 164 (R-08): Explicit 'any' type bypass when mapping the Supabase join response.

#### [MODIFY] src/components/CrewModal.tsx
- Line 104 (R-04): Raw console.warn used instead of the AppLogger service for error telemetry.
- Line 92 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/DockedController.tsx
- Line 1 (R-23): File exceeds 30KB size limit (67.6KB)
- Line 190 (R-27): Component consumes 4+ React Contexts directly
- Line 415 (R-08): Parameter 'scenePayload' is typed as 'any'
- Line 446 (R-04): AppLogger.warn for BLE_TRANSPORT lacks 'payload_size' or 'ssi' context
- Line 448 (R-09): Array of MAC addresses passed to AppLogger. The internal PII scrubber fails to redact strings within arrays, causing a MAC leak.
- Line 401 (R-14): Missing Error state handling for useCuratedPicks hook.
- Line 402 (R-14): Missing isLoading and Error state destructuring/handling from useSharedFavorites.
- Line 215 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 506 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 1 (R-23): File exceeds 30KB limit (67686 bytes) - flag for mandatory component extraction
- Line 190 (R-27): Component DockedController directly consumes 4 React Contexts (useAppConfig, useTheme, useSharedBLE, useSharedFavorites). Every context change triggers a re-render of this monolithic component. Flagged for useMemo wrapping or context consolidation.

#### [MODIFY] src/components/docked/UniversalSlidersFooter.tsx
- Line 1 (R-23): File exceeds 30KB size limit (33.6KB)
- Line 1 (R-23): File exceeds 30KB limit (33695 bytes) - flag for mandatory component extraction

#### [MODIFY] src/components/docked/MusicPanel.tsx
- Line 32 (R-08): Use of 'any[]' in function signature

#### [MODIFY] src/components/CommunityModal.tsx
- Line 66 (R-08): Type laundering using `as unknown as number` in animated style transform.
- Line 147 (R-08): Type laundering using `as unknown as Record<string, any>`.
- Line 109 (R-06): Missing standard e instanceof Error unwrapping in catch block.
- Line 188 (R-07): Inline arrow function in onPress inside FlatList renderItem breaks memoization.
- Line 66 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.
- Line 147 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.
- Line 8 (R-20): Blind cross-platform assumption: Importing `SafeAreaView` from `react-native` instead of `react-native-safe-area-context`. The core `SafeAreaView` only pads for iOS notches and ignores Android entirely.
- Line 89 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

#### [MODIFY] src/components/CustomSlider.tsx
- Line 97 (R-08): Type laundering using `as unknown as` for touchAction styles.
- Line 46 (R-21): Split-Brain duplication of PanResponder drag calculation logic between CustomSlider and TacticalSlider.
- Line 97 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/TacticalSlider.tsx
- Line 118 (R-08): Type laundering using `as unknown as` for touchAction styles.
- Line 118 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/SessionSummaryModal.tsx
- Line 210 (R-08): Type laundering using `as unknown as` for boxShadow styles.
- Line 210 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/DeviceSettingsModal.tsx
- Line 213 (R-16): Hardcoded setTimeout used for BLE probe fallback instead of using the queue manager's timeout facilities.
- Line 213 (R-22): setTimeout initiated without a corresponding clearTimeout on unmount, causing potential memory leaks.

#### [MODIFY] src/components/AccountModal.tsx
- Line 1 (R-23): File exceeds 30KB monolith limit (30866 bytes).
- Line 417 (R-04): Error logged without payload_size or ssi context
- Line 378 (R-11): Missing try/catch on async network/storage operation
- Line 388 (R-11): Missing try/catch on async network/storage operation
- Line 331 (R-15): Direct supabase.auth.signInWithPassword() bypasses AuthContext's signIn method.
- Line 378 (R-15): Direct supabase.auth.signOut() bypasses AuthContext's signOut method.
- Line 388 (R-15): Direct supabase.auth.signOut() bypasses AuthContext's signOut method.
- Line 425 (R-15): Direct supabase.auth.signOut() bypasses AuthContext's signOut method.
- Line 1 (R-23): File exceeds 30KB limit (30866 bytes) - flag for mandatory component extraction

#### [MODIFY] src/components/dashboard/RegisteredFleetSlab.tsx
- Line 19 (R-08): any cast on renderItem prop

#### [MODIFY] src/components/LocationPicker.tsx
- Line 105 (R-08): any cast on selectSuggestion item
- Line 34 (R-14): Missing Loading and Error UI state handling for useRecentSpots hook.
- Line 39 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/ProductVisualizer.tsx
- Line 29 (R-08): any cast used for device parameter in onLongPressDevice callback.
- Line 41 (R-08): any cast used for builderNodes array.
- Line 38 (R-18): Boolean trap: isStreetBraking boolean exists alongside motionState string union, leading to conflicting states.

#### [MODIFY] src/components/NeonHueStrip.tsx
- Line 12 (R-08): any cast used for style prop.
- Line 94 (R-08): as unknown as cast used to launder type for style.
- Line 94 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/VerticalPatternDrum.tsx
- Line 36 (R-22): commitTimeoutRef is set but never cleared on component unmount, causing a potential memory leak.
- Line 73 (R-07): renderItem useCallback dependencies (commitValue, scrollToValue) are recreated every render, breaking memoization. Also inline functions inside renderItem.
- Line 80 (R-07): Inline arrow function in onPress inside FlatList renderItem.
- Line 98 (R-17): Missing cleanup for listener/subscription in useEffect

#### [MODIFY] src/components/VisualizerUnit.tsx
- Line 1 (R-23): File exceeds 30KB limit (30.5KB).

#### [MODIFY] src/components/PositionalGradientBuilder.tsx
- Line 53 (R-11): writeToDevice returns a Promise but is not awaited or caught inside the setTimeout, leading to unhandled promise rejections on BLE failure.

#### [MODIFY] src/components/patterns/UnifiedPatternPicker.tsx
- Line 62 (R-11): writeToDeviceRef.current is a Promise that is called but neither awaited nor caught.

#### [MODIFY] src/components/patterns/GradientLibraryTab.tsx
- Line 45 (R-07): Inline functions and styles in FlatList renderItem and configuration props.
- Line 45 (R-07): Multiple inline arrow functions (onPress) inside FlatList renderItem.

#### [MODIFY] src/components/account/SkaterStatsPanel.tsx
- Line 65 (R-04): Error logged without payload_size or ssi context
- Line 26 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

#### [MODIFY] src/components/docked/BuilderPanel.tsx
- Line 109 (R-04): Error logged without payload_size or ssi context
- Line 54 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/docked/QuickPresetModal.tsx
- Line 128 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/components/shared/BLEErrorBoundary.tsx
- Line 39 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/components/CameraTracker.tsx
- Line 88 (R-04): Error logged without payload_size or ssi context
- Line 150 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/components/crew/CrewJoinScreen.tsx
- Line 70 (R-07): Inline arrow function in onPress inside FlatList renderItem breaks memoization.

#### [MODIFY] src/components/docked/FavoritesPanel.tsx
- Line 55 (R-07): Inline arrow function in onPress inside FlatList renderItem.
- Line 89 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.
- Line 106 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/patterns/PatternPickerTab.tsx
- Line 115 (R-07): Inline style object in FlatList renderItem creates new object reference every render.
- Line 66 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/dashboard/SkateGroupCard.tsx
- Line 65 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/docked/DockedDock.tsx
- Line 164 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.
- Line 191 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/docked/PresetCard.tsx
- Line 54 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\dashboard\DashboardCrewPanel.tsx
- Line 122 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\DeviceSettingsModal.tsx
- Line 213 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\DockedController.tsx
- Line 489 (R-16): Hardcoded delay using setTimeout detected
- Line 493 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\LocationPicker.tsx
- Line 54 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\PositionalGradientBuilder.tsx
- Line 44 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\VerticalPatternDrum.tsx
- Line 27 (R-16): Hardcoded delay using setTimeout detected
- Line 37 (R-16): Hardcoded delay using setTimeout detected
- Line 98 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] src/components/docked/CameraPanel.tsx
- Line 55 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/GroupSettingsModal.tsx
- Line 20 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/SkateSpotBottomSheet.tsx
- Line 21 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 24 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union

#### [MODIFY] src/components/crew/CrewDetailScreen.tsx
- Line 1 (R-23): File exceeds 30KB limit (32185 bytes) - flag for mandatory component extraction

#### [MODIFY] src/components/crew/CrewLandingScreen.tsx
- Line 1 (R-23): File exceeds 30KB limit (36610 bytes) - flag for mandatory component extraction

#### [MODIFY] src/components/permissions/GranularPermissionsList.tsx
- Line 91 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

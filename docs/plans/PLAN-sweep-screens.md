# Implementation Plan: BATCH:sweep-screens

## Proposed Changes

### Domain: screens

#### [MODIFY] src/screens/DashboardScreen.tsx
- Line 539 (R-08): any cast on scene parameter
- Line 628 (R-08): any cast on _snapshot parameter
- Line 630 (R-08): any cast on lastFav variable
- Line 1201 (R-08): any cast on device parameter
- Line 608 (R-16): Hardcoded delay with setTimeout instead of queue-managed delays
- Line 178 (R-18): Scattered isX booleans instead of FSM state unions
- Line 1 (R-23): File exceeds 30KB (51KB), flagged for component extraction
- Line 632 (R-24): AsyncStorage key collision risk for '@Sk8lytz_Favorites'
- Line 78 (R-27): Component consumes multiple contexts directly causing performance risk
- Line 321 (R-04): Error logged without payload_size or ssi context
- Line 828 (R-07): Inline arrow function in onPress inside FlatList renderItem causes component re-creation on every render.
- Line 178 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 180 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 187 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 304 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 473 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 22 (R-20): Blind cross-platform assumption: Importing `SafeAreaView` from `react-native` instead of `react-native-safe-area-context`. The core `SafeAreaView` only pads for iOS notches and ignores Android entirely, causing UI to render under the status bar on Android devices.
- Line 1 (R-23): File exceeds 30KB limit (51383 bytes) - flag for mandatory component extraction
- Line 426 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.
- Line 77 (R-27): Component DashboardScreen directly consumes 4 React Contexts (useAppConfig, useTheme, BLEContext, useSession). Every context change triggers a re-render of this monolithic screen. Flagged for useMemo wrapping or context consolidation.

#### [MODIFY] src/screens/Onboarding/HardwareSetupWizardScreen.tsx
- Line 69 (R-08): any cast on configsOverride parameter
- Line 124 (R-08): any type for timer
- Line 642 (R-16): Hardcoded async delay with setTimeout
- Line 1 (R-23): File exceeds 30KB (41KB), flagged for component extraction
- Line 90 (R-04): Error logged without payload_size or ssi context
- Line 95 (R-04): Error logged without payload_size or ssi context
- Line 681 (R-04): Error logged without payload_size or ssi context
- Line 53 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 55 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 1 (R-23): File exceeds 30KB limit (41306 bytes) - flag for mandatory component extraction

#### [MODIFY] src/screens/Onboarding/PermissionsOnboardingScreen.tsx
- Line 95 (R-08): any cast on insets parameter

#### [MODIFY] src/screens/AuthScreen.tsx
- Line 48 (R-18): Single boolean state which may need FSM refactoring if complexity grows

# Implementation Plan: sweep-src-ui-screens-and-modals

This is a synthesized sweep plan addressing all rule violations identified in the **UI_SCREENS_AND_MODALS** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### UI_SCREENS_AND_MODALS Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [AndroidManifest.xml](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
- **Line 8 [MEDIUM]:** The AndroidManifest.xml declares the BLUETOOTH_SCAN permission with android:usesPermissionFlags='neverForLocation'. However, the app.config.js specifies neverForLocation: false for the react-native-ble-plx plugin. This configuration mismatch causes prebuild and build-time configuration drift. (Rule: R-20)
- **Line 29 [HIGH]:** The Google Maps API key is hardcoded directly inside the AndroidManifest.xml as a string literal. This exposes sensitive credentials in source control. Although app.config.js dynamically references the key via env variables, the static AndroidManifest.xml has it hardcoded. (Rule: R-09)
- **Line 33 [HIGH]:** The app defines foregroundServiceType='location|health|connectedDevice|shortService|dataSync' for ForegroundService in AndroidManifest.xml, but fails to declare the corresponding Android 14 (API 34+) permissions 'android.permission.FOREGROUND_SERVICE_HEALTH' and 'android.permission.FOREGROUND_SERVICE_DATA_SYNC'. This will trigger a SecurityException crash at runtime on devices running Android 14+ when the foreground service is started. (Rule: R-20)

#### [MODIFY] [AndroidManifest.xml](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/AndroidManifest.xml)
- **Line 13 [HIGH]:** The Wear OS app manifest declares the 'android.permission.BODY_SENSORS' permission to read heart rate sensors but fails to declare 'android.permission.BODY_SENSORS_BACKGROUND'. On Wear OS 4+ (Android 13+), when the app goes into ambient mode or background, it will lose access to body sensors and stop collecting heart rate. (Rule: R-20)

#### [MODIFY] [app.config.js](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js)
- **Line 15 [HIGH]:** The app.config.js defines 'NSHealthShareUsageDescription' and 'NSHealthUpdateUsageDescription' to support fitness metrics tracking but does not configure the 'com.apple.developer.healthkit' entitlement for the main iOS app. Without this entitlement, HealthKit initialization will fail with an authorization error on iOS. (Rule: R-20)
- **Line 22 [MEDIUM]:** The app.config.js specifies 'NSLocationWhenInUseUsageDescription' for location tracking but does not specify 'NSLocationAlwaysAndWhenInUseUsageDescription' or 'NSLocationAlwaysUsageDescription'. Since the app supports background session tracking (and defines foreground service location permissions on Android), iOS requires these keys to enable background location tracking. (Rule: R-20)
- **Line 75 [MEDIUM]:** The app.config.js specifies 'bluetoothAlwaysPermission' for react-native-ble-plx but does not specify 'bluetoothPeripheralPermission'. On iOS 12 and below, the 'NSBluetoothPeripheralUsageDescription' key is required. Omitting this key can cause App Store rejections or runtime failure on older iOS devices. (Rule: R-20)

#### [MODIFY] [pre-commit](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../.husky/pre-commit)
- **Line 32 [MEDIUM]:** Windows-specific cmd.exe invocation inside git worktree verification block runs 'cmd.exe /c mklink ...' without checking Platform / OS, causing a shell execution failure (exit code 127) and blocking commits on Unix-based developer environments (macOS/Linux). (Rule: R-20)
- **Line 59 [HIGH]:** Pre-commit hook directly runs raw npx tsc and npx jest commands instead of calling the unified, Windows/cross-platform-safe check runner, violating Prime Directive S7. (Rule: R-08)

#### [MODIFY] [AndroidManifest.xml](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../android/app/src/main/AndroidManifest.xml)
- **Line 29 [MEDIUM]:** The production Google Maps API key is hardcoded directly in the AndroidManifest.xml file instead of being injected via build configuration, environment variables, or Expo Config Plugins, presenting a security credentials exposure risk. (Rule: R-11)

#### [MODIFY] [WearMessageSender.kt](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt)
- **Line 85 [HIGH]:** Outbound health data buffering performs non-atomic read-modify-write operations on SharedPreferences inside scope.launch running on the multi-threaded Dispatchers.IO. Concurrent invocations can lead to race conditions where data is lost. (Rule: R-11)

#### [MODIFY] [WearableCommunicationService.kt](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt)
- **Line 66 [MEDIUM]:** notifyListeners() iterates over stateListeners using forEach within a synchronized block. If a listener attempts to remove itself or modify the subscription list during execution, it will trigger a ConcurrentModificationException. (Rule: R-11)

#### [MODIFY] [Sk8lytzTileService.kt](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/tiles/Sk8lytzTileService.kt)
- **Line 57 [LOW]:** The Tile service spawns a raw, unmanaged Java Thread (Thread { ... }.start()) to perform asynchronous data loading. This thread is not tied to any lifecycle or coroutine scope, which could lead to memory or background resource leaks if the tile request is cancelled. (Rule: R-11)

#### [MODIFY] [app.config.js](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../app.config.js)
- **Line 69 [HIGH]:** The app targets Android SDK 36 (targetSdkVersion) and defines a foreground service of type 'health|dataSync' in AndroidManifest.xml (line 33), but fails to request the required 'android.permission.FOREGROUND_SERVICE_HEALTH' and 'android.permission.FOREGROUND_SERVICE_DATA_SYNC' permissions. This results in a fatal SecurityException crash on Android 14+ runtimes. (Rule: R-25)

#### [MODIFY] [Sk8lytzWatchBridgeModule.swift](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../modules/sk8lytz-watch-bridge/ios/Sk8lytzWatchBridgeModule.swift)
- **Line 77 [MEDIUM]:** iOS implementation of the watch bridge's onWatchHealthUpdate event payload omits status and startTimeMs, whereas the Android module implementation includes these fields. This creates platform drift and inconsistency for the JS API client. (Rule: R-21)

#### [MODIFY] [package-lock.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../package-lock.json)
- **Line 14652 [HIGH]:** react-native-health depends on legacy '@expo/config-plugins' ^7.2.2 (SDK 50 era), creating a duplicate nested resolve of config-plugins (v7.9.2) in node_modules/react-native-health/node_modules/ which conflicts with the project's root Expo SDK 55 config plugins (^55.0.8). (Rule: R-21)

#### [MODIFY] [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../package.json)
- **Line 19 [MEDIUM]:** The package '@config-plugins/react-native-ble-plx' is deprecated by npm because 'react-native-ble-plx' >= 3.1.1 now natively bundles its Expo config plugin. Having both creates redundancy and dependency overhang. (Rule: R-21)
- **Line 33 [MEDIUM]:** Expo packages expo-battery, expo-font, expo-sensors, and expo-splash-screen use caret version ranges (^55.0.x) instead of Expo's standard tilde version ranges (~55.0.x), which risks pulling in incompatible minor updates that bypass SDK guarantees. (Rule: R-21)
- **Line 44 [LOW]:** expo-location version (~55.1.6) is mismatched with the project's base Expo SDK version (55.0.x), representing a minor version drift that could lead to compilation or runtime inconsistency. (Rule: R-21)
- **Line 58 [MEDIUM]:** The 'react-native-nitro-image' package is declared in package.json dependencies but is completely unused throughout the source code, representing a dead/dangling dependency. (Rule: R-21)
- **Line 72 [LOW]:** Babel plugins (@babel/plugin-proposal-nullish-coalescing-operator, @babel/plugin-proposal-optional-chaining, @babel/plugin-transform-template-literals) are specified as devDependencies in package.json but are not configured or loaded in babel.config.js. (Rule: R-21)
- **Line 80-84 [MEDIUM]:** Detox dependencies and Expo config plugin are defined in package.json and app.config.js, but no .detoxrc.js or detox.config.js is present at the project root. This renders the E2E setup non-functional (zombie configuration). (Rule: R-21)
- **Line 90 [MEDIUM]:** jest-circus is defined at version ^30.4.2 while the core jest library is at ^29.7.0. Using a newer major version of the circus runner than jest itself can cause runtime issues or assertion mismatches in tests. (Rule: R-21)

#### [MODIFY] [index.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../supabase/functions/notify-crew-session/index.ts)
- **Line 30 [HIGH]:** Uncaught Database and Auth exceptions in Deno edge function. The network requests to Supabase auth (getUser) and database tables (crew_memberships, push_tokens) are executed using await without try/catch wrapper blocks. A network interruption or database service failure will throw an unhandled exception, causing the Deno execution worker to crash and return an unhandled 500 server error. (Rule: R-11)
- **Line 59 [MEDIUM]:** Inefficient sequential database queries. The function makes two consecutive database calls (one to crew_memberships to select user IDs, and one to push_tokens to select the tokens). These should be combined into a single JOIN query, cutting database roundtrip latency in half. (Rule: R-07)
- **Line 99 [MEDIUM]:** Sequential network requests inside a loop. The edge function iterates through notification batches of 100 and awaits fetch(EXPO_PUSH_URL, ...) sequentially. When notifying large crews, this blocks execution serially, causing high cumulative latency and increasing Deno execution CPU time charges. (Rule: R-07)

#### [MODIFY] [20260414_account_deletion_rpc.sql](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../supabase/migrations/20260414_account_deletion_rpc.sql)
- **Line 4 [HIGH]:** Insecure search path on SECURITY DEFINER function. The function public.delete_account() executes with the elevated privileges of the definer to delete from auth.users, but fails to lock down search_path to public. This exposes the function to search path hijacking/escalation attacks. (Rule: R-08)

#### [MODIFY] [20260426200000_phase_control_panels.sql](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../supabase/migrations/20260426200000_phase_control_panels.sql)
- **Line 79 [HIGH]:** Excessively permissive anonymous RLS policy on scraper_blocklist. The policy allows public (anonymous) users full access to query and modify (insert, update, delete) records in scraper_blocklist, allowing public users to hijack or disable scraping operations. (Rule: R-08)

#### [MODIFY] [20260607100000_fix_telemetry_schema.sql](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../supabase/migrations/20260607100000_fix_telemetry_schema.sql)
- **Line 8 [HIGH]:** Overwriting SECURITY DEFINER function without search path. The public.flush_telemetry(payload JSONB) function was recreated with CREATE OR REPLACE FUNCTION in migration 20260607100000 without specifying SET search_path = public. This overrode the setting from the May 26 security hardening sweep, making it vulnerable to search path hijacking. (Rule: R-08)

#### [MODIFY] [20260609140000_live_debugger_views.sql](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../supabase/migrations/20260609140000_live_debugger_views.sql)
- **Line 22 [HIGH]:** Insecure search path on SECURITY DEFINER function. The resolve_crash_signature RPC function is created with SECURITY DEFINER but fails to lock down search_path to public, enabling search path hijacking attacks. (Rule: R-08)

#### [MODIFY] [20260609175500_restore_domain_admin_promotion.sql](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../supabase/migrations/20260609175500_restore_domain_admin_promotion.sql)
- **Line 8 [HIGH]:** Overwriting handle_auto_promotion trigger function without search path. Recreating public.handle_auto_promotion() trigger function stripped the SET search_path configured by the security sweep, introducing a search path hijacking security risk to the elevated trigger function. (Rule: R-08)
- **Line 14 [HIGH]:** Insecure email domain validation check. The handle_auto_promotion function uses `%@sk8lytz.com` and `%@neogleamz.com` with LIKE. Since `%` matches any prefix, an attacker can register emails under domain suffixes such as `evil@fakesk8lytz.com` or `test@attackerneogleamz.com` and receive automatic admin role assignment. (Rule: R-08)

#### [MODIFY] [expo-target.config.js](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../targets/watch/expo-target.config.js)
- **Line 11 [MEDIUM]:** The required background delivery entitlement com.apple.developer.healthkit.background-delivery specified in the Master Reference (line 1316) is missing from the watch targets configuration. (Rule: R-20)

#### [MODIFY] [HealthManager.swift](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../targets/watch/HealthManager.swift)
- **Line 52 [MEDIUM]:** The HKWorkoutSession activity type is set to .skatingSports, contradicting the Master Reference mandate (line 1315) to use .rollerSkating. (Rule: R-20)

#### [MODIFY] [WatchConnectivityManager.swift](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/../targets/watch/WatchConnectivityManager.swift)
- **Line 57 [LOW]:** WCSession activation errors are printed to stdout via standard print statement rather than routed to an error reporting handler or logger, leading to silent failures in production. (Rule: R-11)
- **Line 105 [HIGH]:** Delegate callback functions from WCSession execute on a background serial queue. Modifying @Published properties (like isSessionActive and currentSpeed) directly inside handlePayload causes SwiftUI runtime errors or thread races. (Rule: R-20)

#### [MODIFY] [AccountModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line 1 [MEDIUM]:** Monolith Component Detected. The file size of AccountModal.tsx is ~35KB, which exceeds the 30KB threshold. It manages multiple distinct sub-tabs (stats, prefs, devices, crews) inline, creating high maintenance and merge collision risks. (Rule: R-23)
- **Line 131 [MEDIUM]:** Renders tabs for user profile editing, hardware stats, security, and sync settings in a modal. File size is 31.4KB, violating R-23. (Rule: R-23)
- **Line 344 [LOW]:** Direct supabase.auth.updateUser() call to update user password bypasses AuthContext abstraction. (Rule: R-15)
- **Line 362 [LOW]:** Direct supabase.auth.updateUser() call to update user email bypasses AuthContext abstraction. (Rule: R-15)
- **Line 382 [HIGH]:** Catch variable 'err' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 396 [HIGH]:** Catch variable 'err' is used without an 'instanceof Error' check. (Rule: R-06)

#### [MODIFY] [CommunityModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CommunityModal.tsx)
- **Line 101 [MEDIUM]:** Type Safety Violation. Prop 'styles' in the SceneCard memoized subcomponent is typed as 'any', bypassing typescript compilation validation. (Rule: R-08)
- **Line 266 [MEDIUM]:** The 'styles' object is reconstructed on every render (line 195) and passed as a dependency to the 'renderItem' callback's useCallback array (line 279). This causes the 'renderItem' callback to be recreated on every single render of CommunityModal, defeating render optimization. Furthermore, because 'styles' changes every render, 'SceneCard' (which is React.memo'ed) will re-render for every item on every parent update. (Rule: R-07)
- **Line 292 [MEDIUM]:** Hardcoded user-facing English string in UI component without any internationalization or localization framework support. (Rule: R-25)
- **Line 339 [LOW]:** FlatList passes an inline arrow function to the ListEmptyComponent prop. This function gets re-created on every render. (Rule: R-07)

#### [MODIFY] [CrewMemberDashboard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewMemberDashboard.tsx)
- **Line 163 [HIGH]:** Async function `loadMembers` inside `useEffect` (also invoked by `setInterval` every 30s) uses a shared reference ref `_isFlushingRef.current` as an in-flight guard, but fails to check if the component is still mounted before resolving and calling `setMembers` state updates. Furthermore, if `session.id` changes, the effect tear-down fires, but because the previous network request is still in-flight, `_isFlushingRef.current` remains `true`. The subsequent run of the hook instantly returns (skipping the fetch for the new session), and when the old request finally resolves, it overrides the list with members from the previous session. (Rule: R-26)
- **Line 177 [HIGH]:** Type laundering of Supabase query result 'data' to 'CrewMemberRow[]' via double cast 'as unknown as'. (Rule: R-08)
- **Line 190 [MEDIUM]:** AppLogger.warn on members load failure inside dynamic import missing payload_size and ssi context (Rule: R-04)

#### [MODIFY] [CustomSlider.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomSlider.tsx)
- **Line 18 [LOW]:** Unused Props Declared. The component destructures 'step' and 'thumbTintColor' from CustomSliderProps but never uses them inside the slider logic. (Rule: R-21)
- **Line 102 [HIGH]:** Custom interactive slider implemented via PanResponder is missing all accessibility props (accessible, accessibilityRole, accessibilityLabel, accessibilityValue), making it completely unusable and invisible for screen readers. (Rule: R-25)

#### [MODIFY] [CrewHubSlab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/CrewHubSlab.tsx)
- **Line 181 [HIGH]:** Instantiating 'new Animated.Value(1)' inside component render cycle leads to memory leaks and animation resets on render. (Rule: R-22)

#### [MODIFY] [DashboardCrewPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardCrewPanel.tsx)
- **Line 122 [MEDIUM]:** A naked 'setTimeout' callback is registered without tracking or clearing on component unmount. (Rule: R-17)

#### [MODIFY] [DashboardGroupList.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardGroupList.tsx)
- **Line 1 [LOW]:** File is empty and contains only a verification anchor comment, serving no active code purpose. (Rule: R-21)

#### [MODIFY] [DashboardTelemetryHero.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardTelemetryHero.tsx)
- **Line 12 [HIGH]:** CircleWrapper forwardRef generic parameters and props use 'any' to bypass strict component typings. (Rule: R-08)

#### [MODIFY] [HardwareStatusPills.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/HardwareStatusPills.tsx)
- **Line 8 [HIGH]:** The 'device' prop is typed as 'any' in HardwareStatusPillsProps, losing shape guarantees. (Rule: R-08)

#### [MODIFY] [MySkatesSlab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/MySkatesSlab.tsx)
- **Line 21 [HIGH]:** allDevices, connectedDevices, and registeredDevices props are typed as 'any[]'. (Rule: R-08)

#### [MODIFY] [SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx)
- **Line 18 [HIGH]:** userProfile prop is typed as 'any', bypassing type checking. (Rule: R-08)
- **Line 65 [LOW]:** Type safety violation with type laundering (as unknown as) to bypass TypeScript compiler warnings for LinearGradient colors array structure. (Rule: R-08)

#### [MODIFY] [DashboardGroupList.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DashboardGroupList.tsx)
- **Line 1 [LOW]:** Duplicate legacy file present outside 'components/dashboard' directory. (Rule: R-21)

#### [MODIFY] [DeviceItem.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx)
- **Line 35 [MEDIUM]:** Interactive device list item TouchableOpacity does not specify an accessibilityRole or accessibilityLabel, which prevents screen readers from understanding that it is a button or announcing its status. (Rule: R-25)

#### [MODIFY] [DeviceSettingsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceSettingsModal.tsx)
- **Line 7 [MEDIUM]:** Bypassing Dynamic Theme Mode. Colors are imported statically from theme/theme and defined statically in StyleSheet.create. When the app shifts from light to dark mode (or vice versa), these modal elements will not dynamically update. (Rule: R-21)

#### [MODIFY] [GroupSettingsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/GroupSettingsModal.tsx)
- **Line 4 [MEDIUM]:** Bypassing Dynamic Theme Mode. Colors are imported statically from theme/theme and defined statically in StyleSheet.create, breaking dynamic theme changes. (Rule: R-21)
- **Line 14 [LOW]:** Type Safety Violation. Prop 'allDevices' is typed as 'any[]' instead of a typed array of device objects. (Rule: R-08)

#### [MODIFY] [LocationPicker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPicker.tsx)
- **Line 89 [MEDIUM]:** Search debounce timeout 'debounceTimer.current' is not cleared on component unmount, risking state updates on unmounted component and memory leaks. (Rule: R-17)
- **Line 125 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 168 [MEDIUM]:** The recent spots horizontal list relies on the useRecentSpots hook, which exposes isLoading and error states. While the component renders static error text on failure, it doesn't handle the isLoading state at all (leaving a blank space during loading) and lacks a proper interactive error retry pattern. (Rule: R-14)

#### [MODIFY] [LocationPickerMap.web.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPickerMap.web.tsx)
- **Line 11 [HIGH]:** Web stub components props type contains open-ended index signature with 'any'. (Rule: R-08)
- **Line 14 [HIGH]:** The Web implementation of LocationPickerMap does not match the exports of the Native version. Specifically, the Native file (LocationPickerMap.tsx) exports LocationPickerMap and LocationMarker as named exports. The Web file (LocationPickerMap.web.tsx) exports LocationPickerMap as a default export, and completely lacks an export for LocationMarker. Since LocationPicker.tsx imports them both as named exports, this mismatch will result in compile or runtime failures on the Web platform. (Rule: R-20)

#### [MODIFY] [MarqueeText.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/MarqueeText.tsx)
- **Line 7 [LOW]:** Type Safety Violation. Prop 'containerStyle' in MarqueeTextProps is typed as 'any'. (Rule: R-08)

#### [MODIFY] [SessionSummaryModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/SessionSummaryModal.tsx)
- **Line 33 [LOW]:** formatDuration helper function is duplicated across src/components/SessionSummaryModal.tsx, src/components/dashboard/DashboardTelemetryHero.tsx, and src/components/dashboard/LiveTelemetryHUD.tsx. All of them implement identical logic to format elapsed seconds to 'mm:ss' string format. (Rule: R-21)
- **Line 51 [MEDIUM]:** estimateCalories helper function is duplicated here and in src/services/SpeedTrackingService.ts. This causes a split-brain condition where changes to the MET calculations or formulas in one file won't reflect in the other. (Rule: R-21)
- **Line 207 [LOW]:** Bypassing Theme Mode with Hardcoded Style Colors. The overlay is defined to use useTheme but overrides dynamic style parameters with hardcoded color values (e.g. background '#111'), ignoring theme context. (Rule: R-21)

#### [MODIFY] [BLEErrorBoundary.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/shared/BLEErrorBoundary.tsx)
- **Line 39 [HIGH]:** AppLogger.error is called to record component crash but misses mandatory 'payload_size' and 'ssi' telemetry attributes. (Rule: R-04)

#### [MODIFY] [storageKeys.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/storageKeys.ts)
- **Line 1 [HIGH]:** Multiple AsyncStorage keys are hardcoded and used directly across the application (e.g., `@Sk8lytz_ThemeMode`, `@Sk8lytz_ControlUITheme`, `@Sk8lytz_hardware_blacklist`, `@sk8_gatt_`, `@Sk8lytz_registered_devices`, etc.) instead of being centralized in `src/constants/storageKeys.ts`. This violates the mandate for `storageKeys.ts` to be the single source of truth for all 20+ AsyncStorage keys. (Rule: R-24)
- **Line 17 [LOW]:** Registry casing drift. The key '@Sk8lytz_app_settings' defined in storageKeys.ts has a casing mismatch with the registered Master Reference key '@sk8lytz_app_settings' (lowercase 's'). (Rule: R-24)

#### [MODIFY] [AppConfigContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AppConfigContext.tsx)
- **Line 25 [HIGH]:** AppSettingsService.fetchAllSettings() is awaited without try-catch protection. If the fetch rejects (e.g., due to database or network unavailability), it will bubble up as an unhandled promise rejection because its callers in useEffect and AppState changes are not caught. (Rule: R-11)
- **Line 45 [LOW]:** AppState change listener is wired within a useEffect hook and cleaned up by calling `sub.remove()` in its cleanup return function. Compliant and safe. (Rule: R-22)

#### [MODIFY] [SessionContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
- **Line 32 [MEDIUM]:** Hardcoded and undocumented AsyncStorage keys (@sk8lytz_session_phase, @sk8lytz_session_pause_time, @sk8lytz_session_active) that are missing from the Master Reference §2 Key Registry. (Rule: R-24)
- **Line 51 [MEDIUM]:** AsyncStorage.getItem is awaited in the mount hook inside load() without try-catch coverage. Any storage corruption/read error will cause an unhandled promise rejection. (Rule: R-11)
- **Line 212 [MEDIUM]:** Hardcoded 10-second delay for the auto-pause trigger check. (Rule: R-16)
- **Line 226-228 [HIGH]:** The recover() function executes AsyncStorage operations (getItem, persistSessionPhase) without any try/catch wrapper or .catch() handler, risking unhandled rejections during startup state recovery. (Rule: R-11)
- **Line 230 [HIGH]:** AsyncStorage.removeItem is awaited in the recover() helper without try-catch protection. (Rule: R-11)
- **Line 236 [HIGH]:** AsyncStorage.getItem is awaited in the recover() helper without try-catch protection. (Rule: R-11)
- **Line 301 [MEDIUM]:** AsyncStorage.removeItem('@sk8lytz_session_active') in endSession() is executed outside a try/catch block, risking an unhandled promise rejection if storage fails during session teardown. (Rule: R-11)
- **Line 341 [MEDIUM]:** Hardcoded 10-second delay before pushing the final 'STOPPED' state to the WatchBridge companion sync. (Rule: R-16)
- **Line 367 [MEDIUM]:** notifee.onForegroundEvent listener re-subscribes frequently because it depends on endSession, pauseSession, and resumeSession, which mutate on almost every telemetry tick due to changing state dependencies. (Rule: R-12)

#### [MODIFY] [ThemeContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/ThemeContext.tsx)
- **Line 6-7 [MEDIUM]:** Key namespace/registry casing mismatch and bypass. ThemeContext.tsx defines CONTROL_THEME_KEY as '@Sk8lytz_ControlUITheme' locally, which does not match the registered Master Reference key name '@sk8lytz_control_theme', and it is missing from storageKeys.ts. (Rule: R-24)

#### [MODIFY] [useWebDemoConsoleBridge.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/dev/useWebDemoConsoleBridge.ts)
- **Line 22-23 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)
- **Line 55-60 [MEDIUM]:** Bypasses AppLogger telemetry context and formatting by using raw console.error (Rule: R-04)

#### [MODIFY] [useDeviceStateLedger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDeviceStateLedger.ts)
- **Line 27 [MEDIUM]:** Undocumented AsyncStorage key prefix (@SK8Lytz_DeviceState_v2_) missing from the Master Reference Key Registry. (Rule: R-24)
- **Line 51 [MEDIUM]:** Type laundering of global runtime context object to a custom interface structure 'GlobalWithLedger' via double cast 'as unknown as'. (Rule: R-08)
- **Line 95 [LOW]:** Missing payload_size and ssi context on storage parsing failure during cache warm warning log. (Rule: R-04)
- **Line 140 [HIGH]:** The key value containing the raw MAC address (e.g. @SK8Lytz_DeviceState_v2_AA:BB:CC:DD:EE:FF) is passed under the property name 'key'. Since 'key' is not in PII_KEY_PATTERNS, the payload logs the raw MAC address in the telemetry uploaded. (Rule: R-09, R-04)
- **Line 199 [HIGH]:** The key value containing the raw MAC address is passed under the property name 'key', escaping scrubbing because the property name 'key' is not in the scrub patterns. (Rule: R-09, R-04)

#### [MODIFY] [useTelemetryLedger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useTelemetryLedger.ts)
- **Line 7 [MEDIUM]:** Undocumented AsyncStorage key (@sk8lytz_telemetry_buffer) missing from the Master Reference Key Registry and defined locally instead of centralizing in storageKeys.ts. (Rule: R-24)
- **Line 146 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 168 [LOW]:** Missing payload_size and ssi context on local storage buffering fallback warning log. (Rule: R-04)
- **Line 173 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

#### [MODIFY] [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- **Line 69 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line 1 [MEDIUM]:** DashboardScreen is a monolithic component exceeding 30KB (~54.2KB). This increases cognitive complexity and increases risk of race conditions during concurrent modifications. (Rule: S-04)
- **Line 81 [HIGH]:** The main application dashboard coordinates BLE discovery, device connection states, battery updates, and tabs. It reaches 54.2KB, violating R-23. (Rule: R-23)
- **Line 82 [MEDIUM]:** DashboardScreen directly consumes 4 React Contexts: AppConfigContext, ThemeContext, BLEContext, and SessionContext, violating rule R-27 context consumer depth. (Rule: R-27)
- **Line 90 [MEDIUM]:** Ternary OS check 'Platform.OS === 'ios' ? 'padding' : undefined' assumes Android or other platform matches default layout behaviour without using Platform.select. (Rule: R-20)
- **Line 183 [MEDIUM]:** Scattered boolean flags (isTestModeActive and isDiagnosticsMode) are used to manage mutually exclusive screen modes. This is vulnerable to invalid or dual-active states where both booleans could be true, leading to split-brain rendering issues. They should be unified into a single state union or FSM. (Rule: R-18)
- **Line 239-240 [MEDIUM]:** Type laundering of React state Dispatch function from Device state type to DisplayDevice state action type using 'as unknown as'. (Rule: R-08)
- **Line 241 [MEDIUM]:** Type laundering of MutableRefObject holding Device array to a MutableRefObject holding DisplayDevice array using 'as unknown as'. (Rule: R-08)
- **Line 328 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 515 [MEDIUM]:** Double-layered type laundering within React state updater callback. Casts state array to 'BLEDeviceMinimal[]' and back to 'Device[]' using 'as unknown as'. (Rule: R-08)
- **Line 596 [HIGH]:** The payload property 'expectedMacs' contains an array of raw MAC addresses. While the key name matches PII_KEY_PATTERNS because it contains 'mac', the AppLogger scrubbing loop (obfuscate) does not scrub primitive values within arrays (it only recurses if items are objects). Hence, the array of raw MAC addresses is logged completely raw. (Rule: R-09)
- **Line 648 [HIGH]:** Direct access to AsyncStorage using hardcoded key literal '@Sk8lytz_Favorites' instead of using centralized constants, leading to potential collision with dynamic resolution inside useFavorites.ts. (Rule: R-24)
- **Line 656 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 755 [MEDIUM]:** In handlePowerToggle, power control commands are sent to multiple devices in a loop ('for (const mac of deviceIds)') by sequentially invoking 'dispatch.setPower(targetState, mac)'. This causes multiple separate write requests to enter the BLE queue sequentially instead of being batched together into a single concurrent group write via dispatch.setPower(targetState) or a multi-device dispatch call. (Rule: R-10)
- **Line 809-810 [HIGH]:** Type laundering of setAllDevices dispatch from React.Dispatch<React.SetStateAction<Device[]>> to React.Dispatch<React.SetStateAction<DisplayDevice[]>> using 'as unknown as'. (Rule: R-08)
- **Line 811 [MEDIUM]:** Type laundering of Ref object from Device[] to DisplayDevice[] type using 'as unknown as'. (Rule: R-08)
- **Line 948 [MEDIUM]:** Type laundering of Device[] array to DisplayDevice[] type when passing scanned list to handleRegistrationComplete. (Rule: R-08)
- **Line 1124 [MEDIUM]:** Type laundering of merged device settings configuration object to 'DeviceSettings' type using 'as unknown as'. (Rule: R-08)

#### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- **Line 1 [MEDIUM]:** HardwareSetupWizardScreen is a monolithic setup wizard exceeding 30KB (~42.3KB). It contains inline sub-rendering for multiple setup stages. (Rule: S-04)
- **Line 55 [HIGH]:** A multi-step setup flow wizard (intro, scanning/pairing, and device configuration) implemented as a single screen monolith of 42.4KB, violating R-23. (Rule: R-23)
- **Line 106-111 [HIGH]:** Catch variable 'err' is used without an 'instanceof Error' check. (Rule: R-06)

#### [MODIFY] [BlePingService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line 47 [MEDIUM]:** Type laundering of structural alias type BleManagerPingLike to concrete react-native-ble-plx BleManager type using 'as unknown as'. (Rule: R-08)

#### [MODIFY] [BleWriteDispatcher.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line 77 [HIGH]:** Inside the async debounced setTimeout callback, `_executeWriteToDeviceInternal` is awaited but not wrapped in try-catch. Rejections in this timer callback will propagate to the global thread as unhandled promise rejections. (Rule: R-11)
- **Line 122 [MEDIUM]:** Write failure alerts log raw MAC address targetDeviceId/device.id directly to AppLogger warnings during write execution or chunked failures. (Rule: R-09)
- **Line 148 [MEDIUM]:** The raw MAC address (contained in device.id) is logged directly within the warning log string, escaping scrubbing. (Rule: R-09)
- **Line 164 [HIGH]:** Promise.all is used to dispatch concurrent characteristic writes across multiple BLE devices in parallel. Even with a simulated index-based delay, this creates concurrent promises in the Javascript event loop, risking GATT 133 collisions on Android. (Rule: R-13)
- **Line 176 [MEDIUM]:** The raw MAC address (contained in device.id) is logged directly within the warning log string, escaping scrubbing. (Rule: R-09)
- **Line 228 [HIGH]:** Promise.all is used to dispatch chunked writes in parallel across multiple BLE devices. This violates the serial write requirement and risks Android GATT 133 collisions during chunk transmission. (Rule: R-13)
- **Line 238 [MEDIUM]:** The raw MAC address (contained in device.id) is logged directly within the warning log string, escaping scrubbing. (Rule: R-09)
- **Line 278 [HIGH]:** Inside the async debounced setTimeout callback, `_executeProtocolResultsInternal` is awaited but not wrapped in try-catch, causing unhandled promise rejections on write failures. (Rule: R-11)
- **Line 333 [LOW]:** Hardcoded magic number delay of 50ms is used inside write dispatch loop instead of a constant or queue-managed delay. (Rule: R-16)
- **Line 356 [MEDIUM]:** The raw MAC address (contained in targetDeviceId) is logged directly within the warning log string, escaping scrubbing. (Rule: R-09)

#### [MODIFY] [BleWriteQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
- **Line 214 [MEDIUM]:** Catch variable 'retryErr' is used without an 'instanceof Error' check. It uses String(retryErr) but does not unwrap instanceof Error. (Rule: R-06)

#### [MODIFY] [PermissionService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line 37 [MEDIUM]:** Centralized key registry bypass. The key '@sk8lytz_permissions_optout' is defined locally in PermissionService.ts as OPTOUT_LEDGER_KEY instead of being defined in and imported from storageKeys.ts. (Rule: R-24)
- **Line 50 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 132 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 174 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 184 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)

#### [MODIFY] [SessionMachine.test.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/__tests__/SessionMachine.test.ts)
- **Line 136 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

#### [MODIFY] [NotificationService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
- **Line 106 [HIGH]:** Catch variable 'err' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 127 [LOW]:** Foreground service notification updates are pushed at a 5-second interval. The XState service properly returns a callback clearing this interval and stopping foreground services upon state exits. (Rule: R-22)
- **Line 150 [LOW]:** An asynchronous IIFE is invoked within the callback actor cleanup function without a trailing .catch() block. Even though the body is protected by a try-catch, any error that is not caught or thrown during async setup will create a floating promise. (Rule: R-11)

#### [MODIFY] [SessionMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SessionMachine.ts)
- **Line 27 [HIGH]:** Catch variable 'err' is used without an 'instanceof Error' check. (Rule: R-06)

#### [MODIFY] [DashboardStyles.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/styles/DashboardStyles.ts)
- **Line 9 [LOW]:** The fallbacks in `getPatternColors` (`#00F0FF` and `#7000FF`) are hardcoded and do not align with the brand colors defined in `src/theme/theme.ts` (where `primary` is `#FF5A00` and `secondary` is `#FFB800`). This results in visual and logical styling drift if the theme context colors are not successfully passed in. (Rule: R-21)

#### [MODIFY] [theme.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/theme/theme.ts)
- **Line 70-71 [LOW]:** Platform shadow values fall back to an empty object for default platforms (including Web) in the Shadows object definition. Although React Native Web maps standard iOS-style shadow properties directly to CSS box-shadow, using default: {} prevents Web from displaying any shadow styling, resulting in design/UI variance on Web builds. (Rule: R-20)
- **Line 90 [MEDIUM]:** The TextShadows.glow helper uses `as TextStyle` to force compile `{ textShadow: string }` (a web-only property not present in standard React Native's TextStyle type) for the web case. This is a type-safety bypass (laundering) that circumvents TypeScript compiler checks. (Rule: R-08)

#### [MODIFY] [Info.plist](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/Info.plist)
- **Line 4 [LOW]:** The targets/watch/Info.plist is an empty XML dictionary stub. While expo-target.config.js configures plist keys dynamically, having an empty placeholder plist in the codebase could cause Xcode build issues if prebuild steps are bypassed. (Rule: R-20)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

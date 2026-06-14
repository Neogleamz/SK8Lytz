# Implementation Plan: sweep-root

## Goal
Fix static audit findings for the `sweep-root` domain cluster.

## Proposed Changes

### [MODIFY] [build.gradle](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/build.gradle)
- **Line:** 91
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The Android manifest contains placeholder reference '${GOOGLE_MAPS_API_KEY}', but the app's build.gradle does not define 'manifestPlaceholders' for 'GOOGLE_MAPS_API_KEY'. Running a direct native Android build (e.g. ./gradlew assembleRelease or via Android Studio) without EAS prebuild hooks will fail compilation with a manifest merger error.
- **Suggested Fix:** Configure manifestPlaceholders in the defaultConfig block of android/app/build.gradle: 'manifestPlaceholders = [GOOGLE_MAPS_API_KEY: project.findProperty("GOOGLE_MAPS_API_KEY") ?: System.getenv("GOOGLE_MAPS_API_KEY") ?: ""]'.

### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
- **Line:** 1
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Android Health Connect permissions are missing. While Health Connect record permissions are requested at runtime in PermissionService.ts, the manifest does not declare any <uses-permission> tags for Health Connect types (e.g. android.permission.health.READ_HEART_RATE, android.permission.health.READ_STEPS, etc.). As a result, permission requests will fail immediately on Android devices, blocking health telemetry features.
- **Suggested Fix:** Declare the appropriate Health Connect permissions in AndroidManifest.xml and add them to the android.permissions array in app.config.js.

### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
- **Line:** 8
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Prebuild divergence and configuration mismatch. Multiple permissions required in AndroidManifest.xml (INTERNET, VIBRATE, and legacy READ_EXTERNAL_STORAGE/WRITE_EXTERNAL_STORAGE) are missing from the android.permissions list in app.config.js. Additionally, BLUETOOTH_SCAN is declared with android:usesPermissionFlags="neverForLocation" in AndroidManifest.xml, but neverForLocation is set to false under react-native-ble-plx plugin configuration in app.config.js. Running expo prebuild will wipe out the manual changes in AndroidManifest.xml, causing a mismatch in production build permissions.
- **Suggested Fix:** Sync permissions in app.config.js and change neverForLocation to true in app.config.js to match the manifest, or align them according to whether location permission is required.

### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
- **Line:** 17
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** SYSTEM_ALERT_WINDOW is requested in the main/release AndroidManifest.xml, but is already requested in the debug/debugOptimized manifests. This results in the permission being included in production builds where it is not used by any code, presenting an unnecessary security risk and potential rejection from the Google Play Store.
- **Suggested Fix:** Remove line 17 from android/app/src/main/AndroidManifest.xml so that the permission is only requested in the debug/debugOptimized environments where the developer tools require it.

### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
- **Line:** 33
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Declaring 'shortService' combined with long-running foreground service types ('location|connectedDevice') for Notifee's ForegroundService in the Android manifest. Android 14+ (API 34) strictly prohibits combining shortService with other types. This triggers a SecurityException at runtime or aborts the background service after 3 minutes.
- **Suggested Fix:** Remove the incompatible 'shortService' type from the android:foregroundServiceType attribute, retaining only 'location|connectedDevice' as needed.

### [MODIFY] [WearMessageSender.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt)
- **Line:** 96
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Using synchronized(this) inside a launched coroutine block in WearMessageSender.kt. Because the coroutine builder (scope.launch) creates a new CoroutineScope receiver instance for each execution context, 'this' refers to different instances. Consequently, the synchronized block fails to serialize concurrent SharedPreferences read-modify-write telemetry operations, leading to race conditions and potential data loss.
- **Suggested Fix:** Change the synchronization target from 'this' to a dedicated, stable private lock object defined in the companion or package scope, e.g., 'private val lock = Any()', and synchronize on that object.

### [MODIFY] [OngoingActivityManager.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/OngoingActivityManager.kt)
- **Line:** 18
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The Wear OS workout tracking notification is displayed as ongoing via NotificationManager.notify(), but is not associated with an active Foreground Service (Service.startForeground()). Since WearableCommunicationService is a transient WearableListenerService managed by Google Play Services, the Wear OS application lacks a persistent foreground lifecycle binder. The OS will suspend or kill the app process in the background, terminating the active Health Services ExerciseClient tracking.
- **Suggested Fix:** Refactor OngoingActivityManager to start and bind to a persistent foreground Service (e.g. promoting the listener service or starting a dedicated workout tracking Service) and call startForeground(ONGOING_NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_HEALTH).

### [MODIFY] [app.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js)
- **Line:** 15
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Background location tracking is configured on iOS with usage descriptions (NSLocationAlwaysAndWhenInUseUsageDescription, etc.) and on Android via FOREGROUND_SERVICE_LOCATION. However, the required UIBackgroundModes array with the location string is missing from the ios configuration in app.config.js. This will cause the iOS app to be terminated or location tracking to fail as soon as the app goes to the background.
- **Suggested Fix:** Add "UIBackgroundModes": ["location", "bluetooth-central"] to the ios.infoPlist configuration in app.config.js to properly register background modes.

### [MODIFY] [App.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/App.tsx)
- **Line:** 37
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Lack of safety boundary / try-catch in the global error handler. If AppLogger.log or AppLogger.uploadLogsToSupabase throws an error, it will create an unhandled promise rejection inside the global exception handler, causing the app to crash catastrophically or enter an infinite loop.
- **Suggested Fix:** Wrap the asynchronous logging and upload operations inside a try-catch block, so errors within the handler do not crash the React Native JS runtime.

### [MODIFY] [eslint.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/eslint.config.js)
- **Line:** 72
- **Rule:** R-08
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The ESLint configuration disables the @typescript-eslint/no-explicit-any rule by setting it to 'off'. This allows explicit any types to pass lint checks without warnings, undermining the project's strict 'No any Cast Law' (Rule R-08).
- **Suggested Fix:** Change the rule configuration to 'error' or 'warn' to enforce explicit any bans during linting.

### [MODIFY] [package.json](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json)
- **Line:** 19
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The package `@config-plugins/react-native-ble-plx` (v7.0.0) is declared as a dependency in `package.json`. However, it is fully deprecated because `react-native-ble-plx` version `3.1.1` and above (our project uses `^3.5.1`) natively ships with its own Expo configuration plugin. Indeed, `app.config.js` configures the built-in 'react-native-ble-plx' plugin directly at line 77, and does not reference `@config-plugins/react-native-ble-plx`. Keeping this deprecated dependency is redundant, conflicts with the built-in plugin, and introduces peer dependency warnings since `@config-plugins/react-native-ble-plx` requires Expo SDK 49 whereas the project uses Expo SDK 55. This is bypassed by setting `legacy-peer-deps=true` in `.npmrc`, which hides but does not fix the version conflict.
- **Suggested Fix:** Remove the `@config-plugins/react-native-ble-plx` dependency from `package.json` and run `npm install` to update the lockfile.

### [MODIFY] [20260418105200_daemon_status_anon_rls.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260418105200_daemon_status_anon_rls.sql)
- **Line:** 1
- **Rule:** R-15
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Table 'daemon_status' has overly permissive RLS policies 'Allow anon to update daemon status' and 'Allow anon to insert daemon status' allowing write (UPDATE/INSERT) operations to anonymous public users.
- **Suggested Fix:** Restrict the policies to the 'authenticated' role or remove anonymous write access completely, executing status writes using the 'service_role' key.

### [MODIFY] [20260419100000_scraper_evasion_config.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260419100000_scraper_evasion_config.sql)
- **Line:** 14
- **Rule:** R-15
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Table 'scraper_config' has overly permissive RLS policy 'Allow anon all on scraper_config' allowing write (ALL) operations to anonymous users, leaving scraper parameters open to public manipulation.
- **Suggested Fix:** Change access from FOR ALL to FOR SELECT and restrict updates to the 'authenticated' role with admin verification.

### [MODIFY] [20260609050000_get_all_devices_rpc.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260609050000_get_all_devices_rpc.sql)
- **Line:** 2
- **Rule:** R-15
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** SECURITY DEFINER function 'get_all_registered_devices()' lacks role verification inside the body, allowing any anonymous or regular caller to bypass RLS and fetch all registered devices (and MAC addresses) in the system.
- **Suggested Fix:** Add an authorization check verifying that public.user_profiles.role for auth.uid() is 'admin' before returning query.

### [MODIFY] [20260609175500_restore_domain_admin_promotion.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260609175500_restore_domain_admin_promotion.sql)
- **Line:** 15
- **Rule:** R-06
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Logic bug: RIGHT(NEW.email, 15) extracts 15 characters, but the literal '@neogleamz.com' is only 14 characters. This check will always evaluate to false, preventing auto-promotion of @neogleamz.com users.
- **Suggested Fix:** Change RIGHT(NEW.email, 15) to RIGHT(NEW.email, 14)

### [MODIFY] [WatchConnectivityManager.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift)
- **Line:** 105
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Mutating SwiftUI @Published properties (specifically sessionStartTime, isSessionActive, isPaused, currentSpeed) on WCSession background threads instead of dispatching to the main thread. This causes SwiftUI runtime warnings, thread sanitization failures, and potential application crashes.
- **Suggested Fix:** Wrap the mutations in a DispatchQueue.main.async block or annotate the class/methods with @MainActor to ensure all published property changes occur on the main thread.

### [MODIFY] [ast_r14_scanner.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ast_r14_scanner.js)
- **Line:** 97
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Output directory 'outDir' for the R-14 scanner findings is hardcoded to a specific stale conversation ID, which will fail or write to the wrong folder on other workspaces or sessions.
- **Suggested Fix:** Dynamically resolve the brain workspace directory or write the findings file relative to the project root directory.

### [MODIFY] [cartographer-injector.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/cartographer-injector.js)
- **Line:** 4
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Path to the Master Reference is hardcoded as 'tools/SK8Lytz_App_Master_Reference.md' instead of the correct location 'docs/SK8Lytz_App_Master_Reference.md', resulting in a file-not-found error when the script is run.
- **Suggested Fix:** Change the path from 'tools/SK8Lytz_App_Master_Reference.md' to 'docs/SK8Lytz_App_Master_Reference.md'.

### [MODIFY] [dump_rejections.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scraper/dump_rejections.ts)
- **Line:** 22
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Write path for 'rejected_spots.md' is hardcoded to a specific stale conversation ID folder, which will cause output writes to fail or end up in a different session log.
- **Suggested Fix:** Use relative pathing or write directly to the active artifacts folder.

### [MODIFY] [sentinel-rules-guard.ps1](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/sentinel-rules-guard.ps1)
- **Line:** 80
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Path to the active Bucket List file is hardcoded as 'tools\SK8Lytz_Bucket_List.md' instead of the actual path 'docs\SK8Lytz_Bucket_List.md', causing the script to warn that the file is not found and fail to back up or restore the task board.
- **Suggested Fix:** Change the path portion from 'tools\SK8Lytz_Bucket_List.md' to 'docs\SK8Lytz_Bucket_List.md'.

### [MODIFY] [friction_analyzer.py](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/sentinel/friction_analyzer.py)
- **Line:** 10
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Hardcoded brain directory defaults to a stale path 'C:\\Users\\Magma\\.gemini\\antigravity-ide\\brain' containing the '-ide' suffix. The actual path is '.gemini\antigravity\brain', causing the script to return None and miss all session logs.
- **Suggested Fix:** Change 'antigravity-ide' to 'antigravity' in the brain_dir default argument.

### [MODIFY] [update_docs.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/update_docs.js)
- **Line:** 2
- **Rule:** R-21
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Stale reference path for the Master Reference 'tools/SK8Lytz_App_Master_Reference.md' which does not exist in tools/. Running this script results in a file-read exception.
- **Suggested Fix:** Update path to point to 'docs/SK8Lytz_App_Master_Reference.md'.

### [MODIFY] [pre-commit](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.husky/pre-commit)
- **Line:** 32
- **Rule:** R-20
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The worktree node_modules junction setup block assumes a Windows environment by executing cmd.exe and mklink /j when inside a worktree. On non-Windows platforms (macOS/Linux), this fails with 'command not found' and aborts the commit because set -e is enabled.
- **Suggested Fix:** Wrap the directory junction logic in a platform check checking if OSTYPE matches msys/cygwin, and fallback to creating a standard symbolic link via 'ln -s' on macOS/Linux.

### [MODIFY] [app.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js)
- **Line:** 35
- **Rule:** R-20
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Mismatch/divergence between the Android permissions declared in app.config.js and AndroidManifest.xml. Permissions like INTERNET, VIBRATE, SYSTEM_ALERT_WINDOW, READ_EXTERNAL_STORAGE, and WRITE_EXTERNAL_STORAGE are in the manifest but missing from app.config.js. Additionally, neverForLocation is configured as false in app.config.js, but the AndroidManifest.xml uses neverForLocation on the BLUETOOTH_SCAN permission.
- **Suggested Fix:** Update app.config.js to include all permissions listed in AndroidManifest.xml and set neverForLocation: true under react-native-ble-plx plugin to match usesPermissionFlags='neverForLocation' in the manifest.

### [MODIFY] [App.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/App.tsx)
- **Line:** 135
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Floating promises on async AppLogger.uploadLogsToSupabase calls inside useEffect and AppState event listeners. If network operations fail, they are not caught, leading to unhandled promise rejections.
- **Suggested Fix:** Catch potential errors or execute within an async IIFE containing proper error handling, e.g. AppLogger.uploadLogsToSupabase().catch((e) => AppLogger.warn('Sync failed', e));

### [MODIFY] [package.json](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json)
- **Line:** 90
- **Rule:** R-21
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The project specifies `"jest-circus": "^30.4.2"` as a devDependency in `package.json`. However, `"jest": "^29.7.0"` is used. Because `jest-circus` is the internal test execution engine of Jest, having a major version mismatch (Jest 29 vs Jest-Circus 30) can lead to subtle bugs, unexpected behavior, and execution failures during test runs. The `jest-circus` dependency should be version-aligned with the active `jest` package (`^29.7.0`), or removed entirely if `jest` already embeds the matching runner version.
- **Suggested Fix:** Update `jest-circus` in `package.json` to `^29.7.0` (or match the `jest` package version exactly) and run `npm install` to update the lockfile.

### [MODIFY] [index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/functions/notify-crew-session/index.ts)
- **Line:** 103
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Silent swallowing of fetch promise rejections: The Expo notification dispatch uses Promise.allSettled(fetchPromises) but does not inspect the outcomes array, causing any network/server failures to fail silently.
- **Suggested Fix:** Loop through the result array and log/raise errors for any items where result.status === 'rejected'.

### [MODIFY] [20260609140000_live_debugger_views.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260609140000_live_debugger_views.sql)
- **Line:** 22
- **Rule:** R-15
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Function 'resolve_crash_signature' is SECURITY DEFINER and execute permissions are granted to 'authenticated' users, but the function body lacks admin/moderator role check, allowing any user to resolve crashes.
- **Suggested Fix:** Add a check verifying that public.user_profiles.role for auth.uid() is 'admin' or 'moderator' before performing the update.

### [MODIFY] [expo-target.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/expo-target.config.js)
- **Line:** 15
- **Rule:** R-20
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Entitlements for HealthKit access are empty. In targets/watch/expo-target.config.js, the entitlement "com.apple.developer.healthkit.access" is configured as [] (empty array), but the watch extension's HealthManager.swift requests read access to heart rate and calorie metrics. This mismatch will prevent the watch companion from successfully retrieving these metrics.
- **Suggested Fix:** Populate the "com.apple.developer.healthkit.access" array with the required types (e.g., ["HKQuantityTypeIdentifierHeartRate", "HKQuantityTypeIdentifierActiveEnergyBurned"]) in targets/watch/expo-target.config.js.

### [MODIFY] [powershell.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/discord-bridge/powershell.js)
- **Line:** 25
- **Rule:** R-20
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Executes Windows-specific powershell commands directly without verifying that the operating system platform is Windows, causing command execution failure on unix/macOS systems.
- **Suggested Fix:** Wrap shell execution in a check for process.platform === 'win32' and log a warning or execute alternative scripts for other platforms.

### [MODIFY] [App.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/App.tsx)
- **Line:** 38
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Weak type assertion 'error as Error | undefined' instead of standard 'error instanceof Error' check. If the thrown error is a primitive or custom object, message and stack fields could be accessed unsafely or fail silently.
- **Suggested Fix:** Perform a standard check using instanceof Error, or dynamically inspect properties of the error object safely.

### [MODIFY] [index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/functions/notify-crew-session/index.ts)
- **Line:** 44
- **Rule:** R-08
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Lack of payload structural validation: Edge function extracts fields from req.json() directly without input validation (e.g. Zod), which can cause runtime errors or unhandled SQL exceptions downstream.
- **Suggested Fix:** Integrate a structural validation schema or perform manual schema/existence checks before parsing.

### [MODIFY] [20260418061000_admin_user_management.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260418061000_admin_user_management.sql)
- **Line:** 34
- **Rule:** R-15
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Overly broad target role for RLS policy 'Admins can insert audit logs': The policy lacks 'TO authenticated' limit, which unnecessarily exposes the trigger engine to anonymous insert attempts.
- **Suggested Fix:** Add 'TO authenticated' to the CREATE POLICY statement.

### [MODIFY] [20260424171000_create_app_settings.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260424171000_create_app_settings.sql)
- **Line:** 25
- **Rule:** R-15
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Overly broad target role for RLS policy 'Admins can update app settings': Scopes the policy to public instead of explicitly 'authenticated' users.
- **Suggested Fix:** Add 'TO authenticated' to the policy statement.

### [MODIFY] [20260506000000_admin_tools_expansion.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260506000000_admin_tools_expansion.sql)
- **Line:** 15
- **Rule:** R-15
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Overly broad target role for administrative policies: Policies 'Admins can manage hardware blacklist' and 'Admins can manage feature flags' default to public/anon scope.
- **Suggested Fix:** Add 'TO authenticated' to both policies to enforce authentication-level gating in database engines.

### [MODIFY] [MapWidget.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/command-center/src/components/widgets/MapWidget.tsx)
- **Line:** 222
- **Rule:** R-08
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Type safety bypass via an explicit 'as any' cast on the return value of supabase query, which disables compilation safety checks.
- **Suggested Fix:** Import or declare correct types/interfaces for skate sessions and remove the 'as any' cast.

### [MODIFY] [index.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/discord-bridge/index.js)
- **Line:** 62
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Accesses the message property of the caught error directly in the catch block without checking if the caught error is an instance of Error or an object.
- **Suggested Fix:** Check 'err instanceof Error' or safely cast/stringify the error payload.

### [MODIFY] [scratch_fetch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/scratch_fetch.ts)
- **Line:** 15
- **Rule:** R-11
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Asynchronous network query to Supabase via await is not wrapped inside a try/catch block or handled, causing unhandled promise rejections if there is a network or database failure.
- **Suggested Fix:** Wrap asynchronous database calls in try/catch blocks to ensure clean exits on failure.

### [MODIFY] [tsconfig.json](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tsconfig.json)
- **Line:** 13
- **Rule:** R-07
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** The tsconfig.json file does not exclude temporary build and cache directories like .expo, .local-builder, or web-build from TypeScript compilation, leading to potential slow compilation times and IDE type-checking lag.
- **Suggested Fix:** Add ".expo", ".local-builder", and "web-build" to the exclude array in tsconfig.json.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

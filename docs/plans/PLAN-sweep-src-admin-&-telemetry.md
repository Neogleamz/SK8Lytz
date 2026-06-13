# Implementation Plan: sweep-src-admin-&-telemetry

This is a synthesized sweep plan addressing all rule violations identified in the **ADMIN_&_TELEMETRY** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### ADMIN_&_TELEMETRY Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [AdminToolsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
- **Line 145 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 207 [MEDIUM]:** FlatList utilizes an inline arrow function for the keyExtractor prop and an inline JSX element with inline styles for ListEmptyComponent. (Rule: R-07, R-28)

#### [MODIFY] [AdminAuditLogViewer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminAuditLogViewer.tsx)
- **Line 100 [HIGH]:** Catch variable 'e' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 178 [MEDIUM]:** Inline arrow function passed to keyExtractor prop in FlatList. (Rule: R-28)

#### [MODIFY] [AdminPicksScheduler.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminPicksScheduler.tsx)
- **Line 278 [LOW]:** Iterates and renders picks items inside a ScrollView using picks.map() instead of utilising a FlatList component for dynamic lists. (Rule: R-28)
- **Line 486 [MEDIUM]:** The iosDatePickerOverlay style definition specifies absolute positioning and border parameters but lacks a background color. On iOS, the DateTimePicker overlay will render transparently, rendering overlapped text unreadable. (Rule: R-20)

#### [MODIFY] [AdminRosterPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminRosterPanel.tsx)
- **Line 172 [MEDIUM]:** The FlatList rendering the administrator roster does not provide a ListEmptyComponent for the empty status. Furthermore, when the fetch fails, the status is set to 'error' (and an alert is shown once), but the screen falls back to showing an empty list instead of a persistent error card with a retry option. (Rule: R-14)
- **Line 178 [MEDIUM]:** Inline arrow function passed to keyExtractor prop in FlatList. (Rule: R-28)

#### [MODIFY] [FeatureFlagsPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/FeatureFlagsPanel.tsx)
- **Line 267 [MEDIUM]:** The FeatureFlagsPanel does not persistently render an error state in the UI when fetchFlags fails. The component raises a one-time Alert dialog and falls back to rendering an empty list or cached data, hiding the database connection failure. (Rule: R-14)
- **Line 273 [MEDIUM]:** Inline arrow function passed to keyExtractor prop in FlatList. (Rule: R-28)

#### [MODIFY] [GlobalAnalyticsPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/GlobalAnalyticsPanel.tsx)
- **Line 34 [HIGH]:** Catch variable 'e' is used without an 'instanceof Error' check. (Rule: R-06)

#### [MODIFY] [HardwareBlacklistPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/HardwareBlacklistPanel.tsx)
- **Line 249 [MEDIUM]:** The FlatList rendering blacklisted devices does not handle a persistent error state in the UI. If fetchBlacklist fails, it triggers an alert and falls back to rendering the empty state 'No devices currently blacklisted.', obscuring the actual failure. (Rule: R-14)
- **Line 255 [MEDIUM]:** Inline keyExtractor function triggers recreation on every render, bypassing FlatList optimizations. (Rule: R-28)

#### [MODIFY] [Sk8LytzProgrammer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line 78 [MEDIUM]:** Telemetry error logged via AppLogger.error without providing the required payload_size and ssi context parameter keys. (Rule: R-04)
- **Line 160 [HIGH]:** Catch variable 'e' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 176 [HIGH]:** Catch variable 'e' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 201 [LOW]:** Hardcoded raw setTimeout delays (using delay wrapper) used to stagger connection setup (FLASH_SETTLE_MS), write landing (FLASH_WRITE_LAND_MS), and disconnect gap (FLASH_DISCONNECT_GAP_MS) during batch programming. (Rule: R-16)

#### [MODIFY] [DiagnosticLabBuilderTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx)
- **Line 24 [HIGH]:** Implements manual command builders and mathematical calculators for testing protocol opcodes. The file size is 42.2KB, violating R-23. (Rule: R-23)

#### [MODIFY] [DiagnosticLabOracleTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx)
- **Line 27 [MEDIUM]:** Acts as a comprehensive test lab tab for validating individual BLE opcodes on hardware, running to 48.2KB, violating R-23. (Rule: R-23)
- **Line 97 [HIGH]:** Low-level hardware protocol opcodes (raw byte arrays) are constructed and parsed directly inside the UI/admin components instead of being encapsulated in protocol adapter classes under src/protocols/. (Rule: R-19)
- **Line 214 [MEDIUM]:** Telemetry error logged via AppLogger.error without providing the required payload_size and ssi context parameter keys. (Rule: R-04)

#### [MODIFY] [UserManagementPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/UserManagementPanel.tsx)
- **Line 160 [HIGH]:** Catch variable 'e' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 294 [LOW]:** Inline callbacks renderItem and keyExtractor passed directly to FlatList, causing performance regressions during re-renders. (Rule: R-28)
- **Line 384 [MEDIUM]:** Inline arrow function passed to keyExtractor prop in FlatList. (Rule: R-28)

#### [MODIFY] [useDiagnosticLog.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDiagnosticLog.ts)
- **Line 173 [MEDIUM]:** Asynchronous storage operations (AsyncStorage.setItem) are executed directly inside the setTestLog state updater callback. Side-effects must not run in pure state updates due to concurrent mode rendering hazards. (Rule: R-11)

#### [MODIFY] [AppLogger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Line 1 [LOW]:** AppLogger.ts size is 34.34KB (34,340 bytes) which exceeds the strict 30KB component size limit. (Rule: R-23)
- **Line 219 [MEDIUM]:** App-wide logger managing console streams, debug queues, and analytics dispatching, exceeding 34.3KB, violating R-23. (Rule: R-23)
- **Line 318 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 494 [MEDIUM]:** Type laundering of FlightRecorder breadcrumbs array to Supabase recursive 'Json' type using 'as unknown as'. (Rule: R-08)
- **Line 501 [MEDIUM]:** Type laundering of environment state properties block to Supabase recursive 'Json' type using 'as unknown as'. (Rule: R-08)
- **Line 631 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 674 [HIGH]:** Direct supabase.auth.getUser() call bypasses AuthContext and executes an unnecessary network request. Services should accept user/userId as arguments from the caller or store it via a setter. (Rule: R-15)
- **Line 676 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 761 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 797 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

#### [MODIFY] [AppSettingsService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppSettingsService.ts)
- **Line 80 [MEDIUM]:** In fetchAllSettings(), syncCloud() is executed in the background without being awaited or returning a promise. The caller receives the stale cached configuration from AsyncStorage, and when syncCloud completes updating the cache, the UI is never notified, leading to state inconsistencies. (Rule: R-05)
- **Line 91 [HIGH]:** Blocks the local cache update behind a synchronous, blocking network call to Supabase for app settings updates. If the user is offline, the Supabase upsert fails, throwing/returning an error, which causes the function to catch and return false, completely bypassing the local AsyncStorage cache update. This prevents users from toggling settings (e.g. global_telemetry_enabled) while offline. (Rule: R-05)
- **Line 97 [MEDIUM]:** Type laundering of setting value argument to Supabase recursive 'Json' type using 'as unknown as'. (Rule: R-08)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

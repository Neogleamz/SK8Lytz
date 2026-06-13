# Implementation Plan: sweep-src-data-layer

This is a synthesized sweep plan addressing all rule violations identified in the **DATA_LAYER** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### DATA_LAYER Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [useCuratedPicks.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCuratedPicks.ts)
- **Line 64 [MEDIUM]:** The database fetch error from Supabase is a PostgrestError object. Checking error instanceof Error evaluates to false, causing the logger to stringify the error object (String(error)), which records "[object Object]" in telemetry instead of the actual error description. (Rule: R-06)

#### [MODIFY] [useFavorites.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useFavorites.ts)
- **Line 33 [HIGH]:** AsyncStorage key collision and duplicate usage. The custom/preset favorites key is resolved dynamically via template literal `${STORAGE_PREFIX}Favorites` (resolving to '@Sk8lytz_Favorites') in multiple files (useFavorites.ts, DashboardScreen.tsx) without centralized registration, creating namespace collision and state sync drift risk. (Rule: R-24)
- **Line 178 [LOW]:** A duplicate ternary condition checking e instanceof Error twice. It compiles and runs, but represents redundant dead code. (Rule: R-06)
- **Line 187 [MEDIUM]:** Saves and deletes favorite presets directly to/from Supabase user_saved_presets table. Like GradientsService, this bypasses the offline sync queue mechanism. When offline, favorite creation or deletion will fail to sync to the cloud, resulting in state divergence between local device and remote database. (Rule: R-05)

#### [MODIFY] [DeviceRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line 1 [MEDIUM]:** DeviceRepository.ts is ~39KB in size, which exceeds the project's 30KB monolith file size boundary (R-23). This file manages in-memory caching, offline write queues, position swapping, and background sync logic, and is a candidate for component extraction. (Rule: R-23)
- **Line 64 [MEDIUM]:** Manages state persistence, AsyncStorage caching, and remote database syncing for skate devices, with a size of 39.5KB, violating R-23. (Rule: R-23)
- **Line 83 [MEDIUM]:** Centralized key registry bypass. The key '@Sk8lytz_registered_devices' is defined locally in DeviceRepository.ts as DEVICES_KEY instead of being defined in and imported from storageKeys.ts. (Rule: R-24)
- **Line 126 [MEDIUM]:** Local key definition bypass. The key '@Sk8lytz_deleted_macs' is defined locally as a constant TOMBSTONE_KEY in DeviceRepository.ts and is used to store and check deleted devices, bypassing the centralized storageKeys.ts registry. (Rule: R-24)
- **Line 291 [LOW]:** Uses (group as any).devices type cast bypass to access devices array from the query results. (Rule: R-08)
- **Line 587 [LOW]:** Uses as any casting at the end of supabase query selection. (Rule: R-08)
- **Line 693 [HIGH]:** AppLogger.error is called with two arguments, passing the context object { error: msg, payload_size: 0, ssi: 0 } as the second argument (errorObj). Since this is a plain object and not an instance of Error, the logger fallback String(errorObj) runs, recording "[object Object]" in telemetry, discarding the actual error message msg. (Rule: R-06, R-04)
- **Line 715 [MEDIUM]:** Local key definition bypass. The key '@Sk8lytz_pending_sync' is defined locally as PENDING_KEY in DeviceRepository.ts for storing the registration sync queue, bypassing the centralized storageKeys.ts registry. (Rule: R-24)
- **Line 753 [HIGH]:** AppLogger.error is called with the context object in the second parameter (errorObj), which results in "[object Object]" being logged instead of the actual error message msg. (Rule: R-06, R-04)

#### [MODIFY] [GradientsService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line 17 [MEDIUM]:** Centralized key registry bypass. The key '@Sk8lytz_Builder_Presets' is defined locally in GradientsService.ts as LOCAL_GRADIENTS_KEY instead of being defined in and imported from storageKeys.ts. (Rule: R-24)
- **Line 83 [MEDIUM]:** Storing gradient cache data in AsyncStorage without try-catch or .catch(). (Rule: R-11)
- **Line 118 [MEDIUM]:** Type laundering of CustomBuilderNode[] newPreset.nodes array to Supabase recursive Json nodes column type using 'as unknown as'. (Rule: R-08)
- **Line 125 [MEDIUM]:** Saves and deletes gradients (presets) directly to/from Supabase user_saved_presets table. Although the local AsyncStorage is updated, if the user is offline, the remote database update fails and is never retried because GradientsService does not use the sync queue (which is currently only used by ScenesService). (Rule: R-05)
- **Line 161 [HIGH]:** flushSyncQueue reads, pushes to database, and rewrites the sync queue in AsyncStorage. Lacking a re-entrancy guard, it is prone to data races and duplicate API calls under concurrent executions. (Rule: R-26)
- **Line 186 [MEDIUM]:** Writing local gradient sync queue to AsyncStorage without error boundaries. (Rule: R-11)

#### [MODIFY] [ScenesService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line 75 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 87 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 112 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 146 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 164 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 178 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 186 [MEDIUM]:** AsyncStorage.setItem for scene caching runs without try-catch or .catch(), leaving storage writes unhandled. (Rule: R-11)
- **Line 192 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 205 [MEDIUM]:** Centralized key registry bypass. The key '@Sk8lytz_Scenes' is defined locally in ScenesService.ts as LOCAL_SCENES_KEY instead of being defined in and imported from storageKeys.ts. (Rule: R-24)
- **Line 213 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 258 [HIGH]:** The async flushSyncQueue function reads the local sync queue from AsyncStorage, makes network requests to upsert to Supabase, and writes back the filtered queue. It does not have an internal re-entrancy flag guard. If called concurrently (e.g. if the background sync worker ticks while a manual sync or another sync cycle is running), it causes race conditions leading to duplicate inserts or loss of unsynced local data. (Rule: R-26)
- **Line 272 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 283 [MEDIUM]:** AsyncStorage write setItem is called without a try-catch or .catch() block, which leads to unhandled promise rejections if write operations fail. (Rule: R-11)
- **Line 297 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 311 [MEDIUM]:** Type laundering of scene.steps steps array to Supabase recursive Json nodes column type using 'as unknown as'. (Rule: R-08)
- **Line 336 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 356 [MEDIUM]:** Centralized key registry bypass. The key '@Sk8lytz_Scene_Sync_Queue' is defined locally in ScenesService.ts as LOCAL_SCENE_SYNC_QUEUE_KEY instead of being defined in and imported from storageKeys.ts. (Rule: R-24)
- **Line 362 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 395 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 412 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)

#### [MODIFY] [SkateSpotsService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SkateSpotsService.ts)
- **Line 37 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

#### [MODIFY] [SpeedTrackingService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts)
- **Line 140 [MEDIUM]:** Writing to stats cache in AsyncStorage without try-catch or .catch(). (Rule: R-11)
- **Line 243 [HIGH]:** The async flushPendingSessionQueue reads, uploads, and updates local AsyncStorage session sync queue. It does not have a re-entrancy guard, exposing the queue to race conditions and duplicate uploads under concurrent invocations. (Rule: R-26)
- **Line 268 [MEDIUM]:** Writing local session queue to AsyncStorage without catch or try-catch. (Rule: R-11)
- **Line 456 [LOW]:** Bypasses AppLogger telemetry context and formatting by using raw console.warn (Rule: R-04)
- **Line 587 [LOW]:** Bypasses AppLogger telemetry context and formatting by using raw console.warn (Rule: R-04)

#### [MODIFY] [supabaseClient.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/supabaseClient.ts)
- **Line 30-31 [LOW]:** Bypasses AppLogger telemetry context and formatting by using raw console.warn (Rule: R-04)
- **Line 44 [LOW]:** Bypasses AppLogger telemetry context and formatting by using raw console.warn (Rule: R-04)
- **Line 85 [HIGH]:** Uses as unknown as ReturnType<typeof createClient<Database>> to cast an incomplete mock client object to the full Supabase client type structure, bypassing compiler validation checks. (Rule: R-08)

#### [MODIFY] [TelemetryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/TelemetryService.ts)
- **Line 27 [LOW]:** Casts frozen telemetry state object to any. (Rule: R-08)

#### [MODIFY] [supabase.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/supabase.ts)
- **Line 1 [LOW]:** This auto-generated file contains the database type definitions for Supabase. It has a file size of 146.5KB, violating the R-23 limit of 30KB. (Rule: R-23)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

# Implementation Plan: sweep-services-ScenesService.ts

## Goal
Fix static audit findings for the `sweep-services-ScenesService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 12
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Scenes persistence keys '@Sk8lytz_Scenes' and '@Sk8lytz_Scene_Sync_Queue' are defined locally rather than in storageKeys.ts.
- **Suggested Fix:** Move both keys to storageKeys.ts and import.

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 95
- **Rule:** R-08
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Type safety bypass. The 'as any' cast is used when converting Supabase scene data nodes to application types.
- **Suggested Fix:** Cast to CustomBuilderNode[] instead of any, or double-cast via 'as unknown as CustomBuilderNode[]' if types are structurally compatible but not easily matched.

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 65
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unhandled Promise Rejection risk in fire-and-forget sync. Exceptions during local merging or parsing inside syncCloud() are not caught.
- **Suggested Fix:** Wrap the invocation with a .catch() handler: syncCloud().catch(err => AppLogger.warn('SCENE_SYNC_FAILED', err));

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 103
- **Rule:** R-05
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Queries shared_scenes directly via Supabase for user-authored scenes with no caching or offline fallback, failing completely offline.
- **Suggested Fix:** Cache user's personal shared scenes locally in AsyncStorage and merge on retrieve, similar to the pattern used in getSavedScenes.

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 312
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders scene steps structure to Supabase DB nodes schema.
- **Suggested Fix:** Unify nodes representation models across local scenes and saved preset databases.

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 69
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 124
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 238
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- **Line:** 259
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

# Implementation Plan: sweep-services-CrewService.ts

## Goal
Fix static audit findings for the `sweep-services-CrewService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Real-time crew FSM sync manager handling crew session establishment, participant heartbeats, and peer synchronization.
- **Suggested Fix:** Extract the real-time Supabase channels event bus, session database persistence logic, and background TTL re-join rules into individual sync files.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 69
- **Rule:** R-15
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Queries supabase.auth.getUser() directly in the real-time layer, bypassing Auth Context.
- **Suggested Fix:** Pass down user details from the consuming hook or component that uses AuthContext.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 125
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 174
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 224
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 266
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 303
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 314
- **Rule:** R-17
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Supabase real-time channels are instantiated but unsubscribe and channel tear down are not robustly cleaned up on connection failure, risking resource leaks.
- **Suggested Fix:** Ensure all created channels are registered in a centralized array/object and explicitly clean them up on failure or teardown.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 379
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 532
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 118
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Logs errors via AppLogger without payload_size or ssi context parameters.
- **Suggested Fix:** Supply { payload_size: 0, ssi: 0 } in the log metadata.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 461
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "supabase
        .from('crew_members')
        .delete()
        .eq('session_id', sessionId)
        .then(() => AppLogger.log('CREW_CLEANUP', { action: 'crew_members_deleted', sessionId }))"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 475
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "supabase.removeChannel(channelRef)"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 689
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "AsyncStorage.multiRemove([STORAGE_LAST_SESSION_ID, STORAGE_LAST_SESSION_EXP])"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 757
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "supabase.removeChannel(this.channel)"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 790
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "supabase.removeChannel(this.channel)"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 144
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 168
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 353
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 421
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 483
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 524
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 597
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 691
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line:** 784
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

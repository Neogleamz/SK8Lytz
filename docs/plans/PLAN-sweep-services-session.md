# Implementation Plan: sweep-services-session

## Goal
Fix static audit findings for the `sweep-services-session` domain cluster.

## Proposed Changes

### [MODIFY] [SessionMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SessionMachine.ts)
- **Line:** 12
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Redundant/duplicate local definition of AsyncStorage keys '@sk8lytz_session_phase' and '@sk8lytz_session_pause_time' as local consts, instead of importing registered constants STORAGE_SESSION_PHASE and STORAGE_SESSION_PAUSE_TIME from storageKeys.ts.
- **Suggested Fix:** Import STORAGE_SESSION_PHASE and STORAGE_SESSION_PAUSE_TIME from '../../constants/storageKeys' and use them instead of local definitions.

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
- **Line:** 1
- **Rule:** R-21
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** There are two service files named exactly NotificationService.ts in different directories: src/services/NotificationService.ts and src/services/session/NotificationService.ts. The former manages Expo push notification configurations and handlers, while the latter is an XState callback service wrapper managing local foreground notification updates via Notifee. This structural naming overlap causes developer confusion, import ambiguity, and potential namespace pollution.
- **Suggested Fix:** Rename src/services/session/NotificationService.ts to src/services/session/SessionNotificationService.ts or ForegroundNotificationService.ts to clearly separate the file name from the push notification wrapper and disambiguate import statements.

### [MODIFY] [SessionMachine.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/__tests__/SessionMachine.test.ts)
- **Line:** 214
- **Rule:** R-16
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses hardcoded `setTimeout` delay inside Promise to flush state machine states.
- **Suggested Fix:** Yield to event loop using repeated `await Promise.resolve()` cycles instead of zero-second timers.

### [MODIFY] [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts)
- **Line:** 126
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts)
- **Line:** 141
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts)
- **Line:** 155
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts)
- **Line:** 169
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts)
- **Line:** 199
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
- **Line:** 106
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', err instanceof Error ? err : new Error(String(err)));

### [MODIFY] [SessionMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SessionMachine.ts)
- **Line:** 27
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', err instanceof Error ? err : new Error(String(err)));

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

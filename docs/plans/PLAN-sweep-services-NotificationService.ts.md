# Implementation Plan: sweep-services-NotificationService.ts

## Goal
Fix static audit findings for the `sweep-services-NotificationService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line:** 94
- **Rule:** R-17
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Memory leaks and duplicate event listeners. The methods _wireForegroundHandler() and _wireResponseHandler() are called inside init(). If init() is triggered multiple times concurrently or sequentially, duplicate listeners are registered on Notifications without clearing the previous ones, leaking memory and generating duplicate events.
- **Suggested Fix:** Ensure previous subscriptions are removed before adding new ones, e.g., in _wireForegroundHandler check if (this.foregroundSub) { this.foregroundSub.remove(); }

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line:** 113
- **Rule:** R-21, R-11
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Uncaught database exception in token unregistration during cleanup. The profileService.unregisterPushToken call is not wrapped in a try-catch block. If the network call fails, cleanup is aborted prematurely, and this.token is not set to null, causing invalid token states.
- **Suggested Fix:** Wrap the async call in a try-catch block inside the cleanup method to prevent network errors from failing the state clean-up.

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line:** 72
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line:** 90
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line:** 146
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Diagnostic warning logs logged without standard telemetry parameters (payload_size and ssi metrics).
- **Suggested Fix:** Add default metadata payload_size: 0 and ssi: 0 to telemetry warnings to prevent parsing issues in search and ingestion schemas.

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line:** 196
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line:** 221
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

# Implementation Plan: sweep-services-LocationService.ts

## Goal
Fix static audit findings for the `sweep-services-LocationService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Line:** 123
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Awaited promise for storage/network IO without try-catch block or chained .catch: "await supabase
      .from('crew_sessions')
      .select(SESSION_SELECT)
      .eq('is_active', true)
      .eq('is_public', true)
      .order('created_at', { ascending: false })
      .limit(80)"
- **Suggested Fix:** Wrap the call in a try-catch block or append .catch() to handle rejection.

### [MODIFY] [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Line:** 138
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Swallowing database errors during parallel private session check. Promise.all executes two select queries concurrently but does not check mRes.error or sRes.error, defaulting database failures silently to empty arrays.
- **Suggested Fix:** Verify that both queries resolved without errors before mapping results: if (mRes.error) throw mRes.error; if (sRes.error) throw sRes.error;

### [MODIFY] [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Line:** 96
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.warn.
- **Suggested Fix:** Replace with AppLogger.warn and provide required telemetry context.

### [MODIFY] [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Line:** 30
- **Rule:** R-20
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Direct platform string equality check on require('react-native').Platform.OS === 'web' rather than using standard Platform.select.
- **Suggested Fix:** Use Platform.select or a clean abstraction to determine web demo environment parameters.

### [MODIFY] [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Line:** 71
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Line:** 178
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

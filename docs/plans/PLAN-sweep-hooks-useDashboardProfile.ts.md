# Implementation Plan: sweep-hooks-useDashboardProfile.ts

## Goal
Fix static audit findings for the `sweep-hooks-useDashboardProfile.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useDashboardProfile.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardProfile.ts)
- **Line:** 89
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "AppSettingsService.fetchAllSettings().then(val => {
      if (active) setAppSettings(val);
    })"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

### [MODIFY] [useDashboardProfile.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardProfile.ts)
- **Line:** 116
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useDashboardProfile.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardProfile.ts)
- **Line:** 126
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useDashboardProfile.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardProfile.ts)
- **Line:** 141
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

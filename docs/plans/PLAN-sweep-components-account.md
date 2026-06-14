# Implementation Plan: sweep-components-account

## Goal
Fix static audit findings for the `sweep-components-account` domain cluster.

## Proposed Changes

### [MODIFY] [AccountTabDevices.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/AccountTabDevices.tsx)
- **Line:** 37
- **Rule:** R-25
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** LayoutAnimation platform API is invoked without Platform check or Platform.select.
- **Suggested Fix:** Guard layout animation call with Platform.OS !== 'web' check.

### [MODIFY] [SkaterStatsPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/SkaterStatsPanel.tsx)
- **Line:** 13
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Locally defined key '@sk8lytz_lifetime_stats_cache' used for caching stats, bypasses central storageKeys.ts registry.
- **Suggested Fix:** Move to storageKeys.ts as a registered key and import it.

### [MODIFY] [account.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/account.types.ts)
- **Line:** 111
- **Rule:** R-08
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Use of any cast in type declaration of saveNotifPrefs.
- **Suggested Fix:** Import a strict interface for notification preferences and use it to type the parameter.

### [MODIFY] [SkaterStatsPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/SkaterStatsPanel.tsx)
- **Line:** 43
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

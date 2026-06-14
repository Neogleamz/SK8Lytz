# Implementation Plan: sweep-providers-ComplianceGate.tsx

## Goal
Fix static audit findings for the `sweep-providers-ComplianceGate.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [ComplianceGate.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/ComplianceGate.tsx)
- **Line:** 100
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Swallowing database error during EULA acceptance update. Supabase calls do not throw on database errors; they return an object containing { data, error }. By not checking the error, updates could fail in Supabase but proceed anyway, allowing the user to bypass legal compliance check.
- **Suggested Fix:** Destructure the return value to capture { error } and throw or handle it appropriately: const { error } = await supabase.from(...); if (error) throw error;

### [MODIFY] [ComplianceGate.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/ComplianceGate.tsx)
- **Line:** 28
- **Rule:** R-26
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** State updates on unmounted component inside checkCompliance() async chain. The component checks multiple async queries (settings and profile), and if it unmounts during the resolution, setting state (requiresEula, status, error) causes memory leaks/warnings.
- **Suggested Fix:** Implement a mounted guard ref to check if the component is still mounted before executing state setters in the async/promise callbacks.

### [MODIFY] [ComplianceGate.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/ComplianceGate.tsx)
- **Line:** 59
- **Rule:** R-11, R-05
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Swallowing database error during EULA version check. The { error } returned from the single() query is completely ignored, defaulting the version to 0 on failure which causes the user to be re-prompted even if they have already accepted.
- **Suggested Fix:** Destructure and check { error } from the query result; handle query failure gracefully with a diagnostic warn/error.

### [MODIFY] [ComplianceGate.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/ComplianceGate.tsx)
- **Line:** 72
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ComplianceGate.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/ComplianceGate.tsx)
- **Line:** 118
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ComplianceGate.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/ComplianceGate.tsx)
- **Line:** 138
- **Rule:** R-14
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Lack of error recovery path on compliance error page. Shows static text explaining that verification failed but provides no button to retry, reload, or log out.
- **Suggested Fix:** Add a 'Retry' button that re-invokes checkCompliance(), and optionally a sign-out mechanism.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

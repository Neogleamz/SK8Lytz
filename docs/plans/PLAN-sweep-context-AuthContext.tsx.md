# Implementation Plan: sweep-context-AuthContext.tsx

## Goal
Fix static audit findings for the `sweep-context-AuthContext.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 87
- **Rule:** R-26
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mount-only initialization fetches active session and profile status asynchronously. Though it is a singleton root context that rarely unmounts, the lack of an active guard can raise warning logs during fast navigation hot-reloads or unit testing.
- **Suggested Fix:** Add a boolean flag `let active = true;` inside the effect, verify `if (active)` before calling any state setters in the async function, and return a cleanup setting `active = false;`.

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 89
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 120
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 129
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 142
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 163
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 188
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 254
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', e instanceof Error ? e : new Error(String(e)));

### [MODIFY] [AuthContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line:** 445
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing standard e instanceof Error unwrapping in catch block (uses String(e) directly).
- **Suggested Fix:** Change to error: e instanceof Error ? e.message : String(e)

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

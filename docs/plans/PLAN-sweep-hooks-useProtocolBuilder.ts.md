# Implementation Plan: sweep-hooks-useProtocolBuilder.ts

## Goal
Fix static audit findings for the `sweep-hooks-useProtocolBuilder.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useProtocolBuilder.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProtocolBuilder.ts)
- **Line:** 80
- **Rule:** R-18
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Boolean Trap: bldMic and setBldMic expose a boolean toggle API that wraps and maps the descriptive bldMicSource string union ('APP' | 'DEVICE'), introducing a boolean trap wrapper for state representation.
- **Suggested Fix:** Expose the descriptive string union bldMicSource directly to consumers and update UI control state to bind directly to the enum values.

### [MODIFY] [useProtocolBuilder.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProtocolBuilder.ts)
- **Line:** 131
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** AppLogger.error is called with a string unwrapped message (e.g. e.message) instead of the raw error instance, discarding stack trace.
- **Suggested Fix:** Pass the raw error object 'e' as the second parameter to AppLogger.error.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

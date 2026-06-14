# Implementation Plan: sweep-hooks-cloud

## Goal
Fix static audit findings for the `sweep-hooks-cloud` domain cluster.

## Proposed Changes

### [MODIFY] [useOfflineSyncWorker.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/cloud/useOfflineSyncWorker.ts)
- **Line:** 28
- **Rule:** R-26
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** The offline sync worker runs a recurring async sync loop. While `_isFlushingSyncRef` prevents concurrent runs in the same instance, if the hook unmounts and remounts, a new hook instance will instantiate a new ref lock and start a new sync cycle immediately, racing on Supabase mutations with any in-flight sync from the old instance.
- **Suggested Fix:** Introduce `let active = true;` inside `useEffect`. Check `if (active)` after each await statement within the try block. Set `active = false;` in the hook's cleanup function.

### [MODIFY] [useOfflineSyncWorker.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/cloud/useOfflineSyncWorker.ts)
- **Line:** 41
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

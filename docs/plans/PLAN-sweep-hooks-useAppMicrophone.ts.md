# Implementation Plan: sweep-hooks-useAppMicrophone.ts

## Goal
Fix static audit findings for the `sweep-hooks-useAppMicrophone.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useAppMicrophone.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAppMicrophone.ts)
- **Line:** 46
- **Rule:** R-18
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Boolean Trap: isCapturingRef is a mutable boolean ref that tracks recording state in parallel with the recorder.isRecording or recorder.getStatus().isRecording state, creating duplicate sources of truth for the recording status.
- **Suggested Fix:** Eliminate the isCapturingRef flag and rely directly on the recorder's native recording state queries to guard start and stop routines.

### [MODIFY] [useAppMicrophone.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAppMicrophone.ts)
- **Line:** 147
- **Rule:** R-12
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing useEffect dependencies: The useEffect hook responsible for managing the recording lifecycle has eslint-disable-next-line react-hooks/exhaustive-deps and excludes startRecording and stopRecording from the dependency array. Since these helper functions are recreated on every render, the cleanup function registered on dependency changes will capture stale closures of stopRecording.
- **Suggested Fix:** Wrap startRecording and stopRecording in useCallback with correct dependencies, remove the eslint disable comment, and include them in the dependency array.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

# Implementation Plan: sweep-components-CommunityModal.tsx

## Goal
Fix static audit findings for the `sweep-components-CommunityModal.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [CommunityModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CommunityModal.tsx)
- **Line:** 239
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Async operations (downloadScene, upvoteScene, deleteScene) called inside action press events are not wrapped in try/catch blocks, potentially leaking rejections or trapping the UI in a locked processing state.
- **Suggested Fix:** Wrap upvoteScene, deleteScene, and downloadScene in try/catch blocks, resetting the processing ID inside a 'finally' block.

### [MODIFY] [CommunityModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CommunityModal.tsx)
- **Line:** 266
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The FlatList renderItem callback's dependency array includes non-memoized handlers (handleUpvote, handleDelete, handleApply) from the parent component scope, invalidating the useCallback reference on every parent render.
- **Suggested Fix:** Wrap handleUpvote, handleDelete, and handleApply in useCallback hooks in the parent scope.

### [MODIFY] [CommunityModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CommunityModal.tsx)
- **Line:** 228
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** System warning log output lacks mandatory BLE signal context (payload_size or RSSI/ssi) as mandated by R-04.
- **Suggested Fix:** Supply current session BLE details or other hardware metadata context in the telemetry payload if available.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

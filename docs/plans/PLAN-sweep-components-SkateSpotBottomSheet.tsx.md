# Implementation Plan: sweep-components-SkateSpotBottomSheet.tsx

## Goal
Fix static audit findings for the `sweep-components-SkateSpotBottomSheet.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [SkateSpotBottomSheet.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/SkateSpotBottomSheet.tsx)
- **Line:** 55
- **Rule:** R-07
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Declares a child component SurfaceChip inline inside the parent component body, causing it to be completely recreated on every parent render and triggering unmounting/remounting behavior.
- **Suggested Fix:** Move SurfaceChip outside the SkateSpotBottomSheet declaration, passing the required handler/selected status as props.

### [MODIFY] [SkateSpotBottomSheet.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/SkateSpotBottomSheet.tsx)
- **Line:** 19
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Calls createStyles(Colors) inside the render body on every render.
- **Suggested Fix:** Move styles outside or memoize them.

### [MODIFY] [SkateSpotBottomSheet.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/SkateSpotBottomSheet.tsx)
- **Line:** 47
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** AppLogger.warn call in catch block for claimAndUpdateSpot fails to pass required payload_size and ssi context properties.
- **Suggested Fix:** Add { payload_size: 0, ssi: 0 } to options block.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

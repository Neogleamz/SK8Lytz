# Implementation Plan: sweep-src-utils

This is a synthesized sweep plan addressing all rule violations identified in the **UTILS** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### UTILS Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [kMeansPalette.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/kMeansPalette.ts)
- **Line 25 [HIGH]:** The 'extractKMeansPalette' function is marked as a Reanimated Worklet ('worklet') meaning it executes synchronously on the UI thread. Because it implements K-Means clustering, passing a large pixel array can block the UI thread, resulting in frame drops and blocking gestures/animations. (Rule: R-07)

#### [MODIFY] [migrateAuthTokens.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/migrateAuthTokens.ts)
- **Line 24 [HIGH]:** The catch block catches an error parameter 'e' typed as 'unknown' and passes it directly to 'AppLogger.error' without any 'instanceof Error' unwrapping or string coercion, violating standard error handling practices. (Rule: R-06)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

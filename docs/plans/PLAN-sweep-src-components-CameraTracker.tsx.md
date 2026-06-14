# Implementation Plan: sweep-src-components-CameraTracker.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-CameraTracker.tsx` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] CameraTracker.tsx
- Line 52: Fix `R-06` violation. Catch block inside permission request fails to unwrap error properly and logs directly via console.error instead of AppLogger. (Suggested: Log the camera permission error via AppLogger.error and unwrap the error as standard err instanceof Error ? err : new Error(String(err)).)
- Line 90: Fix `R-04` violation. GPU Resizer initialization error logged with console.error instead of AppLogger.error. (Suggested: Log the error to AppLogger.error to provide central crash/telemetry visibility.)
- Line 152: Fix `R-04` violation. Camera Frame Processor Error logged via console.error instead of AppLogger.error. (Suggested: Log the frame processor error to AppLogger to track processing failures.)
- Line 151: Fix `R-08` violation. Unused variable safeErr defined in catch block of camera frame processor. (Suggested: Either remove safeErr or pass it to AppLogger as part of the refactored error handling logic.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

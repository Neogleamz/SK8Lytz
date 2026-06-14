# Implementation Plan: sweep-protocols-SpatialEngine.ts

## Goal
Fix static audit findings for the `sweep-protocols-SpatialEngine.ts` domain cluster.

## Proposed Changes

### [MODIFY] [SpatialEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SpatialEngine.ts)
- **Line:** 1
- **Rule:** R-23
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** SpatialEngine is a monolith exceeding 30KB. It contains complex layout builders, native hardware parity pattern generation, and visualizer helpers in a single massive file (1453 lines, ~60.5KB).
- **Suggested Fix:** Extract pattern generation and math helpers into separate modules (e.g., src/protocols/SpatialMath.ts and src/protocols/HardwarePatterns.ts) to keep each module under the 30KB threshold.

### [MODIFY] [SpatialEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SpatialEngine.ts)
- **Line:** 3
- **Rule:** R-29
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** SpatialEngine.ts imports type RGB, PatternId, PatternOptions from protocols/PatternEngine.ts, which imports value functions getPatternTransitionType, getHardwarePixelArray from protocols/SpatialEngine.ts. This forms a circular dependency, although because the reverse import is type-only, it is compiled away at runtime.
- **Suggested Fix:** Move the shared types to a shared type declaration file to break the static dependency loop.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

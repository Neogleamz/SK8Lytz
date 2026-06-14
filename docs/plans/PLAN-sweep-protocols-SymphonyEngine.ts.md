# Implementation Plan: sweep-protocols-SymphonyEngine.ts

## Goal
Fix static audit findings for the `sweep-protocols-SymphonyEngine.ts` domain cluster.

## Proposed Changes

### [MODIFY] [SymphonyEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SymphonyEngine.ts)
- **Line:** 1
- **Rule:** R-29
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** SymphonyEngine.ts imports type RGB from protocols/PatternEngine.ts, which imports the value functions getMusicVisualizerFrame, getSymphonyVisualizerFrame from protocols/SymphonyEngine.ts. This forms a circular dependency, although because the reverse import is type-only, it is compiled away at runtime.
- **Suggested Fix:** Move the shared types to a shared type declaration file to break the static dependency loop.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

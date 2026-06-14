# Implementation Plan: sweep-protocols-VisualizerEngine.ts

## Goal
Fix static audit findings for the `sweep-protocols-VisualizerEngine.ts` domain cluster.

## Proposed Changes

### [MODIFY] [VisualizerEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/VisualizerEngine.ts)
- **Line:** 1
- **Rule:** R-29
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** VisualizerEngine.ts imports type RGB, PatternId, PatternOptions from protocols/PatternEngine.ts, which imports the value function getVisualizerFrame from protocols/VisualizerEngine.ts. This forms a circular dependency, although because the reverse import is type-only, it is compiled away at runtime.
- **Suggested Fix:** Move the shared types to a shared type declaration file to break the static dependency loop.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

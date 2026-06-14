# Implementation Plan: sweep-components-VisualizerUnit.tsx

## Goal
Fix static audit findings for the `sweep-components-VisualizerUnit.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [VisualizerUnit.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VisualizerUnit.tsx)
- **Line:** 452
- **Rule:** R-08
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Uses 'as unknown as DisplayDevice & IDeviceState' type laundering on partial mock objects, bypassing Type Safety and introducing high risk of runtime undefined property crashes.
- **Suggested Fix:** Provide a safe conversion helper or check specific optional properties, or refactor onLongPress callback signature to accept partial device definitions.

### [MODIFY] [VisualizerUnit.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VisualizerUnit.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** VisualizerUnit.tsx is 30.7KB, exceeding the 30KB threshold for component extraction. The geometry rendering structures and helper calculations should be extracted to reduce file complexity.
- **Suggested Fix:** Extract sub-components (such as layout paths or rendering layers) into individual files under src/components/visualizer/.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

# Implementation Plan: sweep-src-pattern-engine

This is a synthesized sweep plan addressing all rule violations identified in the **PATTERN_ENGINE** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### PATTERN_ENGINE Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [useAppMicrophone.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAppMicrophone.ts)
- **Line 147 [LOW]:** Hook functions startRecording(), stopRecording(), and requestMicPermission() are not memoized with useCallback. They are recreated on every render, and start/stop recording calls are bypassed in useEffect using eslint-disable-next-line. (Rule: R-07)
- **Line 161 [LOW]:** Unguarded async permission requests: requestMicPermission() calls async openGlobalPermissionsModal() and checkPermission('MIC') without a try/catch block, risking unhandled promise rejections on device/storage failures. (Rule: R-11)

#### [MODIFY] [useMusicMode.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useMusicMode.ts)
- **Line 109 [HIGH]:** Severe hook dependency array violation in useMusicMode.ts: the useEffect that triggers handleMusicChange() omits activeMode, micSensitivity, brightness, and handleMusicChange from its dependencies. Modifying mic sensitivity or brightness in the UI will not trigger a configuration update to the hardware. (Rule: R-08)

#### [MODIFY] [useStreetMode.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useStreetMode.ts)
- **Line 188 [HIGH]:** Stale closure of `deviceContext` in Accelerometer listener. The `deviceContext` is used inside the listener callback to log AppLogger events, but it is omitted from the dependency array of the `useEffect` block, meaning changes to the device context won't update the active listener. (Rule: R-12)

#### [MODIFY] [SpatialEngine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SpatialEngine.ts)
- **Line 1-5 [HIGH]:** This file is a massive spatial color pattern generator that contains 1,452 lines of animation logic. It compiles native breathe, strobe, and fade patterns. The file size is 60.7KB, violating R-23. (Rule: R-23)
- **Line 856 [HIGH]:** Missing case implementation for SK8Lytz Signature (patternId 44) in generateArray(). Falls back to default solid color instead of rendering the correct 7-Color Center-In Fill animation on the visualizer. (Rule: R-21)
- **Line 863 [HIGH]:** Visualizer parity mismatch in generateArray(): Case 72 (Center-Out Marquee) incorrectly routes to buildNativeCenterOut() (which returns a Center-Out solid fill) instead of calling the existing buildCenterOutMarquee() generator. (Rule: R-21)

#### [MODIFY] [VisualizerEngine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/VisualizerEngine.ts)
- **Line 42 [HIGH]:** Visualizer pattern range check in getVisualizerFrame() only checks up to patternId 33, excluding new templates (IDs 34-43). These fall through to the continuous scroll logic, causing them to animate using simple pixel shifts instead of executing their custom animation math (like buildPlasma, buildFireFlame, buildStarlight). (Rule: R-21)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

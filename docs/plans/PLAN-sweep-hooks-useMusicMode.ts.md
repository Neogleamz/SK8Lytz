# Implementation Plan: sweep-hooks-useMusicMode.ts

## Goal
Fix static audit findings for the `sweep-hooks-useMusicMode.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useMusicMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useMusicMode.ts)
- **Line:** 109
- **Rule:** R-12
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Missing useEffect dependencies: The useEffect hook responsible for sending the current music configuration to hardware on change does not include activeMode, micSensitivity, brightness, or handleMusicChange in its dependency array. This means if a user updates the mic sensitivity or brightness in the UI, or transitions activeMode to MUSIC, the updated configuration will not be synchronized to the hardware.
- **Suggested Fix:** Include handleMusicChange in the dependency array (ensure handleMusicChange is properly wrapped in useCallback, including its stable references), and add activeMode, micSensitivity, and brightness to the useEffect dependencies.

### [MODIFY] [useMusicMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useMusicMode.ts)
- **Line:** 131
- **Rule:** R-12
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Stale Closure in exit useEffect: The hook executes setMusicConfig with isOn: false when transitioning away from MUSIC mode, but reads all state variables (musicPrimaryColor, musicSecondaryColor, musicPatternId, musicMatrixStyle, micSensitivity, brightness) inside the useEffect callback without including them in the dependency array (only activeMode is included). The values captured in this effect's closure will be stale, causing the exit packet to be sent to the device with outdated settings.
- **Suggested Fix:** Utilize mutable refs to store the latest values of colors, pattern ID, sensitivity, brightness, and matrix style, and read those refs inside the transition-gated useEffect instead of reading the state variables directly.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

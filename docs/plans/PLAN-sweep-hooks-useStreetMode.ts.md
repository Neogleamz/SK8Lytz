# Implementation Plan: sweep-hooks-useStreetMode.ts

## Goal
Fix static audit findings for the `sweep-hooks-useStreetMode.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useStreetMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useStreetMode.ts)
- **Line:** 81
- **Rule:** R-18
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Boolean Trap: streetBrakingRef.current is a mutable boolean ref that tracks the hard braking state in parallel with the motionState/motionStateRef FSM, creating scattered states and synchronization/race condition risks.
- **Suggested Fix:** Refactor useStreetMode to rely entirely on motionState and motionStateRef for braking state, eliminating the duplicate streetBrakingRef boolean variable.

### [MODIFY] [useStreetMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useStreetMode.ts)
- **Line:** 112
- **Rule:** R-21
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Redundant/Split-Brain State Variable: The state 'streetBrakeColor' is declared, exposed, but completely unused/ignored in applyStreetPattern. The braking states STOPPED and HARD_BRAKING hardcode #FF0000 instead of referencing the user-configurable streetBrakeColor.
- **Suggested Fix:** Use the configured state variable 'streetBrakeColor' in place of the hardcoded '#FF0000' color inside applyStreetPattern.

### [MODIFY] [useStreetMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useStreetMode.ts)
- **Line:** 193
- **Rule:** R-17
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Event Listener Leak Side-Effect: On activeMode change away from STREET, calling Accelerometer.removeAllListeners() destroys all accelerometer listeners registered globally across the app instead of surgically removing the hook's local listener. This can disrupt other features using the accelerometer concurrently.
- **Suggested Fix:** Store the subscription token returned by Accelerometer.addListener and call subscription.remove() to clean up only this hook's listener surgically.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

# Implementation Plan: sweep-src-context

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-context` domain cluster.

## Batch & Wave
- **Wave:** 2
- **Prerequisite:** Wave 1 fully merged

## Proposed Changes
### [MODIFY] SessionContext.tsx
- Line 225: Fix `R-26` violation. Crash recovery recover() is an async function that reads/writes AsyncStorage and sends state transition events. It is triggered on mount and on every AppState 'change' transition to 'active'. Because there is no concurrency lock or mount-guard inside recover(), rapid foreground/background app transitions or component re-renders trigger concurrent asynchronous recovery runs that race on AsyncStorage access and send redundant state transition events to the XState actor. (Suggested: Add a boolean flag 'let active = true;' inside useEffect. Inside recover(), verify 'if (!active) return;' after each AsyncStorage await statement. Return a cleanup function '() => { active = false; subscription.remove(); }' in the useEffect.)
- Line 228: Fix `R-11` violation. Awaited promise for AsyncStorage IO without try-catch block or chained .catch() inside recover(). If reading the pending background end key fails, it will cause an unhandled promise rejection in the React lifecycle, potentially crashing the app. (Suggested: Wrap the async operation in a try-catch block to handle the potential failure cleanly.)
- Line 27: Fix `R-24` violation. Redundant local definition of AsyncStorage keys '@sk8lytz_session_phase' and '@sk8lytz_session_pause_time' instead of importing and using centralized constants from storageKeys.ts. This bypasses the centralized storage registry and risks key collisions or drift. (Suggested: Import STORAGE_SESSION_PHASE and STORAGE_SESSION_PAUSE_TIME from src/constants/storageKeys.ts and use them directly.)
- Line 323: Fix `R-12` violation. The notification display useEffect depends directly on telemetry.gpsSpeed and telemetry.sessionDistanceMiles. Because these values update rapidly (multiple times a second) during a live session, this effect is constantly destroyed and recreated, triggering frequent notification updates and potential stale closures on the interval timers. (Suggested: Keep stable values or use a mutable useRef for fast-changing values like speed and distance inside the notification generator, rather than listing them as reactive effect dependencies.)
### [MODIFY] AuthContext.tsx
- Line 89: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 120: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 129: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 142: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 163: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 188: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)
- Line 219: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
- Line 234: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
- Line 245: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
- Line 255: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
- Line 265: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

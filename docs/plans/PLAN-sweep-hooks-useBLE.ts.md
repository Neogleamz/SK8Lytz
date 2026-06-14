# Implementation Plan: sweep-hooks-useBLE.ts

## Goal
Fix static audit findings for the `sweep-hooks-useBLE.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 327
- **Rule:** R-09
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Unredacted deviceId is logged in the PROTOCOL_ERROR trace when failing to parse characteristic notifications.
- **Suggested Fix:** Use scrubPII(deviceId)

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 34
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Hardware blacklist cache key '@Sk8lytz_hardware_blacklist' defined locally as a constant instead of registering it in storageKeys.ts.
- **Suggested Fix:** Register @Sk8lytz_hardware_blacklist in constants/storageKeys.ts and import it.

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 336
- **Rule:** R-26
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The AppState listener triggers an asynchronous audit of native connections on wake. If the hook unmounts while the async checking loop is running, the callback will still call `updateConnectedDevices` when resolves complete, attempting to update state on an unmounted component.
- **Suggested Fix:** Introduce `let active = true;` inside the `useEffect`. Check `if (active)` inside the listener callback before performing state updates. Set `active = false;` in the hook's cleanup function.

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 224
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 308
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 364
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 398
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line:** 454
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

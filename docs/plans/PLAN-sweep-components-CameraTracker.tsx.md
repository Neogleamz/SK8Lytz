# Implementation Plan: sweep-components-CameraTracker.tsx

## Goal
Fix static audit findings for the `sweep-components-CameraTracker.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- **Line:** 90
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- **Line:** 152
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- **Line:** 172
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Missing try/catch around await requestPermission('CAMERA') in onPress callback. If the native permission module throws or rejects, it will cause an unhandled promise rejection crash.
- **Suggested Fix:** Wrap requestPermission in a try/catch block and handle errors gracefully.

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- **Line:** 12
- **Rule:** R-21
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** The interface definition CameraTrackerProps is duplicated three times across platform files (.tsx, .web.tsx, .d.ts), introducing risk of API drift if props are updated in one location and forgotten in others.
- **Suggested Fix:** Declare the props interface in a shared types file or let platform files import it from .d.ts.

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- **Line:** 47
- **Rule:** R-26
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Requests camera permissions when `hasPermission` or `requestFromHook` changes. If the permission promise resolves after the component has unmounted or dependencies updated, `requestFromHook()` is invoked, leading to potential state updates on an unmounted component.
- **Suggested Fix:** Declare `let active = true;` inside `useEffect`, and verify `if (active && granted) requestFromHook();`. Set `active = false;` in the cleanup function.

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
- **Line:** 52
- **Rule:** R-06, R-04
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Permission catch block passes raw err directly to console.error instead of unwrapping via err instanceof Error ? err.message : String(err).
- **Suggested Fix:** Unwrap the error: console.error('Camera permission request failed:', err instanceof Error ? err.message : String(err));

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

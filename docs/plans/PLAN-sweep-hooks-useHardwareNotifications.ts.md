# Implementation Plan: sweep-hooks-useHardwareNotifications.ts

## Goal
Fix static audit findings for the `sweep-hooks-useHardwareNotifications.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useHardwareNotifications.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHardwareNotifications.ts)
- **Line:** 154
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Delta check re-entrancy race condition. The delta checks use deviceConfigsRef.current[deviceId] to prevent redundant updates. However, deviceConfigs is updated asynchronously via state setters. If multiple BLE notification packets arrive rapidly in the same execution cycle, the state updates are batched, the ref is not yet updated, and the dirty checks return true for all packets, causing redundant database and state updates.
- **Suggested Fix:** Introduce a synchronous in-memory tracking cache or updates lock ref to instantly mark a packet write as in-progress and filter out rapid successive notifications.

### [MODIFY] [useHardwareNotifications.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHardwareNotifications.ts)
- **Line:** 150
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Floating promise call on async confirmProductId(). The async function is called inside setOnDataReceived and setOnHardwareProbed callbacks without being awaited or caught.
- **Suggested Fix:** Add an explicit .catch() block to prevent unhandled rejections: DeviceRepository.getInstance().confirmProductId(deviceId).catch(e => AppLogger.warn('Confirm failed', e));

### [MODIFY] [useHardwareNotifications.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHardwareNotifications.ts)
- **Line:** 136
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useHardwareNotifications.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHardwareNotifications.ts)
- **Line:** 185
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useHardwareNotifications.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHardwareNotifications.ts)
- **Line:** 199
- **Rule:** R-12
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing state setter setLastRawNotification in the useEffect dependency array registering BLE data callback, which is a static analysis lint warning.
- **Suggested Fix:** Append setLastRawNotification to the dependency array.

### [MODIFY] [useHardwareNotifications.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHardwareNotifications.ts)
- **Line:** 208
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

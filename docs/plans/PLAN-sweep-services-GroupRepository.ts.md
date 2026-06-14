# Implementation Plan: sweep-services-GroupRepository.ts

## Goal
Fix static audit findings for the `sweep-services-GroupRepository.ts` domain cluster.

## Proposed Changes

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 23
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Locally defined custom group storage keys '@Sk8lytz_custom_groups' and '@Sk8lytz_pending_group_sync' defined locally as literals rather than being registered in storageKeys.ts. This leads to undocumented storage keys.
- **Suggested Fix:** Move both keys to constants/storageKeys.ts and import them.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 24
- **Rule:** R-15
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Invokes supabase.auth.getUser() directly from inside the service, bypassing reactive React Auth Context.
- **Suggested Fix:** Accept userId: string as an argument from the caller component/hook (which consumes AuthContext).

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 68
- **Rule:** R-04, R-06
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Logs exceptions via AppLogger without supplying the mandatory telemetry parameters payload_size and ssi.
- **Suggested Fix:** Pass { payload_size: 0, ssi: 0 } to AppLogger.error().

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 17
- **Rule:** R-29
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** GroupRepository.ts imports type RegisteredDevice from hooks/useRegistration.ts, while hooks/useRegistration.ts imports the value DeviceRepository class, which in turn imports GroupRepository. This forms a circular dependency, although because the final import is type-only, it is compiled away at runtime and does not cause runtime issues.
- **Suggested Fix:** Move the RegisteredDevice interface definition to a shared type declaration file (e.g. src/types/devices.types.ts) to break the static dependency loop.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 88
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 151
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 159
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 233
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 254
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 285
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 314
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line:** 323
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

# Implementation Plan: sweep-constants-storageKeys.ts

## Goal
Fix static audit findings for the `sweep-constants-storageKeys.ts` domain cluster.

## Proposed Changes

### [MODIFY] [storageKeys.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/storageKeys.ts)
- **Line:** 1
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Lack of centralization for AsyncStorage keys. A primary goal of storageKeys.ts is to centralize all AsyncStorage keys in a single registry to prevent key collisions, split-brain, and developer confusion. However, there are at least 18 other storage keys declared locally or inline in other files (such as ThemeContext.tsx, useBLE.ts, DeviceRepository.ts, GroupRepository.ts, ScenesService.ts, etc.) instead of importing them from storageKeys.ts.
- **Suggested Fix:** Move all inline-defined storage keys from repositories, contexts, and hooks into src/constants/storageKeys.ts as exported constants, and refactor the files to import and consume them from storageKeys.ts.

### [MODIFY] [storageKeys.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/storageKeys.ts)
- **Line:** 30
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Direct string bypasses of central storageKeys.ts constants. Several keys that are defined in storageKeys.ts (e.g., STORAGE_SESSION_PHASE, STORAGE_SESSION_PAUSE_TIME, STORAGE_SESSION_ACTIVE) are completely bypassed in other files (SessionContext.tsx and SessionMachine.ts) which hardcode the string literals '@sk8lytz_session_phase', '@sk8lytz_session_pause_time', '@sk8lytz_session_active' inline instead of using the exported constants.
- **Suggested Fix:** Import STORAGE_SESSION_PHASE, STORAGE_SESSION_PAUSE_TIME, and STORAGE_SESSION_ACTIVE from storageKeys.ts inside SessionContext.tsx and SessionMachine.ts instead of declaring local duplicates.

### [MODIFY] [storageKeys.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/storageKeys.ts)
- **Line:** 3
- **Rule:** R-21
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Unused/Dead AsyncStorage key constants in registry. Multiple key constants defined in the registry are dead code and are never referenced or used in the entire codebase: STORAGE_DEMO_HALO, STORAGE_DEMO_SOUL, and STORAGE_GROUPS_MIGRATED_V2.
- **Suggested Fix:** Remove the dead constants from storageKeys.ts to clean up the registry.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

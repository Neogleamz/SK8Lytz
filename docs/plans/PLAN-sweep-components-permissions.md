# Implementation Plan: sweep-components-permissions

## Goal
Fix static audit findings for the `sweep-components-permissions` domain cluster.

## Proposed Changes

### [MODIFY] [GranularPermissionsList.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/permissions/GranularPermissionsList.tsx)
- **Line:** 99
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Sequential async calls to check permissions inside useEffect (L98-100) are not wrapped in a try/catch block. If checkPermission throws an error, it will crash the initialization sequence.
- **Suggested Fix:** Wrap the permission checking loop in a try/catch block.

### [MODIFY] [GranularPermissionsList.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/permissions/GranularPermissionsList.tsx)
- **Line:** 138
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Linking.openSettings() returns a promise that is not handled or caught with a .catch() block inside the alert's button action, risking unhandled promise rejection if setting panel access fails.
- **Suggested Fix:** Chain .catch(...) to Linking.openSettings() or wrap it in a function with error catching.

### [MODIFY] [GranularPermissionsList.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/permissions/GranularPermissionsList.tsx)
- **Line:** 77
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Dynamically calls createStyles(Colors) on every render.
- **Suggested Fix:** Move styles outside or memoize them.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

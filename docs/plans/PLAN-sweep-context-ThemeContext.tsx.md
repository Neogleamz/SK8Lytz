# Implementation Plan: sweep-context-ThemeContext.tsx

## Goal
Fix static audit findings for the `sweep-context-ThemeContext.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [ThemeContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/ThemeContext.tsx)
- **Line:** 10
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Theme persistence keys '@Sk8lytz_ThemeMode' and '@Sk8lytz_ControlUITheme' are defined as local constants (THEME_KEY and CONTROL_THEME_KEY) instead of being exported and imported from storageKeys.ts.
- **Suggested Fix:** Move constants to constants/storageKeys.ts and import them.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

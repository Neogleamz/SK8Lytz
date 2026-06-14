# Implementation Plan: sweep-theme-theme.ts

## Goal
Fix static audit findings for the `sweep-theme-theme.ts` domain cluster.

## Proposed Changes

### [MODIFY] [theme.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/theme/theme.ts)
- **Line:** 93
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Incompatible web property cast to TextStyle. In TextShadows.glow, the web-specific attribute 'textShadow' is passed to Platform.select. Since React Native's TextStyle type does not contain a 'textShadow' property, it is cast to TextStyle with 'as TextStyle'. This is a type safety bypass that uses type-laundering to mask a type incompatibility.
- **Suggested Fix:** Use a custom type/interface extending TextStyle that permits the textShadow parameter (e.g., WebTextStyle), or allow Platform.select to infer a wider type and only cast on target screen usage.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

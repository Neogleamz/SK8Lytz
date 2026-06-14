# Implementation Plan: sweep-screens-AuthScreen.tsx

## Goal
Fix static audit findings for the `sweep-screens-AuthScreen.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [AuthScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- **Line:** 104
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Linking.openURL in showHelp has no catch block or error handling wrapper, risking unhandled promise rejection if browser/intent handler fails or is absent.
- **Suggested Fix:** Wrap Linking.openURL(...) in a try-catch block or chain a .catch(...) block to handle rejection safely.

### [MODIFY] [AuthScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- **Line:** 79
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** AppLogger.warn call lacks the required payload_size and ssi context properties in its options payload.
- **Suggested Fix:** Include { payload_size: 0, ssi: 0 } or real values in the options object parameter.

### [MODIFY] [AuthScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- **Line:** 102
- **Rule:** R-20
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Platform check compares Platform.OS === 'ios' or 'android' directly, but doesn't check for web platform compatibility when opening email links via mailto.
- **Suggested Fix:** Use Platform.select() or add explicit web support handling.

### [MODIFY] [AuthScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)
- **Line:** 90
- **Rule:** R-20
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses ternary Platform.OS === 'ios' check for keyboard avoiding behavior instead of Platform.select.
- **Suggested Fix:** Use Platform.select({ ios: 'padding', default: undefined })

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

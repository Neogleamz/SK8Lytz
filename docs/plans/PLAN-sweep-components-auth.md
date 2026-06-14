# Implementation Plan: sweep-components-auth

## Goal
Fix static audit findings for the `sweep-components-auth` domain cluster.

## Proposed Changes

### [MODIFY] [AuthFormForgotPassword.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormForgotPassword.tsx)
- **Line:** 63
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** handleResetPassword executes an async request without any re-entrancy guard.
- **Suggested Fix:** Add a locking state to prevent duplicate reset email dispatches.

### [MODIFY] [AuthFormSignIn.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormSignIn.tsx)
- **Line:** 72
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** handleSignIn is an async action triggering database auth requests but does not guard against concurrent clicks (no re-entrancy locking), potentially invoking multiple Supabase auth calls.
- **Suggested Fix:** Set a loading ref or lock to block subsequent calls until the initial login attempt finishes.

### [MODIFY] [AuthFormSignUp.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormSignUp.tsx)
- **Line:** 84
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** handleSignUp lacks a re-entrancy blocking lock. Double tapping can send duplicate sign-up confirmation emails/requests.
- **Suggested Fix:** Prevent multiple invocation clicks using a re-entrancy guard.

### [MODIFY] [DevSandboxDrawer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/DevSandboxDrawer.tsx)
- **Line:** 20
- **Rule:** R-26
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Reads demo mode preferences from AsyncStorage on mount. If the drawer unmounts before the read completes, `setIsVirtualSkatesEnabled` is called on the unmounted component.
- **Suggested Fix:** Declare `let active = true;` inside `useEffect`, check `if (active) setIsVirtualSkatesEnabled(...)`, and return a cleanup function setting `active = false;`.

### [MODIFY] [DevSandboxDrawer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/DevSandboxDrawer.tsx)
- **Line:** 31
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Uses 'String(e)' directly for formatting error messages without checking if it is an instance of Error first, missing standard error details (like e.message).
- **Suggested Fix:** Use standard unwrapping check:

setErrorMessage('...' + (e instanceof Error ? e.message : String(e)));

### [MODIFY] [DevSandboxDrawer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/DevSandboxDrawer.tsx)
- **Line:** 86
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Uses 'String(e)' directly for formatting error messages without checking if it is an instance of Error first, missing standard error details (like e.message).
- **Suggested Fix:** Use standard unwrapping check:

setErrorMessage('...' + (e instanceof Error ? e.message : String(e)));

### [MODIFY] [DevSandboxDrawer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/DevSandboxDrawer.tsx)
- **Line:** 104
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Uses 'String(e)' directly for formatting error messages without checking if it is an instance of Error first, missing standard error details (like e.message).
- **Suggested Fix:** Use standard unwrapping check:

setErrorMessage('...' + (e instanceof Error ? e.message : String(e)));

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

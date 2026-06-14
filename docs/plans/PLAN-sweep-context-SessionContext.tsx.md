# Implementation Plan: sweep-context-SessionContext.tsx

## Goal
Fix static audit findings for the `sweep-context-SessionContext.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
- **Line:** 225
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Crash recovery `recover()` is an async function that reads/writes AsyncStorage and sends state transition events. It is triggered on mount and on every AppState 'active' change. Because there is no boolean guard inside `recover()` checking if the hook has unmounted, or if a concurrent recovery run is in progress, rapid background/foreground transitions or component rerenders trigger concurrent asynchronous recovery runs that race on AsyncStorage access and send redundant state transition events to the XState actor.
- **Suggested Fix:** Add a boolean flag `let active = true;` within the `useEffect`. Inside `recover()`, verify `if (!active) return;` after each `AsyncStorage` await statement. Return `() => { active = false; sub.remove(); }` in the cleanup function.

### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
- **Line:** 228
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Awaited promise for storage/network IO without try-catch block or chained .catch: "await AsyncStorage.getItem(STORAGE_PENDING_BG_END)"
- **Suggested Fix:** Wrap the call in a try-catch block or append .catch() to handle rejection.

### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
- **Line:** 236
- **Rule:** R-11, R-24
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Redundant local definition of AsyncStorage key '@sk8lytz_session_phase' as a local constant 'SESSION_PHASE_KEY' instead of importing and using STORAGE_SESSION_PHASE from storageKeys.ts. This leads to key duplication across files.
- **Suggested Fix:** Import STORAGE_SESSION_PHASE from '../constants/storageKeys' and use it directly or map it to SESSION_PHASE_KEY.

### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
- **Line:** 50
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Async storage operations on app boot (load()) and crash recovery (recover()) are executed without try/catch protection, risking unhandled promise rejections on storage failure.
- **Suggested Fix:** Wrap the async function body in a try/catch block and handle errors appropriately.

### [MODIFY] [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx)
- **Line:** 49
- **Rule:** R-26
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mount-only initialization fetches initial session settings from AsyncStorage asynchronously. Lacks active guard check, raising warnings in unit testing environments if unmounted during the read.
- **Suggested Fix:** Declare `let active = true;` inside `useEffect`. Check `if (active)` inside `load()` before calling state setters, and return a cleanup function setting `active = false;`.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

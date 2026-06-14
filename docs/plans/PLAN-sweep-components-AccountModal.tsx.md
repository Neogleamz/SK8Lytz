# Implementation Plan: sweep-components-AccountModal.tsx

## Goal
Fix static audit findings for the `sweep-components-AccountModal.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Monolithic overlay hosting multi-tabbed interface managing profile information, crew memberships, registration listings, and global configuration options.
- **Suggested Fix:** Refactor tab pages (ProfileTab, CrewzTab, SettingsTab) into separate files inside a subcomponents tab directory.

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 140
- **Rule:** R-08
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Bypass of strict TypeScript typing using raw 'any' array definitions for database structures, crew management data, and list items.
- **Suggested Fix:** Define robust, type-safe interfaces for Crews, Members, and Profiles, replacing all instances of 'any' with the specific types.

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 333
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Asynchronous Supabase storage and service calls are executed without try/catch wrapper blocks, risking unhandled promise rejections on network or backend failure.
- **Suggested Fix:** Wrap all async calls in try/catch blocks, show friendly error alerts to users, and log failure events via AppLogger.

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 317
- **Rule:** R-09
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** AppLogger logs the raw file name during avatar uploads, which may leak PII if the file name contains user-identifying info (e.g. John_Doe_avatar.png).
- **Suggested Fix:** Sanitize the filename by removing user names or extract only the file extension, logging it as a sanitized description.

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 360
- **Rule:** R-26
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Lack of loading state or button disabling on async mutations (crew creation, member additions, removals) allows duplicate execution if user double-taps buttons.
- **Suggested Fix:** Add a boolean loading state (e.g., isProcessing) and disable touchable controls while the operation is pending.

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 601
- **Rule:** R-28
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** FlatLists used for rendering crews and members lack key performance optimization attributes (initialNumToRender, windowSize), leading to rendering bottlenecks on large datasets.
- **Suggested Fix:** Add initialNumToRender={10}, windowSize={5}, and removeClippedSubviews={true} attributes to the FlatLists.

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 364
- **Rule:** R-20
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses binary Platform.OS check for web sign out redirect bypass instead of Platform.select.
- **Suggested Fix:** Use Platform.select logic or a structured platform helper.

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 369
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', err instanceof Error ? err : new Error(String(err)));

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 383
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', err instanceof Error ? err : new Error(String(err)));

### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- **Line:** 579
- **Rule:** R-20
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses ternary Platform.OS === 'ios' check to select 'Courier New' vs 'monospace' fontFamily.
- **Suggested Fix:** Use Platform.select({ ios: 'Courier New', default: 'monospace' })

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

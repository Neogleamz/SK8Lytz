# Implementation Plan: sweep-services-AuthProfileService.ts

## Goal
Fix static audit findings for the `sweep-services-AuthProfileService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [AuthProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line:** 26
- **Rule:** R-05
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Queries user profile from user_profiles table directly on boot via Supabase client. When offline, this query fails, preventing the user profile from being loaded or auto-created.
- **Suggested Fix:** Store user profile locally in AsyncStorage and retrieve it on boot. Revalidate in the background when a connection is available.

### [MODIFY] [AuthProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line:** 101
- **Rule:** R-05
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Performs user profile updates by calling Supabase client directly without write-through local caching or offline queueing, causing updates to fail completely when offline.
- **Suggested Fix:** Write profile updates directly to local cache (optimistic UI update) and queue a pending sync job in AsyncStorage to flush once online.

### [MODIFY] [AuthProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line:** 123
- **Rule:** R-05
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Queries session history directly from Supabase, causing the session history view to be empty or error out when offline.
- **Suggested Fix:** Cache session history list locally. Return cached sessions instantly, then fetch and merge new sessions from Supabase in the background.

### [MODIFY] [AuthProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line:** 68
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing standard e instanceof Error unwrapping in catch block (uses String(err) directly).
- **Suggested Fix:** Change to error: err instanceof Error ? err.message : String(err)

### [MODIFY] [AuthProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line:** 128
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing standard e instanceof Error unwrapping in catch block (uses String(err) directly).
- **Suggested Fix:** Change to error: err instanceof Error ? err.message : String(err)

### [MODIFY] [AuthProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line:** 188
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing standard e instanceof Error unwrapping in catch block (uses String(err) directly).
- **Suggested Fix:** Change to error: err instanceof Error ? err.message : String(err)

### [MODIFY] [AuthProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line:** 237
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Missing standard e instanceof Error unwrapping in catch block (uses String(err) directly).
- **Suggested Fix:** Change to error: err instanceof Error ? err.message : String(err)

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

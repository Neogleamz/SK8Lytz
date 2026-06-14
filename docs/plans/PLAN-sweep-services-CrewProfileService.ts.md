# Implementation Plan: sweep-services-CrewProfileService.ts

## Goal
Fix static audit findings for the `sweep-services-CrewProfileService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [CrewProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** CrewProfileService.ts is 606 lines and approximately 32KB, exceeding the 30KB monolith threshold.
- **Suggested Fix:** Decompose the file by extracting permanent crew membership logic and cache-refresh queries into dedicated helpers.

### [MODIFY] [CrewProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
- **Line:** 26
- **Rule:** R-15
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Bypasses React Auth Context by fetching user authentication status directly from Supabase auth APIs.
- **Suggested Fix:** Extract user details from UI React context and pass it to the profile service functions.

### [MODIFY] [CrewProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
- **Line:** 32
- **Rule:** R-05
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Queries crew memberships directly via Supabase. If offline, the call returns an empty list, displaying a blank crews tab to the user.
- **Suggested Fix:** Cache the user's permanent crew details in AsyncStorage. Load this cache first, then fetch fresh crews data from the database in the background.

### [MODIFY] [CrewProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
- **Line:** 315
- **Rule:** R-17
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Supabase Realtime channel subscription leak in deleteCrew. A temporary channel is subscribed to with broadcast config, and a timeout to remove the channel is registered ONLY if status is 'SUBSCRIBED'. If the subscription fails or does not reach 'SUBSCRIBED' state (e.g. offline status), the channel remains active in the Supabase Client's list of registered channels, leaking memory and network connections.
- **Suggested Fix:** Ensure the channel is removed even if the subscription status is not 'SUBSCRIBED', or register a timeout/error handler that cleans up tempChannel regardless of connection status.

### [MODIFY] [CrewProfileService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
- **Line:** 593
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

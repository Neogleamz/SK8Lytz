# Implementation Plan: Crew Hub Stability Patch

This plan addresses the critical bugs and logic gaps identified in the April 7th Crew Hub audit, focusing on JSX malformation, privacy leaks, and session matching failures.

## User Review Required

> [!CAUTION]
> **Privacy Leak**: The current `getNearbyPublicSessions()` query fetches ALL active sessions regardless of public/private status if RLS is not configured correctly. We must add the `.eq('is_public', true)` filter immediately to prevent private session discovery.

## Proposed Changes

### [UI Components]
Fixing the malformed JSX in the detail view.

#### [MODIFY] [CrewModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/CrewModal.tsx)
- **Fix JSX Stacking**: Move the closing Action `</View>` tag before the fragment `</>` close at line 1888.
- **Text Cleanup**: Fix "Invite & Add Sktaters" typo.
- **Session Matching**: Update `getLiveSessionForCrew` to match by `crewId` instead of `crew.name`.

---

### [Services & Database]
Hardening the session query logic.

#### [MODIFY] [LocationService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/services/LocationService.ts)
- Update `getNearbyPublicSessions()` to include `.eq('is_public', true)`.

#### [MODIFY] [CrewService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/services/CrewService.ts)
- Ensure `createSession()` takes a `crewId` argument and stores it in the `crew_sessions` table.

## Verification Plan

### Automated Tests
- `scratch/test-session-privacy.ts`: Query the nearby session endpoint with a mock private session in the DB and verify it is NOT returned.

### Manual Verification
1. Create a Private Crew.
2. Start a Session.
3. Open "Live Near You" on a second device (not a member).
4. Verify the session is NOT visible.

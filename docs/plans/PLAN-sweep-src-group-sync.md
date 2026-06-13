# Implementation Plan: sweep-src-group-sync

This is a synthesized sweep plan addressing all rule violations identified in the **GROUP_SYNC** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### GROUP_SYNC Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [CrewDetailScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewDetailScreen.tsx)
- **Line 24 [MEDIUM]:** Coordinates permanent crew management, stats display, members table, and member invitation flows. Reaches 32.2KB, violating R-23. (Rule: R-23)

#### [MODIFY] [CrewJoinScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewJoinScreen.tsx)
- **Line 40 [HIGH]:** Direct rendering of active sessions list without proper success/empty states (Rule: R-14)
- **Line 67 [MEDIUM]:** The 'styles' object is created at the top of the component using 'createStyles(Colors)' (line 17) and is passed as a dependency to the renderActiveSessionCard callback's useCallback dependencies (line 94). This invalidates the render item memoization on every render of the screen. (Rule: R-07)
- **Line 124 [HIGH]:** Inline arrow function passed to keyExtractor prop in FlatList. Since this screen is user-facing, this can lead to performance degradation or UI glitches. (Rule: R-28, R-07)

#### [MODIFY] [CrewLandingScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingScreen.tsx)
- **Line 19 [MEDIUM]:** timeAgo helper is duplicated across five screens in src/components/crew/, namely CrewControllerScreen.tsx, CrewCreateScreen.tsx, CrewDetailScreen.tsx, CrewLandingScreen.tsx, and CrewManageScreen.tsx. (Rule: R-21)
- **Line 27 [MEDIUM]:** Renders the list of user crews, active nearby live sessions, and the interactive map filters. Its size is 36.6KB, violating R-23. (Rule: R-23)
- **Line 87 [HIGH]:** Hub UI lack full loading, error, and empty screens/views for public active sessions. (Rule: R-14)

#### [MODIFY] [CrewManageScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewManageScreen.tsx)
- **Line 71 [HIGH]:** Catch variable 'e' is used without an 'instanceof Error' check. (Rule: R-06)

#### [MODIFY] [useCrewHub.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewHub.ts)
- **Line 19 [MEDIUM]:** AppLogger.warn logging AsyncStorage failure missing telemetry context (Rule: R-04)
- **Line 124 [MEDIUM]:** AppLogger.warn logging queries failure with double string parameters instead of context object (Rule: R-04)

#### [MODIFY] [useCrewManage.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
- **Line 92 [MEDIUM]:** PII Leak: user search query logged directly in AppLogger warning (Rule: R-09)

#### [MODIFY] [useCrewProximityRadar.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewProximityRadar.ts)
- **Line 131 [HIGH]:** Stale closure/effect trap: useEffect dependency includes non-reactive singleton field crewService.isInCrew (Rule: R-12)

#### [MODIFY] [useCrewSession.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewSession.ts)
- **Line 79 [HIGH]:** Direct, synchronous update to user_profiles table on session end via Supabase client, bypassing local sync queue mechanism. If the user is offline, this update is lost, and the user's lifetime stats (lifetime_distance_miles and lifetime_top_speed_mph) are never synced to the database. (Rule: R-05)

#### [MODIFY] [CrewProfileService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
- **Line 60 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 109 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 138 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 169 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 188 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 204 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 231 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 266 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 291 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 347 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 429 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 468 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 488 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 508 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 528 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 548 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 575 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 593 [MEDIUM]:** PII Leak: raw query search text (usernames/display names) logged directly in AppLogger warning (Rule: R-09)
- **Line 599 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)

#### [MODIFY] [CrewService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
- **Line 1 [HIGH]:** CrewService.ts exceeds the 30KB limit (approx 31,092 bytes) (Rule: R-23)
- **Line 59 [MEDIUM]:** Coordinates live skate sync sessions and crew database updates, reaching 31.1KB, violating R-23. (Rule: R-23)
- **Line 123 [LOW]:** Broad any cast in createSession catch block (Rule: R-06)
- **Line 125 [HIGH]:** AppLogger.error in createSession missing payload_size and ssi context (Rule: R-04)
- **Line 149 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 174 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 224 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 266 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 303 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 372 [MEDIUM]:** PII Leak: leaderName is logged in plain text in AppLogger (Rule: R-09)
- **Line 379 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 461 [MEDIUM]:** The Supabase delete operation chains a `.then` call but lacks any error callback or `.catch()` block. Unhandled rejections will leak if the network goes down or access token expires during session termination. (Rule: R-11)
- **Line 475 [LOW]:** Unawaited supabase.removeChannel returns a promise but has no catch handler attached. (Rule: R-11)
- **Line 488 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 504 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 532 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 554 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 572 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 657 [MEDIUM]:** In subscribeAsLeader, fetchMembers(sessionId) is called in the event callback with `.then` but no `.catch`. Rejections will leak into the Realtime socket handler loop. (Rule: R-11)
- **Line 689 [MEDIUM]:** Synchronous try-catch trap: AsyncStorage.multiRemove is wrapped inside a try-catch block but is not awaited. The try-catch block finishes synchronously, and if multiRemove rejects, the exception bypasses the catch block completely. (Rule: R-11)
- **Line 712 [HIGH]:** Type safety bypass using as any on supabase query object execution (Rule: R-08)
- **Line 741 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)
- **Line 757 [LOW]:** Unawaited supabase.removeChannel returns a promise but has no catch handler attached. (Rule: R-11)
- **Line 790 [LOW]:** Unawaited supabase.removeChannel returns a promise but has no catch handler attached. (Rule: R-11)

#### [MODIFY] [GroupRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Line 27 [HIGH]:** Use of as any type assertion to bypass compiler checks on class method dynamic resolution (Rule: R-08)
- **Line 46 [MEDIUM]:** AppLogger.warn on transaction write failure missing payload_size and ssi context (Rule: R-04)
- **Line 62 [HIGH]:** Use of as any type assertion to assign device configuration properties dynamically (Rule: R-08)
- **Line 80 [MEDIUM]:** Centralized key registry bypass. The key '@Sk8lytz_custom_groups' is registered in Master Reference and defined locally in GroupRepository.ts (as GROUPS_KEY) rather than being imported from storageKeys.ts. (Rule: R-24)
- **Line 227 [HIGH]:** Any array casting on retrieved database rows (Rule: R-08)
- **Line 275 [MEDIUM]:** Local key definition bypass. The key '@Sk8lytz_pending_group_sync' is defined locally as PENDING_GROUP_KEY in GroupRepository.ts, bypassing the centralized storageKeys.ts registry. (Rule: R-24)
- **Line 276 [MEDIUM]:** Missing standard e instanceof Error unwrapping in catch block for config merge (Rule: R-06, R-04)
- **Line 322 [MEDIUM]:** Missing standard e instanceof Error unwrapping in catch block for loadGroups (Rule: R-06, R-04)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

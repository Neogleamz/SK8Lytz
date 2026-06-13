# Implementation Plan: sweep-src-identity

This is a synthesized sweep plan addressing all rule violations identified in the **IDENTITY** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### IDENTITY Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [AccountTabDevices.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/AccountTabDevices.tsx)
- **Line 8 [LOW]:** formatDate helper is duplicated between AccountTabDevices.tsx and AccountTabProfile.tsx. (Rule: R-21)

#### [MODIFY] [AccountTabProfile.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/AccountTabProfile.tsx)
- **Line 8 [LOW]:** initials helper is duplicated in AccountTabProfile.tsx and CrewMemberDashboard.tsx. (Rule: R-21)

#### [MODIFY] [AccountTabStats.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/AccountTabStats.tsx)
- **Line 19 [LOW]:** fmtDuration helper function is duplicated here and in src/components/crew/CrewDetailScreen.tsx. Both functions format a duration in seconds to a string (e.g. '1h 23m' or '23m'). (Rule: R-21)

#### [MODIFY] [SkaterStatsPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/SkaterStatsPanel.tsx)
- **Line 36 [LOW]:** The local storage cache key '@sk8lytz_lifetime_stats_cache' is used for persisting lifetime statistics but is omitted from the AsyncStorage Key Registry in tools/SK8Lytz_App_Master_Reference.md. (Rule: R-24)

#### [MODIFY] [types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/types.ts)
- **Line 5 [MEDIUM]:** Circular dependency chain between AccountModal.tsx, AccountTab*.tsx files, and types.ts. types.ts imports StoredDevice from '../AccountModal' using a value import instead of a type import. This creates circular import dependencies in bundlers, increasing risk of undefined exports at runtime. (Rule: R-29)

#### [MODIFY] [AuthFormSignIn.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormSignIn.tsx)
- **Line 18 [MEDIUM]:** isValidEmail utility is duplicated across the registration and authentication forms, including AuthFormForgotPassword.tsx, AuthFormSignIn.tsx, and AuthFormSignUp.tsx. (Rule: R-21)

#### [MODIFY] [DevSandboxDrawer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/DevSandboxDrawer.tsx)
- **Line 31 [MEDIUM]:** Catch variable 'e' is used without an 'instanceof Error' check. It uses String(e) but does not unwrap instanceof Error. (Rule: R-06)
- **Line 86 [MEDIUM]:** Catch variable 'e' is used without an 'instanceof Error' check. It uses String(e) but does not unwrap instanceof Error. (Rule: R-06)
- **Line 104 [MEDIUM]:** Catch variable 'e' is used without an 'instanceof Error' check. It uses String(e) but does not unwrap instanceof Error. (Rule: R-06)

#### [MODIFY] [AuthContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Line 252 [HIGH]:** Catch variable 'e' is used without an 'instanceof Error' check. (Rule: R-06)

#### [MODIFY] [useAccountOverview.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAccountOverview.ts)
- **Line 178 [HIGH]:** AppLogger.error is called with telemetry parameters (payload_size, ssi) incorrectly nested within the errorObj parameter (second argument) instead of the context parameter (third argument). (Rule: R-04)
- **Line 221 [HIGH]:** AppLogger.error is called with telemetry parameters (payload_size, ssi) incorrectly nested within the errorObj parameter (second argument) instead of the context parameter (third argument). (Rule: R-04)
- **Line 232 [HIGH]:** Explicit cast to 'any' on caught exception 'e' in catch block, bypassing compiler type checks. (Rule: R-08)
- **Line 307 [HIGH]:** AppLogger.error is called with telemetry parameters (payload_size, ssi) incorrectly nested within the errorObj parameter (second argument) instead of the context parameter (third argument). (Rule: R-04)

#### [MODIFY] [AuthProfileService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts)
- **Line 79 [HIGH]:** AppLogger.error is called with telemetry parameters (payload_size, ssi) incorrectly passed inside the errorObj parameter (second argument) rather than the context parameter (third argument). This prevents the logging infrastructure from parsing the telemetry context. (Rule: R-04)
- **Line 155 [HIGH]:** AppLogger.error is called without required telemetry variables (payload_size, ssi) in the logging call. (Rule: R-04)

#### [MODIFY] [ProfileService.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ProfileService.types.ts)
- **Line 21 [HIGH]:** Explicit use of the forbidden 'any' type in UserProfile type definition for 'notif_preferences'. (Rule: R-08)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

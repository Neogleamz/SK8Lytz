# Implementation Plan: sweep-src-notifications-&-routing

This is a synthesized sweep plan addressing all rule violations identified in the **NOTIFICATIONS_&_ROUTING** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### NOTIFICATIONS_&_ROUTING Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [App.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/App.tsx)
- **Line 48 [MEDIUM]:** AppLogger.error is called to capture unhandled promise rejections on the web without providing the required telemetry context properties (payload_size and ssi). (Rule: R-04)

#### [MODIFY] [BluetoothGuard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/BluetoothGuard.tsx)
- **Line 43 [MEDIUM]:** Asynchronous event handler handleRequestPermission has try and finally blocks but lacks a catch block. If requestPermission throws an error, the exception will go unhandled and cause an unhandled promise rejection. (Rule: R-11)

#### [MODIFY] [ComplianceGate.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/providers/ComplianceGate.tsx)
- **Line 24 [MEDIUM]:** The useEffect calls the asynchronous function checkCompliance() when isOfflineMode changes, but lacks a re-entrancy or active-flag check. If isOfflineMode changes rapidly, network requests and state updates may race and resolve out of order. (Rule: R-26)
- **Line 108 [HIGH]:** AppLogger.error lacks context parameter (arguments count: 2) (Rule: R-04)

#### [MODIFY] [LocationService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Line 81 [MEDIUM]:** The method getSilentLocation relies on Location.getLastKnownPositionAsync which is not supported on the Web platform according to expo-location specifications. While the method catches errors and returns null, calling it on Web will always throw or warn. It should be skipped early when Platform.OS === 'web' to avoid unnecessary console warnings or performance overhead. (Rule: R-20)
- **Line 95 [LOW]:** Bypasses AppLogger telemetry context and formatting by using raw console.warn (Rule: R-04)
- **Line 122 [HIGH]:** getNearbyPublicSessions executes a database query (supabase.from('crew_sessions').select(...)) without a surrounding try/catch block. If the Supabase client experiences network failure, this call will throw an unhandled exception and crash the dashboard screen that calls it. (Rule: R-11)

#### [MODIFY] [NotificationService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/NotificationService.ts)
- **Line 53 [HIGH]:** Two notification services exist: src/services/NotificationService.ts (Expo push notifications) and src/services/session/NotificationService.ts (XState local active session notifications). While they serve different purposes, having the exact same name causes import confusion, split-brain logic for telemetry reporting, and potential runtime conflicts. (Rule: R-21)
- **Line 94 [HIGH]:** NotificationService.init() calls private methods `_wireForegroundHandler()` and `_wireResponseHandler()` to subscribe to push notification events, reassigning `this.foregroundSub` and `this.responseSub` without clearing pre-existing subscriptions first. If init() is called multiple times (e.g. during authentication transitions, retries, or component mount/unmount cycles), pre-existing event listeners will leak in memory as their references are overwritten and lost. (Rule: R-22)
- **Line 106 [MEDIUM]:** cleanup() performs an asynchronous database/network deletion without wrapping the call in a try/catch block. If the connection fails or token is not found, it throws an unhandled promise rejection, which is especially hazardous since cleanup is called within synchronous useEffect return statements. (Rule: R-11)
- **Line 113 [HIGH]:** The service calls deprecated profileService.unregisterPushToken(...) inside cleanup(). Under God Object Decomposition (Meal 1: ProfileService split), this should be routed through pushTokenService.unregisterPushToken(...) instead. (Rule: R-21)
- **Line 173 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.

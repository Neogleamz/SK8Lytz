# Implementation Plan: Micro-App Refactor (CrewModal Domain)

This plan details the extraction of the massive 2,200-line `CrewModal.tsx` into decoupled domain hooks and sub-components to improve maintainability and performance.

## Design Rationale
The `CrewModal` has become a "God Component." It handles authentication, real-time polling, GPS location picking, member management, and session orchestration. By extracting these into domain hooks (`useCrewHub`, `useCrewSession`), we can unit test the logic without rendering the complex UI, and allow for better memoization of the modal sub-screens.

## Proposed Changes

### [Hooks]
Extracting logic from the monolith.

#### [NEW] [useCrewHub.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/hooks/useCrewHub.ts)
- Handles mapping `myCrews`, `publicCrews`, and `loadingStates`.
- Logic for `join`, `leave`, and `delete` crew actions.

#### [NEW] [useCrewSession.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/hooks/useCrewSession.ts)
- Handles the real-time session subscription.
- GPS polling for nearby sessions.
- Scene broadcast logic.

### [UI Components]
Breaking down the main view.

#### [NEW] [CrewHubTabs.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/crews/CrewHubTabs.tsx)
- Isolated tab navigation logic.

#### [NEW] [CrewActionCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/crews/CrewActionCard.tsx)
- Unified card component for Start/Join/Schedule actions.

#### [MODIFY] [CrewModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/CrewModal.tsx)
- Strip out 80% of the internal functions and replace them with calls to the new hooks.
- Move sub-renders (`renderHub`, `renderDetail`, `renderCreate`) into their own files.

## Verification Plan

### Automated Tests
- Unit test `useCrewHub.ts` using `@testing-library/react-hooks` to verify state transitions during a mock Join/Leave flow.

### Manual Verification
1. Open Crew Hub.
2. Navigate tabs.
3. Start a Session.
4. Verify performance (FPS) is improved compared to the monolithic version.

# Implementation Plan: TTID & TTFD Performance Telemetry

## Goal
Implement industry-standard Time to Initial Display (TTID) and Time to Fully Drawn (TTFD) load time tracking using a reusable React Native hook, bypassing bulky 3rd-party APM SDKs. 

## Scope Strategy: Surgical "Heavy Screens"
We are explicitly rejecting global navigation middleware to prevent log flooding. We will surgically target the following core interaction screens and heavy components:

1. **DashboardScreen** (`src/screens/DashboardScreen.tsx`) — Core loop
2. **AuthScreen** (`src/screens/AuthScreen.tsx`) — Login/startup
3. **HardwareSetupWizardScreen** (`src/screens/Onboarding/HardwareSetupWizardScreen.tsx`) — Device pairing
4. **DockedController** (`src/components/DockedController.tsx`) — Heavy BLE interaction panel
5. **AccountModal** (`src/components/AccountModal.tsx`) — Heavy user settings fetch
6. **CrewMemberDashboard / DashboardCrewPanel** (`src/components/CrewMemberDashboard.tsx`) — Sync-heavy social features
7. **SkateSpotBottomSheet** (`src/components/SkateSpotBottomSheet.tsx`) — Maps/location loading

## Proposed Changes

### Telemetry Core
#### [MODIFY] [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- Update `EventType` union to include `SCREEN_LOAD_TTID` and `SCREEN_LOAD_TTFD`.
- Ensure these new events map correctly to the `NAVIGATION` breadcrumb category.

#### [NEW] [useScreenPerformance.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useScreenPerformance.ts)
- Create a reusable hook that captures `Date.now()` immediately on mount.
- Use `InteractionManager.runAfterInteractions` to measure when React Native completes the first full UI paint (TTID).
- Expose a `markFullyDrawn()` function so screens can explicitly signal when their API calls or async data finishes loading (TTFD).
- Dispatches `AppLogger.log` with the timings payload `{ screen: string, ttidMs: number, ttfdMs?: number }`.

### UI Implementation
#### [MODIFY] Multiple Screens
Inject `useScreenPerformance('ScreenName')` into the 7 heavy screens listed in the Scope Strategy. Hook the `markFullyDrawn()` call to fire when their respective primary data finishes loading (e.g., Supabase query resolves, BLE scan starts, Map finishes rendering).

## Out of Scope
- Rewriting the navigation router.
- Modifying React Navigation transitions.
- Adding Sentry, Datadog, or Firebase Performance Monitoring dependencies.

## Verification Plan
### Manual Verification
- Launch the app locally and navigate between the targeted heavy screens.
- Verify the terminal logs explicitly output `[AppLogger] SCREEN_LOAD_TTID`.
- Verify `SCREEN_LOAD_TTFD` fires post-render once the screen's state settles.

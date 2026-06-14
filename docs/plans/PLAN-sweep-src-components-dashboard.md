# Implementation Plan: sweep-src-components-dashboard

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-dashboard` domain cluster.

## Batch & Wave
- **Wave:** 4
- **Prerequisite:** Wave 3 fully merged

## Proposed Changes
### [MODIFY] SupportModal.tsx
- Line 41: Fix `R-06` violation. SupportModal utilizes raw console.warn for promise error catch blocks when triggering URL redirection. This bypasses the AppLogger subsystem and central logging metrics. (Suggested: Replace console.warn with AppLogger.warn or AppLogger.error calls.)
- Line 51: Fix `R-06` violation. Use of console.warn to capture link redirect errors on Visit Store CTA. (Suggested: Log the error telemetry using AppLogger.warn.)
- Line 61: Fix `R-06` violation. Use of console.warn to capture link redirect errors on Contact Support CTA. (Suggested: Log the error through the AppLogger telemetry pipeline.)
### [MODIFY] CrewHubSlab.tsx
- Line 23: Fix `R-08` violation. Type definition for 'appSettings' uses type 'Record<string, any>', which allows arbitrary types to bypass validation. (Suggested: Replace 'any' with the specific configuration value type schema, e.g. Record<string, string | boolean>.)
### [MODIFY] DashboardCrewPanel.tsx
- Line 16, 17: Fix `R-08` violation. Prop type definition 'lastLeaderScene' uses type 'Record<string, any> | null', introducing 'any' typing into component attributes. (Suggested: Use a strongly typed interface representing crew scenes.)
- Line 26: Fix `R-08` violation. Prop callback parameter 'onApplyCloudScene' is typed as 'any'. (Suggested: Declare proper typing for the cloud scene parameter.)
- Line 107: Fix `R-22` violation. The return cleanup function (unsubscribe) from crewService.subscribeAsLeader and subscribeAsMember is discarded. Although the service is a global singleton and manages its active channel subscription idempotently by clearing any existing channel when a new one is created, standard cleanup patterns should track and call these unsubscribe functions when the panel or session modal unmounts. (Suggested: Track the returned unsubscribe functions in a React ref (e.g., crewUnsubRef.current) and invoke the unsubscription when the component unmounts or when the session transitions.)
### [MODIFY] MySkatesSlab.tsx
- Line 20: Fix `R-08` violation. Prop type 'lastGroupPatterns' contains 'any' definition. (Suggested: Create a type definition mapping custom group IDs to their patterns.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

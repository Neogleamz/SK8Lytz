# Implementation Plan: sweep-src-components-DockedController.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-DockedController.tsx` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] DockedController.tsx
- Line 1: Fix `R-23` violation. The main component file DockedController.tsx is a monolith of approximately 70KB containing over 1500 lines of code, directly violating the 30KB safety constraint of R-23. Monoliths increase risk of merge conflicts and accidental regression during edits. (Suggested: Refactor DockedController.tsx by extracting sub-modules such as the community scene share modal, permissions management logic, and crew member/leader state handling into separate files.)
- Line 459: Fix `R-09` violation. Raw device MAC addresses are logged as 'deviceId' within the AppLogger warning payloads on write replays, violating PII redaction rules. (Suggested: Use normalizeMac() on d.id or mask/truncate the identifier before logging to telemetry.)
- Line 610: Fix `R-09` violation. PII key 'name' passed with unscrubbed value 'favRaw.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(favRaw.name) or log the unique ID instead of name.)
- Line 727, 729: Fix `R-09` violation. PII key 'name' passed with unscrubbed value 'favRaw.name || favRaw.customName' in AppLogger call. (Suggested: Wrap resource name in scrubPII(favRaw.name || favRaw.customName) or log the unique ID instead of name.)
- Line 1022: Fix `R-11` violation. Async permissions requests (e.g. checkPermission and openGlobalPermissionsModal) inside handleDockModeChange are not wrapped in try/catch blocks, which can lead to unhandled promise rejections if the native module fails. (Suggested: Wrap asynchronous permission requests and modal triggers in a try/catch block.)
- Line 152: Fix `R-08` violation. Type annotations in DockedController.tsx use 'any' rather than strict types or 'unknown'. (Suggested: Change 'any' type references to 'unknown' or specify complete interfaces to enforce TS compilation checks.)
- Line 199: Fix `R-27` violation. Component consumes 4 React Contexts directly/via direct wrappers: ThemeContext (via useTheme), AppConfigContext (via useAppConfig), FavoritesContext (via useSharedFavorites), and BLEContext (via useSharedBLE). This exceeds the maximum context consumption depth of 3, leading to potential performance regressions and tighter coupling. (Suggested: Consolidate context values, pass context values down as props from a parent container or wrapper component, or refactor state logic to reduce the direct dependency on 4 distinct contexts.)
- Line 233: Fix `R-27` violation. DockedController.tsx consumes 4 contexts directly (FavoritesContext, ThemeContext, AppConfigContext, BLEContext). This high depth of consumer dependencies increases re-render frequency on unrelated changes. (Suggested: Isolate context consumption into dedicated custom hooks or use a selector-based architecture to minimize the component's re-render footprint.)
- Line 468: Fix `R-16` violation. Raw setTimeout wrapped in a Promise is used inside replaySequentially to stagger status write updates to multiple devices on reconnection. (Suggested: Use BleWriteQueue.enqueueDelay to space out the sequential replay writes rather than introducing an unmanaged delay.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

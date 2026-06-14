# Implementation Plan: sweep-components-DockedController.tsx

## Goal
Fix static audit findings for the `sweep-components-DockedController.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** DockedController.tsx is 1,482 lines long (approx. 68.8 KB), which heavily exceeds the 30KB threshold for monoliths. It contains massive state management, layout rendering, permissions gating, and event dispatch logic, creating a high risk of collisions and code drift.
- **Suggested Fix:** Extract sub-components, animation states, and gesture navigators into smaller standalone components. Delegate state orchestration to a smaller hook or service controller.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 452
- **Rule:** R-09
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The 'macs' parameter logs an array of group MAC addresses (string[]) compiled from `devicesToReplay.map(x => x.id)`. Because it is an array of primitives, it bypasses the AppLogger recursive scrubber's `typeof obj[key] === 'string'` check, leaking all group MAC addresses to telemetry.
- **Suggested Fix:** Map the array using `scrubPII` for each item individually, or fix the recursive scrubber in `AppLogger.ts` to support arrays of primitives.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 192
- **Rule:** R-27
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** DockedController directly consumes 4 React contexts (useTheme, useAppConfig, useSharedFavorites, useSharedBLE) in addition to useWindowDimensions. This creates an excessive context dependency depth, causing DockedController to trigger expensive full re-renders on any minor update to theme, configs, or favorite records.
- **Suggested Fix:** Create a unified container hook or delegate context consumption to smaller sub-panels directly, keeping the main shell clean of context changes, or selectively memoize sub-trees using React.useMemo.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 449
- **Rule:** R-10
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Concurrent mapped write anti-pattern in state replay: devicesToReplay.forEach maps over devices and dispatches replayed write commands (parentWriteToDevice) concurrently, violating sequential write isolation. If multiple devices reconnect simultaneously, concurrent writes without sequence enforcement will overwhelm the queue.
- **Suggested Fix:** Refactor the devicesToReplay.forEach loop into a sequential for...of loop, and await each parentWriteToDevice call to ensure sequential execution. Consider adding a small delay between device dispatches if needed.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 638
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** In the loadFavorite routine, the camera permission check 'checkPermission('CAMERA').then(granted => { ... })' lacks a .catch() handler. If the permission request rejects or fails, it will cause an unhandled promise rejection in the React Native environment.
- **Suggested Fix:** Append a .catch(...) handler to the promise chain that safely logs the exception using AppLogger.error with telemetry context details.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 1221
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Multiple callbacks (e.g., handleMusicChange in MusicPanel, onEditFavorite in FavoritesPanel, onViewModeChange in BuilderPanel) are passed as inline arrow functions in render. Major handlers like handleConfirmSaveFavorite and handleSaveFavoriteClick are declared as plain arrow functions without useCallback, which breaks React.memo props-equality checks, causing constant re-renders.
- **Suggested Fix:** Wrap all callbacks and event handlers passed as props to child components inside useCallback hooks, and define inline callbacks statically or as stable references.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 237
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 430
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 450
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 482
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 537
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 548
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Line:** 644
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

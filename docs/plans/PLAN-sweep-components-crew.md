# Implementation Plan: sweep-components-crew

## Goal
Fix static audit findings for the `sweep-components-crew` domain cluster.

## Proposed Changes

### [MODIFY] [CrewDetailScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewDetailScreen.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Active crew member information details overlay showing active group scenes, connection status metrics, and speed tracking maps.
- **Suggested Fix:** Extract active telemetry widgets, crew details headers, and action panels into separate files within the crew folder.

### [MODIFY] [CrewDetailScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewDetailScreen.tsx)
- **Line:** 238
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Calls Clipboard.setStringAsync without await or catch block, allowing exceptions to trigger unhandled promise rejections.
- **Suggested Fix:** Wrap in a try-catch construct or handle error parameters explicitly.

### [MODIFY] [CrewJoinScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewJoinScreen.tsx)
- **Line:** 67
- **Rule:** R-07
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** FlatList renderItem (renderActiveSessionCard) contains an inline arrow function handler (onPress={() => handleJoinById(item.id)}). Furthermore, the renderItem callback depends on handleJoinById (not memoized in parent screen) and styles (recreated every render), causing the callback reference to change on every parent render.
- **Suggested Fix:** Wrap handleJoinById in a useCallback inside the parent component. Memoize styles using useMemo. Extract the individual session card into a memoized component (e.g., SessionCard) that takes handleJoinById as a prop and wraps its onPress handler in a local useCallback.

### [MODIFY] [CrewLandingMap.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingMap.tsx)
- **Line:** 50
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Directly imports react-native-maps which is unsupported and will crash on web environments.
- **Suggested Fix:** Use Platform.select() or platform-specific extensions (.web.tsx) to isolate standard mobile map controls.

### [MODIFY] [CrewLandingScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingScreen.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** User crew dashboard panel managing local crew session listings, map visualizations, filter controls, and invite sharing panels.
- **Suggested Fix:** Split the crew landing screen's filter logic, maps viewer, and list renderer into smaller modular screen sub-components.

### [MODIFY] [CrewLandingScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingScreen.tsx)
- **Line:** 98
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Async handlers (like location detection and search triggers) lack UI-level double-tap protection, allowing concurrent execution.
- **Suggested Fix:** Implement dynamic loading state gates to disable the buttons while async operations are unresolved.

### [MODIFY] [CrewManageScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewManageScreen.tsx)
- **Line:** 34
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Invokes ImagePicker.launchImageLibraryAsync asynchronously without enclosing error handlers.
- **Suggested Fix:** Wrap the async call in a try/catch structure to handle device cancellations or permission rejections safely.

### [MODIFY] [CrewManageScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewManageScreen.tsx)
- **Line:** 71
- **Rule:** R-06
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Catch block uses unsafe type cast 'e as Error' to unwrap the catch variable. If the caught variable is not an instance of Error, this can lead to runtime issues or empty error messages.
- **Suggested Fix:** Replace type cast with standard runtime type check:

catch (e: unknown) {
  setCreateCrewError(e instanceof Error ? e.message : 'Failed to create crew');
}

### [MODIFY] [CrewJoinScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewJoinScreen.tsx)
- **Line:** 124
- **Rule:** R-28
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Inline arrow function 's => s.id' passed to keyExtractor prop in FlatList.
- **Suggested Fix:** Extract the keyExtractor to a memoized function using useCallback or define it as a stable static function outside the component: const keyExtractor = useCallback((item: CrewSession) => item.id, []); and reference it as keyExtractor={keyExtractor}.

### [MODIFY] [CrewLandingScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingScreen.tsx)
- **Line:** 230
- **Rule:** R-07
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Defines renderItem as an inline arrow function in FlatList instead of wrapping it in useCallback.
- **Suggested Fix:** Move the renderItem callback outside of the component body or wrap it in a useCallback hook.

### [MODIFY] [CrewControllerScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewControllerScreen.tsx)
- **Line:** 39
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

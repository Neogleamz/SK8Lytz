# Implementation Plan: sweep-screens-DashboardScreen.tsx

## Goal
Fix static audit findings for the `sweep-screens-DashboardScreen.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Monolithic screen file size is 54.6KB, which significantly exceeds the 30KB threshold for screen/component files. Needs extraction of subcomponents or helper functions.
- **Suggested Fix:** Extract custom modals (GroupSettingsModal, AccountModal) and logic blocks into dedicated component files.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 84
- **Rule:** R-27
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** DashboardScreen directly consumes 4 React contexts (AppConfigContext, ThemeContext, BLEContext, and SessionContext) via useAppConfig(), useTheme(), React.useContext(BLEContext), and useSession() hooks. This exceeds the maximum context depth threshold, coupling the root screen component to multiple global state flows.
- **Suggested Fix:** Extract sub-flows into separate child components that consume their respective contexts directly (e.g., let LiveTelemetryHUD consume useSession() directly, and header components consume useTheme() directly) to avoid root-level rendering cascades on every context change.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 600
- **Rule:** R-09
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The 'expectedMacs' parameter logs an array of group MAC addresses (string[]) from `group.deviceIds`. Because it is an array of primitives, it bypasses the AppLogger recursive scrubber's `typeof obj[key] === 'string'` check, leaking all group MAC addresses to telemetry.
- **Suggested Fix:** Map the array using `scrubPII` for each item individually, or fix the recursive scrubber in `AppLogger.ts` to support arrays of primitives.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 648
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Uses hardcoded string '@Sk8lytz_Favorites' for AsyncStorage rather than referencing a centralized registry key or constant.
- **Suggested Fix:** Reference @Sk8lytz_Favorites from a centralized key registry constant to avoid collision risks.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 724
- **Rule:** R-12
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The edgePanResponder is created once on mount using useRef(PanResponder.create(...)).current. Its handlers onPanResponderRelease and onPanResponderTerminate capture React state variables (isTestModeActive, isActuallyConnected, isSkateSessionActive, and bleState) which form stale closures over their initial values. Consequently, gesture-based actions like edge-swiping to close the visualizer screen fail once connection/session states update.
- **Suggested Fix:** Use useRef for all dynamic state variables used in the PanResponder callbacks, updating their values in a useEffect hook. Then, access their values inside the callbacks using the .current property.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 856
- **Rule:** R-07
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The FlatList renderItem callback creates a new dynamic mergedItem object on every invocation. This invalidates the shallow prop comparison of the child component MemoizedDeviceItem (which is wrapped in React.memo), rendering its memoization completely ineffective and causing unnecessary component re-renders.
- **Suggested Fix:** Memoize the mapping logic using useMemo for all fleet/registered items or within the item component itself, rather than reconstructing the device settings object inline inside the renderItem callback. Also, pull the style={{ paddingHorizontal: Layout.padding }} out into a static stylesheet.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 928
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Async helper handlePowerToggle performs asynchronous operations (BLE writes) without checking/setting a re-entrancy blocking lock, which can result in race conditions if tapped rapidly.
- **Suggested Fix:** Implement an _isFlushing ref or boolean locking guard inside handlePowerToggle to reject concurrent executions until the current toggle completes.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 100
- **Rule:** R-27
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Component directly consumes more than 4 React contexts (useTheme, useAuth, useBLE, useAppConfig, useFavorites), causing unnecessary re-renders on any context value change.
- **Suggested Fix:** Consolidate BLE and Session tracking contexts, or wrap child elements in useMemo to prevent deep rendering trees from recalculating on context updates.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 243
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Double casts partial DisplayDevice slice back to full DisplayDevice array, laundering partial attributes.
- **Suggested Fix:** Map values explicitly or adjust hook interface requirements.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 519
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders state update types back and forth between BLEDeviceMinimal[] and Device[] to force setter compatibility.
- **Suggested Fix:** Refactor hardware callback interface to take general minimal types, avoiding inline type conversion casting.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 813
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders BLE Device[] array to custom DisplayDevice[] array.
- **Suggested Fix:** Ensure types align or write mapper function mapping device properties correctly.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 952
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders allDevices to DisplayDevice[] inside registration completion callback.
- **Suggested Fix:** Pass clean mapper outputs instead of casting state arrays.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 1082
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders RegisteredDevice[] list to DisplayDevice[] list.
- **Suggested Fix:** Map registered items to display items explicitly.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 1128
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Launders selected device settings object to custom settings type.
- **Suggested Fix:** Derive settings object format safely or use defined default variables.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 344
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 350
- **Rule:** R-04, R-24
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Direct use of string literal key '@Sk8lytz_crewHubCollapsed' in AsyncStorage calls, bypassing central registry.
- **Suggested Fix:** Move to storageKeys.ts and import.

### [MODIFY] [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Line:** 390
- **Rule:** R-20
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses binary Platform.OS check to return early on Web and bypass sweep lifecycle instead of Platform.select.
- **Suggested Fix:** Use Platform.select logic for clean control flow.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

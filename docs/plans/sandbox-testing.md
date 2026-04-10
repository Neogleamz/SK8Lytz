# Establish Dev Sandbox & Dashboard Teardown

Implementing the final phase of the device registration epic by formally establishing the dev sandbox and clearing stale mockup logic out of the production UI layer.

### Design Decisions & Rationale
We are fully shifting all sandbox simulation logic down to the foundational layer within `useBLE.ts`. This ensures our UI (`DashboardScreen.tsx` and others) operates completely blindly—it will render and respond identically whether the underlying Bluetooth environment is native physical hardware or simulated web mocks. This achieves zero-friction cross-platform parity and significantly sanitizes the production component code by removing scattered `mockConnected` flags and web-specific conditional renders. The Nuke functionality has already been migrated cleanly to Auth for secure offline footprint wiping.

### UI & Platform Strategy
By tearing down `IS_BROWSER_DEMO` constants and `mockConnectedGroup` state variables inside the `DashboardScreen`, the frontend logic becomes 100% platform-agnostic. The UI will naturally respond to the `connectedDevices` array provided by `useBLE`. If the platform is Web (`Platform.OS === 'web'`), `useBLE` will automatically inject simulated objects into that array, allowing the UI to effortlessly scale and adapt responsively across Native iOS/Android and Web environments without requiring any platform-specific hacks in the rendering tree.

## Proposed Changes

### Dashboard Mock Cleanup

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- Remove `mockConnected`, `mockConnectedDevice`, `mockConnectedGroup` `useState` declarations.
- Strip all conditional logic branching off `IS_BROWSER_DEMO` across `handleDisconnect`, `toggleGlobalPower`, and device selection loops.
- Simplify the `displayConnectedDevices` `useMemo` hook to solely process elements from `connectedDevices` mapped against local storage `deviceConfigs`, deleting the branch that simulated group structures via `customGroups`.
- Ensure all click handlers directly call the native `useBLE` equivalents like `connectToDevice`, which natively handles simulation states.

## Verification Plan

### Manual Verification
1. Access the web sandbox environment (`npm run dev`).
2. Navigate to the Hardware Setup wizard.
3. Observe the `sim-soul-xx` devices appearing natively in the discovery pool.
4. Execute the FTUE flow and verify the Dashboard automatically connects and routes data back to the mock devices using the completely cleaned logic path.

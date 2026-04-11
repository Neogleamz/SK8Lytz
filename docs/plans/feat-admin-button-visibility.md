# Fix Admin Tools Visibility & Regression

This plan resolves the regression where `LogViewerModal` was accidentally removed from `DashboardScreen.tsx`, and implements the requested visibility rules for the Dev Sandbox.

## Root Cause Analysis
During a previous refactor, the `<LogViewerModal>` (Admin Tools) component was accidentally deleted from the `DashboardScreen.tsx` render tree. Concurrently, the 10-tap header logo button was wired directly to the `Sk8LytzDiagnosticLab`, completely bypassing the main Admin menu (which houses Stats, Timeline, Devices, and the new Scheduler).

## Proposed Changes

### `src/screens/DashboardScreen.tsx`
- **[DELETE]** The 10-tap Logo Easter Egg (`logoClickCount`, `handleLogoClick`, `isPasscodeVisible`, and the Passcode Modal).
- **[NEW]** Add state: `const [isSandboxMode, setIsSandboxMode] = useState(false);` and `const [isLogViewerVisible, setIsLogViewerVisible] = useState(false);`.
- **[NEW]** Add a `useEffect` to read `@Sk8lytz_demo_mode` from AsyncStorage on mount and set `isSandboxMode`.
- **[NEW]** Add a single "Admin Tools" button at the very bottom of the Dashboard `ScrollView`, conditionally rendered ONLY when `isSandboxMode` is true. 
- **[NEW]** Restore the missing `<LogViewerModal>` component at the bottom of the screen alongside the other Modals, wired correctly to the `LogViewerModalProps`.

## Verification Plan
1. Open the App in regular mode. Verify no Admin button exists at the bottom, and tapping the logo 10 times does nothing.
2. Go to `Account Modal` -> Sign out to hit `AuthScreen`.
3. Enable "Dev Sandbox". Log back in.
4. Verify an "Admin Tools" button now appears at the bottom of the Dashboard.
5. Click it and verify the full Log Viewer (Admin Tools menu) opens, giving access to the Picks Scheduler and hardware logs.

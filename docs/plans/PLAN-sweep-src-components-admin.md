# Implementation Plan: sweep-src-components-admin

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-admin` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] AdminToolsModal.tsx
- Line 341: Fix `R-25` violation. Icon action buttons (download, cloud upload, delete sweep, close) and navigation tab buttons lack accessibility labels or hints, rendering the admin telemetry panel unusable for screen-reader users. (Suggested: Add accessibilityRole="button" and clear accessibilityLabel definitions for each action icon button.)
### [MODIFY] AdvancedHardwareModal.tsx
- Line 59: Fix `R-11` violation. The asynchronous dispatch.writeSettingsByName call is awaited inside an async function without an enclosing try/catch block. A failure in BLE write operations will lead to an unhandled promise rejection. (Suggested: Enclose the await statement in a try/catch block and handle failures by displaying error notifications or logging to AppLogger.)
### [MODIFY] DeviceTab.tsx
- Line 18: Fix `R-08` violation. Uses 'any' in deviceConfigs prop type definition, bypassing TypeScript safety. (Suggested: Replace 'any' with a defined type like DeviceSettings from dashboard types.)
- Line 24: Fix `R-08` violation. Uses 'any' in Map typing: Map<string, any>. (Suggested: Change Map typing to Map<string, unknown> or map to specific custom metadata structures.)
### [MODIFY] AdminPicksScheduler.tsx
- Line 193: Fix `R-08` violation. Event parameter in onDateChanged is typed as 'any'. (Suggested: Type the event parameter with the appropriate datepicker event type or use 'unknown'.)
- Line 149: Fix `R-03` violation. Updates picks state after asynchronous network queries without checking if the component is still mounted. (Suggested: Introduce an isMountedRef check before updating component state.)
- Line 105: Fix `R-04` violation. AppLogger.error call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.error(message, errorObj, { ...context, payload_size: 0, ssi: 0 });)
- Line 126: Fix `R-04` violation. AppLogger.error call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.error(message, errorObj, { ...context, payload_size: 0, ssi: 0 });)
- Line 172: Fix `R-04` violation. AppLogger.error call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.error(message, errorObj, { ...context, payload_size: 0, ssi: 0 });)
### [MODIFY] Sk8LytzDiagnosticLab.tsx
- Line 57: Fix `R-08` violation. allDevices prop is typed as 'any[]', removing compile-time checks. (Suggested: Replace with the correct ScannedDevice[] or Device[] type definition.)
### [MODIFY] UserManagementPanel.tsx
- Line 158: Fix `R-03` violation. Multiple async callbacks update React state directly without confirming if the component is still mounted. (Suggested: Introduce an isMountedRef using useRef(true) with a useEffect cleanup, and verify isMountedRef.current is true before modifying component state.)
- Line 161: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
- Line 385: Fix `R-07, R-28` violation. An inline arrow function is passed directly to the `keyExtractor` prop of `FlatList`. This causes the function to be recreated on every render of the `UserManagementPanel` component, invalidating FlatList rendering optimizations. (Suggested: Extract the key extractor to a memoized callback using `useCallback` or define it as a static function outside the component scope: `const keyExtractor = useCallback((i: AdminUserProfile) => i.user_id, []);`)
### [MODIFY] AdminAuditLogViewer.tsx
- Line 76: Fix `R-03` violation. Updates logs and other UI states after asynchronous network queries without checking if the component is still mounted. (Suggested: Introduce an isMountedRef check before updating component state.)
### [MODIFY] AdminRosterPanel.tsx
- Line 103: Fix `R-03` violation. Updates roster states after asynchronous database queries without checking if the component is still mounted. (Suggested: Introduce an isMountedRef check before updating component state.)
- Line 106: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
### [MODIFY] FeatureFlagsPanel.tsx
- Line 121: Fix `R-03` violation. Updates feature flags state after asynchronous database queries without checking if the component is still mounted. (Suggested: Introduce an isMountedRef check before updating component state.)
- Line 123: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
- Line 351: Fix `R-20` violation. Uses hardcoded 'monospace' font family for feature flag keys without a fallback fallback for iOS devices. (Suggested: fontFamily: Platform.select({ ios: 'Courier', default: 'monospace' }))
### [MODIFY] GlobalAnalyticsPanel.tsx
- Line 28: Fix `R-03` violation. Updates data and error states after asynchronous RPC calls without checking if the component is still mounted. (Suggested: Introduce an isMountedRef check before updating component state.)
### [MODIFY] HardwareBlacklistPanel.tsx
- Line 112: Fix `R-03` violation. Updates hardware blacklist array after database queries without checking if the component is still mounted. (Suggested: Introduce an isMountedRef check before updating component state.)
- Line 114: Fix `R-06` violation. Passed raw exception variable "e" directly to logging call "AppLogger.error". (Suggested: Wrap the exception object or convert using a ternary: "e instanceof Error ? e.message : String(e)")
- Line 333: Fix `R-20` violation. Hardcodes 'monospace' font family on style definitions, ignoring iOS system defaults. (Suggested: fontFamily: Platform.select({ ios: 'Courier', default: 'monospace' }))
### [MODIFY] Sk8LytzProgrammer.tsx
- Line 213: Fix `R-09` violation. PII key 'deviceId' passed with unscrubbed value 'id' in AppLogger call. (Suggested: Wrap value in scrubPII(id) if it contains sensitive user information.)
- Line 202, 206: Fix `R-16` violation. Raw delay-based timing (wrapping setTimeout via delay helper) is used to let the GATT state settle after connecting, bypassing the centralized queue-managed BLE dispatcher. (Suggested: Refactor the batch programmer loop to enqueue operations or delays using BleWriteQueue.enqueueDelay instead of direct Promise-wrapped setTimeouts.)
- Line 210: Fix `R-16` violation. Raw delay-based timing (wrapping setTimeout via delay helper) is used to insert a gap between disconnecting one device and processing the next device in the batch programming loop. (Suggested: Incorporate the disconnect gap directly into the queue-managed loop or use BleWriteQueue.enqueueDelay to serialize the connection intervals.)
### [MODIFY] ProductManager.tsx
- Line 220: Fix `R-14` violation. The ProductManager component displays and edits a list of product configurations but lacks any UI-level loading, error, or empty states. It relies on a saving boolean flag only for submission. (Suggested: Introduce a ViewState state enum ('loading' | 'error' | 'empty' | 'success'). Render ActivityIndicator while loading, ErrorCard on fetch failure, and EmptyState when the product list is empty.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.

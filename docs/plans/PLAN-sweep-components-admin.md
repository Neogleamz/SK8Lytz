# Implementation Plan: sweep-components-admin

## Goal
Fix static audit findings for the `sweep-components-admin` domain cluster.

## Proposed Changes

### [MODIFY] [AdvancedHardwareModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdvancedHardwareModal.tsx)
- **Line:** 81
- **Rule:** R-09
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Bluetooth MAC addresses are classified as hardware/physical PII and are displayed fully unmasked in the UI modal header.
- **Suggested Fix:** Mask the displayed MAC address, only exposing the last 4 characters: targetDeviceId ? 'Device ID: ...' + targetDeviceId.slice(-4) : 'No Device Selected'.

### [MODIFY] [AdvancedHardwareModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdvancedHardwareModal.tsx)
- **Line:** 100
- **Rule:** R-19
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Slider allows selecting LED Points as low as 1. Constructing Colorful mode commands with less than 12 pixels triggers physical EEPROM chip locks on the 0xA3 chipset.
- **Suggested Fix:** Enforce minimumValue={12} or perform a validation check in handleWriteEEPROM that denies flashing counts under 12.

### [MODIFY] [AdminPicksScheduler.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminPicksScheduler.tsx)
- **Line:** 9
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Imports SafeAreaView from 'react-native' instead of 'react-native-safe-area-context', causing layout issues and inconsistent padding/insets on Android and Web platforms.
- **Suggested Fix:** Remove SafeAreaView from the 'react-native' import and instead import it from 'react-native-safe-area-context'.

### [MODIFY] [AdminRosterPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminRosterPanel.tsx)
- **Line:** 180
- **Rule:** R-14
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** AdminRosterPanel queries admin profile data from Supabase but only handles 'loading' status and successful states in its render method. The 'error' status is set in catch blocks, but it is completely ignored during UI rendering. This leads to a blank screen or the ListEmptyComponent showing a misleading 'No admins found.' message if fetching fails, with no retry mechanism provided to the user.
- **Suggested Fix:** Introduce an explicit check for the 'error' status and display the standard ErrorCard or an error message with a retry button, such as:

        {status === 'loading' ? (
          <ActivityIndicator size="large" color="#FFD700" style={{ marginTop: Spacing.xl }} />
        ) : status === 'error' ? (
          <ErrorCard message="Failed to load roster. Tap to retry." onRetry={fetchAdmins} />
        ) : (
          <FlatList ... />
        )}

### [MODIFY] [Sk8LytzDiagnosticLab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzDiagnosticLab.tsx)
- **Line:** 23
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Imports SafeAreaView from 'react-native' instead of 'react-native-safe-area-context', causing unsafe area layout issues on non-iOS platforms (Android/Web).
- **Suggested Fix:** Remove SafeAreaView from the 'react-native' import and import it from 'react-native-safe-area-context'.

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 6
- **Rule:** R-20
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Imports SafeAreaView from 'react-native' instead of 'react-native-safe-area-context', creating unsafe layouts on Android and Web.
- **Suggested Fix:** Import SafeAreaView from 'react-native-safe-area-context' instead of 'react-native'.

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 135
- **Rule:** R-19
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Programming profile validation allows LED points configuration down to 1. Flashing segments shorter than 12 pixels causes lockouts on active controllers.
- **Suggested Fix:** Change the lower bound threshold to 12: const v = Math.max(12, Math.min(300, ...));

### [MODIFY] [AdminPicksScheduler.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminPicksScheduler.tsx)
- **Line:** 102
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Database exceptions during Admin Picks modifications (create, delete, toggle) are shown in UI alerts but completely bypass error telemetry logging.
- **Suggested Fix:** Log the error details via AppLogger.error() inside the catch block to enable remote diagnostics.

### [MODIFY] [AdminPicksScheduler.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminPicksScheduler.tsx)
- **Line:** 348
- **Rule:** R-20
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Ternary platform check Platform.OS === 'ios' used for date picker overlays and visibility checks, ignoring web or other platforms and bypassing Platform.select().
- **Suggested Fix:** Use Platform.select({ ios: styles.iosDatePickerOverlay, default: {} }) to clearly delineate cross-platform styling.

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 153
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Hardcoded AsyncStorage key 'ng_programmer_profiles' used for legacy migration checks instead of utilizing the centralized storage keys registry.
- **Suggested Fix:** Migrate the legacy key string to storageKeys.ts and refer to it as an exported constant.

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 510
- **Rule:** R-20
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Uses binary Platform.OS === 'web' check to choose styling shadows, assuming all other platforms support iOS/Android shadow layout constructs.
- **Suggested Fix:** Use Platform.select with a clean default fallback object.

### [MODIFY] [DiagnosticLabBuilderTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Admin developer pattern builder editor allowing creation, compilation, and live transmission of custom LED effects.
- **Suggested Fix:** Extract custom effects creator builder logic, visualizer components, and slider configuration tables into isolated builder tabs.

### [MODIFY] [DiagnosticLabOracleTab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx)
- **Line:** 1
- **Rule:** R-23
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Developer utility diagnostics panel tab. Integrates commands log lists, custom command transmission blocks, and opcode testing validators.
- **Suggested Fix:** Modularize diagnostic helper components like quick grid color triggers, live test logs console list, and firmware badge widgets.

### [MODIFY] [UserManagementPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/UserManagementPanel.tsx)
- **Line:** 384
- **Rule:** R-28
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Inline arrow function '(i) => i.user_id' passed to keyExtractor prop in FlatList.
- **Suggested Fix:** Extract the keyExtractor to a memoized function using useCallback or define it as a stable static function outside the component: const keyExtractor = useCallback((item: AdminUserProfile) => item.user_id, []); and reference it as keyExtractor={keyExtractor}.

### [MODIFY] [AdminAuditLogViewer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminAuditLogViewer.tsx)
- **Line:** 121
- **Rule:** R-07
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** FlatList renderItem callback performs inline dynamic style array allocations every time the log entry includes a reason.
- **Suggested Fix:** Pre-calculate or define style rules in StyleSheet, or dynamically set values without recreating style arrays.

### [MODIFY] [Sk8LytzDiagnosticLab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzDiagnosticLab.tsx)
- **Line:** 411
- **Rule:** R-20
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses ternary Platform.OS === 'ios' check for choosing monospace fontFamily ('Menlo' vs 'monospace') instead of Platform.select().
- **Suggested Fix:** Use Platform.select({ ios: 'Menlo', default: 'monospace' })

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 160
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', e instanceof Error ? e : new Error(String(e)));

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 176
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', e instanceof Error ? e : new Error(String(e)));

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 201
- **Rule:** R-16
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Hardcoded raw setTimeout delays (using delay wrapper) used to stagger connection setup (FLASH_SETTLE_MS) during batch programming.
- **Suggested Fix:** Incorporate the settle delay directly into a queue-managed loop or refactor the batch flash command to use a command queue instead of UI-layer raw setTimeouts.

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 207
- **Rule:** R-16
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Hardcoded raw setTimeout delays (using delay wrapper) used to stagger disconnect gap (FLASH_DISCONNECT_GAP_MS) during batch programming.
- **Suggested Fix:** Incorporate the disconnect gap delay directly into a queue-managed loop or refactor the batch flash command to use a command queue instead of UI-layer raw setTimeouts.

### [MODIFY] [Sk8LytzProgrammer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/Sk8LytzProgrammer.tsx)
- **Line:** 410
- **Rule:** R-20
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses binary Platform.OS check to select 'Menlo' vs 'monospace' fontFamily for devices list.
- **Suggested Fix:** Use Platform.select({ ios: 'Menlo', default: 'monospace' })

### [MODIFY] [UserManagementPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/UserManagementPanel.tsx)
- **Line:** 160
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', e instanceof Error ? e : new Error(String(e)));

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

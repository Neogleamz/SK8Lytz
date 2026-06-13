# Implementation Plan: BATCH:sweep-components-admin

## Proposed Changes

### Domain: components-admin

#### [MODIFY] src/components/admin/tools/AdminAuditLogViewer.tsx
- Line 70 (R-08): Direct type assertion casting Supabase data to AuditLogEntry[].
- Line 168 (R-07): Performance Guardrails: Inline onRefresh and ListEmptyComponent functions in FlatList.
- Line 96 (R-04): Error logged without payload_size or ssi context
- Line 121 (R-07): Inline style object in FlatList renderItem.
- Line 55 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/admin/tools/UserManagementPanel.tsx
- Line 270 (R-07): Inline functions in FlatList renderItem causing full list re-renders.
- Line 51 (R-18): Boolean Traps. Scattered loading/error/isRefreshing booleans instead of unified state matrix.
- Line 73 (R-04): Error logged without payload_size or ssi context
- Line 270 (R-07): Multiple inline arrow functions in onPress inside FlatList renderItem.
- Line 54 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/admin/tools/AdminRosterPanel.tsx
- Line 61 (R-04): Error logged without payload_size or ssi context
- Line 118 (R-07): Inline arrow function in onPress inside FlatList renderItem.
- Line 47 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/admin/tools/FeatureFlagsPanel.tsx
- Line 68 (R-04): Error logged without payload_size or ssi context
- Line 173 (R-07): Inline arrow function in onPress inside FlatList renderItem.

#### [MODIFY] src/components/admin/tools/GlobalAnalyticsPanel.tsx
- Line 28 (R-04): Error logged without payload_size or ssi context
- Line 26 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.

#### [MODIFY] src/components/admin/tools/HardwareBlacklistPanel.tsx
- Line 66 (R-04): Error logged without payload_size or ssi context
- Line 165 (R-07): Inline arrow function in onPress inside FlatList renderItem.

#### [MODIFY] src/components/admin/tools/Sk8LytzProgrammer.tsx
- Line 157 (R-04): Error logged without payload_size or ssi context
- Line 171 (R-04): Error logged without payload_size or ssi context
- Line 501 (R-08): Type laundering anti-pattern detected: Unsafe cast bypassing TypeScript compiler.
- Line 102 (R-18): Single boolean state which may need FSM refactoring if complexity grows
- Line 143 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

#### [MODIFY] src/components/admin/AdminToolsModal.tsx
- Line 163 (R-07): Inline style object in FlatList renderItem.
- Line 105 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

#### [MODIFY] src/components/admin/tools/tabs/DiagnosticLabSnifferTab.tsx
- Line 21 (R-07): Multiple inline style objects in FlatList renderItem.

#### [MODIFY] src/components/admin/tools/Sk8LytzDiagnosticLab.tsx
- Line 99 (R-14): Missing isLoading and error state handling for useRegistration hook.

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\components\admin\tools\Sk8LytzProgrammer.tsx
- Line 195 (R-16): Hardcoded delay using setTimeout detected
- Line 198 (R-16): Hardcoded delay using setTimeout detected
- Line 201 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] src/components/admin/tools/AdminPicksScheduler.tsx
- Line 43 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/components/admin/tools/ProductManager.tsx
- Line 4 (R-20): Blind cross-platform assumption: Importing `SafeAreaView` from `react-native` instead of `react-native-safe-area-context`. The core `SafeAreaView` only pads for iOS notches and ignores Android entirely.

#### [MODIFY] src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx
- Line 1 (R-23): File exceeds 30KB limit (42245 bytes) - flag for mandatory component extraction

#### [MODIFY] src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx
- Line 1 (R-23): File exceeds 30KB limit (48180 bytes) - flag for mandatory component extraction

# Implementation Plan: Fix Admin Modal & Account Modal Safe Areas

## User Review Required
No breaking changes.

## Open Questions
None.

## Goal Description
Migrate all remaining legacy `SafeAreaView` imports in the Admin Tools suite and EulaModal to `react-native-safe-area-context`, and update `AccountModal` to use `useSafeAreaInsets` for its absolute positioned close button. This resolves UI notch-bleeding on Android devices where the OS status bar overlaps app UI elements.

## Proposed Changes

### Admin & Legal Components
Migrate `import { ..., SafeAreaView, ... } from 'react-native'` to `import { SafeAreaView } from 'react-native-safe-area-context'`.

#### [MODIFY] [AdminToolsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
#### [MODIFY] [AppManager.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AppManager.tsx)
#### [MODIFY] [AdminRosterPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminRosterPanel.tsx)
#### [MODIFY] [AdminAuditLogViewer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminAuditLogViewer.tsx)
#### [MODIFY] [HardwareBlacklistPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/HardwareBlacklistPanel.tsx)
#### [MODIFY] [FeatureFlagsPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/FeatureFlagsPanel.tsx)
#### [MODIFY] [UserManagementPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/UserManagementPanel.tsx)
#### [MODIFY] [EulaModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/modals/EulaModal.tsx)

### Account Modal
#### [MODIFY] [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
- Import `useSafeAreaInsets` from `react-native-safe-area-context`.
- Update the `closeBtn` inline style to include `top: Math.max(insets.top + 16, 16)`.

## Verification Plan
### Automated Tests
- `npm run verify` in the worktree.
- AST compilation check and test suite passing.

### Manual Verification
- Open Admin tools and confirm the modal header sits beneath the notch padding.
- Open Account Modal and confirm the close button is clickable.

# Implementation Plan: fix/reentrant-handler-guards

## Goal
Add re-entrancy guards (`isProcessing` ref pattern) to all async UI handler functions that interact with BLE or Supabase, preventing double-tap race conditions that cause duplicate crew entries, orphaned sessions, duplicate presets, and BLE scan collisions.

## Source Analysis Link
- [system_audit_report.md — CLUSTER 6](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) (lines 153–167)
- Rule: R-26 (Re-entrancy Races)

## Pattern
Every guarded handler follows this template:
```typescript
const isProcessingRef = useRef(false);

const handleFoo = async () => {
  if (isProcessingRef.current) return;
  isProcessingRef.current = true;
  try {
    // ... existing async body ...
  } finally {
    isProcessingRef.current = false;
  }
};
```
For components that already have a `try/finally` block, the guard wraps the existing structure. For components using `React.memo` or `useCallback`, the ref is declared at the component/hook scope level.

## Files to Modify

| # | File | Handlers | Existing Guard? |
|---|------|----------|-----------------|
| 1 | `src/screens/Onboarding/HardwareSetupWizardScreen.tsx` | `handleStartScan` (L170) | ❌ No — `bleState !== 'SCANNING'` check is not a ref guard |
| 2 | `src/components/crew/CrewControllerScreen.tsx` | `handleLeave` (L38), `handleEndSession` (L42) | ❌ No |
| 3 | `src/components/crew/CrewJoinScreen.tsx` | `handleJoinByCode` (L29) | ❌ Partial — uses `isLoading` state but state is async, not synchronous ref |
| 4 | `src/components/crew/CrewCreateScreen.tsx` | `handleCreate` (L32) | ❌ Same partial pattern |
| 5 | `src/components/crew/CrewLandingScreen.tsx` | `handleJoinById` (L75), `handleJoinByCode` (L150) | ❌ Same partial pattern |
| 6 | `src/components/crew/CrewScheduleScreen.tsx` | `handleCreate` (L26) | ❌ Same partial pattern |
| 7 | `src/components/docked/BuilderPanel.tsx` | `handleSavePreset` (L94) | ❌ Uses `isSaving` state — not synchronous |
| 8 | `src/components/docked/QuickPresetModal.tsx` | `handlePublish` (L97) | ❌ Uses `isPublishingCloud` state — not synchronous |
| 9 | `src/components/AccountModal.tsx` | `handleChangePassword` (L274), `handleChangeEmail` (L305), `handleSignOut` (L326) | ❌ Uses `modalStatus` FSM — not a ref guard |
| 10 | `src/components/admin/AdminToolsModal.tsx` | `handleExport` (L155), `handleUpload` (L169) | ❌ No |
| 11 | `src/hooks/useAccountOverview.ts` | `handleSaveProfile` (L168), `handlePickProfilePhoto` (L190), `handleToggleHealthSync` (L243), `handleToggleAutoPause` (L260), `handleCreateCrew` (L271), `handleJoinCrew` (L288) | ❌ `isDataLoadingRef` exists but only guards `loadData`, not individual handlers |
| 12 | `src/providers/ComplianceGate.tsx` | `handleAccept` (L94), `handleDecline` (L130) | ❌ No |

## Steps

### Step 1 — Add guards to HardwareSetupWizardScreen
- Add `const isScanProcessingRef = useRef(false);` near existing refs (L83). Source: `HardwareSetupWizardScreen.tsx:83`
- Wrap `handleStartScan` (L170–177) with ref guard and `try/finally`. Source: `HardwareSetupWizardScreen.tsx:170`
- **Verify:** `handleStartScan` body starts with `if (isScanProcessingRef.current) return;` and ends in `finally { isScanProcessingRef.current = false; }`.

### Step 2 — Add guards to CrewControllerScreen
- Add `import { useRef } from 'react';` to existing React import (L3). Source: `CrewControllerScreen.tsx:3`
- Add `const isProcessingRef = useRef(false);` inside component (after L33). Source: `CrewControllerScreen.tsx:33`
- Wrap `handleLeave` (L38–40) and `handleEndSession` (L42–44) with ref guard. Source: `CrewControllerScreen.tsx:38`
- **Verify:** Both handlers early-return on `isProcessingRef.current === true` and reset in `finally`.

### Step 3 — Add guards to CrewJoinScreen
- Add `useRef` to React import (L2). Source: `CrewJoinScreen.tsx:2`
- Add `const isProcessingRef = useRef(false);` inside component. Source: `CrewJoinScreen.tsx:25`
- Wrap `handleJoinByCode` (L29–52). Source: `CrewJoinScreen.tsx:29`
- **Verify:** Guard is synchronous ref check, not the existing `isLoading` state.

### Step 4 — Add guards to CrewCreateScreen
- Add `useRef` to React import (L2). Source: `CrewCreateScreen.tsx:2`
- Add ref and wrap `handleCreate` (L32–69). Source: `CrewCreateScreen.tsx:32`
- **Verify:** Ref guard wraps the entire function including the early `return` on empty name.

### Step 5 — Add guards to CrewLandingScreen
- Ref already imported (L3). Add `const isProcessingRef = useRef(false);` after existing refs. Source: `CrewLandingScreen.tsx:50`
- Wrap `handleJoinById` (L75–93) and `handleJoinByCode` (L150–175). Source: `CrewLandingScreen.tsx:75`
- **Verify:** Both handlers are guarded independently via the shared ref (they are mutually exclusive user actions).

### Step 6 — Add guards to CrewScheduleScreen
- Add `useRef` to React import (L3). Source: `CrewScheduleScreen.tsx:3`
- Add ref and wrap `handleCreate` (L26–65). Source: `CrewScheduleScreen.tsx:26`
- **Verify:** Ref guard present at handler entry, reset in existing `finally` block.

### Step 7 — Add guards to BuilderPanel
- Add `useRef` to React import (L1). Source: `BuilderPanel.tsx:1`
- Add `const isSavingRef = useRef(false);` inside component (after L54). Source: `BuilderPanel.tsx:54`
- Wrap `handleSavePreset` (L94–113). Source: `BuilderPanel.tsx:94`
- **Verify:** Ref guard prevents double-save even if `isSaving` state update is batched.

### Step 8 — Add guards to QuickPresetModal
- Add `useRef` to React import (L11). Source: `QuickPresetModal.tsx:11`
- Add `const isPublishingRef = useRef(false);` inside memo component. Source: `QuickPresetModal.tsx:68`
- Wrap `handlePublish` (L97–133). Source: `QuickPresetModal.tsx:97`
- **Verify:** Ref guard is the first check, before `setIsPublishingCloud(true)`.

### Step 9 — Add guards to AccountModal
- Add `useRef` to React import (L17). Source: `AccountModal.tsx:17`
- Add `const isSecurityProcessingRef = useRef(false);` inside component. Source: `AccountModal.tsx:84`
- Wrap `handleChangePassword` (L274–302), `handleChangeEmail` (L305–321), `handleSignOut` (L326–352). Source: `AccountModal.tsx:274`
- **Verify:** Each handler has independent ref guard. `modalStatus` FSM remains as UI state.

### Step 10 — Add guards to AdminToolsModal
- `useRef` already imported (L5). Source: `AdminToolsModal.tsx:5`
- Add `const isExportingRef = useRef(false);` and `const isUploadingRef = useRef(false);` inside component. Source: `AdminToolsModal.tsx:111`
- Wrap `handleExport` (L155–157) and `handleUpload` (L169–176). Source: `AdminToolsModal.tsx:155`
- **Verify:** Each async handler has its own dedicated ref guard.

### Step 11 — Add guards to useAccountOverview
- `useRef` already imported (L3). Source: `useAccountOverview.ts:3`
- Add `const isProfileSavingRef = useRef(false);` and `const isPhotoPickingRef = useRef(false);` near L46. Source: `useAccountOverview.ts:46`
- Wrap: `handleSaveProfile` (L168), `handlePickProfilePhoto` (L190), `handleToggleHealthSync` (L243), `handleToggleAutoPause` (L260), `handleCreateCrew` (L271), `handleJoinCrew` (L288). Source: `useAccountOverview.ts:168`
- Use shared `isProcessingRef` for crew handlers (mutually exclusive), separate refs for profile/photo.
- **Verify:** All 6 handlers have ref guards. Existing `isDataLoadingRef` for `loadData` is untouched.

### Step 12 — Add guards to ComplianceGate
- `useRef` already imported (L2). Source: `ComplianceGate.tsx:2`
- Add `const isAcceptingRef = useRef(false);` inside component (after L19). Source: `ComplianceGate.tsx:19`
- Wrap `handleAccept` (L94–128) and `handleDecline` (L130–137). Source: `ComplianceGate.tsx:94`
- **Verify:** Both handlers early-return on ref guard, reset in `finally`.

## Verification Plan
1. **TSC clean:** `npm run verify` passes with zero new type errors.
2. **Jest green:** All existing tests pass — no handler signature changes.
3. **Manual spot-check:** For each modified file, `git diff HEAD <file>` shows ONLY the ref declaration + guard lines added. No unrelated changes.
4. **Pattern audit:** `grep -rn "isProcessingRef\|isSavingRef\|isExportingRef\|isUploadingRef\|isAcceptingRef\|isScanProcessingRef\|isSecurityProcessingRef\|isProfileSavingRef\|isPhotoPickingRef" src/` returns exactly the expected count (12 files, ~28 handler entry points).

## Out of Scope
- `src/components/admin/*` sub-panels (lower risk, admin-only tools)
- `src/components/SkateSpotBottomSheet.tsx` (low frequency user action)
- Refactoring existing `isLoading` state into refs — the ref guard is additive, not a replacement
- Any BLE protocol or payload changes
- UI loading indicator changes (refs are invisible to users — existing `isLoading`/`isSaving` state continues to drive UI)

# Implementation Plan: Telemetry Hardening v2

## Goal
Eliminate all silent error-swallowing (`catch (e) {}`) and raw `console.log`/`console.warn`/`console.error` calls across the codebase, replacing them with proper `AppLogger` instrumentation to ensure production visibility.

## Target Files & Proposed Changes

### High Risk Target
- **[MODIFY]** `src/services/DeviceRepository.ts`
  - *Change:* At line 758, in the `_fk` catch block during multi-attempt ownership linking, add `AppLogger.error('[DeviceRepository] FK fallback failed', { error: String(_fk) })`.
  - *Why:* If this fails silently, orphaned records are created that users can never manage.

- **[MODIFY]** `src/hooks/ble/useBLESweeper.ts`
  - *Change 1:* At line 117, inside the `AsyncStorage.multiGet` JSON.parse loop, add `AppLogger.warn('[useBLESweeper] Malformed HW cache', { mac, error: String(e) })`.
  - *Change 2:* At line 257, inside the BLE notification parse loop, add `AppLogger.warn('[useBLESweeper] ZenggeProtocol parse failed', { mac, error: String(e) })`.

### Structural Gaps (Missing Try/Catch)
- **[MODIFY]** `src/hooks/useDashboardCrew.ts`
  - *Change:* Wrap the entire contents of the `tryRejoin` async function (lines 47-70) in a try/catch. Log errors via `AppLogger.warn('[useDashboardCrew] auto-rejoin failed', { error: String(e) })`.

- **[MODIFY]** `src/hooks/useCrewManage.ts`
  - *Change 1:* Append `.catch(e => AppLogger.warn('[CrewManage] loadCrewMembers failed', { crewId, error: String(e) }))` to `profileService.getCrewMembersWithNames`.
  - *Change 2:* Append `.catch(e => AppLogger.warn('[CrewManage] getCrewStats failed', { id, error: String(e) }))` to `profileService.getCrewStats`.
  - *Change 3:* Append `.catch(e => AppLogger.warn('[CrewManage] searchUsers failed', { query: userSearchQuery, error: String(e) }))` to `profileService.searchUsers`.

- **[MODIFY]** `src/hooks/useAccountOverview.ts`
  - *Change:* Add `AppLogger.error`/`AppLogger.warn` to the catch blocks in `handleSaveProfile`, `handlePickProfilePhoto`, `handleCreateCrew`, `handleJoinCrew`, and `handleLeaveCrew`. Keep existing `Alert.alert` calls.

### Raw Console Replacements
- **[MODIFY]** `src/components/admin/tools/GlobalAnalyticsPanel.tsx`
  - *Change:* Replace `console.error` at line 17 with `AppLogger.error('[GlobalAnalytics] RPC failed', { error: String(e) })`.

- **[MODIFY]** `src/components/admin/tools/Sk8LytzProgrammer.tsx`
  - *Change:* Replace `console.error` at line 158 with `AppLogger.error('[Sk8LytzProgrammer] persist failed', ...)`.

- **[MODIFY]** `src/providers/ComplianceGate.tsx`
  - *Change:* Replace `console.warn` at line 62 with `AppLogger.warn('[ComplianceGate] check error', { error: String(e) })`.

- **[MODIFY]** `src/components/LocationPicker.tsx`
  - *Change:* Replace `console.warn` at line 94 with `AppLogger.warn('[LocationPicker] OSM fetch error', { error: String(err) })`.

- **[MODIFY]** `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
  - *Change:* Replace `console.warn` and 3 `console.log` calls with `AppLogger.warn('[FTUE] Blink test failed')` and `AppLogger.log('FTUE_PHASE_COMPLETE', ...)`.

### Minor Fixes
- **[MODIFY]** `src/services/ScenesService.ts`
  - *Change:* Line 224: `catch (e) { AppLogger.warn('[ScenesService] deleteLocalSceneFromQueue failed', { error: String(e) }); }`
- **[MODIFY]** `src/services/GradientsService.ts`
  - *Change:* Line 70: `catch (e) { AppLogger.warn('[GradientsService] cache write failed', { error: String(e) }); }`
- **[MODIFY]** `src/services/AppSettingsService.ts`
  - *Change:* Line 86: `catch (e) { AppLogger.warn('[AppSettingsService] cache sync failed', { error: String(e) }); }`
- **[MODIFY]** `src/hooks/useRecentSpots.ts`
  - *Change:* Line 25 & 44: Add `AppLogger.warn` to catch blocks.

## Verification Plan
1. Run `npx tsc --noEmit` to verify type safety after imports.
2. Ensure no UI flows are broken by the addition of logging.
3. Review git diff before commit.

# Implementation Plan: Sentient Tech Debt Sweep

This plan addresses the high-risk technical debt identified in the `whats_left_to_fix.md` audit, focused on storage key synchronization, speed normalization, and performance bottlenecks.

## Proposed Changes

### [Architectural]
Standardizing global constants.

#### [NEW] [AppConstants.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/constants/AppConstants.ts)
- Define a unified storage prefix: `export const STORAGE_PREFIX = '@Sk8lytz_';`
- Define hardware speed limits: `export const HW_SPEED_MAX = 31;`

#### [MODIFY] [NormalizationUtils.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/utils/NormalizationUtils.ts)
- Add `normalizeUISpeedToHardware(val: number): number` (maps 0-100 to 1-31).

### [Refactoring]
Updating call sites to use the new standards.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/DockedController.tsx)
- Replace all un-prefixed AsyncStorage keys with `AppConstants.STORAGE_PREFIX`.
- Use `normalizeUISpeedToHardware` before calling protocol methods.

#### [MODIFY] [AccountModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/AccountModal.tsx)
- Re-map notification preference keys to `@Sk8lytz_notif_prefs`.

#### [MODIFY] [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/hooks/useBLE.ts)
- Extract all inline `require('buffer')` calls to a single top-level `import { Buffer } from 'buffer'`.

## Verification Plan

### Automated Tests
- `npm test`: Verify the speed normalization math (edge cases: 0 -> 1, 100 -> 31).

### Manual Verification
1. Open App -> Change Theme and Toggle a preference -> Reload App.
2. Verify all settings persist (confirms storage key migration).
3. Set speed slider to 1% -> Verify skaters move at minimum hardware speed (not 0).

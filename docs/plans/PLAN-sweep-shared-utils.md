# Implementation Plan

## Task: sweep-shared-utils
**Slug:** sweep-shared-utils
**Wave:** [WAVE:5] — Prerequisite: Wave 4 fully merged
**Size:** [Snack] — 7 files
**Risk:** [M-RISK] — Shared utilities and theme; changes propagate widely but are low-complexity
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_UTILS_findings.json` + `artifacts/deepdive_raw/DOMAIN_ACCESSIBILITY_AND_I18N_findings.json`
**Prerequisite:** Wave 4 fully merged

## Goal
Fix 14 findings in shared utilities, accessibility, and theme. Key work: deduplicate `isValidEmail` utility (duplicated across 3+ auth forms), add accessibility props to `CustomSlider` and `DeviceItem`, fix `PositionalGradientBuilder` error handling (missing `instanceof Error` check + hardcoded `setTimeout`), fix the `LocationPickerMap.web.tsx` export mismatch, and align `LocationService.getSilentLocation` with the platform availability contract.

## Decision Log
- **`isValidEmail` dedup (R-21)**: Duplicated across `AuthFormSignIn.tsx`, `AuthFormRegister.tsx`, and possibly more. The canonical version must live in `src/utils/validation.ts` and be imported everywhere.
- **`CustomSlider` accessibility (R-25 HIGH)**: The slider uses `PanResponder` and is completely invisible to screen readers — no `accessible`, `accessibilityRole`, `accessibilityLabel`, or `accessibilityValue` props. This is a HIGH finding since sliders are primary interaction elements.
- **`PositionalGradientBuilder` error (R-06 + R-16)**: The catch block accesses `e.message` without an `instanceof Error` check (crashes on non-Error throws). Also has a hardcoded `setTimeout` for BLE write throttling — must be replaced with a named constant.
- **`LocationService.getSilentLocation` (R-20)**: `Location.getLastKnownPositionAsync` is not supported on all platforms — must add a platform availability guard before calling.

## Files to Create/Modify

### [MODIFY] src/components/PositionalGradientBuilder.tsx
- L55: Add `instanceof Error` check in catch block before accessing `e.message` (R-06)
- L44: Replace hardcoded `setTimeout` delay with named constant `BLE_WRITE_THROTTLE_MS` (R-16)
- L55: Fix `AppLogger.error` — fix type on the `err` catch variable (R-08)

### [MODIFY] src/components/DeviceItem.tsx
- L35: Add `accessibilityRole="button"` and `accessibilityLabel` to the `TouchableOpacity` (R-25)

### [MODIFY] src/components/CustomSlider.tsx
- L102: Add `accessible={true}`, `accessibilityRole="adjustable"`, `accessibilityLabel`, and `accessibilityValue={{ min, max, now: value }}` to the `PanResponder` wrapper `View` (R-25)

### [MODIFY] src/components/LocationPicker.tsx
- L89: Clear `debounceTimer.current` in the `useEffect` cleanup return function (R-17)

### [MODIFY] src/components/LocationPickerMap.web.tsx
- L14: Fix Web stub component export interface to match the native `LocationPickerMap` exports (R-20)

### [MODIFY] src/services/LocationService.ts
- L81: Add `Platform.OS !== 'web'` guard before calling `Location.getLastKnownPositionAsync` (R-20)

### [MODIFY] src/theme/theme.ts
- L70: Add `web` platform to the `Platform.select` shadow values (R-20 LOW)

### [NEW] src/utils/validation.ts (if not already exists)
- Extract `isValidEmail` function and any other duplicated validators into a canonical utility module
- Update all import sites

## Out of Scope
- `CommunityModal.tsx` hardcoded string (i18n gap — deferred, no i18n system exists yet)
- `src/components/auth/AuthFormSignIn.tsx` beyond the isValidEmail import update
- No BLE changes

## Verification Plan
- `npm run verify` — TSC must pass
- `grep 'isValidEmail' src/components/auth/` should show only import statements, no inline definitions
- Screen reader test: `CustomSlider` must announce value on iOS VoiceOver / Android TalkBack

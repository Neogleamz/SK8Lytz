# Implementation Plan: C11 — Accessibility i18n Sweep

## Goal
Wrap all hardcoded accessibilityLabel strings with t() from i18n.

## Rules Addressed
- Accessibility (9 findings)

## Files to Create/Modify
- `src/components/admin/AdminToolsModal.tsx`
- `src/components/ConnectionStrengthBadge.tsx`
- `src/components/CustomSlider.tsx`
- `src/components/DeviceItem.tsx`
- `src/components/NeonHueStrip.tsx`
- `src/components/PatternCard.tsx`
- `src/components/TacticalSlider.tsx`
- `src/components/VerticalPatternDrum.tsx`

## Implementation Steps
1. View each file. Find accessibilityLabel with hardcoded English strings.
2. Add i18n key to translation file.
3. Replace hardcoded string with t('key').
4. Verify: git diff shows only label changes.
5. Run npm run verify.

## Out of Scope
- PositionalGradientBuilder.tsx (C6)

# Implementation Plan: Type Safety Sweep

## Overview
The audit identified numerous violations of the "No any Cast Law" across UI, Hooks, and Services. Bypassing strict typing degrades reliability and hides compilation errors. 

## Affected Files
- `src/components/AccountModal.tsx`
- `src/components/CustomSlider.tsx`
- `src/components/DeviceSettingsModal.tsx`
- `src/components/VerticalPatternDrum.tsx`
- `src/components/VisualizerUnit.tsx`

## Rule Violations
- The No any Cast Law (Rule S3).

## Proposed Changes
1. Replace `any` casts with `unknown` or specific interfaces in `AccountModal.tsx` and `DeviceSettingsModal.tsx`.
   - **Source:** `src/components/AccountModal.tsx:310`
   - **Verify:** Ensure `npm run verify` TypeScript checks pass with `unknown` or a strict interface.
2. For React Native styling casts, enforce `StyleProp<ViewStyle>` or `TextStyle` in `CustomSlider.tsx` and `VisualizerUnit.tsx`.
   - **Source:** `src/components/CustomSlider.tsx:12`
   - **Verify:** Ensure `npm run verify` TypeScript checks pass without using `any`.

## Out of Scope
- Rewriting the component logic. 
- Fixing non-type related issues in these components.

## Kanban Tags
- [Status]: `[🔥 ON DECK]`
- [Verification Status]: `[✅ VERIFIED]`
- [Layer]: `[DOMAIN_UI_MODALS]`
- [Risk]: `[L-RISK]`
- [Size]: `[Batch]`
- [Cognitive Load]: `[LOW]`
- Source of Truth: `.agents/rules/prime-directive.md` S3

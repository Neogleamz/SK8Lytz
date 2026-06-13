# Implementation Plan

## Task: sweep-ui-visualizer-patterns
**Slug:** sweep-ui-visualizer-patterns
**Wave:** [WAVE:1] — Parallel-safe with other Wave 1 clusters
**Size:** [Meal] — 9 files
**Risk:** [M-RISK] — UI/rendering layer; no BLE protocol changes
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_UI_VISUALIZER_findings.json`

## Goal
Fix 12 findings in the visualizer and pattern picker layer. Critical: the floating promise in `UnifiedPatternPicker` where `writeToDeviceRef.current(payload)` returns a Promise that is never caught. Fix `any` typed props in `VisualizerUnit` and `ProductVisualizer`. Remove the locally-duplicated `hexToRgb` in `CustomEffectVisualizer`. Fix web-only style props in `NeonHueStrip`. Extract inline FlatList callbacks to stable references in pattern pickers. Note: `SpatialEngine.ts` monolith (1,452 lines) is flagged but extraction is deferred — only surgical fixes inside it.

## Decision Log
- **Floating promise (CONFIRMED — R-11 HIGH)**: `writeToDeviceRef.current(payload)` in `UnifiedPatternPicker` L62 is called bare. Must add `.catch()` or `await` inside an async handler with try/catch.
- **`hexToRgb` dedup**: The canonical version lives in `src/utils/` — remove the local copy in `CustomEffectVisualizer` and import from utils.
- **`NeonHueStrip` web styles**: `touchAction` and `userSelect` are web-only CSS properties passed to a standard RN `View` — this crashes at runtime on native. Must be conditionally applied via `Platform.select`.
- **SpatialEngine.ts monolith**: Too large for safe surgical edit in this sweep. Flag with `// TODO: extract` comment only.

## Files to Create/Modify

### [MODIFY] src/components/patterns/UnifiedPatternPicker.tsx
- Wrap `writeToDeviceRef.current(payload)` at L62 in a try/catch async handler with `AppLogger.error` on failure (R-11)

### [MODIFY] src/components/VisualizerUnit.tsx
- Type `onLongPress` prop's `device` parameter with correct `DisplayDevice` type instead of `any` (L45)

### [MODIFY] src/components/ProductVisualizer.tsx
- Type `onLongPressDevice` prop's `device` parameter with correct `DisplayDevice` type instead of `any` (L29)

### [MODIFY] src/components/CustomEffectVisualizer.tsx
- Remove local `hexToRgb` definition at L18 — import from `src/utils/` (R-21)

### [MODIFY] src/components/NeonHueStrip.tsx
- Wrap `touchAction` and `userSelect` style props in `Platform.select({ web: { touchAction: ..., userSelect: ... }, default: {} })` at L99 (R-20)

### [MODIFY] src/components/patterns/GradientLibraryTab.tsx
- Extract `renderGradientCard` inline arrow function to a stable `useCallback` at L45 (R-28)
- Move inline `contentContainerStyle` and `columnWrapperStyle` objects outside render (R-07)

### [MODIFY] src/components/patterns/PatternPickerTab.tsx
- Extract `renderItem` inline function to a stable `useCallback` at L115 (R-28)
- Move inline style `{ width: '48%' }` to a `StyleSheet` constant (R-07)

### [MODIFY] src/components/CameraTracker.tsx
- Add `.catch()` handler to `requestPermission('CAMERA')` call inside `useEffect` at L49 (R-11)

## Out of Scope
- `SpatialEngine.ts` extraction (separate task)
- No changes to BLE dispatch layer
- No changes to `VisualizerUnit.tsx` animation logic (R-07 LOW finding deferred)

## Verification Plan
- `npm run verify` — TSC must pass
- Verify `hexToRgb` import resolves correctly after dedup
- `git diff HEAD` after each edit

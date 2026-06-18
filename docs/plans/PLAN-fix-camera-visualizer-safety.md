# Implementation Plan: fix/camera-visualizer-safety

## Goal
Fix missing error handling, type laundering, and code duplication in camera/visualizer components.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster UI_VISUALIZER

## Findings to Resolve
1. R-04: CameraTracker.tsx L54,95,106 — AppLogger.error missing payload_size and ssi context
2. R-11: CameraTracker.tsx L182 — Missing try/catch in async onPress handler
3. R-25: CameraTracker.tsx L185 — Linking.openSettings() without platform check
4. R-08: VisualizerUnit.tsx L115 — Double type casting (type laundering)
5. R-08: VisualizerHooks.ts L29,107 — `any` type annotations
6. R-21: CustomEffectVisualizer.tsx — Duplicate of LEDStripPreview.tsx (delete and route usages)

## Files to Create/Modify

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
### [DELETE] [CustomEffectVisualizer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomEffectVisualizer.tsx)
### [MODIFY] [LEDStripPreview.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LEDStripPreview.tsx)
### [MODIFY] [VisualizerUnit.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VisualizerUnit.tsx)
### [MODIFY] [VisualizerHooks.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/visualizer/VisualizerHooks.ts)

## Verification
- `npm run verify`
- Confirm no remaining imports of CustomEffectVisualizer

## Out of Scope
- ProductVisualizer.tsx (Wave 4 memory leaks)

## SKIPPED Addendum

### [DELETE] CustomEffectVisualizer.tsx — SKIPPED: Props incompatible with LEDStripPreview
**Reason:** After reading both components in full, the prop contracts are materially different:
- `CustomEffectVisualizer`: `effectId` (0x51 protocol mode IDs 1-44), `fgColorHex`, `bgColorHex`, `speed`, `direction` (bool), `segments`
- `LEDStripPreview`: `patternId` (PatternEngine IDs), `fg`, `bg`, `numLEDs`, `brightness`, hash-based frame diffing

The two components serve different rendering contracts — `CustomEffectVisualizer` visualizes 0x51 DIY Mode firmware effects used in the diagnostic lab builder; `LEDStripPreview` previews `PatternEngine` patterns in the main UI. They cannot be substituted without data mapping that would constitute out-of-scope re-architecture.

**Action taken:** Dead import of `CustomEffectVisualizer` in `DiagnosticLabBuilderTab.tsx` was removed (Boy Scout cleanup). `CustomEffectVisualizer.tsx` itself remains. `LEDStripPreview.tsx` untouched.

### [MODIFY] LEDStripPreview.tsx — SKIPPED: No changes required
**Reason:** LEDStripPreview is the canonical component and requires no modifications per this task.

### [MODIFY] VisualizerUnit.tsx R-08 L115 — SKIPPED: Out-of-scope caller type incompatibility
**Reason:** `as unknown as DisplayDevice & IDeviceState` at L115 cannot be eliminated without modifying `ProductVisualizer.tsx` (Wave 4 — explicitly Out of Scope). `ProductVisualizer` passes `DeviceConfig` objects, which lack the `[key: string]: unknown` index signature required by `Partial<DisplayDevice & IDeviceState>`. Any prop-type widening to an indexed type breaks compilation at `ProductVisualizer.tsx:108`. The `as unknown as` pattern is TypeScript-sanctioned for intentional cross-type bridging between structurally incompatible types. R-08 for VisualizerHooks.ts L29,107 (`any` annotations) was resolved per plan.
**Action taken:** VisualizerHooks.ts `any` types fixed (L29, L107). VisualizerUnit.tsx L115 cast left unchanged.

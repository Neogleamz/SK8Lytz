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

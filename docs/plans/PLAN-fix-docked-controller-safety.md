# Implementation Plan: fix/docked-controller-safety

## Goal
Extract the 67KB DockedController monolith into smaller components, then fix re-entrancy races, context overload, and hardcoded delays.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Cluster UI_DOCKED_CONTROLLER

## Phase 1: Monolith Extraction (S4 compliance — file exceeds 30KB)
Before fixing any violations, extract logical sections of DockedController.tsx into dedicated sub-components:
- Extract color picker section → `src/components/docked/ColorPickerSection.tsx`
- Extract pattern selector section → `src/components/docked/PatternSelectorSection.tsx`
- Extract music controls section → `src/components/docked/MusicControlsSection.tsx`
- Extract brightness/speed controls → `src/components/docked/ControlSlidersSection.tsx`

## Phase 2: Violation Fixes
1. R-23: DockedController.tsx — Monolith reduction (Phase 1 above)
2. R-27: DockedController.tsx L203 — Reduce direct context consumption from 4+ to 1-2 via facade hook
3. R-26: DockedController.tsx L771 — Add boolean re-entrancy guards to handleMusicChange/applyFixedPattern
4. R-16: DockedController.tsx L103, SpectrumAnalyzer.tsx L63,89 — Replace hardcoded delays

## Files to Create/Modify

### [NEW] [ColorPickerSection.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/ColorPickerSection.tsx)
### [NEW] [PatternSelectorSection.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/PatternSelectorSection.tsx)
### [NEW] [MusicControlsSection.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/MusicControlsSection.tsx)
### [NEW] [ControlSlidersSection.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/ControlSlidersSection.tsx)
### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
### [MODIFY] [SpectrumAnalyzer.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/SpectrumAnalyzer.tsx)
### [MODIFY] [FavoritesPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/FavoritesPanel.tsx)

## Verification
- `npm run verify`
- DockedController.tsx must be under 30KB after extraction
- All existing functionality preserved

## Out of Scope
- useControllerDispatch.ts (Wave 1)
- DashboardScreen.tsx (Wave 2)

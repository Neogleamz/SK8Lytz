# Implementation Plan: C4 — DockedController Monolith Extraction

## Goal
Extract DockedController.tsx (57.09KB) into sub-components, reduce context depth.

## Source Analysis
[system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md)

## Rules Addressed
- R-23: Monolith (57.09KB), R-27: 4 context consumers, R-21: Hardcodes ZenggeProtocol

## Files to Create/Modify
- `src/components/docked/DockedController.tsx` — Reduce to orchestrator
- `src/components/docked/DockedColorPanel.tsx` [NEW]
- `src/components/docked/DockedEffectPanel.tsx` [NEW]
- `src/components/docked/DockedMusicPanel.tsx` [NEW]
- `src/components/docked/DockedHeader.tsx` [NEW]
- `src/components/docked/useDockedState.ts` [NEW]

## Implementation Steps
1. Read DockedController.tsx in full. Identify panel boundaries.
2. Extract color panel. Verify: color picker renders.
3. Extract effect panel. Verify: effect selection works.
4. Extract music panel. Verify: music controls work.
5. Create useDockedState to reduce context consumption from 4 to 1 facade.
6. Route protocol calls through ControllerRegistry instead of direct ZenggeProtocol.
7. Run npm run verify.

## Out of Scope
- DashboardScreen.tsx (C2), ZenggeProtocol.ts (C3)

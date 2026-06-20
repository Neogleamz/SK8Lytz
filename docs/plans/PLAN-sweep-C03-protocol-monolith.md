# Implementation Plan: C3 — Protocol Monolith Decomposition

## Goal
Extract ZenggeProtocol.ts (54.55KB), SpatialEngine (59.28KB), effectProcessors.ts (35.02KB) below 30KB.

## Source Analysis
[system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md)

## Rules Addressed
- R-23: Monolith Detection (3 HIGH findings)
- R-21: Split-brain useControllerDispatch vs useProtocolDispatch

## Files to Create/Modify
- `src/protocols/ZenggeProtocol.ts` — Extract handlers, keep as facade (<25KB)
- `src/protocols/handlers/staticColorHandler.ts` [NEW]
- `src/protocols/handlers/dynamicEffectHandler.ts` [NEW]
- `src/protocols/handlers/musicModeHandler.ts` [NEW]
- `src/protocols/effectProcessors.ts` — Extract large switch blocks
- `src/protocols/processors/colorfulProcessor.ts` [NEW]
- `src/protocols/processors/gradientProcessor.ts` [NEW]

## Implementation Steps
1. Read ZenggeProtocol.ts. Map method groups by opcode domain.
2. Extract static color methods (0x56, 0x59). Verify: imports resolve.
3. Extract dynamic effect methods. Verify: imports resolve.
4. Extract music mode methods (0x74). Verify: imports resolve.
5. Extract effectProcessors large blocks. Verify: all processors accessible.
6. Run npm run verify.

## Out of Scope
- DockedController.tsx (C4), BanlanxAdapter.ts, IControllerProtocol.ts

# Implementation Plan: refactor/spatial-pattern-engines

## Goal
Resolve circular dependencies between pattern engine files and extract SpatialEngine (61KB) monolith into manageable modules.

## Source Analysis
📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) — Clusters R-23 + R-29

## Phase 1: Circular Dependency Resolution
Break the following cycles by extracting shared types/utilities into a new `src/protocols/shared/` directory:
1. PatternEngine ↔ SpatialEngine
2. PatternEngine ↔ VisualizerEngine
3. PatternEngine ↔ SymphonyEngine

### Strategy
- Create `src/protocols/shared/engineTypes.ts` — shared interfaces and type definitions
- Create `src/protocols/shared/engineUtils.ts` — shared utility functions
- Update all engine imports to point to shared modules instead of each other

## Phase 2: SpatialEngine Monolith Extraction
SpatialEngine.ts at 61KB exceeds the 30KB S4 safety threshold.
- Extract spatial math utilities → `src/protocols/shared/spatialMath.ts`
- Extract coordinate system logic → `src/protocols/shared/coordinateSystem.ts`
- Extract effect processors → `src/protocols/spatial/effectProcessors.ts`

## Files to Create/Modify

### [NEW] [engineTypes.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/shared/engineTypes.ts)
Shared interfaces and type definitions extracted from cross-importing engine files. Contains:
- Common engine state types
- Shared effect parameter interfaces
- Pattern configuration types used by multiple engines

### [NEW] [engineUtils.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/shared/engineUtils.ts)
Shared utility functions that currently cause circular imports between engines. Contains:
- Color conversion helpers
- Timing/interpolation utilities
- Common validation functions

### [NEW] [spatialMath.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/shared/spatialMath.ts)
Spatial math utilities extracted from the SpatialEngine monolith. Contains:
- Vector math operations
- Distance/angle calculations
- Spatial interpolation functions

### [NEW] [coordinateSystem.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/shared/coordinateSystem.ts)
Coordinate system logic extracted from SpatialEngine. Contains:
- LED position mapping
- Coordinate transforms (polar, cartesian, strip-indexed)
- Zone/region definitions

### [NEW] [effectProcessors.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/spatial/effectProcessors.ts)
Effect processor logic extracted from SpatialEngine. Contains:
- Individual spatial effect implementations
- Effect composition pipeline
- Per-frame render functions

### [MODIFY] [SpatialEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SpatialEngine.ts)
- Remove extracted spatial math, coordinate system, and effect processor code
- Replace with imports from new modules
- Target: reduce from 61KB to under 30KB (S4 threshold)

### [MODIFY] [PatternEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/PatternEngine.ts)
- Replace direct imports from SpatialEngine/VisualizerEngine/SymphonyEngine with imports from `shared/engineTypes.ts` and `shared/engineUtils.ts`
- Break all circular dependency cycles

### [MODIFY] [VisualizerEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/VisualizerEngine.ts)
- Replace direct imports from PatternEngine with imports from `shared/engineTypes.ts`
- Break PatternEngine ↔ VisualizerEngine cycle

### [MODIFY] [SymphonyEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SymphonyEngine.ts)
- Replace direct imports from PatternEngine with imports from `shared/engineTypes.ts`
- Break PatternEngine ↔ SymphonyEngine cycle

## Verification
- `npm run verify` — full TSC + Jest + AST + TypeSafety suite must pass
- Run `node tools/ast-parser.js --files src/protocols/SpatialEngine.ts src/protocols/PatternEngine.ts` to confirm no circular imports remain
- SpatialEngine.ts must be under 30KB after extraction
- All existing tests must continue to pass with zero regressions

## Out of Scope
- ZenggeAdapter.ts, ZenggeProtocol.ts (Wave 1)
- DockedController.tsx (Wave 3)
- All non-protocol files
- Adding new features or changing engine behavior — this is a pure structural refactor

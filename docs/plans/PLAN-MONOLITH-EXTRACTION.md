# Implementation Plan
# PLAN-MONOLITH-EXTRACTION

## Summary
11 files exceed the 30KB extraction threshold (R-23), with 3 reaching critical size (50KB+). These create collision zones where any edit risks destroying unrelated functionality. The 3 critical files are flagged for extraction spikes before any other work on them is attempted. The remaining 8 are scheduled for extraction in priority order.

**Batch:** `BATCH:monolith-extraction`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/screens/DashboardScreen.tsx` — 50KB (R-23-011): CRITICAL
- `src/components/DockedController.tsx` — 67KB (R-23-007): CRITICAL  
- `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx` — 48KB (R-23-002): CRITICAL
- `src/protocols/SpatialEngine.ts` — 60KB (R-23-008): CRITICAL
- `src/types/supabase.ts` — 146KB (R-23-013): EXEMPT (auto-generated)
- Raw audit: `R-23_findings.json`

---

## All Oversized Files (Ordered by Priority)

| ID | File | Size | Priority |
|----|------|------|----------|
| R-23-007 | `DockedController.tsx` | 67KB | P0 — collision zone |
| R-23-008 | `SpatialEngine.ts` | 60KB | P0 — collision zone |
| R-23-011 | `DashboardScreen.tsx` | 50KB | P0 — collision zone |
| R-23-002 | `DiagnosticLabOracleTab.tsx` | 48KB | P1 |
| R-23-001 | `DiagnosticLabBuilderTab.tsx` | 42KB | P1 |
| R-23-009 | `ZenggeProtocol.ts` | 42KB | P1 |
| R-23-010 | `HardwareSetupWizardScreen.tsx` | 39KB | P2 |
| R-23-004 | `CrewLandingScreen.tsx` | 34KB | P2 |
| R-23-012 | `DeviceRepository.ts` | 35KB | P2 |
| R-23-005 | `UniversalSlidersFooter.tsx` | 33KB | P2 |
| R-23-006 | `AccountModal.tsx` | 30KB | P3 |
| R-23-003 | `CrewDetailScreen.tsx` | 31KB | P3 |
| R-23-013 | `supabase.ts` | 146KB | EXEMPT (generated) |

---

## Extraction Approach Per File

### P0 — DockedController.tsx (67KB) → Extract to:
- `src/components/docked/DockedHeader.tsx` — top bar + device status pill
- `src/components/docked/DockedPatternSelector.tsx` — pattern grid
- `src/components/docked/DockedSliderSection.tsx` — RGB sliders (already in UniversalSlidersFooter?)
- `src/hooks/useDockedController.ts` — all state logic extracted from component

### P0 — SpatialEngine.ts (60KB) → Extract to:
- `src/protocols/SpatialMath.ts` — pure math functions (easing, lerp, interpolation)
- `src/protocols/SpatialPatternBuilder.ts` — pattern construction logic
- `src/protocols/SpatialEngine.ts` — orchestrator (remains, now lean)

### P0 — DashboardScreen.tsx (50KB) → Extract to:
- `src/components/dashboard/SessionHUD.tsx` — active session banner + controls
- `src/components/dashboard/HardwareList.tsx` — connected device cards
- `src/components/dashboard/CrewHub.tsx` — crew proximity + crew status section
- `src/hooks/useDashboard.ts` — all state + effects from screen (see also R-27-001)

### P1 — ZenggeProtocol.ts (42KB) → Extract to:
- `src/protocols/ZenggeCommands.ts` — all command builders (setColor, setPattern, etc.)
- `src/protocols/ZenggeConstants.ts` — opcode maps, timing constants
- `src/protocols/ZenggeProtocol.ts` — sequencer + framing only

---

## Implementation Steps

### Each extraction follows this 5-step process:
1. **Read the entire file** with `view_file` — identify logical boundaries
2. **Create sub-components** — extract pure JSX sections into named components
3. **Create sub-hooks** — extract `useState`/`useEffect`/`useCallback` blocks into a `use<Name>.ts`
4. **Verify imports** — ensure all cross-references update correctly
5. **Confirm file size** — each output file must be ≤30KB

### Execution order for worktrees:
- `feat/extract-docked-controller` → `feat/extract-spatial-engine` → `feat/extract-dashboard-screen` → `feat/extract-zengge-protocol` → *(continue)*

> [!IMPORTANT]
> These extractions are PRECONDITIONS for any other task that touches these files. DO NOT approve editing `DockedController.tsx`, `DashboardScreen.tsx`, or `SpatialEngine.ts` without extraction first.

---

## Verification
- Each extracted file: `wc -c <file>` → must be ≤30720 bytes (30KB)
- `npm run verify` (TSC must pass — extraction cannot break type references)
- Visual smoke test: `/smoke-test` after each extraction merge

## Kanban Task Tags
- `[Status: 🏗️ ROADMAP]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: UI]`
- `[Risk: H-RISK]`
- `[Size: Banquet]`
- `[Cognitive Load: High]`
- `[BATCH: monolith-extraction]`

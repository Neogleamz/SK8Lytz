# Implementation Plan: refactor/break-circular-deps

## Goal
Break the 9 circular dependencies remaining in master so `npx madge --circular src/` returns zero, with NO behavior change.

## Source Analysis
`npx madge --circular --extensions ts,tsx src/` (2026-06-26) reported 9 cycles in two independent clusters (none in the C16-fixed appLogger/CrewService scope):

**Cluster A — deviceRepository barrel back-edge (6 cycles):**
```
hooks/useRegistration.ts > services/deviceRepository/index.ts > services/deviceRepository/DeviceRepositoryService.ts
  > {GroupRepository.ts, DeviceCloudSync.ts, DeviceStateManagement.ts, DeviceStorage.ts, types.ts}
```
The sub-modules import back through the barrel `index.ts` instead of the concrete files, creating the cycle.

**Cluster B — docked Universal* sibling cycles (3 cycles):**
```
components/docked/UniversalSlidersFooter.tsx > {UniversalColorGrid.tsx, UniversalHueStripSlider.tsx, UniversalTacticalSliders.tsx}
```
The siblings import each other (or import the footer back).

## Files to Create/Modify
- `src/services/deviceRepository/index.ts`
- `src/services/deviceRepository/DeviceRepositoryService.ts`
- `src/services/deviceRepository/DeviceCloudSync.ts`
- `src/services/deviceRepository/DeviceStateManagement.ts`
- `src/services/deviceRepository/DeviceStorage.ts`
- `src/services/deviceRepository/types.ts`  (types likely the cleanest break point — see steps)
- `src/components/docked/UniversalSlidersFooter.tsx`
- `src/components/docked/UniversalColorGrid.tsx`
- `src/components/docked/UniversalHueStripSlider.tsx`
- `src/components/docked/UniversalTacticalSliders.tsx`

## Implementation Steps
1. Run `npx madge --circular --extensions ts,tsx src/` and capture the exact 9 baseline cycles. Verify: matches the two clusters above.
2. **Cluster A:** For each sub-module that imports from `./index` (the barrel), change it to import directly from the concrete sibling file (e.g. `import { X } from './types'` not `from './index'`). Pure types should move to / be imported from `types.ts` which must import nothing from the barrel. Verify after each file: `git diff HEAD <file>` shows only import-path changes, no logic.
3. **Cluster B:** Identify the back-edge among `UniversalSlidersFooter` and the three children. Break it by importing the concrete child directly, extracting a shared type to a leaf module, or `import type` where the usage is type-only (runtime-erased). Verify: no runtime value import forms a cycle.
4. After all edits: `npx madge --circular --extensions ts,tsx src/` MUST report zero cycles among these 10 files (other pre-existing cycles outside these clusters, if any newly surface, are out of scope — report them).
5. Verify: `npm run verify` passes clean. Confirm zero behavior change (structural import-only refactor).

## Out of Scope
- Any cycle NOT in the two clusters above.
- Functional/logic changes to any of these modules — imports and type-only restructuring ONLY.
- The appLogger/CrewService modules (already cycle-free per C16).

## Coordination note
Cluster B files are in `src/components/docked/` — the same directory as the C4 fixes, but DIFFERENT files (BuilderPanel/DockedController are not in this task). Confirm no file overlap before merge.

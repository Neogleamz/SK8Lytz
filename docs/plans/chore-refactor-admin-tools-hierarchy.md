# [PLAN] chore/refactor-admin-tools-hierarchy

## Design Decisions & Rationale
The current `AdminToolsModal` logic and folder structure is heavily fragmented. While the UI implies a clean "Tools" tab containing 5 distinct sub-modules, the actual filesystem is scattered. Some files are floating loosely in `src/components`, while others are vaguely organized in `src/components/admin/`. This causes constant import resolution issues and violates our modular architecture principles.

To resolve this, we will create a strict namespace for Admin Tools and consolidate all 5 sub-tools into it, ensuring they act as modular, embedded children of the main `AdminToolsModal` container rather than floating global modals.

---

## Scope

**Target file(s):**
- `src/components/admin/AdminToolsModal.tsx`

**Files to relocate & refactor:**
1. `src/components/Sk8LytzDiagnosticLab.tsx` → `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx`
2. `src/components/Sk8LytzProgrammerModal.tsx` → `src/components/admin/tools/Sk8LytzProgrammer.tsx`
3. `src/components/admin/AppManagerModal.tsx` → `src/components/admin/tools/AppManager.tsx`
4. `src/components/admin/ProductManagerModal.tsx` → `src/components/admin/tools/ProductManager.tsx`
5. `src/components/AdminPicksScheduler.tsx` → `src/components/admin/tools/AdminPicksScheduler.tsx`

### Proposed Architecture

```
src/components/admin/
  ├─ AdminToolsModal.tsx (The Master Container & Tab Router)
  ├─ timeline/ ...
  ├─ stats/ ...
  ├─ devices/ ...
  └─ tools/
      ├─ Sk8LytzDiagnosticLab.tsx
      ├─ Sk8LytzProgrammer.tsx
      ├─ AppManager.tsx
      ├─ ProductManager.tsx
      └─ AdminPicksScheduler.tsx
```

---

## Proposed Changes

### [DIRECTORY SETUP]
- Create the explicit `src/components/admin/tools/` directory.

### [FILE MOVES]
- Move all 5 required components into the new `tools/` directory.
- Rename the `*Modal.tsx` files to simply `*.tsx` (e.g. `AppManager.tsx`).
- **Modal Wrapper Stripping:** Since these components are 100% exclusive to the `AdminToolsModal` parent, aggressively strip out their redundant top-level React Native `<Modal>` wrappers during the transplant. They will now render as standard embedded `<View>` panes inside the main modal's 4th tab.

### [MODIFY] `src/components/admin/AdminToolsModal.tsx`
- Refactor the 4th tab ("Tools") logic to act as a proper sub-router.
- Implement a secondary navigation (e.g., a segmented control or sub-menu) inside the Tools tab to hot-swap between the 5 newly enclosed Tool components.
- Fix all broken import statements globally across the app that were pointing to the old scattered locations.

---

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` to guarantee all global imports have been successfully re-linked to the new `admin/tools/` directory.

### Manual Verification
1. Boot the application and open `AdminToolsModal`.
2. Navigate to the 4th tab ("Tools").
3. Verify that all 5 sub-tools are visually accessible and render correctly within the main modal's bounds without triggering nested overlapping overlays.

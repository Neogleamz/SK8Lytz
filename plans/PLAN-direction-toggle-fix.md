# PLAN-direction-toggle-fix.md — Swap Universal Direction Toggles

Swap the rendering order, labels, icons, action dispatches, and highlights of the FWD and REV toggles in `UniversalSlidersFooter.tsx` so that REV is on top (sends `0`) and FWD is on the bottom (sends `1`).

## Proposed Changes

### UI Component

#### [MODIFY] [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- In the `DIRECTION TOGGLE` rendering block (around lines 420-437):
  - Swap the two `TouchableOpacity` blocks.
  - The top button should represent `REV` (Reverse):
    - On press, calls `setFixedDirection(0)` and `applyFixedPattern(..., 0)`.
    - Highlights (active state styling) when `!isForward` is true.
    - Displays `▼` (down arrow) and label `REV`.
  - The bottom button should represent `FWD` (Forward):
    - On press, calls `setFixedDirection(1)` and `applyFixedPattern(..., 1)`.
    - Highlights (active state styling) when `isForward` is true.
    - Displays `▲` (up arrow) and label `FWD`.

---

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify that there are no TypeScript compile errors.

### Manual Verification
- Render the dashboard, select an animated pattern (e.g. Comet Chase) that supports direction, and check the footer:
  - Verify `REV` (▼) is the top button, and `FWD` (▲) is the bottom button.
  - Tapping `REV` highlights the top button, and tapping `FWD` highlights the bottom button.
  - Check console logs or device behavior to ensure `REV` sends `0` and `FWD` sends `1` to `applyFixedPattern`.

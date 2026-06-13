# Fix Music Panel Layout Scaling

Address layout crowding and clipping issues in Music Mode where buttons are pushed underneath the "SOUND COLUMN" bar, and resolve duplicate/cramped sliders in the footer.

## User Review Required

> [!NOTE]
> This change optimizes the UI layout on all screens, especially smaller mobile viewports. It reduces the size of the mic toggle buttons, adds defensive flex scaling, and removes a redundant duplicate sensitivity slider in the footer so only two sliders (MIC SENSITIVITY and BRIGHTNESS) render side-by-side.

## Open Questions

None.

## Proposed Changes

### UI Components

#### [MODIFY] [MusicPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/MusicPanel.tsx)
- Add `flex: 1` to the parent container View.
- Reduce margins and padding of headers and control sections.
- Reduce sizes of `micIconCircle` (48 -> 38), `playButtonMain` (52 -> 44), and `playIconInner` (42 -> 34) to allow them to shrink and fit compact screens.
- Scale down icon sizes inside buttons accordingly.

#### [MODIFY] [UniversalSlidersFooter.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
- Remove the duplicated sensitivity slider from the right slot when `activeMode === 'MUSIC'`.
- Render only the `BRIGHTNESS` slider in the right slot for `MUSIC` mode, establishing a clean 2-slider footer (MIC SENSITIVITY on the left, BRIGHTNESS on the right).

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TypeScript compiler passes with no type errors.

### Manual Verification
- Launch the application, navigate to the Music tab.
- Verify that the layout fits nicely within the controls container without overflowing or clipping the mic buttons.
- Confirm that only two sliders (MIC SENSITIVITY and BRIGHTNESS) render in the footer when in Music Mode.

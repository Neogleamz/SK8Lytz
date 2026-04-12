# Plan: `add-swipe-nav`

### Design Decisions & Rationale
**(Icebox — Manual Authorization Required)**

Card-level swipe navigation (Favorites ↔ Picks ↔ Presets) would replace the current tab/button navigation. A `FlatList` with `pagingEnabled` or a third-party `react-native-pager-view` is the standard approach. This is intentionally parked because the current tab system is stable and a swipe gesture could collide with horizontal color picker swipes inside the cards.

---

## ⚠️ User Review Required
> [!CAUTION]
> This is an ICEBOX item. Do not proceed without explicit authorization. The swipe gesture may interfere with horizontal sliders and color pickers inside the DockedController.

---

## Proposed Changes (If Authorized)

### [MODIFY] `src/components/DockedController.tsx`
- Identify the section that renders Favorites / Picks / Presets cards.
- Wrap in a `FlatList` with `horizontal`, `pagingEnabled`, `showsHorizontalScrollIndicator: false`.
- Add page dot indicators at the bottom.

### Collision Risk Assessment:
- Horizontal color wheel / NeonHueStrip: these use `PanResponder` — need `simultaneousHandlers` or `waitFor` refs to prevent gesture theft.
- Custom sliders (`TacticalSlider`, `CustomSlider`): same risk.

---

## Open Questions
- **Q:** Has user explicitly authorized this? (It is in the Icebox — do not proceed without "yes".)
- **Q:** If yes: do we use native `FlatList` paging or install `react-native-pager-view`?

## Verification Plan
1. Swipe between all three card types and confirm correct content loads.
2. Verify horizontal sliders inside cards still function without triggering card swipe.

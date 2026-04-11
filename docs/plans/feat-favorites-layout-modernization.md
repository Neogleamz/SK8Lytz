### Design Decisions & Rationale
We will employ "Concept A", converting the static flex grid into a single master vertical `ScrollView` containing dedicated horizontal `FlatList` rows for "YOURS" and "SK8Lytz Picks". To satisfy the requirement of fitting at least 4 cards on the screen simultaneously, we will dynamically set the card width to `(WindowWidth / 4.2)` (roughly 85px each) so 4 full cards are visible per row with a slight peek at the next card to encourage swiping. Because the cards will be much narrower, we will aggressively strip down the telemetry to just the essential Mode Icon, truncated preset Name, and the core Color Swatch, ensuring the dense layout remains clean and readable.

### Proposed Changes

#### [MODIFY] `src/components/DockedController.tsx`
- Replace the static `<View>` packing logic inside the `FAVORITES` mode active block with a `ScrollView`.
- Implement `<FlatList horizontal ...>` for the `favorites` (Yours) array.
- Implement `<FlatList horizontal ...>` for the `curatedPresets` (SK8Lytz Picks) array.
- Update the styling of `styles.presetCard` (or use inline overrides) to restrict width to approx `(width - padding) / 4.2`.
- Trim the rendering logic inside the `TouchableOpacity` card: 
  - Remove the percentage icons for brightness/speed to save horizontal space.
  - Keep the `MarqueeText` title.
  - Keep the 6px high color swatch (`fixedFgColor`/`fixedBgColor`, or `musicPrimaryColor`/`musicSecondaryColor`).
- Ensure `showsHorizontalScrollIndicator={false}` for a clean, app-like feel.

### Open Questions
None. 

### Verification Plan
**Automated Tests**:
- Run TypeScript compiler `npx tsc` to verify component props.

**Manual Verification**:
1. Open the SK8Lytz app on the `DockedController` and switch to the Favorites tab.
2. Verify "Yours" and "SK8Lytz Picks" render as independent horizontal scrolling lists.
3. Validate that you can see exactly 4 cards horizontally on the screen within one row, with the 5th slightly protruding.
4. Verify the telemetry on each card is clean (Name + Icon + Swatch) despite the compact width.

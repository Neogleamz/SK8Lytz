# Dashboard Layout Update

This plan fulfills the user's request to modernize and resize specific hierarchy slabs on the Dashboard.

## Proposed Changes

### [MODIFY] `DashboardScreen.tsx`

**1. Registered Devices Collapsibility**
- Introduce a new state `const [isFleetCollapsed, setIsFleetCollapsed] = useState(true);`.
- Wrap the entire "REGISTERED DEVICES" `<View style={styles.slabHeader}>` in a `TouchableOpacity` to toggle the collapse state.
- Add a visual indicator (a chevron `chevron-down` or `chevron-up`) to the header next to the title.
- Render the `registeredDevices.map(...)` block conditionally based on `!isFleetCollapsed`.
- This slab is already the final element in the scroll view, so it naturally sits at the bottom.

**2. Crew Hub Expansion**
- Target the "SLAB 2: CREW HUB" structure.
- Add a larger `paddingVertical` (e.g., `40`) to the container `styles.glassSlab` wrapping the crew hub details when empty, and `paddingVertical: 24` to the active `CrewPill` when connected to double its physical height natively on the screen.

**3. My Skates Card Resizing**
- Target the `styles.skateCard` and its children in the StyleSheet.
- Increase padding of the `skateCard` from `20` to `28`.
- Increase `skateCardName` font size from `18` to `24`.
- Increase `skateCardMeta` font size from `10` to `12`.
- Increase the active power icon size from `20` to `28`.

## Open Questions
- Should the `Registered Devices` list be collapsed by default or expanded by default? (I will assume collapsed by default since it cleans up the UI).

## Verification Plan
1. Start the React Native dev server.
2. Launch the app and view the Dashboard.
3. Validate that Crew Hub is visibly twice as tall.
4. Validate that My Skates cards are significantly larger.
5. Tap on the "Registered Devices" header and confirm the list smoothly collapses and expands.

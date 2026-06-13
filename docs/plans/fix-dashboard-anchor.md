# Anchor Registered Devices & Remove Dashboard Scrolling

## Design Decisions & Rationale

The user expressly requested that the Dashboard operate perfectly in the viewport without any vertical scrolling. To achieve this, the overarching `<ScrollView>` wrapping the dashboard interaction slabs must be removed entirely. We will structure the layout using a strict native Flexbox hierarchy so that the 'Registered Devices' slab dynamically pins to the absolute bottom of the container, filling the available viewport space cleanly.

## Proposed Changes

### 1. Remove `ScrollView` Element

- **File:** `DashboardScreen.tsx`
- Replace `<ScrollView style={{ flex: 1 }}...>` wrapping Slabs 2, 3, and 4 with a standard `<View style={{ flex: 1 }}>`.

### 2. Flex Distribution

- **Spacer Injection:** Add a `<View style={{ flex: 1 }} />` space between the 'My Skates' (Slab 3) and 'Registered Devices' (Slab 4) components. This native flex property will automatically absorb all remaining free space on the screen, naturally "pushing" the Registered Devices slab securely to the absolute bottom of the viewport.
- Maintain existing scaling padding on the Crew Hub and My Skates cards to maintain visibility.

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

## Verification Plan

1. Render the app in the Expo client.
2. Confirm that dragging/swiping up and down on the Dashboard executes NO scrolling behavior whatsoever.
3. Verify that the 'Registered Devices' slab rests glued precisely to the bottom of the screen regardless of the host device's screen height.

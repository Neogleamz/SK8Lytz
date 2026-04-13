# Implementation Plan - "My Skates" Dashboard Card Stylization

Upgrade the "My Skates" (Groups) dashboard card with premium styling, animations, and telemetry indicators (RSSI, Battery, Avatar) to match the "Favorites" card aesthetic.

## User Review Required

> [!IMPORTANT]
>
> - **Battery Telemetry**: The hardware does not currently report live battery levels. I will implement a visual placeholder icon as requested.
> - **Avatar**: I will fetch the current user's avatar (or color/initials) from the `ProfileService` to place in the top-left corner.
> - **Persistence**: "Last known state" (pattern name) will be tracked locally in AsyncStorage and updated whenever a group command is sent.

## Proposed Changes

### Dashboard & UI Components

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

- Integrate `ProfileService` to fetch user avatar/profile data on mount.
- Add `lastGroupPatterns` state initialized from AsyncStorage.
- Refactor the `customGroups.map` loop to render a stylized, animated `SkateGroupCard`.
- **Card Features**:
  - `LinearGradient` animated border (mimicking the "Favorites" flow).
  - Glassmorphism background (blur/translucency).
  - User avatar in the top-left.
  - RSSI signal strength indicators (bars) in the top-right.
  - Battery status icon placeholder.
  - Currently running pattern name (or "Last: [Pattern Name]").
  - Righteous font for titles.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)

- Add a notification side-effect or callback to update the parent `DashboardScreen` (and AsyncStorage) whenever a `PATTERN_CHANGED` event occurs.
- Ensure the pattern selection logic persists the name of the active effect.

### Services & Persistence

- No new services required, but `DashboardScreen` logic will be expanded to handle the overlay data.

## Open Questions

1. **Avatar Source**: If the user hasn't set an `avatar_url`, should we show a default SK8Lytz "Lyt" icon or their `avatar_color` with initials?
2. **Gradient Animation**: For the border flow, should the colors match the _active_ pattern colors (dynamic) or stick to the SK8Lytz signature neon blue/pink (static premium)?

## Verification Plan

### Automated Tests

- Verify that pattern selections in `DockedController` correctly update the "last known state" on the dashboard card.
- Check that `registeredDevices` RSSI values are correctly averaged and displayed for the group.

### Manual Verification

- Visual inspection of the card rendering: check border animation, glass effect, and element alignment (8-point grid).
- Verify avatar displays correctly (Top-Left).
- Verify RSSI bars change when proximity to hardware changes (if testing on device).

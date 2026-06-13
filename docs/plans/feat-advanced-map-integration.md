# Plan: `feat/advanced-map-integration`

### Design Decisions & Rationale

The existing `SkateMapScreen` uses `react-native-maps` for spot display but lacks live session overlays and scheduler integration. This plan adds a radius circle overlay, live session dots, and a "tap to preview" interaction for scheduled sessions — all driven by existing Supabase `crew_sessions` and `skate_spots` data.

---

## Proposed Changes

### [MODIFY] `src/components/CrewModal.tsx`

- Add a Map tab or embedded map view showing the user's current radius as a shaded circle.
- Overlay active `crew_sessions` as animated pulse dots on the map (color coded: green = joinable, grey = private/member-only).
- Tapping a dot opens the session join bottom sheet.

### [MODIFY] `src/components/LocationPicker.tsx`

- Ensure the location picker used in session scheduling shows a full map with a draggable pin instead of just text input.
- Display the chosen radius as a circle preview on the map.

### [MODIFY] `src/screens/SkateMapScreen.tsx`

- Add session count badge to each rink pin.
- Long-press on a rink pin to open a "Create session here" quick-action sheet.

---

## Open Questions

- **Q:** Is `react-native-maps` already installed and working? (Check `package.json` — likely yes given `SkateMapScreen` exists.)
- **Q:** Should the live session overlay auto-refresh on a timer or rely on Supabase real-time subscriptions?

## Verification Plan

1. Open Crew Hub and verify the map shows active sessions as dots.
2. Tap a session dot and confirm the join sheet opens.
3. Create a session and verify your own dot appears on others' maps.

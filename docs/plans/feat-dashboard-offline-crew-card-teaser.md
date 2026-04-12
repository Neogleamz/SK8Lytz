# Plan: `feat/dashboard-offline-crew-card-teaser`

### Design Decisions & Rationale
Rather than fully collapsing the Crew Hub card when offline, it should shrink to a compact "teaser" state that communicates the feature exists but is gated. This keeps the dashboard layout stable and entices users to connect. Uses conditional height animation triggered by the online/offline state.

---

## Proposed Changes

### [MODIFY] `src/screens/DashboardScreen.tsx`
- The Crew Hub slab (Slab 2 per the Master Reference 4-Slab Architecture) currently renders its full content regardless of auth state.
- Add a `Animated.Value` that interpolates the card height:
  - Online: full height ~180px
  - Offline: compact height ~64px (showing just a "📡 Connect to join a crew" row)
- The compact offline row includes a simple WiFi icon + muted text. No action buttons.

---

## Open Questions
- **Q:** Should this task be merged with `gate-offline-mode` since they affect the same Crew Hub card? Consider combining into one PR.

## Verification Plan
1. Toggle airplane mode on/off.
2. Verify the Crew Hub card animates smoothly between full and compact states.
3. Verify no layout jump occurs in the cards below it.

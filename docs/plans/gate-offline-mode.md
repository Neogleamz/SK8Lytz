# Plan: `gate-offline-mode`

### Design Decisions & Rationale
Online-only features (Crew Hub, Community Favorites, SK8Lytz Picks) should be gracefully disabled when the Supabase session is unavailable. We use the existing `isOnline` / auth context state already tracked in the app, and wrap online-only UI sections with a conditional render that substitutes an "Offline" overlay rather than fully removing the card (to avoid jarring layout shifts).

---

## Proposed Changes

### [MODIFY] `src/screens/DashboardScreen.tsx`
- Identify the Crew Hub card render block.
- Wrap it: if `!isOnline`, render the card at reduced opacity (50%) with an "📡 Offline" badge instead of hiding it entirely.
- Community Favorites section: show a "Requires connection" empty state placeholder.
- SK8Lytz Picks carousel: show a "Requires connection" empty state.

### [MODIFY] `src/components/CrewModal.tsx`
- On mount, if `!isOnline`, prevent tab navigation into the discovery/join flows and show a banner at the top: "Crew Hub requires internet. Connect to join sessions."

---

## Open Questions
- **Q:** Should the "Offline" Crew Hub card still be tappable (leading to the modal with an offline banner), or completely non-interactive?

## Verification Plan
1. Enable airplane mode on the test device.
2. Open the Dashboard.
3. Confirm Crew Hub card shows as "Offline" teaser, Community section shows empty state, and no networking calls throw errors to the console.

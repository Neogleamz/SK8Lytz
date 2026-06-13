# feat/dashboard-offline-crew-card-teaser

### Design Decisions & Rationale

We are refining the offline state of the Crew Hub dashboard card to act as a "teaser" rather than a generic error box. Currently, when `isOfflineMode` is true, the card shrinks slightly and shows a stark red "NETWORK DISCONNECTED" error. To improve the user experience and encourage online participation, we will redesign this offline state to visually hint at the features they are missing (e.g., syncing lights with friends) using a muted, glassmorphic aesthetic with a lock icon, adhering strictly to the 4-State Matrix UI rule.

## Proposed Changes

### `src/screens/DashboardScreen.tsx`

#### [MODIFY] DashboardScreen.tsx

- Replace the harsh red `#ff4444` "NETWORK DISCONNECTED" box in the Crew Hub card with a sleek, muted teaser UI.
- Use a lock icon (`lock`) and a brief teaser text like "Go online to sync lights with nearby skaters."
- Adjust the padding and styling to ensure it sits compactly but elegantly in the vertical hierarchy when offline.
- Ensure the header maintains harmony with the premium feel.

## Verification Plan

### Manual Verification

- Disconnect internet to enter offline mode.
- View the main Dashboard and verify the Crew Hub card renders the new teaser design.
- Verify it adheres to the 8-point grid.

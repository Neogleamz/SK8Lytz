# [GATE] Offline Mode Stability

## Goal

Gate off online capabilities when in offline mode (Crew Hub, Community Favorites, SK8Lytz Picks). Ensure Crew Hub card stays on dashboard but displays an "Offline" warning.

## Proposed Changes

- Implement `useNetworkStatus` hook.
- Disable 'Join' buttons in `CrewModal` when offline.
- Add "Offline Mode" banner to `CommunityFavorites`.
- Gray out `Sk8LytzPicks` feed.

## Verification Plan

- Toggle Airplane mode.
- Verify UI states update dynamically.

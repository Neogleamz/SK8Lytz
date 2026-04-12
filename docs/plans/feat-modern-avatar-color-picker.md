# Feature Integration: Modern Avatar Color Picker & Offline Crew Teaser

## Goal
To implement two closely related UI refinements:
1. **Modern Avatar Color Picker (`feat/modern-avatar-color-picker`)**: Replace the legacy hardcoded hex-color dots in `AccountModal.tsx` with the continuous, gradient-based `CustomSlider` configured for Hue selection, aligning with the Crew Hub's icon color picker.
2. **Dashboard Offline Crew Card Teaser (`feat/dashboard-offline-crew-card-teaser`)**: Redesign the "CREW HUB" section on the Dashboard so it visually shrinks into a compact teaser banner when the app is disconnected from the network, reclaiming valuable vertical screen real estate without fully hiding the feature.

### Design Decisions & Rationale
We are replacing the static grid of 15 predefined color swatches with a fluid, 360-degree Hue Slider to unlock infinite profile personalization and establish uniform UI controls across the app. For the offline Dashboard state, we dynamically scale down the Crew Hub `glassSlab` padding (from `40` to `16`) when the network drops, functioning perfectly as an informative teaser without overwhelming the UI empty state layout.

## User Review Required

> [!IMPORTANT]  
> Removing the static `AVATAR_COLORS` list means the color picker will now initialize by approximating the user's previously chosen hex color back into a Hue angle (0-360). Is there any specific behavior you want when existing users open the profile screen for the first time with an old predefined color? By default, the slider will seamlessly jump to the closest matching hue.

## Proposed Changes

### UI Components

#### [MODIFY] `AccountModal.tsx`
- **Import Update**: Add `import CustomSlider from './CustomSlider';`
- **State Initialization**: Add a helper to parse `profile.avatar_color` back to a Hue value (0-360) and introduce an `avatarHue` state to safely bind to the slider.
- **Render Replacement**: Delete the `<View style={styles.colorRow}>...` mapping of `AVATAR_COLORS`.
- **Render Insertion**: Insert the `<CustomSlider gradientTrack={true} ... />` inside the Avatar Color label block. Set the `onSlidingComplete` handler to compute and dispatch the `profileService.updateProfile({ avatar_color: newHex })`.

#### [MODIFY] `DashboardScreen.tsx`
- **Conditional Styling**: Locate the second slab (`SLAB 2: CREW HUB`).
- **Teaser Compaction**: Modify the `glassSlab` container style to use conditional height/padding. E.g., `paddingVertical: isOfflineMode ? 16 : 40`.
- **Empty State Typography**: Tighten margins so the "NETWORK DISCONNECTED" element acts gracefully as a teaser row rather than an empty void.

## Verification Plan

### Automated Tests
- TypeScript compilation to ensure `CustomSlider` props align correctly.

### Manual Verification
1. Open the Account Profile tab while online and test moving the Avatar Color slider. Ensure the avatar preview changes color appropriately and retains it upon re-opening.
2. Sever the internet connection, restart the app, and evaluate the Dashboard Crew Hub layout. Visually ensure the vertical height collapses into a sleek teaser status bar rather than a massive empty box.

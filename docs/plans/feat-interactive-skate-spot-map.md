# Implementation Plan: Interactive Skate Spot Map

This plan outlines the architecture for the "Waze of Roller Skating," a high-density Map view powered by `react-native-maps` and a hybrid data pipeline that relies on native Supabase data combined with a fallback Google Places model for crowdsourced enrichment.

## Design Decisions & Rationale
We are using `react-native-maps` (already in the project's `package.json`) to render native Google/Apple maps depending on the device. Because plotting thousands of rinks simultaneously creates serious memory and render-loop blockages, we must use a clustering algorithm. We will introduce a fast, specialized JS port of MapBox's Supercluster for this. We are also defining a dual-layer data source (our Supabase DB for rich skating metadata + a read-only OSM/Google fallback) to solve the "empty map" cold-start problem.

---

## 📦 Dependency Diet Proposal
Before adding any packages, please review the following justification:

**Proposed Library 1:** `react-native-map-clustering` (and its peer `supercluster`)
- **Weight**: Extremely light (~40kb minified).
- **Activity**: Heavily maintained and widely used as the default clustering solution for `react-native-maps`.
- **Necessity**: Attempting to render > 500 MapMarker elements in React Native without clustering will crash older Android devices and stutter the main UI thread. A spatial index tree (like Supercluster) is mandatory for high-density map exploration.
- **Alternatives**: We could write our own quad-tree clustering algorithm from scratch in JS, but it would likely be less performant than `supercluster` and introduce significant overhead to test. I highly recommend using the standard library.

> [!WARNING]
> Please explicitly approve the `react-native-map-clustering` package when you respond to this plan.

---

## Proposed Changes

### Database Layer
#### [NEW] Supabase Migration: `add_skate_spots_table` (via MCP)
We will execute an MCP database migration to create the foundational `skate_spots` table.
- **Table:** `skate_spots`
- **Columns:**
  - `id` (UUID, PK)
  - `name` (Text)
  - `lat` (Float8)
  - `lng` (Float8)
  - `surface_type` (Enum: 'wood', 'concrete', 'asphalt', 'sport_court', 'unknown')
  - `is_indoor` (Boolean)
  - `adult_night_details` (Text, nullable)
  - `source` (Text: e.g. 'native', 'google_fallback')
  - `is_verified` (Boolean)
- *Note: We will automatically run `generate_typescript_types` after the migration.*

---

### Service Layer
#### [NEW] `src/services/SkateSpotsService.ts`
- Functions to fetch `skate_spots` within a geographical bounding box.
- Function `claimAndUpdateSpot` to write back crowdsourced metadata when a user "claims" an unverified location.
- **Note:** We will integrate OSM Nominatim / Google Places API directly via Axios for the fallback layer (using the same endpoint seen in `LocationPicker.tsx`).

---

### UI & Platform Strategy
#### [NEW] `src/screens/SkateMapScreen.tsx`
- **Full Viewport Map**: Utilizes `<MapView>` wrapped in the clustering provider.
- **Responsive Layout**: Uses Flexbox to ensure the map fills the remaining safe-area on all devices.
- **Platform Parity**: `react-native-maps` handles the platform split (Google Maps on Android, MapKit on iOS) automatically. Touch targets for the markers will be artificially padded (44x44) to ensure they are easily tappable while skating or wearing gear.

#### [NEW] `src/components/SkateSpotBottomSheet.tsx`
- The slide-up UI panel that appears when tapping a marker.
- **4-State Matrix Handled**:
  - *Loading*: Skeleton outlines for details while verifying claims.
  - *Empty/Unverified*: Prompts the user with a glowing "Verify this Spot" CTA.
  - *Error*: "Failed to load details" text with a retry icon.
  - *Success*: Rich display of `surface_type` and `adult_night_details`.

---

## Verification Plan

### Automated Tests
- Run `tsc --noEmit` to ensure the generated Supabase types are perfectly mirrored by `SkateSpotsService.ts`.
- Ensure NO `any` types leak into the cluster calculations.

### Manual Verification
- Render the `SkateMapScreen` via the browser subagent in the Expo Web setup.
- Verify map clustering combines 10+ fake localized points into a single numbered cluster icon.
- Tap a fake spot to dynamically verify the `SkateSpotBottomSheet` sliding state and UI form. 

I have generated the plan artifact. Review the plan above. Type 'proceed' to execute, or provide feedback.

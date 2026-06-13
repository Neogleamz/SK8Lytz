# feat/dashboard-offline-and-feature-gating

### Design Decisions & Rationale

We are combining related visual/logic UI enhancements into a single "Feature Gating & Offline Support" pass. This includes:

1. **Crew Card Teaser:** Refining the "NETWORK DISCONNECTED" state to a sleek, 4-state matrix friendly "teaser" state.
2. **Offline Logic Gating (`gate-offline-mode`):** Explicitly locking down Community Favorites, Picks, and Crew features when no network is present.
3. **App Settings Manager (New):** Adding a tab to `AdminToolsModal` that reads/writes to the existing Supabase `app_settings` table. This allows us to remotely toggle entire features off/on (e.g. `feature_crew_hub_enabled`, `feature_community_enabled`) globally, and set overarching App/Product defaults, directly from the app.
4. **Auth Scrolling (`fix/auth-page-scrolling`):** Quick flexbox fix to prevent vertical bounce on the login page.

## Proposed Changes

### `src/services/AppSettingsService.ts` (NEW)

#### [NEW] AppSettingsService.ts

- Create a lightweight Supabase service to `getSettings()` and `updateSetting()`.
- Define a strict TypeScript type/interface for valid feature flags (e.g. `global_crew_hub_locked`, `global_community_hub_locked`).

### `src/components/AdminToolsModal.tsx`

#### [MODIFY] AdminToolsModal.tsx

- Add a new "Settings" button (`⚙️ App Settings`) beneath the Product Manager.
- Build a new Modal screen rendering an interface to toggle the feature flags stored in `app_settings`.
- Provide input fields to override global parameter defaults.

### `src/screens/DashboardScreen.tsx`

#### [MODIFY] DashboardScreen.tsx

- Integrate `AppSettingsService.ts` to dynamically gate slabs (Crew Hub, Picks) if the admin has toggled them off in reality.
- Integrate the active `isOfflineMode` state to show the offline layout natively when disconnected.
- Replace the harsh red `#ff4444` "NETWORK DISCONNECTED" box in the Crew Hub card with a muted lock-icon teaser "Go online to sync lights with nearby skaters."

### `src/screens/AuthScreen.tsx`

#### [MODIFY] AuthScreen.tsx

- Fix vertical flex properties to strictly contain the UI in a `flex: 1` non-scrollable box, ensuring the "Continue Offline" button sits flush at the bottom.

## Verification Plan

### Automated & Manual Verification

- Turn off network on device; verify Auth Page continues to allow offline mode without scrolling.
- View Dashboard; verify Crew Hub card has the new 8-point teaser UI instead of the red error box.
- Connect network; open Admin Tools modal. Toggle "Lock Community Hub" to true. Refresh dashboard and verify the Community Hub slab updates to indicate it is locked by an admin.

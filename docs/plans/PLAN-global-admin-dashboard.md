# Telemetry Cockpit & Control Tower

Build the "Telemetry Cockpit & Control Tower" Global Admin Dashboard for SK8Lytz using the existing CCTower Docker container and the live Supabase data. The goal is to move global app configurations out of the mobile client and visualize real telemetry.

## Resolved Open Questions
1. **Authentication**: Uses Supabase Auth (validates if user has an admin role/row).
2. **Architecture**: Runs as a new Vite/React app on a new port within the existing CCTower container (scraper will eventually fold into this).

## Proposed Changes

### 1. Database Mapping (Zero Placeholder Policy)
We will map UI widgets directly to the existing `supabase.ts` schema. No new tables are required for V1.

**Widget 1: Global Live Sessions Map (Fleet Ops Center)**
- **Table**: `crew_sessions`
- **Fields**: `location_coords`, `top_speed_mph`, `is_active`, `total_distance_miles`
- **Visual**: A live Mapbox/Leaflet instance showing where skaters are actively broadcasting sessions, with hover tooltips for speed and distance.

**Widget 2: Device Fleet Health & Fragmentation**
- **Table**: `discovered_devices_telemetry` and `registered_devices`
- **Fields**: `firmware_ver`, `ble_version`, `product_type`, `ic_type`
- **Visual**: Doughnut charts showing the exact distribution of firmware versions and hardware ICs in the wild.

**Widget 3: App Performance & OS Analytics**
- **Table**: `parsed_session_stats`
- **Fields**: `average_load_time_ms`, `battery_level`, `os_name`, `os_version`, `total_memory_mb`
- **Visual**: Line charts tracking app load time grouped by OS version. Scatter plots checking for battery drain correlation.

**Widget 4: Feature Flags & Global Settings (Control Tower)**
- **Table**: `app_settings` and `feature_flags`
- **Fields**: `setting_key`, `setting_value`, `is_enabled`
- **Visual**: A clean list of admin toggle switches. Changing `hw_setup_rssi_threshold` here will instantly propagate to the mobile app via Supabase Realtime.

**Widget 5: Hardware Access Control**
- **Table**: `hardware_blacklist`
- **Fields**: `mac_address`, `reason`
- **Visual**: A data grid to immediately ban specific cloned or malfunctioning MAC addresses globally.

**Widget 6: User Management & Profiles**
- **Table**: `user_profiles` (and `admin_audit_logs`)
- **Visual**: A user management grid to view registered app users, reset passwords, and assign admin roles, fully migrating this capability off the mobile device.

### 2. Dashboard Shell
#### [NEW] `admin-dashboard/` (or integrated into CCTower)
We will scaffold a modern React app (Vite or Next.js) tailored for data visualization within the CCTower Docker stack.
- `src/components/widgets/MapWidget.tsx` (Reads `crew_sessions`)
- `src/components/widgets/FleetHealthWidget.tsx` (Reads `parsed_session_stats`)
- `src/pages/SettingsControl.tsx` (Mutates `app_settings` and `feature_flags`)
- `src/pages/UserManagement.tsx` (Mutates `user_profiles` and roles)
- **Theme**: Dark mode, glassmorphism CSS, Recharts for rendering graphs.

### 3. Mobile App Deprecation (Bundle Reduction)
- **Action**: Delete the existing User Profile and Admin Tools UI components from the SK8Lytz React Native codebase.
- **Benefit**: Removes heavy UI dependencies from the mobile bundle, fully migrating control to this new web interface.

## Verification Plan

### Automated Tests
- N/A (Web UI visualization layer)

### Manual Verification
1. Boot the dev server for the new dashboard on the new container port.
2. Verify the map correctly plots points from `crew_sessions.location_coords`.
3. Verify the `parsed_session_stats` graph accurately groups load times.
4. Toggle a feature flag in the web UI and verify the Supabase `feature_flags` table is successfully updated.

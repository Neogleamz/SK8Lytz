# Dashboard "No-Scrolling" Redesign (Revised Hierarchy)

We are refactoring the SK8Lytz Dashboard into a high-fidelity, single-page interface centered around observability. The layout follows a strict "Vertical Slab" hierarchy to ensure full-page visibility without scrolling.

## Design Decisions & Rationale

1. **Terminology Correction**:
   - **Crew Hub**: Specifically for multi-user social sessions (Join/Pulse active sessions).
   - **Skates (Groups)**: The primary hardware controls for the user's paired LED sets (HALOZ/SOULZ pairs).
2. **Registered Devices Only**: We are removing the "Available Devices" (un-vetted Bluetooth noise) from the main dashboard. This declutters the UX. Users will trigger the `Setup Wizard` via a dedicated button to find new hardware.
3. **Vertical Slab Hierarchy**:
   - **Slab 1 (Header)**: Logo + Pulsing Polling Indicator.
   - **Slab 2 (Crew Hub)**: Glassmorphism container for Session discovery and current session state.
   - **Slab 3 (Skates / Groups)**: High-impact cards for your paired hardware sets (e.g., "My Haloz") with background pattern previews.
   - **Slab 4 (My Devices)**: A compact list of registered individual devices and the `+ ADD DEVICE` action button.

## Proposed Changes

### UI Components

#### [MODIFY] [DashboardSlabs.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DashboardSlabs.tsx) (New file design)

- **`CrewHubSlab`**: Redesign to focus on session-level "Join" pills and active rink status.
- **`SkatesGroupSlab`**: Replaces the generic "Active Groups". These cards will show the pair of skates as a unified visual unit.
- **`RegisteredFleetSlab`**: Replaces the mixed device list. Strictly filters to `registeredDevices` from the `useRegistration` hook.

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

- Pivot the `allDevices` filtering logic.
- Completely remove the `availableDevices` section from the main view.
- Update the layout to follow: [Header] ➔ [Crew Hub] ➔ [Skates] ➔ [Device List].

## AI Generated Mockup (The "Hierarchy" Vision)

```carousel
![Style 3: Optimized Slab Dashboard with Hardware Hierarchy](/C:/Users/Magma/.gemini/antigravity/brain/39ad188a-38fa-400e-9f7b-11988c9e263a/dashboard_v3_slab_layout_1775809491626.png)
```

## Open Questions

None. The hierarchy is now perfectly aligned with your feedback.

## Verification Plan

1. **Navigation**: Ensure clicking "+ Add Device" correctly launches the `HardwareSetupWizardScreen`.
2. **Filtering**: Verify that a brand new, un-registered device does NOT appear in the dashboard list, but DOES appear in the Wizard.
3. **Responsiveness**: Validate that all four slabs fit on a standard iPhone/Android screen without needing to scroll.

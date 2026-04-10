# Dashboard "No-Scrolling" Redesign (Task #feat/dashboard-redesign)

We are refactoring the SK8Lytz Dashboard into a high-fidelity, single-page interface centered around observability. The goal is to eliminate vertical scrolling while maximizing information density for Crew and Group management.

## Design Decisions & Rationale

1. **Vertical Slab System**: The screen will be divided into 4 fixed-height segments ("Slabs") using Flexbox. This ensures the UI remains responsive and contained within any device aspect ratio without overflow.
2. **Horizontal Content Containers**: To support unlimited devices/groups without vertical scrolling, we will implement horizontal paging within the `Active Groups` and `My Devices` slabs.
3. **Immersive Patterns**: Active Groups will not just show text; the entire card will feature a "Ribbon Visualizer" reflecting the actual colors and speeds being sent to the hardware.
4. **Status-First Header**: The radar scanner is relegated to a small, pulsating neon glow next to the system status in the header to indicate auto-polling is active.

## Proposed Changes

### UI Components

#### [NEW] [DashboardSlabs.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DashboardSlabs.tsx)
Create a new set of optimized component "Slabs" to keep `DashboardScreen.tsx` clean:
- **`CrewHubSlab`**: Large Glassmorphism card. Layout: [Avatar Strip] | [Session Info] | [Leader Status].
- **`GroupVisualSlab`**: Horizontal list of Group cards. Each card acts as a massive "Click-to-Connect" button with background pattern previews.
- **`DeviceFleetSlab`**: Compact bottom strip for "My Devices" and "Available Devices" with a bold neon `+ ADD DEVICE` action button.

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- Remove the legacy `FlatList` wrapper.
- Root container becomes `View style={{ flex: 1 }}`.
- Replace the legacy rendering loop with the 4-Slab layout.
- Bind the `SCANNING...` pulse to the `isScanning` state in the header.

#### [MODIFY] [theme.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/theme/theme.ts)
- Add `SecondaryBackground: 'rgba(255, 90, 0, 0.05)'` for Glassmorphism effects.

## AI Generated Mockup (The "No-Scrolling" Vision)

````carousel
![Style 3: Optimized Slab Dashboard](/C:/Users/Magma/.gemini/antigravity/brain/39ad188a-38fa-400e-9f7b-11988c9e263a/dashboard_v3_slab_layout_1775809491626.png)
````

## Open Questions

> [!IMPORTANT]
> 1. **Avatar Icons**: For the "Crew Hub", do you want us to use real user photos (from profile service) or should we generate standard SK8Lytz geometric neon skater icons for now?
> 2. **Device List Density**: If there are many available devices, do you prefer a horizontal scroll strip at the bottom, or showing only the "Top 3" with a "See All" modal?
> 3. **Docked Controller**: Should the controller slide *over* the dashboard (standard) or should the dashboard "squeeze" slightly when the controller is open?

## Verification Plan
1. **Layout Check**: Verify the screen does not scroll on iPhone SE (small) and iPhone 15 Pro Max (large).
2. **Tab Transition**: Verify clicking an Active Group card instantly triggers the `DockedController` slide-up.
3. **Dynamic States**: Verify the "Scanning" pulse activates/deactivates based on the `isScanning` BLE state.

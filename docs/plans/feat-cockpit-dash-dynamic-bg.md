# [PLAN] feat/cockpit-dash-dynamic-bg (The Cockpit Experience)

### Design Decisions & Rationale
We are moving the Dashboard from a "Remote Control" to a "Sensing Cockpit." The dashboard background will dynamically pulse and shift colors in sync with the physical hardware state, and the Street Mode will receive a "Performance Facelift" to feel like a high-speed telemetry deck.

## Proposed Changes

### [Component Name] Aesthetic Engine

#### [NEW] [components/DynamicDashboardBg.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DynamicDashboardBg.tsx)
- A performant, low-opacity Canvas or SVG background that tracks the `activePalette` from the `ThemeContext`.
- Implements a subtle "Pulse" animation synced to the `activeMode` speed (if hardware is active).

#### [MODIFY] [StreetMode.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/StreetMode.tsx)
- Implement "Cockpit Aesthetics": Large, rounded gauges, glass-morphism overlays, and high-intensity "Warning" states when reaching peak speeds or low battery.
- Integrated the `DynamicDashboardBg` to provide a "Glow" that surrounds the telemetry data.

## Verification Plan
1. **Performance Audit**: Use the `Flashlight` component or similar to verify that the background animation doesn't drop the UI below 60fps.
2. **Context Sync**: Change the color of the physical skates (via BLE); verify the dashboard background updates its hue within 100ms.
3. **Glanceability Test**: Verify that the Street Mode telemetry is readable at a distance of 3 feet (skating posture).

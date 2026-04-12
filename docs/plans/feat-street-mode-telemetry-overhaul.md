# Plan: Street Mode Telemetry Overhaul (`feat/street-mode-telemetry-overhaul`)

### Design Decisions & Rationale
We are moving away from the "Recording" paradigm to a "Live Dashboard" experience. The Street mode should feel like a performance instrument cluster. By extracting `AnalogGauge` and isolating `StreetDashboard`, we keep the codebase modular and ensure UI elements resize perfectly on all mobile devices.

## Proposed Changes

### 1. Extract & Refactor Gauges
- **[NEW] `src/components/AnalogGauge.tsx`**:
  - Extract SVG logic from `DockedController.tsx`.
  - Use `viewBox` for auto-scaling.
  - Implement a "Glassmorphism" dial effect with `Righteous` font.

### 2. Street Mode UI Overhaul
- **[NEW] `src/components/StreetDashboard.tsx`**:
  - **Always-On Telemetry**: Auto-trigger `SpeedTrackingService` session on mount (if in Street Mode).
  - **Gauge View**: Speed (Large) + G-Force (Small) side-by-side.
  - **Data Grid**: 2x3 grid showing:
    1. Trip Distance (Current)
    2. Trip Time
    3. Avg MPH (Session)
    4. Top Speed (Session)
    5. Lifetime Distance
    6. Lifetime Peak Speed
  - **Visuals**: Use `LinearGradient` backgrounds, neon shadows, and high-contrast labels.

### 3. Logic Integration
- **[MODIFY] `src/components/DockedController.tsx`**:
  - Replace the old `STREET` render block with `<StreetDashboard />`.
  - Pass the necessary sensor state (`gpsSpeed`, `peakGForce`, `motionState`) down.
  - Boy Scout cleanup: Fix `any` types in props and product refs.

## Verification Plan
- **Manual Test**: Confirm the session timer starts immediately upon entering Street mode.
- **Visual Check**: Open the app on different simulated screen sizes to verify gauge scaling.
- **Database Sync**: Ensure Lifetime stats populate correctly from `SpeedTrackingService`.

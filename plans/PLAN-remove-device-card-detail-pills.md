# PLAN-remove-device-card-detail-pills.md — Remove Device Card detail pills

Remove the `icType` and `sorting` pills from the device card status pills layout to clean up the visual representation.

## Proposed Changes

### UI Component

#### [MODIFY] [HardwareStatusPills.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/HardwareStatusPills.tsx)
- Remove the rendering of the `strip` (icType) and `sort` (color sorting) pills in the `HardwareStatusPills` component return block.

---

## Verification Plan

### Manual Verification
- Render the dashboard and verify that registered and scanned device cards display only the LED count and segment count pills, and do not show `icType` (e.g. `WS2812B`) or `sorting` (e.g. `GRB`) pills.

# [PLAN] feat/impact-sentinel-safety (The Flare Standard)

### Design Decisions & Rationale

Skaters are most vulnerable immediately after a fall, especially in dark street environments. We are implementing **Impact Detection** using the phone's accelerometer. If a high-G spike is detected followed by a sustained period of zero motion, the app will trigger an emergency "Flare" mode on all connected hardware to ensure the skater is visible to traffic and nearby crews.

## Proposed Changes

### [Component Name] Kinetic Safety Monitor

#### [NEW] [KineticSafetyService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/KineticSafetyService.ts)

- **Impact Sensor**: Monitor `Accelerometer` for spikes > 3G (threshold TBD).
- **Post-Impact Watchdog**: If a spike occurs, wait 10 seconds. If motion remains < 0.1G, trigger `EMERGENCY_FLARE`.

### [Component Name] Hardware Override

#### [MODIFY] [ZenggeController.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ZenggeController.ts)

- Implement `triggerEmergencyFlare`: High-priority override that dispatches a Maximum Brightness White Strobe to all MACs, bypassing current mode settings.

## Verification Plan

1. **False Positive Test**: Rapidly shake the phone (simulating "dancing" or "pumping") to ensure we don't trigger a Flare accidentally.
2. **Impact Simulation**: Set the phone down sharply (safely) on a padded surface and stop moving. Verify the Flare triggers after the 10s watchdog.
3. **Cancellation UX**: Implement a "False Alarm" button that appears on impact, allowing the user to abort the Flare before it dispatches.

# Implementation Plan: Kinetic Brake Light Integration

This plan details the implementation of accelerometer-driven safety lights, which pulse the skates RED upon detecting sudden deceleration (braking).

## User Review Required

> [!WARNING]
> **Battery Consideration**: Constant accelerometer polling can impact device battery life. We will implement "Drive Detection" to only enable high-frequency polling when the skates are actively connected and in motion.

## Proposed Changes

### [Services]

Implementing the movement engine.

#### [NEW] [KineticService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/services/KineticService.ts)

- Initialize `Accelerometer` from `expo-sensors`.
- Set update interval to 100ms.
- Implement a **Brake Detection Algorithm**:
  - Monitor the Y-axis (or Z, depending on orientation) for significant negative G-force.
  - Threshold: TBD (likely -2.0g to -3.0g).
- Dispatch an event: `EVENT_BRAKE_DETECTED`.

### [Hardware Control]

Triggering the override.

#### [MODIFY] [ZenggeController.js](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/services/zenggeController.js)

- Clear existing patterns when `EVENT_BRAKE_DETECTED` is received.
- Send `0x71, 0x22` (Priority Red Solid) or a custom 0x51 payload.
- Auto-resume previous state after 1500ms.

### [UI Components]

User toggle for the safety feature.

#### [MODIFY] [DeviceSettingsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/DeviceSettingsModal.tsx)

- Add "Kinetic Brake Lights" toggle switch.

## Verification Plan

### Manual Verification

1. Enable "Kinetic Brake Lights".
2. Set skates to "Rainbow Mode".
3. Move the phone quickly and stop abruptly.
4. Verify the skates pulse RED and then return to Rainbow.

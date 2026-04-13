# [PLAN] hw-test/proximity-magic-tap (The Magic Tap)

### Design Decisions & Rationale

To eliminate the friction of manual device selection, we are investigating **Proximity-Gated Identification**. By monitoring the RSSI (Received Signal Strength Indicator) in real-time, the app can identify when a phone is physically touched to a skate (within 1-2 inches) and automatically open the controller for that specific hardware unit.

## Proposed Strategy

### Phase 1: RSSI Threshold Calibration

- Conduct hardware tests to identify the "Contact Decibel" (e.g., RSSI > -40dBm).
- Account for signal "Bounce" and environmental interference in high-metal environments (rinks).

### Phase 2: The "Magic Handshake"

- **Hardware Feedback**: On detection, send a 500ms White Glow (0x59) to the skate to visually confirm the "Tap."
- **UI Deep-Link**: Automatically transition the `DashboardScreen` to the `DockedController` for the tapped MAC address.

## Verification Plan

1. **Distance Accuracy**: Use a physical tape measure to verify that the "Magic Tap" only triggers within a 2-inch radius, preventing accidental triggers from nearby skaters.
2. **Battery Impact**: Ensure the "High-Pulse Scan" only activates when the user is on the Dashboard and the screen is on.
3. **Collision Test**: Verify the app handles "Double Taps" (contacting two skates simultaneously) gracefully (e.g. opens the Group controller).

# Epic: Standalone Wear OS Companion App

## Overview
Create a native Wear OS companion application for SK8Lytz that allows users to control their skate lights directly from their wrist, eliminating the need to carry a mobile device during active skate sessions.

## The Vision: "Micro-Controller on the Wrist"
Skaters often wear wrist guards and cannot safely pull out a phone while rolling. A smartwatch companion solves this friction by providing a high-contrast, simple interface with massive tap targets:
- **Quick Actions:** Tap to toggle power, swipe to change modes, rotate bezel/crown to adjust brightness.
- **Biometric Integration:** Continuous, high-fidelity (1Hz) heart-rate monitoring natively on the wrist to drive pulsing LED patterns in real-time.
- **Motion Events:** Trigger specific strobes or visualizer effects based on the watch's accelerometer/gyro (e.g., detecting jumps, spins, or sudden stops).

## Architectural Strategy (The "Phone-less" Approach)
The watch will connect to and control the ZENGGE hardware directly via its own Bluetooth Low Energy (BLE) radio.

### Phase 1: Native Wear OS Foundation
- **Tech Stack:** Kotlin, Jetpack Compose for Wear OS (React Native is not viable for a standalone Wear OS experience).
- **Setup Flow:** The phone app must be used for initial setup and device discovery. The phone will securely transmit the saved ZENGGE MAC addresses, configuration data, and current user profile to the watch via the Wearable Data Layer API.

### Phase 2: The Bluetooth Port (The "Kotlin Tax")
- **Protocol Porting:** The entire `ZenggeProtocol.ts` (0x51 payloads, 0x40 chunked framing, checksum math, etc.) must be ported to Kotlin.
- **BLE Management:** Implement a robust background BLE service on the watch to manage GATT connections to both the left and right skates simultaneously.

### Phase 3: Mitigating the Tradeoffs
1. **Multi-Connection Bottleneck:** ZENGGE hardware only supports one BLE connection. A "Handoff Protocol" must be designed so the phone and watch don't fight for control. When the watch app opens, it connects; if the phone app opens, it commands the watch to disconnect via the Data Layer API.
2. **Battery Drain:** Aggressive BLE optimization is required. The watch should only maintain the connection while the SK8Lytz app is actively tracking a session, and immediately drop GATT when paused.

## Alternative/Fallback Strategy: "The Bridge"
If battery drain or protocol-porting proves too costly, the watch app can be downgraded to a "remote control" that merely sends JSON commands via the Data Layer API back to the phone (which remains in the user's pocket and handles all BLE communication). This avoids the Kotlin protocol rewrite but requires the phone to be nearby.

## Next Steps Before Execution
- [ ] Determine if Apple Watch (watchOS/SwiftUI) requires parity at launch.
- [ ] Prototype a basic BLE GATT connection from a Wear OS emulator to a ZENGGE controller to test latency and battery draw.
- [ ] Finalize the "Handoff" UX between the phone and watch.

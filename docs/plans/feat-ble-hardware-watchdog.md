# [PLAN] feat/ble-hardware-watchdog (The Self-Healing Standard)

### Design Decisions & Rationale

Bluetooth LE hardware is prone to "Silent Failures" where the GATT connection is alive but the underlying microcontroller has soft-locked. We are implementing an **Autonomous Hardware Watchdog** that monitors the "Pulse" of the hardware and performs silent re-latches to ensure 100% control reliability.

## Proposed Changes

### [Component Name] Watchdog Orchestrator

#### [NEW] [HardwareWatchdogService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/HardwareWatchdogService.ts)

- **Pulse Check**: Every 30 seconds, dispatch a non-intrusive 0x63 (Query) to all active devices.
- **Health Delta**: Track the "Response Latency" for each device.
- **Heuristic Recovery**: If a device fails 2 consecutive queries while "Connected," trigger an `AUTO_RECOVER` event.

### [Component Name] Recovery Loop

#### [MODIFY] [ZenggeController.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ZenggeController.ts)

- Implement `silentRelatch`: Forcefully disconnect and reconnect to the target MAC, then re-dispatch the the active session state (using our Eternal Glow cache).

## Verification Plan

1. **Silent Recovery Test**: Manually interfere with a hardware controller (e.g. simulate a soft-lock); verify the app detects the unresponsive state and re-establishes control within 60 seconds without user input.
2. **Battery Optimization**: Ensure the 0x63 heartbeat doesn't wake the radio too aggressively (use the existing connection context).
3. **Admin Visibility**: Expose the "Watchdog Events" (Retries/Relatches) in the Admin Hub's TIMELINE tab for diagnostics.

# HW Power HUD & RF Remote Discovery Plan

This plan addresses the missing Power control in the main UI and the discovery of paired RF Remote IDs to match Zengge app parity.

### Design Decisions & Rationale

We are implementing a full-stack parity check for Power and RF Remote IDs. By adding a dedicated Power Toggle to the main controller, we provide the user with absolute control over hardware standby. By extending the 0x2B parser, we surface "hidden" hardware metadata (Remote IDs) that ensures the user can verify their RF ecosystem.

## User Review Required

> [!IMPORTANT]
> **Power Standby Logic**: When Power is toggled OFF, I will implement a "Standby Overlay" on the DockedController. This prevents accidental slider movements from waking up the device until Power is explicitly toggled back ON.

## Proposed Changes

### [DockedController]

Implementation of the primary Power Toggle and reactive UI dimming.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/DockedController.tsx)

- Add `powerState` local state (or use prop).
- Add a large Power Icon button in the top header.
- Implement an `isPoweredOff` overlay that dims the control surface.
- Dispatch `0x71, 0x23` (ON) and `0x71, 0x24` (OFF) via `writeToDevice`.

---

### [Zengge Protocol]

Hardware command expansion for Power and Remote Identification.

#### [MODIFY] [ZenggeProtocol.ts](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/protocols/ZenggeProtocol.ts)

- Add `powerOn()` and `powerOff()` static methods.
- Update `parseRfRemoteState` to extract 4-byte Hex IDs from the `0x2B` response buffer.
- Map `pairedRemoteIds: string[]` in the return object.

---

### [Device Settings]

UI update to display discovered Remote IDs.

#### [MODIFY] [DeviceSettingsModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App\SK8Lytz/src/components/DeviceSettingsModal.tsx)

- Add a new "Paired Remotes" list in the RF section.
- Display IDs in a "tag" format (e.g., `⚡ ID: AF23`).

## Open Questions

1. **Remote Capacity**: Does your hardware support pairing multiple remotes (e.g. 2 or 3)? I will build the parser to handle a list of IDs.
2. **RF Remote Sync**: Do you want the App to "listen" for RF remote power commands? (Note: The hardware doesn't always broadcast RF-triggered changes via BLE, but we can try to query state after an RF event if detected).

## Verification Plan

### Automated Tests

- `scratch/rf-state-dump.ts`: A script to simulate/capture 0x2B responses and verify the ID parser returns correct HEX formatting.

### Manual Verification

1. Open Dashboard -> Connect Skates.
2. Open Controller -> Tap Power Button (Verify Skates go black).
3. Open Device Settings -> Tap 'QUERY STATE' (Verify Paired IDs appear).
4. Press physical RF Remote button -> Verify ID in app matches (if possible).

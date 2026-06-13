# [feat/hardware-telemetry-deep-dive] ZENGGE Hardware Telemetry & RF Mapping

## Goal
Conduct a deep research session into `0x62` and `0x63` opcodes, as well as `0xFF` BLE Advertisements, to ensure accurate bidirectional mapping of hardware settings including led points, segments, sorting type, IC type, firmware version, product ID, and paired remotes.

## User Review Required
> [!IMPORTANT]
> The RF Remote configuration ("allow all / allow paired") is handled at the Flutter UI layer via `MethodChannel` payloads, bypassing the native BLE Java code entirely. We cannot send a simple `0x64` hex packet. We must intercept the Flutter JSON event payload directly to replicate this.

## Proposed Changes

### ZENGGE Protocol Bible & Master Reference Updates
- Map the BLE advertisement Manufacturer Data payload (`type 0xFF`) that continuously broadcasts:
  - `Firmware Version` (byte 12 & 14)
  - `BLE Version` (byte 3)
  - `Product ID` (byte 10 & 11)
  - `MAC Address` (bytes 4-9)
- Document the `0x63` Read Query: `[0x63, 0x12, 0x21, 0x0F, checksum]`. Returns 12 bytes. LED points are Little-Endian swapped.
- Document the `0x62` Write Command: `[0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, checksum]`. Uses Big-Endian for points.

### Code Implementation (`useBLE.ts` & `ZenggeProtocol.ts`)
- Upgrade `ZenggeProtocol.ts` to natively parse the `0xFF` BLE Manufacturer Specific Data. This provides instant, passive identification of the Firmware and Product ID without needing an active `0x63` query.
- Add robust `0x63` parsing that specifically handles the Little-Endian swap for LED points so the UI doesn't display garbled LED counts.
- Add the `0x62` writer function handling the Big-Endian flip for writing.

## Verification Plan
- Build APK and read back the Manufacturer Data on the Android logcat.
- Compare our parsed `firmwareVer` and `productId` with the Official ZENGGE app UI.
- Verify that setting LED points to `150` via our UI correctly translates to the device and persists through a reboot.

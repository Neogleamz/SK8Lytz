# 🛡️ Micro-Doc: The Hardware Layer (BLE)

> **CRITICAL AI CONTEXT**: If you are editing files in `src/services/ble/`, you MUST read this first. This defines our BLE constraints.

## The Co-Location Law
**`DashboardScreen.tsx` is the sole owner of the BLE pipeline.**
You are strictly forbidden from moving the following out of DashboardScreen:
- `useBLE` (the host connection)
- `connectedDevices` state
- `writeToDevice` function
- `setOnDataReceived` listeners

We use the "Hollow Shell" pattern: UI components are passed a memoized `dockedBus` that contains references to these functions. **Do not attempt to lift BLE state into Redux or Context.**

---

## MTU Fragmentation (`writeChunked`)
- Standard BLE MTU is small.
- Any payload over **54 pixels** (approx 160 bytes) MUST be chunked using the `0x40` framing protocol.
- `useBLE.ts` provides `writeChunked()`. This wraps payloads automatically.

**0x40 Framing Format**:
1. **Header Chunk**: `[0x40, total_hi, total_lo, checksum]`
2. **Data Chunks**: `[0x41, seq_num, ...chunk_data]` (seq_num starts at `0x00`)
3. **Footer Chunk**: `[0x42, crc_hi, crc_lo]` (CRC16 of the entire unchunked payload)

Never use `bleManager.writeCharacteristic` directly for `0x59` or `0x51` payloads. Always route through `writeChunked`.

---

## Auto-Recovery & Reconnect Replay
- Skates disconnect constantly (crashes, distance, sleep).
- The `useDeviceStateLedger` hooks intercept the last known good state.
- When `useDashboardAutoConnect` successfully recovers a dropped skate, the Dashboard wait 300ms, then replays the last known `rawPayload` automatically.
- Do not build custom retry logic inside UI components. Let the ledger handle it.

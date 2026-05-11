# 🛡️ Micro-Doc: The Hardware Layer (BLE)

> **CRITICAL AI CONTEXT**: If you are editing files in `src/services/ble/`, you MUST read this first.

## The Co-Location Law
**`DashboardScreen.tsx` is the sole owner of the BLE pipeline.**
You are strictly forbidden from moving the following out of DashboardScreen:
- `useBLE` (the host connection)
- `connectedDevices` state
- `writeToDevice` function
- `setOnDataReceived` listeners

We use the "Hollow Shell" pattern: UI components (like `ProEffectsPanel` or `DashboardCrewPanel`) are passed a memoized `dockedBus` that contains references to these functions. **Do not attempt to lift BLE state into Redux or Context.**

## MTU Fragmentation (`writeChunked`)
- Standard BLE MTU is small.
- Any payload over **54 pixels** (approx 160 bytes) MUST be chunked.
- `useBLE.ts` provides `writeChunked()`. This wraps payloads in a `0x40` framing protocol:
  - Header chunk: `[0x40, total_hi, total_lo, checksum]`
  - Data chunks: `[0x41, seq, data...]`
  - Footer chunk: `[0x42, crc_hi, crc_lo]`
- Never use `bleManager.writeCharacteristic` directly for 0x59 or 0x51 payloads. Always route through `writeChunked`.

## Auto-Recovery & Reconnect Replay
- Skates disconnect constantly (crashes, distance, sleep).
- The `useDeviceStateLedger` hooks intercept the last known good state.
- When `useDashboardAutoConnect` successfully recovers a dropped skate, the Dashboard wait 300ms, then replays the last known `rawPayload` automatically.
- Do not build custom retry logic inside UI components. Let the ledger handle it.

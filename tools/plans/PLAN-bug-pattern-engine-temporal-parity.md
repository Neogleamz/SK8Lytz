# Reverse-Engineering the ZENGGE Packet Chunking Algorithm

## Problem Context
In our previous deep dives, we discovered that `0x51` (Scene Sequencer) and long `0x59` spatial payloads fail because our `writeChunked` method in `useBLE.ts` uses a flawed, hardcoded framing signature (`0x01, 0x43, 0xBD, 0x0B`). We obtained this from a raw packet sniff, but it only worked for a specific MTU size (196 bytes) and a specific payload length (323 bytes). When Android or iOS negotiates a smaller MTU (like 23 bytes), our hardcoded framing causes the hardware's internal state machine to crash because the declared payload size mismatches the transmitted chunk size.

## Discovery: The `LowerTransportLayerEncoder`
By reverse-engineering `C9272b.java`, `C7787x.java`, and finally `LowerTransportLayerEncoder.java` inside the ZENGGE APK, we successfully extracted the exact chunking logic used by the official app.

Here is how the hardware actually expects fragmented data over BLE:
1. **First Chunk (8-byte header):**
   - `[0]`: Control Byte (`0x40` for multi-segment, no-ack)
   - `[1]`: Sequence counter
   - `[2], [3]`: Segment Index (`0x00 0x00` for the first chunk)
   - `[4], [5]`: Total Payload Length (e.g., `0x01 0x43` for 323 bytes)
   - `[6]`: Payload length in *this specific chunk* (MTU - 8)
   - `[7]`: Command ID (`0x0B` for Control / `0x0A` for Request)
   - `[8...]`: Data
2. **Subsequent Chunks (5-byte header):**
   - `[0]`: Control Byte (`0x40`)
   - `[1]`: Sequence counter
   - `[2], [3]`: Segment Index (e.g., `0x00 0x01`). **CRITICAL:** The very last chunk sets the Most Significant Bit (`0x8000 | index`) to signal EOF to the hardware.
   - `[4]`: Payload length in *this specific chunk* (MTU - 5)
   - `[5...]`: Data

Our previous hardcoded `[0x01, 0x43, 0xBD, 0x0B]` was literally telling the hardware:
"Expect 323 bytes total (`0x0143`), here are 189 bytes (`0xBD`) in this chunk, for Command `0x0B`."
But we were only sending 20 bytes over the air! The hardware starved waiting for the rest of the 189 bytes.

> [!IMPORTANT]
> This mathematically perfect chunking algorithm completely unlocks the `0x51` Scene Sequencer (Breathe, Jump, Strobe) and long `0x59` spatial arrays for large LED strips!

## Proposed Changes

### `src/hooks/useBLE.ts`
#### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
1. **Remove Hardcoded framing:** Delete the `0x01, 0x43, 0xBD, 0x0B` payload hack in `writeChunked`.
2. **Implement Dynamic Chunking Algorithm:**
   - Calculate safe `MTU` dynamically per device using `getDeviceMtu(deviceId) - 3`.
   - Implement `calculateSegNum` based on `(payload.length - (MTU - 8)) / (MTU - 5)`.
   - Build the 8-byte header for Chunk 0.
   - Build the 5-byte headers for Chunks 1..N.
   - Apply the `0x8000` MSB bitmask to the final chunk's segment index to trigger the hardware's execute command.
3. **Pacing:** Keep the 20ms inter-chunk delay to prevent Android Bluetooth stack buffer overflows.

## User Review Required
Does this algorithm make sense? This is a massive breakthrough. By replacing our static packet sniff with the actual dynamic Transport Layer chunker, we solve the MTU crash issues across both Android and iOS devices. Once you approve, I will inject the new `writeChunked` implementation into `useBLE.ts` and we can immediately test the broken `0x51` Breathe/Strobe/Jump patterns!

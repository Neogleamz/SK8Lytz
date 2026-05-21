# HAL Migration Plan: BanlanX Adapter (Phase 2)

**Slug:** `feat/banlanx-adapter-phase2-pixels`
**Goal:** Implement the raw pixel streaming capability (`buildPixelArray`) for the BanlanX Hardware Abstraction Layer, allowing SK8Lytz to run custom Pattern Engine animations on SP621E hardware.

## Context
Phase 1 covered basic state controls using confirmed opcodes. Phase 2 requires translating the SK8Lytz 60FPS raw pixel arrays into the BanlanX `DiyPixel` format. While the Dart class `DiyPixel` and the native C++ functions (`get_frame_data`, `render_frame` in `libwled_lfx.so`) have been confirmed via Blutter, the exact BLE opcode wrapping the frame chunks is still unmapped.

## Implementation Steps

### 1. HCI Snoop Verification (Blocker)
- **Action:** Before writing code, execute a live ADB HCI logcat sniff on physical SP621E hardware.
- **Procedure:** Connect BanlanX App → Trigger "Car Lights" or "DIY Pixel" modes → Sniff `btsnoop_hci.log` to capture the exact hex payload of the frame packets.
- **Goal:** Identify the BLE opcode (e.g., `[0xA1]`, `[0xA2]`, or a `0x5X` variant) used to wrap the `[R, G, B]` byte sequence.

### 2. Implement `buildPixelArray()`
- **File:** `src/protocols/BanlanxAdapter.ts`
- **Action:** Implement the `buildPixelArray(colors: number[][])` interface method.
- **Logic:** 
  1. Flatten the `colors` array into a 1D sequence of `[R, G, B, R, G, B...]`.
  2. Map the flattened sequence into the chunked frame protocol discovered in Step 1.
  3. Include any required segment length headers (`0x55` `setLfxPixelCount` may need to precede the stream).

### 3. Native Frame Rendering Bridge (Alternative Route)
- **If Step 1 fails or chunking is complex:** We may need to build a Native Module bridge to `libwled_lfx.so` via JSI/FFI to directly utilize `get_frame_data()` offline.
- **Action:** This is the fallback plan and requires significant architectural risk. Only pursue if the HCI sniff reveals an encrypted or highly complex frame buffer.

### 4. Wire Pattern Engine
- **File:** `src/engine/VisualizerUnit.ts`
- **Action:** Ensure the generic output of the `PatternEngine` is passed directly to the generic `useBLE` write pipeline, trusting `BanlanxAdapter` to format it correctly.
- **Constraint:** Ensure that when connected to BanlanX, `useAppMicrophone` software FFT stream (`0x74`) is **suppressed**, as BanlanX hardware will desync if bombarded with unnecessary magnitude bytes during native pixel rendering.

## Rollback Strategy
If pixel streaming proves unstable or unachievable via simple byte array reconstruction, disable `buildPixelArray` for BanlanX (throw `NotImplementedError`) and fallback to native `0x53` built-in effects.

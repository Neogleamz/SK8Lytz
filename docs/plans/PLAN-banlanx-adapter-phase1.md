# HAL Migration Plan: BanlanX Adapter (Phase 1)

**Slug:** `feat/banlanx-adapter-phase1`
**Goal:** Implement the BanlanX Hardware Abstraction Layer (Phase 1) to support full basic control parity (Power, Color, Brightness, Effect, Music, State Query) using the newly extracted Dart AOT `0x5X` opcodes.

## Context
Following the successful Blutter decompile of the BanlanX `libapp.so`, we now have the exact protocol opcodes used by the SP621E hardware. This Phase 1 plan covers all non-pixel-streaming commands.

## Implementation Steps

### 1. Create Adapter Skeleton
- **File:** `src/protocols/BanlanxAdapter.ts`
- **Action:** Implement the `IControllerProtocol` interface.
- **Constraints:** Do not use the `ZenggeProtocol` V2 wrapper or checksum. All BanlanX packets are raw `[0xA0, OPCODE, LEN, ...DATA]`.

### 2. Opcode Mapping
Implement the `IControllerProtocol` methods using the following mappings:
- **`buildPowerOn()`**: `[0xA0, 0x50, 0x01, 0x01]`
- **`buildPowerOff()`**: `[0xA0, 0x50, 0x01, 0x00]`
- **`buildSolidColor(r, g, b)`**: `[0xA0, 0x52, 0x03, r, g, b]`
- **`buildBrightness(level)`**: `[0xA0, 0x51, 0x01, level]` (Requires 0-100 to 0-255 scaling)

### 3. Multi-Packet Command Refactor
BanlanX splits Effects and Speed into two separate commands, unlike Zengge's bundled `0x42`.
- **Action:** Modify `IControllerProtocol` to allow `buildEffect` to return a multi-packet array `number[][]`.
- **`buildEffect(id, speed)`**:
  - Packet 1: `[0xA0, 0x53, 0x01, id]`
  - Packet 2: `[0xA0, 0x54, 0x01, speed]`
- **Action:** Update `src/hooks/useBLE.ts` (`sendProtocolResult` helper) to natively support iterating and writing `number[][]` sequentially.

### 4. Hardware Mic Mode (Music)
- **`buildMusicConfig(isOn, type, ...)`**: The BanlanX SP621E features native hardware FFT. We do not need the software magnitude stream (`0x74`).
- **Implementation:** Return `[0xA0, 0x59, 0x01, 0x00]` (Set Input Source to Internal Mic) and `[0xA0, 0x5A, 0x01, gain]` (Set Sensitivity).

### 5. Controller Registry
- **File:** `src/protocols/ControllerRegistry.ts`
- **Action:** Add `ControllerType.BANLANX_SP621E` mapping to the new adapter.
- **Matching:** Update `matchesAdvertisement()` to check for MFR ID `0x5053` or the byte offsets `[0x0D]/[0x16]`.

## Rollback Strategy
If `number[][]` array refactoring breaks Zengge commands, wrap Zengge results in single-element arrays `[zenggePayload]` to satisfy the multi-packet type signature.

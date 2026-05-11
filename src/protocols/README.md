# 🛡️ Micro-Doc: The Protocol Sandbox

> **CRITICAL AI CONTEXT**: If you are editing files in `src/protocols/`, you MUST read this first.

## The Hardware Rule (Single Source of Truth)
The SK8Lytz hardware (ZENGGE 0xA3 controllers) is a **Playback Engine**. 
We send the hex payload exactly **ONCE**. The hardware animates it forever.
There is **NO** continuous BLE streaming (except for the `0x74` audio magnitude byte in music mode).
If an animation looks frozen, it is because you sent the wrong `commandType` byte, not because you need to send it repeatedly.

## The 0x59 Primary Payload (Multi-Color Engine)
- Almost **all** patterns are generated locally in `PatternEngine.ts` and sent via `0x59`.
- **Command Structure:** `[0x59, len_hi, len_lo, R1,G1,B1..., pts_hi, pts_lo, commandType, speed, direction, checksum]`
- **The Transition Byte (`commandType`):** This is the magic byte that tells the hardware what to do with the pixel array you just gave it.
  - `0x01` = **Static**: Freeze the array exactly as it is.
  - `0x02` = **Running**: Continuously scroll the array (hardware animation).
  - `0x03` = **Strobe**: Flash the array.
  - `0x04` = **Jump**: Hard cut (use sparingly).
  - `0x05` = **Breathe**: Pulse the brightness.
  - `0x00` = **UNDEFINED!** If you send 0x00, the hardware will freeze. DO NOT SEND 0x00.

## The 0x51 Interception (Native Symphony Modes)
- Hardware patterns 201-244 bypass the 0x59 array generator.
- They are intercepted in `PatternEngine.ts` and routed to `0x51` (Extended 323-byte chunks).
- These are native firmware routines. You cannot modify their pixel math.

## The Condemned Opcode (0x43)
- **DO NOT USE `0x43`**. It causes the 0xA3 hardware to hard-fault and requires a physical battery pull to recover.
- If you find `0x43` in the codebase, it is only for the `DiagnosticLab` when explicitly enabled.

## Math Modifications & Testing
- If you modify `PatternEngine.ts` or `ZenggeProtocol.ts`, you **MUST** run `npm test` and update the corresponding `.test.ts` file in the `__tests__` directory.
- Never merge a broken test.

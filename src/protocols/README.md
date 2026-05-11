# 🛡️ Micro-Doc: The Protocol Sandbox

> **CRITICAL AI CONTEXT**: If you are editing files in `src/protocols/`, you MUST read this first. This is the authoritative byte-math reference for the ZENGGE 0xA3 hardware.

## The Hardware Rule (Single Source of Truth)
The SK8Lytz hardware (ZENGGE 0xA3 controllers) is a **Playback Engine**. You send the hex payload exactly **ONCE**. The hardware animates it forever.

---

## 🎨 0x59 Primary Payload (Multi-Color Engine)
This is the spatial array payload. It defines every pixel on the strip.
**Format**: `[0x59, len_hi, len_lo, R,G,B..., pts_hi, pts_lo, commandType, speed, direction, checksum]`
* `totalLen` = `(numLEDs * 3) + 9`
* `pts_hi` / `pts_lo`: Big-endian hardware LED length.
* `speed`: `1-100`
* `direction`: `0x01` (Forward) or `0x00` (Reverse)
* **`commandType` (Transition Byte)**:
  - `0x01` = Static (Freeze array)
  - `0x02` = Running Water (Hardware scroll)
  - `0x00` = UNDEFINED (Hardware freezes. Never use.)
  - *Note: `0xA3` hardware DOES NOT support Breathe, Strobe, or Jump via `0x59`. Use `0x51` instead.*

---

## 🎬 0x51 Custom Scene (Native Sequences)
Bypasses the `0x59` array generator. Uses baked hardware effects.
**Format (9-Byte Compact Slot)**: `[0x51, slot(9B) x 32, 0x0F, checksum]`
**Active 9B Slot**: `[0xF0, effectId, speed, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B]`
**Empty 9B Slot**: `[0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00]`
* `effectId`: `1-33` (maps to internal hardware SymphonyEffects).
* `flags`: `0xF0` indicates active slot. `0x0F` indicates empty slot.

---

## 🎵 0x73 & 0x74 Music Mode
**0x73 (Config)**: `[0x73, isOn, modeType, effectId, FG.R,G,B, BG.R,G,B, sensitivity, brightness, checksum]`
* `modeType`: `0x26` (Light Bar, 16 modes), `0x27` (Light Screen, 30 modes).
* `isOn`: `0x00` means App Mic is active.
**0x74 (Live Audio Stream)**: `[0x74, magnitude(0-255), checksum]` (Fired continuously at 20fps when App Mic is active).

---

## ⚡ 0x71 Power
**Format**: `[0x71, action, 0x0F, checksum]`
* Power ON: `action = 0x23` (Checksum = `0xA3`)
* Power OFF: `action = 0x24` (Checksum = `0xA4`)

---

## 💀 The Condemned Opcode (0x43)
- **DO NOT USE `0x43`**. It causes the 0xA3 hardware to hard-fault and requires a physical battery pull to recover.
- `0x42` is the correct opcode for RBM (built-in patterns).

## Math Modifications & Testing
- If you modify `PatternEngine.ts` or `ZenggeProtocol.ts`, you **MUST** run `npm test` and update the corresponding `.test.ts` file in the `__tests__` directory.

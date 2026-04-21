# ZENGGE PROTOCOL BIBLE
## Authoritative Hardware Reference — Derived from Decompiled ZENGGE 1.5.0 APK

> **Source Authority**: All entries in this document are traced to exact Java class files in
> `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\ZENGGE_APK\ZENGGE_DECOMPILED\sources\`
> No community docs, no guesses. Every byte is APK-verified.

> **Last Updated**: 2026-04-21
> **Confirmed Hardware**: `Ctrl_Mini_RGB_Symphony_new_0xA3` (product_id: 163 = 0xA3)
> **Source Files**: `tc/C14184b.java`, `tc/C14187d.java`, `com/zengge/wifi/COMM/Protocol/C77*.java`,
>   `com/zengge/wifi/activity/NewSymphony/fragment/*.java`

---

## SECTION 1: DEVICE IDENTITY

### Confirmed Controller: `0xA3` (163 decimal)

| Property | Value | Source |
|:---------|:------|:-------|
| Java Class | `Ctrl_Mini_RGB_Symphony_new_0xa2.java` (0xA3 extends 0xA2) | `Device/Type/` |
| `mo20320T()` return | `163` (0xA3) | `C14184b` branch check |
| product_id in telemetry | `163` — confirmed across ALL 3 device MACs | Supabase `discovered_devices_telemetry` |
| firmware_ver | `45` or `46` | Telemetry |
| ble_version | `5` | Telemetry |
| led_version | `3` | Telemetry |
| Device MACs (ours) | `08:65:F0:9A:C2:3C`, `08:65:F0:9A:5E:06`, `08:65:F0:5F:03:B1` | Telemetry |

### Key Difference: 0xA2 vs 0xA3

| Feature | 0xA2 | **0xA3 (OURS)** |
|:--------|:-----|:----------------|
| Function Mode effects (0x42) | 1–100 | **1–100 (same)** |
| `0x59` Static Colorful tab | ❌ NOT available | **✅ AVAILABLE** |
| `0x51` custom scene format | 291B (9B/slot) | **323B (10B/slot with direction flag)** |
| `mo20320T()` | 162 | **163** |
| `C7760a` LED count branch | n/a | `== 167` check → standard count for 0xA3 |

### Power Command Routing — CONFIRMED

`C7780q.m20873a()` is the definitive power dispatcher:
```java
if (baseDeviceInfo.m20497E()) {  // true = legacy device ONLY
    return C14187d.m4725c(PowerType.code, 0,0,0,0,0,0,0);  // → 0x3B (NOT for us)
}
return C14184b.m4796M(z, false);  // → 0x71 (THIS IS OURS)
```
**0xA3 is NOT a legacy device → always uses `0x71`.**

---

## SECTION 2: TRANSPORT WRAPPER

### V2 BLE Packet Framing (Applied to ALL commands)

```
[0x00, SeqNum, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]
```

Every inner payload listed below is wrapped in this before transmission.

### Checksum Algorithm

Source: `C14184b.m4780b(byte[] bArr, int i)`
```java
// Sum of all bytes 0..i-1, return as byte (truncated, NOT XOR)
int sum = 0;
for (int j = 0; j < i; j++) { sum += bArr[j] & 0xFF; }
return (byte) sum;
```
**CRITICAL:** This is a simple SUM checksum, NOT XOR. Always passed as `m4780b(bArr, payloadLen - 1)`.

---

## SECTION 3: COMPLETE OPCODE MAP

### 0x11 — Time Sync
- **Builder**: `C14184b.m4760m(boolean z)` and `C14184b.m4787V(boolean z, boolean z2)`
- **Format**: 7 bytes containing time data
- **Used by**: Timer/scheduler system
- **SK8Lytz relevance**: Low — automated timer functions only

---

### 0x21 — Timer Command
- **Builder**: `C14184b.m4794O()`, `m4793P()`, `m4792Q()`, `m4791R()`, `m4790S()`
- **Used by**: Timer scheduler — multiple variants depending on timer type
- **SK8Lytz relevance**: Low

---

### 0x31 — Solid Color (RGB static)
- **Builder**: `C14184b.m4798K(r, g, b, brightness)` and several others
- **Format**: `[0x31, r, g, b, brightness, 0x0F, checksum]` (7 bytes)
- **Called by**: Color pickers, single-color solid commands
- **SK8Lytz relevance**: MEDIUM — legacy solid color path. Currently causes flicker on Symphony hardware. Use `0x59` with FREEZE instead.

---

### 0x36 — CCT (Warm/Cold White)
- **Builder**: `C14184b.m4781a0(isRGB, r, g, b, ledCount)`
- **Format**: 9 bytes
- **Used by**: CCT-capable devices only (warm/cool temperature control)
- **SK8Lytz relevance**: LOW — only for white-tunable CCT products, not our RGB skate hardware

---

### 0x37 — Global Brightness
- **Builder**: `C14184b.m4779b0(int brightness)`
- **Format**: `[0x37, brightness(0-100), 0, checksum]` (4 bytes)
- **SK8Lytz relevance**: MEDIUM — global brightness override independent of pattern

---

### 0x41 — Settled Mode (FG + BG dual color)
- **Builder**: `C7775l.java` → `m20877a(effectId, fgColor, bgColor, speed, dir, device)`
- **Called by**: `SettledModeFragment.m18679m2()`
- **Format**: 13 bytes
```
[0x41, effectId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, speed, dir(0=fwd/1=rev), 0, 0xF0, checksum]
```
- **effectId range**: 1–33 (SymphonyEffect IDs, same as `0x51` effect column)
- **dir**: `0` = forward, `1` = reverse
- **SK8Lytz relevance**: HIGH — dual-palette animated effects with FG/BG color control

---

### 0x42 — Function Mode (Built-in RBM patterns)
- **Builder**: `C7776m.java` → `m20876a(effectId, brightness, speed, device)`
- **Called by**: `FunctionModeFragment.m18912P1()`
- **Format**: 5 bytes
```
[0x42, effectId(1-100), speed(1-100), brightness(1-100), checksum]
```
- **effectId range for 0xA3**: 1–100 (confirmed from `Ctrl_Mini_RGB_Symphony_new_0xa2`)
- **Speed range**: 0–100 (full range, different from 0x59 which caps at 31)
- **SK8Lytz relevance**: **CRITICAL** — this is the primary RBM effect command. Our `setCustomRbm()` calls this. ✅ CORRECT in current codebase.

> **CORRECTION FROM EARLIER SESSION**: `0x38` is NOT used by our device for effects.
> `0x38` appears only in `C14187d.m4724d()` which routes to legacy/other device types.
> `0x42` is confirmed as the correct opcode for our 0xA3.

---

### 0x43 — Multi-Effect Sequence (up to 50 effect IDs)
- **Builder**: `C7778o.java` → `m20874a(effectIdList, speed, brightness, device)`
- **Called by**: `FunctionModeFragment.m18913O1()` (when no single effect selected, f28108u0 == -1)
- **Format**: 54 bytes
```
[0x43, effectId[0]...effectId[49](pad 0 if fewer), speed, brightness, checksum]
// effectId bytes: up to 50 sequential pattern IDs that loop
```
- **SK8Lytz relevance**: MEDIUM — useful for auto-cycling effect sequences without BLE resend

---

### 0x47 — State Query
- **Builder**: `C14184b.m4764j(int i)`
- **Format**: `[0x47, queryParam, checksum]` (3 bytes)
- **Returns**: Current device state
- **SK8Lytz relevance**: MEDIUM — use to confirm power/mode state

---

### 0x51 — Custom Scene (Save/Play from device EEPROM)
- **Builder**: `C7787x.java` → `m20864c()` (extended) or `m20865b()` (short)
- **Called by**: `CustomModeFragment` via `ActivityCustomSymphonyEdit`
- **0xA3 uses EXTENDED format** (323 bytes, 10B/slot):

```
SHORT FORMAT (291 bytes) — NOT FOR 0xA3:
[0x51, slot[0](9B)...slot[31](9B), 0x0F_terminator, checksum]
Each 9B slot: [0xF0(active)|0x0F(empty), effectId, colorId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B]

EXTENDED FORMAT (323 bytes) — 0xA3 CONFIRMED:
[0x51, slot[0](10B)...slot[31](10B), 0x0F_terminator, checksum]
Each 10B slot: [0xF0(active)|0x0F(empty), effectId, colorId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, directionFlags]
directionFlags byte: (mirror ? 128 : 0) | c9273c.m16246c()
```

- **Slot active flag**: `0xF0` = active, `0x0F` = empty slot
- **Max slots**: 32
- **Terminator after last slot**: `0x0F`
- **SK8Lytz relevance**: **CRITICAL** — currently using 291B short format, **MUST switch to 323B for 0xA3**

> **BUG CONFIRMED**: Our current `ZenggeProtocol.setCustomMode()` uses 9-byte slots (291B total).
> The 0xA3 hardware expects 10-byte slots (323B total). This may cause scene corruption.

---

### 0x53 — Live Pixel Streaming (real-time bitmap row send)
- **Builder**: Built inline in `SceneModeFragment.m18748Z2(int[] iArr)` — NO protocol class
- **Format**: Variable length
```
[0x53, totalLen_hi, totalLen_lo, R, G, B, ...(numLEDs × RGB triplets)..., numLEDs_hi, numLEDs_lo, checksum]
totalLen = (numLEDs × 3) + 6
```
- **Behavior**: Sends ONE row of pixel data from a bitmap, called rapidly in a loop to stream animation frames
- **Rate-limiting**: `f28247B0` AtomicBoolean gates concurrent sends — one frame at a time
- **SK8Lytz relevance**: HIGH — this is the live "scene streaming" protocol for real-time pixel updates. Different from `0x59` which sends a full static array.

---

### 0x56 — Delete Scene Slot
- **Builder**: Built inline in `SceneModeFragment.m18778K2(int i, ...)`
- **Format**:
```
[0x56, slotIndex, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, checksum]
// Total: 15 bytes. slotIndex = 0-based scene slot number
```
- **SK8Lytz relevance**: MEDIUM — for EEPROM scene management

---

### 0x57 — Select/Activate Scene + Speed + Brightness
- **Builder**: Built inline in `SceneModeFragment.m18750Y2(int i)`
- **Format**:
```
[0x57, sceneIndex, speed, brightness, checksum]
// Total: 5 bytes
// sceneIndex = 0xFF (255 / -1 as byte) to replay ALL scenes
// sceneIndex = 0-9 to activate specific slot
```
- **SK8Lytz relevance**: HIGH — activates a scene stored in EEPROM by slot number

---

### 0x58 — State Query Flag
- **Builder**: `C14184b.m4769g0(boolean z)`
- **Format**:
```
[0x58, 0xF0 (if z=true) | 0x0F (if z=false), checksum]
// Total: 3 bytes
```
- **Used by**: Scene state request — queries which scene is currently active
- **SK8Lytz relevance**: MEDIUM — diagnostic use

---

### 0x59 — Static Colorful Pixel Array
- **Builder**: `C7760a.java` → `m20886a(numLEDs, pixelColors, transitionType, speed, direction)`
- **Called by**: `C8856x.m17452i2()` (the "Static Colorful" tab, 0xA3 ONLY)
- **Format**: Variable length
```
[0x59, totalLen_hi, totalLen_lo,
 R, G, B, ...(numLEDs × RGB triplets)...,
 numLEDs_hi, numLEDs_lo,
 transitionType, speed, direction,
 checksum]

totalLen = (numLEDs × 3) + 9
```
- **Transition Types** (hardware-confirmed):

| Byte | Name | Behavior |
|:-----|:-----|:---------|
| `0x00` | CASCADE | Continuous hardware scroll |
| `0x01` | FREEZE | Static locked in place |
| `0x02` | STROBE | Flash (implementation varies) |
| `0x03` | RUNNING_WATER | One-shot trigger, hard jump marquee |

- **Speed**: 0–100 UI → 1–31 HW. Formula: `max(1, min(31, round(uiSpeed/100 × 30) + 1))`
- **Direction**: `0x01` = forward, `0x00` = reverse
- **Minimum pixels**: 12 (hardware glitches below 10)
- **LED count branch** in `C7760a` line 23: `if (mo20320T() == 167)` → different LED count constant (for 0xA7 variant). Our 0xA3 (163) takes the ELSE branch = standard LED count path.
- **SK8Lytz relevance**: **CRITICAL** — primary pixel array command. ✅ Available on 0xA3.

---

### 0x62 — IC Config Write (EEPROM)
- **Builder**: `C14184b.m4807B()`, `m4806C()`, `m4804E()`
- **Format (full, 13 bytes)**:
```
[0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, checksum]
```
- **Endianness**: Big-Endian (`ptsHi = points >> 8`, `ptsLo = points & 0xFF`)
- **SK8Lytz relevance**: CRITICAL — hardware provisioning (LED count, IC type, strip config)

---

### 0x63 — IC Config Query (EEPROM read)
- **Builder**: `C14184b.m4771f0(boolean z)` (confirmed 5 bytes: `[0x63, 0x12, 0x21, 0x0F, checksum]`)
- **Response**: Contains ledPoints (little-endian swapped!): `((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF)`
- **SK8Lytz relevance**: CRITICAL — hardware config read during probing

---

### 0x71 — Power ON/OFF ✅ OUR DEVICE
- **Builder**: `C14184b.m4796M(boolean isOn, boolean z2)`
- **Called by**: `C7780q.m20873a()` for all non-legacy devices (including 0xA3)
- **Format**: 4 bytes
```
Power ON:  [0x71, 0x23, 0x0F, checksum]  → checksum = 0x71+0x23+0x0F = 0xA3 ✓
Power OFF: [0x71, 0x24, 0x0F, checksum]  → checksum = 0x71+0x24+0x0F = 0xA4 ✓
```
- **SK8Lytz relevance**: **CRITICAL** ✅ Confirmed correct in codebase.

---

### 0x72 — Power with Duration (timed power)
- **Builder**: `C14184b.m4797L(boolean isOn, float duration, boolean z2)`
- **Called by**: `CommandPackagePowerOverDuraion` second constructor
- **Format**: 7 bytes (includes duration field)
- **SK8Lytz relevance**: LOW — timer-based power only

---

### 0x73 — Music Mode Configuration ✅ OUR DEVICE
- **Builder**: `C7789z.java` (full 13B), `C7774k.java` (5B short form)
- **Called by**: `MusicModeFragment`, confirmed for 0xA2 and 0xA3
- **Format (13 bytes)**:
```
[0x73, isOn(1=on/0=off), micSource, effectId,
 FG.R, FG.G, FG.B, BG.R, BG.G, BG.B,
 sensitivity, brightness, checksum]
```
- **micSource**: `0x26` (38) = phone/app mic, `0x27` (39) = device mic
- **Confirmed from MusicModeFragment line 752**: `bool z2 = bArr[2] == 38` (38=phone, 39=device)

> **CORRECTION FROM MASTER REFERENCE**: The Master Reference lists `micSource: 0x01 = Device, 0x00 = App`.
> **APK TRUTH**: `0x26` (38) = app/phone mic, `0x27` (39) = device mic.
> The 0x00/0x01 values are WRONG in the current codebase/reference.

---

### 0x74 — Music Magnitude Stream ✅ OUR DEVICE
- **Builder**: `C7788y.java` → `m20863a(int magnitude, device)`
- **Format**: 3 bytes
```
[0x74, magnitude(0-255), checksum]
```
- **Used when**: micSource = `0x26` (app mic) in the `0x73` config
- **SK8Lytz relevance**: **CRITICAL** ✅ Used by `useAppMicrophone.ts`

---

### 0x75 — Multi-Zone Power
- **Builder**: `C14184b.m4808A(int i, boolean z)`
- **Format**: 7 bytes
- **Used by**: Multi-zone/segmented controllers
- **SK8Lytz relevance**: LOW unless we implement zone-level power control

---

## SECTION 4: COMMANDS NOT IN SYMPHONY FRAGMENTS (Background/System)

These exist in `C14184b` but are NOT called by any Symphony fragment for our device:

| Opcode | Decimal | Method | Purpose |
|:-------|:--------|:-------|:--------|
| `0x11` | 17 | `m4760m` | Time sync |
| `0x22` | 34 | `m4639q0` | Mic/sensor config |
| `0x34` | 52 | `m4788U` | LED count query shorthand |
| `0x3A` | 58 | `m4767h0` | Plant light mode |
| `0x61` | 97 | `m4795N`, `m4778c` | Legacy built-in pattern (older firmware) |
| `0x72` | 114 | `m4797L` | Timed power |
| `0x75` | 117 | `m4808A` | Multi-zone power |

---

## SECTION 5: SYMPHONY EFFECT IDs (0xA2/0xA3)

Per `Ctrl_Mini_RGB_Symphony_new_0xa2.java` and `FunctionModeFragment`:

- **0x42 effect range**: `1–100` for 0xA2/0xA3
- **0x41 effect range**: `1–33` (SymphonyEffect IDs, same as the APK's SymphonyEffect enum)
- **0x51 effectId column**: `1–33` (per C7787x slot structure using C9273c effect model)

SymphonyEffect IDs are defined in `com/zengge/wifi/Model/SymphonyEffect.java` as a Java enum.
The `SymphonyEffectUIType` enum defines color input requirements:
- `RGB_ONLY` = single color
- `TWO_COLORS` = FG + BG required
- `NO_COLOR` = no color input (pure algorithmic)

---

## SECTION 6: MANUFACTURER DATA ADVERTISEMENT PARSING

Our scanner reads `productId` from advertisement via `ZenggeProtocol.parseFirmwareFromAdvertisement()`:
```typescript
const productId = ((buffer[10] & 0xFF) << 8) | (buffer[11] & 0xFF);
```

Sample confirmed manufacturer data (base64): `AFpWBQhl8JrCPACjLgMBAiMkAR8AAP8AAwALAAA=`
Decoded relevant fields: `product_id = 163` (0xA3), `firmware_ver = 46`, `ble_ver = 5`, `led_ver = 3`

**Symphony detection byte**: `mfBuf[9] === 0x33 || mfBuf[9] === 0xBF` — our scanner reads this correctly.

---

## SECTION 7: PROTOCOL CLASS → OPCODE CROSS-REFERENCE

| Class | Opcode | Called From | Notes |
|:------|:-------|:-----------|:------|
| `C7775l` | `0x41` | `SettledModeFragment` | FG+BG settled effects |
| `C7776m` | `0x42` | `FunctionModeFragment` | RBM built-in patterns |
| `C7778o` | `0x43` | `FunctionModeFragment` | Multi-effect sequence |
| inline `m18748Z2` | `0x53` | `SceneModeFragment` | Live pixel streaming |
| inline `m18778K2` | `0x56` | `SceneModeFragment` | Delete scene slot |
| inline `m18750Y2` | `0x57` | `SceneModeFragment` | Activate scene slot |
| `C14184b.m4769g0` | `0x58` | `SceneModeFragment.m18781I2` | Scene state query |
| `C7760a` | `0x59` | `C8856x` (0xA3 tab only) | Static Colorful pixel array |
| `C14184b.m4807B` etc. | `0x62` | Programmer/Settings | EEPROM write |
| `C14184b.m4771f0` | `0x63` | Programmer/Settings | EEPROM read |
| `C7780q` → `m4796M` | `0x71` | Power button / toolbar | Power ON/OFF |
| `C14184b.m4797L` | `0x72` | Timer system | Timed power |
| `C7789z` | `0x73` | `MusicModeFragment` | Music config (13B) |
| `C7788y` | `0x74` | `MusicModeFragment` | Mic magnitude |
| `C7787x.m20864c` | `0x51` (323B) | `ActivityCustomSymphonyEdit` | 0xA3 extended scene |
| `C7787x.m20865b` | `0x51` (291B) | same | 0xA2 short scene |

---

## SECTION 8: KNOWN BUGS IN CURRENT SK8LYTZ CODEBASE

### BUG-1: `0x51` Wrong Format for 0xA3 (HIGH SEVERITY)
- **Current behavior**: `ZenggeProtocol.setCustomMode()` uses 9B/slot = 291B total
- **Required for 0xA3**: 10B/slot (direction flag) = 323B total (`C7787x.m20864c`)
- **Impact**: Custom scenes may corrupt or not activate correctly on hardware

### BUG-2: `0x73` micSource Wrong Values (HIGH SEVERITY)
- **Master Reference says**: `0x01` = Device mic, `0x00` = App mic
- **APK truth (MusicModeFragment line 752)**: `38` (0x26) = phone/app mic, `39` (0x27) = device mic
- **Impact**: Mic source selection may be inverted or ineffective

### BUG-3: Mock Product ID (LOW SEVERITY)
- **Location**: `useBLEScanner.ts` line 313
- **Current value**: `product_id: 115` in mock device injection
- **Correct value**: `163` (0xA3)

### BUG-4: `0x42` Effect Count Ceiling Not Enforced (MEDIUM)
- **Current behavior**: Unknown — may allow effect IDs > 100
- **Hardware limit**: 1–100 for 0xA3
- **Impact**: Effects above 100 may cause undefined hardware behavior

### BUG-5: `0x38` in Any Legacy Path (VERIFY)
- **Risk**: If `0x38` appears anywhere in our codebase as an effect command, it is WRONG for 0xA3
- **Corrected opcode**: `0x42`

---

## SECTION 9: DIAGNOSTIC LAB — MISSING PROTOCOL TESTS

The current `Sk8LytzDiagnosticLab.tsx` should expose test panels for:

| Opcode | Test Panel Needed | Priority |
|:-------|:-----------------|:---------|
| `0x41` | Settled Mode: effectId + FG/BG color pickers + speed/dir | HIGH |
| `0x42` | RBM: effectId 1-100 + speed/brightness sliders | ✅ Exists |
| `0x43` | Multi-Effect Sequence: up to 50 IDs + speed/brightness | MEDIUM |
| `0x51` | Custom Scene: 32-slot builder (323B for 0xA3) | HIGH |
| `0x53` | Live Pixel Stream: color array + numLEDs | HIGH |
| `0x56` | Delete Scene Slot: slot index input | LOW |
| `0x57` | Activate Scene: slot index + speed/brightness | MEDIUM |
| `0x58` | Scene State Query (no params) | LOW |
| `0x59` | Static Colorful: pixel array + transition/speed/dir | ✅ Exists |
| `0x62` | EEPROM Write: all config fields | ✅ Exists |
| `0x63` | EEPROM Query (no params) | ✅ Exists |
| `0x71` | Power ON/OFF toggle | HIGH (power bugs) |
| `0x73` | Music Config: all 13 params with correct mic values | ✅ Exists |
| `0x74` | Mag stream: magnitude slider | ✅ Exists |

---

## SECTION 10: CALL CHAIN SUMMARY (0xA3 Exclusive Path)

```
Physical User Action → UI Fragment → Protocol Class → C14184b utility → BLE bytes

Power:          Toolbar tap → C7780q → C14184b.m4796M → [0x71, 0x23/24, 0x0F, chk]
RBM Effect:     FunctionModeFragment → C7776m → [0x42, id, spd, bri, chk]
Effect Seq:     FunctionModeFragment → C7778o → [0x43, id×50, spd, bri, chk]  
Settled:        SettledModeFragment → C7775l → [0x41, id, FGR,G,B, BGR,G,B, spd, dir, 0, 0xF0, chk]
Custom Scene:   ActivityCustomSymphonyEdit → C7787x.m20864c → [0x51, slot×10×32, 0x0F, chk]
Scene Delete:   SceneModeFragment.m18778K2 → inline → [0x56, idx, 0×12, chk]
Scene Select:   SceneModeFragment.m18750Y2 → inline → [0x57, idx, spd, bri, chk]
Scene Query:    SceneModeFragment.m18781I2 → C14184b.m4769g0 → [0x58, 0xF0/0x0F, chk]
Pixel Array:    StaticColorfulTab (C8856x) → C7760a → [0x59, lenHi,Lo, R,G,B×N, numHi,Lo, trans, spd, dir, chk]
Stream Frame:   SceneModeFragment.m18748Z2 → inline → [0x53, lenHi,Lo, R,G,B×N, numHi,Lo, chk]
Music Config:   MusicModeFragment → C7789z → [0x73, on, 0x26/27, id, FG, BG, sens, bri, chk]
Mic Magnitude:  MusicModeFragment/useAppMic → C7788y → [0x74, mag, chk]
EEPROM Write:   Programmer → C14184b.m4807B etc → [0x62, ...]
EEPROM Read:    Programmer → C14184b.m4771f0 → [0x63, 0x12, 0x21, 0x0F, chk]
```

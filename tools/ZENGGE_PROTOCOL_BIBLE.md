# ZENGGE PROTOCOL BIBLE

## Authoritative Hardware Reference — Derived from Decompiled ZENGGE 1.5.0 APK

> **Source Authority**: All entries in this document are traced to exact Java class files in
> `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\ZENGGE_APK\ZENGGE_DECOMPILED\sources\`
> No community docs, no guesses. Every byte is APK-verified.

> **Last Updated**: 2026-04-22 (Oracle Hardware Validation Session + Live BLE Sniff)
> **Confirmed Hardware**: `Ctrl_Mini_RGB_Symphony_new_0xA3` (product_id: 163 = 0xA3)
> **Source Files**: `tc/C14184b.java`, `tc/C14187d.java`, `com/zengge/wifi/COMM/Protocol/C77*.java`,
> `com/zengge/wifi/activity/NewSymphony/fragment/*.java`

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
- **Speed range**: 0–100 (full range, same as 0x59 — both accept 1–100)
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

> **⚠️ ORACLE LAB RESULT (2026-04-22)**: Sending our hypothesized `0x43` payload caused the hardware to **cut all LEDs** (state machine crash/reset). The payload structure from the APK decompile was NOT accepted by our firmware.
>
> **BLE SNIFF FINDING**: The ZENGGE app's "Customize Tab" (multi-step effects) actually uses **`0x51`** — NOT `0x43`. The `0x43` opcode may be unused in our firmware revision, reserved for a newer OEM variant, or require a different wrapping protocol. **Do NOT use `0x43` in production until confirmed via future sniff with a different BLE connection setup.**

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

> **🔬 ORACLE + BLE SNIFF GROUND TRUTH (2026-04-22)**: Both Oracle Lab tests AND live BLE capture of the official ZENGGE app confirm the following. See Section 11 for full raw bytes.

**HARDWARE REALITY (contradicts APK branch logic):**

- The `9B compact` format (`setCustomMode`, 291B) **fires correctly** on our 0xA3 hardware ✅
- The `10B extended` format (`setCustomModeExtended`, 323B) **does nothing** on our 0xA3 hardware ❌
- **HOWEVER**: Live BLE sniff of the ZENGGE app reveals it sends **10-byte slots** for `0x51`, reaching the hardware via a DIFFERENT BLE framing header.

**CONCLUSION**: Our `setCustomModeExtended` fails not because the hardware rejects 10B slots, but because our **BLE framing/wrapper is wrong** for multi-packet `0x51` payloads. The ZENGGE app uses a chunked framing protocol with header `[40 seq 00 00 01 43 BD 0B]` before the opcode.

```
CONFIRMED 10-BYTE SLOT STRUCTURE (from live BLE sniff):
[0x51, slot[0](10B)...slot[31](10B), checksum]

Active slot (10 bytes):
[0xF0, effectId, speed, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, flags]
  0xF0   = active slot marker
  effectId = SymphonyEffect ID 1-33
  speed  = 0x00-0x64 (0-100)
  FG.RGB = foreground color (ignored for NO_COLOR effects)
  BG.RGB = background color (ignored for NO_COLOR effects)
  flags  = 0x80 (forward+section_toggle) | 0x00 (reverse)

Empty slot (10 bytes):
[0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00]
  0x0F   = empty/null slot marker
```

**ZENGGE BLE Chunked Framing Header** (precedes opcode for large payloads):

```
[0x40, seqNum, offset_lo, offset_hi, 0x01, 0x43, 0xBD, 0x0B, opcode=0x51, ...payload]
Chunk 1: offset = 0x0000
Chunk 2: offset = 0x0180
seqNum increments per save operation (0x04, 0x05, 0x06...)
```

**ZENGGE App UI → Protocol Mapping (confirmed from sniff):**

| ZENGGE UI | Opcode | Notes |
|:---|:---|:---|
| Customize Tab (multi-step effects) | `0x51` | 10B slots, F0=active, 0F=empty |
| Multi-Color → Create (live editor) | `0x31` | Continuous frame stream while editing |
| Multi-Color → Save/Play | `0x31` | Final frame sent to hardware |

- **Slot active flag**: `0xF0` = active, `0x0F` = empty slot
- **Max slots**: 32
- **SK8Lytz relevance**: **CRITICAL** — `setCustomMode` (9B compact) works via our current wrapper. To use 10B slots correctly, the BLE chunked framing header must also be replicated.

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

- **Transition Types** (`StaticColorfulMode.java`):

> ⚠️ **0xA3 HARDWARE LIMITATION:** The `0x59` command is a spatial payload. The ZENGGE app explicitly *hides* Breathe and Twinkly from the `0x59` UI for the `0xA3` chip because the hardware cannot calculate temporal math over a 450-byte custom array. Strobe and Jump are also known to fail. **For temporal transitions (Breathe, Jump, Strobe), use the `0x51` Scene Sequencer instead!**

| Byte | Name | Behavior | 0xA3 Status |
|:-----|:-----|:---------|:------------|
| `0x01` | Static | Freeze in place | ✅ **Fully Supported** |
| `0x02` | Running Water | Continuous hardware scroll | ✅ **Fully Supported** |
| `0x03` | Strobe | Flash effect | ❌ Fails (Requires `0x51`) |
| `0x04` | Jump | Hard color jump | ❌ Fails (Requires `0x51`) |
| `0x05` | Breathe | Breathe fade effect | ⛔ **Firmware Locked/Hidden** (Use `0x51`) |
| `0x06` | Twinkly | Twinkle effect | ⛔ **Firmware Locked/Hidden** |

- **Speed**: 0–100 UI → 1–100 HW. Pass UI speed directly (clamped to 1–100). ⚠️ **ORACLE-CONFIRMED 2026-04-23**: The APK's `Protocol/n.java: ad.e.a(f, 1, 31)` clamp is for 0x51, NOT 0x59. Physical hardware responds to the full 1–100 range on the 0x59 speed byte. Setting speed=31 while UI slider is at 100 produced exactly 31%-speed animation on physical 0xA3 hardware.
- **Direction**: `0x01` = forward, `0x00` = reverse
- **Minimum pixels**: 12 (hardware glitches below 10)
- **Tick Settings (Point Count)**: The `numLEDs_hi` and `numLEDs_lo` bytes represent the **physical length of the hardware strip** that the effect will span across, NOT the length of the RGB byte array sent! If clamped to `MAX_PIXELS = 54` for MTU reasons, the transition effects (Strobe, Jump, Breathe) will only animate on the first 54 LEDs of the strip and freeze/truncate the rest.
- **LED count branch** in `C7760a` line 23: `if (mo20320T() == 167)` → different LED count constant (for 0xA7 variant). Our 0xA3 (163) takes the ELSE branch = standard LED count path.
- **SK8Lytz relevance**: **CRITICAL** — primary pixel array command. ✅ Available on 0xA3.

---

### 0xFF — Passive Telemetry (Manufacturer Specific Data)
ZENGGE hardware constantly broadcasts its identity in BLE Advertisement packets (`type 0xFF`), enabling passive identification without an active connection.
- **Source of Truth**: [ZGHBDevice.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/com/zengge/hagallbjarkan/device/ZGHBDevice.java) (`setDeviceInfo`)
- **Format**:
  - `bArr[3]`: BLE Version (Typically `5`)
  - `bArr[4]` to `bArr[9]`: MAC Address
  - `bArr[10]` & `bArr[11]`: Product ID (Big-Endian `(bArr[10] << 8) | bArr[11]`). SK8Lytz is `163` (`0xA3`).
  - `bArr[12]` & `bArr[14]`: Firmware Version
  - `bArr[13]`: LED Version

---

### RF Remote Configuration
RF Remotes are completely decoupled from the native `C14184b` Java protocol layer.
- **Source of Truth**: [FlutterNewControlPlugin.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/com/zengge/wifi/flutter/plugin/FlutterNewControlPlugin.java)
- **Mechanism**: The "Allow All / Allow Paired / Allow None" logic is sent via JSON payloads over Flutter `MethodChannel`. It does not use standard hex `0x64` opcodes.
- **SK8Lytz relevance**: We must intercept these Flutter events via logcat to replicate remote pairing.

---

### 0x62 — IC Config Write (EEPROM)

- **Source of Truth**: [C14184b.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/tc/C14184b.java) (`m4806C()`) and [C9021i.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/p067dd/C9021i.java)
- **Format (11 bytes)**:

```
[0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, checksum]
```

- **Endianness**: Big-Endian (`ptsHi = points >> 8`, `ptsLo = points & 0xFF`)
- **SK8Lytz relevance**: CRITICAL — hardware provisioning (LED count, IC type, strip config)

> **🔬 EXTREME DETAIL: THE 0x62 BYTE MAPPINGS**
>
> 1. **`ptsHi` / `ptsLo`**: The number of addressable LEDs **per segment** (Big-Endian).
> 2. **`segHi` / `segLo`**: The number of identical physical copies that mirror the pattern in parallel (Big-Endian). Total physical LEDs = `points × segments`.
> 3. **`icType`**: Defines the hardware chip. Mapped directly from [C9021i.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/p067dd/C9021i.java):
>    - `1` = UCS1903, `2` = SM16703, `3` = WS2811, `4` = WS2812B, `5` = SK6812, `6` = INK1003, `7` = WS2801, `8` = LB1914.
>    *(SK8Lytz defaults to `4` / WS2812B)*
> 4. **`sorting`**: The RGB color order. 
>    - `1` = RGB, `2` = RBG, `3` = GRB, `4` = GBR, `5` = BRG, `6` = BGR.
> 5. **`micPts` / `micSegs`**: Same segment logic, but specifically sets the bounds for when `0x73` Music Mode is active.

> **🔬 SEGMENT MODEL DISCOVERY (2026-04-22 — BLE Sniff Observation)**
>
> `points` and `segments` are NOT equivalent to total LED count. The hardware treats them as:
>
> - **`points`** = number of addressable LEDs **per segment** (the design canvas)
> - **`segments`** = number of identical physical copies that mirror the pattern in parallel
> - **Total physical LEDs** = `points × segments`
>
> **Example — HALOZ (16 bulbs, 8 points, 2 segments):**
> The ZENGGE Multi-Color creator shows 11 color positions (= `points`), NOT 22.
> The hardware automatically mirrors the 11-LED pattern onto the second segment.
> The app never exposes segments to the user — it is hardware-transparent.
>
> **Critical Implication for `0x59` and `0x51`:**
> All pixel array commands (`0x59`, `0x31`) should be built using `ledPoints` (11), NOT `ledPoints × segments` (22).
> Sending 22 pixels to a 2-segment device bypasses the hardware's segment mirror engine and fills both segments manually, which may produce unexpected results.
>
> **The `0x51` `flags=0x80` connection:**
> The "section toggle" bit in the `0x51` slot flags byte (`0x80`) is believed to control segment mirroring:
>
> - `flags = 0x80` → pattern mirrors across ALL segments (hardware duplication ON)
> - `flags = 0x00` → pattern plays linearly, ignoring segment boundaries

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
- **Format (13 bytes)**: `[0x73, isOn, modeType, effectId, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, sensitivity, brightness, checksum]`
- **isOn**: `0x01` = Device Mic Active (Hardware processes audio). `0x00` = App Mic Active (Hardware mic OFF, waits for `0x74` magnitude streams).
- **modeType**: `0x26` (38) = Light Bar Mode (16 built-in patterns). `0x27` (39) = Light Screen Mode (30 built-in patterns).
- **effectId**: 1–30 music-reactive pattern IDs (mapped in `MusicDictionary.ts`)
- **FG/BG Colors:** Dependent on `modeType` and `effectId`.
  - **Light Bar (`0x26`)**: Only IDs 11, 14, 15 support a color picker. The selected color populates BOTH FG and BG bytes simultaneously.
  - **Light Screen (`0x27`)**: ALL 30 IDs support a Foreground (Sound Column) color. Only IDs 19-24 and 28-30 support a Background (Drop) color.
- **sensitivity / brightness**: 0–255

---

### 0x74 — Music Magnitude Stream ✅ OUR DEVICE

- **Builder**: `C7788y.java` → `m20863a(int magnitude, device)`
- **Format**: 3 bytes

```
[0x74, magnitude(0-255), checksum]
```

- **Used when**: micSource in the `0x73` config
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
| `0x10` | 16 | Unknown (ZENGGE app init) | **Session Time Sync** — sent as ATT Write REQUEST (expects response) on connection. Payload contains year/month/day/time. Checksum-verified from sniff. |
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

### SymphonyEffect Color UI Mapping — HARDWARE OBSERVED GROUND TRUTH

> [!CAUTION]
> **The APK-derived `SymphonyEffectUIType` mapping does NOT match what the ZENGGE app shows on 0xA3 hardware.**
> The user directly tested all 33 effects in the ZENGGE Customize Tab (2026-04-22).
> **Hardware observation is the source of truth. APK enum is reference only.**

**HARDWARE-OBSERVED mapping (ZENGGE app, Customize Tab, 0xA3 firmware):**

| Effect IDs | Color Input Available | Notes |
|:-----------|:---------------------|:------|
| 1–6, 17–19, 25, 31, 32 | **FG + BG both** | Full two-color picker |
| 7 | **FG only** | Single foreground color picker |
| 8–13, 15–16, 20–24, 26–30, 33 | **No color picker** | Rainbow / multicolor / algorithmic |
| 14 | Unknown | Not reported — needs manual check |

> **CONFLICT WITH APK**: `C9021i.java` assigns `UIType_ForegroundColor_BackgroundColor` to effects 5–18, but hardware only shows color pickers on 1–6, 17–19. The APK UIType reflects a different firmware variant. **Do NOT use the APK table to drive SK8Lytz UI behavior.**

**APK-derived `SymphonyEffectUIType` enum (reference only — not HW truth for 0xA3):**

```java
enum SymphonyEffectUIType {
    UIType_ForegroundColor_BackgroundColor,  // APK claims 5–18
    UIType_StartColor_EndColor,              // APK claims 1, 3, 4
    UIType_FirstColor_SecondColor,           // APK claims 19–26
    UIType_Only_ForegroundColor,             // APK claims 2
    UIType_Only_BackgroundColor,             // APK claims 27–28
    IType_NoColor                            // APK claims 29–44
}
```

---

## SECTION 6: MANUFACTURER DATA ADVERTISEMENT PARSING

Our scanner reads `productId` from advertisement via `ZenggeProtocol.parseFirmwareFromAdvertisement()`:

```typescript
const productId = ((buffer[10] & 0xFF) << 8) | (buffer[11] & 0xFF);
```

Sample confirmed manufacturer data (base64): `AFpWBQhl8JrCPACjLgMBAiMkAR8AAP8AAwALAAA=`
Decoded relevant fields: `product_id = 163` (0xA3), `firmware_ver = 46`, `ble_ver = 5`, `led_ver = 3`

**Symphony detection byte**: `mfBuf[9] === 0x33 || mfBuf[9] === 0xBF` — our scanner reads this correctly.

> **BLE Characteristic Confirmation (2026-04-22)**: Our app writes to `ZENGGE_CHARACTERISTIC_UUID = '0000ff01-...'` (FF01). Live BLE sniff confirms the ZENGGE app writes to ATT handle `0x0017` on this device. Both apps resolve the same GATT service table, so handle `0x0017` = FF01 on our hardware. **Our write characteristic is correct.** ✅

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

### BUG-1: `0x51` Slot Format — UPDATED VERDICT (2026-04-22)

- **Previous diagnosis**: 10B extended (323B) required for 0xA3 per APK
- **Oracle Lab result**: 9B compact (291B) WORKS ✅ — 10B extended (323B via our wrapper) does NOTHING ❌
- **Root cause**: The 10B extended format requires the ZENGGE chunked BLE framing header (`40 seq 00 00 01 43 BD 0B`) which our `wrapCommand` does not emit
- **Current status**: 9B compact format is safe and functional for production use
- **Future work**: Replicate ZENGGE chunked framing to unlock true 10B slot support
- **Impact**: LOW (current scenes work) — track as enhancement, not critical bug

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
Mic Magnitude:  RecordService → C7788y → [0x74, magnitude, chk]
Custom Scene:   ZENGGE App Customize Tab → chunked 0x51 → [40 seq 00 00 01 43 BD 0B 51 slot×10×32 chk]
Custom Scene:   SK8Lytz setCustomMode() → standard wrap → [0x51 slot×9×32 0x0F chk]  ← 9B works on HW
Multi-Color:    ZENGGE App Multi-Color Tab → live 0x31 stream → per-frame pixel array

---

## SECTION 11: ORACLE HARDWARE VALIDATION — GROUND TRUTH (2026-04-22)

All results from physical hardware testing using `Sk8LytzDiagnosticLab` Oracle tab + live BLE HCI sniff.
Device: Pixel 7 (Android 16), HCI log extracted via `adb bugreport`.

### Power (0x71) — ✅ VERIFIED
- PWR ON and PWR OFF both work correctly. No surprises.

### RBM Ceiling (0x42) — ✅ VERIFIED  
- Effect IDs 1–100 all work correctly.
- Effect ID 101 (over ceiling): hardware **accepts it** and plays an undocumented effect. The ceiling is soft, not enforced.

### Music Mode (0x73) — ✅ TRUE-UP COMPLETED

**The `0x73` payload is strictly a 13-byte configuration command.** It dictates the active music visualization mode, its colors, and whether the hardware microphone is active. It does **not** transmit magnitude data (that is `0x74`).

**Format (13 Bytes)**: `[0x73, isOn, modeType, modeId, fgR, fgG, fgB, bgR, bgG, bgB, sensitivity, brightness, checksum]`

- **`isOn` (Byte 1)**: `0x01` activates music mode. `0x00` disables it.
- **`modeType` (Byte 2)**: Defines the hardware pattern matrix.
  - `0x26`: Light Bar Matrix (16 modes)
  - `0x27`: Light Screen Matrix (30 modes)
- **`modeId` (Byte 3)**: The specific effect ID (1-16 or 1-30).
- **`fg` and `bg` (Bytes 4-9)**: Foreground (Sound Column) and Background (Drop) colors. Ignored by generative modes.
- **`sensitivity` (Byte 10)**: 0-100 (hardware range).
- **`brightness` (Byte 11)**: 0-100 (hardware range).

> ⚠️ **The "micSource" Hallucination:** Previous documentation incorrectly assumed `modeType` was `micSource`. In reality, the device mic *always* listens when a `0x27` or `0x26` mode is active *unless* it is overridden by rapid `0x74` magnitude streams from the app mic.

### 🚨 Live Audio Streaming Protocol (0x74) — NEW DISCOVERY
When the user toggles "App Mic" in the ZENGGE app, it uses the phone's microphone to analyze the audio spectrum and continuously firehoses a rapid **3-byte** volume magnitude stream directly to the hardware.

**Format (3 Bytes)**: `[0x74, magnitude, checksum]`
- `magnitude` (Byte 2): Scaled volume/frequency value from 0-150.

This allows the hardware to respond instantly to high-quality audio captured directly by the mobile device, bypassing the cheap built-in microphone on the `0xA3` controller.

#### Light Bar Modes (`0x26`) — 16 Total
| ID | Mode Name | FG (Sound) | BG (Drop) | Banner Image Asset |
|:---|:---|:---:|:---:|:---|
| 1-10, 12, 13, 16 | Generative / Spectrum | ❌ | ❌ | `music_ic_[ID].png` |
| 11, 14, 15 | Monochromatic Rhythm | ✅ | ❌ | `music_ic_[ID].png` |

#### Light Screen Modes (`0x27`) — 30 Total
| ID | Mode Name | FG (Sound) | BG (Drop) | Banner Image Asset |
|:---|:---|:---:|:---:|:---|
| 1-18, 25-27 | Advanced Spectrum | ❌ | ❌ | `banner_[ID]_1.png` / `banner_[ID]_2.png` |
| 19-24, 28-30 | Custom Matrix Rhythm | ✅ | ✅ | `banner_[ID]_1.png` / `banner_[ID]_2.png` |

### The Parity Bridge (0x59 vs 0x51) — ARCHITECTURAL MANDATES
1. **The Spatial-Temporal Fallacy:** `0x59` is mathematically locked out of temporal effects (Breathe, Jump, Twinkly). We cannot send a custom 150-pixel segment array and ask the hardware to "Breathe" it natively. The hardware firmware rejects it and forces Static (`0x01`) or Running (`0x02`).
2. **The "Software Ticking" Imperative:** To achieve custom sub-segment temporal effects, the App's `PatternEngine` must act as a Software Sequencer, manually calculating frames and streaming `0x59` spatial payloads at 15-30 FPS over BLE.
3. **The 512-Byte MTU Mandate:** To fix spatial truncation (where `0x59` freezes on strips longer than the MTU constraint), we MUST negotiate a 512-byte MTU (`device.requestMTU(512)`) in React Native BLE Plx. The previous `numPoints=54` tiling hack has been proven mathematically invalid for the `0xA3` controller.
4. **Scene Sequencer (0x51) is King:** For whole-strip temporal transitions (Breathe/Jump/Strobe), the `PatternEngine` MUST bypass `0x59` and route the request to a 9-byte `0x51` Scene Sequence.

### Custom Scene (0x51) — ✅ 9B COMPACT WORKS
| Test | Result |
|:-----|:-------|
| 9B compact format (current production) | ✅ PASS — Red/blue animated pattern fires correctly |
| 10B extended format (via our wrapCommand) | ❌ FAIL — Does nothing |

> **SEE BUG-1 UPDATE**: 10B extended fails due to our wrapper mismatch, not the hardware rejecting 10B slots.

#### 0x51 Sequence Modes (The 44 Baked Hardware Effects / SymphonyEffects)
The ZENGGE `0x51` sequence editor (`ActivityCustomSymphonyEdit.java`) relies on 44 baked-in hardware effects (known internally as `SymphonyEffect` 1-44). 

We extracted the actual English string translations for all 44 modes from the APK's `strings.xml`:

**Single/Dual Color Modifiable (1-34):**
1. Change gradually *(Crossfade/Breathe between FG and BG)*
2. Bright up and Fade gradually *(Pulse to black)*
3. Change quickly *(Hard Jump)*
4. Strobe-flash
5. Running, 1point from start to end
6. Running, 1point from end to start
7. Running, 1point from the middle to the both ends
8. Running, 1point from the both ends to the middle
9. Overlay, from start to end
10. Overlay, from end to start
11. Overlay, from the middle to the both ends
12. Overlay, from the both ends to the middle
13. Fading and running, 1point from start to end
14. Fading and running, 1point from end to start
15. Olivary Flowing, from start to end
16. Olivary Flowing, from end to start
17. Running, 1point w/background from start to end
18. Running, 1point w/background from end to start
19. 2 colors run, multi points w/black background from start to end
20. 2 colors run, multi points w/black background from end to start
21. 2 colors run alternately, fading from start to end
22. 2 colors run alternately, fading from end to start
23. 2 colors run alternately, multi points from start to end
24. 2 colors run alternately, multi points from end to start
25. Fading out Flows, from start to end
26. Fading out Flows, from end to start
27. 7 colors run alternately, 1 point with multi points background, from start to end
28. 7 colors run alternately, 1 point with multi points background, from end to start
29. 7 colors run alternately, 1 point from start to end
30. 7 colors run alternately, 1 point from end to start
31. 7 colors run alternately, multi points from start to end
32. 7 colors run alternately, multi points from end to start
33. 7 colors overlay, multi points from start to end
34. 7 colors overlay, multi points from end to start

**7-Color Generative / No-Color (35-44):**
35. 7 colors overlay, multi points from the middle to the both ends
36. 7 colors overlay, multi points from the both ends to the middle
37. 7 colors flow gradually, from start to end
38. 7 colors flow gradually, from end to start
39. Fading out run, 7 colors from start to end
40. Fading out run, 7 colors from end to start
41. Runs in olivary, 7 colors from start to end
42. Runs in olivary, 7 colors from end to start
43. Fading out run, 7 colors start with white color from start to end
44. Fading out run, 7 colors start with white color from end to start

Below is the definitive UI gating logic (`C9273c.java`) dictating which features each effect supports:

If a feature is ❌, the hardware ignores that byte in the `0x51` slot.

| Effect ID (1-44) | Foreground Color | Background Color | Direction | Segment / Section |
|:---|:---:|:---:|:---:|:---:|
| 1, 2, 3, 6, 17, 18, 19, 31 | ✅ | ✅ | ✅ | ✅ |
| 4, 5 | ✅ | ✅ | ❌ | ✅ |
| 7, 14 | ✅ | ❌ | ❌ | ❌ |
| 8, 9, 10, 11, 12, 13, 15, 16 | ❌ | ❌ | ❌ | ❌ |
| 20, 21, 22 | ❌ | ❌ | ✅ | ❌ |
| 23, 26, 30 | ❌ | ❌ | ❌ | ✅ |
| 24, 28, 29 | ❌ | ❌ | ✅ | ✅ |
| 25, 32 | ✅ | ✅ | ✅ | ❌ |
| 27 | ❌ | ❌ | ✅ | ❌ |
| 33, 34 | ❌ | ✅ | ✅ | ✅ |
| 35-44 (IType_NoColor) | ❌ | ❌ | ✅ | ✅ |

*Note: Speed is supported by ALL 44 effects.*

### Parity Bridge Implementation Strategy
To achieve perfect visualizer-to-hardware parity for temporal effects (Breathe, Jump, Strobe) without triggering `0x59` MTU lag, `PatternEngine.ts` must intercept these specific selections and route them through `0x51` instead:
- **Color Breathing**: Intercept `PatternEngine` ID 24 -> Route to `0x51` `SymphonyEffect` ID **1** (Change gradually)
- **Strobe Flash**: Intercept `PatternEngine` ID 26 -> Route to `0x51` `SymphonyEffect` ID **4** (Strobe-flash)
- **Jump**: Re-add to `PatternEngine` -> Route to `0x51` `SymphonyEffect` ID **3** (Change quickly)


### Phase 2 Extended Panels — ❌ ALL FAILED via our wrapper
| Opcode | Test | Result | Notes |
|:-------|:-----|:-------|:------|
| `0x41` Settled Mode | TX effectId=1, red/blue | ❌ No response | Our payload format doesn't match actual firmware expectation |
| `0x43` Multi-Sequence | TX effectIds 1,2,3 | ❌ LEDs SHUT OFF | Hardware state machine crash — packet rejected |
| `0x53` Live Pixel Stream | Gradient animation | ❌ No response | Hardware likely doesn't support live pixel streaming |

### Live BLE Sniff — Raw Packet Evidence
Capture 1: 1-step customize (Effect 6, Speed 50%, White FG, Black BG):
```
ATT Write 0x0017: 40 04 00 00 01 43 BD 0B 51 F0 06 32 FF FF FF 00 00 00 80 [0F×31] D6
```

### The MTU Packet-Chunking Algorithm (Reverse-Engineered 2026-04-24)
> **🔬 SOURCE CODE DISCOVERY:** 
> Found inside the APK at `com.zengge.hagallbjarkan.protocol.zgble.LowerTransportLayerEncoder.java` (`generator` method) and `UpperTransportLayer.java`.

The raw BLE sniff above contains the signature `40 04 00 00 01 43 BD 0B`. Initially mistaken for a magic static header, we reverse-engineered the actual ZENGGE fragmentation protocol used for payloads larger than the BLE MTU (e.g., `0x51` 323-byte payloads or long `0x59` spatial arrays).

If a payload exceeds `MTU - 3` bytes (the max GATT write size), it must be chunked:

1. **First Chunk (8-byte header):**
   - `[0]`: Control Byte (`0x40` = multi-segment, no ack, no protect)
   - `[1]`: Sequence Counter (`0x00`-`0xFF`, increments per transaction)
   - `[2], [3]`: Segment Index (`0x00 0x00` for the first chunk)
   - `[4], [5]`: Total Payload Length (`0x01 0x43` = 323 bytes)
   - `[6]`: Payload length in *this specific chunk* (`MTU - 8`)
   - `[7]`: Command ID (`0x0B` for Control/Write)
   - `[8...]`: First batch of payload data.

2. **Subsequent Chunks (5-byte header):**
   - `[0]`: Control Byte (`0x40`)
   - `[1]`: Sequence Counter (Must match Chunk 1)
   - `[2], [3]`: Segment Index (e.g., `0x00 0x01`, `0x00 0x02`...)
      - ⚠️ **CRITICAL (The Terminator):** The absolute last chunk sets the Most Significant Bit (`0x8000 | index`) to signal EOF and trigger the hardware execution.
   - `[4]`: Payload length in *this specific chunk* (`MTU - 5`)
   - `[5...]`: Next batch of payload data.

**Why `0x43`, `0x41`, and extended `0x51` failed previously:**
Our `writeChunked` function was forcing the 8-byte header `0x01, 0x43, 0xBD, 0x0B` on *every* chunk and assuming an MTU of 196 (`0xBD` = 189 bytes data + 7). If Android/iOS negotiated an MTU of 23 (20 bytes data), the hardware's state machine crashed waiting for the missing 169 bytes of chunk 1.

**Impact on other protocols / probing:**
- **Probing & Connecting:** Probing uses short packets (`0x63` requests are only ~12 bytes) so they never trigger chunking. However, connection MTU negotiation is now critical. We must cache the negotiated MTU (`getDeviceMtu(deviceId)`) and dynamically feed it into the chunking math, since older Androids negotiate MTU 23 while modern iOS/Android negotiate 185-512.
- **Large Spatial Arrays:** The `0x59` protocol on large strips (e.g., 450 bytes) MUST use this identical chunking algorithm to prevent spatial pattern corruption.

Capture 2: 3-step customize (Effect 3 Speed 25% Red/Blue, Effect 10 Speed 75%, Effect 20 Speed 50%):
```

ATT Write 0x0017: 40 06 00 00 01 43 BD 0B 51
  F0 03 19 FF 00 00 00 00 FF 80   ← Effect 3, Speed 0x19=25%, FG=Red, BG=Blue, Forward
  F0 0A 4B FF FF FF 00 00 00 80   ← Effect 10, Speed 0x4B=75%, FG=White (ignored), Forward
  F0 14 32 FF FF FF 00 00 00 80   ← Effect 20, Speed 0x32=50%, FG=White (ignored), Forward
  [0F×29 empty slots]
  D? checksum

```

> **NOTE (corrected by SymphonyEffect map)**: Effects 10 and 20 are NOT `IType_NoColor` — they are `UIType_ForegroundColor_BackgroundColor` and `UIType_FirstColor_SecondColor` respectively and DO accept colors. The `FF FF FF` / `00 00 00` in this capture were the default unconfigured values, not proof of NoColor type.

### Confirmed 10-Byte Slot Byte Map

```

Offset  Field          Notes
[0]     ACTIVE_FLAG    0xF0 = active, 0x0F = empty
[1]     effectId       SymphonyEffect 1–44 (29–44 = NoColor, 1–28 = color-accepting)
[2]     speed          0x00–0x64 (0–100)
[3]     FG.R           Foreground Red   (ignored for effects 29–44)
[4]     FG.G           Foreground Green (ignored for effects 29–44)
[5]     FG.B           Foreground Blue  (ignored for effects 29–44)
[6]     BG.R           Background Red   (ignored for effects 29–44)
[7]     BG.G           Background Green (ignored for effects 29–44)
[8]     BG.B           Background Blue  (ignored for effects 29–44)
[9]     flags          0x80 = forward+section_toggle ON, 0x00 = reverse/no-toggle

```

### Mystery Session Init Packets — Partial Decode

These ~3 packets appear in EVERY capture BEFORE any LED commands. Sent by ZENGGE app immediately on connection.

**[12157] ATT Write REQUEST (0x12, expects response)**:
```

Raw: 00 01 80 00 00 0C 0D 0B 10 14 1A 04 16 00 04 2B 03 00 0F | 99
Header:  [00 01 80 00 00 0C 0D 0B] (standard 8-byte ZENGGE wrapper) ✓
Inner:   10 14 1A 04 16 00 04 2B 03 00 0F  (11 bytes)
Chksum:  99 = (10+14+1A+04+16+00+04+2B+03+00+0F) & 0xFF ✓ VERIFIED
Opcode:  0x10 = SESSION TIME SYNC (variant of 0x11)
Payload: 0x1A=26(year), 0x04=04(April), 0x16=22(day), remainder=time/weekday
Note:    Uses ATT WRITE REQUEST (not Write Without Response) — device ACKs this.

```

**[12169] and [12178]** — Use a DIFFERENT framing (byte 7 ≠ `0x0B`). Possibly a lightweight 7-byte or proprietary init packet format. Opcodes cannot be confirmed without the matching framing spec. Marked as **PARTIALLY DECODED**.

> **HYPOTHESIS**: These 3 packets are the ZENGGE app's session handshake sequence: (1) time sync to the device, (2) firmware version exchange, (3) ack/response. The mandatory time sync enables timer schedules stored in device EEPROM.

### Music Mode — The "Play" Button Explained

> **🔬 DISCOVERY (2026-04-22)**: The ZENGGE app has a separate "Play" button in the Music Mode tab. Here's why:
>
> - Sending `0x73` (music config) alone sets the MODE and mic routing but sends NO audio magnitude data
> - The hardware sits in music mode but receives no `0x74` magnitude packets → LEDs don't react
> - Pressing **Play** in the ZENGGE app starts the mic recording and the continuous `0x74` stream
>
> **SK8Lytz behavior** (`useAppMicrophone.ts`): automatically starts the `0x74` stream the moment `activeMode === 'MUSIC' && micSource === 'APP' && isPoweredOn` — effectively merging "configure" and "play" into one action.
>
> This is WHY music mode felt "fully enabled" during Oracle tests — the `useAppMicrophone` hook fires `0x74` the moment you're in MUSIC mode, masking any delays from the manual test panel.
EEPROM Write:   Programmer → C14184b.m4807B etc → [0x62, ...]
EEPROM Read:    Programmer → C14184b.m4771f0 → [0x63, 0x12, 0x21, 0x0F, chk]
```

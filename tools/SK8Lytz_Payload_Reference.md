# SK8Lytz Payload Reference
> Last updated: April 8, 2026  
> All payloads we BUILD OURSELVES (pixel-by-pixel, byte-by-byte) — not pass-through hardware modes

---

## 🔧 Packet Wrapper (Applied to Every Command)

Before any command hits the wire, `ZenggeProtocol.wrapCommand()` prepends an 8-byte BLE header:

```
[0x00] [SEQ] [0x80] [0x00] [LEN_HI] [LEN_LO] [LEN+1] [0x0B] [...payload]
```

| Byte | Value | Meaning |
|------|-------|---------|
| `00` | `0x00` | Packet start |
| `01` | `SEQ` | Rolling counter 0–255, increments each send |
| `02` | `0x80` | Fixed protocol flag |
| `03` | `0x00` | Reserved |
| `04–05` | `LEN_HI/LO` | 16-bit payload length |
| `06` | `LEN+1` | Length overflow byte |
| `07` | `0x0B` | Command family ID |
| `08+` | Payload | The actual command payload |

> [!NOTE]
> Checksum is always the last byte of the payload, calculated as: `sum(all_payload_bytes) & 0xFF`

---

## 📦 PAYLOAD 1: `0x59` — MultiColor Pixel Array (Our Primary Tool)

**Used by**: Multimode Patterns 1–5 & 9–10, Street Mode, Emergency Mode, Solid Color, DIY Array  
**Source**: `ZenggeProtocol.setMultiColor()`

### Byte Layout

```
[0x59] [LEN_HI] [LEN_LO]
  [R0] [G0] [B0]              ← LED 0  (pixel 0 color)
  [R1] [G1] [B1]              ← LED 1
  ...                          ← ... (one R/G/B triple per LED)
  [Rn] [Gn] [Bn]              ← LED n  (full LED count)
[PTS_HI] [PTS_LO]             ← numPoints (total LED count, 16-bit BE)
[TRANS]                        ← transitionType (see table below)
[SPEED]                        ← animation speed 1–31
[DIR]                          ← direction: 1=forward, 0=reverse
[CHECKSUM]
```

### Total Length
`totalLen = (numLEDs × 3) + 9`  
e.g. HALOZ 16 LEDs → 57 bytes. SOULZ 43 LEDs → 138 bytes.

### Transition Type Byte (Hardware-Confirmed via Live Device Testing Apr 2026)

| Value | Label | Confirmed Behavior |
|-------|-------|--------------------|
| `0x00` | CASCADE | ✅ Continuous scroll — hardware loops pixel array around strip. **Use for all animated patterns.** |
| `0x01` | FREEZE | ✅ Static lock — array held in place exactly as sent. **Use for solid/static patterns and street headlights/taillights.** |
| `0x02` | STROBE | ⚠️ Intended flash — behavior similar to FREEZE on some firmware. Use for hard brake emergency. |
| `0x03` | TRIGGER | 🔴 One-shot trigger — hardware renders array at NEXT internal offset then STOPS. Causes "blink + jump to new position" on every send. **NOT continuous animation. DO NOT use for animated patterns.** |

### Speed Range
- Animated types (`0x03`): clamped `1–31` (from APK Protocol/n.java)
- Static/Freeze (`0x01`): always sends `1` (speed unused)

### Color Rule
> [!IMPORTANT]
> **SEND PURE RGB** — do NOT call `applyColorSorting()` before building 0x59 payload.  
> The hardware remaps GRB internally via its 0x81/0x62 config register.  
> Exception: DIY Array mode intentionally applies sorting because those colors are user-chosen hex strings.

---

## 📦 PAYLOAD 2: `0x51` — DIY Custom Mode (2-Step Hardware Fade/Flash)

**Used by**: Multimode Patterns 6 (Breath), 7 (Flash), 8 (Strobe)  
**Source**: `ZenggeProtocol.setCustomMode()`

### Byte Layout

```
[0x51]
  [STEP_FLAG] [MODE] [SPEED] [FG.R] [FG.G] [FG.B] [BG.R] [BG.G] [BG.B]   ← Step 0 (9 bytes)
  [STEP_FLAG] [MODE] [SPEED] [FG.R] [FG.G] [FG.B] [BG.R] [BG.G] [BG.B]   ← Step 1 (9 bytes)
  ... × 32 steps total (inactive steps = 0x0F + 8 zeros)
[0x0F]        ← Terminator
[CHECKSUM]
```

**Total size: always 291 bytes** (32 steps × 9 bytes + 0x51 header + 0x0F + checksum)

### Step Mode Values

| Value | Constant | Effect |
|-------|----------|--------|
| `0x3A` | STEP_JUMP | Hard cut (instant color change) |
| `0x3B` | STEP_GRADUAL | Smooth fade/crossfade |
| `0x3C` | STEP_STROBE | Rapid flash |

> [!WARNING]
> Using `0x01` or `0x02` as step mode sends wrong bytes. Must use `0x3A`, `0x3B`, `0x3C`.

### How Patterns 6, 7, 8 use 0x51

| Pattern | Steps | Mode | Behavior |
|---------|-------|------|----------|
| **6 Breath** | Step0: FG→BG, Step1: BG→FG | `STEP_GRADUAL (0x3B)` | Hardware loops smooth fade |
| **7 Flash** | Step0: FG→BG, Step1: BG→FG | `STEP_JUMP (0x3A)` | Hardware loops hard cuts |
| **8 Strobe** | Step0: FG→BG, Step1: BG→FG | `STEP_JUMP (0x3A)` at speed=100 | Max rate hard flash |

---

## 🎨 PAYLOAD 3: Solid Color (`sendColor`)

**Used by**: Any mode that sends a single flat color (color picker, brightness slider, etc.)  
**Source**: `DockedController.sendColor(r, g, b)`

**How it's built:**
```
colors = Array(numLEDs).fill({ r, g, b })
ZenggeProtocol.setMultiColor(colors, speed=1, direction=1, transitionType=0x01)
```

- Same structure as PAYLOAD 1 (0x59)
- All pixels identical color
- `transitionType=0x01` (FREEZE) — no animation, immediate static output
- Speed always `1` (unused for freeze)

---

## 🔲 PAYLOAD 4: Multimode Patterns (PatternEngine pixel tiles)

**Used by**: Fixed Mode / Multimode → Patterns 1–5, 9–10  
**Source**: `DockedController.applyFixedPattern()` → `PatternEngine.buildPatternPayload()`

### How patternId maps to a pixel tile (tiled to full strip length)

| Pattern | Name | Pixel Tile (FG=foreground, BG=background) | Protocol | TransitionType |
|---------|------|-------------------------------------------|----------|----------------|
| **1** | Solid | `[FG]` — tiled | `0x59` | `0x01` FREEZE |
| **2** | Single Dot | `[FG, BG, BG, BG, BG, BG, BG, BG]` — 1 dot in 7 bg | `0x59` | `0x03` RunningWater |
| **3** | Comet | `[FG, FG×0.5, FG×0.2, BG, BG, BG]` — FG + 2-step dim trail | `0x59` | `0x03` RunningWater |
| **4** | Dashed | `[FG, FG, FG, FG, BG, BG, BG, BG]` — 4-on 4-off | `0x59` | `0x03` RunningWater |
| **5** | Alternating | `[FG, FG, BG, BG]` — 2-on 2-off | `0x59` | `0x03` RunningWater |
| **6** | Breath | *(no pixel array)* | `0x51` GRADUAL | N/A |
| **7** | Flash | *(no pixel array)* | `0x51` JUMP | N/A |
| **8** | Strobe | *(no pixel array)* | `0x51` JUMP max speed | N/A |
| **9** | Wave | `[BG, BG, FG, FG, BG, BG]` — FG pair in BG field | `0x59` | `0x03` RunningWater |
| **10** | Pinch | `[FG, BG, BG, BG, BG, FG]` — FG at each end | `0x59` | `0x03` RunningWater |

### Brightness: applied before tile construction
```
fgRgb.r = Math.round(parseInt(fgHex.r) * (brightness / 100))
```

### Speed: UI 0–100 → Hardware 1–31
```
hwSpeed = Math.round((uiSpeed / 100) * 31)  // clamped min=1, max=31
```

---

## 🎿 PAYLOAD 5: Street Mode — Car Light Layout

**Used by**: Street Mode (accelerometer-reactive)  
**Source**: `DockedController.applyStreetPattern()`  
**Protocol**: `0x59` via `ZenggeProtocol.setMultiColor(arr, hwSpeed, 1, transType)`

### Zone Layout

**HALOZ** (2-segment ring, 16 LEDs — 8-LED mirrored frame):
```
Frame8:  [RED, RED, CRUISE, CRUISE_DIM, CRUISE, CRUISE_DIM, WHITE, WHITE]
Mirror8: [WHITE, WHITE, CRUISE_DIM, CRUISE, CRUISE_DIM, CRUISE, RED, RED]
Result:  RED at back of ring, WHITE at front, CRUISE color on both sides
```

**SOULZ** (linear strip, N LEDs — zone proportions):
```
[rear 30% = RED tail lights]
[mid 40%  = alternating CRUISE / CRUISE_DIM (zebra)]
[front 30% = WHITE headlights]
```

### Motion State → Color Mapping

| Motion State | Cruise Color | Tail Brightness | TransitionType |
|-------------|-------------|-----------------|----------------|
| CRUISING | User's chosen color (default `#FF8C00` amber) | 45% dim red | `0x01` FREEZE |
| ACCELERATING | User's chosen color | 45% dim red | `0x01` FREEZE |
| SLOWING_DOWN | `#FFFF00` Yellow | 45% dim red | `0x01` FREEZE |
| STOPPED | `#FF0000` Red | Full red | `0x01` FREEZE |
| HARD_BRAKING | `#FF0000` Red | Full red | `0x02` STROBE |

> [!NOTE]
> `0x01` (FREEZE) is used for CRUISE/STOP so the headlight/taillight zones stay physically locked in position.
> `0x02` (STROBE) is only used for HARD_BRAKING to flash the entire array as an emergency signal.

---

## 🚨 PAYLOAD 6: Emergency Alert Pattern

**Used by**: Programs mode Pattern ID 100, and Emergency Alert button  
**Source**: `DockedController.applyEmergencyPattern()`  
**Protocol**: `0x59` with `transitionType=0x03` (RunningWater — hardware scrolls)

**HALOZ** (16-LED ring):
```
Frame8:  [RED, RED, YELLOW, OFF, YELLOW, OFF, WHITE, WHITE]
Mirror:  [WHITE, WHITE, OFF, YELLOW, OFF, YELLOW, RED, RED]
```

**SOULZ** (linear):
```
[RED, RED, RED, RED, YELLOW, OFF, YELLOW, OFF, YELLOW, OFF, YELLOW, OFF, WHITE, WHITE, WHITE, WHITE]
```

Hardware scrolls this with `0x03` (RunningWater) — so the red/yellow/white zones cycle visually around the strip.

---

## 🌈 PAYLOAD 7: DIY Array (User-Built Color Sequence)

**Used by**: Multimode → DIY Array sub-mode  
**Source**: `DockedController` speed slider `onSlidingComplete` + DIY Array Builder component

```js
rgbColors = multiColors.map(hexColor => {
  // Parse hex → RGB, scale by brightness factor
  rawR = parseInt(hex.r) * brightnessFactor
  // Then COLOR SORT (unlike other modes, DIY applies sorting):
  return ZenggeProtocol.applyColorSorting(rawR, rawG, rawB, hwSettings.colorSorting)
})
ZenggeProtocol.setMultiColor(rgbColors, clampSpeed(speed), direction=1, multiTransition)
```

> [!IMPORTANT]
> **DIY Array DOES apply `applyColorSorting()`** — this is intentional and correct here because the user is picking colors manually by name ("red", "green") and the hardware needs the channels swapped to display them right. All other modes skip this.

### `multiTransition` options in DIY mode

| Value | Visual Effect |
|-------|--------------|
| `0x01` | FREEZE — static array (no scroll) |
| `0x03` | RunningWater — hardware scrolls colors around the strip |

---

## 📡 Other Utility Commands (Not Custom-Built Payloads)

| Command | Payload | Purpose |
|---------|---------|---------|
| **Power On** | `[0x71, 0x23, 0x0F, 0xA3]` | Turns all LEDs on |
| **Power Off** | `[0x71, 0x24, 0x0F, 0xA4]` | Turns all LEDs off |
| **Query HW Settings** | `[0x63, 0x12, 0x21, 0x0F, CS]` | Request device's IC, LED count, color order |
| **Programs/RBM** | `0x61` via `setCustomRbm(patternId, speed, brightness)` | Select one of 210 built-in hardware patterns by ID |
| **Music Mode** | `0x73` via `setMusicConfig(...)` | Configure mic-reactive symphony mode |

---

## 🔑 Key Rules Summary

1. **Always send PURE RGB** in `0x59` (except DIY Array which uses sorted colors)
2. **Animated patterns need `0x03`** — `0x00` appears to be CASCADE but actually FREEZES the array
3. **Static/solid needs `0x01`** — locks the exact pixel positions sent  
4. **STROBE `0x02`** flashes the whole array — only for hard braking emergency  
5. **Speed for 0x59**: UI `0–100` → hardware `1–31` via `clampSpeed()`
6. **Speed for 0x51** (DIY step mode): full `1–100` range valid
7. **LED count = `hwSettings.ledPoints`** — do NOT divide by segments (segments is an IC layout param, not a pixel-count divisor)

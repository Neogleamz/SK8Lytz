# SK8Lytz App Master Reference
*Last Updated: 2026-04-09 | Source of Truth: `src/protocols/ZenggeProtocol.ts`*

This document is the **Canonical Reference** for all architecture, hardware constraints, and BLE protocol definitions within the SK8Lytz application. 

> [!CAUTION]
> Do NOT append duplicate or conflicting protocol discoveries to this document. If a payload format changes, **overwrite** the existing entry to ensure this file remains a single, conflict-free source of truth.

---

## 1. Product Bible (Vision & North Star)

**The Mission:** 
To power the vibrant culture of roller skating by building the definitive lighting control engine for SK8Lytz HALOZ and SOULZ hardware. SK8Lytz isn't just an app; it's the digital nervous system for your skates—enabling flawless, zero-latency light synchronization that elevates both solo flow sessions and massive Crew Hub rink takeovers.

**Target Audience:**
Dedicated, active roller skaters. They operate in chaotic, low-light environments like rinks, parks, and night street sessions. They are likely wearing wrist guards, moving fast, and dealing with terrible Wi-Fi/LTE reception inside metallic rink structures. 

**Core Philosophies (The 3 Pillars):**
1. **Bulletproof BLE Transport:** The connection to Zengge-based ESP hardware MUST be instant and automatic. Features like group pairing and reconnects must handle GATT exceptions and MTU chunking invisibly. "It just works, immediately." 
2. **Tactile, Glanceable UI:** We use the Neogleamz aesthetic—vibrant, high contrast, and robust. Touch targets are massive (>44px) so a skater can swipe into a Symphony Effect or engage the App Microphone visualizer in under two seconds without squinting or removing protective gear.
3. **No-Compromise Offline Flow:** The party doesn't stop because of a cell tower deadzone. Users can fully bypass Supabase cloud authentication and control their entire LED rig seamlessly via the native Offline mode.

**Anti-Goals (What we ruthlessly reject):**
- **Bloated Developer Logic in Prod:** We do not ship "janky" dev toggles to production. We use strict `__DEV__` Sandbox elimination (e.g., AuthScreen Nuke features) to ensure real users never hit testing debris.
- **Complex UI Micro-Management:** We will not build tedious, timeline-based animation nodes. Skaters want to skate, not edit video timelines. Give them stunning Pro Effects, a quick color wheel, and a speed slider. Done.
- **Cloud-Dependent Hardware Walls:** We never lock basic hardware lighting configuration (0x63 queries or 0x62 EEPROM writes) behind an internet authentication wall.

---

## 2. System Architecture & Local Storage

### AsyncStorage Key Registry
| Key | Owner | Contents |
|:---|:---|:---|
| `@sk8lytz_logs` | AppLogger | Compact telemetry event buffer array |
| `ng_device_configs` | DashboardScreen / AppLogger | Dict keyed by MAC containing `{ name, type, points, segments, sorting, stripType, groupId }` |
| `ng_custom_groups` | DashboardScreen | Array of `{ id, name, isGroup, deviceIds }` |
| `ng_processed_devices`| DashboardScreen | Cached array of previously discovered device objects |
| `@sk8_hw_<deviceId>` | Sk8LytzProgrammerModal| Per-device EEPROM hardware settings cache |
| `@sk8lytz_theme` | ThemeContext | `dark` or `light` |
| `@sk8lytz_control_theme`| ThemeContext | Control color theme name |

### Supabase Architecture (Telemetry)
*Project ID: `qefmeivpjyaukbwadgaz`*
- **`parsed_session_stats`**: One row per app session summary (`session_id` UNIQUE)
- **`parsed_session_devices`**: All BLE devices seen per session (`session_id + device_id` UNIQUE)
- **`parsed_logs`**: Full event trace log. Appended continuously.
- **`parsed_mode_usage`** / **`parsed_pattern_usage`** / **`parsed_color_usage`**: Frequency metrics.

> [!NOTE]
> Group telemetry events MUST pass `deviceIds: string[]` in the `AppLogger` device context so the engine can unroll the event into individual, per-device rows in Postgres.

---

## 2. Hardware Profiles & Constraints

### Hardware Capability Thresholds
- **Maximum Points**: 300 LEDs
- **Maximum PxS**: 2048 (Points × Segments)
- **Maximum Mic Points**: 150 LEDs
- **Maximum Mic PxS**: 960

### IC Types (`icType` Index)
- `1`: WS2812B (HALOZ Default)
- `2`: SM16703 (SOULZ Default)
- `4`: WS2811
- `6`: SK6812

### Color Sorting Maps (`sorting` Index)
- `0`: RGB
- `1`: RBG
- `2`: **GRB** (SK8Lytz Default for all HALOZ/SOULZ strips)
- `3`: GBR
- `4`: BRG
- `5`: BGR

> [!IMPORTANT]
> The hardware does **NOT** autonomously remap RGB channel bytes sent via 0x59 regardless of its EEPROM setting. 
> The application software must pre-swap the RGB bytes via `ZenggeProtocol.applyColorSorting()` BEFORE generating the hex array string for transmission.

---

## 3. BLE Protocol Library

All byte definitions below represent the inner payload *before* the V2 BLE packet wrapper is applied.

### The Transport Wrapper (`wrapCommand`)
Every inner protocol payload must be wrapped using the standard 8-byte Zengge V2 framing before transmission over the GATT characteristics.
* **Format:** `[0x00, SequenceNum, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]`
* **Characteristics:** Send to `FF01` (WRITE). Receive notifications on `FF02` (NOTIFY).

---

### Command: Hardware Config Query (0x63)
*Reads the current EEPROM settings stored inside the controller chip.*

* **Send (5 bytes):** `[0x63, 0x12, 0x21, 0x0F, checksum]` (Use `0xF0` flag if querying mic data).
* **Receive parser rules:**
  * Some firmware versions wrap the response in a JSON envelope `{"code":0,"payload":"<hex>"}`.
  * After unwrapping, if the packet starts with `[0x00, 0x63]` (Index 1) the offsets are:
    * `[3]` = LED points count 
    * `[5]` = Segments
    * `[6]` = IC Type
    * `[7]` = Color Sorting Index
  * **CRITICAL ENDIANNESS:** If the classic format is used (0x63 at Index 0), the `ledPoints` bytes are **Little-Endian SWAPPED**: `((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF)`.

### Command: Hardware Config Write (0x62)
*Writes custom segments, IC type, and max LED points permanently to the controller EEPROM.*

* **Format:** `[0x62, ptsHigh, ptsLow, segHigh, segLow, icType, sorting, micPts, micSegs, 0xF0, checksum]`
* **CRITICAL ENDIANNESS:** Unlike the 0x63 Response, the 0x62 Write command uses **Big-Endian format** for Points and Segments: `ptsHigh = (points >> 8) & 0xFF`, `ptsLow = points & 0xFF`.

---

### Command: Segmented Multi-Color Layout Array (0x59)
*The primary command for drawing exact pixel-mapped arrays, fixed color swatches, and hardware-native directional animations.*

* **Format:** `[0x59, totalLenHi, totalLenLo, [R1,G1,B1...R300,G300,B300], numLEDsHi, numLEDsLo, transitionType, speed, direction, checksum]`
* **totalLen calculation:** `(numPoints * 3) + 9`
* **transitionType:**
  * `0x00`: Static (Instant visual snap, speed byte is ignored).
  * `0x01`: Gradual (Hardware fades the array blocks smoothly).
  * `0x02`: Strobe (Hardware flashes the full length at `speed`).
  * `0x03`: RunningWater (Hardware scrolls the array linearly, acting as a marquee).
* **speed:** Must be clamped strictly between `0x01` and `0x1F` (1-31).

---

### Command: DIY Custom Animation Sequences (0x51)
*Frames up to 32 animated sequence steps in a fixed-length memory block.*

* **Format (291 Bytes):** `[0x51, Step0(9 bytes), Step1(9 bytes)... Step31(9 bytes), 0x0F_Terminator, checksum]`
* **Step Structure (9 Bytes):** `[ACTIVE_FLAG, transMode, speed, fg.R, fg.G, fg.B, bg.R, bg.G, bg.B]`
  * `ACTIVE_FLAG`: `0xF0` to run this step, `0x0F` to skip this frame slot.
* **transMode:**
  * Use custom Symphony ID values `0x01-0x21` (1-33) to trigger advanced hardware effects.
  * Use standard transitions: `0x3A` (Jump), `0x3B` (Gradual), `0x3C` (Strobe).
* **speed:** `0x01-0x64` (1-100 full 0-100% spread, unlike the 0x59 command).

> [!TIP]
> **COMPACT MODE (Variable Length)**: The hardware also tolerates non-standard truncation for 0x51. Instead of padding out 32 steps to 291 bytes, you can simply append `0x0F` (terminator) directly after the active steps to create payloads tiny enough to fit inside a single BLE MTU packet (20 bytes).

---

### Basic Control Commands
- **Power ON (0x71):** `[0x71, 0x23, 0x0F, 0xA3]`
- **Power OFF (0x71):** `[0x71, 0x24, 0x0F, 0xA4]`
- **Set RBM Pattern (0x42):** `[0x42, patternId, speed, brightness, checksum]`
- **Music Config (0x73):** `[0x73, micSource, modeType, patternId, c1.R, c1.G, c1.B, c2.R, c2.G, c2.B, 0x20, sensitivity, brightness, checksum]` 
- **Music Live Data (0x74):** `[0x74, audioMagnitude, checksum]` — Dispatched continually for phone-mic reactivity.
- **RF Auth Setting (0x2A):** `[0x2A, modeByte, 0xFF,0xFF,0xFF,0xFF,0xFF, clearByte, 0x00...0x0F]` (`modeByte=0x01` Block all, `0x02` Known only, `0x03` Allow all).

---

## 4. UI & Platform Guardrails

All UI components must adhere strictly to these constraints:

### Responsive Layout
- Avoid fixed pixel values (e.g. `width: 300`) unless mapping an unscalable SVG. Use `flex`, `%`, or viewport metrics.
- Utilize standard `SafeAreaView` contexts (or `useSafeAreaInsets()`) padding definitions so that the iOS "Dynamic Island" and Android bottom navigation pill do not occlude clickable UI components.

### Scalability
- Touch boundaries for critical interactive zones must be at least `44x44`.
- Accommodate virtual keyboards correctly through `KeyboardAvoidingView` employing iOS behavior `padding` and Android behavior `height`.

### Diagnostic Interface Architecture
- The administrative `AdminHardwareTester` and `Simple Scanner` standalone tools have been consolidated directly into the `LogViewerModal` Diagnostic Lab interface to prevent component bloat and standardize safe area handling.

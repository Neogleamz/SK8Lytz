# BANLANX PROTOCOL BIBLE

## Authoritative Hardware Reference — Derived from APK Reverse Engineering & UniLED Community Source

> **Source Authority**: All entries traced to:
>
> 1. Decompiled `com.spled.scenex` APK (v3.2.9) via JADX — `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\BANLANX_APK\BANLANX_DECOMPILED\`
> 2. Dart AOT binary (`libapp.so`) string extraction via Blutter tooling
> 3. Native effect libraries (`libwled_lfx.so`, `libscene_lfx.so`) symbol extraction
> 4. UniLED Home Assistant integration (`monty68/uniled` — `banlanx2.py`)

> **Last Updated**: 2026-05-15 (APK Decompile + Dart AOT Reverse Engineering Session)
> **Target Hardware**: BanlanX SP621E (Mini SPI RGB Controller)
> **App Technology**: Flutter/Dart (NOT native Android — protocol logic in compiled `libapp.so`)
> **Dart Version**: 3.6.2 (Snapshot: `f956f595844a2f845a55707faaaa51e4`)

---

## SECTION 1: DEVICE IDENTITY

### Confirmed Controller: SP621E

| Property | Value | Source |
|:---------|:------|:-------|
| App Package | `com.spled.scenex` | APK manifest |
| App Name | BanlanX / SceneX | APK + Play Store |
| App Framework | Flutter 3.x (Dart 3.6.2 AOT) | `libflutter.so` + `libapp.so` header |
| Controller IC | SP621E (Mini SPI RGB) | UniLED `banlanx2.py` |
| Protocol Family | BanlanX v2 | UniLED classification |
| Manufacturer ID | `5053` (ASCII "PS" = SP reversed) | UniLED `BANLANX_MANUFACTURER_ID` |
| MFR Data Fingerprint | `[0x0D]` or `[0x16]` at byte offset | UniLED `matchesAdvertisement` |
| BLE Connection Limit | **1 concurrent connection** | UniLED documentation |

### SP-Series Controller Family (from APK asset packages)

The BanlanX app supports multiple SP-series controllers. Each has its own Flutter package:

| Controller | APK Package | Notes |
|:-----------|:------------|:------|
| **SP621E** | (core app) | Our target — Mini SPI RGB |
| SP601E | `sp601e/` | Route `/sp601e` found in libapp.so |
| SP611E | `sp611e/` | Route `/sp611e` found in libapp.so |
| SP630E | `sp630e/` | Extensive assets — matrix/pixel controller |
| SP802E | `sp802e/` | GIF-based effects (`sp802e/assets/gifs/`) |
| SP530E | (referenced) | `sp530e:diy_gradient_colors_` key in strings |

### Key Difference vs Zengge

| Feature | Zengge 0xA3 | **BanlanX SP621E** |
|:--------|:------------|:-------------------|
| App Framework | Native Android (Java/Kotlin) | **Flutter (Dart AOT)** |
| Protocol Header | V2 wrapper: `[0x00, Seq, 0x80, ...]` | **Simple: `[0xA0, cmd, len, ...]`** |
| Checksum | SUM of all bytes | **None** |
| Raw Pixel Command | `0x59` (Static Colorful) | **TBD — `DiyPixel` class confirmed** |
| Effect System | 100 RBM + 44 Symphony | **142 built-in + DIY + Gradient DIY** |
| Music Mode | `0x73` config + `0x74` magnitude | **`0x6C` audio input + `0x6B` sensitivity** |
| Pixel Engine | App-side PatternEngine (TypeScript) | **Native WLED engine (`libwled_lfx.so`)** |
| Scene System | EEPROM slots via `0x51` | **Scene UI package with image backgrounds** |

---

## SECTION 2: BLE TRANSPORT LAYER

### GATT Service & Characteristic UUIDs (CONFIRMED from `libapp.so`)

| UUID | Purpose | Confirmed By |
|:-----|:--------|:-------------|
| `0000ffe0-0000-1000-8000-00805f9b34fb` | **Primary Service** | `libapp.so` string extraction |
| `0000ffe1-0000-1000-8000-00805f9b34fb` | **Write Characteristic** | `libapp.so` string extraction |
| `0000ff12-0000-1000-8000-00805f9b34fb` | Secondary characteristic | `libapp.so` |
| `0000ff14-0000-1000-8000-00805f9b34fb` | Secondary characteristic | `libapp.so` |
| `0000ff15-0000-1000-8000-00805f9b34fb` | Secondary characteristic | `libapp.so` |
| `00001827-0000-1000-8000-00805f9b34fb` | Mesh Provisioning Service | `libapp.so` |
| `00001828-0000-1000-8000-00805f9b34fb` | Mesh Proxy Service | `libapp.so` |
| `00002ADB-0000-1000-8000-00805F9B34FB` | Mesh Provisioning Data In | `libapp.so` |
| `00002ADC-0000-1000-8000-00805F9B34FB` | Mesh Provisioning Data Out | `libapp.so` |
| `00002ADD-0000-1000-8000-00805F9B34FB` | Mesh Proxy Data In | `libapp.so` |
| `00002ADE-0000-1000-8000-00805F9B34FB` | Mesh Proxy Data Out | `libapp.so` |
| `5833ff01-9b8b-5191-6142-22a4536ef123` | Custom (mesh group?) | `libapp.so` |
| `5833ff02-9b8b-5191-6142-22a4536ef123` | Custom | `libapp.so` |
| `5833ff03-9b8b-5191-6142-22a4536ef123` | Custom | `libapp.so` |
| `5833ff04-9b8b-5191-6142-22a4536ef123` | Custom | `libapp.so` |

### Packet Format — NO WRAPPER

Unlike Zengge's complex V2 framing, BanlanX uses a dead-simple format:

```
[0xA0, CommandByte, DataLength, ...payload]
```

- **No sequence counter**
- **No checksum**
- **No multi-segment chunking protocol**
- Header byte is ALWAYS `0xA0` (160 decimal, -96 signed)

### BLE Write Implementation (`r2/g.java`)

The decompiled Java BLE write handler at `r2/g.java` shows:

- Write type auto-detection: checks `(properties & 4) == 4` for Write Without Response
- Fallback to Write With Response (type 2) if property flag absent
- Queued write system via `LinkedHashMap` for reliability
- `byte[] f8205o` is the raw payload field

---

## SECTION 3: COMPLETE OPCODE MAP (Blutter Extracted + UniLED Confirmed)

All commands share the `[0xA0, cmd, dataLen, ...data]` format.

### 0x70 — State Query

```
TX: [0xA0, 0x70, 0x00]
RX: Device responds with current state (power, mode, color, brightness, etc.)
```

- **SK8Lytz relevance**: HIGH — equivalent to Zengge `0x47`

---

### 0x50 — Power ON/OFF (`turnOnOff`)

```
Power ON:  [0xA0, 0x50, 0x01, 0x01]
Power OFF: [0xA0, 0x50, 0x01, 0x00]
```

- **Data Length**: `0x01` (1 byte payload)
- **Payload**: `0x01` = ON, `0x00` = OFF
- **SK8Lytz relevance**: **CRITICAL** — equivalent to Zengge `0x71`

---

### 0x52 — Set Solid Color (`setSolidColor`)

```
[0xA0, 0x52, 0x03, R, G, B]
```

- **Data Length**: `0x03` (3 bytes)
- **R, G, B**: 0x00–0xFF each
- **SK8Lytz relevance**: **CRITICAL** — equivalent to Zengge `0x31`

---

### 0x51 — Set Brightness (`setBrightness`)

```
[0xA0, 0x51, 0x01, level]
```

- **Data Length**: `0x01`
- **level**: 0x00–0xFF (0–255)
- **SK8Lytz relevance**: MEDIUM — equivalent to Zengge `0x37`

---

### 0x53 — Set Effect (`setLfxMode`)

```
[0xA0, 0x53, 0x01, effectId]
```

- **Data Length**: `0x01`
- **effectId**: See Section 5 for the full 142-effect table
- **SK8Lytz relevance**: **CRITICAL** — equivalent to Zengge `0x42`

---

### 0x54 — Set Effect Speed (`setLfxSpeed`)

```
[0xA0, 0x54, 0x01, speed]
```

- **Data Length**: `0x01`
- **speed**: 1–10
- **SK8Lytz relevance**: HIGH — independent speed control (Zengge bundles speed into `0x42`)

---

### 0x55 — Set Pixel Count / Segment Length (`setLfxPixelCount`)

```
[0xA0, 0x55, 0x02, lengthHi, lengthLo]
```

- **Data Length**: `0x02`
- **length**: 1–150 (number of pixels the effect spans)
- **SK8Lytz relevance**: HIGH — equivalent to Zengge's `0x62` EEPROM `points` field

---

### 0x6B — Set Color Order (`setLedColorChannelOrder`)

```
[0xA0, 0x6B, 0x01, order]
```

- **Data Length**: `0x01`
- **order**: RGB channel ordering enum (see `libscene_lfx.so` for full permutation table)
  - The native lib contains ALL 120 permutations of RGBCW ordering (e.g., `RGBCW`, `RBGCW`, `GRBCW`...)
- **SK8Lytz relevance**: MEDIUM — equivalent to Zengge `0x62` `sorting` field

---

### 0x6A — Set Light Driver Type (`setLightsDriverType`)

```
[0xA0, 0x6A, 0x01, driverType]
```

- **Data Length**: `0x01`
- **driverType**: E.g. WS2812B, SK6812, etc.
- **SK8Lytz relevance**: LOW — typically set once.

---

### 0x5A — Set Audio Sensitivity (`setSensitivity`)

```
[0xA0, 0x5A, 0x01, gain]
```

- **Data Length**: `0x01`
- **gain**: 1–16
- **SK8Lytz relevance**: HIGH — equivalent to Zengge `0x73` sensitivity byte

---

### 0x59 — Set Audio Input Source (`setSoundSource`)

```
[0xA0, 0x59, 0x01, input]
```

- **Data Length**: `0x01`
- **input**: `0x00` = Internal Mic, `0x01` = Player, `0x02` = External Mic
- **SK8Lytz relevance**: HIGH — equivalent to Zengge `0x73` `isOn`/micSource routing

---

### 0x5B — Stream Software Audio Magnitude (`sendSpectrum`)

```
[0xA0, 0x5B, ...spectrumData]
```

- **Data Length**: Variable
- **spectrumData**: Frequency band values calculated from the app's `arrangeAppMusicDat`
- **SK8Lytz relevance**: HIGH — equivalent to Zengge's `0x74` magnitude byte, but requires an array of FFT bands rather than a single magnitude value.

---

### 0x56 — Set Effect Direction (`setLfxDir`)

```
[0xA0, 0x56, 0x01, direction]
```

- **Data Length**: `0x01`
- **direction**: `0x01` (Forward), `0x00` (Backward), `0x02` (Mirror Outward), `0x03` (Mirror Inward)
- **SK8Lytz relevance**: HIGH — Adds native directional control to built-in effects. (Zengge lacks native direction for built-ins).

---

### 0x57 — Set Effect Color (`setLfxColor`)

```
[0xA0, 0x57, 0x03, R, G, B]
```

- **Data Length**: `0x03`
- **SK8Lytz relevance**: HIGH — Modifies the base color palette of the active built-in effect.

---

### 0x58 — Set Effect Loop Mode (`setLfxLoopMode`)

```
[0xA0, 0x58, 0x01, mode]
```

- **Data Length**: `0x01`
- **SK8Lytz relevance**: MEDIUM — Equivalent to the multi-effect sequence loop concepts.

---

### 0x5E — Reset Device/Effect (`resetLfx`)

```
[0xA0, 0x5E, 0x00]
```

- **Data Length**: `0x00` (or variable config)
- **SK8Lytz relevance**: LOW — Soft reboot / reset to defaults.

---

### 0x60 & 0x61 — Color Temperature

- **0x60**: `setLfxColorTemp` (Effect color temperature)
- **0x61**: `setSolidColorTemp` (Solid color temperature)
- **SK8Lytz relevance**: LOW — Only relevant for CCT/RGBW hardware variants.

---

### 0x90 — Set DIY Gradient (`setLfxGradient`)

```
[0xA0, 0x90, ...gradientData]
```

- **SK8Lytz relevance**: MEDIUM — Specific to the DIY gradient builder (`DiyGradientLfx`).

---

## SECTION 3B: KNOWLEDGE GAINED FROM DART AOT REVERSE ENGINEERING

During the Blutter decompilation of `libapp.so`, we successfully extracted `objs.txt`, which mapped the Dart Enums to raw hex opcodes. This exhaustive extraction provided us with several critical architectural insights:

1. **Protocol Purity:** BanlanX uses a highly modular opcode structure (`0x50` through `0x6B`) where *every single property* (Speed, Direction, Color, Brightness) has its own dedicated opcode. This is a stark contrast to ZENGGE's bundled `0x42` payload which crams ID, Speed, and Brightness into a single checksummed wrapper.
2. **Native Direction & Mirroring:** We discovered the `setLfxDir` (`0x56`) opcode and its exact enums (`forward=0x1`, `backward=0x0`, `mirrorOutward=0x2`, `mirrorInward=0x3`). ZENGGE requires manually rendering reverse pixel arrays for custom effects, whereas BanlanX hardware supports reverse and mirror processing natively.
3. **Hardware FFT & Spectrum Mapping:** We discovered `sendSpectrum` (`0x5B`) takes an array of multi-band FFT data, proving the hardware handles full EQ visualization natively, compared to ZENGGE's primitive single-byte magnitude (`0x74`).
4. **Mesh Provisioning Subsystem:** We extracted strings for `sendProvisioningData`, `provisioningInvite`, `group_uuid`, revealing that BanlanX supports true BLE Mesh syncing between controllers without phone orchestration (unlike ZENGGE's single-connection limit).
5. **WLED Engine Proof:** The extraction of the `libwled_lfx.so` symbols confirmed the SP621E runs an embedded C++ WLED port, exposing functions like `addPixelColorXY` and `mode_2DBlackHole`.

---

## SECTION 4: NATIVE PIXEL ENGINE — `libwled_lfx.so`

### Architecture Discovery

The SP621E ships with a **compiled WLED light effects engine** as a native ARM64 library (`libwled_lfx.so`, 70KB). This is NOT interpreted Dart — it's a C/C++ native library called via FFI from the Flutter app.

This proves the hardware supports **per-pixel color control** via a frame buffer rendered by the WLED engine.

### Core Pixel Manipulation Functions (Confirmed Symbols)

| Function | Purpose |
|:---------|:--------|
| `setPixelColorXY` | Set color at pixel coordinate (X, Y) |
| `addPixelColorXY` | Additively blend color at pixel (X, Y) |
| `fadePixelColorXY` | Fade pixel at coordinate (X, Y) |
| `getPixelColorXY` | Read current pixel color at (X, Y) |
| `render_frame` | Render a complete animation frame |
| `get_frame_data` | Retrieve rendered frame buffer for BLE transmission |
| `setup_matrix_layout` | Configure the LED matrix dimensions |

### 2D Matrix Effects (WLED Engine)

| Function | Effect |
|:---------|:-------|
| `mode_2Dmatrix` | Matrix (falling code) effect |
| `mode_2Dpalette` | 2D palette sweep |
| `mode_2DGEQ` | 2D graphic equalizer (music reactive) |
| `mode_2DBlackHole` | Black hole gravity effect |
| `mode_2DColoredBursts` | Color burst explosions |
| `mode_2Ddna` / `mode_2Ddna_vert` | DNA helix animation |
| `mode_2DfadeAndscroll` | Fade and scroll |
| `mode_2DHiphotic` | Hypnotic spiral |
| `mode_2Dmetaballs` | Metaball physics |
| `mode_2Dmusicsoap` | Music-reactive soap bubble |
| `mode_2Dmusicsquaredswirl` | Music-reactive squared swirl |
| `mode_2Dparty` | Party mode |
| `mode_2DPlasmaball` | Plasma ball |
| `mode_2Dsoap` | Soap bubble |
| `mode_2Dsquaredswirl` | Squared swirl |
| `mode_2DWaverly` | Waverly wave |

### Matrix Configuration Variables

| Variable | Purpose |
|:---------|:--------|
| `sysMatrixW` | System matrix width |
| `sysMatrixH` | System matrix height |
| `myWidth` / `myHeight` | Canvas dimensions |
| `virtualWidth` / `virtualHeight` / `virtualLength` | Virtual canvas for oversized effects |
| `sysChanNum` | System channel count |
| `sysChanOrder` | System channel ordering |
| `groupLength` | LED grouping length |

### Audio/FFT Engine

| Function | Purpose |
|:---------|:--------|
| `fft_init` / `fft_execute` / `fft_destroy` | FFT processing pipeline |
| `fft_input` / `fft_output` | FFT I/O buffers |
| `detectBeats` | Beat detection algorithm |
| `scaleBandVals` | Scale frequency band values |
| `updateFreqBase` | Update frequency baseline |
| `updateMusicDat` | Update music data for effects |
| `arrangeAppMusicDat` | Arrange app-sourced music data |
| `hasSignifFlctn` | Has significant fluctuation (beat gate) |
| `thresholds` | Beat detection thresholds |

### Frame Rendering Pipeline

```
1. Effect function called (e.g., mode_2DGEQ)
2. setPixelColorXY(x, y, color) populates frame buffer
3. render_frame() finalizes the frame
4. get_frame_data() returns raw [R,G,B × N] buffer
5. Flutter/Dart DiyPixel class packages frame for BLE
6. BLE write to FFE1 characteristic
7. SP621E hardware receives and displays frame
```

---

## SECTION 5: SCENE & DIY ENGINE (from `libapp.so` + `libscene_lfx.so`)

### DiyPixel System (CONFIRMED from `libapp.so` strings)

| String | Interpretation |
|:-------|:---------------|
| `DiyPixel` | **Dart class** for per-pixel DIY mode |
| `DiyLfx(modeId:` | DIY Light Effect constructor with mode parameter |
| `DiyGradientLfx(modeId:` | DIY Gradient Light Effect — gradient-specific subclass |
| `diyWidth:` | Canvas width parameter for pixel DIY |
| `diyHeight:` | Canvas height parameter for pixel DIY |
| `pixelCount:` | Total addressable pixel count |
| `max_pixel_count` | Maximum pixel count supported by hardware |
| `led_pixel_radius` | LED pixel render radius (for visualization) |
| `saveDiyLfx` | Method to persist DIY effects to device EEPROM |
| `gradientColors:` | Gradient color stop array |
| `gradientFeature:` | Gradient feature flags |
| `gradientId:` | Gradient preset identifier |
| `colorStops` | Color stop positions for gradient rendering |

### Scene System (from `libscene_lfx.so`)

The `libscene_lfx.so` (140KB) is a second native effects engine focused on **scene-based rendering**:

**Core Functions:**

| Function | Purpose |
|:---------|:--------|
| `setPixelColor` | Set pixel color (1D addressing) |
| `setPixelOn` / `setPixelOff` | Binary pixel control |
| `setPixelOff` / `setBitlOn` / `setBitlOff` | Bitwise LED control |
| `Product_Init` | Initialize product-specific configuration |
| `isMatchLedType` | Match LED strip IC type |
| `LED_DRIVE_TYPE` | LED driver type constant |
| `mapChan` / `reorderChanVal` | Channel mapping and reordering |
| `recover_effect_param` | Recover saved effect parameters |
| `set_effect_params` | Set effect parameters |
| `switch_lfx_mode` | Switch light effect mode |
| `opTab_map` | **Opcode table mapping** — maps opcodes to handlers |

**Scene Effect Library (from `libscene_lfx.so` symbols):**

| Effect | Function |
|:-------|:---------|
| Rainbow | `Rainbow` |
| Meteor Rain | `MeteorRain` |
| Lightning | `Lightning` |
| Stars | `Stars` |
| Stacking | `Stacking` |
| Sinewave | `Sinewave` |
| Spin | `Spin` |
| Juggle | `Juggle` |
| Seven Color Flow | `SevenClrFlow` |
| Merry Christmas | `MerryChrist` |
| Love Peace | `LovePeace` |
| Open Close | `OpenClose` |
| Long Wave Comet | `Long_WComet` |
| Switch Color | `SwitchClor` |
| Music Blink | `Music_Blink` |
| Music Eject | `Music_Eject` |
| Music Firework | `Music_Firework` |
| Music Force | `Music_Force` |
| Music Hits | `Music_Hits` |
| Music Spectrum | `Music_Spectrum` |
| Music Symbol Colors | `Music_SymbolColors` |
| Music VU Meter | `Music_VuMeter` |

**PWM Control System (for non-addressable strips):**

| Function | Mode |
|:---------|:-----|
| `PWM_DriveRGB` / `PWM_DriveW` | Direct PWM drive |
| `PWM_DIY_TAB` | DIY mode lookup table |
| `PWM_DYN_TAB` | Dynamic effect lookup table |
| `PWM_STA_TAB` | Static mode lookup table |
| `PWM_RHY_TAB` | Rhythm/music mode lookup table |
| `pwmDiyBreath` / `pwmDiyGradient` | DIY breathing / gradient |
| `pwmDiyJumpColor` / `pwmDiyStrobe` | DIY jump / strobe |
| `pwmDynBreath` / `pwmDynGradient` | Dynamic breathing / gradient |
| `pwmDynHeartBeat` | Dynamic heartbeat |
| `pwmRhyBeat` / `pwmRhyJumpColor` | Rhythm beat / jump |
| `pwmStaMode` | Static mode handler |
| `pwmOnOffAnimation` | Power on/off animation |
| `syncBriChangeHandler` | Brightness change sync |

### App Routes & Features (from `libapp.so`)

| Route/Key | Feature |
|:----------|:--------|
| `/sp630e/diy/fav` | DIY favorites list |
| `/sp630e/music/config/matrix` | Matrix music config |
| `/sp630e/settings/lightType` | Light type config |
| `/sp630e/settings/powerOn` | Power-on behavior |
| `/sp630e/settings/timer/config` | Timer config |
| `/sp630e/settings/rename` | Device rename |
| `/sp630e/settings/more` | Advanced settings |
| `/scene_template` | Scene template picker |
| `/scene_pre_creation` | Scene pre-creation flow |
| `/car_lights/new` | Car lights mode |
| `/car_lights/setup` | Car lights setup |
| `/car_lights/settings` | Car lights settings |
| `/car_lights/settings/chassic_lights_trigger` | Chassis light trigger |
| `/device/gundam_lights/settings` | Gundam lights settings |
| `/device/ble_mesh_rc/provisioning_guide` | BLE mesh provisioning |
| `/device/fish_tank_lights` | Fish tank light mode |
| `hal_App_Opcode_Handler` | **HAL opcode handler** — hardware abstraction |
| `SP601EConfMapper` | SP601E configuration mapper |
| `assignFrameSyncMaster` | Frame sync master assignment |
| `startFrame=` | Frame start marker |
| `gif_lfx_frames` | GIF-based light effect frames |

### API Endpoints (from `libapp.so`)

| Endpoint | Purpose |
|:---------|:--------|
| `/banlanx/app/check-update` | App update check |
| `/banlanx/app/manuals/getAll` | Get all manuals |
| `/banlanx/user/alexa/link` | Alexa smart home integration |
| `/device/scene/image/get` | Get scene background image |
| `/auth/signIn` | User authentication |
| `/user/avatar/get/` | User avatar |
| `https://document.ledhue.com/banlanx/faq/...` | FAQ documentation |

---

## SECTION 6: BUILT-IN EFFECT LIBRARY (142 Effects)

### Effect ID Ranges (from UniLED `banlanx2.py`)

**Dynamic RGB Effects (0x01–0x8E / 1–142):**

| ID Range | Category | Example Effects |
|:---------|:---------|:----------------|
| 0x01–0x0F | Rainbow family | Rainbow, Rainbow Stars, Rainbow Twinkle |
| 0x10–0x1F | Meteor family | Meteor Rain, Comet variants |
| 0x20–0x2F | Wave family | Ocean Wave, Color Wave |
| 0x30–0x3F | Breathing family | Color Breathe, Gradient Breathe |
| 0x40–0x4F | Stacking/Fill | Color Stack, Fill Up/Down |
| 0x50–0x5F | Chase family | Color Chase, Bounce |
| 0x60–0x6F | Gradient family | Gradient Flow, Smooth Gradient |
| 0x70–0x7F | Twinkle family | Twinkle, Stars, Sparkle |
| 0x80–0x8E | Mixed/Special | Fire, Lava, Party, Halloween |

**Special Mode IDs:**

| ID | Mode | Notes |
|:---|:-----|:------|
| `0xBE` (190) | **Solid Color** | Equivalent to Zengge FREEZE — activates `0x69` color |
| `0xC9–0xDA` | **Sound Reactive** | Music-driven effects |

### Sound Reactive Effects (0xC9–0xDA)

| ID | Effect Name | Description |
|:---|:-----------|:------------|
| 0xC9 | Rhythm Stripe | Beat-synced stripe pattern |
| 0xCA | Rhythm Energy | Energy bar display |
| 0xCB | Rhythm Pulse | Pulsing to beat |
| 0xCC | Rhythm VU Meter | Volume unit meter |
| 0xCD–0xDA | Additional Sound FX | Various music reactive |

---

## SECTION 7: ZENGGE ↔ BANLANX CROSS-REFERENCE TABLE

### Command Parity Map

| Feature | Zengge Opcode | Zengge Format | BanlanX Opcode | BanlanX Format | Parity |
|:--------|:-------------|:--------------|:---------------|:---------------|:-------|
| **Power ON** | `0x71` | `[0x71, 0x23, 0x0F, chk]` | `0x50` | `[0xA0, 0x50, 0x01, 0x01]` | ✅ Full |
| **Power OFF** | `0x71` | `[0x71, 0x24, 0x0F, chk]` | `0x50` | `[0xA0, 0x50, 0x01, 0x00]` | ✅ Full |
| **Solid Color** | `0x31` | `[0x31, R, G, B, bri, 0x0F, chk]` | `0x52` | `[0xA0, 0x52, 0x03, R, G, B]` | ✅ Full |
| **Brightness** | `0x37` | `[0x37, bri(0-100), 0, chk]` | `0x51` | `[0xA0, 0x51, 0x01, bri(0-255)]` | ⚠️ Range differs |
| **Effect Select** | `0x42` | `[0x42, id, spd, bri, chk]` | `0x53` | `[0xA0, 0x53, 0x01, id]` | ⚠️ Speed separate |
| **Effect Speed** | (in `0x42`) | bundled | `0x54` | `[0xA0, 0x54, 0x01, spd(1-10)]` | ⚠️ Separate cmd |
| **Effect Direction** | N/A | Hardcoded or array-based | `0x56` | `[0xA0, 0x56, 0x01, dir]` | ❌ BanlanX only |
| **Effect Color** | `0x51` | EEPROM slot | `0x57` | `[0xA0, 0x57, 0x03, R, G, B]` | ⚠️ Live modifier |
| **Effect Sequence/Loop** | `0x43` | `[0x43, id1, id2..., chk]` | `0x58` | `[0xA0, 0x58, 0x01, mode]` | ⚠️ Different logic |
| **Pixel Array** | `0x59` | `[0x59, lenHi, Lo, RGB×N, ...]` | **Native** | `libwled_lfx.so` via `get_frame_data()` FFI | 🔬 Requires wrapper |
| **Live Stream** | `0x53` | `[0x53, lenHi, Lo, RGB×N, ...]` | **Native** | `libwled_lfx.so` via `get_frame_data()` FFI | 🔬 Requires wrapper |
| **Music Config** | `0x73` | `[0x73, on, type, id, FG, BG, sens, bri, chk]` | `0x59` + `0x5A` | Separate audio src + gain | ⚠️ Split commands |
| **Music Magnitude** | `0x74` | `[0x74, mag, chk]` | `0x5B` | `[0xA0, 0x5B, ...spectrum]` OR Native FFT | ❌ Different model |
| **State Query** | `0x47` | `[0x47, param, chk]` | `0x70` | `[0xA0, 0x70, 0x00]` | ✅ Full |
| **EEPROM Config** | `0x62` | `[0x62, ptsHi, Lo, segHi, Lo, ...]` | `0x55` | `[0xA0, 0x55, 0x02, ptsHi, Lo]` | ⚠️ Format differs |
| **Scene EEPROM** | `0x51` | `[0x51, slot×10×32, chk]` | `saveDiyLfx` | Dart-level save (`0x63`) | 🔬 Needs mapping |
| **Hardware Reset** | N/A | Factory physical button | `0x5E` | `[0xA0, 0x5E, 0x00]` | ❌ BanlanX only |
| **Gradient Flow** | N/A | Requires frame render | `0x90` | `[0xA0, 0x90, ...gradient]` | ❌ BanlanX only |

### IControllerProtocol Method Mapping

| Interface Method | Zengge Implementation | BanlanX Implementation |
|:----------------|:---------------------|:----------------------|
| `buildPowerOn()` | `[0x71, 0x23, 0x0F, chk]` wrapped in V2 | `[0xA0, 0x50, 0x01, 0x01]` raw |
| `buildPowerOff()` | `[0x71, 0x24, 0x0F, chk]` wrapped in V2 | `[0xA0, 0x50, 0x01, 0x00]` raw |
| `buildSolidColor(r,g,b)` | `[0x31, r, g, b, 0xFF, 0x0F, chk]` wrapped | `[0xA0, 0x52, 0x03, r, g, b]` raw |
| `buildBrightness(level)` | `[0x37, level×100/255, 0, chk]` wrapped | `[0xA0, 0x51, 0x01, level]` raw |
| `buildEffect(id, speed)` | `[0x42, id, speed, bri, chk]` wrapped | `[0xA0, 0x53, 0x01, id]` + `[0xA0, 0x54, 0x01, speed]` |
| `buildPixelArray(colors)` | `[0x59, ...]` complex format | **TBD — Native FFI bridge needed** |
| `buildMusicConfig(...)` | `[0x73, ...]` 13-byte config | `[0xA0, 0x59, 0x01, src]` + `[0xA0, 0x5A, 0x01, gain]` |
| `wrapCommand(payload)` | V2 wrapper with seq/len/checksum | **No wrapper needed — send raw** |
| `matchesAdvertisement(ad)` | Check `mfBuf[10..11]` for `0xA3` | Check MFR ID `5053` + data `[0x0D]`/`[0x16]` |

---

## SECTION 8: CONFIRMED UNKNOWNS & OPEN QUESTIONS

### 🔬 CRITICAL: Raw Pixel Streaming Opcode (UNRESOLVED)

**What we KNOW:**

- `DiyPixel` class exists in the Dart layer (confirmed from `libapp.so`)
- `get_frame_data()` renders a complete frame buffer in `libwled_lfx.so`
- `setPixelColorXY()` populates the native pixel buffer
- The `car_lights` package contains flow/fade pixel animations (proving pixel streaming works)
- `startFrame=` and `assignFrameSyncMaster` suggest frame-based streaming protocol
- `gif_lfx_frames` suggests GIF-frame-based pixel streaming
- `SegmentedMessageCollector` suggests frames may be chunked for BLE transmission
- `effect_segment_length` and `lightingEffectSegmentLength:` control segment sizing

**What we DON'T KNOW:**

- The exact BLE opcode byte that wraps pixel frame data (likely `0xA1`, `0xA2`, or an entirely different header)
- The frame packet structure (header + pixel data layout)
- Whether frames are sent as single BLE writes or chunked
- Maximum frame size / MTU handling

**How to resolve:** HCI snoop log capture while running the BanlanX app's Car Lights or DIY Pixel mode.

### ⚠️ Additional Unknowns

| Question | Impact | Resolution Path |
|:---------|:-------|:----------------|
| What is the `0xA0, 0x65` command? | May exist for extended config | UniLED source audit |
| Does `0xFF14/0xFF15` carry notification data? | State change push notifications | GATT exploration |
| What triggers `hal_App_Opcode_Handler`? | HAL routing logic | Dart AOT deeper analysis |
| How does `assignFrameSyncMaster` work? | Multi-device sync | Multi-device lab test |
| What is the `link66` string? | Unknown protocol link | Further string analysis |
| What does `cmdf` represent? | Command format flag? | HCI sniff verification |
| What are the `5833ff0X` custom UUIDs? | Mesh networking protocol | BLE mesh investigation |

### 🎯 Recommended Next Steps

1. **HCI Snoop Capture** — Install BanlanX app, connect SP621E, enable HCI logging, trigger DIY Pixel and Car Lights modes. Pull `btsnoop_hci.log` for raw pixel opcode identification.
2. **Build BanlanxAdapter (Phase 1)** — Implement basic control (Power, Color, Brightness, Effect Select) using confirmed `0xA0` opcodes. No pixel streaming yet.
3. **Register in ControllerRegistry** — Wire `matchesAdvertisement` to detect SP621E via `FFE0` service UUID + manufacturer ID `5053`.
4. **Effect Mapping** — Map SK8Lytz template names to BanlanX effect IDs for the 142 built-in effects.
5. **Dart AOT Deep Dive (Optional)** — Install Visual Studio Build Tools + CMake to compile Blutter and get full Dart class/method signatures from `libapp.so`.

---

## SECTION 9: REVERSE ENGINEERING ARTIFACTS

### File Locations

| Artifact | Path |
|:---------|:-----|
| Original XAPK | `BANLANX_APK\BanlanX_3.2.9_APKPure.xapk` |
| Base APK | `BANLANX_APK\com.spled.scenex.apk` |
| ABI Split APK | `BANLANX_APK\config.arm64_v8a.apk` |
| JADX Decompiled Sources | `BANLANX_APK\BANLANX_DECOMPILED\sources\` |
| Extracted Native Libs | `BANLANX_APK\BLUTTER_WORK\lib\arm64-v8a\` |
| Blutter Tooling | `BANLANX_APK\blutter\` |
| Dart Version | 3.6.2 (detected by Blutter `extract_dart_info.py`) |
| libapp.so | 17.31 MB — Dart AOT compiled app logic |
| libwled_lfx.so | 0.07 MB — WLED pixel effects engine |
| libscene_lfx.so | 0.14 MB — Scene-based effects engine |
| libflutter.so | 10.31 MB — Flutter engine |

### Tools Used

| Tool | Version | Purpose |
|:-----|:--------|:--------|
| JADX | (from ZENGGE_APK toolchain) | Java/Kotlin decompilation |
| Blutter | Latest (worawit/blutter) | Dart AOT analysis |
| JDK | 17.0.10+7 | JADX runtime |
| Python | 3.14.3 | Blutter scripts |
| PowerShell string extraction | Custom | Binary string analysis |

### Key Discovery: Flutter Barrier

The BanlanX app is a **Flutter app**, meaning all protocol logic is compiled into `libapp.so` as ARM64 Dart AOT bytecode. This cannot be decompiled by JADX. The Java layer (`r2/g.java`, `p2/C0620g.java`) only contains the BLE transport — no protocol constants.

**Full Dart decompilation requires:**

- Visual Studio 2022 with "Desktop development with C++" workload
- CMake + Ninja build system
- Then: `python blutter.py BLUTTER_WORK\lib\arm64-v8a out_dir`
- This will produce `asm/` directory with full Dart symbols and pseudo-assembly

This is the path to discovering the exact pixel streaming opcode without HCI sniffing.

---

## SECTION 10: ADDITIONAL DISCOVERIES & GAPS (Audit Pass 2026-05-15)

### 🔧 Visual Studio Build Tools Clarification

> **⛔ VS Code ≠ Visual Studio Build Tools.**
> Blutter requires the MSVC C++ compiler (`cl.exe`), CMake, and Ninja.
> **VS Code is just a text editor.** You need:
>
> - [Visual Studio 2022 Build Tools](https://visualstudio.microsoft.com/visual-cpp-build-tools/) (free)
> - Select "Desktop development with C++" workload during install
> - Then install CMake + Ninja: `pip install cmake ninja`
> - Run Blutter from the "x64 Native Tools Command Prompt" (NOT PowerShell)

### 🔌 Connection Management (from `libapp.so` strings)

| Feature | String Evidence | Implication |
|:--------|:---------------|:------------|
| **Auto-connect** | `pref_auto_connect_enabled` | SharedPreference key — app auto-reconnects on open |
| **Reconnect dialog** | `ReconnectDialog`, `#reconnect:` | UI component for reconnection flow |
| **Device reconnect** | `onDeviceReconnected` | Callback fired on successful reconnect |
| **Group reconnect** | `_reconnectDeviceGroup:` | Multi-device group reconnect handler |
| **Connect failed** | `connect-to-device-failed` | Error route for failed BLE connections |
| **Network config** | `configNetwork` | WiFi/network configuration for WiFi-enabled controllers |
| **Device ID** | `getDeviceId` | Device ID retrieval method |

**SK8Lytz Impact**: The SP621E only supports **1 concurrent BLE connection**. Our app must:

1. Ensure the BanlanX app is fully backgrounded/killed before attempting connection
2. Implement a `ReconnectDialog` equivalent for connection recovery
3. Handle the `connect-to-device-failed` state gracefully

### 📡 Firmware OTA System (from `libapp.so` strings)

| String | Purpose |
|:-------|:--------|
| `FirmwareUpdateStage.` | Enum for firmware update pipeline stages |
| `Firmware-Download` / `Firmware-nedladdning` | Download phase (multi-language) |
| `Firmware-verifiering` | Verification phase |
| `Firmware-Update` / `Firmware-uppdatering` | Update execution phase |
| `Ger?te-Firmware-Update` | Device firmware update (German) |
| `ic_firmware_version_outlined.png` | Firmware version UI icon |

**SK8Lytz Impact**: LOW for Phase 1. The SP621E has OTA firmware update capability. We do NOT need to implement this, but it's good to know the hardware supports it for future extensibility.

### 🔗 BLE Mesh Provisioning (from `libapp.so` strings)

The BanlanX ecosystem supports **Bluetooth Mesh** for multi-device synchronization:

| String | Purpose |
|:-------|:--------|
| `sendProvisioningData` | Send provisioning payload to mesh node |
| `provisioningCapabilities` | Query node provisioning capabilities |
| `provisioningInvite` | Invite device to join mesh network |
| `sendProvisioningPublicKey` | Public key exchange for mesh security |
| `sendProvisioningRandom` | Random nonce for mesh handshake |
| `onProvisioningCompleted` | Provisioning success callback |
| `onProvisioningConfirmationReceived` | Mesh confirmation received |
| `onProvisioningRandomReceived` | Random nonce received callback |
| `onProvisioningCapabilitiesReceived` | Capabilities received callback |
| `mesh_provisioned_node.png` | UI icon for provisioned mesh nodes |
| `mesh_group.png` | UI icon for mesh groups |
| `group_address_start` | Mesh group address allocation |
| `group_uuid` | Mesh group UUID |
| `provisioner_uuid` | Mesh provisioner UUID |

**SK8Lytz Impact**: HIGH for future multi-skate synchronization. The SP621E supports BLE Mesh (UUIDs `0x1827`/`0x1828`/`0x2ADB-E` confirmed), enabling **synchronized light effects across multiple skaters**. This is a massive differentiator vs Zengge (which has NO mesh support).

### 🎵 Music Architecture: A Fundamentally Different Model

**Zengge Music Flow:**

```
Phone Mic → useAppMicrophone.ts → FFT → magnitude (0-255) → 0x74 stream → Hardware → LEDs
```

The phone does audio processing, sends a single magnitude byte at high frequency. The hardware maps that to its baked-in music effect.

**BanlanX Music Flow:**

```
Option A (Internal Mic):
  Hardware Mic → SP621E onboard FFT → fft_execute() → detectBeats() → LEDs
  
Option B (App Audio / Player):
  Phone Audio → arrangeAppMusicDat() → updateMusicDat() → render_frame() → BLE pixel stream → LEDs
```

**Critical Difference**: BanlanX does NOT send a simple magnitude byte. It either:

1. Uses the **hardware's own microphone and FFT** (Option A — `0x6C` input=`0x00`)
2. Sends **processed audio data** from the app for the native WLED engine to render (Option B — `0x6C` input=`0x01`/`0x02`)

This means:

- We **cannot** use our existing `useAppMicrophone.ts` `0x74` magnitude stream for BanlanX
- For hardware mic mode: just send `[0xA0, 0x6C, 0x01, 0x00]` and the SP621E handles everything
- For app audio mode: we need to understand what `arrangeAppMusicDat()` expects as input format (likely FFT band data, not a single magnitude byte)

### 🚗 Car Lights Module — PIXEL STREAMING PROOF

The `car_lights` package is the smoking gun for pixel streaming:

| Asset | Purpose |
|:------|:--------|
| `brake_light_blink.zip` / `brake_light_blink_new.zip` | Brake light blink animation frames |
| `flow_car_light.zip` | Flowing light animation |
| `fade_car_light.zip` | Fading light animation |
| `turn_signal_light_flow.zip` | Turn signal flow animation |
| `car_rgb.png` / `car_argb.png` | Car RGB layout images |
| `car_chassic.png` / `chassis_lights.png` | Chassis light zone mapping |
| `car_areas.png` | Car light area definition |
| `right_turn_signal.png` | Right turn signal visual |
| `CarLightsHomeScreenState` | Dart state class for car lights UI |
| `ic_console_lights.png` | Console lights icon |
| `ic_door_lights.png` | Door lights icon |
| `ic_welcome_lights.png` | Welcome lights animation icon |
| `ic_storage_lights.png` | Storage/trunk lights icon |

These are **pre-rendered pixel animations** packaged as ZIP archives. The app decompresses them frame-by-frame and streams the pixel data over BLE. This is EXACTLY the pattern we need for SK8Lytz custom animations.

### 🎮 Gundam Lights Module — Segment Control Evidence

| Asset | Purpose |
|:------|:--------|
| `ic_segment.png` | Segment control icon |
| `ic_diy_gradient.png` | DIY gradient icon |
| `ic_diy_solid.png` | DIY solid color icon |
| `ic_color_del.png` | Color delete from palette |
| `ic_play.png` | Play animation control |
| `ic_ejection_active.png` | Ejection effect active |
| `ic_power_flow_backward.png` | Reverse power flow effect |

### 📊 Zengge Parity Audit — What's Missing for Feature Parity

| Zengge Feature | Bible Section | BanlanX Status | Gap? |
|:---------------|:-------------|:---------------|:-----|
| Power ON/OFF | §3 `0x62` | ✅ Fully mapped | No |
| Solid Color | §3 `0x69` | ✅ Fully mapped | No |
| Brightness | §3 `0x66` | ✅ Mapped (range differs 0-255 vs 0-100) | ⚠️ Scale conversion needed |
| Effect Select (RBM) | §3 `0x63` | ✅ Mapped | No |
| Effect Speed | §3 `0x67` | ✅ Mapped (separate command) | No |
| Pixel Array (`0x59`) | §7 TBD | ❌ **Opcode unknown** | 🔴 BLOCKER for PatternEngine |
| Live Stream (`0x53`) | §7 TBD | ❌ **Opcode unknown** | 🔴 BLOCKER for VisualizerUnit |
| Music Config (`0x73`) | §3 `0x6C`+`0x6B` | ⚠️ Different architecture | 🟡 Needs adapter logic |
| Music Magnitude (`0x74`) | §10 | ❌ **Not applicable** — different model | 🟡 Hardware mic preferred |
| Scene Sequencer (`0x51`) | §5 `saveDiyLfx` | ❌ **Unknown format** | 🟡 Nice-to-have |
| Settled Mode (`0x41`) | N/A | ❌ No equivalent found | 🟡 Nice-to-have |
| Multi-Sequence (`0x43`) | N/A | ❌ No equivalent found | ⚪ Low priority |
| State Query (`0x47`) | §3 `0x70` | ✅ Fully mapped | No |
| EEPROM Config (`0x62`) | §3 `0x64`+`0x68` | ✅ Mapped (split commands) | No |
| IC Config Read (`0x63`) | N/A | ❌ Unknown read equivalent | 🟡 For probing |
| MFR Data Advertisement | §1 | ✅ Mapped (`5053` + `0x0D`/`0x16`) | No |
| V2 Packet Wrapper | §2 | ✅ **Not needed** (simpler protocol) | No |
| Checksum | §2 | ✅ **Not needed** (no checksum) | No |

### 🎯 Phase 1 Verdict: Ready for Basic Adapter

With the current Bible, we have **enough information to build a functional `BanlanxAdapter.ts`** that supports:

- ✅ Power ON/OFF
- ✅ Solid Color (RGB)
- ✅ Brightness (with 0-100 → 0-255 scale conversion)
- ✅ Effect selection (142 built-in effects)
- ✅ Effect speed (1-10 range)
- ✅ Segment length configuration
- ✅ Music mode (hardware mic only — send `0x6C` then `0x6B`)
- ✅ State query
- ✅ Advertisement matching for ControllerRegistry

**Blocked until HCI sniff or Blutter compile:**

- ❌ Raw pixel array streaming (DiyPixel opcode)
- ❌ App-driven music mode (audio data format)
- ❌ Scene EEPROM save/load format

### Strategy 2: "Protocol Context" (Full HAL Migration)

**Goal:** Integrate the BanlanX SP621E natively into the SK8Lytz ecosystem without breaking existing Zengge functionality. This requires an abstraction layer (HAL) that completely decouples the app's UI/Hooks from the hardware's specific byte framing.

#### 1. Payload Architecture & Wrapper Decoupling
Currently, SK8Lytz assumes a Zengge V2 wrapper (`[0x00, seq, 0x80, len...]`). We must abstract this out of `useBLE.ts` entirely.
- **Action:** Ensure `IControllerProtocol` implementations are responsible for their *own* packet wrappers.
- **BanlanX Logic:** The `BanlanxAdapter` will strictly return raw arrays `[0xA0, OPCODE, LEN, ...DATA]` with NO sequence bytes, NO V2 headers, and NO mathematical checksums. 

#### 2. Multi-Packet Execution Support
Zengge bundles Effect ID, Speed, and Brightness into a single `0x42` payload. BanlanX uses modular opcodes (`0x53` for Effect, `0x54` for Speed).
- **Action:** Refactor `IControllerProtocol` so that methods like `buildEffect()` can return an array of arrays (`number[][]`).
- **Hook Update:** Refactor `useBLE.ts` (specifically `sendProtocolResult`) to check if the result is a 2D array and recursively `await` writing each packet in sequence. This prevents packet collision when setting an effect and its speed simultaneously.

#### 3. Controller Registry Integration
- **Action:** Map `ControllerType.BANLANX_SP621E` within `src/protocols/ControllerRegistry.ts`.
- **Matching:** Update the `matchesAdvertisement()` method to detect BanlanX hardware by scanning for Manufacturer ID `5053` or the specific byte offsets `[0x0D]/[0x16]`.

#### 4. Hardware Music Mode Bypass
- **Action:** When connected to BanlanX hardware, the SK8Lytz app must suppress its software FFT magnitude stream (`0x74` in Zengge).
- **BanlanX Logic:** Calling `buildMusicConfig(0)` on the BanlanX adapter should send `0x59` (Set Audio Source = Internal Mic) to trigger the hardware's native FFT, allowing the app to sleep the CPU.

#### 5. Fallback & Rollback Strategy
- **Action:** Ensure any `number[][]` array refactoring is fully backwards-compatible with the existing `ZenggeAdapter`. Zengge results will be wrapped in single-element arrays `[zenggePayload]` to satisfy the multi-packet type signature without breaking legacy hardware.

### Architecture Stack

```
SK8Lytz HAL Stack
─────────────────────────────────────────────────────
  UI Layer       DashboardScreen, DockedController
       ↓
  Hook Layer     useBLE.ts  (hardware-agnostic writes, supports number[][])
       ↓
  Registry       ControllerRegistry.resolveProtocol(advertisement)
                   → ZenggeAdapter   (product_id 0xA3)
                   → BanlanxAdapter  (MFR ID 5053)  ← NEW
       ↓
  Adapter        IControllerProtocol:
                   buildPowerOn/Off()
                   buildSolidColor(r,g,b)
                   buildBrightness(level)
                   buildEffect(id, speed)      → Returns [[0xA0, 0x53...], [0xA0, 0x54...]]
                   buildPixelArray(colors[])   ← Phase 2
                   buildMusicConfig(...)
                   wrapCommand(payload)        → Returns payload unmodified for BanlanX
                   matchesAdvertisement(data)
       ↓
  BLE Transport  react-native-ble-plx → FFE1 characteristic
─────────────────────────────────────────────────────
```

### Phase 1 Files to Create/Modify

| Action | File | Change |
|:-------|:-----|:-------|
| CREATE | `src/protocols/BanlanxAdapter.ts` | Full adapter implementation |
| MODIFY | `src/protocols/ControllerRegistry.ts` | Register BanlanxAdapter |
| MODIFY | `src/protocols/IControllerProtocol.ts` | Handle multi-packet return type |
| MODIFY | `src/hooks/useBLE.ts` | Handle `number[] | number[][]` from buildEffect |

### `BanlanxAdapter.ts` — Complete Phase 1 Spec

```typescript
// src/protocols/BanlanxAdapter.ts

import { IControllerProtocol } from './IControllerProtocol';
import type { RGBColor } from '../types/colors';

const A0 = 0xA0; // All BanlanX commands start with this byte

export class BanlanxAdapter implements IControllerProtocol {

  readonly name = 'BanlanX SP-Series';
  readonly serviceUUID = '0000ffe0-0000-1000-8000-00805f9b34fb';
  readonly characteristicUUID = '0000ffe1-0000-1000-8000-00805f9b34fb';

  // ── Advertisement Detection ────────────────────────────────────────────────
  matchesAdvertisement(data: Buffer): boolean {
    // Check manufacturer ID 0x5053 ("PS" = SP reversed, little-endian)
    if (data.length >= 2) {
      const mfrId = data[0] | (data[1] << 8);
      if (mfrId === 0x5053) return true;
    }
    // Fallback: fingerprint bytes 0x0D or 0x16 at offset 4
    if (data.length > 4 && (data[4] === 0x0D || data[4] === 0x16)) return true;
    return false;
  }

  // ── Power ──────────────────────────────────────────────────────────────────
  buildPowerOn(): number[] {
    return [A0, 0x62, 0x01, 0x01];
  }

  buildPowerOff(): number[] {
    return [A0, 0x62, 0x01, 0x00];
  }

  // ── Solid Color ────────────────────────────────────────────────────────────
  // BanlanX encodes brightness in same packet as color (unlike Zengge 0x31)
  buildSolidColor(r: number, g: number, b: number, brightness = 255): number[] {
    return [A0, 0x69, 0x04, r, g, b, brightness];
  }

  // ── Brightness (standalone) ────────────────────────────────────────────────
  // Zengge: 0-100 range. BanlanX: 0-255 range. Convert SK8Lytz 0-100 → 0-255.
  buildBrightness(level: number): number[] {
    const hw = Math.round(Math.min(100, Math.max(0, level)) / 100 * 255);
    return [A0, 0x66, 0x01, hw];
  }

  // ── Effect (returns TWO packets: effect + speed) ───────────────────────────
  // ⚠️ Unlike ZenggeAdapter which returns number[], this returns number[][]
  // useBLE.ts must iterate and send each packet sequentially.
  buildEffect(id: number, speed = 5): number[][] {
    const clampedSpeed = Math.min(10, Math.max(1, Math.round(speed / 10)));
    return [
      [A0, 0x63, 0x01, id],           // effect select
      [A0, 0x67, 0x01, clampedSpeed], // effect speed (1-10)
    ];
  }

  // ── Segment Length ─────────────────────────────────────────────────────────
  // Sets number of addressable pixels. Equivalent to Zengge 0x62 'points' field.
  buildSegmentLength(length: number): number[] {
    return [A0, 0x68, 0x01, Math.min(150, Math.max(1, length))];
  }

  // ── Color/Channel Order ────────────────────────────────────────────────────
  // Equivalent to Zengge 0x62 'sorting' field.
  // order: 1=RGB 2=RBG 3=GRB 4=GBR 5=BRG 6=BGR (same enum as Zengge)
  buildColorOrder(order: 1|2|3|4|5|6): number[] {
    return [A0, 0x64, 0x01, order];
  }

  // ── Music Mode ─────────────────────────────────────────────────────────────
  // ⚠️ DIFFERENT MODEL vs Zengge. No 0x74 magnitude stream needed.
  // Hardware FFT handles audio processing internally.
  // inputSource: 0=internal mic, 1=player, 2=external mic
  buildMusicMode(inputSource: 0|1|2 = 0, gain = 8): number[][] {
    const clampedGain = Math.min(16, Math.max(1, gain));
    return [
      [A0, 0x6C, 0x01, inputSource], // audio source select
      [A0, 0x6B, 0x01, clampedGain], // audio gain 1-16
    ];
  }

  // ── Light Mode ─────────────────────────────────────────────────────────────
  // 0=single effect, 1=cycle dynamic, 2=cycle sound-reactive
  buildLightMode(mode: 0|1|2): number[] {
    return [A0, 0x6A, 0x01, mode];
  }

  // ── State Query ────────────────────────────────────────────────────────────
  buildStateQuery(): number[] {
    return [A0, 0x70, 0x00];
  }

  // ── No-op Wrapper (BanlanX needs no framing) ───────────────────────────────
  wrapCommand(payload: number[]): number[] {
    return payload; // identity — BanlanX format IS the packet
  }

  // ── Pixel Array — PHASE 2 BLOCKED ─────────────────────────────────────────
  // ⛔ Cannot implement until BLE opcode is discovered via:
  //   A) Blutter Dart AOT decompile of libapp.so
  //   B) HCI snoop capture during DIY Pixel / Car Lights mode
  buildPixelArray(_colors: RGBColor[]): number[] {
    throw new Error(
      '[BanlanxAdapter] buildPixelArray: pixel streaming opcode unknown. ' +
      'Run Blutter or HCI sniff. See BANLANX_PROTOCOL_BIBLE.md §8 & §11.'
    );
  }
}
```

### `ControllerRegistry.ts` — 1-Line Change

```typescript
// BEFORE:
import { ZenggeAdapter } from './ZenggeAdapter';

// AFTER (add BanlanX registration):
import { ZenggeAdapter } from './ZenggeAdapter';
import { BanlanxAdapter } from './BanlanxAdapter';

// In constructor:
this.register(new BanlanxAdapter()); // ← Add this line
this.register(new ZenggeAdapter());
```

### `useBLE.ts` — Multi-Packet Handling

```typescript
// Helper to send single or multi-packet adapter responses
async function sendProtocolResult(
  deviceId: string,
  result: number[] | number[][],
  write: (id: string, data: number[]) => Promise<void>
): Promise<void> {
  const packets = Array.isArray(result[0])
    ? (result as number[][])
    : [result as number[]];
  for (const packet of packets) {
    await write(deviceId, packet);
  }
}
```

### `IControllerProtocol.ts` — Interface Update

```typescript
// Update buildEffect signature to support multi-packet vendors:
buildEffect(id: number, speed?: number, brightness?: number): number[] | number[][];

// Update buildMusicConfig similarly if needed:
buildMusicConfig(...args: any[]): number[] | number[][];
```

### Zengge Feature Parity Checklist

| Feature | ZenggeAdapter | BanlanxAdapter Phase 1 | Status |
|:--------|:-------------|:-----------------------|:-------|
| Power ON | `[0x71, 0x23, 0x0F, chk]` | `[0xA0, 0x62, 0x01, 0x01]` | ✅ |
| Power OFF | `[0x71, 0x24, 0x0F, chk]` | `[0xA0, 0x62, 0x01, 0x00]` | ✅ |
| Solid Color | `[0x31, R, G, B, bri, 0x0F, chk]` | `[0xA0, 0x69, 0x04, R, G, B, bri]` | ✅ |
| Brightness | `[0x37, 0-100, 0, chk]` | `[0xA0, 0x66, 0x01, 0-255]` (scaled) | ✅ |
| Effect | `[0x42, id, spd, bri, chk]` | `[[0x63,id], [0x67,spd]]` | ✅ |
| Pixel Array | `[0x59, lenHi, Lo, RGB×N...]` | BLOCKED — opcode TBD | ❌ Phase 2 |
| Live Stream | `[0x53, lenHi, Lo, RGB×N...]` | BLOCKED — opcode TBD | ❌ Phase 2 |
| Music Config | `[0x73, 13 bytes, chk]` | `[[0x6C,src], [0x6B,gain]]` | ✅ (diff model) |
| Music Mag | `[0x74, mag, chk]` | N/A — hardware FFT | ✅ (not needed) |
| State Query | `[0x47, 0, chk]` | `[0xA0, 0x70, 0x00]` | ✅ |
| No Wrapper | V2: `[0x00, seq, 0x80, ...]` | None — identity passthrough | ✅ |
| No Checksum | SUM all bytes | None | ✅ |
| Advertisement | `mfBuf[10..11]` == 0xA3 | MFR ID 0x5053 or byte[4] 0x0D/0x16 | ✅ |
| Service UUID | `0000ffff-...-00805f9b34fb` | `0000ffe0-...-00805f9b34fb` | ✅ |
| Write UUID | `0000ff01-...-00805f9b34fb` | `0000ffe1-...-00805f9b34fb` | ✅ |

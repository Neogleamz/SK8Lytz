# SK8Lytz

> **Professional Bluetooth LED Controller for Roller Skates**
> Built by [Neogleamz](https://github.com/Neogleamz) · React Native · Android

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![React Native](https://img.shields.io/badge/React_Native-Expo-0052CC?style=flat-square&logo=expo&logoColor=white)
![Protocol](https://img.shields.io/badge/protocol-Zengge_BLE-FF5A00?style=flat-square)
![License](https://img.shields.io/badge/license-Private-1B4279?style=flat-square)

---

## What is SK8Lytz?

SK8Lytz is a high-performance Android application for controlling addressable RGB LED lighting systems embedded in roller skates. It communicates with **Zengge Symphony BLE controllers** (Magic Home / LEDnetWF protocol) via Bluetooth Low Energy to provide real-time, professional-grade lighting control.

Supports individual device control, synchronized multi-device groups, music-reactive lighting, camera color tracking, and 210+ built-in animation patterns.

---

## Features

- **BLE Device Management** — Scan, connect, and manage multiple Zengge Symphony LED controllers simultaneously
- **Group Control** — Sync multiple skate controllers for coordinated lighting across both skates
- **10+ Control Modes** — Fixed color, RBM Patterns (210 total), Music sync, Camera tracking, Candle flicker, DIY Array, and more
- **Hardware Programmer** — Read and write EEPROM LED config (points, segments, IC type, color sorting) via `0x62`/`0x63` protocol
- **SK8Lytz Analytics** — Full session telemetry: per-device event logs, mode/pattern/color usage stats, BLE performance metrics
- **Admin Hardware Tester** — Developer-only BLE packet sniffer, payload factory, and EEPROM configurator
- **Curated Presets** — Cloud-synced "SK8Lytz Picks" preset library fetched from Supabase
- **Dark / Light Theme** — Fully themed UI with Neogleamz brand palette

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | React Native (Expo) |
| Language | TypeScript |
| Bluetooth | `react-native-ble-plx` |
| Backend / Telemetry | Supabase (Postgres + Storage) |
| Build | Gradle (local) / EAS Build (cloud) |
| Platform | Android (API 31+) |

---

## Hardware Protocol

SK8Lytz implements the **Zengge V2 BLE Protocol** (Magic Home / LEDnetWF / Symphony):

- **Transport**: BLE GATT — Service `0000ffff`, Characteristic `0000ff01`
- **Packet Format**: 8-byte V2 wrapper (`0x00 SEQ 0x80 0x00 LEN_H LEN_L CMD_LEN 0x0B`) + raw payload
- **Key Commands**:
  - `0x31` — Solid color (RGB)
  - `0x42` — RBM pattern selection (1-210)
  - `0x62` / `0x63` — EEPROM write / read (LED points, IC type, color sorting)
  - `0x71` — Power on/off
  - `0x73` — Music sync mode

Supported IC types: WS2812B, SM16703, SM16704, WS2811, UCS1903, SK6812, and more.

**Default hardware profiles:**

- **HALOZ** — 16 LEDs, WS2812B, GRB color order (wheel/frame ring)
- **SOULZ** — 43 LEDs, WS2812B order (boot/sole strip)

---

## Project Structure

```
SK8Lytz/
├── src/
│   ├── screens/          # DashboardScreen — root app screen, owns all BLE state
│   ├── components/       # UI components (DockedController, AdminHardwareTester, LogViewerModal, ...)
│   ├── hooks/            # useBLE — BLE engine (scan, connect, write)
│   ├── protocols/        # ZenggeProtocol — all BLE command generation
│   ├── services/         # AppLogger — telemetry & Supabase analytics
│   ├── constants/        # RbmPatterns — full 210-pattern library
│   ├── utils/            # RbmDictionary, MusicDictionary
│   ├── context/          # ThemeContext — dark/light theme
│   └── theme/            # Brand colors, typography, layout tokens
├── tools/
│   ├── SK8Lytz_App_Master_Reference.txt   # Full protocol & codebase reference
│   ├── SK8Lytz_Image_Cross_Reference.txt  # Asset cross-reference
│   ├── build-apk.ps1                      # Local APK build script
│   ├── install-on-device.ps1             # ADB install script
│   ├── ingestPicks.mjs                   # Supabase presets ingestion
│   └── upload_settings.mjs              # Settings upload helper
├── assets/               # App icons, splash screen, logo
├── app.json              # Expo app configuration
├── eas.json              # EAS Build profiles (dev / preview / production)
└── package.json          # Dependencies
```

---

## Building

### Local APK (recommended)

```powershell
# Build release APK
.\tools\build-apk.ps1

# Install to connected Android device
.\tools\install-on-device.ps1
```

### Requirements

- Node.js 18+
- Android SDK (API 31+)
- ADB connected device with USB debugging enabled
- `npm install` (uses `legacy-peer-deps=true` — see `.npmrc`)

---

## Analytics & Telemetry

SK8Lytz includes a built-in enterprise telemetry pipeline powered by **Supabase**:

- Events are buffered locally in AsyncStorage and uploaded on demand
- Group commands are **unrolled per device** — each hardware node gets its own telemetry row, enabling per-device malfunction detection
- Custom device names (set in Hardware Settings) flow through to all Supabase tables
- Local buffer is cleared after every confirmed successful upload (no re-uploads)

**Supabase tables:** `parsed_session_stats`, `parsed_session_devices`, `parsed_logs`, `parsed_mode_usage`, `parsed_pattern_usage`, `parsed_color_usage`

---

## Key Files at a Glance

| File | Purpose |
|---|---|
| `src/screens/DashboardScreen.tsx` | Root screen — owns all BLE + device state |
| `src/hooks/useBLE.ts` | BLE engine — scan, connect, write |
| `src/protocols/ZenggeProtocol.ts` | All hardware command generation |
| `src/services/AppLogger.ts` | Telemetry, analytics, Supabase upload |
| `src/components/DockedController.tsx` | Main LED control UI panel |
| `src/components/AdminHardwareTester.tsx` | Admin BLE sniffer + EEPROM tool |
| `src/components/LogViewerModal.tsx` | In-app analytics viewer |
| `tools/SK8Lytz_App_Master_Reference.txt` | Complete protocol & hardware reference |

---

*© Neogleamz. All rights reserved.*

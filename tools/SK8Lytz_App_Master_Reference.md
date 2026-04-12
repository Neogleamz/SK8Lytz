# SK8Lytz App Master Reference

*Last Updated: 2026-04-11 | Source of Truth: `src/protocols/ZenggeProtocol.ts`*

This document is the **Canonical Reference** for all architecture, hardware constraints, and BLE protocol definitions within the SK8Lytz application.

> [!CAUTION]
> Do NOT append duplicate or conflicting protocol discoveries to this document. If a payload format changes, **overwrite** the existing entry to ensure this file remains a single, conflict-free source of truth.

---

## 1. Product Bible (Vision & North Star)

**The Mission:**
To empower the radiant culture of roller skating by building the world's most expressive and innovative lighting ecosystem. SK8Lytz isn't just an app; it's the digital pulse for your skates—enabling flawless, zero-latency light synchronization ("Glow Your Way") that transforms solo sessions into high-performance visual art and massive Crew Hub rink takeovers into coordinated spectacles.

**Target Audience:**
Sk8Lytz caters to a diverse, family-oriented community of dedicated roller skaters. They operate in high-energy, low-light environments (rinks, street night sessions, park bowls). They value durability, ease of use (wrist guards, movement), and the ability to express their unique style through synchronized, diffused lighting.

### Core Product Lines

#### **SOULZ** (The High-Intensity Pro Strip)

* **Concept**: 56" of total illumination via four 14" diffused silicone addressable LED strips.
* **Performance**: 2-6+ hours of run time.
* **Charging**: 90 min full cycle (USB-C).
* **Control**: Integrated Bluetooth/RF + High-sensitivity integrated microphone for instant "vibe" reactivity.

#### **HALOZ** (The Compact Matrix Box)

* **Concept**: Individually controllable high-density pixel boxes for wheels/plates.
* **Performance**: 2-4+ hours of run time.
* **Charging**: 60 min fast-charge (USB-C).
* **Control**: Integrated Bluetooth/RF + High-sensitivity integrated microphone.

#### **RAILZ** (Integrated Chassis Strips)

* **Concept**: Dual parallel vertical LED strips designed for undercarriage/frame mounting.
* **Performance**: Integrated 4-6+ hour run time.
* **Charging**: 90 min (USB-C).
* **Control**: Integrated Bluetooth/RF + High-sensitivity integrated microphone.

**Core Philosophies (The 3 Pillars):**

1. **Bulletproof BLE Transport:** The connection to Neogleamz hardware MUST be instantaneous and nearly sentient. Reconnects and pairing must handle GATT exceptions and MTU drift invisibly. "It just works, immediately."
2. **Tactile, Glanceable UI:** High-contrast, Neogleamz standard aesthetics. Massive touch targets (>44px) for skaters in gear. One-tap access to Symphony effects and App-mic visualization.
3. **No-Compromise Offline Flow:** Hardware control is a fundamental right. basic lighting and EEPROM configuration (0x62/0x63) never require cloud authentication.

**Anti-Goals (What we ruthlessly reject):**
* **Bloated Developer Logic in Prod:** We use strict `__DEV__` elimination to keep the binary lean and free of testing debris.
* **Complex UI Micro-Management:** Skaters want to skate. We provide stunning Pro Effects and high-precision HUDs (Speed/Brightness), not frame-by-frame animation editors.
* **Hardware-Cloud Gating:** We never lock essential local hardware features behind an internet authentication wall.
* **Hardcoded Hardware Heuristics:** The UI layer must NEVER use explicit string literals (e.g. `type === 'HALOZ'`) or hardcoded binary logic to render products. All hardware metadata (shape, icons, colors) must be dynamically derived from `LOCAL_PRODUCT_CATALOG` (`src/constants/ProductCatalog.ts`) to ensure scalable, zero-code support for new OEM devices.

---

## 2. System Architecture & Local Storage

### AsyncStorage Key Registry

| Key | Owner | Contents |
|:---|:---|:---|
| `@sk8lytz_logs` | AppLogger | Compact telemetry event buffer array |
| `@Sk8lytz_auth_username`| DashboardScreen | Local cache of Supabase display_name for instant UI feedback |
| `ng_device_configs` | DashboardScreen / AppLogger | Dict keyed by MAC containing `{ name, type, points, segments, sorting, stripType, groupId }` |
| `ng_custom_groups` | DashboardScreen | Array of `{ id, name, isGroup, deviceIds }` |
| `ng_processed_devices`| DashboardScreen | Cached array of previously discovered device objects |
| `@sk8_hw_<deviceId>` | Sk8LytzProgrammerModal| Per-device EEPROM hardware settings cache |
| `@sk8lytz_theme` | ThemeContext | `dark` or `light` |
| `@sk8lytz_control_theme`| ThemeContext | Control color theme name |
| `@Sk8lytz_Favorites` | DashboardScreen | Dictionary of user-defined lighting presets (Name, Palette, Mode) |
| `@Sk8lytz_voice_tutorial_dismissed` | boolean | Gating for the Voice Command onboarding modal |

## Build Config & Troubleshooting 🛠️

### Android Build Requirements
To resolve dependency conflicts and legacy library issues, the following configurations are required:
- **Jetifier**: Must be enabled (`android.enableJetifier=true`) to migrate legacy Support libraries to AndroidX.
- **SDK Versions**: Project currently targets SDK 34 (`compileSdk`, `targetSdk`).

### Third-Party Library Patches
- **@react-native-voice/voice**:
  - Requires manual removal of `jcenter()` repository from `node_modules/@react-native-voice/voice/android/build.gradle`.
  - SDK versions in the library's `build.gradle` must be bumped to 34 to avoid manifest merger conflicts.
  - Legacy `com.android.support` dependencies must be replaced with `androidx` equivalents (e.g., `androidx.appcompat:appcompat:1.3.1`).

### Dashboard UI Layout (4-Slab Architecture)

The primary dashboard uses a **Vertical Slab (No-Scroll)** layout to maximize glanceability and touch accuracy.

1. **Slab 1: Dynamic Header**: Logo, user profile, and active polling/telemetry indicator.
2. **Slab 2: Crew Hub**: Active session discovery and quick-join pills.
3. **Slab 3: My Skates / Groups**: High-impact cards for grouped hardware with global power controls.
4. **Slab 4: Hardware Fleet**: List of all registered devices with a "TAP TO ADD" quick-access wizard link.

### UI Design Patterns & Branding

*   **Tucked-in Attribution**: Credit links (e.g., "by neogleamz.com") must be placed discreetly within header containers, aligned with the visual boundary of the primary logo (e.g., `marginRight: '16%'` for a 300px logo) and using `fontSize: 9` with `fontWeight: '800'` muted text.
*   **Fluid Component Scaling**: Components (Builders, Camera Viewers) must NOT use hardcoded heights. They must utilize available `flex` space between the `ProductVisualizer` and the bottom dock to ensure responsiveness across all aspect ratios.
*   **One-Screen Setup Policy**: The Hardware Setup Wizard must minimize vertical occupancy. For naming and registration (Step 3), all primary controls (Fleet Name, Device Labels, Type, Position) must be visible on a single standard mobile viewport (e.g. iPhone SE) without requiring a vertical scroll for a standard 2-skate setup. Use horizontal inlining and 8pt grid proximity instead of explicit labels where possible.

### Admin Tools Hub (The Command Center)

The **Admin Tools Hub** (`AdminToolsModal`) is the unified gateway for all system-level diagnostics and hardware maintenance. 

* **Access**: 10-tap the SK8Lytz logo in the dashboard header + Passcode: `0000`.
* **Tab 1: TIMELINE**: Virtualized system event log (BLE protocol, app lifecycle, errors).
* **Tab 2: STATS**: Session analytics, mode usage frequency, and hardware performance metrics.
* **Tab 3: DEVICE**: Deep-dive hardware view showing all discovered peripherals and their cached configs.
* **Tab 4: TOOLS**: Administrative portal for low-level components:
    *   **Catalog Manager**: Unified editor for product profiles. **MANDATORY**: All write operations (`upsert`) are gated by a Supabase Session check to prevent unauthorized database manipulation.
    *   **LED Diagnostic Lab**: Atomic protocol validation and DIY payload building.
    *   **Firmware Programmer**: Low-level hardware updates and serial-over-BLE tools.
    *   **Optical Simulation Mode (Web Fallback)**: A dedicated developer interface for non-native environments (Expo Web). It provides manual telemetry simulation (randomized hex dispatch) to smoke-test visualizer and state-management pipelines without physical hardware.
* **Schema Reliability**: Product Manager upserts are strictly typed to enforce the `batteryCapacityMilliAmpereHour` field, preventing database record drift for new HALOZ/SOULZ revisions.
* **Navigation Orchestration**: Closing any administrative sub-tool (Lab, Programmer) must explicitly re-trigger the visibility of the `AdminToolsModal` in the parent `DashboardScreen` to ensure a consistent "nested" navigation experience.

### Test Users & Environments

For testing App Sync behavior vs. Offline mode offline fallbacks, you can authenticate using the primary test user:
* **Email**: `testuser@sk8lytz.com`
* **Password**: `Password!2026`
* **Username**: `TestSkater`

### Supabase Architecture (Telemetry & Registration)

*Project ID:* `qefmeivpjyaukbwadgaz`

#### **`registered_devices`** (Hardened Schema)
| Column | Type | Purpose |
|:---|:---|:---|
| `id` | TEXT (PK) | Unique system identifier (Client-generated UUID or Hash) |
| `device_mac` | TEXT | Unique hardware address |
| `user_id` | UUID | Owner ID |
| `device_name` | TEXT | Custom alias |
| `product_type` | TEXT | HALOZ / SOULZ / RAILZ (Dynamic from `product_catalog`) |
| `position` | TEXT | Left / Right / Front / Back |
| `group_name` | TEXT | Auto-assignment to hardware groups |
| `led_points` | INT | Physical pixel count |
| `segments` | INT | Virtual segments |
| `ic_type` | INT | 1 (WS2812), etc. |
| `color_sorting`| INT | 2 (GRB), etc. |
| `rssi_at_register`| INT | Connection quality at first sync |
| `firmware_ver` | INT | |
| `led_version` | INT | |
| `product_id` | INT | |

#### **`product_catalog`** (Dynamic Hardware Definitions)
| Column | Type | Purpose |
|:---|:---|:---|
| `id` | TEXT (PK) | Unique product key (e.g., SOULZ, HALOZ, RAILZ) |
| `display_name` | TEXT | Human-readable name used in UI |
| `is_active` | BOOLEAN | If false, app ignores this profile |
| `detect_min_points` / `max` | INT | FTUE Auto-Detection ranges (0x63 query) |
| `default_led_points` | INT | Factory default written via 0x62 |
| `viz_shape` | TEXT | Geometry ID for ProductVisualizer (RING / OVAL / DUAL_STRIP) |
| `viz_blob_diameter_mm` | REAL | Physical pixel diameter for canvas rendering scale |
| `brand_icon` | TEXT | MaterialCommunityIcons string (e.g., 'circle-double') |
| `viz_theme_color` | TEXT | HEX branding color (e.g., '#00C8FF') |
| `battery_capacity_mah`| INT | Rated capacity for power modeling (e.g. 3000) |

> [!WARNING]
> The app enforces **Strict Column Mapping** in `useRegistration.ts`. Any new database column MUST be added to the explicit mapping in the `dbRow` object to prevent schema cache mismatch errors during cloud sync.

#### **`app_settings`** (Governance & Registry)
| Column | Type | Purpose |
|:---|:---|:---|
| `setting_key` | TEXT (PK) | Unique key (e.g., `required_eula_version`) |
| `setting_value` | TEXT | Value (Version num, JSON flags) |

#### **`user_profiles`** (Compliance Tracking)
- **`accepted_eula_version`** (INT): Tracks the last EULA version signed by the user. Blocking logic triggers if this is less than `required_eula_version`.

### 🏛️ Feature Governance Policy Model

Features are managed via a centralized policy registry in `AppSettingsService`. All module visibility and "Smart Locks" are derived from this state:

| Strategy | Behavior |
|:---|:---|
| `GLOBAL_LOCK` | Feature is physically disabled and grayed out across the entire fleet. |
| `HIDE_OFFLINE` | Feature vanishes from the UI if the device has no internet/Supabase heartbeat. |
| `GATED_OFFLINE` | Feature is visible but displays a "Connect to Cloud" overlay when tapped. |
| `ADMIN_ONLY` | Feature only visible to `is_admin: true` profiles. |

* **`parsed_session_stats`**: One row per app session summary (`session_id` UNIQUE)
* **`parsed_session_devices`**: All BLE devices seen per session (`session_id + device_id` UNIQUE)
* **`parsed_logs`**: Full event trace log. Appended continuously.
* **`parsed_mode_usage`** / **`parsed_pattern_usage`** / **`parsed_color_usage`**: Frequency metrics.
* **`telemetry_errors`**: Global crash hounds and unhandled exception tracker. Target for AI bug-hunter triage.

> Group telemetry events MUST pass `deviceIds: string[]` in the `AppLogger` device context so the engine can unroll the event into individual, per-device rows in Postgres.

### Hardware Discovery & Identification (The FTUE Logic)

The app uses a "Search & Enrich" strategy for First Time User Experience (FTUE).

1. **Instant Enrollment**: All Zengge/Symphony MAC addresses are listed immediately in the Wizard as "SCANNING".
2. **Round-Robin Probing**: A sequential, persistent background loop connects to each unknown device to retrieve EEPROM data (0x63).
3. **Threshold Classification**: (Dynamic via `ProductCatalog.ts` / `product_catalog` DB table)
   * **SOULZ**: 28–300 LEDs (Standard 43).
   * **HALOZ**: 10–27 LEDs (Standard 16).
   * **RAILZ**: 1–9 LEDs (Standard 10 - *Pending HW confirmation*).
   * **UNKNOWN**: Fallback to safe defaults (SOULZ @ 43pts) if probing fails after 3 retries.
4. **Clean Install Enforcement**: The build-apk script strictly aborts on Gradle failure, and install-apk performs a full `adb uninstall` before push.

---

## 2. Hardware Profiles & Constraints

### Physical Product Specifications

| **Feature** | **SOULZ** | **HALOZ** | **RAILZ** |
|:---|:---|:---|:---|
| **Form Factor** | Diffused Silicone Strips (x4) | Compact High-Density Box | Parallel Under-Strips (x2) |
| **Length/Size** | 56" Total (14" per) | 1.5" x 3" Body | Inline Vertical Tracks |
| **Battery Life**| 4-8+ hours (2000mAh) | 3-5+ hours (1200mAh) | 6-10+ hours (2000mAh) |
| **Charging** | 90 min (USB-C) | 60 min Fast-Charge | 90 min (USB-C) |
| **LED Type** | Diffused Addressable RGB | High-Density RGB Pixels | High-Intensity Strips |
| **Audio** | Integrated Mic | Integrated Mic | Integrated Mic |

### Hardware Capability Thresholds

- **Maximum Points**: 300 LEDs
* **Maximum PxS**: 2048 (Points × Segments)
* **Maximum Mic Points**: 150 LEDs
* **Maximum Mic PxS**: 960

### IC Types (`icType` Index)

- `1`: WS2812B (SK8Lytz Default for all HALOZ/SOULZ)
* `2`: SM16703
* `4`: WS2811
* `6`: SK6812

### Color Sorting Maps (`sorting` Index)

- `0`: RGB
* `1`: RBG
* `2`: **GRB** (SK8Lytz Default for all HALOZ/SOULZ)
* `3`: GBR
* `4`: BRG
* `5`: BGR

> [!IMPORTANT]
> Modern HALOZ/SOULZ hardware (WS2811/WS2812B) natively handles color remapping internally based on the EEPROM sorting index (set via 0x62).
> The application software should send **PURE RGB** bytes. Do NOT pre-sort colors using `applyColorSorting()` for 0x59 or 0x73 payloads, as this will result in double-swapped / incorrect colors.

---

## 3. BLE Protocol Library

> [!IMPORTANT]
> **Dynamic Catalog Migration (2026-04-11)**: The legacy `SK8_DEFAULTS` constant in `ZenggeProtocol.ts` has been REMOVED.
> All hardware profile logic—including default LED counts, visualization themes, and discovery categorization—is now handled strictly via `LOCAL_PRODUCT_CATALOG` (`src/constants/ProductCatalog.ts`).


All byte definitions below represent the inner payload *before* the V2 BLE packet wrapper is applied.

### BLE Stability Constraints & GATT Error Prevention

> [!CAUTION]
> React Native BLE PLX and the Android native `BluetoothAdapter` suffer from extreme race conditions. To avoid GATT 133 exceptions, UI freezes, and buffer overflows, all logic must follow these three architectural constraints:

1. **Strict Sequential Teardowns (`GATT 133` Prevention):** If `disconnectFromDevice()` fires overlapping or un-awaited `cancelDeviceConnection()` promises, the Android stack will overflow. 
   - **Rule:** Teardowns MUST be strictly awaited sequentially using standard `for..of` loops, followed by a soft ~250ms buffer to allow the OS to complete physical teardown before clearing the UI state.
   - **Latch Requirement:** UI components and the `disconnectFromDevice` function must implement and check an `isDisconnecting` state lock to intercept and drop any debounced Write promises so they don't fire into a dead stack.
2. **Global Mutex Queue (Parallel Write Crash Prevention):** Calling multiple `writeCharacteristic` commands within milliseconds (e.g. user rapidly dragging a slider) will lock up the adapter.
   - **Rule:** The `writeToDevice` function must be wrapped in a global Promise Mutex (FIFO queue). No payload chunk can be transmitted until the previous chunk's promise resolves or times out.
3. **Hard Connection Timeouts (Infinite Freeze Prevention):** BLE promises can silently hang forever.
   - **Rule:** EVERY connection attempt (`bleManager.connectToDevice()`) MUST include an explicit `{ timeout: 5000 }` parameter. Connect logic should aggressively catch timeout rejects, clear the connection, and reset the state machine.

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
* **Power OFF (0x71):** `[0x71, 0x24, 0x0F, 0xA4]`
* **Set RBM Pattern (0x42):** `[0x42, patternId, speed, brightness, checksum]`
* **Music Config (0x73):** `[0x73, micSource, modeType, patternId, c1.R, c1.G, c1.B, c2.R, c2.G, c2.B, 0x20, sensitivity, brightness, checksum]`
* **Music Live Data (0x74):** `[0x74, audioMagnitude, checksum]` — Dispatched continually for phone-mic reactivity.
* **RF Auth Setting (0x2A):** `[0x2A, modeByte, 0xFF,0xFF,0xFF,0xFF,0xFF, clearByte, 0x00...0x0F]` (`modeByte=0x01` Block all, `0x02` Known only, `0x03` Allow all).

### 4. Proactive Battery Management System (Architectural Skill)

Due to the lack of high-fidelity state-of-charge feedback from the hardware, the app implements a **Mathematical Consumption Modeling** system:
1. **Capacity Presets**: SOULZ (2000mAh), HALOZ (1200mAh), RAILZ (2000mAh).
2. **Drain Calculation**: Real-time modeling of pixel density, brightness, and pattern intensity.
3. **Safety "Limp Mode"**: Automatic 0x71 brightness dimming to **20%** when the modeled reserve reaches the 20% critical threshold.
4. **Telemetry Sniffing**: Ongoing research into secondary 0x63 response bytes to identify raw voltage/SoC data.

---

## 5. Agentic PM Protocols (The Brain)

This project is governed by a custom-built **Agentic OS**—a suite of 38 strict protocols located in `.agents/rules/`. These rules are permanently active via the `trigger: always_on` YAML framework, ensuring the AI assistant operates with human-level precision, safety, and product empathy.

### The Agentic Manifesto
We believe that AI coding shouldn't be a "black box." By enforcing strict intake gates, surgical edit patterns, and mandatory corporate memory synchronization, we ensure that every line of code is intentional, auditable, and aligned with the Neogleamz mission.

---

### Tier 1: Safety, Precision & Stealth (The Shield)
*Protects the codebase from regressions, accidental deletions, and secondary impacts.*

| Rule | Function |
|:---|:---|
| **Critical Safety & Quarantine** | Strict 7-step branching strategy. Forbids `main` pushes and `.git/hooks` manipulation. |
| **Surgical Strike Protocol** | Mandates micro-edits and 10-line chunking. Forbids monolithic file overwrites. |
| **Absolute Truth (Anti-Hallucination)** | Forces the AI to read the Master Reference before proposing byte-level or architectural changes. |
| **Security & Secrets Standard** | Zero-tolerance for hardcoded keys. Enforces `.env` boundaries. |
| **Panic Button (Emergency Triage)** | Immediate read-only mode. Provides safe git escape routes during crises. |
| **Local Tool Enforcement** | Forces use of native API tools over generic terminal commands for FS operations. |

---

### Tier 2: Workflow, PM & Intake (The Engine)
*Manages the backlog, branch lifecycle, and project velocity.*

| Rule | Function |
|:---|:---|
| **Zero-Bypass Intake** | Intercepts casual requests and routes them through the formal Bucket List system. |
| **Auto-Branching (Bucket List)** | Sequentially parses `tools/SK8Lytz_Bucket_List.md` to automate feature development. |
| **Idea Intake Workflow** | Categorizes and slugs new ideas with priority overrides (`bump`/`up next`). |
| **Ship It / Merge Task** | Formalized release manager workflow for merging features into base branches. |
| **Midnight Oil (Wind Down)** | Standardized end-of-session sequence (Git sync, Memory sync, SITREP). |
| **Status Update (SITREP)** | Generates high-density progress dashboards for active Epics. |
| **Semantic Commits Enforcer** | strictly enforces Conventional Commits (`feat:`, `fix:`, `docs:`) with scopes. |
| **Whiteboard / Brainstorming** | Mode-switch for abstract thinking, logic mapping, and non-coding discussion. |
| **Context Memory Compiler** | Rebuilds the `ARCHITECTURE_MAP.md` to ensure the AI's mental map is current. |

---

### Tier 3: Design & Quality Standards (The Ruler)
*Enforces the visual and technical "Neogleamz Standard."*

| Rule | Function |
|:---|:---|
| **Modern UI/UX Architect** | Enforces the 4-State Matrix (Loading/Error/Empty/Success) and 8pt grid discipline. |
| **Mobile-First Standards** | Ensures absolute responsiveness and iOS/Android compatibility (Safe Areas, Touch Targets). |
| **Coding Standards (Clean Code)** | Mandates modularity, single responsibility, and modern ES6+ syntax. |
| **Test-First Standard (TDD)** | Requires unit test generation BEFORE application code modifications. |
| **Dependency Diet** | requires 3-point justification before adding any external npm packages. |
| **Product Alignment** | Lead PM persona that pushes back on features misaligned with the "Product Bible." |
| **Meta-Evolution** | Enables the AI to propose updates to its own rule set when friction is detected. |

---

### Tier 4: Assistance, Empathy & Mentorship (The Guide)
*Ensures human-AI alignment and logic transparency.*

| Rule | Function |
|:---|:---|
| **Echo Protocol (Verification)** | mandatory playback of intent and assumptions to prevent scope creep. |
| **Jargon Brake (Mentorship)** | ELI5 mode—breaks down complex BLE/Math logic using simple analogies. |
| **Rubber Duck (Logic ELI5)** | Forced pause to explain "Black Box" logic before writing the first line of code. |
| **Devil's Advocate** | Ruthlessly identifies critical failure points in new ideas before implementation. |
| **Simulated User (UX)** | Novice skater persona—tests UI for usability while wearing wrist guards and skating. |

---

### Tier 5: Debugging, Hygiene & Maintenance (The Janitor)
*Keeping the repo clean and technical debt low.*

| Rule | Function |
|:---|:---|
| **Emergency Debug Drill** | Halts production fix-guessing. Enforces instrumentation and telemetry validation. |
| **Isolated Test & Verify** | Scope-locks testing to the current git diff only. |
| **Legacy Audit & Refactor** | Formalized procedure for bringing old code up to the current Agentic Standard. |
| **Boy Scout (Tech Debt)** | Mandates exactly ONE small cleanup in every file modified. |
| **Tech Debt Janitor** | Maintenance sweep of TODOs, HACKs, and outdated dependencies. |
| **Bug Hunter Workflow** | Specialized persona for root-cause analysis and edge-case discovery. |
| **Supabase Schema Sync** | Automatically regenerates TypeScript types after any DB migration. |
| **Repository Cleanup** | Automates the pruning of merged branches. |

---

### Tier 6: Persistence & Continuity (The Memory)
*Ensures the project learns and remembers.*

| Rule | Function |
|:---|:---|
| **Corporate Memory Sync** | Mandatory updates to this Master Reference after every complex fix or discovery. |
| **Save Point & Abort** | Enables rapid check-pointing of the workspace to allow safe rollbacks. |

---

## 8. Sentinel Engineering Governance (Workflow V6)

*Added: 2026-04-12 | Doctrine: "Stability-First"*

The SK8Lytz development lifecycle is governed by the **Sentinel Engine**, a deterministic framework designed for high-reliability engineering.

### Strategic Priority Hierarchy
1.  **CRITICAL**: Performance, Stability & Security (Crashes, RLS Blocks).
2.  **HIGH**: Engineering Excellence & Tech Debt (Refactors, FSMs).
3.  **MEDIUM**: Compliance & Governance (EULA, Admin).
4.  **LOW**: New Features & UI Polish.

### Mandatory Safety Governors
- **Safe-Commit Anchors**: For all `[H-RISK]` tasks, a git restore point is created immediately after branching to ensure 100% rollback reliability.
- **The Senior Auditor (Step 6.5)**: Every task is blocked from transition until a mandatory "Self-Review & Refactor" pass is completed by the AI persona acting as a Senior Auditor.
- **Devil's Advocate Gate**: All `[Feast]` items require a formal pre-mortem identifying 3 failure points before planning.
- **Knowledge Audit Gate**: Merge blocks are enforced until this Master Reference is synced with the session's new architectural truths.

### Velocity Protocols
- **Turbo Step (`// turbo`)**: Authorized auto-run for terminal commands to reduce user approval friction.
- **Snack Autopilot**: Zero-bypass execution allowed for tasks meeting the **`[BATCH]` + `[Snack]` + `[L-RISK]`** criteria.
- **T-Shirt Sizing**: All tasks must be tagged `[Snack]` (<15m), `[Meal]` (1-2h), or `[Feast]` (Multi-day).

---

### Developer Tooling & Distribution

#### **Agentic PM Starter Kit**
*   **Location**: `Agentic_PM_Starter_Kit.zip` (Root)
*   **Contents**: All 38 rules + the `INIT_PM_AGENT.md` bootloader.
*   **Purpose**: Allows any developer to inject this entire PM brain into their own AI-powered IDE (Cursor, VS Code + Agent) instantly.

#### **The Genesis Prompt**
The `INIT_PM_AGENT.md` is a specifically tuned bootloader. When pasted into a new AI session, it commands the agent to:
1.  Ingest the rule vault.
2.  Acknowledge the protocols.
3.  Autonomously reformat the existing backlog into the **Bucket List Standard**.
4.  Perform a "Gap Audit" on the project's documentation.

---

## 6. Crew Hub & Session Lifecycle

To ensure high-fidelity discovery and telemetry, the Crew Hub follows strict lifecycle and naming protocols.

### Session Naming Convention
To prevent confusion in the "Live Near You" discovery feed, all new sessions automatically append an `_MM/DD` suffix to the crew name (e.g., `O-Town_04/10`). This logic is enforced in `CrewModal.handleCreate`.

### Proximity Discovery & Visibility Rules
The Hub discovery feed is optimized for active, real-time sessions:

- **Universal Radius Filter**: Discovery is governed by `LocationService.getNearbyPublicSessions(radiusMi)`. This console fetches both public and private sessions (for crews the user belongs to) with a strict distance constraint.
- **Location Requirement**: Sessions without valid `location_coords` are suppressed from the "Live Near You" feed to maintain high-signal discovery.
- **Visibility Logic**:
    - **Public Sessions**: Visible to all within radius.
    - **Private Sessions**: Visible ONLY to members of the parent Private Crew who are also within the radius.

### Global Session Lifecycle Cleanup
To prevent stale "ghost" sessions, the system implements multi-layered cleanup:
1. **Global Proactive Cleanup**: `CrewService.cleanupExpiredSessions()` is a system-wide method that ends ANY session older than 24 hours or with stale activity. It is invoked automatically during every Discovery refresh.
2. **Atomic Cleanup (Leader-Based)**: `CrewService.cleanupLegacySessions(userId)` ensures a single user cannot lead multiple concurrent sessions.
3. **Database Hygiene**: `public.crew_sessions` includes an `expires_at` logic (handled via `cleanupExpiredSessions`) to ensure `is_active: true` remains an accurate flag for joint-ready skating events.

---
---
> [!IMPORTANT]
> To remain active, every rule file MUST contain the `trigger: always_on` YAML frontmatter. Failure to include this will cause the AI to revert to standard "Lax" coding behavior.

---

## 7. Session Telemetry Architecture

*Added: 2026-04-11 | Branch: feat/speed-tracking-telemetry*

### Supabase Table: `skate_sessions`

The canonical store for all individual user skate sessions. Each row maps to one recorded session from Street Mode.

| Column | Type | Notes |
|--------|------|-------|
| `id` | `uuid` (PK) | Auto-generated |
| `user_id` | `uuid` | FK → `auth.users` |
| `session_date` | `timestamptz` | Defaults to `now()` |
| `duration_sec` | `int4` | Total session length in seconds |
| `distance_miles` | `float8` | Accumulated via speed × time delta from GPS |
| `avg_speed_mph` | `float8` | Mean of all GPS speed samples during session |
| `peak_speed_mph` | `float8` | Maximum GPS speed recorded |
| `peak_gforce` | `float8` | Peak accelerometer magnitude (G units) |
| `calories` | `int4` | Estimated via MET formula (see below) |
| `location_label` | `text` | Optional human-readable location string |
| `crew_session_id` | `uuid` | Optional FK → `crew_sessions.id` |

### Service: `SpeedTrackingService` (`src/services/SpeedTrackingService.ts`)

Stateless singleton for session persistence. All GPS/Accelerometer state lives in `DockedController.tsx`.

| Method | Signature | Description |
|--------|-----------|-------------|
| `saveSession` | `(snapshot: ISessionSnapshot) => Promise<string \| null>` | Inserts one row into `skate_sessions`. Guards against zero-data saves (<0.01 mi AND <10s). Returns inserted `id`. |
| `fetchRecentSessions` | `(limit?: number) => Promise<ISkateSession[]>` | Returns last N sessions for authenticated user, newest first. |
| `fetchLifetimeStats` | `() => Promise<ILifetimeStats>` | Aggregates all sessions into a lifetime summary object. |

### Contract: `ISessionSnapshot`

```typescript
interface ISessionSnapshot {
  durationSec: number;      // seconds elapsed
  distanceMiles: number;    // GPS speed × time delta accumulation
  avgSpeedMph: number;      // mean of all GPS samples
  peakSpeedMph: number;     // highest GPS sample
  peakGForce: number;       // highest accelerometer magnitude (G)
  locationLabel?: string;   // optional reverse-geocoded label
  crewSessionId?: string;   // optional crew session linkage
}
```

### Calorie Estimation Formula

Uses MET (Metabolic Equivalent of Task) scaled by average speed. Base weight: **70 kg** (canonical approximation — no personal data stored).

```
MET = avgSpeedMph > 12 ? 12 : avgSpeedMph > 8 ? 9 : 7
calories = Math.round(MET × 70 × (durationSec / 3600))
```

### DockedController Integration

- **Start/Stop button** lives in the Street Mode dashboard footer row alongside the TOP SPEED / DISTANCE readouts.
- **Accumulation** happens in `useRef`s (`sessionSpeedSamplesRef`, `sessionDistanceMilesRef`, `sessionPeakSpeedRef`) inside the GPS `watchPositionAsync` callback — **no state updates in the hot path**.
- On STOP: a snapshot is built, `sessionActive` set to false, and `SessionSummaryModal` is shown.
- On Save: `SpeedTrackingService.saveSession()` is called from the modal's `onSave` handler.

### SessionSummaryModal (`src/components/SessionSummaryModal.tsx`)

Post-session debrief modal. Dynamic **speed-zone accent colour** based on peak speed:

| Peak Speed | Accent | Theme |
|-----------|--------|-------|
| ≥ 18 mph | `#FF3D00` | Inferno — elite |
| ≥ 12 mph | `#FF8C00` | SK8Lytz Orange — fast |
| ≥ 6 mph  | `#00E676` | Neon Green — moderate |
| < 6 mph  | `#00B0FF` | Cool Blue — chill |

### AccountModal — Statistics Tab

Tab ID: `'stats'`, icon: `lightning-bolt`. Loaded non-blocking on modal open via `Promise.all([fetchLifetimeStats(), fetchRecentSessions(10)])`. Shows:
- 6-tile lifetime grid: Sessions, Distance, Record Speed, Avg Speed, Time on Skates, Calories
- Scrollable recent session cards with left accent border

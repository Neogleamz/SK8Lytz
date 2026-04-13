# SK8Lytz App Master Reference

*Last Updated: 2026-04-13 | Synced with DDA Refactor — all 14 domain hooks documented | Source of Truth: `src/protocols/ZenggeProtocol.ts`*

This document is the **Canonical Reference** for all architecture, hardware constraints, and BLE protocol definitions within the SK8Lytz application.

1. [Product Bible](#1-product-bible-vision--north-star)
2. [System Architecture](#2-system-architecture--local-storage)
3. [BLE Protocol Library](#3-ble-protocol-library)
4. [Domain-Driven Architecture](#4-domain-driven-architecture)
5. [Database Schemas](#5-database-schemas)
6. [Crew Hub & Session Lifecycle](#6-crew-hub--session-lifecycle)
7. [Session Telemetry Architecture](#7-session-telemetry-architecture)
8. [Agentic PM Protocols](#8-agentic-pm-protocols-the-brain)
9. [Sentinel Engineering Governance](#9-sentinel-engineering-governance-workflow-v6)

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
| `@Sk8lytz_auth_username`| DashboardScreen | Local cache of Supabase display_name for instant UI feedback. Synced via Reactive Context Pattern (Load Cache -> Hydrate Profile -> Update UI). |
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
* **Architecture (Refactor/2026-04-12)**: To prevent "re-render storms" from high-frequency telemetry, the modal utilizes a **Memoized Tab Architecture**. Rendering logic for Timeline, Stats, Device, and Tools is extracted into standalone `React.memo` sub-components, ensuring UI stability during 20Hz notification bursts.
* **Tab 1: TIMELINE**: Virtualized system event log (BLE protocol, app lifecycle, errors). Filtered to exclude RAW_PAYLOAD by default to preserve list performance.
* **Tab 2: STATS**: Session analytics, mode usage frequency, and hardware performance metrics.
* **Tab 3: DEVICE**: Deep-dive hardware view showing all discovered peripherals and their cached configs.
* **Tab 4: TOOLS**: Administrative portal for low-level components:
    *   **Catalog Manager**: Unified editor for product profiles. **MANDATORY**: All write operations (`upsert`) are gated by a Supabase Session check to prevent unauthorized database manipulation.
    *   **LED Diagnostic Lab**: Atomic protocol validation and DIY payload building.
    *   **Firmware Programmer**: Low-level hardware updates and serial-over-BLE tools.
    *   **Optical Simulation Mode (Web Fallback)**: A dedicated developer interface for non-native environments (Expo Web). It provides manual telemetry simulation (randomized hex dispatch) to smoke-test visualizer and state-management pipelines without physical hardware.
* **Persistence & Governance**: 
    * App settings (feature flags) are persisted via `AppSettingsService` with atomic rollback on failure.
    * Product Manager upserts are strictly typed to enforce the `batteryCapacityMilliAmpereHour` field, preventing database record drift.
* **Navigation Orchestration**: Closing any administrative sub-tool (Lab, Programmer) must explicitly re-trigger the visibility of the `AdminToolsModal` in the parent `DashboardScreen` to ensure a consistent "nested" navigation experience.

### Optimistic BLE Write Pipeline ("The Ghost Standard")

The BLE write path uses an **Optimistic UI** architecture to eliminate perceived 80–500ms hardware latency:

| Phase | Status FSM | Behavior |
|:---|:---|:---|
| 1. **OPTIMISTIC** | `onOptimistic()` fires | UI updates INSTANTLY before BLE write |
| 2. **PENDING** | `writeStatus = 'PENDING'` | BLE command dispatched (40ms debounce) |
| 3. **CONFIRMED** | `writeStatus = 'CONFIRMED'` | Hardware ACK'd — light haptic |
| 4. **RECONCILED** | `writeStatus = 'RECONCILED'` | Hardware FAILED — error haptic + `onReconcile()` rollback |

**Key Files:**
- `src/hooks/useOptimisticBLE.ts` — Ghost state FSM, debounce, haptics
- `src/hooks/useBLE.ts` — Core write function (`writeToDevice` returns `Promise<boolean>`)
- `src/components/DockedController.tsx` — Consumer integration (status indicator dot)

**Architectural Constraint:** `writeToDevice` MUST return `Promise<boolean>` (true = success, false = failure) to enable the reconciliation pipeline. All component prop interfaces use `Promise<void | boolean>` for backwards compatibility.

### Test Users & Environments

For testing App Sync behavior vs. Offline mode offline fallbacks, you can authenticate using the primary test user:
* **Email**: `testuser@sk8lytz.com`
* **Password**: `Password!2026`
* **Username**: `TestSkater`

---

## 3. BLE Protocol Library

> [!IMPORTANT]
> **Dynamic Catalog Migration (2026-04-11)**: All hardware profile logic—including default LED counts, visualization themes, and discovery categorization—is now handled strictly via `LOCAL_PRODUCT_CATALOG` (`src/constants/ProductCatalog.ts`).

All byte definitions below represent the inner payload *before* the V2 BLE packet wrapper is applied.

### BLE Stability Constraints & GATT Error Prevention

> [!CAUTION]
> React Native BLE PLX and the Android native `BluetoothAdapter` suffer from extreme race conditions. To avoid GATT 133 exceptions, UI freezes, and buffer overflows, all logic must follow these three architectural constraints:

1. **Strict Sequential Teardowns (`GATT 133` Prevention):** Teardowns MUST be strictly awaited sequentially, followed by a soft ~250ms buffer.
2. **Global Mutex Queue (Parallel Write Crash Prevention):** The `writeToDevice` function must be wrapped in a global Promise Mutex (FIFO queue).
3. **Hard Connection Timeouts (Infinite Freeze Prevention):** Connect logic MUST include explicit timeouts (5000ms).

### The Transport Wrapper (`wrapCommand`)

Every inner protocol payload must be wrapped using the standard 8-byte Zengge V2 framing: 
`[0x00, SequenceNum, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]`

---

### Command: Hardware Config Query (0x63)
*Reads the current EEPROM settings stored inside the controller chip.*
* **Send (5 bytes):** `[0x63, 0x12, 0x21, 0x0F, checksum]`
* **CRITICAL ENDIANNESS:** `ledPoints` bytes are **Little-Endian SWAPPED**: `((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF)`.

### Command: Hardware Config Write (0x62)
*Writes custom segments, IC type, and max LED points permanently to the controller EEPROM.*
* **Format:** `[0x62, ptsHigh, ptsLow, segHigh, segLow, icType, sorting, micPts, micSegs, 0xF0, checksum]`
* **CRITICAL ENDIANNESS:** Uses **Big-Endian format**: `ptsHigh = (points >> 8) & 0xFF`, `ptsLow = points & 0xFF`.

---

### Command: Segmented Multi-Color Layout Array (0x59)
* **Format:** `[0x59, totalLenHi, totalLenLo, [R1,G1,B1...], numLEDsHi, numLEDsLo, transitionType, speed, direction, checksum]`
* **transitionType:** `0x00` Static, `0x01` Gradual, `0x02` Strobe, `0x03` RunningWater.
* **speed:** Clamped strictly between `0x01` and `0x1F`.

---

### Command: DIY Custom Animation Sequences (0x51)
* **Format (291 Bytes):** `[0x51, Step0...Step31, 0x0F_Terminator, checksum]`
* **Step Structure (9 Bytes):** `[ACTIVE_FLAG, transMode, speed, fgRGB, bgRGB]`

---

### Basic Control Commands
- **Power ON (0x71):** `[0x71, 0x23, 0x0F, 0xA3]`
- **Power OFF (0x71):** `[0x71, 0x24, 0x0F, 0xA4]`
- **Set RBM Pattern (0x42):** `[0x42, patternId, speed, brightness, checksum]`
- **Music Config (0x73):** `[0x73, micSource, modeType, patternId, c1RGB, c2RGB, 0x20, sensitivity, brightness, checksum]`

### ### Proactive Battery Management System (Architectural Skill)
The app implements a **Mathematical Consumption Modeling** system using real-time modeling of pixel density, brightness, and pattern intensity to estimate battery reserve.

---

## 4. Domain-Driven Architecture

> [!IMPORTANT]
> **DDA Refactor Shipped: 2026-04-13** — The architecture was refactored from a monolithic component model to a Hook-First domain model. All 14 domain hooks are live on `master`. The audit resolved 4 bugs (2x P0, 2x P1). TSC exit 0.

To ensure scalability and maintain UI performance, the SK8Lytz app enforces a **Hook-First** architecture. Complex business logic, hardware protocols, and Supabase data fetching must be extracted from UI components into decoupled domain hooks. UI components must focus strictly on rendering.

---

### ⚡ Critical Architectural Constraint: BLE Co-location

> [!CAUTION]
> **BLE state (`connectedDevices`, `writeToDevice`, `setOnDataReceived`) MUST remain co-located in `DashboardScreen.tsx`.** Do NOT move these into any domain hook. The BLE lifecycle manager is a singleton with hardware-level race conditions (GATT 133). Distributing it across multiple hook contexts would create multiple competing subscribers which cause silent write failures and GATT exceptions. All domain hooks receive BLE context via **prop injection** only.

---

### 🗺️ Complete Hook Registry (All 14 Hooks)

#### Dashboard Screen Domain (`src/hooks/`)

| Hook | Consumer | Owns |
| :--- | :--- | :--- |
| `useDashboardProfile` | `DashboardScreen` | User profile, `displayName`, `avatarUrl`, Supabase profile fetch |
| `useDashboardGroups` | `DashboardScreen` | `customGroups`, `deviceConfigs` AsyncStorage load/save, group CRUD |
| `useDashboardVoice` | `DashboardScreen` | Voice command engine, mic permissions, command resolution |

#### DockedController Domain (`src/hooks/`)

| Hook | Consumer | Owns |
| :--- | :--- | :--- |
| `useDockedControllerState` | `DockedController` | All LED control FSM state: `activeMode`, `selectedColor`, `brightness`, `speed`, `multiColors`, `builderNodes`, scene capture/restore |
| `useFavorites` | `DockedController` | `favorites[]`, `quickPresets[]`, save/delete/load operations, prompt FSM |
| `useStreetMode` | `DockedController` | Accelerometer subscription, G-force calculation, brake/cruise color dispatch, GPS speed sampling |
| `useSessionTracking` | `DockedController` | Session FSM (`IDLE → RECORDING → SUMMARY`), duration, distance, peak speed, session summary modal |

#### AccountModal Domain (`src/hooks/`)

| Hook | Consumer | Owns |
| :--- | :--- | :--- |
| `useAccountOverview` | `AccountModal` | Supabase profile read/write, avatar upload, display name update |
| `useSkateStats` | `AccountModal` | Aggregate session stats fetch, totals calculation |
| `useDeviceFleet` | `AccountModal` | `registered_devices` Supabase fetch, fleet display list |

#### Admin Domain (`src/hooks/`)

| Hook | Consumer | Owns |
| :--- | :--- | :--- |
| `useDiagnosticLog` | `Sk8LytzDiagnosticLab` | BLE RX/TX log buffer, `targetDeviceId` targeting, raw hex transmission |
| `useProtocolBuilder` | `Sk8LytzProgrammerModal` | FSM-based payload generation for `0x51`, `0x59`, `0x62`, `0x63`, `0x73` |
| `useAdminTelemetry` | `AdminToolsModal` | App analytics, system health metrics, cloud log uploads |
| `useProductManager` | `AdminToolsModal` | Hardware catalog CRUD, `product_catalog` upserts, blank profile creation |
| `useAdminSettings` | `AdminToolsModal` | Global remote feature flags, `AppSettingsService` read/write |

---

### 📐 Shared Type Contract

All FSM states and shared interfaces live in **`src/types/dashboard.types.ts`**. Never re-declare these types in individual hooks or components.

| Type | Values |
| :--- | :--- |
| `ModeType` | `'FAVORITES' \| 'MULTIMODE' \| 'PROGRAMS' \| 'MUSIC' \| 'STREET' \| 'CAMERA'` |
| `FixedSubMode` | `'PATTERN' \| 'BUILDER'` |
| `MicSource` | `'APP' \| 'DEVICE'` |
| `MusicColorFocus` | `'PRIMARY' \| 'SECONDARY'` |
| `DeviceSettingsState` | FSM: `'IDLE' \| 'LOADING' \| 'READY' \| 'ERROR'` |
| `IDeviceConfigEntry` | `{ name, type, points, segments, sorting, stripType, groupId }` |

---

### Engineering Standards
- **UI Components**: Must focus strictly on rendering and presentation.
- **State Machines**: Complex multi-state logic must use `ModeType`/string-union FSMs, never boolean flag clusters.
- **Atomic Operations**: All hardware writes must be wrapped in `try/catch` and logged via `AppLogger`.
- **Database Telemetry Masking**: All non-critical DB telemetry inserts (e.g. `skate_sessions` or metrics) must be wrapped in `try/catch` blocks so they do NOT block the critical BLE execution pipeline on failure.
- **Type Safety per Schema constraints**: Tactical type casting using `as unknown as CustomType` or `as any` is authorized when bypassing strict, auto-generated Supabase overload methods for `Json` fields and unresolvable overloads, as long as runtime validation aligns with the hardened database schema.
- **Type Imports**: Always import `ModeType` and shared interfaces from `dashboard.types.ts`, not from hook files.
- **Hook Contracts**: Hooks receive BLE context via props, never via direct import of BLE libraries.

---

## 5. Database Schemas

### Supabase Architecture (Telemetry & Registration)
*Project ID:* `qefmeivpjyaukbwadgaz`

#### **`registered_devices`** (Hardened Schema)
| Column | Type | Purpose |
|:---|:---|:---|
| `id` | TEXT (PK) | Unique system identifier |
| `device_mac` | TEXT | Unique hardware address |
| `user_id` | UUID | Owner ID |
| `device_name` | TEXT | Custom alias |
| `product_type` | TEXT | HALOZ / SOULZ / RAILZ |
| `led_points` | INT | Physical pixel count |

#### **`product_catalog`** (Dynamic Hardware Definitions)
| Column | Type | Purpose |
|:---|:---|:---|
| `id` | TEXT (PK) | Unique product key |
| `display_name` | TEXT | Human-readable name |
| `viz_shape` | TEXT | RING / OVAL / DUAL_STRIP |
| `battery_capacity_mah`| INT | Rated capacity |

#### **`skate_spots`** (Map Grounding)
| Column | Type | Purpose |
|:---|:---|:---|
| `id` | UUID (PK) | Unique spot ID |
| `name` | TEXT | Rink/Park name |
| `lat` | DOUBLE | Latitude |
| `lng` | DOUBLE | Longitude |
| `surface_type` | TEXT | `wood` / `concrete` / `sport_court` |
| `is_indoor` | BOOLEAN | Indoor vs Outdoor |
| `source` | TEXT | `native_seed` / `osm` / `user_submit` |
| `is_verified` | BOOLEAN | Administrative verification status |

> [!NOTE]
> **Map Grounding Strategy**: 20 iconic US Roller Rinks were seeded on 2026-04-13 using the `native_seed` tag to ensure immediate platform value in US territories.

---

## 6. Crew Hub & Session Lifecycle

To ensure high-fidelity discovery and telemetry, the Crew Hub follows strict lifecycle and naming protocols.

### Session Naming Convention
Automatic `_MM/DD` suffix enforced in `CrewModal.handleCreate`.

### Proximity Discovery & Visibility Rules
Discovery radius filter governed by `LocationService.getNearbyPublicSessions(radiusMi)`.

### Global Session Lifecycle Cleanup
System-wide `cleanupExpiredSessions()` ends sessions older than 24h.

---

## 7. Session Telemetry Architecture

### Supabase Table: `skate_sessions`
| Column | Type | Notes |
|--------|------|-------|
| `duration_sec` | `int4` | Total session length |
| `distance_miles` | `float8` | Accumulated GPS distance |
| `avg_speed_mph` | `float8` | Mean speed |
| `peak_speed_mph` | `float8` | Maximum speed |
| `calories` | `int4` | Estimated via MET formula |

---

## 8. Agentic PM Protocols (The Brain)

This project is governed by a custom-built **Agentic OS**—a suite of 38 strict protocols located in `.agents/rules/`.

### Tier 1: Safety, Precision & Stealth
| Rule | Function |
|:---|:---|
| **Critical Safety & Quarantine** | Strict branching and rollback protocols. |
| **Surgical Strike Protocol** | Mandates micro-edits and precision coding. |
| **Absolute Truth** | Eliminates hallucinations via mandatory reference audits. |

### Tier 5: Debugging, Hygiene & Maintenance
| Rule | Function |
|:---|:---|
| **Emergency Debug Drill** | Enforces instrumentation over guess-fixing. |
| **Boy Scout (Tech Debt)** | Mandates one small cleanup in every modified file. |
| **Supabase Schema Sync** | Automatic type regeneration after DB changes. |

---

## 9. Sentinel Engineering Governance (Workflow V6)

The SK8Lytz lifecycle is governed by the **Sentinel Engine**.

### Priority Hierarchy
1. **CRITICAL**: Stability & Security.
2. **HIGH**: Engineering Excellence.
3. **MEDIUM**: Compliance & Governance.
4. **LOW**: New Features.

### Mandatory Safety Governors
- **Safe-Commit Anchors**: Git restore points for `[H-RISK]` tasks.
- **The Senior Auditor**: Mandatory self-review pass before commit.
- **Devil's Advocate Gate**: Pre-mortem required for `[Feast]` items.

## 10. Environment & Build Ops

### Android Build Pipeline
* **JAVA_HOME**: Must be set to `C:\Program Files\Android\Android Studio\jbr` (standard JBR included with Android Studio) to resolve Gradle/JDK compatibility issues for Android builds.
* **Build Type**: `release` (assembleRelease).
* **Output Artifact**: `android\app\build\outputs\apk\release\SK8Lytz.apk`

### Known Dev Environment Limitations
* **Supabase Auth (SignUp)**: Signup operations via `auth/v1/signup` may return `400 Bad Request` in local web/emulator environments due to strict redirect URI validation or rate limiting on development shards. Use "Continue Offline" or existing test credentials for UI/UX validation.

---
> [!IMPORTANT]
> To remain active, every rule file MUST contain the `trigger: always_on` YAML frontmatter.

# SK8Lytz App Master Reference

_Last Updated: 2026-06-10 | **21-Domain Cartographer Synthesis Completed** — watchOS + Wear OS companion apps, Expo native bridge module (sk8lytz-watch-bridge), watch-preferred health priority system, bidirectional phoneâ†”watch session sync, Speed push to watch, VS-002 gitignore fix. v3.9.1 | Source of Truth: artifacts/deepdive_docs/_

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
10. [Environment & Build Ops](#10-environment--build-ops)
11. [Wearable Companion Architecture](#11-wearable-companion-architecture)

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

- **Concept**: 56" of total illumination via four 14" diffused silicone addressable LED strips.
- **Performance**: 2-6+ hours of run time.
- **Charging**: 90 min full cycle (USB-C).
- **Control**: Integrated Bluetooth/RF + High-sensitivity integrated microphone for instant "vibe" reactivity.

#### **HALOZ** (The Compact Matrix Box)

- **Concept**: Individually controllable high-density pixel boxes for wheels/plates.
- **Performance**: 2-4+ hours of run time.
- **Charging**: 60 min fast-charge (USB-C).
- **Control**: Integrated Bluetooth/RF + High-sensitivity integrated microphone.

#### **RAILZ** (Integrated Chassis Strips)

- **Concept**: Dual parallel vertical LED strips designed for undercarriage/frame mounting.
- **Performance**: Integrated 4-6+ hour run time.
- **Charging**: 90 min (USB-C).
- **Control**: Integrated Bluetooth/RF + High-sensitivity integrated microphone.

### Hardware Truth Table — Confirmed 2026-04-22

> [!IMPORTANT]
> This is the **canonical source of truth** for all LED count math, pixel array sizing, and EEPROM provisioning. The three-layer model below governs ALL protocol and UI decisions. `ProductCatalog.ts` code comments cite this table. See `ZENGGE_PROTOCOL_BIBLE.md` §3 for `0x62`/`0x63` EEPROM command details.

#### The Three-Layer LED Model

Every product has three distinct LED "counts" that mean different things:

| Layer | Name | What it represents | Code field |
|:------|:-----|:-------------------|:-----------|
| **1** | `ledPoints` | Addressable LEDs **per segment** — the design canvas | `hwSettings.ledPoints` |
| **2** | `segments` | Number of hardware mirrors of Layer 1 | `hwSettings.segments` |
| **3** | Physical LEDs | Total real LEDs in the world (`ledPoints Ã— segments`, or Ã— wiring factor) | Not stored — derived only |

> **Golden Rule**: All pixel arrays (`0x59`, `0x31`) MUST be built using `ledPoints` (Layer 1). Segments and wiring are the hardware's job, not the app's.

#### Confirmed Product Defaults

| Product | `ledPoints` | `segments` | Physical LEDs | Adjustable? | Architecture |
|:--------|:-----------:|:----------:|:-------------:|:-----------:|:-------------|
| **HALOZ** | **8** | **2** | 16 | âŒ Fixed | Ring. Hardware **auto-mirrors** the 8-point pattern to a 2nd segment. Always send 8-element arrays. |
| **SOULZ** | **43** | **1** | 86* | âœ… Yes | Strip. No hardware mirroring. Controller drives one 43-point canvas. Physical doubling from Y-wire is transparent. |
| **RAILZ** | **30** | **2** | 60 | âœ… Yes | Dual rail. Placeholder — confirm with hardware before shipping. |

*SOULZ physical reality: 43 LEDs on LEFT skate (outside boot) + 43 LEDs on RIGHT skate (inside boot), both Y-wired to the same controller output. The controller is **oblivious to the doubling**.

#### SOULZ — User-Adjustable `ledPoints`

SOULZ strips are cut-to-length. If a user physically cuts the strip shorter, they **must** update `ledPoints` in the HW Setup Wizard to match the physical count. Example: cut from 43→36 → set `ledPoints=36`. The LED Points adjuster in the wizard (`hardwareAllowsCustomPoints: true`) exists for exactly this reason.

Every pixel array builder (`PatternEngine`, `applyEmergencyPattern`, etc.) must read `hwSettings.ledPoints` dynamically — NEVER hardcode 43.

#### âš ï¸ Previous Bug (Fixed 2026-04-22)

`ProductCatalog.ts` previously had `HALOZ.defaultLedPoints = 16, segments = 1`. This was **wrong** — it caused:
1. `applyEmergencyPattern` sending 16-element arrays to an 8-point device, bypassing the hardware segment mirror engine
2. Any EEPROM probe (`0x63`) returning `ledPoints=8` would have caused a mismatch with stored defaults

Fixed: `HALOZ.defaultLedPoints = 8, segments = 2`.

#### âœ… HALOZ Ring Topology — Confirmed Physical LED Map (2026-04-25)

```
              â•”â•â•â•â•â•â•â•â•â•â•â•—
              â•‘   TOP    â•‘
  L-pSlot 0 â•â•â•¬â•â•â•â•â•â•â•â•â•â•â•¬â•â• R-pSlot 7    â† Left TOP = pSlot 0, Right TOP = pSlot 7
  L-pSlot 1 â•â•â•¬          â•¬â•â• R-pSlot 6
  L-pSlot 2 â•â•â•¬          â•¬â•â• R-pSlot 5
  L-pSlot 3 â•â•â•¬  CENTER  â•¬â•â• R-pSlot 4
  L-pSlot 4 â•â•â•¬          â•¬â•â• R-pSlot 3
  L-pSlot 5 â•â•â•¬          â•¬â•â• R-pSlot 2
  L-pSlot 6 â•â•â•¬          â•¬â•â• R-pSlot 1
  L-pSlot 7 â•â•â•¬â•â•â•â•â•â•â•â•â•â•â•¬â•â• R-pSlot 0    â† Left BOTTOM = pSlot 7, Right BOTTOM = pSlot 0
              â•‘  BOTTOM  â•‘
              â•šâ•â•â•â•â•â•â•â•â•â•â•
  LEFT side: â†“ top→bottom     RIGHT side: â†‘ bottom→top
  pSlot: 0,1,2,3,4,5,6,7      pSlot: 0,1,2,3,4,5,6,7
```

**Rule:** Hardware auto-mirrors the 8-pixel pattern to both segments simultaneously.
- Seg 1 (RIGHT): LED 0 at physical BOTTOM, LED 7 at physical TOP.
- Seg 2 (LEFT): Hardware mirror places LED 0 at physical TOP, LED 7 at physical BOTTOM.
- If pixel[0] = RED → **Right BOTTOM = RED, Left TOP = RED**. True horseshoe symmetry.

#### VisualizerUnit Rendering Rules (HALOZ RING only)

These rules govern `src/components/VisualizerUnit.tsx`. **Do NOT apply to SOULZ (OVAL) or RAILZ (DUAL_STRIP).**

| Rule | Correct Value | Wrong (causes bugs) |
|:-----|:-------------|:--------------------|
| `numLeds` formula | `Math.floor(devicePoints)` — `ledPoints` IS the per-segment canvas | `Math.floor(devicePoints / deviceSegments)` — causes 4 LEDs, not 8 |
| `devicePoints` fallback | `productProfile.defaultLedPoints` (8) | `productProfile.vizDefaultPoints` (was 16) — causes 16-color arcs |
| `deviceSegments` fallback | `productProfile.defaultSegments` (2) | Hard-coded `1` — kills gap rendering |
| `getVisualizerFrame` numLeds arg | `numLeds` (8) | `activeSegmentLedsHoisted` (32) — 4Ã— oversampled palette |
| Product lookup guard | Guard `device.type !== 'undefined'` before `String()` | `String(undefined)` = `"undefined"` → SOULZ fallback → `vizShape='OVAL'` → RING inversion never fires |
| Left arc pSlot direction | `rawFract` (inverted for i â‰¥ renderLeds/2 when `vizShape==='RING'`) | `segmentI / activeSegmentLeds` (never inverted) → both arcs identical |

> **SOULZ Safety:** `rawFract` for SOULZ (`vizShape='OVAL'`) is NEVER inverted. Changing slot lookups to use `rawFract` instead of `segmentI/activeSegmentLeds` is identical for SOULZ — zero regression risk.



---

**Core Philosophies (The 4 Pillars):**

1. **Bulletproof BLE Transport:** The connection to Neogleamz hardware MUST be instantaneous and nearly sentient. Reconnects and pairing must handle GATT exceptions and MTU drift invisibly. "It just works, immediately."
2. **Tactile, Glanceable UI:** High-contrast, Neogleamz standard aesthetics. Massive touch targets (>44px) for skaters in gear. One-tap access to Symphony effects and App-mic visualization.
3. **No-Compromise Offline Flow:** Hardware control is a fundamental right. basic lighting and EEPROM configuration (0x62/0x63) never require cloud authentication.
4. **Wrist Extension (Watch Companions):** The watch is a session HUD and health relay — NOT an LED controller. It mirrors speed, HR, and calories from the phone, relays on-wrist health sensor data back, and provides remote session start/stop. All BLE LED protocol commands originate exclusively from the phone app.

**Anti-Goals (What we ruthlessly reject):**

- **Bloated Developer Logic in Prod:** We use strict `__DEV__` elimination to keep the binary lean and free of testing debris.
- **Complex UI Micro-Management:** Skaters want to skate. We provide stunning Pro Effects and high-precision HUDs (Speed/Brightness), not frame-by-frame animation editors.
- **Hardware-Cloud Gating:** We never lock essential local hardware features behind an internet authentication wall.
- **Hardcoded Hardware Heuristics:** The UI layer must NEVER use explicit string literals (e.g. `type === 'HALOZ'`) or hardcoded binary logic to render products. All hardware metadata (shape, icons, colors) must be dynamically derived from `LOCAL_PRODUCT_CATALOG` (`src/constants/ProductCatalog.ts`) to ensure scalable, zero-code support for new OEM devices.

### âŒ Condemned Opcodes — Never Use in Production

> [!CAUTION]
> The following BLE opcodes are PERMANENTLY CONDEMNED for production UI use.
> They cause a fundamental visualizer-parity gap: the hardware controls the animation internally,
> so the ProductVisualizer cannot know what the hardware is showing. This breaks our core parity promise.

| Opcode | Name | Why Condemned | What Replaced It |
|:-------|:-----|:--------------|:-----------------|
| **`0x41`** | Settled Mode (Symphony Effects) | Used for native hardware parity on test patterns. | 33 native hardware effects (IDs 201-233) fired via `0x41`, fully integrated into PatternEngine |
| **`0x42`** | RBM Programs Mode | Hardware runs one of 100 baked-in Programs internally. App cannot know the pixel state. | All Programs effects reimplemented as PatternEngine TypeScript, fired via `0x59` |

**Architecture decision 2026-04-22**: Every LED effect in SK8Lytz is computed in TypeScript,
sent as a pixel array via `0x59`, and rendered identically in the ProductVisualizer.
`0x41` and `0x42` are available in DiagnosticLab only (guarded by `__DEV__`).

---

### SK8Lytz Pattern Architecture (Canonical Reference)

#### The One Law

```
PatternEngine (TypeScript math) → getVisualizerFrame() → pixel array → 0x59
ProductVisualizer               → getVisualizerFrame() → same pixel array → rendered on screen
Visualizer = Skates. Always. No exceptions.
```

#### Three-Tier Pattern Library

Every pattern belongs to one of three tiers:

| Tier | Source | Count | Description |
|:-----|:-------|:-----:|:------------|
| **Tier 1** | ge.* Java class reversal | 33 | Settled Mode effects. `0x41` was originally reverse-engineered, but test patterns 201-233 now utilize native `0x41` hardware routing for byte parity checks. |
| **Tier 2** | Programs Mode reversal | ~28 | Standard LED strip effects. Each Programs effect is reimplemented in TypeScript. `0x42` is NEVER called. |
| **Tier 3** | SK8Lytz originals | âˆž | Effects only possible because we own the payload. Positional gradients, reactive splits, sport sequences, etc. |

**Current total**: 81 templates (43 spatial/temporal + 5 street + 33 Multimode Pro Effects), all in one unified picker.

#### Pattern Template Schema

Every pattern in `src/protocols/PatternEngine.ts` (`SK8LYTZ_TEMPLATES`) has this structure:

```typescript
interface SK8LytzTemplate {
  id: number;                          // Unique, never reuse. 1-28 = existing. 29+ = new.
  name: string;                        // User-facing name in picker
  icon: string;                        // Emoji icon for picker card
  colorMode: 'FG_BG' | 'FG_ONLY' | 'BG_ONLY' | 'GENERATIVE';  // Which color pickers to show
  supportsDirection: boolean;          // Show direction toggle in UI?
  tier: 1 | 2 | 3;                    // Source tier (ge.* | Programs | Original)
  sourceRef?: string;                  // e.g. 'ge.OceanWaveEffect' or 'Programs:CometChase'
  group?: string;                      // UI grouping label in picker
}
```

#### Universal Controls (All Patterns Support All)

| Control | Implementation | Notes |
|:--------|:---------------|:------|
| **FG Color** (RGB) | `fg: RGB` passed to `getVisualizerFrame()` | Always active |
| **BG Color** (RGB) | `bg: RGB` passed to `getVisualizerFrame()` | UI hidden if `colorMode !== 'FG_BG'` |
| **Speed** | Controls `tick` rate + `0x59` scroll param | Always active |
| **Brightness** | `0x55` packet — independent of pixel array | Always active, global |
| **Direction** | `direction: 0\|1` → `getVisualizerFrame()` + `0x59` dir byte | UI shown only if `supportsDirection: true` |

#### colorMode Gate

Controls which color pickers the UI renders for a given pattern:

- `FG_BG` — Both FG and BG pickers shown (e.g. Comet: FG=trail, BG=background color)
- `FG_ONLY` — Only FG shown (e.g. Breathing: single color fade, BG irrelevant)
- `BG_ONLY` — Only BG shown (e.g. ID 233 Rainbow Stream: hardware ignores FG entirely)
- `GENERATIVE` — Neither picker shown (e.g. Rainbow Flow: hue is computed by math, not user-set)

> Note: The pattern ALWAYS receives both `fg` and `bg` arguments — the gate is purely a UI affordance.

#### Implementation Contract for Every New Pattern

```
1. Read source math (ge.* Java class, or Bible §0x51 Pattern Index for test modes 201-233)
2. Write TypeScript math: add case to `src/protocols/SpatialEngine.ts` or `SymphonyEngine.ts`
3. Add case to `src/protocols/VisualizerEngine.ts` `getVisualizerFrame()`
4. Add entry to `src/protocols/PatternEngine.ts` `SK8LYTZ_TEMPLATES` with correct colorMode/tier/sourceRef
5. For test patterns (201-233): dispatch via `ZenggeProtocol.setCustomModeCompact()` — NOT `0x41`, NOT 10B extended
6. Verify: ProductVisualizer shows the effect â† identical to hardware via 0x59 (or 0x51 for test modes)
7. Hardware test on HALOZ: tap pattern → LED ring matches visualizer
```

---

## 2. System Architecture & Local Storage

### AsyncStorage Key Registry

| Key                                 | Owner                           | Contents                                                                                                                                        |
| :---------------------------------- | :------------------------------ | :---------------------------------------------------------------------------------------------------------------------------------------------- |
| `@sk8lytz_logs`                     | AppLogger                       | Compact telemetry event buffer array                                                                                                            |
| `@Sk8lytz_auth_username`            | DashboardScreen                 | Local cache of Supabase display_name for instant UI feedback. Synced via Reactive Context Pattern (Load Cache -> Hydrate Profile -> Update UI). |
| `@Sk8lytz_registered_devices`       | DeviceRepository                | Primary SSOT ledger of all claimed/bound hardware keyed by BLE MAC. Each entry uses `group_ids: string[]` and `group_names: string[]` (many-to-many migration 2026-05-28). Legacy scalar `group_id` is dead. |
| `@Sk8lytz_device_configs`           | useDashboardGroups / AppLogger  | Dict keyed by **BLE MAC** containing `{ name, type, points, segments, sorting, stripType, group_ids: string[], group_names: string[] }` |
| `@Sk8lytz_custom_groups`            | useDashboardGroups              | Array of `{ id, name, isGroup, deviceIds }` — group memberships (junction-table backed post v3.6.5) |
| `@sk8_hw_<deviceId>`                | Sk8LytzProgrammerModal          | Per-device EEPROM hardware settings cache                                                                                                       |
| `@sk8lytz_theme`                    | ThemeContext                    | `dark` or `light`                                                                                                                               |
| `@sk8lytz_control_theme`            | ThemeContext                    | Control color theme name                                                                                                                        |
| `@Sk8lytz_hardware_blacklist`       | useBLE                          | Cache-first offline ledger of MAC addresses banned from connecting                                                                              |
| `@Sk8lytz_Builder_Presets`          | GradientsService                | Cache-first offline storage of custom and global builder presets                                                                                |
| `@Sk8lytz_Scenes`                   | ScenesService                   | Cache-first offline storage of downloaded and authored multi-step scenes                                                                        |
| `@Sk8lytz_Scene_Sync_Queue`         | ScenesService                   | Offline mutation queue for publishing and deleting scenes in the background                                                                     |
| `@Sk8lytz_skate_spots_cache`        | LocationService                 | 24h TTL cache-first storage of 500 closest skate spots for offline map degradation survival                                                     |
| `@Sk8lytz_Favorites`                | useFavorites                    | Dictionary of user-defined lighting presets (Name, Palette, Mode)                                                                               |
| `@sk8lytz_permissions_optout`       | PermissionService               | App-Level Opt-Out Ledger. User toggles that override OS permissions for legal/privacy reasons.                                                  |
| `@Sk8lytz_voice_tutorial_dismissed` | boolean                         | Gating for the Voice Command onboarding modal                                                                                                   |
| `@sk8lytz_app_settings`             | AppSettingsService / useBLEScanner | App-wide admin feature flags. Cache-first layer provides offline access. Key `hw_setup_rssi_threshold` controls the RSSI gate. |
| `@Sk8lytz_remember_creds`           | AuthFormSignIn / DevSandboxDrawer | Remembers login email and checkbox status if "Remember Me" is enabled                                                                           |
| `@Sk8lytz_demo_mode`               | DevSandboxDrawer / useBLE       | Flag controlling whether "Virtual Skates" demo mode is active for sandbox testing                                                               |
| `@Sk8lytz_auth_last_email`          | AuthScreen / DevSandboxDrawer   | Caches the last logged-in email to pre-populate the login screen on next launch                                                                 |
| `@Sk8lytz_offline_skip`            | AuthFooterActions / AuthFormSignIn | Stores whether the user opted to bypass online login and continue using the app in offline mode                                                |
| `@Sk8lytz_demo_halo`                | DevSandboxDrawer / useBLE       | Cached MAC address of virtual HALOZ device in offline/demo mode                                                                                 |
| `@Sk8lytz_demo_soul`                | DevSandboxDrawer / useBLE       | Cached MAC address of virtual SOULZ device in offline/demo mode                                                                                 |
| `@SK8Lytz_PublicScenes_Cache`       | ScenesService                   | Local cache of public patterns/scenes downloaded from Supabase                                                                                  |
| `@SK8Lytz_PendingSession_Queue`      | SpeedTrackingService            | Queue of offline-saved skate session snapshots awaiting database synchronization                                                                 |
| `@SK8Lytz_notif_prefs`               | AppSettingsService              | User preferences for push notification channels (Crew alerts, system, etc.)                                                                     |
| `@Sk8lytz_auto_pause_enabled`        | SpeedTrackingService            | Flag indicating whether telemetry tracking auto-pauses when skater speed drops to zero                                                          |
| `@Sk8lytz_programmer_profiles`       | Sk8LytzProgrammerModal          | Locally saved profiles and config files for hardware programming tab                                                                           |
| `@Sk8lytz_product_catalog`           | AppSettingsService              | Cached local catalog of Neogleamz product models and features                                                                                  |
| `@Sk8lytz_pending_bg_end`            | BackgroundSessionService        | Timestamp/flag for session telemetry ending in background                                                                                       |
| `@Sk8lytz_last_group_patterns`       | useDashboardGroups              | Dictionary mapping active groups to their last selected lighting patterns for state restoration                                                 |
| `@Sk8lytz_scanner_telemetry_queue`   | TelemetryService                | Local buffer of bluetooth scanner telemetry data waiting for background upload                                                                  |
| `@Sk8lytz_groups_migrated_v2`        | MigrationService                | Boolean flag indicating successful migration of device groups layout to many-to-many model                                                      |
| `@Sk8lytz_app_settings_logger`       | AppLogger                       | Local configurations and levels for the internal telemetry event logger                                                                         |
| `@Sk8lytz_auth_migration_v1`         | MigrationService                | Flag indicating successful migration of user credentials to v1 schema                                                                           |
| `supabase.auth.token`                | Supabase / AuthContext          | Persisted JWT authentication token for Supabase client sessions                                                                                 |
| `@Sk8lytz_offline_eula_accepted`     | ComplianceGate                  | Boolean flag indicating whether the user accepted the offline-mode EULA                                                                         |

> [!CAUTION]
> **PURGED KEYS (2026-04-17):** The following legacy `ng_*` keys are fully deprecated and MUST NOT be used anywhere in the codebase. They caused split-brain bugs due to namespace drift:
> - ~~`ng_device_configs`~~ → migrated to `@Sk8lytz_device_configs`
> - ~~`ng_custom_groups`~~ → migrated to `@Sk8lytz_custom_groups`
> - ~~`ng_processed_devices`~~ → DELETED (one-shot cleanup on boot)

## Build Config & Troubleshooting ðŸ› ï¸

### Android Build Requirements

To resolve dependency conflicts and legacy library issues, the following configurations are required:

- **Jetifier**: Must be enabled (`android.enableJetifier=true`) to migrate legacy Support libraries to AndroidX.
- **SDK Versions**: Project currently targets SDK 34 (`compileSdk`, `targetSdk`).

### Third-Party Library Patches

- **@react-native-voice/voice**: ~~REMOVED~~ — The voice command engine was deleted. Do not reinstall this dependency. Any references to it in legacy build configs are dead code.

### Dashboard UI Layout (4-Slab Architecture) [MOVE_TO_ARCHIVE]

The primary dashboard uses a **Vertical Slab (No-Scroll)** layout to maximize glanceability and touch accuracy.

1. **Slab 1: Dynamic Header**: Logo, user profile, and active polling/telemetry indicator.
2. **Slab 2: Crew Hub**: Active session discovery and quick-join pills.
3. **Slab 3: My Skates / Groups**: High-impact cards for grouped hardware with global power controls.
4. **Slab 4: Hardware Fleet**: List of all registered devices with a "TAP TO ADD" quick-access wizard link.

### UI Design Patterns & Branding

- **Tucked-in Attribution**: Credit links (e.g., "by neogleamz.com") must be placed discreetly within header containers, aligned with the visual boundary of the primary logo (e.g., `marginRight: '16%'` for a 300px logo) and using `fontSize: 9` with `fontWeight: '800'` muted text.
- **Fluid Component Scaling**: Components (Builders, Camera Viewers) must NOT use hardcoded heights. They must utilize available `flex` space between the `ProductVisualizer` and the bottom dock to ensure responsiveness across all aspect ratios.
- **One-Screen Setup Policy** [MOVE_TO_ARCHIVE]: The Hardware Setup Wizard must minimize vertical occupancy. For naming and registration (Step 3), all primary controls (Fleet Name, Device Labels, Type, Position) must be visible on a single standard mobile viewport (e.g. iPhone SE) without requiring a vertical scroll for a standard 2-skate setup. Use horizontal inlining and 8pt grid proximity instead of explicit labels where possible.

### Admin Tools Hub (The Command Center)

The **Admin Tools Hub** (`AdminToolsModal`) is the unified gateway for all system-level diagnostics and hardware maintenance.

- **Access**: 10-tap the SK8Lytz logo in the dashboard header + Passcode: `0000`.
- **Architecture (Refactor/2026-04-12)**: To prevent "re-render storms" from high-frequency telemetry, the modal utilizes a **Memoized Tab Architecture**. Rendering logic for Timeline, Stats, Device, and Tools is extracted into standalone `React.memo` sub-components, ensuring UI stability during 20Hz notification bursts.
- **Tab 1: TIMELINE**: Virtualized system event log (BLE protocol, app lifecycle, errors). Filtered to exclude RAW_PAYLOAD by default to preserve list performance.
- **Tab 2: STATS**: Session analytics, mode usage frequency, and hardware performance metrics.
- **Tab 3: DEVICE**: Deep-dive hardware view showing all discovered peripherals and their cached configs.
- **Tab 4: TOOLS**: Administrative portal for low-level components:
  - **Catalog Manager**: Unified editor for product profiles. **MANDATORY**: All write operations (`upsert`) are gated by a Supabase Session check to prevent unauthorized database manipulation.
  - **LED Diagnostic Lab**: Atomic protocol validation and DIY payload building.
  - **Firmware Programmer**: Low-level hardware updates and serial-over-BLE tools.
  - **Optical Simulation Mode (Web Fallback)**: A dedicated developer interface for non-native environments (Expo Web). It provides manual telemetry simulation (randomized hex dispatch) to smoke-test visualizer and state-management pipelines without physical hardware.
- **Persistence & Governance**:
  - App settings (feature flags) are persisted via `AppSettingsService` with atomic rollback on failure.
  - Product Manager upserts are strictly typed to enforce the `batteryCapacityMilliAmpereHour` field, preventing database record drift.
  - **Account Deletion (Danger Zone)**: Implements a destructive `delete_account` RPC on Supabase. This uses `ON DELETE CASCADE` to completely shred user data across all tables (telemetry, profiles, and auth).
- **Navigation Orchestration**: Closing any administrative sub-tool (Lab, Programmer) must explicitly re-trigger the visibility of the `AdminToolsModal` in the parent `DashboardScreen` to ensure a consistent "nested" navigation experience.

### Optimistic BLE Write Pipeline ("The Ghost Standard")

The BLE write path uses an **Optimistic UI** architecture to eliminate perceived 80—500ms hardware latency:

| Phase             | Status FSM                   | Behavior                                                  |
| :---------------- | :--------------------------- | :-------------------------------------------------------- |
| 1. **OPTIMISTIC** | `onOptimistic()` fires       | UI updates INSTANTLY before BLE write                     |
| 2. **PENDING**    | `writeStatus = 'PENDING'`    | BLE command dispatched (40ms debounce)                    |
| 3. **CONFIRMED**  | `writeStatus = 'CONFIRMED'`  | Hardware ACK'd — light haptic                             |
| 4. **RECONCILED** | `writeStatus = 'RECONCILED'` | Hardware FAILED — error haptic + `onReconcile()` rollback |

**Key Files:**

- `src/hooks/useOptimisticBLE.ts` — Ghost state FSM, debounce, haptics
- `src/hooks/useBLE.ts` — Core write function (`writeToDevice` returns `Promise<boolean | 'partial'>`)
- `src/components/DockedController.tsx` — Consumer integration (status indicator dot)

**Architectural Constraint:** `writeToDevice` MUST return `Promise<boolean | 'partial'>` where:
- `true` = all devices received the payload
- `false` = write failed, trigger reconciliation
- `'partial'` = some devices received it (ghosted devices skipped) — treated as success for UI

All component prop interfaces must use `Promise<void | boolean | 'partial'>` for full compatibility.

### Test Users & Environments

For testing App Sync behavior vs. Offline mode offline fallbacks, you can authenticate using the primary test user:

- **Email**: `testuser@sk8lytz.com`
- **Password**: `Password!2026`
- **Username**: `TestSkater`

### Offline & Guest Gating Architecture

The application enforces a strict "Hardware First, Cloud Second" policy. Core hardware control (BLE opcodes) is NEVER gated behind an authentication wall.

- **Offline Mode State**: Propagated dynamically via the `isOfflineMode` prop in the component tree (`DashboardScreen` → `DockedController` → Modals).
- **Graceful Degradation**: 
  - `QuickPresetModal`: Cloud preset saving is hidden when `isOfflineMode === true`. Only local device EEPROM saves are permitted.
  - `CommunityModal`: The 'Community Profiles' tab is entirely disabled in offline mode. The UI defaults gracefully to the 'My Skates' local tab.
- **Rule of Thumb**: Local SQLite (`AsyncStorage`) and direct GATT manipulation are 100% available to Guests. Any feature requiring Supabase REST/PostgREST must explicitly check `isOfflineMode` and display a friendly 'Login Required' state.

### Camera Mode: Camera Vibe Catcher v2 Architecture

The `CAMERA` mode provides real-time ambient lighting translation and dual-mode color analysis.
- **Unified Cross-Platform Frame Processing**: 
  - Uses a unified cross-platform `CameraTracker.tsx` utilizing `react-native-vision-camera` v5's GPU-backed `useFrameOutput` pipeline and `vision-camera-resize-plugin`.
  - Frames are downscaled on the GPU to 50x50 pixels RGB format at 5Hz (200ms throttle interval) with JSI `'worklet';` execution.
  - Hardened with explicit `frame.dispose()` invocation wrapped in a `try...finally` block inside the worklet thread to eliminate camera pipeline stalls. Dispatches are scheduled via `runOnJS` from `react-native-worklets` to transition back to the React JS thread.
- **SNIPER Sub-Mode (Focus reticle)**:
  - Samples the center pixel `(25, 25)` from the 50x50 resized frame.
  - The GPU resizer (`react-native-vision-camera-resizer@5.0.10`) delivers accurate RGB bytes directly. The reticle displays the raw camera color (unmodified truth). No client-side vivid boost is applied in the frame processor — the captured color is the real scene color.
  - On capture, `boostForLED()` (`src/utils/ColorUtils.ts`) applies industry-standard HSV saturation maximization (S=1.0, V=1.0) to translate the muted camera capture into vivid WS2812B-optimized output. Neutrals (HSV S < 0.05) pass through as white. The boosted color is dispatched via 0x59 Freeze to the skates and saved in the 5-item swatch history.
- **VIBE Sub-Mode (Palette extractor)**:
  - Evaluates the 2,500 pixel array to extract the 3 most dominant colors via an optimized client-side K-Means clustering algorithm (k=3, 5 iterations max) with thread-safe `'worklet';` annotations.
  - Dominant colors populate FG/BG/ACCENT slots in the UI and generate a live liquid gradient preview.
  - Tapping Apply auto-generates a `BuilderNode[]` array, maps them to a linear gradient matching the user's Flow/Static preference, and dispatches via `0x59`.
- **Surgical Buffer Overflow Defense**:
  - Enforces a minimum canvas length of 12 RGB pixels for all `0x59` spatial payload dispatches by interpolating dominant swatches to prevent physical controller EEPROM buffer lockouts on the `0xA3` chipset.

---

## 3. BLE Protocol Library

> [!IMPORTANT]
> **Dynamic Catalog Migration (2026-04-11)**: All hardware profile logic—including default LED counts, visualization themes, and discovery categorization—is now handled strictly via `LOCAL_PRODUCT_CATALOG` (`src/constants/ProductCatalog.ts`).

All byte definitions below represent the inner payload _before_ the V2 BLE packet wrapper is applied.

### Confirmed Hardware Identity (APK-Verified 2026-04-21)

> [!IMPORTANT]
> All 3 physical SK8Lytz devices confirmed as **`Ctrl_Mini_RGB_Symphony_new_0xA3`** (product_id: **163 = 0xA3**). Confirmed from `discovered_devices_telemetry` across MACs `08:65:F0:9A:C2:3C`, `08:65:F0:9A:5E:06`, `08:65:F0:5F:03:B1`. Firmware: v45—46, BLE: 5, LED version: 3.
>
> **Key implications of 0xA3 vs 0xA2:**
> - `0x59` Static Colorful tab **IS available** on 0xA3 (not available on 0xA2) âœ…
> - `0x51` Custom Scene — **9B compact format (291B) WORKS** on 0xA3 via our standard `wrapCommand` âœ…
> - `0x51` 10B extended format (323B) does NOT work via our wrapper — requires ZENGGE chunked framing header (see Protocol Bible Section 11)
> - `0x42` effect ceiling: **1—100** (same as 0xA2). Effect 101 plays an undocumented effect (ceiling is soft).
> - `0x43` Multi-Sequence: **DO NOT USE** — Oracle test caused hardware LED shutoff (state machine crash). ZENGGE app uses `0x51` for multi-step effects, not `0x43`.
> - `0x41` Settled Mode: **DO NOT USE for IDs 201-233.** `0x41` and `0x51` share the same effectId range (1-33) but are different hardware engines producing different visuals. Using `0x41` for test patterns destroys parity. It is available in DiagnosticLab only. See Protocol Bible §0x41 and the AGENT SENTINEL warning in §0x51 Pattern Index.
> - Source: Oracle Lab + live BLE HCI sniff (2026-04-22), `ZENGGE_PROTOCOL_BIBLE.md` Section 11

### BLE Connection Handshake (2026-04-22)

Every GATT connection fires this sequence before the device is added to React state:

1. **MTU Negotiation** — `requestMTUForDevice(conn.id, 512)`
2. **0x10 Session Time Sync** — `ZenggeProtocol.setSessionTime()` → written directly to `ZENGGE_CHARACTERISTIC_UUID`. Format: `[0x10, year-2000, month(1-12), day, hour, min, sec, weekday(0=Sun), checksum]`. Source: `TimeControllerFragment.java` APK decompile. Non-fatal — wrapped in try/catch.
3. **React state update** — `setConnectedDevices()` fires _after_ GATT is booted to prevent UI from blasting payloads during MTU queries.

<!-- AST_COMPILER_START: ZENGGE_CONSTANTS -->
#### 📝 Auto-Compiled Zengge Protocol Constants (AST Compiler)

##### 🔌 BLE UUIDs
- **Service UUID**: `0000ffff-0000-1000-8000-00805f9b34fb` (`ZENGGE_SERVICE_UUID`)
- **Write Characteristic UUID**: `0000ff01-0000-1000-8000-00805f9b34fb` (`ZENGGE_CHARACTERISTIC_UUID`)
- **Notification Characteristic UUID**: `0000ff02-0000-1000-8000-00805f9b34fb` (`ZENGGE_NOTIFY_UUID`)

##### 🛠️ Hardware Constraints
| Constraint | Value | Description |
|:---|:---:|:---|
| `maxPoints` | 300 | Maximum addressable points per segment |
| `maxSegments` | 2048 | Maximum physical segment duplicates |
| `maxPxS` | 2048 | Max points * segments limit |
| `maxMicPoints` | 150 | Maximum points when microphone is active |
| `maxMicPxS` | 960 | Max micPoints * micSegments limit |
| `defaultPoints` | 30 | Fallback default point count |
| `defaultSegments` | 10 | Fallback default segment count |

##### 📟 IC Chip Types (`IC_TYPES`)
| Key | Chip Type |
|:---:|:---|
| 1 | WS2812B |
| 2 | SM16703 |
| 3 | SM16704 |
| 4 | WS2811 |
| 5 | UCS1903 |
| 6 | SK6812 |
| 7 | SK6812RGBW |
| 8 | INK1003 |
| 9 | UCS2904B |
| 10 | JY1903 |
| 11 | WS2812E |

##### 🎨 Color Sorting RGB (`COLOR_SORTING_RGB`)
| Key | RGB Order |
|:---:|:---|
| 0 | RGB |
| 1 | RBG |
| 2 | GRB |
| 3 | GBR |
| 4 | BRG |
| 5 | BGR |

<!-- AST_COMPILER_END: ZENGGE_CONSTANTS -->

### writeChunked — 0x51 Extended Payload Framing [MOVE_TO_ARCHIVE]

Required for 323-byte 0x51 Extended Scene Builder payloads (32 steps × 10B + 3B header).

- **Function**: `useBLE.writeChunked(payload: number[], chunkSize = 20): Promise<void>`
- **Framing**: `[0x40, seqByte, 0x00, 0x00, 0x01, 0x43, 0xBD, 0x0B, ...data]`
- **12 bytes data per 20-byte BLE chunk** (8-byte header overhead)
- **20ms inter-chunk delay** — prevents BLE TX buffer overflow on Android
- **⚠️ Framing signature `[0x01, 0x43, 0xBD, 0x0B]` needs Oracle Lab HCI sniff** before wiring to production Scene Builder UI
- Exported in `BluetoothLowEnergyApi` interface (commit `fdc0ff3`)

### BLE Stability Constraints & GATT Error Prevention

> [!CAUTION]
> React Native BLE PLX and the Android native `BluetoothAdapter` suffer from extreme race conditions. To avoid GATT 133 exceptions, UI freezes, and buffer overflows, all logic must follow these architectural constraints:

1. **Global BLE State Machine (`BleMachine.ts` — XState v5):** `src/services/ble/BleMachine.ts` owns all radio state via 6 XState states: `IDLE → SCANNING → CONNECTING → READY → RECOVERING → DISCONNECTING`. The machine is the ONLY entity that calls `startDeviceScan`/`stopDeviceScan`. Calling the radio directly from any hook or service is FORBIDDEN — use `bleSend({ type: 'SCAN_START' })` instead.
2. **Write Serialization (`BleWriteQueue.ts`):** All BLE GATT writes are serialized through a priority FIFO queue. Priority tiers: `critical` (0xCC power, 0x71, 0x63 heartbeat), `normal` (default pattern writes), `bulk` (0x51 scene uploads). MAX_QUEUE_DEPTH=8 with backpressure. Stale-write pruning via generation counter. One write at a time — Android BLE stack hard constraint.
3. **The GATT 133 Exponential Backoff:** `ConnectService.ts` wraps `connectToDevice` in a 3-attempt retry loop with exponential delays `[500ms, 1500ms, 4000ms]` + `refreshGatt: 'OnConnected'` on each retry to silently absorb Android RF congestion.
4. **Connection Priority Downgrade after Handshake:** On Android, `requestConnectionPriority(HIGH)` fires immediately on connect for fast MTU/handshake. After the first successful write, priority is downgraded to `BALANCED` — saves 2—3× battery on fire-and-forget traffic.
5. **Machine Gate Check:** The XState machine's `CONNECTING` guard checks current state before invoking `connectService`. Concurrent connect attempts are structurally impossible — the machine only enters `CONNECTING` from `IDLE` or `SCANNING`.
6. **Lean Connection Loops:** `ConnectService.ts` strictly establishes MTU (request 512 bytes) and notification pipes only. EEPROM hardware probes (0x63) run through `InterrogatorService.ts` independently after connect.
7. **50ms Inter-Device Write Gap:** All multi-device group writes in `BleWriteDispatcher` enforce a 50ms pause between per-device GATT writes. Prevents silent GATT drops on Qualcomm Snapdragon 665/675 and MediaTek Helio chipsets.
8. **clearWriteQueue on Recovery Start:** `RecoveryService.ts` calls `clearWriteQueue()` as its FIRST action before any GATT reconnect attempt. Purges pre-disconnect stale pattern writes that would compete with recovery pings.
9. **Parallel Writes and Teardowns (`Promise.all`):** Group-wide commands (sliders) and teardowns (`cancelDeviceConnection`) MUST be wrapped in `Promise.all` loops to eliminate staggered latency.

### The Transport Wrapper (`wrapCommand`)

Every inner protocol payload must be wrapped using the standard 8-byte Zengge V2 framing:
`[0x00, SequenceNum, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]`

### Auto-Recovery System (XState RecoveryService — 3-Phase)

_Migrated to XState: 2026-06-10 | Lives in: `src/services/ble/RecoveryService.ts` (invoked by `BleMachine.ts` RECOVERING state)_

The **RecoveryService** is a `fromCallback` XState actor invoked when the machine enters `RECOVERING`. It owns the full 3-phase recovery loop. The machine entering `RECOVERING` is the ONLY path into recovery — there is no external hook or ref that can start recovery. This makes concurrent recovery + connect structurally impossible.

**Organic Disconnect Trigger:**
- `ConnectService.ts` registers `bleManager.onDeviceDisconnected` for each connected device
- On organic drop, TWO callbacks fire: `handleOrganicDisconnect(error, deviceId)` (logging/telemetry only) and `onOrganicDisconnect(deviceId)` (sends `RECOVERY_START` to machine)
- `useBLE.ts` wires `onOrganicDisconnect` → `bleSend({ type: 'RECOVERY_START', ghostedMacs: [deviceId] })` with a guard that suppresses it during intentional `DISCONNECTING` state
- **CRITICAL:** Do NOT merge `handleOrganicDisconnect` and `onOrganicDisconnect` — they serve different purposes. Removing `onOrganicDisconnect` silently kills recovery.

#### 3-Phase Recovery Architecture

| Phase | Name | Duration | Backoff | Behavior |
| :--- | :--- | :--- | :--- | :--- |
| **Phase 1** | Aggressive | 0—2 min | `1500ms × 1.5^attempt` + jitter(0—1500ms), capped 30s | Rapid reconnect attempts via `createGattSession` |
| **Phase 2** | Moderate | 2—10 min | Same formula, longer natural gaps | Reduced frequency. Device may be out of range temporarily. |
| **Phase 3** | Passive | 10 min+ | **No active polling** | Zero-cost sweeper watch mode. If device reappears in scan results, `RECOVERY_START` re-fires from Phase 1. |

#### Recovery Properties

| Property | Value |
| :--- | :--- |
| **Trigger** | `RECOVERY_START` XState event (fired by `onOrganicDisconnect` callback or `HEARTBEAT_FAIL` event) |
| **First action** | `clearWriteQueue()` — purges stale pre-disconnect writes before any GATT attempt |
| **On success** | `sendBack({ type: 'RECOVERY_COMPLETE' })` — machine transitions to `READY`, adapter re-mapped, notifications re-registered |
| **On exhaustion** | `sendBack({ type: 'RECOVERY_FAIL' })` — machine transitions to `IDLE`, device ghosted in UI |
| **Cancellation** | Returning from the `fromCallback` cleanup function cancels the loop instantly |
| **Ghosting** | `ghostedDeviceIds` context updated by machine on `RECOVERY_FAIL` — UI dims card |

**Telemetry Events:**
- `AUTO_RECOVERY_STARTED`, `AUTO_RECOVERY_SUCCESS`, `AUTO_RECOVERY_FAILED`, `AUTO_RECOVERY_CANCELLED`, `AUTO_RECOVERY_SUMMARY`

> [!NOTE]
> **History:** Legacy `useBLEAutoRecovery.ts` hook DELETED in Phase 4 (2026-06-10). The hook owned recovery logic outside XState, making concurrent recovery+connect possible via race. `RecoveryService.ts` invoked as an XState actor makes this structurally impossible. Legacy `useBLEWatchdog.ts` was deleted 2026-04-17.

### Connection Health Heartbeat

_Migrated to XState: 2026-06-10 | Lives in: `src/services/ble/HeartbeatService.ts` (invoked by `BleMachine.ts` READY state)_

The **HeartbeatService** is a `fromCallback` XState actor invoked when the machine enters `READY`. Pings every connected device every 45s via a 0x63 EEPROM query to detect stale GATT handles early. Samsung Galaxy A-series can hold stale handles alive for minutes after the physical device powers off — without heartbeat, the stale link is only discovered on the next user write.

| Property | Value |
| :--- | :--- |
| **Interval** | 45s (`HEARTBEAT_INTERVAL_MS`) |
| **Probe** | `0x63` hardware query via `enqueueWrite('critical', ...)` — inner bytes: `[0x63, 0x12, 0x21, 0x0F, checksum]` |
| **Fallback** | If no adapter in `adapterMap` (BanlanX), falls back to `bleManager.readRSSIForDevice(mac)` directly |
| **On failure** | `sendBack({ type: 'HEARTBEAT_FAIL', deviceId: mac })` — machine transitions to `RECOVERING` |
| **On failure cleanup** | `bleManager.cancelDeviceConnection(mac)` called before sending HEARTBEAT_FAIL |
| **Cleanup** | Returned cleanup function calls `clearInterval` — timer stops when machine exits READY |

> [!NOTE]
> **History:** Legacy `useBLEHeartbeat.ts` hook DELETED in Phase 5 (2026-06-10). The hook owned heartbeat logic outside XState, requiring manual lifecycle management in `useBLE.ts`. `HeartbeatService.ts` as an XState actor ties the heartbeat lifetime to the READY state — it starts and stops automatically with the machine.

### Post-Connect RSSI Monitor

_Added: 2026-06-06 | Lives in: `src/hooks/ble/useBLERSSIMonitor.ts`_

Polls `readRSSIForDevice` every 30s on all connected devices. Surfaces live signal strength as `rssiMap: Record<string, number>` keyed by device MAC.

| Property | Value |
| :--- | :--- |
| **Interval** | 30s (`RSSI_POLL_INTERVAL_MS`) |
| **Weak threshold** | -75 dBm (`RSSI_WEAK_THRESHOLD`) — UI badge turns orange |
| **Critical threshold** | -82 dBm (`RSSI_CRITICAL_THRESHOLD`) — triggers proactive reconnect |
| **Proactive reconnect** | Calls `autoRecovery.initiateRecovery(mac)` if device not already in `ghostedDeviceIds` — forces GATT tear-down + fresh reconnect, which often picks a better radio channel |
| **UI integration** | `rssiMap[mac]` injected into `mergedItem.rssi` in `DashboardScreen.renderItem` — existing wifi icon auto-updates to reflect live post-connect signal quality |
| **Badge component** | `ConnectionStrengthBadge` — 3-bar signal icon using pure View rectangles (no SVG). 4-tier colour: green (≥-60), amber (-60 to -75), orange (-75 to -82), red (<-82). Hidden when rssi is null. |
| **Testability** | `readDeviceRSSI()` exported as pure async fn — 9 unit tests |

### Auto-Connect Observer (Debounced)

_Lives in: `src/hooks/useDashboardAutoConnect.ts`_

The dashboard auto-connect observer watches `allDevices` for registered peripherals that appear during passive scanning. It is hardened with:
- **500ms debounce** — batches devices discovered within 500ms into a single `connectToDevices` call
- **Gate check** — skips connection when `bleGateRef ≠ IDLE`
- **Pre-lock gate check** — checks gate state _before_ entering the 8s GATT lock poll (RC-04)
- **Ref-forwarded closures** — `connectToDevices` and `scanForPeripherals` are captured via stable refs to eliminate stale closure bugs on re-render (RC-02)
- **Prevents stampeding herd** — no concurrent auto-connect attempts
- **Sweeper-aware** — when Sweeper is active, routes through `burstScan()` instead of `startDeviceScan()` to avoid dual scan conflicts
- **Group-IDs array aware (2026-05-29)**: The offline fallback `processLocalDevices()` iterates `d.group_ids` (array, post-migration) with a scalar `d.group_id` fallback for legacy persisted rows. **Never assume `d.group_id` is populated on newly-registered devices.**

### RSSI Proximity Gating (Setup Wizard)

_Lives in: `src/hooks/ble/useBLEScanner.ts`_

To prevent skatepark BLE noise from hijacking the Setup Wizard, the scanner enforces a two-tier RSSI gate:
- **Registered devices** (already in fleet): -80 dBm hardcoded threshold — passes through as long as device is physically nearby
- **Unregistered devices** (not yet claimed): `hw_setup_rssi_threshold` from `@sk8lytz_app_settings` (default -70 dBm) — tunable via Admin → App Manager → Hardware section
- Threshold is loaded **once on scanner mount** from AsyncStorage. Changing it mid-session requires app restart to take effect.
- Admin stepper control: `ControlsRegistry.ts` key `hw_setup_rssi_threshold`, type `number_stepper`, range -100 to -30 dBm, step 1.

### iOS Platform Guards

_Added: 2026-06-05 (iOS-01, iOS-03)_

| Guard | File | Fix |
| :--- | :--- | :--- |
| **MTU Platform Guard** | `src/services/ble/ConnectService.ts` | `requestMTU()` wrapped in `Platform.OS === 'android'` block — iOS negotiates MTU automatically during GATT connection. Calling `requestMTU` on iOS throws. On iOS, `conn.mtu` is read directly. |
| **UUID Filter in `startDeviceScan`** | `src/services/ble/BleMachine.ts` | `startDeviceScan([ZENGGE_SERVICE_UUID, BANLANX_SERVICE_UUID], ...)`. Enables iOS background scanning. BleMachine is the ONLY place `startDeviceScan` is called. |

### Android Platform Guards

_Added: 2026-06-05 (AND-02, AND-03, AND-04)_

| Guard | File | Fix |
| :--- | :--- | :--- |
| **Scan Budget Guard** | `useBLEBatterySweep.ts/useBLEInterrogator.ts` | Tracks `startDeviceScan` calls against Android 12+'s 4-per-30s budget. If exhausted, defers the scan start until the budget window resets. Prevents silent throttling where Android OS stops delivering scan results with zero error feedback. |

### Battery-Adaptive Sweeper (The Silent Sweeper)

_Added: 2026-06-05 | Lives in: `src/hooks/ble/useBLEBatterySweep.ts/useBLEInterrogator.ts` (BAT-01)_

The Silent Sweeper is a persistent background LowPower BLE scan that runs after dashboard mount. It handles:
1. **Background device discovery** — no manual scan button needed
2. **Interrogator Queue** — queues EEPROM probes (0x63) for newly-discovered devices, populates `hwCache`
3. **Battery-adaptive throttling** — 3-tier system adjusts scan intensity based on phone battery level

| Tier | Battery Level | Scan Interval | Behavior |
| :--- | :--- | :--- | :--- |
| **Normal** | â‰¥30% | Continuous LowPower | Full scan, all features active |
| **Conservative** | 15—30% | Reduced frequency | Longer gaps between scan windows |
| **Critical** | <15% | Minimal scanning | Sweeper pauses non-essential scans, only responds to burst requests |

- **burstScan(durationMs)**: Elevates to `LowLatency` for a timed burst (default 5s), then reverts to `LowPower`. Used by the Wizard and auto-connect observer instead of calling `startDeviceScan` directly. Prevents dual-scan conflicts.
- **`hwCache: Record<string, any>`**: In-memory EEPROM settings cache keyed by uppercase MAC. Populated by Interrogator Queue. Consumed by `useBLEScanner` and `DashboardScreen`.
- Sweeper is paused during `AppState.background` and resumed on foreground via `startSweeper()`/`stopSweeper()` in `useBLE.ts`.

---

### LED Modes & Math Synthesizer Engine

> [!IMPORTANT]
> **Math Synthesizer Refactor (2026-04-21)**: The legacy "Fixed Mode" (10 hardcoded behaviors) and firmware-dependent RBM logic have been entirely superseded by a deterministic, client-side mathematical synthesizer. All lighting visualizations and sub-protocols are now driven by `SK8LYTZ_TEMPLATES` in `src/protocols/PatternEngine.ts`. **Current count: 43 spatial/temporal patterns (IDs 1-43) + 5 street modes (IDs 101-105) + 33 Multimode Pro Effects test patterns (IDs 201-233) = 81 total templates.**

> [!NOTE]
> **ProductVisualizer Architecture (2026-04-23)**: The legacy `simMode` Protocol Synchronization Engine (~120 lines) was removed. `ProductVisualizer` now passes props **directly** to `VisualizerUnit` with no intermediate state override layer. React state is the ground truth. The `rawHexPayload` BLE decoder, `applySorting()` color swapper, and dead `CANDLE`/`MULTICOLOR` branches were deleted. The `ledDot`/`ledDotSmall` unused StyleSheet entries were also purged. Visualizer animation now correctly fires on all `BUILDER`/`PROGRAMS`/`MUSIC`/`STREET`/`MULTIMODE` modes.

_Source of Truth: `src/protocols/PatternEngine.ts` (`SK8LYTZ_TEMPLATES`), `src/utils/MusicDictionary.ts` (Music)_

#### User-Facing Mode Taxonomy

| ModeType (FSM) | UI Tab | Protocol Family | Key Hook |
|:---|:---|:---|:---|
| `FAVORITES` | Quick Presets | `0x59` / `0x51` (replays saved state) | `useFavorites` |
| `MULTIMODE` | Pattern Synth / Color Picker | `0x59` / `0x51` | `useDockedControllerState` |
| `MUSIC` | Music Reactive | `0x73` (`setMusicConfig`) | `useMusicMode` |
| `STREET` | Motion Reactive | `0x59` (solid dispatches on accelerometer) | `useStreetMode` |
| `CAMERA` | Camera Color Capture | `0x59` | `useDockedControllerState` |

#### The Mathematical Pattern Registry (`SK8LYTZ_TEMPLATES`)

**81 templates** define the full pattern library — 43 spatial/temporal patterns (Comet, Breathing, Double Meteor etc.), 5 street mode patterns, and 33 Multimode Pro Effects test patterns (IDs 201-233, dispatched via `0x51` compact). All use a primary foreground (`FG`) and secondary background (`BG`) palette where applicable.
Dispatch chain: `useControllerDispatch.ts` → `PatternEngine.ts` (Synthesizer) → `ZenggeProtocol.ts` (BLE bytes).

**Archetypes & Auto-Routing:**
- **Spatial Mode (`0x59` CASCADE/FREEZE):** Synthesizes full 300-pixel RGB arrays client-side using waveform math (sine waves, pulse trains, alternating grids). Automatically routed to `<ProductVisualizer>` and `<CustomEffectVisualizer>` without duplicative business logic.
  > **âš ï¸ 0x59 SPATIAL LIMITATION (Center-Out Reality):** The hardware `0x59` command ONLY supports autonomous scrolling (`0x02 Running`). It CANNOT mathematically expand or contract pixels from a center point. Center-Out math functions generate static arrays that merely scroll, creating visual duplicates of standard Wipes/Comets. Furthermore, HALOZ hardware physically mirrors left/right segments (both wipe Heel-to-Toe), meaning a standard Wipe natively behaves as a Center-Out effect. Thus, Center-Out pattern math is redundant and incompatible with `0x59`.
- **Temporal Mode (`0x51` STEP_JUMP/GRADUAL):** For whole-strip temporal patterns (Jump, Strobe, Breathe), the engine MUST route to the `0x51` 32-step hardware scheduler. `0x59` is the wrong tool for whole-strip temporals because evaluating a Jump/Strobe equation at a static `seedTick` produces an un-animatable solid color or pure black frame that the hardware cannot jump/strobe properly. For patterns that require sub-millisecond fade interpolations (e.g., `Breath`, `Strobe`), the engine automatically routes to the `0x51` 32-step hardware scheduler to prevent BLE bus saturation.

> [!NOTE]
> The legacy `Fixed` UI tab was completely eliminated. The `MULTIMODE` hub now acts as a unified portal for all spatial/temporal mathematical templates.

#### RBM Built-in Patterns (100 Modes)

Source of truth: `src/utils/RbmDictionary.ts` — IDs 1—100, mapped 1:1 to Zengge `SymphonyBuild` string table.
Visualizer: `src/utils/RbmSimulator.ts` (pixel-perfect frame generation). [MOVE_TO_ARCHIVE] (Note: RbmSimulator has been deleted/refactored, visualizer frame generation has been migrated to protocols/SymphonyEngine.ts)
Protocol: `0x42` (`setCustomRbm`) or `0x61` (legacy APK path — same pattern table).

#### Music Mode Patterns (46 Profiles)

Source of truth: `src/utils/MusicDictionary.ts` — music-reactive patterns keyed to protocol IDs depending on Mode Type (Light Bar = 16, Light Screen = 30).
Visualizer: `src/utils/RbmSimulator.ts` → `getRbmMusicFrame()`. [MOVE_TO_ARCHIVE] (Note: migrated to protocols/SymphonyEngine.ts -> getMusicVisualizerFrame)
Protocol: `0x73` (`setMusicConfig` with `0x26`/`0x27` Mode Type) + `0x74` (App Mic magnitude stream when `0x73` isOn = 0).

---

### Command: Hardware Config Query (0x63)

_Reads the current EEPROM settings stored inside the controller chip._

- **Send (5 bytes):** `[0x63, 0x12, 0x21, 0x0F, checksum]`
- **CRITICAL ENDIANNESS:** `ledPoints` bytes are **Little-Endian SWAPPED**: `((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF)`.

### Command: Hardware Config Write (0x62)

_Writes custom segments, IC type, and max LED points permanently to the controller EEPROM._

- **Format:** `[0x62, ptsHigh, ptsLow, segHigh, segLow, icType, sorting, micPts, micSegs, 0xF0, checksum]`
- **CRITICAL ENDIANNESS:** Uses **Big-Endian format**: `ptsHigh = (points >> 8) & 0xFF`, `ptsLow = points & 0xFF`.

> [!NOTE]
> **`points` â‰  total LEDs.** `points` = LEDs per segment. `segments` = number of parallel mirrors.
> Total physical LEDs = `points Ã— segments`. The hardware's segment engine mirrors the pattern automatically.
> **HALOZ example**: 22 bulbs = 11 points Ã— 2 segments. All pattern commands use 11, not 22.
> The `0x51` slot `flags=0x80` byte enables segment mirroring ("section toggle"). `flags=0x00` disables it.
> Full model documented in `ZENGGE_PROTOCOL_BIBLE.md` under `0x62`.

---

### Command: Segmented Multi-Color Layout Array (0x59)

_Primary command for all IC-strip patterns. Sends a per-pixel RGB array that the hardware loops autonomously._

> [!IMPORTANT]
> **SEGMENT MODEL — Array Length Must Use `ledPoints`, NOT Total LEDs.**
> The ZENGGE hardware segment engine automatically mirrors the `ledPoints` pattern across all segments.
> For HALOZ (22 bulbs = 11 points Ã— 2 segments), send an array of **11** pixels, not 22.
> Sending `ledPoints Ã— segments` pixels bypasses the hardware mirror and fills both segments manually.
> Source: BLE sniff observation (2026-04-22) — ZENGGE Multi-Color creator uses `points` exclusively.

- **Format:** `[0x59, totalLenHi, totalLenLo, [R1,G1,B1...], numLEDsHi, numLEDsLo, transitionType, speed, direction, checksum]`
- **Source of Truth:** `ZenggeProtocol.setMultiColor()` — _do NOT replicate this logic elsewhere._
- **Minimum Payload:** 12 pixels. Payloads <10 cause **hardware memory lock glitching**.
- **TransitionType Bytes (APK Verified Truth: `StaticColorfulMode.java`):**

> âš ï¸ **0xA3 HARDWARE LIMITATION:** The `0x59` command is a spatial payload. The ZENGGE app explicitly *hides* Breathe and Twinkly from the `0x59` UI for the `0xA3` chip because the hardware cannot calculate temporal math over a 450-byte custom array. Strobe and Jump are also known to fail. **For temporal transitions (Breathe, Jump, Strobe), use the `0x51` Scene Sequencer instead!**

| Byte | Name | Behavior | 0xA3 Status |
|:---|:---|:---|:---|
| `0x01` | Static | Freeze in place | âœ… **Fully Supported** |
| `0x02` | Running Water | Continuous hardware scroll | âœ… **Fully Supported** |
| `0x03` | Strobe | Flash effect | âŒ Fails (Requires `0x51`) |
| `0x04` | Jump | Hard color jump | âŒ Fails (Requires `0x51`) |
| `0x05` | Breathe | Breathe fade effect | â›” **Firmware Locked/Hidden** (Use `0x51`) |
| `0x06` | Twinkly | Twinkle effect | â›” **Firmware Locked/Hidden** |

> [!IMPORTANT]
> **Tick Settings (Point Count) Mismatch Flaw**: The `numLEDsHi` and `numLEDsLo` bytes at the end of the `0x59` payload dictate the **physical hardware strip length** that the transition effect will span across. Our previous implementation clamped this value to the RGB array length (max 54). If the hardware has 150 LEDs, clamping this to 54 causes transitions to truncate because the hardware thinks the spatial size is only 54! To bypass MTU limits while preserving spatial effects, we must decouple the RGB array length from the hardware point count sent in the payload.

- **Speed:** UI 0—100 → HW 1—31. Formula: `max(1, min(31, round(uiSpeed / 100 Ã— 30) + 1))`. Source: APK `Protocol/n.java: ad.e.a(f, 1, 31)`.
- **Direction:** `0x01` Forward, `0x00` Reverse.
- **Solid Mode Replication:** A single 1-pixel padded array with `transitionType=0x01` (FREEZE) safely replicates Solid Mode without `0x31` flickering glitches.

---

### Command: DIY Custom Animation Sequences (0x51)

_Sends up to 32 animation steps. Hardware loops through active steps autonomously. Steps are stored in device EEPROM._

> [!IMPORTANT]
> **ORACLE + BLE SNIFF CONFIRMED (2026-04-22)**: The 9B compact format (291B) fired by our current `setCustomMode()` **works correctly** on 0xA3 hardware. The 10B extended format via our `wrapCommand` does nothing. The ZENGGE app sends 10B slots using a different chunked BLE framing header. Full evidence in `ZENGGE_PROTOCOL_BIBLE.md` Section 11.

- **Format (current working):** `[0x51, Step0(9B)...Step31(9B), 0x0F_Terminator, checksum]` (291 bytes)
- **Format (ZENGGE app, requires chunked framing):** `[0x51, Step0(10B)...Step31(10B), checksum]` (via `[40 seq 00 00 01 43 BD 0B]` header)

**Step Structure — Hardware-Confirmed 10-Byte (from live BLE sniff):**
```
[ACTIVE_FLAG, effectId, speed, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, flags]
```
- `ACTIVE_FLAG`: `0xF0` = active step, `0x0F` = inactive (skip).
- `effectId`: SymphonyEffect ID 1—33
- `speed`: 0—100 (direct, no scaling)
- `FG.RGB`: Foreground color (ignored for NO_COLOR/rainbow effects)
- `BG.RGB`: Background color (ignored for NO_COLOR/rainbow effects)
- `flags`: `0x80` = forward + section toggle enabled, `0x00` = reverse

**Step Structure — Current Production 9-Byte (works via our wrapper):**
```
[ACTIVE_FLAG, transMode, speed, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b]
```

- **Step Transition Mode Bytes (for 9B format):**

| Byte | Constant | Behavior |
|:---|:---|:---|
| `0x3A` | `STEP_JUMP` | Hard cut between FG and BG colors |
| `0x3B` | `STEP_GRADUAL` | Smooth cross-fade between FG and BG |
| `0x3C` | `STEP_STROBE` | Rapid flash between FG and BG |
| `0x01`—`0x2C` | Custom Effects 1—44 | Hardware `SymphonyEffect` IDs. Full mapping documented in `ZENGGE_PROTOCOL_BIBLE.md` |

- **Speed:** Full 1—100 range valid (unlike `0x59` which is capped at 31).
- **Max slots:** 32 active steps.
- **Source of Truth:** `ZenggeProtocol.setCustomMode()` — current 9B format is production-safe.

---

### Basic Control Commands

- **Power ON (0x71):** `[0x71, 0x23, 0x0F, 0xA3]` — checksum `0xA3` = sum of first 3 bytes âœ…
- **Power OFF (0x71):** `[0x71, 0x24, 0x0F, 0xA4]` — checksum `0xA4` = sum of first 3 bytes âœ…
- **Source:** `C14184b.m4796M()` via `C7780q.m20873a()` — 0xA3 is NOT a legacy device → always uses `0x71`, never `0x3B`.

### Command: Settled Mode — FG + BG Dual Color (0x41)

_Triggers one of 33 Symphony effects with explicit foreground and background colors._

- **Format:** `[0x41, effectId, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, speed, direction, 0x00, 0xF0, checksum]` (13 bytes)
- **effectId range:** 1—33 (SymphonyEffect IDs)
- **direction:** `0x00` = forward, `0x01` = reverse
- **Source:** `C7775l.java` → `m20877a()`, called by `SettledModeFragment`

### Command: Multi-Effect Sequence (0x43)

> [!CAUTION]
> **DO NOT USE IN PRODUCTION.** Oracle Lab test (2026-04-22) confirmed that our `0x43` payload causes the hardware's **LEDs to shut off completely** (state machine crash). The ZENGGE app's "Customize Tab" actually uses `0x51` with 10-byte slots for multi-step effects — NOT `0x43`. This opcode is either unused in our firmware revision or requires a completely different BLE framing to activate safely.

_APK-documented format below is preserved for reference only:_
- **Format:** `[0x43, effectId[0]...effectId[49], speed, brightness, checksum]` (54 bytes total)
- **Source:** `C7778o.java` → `m20874a()`, called by `FunctionModeFragment` when no single effect is selected

### Command: Set RBM Built-in Pattern (0x42)

_Triggers one of 100 hardware-native RBM patterns by ID. The controller runs the animation internally — no pixel array needed._

- **Format:** `[0x42, patternId(1—100), speed(1—100), brightness(1—100), checksum]`
- **Source of Truth:** `ZenggeProtocol.setCustomRbm()`
- **Pattern IDs:** 1—100 mapped 1:1 to Zengge `SymphonyBuild` string table. Full dictionary in `src/utils/RbmDictionary.ts`.

### Command: Symphony Multi-Color / RBM Legacy (0x61)

_Legacy/alternative opcode for triggering RBM patterns. Present in Zengge APK code paths and the SK8Lytz Diagnostic Lab._

- **Format:** `[0x61, patternId, speed, brightness, checksum]` — identical structure to `0x42`.
- **Relationship to 0x42:** Both target the same on-chip RBM pattern table. The `0x61` opcode appears in older Zengge firmware revisions and specific APK UI code paths (`symphony_SymphonyBuild_*`). The production dispatch path uses `0x42` via `setCustomRbm()`, while the Diagnostic Lab UI labels it `0x61` for APK parity.
- **SK8Lytz Usage:** Exposed in Admin Diagnostic Lab (`useProtocolBuilder.ts`, `Sk8LytzDiagnosticLab.tsx`) for protocol testing.

### Command: Music Configuration (0x73)

_Configures the hardware's music-reactive mode with mode type (Bar vs Screen), pattern, and dual colors._

> [!CAUTION]
> **The `0x73` structure does NOT contain a trailing micSource byte.** The `0x26` and `0x27` values dictate the Matrix Style (Light Bar vs Light Screen), NOT the microphone source.
> Microphone Source is toggled implicitly via the `isOn` byte.

- **Format (13 bytes):** `[0x73, isOn, modeType, effectId, dropR, dropG, dropB, colR, colG, colB, sensitivity, brightness, checksum]`
- **isOn:** `0x01` = Device Mic Active (Hardware processes audio). `0x00` = App Mic Active (Hardware mic OFF, waits for `0x74` magnitude streams).
- **modeType:** `0x26` (38) = Light Bar Mode (16 built-in patterns). `0x27` (39) = Light Screen Mode (30 built-in patterns).
- **effectId:** 1—30 music-reactive pattern IDs (mapped in `MusicDictionary.ts`)
- **dropR, dropG, dropB (Bytes 4-6):** Drop Color (controlled by `sb_point` in the native app). Verified via `strings.xml` translation `<string name="point_color">drop color</string>`.
- **colR, colG, colB (Bytes 7-9):** Sound Column Color (controlled by `sb_col` in the native app). Verified via `strings.xml` translation `<string name="col_color">sound column color</string>`.
  - *Light Bar (0x26) specific behavior*: The native ZENGGE app clones the primary color to **both** the Drop Color and Sound Column Color slots inside the `0x73` payload to prevent hardware rendering confusion.
- **sensitivity / brightness:** 0—255
- **Source of Truth:** `ZenggeProtocol.setMusicConfig()` (decompiler trace: `C7789z.java`, `MusicModeFragment.java` line 752)

### Command: App Mic Magnitude (0x74)

_Streams real-time audio magnitude from the app's microphone to drive hardware music-reactive LEDs._

- **Format:** `[0x74, magnitude(0—255), checksum]` (3 bytes)
- **Used when:** `isOn = 0x00` (App mic) in the `0x73` music config.
- **Source:** `C7788y.java` → `m20863a()`, `useAppMicrophone.ts` → `ZenggeProtocol.sendMusicMagnitude()`

### Command: Live Pixel Stream — Frame-by-Frame (0x53)

_Streams one row of real-time pixel data per call. Used for live bitmap/image projection onto LEDs._

- **Format (variable):** `[0x53, totalLen_hi, totalLen_lo, R, G, B, ...(numLEDs Ã— RGB)..., numLEDs_hi, numLEDs_lo, checksum]`
- **totalLen:** `(numLEDs Ã— 3) + 6`
- **Rate-limited:** Hardware uses AtomicBoolean gate — must wait for ACK before next frame.
- **Behavior:** Sends one bitmap row. Call repeatedly in a loop to stream animation frames.
- **APK Source:** Built inline `SceneModeFragment.m18748Z2(int[] iArr)` — no dedicated Protocol class.

### Command: Scene Slot Management (0x56 / 0x57 / 0x58)

_EEPROM-based scene storage and playback control._

**Delete Scene Slot (0x56) — 15 bytes:**
```
[0x56, slotIndex(0-9), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, checksum]
```

**Activate Scene + Set Speed/Brightness (0x57) — 5 bytes:**
```
[0x57, sceneIndex, speed, brightness, checksum]
// sceneIndex: 0—9 for specific slot, 0xFF (-1 as byte) to replay ALL
```

**Scene State Query (0x58) — 3 bytes:**
```
[0x58, 0xF0 (query active) | 0x0F (query inactive), checksum]
```
- **APK Source:** `SceneModeFragment.m18778K2()`, `m18750Y2()`, `C14184b.m4769g0()`

### Proactive Battery Management System (Architectural Skill)

The app implements a **Mathematical Consumption Modeling** system using real-time modeling of pixel density, brightness, and pattern intensity to estimate battery reserve.

---

## 4. Domain-Driven Architecture

> [!IMPORTANT]
> **DDA Refactor Shipped: 2026-04-14** — Hook-First domain model. **BLE Engine XState Migration: 2026-06-10** — `useBLE.ts` is now a thin XState orchestrator. 5 hooks deleted (`useBLEAutoRecovery`, `useBLEGattMutex`, `useBLEHeartbeat`, `BleStateMachine`, `BleConnectionManager`, `BleLifecycleManager`). Logic now lives in 5 XState actors: `BleMachine`, `ConnectService`, `RecoveryService`, `HeartbeatService`, `RSSIService`, `InterrogatorService`.

`_useBLE.ts` is a thin orchestrator. It constructs the XState machine and wires callbacks. BLE sub-hooks and the sweeper are NEVER consumed directly by UI components._

#### XState Actor Architecture (as of 2026-06-10)

| Actor / Service | File | XState Type | State | Owns |
| :--- | :--- | :--- | :--- | :--- |
| `BleMachine` | `src/services/ble/BleMachine.ts` | Machine root | All states | Radio ownership, state transitions, actor lifecycle |
| `connectService` | `src/services/ble/ConnectService.ts` | `fromPromise` | CONNECTING | Group GATT connect, MTU, adapter mapping, notification wiring, stale device flush |
| `recoveryService` | `src/services/ble/RecoveryService.ts` | `fromCallback` | RECOVERING | 3-phase backoff loop, clearWriteQueue, adapter re-mapping, RECOVERY_COMPLETE/FAIL |
| `heartbeatService` | `src/services/ble/HeartbeatService.ts` | `fromCallback` | READY | 45s 0x63 ping via enqueueWrite, RSSI fallback, HEARTBEAT_FAIL on error |
| `scanService` | `src/services/ble/BleMachine.ts` | Entry/Exit actions | SCANNING | `startDeviceScan`/`stopDeviceScan` — machine is the ONLY call site |

#### Hook-Level Services (Not XState Actors)

| Hook / Service | File | Owns |
| :--- | :--- | :--- |
| `useBLEScanner` | `src/hooks/ble/useBLEScanner.ts` | Peripheral discovery, RSSI proximity gating, pending registrations |
| `useBLEBatterySweep + useBLEInterrogator` | `src/hooks/ble/useBLEBatterySweep.ts / useBLEInterrogator.ts` | Silent background LowPower scan, Interrogator Queue, `hwCache`, 3-tier battery-adaptive throttling (BAT-01) |
| `useBLERSSIMonitor` | `src/hooks/ble/useBLERSSIMonitor.ts` | 30s post-connect RSSI polling, `rssiMap`, proactive reconnect at -82 dBm. Thin wrapper around `RSSIService.ts`. |
| `RSSIService` | `src/services/ble/RSSIService.ts` | Pure RSSI polling logic. Not an XState actor. |
| `InterrogatorService` | `src/services/ble/InterrogatorService.ts` | Hardware EEPROM probe via 0x63 opcode, FTUE vs standard queue delay, AsyncStorage cache. Not an XState actor. |
| `BleWriteDispatcher` | `src/services/BleWriteDispatcher.ts` | Serialized group writes with 50ms inter-device gap, debounce, generation counter |
| `BleWriteQueue` | `src/services/BleWriteQueue.ts` | Priority FIFO write queue: `critical`/`normal`/`bulk` tiers, MAX_QUEUE_DEPTH=8, clearWriteQueue() for recovery |
| `BlePingService` | `src/services/BlePingService.ts` | Wizard-exclusive atomic GATT session (Connect→Blink→Probe→Disconnect) |

#### Auth Domain (`src/context/`, `src/providers/`)

| Hook / Provider | Consumer | Owns |
| :--- | :--- | :--- |
| `AuthProvider` | `App.tsx` | Global authentication state (`session`, `user`, `isOfflineMode`, `isAuthenticated`). Eliminates N parallel `supabase.auth.getUser()` calls. |
| `useAuth` | Global | Exposes auth state to components and hooks. |
| `ComplianceGate` | `App.tsx` | EULA version enforcement. Decoupled from `supabase.auth.getUser()`, reads `user` and `isOfflineMode` strictly from `useAuth()`. |

#### Dashboard Screen Domain (`src/hooks/`)

| Hook                  | Consumer          | Owns                                                               |
| :-------------------- | :---------------- | :----------------------------------------------------------------- |
| `useDashboardProfile` | `DashboardScreen` | User profile, `displayName`, `avatarUrl`, Supabase profile fetch   |
| `useDashboardGroups`  | `DashboardScreen` | `customGroups`, `deviceConfigs` AsyncStorage load/save, group CRUD |
| `useDashboardAutoConnect` | `DashboardScreen` | Debounced auto-connect observer, gate-checked, ref-forwarded closures |

#### DockedController Domain (`src/hooks/`)

| Hook                       | Consumer           | Owns                                                                                                                                  |
| :------------------------- | :----------------- | :------------------------------------------------------------------------------------------------------------------------------------ |
| `useDockedControllerState` | `DockedController` | All LED control FSM state: `activeMode`, `selectedColor`, `brightness`, `speed`, `multiColors`, `builderNodes`, scene capture/restore |
| `useFavorites`             | `DockedController` | `favorites[]`, `quickPresets[]`, save/delete/load operations, prompt FSM                                                              |
| `useStreetMode`            | `DockedController` | Accelerometer subscription, G-force calculation, brake/cruise color dispatch, GPS speed sampling                                      |
| `useSessionTracking` (stale) | `DockedController` | [MOVE_TO_ARCHIVE] - Session FSM (`IDLE → RECORDING → SUMMARY`), duration, distance, peak speed, session summary modal                                     |
| `useMusicMode`             | `DockedController` | Owns 0x73 music config dispatch, pattern names, pattern navigation.                                                                   |
| `useCuratedPicks`          | `DockedController` | Fetches and caches SK8Lytz Picks (curated presets) from Supabase.                                                                     |
| `useAppMicrophone`         | `DockedController` | Manages the expo-av Audio.Recording lifecycle for APP MIC mode. Streams normalized magnitude (0—1).                                   |
| `useControllerAnalytics`   | `DockedController` | Debounced telemetry logging for mode, pattern, color, brightness, speed changes.                                                      |

#### AccountModal Domain (`src/hooks/`)

| Hook                 | Consumer       | Owns                                                            |
| :------------------- | :------------- | :-------------------------------------------------------------- |
| `useAccountOverview` | `AccountModal` | Supabase profile read/write, avatar upload, display name update |
| `useSkateStats`      | `AccountModal` | Aggregate session stats fetch, totals calculation               |
| `useDeviceFleet`     | `AccountModal` | `registered_devices` Supabase fetch, fleet display list [MOVE_TO_ARCHIVE]         |

#### Admin Domain (`src/hooks/`)

| Hook                 | Consumer                 | Owns                                                                     |
| :------------------- | :----------------------- | :----------------------------------------------------------------------- |
| `useDiagnosticLog`   | `Sk8LytzDiagnosticLab`   | BLE RX/TX log buffer, `targetDeviceId` targeting, raw hex transmission   |
| `useProtocolBuilder` | `Sk8LytzDiagnosticLab`   | [MOVE_TO_ARCHIVE] - Stale owner Sk8LytzProgrammerModal replaced. FSM-based payload generation for `0x51`, `0x59`, `0x62`, `0x63`, `0x73`  |
| `useAdminTelemetry`  | `AdminToolsModal`        | App analytics, system health metrics, cloud log uploads                  |
| `useProductManager`  | `AdminToolsModal`        | Hardware catalog CRUD, `product_catalog` upserts, blank profile creation |
| `useAdminSettings`   | `AdminToolsModal`        | Global remote feature flags, `AppSettingsService` read/write             |

#### Watch & Health Domain (`src/hooks/`, `modules/`, `src/services/`)

| Hook / Service            | Consumer           | Owns                                                                                              |
| :------------------------ | :----------------- | :------------------------------------------------------------------------------------------------ |
| `useHealthTelemetry`      | `SessionContext`   | Phone/watch health polling, watch-preferred priority logic, HR/cal/peak/avg state, `mergeWatchHealth()` |
| `WatchBridge` (native module) | `SessionContext` | Phoneâ†”watch session state sync, command relay (START/STOP), health data relay via native DataLayer/WCSession |
| `SpeedTrackingService`    | `SessionContext`   | GPS speed push to watch via `WatchBridge.sendMetricUpdate()` during active sessions               |

---

### ðŸ“ Shared Type Contract

All FSM states and shared interfaces live in **`src/types/dashboard.types.ts`**. Never re-declare these types in individual hooks or components.

| Type                  | Values                                                                        |
| :-------------------- | :---------------------------------------------------------------------------- |
| `ModeType`            | `'FAVORITES' \| 'FIXED' \| 'MULTIMODE' \| 'MUSIC' \| 'STREET' \| 'CAMERA'` |
| `FixedSubMode`        | `'PATTERN' \| 'BUILDER'`                                                      |
| `MicSource`           | `'APP' \| 'DEVICE'`                                                           |
| `MusicColorFocus`     | `'PRIMARY' \| 'SECONDARY'`                                                    |
| `DeviceSettingsState` | FSM: `'IDLE' \| 'LOADING' \| 'READY' \| 'ERROR'`                              |
| `IDeviceConfigEntry`  | `{ name, type, points, segments, sorting, stripType, group_ids: string[], group_names: string[] }` — **NOTE**: scalar `groupId` removed in many-to-many migration (2026-05-28). |

**BLE Domain Types** — `src/types/ble.types.ts` _(added P2 type-safety pipeline, 2026-06-05)_:

| Type | Purpose |
| :--- | :--- |
| `BleConnectionRequest` | Replaces 13 `any` params in `executeConnectToDevices` with a single typed interface. Fields: `devices`, `bleManager`, `connectedDevicesRef`, `blacklistedMacsRef`, `keepaliveTimerRef`, `disconnectListeners`, `sweeper`, `scanner`, `autoRecovery`, `bleGateRef`, `mtuMapRef`, `adapterMapRef`, `dataReceivedCallbackRef`, `handleNotificationRef`, `handleOrganicDisconnect`, `setConnectedDevices`, `setGate`. |
| `GattPriority` | `'P1_CRITICAL' \| 'P2_RECOVERY' \| 'P3_INTERROGATION' \| 'P4_MAINTENANCE'` — 4-tier GATT mutex priority enum |
| `BleWriteStateRefs` | Typed refs for `BleWriteDispatcher`: `writeGeneration`, `writeDebounceTimerRef` |

---

### Engineering Standards

- **UI Components**: Must focus strictly on rendering and presentation.
- **State Machines**: Complex multi-state logic must use `ModeType`/string-union FSMs, never boolean flag clusters.
- **Strict Domain Exceptions (VIP Telemetry)**: All domain hooks (hardware dispatch, Supabase reads, filesystem access) MUST implement unified `try/catch` execution blocks. Errors must trigger `AppLogger.error()` to route telemetry into the Supabase VIP Fast-Lane. The use of naked `console.warn` or `console.error` inside domain hooks is strictly prohibited.
- **Hook Scaffolding Constraint**: New hooks must be generated via the project `/scaffold-hook` automated workflow, which pre-injects the mandatory `AppLogger` crash loop sequence.
- **Database Telemetry Masking**: All non-critical DB telemetry inserts (e.g. `skate_sessions` or metrics) must be wrapped in `try/catch` blocks so they do NOT block the critical BLE execution pipeline on failure.
- **Type Safety per Schema constraints**: Tactical type casting using `as unknown as CustomType` or `as any` is authorized when bypassing strict, auto-generated Supabase overload methods for `Json` fields and unresolvable overloads, as long as runtime validation aligns with the hardened database schema.
- **Type Imports**: Always import `ModeType` and shared interfaces from `dashboard.types.ts`, not from hook files.
- **Hook Contracts**: Hooks receive BLE context via props, never via direct import of BLE libraries.

---

## 5. Database Schemas

### Supabase Architecture (Telemetry & Registration)

_Project ID:_ `qefmeivpjyaukbwadgaz`

#### **`registered_devices`** (Hardened Schema — Updated 2026-04-22)

| Column                      | Type       | Purpose                                              |
| :-------------------------- | :--------- | :--------------------------------------------------- |
| `id`                        | TEXT (PK)  | Unique system identifier (MAC+userId composite)      |
| `device_mac`                | TEXT       | Unique hardware address (UPPERCASE)                  |
| `user_id`                   | UUID       | Owner ID                                             |
| `device_name`               | TEXT       | Custom alias                                         |
| `product_type`              | TEXT       | HALOZ / SOULZ / RAILZ                                |
| `led_points`                | INT        | Physical pixel count                                 |
| `segments`                  | INT        | Hardware mirror segments                             |
| `ic_type`                   | TEXT       | LED chipset (WS2812B, SK6812, etc.)                  |
| `color_sorting`             | TEXT       | RGB channel order (GRB, RGB, etc.)                   |
| `firmware_ver`              | INT        | Firmware version integer from BLE advertisement      |
| `led_version`               | INT        | LED version from BLE advertisement                   |
| `product_id`                | INT        | ZENGGE hardware product ID (0xA3=163 for all SK8Lytz)|
| `product_id_confirmed_at`   | TIMESTAMPTZ| When product_id was confirmed via BLE (added 2026-04-22)|
| `rf_mode`                   | TEXT       | RF remote auth policy                                |
| `rf_paired_count`           | INT        | Number of paired RF remotes                          |
| `group_id`                  | TEXT       | **âš ï¸ LEGACY — do not use for new code.** Superseded by junction table `device_group_members`. Still present in cloud rows for backward compat. |
| `group_name`                | TEXT       | **âš ï¸ LEGACY — do not use for new code.** Superseded by `registered_groups.group_name`. |
| `registered_at`             | TIMESTAMPTZ| First registration timestamp                         |
| `updated_at`                | TIMESTAMPTZ| Last modification timestamp                          |
| `rssi_at_register`          | INT        | Signal strength at registration                      |

> [!IMPORTANT]
> **Many-to-Many Migration (2026-05-28)**: Device-to-group membership is now stored in the `device_group_members` junction table (`device_id TEXT FK → registered_devices.device_mac`, `group_id TEXT FK → registered_groups.id`). The app-side `RegisteredDevice` object uses `group_ids: string[]` and `group_names: string[]` arrays. The `upsert_group_with_devices` RPC handles all atomic group mutations. Never write scalar `group_id` from new code.

#### **`product_catalog`** (Dynamic Hardware Definitions)

| Column                 | Type      | Purpose                  |
| :--------------------- | :-------- | :----------------------- |
| `id`                   | TEXT (PK) | Unique product key       |
| `display_name`         | TEXT      | Human-readable name      |
| `viz_shape`            | TEXT      | RING / OVAL / DUAL_STRIP |
| `battery_capacity_mah` | INT       | Rated capacity           |

#### **`skate_spots`** (Map Grounding)

| Column          | Type      | Purpose                               |
| :-------------- | :-------- | :------------------------------------ |
| `id`            | UUID (PK) | Unique spot ID                        |
| `name`          | TEXT      | Rink/Park name                        |
| `lat`           | DOUBLE    | Latitude                              |
| `lng`           | DOUBLE    | Longitude                             |
| `surface_type`  | TEXT      | `wood` / `concrete` / `sport_court`   |
| `is_indoor`     | BOOLEAN   | Indoor vs Outdoor                     |
| `source`        | TEXT      | `native_seed` / `osm` / `user_submit` |
| `is_verified`   | BOOLEAN   | Administrative verification status    |
| `facility_type` | TEXT      | `roller_rink`/`skatepark`/`pro_shop`  |
| `has_pro_shop`  | BOOLEAN   | Embedded pro shop indicator           |
| `is_featured`   | BOOLEAN   | Highlighted partner or rink           |
| `last_enriched_at`| TIMESTAMP | Last Cultural Daemon crawl timestamp  |
| `socials`       | JSONB     | Scraped Instagram/Facebook URLs       |
| `vibe_rating`   | DOUBLE    | Aggregate 5-star Google/Yelp rating   |
| `has_adult_night`| BOOLEAN  | Scraped confirmation of 18+ nights    |
| `has_lights`    | BOOLEAN   | OSM Tag: Night lighting               |
| `has_fee`       | BOOLEAN   | OSM Tag: Paid entry                   |
| `has_rental`    | BOOLEAN   | OSM Tag: Gear rental                  |
| `has_wifi`      | BOOLEAN   | OSM Tag: Public Wifi                  |

> [!NOTE]
> **Map Grounding Strategy**: Over 10,000 skate spots are horizontally harvested natively from OpenStreetMap (`tools/scraper/USANationalHarvest.ts`) populating coordinates, facility type, and physical properties. 

---

## 8. Data Pipelines & ETL (The Cultural Daemon)

The project leverages a decoupled, two-phase zero-cost backend to populate `skate_spots`.

### Wave 1: Spatial Harvesting (The Cartographer)
A fast, lightweight Node script (`USANationalHarvest.ts`) queries the OSM Overpass API state-by-state. It extracts coordinates, resolves full addresses via Nominatim, and maps physical boolean tags (`has_ac`, `has_food`, `capacity`) into Supabase in bulk.

### Wave 2: Cultural Enrichment (The Daemon)
A stealthy `Puppeteer` background process (`CulturalDaemon.ts`) runs infinitely via PM2. 
- **The Priority Queue:** It queries Supabase via the `get_next_spot_to_enrich()` RPC, which enforces a strict hierarchy: `roller_rink` -> `hybrid` -> `pro_shop` -> `skatepark`.
- **The Engine:** It uses Google Shadow-DOM scraping to extract aggregate `vibe_rating`, Instagram links, and adult-night presence.
- **The Stealth:** The script strictly sleeps for 4 minutes between hits (~280 locations/day), guaranteeing zero CAPTCHAs and eliminating the need for paid IP proxies.

### Wave 3: The AI Detective (Local LM Studio Pipeline)
A localized, schema-driven AI extraction pipeline that processes raw text dumps from deep-crawled websites.
- **The Brain:** LM Studio running locally (Llama-3 / Mistral model via OpenAI-compatible REST endpoint on port 1234). Ollama was permanently removed — see conversation `40767855`.
- **The Interface:** Phase 2 (Detective) dashboard tab provides **AI Target Vectors** toggle switches (e.g., General Hours, Adult Night, Pricing, House Rules).
- **The Config (SSOT):** `scraper_config.ai_target_vectors` stores the active extraction goals as a `text[]` array in Supabase.
- **The Indexer:** The AI Daemon (`Indexer.ts`) dynamically compiles the prompt using the active target vectors and forces the AI to reply strictly in JSON.

---

## 6. Crew Hub & Session Lifecycle

To ensure high-fidelity discovery and telemetry, the Crew Hub follows strict lifecycle and naming protocols.

### Session Naming Convention

Automatic `_MM/DD` suffix enforced in `CrewModal.handleCreate` [MOVE_TO_ARCHIVE].

### Proximity Discovery & Visibility Rules

Discovery radius filter governed by `LocationService.getNearbyPublicSessions(radiusMi)`.

### Global Session Lifecycle Cleanup

System-wide `cleanupExpiredSessions()` ends sessions older than 24h.

---

## 7. Session Telemetry Architecture

### Supabase Table: `skate_sessions`
| Column           | Type     | Notes                                   |
| ---------------- | -------- | --------------------------------------- |
| `duration_sec`   | `int4`   | Total session length                    |
| `distance_miles` | `float8` | Accumulated GPS distance                |
| `avg_speed_mph`  | `float8` | Mean speed                              |
| `peak_speed_mph` | `float8` | Maximum speed                           |
| `calories`       | `int4`   | Estimated via MET formula               |
| `skate_spot_id`  | `uuid`   | Foreign key linked to `skate_spots` PK  |

### Telemetry Storage Optimization (JSONB Consolidation)
To maximize query performance and eliminate cloud storage bloat, SK8Lytz utilizes a unified JSONB ingestion model, completely bypassing legacy flat-file chunking and fragmented Postgres tables.
- **Unified Ingestion (`telemetry_snapshots`)**: All hardware usage, BLE events, and functional telemetry logs are pipe-lined directly into `telemetry_snapshots` utilizing a generic `event_type` and a flexible `JSONB` `metadata` column.
- **GIN Indexing**: The `metadata` JSONB column features a full Postgres `GIN` index, enabling hyper-fast arbitrary querying on any nested diagnostic property without table mutations.
- **VIP Error Fast-Lane (`telemetry_errors`)**: Critical crashes (`ERROR_CAUGHT`, `PROTOCOL_ERROR`) completely bypass the standard Spool buffers. They are instantly asynchronously fired into a dedicated `telemetry_errors` table using isolated `try/catch` fallbacks, guaranteeing delivery even during OOM crashes or buffer failures.
- **Deduplication Strategy**: Multi-device hardware commands (e.g. groups) insert a single row with `device_id` as `null` while identifying targets in the `group_id` or `metadata->>'deviceIds'` array, preventing duplicate db row expansion.

---

## 8. Agentic PM Protocols (The Brain)

This project is governed by a custom-built **Agentic OS**—a suite of 38 strict protocols located in `.agents/rules/`.

### Tier 1: Safety, Precision & Stealth

| Rule                             | Function                                                  |
| :------------------------------- | :-------------------------------------------------------- |
| **Critical Safety & Quarantine** | Strict branching and rollback protocols.                  |
| **Surgical Strike Protocol**     | Mandates micro-edits and precision coding.                |
| **Absolute Truth**               | Eliminates hallucinations via mandatory reference audits. |

### Tier 5: Debugging, Hygiene & Maintenance

| Rule                      | Function                                           |
| :------------------------ | :------------------------------------------------- |
| **Emergency Debug Drill** | Enforces instrumentation over guess-fixing.        |
| **Boy Scout (Tech Debt)** | Mandates one small cleanup in every modified file. |
| **Supabase Schema Sync**  | Automatic type regeneration after DB changes.      |
| **Direct Merge Protocol** | (v1.8.0) Authorized to merge/push to master locally upon explicit user consent. |

---

## 9. Sentinel Engineering Governance (Workflow V6)

The SK8Lytz lifecycle is governed by the **Sentinel Engine**.

### 9.1 Legal Hardening (The Compliance Shield)
_Shipped: v1.8.0 | Mandatory for App Store Governance_

**App-Level Opt-Out Ledger**:
- **Source of Truth**: `@sk8lytz_permissions_optout` in AsyncStorage.
- **Behavior**: If a user revokes inside the app, the app "Soft-Revokes" (stops using the API) even if the OS says "Allowed".
- **OS Sync**: `syncSystemPermissions()` runs on boot/foreground to reconcile the ledger with native OS settings. If OS is "Denied", App ledger is forced to "Opt-Out".

**App Onboarding & Permissions UX**:
- **Bluetooth First, Notifications Last**: `BLUETOOTH` is prioritized as the supreme technical prerequisite for the `device` pipeline. `NOTIFICATIONS` acts solely as an enhancement (Session Invites) and must ALWAYS be requested last in the onboarding flow to respect user fatigue.
- **Prompt Isolation (`NotificationService`)**: Push notification initialization (`notificationService.init()`) is decoupled from app/dashboard mount to prevent rogue, mandatory OS prompts intercepting the UX. It is only fully initialized `init(true)` when explicitly granted by the user via the `GranularPermissionsList`.

**Immutable Audit Trail**:
- **AppLogger Dispatch**: Every permission change and EULA acceptance fires an immutable cloud event (`PERMISSION_OPT_IN`, `PERMISSION_OPT_OUT`, `EULA_ACCEPTED`) with a forced timestamp.
- **Legal Defense**: Provides a non-repudiable log of when a user accepted terms.

### 9.5 PII Scrubbing & Telemetry Hardening
_Shipped: v3.9.1 | Mandatory for GDPR/CCPA Compliance_

**Deterministic Hashing (`scrubPII`)**:
- **Source of Truth**: `src/utils/piiScrubber.ts`
- **Behavior**: Irreversibly hashes raw Bluetooth MAC addresses and sensitive strings using a deterministic 32-bit FNV-1a algorithm before they enter the telemetry stream (`AppLogger`).
- **Log Correlation**: The 8-character hex output (e.g., `a1b2c3d4`) allows engineering to correlate session failures and connection drops for a specific device without ever knowing its true identity or tracking it spatially.
- **Rule**: Variables named `mac` must NEVER be passed as raw payload values. Use `{ deviceId: scrubPII(mac) }`.

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

- **JAVA_HOME**: Must be set to `C:\Program Files\Android\Android Studio\jbr` (standard JBR included with Android Studio) to resolve Gradle/JDK compatibility issues for Android builds.
- **Build Type**: `release` (assembleRelease).
- **Output Artifact**: `android\app\build\outputs\apk\release\SK8Lytz.apk`

### Known Dev Environment Limitations

- **Supabase Auth (SignUp)**: Signup operations via `auth/v1/signup` may return `400 Bad Request` in local web/emulator environments due to strict redirect URI validation or rate limiting on development shards. Use "Continue Offline" or existing test credentials for UI/UX validation.

### Wear OS Build Pipeline

- **Module Path**: `android/sk8lytzWear/` — standalone Gradle subproject compiled as a Wear OS APK
- **Gradle Injection**: `plugins/withWearOsModule.js` Expo config plugin injects `include ':sk8lytzWear'` into `settings.gradle` and `wearApp project(':sk8lytzWear')` dependency into `app/build.gradle` during `npx expo prebuild`
- **Bundling**: The watch APK is automatically bundled inside the phone APK on install (Google Play requirement for Wear OS companion apps)
- **Gitignore**: Root `.gitignore` contains `!/android/sk8lytzWear/` negation rule to prevent the `/android` exclusion from silently dropping watch files (see Victory Snapshot VS-002 in `safety-protocol.md`)

### watchOS Build Pipeline

- **Target Path**: `targets/watch/` — SwiftUI watch extension via Expo Targets
- **Config**: `targets/watch/expo-target.config.js` defines bundleId, deployment target, and HealthKit entitlements
- **Entitlements**: `com.apple.developer.healthkit`, `com.apple.developer.healthkit.background-delivery`
- **Info.plist Keys**: `NSHealthShareUsageDescription`, `NSHealthUpdateUsageDescription` (required for HealthKit access)

### Watch Bridge Module

- **Module Path**: `modules/sk8lytz-watch-bridge/` — custom Expo native module
- **Platforms**: iOS (Swift `WCSession`) + Android (Kotlin `DataClient`/`MessageClient`)
- **TypeScript Entry**: `modules/sk8lytz-watch-bridge/src/index.ts`
- **Jest Mock**: `src/__mocks__/sk8lytz-watch-bridge.ts` — prevents native module crashes in unit tests

---

> [!IMPORTANT]
> To remain active, every rule file MUST contain the `trigger: always_on` YAML frontmatter.


---

## 11. Wearable Companion Architecture

_Shipped: v3.8.2 | 2026-06-03 | Commits: `5bb33b90`..`392b7496` (7 commits, 2142 insertions, 39 files)_

### 11.1 Architecture Overview

SK8Lytz companion apps for **Wear OS** (Android) and **watchOS** (Apple Watch) provide real-time session status, health telemetry, and remote session control. Both companions follow a **Display + Relay** architecture — they mirror the phone's session state and relay on-wrist health sensor data back.

> [!IMPORTANT]
> **BLE LED control is NOT on the watch roadmap.** Sending Bluetooth BLE commands directly from the watch is out of scope. The watch is a session HUD and health relay only. All LED protocol commands originate exclusively from the phone app.

```
Phone (SK8Lytz App)          â—„â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–º          Watch (Companion)
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”         Data Layer API         â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚ SessionContext    â”‚ â”€â”€â”€ speed, status, HR, cal â”€â”€â–º â”‚ DashboardScreen  â”‚
â”‚ SpeedTracking     â”‚          (push)               â”‚ HealthTracker    â”‚
â”‚ useHealthTelemetryâ”‚ â—„â”€â”€ heartRate, calories â”€â”€â”€â”€â”€â”€ â”‚ HR Sensor (5s)   â”‚
â”‚ WatchBridge moduleâ”‚         (relay)               â”‚ MessageService   â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜                                â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

#### The Three Components

| Component | Path | Language | Purpose |
|:----------|:-----|:---------|:--------|
| **watchOS companion** | `targets/watch/` | SwiftUI | Apple Watch app — session HUD, HealthKit workout, WCSession relay |
| **Wear OS companion** | `android/sk8lytzWear/` | Kotlin + Compose | Wear OS app — session HUD, Health Services ExerciseClient, DataLayer relay |
| **Expo bridge module** | `modules/sk8lytz-watch-bridge/` | Swift (iOS) + Kotlin (Android) + TypeScript | Cross-platform native bridge wiring watch events â†” React Native |

---

### 11.2 watchOS Companion (`targets/watch/`)

Built with SwiftUI as an Expo Targets watch extension.

| File | Purpose |
|:-----|:--------|
| `index.swift` | App entry point |
| `ContentView.swift` | Main session dashboard UI — speed, HR, calories, elapsed time, start/stop button |
| `HealthManager.swift` | HealthKit `HKWorkoutSession` + `HKLiveWorkoutBuilder` lifecycle; continuous HR/cal sampling |
| `WatchConnectivityManager.swift` | `WCSessionDelegate` — receives phone state pushes, relays HR/cal back to phone |
| `expo-target.config.js` | Expo Targets configuration — bundleId, deploymentTarget, entitlements (HealthKit) |

**Key Architecture Decisions:**
- Uses `HKWorkoutSession` with activity type `.rollerSkating` (not figure skating) and location type `.outdoor`
- HealthKit entitlements: `com.apple.developer.healthkit`, `com.apple.developer.healthkit.background-delivery`
- `WCSession.sendMessage` relays `{ heartRate, calories }` back to phone every 5 seconds
- The watch auto-starts a HealthKit workout when it receives `status: "ACTIVE"` from the phone
- The watch can independently send `START_SESSION` / `STOP_SESSION` commands back to the phone

---

### 11.3 Wear OS Companion (`android/sk8lytzWear/`)

Built with Jetpack Compose for Wear OS.

| File | Purpose |
|:-----|:--------|
| `MainActivity.kt` | ComponentActivity entry point with Compose theme |
| `DashboardScreen.kt` | Session HUD — speed, HR, calories, elapsed timer, start/stop button (244 lines) |
| `SessionState.kt` | Data class for session state (status, speed, heartRate, calories, startTime) |
| `WearMessageSender.kt` | Outbound `MessageClient` sender for START/STOP commands to phone |
| `WearableCommunicationService.kt` | Inbound `WearableListenerService` — receives phone state via DataLayer |
| `HealthTracker.kt` | Health Services `ExerciseClient` — continuous HR monitoring during active sessions |
| `theme/Theme.kt` | SK8Lytz dark theme (neon magenta + dark backgrounds) |

**Key Architecture Decisions:**
- Uses Health Services `ExerciseClient` with `ExerciseType.SKATING` (roller skating)
- Inbound data via `DataClient` DataLayer API (DataItems are durable, survive brief disconnects)
- Outbound commands via `MessageClient` (fire-and-forget, requires reachability)
- `HealthTracker` lifecycle: `startExercise()` on session start → `endExercise()` on session stop
- Heart rate relay: DataLayer sends `{ heartRate, calories }` on `/sk8lytz/watch-health` path every 5s
- The Wear OS module is injected into the Gradle build via `plugins/withWearOsModule.js` Expo config plugin

---

### 11.4 Watch Bridge Module (`modules/sk8lytz-watch-bridge/`)

A custom Expo native module providing the TypeScript API for phoneâ†”watch communication.

**TypeScript API** (`src/index.ts`):

```typescript
WatchBridge.syncSessionState(state: WatchSessionState)     // Phone → Watch (session state push)
WatchBridge.sendMetricUpdate(metrics)                        // Phone → Watch (live metric snapshot)
WatchBridge.isWatchReachable(): Promise<boolean>             // Connection check
WatchBridge.addWatchCommandListener(handler)                 // Watch → Phone (START/STOP commands)
WatchBridge.addWatchHealthListener(handler)                  // Watch → Phone (HR/cal relay)
```

**Types:**

```typescript
interface WatchSessionState {
  status: 'ACTIVE' | 'STOPPED';
  speed?: number;
  heartRate?: number;
  calories?: number;
  startTime?: string; // ISO 8601
}

type WatchCommand = 'START_SESSION' | 'STOP_SESSION';

interface WatchHealthUpdate {
  heartRate: number;
  calories: number;
}
```

**Native Implementations:**
- **iOS** (`ios/Sk8lytzWatchBridgeModule.swift`): Uses `WCSession` for all communication
- **Android** (`android/.../Sk8lytzWatchBridgeModule.kt`): Uses `DataClient` (push state) + `MessageClient` (commands)
- **Mock** (`src/__mocks__/sk8lytz-watch-bridge.ts`): Jest mock for unit testing without native modules

---

### 11.5 Phone-Side Integration (SessionContext + SpeedTracking)

**SessionContext** (`src/context/SessionContext.tsx`):
- On session start: calls `WatchBridge.syncSessionState({ status: 'ACTIVE', startTime })`
- On session stop: calls `WatchBridge.syncSessionState({ status: 'STOPPED' })`
- Subscribes to `WatchBridge.addWatchCommandListener()` — watch can remotely start/stop sessions
- Subscribes to `WatchBridge.addWatchHealthListener()` — relays to `mergeWatchHealth()`

**SpeedTrackingService** (`src/services/SpeedTrackingService.ts`):
- Pushes live GPS speed to watch via `WatchBridge.sendMetricUpdate({ speed })` during active sessions
- Throttled to prevent BLE saturation

---

### 11.6 Health Data Priority Architecture (Watch-Preferred)

_Lives in: `src/hooks/useHealthTelemetry.ts` | Shipped: commit `392b7496`_

The health telemetry system implements a **watch-preferred** priority model:

🏃 Watch connected & sending data:
   Watch HR/cal —> mergeWatchHealth() —> ALWAYS writes to state
   Phone poll fires every 15s —> sees isWatchHealthActive() = true —> SKIPS
   Result: Dashboard HUD shows 5s-fresh watch sensor data

📱 Watch disconnected / out of range:
   No watch relay for 15s —> isWatchHealthActive() = false
   Phone poll resumes —> reads HealthKit (iOS) / Health Connect (Android)
   Result: Seamless fallback — no user intervention needed

| Property | Value |
|:---------|:------|
| Phone poll interval | 15 seconds |
| Watch relay interval | 5 seconds |
| Watch expiry timeout | 15 seconds (`WATCH_EXPIRY_MS`) |
| Priority logic | `lastWatchHealthMsRef` timestamp gating |
| Telemetry event | `phone_health_poll_deferred` logged when watch suppresses phone |

**Why watch wins:** The watch has a direct optical HR sensor on the wrist sampling every 1-5 seconds. Phone HealthKit/Health Connect polls synced data at 15s intervals — always staler. When both are available, the watch's real-time relay is the gold standard.

**Consumers of health data (all read from `useHealthTelemetry` state):**
- Dashboard HUD (controller top bar)
- Session summary (`useSessionTracking` - [MOVE_TO_ARCHIVE])
- Crew Hub session telemetry
- Street mode (G-force: phone accelerometer only — watch does NOT relay G-force)
- `skate_sessions` Supabase writes

> [!IMPORTANT]
> **GPS Speed and G-Force remain phone-only.** The watch does NOT relay GPS or accelerometer data to save battery. Speed comes from `expo-location` on the phone. G-force comes from `expo-sensors` on the phone. The phone pushes speed TO the watch for display only.

---

### 11.7 Future Watch Enhancements (Planned) [MOVE_TO_ARCHIVE]

| Feature | Plan | Layer | Size | Platform |
|:--------|:-----|:------|:-----|:---------|
| Session Duration Timer | `tools/plans/PLAN-session-duration-timer.md` | UI | Snack | Both |
| watchOS Complications | `tools/plans/PLAN-watchos-complications.md` | UI | Snack | watchOS |
| Wear OS Tiles | `tools/plans/PLAN-wearos-tiles.md` | UI | Meal | Wear OS |
| Wear OS Always-On Display | `tools/plans/PLAN-wearos-always-on.md` | UI | Snack | Wear OS |
| Wear OS Ongoing Activity | `tools/plans/PLAN-wearos-ongoing-activity.md` | UI | Snack | Wear OS |

---

## 12. ZENGGE PROTOCOL BIBLE (Canonical Reference)

> [!IMPORTANT]
> The **authoritative** Zengge Protocol Bible lives in the standalone file:
> **`tools/ZENGGE_PROTOCOL_BIBLE.md`**
>
> That file is the single source of truth for all hardware opcodes, transport framing, EEPROM commands, chipset constraints, and Oracle Lab validation results. Do NOT duplicate protocol content here — reference the standalone Bible by section number (e.g., "See Protocol Bible §3 0x59" or "See Protocol Bible §11 Oracle Validation").
>
> **Last verified: 2026-06-06** — All opcode documentation, bug verdicts, and Oracle ground-truth findings are current in the standalone file.

> [!CAUTION]
> **DEDUPLICATION NOTE (2026-06-06):** The previous inline copy of the Protocol Bible (870+ lines) was removed from this file to eliminate drift risk. The standalone `ZENGGE_PROTOCOL_BIBLE.md` was diverging from the inline copy silently. All protocol lookups must reference the standalone file exclusively.




## 12. Auto-Compiled Domain Architecture

> [!NOTE]
> This section is strictly managed by the Google Antigravity /deepdive-docs Cartographer Fleet.

### Domain: IDENTITY
<!-- CARTOGRAPHER_START: IDENTITY -->

# 🕵️ IDENTITY Domain Cartography

This document contains a comprehensive architectural audit of the **IDENTITY** domain within the SK8Lytz application codebase.

---

## 1. File Manifest

Every file within the Identity domain serves a specific, documented architectural purpose:

| File Path | Architectural Purpose |
| :--- | :--- |
| `src/context/AuthContext.tsx` | Global authentication state provider; manages Supabase auth events, deep-link session handlers, token refresh loops, and local offline-mode fallback logic. |
| `src/services/AuthProfileService.ts` | Handles CRUD operations for skater profiles, avatar hue configs, profile photo storage uploads, and session logs tracking in Supabase. |
| `src/services/AuthUtils.ts` | Provides pure security utilities for password validation, profanity sanitization, and HaveIBeenPwned (HIBP) validation. |
| `src/services/ProfileService.ts` | Centralized barrel facade delegating execution context to specialized singletons (`AuthProfileService`, `CrewProfileService`, and `PushTokenService`). |
| `src/services/ProfileService.types.ts` | Standardizes interfaces and TypeScript definitions for profile records, crew definitions, and session history metadata. |
| `src/hooks/useAccountOverview.ts` | High-complexity aggregation hook managing forms, avatar pickers, settings preferences, and crew actions within the Account UI context. |
| `src/hooks/useDashboardProfile.ts` | UI-facing hook that coordinates reactive crew updates, sign-out commands, and username configuration logic for the main dashboard screen. |
| `src/hooks/useRegistration.ts` | React hook facade interfacing with `DeviceRepository` to manage paired LED skate hardware. |
| `src/components/auth/AuthHeader.tsx` | Renders branding details and logo with navigation shortcuts. |
| `src/components/auth/AuthStyles.ts` | Calculates styling values dynamically based on system safe-area insets and theme selections. |
| `src/components/auth/AuthFooterActions.tsx` | Coordinates continue-offline selections and stores persistent skips inside local AsyncStorage. |
| `src/components/auth/AuthFormForgotPassword.tsx` | Form interface prompting password recovery email dispatches. |
| `src/components/auth/AuthFormSignIn.tsx` | Directs email/username validations and handles credentials verification. |
| `src/components/auth/AuthFormSignUp.tsx` | Validates registration requirements, checks password complexity/breach databases, and confirms EULA acceptance. |
| `src/components/auth/DevSandboxDrawer.tsx` | Exposes sandbox developer switches for Virtual Skates simulation and storage hard-nuke tools in non-production builds. |
| `src/components/account/types.ts` | Declares shared component properties and event callback signatures for all sub-tab views. |
| `src/components/account/AccountTabProfile.tsx` | Editable input fields for Display Name, Username/Handle, and avatar color hue slider. |
| `src/components/account/AccountTabSecurity.tsx` | Contains password changes, email change validations, and permission switches. |
| `src/components/account/AccountTabSettings.tsx` | Displays notification switches, health syncing configs, auto-pause features, and account deletion options. |
| `src/components/account/AccountTabCrewz.tsx` | Renders crew lists, options to leave/delete a crew, and code-based crew join interfaces. |
| `src/components/account/AccountTabDevices.tsx` | Displays registered soul/lightning hardware segments with status indicators and rename/forget actions. |
| `src/components/account/AccountTabStats.tsx` | Displays aggregated mileage metrics, top patterns, colors aura, and crew participation details. |
| `src/components/account/SkaterStatsPanel.tsx` | Renders calculated user metrics wrapped in a seasonal "Wrapped-style" display fetched offline-first. |

---

## 2. Blast Radius (Dependency Map)

```mermaid
graph TD
    %% Core Library/System Dependencies
    Supabase[supabase-js] --> AuthContext[AuthContext.tsx]
    AsyncStorage[@react-native-async-storage/async-storage] --> AuthContext
    ExpoSession[expo-auth-session] --> AuthFormSignIn
    ExpoSession --> AuthFormSignUp
    ExpoSession --> AuthFormForgotPassword
    ExpoImagePicker[expo-image-picker] --> useAccountOverview
    
    %% Internal Identities Domain
    AuthContext --> useDashboardProfile
    AuthContext --> useAccountOverview
    AuthProfileService[AuthProfileService.ts] --> AuthContext
    AuthProfileService --> ProfileService[ProfileService.ts]
    AuthUtils[AuthUtils.ts] --> AuthFormSignUp
    
    %% Consuming Clients
    useDashboardProfile --> DashboardScreen[DashboardScreen.tsx]
    useAccountOverview --> AccountModal[AccountModal.tsx]
    AccountModal --> App[App.tsx]
```

### Imports (Inbound Dependencies)
- **Authentication & Core Libraries**: `@supabase/supabase-js` (via `src/services/supabaseClient.ts`), `expo-auth-session`, `@react-native-async-storage/async-storage`.
- **Media & File Handlers**: `expo-image-picker`, `expo-file-system`.
- **Global Services**: `AppLogger`, `DeviceRepository`, `PermissionService`, `NotificationService`, `AppSettingsService`.

### Exports (Outbound Consumers)
- `AuthProvider` wraps `App.tsx` at the root.
- `useAuth` is consumed globally across screens (`AuthScreen`, `DashboardScreen`, `ComplianceGate`).
- `useDashboardProfile` is consumed inside the main `DashboardScreen`.
- `useAccountOverview` is consumed by the comprehensive `AccountModal`.

---

## 3. Context Matrix

| React Context | Provided By | Consumed By | Key Exposed Data / Handlers |
| :--- | :--- | :--- | :--- |
| **`AuthContext`** | `AuthProvider` | Global (via `useAuth`) | `user`, `session`, `profile`, `offlineMode`, `signIn()`, `signUp()`, `signOut()`, `setOfflineMode()`. |
| **`ThemeContext`** | `ThemeProvider` | Auth Forms, Headers, and Account Tab components | Theme colors configuration, dark/light mode switcher, dynamic borders styling. |
| **`SafeAreaContext`** | `SafeAreaProvider` | `AuthStyles.ts` | Calculates custom layout margins based on OS-level notches, status bars, and navigation offsets. |

---

## 4. Hook/Service I/O Registry

### Services

#### `AuthProfileService`
- **`fetchOrCreateProfile(user)`**
  - **Inputs**: `user: User | null` (Supabase User metadata).
  - **Outputs**: `Promise<UserProfile | null>`.
  - **Side-Effects**: Performs profile lookups; self-heals by auto-inserting missing database profile tables when matching metadata is present.
- **`updateProfile(userId, fields)`**
  - **Inputs**: `userId: string`, `fields: Partial<UserProfile>`.
  - **Outputs**: `Promise<void>`.
  - **Side-Effects**: Mutates profile details; triggers image uploads when custom photo values are provided.
- **`getSessionHistory(userId)`**
  - **Inputs**: `userId?: string`.
  - **Outputs**: `Promise<SessionHistoryItem[]>`.
  - **Side-Effects**: Reads skater session association arrays from Supabase.

#### `AuthUtils`
- **`checkPasswordComplexity(password)`**
  - **Inputs**: `password: string`.
  - **Outputs**: `PasswordStrength` (`{ score, color, label, errors }`).
  - **Side-Effects**: None (Pure calculation).
- **`checkHIBP(password)`**
  - **Inputs**: `password: string`.
  - **Outputs**: `Promise<{ pwned: boolean, count: number }>`.
  - **Side-Effects**: Dispatches REST queries to HaveIBeenPwned API.

---

### Hooks

#### `useAccountOverview(visible, onProfileUpdated)`
- **Inputs**: `visible: boolean`, `onProfileUpdated?: () => void`.
- **Outputs**: States (active tabs, edits, loading flags) and event callbacks (`handleSaveProfile`, `handlePickProfilePhoto`, `handleCreateCrew`, `handleJoinCrew`, `handleDeleteCrew`, `handleLeaveCrew`, `handleChangePassword`, `handleChangeEmail`).
- **Side-Effects**: Reloads data arrays (crews, history, devices, stats) when the modal is active.

#### `useDashboardProfile(options)`
- **Inputs**: `{ onCrewJoinNotification?: (crewName: string) => void }`.
- **Outputs**: `profile`, `loading`, `authUsername`, `signOut()`, modal UI visibility states.
- **Side-Effects**: Sets up real-time socket channels on Supabase tables to listen for crew join events.

#### `useRegistration()`
- **Inputs**: None.
- **Outputs**: `devices: StoredDevice[]`, `loading: boolean`, `saveRegisteredDevice()`, `deregisterDevice()`.
- **Side-Effects**: Interfaces with `DeviceRepository` to manage paired LED hardware states.

---

## 5. OS Variance Matrix

| Feature / Branch | iOS Handling | Android Handling |
| :--- | :--- | :--- |
| **Monospace Fonts** | Utilizes `Courier New` font family (in `AccountTabCrewz.tsx`). | Utilizes system `monospace` font family. |
| **Monospace Console Logs** | Utilizes `Menlo` for system console inputs. | Utilizes standard `monospace`. |
| **Safe Area Insets** | Compensates for the dynamic notch height offset dynamically. | Accommodates standard status bar overlays. |
| **Web Compatibility** | Wraps authentication interfaces with standard container fragments. | Uses `WebFormWrapper` form overlays when testing on browser interfaces to suppress page reloading on form submission. |

---

## 6. Authentication & Self-Healing Sequence

The diagram below details the reactive sign-in pipeline, showing how user profile data is resolved and self-healed using auth metadata.

```mermaid
sequenceDiagram
    actor Skater as Skater
    participant Form as AuthFormSignIn
    participant Context as AuthContext
    participant Client as SupabaseClient
    participant ProfileService as AuthProfileService
    participant DB as Supabase DB

    Skater->>Form: Enters Email/Password & Presses Login
    Form->>Context: Calls signIn(email, password)
    Context->>Client: Calls auth.signInWithPassword()
    Client->>DB: Validates credentials
    DB-->>Client: Returns Auth Session Token
    Client-->>Context: Triggers onAuthStateChange (SIGNED_IN)
    
    rect rgb(20, 30, 40)
        Note over Context, DB: Profile Self-Healing Protocol
        Context->>ProfileService: Calls fetchOrCreateProfile(user)
        ProfileService->>DB: Queries profiles table for user_id
        alt Profile exists
            DB-->>ProfileService: Returns UserProfile record
        else Profile is missing (Needs Self-Healing)
            ProfileService->>DB: Inserts new UserProfile (falls back to username from raw metadata)
            DB-->>ProfileService: Returns newly created UserProfile
        end
        ProfileService-->>Context: Returns profile record
    end

    Context-->>Form: Authentication successful
    Context->>Form: Cleans up login form states
```

---

## Architectural Impact Flags

`[IMPACTS_USER_JOURNEY]`

---

<!-- CARTOGRAPHER_END: IDENTITY -->

### Domain: BLE_CORE
<!-- CARTOGRAPHER_START: BLE_CORE -->

# BLE_CORE Domain Cartography Report

_Last Updated: 2026-06-11 | Read-Only Architectural Audit_

---

## 1. File Manifest

Every file in the `BLE_CORE` domain is listed below with its 1-sentence architectural purpose:

### Services (`src/services/ble/*` and `src/services/Ble*`)
1. **[BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)**: The central XState v5 state machine that orchestrates scanning, connection, heartbeat, and auto-recovery lifecycles.
2. **[BleMachine.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts)**: Declares strict TypeScript types for BleMachine context, events, state values, and phase tags.
3. **[ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)**: An XState `fromPromise` actor that manages sequential GATT connection, MTU negotiation, protocol adapter mapping, and time synchronization handshakes for device groups.
4. **[HeartbeatService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)**: An XState `fromCallback` actor that executes periodic (45s) liveness pings via `0x63` EEPROM queries or RSSI reads to identify and prune dead GATT handles.
5. **[InterrogatorService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)**: Service managing the background hardware query queue (`createProbeQueue`) to fetch EEPROM configurations, populate caching layers, and prevent scanner performance bottlenecks.
6. **[README.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/README.md)**: Architectural tripwire warning documenting domain rules (e.g., Hollow Shell pattern, Co-Location Law).
7. **[RSSIService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RSSIService.ts)**: Pure TypeScript logic containing the periodic (30s) RSSI polling loop and signal strength evaluations.
8. **[RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)**: An XState `fromCallback` actor executing a 3-phase recovery loop (Aggressive GATT Hammering -> Moderate Backoff -> Passive Sweeper Watch) for dropped devices.
9. **[BleCharacteristicCache.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleCharacteristicCache.ts)**: AsyncStorage-backed cache that persists mapped device protocol adapter IDs for 24 hours to skip UUID discovery on subsequent connects.
10. **[BlePingService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)**: Atomic utility that executes a wizard-exclusive setup sequence (Connect -> Blink -> Probe EEPROM -> Turn Off -> Disconnect) bypassing the Fleet connected devices requirement.
11. **[BleSessionFactory.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleSessionFactory.ts)**: A centralized, abortable factory utility that enforces the mandatory connect-discover-resolve sequence across all BLE callsites.
12. **[BleWriteDispatcher.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)**: Engine that manages pattern/color debouncing, chunked sequence execution, and multi-device write serialization gaps.
13. **[BleWriteQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)**: A singleton priority-based write serialization queue enforcing a maximum queue depth of 8 and sequential drains to satisfy Android BLE constraints.

### Hooks (`src/hooks/useBLE.ts`, `src/hooks/ble/*`, `src/hooks/useOptimisticBLE.ts`)
14. **[useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)**: The primary application React hook wrapping the BLE subsystem and acting as the main interface between React views and the XState `BleMachine`.
15. **[useBLEBatterySweep.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts)**: State-machine-coupled hook that implements power-aware scanning duty cycles (e.g. THROTTLED tier) and manages Android API 31+ scan budgets.
16. **[useBLEInterrogator.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEInterrogator.ts)**: Thin hook wrapper synchronizing the background `InterrogatorService` queue results with React state.
17. **[useBLERSSIMonitor.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLERSSIMonitor.ts)**: React hook delegating RSSI polling to `RSSIService` and managing state pruning on device disconnection.
18. **[useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)**: Main scanner hook coordinating sweeper transitions, sandbox mock modes, and discovery callbacks.
19. **[useOptimisticBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useOptimisticBLE.ts)**: UI-focused hook implementing optimistic state updates, background writes, haptic feedback, and rollback triggers.

### Contexts (`src/context/BLEContext.tsx`)
20. **[BLEContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/BLEContext.tsx)**: React Context and Provider exposing the `BluetoothLowEnergyApi` instance globally.

---

## 2. Blast Radius (Imports & Exports)

```
                       ┌────────────────────────┐
                       │      EXTERNAL LIBS     │
                       │ react-native-ble-plx   │
                       │ react-native, xstate   │
                       └───────────┬────────────┘
                                   │
                                   ▼
┌──────────────────────────────────────────────────────────────────────┐
│                            BLE_CORE DOMAIN                           │
│                                                                      │
│  ┌───────────────────────┐             ┌──────────────────────────┐  │
│  │     Core Services     ├────────────►│       React Hooks        │  │
│  │  BleMachine, Connect,  │             │   useBLE, useScanner,    │  │
│  │  Recovery, Session   │             │   useOptimisticBLE       │  │
│  └───────────────────────┘             └────────────┬─────────────┘  │
└─────────────────────────────────────────────────────┼────────────────┘
                                                      │
                                                      ▼
                                         ┌────────────┴─────────────┐
                                         │       CONSUMERS          │
                                         │  HardwareSetupWizard     │
                                         │  DashboardScreen         │
                                         │  DockedController        │
                                         │  AdminToolsModal         │
                                         └──────────────────────────┘
```

### Imports (External Dependencies)
* **`react-native-ble-plx`**: Consumes `BleManager`, `Device`, `State`, `BleError`, and `Characteristic` for hardware interaction.
* **`react-native`**: Consumes `Platform.OS` (branching), `AppState` (wake/sleep pruning), `DeviceEventEmitter` (sandbox toggles), and `Alert`.
* **`@react-native-async-storage/async-storage`**: Cache keys for GATT adapters, virtual demo modes, and hardware blacklists.
* **`xstate` & `@xstate/react`**: Relies on FSM utilities (`setup`, `assign`, `fromPromise`, `fromCallback`, `useMachine`).
* **`buffer`**: Encodes/decodes packets.
* **`expo-haptics`**: Invokes light/error haptic feedback.
* **`src/protocols/*`**: Maps protocols via `ControllerRegistry`, `IControllerProtocol`, and adapter classes.
* **`src/utils/*`**: Uses `piiScrubber` (telemetry protection) and `backoff` (jittered backoff math).

### Exports (System-Wide Consumers)
* **`HardwareSetupWizardScreen`**: Consumes `pingDevice`, `pendingRegistrations`, `scanForPeripherals`, and `setPendingRegistrations` to discover, blink, and configure new skates.
* **`DashboardScreen` / `Dashboard`**: Consumes `useSharedBLE` context to verify connected fleet sizes, display active statuses, and initiate connections.
* **`DockedController.tsx`**: Consumes write states (`writeStatus`, `isPending`) to display connection health status and trigger optimistic updates.
* **`AdminToolsModal` & `DiagnosticLab`**: Inspects `allDevices`, custom EEPROM caches, and sends low-level payloads to diagnostic screens.
* **`BackgroundSessionService`**: Reads `connectedDevices` to log offline metrics in the background.

---

## 3. Context Matrix

### Consumed Contexts
* **`useRegistration`**: Called in `BLEProvider` to fetch list of registered MAC addresses (`registeredDevices`) to bootstrap connection requests.

### Provided Contexts
* **`BLEContext`**: Created in `BLEContext.tsx` and exposed through `BLEProvider`. It injects the full `BluetoothLowEnergyApi` into the React component tree.
* **`useSharedBLE`**: Convenience hook exported to consume `BLEContext` safely with null-gating checks.

---

## 4. Hook/Service I/O Registry

### `useBLE` (React Hook)
* **Inputs**:
  * `registeredMacs: string[]` (List of hardware MAC addresses registered to the user)
* **Outputs**: `BluetoothLowEnergyApi` (Object containing scanning, connection, write, and status methods)
* **Side-Effects**: 
  * Spawns the XState `BleMachine` actor.
  * Audits connection states on app wake-up (removes stale device handles).
  * Performs background blacklist queries from Supabase.

### `useOptimisticBLE` (React Hook)
* **Inputs**:
  * `writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>`
  * `onReconcile?: () => void` (UI rollback callback)
  * `debounceMs?: number` (Default: `50`ms)
  * `disableOptimisticUI?: boolean`
  * `disableHaptics?: boolean`
* **Outputs**:
  * `optimisticWrite: (payload: number[], onOptimistic?: () => void, targetDeviceId?: string) => Promise<boolean>`
  * `directWrite: (payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>`
  * `writeStatus: BLEWriteStatus` (`'IDLE' | 'PENDING' | 'CONFIRMED' | 'RECONCILED'`)
  * `isPending: boolean`
  * `isReconciled: boolean`
* **Side-Effects**: 
  * Triggers immediate UI feedback.
  * Invokes `Haptics` patterns based on write completion status.

### `useBLEScanner` (React Hook)
* **Inputs**:
  * `bleManager: BleManager | null`
  * `allDevices: Device[]`
  * `setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>`
  * `bleSend: (event: BleMachineEvent) => void`
  * `registeredMacs: string[]`
  * `isSandboxEnabled: boolean`
* **Outputs**:
  * `scanCallback: (error: BleError | null, device: Device | null) => void`
  * `startSweeper: () => void`
  * `stopScanner: () => void`
  * `burstScan: (durationMs?: number) => void`
  * `isSweeperActive: boolean`
  * `batteryTier: 'FULL' | 'THROTTLED' | 'PAUSED'`
  * `hwCache: Record<string, any>`
  * `pendingRegistrations: PendingRegistration[]`
  * `setPendingRegistrations: React.Dispatch<React.SetStateAction<PendingRegistration[]>>`
* **Side-Effects**:
  * Spawns periodic sweeper polling.
  * Dynamically schedules background hardware interrogations on scanned devices.

### `useBLEBatterySweep` (React Hook)
* **Inputs**:
  * `bleSend: (event: BleMachineEvent) => void`
* **Outputs**:
  * `startSweeper: () => void`
  * `stopSweeper: () => void`
  * `isSweeperActive: boolean`
  * `batteryTier: 'FULL' | 'THROTTLED' | 'PAUSED'`
* **Side-Effects**:
  * Registers power monitoring listeners.
  * Implements duty-cycle scanning and Android >= 31 start budget throttle loops.

### `BleWriteQueue` (Service Singleton)
* **API**:
  * `enqueueWrite(priority: WritePriority, execute: () => Promise<boolean | 'partial'>, generation?: number): Promise<boolean | 'partial'>`
  * `enqueueDelay(priority: WritePriority, delayMs: number, generation?: number): Promise<boolean | 'partial'>`
  * `clearWriteQueue(): void`
  * `setWriteQueueGeneration(gen: number): void`
* **Side-Effects**:
  * Drops normal/bulk writes on queue saturation (depth >= 8) to enforce backpressure.
  * Prunes stale generation-tagged writes during sequential queue draining.

### `executePingDevice` (Service Function)
* **Inputs**:
  * `bleManager: BleManagerPingLike`
  * `mac: string`
  * `blinkPayload: number[]`
  * `options?: { probe?: boolean; duration?: number; turnOffAtEnd?: boolean }`
* **Outputs**: `Promise<PingResult | null>` (Resolved hardware metadata or null if timed out)
* **Side-Effects**:
  * Controls atomic connection lifecycle.
  * Schedules staggered time and RF queries via `BleWriteQueue`.
  * Monitors notify characteristics and disconnects cleanly in the `finally` block.

---

## 5. OS Variance Matrix

| Feature | iOS Behavior | Android Behavior |
|:---|:---|:---|
| **MTU Negotiation** | Handled natively by iOS BLE stack. The app forces a default cached value of `186` bytes. | Explicitly requests MTU via `conn.requestMTU(512)`. Implements a 2-attempt retry loop with exponential settle delays to recover from native 23-byte MTU lockups. |
| **Connection Priority** | Natively managed by iOS. Priority parameters are ignored. | Requests `HIGH` priority (`1`) during connection/handshake, then downgrades to `BALANCED` (`0`) after sync to reduce battery consumption. |
| **Scan Throttling** | No OS-enforced start frequency limits. Sweeper runs at full duty cycle. | Enforces Android 31+ scan budget (`max 4 scan starts per 30s`). Defers starts using `msUntilBudgetResets` calculations when threshold is exceeded. |
| **GATT 133 Errors** | Non-existent. Connection failures mapped to standard iOS error codes. | Native Android stack frequently throws `133 (0x85)` on congested bands. Connect paths implement 3-attempt backoff loops and invoke `refreshGatt: 'OnConnected'` to purge device caches. |
| **Haptic Feedback** | Calls `expo-haptics` directly. | Calls `expo-haptics` directly. |
| **Web Platform Guards** | N/A | Bypasses native library initialization when `Platform.OS === 'web'`, instating visualizers in simulated loopbacks instead. |

---

## 6. XState FSM State Machine Map

The finite state machine orchestrated by `BleMachine.ts` manages transitions across six primary states:

```mermaid
stateDiagram-v2
    [*] --> IDLE
    
    IDLE --> SCANNING : SCAN_START / setSweeperId
    IDLE --> CONNECTING : CONNECT_REQUEST / setTargetMacs
    IDLE --> DISCONNECTING : DISCONNECT_REQUEST
    IDLE --> RECOVERING : RECOVERY_START / setGhostedMacs
    IDLE --> IDLE : UPDATE_CONNECTED_DEVICES / setConnectedDevices
    
    SCANNING --> IDLE : SCAN_STOP / clearSweeperId
    SCANNING --> CONNECTING : CONNECT_REQUEST / clearSweeperId, setTargetMacs
    SCANNING --> DISCONNECTING : DISCONNECT_REQUEST / clearSweeperId
    SCANNING --> RECOVERING : RECOVERY_START / setGhostedMacs
    
    CONNECTING --> READY : CONNECT_SUCCESS or connectService.done / setConnectedDevices
    CONNECTING --> IDLE : CONNECT_FAIL or connectService.error
    CONNECTING --> RECOVERING : RECOVERY_START / setGhostedMacs
    CONNECTING --> DISCONNECTING : DISCONNECT_REQUEST
    
    READY --> RECOVERING : HEARTBEAT_FAIL / setGhostedMacs
    READY --> RECOVERING : RECOVERY_START [ghostedMacs.length < 2] / setGhostedMacs
    READY --> CONNECTING : RECOVERY_START [ghostedMacs.length >= 2] / setTargetMacs (group recovery)
    READY --> DISCONNECTING : DISCONNECT_REQUEST
    READY --> IDLE : UPDATE_CONNECTED_DEVICES [devices.length === 0] / setConnectedDevices
    
    DISCONNECTING --> IDLE : DISCONNECT_COMPLETE / clearConnectedDevices, clearGhostedMacs
    
    RECOVERING --> READY : RECOVERY_COMPLETE / append reconnectedDevices
    RECOVERING --> CONNECTING : CONNECT_REQUEST / setTargetMacs
    RECOVERING --> IDLE : RECOVERY_PERMANENTLY_FAILED / setDeviceUnreachable, notifyUser, clearGhostedMacs
    RECOVERING --> IDLE : RECOVERY_FAIL / clearGhostedMacs
    RECOVERING --> DISCONNECTING : DISCONNECT_REQUEST

    state ANY_STATE {
        [*] --> FORCE_IDLE_HANDLER
        FORCE_IDLE_HANDLER --> IDLE : FORCE_IDLE / clearSweeperId, clearGhostedMacs
    }
```

---

## 7. BLE Transport Pipeline Map

Commands sent from user interface sliders or buttons traverse a highly serialized pipeline to prevent GATT saturation:

```mermaid
graph TD
    UserAction[User/UI Command] --> |writeToDevice / writeChunked / executeProtocolResults| useBLE[useBLE.ts]
    useBLE --> |enqueueWrite| BleWriteQueue[BleWriteQueue.ts]
    
    subgraph Write Queue Serialization
        BleWriteQueue --> |Priority Insertion| Queue[(Queue - Max 8 Entries)]
        Queue --> |Drop Bulk / Normal| Backpressure[Backpressure Manager]
        Queue --> |Generation Check| Pruning[Stale Write Pruning]
        Queue --> |Sequential Drain loop| SingleWrite[1 Outstanding Write Lock]
    end
    
    SingleWrite --> |execute| BleWriteDispatcher[BleWriteDispatcher.ts]
    
    subgraph Write Dispatcher Processing
        BleWriteDispatcher --> |padStaticColorfulPayload| ColorfulPad[Zengge Opcode 0x59 Padding]
        BleWriteDispatcher --> |buildChunkedFrames| ChunkedFrames[Zengge Opcode 0x40 Chunking]
        BleWriteDispatcher --> |50ms Gap between devices| MultiDeviceGap[Inter-Device Settle Delay]
    end
    
    ColorfulPad --> WriteExec[writeCharacteristicWithoutResponseForService]
    ChunkedFrames --> WriteExec
    MultiDeviceGap --> WriteExec
    
    WriteExec --> |Native BLE Stack| reactNativeBlePlx[react-native-ble-plx]
    reactNativeBlePlx --> |GATT Write| PhysicalSkate[Physical Skate Controller]
```

> [!NOTE]
> **Refactor Note:** The legacy `BleConnectionManager.ts` has been deprecated and deleted. Connection mapping and device monitoring tasks are split between `ConnectService` and `BleWriteDispatcher`.

---

## 8. Sequence Diagrams

### Group Connection & Handshake Flow

The sequential flow below visualizes how multiple skates are connected, handshaked, time-synchronized, and downgraded to lower latency connections:

```mermaid
sequenceDiagram
    autonumber
    actor App as React Component (Wizard/Dashboard)
    participant useBLE as useBLE.ts
    participant Machine as BleMachine (XState)
    participant CS as ConnectService (Actor)
    participant SF as BleSessionFactory.ts
    participant Cache as BleCharacteristicCache
    participant PLX as react-native-ble-plx (BleManager)
    participant Dev as Physical Skate (GATT)

    App->>useBLE: connectToDevices(devices)
    useBLE->>Machine: send CONNECT_REQUEST
    Machine->>Machine: Transition IDLE/SCANNING -> CONNECTING
    Note over Machine: Invoke ConnectService Actor
    Machine->>CS: Start connectService(targetMacs)
    
    loop For each MAC address
        CS->>PLX: isDeviceConnected(mac)
        alt Not Connected Natively
            CS->>PLX: connectToDevice(mac)
            PLX->>Dev: GATT Connect
            Dev-->>PLX: Connect Success
            PLX-->>CS: Device Handle
        else Already Connected Natively
            PLX-->>CS: Retrieve existing Device Handle
        end
    end
    
    loop For each Connected Device Handle
        CS->>SF: createGattSession(bleManager, mac)
        SF->>Dev: discoverAllServicesAndCharacteristics()
        Dev-->>SF: Services & Characteristics list
        
        SF->>Cache: get(mac)
        alt Cache Hit
            Cache-->>SF: protocolId (e.g. Zengge)
        else Cache Miss
            SF->>Dev: services() UUID search
            Dev-->>SF: serviceUUIDs
            SF->>SF: resolveProtocol(serviceUUIDs)
            SF->>Cache: set(mac, protocolId)
        end
        SF-->>CS: conn, adapter, usedCache
        
        Note over CS: Android Specific priority boost
        CS->>PLX: requestConnectionPriority(HIGH)
        
        Note over CS: Android MTU Negotiation
        CS->>Dev: requestMTU(512)
        Dev-->>CS: negotiated MTU (e.g. 186)
        
        CS->>PLX: onDeviceDisconnected(mac) subscription
        CS->>Dev: monitorCharacteristicForService(notifyUUID)
        
        Note over CS: Sync Time Handshake
        CS->>CS: adapter.getHandshakePayloads()
        CS->>useBLE: enqueueWrite(critical, handshakePayload)
        useBLE->>Dev: writeCharacteristicWithoutResponse (0x10 sync)
        
        Note over CS: Android Settle Settle
        CS->>PLX: requestConnectionPriority(BALANCED)
    end
    
    CS-->>Machine: send CONNECT_SUCCESS / UPDATE_CONNECTED_DEVICES
    Machine->>Machine: Transition CONNECTING -> READY
    Machine->>useBLE: Update connectedDevices context
    useBLE-->>App: Re-render UI with connected states
```

### Proactive Auto-Recovery Flow

If a skate drops out of RF range or throws heartbeat failures, the state machine invokes the `RecoveryService` actor:

```mermaid
sequenceDiagram
    autonumber
    participant Dev as Physical Skate (GATT)
    participant PLX as react-native-ble-plx (BleManager)
    participant useBLE as useBLE.ts
    participant Machine as BleMachine (XState)
    participant RS as RecoveryService (Actor)
    participant SF as BleSessionFactory.ts
    participant WQ as BleWriteQueue

    Note over Dev, PLX: Skate drops out of RF range / experiences collision
    PLX->>useBLE: onDeviceDisconnected Callback fires
    useBLE->>Machine: send RECOVERY_START (ghostedMacs)
    Machine->>Machine: Transition READY -> RECOVERING
    Note over Machine: Invoke RecoveryService Actor
    Machine->>RS: Start recoveryService(ghostedMacs)
    
    RS->>WQ: clearWriteQueue() (Purge stale pattern writes)
    WQ-->>RS: Queue cleared
    
    loop Phase 1 & 2 GATT Hammering (attempts <= 35)
        RS->>RS: Calculate Jittered Backoff Delay
        alt Aborted/Cancelled
            Note over RS: Exit recovery
        end
        
        Note over RS: GATT Reconnect Attempt
        RS->>SF: createGattSession(bleManager, mac, timeout=3500, retries=1)
        SF->>PLX: connectToDevice(mac)
        alt Success
            PLX-->>SF: conn
            SF->>Dev: discoverAllServicesAndCharacteristics()
            SF-->>RS: conn, adapter
            Note over RS: GATT Connection Re-established!
            RS->>PLX: requestMTU(512)
            RS->>PLX: onDeviceDisconnected subscription
            RS->>Dev: monitorCharacteristicForService(notifyUUID)
            RS->>WQ: enqueueWrite(critical, recoveryPing)
            WQ->>Dev: writeCharacteristicWithoutResponse (0x63 query)
            Note over RS: Recovery Success! Break loop.
        else Failure
            PLX-->>SF: Error (e.g. GATT 133)
            SF-->>RS: Error
        end
    end
    
    alt Reconnect Failed in Phase 1 & 2
        Note over RS: Enter Phase 3 Passive Watch
        loop Phase 3 Passive Sweeper Polls (up to 120 polls, 5s interval)
            RS->>useBLE: getSweepedDevice(deviceId)
            alt Device Reappears in Scan Advertisements
                useBLE-->>RS: sweepedDevice
                RS->>SF: createGattSession(bleManager, mac, timeout=3500)
                alt Success
                    SF-->>RS: conn, adapter
                    Note over RS: GATT Connection Re-established!
                    RS->>PLX: onDeviceDisconnected subscription
                    RS->>Dev: monitorCharacteristicForService(notifyUUID)
                    Note over RS: Phase 3 Success! Break loop.
                else Failure
                    SF-->>RS: Error
                    Note over RS: Abandon Phase 3, Exit
                end
            else Device Not Found
                useBLE-->>RS: undefined
            end
        end
    end
    
    alt Reconnection Succeeded
        RS-->>Machine: send RECOVERY_COMPLETE (reconnectedDevice)
        Machine->>Machine: Transition RECOVERING -> READY
    else Recovery Permanently Failed
        RS-->>Machine: send RECOVERY_PERMANENTLY_FAILED
        Machine->>Machine: Transition RECOVERING -> IDLE (Ghost device in UI)
    end
```

---

## 9. Stale Documentation Targets

The following references within `tools/SK8Lytz_App_Master_Reference.md` are outdated and must be updated to align with current XState v5 actors:

---

### Architectural Impact Flags

* `[IMPACTS_STATE_CHART]` — The migration to `recoveryService` and `heartbeatService` as official invoked XState callbacks formalizes FSM transitions and state guards.
* `[IMPACTS_C4_CONTEXT]` — The removal of `BleConnectionManager` changes the core boundary between the write-dispatcher and connection services.

<!-- CARTOGRAPHER_END: BLE_CORE -->

### Domain: GROUP_SYNC
<!-- CARTOGRAPHER_START: GROUP_SYNC -->

# Architectural Cartography: GROUP_SYNC Domain

This document provides a comprehensive read-only architectural audit of the `GROUP_SYNC` domain in the SK8Lytz codebase, detailing its structures, data models, real-time sync systems, platforms, and integrations.

---

## 1. File Manifest

### Services & Data layer
* **`src/services/GroupRepository.ts`**: Single Source of Truth for custom device grouping persistence (AsyncStorage) and Supabase cloud sync via transactional RPC delegates.
* **`src/services/CrewService.ts`**: Core service orchestrating active crew session lifecycles, Supabase Realtime channel broadcasts, and local session rejoin caches.
* **`src/services/CrewProfileService.ts`**: Service handling permanent crew metadata, member permissions (multi-owner demotions), stats aggregations, and user lookups.
* **`src/services/ProfileService.ts`**: Unified facade re-exporting and delegating auth, token registration, and crew profile methods to maintain backward compatibility.

### React Context
* **`src/context/CrewContext.tsx`**: Provides the unified crew UI state machine context (`CrewProvider`), supplying discovery states, forms, and custom hook instances.

### Hooks
* **`src/hooks/useCrewHub.ts`**: Manages scanning, discovery, and active session listing by querying Supabase DB and checking user location permissions.
* **`src/hooks/useCrewManage.ts`**: Handles client forms for crew CRUD, photo picking uploads, invite codes, and permanent member arrays.
* **`src/hooks/useCrewSession.ts`**: Manages real-time state listeners, socket connectivity lifecycles (joins, leaves, handoffs), and UI callback updates.
* **`src/hooks/useCrewProximityRadar.ts`**: Periodically tracks background location coordinates and matches nearby sessions, rinks, or empty spots within 0.5 miles.
* **`src/hooks/useDashboardCrew.ts`**: Extracted hook triggering automatic rejoin on app relaunch, state mirroring, and member-to-controller event updates.
* **`src/hooks/useDashboardGroups.ts`**: Manages group selection vectors, UI collapse nodes, bulk registrations, and power-state command routing.

### Components
* **`src/components/CrewModal.tsx`**: Multi-step router overlay managing the transition between landing, join, creation, detail, map, and session controller sheets.
* **`src/components/CrewMemberDashboard.tsx`**: Dedicated dashboard overlay rendering active session statistics, list of members, and read-only leader mode visualizers.
* **`src/components/crew/CrewControllerScreen.tsx`**: Control console interface for session leaders to update, configure, and broadcast active patterns/colors.
* **`src/components/crew/CrewCreateScreen.tsx`**: Form screen prompting users for new crew/session names, location inputs, and initial parameters.
* **`src/components/crew/CrewDetailScreen.tsx`**: Displays permanent crew profile stats, list of members, and home locations.
* **`src/components/crew/CrewJoinScreen.tsx`**: User prompt interface for joining public/private crew sessions via direct link or 6-character codes.
* **`src/components/crew/CrewLandingMap.tsx`**: Renders native maps showing interactive clusters of skate spots and real-time crew beacons.
* **`src/components/crew/CrewLandingMap.web.tsx`**: Fallback stub for web platforms that alerts the user maps are mobile-only.
* **`src/components/crew/CrewLandingScreen.tsx`**: Central landing interface for browsing active sessions, editing user crews, and checking nearby maps.
* **`src/components/crew/CrewManageScreen.tsx`**: Detailed screen enabling owners to edit crew names, upload custom avatars, and select colors.
* **`src/components/crew/CrewScheduleScreen.tsx`**: Renders controls for scheduling future sessions with date/time pickers.
* **`src/components/crew/CrewStyles.ts`**: Centralized stylesheet generating layout properties for all crew components.
* **`src/components/crew/MapFiltersTray.tsx`**: UI control bar enabling toggles for rink, skatepark, and live session map markers.

---

## 2. Blast Radius (Dependency Map)

```
       ┌────────────────────────┐
       │   LocationService      │ (Coordinates & haversine)
       └───────────┬────────────┘
                   │
                   ▼
┌───────────────────────────────────────┐
│         GROUP_SYNC Domain             │
│  (Crews, Sessions, Custom Groups)    │
└──────┬─────────────────────────┬──────┘
       │                         │
       ▼                         ▼
┌──────────────┐          ┌──────────────┐
│  Dashboard   │          │    Supabase  │ (Realtime &
│  Controller  │          │    Database  │  PostgreSQL RPC)
└──────────────┘          └──────────────┘
```

### Imports (Inward Dependencies)
* **`LocationService`**: haversine calculations, nearby spots, coordinate models.
* **`SessionShareService`**: invitation formatting and OS sharing dialogs.
* **`AsyncStorage`**: persistent local cache indices (`@Sk8lytz_custom_groups`, `ng_crew_last_session_id`).
* **`expo-image-picker` / `expo-clipboard`**: handles native photo selecting and clipboard operations.
* **`react-native-maps` / `react-native-map-clustering`**: map rendering primitives and coordinate clusters.

### Exports (Outward Dependencies)
* **`DashboardScreen` / `MainNavigator`**: consumes hooks and displays the router modal (`CrewModal`, `CrewMemberDashboard`).
* **`DeviceRepository`**: integrates with `GroupRepository` delegate updates to register fleet groups.
* **`useHardwareNotifications`**: triggers configuration reads/syncs.

---

## 3. Context Matrix

| React Context | Role | Consumed By |
| --- | --- | --- |
| **`CrewContext`** | **Provides** Step routing, form fields, and hook states to crew sub-screens. | All components under `src/components/crew/*` |
| **`ThemeContext`** | **Consumes** App colors and styling tokens. | `CrewStyles.ts`, `CrewMemberDashboard.tsx` |
| **`AuthContext`** | **Consumes** Authenticated user profile identifiers (`user.id`) and details. | `CrewModal.tsx`, `useDashboardCrew.ts`, `useDashboardGroups.ts` |
| **`AppConfigContext`**| **Consumes** App client flags (`visibility_maps_tab`). | `CrewLandingScreen.tsx` |

---

## 4. Hook/Service I/O Registry

### Services

#### `GroupRepository`
* **Inputs**:
  * `setDeviceDelegate(delegate)`: Dynamic injection of `GroupDeviceDelegate`.
  * `saveGroupTransactional(groupId, groupName, deviceMacs, type, userId)`: Atomic fleet group upsert.
  * `deleteGroup(groupId, userId)`: Group removal cascade.
* **Outputs**:
  * `getGroups()`: Retreives local `CustomGroup[]`.
  * Transactional operations return `Promise<boolean>` or `Promise<void>`.
* **Side-Effects**: Writes to AsyncStorage (`GROUPS_KEY`, `PENDING_GROUP_KEY`), calls PostgreSQL RPC `upsert_group_with_devices` / `delete_group_cascade`.

#### `CrewService`
* **Inputs**:
  * `createSession(name, displayName, opts, userId)`: Spawns active crew session.
  * `joinSession(inviteCode, displayName, userId)`: Join via 6-character code.
  * `joinSessionById(sessionId, displayName, userId)`: Join via UUID.
  * `endSession(explicitSessionId, userId)`: Session termination.
  * `leaveSession(userId)`: Exiting membership.
  * `broadcastScene(scene, userId)`: Broadcasts scene payload.
* **Outputs**:
  * `currentSession` / `currentSessionId` / `currentRole` references.
  * `fetchActiveSessions()` / `fetchPublicSessions()`: Returns array of active sessions.
  * `fetchLastScene(sessionId)`: Query last scene data.
* **Side-Effects**: Subscribes to Supabase Realtime channel `crew:{sessionId}`, manages timers, handles auto-rejoins, updates local storage keys.

#### `CrewProfileService`
* **Inputs**: Permanent crew administration inputs (crew data, memberships, search strings).
* **Outputs**: Permanent crew rows, member counts, stats summaries, user search list.
* **Side-Effects**: Modifies Supabase tables (`crews`, `crew_memberships`).

### Hooks

#### `useCrewHub`
* **Inputs**: `visible: boolean`, `step: ModalStep`.
* **Outputs**: lists of spots/sessions, location statuses, member count indexes.
* **Side-Effects**: Verifies location permission, requests coordinates, queries Supabase, polls on a 15-second timer.

#### `useCrewManage`
* **Inputs**: `myCrews: PermanentCrew[]`.
* **Outputs**: configuration handlers, photo picking callbacks, member selection arrays.
* **Side-Effects**: Invokes `expo-image-picker` library.

#### `useCrewSession`
* **Inputs**: Lifecycle trigger callbacks (`onSessionReady`, `onSessionLeft`, `onSessionEnded`, etc.).
* **Outputs**: session state descriptors and termination controls.
* **Side-Effects**: Wraps `crewService` subscription streams.

#### `useCrewProximityRadar`
* **Inputs**: None.
* **Outputs**: `radarAlert` payload containing match data (`EMPTY_RINK`, `PUBLIC_SESSION`, `PRIVATE_CREW`).
* **Side-Effects**: Acquires location silently, queries spots and active sessions.

#### `useDashboardCrew`
* **Inputs**: `onApplyScene: (scene) => void`.
* **Outputs**: modal indicators, rejoin status flags, last scene snapshots.
* **Side-Effects**: Auto-rejoins session on boot, fetches last known leader scene.

#### `useDashboardGroups`
* **Inputs**: Registration hooks, BLE device references, CRUD handlers.
* **Outputs**: fleet groups list, power matrices, modal routers, save helpers.
* **Side-Effects**: Formulates groups, mutates configurations, persists group patterns.

---

## 5. OS Variance Matrix

### Web Fallback stubbing
* **Native Maps (`CrewLandingMap.tsx`)**: Integrates native map layers (`react-native-maps`, clusters, markers, user location coordinates).
* **Web Maps Stub (`CrewLandingMap.web.tsx`)**: Stubs the map layer on React Native Web target builds, warning the user maps are mobile-only.

### Fonts
* **Invite Code Previews**: `CrewManageScreen.tsx` formats monospace invite codes using Courier New on `ios` and generic monospace on `android` to ensure structural alignment.

### Keyboard Avoidance
* **Layout Offset**: `CrewModal.tsx` activates `KeyboardAvoidingView` with `padding` behavior on `ios`, bypassing manual adjustments on `android` where native window soft-inputs are handled by the system manifest.

---

## 6. Mermaid Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor L as Leader User
    actor M as Member User
    participant LS as Leader Service (CrewService)
    participant MS as Member Service (CrewService)
    database DB as Supabase DB
    participant RT as Supabase Realtime Channel (crew:{sessionId})
    
    %% Session Creation Flow
    rect rgb(30, 30, 40)
        Note over L, DB: Session Creation & Setup
        L->>LS: createSession(name, options)
        LS->>DB: Insert row in crew_sessions (status: 'active')
        DB-->>LS: returns session data
        LS->>DB: Insert row in crew_members (role: 'leader')
        LS->>LS: _persistSession() (local AsyncStorage cache)
        LS->>RT: subscribeAsLeader() (subscribes to presence & member_update)
        RT-->>LS: Connected
    end

    %% Session Discovery & Joining Flow
    rect rgb(40, 30, 30)
        Note over M, DB: Member Join Flow
        M->>MS: joinSessionById(sessionId) / joinSession(inviteCode)
        MS->>DB: Verify active status & expires_at > now
        DB-->>MS: returns session data
        MS->>DB: Insert/Upsert member record in crew_members
        MS->>MS: _persistSession() (local AsyncStorage cache)
        MS->>RT: subscribeAsMember() (subscribes to scene_update & session_ended)
        RT-->>MS: Connected
        MS->>DB: fetchLastScene() (Queries database for late-arrival recovery)
        DB-->>MS: Returns last_scene (if exists)
        MS->>M: Apply initial scene config to hardware
    end

    %% Real-time Sync Flow
    rect rgb(30, 40, 30)
        Note over L, M: Real-time Scene Synchronization
        L->>LS: broadcastScene(sceneData)
        Note over LS: Debounce timer fires (150ms)
        LS->>RT: Broadcast 'scene_update' payload
        RT-->>MS: Receives 'scene_update' broadcast event
        MS->>M: Apply updated scene config to hardware
        Note over LS: Throttle timer check (5s)
        LS->>DB: Update 'last_scene' in crew_sessions row (offline/late join sync buffer)
    end

    %% Session Termination Flow
    rect rgb(30, 30, 40)
        Note over L, M: Session Termination (End Session)
        L->>LS: endSession()
        LS->>DB: Update crew_sessions (is_active = false, status: 'ended', ended_at)
        LS->>RT: Broadcast 'session_ended' event
        RT-->>MS: Receives 'session_ended' broadcast event
        MS->>MS: unsubscribe() & clear local cache
        MS->>M: Revert to solo control mode
        Note over LS: 500ms propagation delay
        LS->>DB: Delete crew_members rows for session
        LS->>LS: unsubscribe() & clear local cache
        LS->>L: Revert to solo control mode
    end
```

---

## 7. Archival Instruction

The following sections in `tools/SK8Lytz_App_Master_Reference.md` are outdated:
* **Section 6: Crew Hub & Session Lifecycle**:
  * "Discovery radius filter governed by `LocationService.getNearbyPublicSessions(radiusMi)`" (line 1120) — This has been replaced by dynamic views (`public_sessions` view) and direct Haversine calculations within the hooks.

---

## 8. Architectural Impact Flags

`[IMPACTS_USER_JOURNEY]`
`[IMPACTS_C4_CONTEXT]`
`[IMPACTS_STATE_CHART]`

<!-- CARTOGRAPHER_END: GROUP_SYNC -->

### Domain: UI_SCREENS
<!-- CARTOGRAPHER_START: UI_SCREENS -->

# UI_SCREENS Domain Cartography

This document contains a comprehensive architectural audit, dependency mapping, and design token manifest for the **UI_SCREENS** domain in the SK8Lytz codebase, located under `src/screens/*`, `src/components/dashboard/*`, `src/components/shared/*`, `src/components/DeviceItem.tsx`, `src/components/LocationPicker*.tsx`, and `src/components/SkateSpotBottomSheet.tsx`.

---

## 1. File Manifest

Below is the list of all files in this domain, with a one-sentence architectural purpose for each:

### 📱 Screens (`src/screens/*`)

| File | Architectural Purpose |
| :--- | :--- |
| [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx) | Renders the primary entry page coordinating user login, signup, password retrieval, email verification, and offline-mode guest logins. |
| [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx) | Acts as the central monolith screen of the application, managing high-level BLE autoconnects, state ledgers, coordinate updates, and custom skate grouping coordination. |
| [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx) | Multi-step interactive flow guiding the user to scan, visually identify via blinking, test connection quality, name, and claim new sole or wheel hardware chipsets. |
| [PermissionsOnboardingScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/PermissionsOnboardingScreen.tsx) | Prompts onboarding users to grant required (Bluetooth) and optional (Location, Camera, Mic, Notifications, Health) native hardware permissions. |

### 🧩 Components (`src/components/*`)

| File | Architectural Purpose |
| :--- | :--- |
| [DeviceItem.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceItem.tsx) | Inline item component rendering RSSI signal bar animations, battery percentages, firmware badges, and claim parameters for a single scanned or registered device. |
| [LocationPicker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPicker.tsx) | Smart geocoding auto-suggest input providing fast curated spot lookups and falling back to OpenStreetMap Nominatim queries. |
| [LocationPickerMap.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPickerMap.tsx) | Native map container wrapper around `react-native-maps` to render coordinate marks and spot ranges. |
| [LocationPickerMap.web.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LocationPickerMap.web.tsx) | Web-compatible styling shim showing a static placeholder since native map rendering is unsupported in browsers. |
| [SkateSpotBottomSheet.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/SkateSpotBottomSheet.tsx) | Modal sheet displaying custom spot metadata (rink surfaces, environments, spots verified counts) and allowing skaters to check in or claim spots. |

### 📊 Dashboard-Specific Components (`src/components/dashboard/*`)

| File | Architectural Purpose |
| :--- | :--- |
| [CrewHubSlab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/CrewHubSlab.tsx) | Slab element organizing active crew groups, nearby rink spot listings, public session invites, and localized radar alert actions. |
| [DashboardCrewPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardCrewPanel.tsx) | Sub-container managing websocket sync subscriptions and mapping proximity alert responses to the crew modal. |
| [DashboardGroupList.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardGroupList.tsx) | Empty anchor file reserved for tracking blast radius verification. |
| [DashboardHeader.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardHeader.tsx) | Navheader rendering branding logos, active user profile tokens, network status chips, quick help modals, and theme controls. |
| [DashboardTelemetryHero.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardTelemetryHero.tsx) | Circular SVG gauge rendering live GPS speeds via spring-animated needles alongside 6 core metrics (Time, Distance, Calories, G-Force, Avg/Max Speed, HR). |
| [HardwareStatusPills.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/HardwareStatusPills.tsx) | Horizontal row displaying diagnostic information: LED count, Segment division, Firmware build, and RF remote modes. |
| [LiveTelemetryHUD.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/LiveTelemetryHUD.tsx) | Compact, high-performance horizontal telemetry bar displaying key session aggregates. |
| [MySkatesSlab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/MySkatesSlab.tsx) | Slab container rendering custom dual-skate groups or prompting initial onboarding setup if the fleet is empty. |
| [RegisteredFleetSlab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/RegisteredFleetSlab.tsx) | Collapsible list component showing every claimed hardware module associated with the skater profile. |
| [SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx) | Premium group interface card displaying multi-device connection status, dynamic linear gradients matching the active pattern, and quick-action control sidebars. |
| [SupportModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SupportModal.tsx) | Overlay popup exposing quick URLs for hardware installation guides, storefronts, and customer service. |

### 🛠️ Shared & Global Domain dependencies (`src/components/shared/*`, `src/theme/*`)

| File | Architectural Purpose |
| :--- | :--- |
| [BLEErrorBoundary.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/shared/BLEErrorBoundary.tsx) | Recoverable error boundary component catching BLE render-time violations and offering recovery steps. |
| [GranularPermissionsList.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/permissions/GranularPermissionsList.tsx) | Detailed settings card layout presenting permission switches linked to the native OS services framework. |
| [theme.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/theme/theme.ts) | Establishes the core visual theme tokens (Colors, Typography font sizes, Spacing, Layout constants, and Shadows). |

---

## 2. Blast Radius (Import/Export Map)

The **UI_SCREENS** domain resides at the top of the application's React layout hierarchy. It depends heavily on custom state management hooks, hardware communication services, and local repositories, but exports very few modules to external consumers.

```mermaid
graph TD
    %% Sub-slabs mapping
    DashboardScreen[screens/DashboardScreen.tsx] --> DashboardHeader[components/dashboard/DashboardHeader.tsx]
    DashboardScreen --> DashboardCrewPanel[components/dashboard/DashboardCrewPanel.tsx]
    DashboardScreen --> CrewHubSlab[components/dashboard/CrewHubSlab.tsx]
    DashboardScreen --> MySkatesSlab[components/dashboard/MySkatesSlab.tsx]
    DashboardScreen --> RegisteredFleetSlab[components/dashboard/RegisteredFleetSlab.tsx]
    
    %% Nested children
    DashboardCrewPanel --> CrewHubSlab
    MySkatesSlab --> SkateGroupCard[components/dashboard/SkateGroupCard.tsx]
    RegisteredFleetSlab --> DeviceItem[components/DeviceItem.tsx]
    DeviceItem --> HardwareStatusPills[components/dashboard/HardwareStatusPills.tsx]
    
    %% Services/Hooks imports
    useRegistration[hooks/useRegistration.ts] --> DashboardScreen
    useDashboardProfile[hooks/useDashboardProfile.ts] --> DashboardScreen
    useDashboardGroups[hooks/useDashboardGroups.ts] --> DashboardScreen
    useDashboardCrew[hooks/useDashboardCrew.ts] --> DashboardScreen
    useDashboardController[hooks/useDashboardController.ts] --> DashboardScreen
    useDashboardAutoConnect[hooks/useDashboardAutoConnect.ts] --> DashboardScreen
    useHardwareNotifications[hooks/useHardwareNotifications.ts] --> DashboardScreen
    BlePingService[services/BlePingService.ts] --> HardwareSetupWizardScreen[screens/Onboarding/HardwareSetupWizardScreen.tsx]
    
    %% Root Navigation
    App[App.tsx] --> AuthScreen[screens/AuthScreen.tsx]
    App --> DashboardScreen
    App --> GlobalPermissionsModal[components/modals/GlobalPermissionsModal.tsx]
    GlobalPermissionsModal --> PermissionsOnboardingScreen[screens/Onboarding/PermissionsOnboardingScreen.tsx]
    PermissionsOnboardingScreen --> GranularPermissionsList[components/permissions/GranularPermissionsList.tsx]
```

### 📥 Internal Dependencies (What UI_SCREENS Imports)
*   **React Contexts**: Consumes `BLEContext`, `ThemeContext`, `SessionContext`, `AppConfigContext`, `CrewContext`, and `AuthContext`.
*   **React Hooks**:
    *   State coordinators: `useDashboardProfile`, `useDashboardGroups`, `useDashboardCrew`, `useDashboardController`, `useDashboardAutoConnect`, `useHardwareNotifications`.
    *   System data hooks: `useRegistration`, `useDeviceStateLedger`, `useTelemetryLedger`, `useCrewProximityRadar`, `useRecentSpots`.
*   **Domain Services**:
    *   `BlePingService`: Coordinates connectionless blinks and characteristic probing.
    *   `SkateSpotsService`: Registers claims and verifications for spots.
    *   `PermissionService`: Drives native iOS/Android permissions checks and storage revokes.
    *   `AppLogger`: Captures navigation events, telemetry frames, and system crashes.
*   **Visual Assets & Libraries**:
    *   `@expo/vector-icons` (MaterialCommunityIcons), `react-native-maps`, `expo-location`, `expo-linear-gradient`, `react-native-svg`.

### 📤 External Consumers (What Imports UI_SCREENS)
*   **Application entry point (`App.tsx`)**: Imports `AuthScreen` and `DashboardScreen`.
*   **Permissions Modal (`GlobalPermissionsModal.tsx`)**: Imports `PermissionsOnboardingScreen`.
*   **Testing Suites**: `HardwareSetupWizardScreen.test.tsx` mounts the setup wizard.

---

## 3. Context Matrix

The screens and components in this domain act primarily as **consumers** of global React Contexts. They ingest state from the core providers and pass subsets down to representational UI slabs.

| Context | Consumed By | Architectural Purpose |
| :--- | :--- | :--- |
| `AuthContext` | `AuthScreen`, `DashboardScreen`, `DashboardHeader`, `MySkatesSlab` | Provides identity states, session timers, and offline modes. |
| `ThemeContext` | Entire Domain (via `useTheme`) | Houses theme palettes and toggles. Re-renders UI nodes on theme transitions. |
| `BLEContext` | `DashboardScreen`, `HardwareSetupWizardScreen` | Supplies list of discovered peripherals, connected state arrays, and connection trigger methods. |
| `SessionContext` | `DashboardScreen`, `DashboardTelemetryHero`, `LiveTelemetryHUD` | Surfaces live session metrics (GPS Speed, G-Force, Duration, Calories) and watch biometrics. |
| `AppConfigContext` | `DashboardScreen`, `LocationPicker`, `CrewHubSlab` | Controls feature-flag gates (such as map visibility constraints in low-bandwidth regions). |
| `CrewContext` | `DashboardCrewPanel`, `CrewHubSlab` | Coordinates live crew session codes, member rosters, and cloud scene triggers. |

---

## 4. Hook/Service I/O Registry

Since this domain hosts complex UI components, it relies heavily on custom coordination hooks. The registry below maps their input requirements, outputs, and side-effects.

### ⚙️ `useDashboardProfile`
*   **Inputs**:
    *   `onCrewJoinNotification: (crewId: string) => void` (Callback for incoming crew sessions).
*   **Outputs**:
    *   `userProfile: UserProfile | null` (Database record).
    *   `appSettings: Record<string, string | boolean>` (System configurations).
    *   `refreshProfile: () => Promise<void>` (Reload trigger).
    *   `authUsername: string | null` (Resolved screen display name).
    *   `handleLogout: () => void` (Session teardown).
    *   `isAccountModalVisible` / `isAdminToolsVisible` / `isSupportModalVisible` (Boolean flags and visibility setters).
*   **Side Effects**: Makes network requests to Supabase `profiles` on mount. Updates persistent AsyncStorage credentials.

### ⚙️ `useDashboardGroups`
*   **Inputs**:
    *   `registeredDevices`, `saveAllRegisteredDevices`, `saveRegisteredDevice`, `deregisterDevice`, `clearPendingRegistrations`, `getAllScannedDevices`, `setAllDevices`, `allDevicesRef`, `onRegistrationComplete`.
*   **Outputs**:
    *   `customGroups: CustomGroup[]` (List of user-defined skate groups).
    *   `deviceConfigs: Record<string, any>` (Per-device LED/GATT mappings).
    *   `powerStates: Record<string, boolean>` (State map indicating which devices are on).
    *   `lastGroupPatterns: Record<string, any>` (Cache of the last pattern run per group).
    *   `isRegisteredCollapsed` (Toggles list collapsibility).
    *   `handleRegistrationComplete` / `saveGroup` / `handleGroupDelete` (CRUD callbacks).
*   **Side Effects**: Writes custom group definitions to AsyncStorage under `@SK8Lytz_CustomGroups`. Modifies local LED state ledgers.

### ⚙️ `useDashboardAutoConnect`
*   **Inputs**:
    *   `isBluetoothSupported`, `isBluetoothEnabled`, `isActuallyConnected`, `allDevices`, `connectedDevices`, `connectToDevices`, `scanForPeripherals`, `requestPermissions`, `registeredDevices`, `getGate`, `isWizardActive`, `burstScan`.
*   **Outputs**:
    *   `retriggerAutoConnect: () => void` (Manual restart trigger).
*   **Side Effects**: Periodically queries scanning states. Compares MAC addresses of scanned BLE devices with the registered fleet list and automatically initiates connections.

---

## 5. OS Variance Matrix

The UI domain implements specific branching conditions to handle rendering discrepancies and API requirements across iOS, Android, and Web platforms.

| Feature Category | iOS Path | Android Path | Web (Simulator/Demo) Path |
| :--- | :--- | :--- | :--- |
| **Keyboard Offsets** | Uses `KeyboardAvoidingView` with `behavior="padding"`. | Uses default layout adjusts (handled via Android manifest soft-input policies). | No adjustment applied. |
| **Edge Back Gestures** | Custom `edgePanResponder` intercepts swipe gestures starting at `startX < 50` to close the DockedController overlay. | Relies on the hardware BackButton handler (`BackHandler.addEventListener`). | Edge gestures are skipped. |
| **Map Rendering** | Renders `react-native-maps` directly with Apple Maps/Google Maps. | Renders `react-native-maps` mapped to Google Play Services. | Metro resolves to `LocationPickerMap.web.tsx`, rendering a static text fallback component. |
| **Health Permissions** | Requests permissions via `react-native-health` (Apple HealthKit). | Triggers `react-native-health-connect` API checks. | Bypassed. |
| **Layout Shadow Effects** | Uses iOS shadow layout layers (`shadowColor`, `shadowRadius`, `shadowOpacity`). | Uses elevation properties (`elevation: 4`). | Applies CSS-like box-shadow shims using `webStyle()`. |

---

## 6. Design System & Token Manifest

Design tokens are centralized in `src/theme/theme.ts`. The UI components import these parameters directly to guarantee visual consistency.

### 🎨 Colors (`Colors` Token)

```json
{
  "DarkColors (Default Brand Style)": {
    "background": "#1B4279",
    "surface": "#245596",
    "surfaceHighlight": "#3172C9",
    "primary": "#FF5A00",
    "secondary": "#FFB800",
    "accent": "#FF3300",
    "text": "#FFFFFF",
    "textMuted": "#A0B4CF",
    "textDim": "#6B85A0",
    "border": "#2E5FA3",
    "success": "#00E88F",
    "error": "#FF3D71",
    "warning": "#FFB800"
  },
  "LightColors (High Contrast Style)": {
    "background": "#EAEFF5",
    "surface": "#CBD6E2",
    "surfaceHighlight": "#DDE5EE",
    "primary": "#FF5A00",
    "secondary": "#FFB800",
    "accent": "#1B4279",
    "text": "#0A1C38",
    "textMuted": "#5C7491",
    "textDim": "#8A9EB5",
    "border": "#B0C0D0",
    "success": "#00C476",
    "error": "#FF3D71",
    "warning": "#E07A00"
  }
}
```

### 📐 Spacing & Layout Constraints (`Spacing` & `Layout` Tokens)
*   **Grid Steps**: `xxs` (2px), `xs` (4px), `sm` (8px), `md` (12px), `lg` (16px), `xl` (24px), `xxl` (32px), `xxxl` (40px), `huge` (48px), `giant` (64px).
*   **Base Margins**: Standard page padding is anchored to `Spacing.lg` (16px).
*   **Corner Radii**: Standard card styling is anchored to `Spacing.xl` (24px).

### 🗚 Typography (`Typography` Token - Righteous Font Family)
*   **Header**: `fontSize: 24`, `letterSpacing: 2`, `textTransform: 'uppercase'`, `fontFamily: 'Righteous'`.
*   **Title**: `fontSize: 16`, `letterSpacing: 0.5`, `fontFamily: 'Righteous'`.
*   **Body**: `fontSize: 14`, `fontFamily: 'Righteous'`.
*   **Caption**: `fontSize: 11`, `fontFamily: 'Righteous'`.

---

## 7. Sequence Diagrams

### Interactive Claiming & Probing Flow (Hardware Setup Wizard)

The diagram below details the actor-to-actor flow involved in scanning, identifying, blinking, and claiming a skate device during the Hardware Onboarding Wizard process.

```mermaid
sequenceDiagram
    autonumber
    actor User as Skater (UI)
    participant Wizard as HardwareSetupWizardScreen
    participant PingService as BlePingService
    participant GATT as BLE Hardware Controller
    participant Registry as useRegistration Hook
    participant DB as Cloud DB (Supabase)

    User->>Wizard: Clicks "Add Skate" / Starts Wizard
    Wizard->>Wizard: Runs BLE Scanning
    Wizard-->>User: Lists Scanned Devices (RSSI >= -90dB)
    User->>Wizard: Selects a Skate (Status: PROBING)
    Wizard->>PingService: Invokes pingDevice(macAddress)
    
    rect rgb(25, 30, 50)
        Note over PingService: Atomic Connectionless Probe
        PingService->>GATT: Initiates GATT connection
        GATT-->>PingService: Connected
        PingService->>GATT: Writes 0x03 start-blink (100% white)
        PingService->>GATT: Reads Firmware Version & Segment Count Characteristics
        GATT-->>PingService: Returns Version Data
        PingService->>GATT: Writes 0x00 stop-blink (Lights off)
        PingService->>GATT: Closes connection
    end
    
    PingService-->>Wizard: Returns Device Characteristics (Points, Segments, FW)
    Wizard-->>User: Device flashes white. "Did your skate blink?"
    User->>Wizard: Confirms blink & chooses name (e.g. Left Skate)
    Wizard->>Registry: Calls saveRegisteredDevice(registrationPayload)
    Registry->>DB: Inserts device record into `skate_devices` table
    DB-->>Registry: Success
    Registry->>Registry: Updates local AsyncStorage cached fleet
    Registry-->>Wizard: Registration Complete
    Wizard-->>User: Transition to Dashboard (Auto-connect triggers)
```

---

## 8. Stale Documentation Archive Review

During this audit, three stale visualizer and layout references were identified in `tools/SK8Lytz_App_Master_Reference.md`:

1.  **Line 1557**:
    ```markdown
    ```
    *Status*: Confirmed. The dashboard was previously mapped as a single monolithic block. It has since been modularized into discrete slab sub-components (`CrewHubSlab.tsx`, `MySkatesSlab.tsx`, `RegisteredFleetSlab.tsx`). **Recommendation**: Archive references to the monolithic design.

2.  **Line 1558**:
    ```markdown
    ```
    *Status*: Confirmed. The "One-Screen Setup Policy" is outdated. Setup flows are split between onboarding wizards, permissions lists, and modal drawers. **Recommendation**: Archive.

3.  **Line 1559**:
    ```markdown
    ```
    *Status*: Confirmed. Payload fragmentation handling is a core GATT transport detail. It has been moved to `BleWriteQueue.ts` and `ZenggeProtocol.ts`, out of the UI layer. **Recommendation**: Archive.

---

## 9. Architectural Impact Flags

Since this is a read-only architectural cartography audit, no functional modifications were introduced. Below is a manifest of where future changes to the visual surface would trigger impact flags:

*   **`[IMPACTS_USER_JOURNEY]`** — If modifying onboarding transition triggers in `HardwareSetupWizardScreen` or adding support guides inside `SupportModal`.
*   **`[IMPACTS_C4_CONTEXT]`** — If connecting maps in `LocationPickerMap` to a new third-party geocoding API or cloud imagery provider.
*   **`[IMPACTS_STATE_CHART]`** — If changing permission toggle paths in `GranularPermissionsList` or modifying FSM state loops in the BLE Connection wizard.

`[NO_ARCHITECTURAL_IMPACT]`

<!-- CARTOGRAPHER_END: UI_SCREENS -->

### Domain: UI_DOCKED_CONTROLLER
<!-- CARTOGRAPHER_START: UI_DOCKED_CONTROLLER -->

# 📐 Architectural Cartography — UI_DOCKED_CONTROLLER Domain

This document provides a complete read-only architectural map of the `UI_DOCKED_CONTROLLER` domain of the SK8Lytz App codebase. It catalogs the internal files, maps hook and context boundaries, registers service I/O, details operating system variances, illustrates connection lifecycles, and highlights refactoring recommendations.

---

## 1. File Manifest

The `UI_DOCKED_CONTROLLER` domain is structured as an orchestration shell surrounded by presentation leaf panels, local logic abstraction hooks, and native tracker hooks.

| File Path | Architectural Purpose |
|:---|:---|
| `src/components/DockedController.tsx` | The central orchestration monolith (65.6KB). Hosts the tab selector, panel routing logic, layout wrappers, and threads prop buses down to leaf nodes. |
| `src/components/docked/AnalogGauge.tsx` | Reusable SVG progress ring displaying real-time speed (MPH) and G-force measurements within Street Mode. |
| `src/components/docked/BuilderPanel.tsx` | UI wrapper coordinating custom multicolor pattern nodes. Bridges the library preset view and the drag-and-drop gradient compiler. |
| `src/components/docked/CameraPanel.tsx` | Viewfinder controller enabling real-time k-means color extraction (Sniper sub-mode) or palette generation (Vibe sub-mode). |
| `src/components/docked/DockedDock.tsx` | Floating horizontal bar navigation dock supporting touch selection and swipe gesture transitions. |
| `src/components/docked/FavoritePromptModal.tsx` | Naming overlay allowing customized color configuration or pattern naming before local/cloud database storage. |
| `src/components/docked/FavoritesPanel.tsx` | Presentational grid showing user-saved preset configurations alongside cloud curation lists. |
| `src/components/docked/MusicPanel.tsx` | Interface controls targeting hardware microphone inputs (Device-Mic), software streaming inputs (App-Mic), EQ spectrum rendering, and color focus targets. |
| `src/components/docked/PresetCard.tsx` | Renders individual visual representations of curated gradient presets or custom user saves. |
| `src/components/docked/ProEffectsPanel.tsx` | Grid picker wrapper routing pre-configured built-in templates (rainbow, chase, flash) defined in the pattern engine. |
| `src/components/docked/QuickPresetModal.tsx` | Small modal form enabling cloud-sharing registration for custom patterns. |
| `src/components/docked/SpectrumAnalyzer.tsx` | Renders a graphic EQ dance visualizer responsive to live software audio magnitude inputs. |
| `src/components/docked/StreetModeDistributionSlider.tsx` | Balance slider configuring color distributions across front and rear LED segment blocks. |
| `src/components/docked/StreetPanel.tsx` | Dashboard HUD rendering Speed, Peak G-Forces, Trip distance recorders, and Cruise/Brake coloring schemes. |
| `src/components/docked/UniversalSlidersFooter.tsx` | A unified footer container housing color presets, Hue slider, and tactical control sliders (brightness, speed, and sensitivity). |
| `src/hooks/useDashboardController.tsx` | Composite hook aggregator context. Coordinates telemetry recording, BLE device state configurations, and crew session overlays. |
| `src/hooks/useDockedControllerState.ts` | Local state abstraction hook. Lazily hydrates values from the telemetry ledger database cache on initialization. |
| `src/hooks/useControllerDispatch.ts` | BLE command translation layer. Builds byte-array payloads aligned with protocol adapters and manages an LRU command cache. |
| `src/hooks/useControllerAnalytics.ts` | telemetrical observer hook. Debounces and dispatches state updates (brightness, speed, color) to the App Logger. |

---

## 2. Blast Radius (Imports/Exports)

```
                       ┌──────────────────────────────┐
                       │    DashboardScreen.tsx       │
                       └──────────────┬───────────────┘
                                      │
                                      ▼
                       ┌──────────────────────────────┐
                       │  useDashboardController.tsx  │
                       └──────────────┬───────────────┘
                                      │
                                      ▼
                       ┌──────────────────────────────┐
                       │    DockedController.tsx      │
                       └──────────────┬───────────────┘
                                      │
           ┌──────────────────────────┼─────────────────────────┐
           ▼                          ▼                         ▼
┌─────────────────────┐    ┌─────────────────────┐   ┌─────────────────────┐
│ src/components/     │    │  src/hooks/         │   │  src/protocols/     │
│ docked/*            │    │  use*State.ts       │   │  ZenggeProtocol.ts  │
└─────────────────────┘    └─────────────────────┘   └─────────────────────┘
```

### Incoming Dependencies (What Imports this Domain)
* **`src/screens/DashboardScreen.tsx`**: Consumes `useDashboardController.tsx` to mount the main controller interface shell and feed system-level telemetry inputs.

### Outgoing Dependencies (What this Domain Imports)
* **Design Tokens & Theme**: `src/theme/theme` (Spacing, Colors, Typography)
* **BLE Context & Services**: `src/context/BLEContext` (`useSharedBLE`), `src/protocols/ZenggeProtocol`
* **Pattern Engines**: `src/protocols/PatternEngine` (`SK8LYTZ_TEMPLATES`), `src/protocols/PositionalMathBuffer`
* **Ledgers & Caches**: `src/hooks/useTelemetryLedger`, `src/hooks/useDeviceStateLedger`
* **Hardware Metadata**: `src/types/dashboard.types` (`IHardwareSettings`, `FixedModePattern`, `MotionState`)
* **Utilities**: `src/utils/ColorUtils`, `src/utils/NormalizationUtils`
* **External Modules**: `expo-haptics`, `expo-linear-gradient`, `@expo/vector-icons`

---

## 3. Context Matrix

This domain acts primarily as a **State Aggregator and Prop Router** rather than a context provider. It consumes global contexts and propagates configuration values down via explicit property bounds.

| React Context | Consumption Purpose | Provider Source |
|:---|:---|:---|
| `ThemeContext` | Fetches colors, dark/light toggle states, and visual modes (`isDark`). | `src/context/ThemeContext` |
| `AppConfigContext` | Controls global variables, debug modes, and sensor configuration overrides. | `src/context/AppConfigContext` |
| `BLEContext` | Supplies direct BLE connection writes, dispatch streams, and device scans. | `src/context/BLEContext` |
| `AuthContext` | Evaluates cloud publishing access when registering presets to the community. | `src/context/AuthContext` |
| `FavoritesContext` | Governs CRUD behaviors on local user saves and cloud-synced style libraries. | `src/context/FavoritesContext` |

---

## 4. Hook/Service I/O Registry

### `useDashboardController`
* **Inputs**:
  * `isOfflineMode: boolean`
  * `isActuallyConnected: boolean`
  * `gpsSpeed: number`
  * `peakGForce: number`
  * `writeToDevice: (payload: number[]) => Promise<void>`
  * `powerStates: Record<string, boolean>`
  * `handlePowerToggle: (mac: string) => void`
  * `handleDisconnect: () => void`
* **Outputs**:
  * `MemoizedSk8lytzController: React.ComponentType`
  * `activeHwSettings: IHardwareSettings`
  * `isSettingsVisible: boolean`
  * `selectedDeviceForSettings: DeviceType | null`
  * `openSettings: (device: DeviceType) => void`
  * `saveSettings: (settings: IHardwareSettings) => void`
* **Side-Effects**: Synchronizes active configs, captures group layout matrices, triggers AppLogger events, and saves pattern configurations to the device ledger.

### `useDockedControllerState`
* **Inputs**:
  * `initialProduct: ProductType` (default: `'HALOZ'`)
  * `ledgerLoadSync?: (mac: string) => any`
  * `mac?: string`
* **Outputs**:
  * Local component state getters/setters (mode, colors, brightness, speeds).
  * `applyCloudScene: (scene: any) => void` (hydrates UI state from database model).
  * `captureEntireState: () => Record<string, any>` (collects all state parameters into a ledger snapshot).
* **Side-Effects**: Pre-warms settings lazily during initialization by loading last-saved values from the in-memory database ledger.

### `useControllerDispatch`
* **Inputs**:
  * `writeToDevice: (payload: number[]) => void`
  * `hwSettings?: IHardwareSettings`
  * `primaryDeviceId?: string`
  * `connectedDevices: DeviceType[]`
* **Outputs**:
  * `{ sendColor, applyFixedPattern, applyStaticModePattern, applyEmergencyPattern, handleMusicChange, setPower, setMultiColor }`
* **Side-Effects**: Serializes state inputs into protocol byte streams using target adapter rules, issues device writes, and caches pattern calculations in a localized LRU cache (capacity: 8) to reduce CPU cycles.

### `useControllerAnalytics`
* **Inputs**:
  * `activeMode: ModeType`
  * `selectedPatternId: number`
  * `selectedColor: string`
  * `brightness: number`
  * `speed: number`
  * `streetSensitivity: number`
  * `deviceContext: { target: string; deviceId?: string; groupSize?: number }`
* **Outputs**:
  * None (Side-effect only).
* **Side-Effects**: Watches parameter modifications, updates the telemetry database ledger, and issues debounced (600ms–800ms) logging dispatches via `AppLogger`.

---

## 5. OS Variance Matrix

| Surface | iOS Behavior | Android Behavior | Web Fallback |
|:---|:---|:---|:---|
| **View Shadows** | Renders using traditional native shadows (`shadowColor`, `shadowOpacity`, `shadowRadius`, `shadowOffset`). | Enforces physical visual depth using the native OS `elevation` rendering system. | Formats shadows using standard CSS styling configurations (`boxShadow` values). |
| **Backdrop Blur** | Achieved via custom native layouts. | Not natively supported; falls back to solid background overlays. | Employs CSS `backdropFilter: 'blur(12px)'` properties. |
| **Microphone Permission** | Requests permission through Apple standard framework; requires `NSMicrophoneUsageDescription` in `Info.plist`. | Requests audio bounds via Android OS permissions; requires `<uses-permission android:name="android.permission.RECORD_AUDIO" />` in `AndroidManifest.xml`. | Checks standard browser Navigator Media Device capabilities. |
| **Location Tracking** | Coordinates GPS via `CoreLocation` layers; checks foreground location (`NSLocationWhenInUseUsageDescription`). | Coordinates GPS via location framework bounds; uses `ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION`. | Relies on browser Geolocation API permissions. |
| **Haptic Feedback** | Triggers iOS Taptic Engine feedback via `expo-haptics`. | Triggers device vibration patterns via Android OS hardware integrations. | No-op (Silently ignored). |

---

## 6. Sequence Diagram: BLE Dispatch & Snapback Reconciliation

This diagram maps the flow of a user color/pattern interaction. It details the optimistic UI update, BLE payload dispatch, and the snapback reconciliation that occurs if the hardware communication fails.

```mermaid
sequenceDiagram
    autonumber
    actor User as Skater (UI)
    participant DC as DockedController
    participant OPT as useOptimisticBLE
    participant DP as useControllerDispatch
    participant BM as BleConnectionManager
    participant GATT as GATT Server (Skates)

    User->>DC: Tap Preset Color (e.g. #FF00FF)
    
    rect rgb(20, 20, 40)
        Note over DC,OPT: Optimistic Update Phase
        DC->>DC: Update SelectedColor State immediately
        DC->>OPT: invoke writeToDevice(hex)
        OPT->>OPT: record lastConfirmedStateRef (#00FFFF)
        OPT->>OPT: trigger Haptic feedback (Impact: Medium)
    end

    rect rgb(40, 20, 20)
        Note over DC,DP: BLE Translation & Dispatch
        DC->>DP: sendColor(255, 0, 255)
        DP->>DP: Lookup Protocol Adapter
        DP->>DP: Compile Byte Array [0x31, 0xFF, 0x00, 0xFF, ...]
        DP->>BM: dispatch payload queue
    end

    BM->>GATT: Write Value (BLE GATT Characteristic)

    alt Write Success
        GATT-->>BM: Write Confirmed
        BM-->>DC: Promise Resolved (Success)
        DC->>DC: Save Snapshot to Telemetry Ledger
    else Write Timeout or Connection Dropped (Split Brain)
        GATT--XBM: Connection Disrupted
        BM-->>OPT: Promise Rejected (Failure)
        
        rect rgb(60, 10, 10)
            Note over OPT,DC: Snapback Reconciliation Phase
            OPT->>DC: handleReconcile()
            DC->>DC: load lastConfirmedStateRef (#00FFFF)
            DC->>DC: reset SelectedColor state to #00FFFF
            DC->>User: Revert UI color dot to Cyan (prevents visual split-brain)
        end
    end
```

---

## 7. Component Extraction & Refactoring Opportunities

`DockedController.tsx` contains roughly 1,400 lines of orchestration logic. While it functions as a performant shell, several extraction opportunities exist to improve readability and safety.

### 1. Extract Inline Sub-components
* **`FixedPatternPreviewRow` (Lines 79–121)**: This inline renderer draws speed indicator bars and styling dots for pro patterns. Moving it to `src/components/docked/FixedPatternPreviewRow.tsx` would decouple visual presentation from the controller's main coordinate calculations.

### 2. Isolate Dashboard Header Elements
* **Tactical Buttons**: The top-level power toggle, device selector modal button, and quick-favorite actions are currently rendered inside the main file's JSX. These can be consolidated into `src/components/docked/DockedHeaderActions.tsx` to simplify layout structures.

### 3. Extract Tab Navigation Bar
* **Product Selectors**: The list map linking `"HALOZ"`, `"WHEELZ"`, and `"STRIPZ"` products relies on custom tab layouts. This block can be moved to `src/components/docked/DockedProductSelector.tsx`.

### 4. Transition to a Controller Context Provider
* **Prop Drilling Mitigation**: The controller passes dozens of callbacks down to `UniversalSlidersFooter.tsx` and the mode panels.
  * **Solution**: Wrap sub-panels in a scoped React Context provider (`DockedBusProvider`). This would allow nested panels to call `useDockedBus()` directly, removing the need for extensive prop drilling.

---
> [!NOTE]
> No code changes were executed during this read-only architectural audit.

<!-- CARTOGRAPHER_END: UI_DOCKED_CONTROLLER -->

### Domain: UI_MODALS
<!-- CARTOGRAPHER_START: UI_MODALS -->

# UI_MODALS Domain Cartography

_Last Updated: 2026-06-11 | Domain: UI_MODALS | Target OS: iOS, Android, Web_

---

## 1. File Manifest

The `UI_MODALS` domain encapsulates the interactive bottom-sheets, overlay dialogs, and compliance screens that govern device settings, user accounts, cloud content syncing, and legal gateways.

| File Path | Component Name | Architectural Purpose |
| :--- | :--- | :--- |
| `src/components/AccountModal.tsx` | `AccountModal` | A monolithic tabbed bottom-sheet that centralizes profile management, security settings, crew memberships, registered devices, telemetry statistics, and account deletion. |
| `src/components/DeviceSettingsModal.tsx` | `DeviceSettingsModal` | A configuration overlay that interfaces with BLE dispatch services to query and write hardware EEPROM parameters (LED count, segment count, strip type, color sorting) and RF remote security levels. |
| `src/components/CommunityModal.tsx` | `CommunityModal` | A scene browser modal that allows users to explore community-published presets and access personal cloud saves, utilizing memoized cards and horizontal moving LED strip animations. |
| `src/components/GroupSettingsModal.tsx` | `GroupSettingsModal` | A dialog modal that facilitates the creation, renaming, and member selection of unified multi-device groups. |
| `src/components/SessionSummaryModal.tsx` | `SessionSummaryModal` | A post-session debriefing modal that calculates estimated calorie burn and formats telemetric aggregates (distance, peak/avg speed, peak G-Force) into speed-zone themed glassmorphic tiles. |
| `src/components/modals/EulaModal.tsx` | `EulaModal` | A legal gatekeeper modal that requires scrolling verification prior to accepting the physical and photosensitivity waiver rules. |
| `src/components/modals/GlobalPermissionsModal.tsx` | `GlobalPermissionsModal` | An event-driven modal that wraps the permissions onboarding flow, dynamically displaying on global show triggers and emitting closure events. |
| `src/components/CustomSlider.tsx` | `CustomSlider` | A high-performance PanResponder slider optimized for React Native gesture responsiveness and Expo Web touch-action bypasses, supporting gradient tracks. |
| `src/components/MarqueeText.tsx` | `MarqueeText` | A helper text wrapper that measures children widths and runs horizontal translation loops to scroll text exceeding container boundaries. |
| `src/components/ConnectionStrengthBadge.tsx` | `ConnectionStrengthBadge` | A lightweight signal-strength component that maps raw BLE RSSI values into 3-bar color-coded vertical indicator panels. |

---

## 2. Blast Radius (Imports/Exports)

```mermaid
graph TD
    %% Imports from other domains
    ThemeDomain[theme/theme.ts] -->|Imports tokens & colors| UI_MODALS[UI_MODALS Components]
    AuthContext[context/AuthContext.ts] -->|Imports useAuth| UI_MODALS
    ThemeContext[context/ThemeContext.ts] -->|Imports useTheme| UI_MODALS
    ProfileService[services/ProfileService.ts] -->|Imports profileService| AccountModal
    SupabaseClient[services/supabaseClient.ts] -->|Imports supabase| AccountModal
    ScenesService[services/ScenesService.ts] -->|Imports ScenesService| CommunityModal
    AppLogger[services/AppLogger.ts] -->|Imports AppLogger| UI_MODALS
    ProtocolDispatch[hooks/useProtocolDispatch.ts] -->|Imports useProtocolDispatch| DeviceSettingsModal
    NamingUtils[utils/NamingUtils.ts] -->|Imports NamingUtils| DeviceSettingsModal & GroupSettingsModal

    %% Exports to other domains
    UI_MODALS -->|Exports AccountModal| DashboardScreen[screens/DashboardScreen.tsx]
    UI_MODALS -->|Exports DeviceSettingsModal| DashboardScreen
    UI_MODALS -->|Exports CommunityModal| ControllerDock[components/DockedController.tsx]
    UI_MODALS -->|Exports GroupSettingsModal| DashboardScreen
    UI_MODALS -->|Exports SessionSummaryModal| StreetModeScreen[screens/StreetModeScreen.tsx]
    UI_MODALS -->|Exports GlobalPermissionsModal| RootApp[App.tsx]
```

### Dependency Ingestion
- **Styles & Tokens**: Imports brand constants (`Colors`, `Spacing`, `Layout`, `Typography`) from `src/theme/theme.ts` and applies theme switches via `ThemeContext`.
- **Hooks & Services**:
  - `useAccountOverview` / `useSkateStats`: Injected into `AccountModal` to segregate data fetching.
  - `profileService` / `supabase`: Feeds database write/update states (crews, passwords, avatar settings) to `AccountModal`.
  - `ScenesService`: Feeds public and personal presets to `CommunityModal`.
  - `useProtocolDispatch`: Injected into `DeviceSettingsModal` to trigger `0x62` (save) and `0x63` (probe) commands.
  - `useSafeAreaInsets`: Injected into `DeviceSettingsModal` from `react-native-safe-area-context` to align elements above the operating system home bar.

### Exposed Boundaries
- Modals are consumed by major screens (Dashboard, Street Mode) and control components (Docked Controller) as conditional overlay sheets.
- Type definitions like `StoredDevice` (`AccountModal.tsx`) are shared with dashboard items.

---

## 3. Context Matrix

| React Context | Consumed By | Provided By | Architectural Purpose |
| :--- | :--- | :--- | :--- |
| `ThemeContext` | `AccountModal`, `CommunityModal`, `EulaModal`, `CustomSlider`, `SessionSummaryModal` | `ThemeProvider` (`App.tsx`) | Standardizes dark/light aesthetic colors and handles theme toggles inside settings. |
| `AuthContext` | `AccountModal`, `CommunityModal` | `AuthProvider` (`App.tsx`) | Exposes raw session user data (`user.id`, `user.email`) and controls authentication mutations (sign-in/sign-out updates). |
| `SafeAreaContext` | `DeviceSettingsModal`, `EulaModal`, `CommunityModal` | `SafeAreaProvider` (`App.tsx`) | Adapts layouts dynamically to safe area boundaries (notch, home bar). |

---

## 4. Hook/Service I/O Registry

### `useAccountOverview(visible, onProfileUpdated)`
- **Input**:
  - `visible`: `boolean` (refetches profile context if transitioning to `true`).
  - `onProfileUpdated`: `() => void` (optional callback triggered when details persist successfully).
- **Output**:
  - `user`: `User | null`
  - `status`: `'idle' | 'loading' | 'saving_profile' | 'crew_loading'`
  - `profile`: `Profile | null`
  - `editName` / `editUsername` / `avatarHue`: Editable React values.
  - `handleSaveProfile`: `() => Promise<void>` (persists changes to Supabase).
  - `handleCreateCrew`: `() => Promise<void>` (triggers crew creation in `ProfileService`).
  - `handleJoinCrew`: `() => Promise<void>` (registers join code against DB).
- **Side-Effects**: Updates internal `AsyncStorage` cache keys (`@Sk8lytz_auth_username`) on save; dispatches telemetry metrics via `AppLogger`.

### `useSkateStats(visible)`
- **Input**: `visible: boolean`
- **Output**:
  - `lifetimeStats`: `{ distance_miles, duration_sec, max_speed_mph, total_sessions } | null`
  - `recentSessions`: `ISessionSnapshot[]`
  - `statsLoading`: `boolean`
- **Side-Effects**: Performs aggregate database queries against `skate_sessions` table on modal mount.

### `useProtocolDispatch()` (via `DeviceSettingsModal`)
- **Input / Functions**:
  - `queryHardwareSettings(false, deviceId)`: Enqueues `0x63` query request to read EEPROM details.
  - `writeSettingsByName(points, segments, stripType, sorting, deviceId)`: Prepares and writes standard `0x62` parameters.
  - `setRfRemoteState(rfMode, false, deviceId)`: Sends RF security permissions payload.
- **Side-Effects**: Appends write frames directly to the `BleWriteQueue` priority queue to enforce serial Bluetooth communication.

---

## 5. OS Variance Matrix

| OS Branch | File / Line | Implementation Detail | Architectural Rationale |
| :--- | :--- | :--- | :--- |
| **iOS** vs **Android** | `AccountModal.tsx:L590` | `fontFamily: Platform.OS === 'ios' ? 'Courier New' : 'monospace'` | Guarantees character-width parity inside input fields for verification codes. |
| **Web** vs **Native** | `AccountModal.tsx:L375-384` | Web directly signs out and calls callbacks bypassing the native `Alert.alert` dialog. | `Alert.alert` lacks full DOM compatibility and crashes in default web viewports. |
| **Web** vs **Native** | `SessionSummaryModal.tsx:L210-212` | Web applies standard CSS `boxShadow` values instead of iOS-specific `shadow*` and Android-elevation styles. | Native styling properties like `shadowOpacity` are ignored by React Native Web render engines. |
| **Web** vs **Native** | `CustomSlider.tsx:L95-99` | Web appends `touchAction: 'none'` and `userSelect: 'none'` styles. | Prevents browser scroll events and text-selection popups from intercepting panning gestures. |
| **iOS** vs **Android** | `CommunityModal.tsx:L284` | `presentationStyle="pageSheet"` (applicable to `GlobalPermissionsModal` as well). | Produces the card-sheet overlay layout on iOS 13+, falling back to default full-screen modals on Android. |

---

## 6. Sequence Diagrams

### A. Cloud Scene Application Flow
This diagram details the sequence of actions occurring when a user applies a community-shared or personal lighting scene to their hardware.

```mermaid
sequenceDiagram
    autonumber
    actor Skater
    participant UI as CommunityModal
    participant SC as SceneCard
    participant Svc as ScenesService
    participant Parent as DashboardScreen (onApplyScene)
    participant BLE as BleWriteQueue

    Skater->>UI: Open Cloud Scenes Modal
    UI->>Svc: getPublicScenes() or getMyScenes()
    Svc-->>UI: Return Scene list
    UI->>SC: Render Scene item with LedStripPreview
    Skater->>SC: Tap "Apply" on Scene
    SC->>UI: handleApply(scene)
    UI->>Parent: Invoke onApplyScene(scene.scene_payload)
    Parent->>BLE: Enqueue payload as 0x59 pattern write
    Note over BLE: Payload is debounced & written via GATT write
    alt is activeTab === 'COMMUNITY'
        UI->>Svc: downloadScene(scene.id)
        Svc->>Svc: Increment download metric in DB
    end
    UI->>Skater: Display Alert.alert("Applied!")
    UI->>Skater: Close Modal
```

### B. Device Settings Hardware Probe and Configuration Save Flow
This diagram illustrates how settings configurations are queried (probed) and committed to physical skates.

```mermaid
sequenceDiagram
    autonumber
    actor Skater
    participant UI as DeviceSettingsModal
    participant Disp as useProtocolDispatch
    participant BLE as BleConnectionManager / GATT
    participant DB as AsyncStorage / Supabase

    Skater->>UI: Tap "PROBE" on unconfigured device settings
    UI->>UI: Set probeStatus = 'connecting'
    UI->>Disp: queryHardwareSettings(false, deviceId)
    Disp->>BLE: Write 0x63 EEPROM inquiry command
    BLE-->>UI: Receive notify callback (new points/segments/sorting)
    Note over UI: React useEffect detects initialSettings changes
    UI->>UI: Update input values, set probeStatus = 'complete'
    
    rect rgb(20, 20, 30)
        Note over Skater, DB: Skater modifies configurations and saves
        Skater->>UI: Tap "SAVE CONFIG"
        UI->>DB: Invoke onSave() to persist fleet changes
        UI->>Disp: writeSettingsByName(points, segments, strip, sorting, deviceId)
        Disp->>BLE: Enqueue 0x62 EEPROM write command
        UI->>Skater: Close Modal
    end
```

---

## 7. Design System & Token Manifest

```
Colors (Dark/Light)  ├── background       #1B4279  /  #EAEFF5
                     ├── surface          #245596  /  #CBD6E2
                     ├── surfaceHighlight #3172C9  /  #DDE5EE
                     ├── primary (Brand)  #FF5A00  (SK8Lytz Orange)
                     ├── secondary        #FFB800  (Skate Amber)
                     └── accent           #FF3300  /  #1B4279
Typography (Font)    └── Family           'Righteous' (Symmetrical geometric round style)
                     ├── header           size 24, letter-spacing 2, uppercase
                     ├── title            size 16, letter-spacing 0.5
                     ├── body             size 14
                     └── caption          size 11
Spacing (8pt grid)   └── xxs: 2 | xs: 4 | sm: 8 | md: 12 | lg: 16 | xl: 24 | xxl: 32
Layout               ├── padding          16 (Spacing.lg)
                     └── borderRadius     24 (Spacing.xl)
Shadows              ├── soft             iOS: shadowOffset {y:2, r:4}, op:0.15 | Android: elevation 3
                     ├── medium           iOS: shadowOffset {y:4, r:6}, op:0.2  | Android: elevation 5
                     └── glow(color)      iOS: shadowRadius:8, op:0.8           | Android: elevation 8
```

---

## 8. Architectural Impact Flags

- `[IMPACTS_USER_JOURNEY]` — The modal flows directly affect the core user experience when managing groups, tweaking strip lengths, reviewing logs, and downloading community presets.
- `[IMPACTS_STATE_CHART]` — Probing and saving settings directly triggers state transitions in the BLE adapter services and updates device status values in `BleMachine`.

---


Stale references and outdated layout policies found in `tools/SK8Lytz_App_Master_Reference.md`:

<!-- CARTOGRAPHER_END: UI_MODALS -->

### Domain: UI_VISUALIZER
<!-- CARTOGRAPHER_START: UI_VISUALIZER -->

# UI_VISUALIZER Domain Architectural Cartography

This document provides a comprehensive, read-only architectural audit of the `UI_VISUALIZER` domain in the SK8Lytz codebase, detailing component functions, dependency interfaces, state contexts, data mapping registers, OS platform deviations, runtime sequence flows, and the Design System tokens.

---

## 1. File Manifest

Every file in the `UI_VISUALIZER` domain mapped to its exact architectural purpose:

| File | Path | Architectural Purpose |
|---|---|---|
| **VisualizerUnit** | [VisualizerUnit.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VisualizerUnit.tsx) | Computes coordinate-math layouts and renders physical LED paths (`RING`, `DUAL_STRIP`, `OVAL`) using a multi-layer glow simulation model, throttled to 30 FPS on web and 60 FPS on native. |
| **ProductVisualizer** | [ProductVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/ProductVisualizer.tsx) | Outer wrapper component managing the shared `Animated.Value` timer loop to drive synchronous animation ticks for single or paired skate visualizer units. |
| **LEDStripPreview** | [LEDStripPreview.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/LEDStripPreview.tsx) | Renders a 2D linear row of colored blocks to preview pattern engine effects, optimized with hash-based change checks and 20 FPS timers. |
| **CustomEffectVisualizer** | [CustomEffectVisualizer.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomEffectVisualizer.tsx) | Renders a small horizontal row of circular LED dots to preview custom effects with dynamic timing adjustments for breathing/static/scrolling transitions. |
| **NeonHueStrip** | [NeonHueStrip.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/NeonHueStrip.tsx) | Renders a full-spectrum linear color strip using `LinearGradient` and implements a dedicated `PanResponder` gesture responder for lag-free local slider changes. |
| **PositionalGradientBuilder** | [PositionalGradientBuilder.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/PositionalGradientBuilder.tsx) | Visual node editor allowing users to insert, remove, position, and color-code pins on a 0-100% strip, and dispatch generated RGB arrays over BLE with 100ms throttle protection. |
| **VerticalPatternDrum** | [VerticalPatternDrum.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/VerticalPatternDrum.tsx) | An infinite-scrolling vertical selector wheel mapping list positions to pattern IDs, designed with haptic reticle overlays and 3D shadow masks. |
| **GradientLibraryTab** | [GradientLibraryTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/GradientLibraryTab.tsx) | Displays a list of custom and built-in gradients in a two-column grid showing 12-block static strips generated by the positional buffer. |
| **PatternCard** | [PatternCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/PatternCard.tsx) | Renders a pattern selection card with glassmorphism overlays, required color indicators (FG/BG dots), and an embedded live `LEDStripPreview`. |
| **PatternPickerTab** | [PatternPickerTab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/PatternPickerTab.tsx) | Categorized grid layout selector for patterns utilizing an `onViewableItemsChanged` flatlist viewport gate that automatically pauses/plays individual card animations to conserve CPU. |
| **UnifiedPatternPicker** | [UnifiedPatternPicker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx) | Coordinates pattern card selection and delegates hex-to-rgb conversion and `0x59` BLE payload packaging to the dispatcher. |
| **CameraTracker (iOS/Android)** | [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx) | Integrates `react-native-vision-camera` feed with a native GPU frame resizer worklet running at 5Hz to extract environment colors (Sniper/Vibe modes). |
| **CameraTracker (Web)** | [CameraTracker.web.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.web.tsx) | Stubs out the camera tracking interface for non-native environments (Expo Web) to prevent compiler and bundler failures on missing JSI packages. |
| **CameraTracker Types** | [CameraTracker.d.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.d.ts) | Core TypeScript interface declaration mapping props and types across native and web CameraTracker components. |

---

## 2. Blast Radius (Imports & Exports)

Cross-domain dependency mappings mapping what this domain relies upon and what consumes its modules:

```
[External Contexts / Modules]
        │
        ▼ (Imports)
┌────────────────────────────────────────────────────────────────────────┐
│ UI_VISUALIZER Domain                                                   │
│                                                                        │
│  protocols/PatternEngine.ts        ◄─── (Frame calculations & opcodes) │
│  protocols/PositionalMathBuffer.ts ◄─── (Gradient pixel interpolation) │
│  protocols/ZenggeProtocol.ts        ◄─── (Opcode binary serialization)  │
│  hooks/useGradients.ts             ◄─── (Loads custom presets)         │
│  services/PermissionService.ts     ◄─── (Native camera permissions)    │
│  utils/kMeansPalette.ts            ◄─── (Dominant color extraction)    │
└────────────────────────────────────────────────────────────────────────┘
        │
        ▼ (Exports)
[Consumer Panels]
  - components/docked/ProEffectsPanel.tsx   ◄── (UnifiedPatternPicker)
  - components/docked/BuilderPanel.tsx      ◄── (PositionalGradientBuilder)
  - components/docked/CameraPanel.tsx       ◄── (CameraTracker)
  - components/docked/UniversalSliders.tsx  ◄── (NeonHueStrip)
  - components/admin/tools/Scheduler.tsx    ◄── (PositionalGradientBuilder)
  - components/DockedController.tsx         ◄── (ProductVisualizer)
```

---

## 3. Context Matrix

Usage of React Contexts consumed by the `UI_VISUALIZER` components:

| Context | Hook / Consumer | Purpose | Scope |
|---|---|---|---|
| **ThemeContext** | `useTheme` / `VisualizerUnit`, `ProductVisualizer`, `NeonHueStrip`, `PositionalGradientBuilder`, `VerticalPatternDrum`, `UnifiedPatternPicker` | Receives theme updates (light/dark mode toggle) and provides the active brand color variables to UI elements. | Read-Only |
| **AuthContext** | `useAuth` / `useGradients` | Accesses the active skateri'd `user.id` to load custom gradients from Supabase. | Read-Only |

---

## 4. Hook/Service I/O Registry

Detailed parameters, outputs, and side-effects for hooks and services used within the visualizer domain:

### `useGradients` (Hook)
* **Inputs:** None (reads user session internally from `AuthContext`).
* **Outputs:**
  * `gradients`: `CustomBuilderPreset[]` — Loaded gradient models.
  * `isLoading`: `boolean` — Current fetch status.
  * `status`: `'idle' | 'loading' | 'error' | 'success'` — FSM state.
  * `error`: `string | null` — Last error message.
  * `saveGradient`: `(preset: Partial<CustomBuilderPreset>) => Promise<void>` — Saves/updates gradient.
  * `deleteGradient`: `(id: string) => Promise<void>` — Deletes custom gradient.
  * `refreshGradients`: `() => Promise<void>` — Forces database refetch.
* **Side-effects:**
  * Fetches user presets from the `GradientsService` Supabase client on mount.
  * Triggers event logs to `AppLogger.error()` on failures.
  * Increments `favorites_created` user telemetry counter on new saves.

### `extractKMeansPalette` (Service Helper)
* **Inputs:**
  * `pixels`: `RGB[]` — Source pixel buffer array.
  * `k`: `number` — Target centroid cluster count (hardcoded to `3`).
  * `maxIterations`: `number` — Convergence limit (hardcoded to `5`).
* **Outputs:** `RGB[]` — Extracted dominant centroids.
* **Side-effects:** None (pure mathematical clustering).

---

## 5. OS Variance Matrix

Critical path branches and execution differences between target platforms (iOS, Android, Web):

| File / Component | Platform / API | Branching Mechanism | Architectural Difference |
|---|---|---|---|
| **CameraTracker** | **Web / Expo Web** | `.web.tsx` file resolution override | Renders a standard message component saying "Camera Not Available". Prevents web bundler crashes on native-only packages (`react-native-vision-camera`, `react-native-worklets-core`). |
| **CameraTracker** | **iOS & Android** | `.tsx` file execution | Renders raw camera stream. Spawns high-performance C++ worklet frames to sample colors at 5Hz using JSI buffers. |
| **VisualizerUnit** | **Web vs Native** | `Platform.OS === 'web'` | Throttles frame rates. Web runs at **30 FPS** to prevent JS MessageQueue flooding. Native runs at **60 FPS** (or uncapped). |
| **PatternPickerTab** | **Web vs Native** | `Platform.OS !== 'web'` | Disables Animated Native Driver on Web (`useNativeDriver: false`) for category pill scale transforms to avoid styling compiler warnings. |
| **Shadows & Glow** | **iOS vs Android** | `Platform.select()` in `theme.ts` | Shadow render engine variances. iOS uses `shadowColor`/`shadowOpacity`/`shadowRadius` styles. Android uses `elevation` and `shadowColor` properties. |

---

## 6. Sequence Diagram: Camera-to-BLE Color Loop

The diagram below maps the runtime flow of environment color tracking, processing on the JSI boundary, updating state within React, and executing a serialized dispatch to the skate hardware over BLE:

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant CT as CameraTracker (VisionCamera)
    participant GPU as GPU Resizer Worklet (C++ JSI)
    participant JS as JS Thread Callback (dispatchSniperColor)
    participant DC as DockedController (State Owner)
    participant PV as ProductVisualizer (Anim Loop)
    participant VU as VisualizerUnit (Render Dot Array)
    participant BLE as BleWriteQueue / Dispatcher
    participant HW as Skate Controller Hardware

    User->>CT: Opens Camera Sniper Tab
    CT->>CT: Requests Camera Permissions
    Note over CT: Frame Processor fires at 30fps
    CT->>GPU: onFrame (yuv format frame)
    Note over GPU: Throttled to 5Hz (200ms check)
    GPU->>GPU: useResizer.resize(frame) to 50x50 RGB
    alt subMode === 'SNIPER'
        GPU->>GPU: Grab Center Pixel [25, 25]
    else subMode === 'VIBE'
        GPU->>GPU: Run K-Means Clustering (k=3)
    end
    GPU->>CT: resized.dispose() && frame.dispose()
    GPU->>JS: runOnJS(dispatchSniperColor/Palette)(r, g, b)
    JS->>DC: onColorDetected(hexColor)
    DC->>DC: Update selectedColor state
    par Immediately update UI preview
        DC->>PV: Rerender with new colors
        PV->>VU: Rerender Visualizer dots
        VU->>VU: recalculate HSL / Diffusion Blends (60fps)
    and Dispatch to hardware (after 100ms throttle)
        DC->>BLE: writeToDevice(ZenggeProtocol.setMultiColor)
        BLE->>HW: Sends 0x59 raw pixel payload via GATT
        Note over HW: Controller animates LEDs forever (fire-and-forget)
    end
```

---

## 7. Design System & Token Manifest

The visualizer domain is governed by the core token specifications from `src/theme/theme.ts` alongside visualizer-specific optical metrics:

### Core Tokens
* **Brand Theme Colors:**
  * **Brand Blue:** `#1B4279` / `#245596`
  * **Brand Orange:** `#FF5A00`
  * **Brand Amber:** `#FFB800`
* **Theme Palettes:**
  * **Dark Mode:**
    * background: `#1B4279`
    * surface: `#245596`
    * surfaceHighlight: `#3172C9`
    * primary: `#FF5A00`
    * secondary: `#FFB800`
    * accent: `#FF3300`
    * text: `#FFFFFF`
    * textMuted: `#A0B4CF`
    * textDim: `#6B85A0`
    * border: `#2E5FA3`
  * **Light Mode:**
    * background: `#EAEFF5`
    * surface: `#CBD6E2`
    * surfaceHighlight: `#DDE5EE`
    * primary: `#FF5A00`
    * secondary: `#FFB800`
    * accent: `#1B4279`
    * text: `#0A1C38`
    * textMuted: `#5C7491`
    * textDim: `#8A9EB5`
    * border: `#B0C0D0`
* **Typography (Family: `Righteous`):**
  * `header`: `fontSize: 24`, `letterSpacing: 2`, `textTransform: 'uppercase'`
  * `title`: `fontSize: 16`, `letterSpacing: 0.5`
  * `body`: `fontSize: 14`
  * `caption`: `fontSize: 11`
* **Spacing Scale:**
  * `xxs`: 2, `xs`: 4, `sm`: 8, `md`: 12, `lg`: 16, `xl`: 24, `xxl`: 32, `xxxl`: 40, `huge`: 48, `giant`: 64
* **Layout:**
  * `padding`: `Spacing.lg` (16)
  * `borderRadius`: `Spacing.xl` (24)

### Visualizer Geometry & Physics Specs
* **Scale Factor:** `S = 0.38` (shrinks coordinates to fit preview containers).
* **Hardware Profile Maps:**
  * **HALOZ:** Layout Shape `RING`, 8 addressable LEDs per segment, 2 segments (16 total physical), LED dot diameter `7.6`mm, base layout size `60`x`90`px, `isMirrored: false` (hardware duplicates segment 1 to segment 2 automatically).
  * **SOULZ:** Layout Shape `OVAL`, 43 addressable LEDs per segment, 1 segment (86 total physical across 2 boots, Y-wired), LED dot diameter `5.7`mm, base layout size `55`x`115`px, `isMirrored: false`.
  * **RAILZ:** Layout Shape `DUAL_STRIP`, 30 addressable LEDs per segment, 2 segments (30 total physical), LED dot diameter `5.0`mm, base layout size `80`x`120`px, `isMirrored: true` (software-mirrored vertical strips), separation distance `32`mm.
* **Optical Diffusion Layer Model:**
  * **Layer 3 (Atmospheric Scatter):** `5.5`× diameter, opacity `0.03`
  * **Layer 2 (Silicone Bloom):** `3.2`× diameter, opacity `0.10`
  * **Layer 1 (Concentrated Halo):** `1.7`× diameter, opacity `0.38`
  * **Main Emitter Body:** `1.0`× diameter, active color
  * **Emitter Core Hotspot:** `0.32`× diameter, color `'rgba(255,255,255,0.55)'`
  * **Silicon Outer Glaze:** `1.0`× diameter, color `'rgba(255,255,255,0.09)'`, border `0.5px`, color `'rgba(255,255,255,0.05)'`

<!-- CARTOGRAPHER_END: UI_VISUALIZER -->

### Domain: DATA_LAYER
<!-- CARTOGRAPHER_START: DATA_LAYER -->

# 🗺️ DATA_LAYER Architectural Cartography Report

This document details the read-only architectural audit of the data persistence, synchronization, and caching layer of the SK8Lytz App.

---

## 1. File Manifest

Every file in the `DATA_LAYER` domain is catalogued below with its canonical architectural purpose:

| File Path | Architectural Purpose |
| :--- | :--- |
| `src/services/DeviceRepository.ts` | Local-first, cloud-second Single Source of Truth (SSOT) for registering, configuring, mutating, and tombstoning devices and custom groups. |
| `src/services/TelemetryService.ts` | Utility for parsing raw BLE write payloads and error strings to extract payload sizes, operation types, and GATT status codes (e.g. status 133). |
| `src/services/ScenesService.ts` | Manages creation, local AsyncStorage caching, background Supabase sync, downloading, and upvoting of lighting scenes. |
| `src/services/SpeedTrackingService.ts` | Captures and persists live session metrics, caches lifetime statistics, and queues session data to be uploaded to Supabase. |
| `src/services/GradientsService.ts` | Handles local persistence and cloud synchronization for custom lighting gradient presets created via the positional mathematical editor. |
| `src/services/SkateSpotsService.ts` | Coordinates cache-first fetching of skate spots, claim updates to crowdsourced spots, and fallback querying to OpenStreetMap Nominatim endpoints. |
| `src/services/SessionShareService.ts` | Generates textual sharing invites for sessions and triggers the native OS Share Sheet wrapper. |
| `src/types/supabase.ts` | Auto-generated TypeScript type definitions representing the complete remote relational schema on Supabase. |
| `src/services/supabaseClient.ts` | Instantiates the Supabase JS client with custom Expo SecureStore adapters and provides structural fallback mocks when offline. |
| `src/hooks/cloud/useOfflineSyncWorker.ts` | Periodic background interval worker (60s loop) that triggers scenes, sessions, and telemetry log uploads to the cloud. |
| `src/hooks/useFavorites.ts` | Coordinates stateful loading, saving, naming, and deleting of user-saved presets and quick presets. |
| `src/hooks/useScenes.ts` | Exposes local scene list retrieval and removal state hooks to the UI, delegating queries to `ScenesService`. |
| `src/hooks/useCuratedPicks.ts` | Implements stale-while-revalidate fetching of curated lighting picks from `sk8lytz_picks` with active status validation. |
| `src/hooks/useGradients.ts` | Exposes custom gradient listings and mutation states, delegating queries to `GradientsService`. |
| `src/hooks/useSkateStats.ts` | Lazily loads and caches lifetime statistics and historical session files when the stats view is rendered. |
| `src/hooks/useRecentSpots.ts` | Manages a capped list (last 10 entries) of recently visited or tapped spots using AsyncStorage. |
| `src/hooks/useMapFilters.ts` | Manages map filtering matrices (rinks, parks, shops, sessions) and applies filtering checks. |
| `src/context/FavoritesContext.tsx` | Context provider that exposes a shared singleton projection of `useFavorites` states across react components. |

---

## 2. Blast Radius (Imports/Exports)

```mermaid
graph TD
    subgraph External
        SDK[Supabase JS SDK]
        AS[AsyncStorage]
        SS[SecureStore]
    end

    subgraph DATA_LAYER [DATA_LAYER Domain]
        Client[supabaseClient.ts]
        Repo[DeviceRepository.ts]
        ScenesSvc[ScenesService.ts]
        SpeedSvc[SpeedTrackingService.ts]
        GradSvc[GradientsService.ts]
        SpotsSvc[SkateSpotsService.ts]
        SyncWorker[useOfflineSyncWorker.ts]
        FavHook[useFavorites.ts]
    end

    subgraph UI_CONSUMERS [UI Controllers]
        DockedCtrl[DockedController.tsx]
        MapScrn[MapScreen.tsx]
        Wiz[SetupWizard.tsx]
    end

    %% External bindings
    Client --> SS
    Repo --> AS
    ScenesSvc --> AS
    SpeedSvc --> AS
    GradSvc --> AS
    SpotsSvc --> AS

    %% Internal relationships
    Repo --> Client
    ScenesSvc --> Client
    SpeedSvc --> Client
    GradSvc --> Client
    SpotsSvc --> Client

    SyncWorker --> ScenesSvc
    SyncWorker --> SpeedSvc

    FavHook --> Client

    %% UI to data layer
    DockedCtrl --> FavHook
    DockedCtrl --> ScenesSvc
    MapScrn --> SpotsSvc
    Wiz --> Repo
```

---

## 3. Context Matrix

This domain consumes and provides the following React Contexts:

*   **AuthContext (`useAuth`)**:
    *   *Consumed by*: `useOfflineSyncWorker.ts`, `useFavorites.ts`, `useScenes.ts`, `useGradients.ts`, `useSkateStats.ts`.
    *   *Purpose*: Obtains the currently authenticated `user.id` to route cloud reads/writes and prevent sync cycles for anonymous sessions.
*   **FavoritesContext (`FavoritesProvider` / `useSharedFavorites`)**:
    *   *Provided by*: `FavoritesContext.tsx` (wrapping `useFavorites.ts` base hook).
    *   *Consumed by*: UI controller components (e.g. `DockedController.tsx`).
    *   *Purpose*: Provides a single global registry of active presets, quick presets, loading states, and mutation callbacks to prevent redundant fetches.

---

## 4. Hook/Service I/O Registry

### `DeviceRepository` (Singleton Service)
*   **`initialize()`**:
    *   *Input*: None.
    *   *Output*: `Promise<void>`.
    *   *Side-effects*: Reads cached device arrays, custom settings maps, and tombstone lists from AsyncStorage to build the in-memory cache.
*   **`saveDevice(device, userId)`**:
    *   *Input*: `device: Partial<RegisteredDevice> & { device_mac: string }`, `userId?: string`.
    *   *Output*: `Promise<boolean>`.
    *   *Side-effects*: Updates memory cache immutably, removes matching MAC address from the local tombstone list, writes to local storage, and invokes Supabase group transaction RPCs or queues details to the offline sync key if the network fails.
*   **`deleteDevice(deviceMac, userId)`**:
    *   *Input*: `deviceMac: string`, `userId?: string`.
    *   *Output*: `Promise<void>`.
    *   *Side-effects*: Adds MAC to the local tombstone list, filters the device out of memory arrays, wipes specific device settings configs, and issues a DELETE query to Supabase.
*   **`syncFromCloud(userId)`**:
    *   *Input*: `userId?: string`.
    *   *Output*: `Promise<RegisteredDevice[]>`.
    *   *Side-effects*: Queries remote `registered_devices`, filters out records in the local tombstone list, merges settings priorities (local updates override defaults), and flushes unsynced local creations or tombstoned deletes.

### `ScenesService` (Singleton Service)
*   **`getSavedScenes(userId)`**:
    *   *Input*: `userId?: string`.
    *   *Output*: `Promise<Scene[]>`.
    *   *Side-effects*: Delivers local cached scene arrays; schedules background sync updates with Supabase in a fire-and-forget promise.
*   **`saveScene(scene, userId)`**:
    *   *Input*: `preset: Partial<Scene>`, `userId?: string`.
    *   *Output*: `Promise<Scene>`.
    *   *Side-effects*: Inserts or updates matching IDs locally and appends a `SceneSyncJob` to local storage queues.
*   **`flushSyncQueue(userId)`**:
    *   *Input*: `userId: string`.
    *   *Output*: `Promise<void>`.
    *   *Side-effects*: Executes batch inserts and deletes of scenes to Supabase, resolving the queue file once complete.

### `SpeedTrackingService` (Singleton Service)
*   **`saveSkateSession(session, userId)`**:
    *   *Input*: `session: Omit<ISkateSession, 'id'>`, `userId?: string`.
    *   *Output*: `Promise<ISkateSession>`.
    *   *Side-effects*: Commits session metrics locally, updates local lifetime statistics (Optimistic UI), and appends data to `@Sk8lytz_PendingSessionQueue`.

---

## 5. OS Variance Matrix

*   **Dynamic Download & App Store URIs** (`SessionShareService.ts`):
    *   Standardized deep links are built via React Native's `Platform.select`:
        *   **iOS**: `https://apps.apple.com/app/sk8lytz`
        *   **Android**: `https://play.google.com/store/apps/details?id=com.neogleamz.sk8lytz`
*   **Native Share Sheet Payload Structure** (`SessionShareService.ts`):
    *   iOS requires passing links inside the distinct `url` argument to force rendering of rich preview cards:
        ```typescript
        ...(Platform.OS === 'ios' ? { url: APP_LINK } : {})
        ```
*   **Supabase Session Persistence Engine** (`supabaseClient.ts`):
    *   Resolves adapters depending on the compilation profile:
        *   **Web/Expo-Go-Web**: Employs `localStorage`.
        *   **Native (iOS/Android)**: Implements asynchronous Expo SecureStore APIs (`SecureStore.getItemAsync`, `SecureStore.setItemAsync`, `SecureStore.deleteItemAsync`) to store and retrieve authentication session tokens securely.

---

## 6. Database Schema & RLS Policies

```
 ┌──────────────────────────────────────────────────────────┐
 │                    Supabase Database                     │
 └────────────────────────────┬─────────────────────────────┘
                              │
       ┌──────────────────────┼──────────────────────┐
       ▼                      ▼                      ▼
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│  registered_ │       │  user_saved_ │       │    shared_   │
│   devices    │       │   presets    │       │    scenes    │
└──────────────┘       └──────────────┘       └──────────────┘
```

*   **`registered_devices`**:
    *   *Columns*: `id` (PK), `device_mac` (unique constraint per user), `user_id` (FK to auth), `device_name`, `custom_name`, `position`, `group_id`, `group_name`, `points`, `led_points`, `segments`, `sorting`, `strip_type`, `firmware_ver`, `led_version`, `product_id`, `is_pending_sync`, `last_lat`, `last_lng`.
    *   *RLS Constraint*: `authenticated` users can `SELECT`, `INSERT`, `UPDATE`, and `DELETE` records where `auth.uid() === user_id`.
*   **`user_saved_presets`**:
    *   *Columns*: `id` (PK), `user_id` (FK to auth), `name`, `fill_mode` (e.g. `'FAVORITE'`, `'GRADIENT'`), `transition_type`, `nodes` (Json representation of lighting points), `created_at`, `updated_at`.
    *   *RLS Constraint*: Users can perform CRUD operations only on presets matching `auth.uid() === user_id`.
*   **`shared_scenes`**:
    *   *Columns*: `id` (PK), `author_id` (FK to auth), `author_username`, `name`, `scene_payload` (Json), `downloads`, `upvotes`, `is_public`, `created_at`.
    *   *RLS Constraint*: Read permission is public (`is_public = true`). Increment updates (upvotes/downloads) are executed via server-side RPC functions.

---

## 7. Environment & Secrets Manifest

*   **`EXPO_PUBLIC_SUPABASE_URL`**:
    *   *Description*: The endpoint hosting the remote PostgREST database engine and GoTrue authentication APIs.
*   **`EXPO_PUBLIC_SUPABASE_ANON_KEY`**:
    *   *Description*: The public client API token parsed at initialization to bypass API gateway firewalls.

---

## 8. Offline Sync Queue Architecture

The app uses a local-first pattern to handle offline states:

1.  **Local Buffering**: Writes are recorded locally in AsyncStorage instantly (e.g. `saveSkateSession` or `saveScene`).
2.  **Queue Keys**:
    *   `@Sk8lytz_SceneSyncQueue`: Scene sync payloads.
    *   `@Sk8lytz_PendingSessionQueue` (`PENDING_SESSION_QUEUE_KEY`): Queue of complete session metrics.
    *   AppLogger diagnostic buffers.
3.  **Background Loop**: The `useOfflineSyncWorker` loops every 60 seconds, invoking flushes of these queues to Supabase.
4.  **Resilience**: Flushes exit gracefully on connection timeouts, keeping the local storage keys populated until a success response clears the queues.

### Offline Sync Lifecycle

```mermaid
sequenceDiagram
    autonumber
    actor User as Skater App
    participant Storage as AsyncStorage Queue
    participant Worker as useOfflineSyncWorker (60s)
    participant Client as Supabase Client
    participant DB as Supabase DB

    User->>Storage: saveSkateSession() / saveScene() (Queued)
    Note over Storage: Records stacked offline

    loop Every 60 seconds
        Worker->>Worker: Trigger RunSync()
        Worker->>Storage: Read active queue items
        Storage-->>Worker: Return queue arrays
        Worker->>Client: flushPendingSessionQueue(userId)
        alt Device is Online
            Client->>DB: Bulk Upsert/RPC Transactions
            DB-->>Client: HTTP 201 Success
            Client-->>Worker: Resolve
            Worker->>Storage: Remove item from queue (Slice)
        else Device is Offline
            Client-xDB: Network Failure (Timeout)
            Client-->>Worker: Catch Exception (Fail Silently)
            Note over Storage: Queue preserved for next loop
        end
    end
```

---

## 9. Architectural Impact Flags

*   *No surface modifications were applied to the data persistent layer during this read-only cartography audit.*
*   `[NO_ARCHITECTURAL_IMPACT]`

<!-- CARTOGRAPHER_END: DATA_LAYER -->

### Domain: UTILS
<!-- CARTOGRAPHER_START: UTILS -->

# UTILS & TYPES Domain Cartography

This document contains a comprehensive architectural audit and dependency mapping of the **UTILS** and **TYPES** domains in the SK8Lytz codebase, located under `src/utils/*` and `src/types/*` (excluding `supabase.ts`).

---

## 1. File Manifest

Below is the list of all files in these domains, with a one-sentence architectural purpose for each:

### 📱 Types (`src/types/*`)

| File | Architectural Purpose |
| :--- | :--- |
| [ProductCatalog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/ProductCatalog.ts) | Defines data structures for the dynamic product catalogue registry, visualizer geometries, and FTUE point auto-classification bounds. |
| [ble.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/ble.types.ts) | Centralizes type contracts for the low-level BLE connectivity framework, re-exporting `react-native-ble-plx` types. |
| [bleGuards.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/bleGuards.ts) | Provides TypeScript runtime type guards (`isDevice`) to narrow down BLE peripherals without utilizing unsafe `any` casts. |
| [dashboard.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/dashboard.types.ts) | Establishes domain contracts for user favorite settings, presets, modal router states, and the stabilized `DockedBus` panel communication interface. |
| [react-test-renderer.d.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/react-test-renderer.d.ts) | Declares ambient test modules to ensure clean compilation of Jest/testing dependencies. |

### 🛠️ Utilities (`src/utils/*`)

| File | Architectural Purpose |
| :--- | :--- |
| [BlePayloadParser.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/BlePayloadParser.ts) | Stateless parser utility wrapping hardware protocol decoders to extract EEPROM configuration bytes from notification payloads. |
| [ColorUtils.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/ColorUtils.ts) | Implements pure color translation math (Hex/RGB/HSV/Hue) and the ambient light saturation booster algorithm. |
| [CrashReporter.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/CrashReporter.ts) | Captures fatal application crashes and routes stack traces securely through the central logging system. |
| [FlightRecorder.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/FlightRecorder.ts) | Manages an in-memory ring-buffer containing the 50 most recent navigation, action, and network events for diagnostics. |
| [MusicDictionary.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/MusicDictionary.ts) | Authoritative registry detailing speed, brightness, and color mode parameters for all 46 hardware-native music patterns. |
| [NamingUtils.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/NamingUtils.ts) | Standardizes identity names for newly discovered BLE hardware and default fallback groups. |
| [NormalizationUtils.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/NormalizationUtils.ts) | Maps UI slider values (0-100) to hardware-acceptable ranges (1-31) to prevent device errors. |
| [backoff.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/backoff.ts) | Generates randomized timing jitter to prevent stampeding reconnect attempts. |
| [classifyBLEDevice.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/classifyBLEDevice.ts) | Combines advertisement data with cached EEPROM settings to dynamically map discovered peripherals to registered catalog items. |
| [kMeansPalette.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/kMeansPalette.ts) | Performs dominant color clustering of pixel lists via high-performance Worklet-annotated execution routines. |
| [migrateAuthTokens.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/migrateAuthTokens.ts) | Migrates Supabase authentication JWT tokens from local unencrypted AsyncStorage to platform SecureStore. |
| [piiScrubber.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/piiScrubber.ts) | Scrubs personal identifiers (MACs, names) into secure hashes to avoid exposure in cloud telemetry. |
| [presetColorUtils.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/presetColorUtils.ts) | Resolves complex gradients, card glows, and UI icons for generative vs. monochromatic modes. |
| [webStyles.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/webStyles.ts) | Provides a simple styling compatibility shim for web testing targets. |

---

## 2. Blast Radius (Import/Export Map)

The **UTILS** and **TYPES** domains act as fundamental, leaves-of-the-tree dependencies. Changes in these modules have a wide impact surface.

```mermaid
graph TD
    %% Imports into UTILS/TYPES
    AppConstants[constants/AppConstants.ts] --> NormalizationUtils[utils/NormalizationUtils.ts]
    ProductCatalogConst[constants/ProductCatalog.ts] --> classifyBLEDevice[utils/classifyBLEDevice.ts]
    PatternEngine[protocols/PatternEngine.ts] --> presetColorUtils[utils/presetColorUtils.ts]
    ControllerRegistry[protocols/ControllerRegistry.ts] --> BlePayloadParser[utils/BlePayloadParser.ts]
    storageKeys[constants/storageKeys.ts] --> migrateAuthTokens[utils/migrateAuthTokens.ts]
    
    %% Internal relations
    NamingUtils[utils/NamingUtils.ts] --> classifyBLEDevice[utils/classifyBLEDevice.ts]
    dashboard_types[types/dashboard.types.ts] --> presetColorUtils[utils/presetColorUtils.ts]
    dashboard_types --> classifyBLEDevice[utils/classifyBLEDevice.ts]
    
    %% Exports to core modules
    classifyBLEDevice --> useBLEScanner[hooks/ble/useBLEScanner.ts]
    BlePayloadParser --> useBLEScanner
    kMeansPalette --> useFrameProcessor[Camera Mode Frame Processor]
    ColorUtils --> useFrameProcessor
    presetColorUtils --> PresetCard[components/docked/PresetCard.tsx]
    presetColorUtils --> SkateGroupCard[components/dashboard/SkateGroupCard.tsx]
    dashboard_types --> DockedController[components/DockedController.tsx]
    dashboard_types --> useDashboardGroups[hooks/useDashboardGroups.ts]
    dashboard_types --> DeviceRepository[services/DeviceRepository.ts]
```

### 📥 Internal Dependencies (What UTILS/TYPES Import)
*   `react-native-ble-plx`: Imports core BLE interfaces (`BleManager`, `Device`, `Subscription`).
*   `expo-secure-store` & `@react-native-async-storage/async-storage`: For credentials migration.
*   `src/services/AppLogger`: Logs diagnostic errors and warning events.
*   `src/constants/ProductCatalog` & `src/constants/AppConstants`: Consumes hardcoded hardware limits and profile defaults.
*   `src/protocols/PatternEngine`: Looks up pattern templates to determine rendering modes.
*   `src/protocols/ControllerRegistry`: Fetches the current protocol driver for payload extraction.

### 📤 External Consumers (What Imports UTILS/TYPES)
*   **Screens**: `DashboardScreen.tsx` (consumes dashboard types, preset validators), `HardwareSetupWizardScreen.tsx`.
*   **Hooks**: `useBLE.ts`, `useOptimisticBLE.ts`, `useDashboardGroups.ts`, `useFavorites.ts`, `useStreetMode.ts`, `useAppMicrophone.ts`.
*   **Services**: `DeviceRepository.ts`, `GroupRepository.ts`, `InterrogatorService.ts`, `BlePingService.ts`.
*   **UI Components**: `DeviceItem.tsx`, `DeviceSettingsModal.tsx`, `DockedController.tsx`, `PresetCard.tsx`, `SkateGroupCard.tsx`, `UniversalSlidersFooter.tsx`.

---

## 3. Context Matrix

The UTILS and TYPES domains are pure, stateless elements and do not provide or consume React Contexts directly.

> [!NOTE]
> While they do not initialize Contexts, their models are heavily integrated with Context interfaces:
> *   `AuthContext.tsx` consumes output from `migrateAuthTokens.ts` to manage auth sessions.
> *   `CrewContext.tsx` references `BleConnectionState` and `CustomGroup` from `dashboard.types.ts`.
> *   `DockedController.tsx` uses the `DockedBus` type contract as the API surface for custom control panels.

---

## 4. Hook/Service I/O Registry

Since this domain contains no React hooks or native stateful services, this registry maps the primary stateless services and utilities:

### ⚙️ `BlePayloadParser` (Stateless Decoder)
*   **`parseLedPayload`**
    *   **Input**: `payload: number[]` (raw notification array).
    *   **Output**: `ParsedLedConfig | null` (resolved points, segments, IC name, RGB order, and diagnostics row).
    *   **Side Effects**: Logs malformed packets to `AppLogger.warn`.
*   **`parseRfPayload`**
    *   **Input**: `payload: number[]` (raw byte packet).
    *   **Output**: `ParsedRfConfig | null` (parsed RF access modes, paired remote array).
    *   **Side Effects**: Logs malformed packets to `AppLogger.warn`.

### 📊 `extractKMeansPalette` (Worklet Optimizer)
*   **Input**: `pixels: RGB[]`, `k: number` (default 3), `maxIterations: number` (default 5).
*   **Output**: `RGB[]` (sorted array containing $K$ dominant color coordinates, padded with black if input is sparse).
*   **Side Effects**: Executes within the React Native Reanimated worklet runtime thread.

### 🔐 `migrateAuthTokensToSecureStore` (Security task)
*   **Input**: none.
*   **Output**: `Promise<void>`.
*   **Side Effects**: Reads unencrypted tokens from `AsyncStorage`, writes them to `SecureStore`, purges legacy `AsyncStorage` values, and writes a completion flag. Logs results via `AppLogger`.

### 🎨 `presetColorUtils` (UI Visual Resolvers)
*   **`resolveGradientColors`**
    *   **Input**: `fav: IFavoriteState`, `glow: string`.
    *   **Output**: `string[]` (resolved hex values for card backgrounds). Routes to a 7-stop rainbow (`GENERATIVE_RAINBOW`) for generative patterns, 2-stop gradient for FX/Music, or solid swatches.
*   **`resolveGroupCardColors`**
    *   **Input**: `snapshot: GroupPatternSnapshot | undefined`, `fallback: string[]`.
    *   **Output**: `string[]` (resolved hex values mapping to the stored FSM state snapshot).

---

## 5. OS Variance Matrix

Specific code paths branch or adapt depending on OS constraints:

| Category | iOS Path | Android Path | Rationale |
| :--- | :--- | :--- | :--- |
| **Shadow Rendering** | `shadowColor`, `shadowOffset`, `shadowOpacity`, `shadowRadius` | `elevation` | Android uses a material elevation model; iOS requires explicit Core Graphics layers. |
| **Text Shadowing** | `textShadowColor`, `textShadowRadius`, `textShadowOffset` | `textShadowColor`, `textShadowRadius`, `textShadowOffset` | Standardized on mobile. Web targets use CSS string formats. |
| **GATT MTU Negotiation** | Negotiated automatically by iOS during socket initialization. | Manual `requestMTU()` call required post-connect. | The Android BLE stack throws if MTU configuration is not explicitly requested; iOS rejects MTU commands. |
| **Keychain Storage** | iOS secure hardware Keychain. | Android secure Keystore. | SecureStore wraps platform-native storage providers. |
| **Worklet Execution** | JavascriptCore (JSC) / Swift thread serialization. | V8 / Java Native Interface (JNI) worklet scheduling. | re-animated worklets run on platform-native UI threads. |
| **PII Hashing** | Custom bitwise loop | Custom bitwise loop | React Native lacks cross-platform node-style `crypto` support out-of-the-box, necessitating a custom fallback. |

---

## 6. Design System & Token Manifest

Extracted from `src/theme/theme.ts`.

### 🎨 Color Palettes

```json
{
  "DarkColors (Default)": {
    "background": "#1B4279",
    "surface": "#245596",
    "surfaceHighlight": "#3172C9",
    "primary": "#FF5A00",
    "secondary": "#FFB800",
    "accent": "#FF3300",
    "text": "#FFFFFF",
    "textMuted": "#A0B4CF",
    "textDim": "#6B85A0",
    "border": "#2E5FA3",
    "success": "#00E88F",
    "error": "#FF3D71",
    "warning": "#FFB800"
  },
  "LightColors": {
    "background": "#EAEFF5",
    "surface": "#CBD6E2",
    "surfaceHighlight": "#DDE5EE",
    "primary": "#FF5A00",
    "secondary": "#FFB800",
    "accent": "#1B4279",
    "text": "#0A1C38",
    "textMuted": "#5C7491",
    "textDim": "#8A9EB5",
    "border": "#B0C0D0",
    "success": "#00C476",
    "error": "#FF3D71",
    "warning": "#E07A00"
  }
}
```

### 📐 Layout & Spacing
*   **Base Spacing Grid**: `xxs` (2px), `xs` (4px), `sm` (8px), `md` (12px), `lg` (16px), `xl` (24px), `xxl` (32px), `xxxl` (40px), `huge` (48px), `giant` (64px).
*   **Component Layouts**: standard page padding is set to `Spacing.lg` (16px); default component corner radius is set to `Spacing.xl` (24px).

### 🗚 Typography (Righteous Brand Font)
*   **Header**: `fontSize: 24`, `letterSpacing: 2`, `textTransform: 'uppercase'`, `fontFamily: 'Righteous'`.
*   **Title**: `fontSize: 16`, `letterSpacing: 0.5`, `fontFamily: 'Righteous'`.
*   **Body**: `fontSize: 14`, `fontFamily: 'Righteous'`.
*   **Caption**: `fontSize: 11`, `fontFamily: 'Righteous'`.

---

## 7. Sequence Diagrams

### 1. BLE Auto-Classification Pipeline

This flow shows how a discovered peripheral gets mapped from raw BLE advertisements and EEPROM pings into a catalog-matched `PendingRegistration`:

```mermaid
sequenceDiagram
    autonumber
    actor User as Skate Onboarding UI
    participant Scanner as useBLEScanner
    participant Sweeper as useBLEBatterySweep
    participant Interrogator as InterrogatorService
    participant Parser as BlePayloadParser
    participant Mapper as classifyBLEDevice (mapDeviceToRegistration)
    participant Catalog as ProductCatalog constants

    Scanner->>Sweeper: Trigger scan for new devices
    Sweeper->>Interrogator: Discovers device, pushes to Interrogate Queue
    Interrogator->>Parser: Probes EEPROM (0x63) & parses response
    Parser-->>Interrogator: Returns ParsedLedConfig (points, segments)
    Interrogator->>Sweeper: Updates local hwCache[MAC]
    Sweeper-->>Scanner: Resolves scanned devices list with hwCache
    Scanner->>Mapper: Calls mapDeviceToRegistration(device, index, hwCache)
    Mapper->>Catalog: resolveProductType using cached ledPoints
    Catalog-->>Mapper: Returns matched ProductProfile (e.g. RAILZ)
    Mapper-->>User: Delivers finalized PendingRegistration object
```

### 2. Camera Mode Color Extraction & Saturation Boost Flow

This flow tracks how raw GPU-resized video frames are clustered and modified for optimal WS2812B LED projection:

```mermaid
sequenceDiagram
    autonumber
    participant Camera as CameraTracker (vision-camera-resize-plugin)
    participant Worklet as useFrameOutput (GPU Worklet Thread)
    participant KMeans as kMeansPalette (extractKMeansPalette)
    participant Boost as ColorUtils (boostForLED)
    participant Dispatch as useControllerDispatch
    participant HW as ZENGGE Controller

    Camera->>Worklet: Delivers 50x50 raw RGB frame at 5Hz
    Worklet->>KMeans: Invokes worklet-safe clustering extractKMeansPalette(pixels, 3)
    KMeans-->>Worklet: Returns 3 dominant colors (FG, BG, Accent)
    Worklet->>Boost: Tapping "Apply" runs boostForLED(r, g, b) on swatches
    Note over Boost: Maximizes HSV Saturation & Value<br/>WS2812B Emitters require separation to avoid washed-out white
    Boost-->>Worklet: Returns boosted high-chroma RGB values
    Worklet->>Dispatch: Constructs spatial array & triggers writeToDevice
    Dispatch->>HW: Dispatches spatial 0x59 payload (min 12 pixels to prevent lockout)
```

### 3. Secure Auth Token Migration Pipeline

This flow details how credential storage shifts from AsyncStorage to secure Keychain on application startup:

```mermaid
sequenceDiagram
    autonumber
    participant Boot as App.tsx Entry Point
    participant Migration as migrateAuthTokensToSecureStore
    participant AS as AsyncStorage
    participant SS as expo-secure-store (Keychain / Keystore)
    participant Log as AppLogger

    Boot->>Migration: Runs migration check on mount
    Migration->>AS: Checks flag (STORAGE_AUTH_MIGRATION_FLAG)
    alt flag === 'true'
        AS-->>Migration: Already migrated
        Migration-->>Boot: Skip execution
    else flag !== 'true'
        AS-->>Migration: Not migrated
        Migration->>AS: Reads legacy JWT token (STORAGE_SUPABASE_AUTH_KEY)
        alt legacy token exists
            AS-->>Migration: Returns JWT token string
            Migration->>SS: Checks for existing secure credentials
            alt secure credentials empty
                Migration->>SS: Securely writes JWT token to Keychain
            end
            Migration->>AS: Purges legacy token from AsyncStorage
            Migration->>Log: Logs info: "Migrated Supabase auth token"
        end
        Migration->>AS: Writes migration flag = 'true'
        Migration-->>Boot: Complete
    end
```

---

## 8. Stale Documentation Archive Review

During this audit, two stale visualizer references were identified inside `tools/SK8Lytz_App_Master_Reference.md`:

1.  **Line 679**:
    ```markdown
    ```
    *Status*: Confirmed. `RbmSimulator.ts` has been removed. Frame rendering is now fully handled inside `protocols/SymphonyEngine.ts`. **Recommendation**: Archive/remove line.

2.  **Line 685**:
    ```markdown
    ```
    *Status*: Confirmed. **Recommendation**: Archive/remove line.

<!-- CARTOGRAPHER_END: UTILS -->

### Domain: NATIVE_&_WATCH
<!-- CARTOGRAPHER_START: NATIVE_&_WATCH -->

# NATIVE_&_WATCH Domain Cartography

This document contains a comprehensive architectural audit and mapping of the `NATIVE_&_WATCH` domain, detailing the iOS/watchOS target, the Android/Wear OS module, and the custom Expo native watch bridge.

---

## 1. File Manifest

Every file within the `NATIVE_&_WATCH` domain is catalogued below with its architectural purpose:

| File Path | Architectural Purpose |
|:---|:---|
| **TypeScript / Expo Bridge Interface** | |
| `modules/sk8lytz-watch-bridge/src/index.ts` | Entry point defining public API types, listeners, and methods for the `WatchBridge` native module wrapper. |
| `src/__mocks__/sk8lytz-watch-bridge.ts` | Jest unit test mock implementation of the native watch bridge module to prevent testing runtime crashes. |
| **iOS / watchOS Native Target** | |
| `modules/sk8lytz-watch-bridge/ios/Sk8lytzWatchBridgeModule.swift` | iOS Swift implementation of the Expo native module, mapping JS actions to Apple's `WCSession`. |
| `targets/watch/expo-target.config.js` | Expo Apple Target Configuration specifying watchOS bundle identifiers, deployment targets, ClockKit complications, and HealthKit entitlements. |
| `targets/watch/index.swift` | The entry point for the watchOS SwiftUI application. |
| `targets/watch/ContentView.swift` | The main SwiftUI application UI, coordinating the dashboard, elapsed session stopwatch, and post-session summaries. |
| `targets/watch/WatchConnectivityManager.swift` | Coordinates WCSession activation, inbound payload parsing, and haptic execution, acting as the local watch state source of truth. |
| `targets/watch/HealthManager.swift` | Coordinates HealthKit authorization and runs the active `HKWorkoutSession` + `HKLiveWorkoutBuilder` to maintain background app execution. |
| `targets/watch/ComplicationController.swift` | Implements `CLKComplicationDataSource` to render live speeds and session indicators on Apple Watch faces. |
| **Android / Wear OS Native Companion** | |
| `modules/sk8lytz-watch-bridge/android/src/main/java/expo/modules/sk8lytzwatchbridge/Sk8lytzWatchBridgeModule.kt` | Android Kotlin implementation of the Expo native module, mapping JS actions to Google Play Services Wearable APIs. |
| `plugins/withWearOsModule.js` | Expo Config Plugin integrating the Wear OS Gradle module dependency (`wearApp project(':sk8lytzWear')`) and injecting Android 14 foreground service types. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/MainActivity.kt` | The entry point activity for the Jetpack Compose-based Wear OS application. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/SessionState.kt` | Enum defining Wear OS session tracking phases (`IDLE`, `ACTIVE`, `PAUSED`, `SUMMARY`). |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/DashboardScreen.kt` | Jetpack Compose UI rendering the active speed, stopwatch, heart rate, and calorie counters. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/SummaryScreen.kt` | Jetpack Compose UI rendering final post-session summaries with a 10-second auto-dismiss timer. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt` | Handles command dispatches to the phone and buffers telemetry updates locally if the phone is disconnected. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt` | Extends `WearableListenerService` to receive background pushes from the phone and update watch face Tiles. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/HealthTracker.kt` | Orchestrates the Android Health Services `ExerciseClient` to collect live heart rate and calorie metrics. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/OngoingActivityManager.kt` | Registers an active workout notification and ongoing activity status visible in Wear OS UI entry points. |
| `android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/tiles/Sk8lytzTileService.kt` | Generates a glanceable watch face Tile showing active speed/status when swiped in the system carousel. |

---

## 2. Blast Radius (Imports & Exports)

The SDE dependency scanner maps the imports and exports of the `NATIVE_&_WATCH` domain as follows:

```
[System Location & GPS]      [Apple HealthKit & WCSession]      [Google Play Wearable Data Layer]
           │                                │                                    │
           ▼                                ▼                                    ▼
    [Phone Host App] ──────────────► [Native Bridge] ◄────────────────────► [Watch Companions]
           │                    (modules/sk8lytz-watch-bridge)                      │
           ▼                                                                        ▼
   • SessionContext.tsx                                                    • targets/watch/ (watchOS)
   • useGlobalTelemetry.ts                                                  • sk8lytzWear/ (Wear OS)
   • SpeedTrackingService.ts
```

### Module Level Imports
- **iOS/watchOS Native Frameworks**: Consumes Apple `WatchConnectivity`, `HealthKit`, `ClockKit`, and `SwiftUI`.
- **Android/Wear OS Native Frameworks**: Consumes Google Play Services Wearable APIs (`com.google.android.gms:play-services-wearable`), Jetpack Compose, Android Health Services (`androidx.health:health-services-client`), and Android Ongoing Activity (`androidx.wear.ongoing:ongoing`).
- **Expo Framework**: Consumes `expo-modules-core` Gradle and Swift plugins for module registration.
- **Phone Notifications**: Consumes `@notifee/react-native` for location background foreground services.

### Module Level Exports
- **React Native Hook Interactions**: Exports `WatchBridge` interface to React Native. Consumed by:
  - `src/context/SessionContext.tsx`: Listens for `onWatchCommandReceived` and `onWatchHealthUpdate`. Coordinates starts/stops.
  - `src/hooks/useGlobalTelemetry.ts`: Syncs session changes (e.g. pauses/resumes) to the watch.
  - `src/services/SpeedTrackingService.ts`: Pushes live GPS speed telemetry to the watch via `sendMetricUpdate`.

---

## 3. Context Matrix

The React Context interactions of this domain are captured below:

| Context Name | Provider Location | Consumer Location | Purpose |
|:---|:---|:---|:---|
| `SessionContext` | `src/context/SessionContext.tsx` | All UI Components | Reads the active session status, speed telemetry, and aggregates watch-relayed heart rate/calories. |
| `AuthContext` | `src/context/AuthContext.tsx` | `SessionContext.tsx` | Links the user identity to the completed session logs when stopping tracking. |

---

## 4. Hook/Service I/O Registry

### `WatchBridge` Native API (TypeScript Wrapper)
- **`syncSessionState(state: WatchSessionState)`**
  - *Inputs*: `state: WatchSessionState` (status: `"ACTIVE" | "PAUSED" | "SUMMARY" | "STOPPED"`, speed, HR, calories, startTime ISO-8601, totalDuration, distance, avgSpeed, peakHR).
  - *Outputs*: `Promise<void>`.
  - *Side-Effects*: Publishes persistent session states to the watch. iOS: updates `WCSession.updateApplicationContext()`. Android: writes `DataClient.putDataItem` on `/sk8lytz/state`.
- **`sendMetricUpdate(metrics: WatchHealthUpdate)`**
  - *Inputs*: `metrics: WatchHealthUpdate` (speed, heartRate, calories).
  - *Outputs*: `Promise<void>`.
  - *Side-Effects*: Dispatches real-time, un-cached metric updates. iOS: sends ephemeral `WCSession.sendMessage`. Android: sends `MessageClient.sendMessage` on `/sk8lytz/metrics` to all paired nodes.
- **`addWatchCommandListener(handler)`**
  - *Inputs*: `handler: (update: { command: "START_SESSION" | "STOP_SESSION" }) => void`.
  - *Outputs*: `Subscription` unsubscribe callback.
  - *Side-Effects*: Automatically invokes native `startListening` to hook the background messaging event streams.
- **`addWatchHealthListener(handler)`**
  - *Inputs*: `handler: (update: WatchHealthUpdate) => void`.
  - *Outputs*: `Subscription` unsubscribe callback.
  - *Side-Effects*: Subscribes to incoming health packages forwarded by the watch.

### `SpeedTrackingService` (Throttler Service)
- **`pushSpeedToWatch(speedMph, calories, heartRateBpm)`**
  - *Inputs*: `speedMph: number`, `calories: number`, `heartRateBpm: number`.
  - *Outputs*: `void`.
  - *Side-Effects*: Throttles output rate limit to a maximum of **one dispatch per 3000ms** to protect watch batteries and prevent transceiver flooding.

---

## 5. OS Variance Matrix

Bidirectional communication mechanisms differ significantly between Apple and Google watch ecosystems:

| Feature / System Area | iOS / watchOS Behavior | Android / Wear OS Behavior | Architectural Safeguard |
|:---|:---|:---|:---|
| **Sync Protocol** | `WCSession`: updates context or sends dictionary messages. | Wearable Data Layer: `DataClient` (persistent) and `MessageClient` (ephemeral). | State mapping abstracts the underlying framework into unified `syncSessionState` and `sendMetricUpdate` methods. |
| **Telemetry Buffering** | No queue buffering. WCSession context overrides older states on disconnect. | SharedPreferences queue buffer caches heart rate/calories during disconnects and flushes on connection recovery. | **Android Only**: Wear OS `WearMessageSender` writes JSON arrays to local memory storage when `connectedNodes` is empty. |
| **Background Execution** | HealthKit `HKWorkoutSession` keeps SwiftUI watch app alive in background. | Android Health Services `ExerciseClient` + Foreground Service + OngoingActivity Notification keeps Wear OS alive. | Watch targets initialize workout sessions on active state to maintain persistent background CPU allocation. |
| **Workout Category** | `HKWorkoutConfiguration` activityType is `.skatingSports`. | `ExerciseConfig` exerciseType is `ExerciseType.INLINE_SKATING`. | Custom health activity profiles map to the roller skating context to optimize calorie algorithms. |
| **Auto-Recovery** | Not supported. iOS health relay payloads omit status/start time properties. | Supported. Wear OS health updates include active state and start time to trigger phone session rebuilds. | **Android Only**: Android health payloads include `status: "ACTIVE"` and `startTimeMs` to restore session structures on reboot. |

---

## 6. Sequence Diagram: Phone-Watch Communication

The following sequence diagram details the execution flows during tracking sessions:

```mermaid
sequenceDiagram
    autonumber
    actor User as User on Wrist
    participant WUI as Watch UI (SwiftUI/Compose)
    participant WCon as Watch Conn Manager / Service
    participant Sens as Sensor Client (HealthKit/HS)
    participant Bridge as Sk8lytzWatchBridge (Native Module)
    participant Phone as Phone App (SessionContext)

    %% Session Initiation
    User->>WUI: Tap "Start Session"
    WUI->>Sens: Start Workout Session (skatingSports / INLINE_SKATING)
    WUI->>WCon: sendStartSession() / sendCommand("START_SESSION")
    Note over WCon: Vibrates wrist haptic buzz
    WCon->>Bridge: Send command message (/sk8lytz/command)
    Bridge->>Phone: Emit event 'onWatchCommandReceived'
    Phone->>Phone: startSession(new timestamp anchor)
    Phone->>Bridge: syncSessionState(status: ACTIVE, startTime)
    Bridge->>WCon: Sync session state context (/sk8lytz/state)
    WCon->>WUI: Set UI State: ACTIVE, start tick stopwatch
    
    %% Telemetry Stream
    loop Active Tracking Loop
        Sens->>WCon: Collect sensor data (HR, Calories)
        WCon->>Bridge: Relay watch health update (/sk8lytz/health, throttled 5s)
        Bridge->>Phone: Emit event 'onWatchHealthUpdate'
        Phone->>Phone: Merge watch metrics into Session state
        
        Phone->>Bridge: sendMetricUpdate(speed from GPS, throttled 3s)
        Bridge->>WCon: Send metric update (/sk8lytz/metrics)
        WCon->>WUI: Refresh display & reload watch complications
    end
    
    %% Session Stop
    User->>WUI: Tap "Stop Session"
    WUI->>Sens: Stop workout session
    WUI->>WCon: sendStopSession() / sendCommand("STOP_SESSION")
    WCon->>Bridge: Send command message (/sk8lytz/command)
    Bridge->>Phone: Emit event 'onWatchCommandReceived'
    Phone->>Phone: stopSession() (calculates final metrics)
    Phone->>Bridge: syncSessionState(status: SUMMARY, final metrics)
    Bridge->>WCon: Pushes session summary (/sk8lytz/state)
    WCon->>WUI: Display Summary screen, freeze elapsed stopwatch
    Note over WUI: Displays summary layout for 10 seconds
    loop After 10 seconds
        WUI->>WCon: dismissSummary() / Auto-Dismiss
        WCon->>WUI: Reset to IDLE view
    end
```

---

## 7. Build Configuration & Native Integration

```
[expo-target.config.js] ────────► Generates Watch Target plist, icons, and entitlements (iOS)
[withWearOsModule.js] ──────────► Injects settings.gradle, build.gradle, and Android 14 tags (Android)
```

### iOS watchOS Build Pipeline (`targets/watch/expo-target.config.js`)
Configured using `@bacons/apple-targets` app plugin:
- **Deployment Target**: `10.0` (provides SwiftUI support).
- **Entitlements**: `"com.apple.developer.healthkit": true` to authorize direct sensor streaming on watch hardware.
- **ClockKit Complication Binding**: `CLKComplicationPrincipalClass` references `$(PRODUCT_MODULE_NAME).ComplicationController` to bind the background dial display.
- **InfoPlist Requirements**: Bundle description strings explaining HealthKit read (`NSHealthShareUsageDescription`) and write (`NSHealthUpdateUsageDescription`) usage to pass App Store review.

### Android Wear OS Gradle Integration (`plugins/withWearOsModule.js`)
Configured via custom Expo Config Plugin to inject gradle properties on prebuild:
- **Module Injection**: Injects `include ':sk8lytzWear'` and directory bindings to the main `settings.gradle`.
- **Dependency Association**: Inject `wearApp project(':sk8lytzWear')` to `app/build.gradle`. Forces Gradle to bundle the Wear OS APK inside the primary Android package (Google Play Store compliance requirement).
- **Foreground Service Alignment**: Modifies `@notifee/react-native` service declaration in the merged `AndroidManifest.xml` to include:
  `android:foregroundServiceType="location|health|connectedDevice|shortService|dataSync"`
  This avoids crash lockouts on Android 14+ when tracking skates and syncing sensors simultaneously.

---

## 8. SDE Vulnerability & Bug Detections

### 🔴 WATCH-001: iOS watchOS health updates lack 'status' and 'startTimeMs' fields, disabling auto-recovery on Apple Watch
- **Consequence**: If the phone app is killed or restarts in the background and returns to the `IDLE` state while the Apple Watch is tracking an active session, the session will never auto-recover. This is because `WatchConnectivityManager.swift` only relays heart rate and calories:
  ```swift
  self.send([
      "healthUpdate": true,
      "heartRate": Int(hr),
      "calories": Int(cal)
  ])
  ```
  On Android, the equivalent `HealthTracker.kt` sends the current session state and anchor:
  ```kotlin
  WearMessageSender.sendHealthUpdate(ctx, hr, cal, statusStr, WearableCommunicationService.sessionStartTimeMs)
  ```
  This allows `SessionContext.tsx` to automatically trigger:
  ```typescript
  if (update.status === 'ACTIVE' && sessionPhaseRef.current === 'IDLE') {
    startSessionRef.current(update.startTimeMs);
  }
  ```
- **Remediation**: Update `WatchConnectivityManager.swift` to append the missing tracking fields in the health relay message:
  ```swift
  let statusStr = self.showingSummary ? "SUMMARY" : (self.isPaused ? "PAUSED" : (self.isSessionActive ? "ACTIVE" : "IDLE"))
  let startTimeMs = self.sessionStartTime != nil ? Int64(self.sessionStartTime!.timeIntervalSince1970 * 1000) : 0
  self.send([
      "healthUpdate": true,
      "heartRate": Int(hr),
      "calories": Int(cal),
      "status": statusStr,
      "startTimeMs": startTimeMs
  ])
  ```

### 🔴 WATCH-002: Lacking persistent health data queuing on iOS watchOS on disconnect
- **Consequence**: When the watch app is disconnected from the phone, `WatchConnectivityManager.swift` delegates back to `WCSession.updateApplicationContext()`. This API overwrites the context dictionary. On connection recovery, only the *latest* calorie and heart rate snapshot is delivered to the phone, causing a loss of intermediate workout data. On Android, `WearMessageSender.kt` stores a JSON list inside SharedPreferences (`telemetry_buffer_prefs`), providing a resilient time-series queue that is fully flushed upon reconnect.
- **Remediation**: Implement a local SQLite or Swift array buffer in `WatchConnectivityManager.swift` to cache telemetry updates during unreachable states and stream them in order once `WCSession.default.isReachable` becomes true.

---

## 9. Architectural Impact Flags

- `[IMPACTS_USER_JOURNEY]` — Modifications to watch state synchronization alter display views, haptic patterns, and phone-side stopwatch triggers.
- `[IMPACTS_STATE_CHART]` — The tracking lifecycle transitions (`IDLE`, `ACTIVE`, `PAUSED`, `SUMMARY`, `STOPPED`) coordinate and sync in real-time across both platform systems.

<!-- CARTOGRAPHER_END: NATIVE_&_WATCH -->

### Domain: NOTIFICATIONS_&_ROUTING
<!-- CARTOGRAPHER_START: NOTIFICATIONS_&_ROUTING -->

# Codebase Cartography — NOTIFICATIONS_&_ROUTING Domain

This document provides a comprehensive architectural audit of the `NOTIFICATIONS_&_ROUTING` domain in the SK8Lytz codebase. This domain spans app bootstrapping, legal compliance gates, system permissions, hardware telemetry dispatch, location-aware session discovery, and push notification routing.

---

## 1. File Manifest

| File Path | Primary Architectural Purpose |
| :--- | :--- |
| `App.tsx` | Entry component configuring global error boundaries, font loading, platform initializers, and provider hierarchies. |
| `src/providers/BluetoothGuard.tsx` | Layout gate blocking core rendering until Bluetooth permissions are granted and the device's adapter is verified active. |
| `src/providers/ComplianceGate.tsx` | Legal compliance gateway checking versioned EULA acceptance (locally for offline users, database-backed for online users). |
| `src/services/NotificationService.ts` | Orchestrates the Expo push notification lifecycle, permission requests, local scheduling, and callback delegation. |
| `src/services/PushTokenService.ts` | Synchronizes Expo push tokens with Supabase, handling upsert registrations and revoking on logout/revoke. |
| `src/services/LocationService.ts` | Wraps Expo Location for ambient coordinates discovery, geocoding venue labels, and sorting nearby skate spots/sessions. |
| `src/hooks/useHardwareNotifications.ts` | BLE data-received callback coordinator that debounces packets, parses configs, and updates local state & repositories. |

---

## 2. Blast Radius (Dependency Map)

```
                       ┌──────────────────────┐
                       │      App.tsx         │
                       └──────────┬───────────┘
                                  │
         ┌────────────────────────┼────────────────────────┐
         ▼                        ▼                        ▼
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│ BluetoothGuard  │      │ ComplianceGate  │      │  LocationServ.  │
└────────┬────────┘      └────────┬────────┘      └────────┬────────┘
         │                        │                        │
         ▼                        ▼                        ▼
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│ PermissionServ  │      │ AppSettingsServ │      │ SkateSpotsServ  │
└─────────────────┘      └─────────────────┘      └─────────────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │    Supabase     │
                         └────────▲────────┘
                                  │
         ┌────────────────────────┴────────────────────────┐
         │                                                 │
┌────────┴────────┐                               ┌────────┴────────┐
│  NotificationS. │ ────► [registerPushToken] ──► │  PushTokenServ. │
└────────┬────────┘                               └─────────────────┘
         │
         ▼
┌─────────────────┐
│ ProfileService  │ ──► [unregisterPushToken]
└─────────────────┘
```

### Imports (Incoming Dependencies)
- **External Packages**:
  - `expo-notifications` (via dynamic require in `NotificationService.ts` for non-web environments).
  - `expo-location` (in `LocationService.ts`).
  - `@react-native-async-storage/async-storage` (in `App.tsx`, `ComplianceGate.tsx`).
  - `react-native-health-connect` (via dynamic runtime require on Android in `App.tsx` [L138]).
- **Internal Modules**:
  - `PermissionService.ts` -> Used by `LocationService.ts` [L34] and `BluetoothGuard.tsx` [L20] to request OS location/Bluetooth permissions.
  - `DeviceRepository.ts` -> Used by `useHardwareNotifications.ts` [L136] to persist hardware and remote configurations.
  - `BlePayloadParser.ts` -> Used by `useHardwareNotifications.ts` [L110] to parse LED and RF configs.
  - `SkateSpotsService.ts` -> Used by `LocationService.ts` [L268] to read cached skate spots.

### Exports (Outgoing Impact)
- `App.tsx`: App entry point, loaded by `index.ts`.
- `BluetoothGuard.tsx` & `ComplianceGate.tsx`: Imported at the root of `App.tsx` to wrap `AppContent`.
- `NotificationService.ts`: Consumed by `useDashboardProfile.ts` [L107] to initialize push alerts and set the tapped crew-join callback.
- `LocationService.ts`: Consumed by `useBLEScanner.ts` [L104], `useCrewHub.ts` [L71], `useCrewProximityRadar.ts` [L34], and `DeviceRepository.ts` [L201] to retrieve current coordinates.
- `useHardwareNotifications.ts`: Hook consumed by `DashboardScreen.tsx` [L508] to handle all raw incoming notifications from the BLE stack.

---

## 3. Context Matrix

The domain implements or consumes the following React Contexts:

| Provider Name | File Source | Consumed By | Architectural Purpose |
| :--- | :--- | :--- | :--- |
| `GlobalErrorBoundary` | `src/components/GlobalErrorBoundary.tsx` | `App.tsx` | Traps uncaught JS rendering exceptions at the root to prevent white screens. |
| `SafeAreaProvider` | `react-native-safe-area-context` | `App.tsx` | Establishes safe area layout guidelines. |
| `ThemeContext` | `src/context/ThemeContext.tsx` | `App.tsx`, `ComplianceGate.tsx`, `BluetoothGuard.tsx` | Supplies style palettes (`Colors`) dynamically based on system dark/light modes. |
| `AuthContext` | `src/context/AuthContext.tsx` | `App.tsx`, `ComplianceGate.tsx` | Provides authentication parameters (`user`, `session`, `isOfflineMode`) to gates. |
| `AppConfigContext` | `src/context/AppConfigContext.tsx` | `App.tsx` | Wraps application configuration values. |
| `FavoritesContext` | `src/context/FavoritesContext.tsx` | `App.tsx` | Manages device color/pattern presets. |
| `SessionContext` | `src/context/SessionContext.tsx` | `App.tsx` | Manages the active skate session tracking lifecycle. |
| `BLEContext` | `src/context/BLEContext.tsx` | `App.tsx`, `BluetoothGuard.tsx` | Provides device scanning and state routines (`startSweeper`, `isBluetoothEnabled`). |

---

## 4. Hook/Service I/O Registry

### `useHardwareNotifications` (Hook)
- **Inputs**:
  - `isDiagnosticsMode` (`boolean`): Enables deep raw hex sniffer logging.
  - `setOnDataReceived` (`function`): Registers the BLE data listener.
  - `setOnHardwareProbed` (`function`): Registers the hardware configuration listener.
  - `allDevices` (`BLEDeviceMinimal[]`): Live devices list for MAC-to-name lookup.
  - `setAllDevices` (`updater`): Updates device list properties.
  - `setDeviceConfigs` (`updater`): Updates device configurations.
  - `deviceConfigs` (`Record<string, Record<string, unknown>>`): Exposing current configurations.
  - `setLastRawNotification` (`function`): Feeds the Diagnostic Lab Sniffer UI.
- **Outputs**: `void`
- **Side-Effects**: 
  - Attaches listeners to the BLE engine on mount, cleans up on unmount.
  - Debounces duplicate incoming data packets using a private ref cache (`lastPacketCacheRef`).
  - Calls `DeviceRepository.getInstance().updateConfig` and `confirmProductId` to sync to local/remote storage.
  - Dispatches `AppLogger.log('RAW_PAYLOAD')` telemetry entries when `isDiagnosticsMode` is enabled.

### `notificationService` (Service)
- **Methods**:
  - `init(autoRequest: boolean, userId?: string): Promise<string | null>`
    - *Inputs*: `autoRequest` (triggers prompt), `userId` (for Supabase mapping).
    - *Outputs*: Promise resolving to `ExpoPushToken` or `null`.
    - *Side-Effects*: Sets up Android notification channels; requests permissions; upserts token to Supabase via `pushTokenService`.
  - `setJoinHandler(handler: JoinHandler): void`
    - *Inputs*: Callback function `(crewId: string, sessionId: string) => void`.
    - *Side-Effects*: Saves callback ref for push invitation taps.
  - `cleanup(userId?: string): Promise<void>`
    - *Side-Effects*: Removes event listeners, unregisters token from Supabase via `profileService.unregisterPushToken`.
  - `sendCrewInviteNotification(opts): Promise<void>`
    - *Side-Effects*: Schedules immediate local OS notification on `crew-alerts` channel.
  - `sendSessionStartingSoon(opts): Promise<string | null>`
    - *Side-Effects*: Schedules local OS reminder on `session-reminders` channel. Returns notification ID.
  - `cancelSessionReminder(notificationId: string): Promise<void>`
    - *Side-Effects*: Cancels scheduled local notification.

### `pushTokenService` (Service)
- **Methods**:
  - `registerPushToken(token, platform, userId): Promise<void>`
    - *Inputs*: Token string, platform ('ios'|'android'|'web'), userId string.
    - *Side-Effects*: Performs database `upsert` in Supabase `push_tokens` table.
  - `unregisterPushToken(token, userId): Promise<void>`
    - *Side-Effects*: Deletes matching row from Supabase `push_tokens`.

### `locationService` (Service)
- **Methods**:
  - `getSessionLocation(): Promise<SessionLocation | null>`
    - *Outputs*: Location label & coordinates, or null.
    - *Side-Effects*: Spawns GPS permission check / modal flow; gets current position via `Location.getCurrentPositionAsync` (balanced accuracy); calls `Location.reverseGeocodeAsync`; logs `LOCATION_ACQUIRED` telemetry (filtering address detail to protect PII).
  - `getSilentLocation(): Promise<{lat, lng} | null>`
    - *Outputs*: Current coordinates, or null.
    - *Side-Effects*: Fetches cached location silently from `Location.getLastKnownPositionAsync` without initiating GPS or permissions.
  - `getNearbyPublicSessions(radiusMi, userCoords, userId): Promise<NearbySession[]>`
    - *Side-Effects*: Fetches active public and private user crew sessions from Supabase; performs Haversine distance computations; filters by radius.

---

## 5. OS Variance Matrix

| Feature | Android | iOS | Web / Simulation |
| :--- | :--- | :--- | :--- |
| **Health Connect Integration** | Requires dynamic initialize (`react-native-health-connect`) early on boot to prevent `UninitializedPropertyAccessException` runtime errors. | N/A (Uses HealthKit natively in Apple Target contexts). | N/A |
| **Push Notification Library** | Dynamically loaded via `require`. Registers physical notification channels (`crew-alerts`, `session-reminders`) with custom vibration and lights. | Dynamically loaded via `require`. Platform handles banner routing without channels. | Dynamic require skipped. Web mock fallbacks returned. |
| **Location Geocoding** | `Location.reverseGeocodeAsync` routes requests through Android's system Google Play Geocoder (Free). | `Location.reverseGeocodeAsync` routes requests through iOS's Apple Maps Geocoder (Free). | Returns a hardcoded static mock location (`Web Demo Area`, `38.9`, `-94.6`). |
| **BLE Addressing** | Handles raw MAC addresses (`XX:XX:XX:XX:XX:XX`) as device identifiers. | CoreBluetooth sanitizes raw MAC addresses, replacing them with virtual OS UUIDs. | N/A |

---

## 6. Sequence Diagrams

### Push Notification Registration & Initialization Pipeline
This diagram traces the flow during app startup when the `DashboardScreen` mounts, requesting push permissions and registering tokens.

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant App as Dashboard (useDashboardProfile)
    participant NS as NotificationService
    participant PTS as PushTokenService
    participant OS as OS Push Services
    participant DB as Supabase (push_tokens)

    App->>NS: init(autoRequest=false, userId)
    NS->>OS: setNotificationChannelAsync (Android only)
    NS->>OS: getPermissionsAsync()
    OS-->>NS: status (undetermined/denied/granted)
    
    alt Status is not granted AND autoRequest is true
        NS->>OS: requestPermissionsAsync()
        OS-->>User: Permission Prompt Dialog
        User-->>OS: Approve
        OS-->>NS: status (granted)
    end

    alt Status is granted
        NS->>OS: getExpoPushTokenAsync(projectId)
        OS-->>NS: token (ExpoPushToken[xxx])
        NS->>PTS: registerPushToken(token, platform, userId)
        PTS->>DB: upsert token mapping (conflict check on user_id, token)
        DB-->>PTS: Success
        PTS-->>NS: Complete
    end
    NS-->>App: returns token
```

### Push Notification Tap & Crew Routing Flow
This diagram details what happens when a user clicks a push invitation notification, launching or foregrounding the app and navigating to the active crew session.

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant OS as OS Notification Center
    participant NS as NotificationService
    participant App as DashboardScreen (DashboardCrewPanel)
    
    User->>OS: Taps Crew Invite Notification
    OS->>NS: Fires responseReceivedListener (payload with crewId, sessionId)
    NS->>NS: Extract data (crewId, sessionId)
    NS->>App: Invokes wired joinHandler(crewId, sessionId)
    Note over App: setPendingJoinCrewId(crewId) & setIsCrewModalVisible(true)
    App-->>User: Displays Crew Modal with active join prompts
```

### EULA Compliance Gating Flow
This flowchart traces the conditional checks performed by `ComplianceGate` to lock the UI until legal terms are validated.

```mermaid
sequenceDiagram
    autonumber
    participant App as AppContent / Dashboard
    participant Gate as ComplianceGate
    participant Local as AsyncStorage
    participant DB as Supabase (user_profiles)
    participant UI as EulaModal

    App->>Gate: Mount Gate (isOfflineMode, user)
    Gate->>Gate: checkCompliance()

    alt isOfflineMode is true
        Gate->>Local: getItem(STORAGE_EULA_ACCEPTED)
        Local-->>Gate: result (accepted version / null)
        alt result is null
            Gate->>UI: Show EulaModal (visible=true)
        else result is valid
            Gate->>App: Render children (DashboardScreen)
        end
    else isOfflineMode is false (Online)
        Gate->>DB: Query user_profiles (accepted_eula_version)
        DB-->>Gate: returns user_version (e.g., v0)
        Note over Gate: AppSettingsService fetches required_eula_version (e.g., v1)
        alt user_version < required_version
            Gate->>UI: Show EulaModal (visible=true)
        else user_version >= required_version
            Gate->>App: Render children (DashboardScreen)
        end
    end

    Note over UI: User accepts terms
    alt Offline User
        UI->>Gate: handleAccept()
        Gate->>Local: setItem(STORAGE_EULA_ACCEPTED, version_data)
        Gate->>App: Render children (DashboardScreen)
    else Online User
        UI->>Gate: handleAccept()
        Gate->>DB: update user_profiles (accepted_eula_version = required)
        Gate->>App: Render children (DashboardScreen)
    end
    
    Note over UI: User declines terms
    UI->>Gate: handleDecline()
    Gate->>App: Calls signOut() -> redirects to AuthScreen
```

---

## 7. Architectural Impact Flags

- `[IMPACTS_USER_JOURNEY]` — The permissions gates (`BluetoothGuard`, `ComplianceGate`) and push routing directly govern the user onboarding journey, app bootstrapping sequence, and deep-link routing transitions.

`[IMPACTS_USER_JOURNEY]`

<!-- CARTOGRAPHER_END: NOTIFICATIONS_&_ROUTING -->

### Domain: SESSION_TRACKING
<!-- CARTOGRAPHER_START: SESSION_TRACKING -->

# 🗺️ SESSION_TRACKING Domain Cartography

This document provides a read-only architectural audit of the **SESSION_TRACKING** codebase domain for the SK8Lytz mobile application. It captures the interaction pathways, sensor polling loops, native platform bridges, and database schemas governing user skate sessions.

---

## 1. 📂 File Manifest

| File Path | Architectural Purpose |
| :--- | :--- |
| `src/context/SessionContext.tsx` | Provides the core React Context (`SessionProvider` and `useSession`) to manage the state machine lifecycle of a session (`IDLE → ACTIVE → PAUSED → ENDING`), control native foreground services, handle watch commands, and coordinate child hooks. |
| `src/hooks/useGlobalTelemetry.ts` | Orchestrates high-frequency sensor streams (GPS speed/distance via balanced location watches; G-Force via the phone accelerometer) to build in-memory session accumulators. Handles auto-pause checks, auto-resume, and session data commits. |
| `src/hooks/useHealthTelemetry.ts` | Manages real-time biometrics (heart rate, active calories, peak/average heart rate) using a watch-preferred priority gateway that suppresses phone-side polling when active watch telemetry is detected. |
| `src/hooks/useTelemetryLedger.ts` | Collects high-level user engagement logs (time spent on specific LED patterns, color hex codes, controller modes) and queues them locally to AsyncStorage, periodically flushing the payloads via a Supabase RPC. |
| `src/hooks/useDeviceStateLedger.ts` | Serves as the single source of truth for the physical hardware state (active pattern config dispatched over BLE) of connected adapters. Caches mapping in-memory and debounces (500ms) writes to AsyncStorage. |
| `src/services/HealthSyncService.ts` | Integrates directly with native platform APIs to save completed workout sessions (skating exercises, duration, distance, and calories) into Apple HealthKit (iOS) or Health Connect (Android). |
| `src/services/SpeedTrackingService.ts` | Manages persistence layers for session snapshots, including database updates, local file/AsyncStorage buffering for offline/unauthenticated rides, queue flushing, and lifetime statistic retrievals. |

---

## 2. 💥 Blast Radius (Imports & Exports)

### 📥 Core Inputs (What this domain depends on)
- **Native Hardware APIs**:
  - `expo-location`: Balanced accuracy GPS tracking for real-time speed, distance, and coordinates.
  - `expo-sensors` (`Accelerometer`): High-frequency (80ms) accelerometer sampling for peak G-Force computation.
  - `@notifee/react-native`: Starts native Android Foreground Services (Location type) and displays ongoing notifications.
- **Watch Relay**:
  - `sk8lytz-watch-bridge` (`WatchBridge`): Dual-platform watch listener (commands and health logs) and metric transmitter.
- **Platform Health SDKs**:
  - `react-native-health` (iOS Apple HealthKit): Initiates SDK read/write blocks for workouts and biometric queries.
  - `react-native-health-connect` (Android Health Connect): Initiates Android Health Connect records insertion/reads.
- **Database & Sync Services**:
  - `supabase-js` Client: Direct insertions to `skate_sessions`, updates to `user_profiles`, and executions of `flush_telemetry`.
- **Cross-Domain Services**:
  - `AppLogger`: Core logging service for telemetric diagnostics and warning/error routing.
  - `PermissionService`: Global authorization checks for location and health access.
  - `CrewService`: Coordinates real-time session updates for active crews.

### 📤 Core Outputs (What imports this domain)
- **`SessionProvider` / `useSession()`**:
  - Imported by `App.tsx` (top-level initialization) and screen controllers (e.g. `DashboardScreen`, `StreetMode`, and header controls) to display session states, durations, and metrics.
- **`useDeviceStateLedger` / `warmLedgerCache()`**:
  - Imported by `DashboardScreen` and `DockedController` to synchronize device card preview statuses and restore last-saved mathematical patterns on hardware initialization.
- **`useTelemetryLedger`**:
  - Imported by docked panels (like `VerticalPatternDrum`, `MusicPanel`, and color selectors) to track feature interactions.
- **`SpeedTrackingService`**:
  - Consumed by background sync workers (`useOfflineSyncWorker`) to periodically clear offline files and profile statistics.

---

## 3. 🕸️ Context Matrix

```
┌──────────────────────────────────────────────────────────────────┐
│                          SessionProvider                         │
├──────────────────────────────────────────────────────────────────┤
│ Provides:                                                        │
│   - sessionPhase ('IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING')       │
│   - isSkateSessionActive (boolean)                               │
│   - startSession() / endSession() (callbacks)                    │
│   - telemetry (GlobalTelemetryState)                             │
│   - health (HealthTelemetry)                                     │
├──────────────────────────────────────────────────────────────────┤
│ Consumes:                                                        │
│   - AuthContext (via useAuth): Retrieves active user credentials  │
└──────────────────────────────────────────────────────────────────┘
```

- **BLE Coupling Decoupling**: The session lifecycle is intentionally decoupled from BLE connection states. Brief Bluetooth radio link disconnects do not drop or reset the active skate session; the GPS accumulators and timers remain persistent.

---

## 4. 🎛️ Hook/Service I/O Registry

### `useGlobalTelemetry` (Hook)
- **Input Parameters**:
  - `sessionPhase`: `'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING'`
  - `healthMetrics?`: `{ avgBpm: number \| null; peakBpm: number \| null; activeCalories: number \| null }`
  - `externalStartTimeMs?`: `number | null` (from watch state restoration)
- **Outputs**:
  - `GlobalTelemetryState` object containing:
    - `gpsSpeed` (mph)
    - `peakGForce` (G)
    - `sessionDistanceMiles` (mi)
    - `sessionDurationSec` (sec)
    - `sessionPeakSpeed` (mph)
    - `sessionAvgSpeed` (mph)
- **Side-Effects & Operations**:
  - Sets up a 1-second interval timer to increment `sessionDurationSec` when active.
  - Subscribes to `Location.watchPositionAsync` (balanced accuracy, 1s interval) to accumulate distance, peak/avg speeds, and map path coordinates (`pathCoords`).
  - Subscribes to `Accelerometer` (80ms interval), executing decay-math to filter out micro-fluctuations and state recalculations.
  - Feeds distance, peak speed, and averages to `crewService.sessionTelemetry` if a crew session is active.
  - Commits session data to `SpeedTrackingService.saveSession` upon transitioning back to `IDLE` (if distance >0.1mi or duration >60s).

### `useHealthTelemetry` (Hook)
- **Input Parameters**:
  - `sessionActive`: `boolean`
- **Outputs**:
  - `HealthTelemetry` object containing:
    - `latestBpm` (bpm)
    - `avgBpm` (bpm)
    - `peakBpm` (bpm)
    - `activeCalories` (cal)
    - `mergeWatchHealth` (function to manually merge watch data)
- **Side-Effects & Operations**:
  - Starts a 15-second interval timer to poll phone SDKs (HealthKit / Health Connect) when `sessionActive` is true.
  - Blocks phone-side SDK polling entirely if a watch health event occurred within the last 15 seconds (`WATCH_EXPIRY_MS`), deferring strictly to watch sensors.
  - Maintains `bpmSamplesRef` array to calculate average and peak heart rates.

### `useTelemetryLedger` (Hook)
- **Outputs**:
  - `trackPattern(patternId)`, `trackColor(hexCode)`, `trackMode(modeId)`: Stops the current timer and buffers elapsed engagement seconds.
  - `incrementCounter(key, count)`: Logs direct event counters.
  - `injectStreetSummary(distance, topSpeed)`: Manual telemetry insertion.
  - `flushToDatabase()`: Aggregates runtime values with AsyncStorage buffers (`@sk8lytz_telemetry_buffer`) and invokes Supabase RPC `flush_telemetry`.
- **Side-Effects & Operations**:
  - Sets up a shared 15-minute flush timer (`_sharedFlushTimer`).
  - Automatically triggers a flush to the database when the app enters `inactive` or `background` states via `AppState`.

### `useDeviceStateLedger` (Hook)
- **Outputs**:
  - `save(mac, state)`: Saves active BLE pattern configuration to Map cache and debounces (500ms) writes to AsyncStorage (`@SK8Lytz_DeviceState_v2_{MAC}`).
  - `load(mac)`: Performs async read from cache or AsyncStorage fallback.
  - `loadSync(mac)`: Synchronous cache-only lookup for fast UI renders.
  - `clear(mac)`: Wipes data from cache and local storage.
- **Side-Effects & Operations**:
  - Immediately clears pending debounced timers and flushes cache configurations to disk on AppState `background` change.

### `HealthSyncService` (Service)
- **Input Parameters**:
  - `saveWorkout(snapshot)`: Receives an `ISessionSnapshot` containing duration, distance, and calories.
- **Outputs**:
  - `Promise<void>`
- **Side-Effects & Operations**:
  - Checks if health permissions are available.
  - Logs the completed skating workout directly to native health databases (Apple HealthKit or Health Connect).

### `SpeedTrackingService` (Service)
- **Outputs**:
  - `saveSession(snapshot, userId)`: Persists completed sessions, syncs with native platforms, updates profiles.
  - `flushPendingSessionQueue(userId)`: Triggers background worker queue flushing.
  - `pushSpeedToWatch(speed, calories, hr)`: Relays live speed and metrics to connected watches.
- **Side-Effects & Operations**:
  - Controls offline AsyncStorage buffering.

---

## 5. 📱 OS Variance Matrix

| Module / Path | Android (`Platform.OS === 'android'`) | iOS (`Platform.OS === 'ios'`) |
| :--- | :--- | :--- |
| **Foreground Services** | **Notifee Foreground Service (FGS)**:<br>- Registers ongoing channel `sk8lytz-session` with LOW importance.<br>- Configures location FGS mapping: `foregroundServiceTypes: [FOREGROUND_SERVICE_TYPE_LOCATION]`.<br>- Requires app state to be `'active'` and location permissions granted on launch to start without throwing `SecurityException` (Android 14+ guard). | **Background Notification**:<br>- Standard notification display using category actions (`session-actions`) and action buttons (e.g. `'🛑 End Session'`). No FGS is required on iOS as the location loop runs natively in background. |
| **Health SDKs** | **Google Health Connect**:<br>- Uses `react-native-health-connect` module.<br>- Calls `initialize()` before reads.<br>- Uses ISO-8601 string-based filters.<br>- Maps exercise code `60` (Skating) and converts distance into meters (`miles * 1609.34`). | **Apple HealthKit**:<br>- Uses `react-native-health` module.<br>- Calls `initHealthKit` on demand.<br>- Uses date parameters for HeartRate / ActiveEnergy queries.<br>- Maps workout to activity type `'SkatingSports'`. |
| **Watch Integration** | Relayed via WearOS companion Data Client bridges (native modules underneath `WatchBridge`). | Relayed via WatchKit standard `WCSession` message logs (native modules underneath `WatchBridge`). |

---

## 6. 🔄 Session Telemetry Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor User as Skater
    participant SC as SessionContext
    participant GT as useGlobalTelemetry
    participant HT as useHealthTelemetry
    participant WB as WatchBridge (Native)
    participant NS as Notifee (FGS/Notification)
    participant STS as SpeedTrackingService
    participant HSS as HealthSyncService
    participant DB as Supabase Database

    %% Session Initiation
    User->>SC: startSession() / WatchCmd (START_SESSION)
    activate SC
    SC->>SC: setSessionPhase('ACTIVE')
    SC->>WB: syncSessionState(ACTIVE, startTime)
    SC->>NS: displayNotification() (Starts Android Foreground Service / iOS banner)
    deactivate SC

    %% Active Tracking Loop
    Note over SC,GT: Active Tracking Loop
    GT->>GT: watchPositionAsync() (balanced accuracy, Balanced/1s interval)
    GT->>GT: Accelerometer listener (80ms interval, decay math)
    GT->>STS: pushSpeedToWatch(speed, calories, heartRate)
    STS->>WB: sendMetricUpdate()
    
    alt Watch Active (relaying health every 5s)
        WB->>SC: addWatchHealthListener() callback
        SC->>HT: mergeWatchHealth(bpm, calories)
        Note over HT: Phone health polling is SUPPRESSED (isWatchHealthActive() = true)
    else Watch Inactive / Disconnected (no data for 15s)
        HT->>HT: pollHealthData() (every 15s)
        HT->>HT: Query Apple HealthKit (iOS) / Health Connect (Android)
    end

    %% Session Termination
    User->>SC: endSession() / WatchCmd (STOP_SESSION)
    activate SC
    SC->>SC: setSessionPhase('ENDING')
    SC->>WB: syncSessionState(SUMMARY, finalStats)
    SC->>SC: setSessionPhase('IDLE')
    SC->>NS: stopForegroundService() / cancelNotification()
    SC->>GT: sessionPhase IDLE trigger
    activate GT
    GT->>STS: saveSession(snapshot, userId)
    deactivate GT
    deactivate SC
    
    activate STS
    alt User Authenticated & Online
        STS->>DB: Insert 'skate_sessions' row
        STS->>HSS: saveWorkout(enrichedSnapshot)
        activate HSS
        HSS->>HSS: Save workout to native Health store (HealthKit/Health Connect)
        deactivate HSS
        STS->>DB: Update 'user_profiles' lifetime stats
    else User Offline or Unauthenticated
        STS->>STS: Buffer session to AsyncStorage (PENDING_SESSION_QUEUE_KEY)
    end
    deactivate STS
    
    SC->>SC: setTimeout(10 seconds)
    Note over SC: 10s delay matches watch card auto-dismiss
    SC->>WB: syncSessionState(STOPPED)
```

---

## 💾 Database Schema Reference (`skate_sessions`)

Data inserted into the Supabase remote schema:

```sql
CREATE TABLE skate_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id),
    session_date TIMESTAMPTZ DEFAULT now(),
    duration_sec INT NOT NULL,
    distance_miles FLOAT8 NOT NULL,
    avg_speed_mph FLOAT8 NOT NULL,
    peak_speed_mph FLOAT8 NOT NULL,
    peak_gforce FLOAT8,
    calories INT,
    avg_bpm INT,
    peak_bpm INT,
    location_label TEXT,
    location_coords JSONB, -- {lat: float, lng: float}
    start_coords JSONB,
    end_coords JSONB,
    path_coords JSONB,     -- Array of {lat: float, lng: float}
    crew_session_id UUID REFERENCES crew_sessions(id)
);
```

---

## 🚨 Architectural Impact Flags

- `[IMPACTS_USER_JOURNEY]` — Oversees the recording lifecycle, HUD display values, active watch interaction cards, and background synchronization mechanisms.
- `[IMPACTS_C4_CONTEXT]` — Interfaces directly with native device stores (Apple Health, Android Health Connect) and manages persistent network requests to the remote Supabase database and local storage.
- `[IMPACTS_STATE_CHART]` — Controls the main session state machine and transitions, syncing command logic across watches and mobile endpoints.

<!-- CARTOGRAPHER_END: SESSION_TRACKING -->

### Domain: PROTOCOL_CORE
<!-- CARTOGRAPHER_START: PROTOCOL_CORE -->

# PROTOCOL_CORE Domain Cartography

This document contains a comprehensive architectural audit and mapping of the `PROTOCOL_CORE` domain.

---

## 1. File Manifest

Every file within the `PROTOCOL_CORE` domain is catalogued below with its architectural purpose:

| File Path | Architectural Purpose |
|:---|:---|
| `src/protocols/IControllerProtocol.ts` | Defines the Hardware Abstraction Layer (HAL) interfaces, custom mode structs, firmware info, music configurations, and result schemas. |
| `src/protocols/ZenggeProtocol.ts` | Monolithic utility implementing the Zengge/MagicHome BLE protocol, packet wrapping, checksum calculation, and opcode builders (`0x59`, `0x51`, `0x73`, `0x74`, `0x62`, `0x63`, `0x71`). |
| `src/protocols/ZenggeAdapter.ts` | HAL adapter implementing `IControllerProtocol` for Zengge controllers, isolating sequence numbers and delegating to its own instance of `ZenggeProtocol`. |
| `src/protocols/BanlanxAdapter.ts` | HAL adapter implementing `IControllerProtocol` for the BanlanX SP621E controller, translating actions to the SP621E packet structure. |
| `src/protocols/ControllerRegistry.ts` | Runtime resolver mapping BLE advertised service UUIDs and manufacturer data to the appropriate `IControllerProtocol` adapter. |
| `src/hooks/useProtocolDispatch.ts` | React Hook providing a unified interface to dispatch commands (power, color, custom mode, music) across all connected devices, resolving individual HAL adapters dynamically. |
| `src/hooks/useProtocolBuilder.ts` | React Hook supporting the Diagnostic Lab by generating raw, wrapped, hex, and annotated outputs for `0x59`, `0x61`, `0x51`, `0x73`, and `0x62` opcodes. |
| `src/hooks/useProductCatalog.ts` | React Hook implementing the offline-safe, local-first merge pattern for fetching, caching, and reconciling hardware profiles from Supabase. |
| `src/hooks/useProductManager.ts` | React Hook offering administrative controls for editing, creating, and saving hardware product catalog profiles back to Supabase. |
| `src/constants/ProductCatalog.ts` | Configures the offline-safe local fallback product catalog (HALOZ™, SOULZ™, RAILZ™) with validated hardware segment counts and geometries. |

---

## 2. Blast Radius (Imports & Exports)

The SDE dependency scanner maps the imports and exports of `PROTOCOL_CORE` as follows:

```
[External Core Libraries / React / RN]
       │
       ▼
[src/protocols/IControllerProtocol.ts] ──┐
       │                                 │
       ├─► [src/protocols/ZenggeProtocol.ts]
       │         ▲
       │         │ (Isolated reference)
       ├─► [src/protocols/ZenggeAdapter.ts]
       │
       ├─► [src/protocols/BanlanxAdapter.ts]
       │
       ▼
[src/protocols/ControllerRegistry.ts]
       │
       ▼
[src/hooks/useProtocolDispatch.ts] <── Consumed by UI Components & Diagnostic Panels
[src/hooks/useProtocolBuilder.ts]  <── Consumed by Sk8LytzDiagnosticLab
```

### Module Level Imports
- **BLE State & Transport**: Consumes `BLEContext` from `src/context/BLEContext` to retrieve connected devices and write queues.
- **Async Storage**: Consumes `@react-native-async-storage/async-storage` for local caching of the product catalog.
- **Authentication**: Consumes `useAuth` from `src/context/AuthContext` to validate admin privileges during catalog upserts.
- **Database**: Consumes `supabase` client from `src/services/supabaseClient` to execute RPCs and upserts.

### Module Level Exports
- **UI & Diagnostics**: Exports `useProtocolDispatch` and `useProtocolBuilder` which are consumed by diagnostic screens, visualizers, and state synchronization loops.
- **Product Definition**: Exports `useProductCatalog` and constants from `ProductCatalog.ts` to coordinate UI layout rendering (e.g. Ring visualizer vs. Dual Strip visualizer).

---

## 3. Context Matrix

The React Context interactions of this domain are captured below:

| Context Name | Provider Location | Consumer Location | Purpose |
|:---|:---|:---|:---|
| `BLEContext` | `src/context/BLEContext.tsx` | `useProtocolDispatch.ts` | Resolves active connected device lists, writes command arrays, and triggers chunked frame sequences. |
| `AuthContext` | `src/context/AuthContext.tsx` | `useProductManager.ts` | Verifies active session token before initiating catalog profile changes on Supabase. |

---

## 4. Hook/Service I/O Registry

### `useProtocolDispatch()`
- **Inputs**: None.
- **Outputs**:
  - `setPower(isOn: boolean, targetDeviceId?: string)`: `Promise<boolean>`
  - `setSolidColor(r: number, g: number, b: number, targetDeviceId?: string)`: `Promise<boolean>`
  - `setMultiColor(colors: RGB[], ledPoints: number, speed: number, direction: number, transitionType?: number, targetDeviceId?: string)`: `Promise<boolean>`
  - `setEffect(effectId: number, speed: number, brightness: number, targetDeviceId?: string)`: `Promise<boolean>`
  - `setCustomMode(steps: CustomModeStep[], targetDeviceId?: string)`: `Promise<boolean>`
  - `setCustomModeExtended(steps: CustomModeStep[], direction?: number, targetDeviceId?: string)`: `Promise<boolean>`
  - `setMusicConfig(config: MusicConfig, targetDeviceId?: string)`: `Promise<boolean>`
  - `setMusicMagnitude(magnitude: number, targetDeviceId?: string)`: `Promise<boolean>`
  - `executeRawPayload(payload: number[], targetDeviceId?: string)`: `Promise<boolean>`
- **Side-Effects**: Writes binary buffers to the BLE connection queue. Over 200-byte `0x51` payloads are automatically redirected to the chunked `writeChunked` buffer pipeline.

### `useProtocolBuilder(hwPts: number)`
- **Inputs**: `hwPts` (defaults to 16).
- **Outputs**:
  - State controls: `bldProtocol`, `bldColors`, `bldTrans`, `bldSpeed`, `bldPoints`, `bldDir`, etc.
  - Formatted Result: `bldResult: BldResult | null` containing:
    - `raw: number[]` (inner packet)
    - `wrapped: number[]` (V2 framed packet)
    - `hex: string` (formatted hex string)
    - `annotations: string[]` (description array of decoded offsets)
- **Side-Effects**: None (pure rendering helper for Diagnostic Lab).

### `useProductCatalog()`
- **Inputs**: None.
- **Outputs**:
  - `allProfiles: ProductProfile[]` (merged local + database entries)
  - `getProfileById(id: string)`: `ProductProfile | undefined`
  - `getProfileByPoints(ledPoints: number)`: `ProductProfile`
  - `saveProfile(profile: ProductProfile)`: `Promise<boolean>`
  - `syncFromCloud()`: `Promise<void>`
- **Side-Effects**: Performs cache-first read on `AsyncStorage` (`@Sk8lytz_product_catalog`), checks legacy keys (`ng_product_catalog`), syncs from Supabase `product_catalog` table, and logs warning telemetry on fail.

### `useProductManager()`
- **Inputs**: None.
- **Outputs**:
  - State indicators: `editingProfile: ProductProfile | null`, `isSaving: boolean`
  - Lifecycle: `startEditing(profile)`, `createNew()`, `patchEdit(patch)`, `saveProduct()`, `cancelEdit()`, `syncFromCloud()`
- **Side-Effects**: Invokes `saveProfile` cloud updater, prompts Native Alerts on validation error, manages local editing draft state.

---

## 5. OS Variance Matrix

Code paths in the `PROTOCOL_CORE` domain undergo the following execution paths based on operating system runtime characteristics:

| Feature / System Area | iOS Behavior | Android Behavior | Architectural Safeguard |
|:---|:---|:---|:---|
| **BLE MTU Constraint** | iOS default CoreBluetooth MTU limits single writes to **185 bytes** (182 payload bytes). | Android negotiates MTU dynamically (up to **512 bytes**). Older chipsets fallback to **23 bytes** (20 payload bytes). | `prepareForTransmission()` uses the negotiated MTU (minus 3 ATT overhead bytes) to slice large packets (`0x51` or `0x59`) into dynamic `0x40` chunks. |
| **Advertisement Parsing** | Manufacturer Data buffer is read as Base64 encoded string from Native BLE PLX layers. | Manufacturer Data is parsed similarly, but OS permission levels dictate scan limits. | `parseFirmwareFromAdvertisement` parses the Base64 payload cleanly, avoiding platform-specific advertisement payload shifts. |
| **FFT Audio Processing** | Continuous `0x74` magnitude writes are scheduled on iOS background runner threads. | Background tasks on Android require explicit foreground services to keep `0x74` write timers active. | `requiresSoftwareFFT` flag maps whether the host device must run internal mic filters (Zengge: `true`, BanlanX: `false`). |

---

## 6. Sequence Diagram: Command Dispatch Pipeline

The following sequence diagram details the exact actor-to-actor flow when a user initiates a command from the UI (e.g. changing the LED color layout):

```mermaid
sequenceDiagram
    autonumber
    actor User as Physical User / UI
    participant Hook as useProtocolDispatch
    participant Reg as ControllerRegistry
    participant Adap as IControllerProtocol Adapter
    participant BLE as BLEContext / BLEProvider
    participant Plx as react-native-ble-plx
    actor HW as Hardware Controller

    User->>Hook: Solid Color Change (R, G, B)
    Note over Hook: Resolves active connected devices
    Hook->>Reg: resolveProtocolForDevice(deviceId, adapterMap)
    Reg-->>Hook: Return device-specific HAL Adapter (e.g. ZenggeAdapter)
    Hook->>Adap: buildSolidColor(R, G, B)
    Note over Adap: Builds opcode payload (e.g. 0x59 spatial array)
    Adap-->>Hook: Returns raw ProtocolResult { packets, delay, rateLimit }
    Hook->>BLE: executeProtocolResults(payloads, opts)
    Note over BLE: Initiates write queue mutex
    BLE->>Adap: prepareForTransmission(result, negotiatedMtu)
    Note over Adap: Slices packets into 0x40 chunk sequence if length > MTU-3
    Adap-->>BLE: Returns chunked / ready ProtocolResult
    loop For each packet in result
        BLE->>Plx: writeCharacteristicWithoutResponse(deviceId, serviceUUID, writeUUID, Base64EncodedBytes)
        Plx->>HW: Sends GATT Write command over BLE radio
        Note over BLE: Applies interPacketDelayMs (e.g. 20ms for BanlanX speed settles)
    end
    BLE-->>Hook: Resolves transaction Promise
    Hook-->>User: Visual Update Success
```

---

## 7. Protocol Byte Offset & Parsing Specifications

### 7.1. ZENGGE / MagicHome Protocol (`ZenggeProtocol.ts`)

#### 🧱 V2 Transmission Wrapper
Every raw payload sent to the Zengge controller must be wrapped inside a V2 transport frame:
- **Header Structure**: `[0x00, SeqNum, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]`
- **Checksum Calculation**: Modulo 256 sum over the inner payload bytes:
  $$\text{Checksum} = \left( \sum_{i=0}^{N-1} \text{byte}[i] \right) \pmod{256}$$
  The checksum byte is appended to the inner payload before V2 wrapper construction.

#### 🎛️ Opcode Specifications

##### `0x71` — Power State Toggle
- **Payload Format**: `[0x71, powerByte, 0x0F, checksum]`
  - `powerByte = 0x23`: Power ON
  - `powerByte = 0x24`: Power OFF

##### `0x42` — Function Mode (Built-In Patterns)
- **Payload Format**: `[0x42, effectId, speed, brightness, checksum]`
  - `effectId`: `1–100` (representing pre-coded RBM templates on the 0xA3 chip)
  - `speed`: `1–100` (hardware animation speed)
  - `brightness`: `1–100` (output power level)

##### `0x59` — Static Colorful Spatial Array
- **Payload Format**: `[0x59, totalLenHi, totalLenLo, R_0, G_0, B_0, ..., R_n, G_n, B_n, ledPointsHi, ledPointsLo, transitionType, speed, direction, checksum]`
  - `totalLenHi / totalLenLo`: Big-endian length of the following array bytes (i.e. $N_{\text{pixels}} \times 3 + 9$).
  - `ledPointsHi / ledPointsLo`: Big-endian length representing the physical strip bounds the transition should span across.
  - `transitionType`:
    - `0x01`: Static Freeze (Fully supported)
    - `0x02`: Running Water Hardware Scroll (Fully supported)
    - `0x03`, `0x04`, `0x05`, `0x06`: Blocked/Fails on 0xA3 hardware (forces static/running).
  - `speed`: `1–100` (controls scroll speed).
  - `direction`: `0x01` (forward), `0x00` (reverse).
  - **Surgical Buffer Overflow Defense**: Payloads with $N_{\text{pixels}} < 10$ cause physical EEPROM buffer lockout on the `0xA3` chipset. The SDK enforces a minimum safety pad size of **12** pixels.

##### `0x51` — Custom Scene Sequence
- **Short 9-Byte Compact Format (No Chunking)**:
  - `[0x51, slot[0]...slot[31], 0x0F, checksum]`
  - Slot payload (9 bytes): `[mode, speed, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, flags]`
- **Extended 10-Byte Format (Chunked)**:
  - `[0x51, slot[0]...slot[31], checksum]`
  - Slot payload (10 bytes): `[activeFlag, mode, speed, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, flags]`
    - `activeFlag`: `0xF0` = Slot Active, `0x0F` = Slot Empty.
    - `mode`: `1–44` (SymphonyEffects).
    - `flags`: `0x80` = forward + section-mirror toggle enabled, `0x00` = reverse / no section-mirroring.
- **Fragmented `0x40` Header**:
  - First Chunk (8 bytes): `[0x40, seqNum, 0x00, 0x00, totalLenHi, totalLenLo, dataLen+1, 0x0B]`
  - Subsequent Chunk (5 bytes): `[0x40, seqNum, indexWordHi, indexWordLo, dataLen]`
    - Last chunk signals EOF by setting the MSB: `indexWordHi = indexWordHi | 0x80`.

##### `0x73` — Symphony Music Configuration
- **Payload Format**: `[0x73, isOn, matrixStyle, patternId, dropR, dropG, dropB, colR, colG, colB, sensitivity, brightness, checksum]`
  - `isOn`: `0x01` (enable hardware microphone routing), `0x00` (disable hardware mic, parse app `0x74` stream).
  - `matrixStyle`: `0x26` (Light Bar - 16 modes), `0x27` (Light Screen - 30 modes).
  - `patternId`: `1–30` (music visualization pattern maps).
  - `dropR/G/B`: Drop visualizer color (Bytes 4-6).
  - `colR/G/B`: Sound Column visualizer color (Bytes 7-9).
  - `sensitivity` & `brightness`: `0–100` hardware range (scales sound response).

##### `0x74` — Software Volume Magnitude Stream
- **Payload Format**: `[0x74, magnitude, checksum]`
  - `magnitude`: Clamped strictly to `0–150` to prevent buffer saturations on `0xA3` hardware. Fired at 30-60Hz during App Mic mode.

##### `0x62` — EEPROM Settings Write
- **Payload Format**: `[0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, checksum]`
  - `ptsHi / ptsLo`: Big-endian points-per-segment count.
  - `segHi / segLo`: Big-endian physical duplicate segment counts.
  - `icType`: Hardware IC configuration index (WS2812B = `1`, SM16703 = `2`).
  - `sorting`: RGB ordering index (RGB = `0`, GRB = `2`).
  - `micPts / micSegs`: Segment counts dedicated specifically for microphone visualizers.

---

### 7.2. BANLANX / SP621E Protocol (`BanlanxAdapter.ts`)

#### 🧱 Packet Framing
- **Wrapper Structure**: `[0xA0, cmd, dataLen, ...payload]` (checksum is not used by SP621E hardware).

#### 🎛️ Opcode Specifications

##### `0x50` — Power Toggle
- **Payload Format**: `[0xA0, 0x50, 0x01, powerState]`
  - `powerState = 0x01`: Power ON
  - `powerState = 0x00`: Power OFF

##### `0x52` — Solid Color
- **Payload Format**: `[0xA0, 0x52, 0x03, R, G, B]` (sets the entire strip to a single static color)

##### `0x53` / `0x54` — Built-in Effects Selector
This operation requires two distinct sequential packets:
1. Select Effect: `[0xA0, 0x53, 0x01, effectId]`
   - `effectId`: `1–142` (built-in animation presets)
2. Set Speed: `[0xA0, 0x54, 0x01, speed]`
   - `speed`: `1–10` (mapped from user-facing `1–100` via `Math.round(speed / 10)`).
- **⚠️ Rate Limit Constraint**: Requires a minimum of **20ms inter-packet delay** between the two writes. If dispatched simultaneously, the hardware drops the speed packet.

##### `0x59` / `0x5A` — Hardware Music Visualizer
- **Payload Format**:
  - Mode Select: `[0xA0, 0x59, 0x01, 0x00]` (forces audio input to internal hardware microphone)
  - Sensitivity Set: `[0xA0, 0x5A, 0x01, gain]`
    - `gain`: Clamped `1–16` (mapped from `0–255` via `Math.round((sens / 255) * 16)`).
- **FFT Architecture**: Onboard DSP (libwled_lfx.so) processes audio signals directly. The app sets `requiresSoftwareFFT = false` and does not stream `0x74` volume data.

---

## 8. SDE Vulnerability & Bug Detections

The read-only audit revealed several critical defects in the current codebase implementations:

### 🔴 PROT-001: Checksum Overwrites Color2 Blue in `setCustomModeCompact`
In `ZenggeProtocol.ts` (Line 550), the builder allocates a 9-byte array for the compact slot format:
```typescript
const packet = [0x51, mode, speed, color1.r, color1.g, color1.b, color2.r, color2.g, color2.b];
const cs = ZenggeProtocol.calculateChecksum(packet.slice(0, 8));
packet[8] = cs;
```
- **Consequence**: `color2.b` is at index 8 of the array. Overwriting index 8 with `cs` corrupts the blue color component. 
- **Remediation**: Append the checksum as a 10th element: `packet.push(cs)` or size the array to 10 bytes: `const packet = [0x51, mode, speed, color1.r, color1.g, color1.b, color2.r, color2.g, color2.b, 0]` and assign `packet[9] = cs`.

### 🔴 PROT-002: Checksum Overwrites Direction in `setCustomModeExtendedCompact`
In `ZenggeProtocol.ts` (Line 570), the builder allocates a 10-byte array:
```typescript
const packet = [0x51, mode, speed, color1.r, color1.g, color1.b, color2.r, color2.g, color2.b, direction];
const cs = ZenggeProtocol.calculateChecksum(packet.slice(0, 9));
packet[9] = cs;
```
- **Consequence**: `direction` is at index 9. Overwriting index 9 with `cs` strips the direction settings byte before transmission.
- **Remediation**: Append `cs` to index 10: `packet.push(cs)` or declare an 11-byte array and assign `packet[10] = cs`.

### 🔴 PROT-003: Profile Saving Omits Essential Columns in `saveProfile`
In `useProductCatalog.ts` (Line 124), the administrative `saveProfile` function maps local state properties into `dbRow` for Supabase upserts but completely omits:
- `vizIsMirrored` (`viz_is_mirrored`)
- `batteryCapacityMilliAmpereHour` (`battery_capacity_milli_ampere_hour`)
- `vizThemeColor` (`viz_theme_color`)
- `brandIcon` (`brand_icon`)
- **Consequence**: Modifying any product catalog profile through the admin dashboard will erase or NULL out these columns in the Supabase database.
- **Remediation**: Add the missing database columns to the upsert object:
  ```typescript
  viz_is_mirrored:                   profile.vizIsMirrored ?? null,
  battery_capacity_milli_ampere_hour: profile.batteryCapacityMilliAmpereHour ?? 0,
  viz_theme_color:                    profile.vizThemeColor ?? null,
  brand_icon:                         profile.brandIcon ?? null,
  ```

### 🟡 PROT-004: Monolith Size Violation in `ZenggeProtocol.ts`
- **Consequence**: File size is **53.7KB**, violating S4 / R-23 guardrails limiting core source files to 30KB. Monolithic files increase risk of merge collisions and compilation delays.
- **Remediation**: Refactor the packet builders or constants into helper modules (e.g. `ZenggeCommandBuilder.ts`) to reduce the core protocol file.

### 🟡 PROT-005: Concurrent Async Re-Entrancy in `useProductCatalog` Effect hook
- **Consequence**: At mount, both `loadCachedCatalog()` and `syncFromCloud()` run async operations without a mutex locks or state checks. Under React StrictMode, this triggers race conditions, double reads, and double writes to AsyncStorage.
- **Remediation**: Add a boolean ref `isSyncingRef = useRef(false)` to lock concurrent requests.

### 🟢 PROT-008: Unused Supabase Import in `useProductManager.ts`
- **Consequence**: Imports `supabase` client on Line 4 but never references it.
- **Remediation**: Clean up the import statement to comply with the Boy Scout cleanup protocol.

---

## 9. Stale Documentation Manifest

The SDE audit scanned the Master Reference files for stale entries in this domain:

- **`tools/SK8Lytz_App_Master_Reference.md` §2 AsyncStorage Key Registry**
  - **Reason**: The table lacks documentation for the active key `@Sk8lytz_product_catalog` (defined as `STORAGE_PRODUCT_CATALOG`) and references outdated keys.
- **`tools/ZENGGE_PROTOCOL_BIBLE.md` §8 BUG-2 `0x73` micSource Wrong Values**
  - **Reason**: The micSource logic is no longer used directly as an state flag since the device microphone listens by default when music visualizers are configured.

---

## 10. Architectural Impact Flags

- `[IMPACTS_USER_JOURNEY]` — Administrative catalog modifications trigger layout structure updates in the product catalog screens.
- `[IMPACTS_STATE_CHART]` — BLE command dispatching and MTU fragmentation directly alter the packet flow sequence of BLE state machines.

<!-- CARTOGRAPHER_END: PROTOCOL_CORE -->

### Domain: PATTERN_ENGINE
<!-- CARTOGRAPHER_START: PATTERN_ENGINE -->

# PATTERN_ENGINE Domain Cartography

This document contains the read-only architectural audit of the **PATTERN_ENGINE** domain in the SK8Lytz codebase. 

---

## 1. File Manifest

Every file in the `PATTERN_ENGINE` domain is cataloged below with its respective architectural purpose:

| File Path | Architectural Purpose |
| :--- | :--- |
| `src/protocols/PatternEngine.ts` | The Single Source of Truth (SSOT) for all pattern metadata and templates (`SK8LYTZ_TEMPLATES`), responsible for constructing binary payloads (`0x59`, `0x51`) for BLE transmission and generating visualizer frame states. |
| `src/protocols/SpatialEngine.ts` | The core mathematical canvas compiler that maps raw coordinates and ticks to `RGB[]` pixel arrays for scrolling, chasing, meteor, waving, and generative patterns. |
| `src/protocols/SymphonyEngine.ts` | Generates real-time audio-reactive frame arrays representing visualizer patterns for the music mode UI. |
| `src/protocols/VisualizerEngine.ts` | Performs rotation shifts, segment reflections, and horseshoe-mirror transformations to map linear pixel arrays to circular visualizer preview shapes. |
| `src/protocols/PositionalMathBuffer.ts` | Manages linear percentage-based node interpolation to compile custom gradient arrays across dynamic LED strip lengths. |
| `src/hooks/useStreetMode.ts` | Implements a motion-reactive finite state machine (FSM) utilizing phone accelerometer data (12.5Hz) to dynamically trigger Stopped, Cruising, Accelerating, and Braking patterns. |
| `src/hooks/useMusicMode.ts` | Coordinates configuration and activation of built-in hardware microphone modes via the `0x73` protocol command when entering or adjusting Music Mode. |
| `src/hooks/useAppMicrophone.ts` | Manages mobile microphone recording, capturing volume amplitude at 20Hz (50ms interval) via Expo Audio, normalizing it, and streaming real-time `0x74` magnitude commands to the BLE controller. |

---

## 2. Blast Radius (Imports & Exports)

This mapping defines the inward dependencies (who imports this domain) and outward dependencies (what this domain imports).

```mermaid
graph TD
    %% Outward Imports
    Sensors[expo-sensors] --> useStreetMode[useStreetMode.ts]
    Audio[expo-audio] --> useAppMicrophone[useAppMicrophone.ts]
    Zengge[ZenggeProtocol.ts] --> useAppMicrophone
    useProtocolDispatch[useProtocolDispatch.ts] --> useMusicMode[useMusicMode.ts]
    MusicDict[MusicDictionary.ts] --> useMusicMode
    
    %% Internal Domain Flows
    SpatialEngine[SpatialEngine.ts] --> PatternEngine[PatternEngine.ts]
    SymphonyEngine[SymphonyEngine.ts] --> PatternEngine
    VisualizerEngine[VisualizerEngine.ts] --> PatternEngine
    PositionalMathBuffer[PositionalMathBuffer.ts] --> SpatialEngine
    PatternEngine --> useStreetMode
    
    %% Inward Imports (Consumers)
    PatternEngine --> DockedController[DockedController.tsx]
    PatternEngine --> useControllerDispatch[useControllerDispatch.ts]
    PatternEngine --> LEDStripPreview[LEDStripPreview.tsx]
    PatternEngine --> ProductVisualizer[ProductVisualizer.tsx]
    
    useStreetMode --> DockedController
    useAppMicrophone --> DockedController
    useMusicMode --> MusicPanel[MusicPanel.tsx]
```

### Outward Dependencies (Imports)
- **Sensor APIs**: `useStreetMode.ts` imports from `expo-sensors` (`Accelerometer`) to capture motion events.
- **Audio APIs**: `useAppMicrophone.ts` imports from `expo-audio` (`useAudioRecorder`, `RecordingPresets`) and `expo-file-system` to capture device microphone sound levels.
- **BLE Protocols**: `useAppMicrophone.ts` imports `ZenggeProtocol` to build `0x74` packets; `useMusicMode.ts` imports `useProtocolDispatch` to write `0x73` configurations.
- **Utilities**: All hooks import `AppLogger` for diagnostic telemetry and events reporting.

### Inward Dependencies (Exports)
- **UI Visualizers**: `PatternEngine` (`SK8LYTZ_TEMPLATES`, `getVisualizerFrame`) is heavily imported by `LEDStripPreview.tsx`, `ProductVisualizer.tsx`, and `VisualizerUnit.tsx` to render pixel-perfect previews on the UI canvas.
- **BLE Dispatch**: `useControllerDispatch.ts` imports `PatternEngine` to serialize the selected pattern template into a `0x59` or `0x51` payload before piping it into the BLE queue.
- **Orchestration**: `DockedController.tsx` serves as the central container, importing `useStreetMode` and `useAppMicrophone` to wire sensor/mic activities directly into the active device connection session.

---

## 3. Context Matrix

The React Context interactions of this domain are highly targeted to avoid re-render performance bottlenecks:

| Component / Hook | Context Consumed | Purpose | Context Provided |
| :--- | :--- | :--- | :--- |
| `useMusicMode` | `BLEContext` (via `useProtocolDispatch`) | Sends `0x73` configuration packets directly to the connected device write characteristic. | None |
| `useStreetMode` | None (Props Injected) | Accepts `writeToDevice` and `hwSettings` from its parent container (`DockedController`) to decouple physical writing from the hook's sensor state. | None |
| `useAppMicrophone` | None (Props Injected) | Accepts `writeToDevice` as a delegate callback to avoid spawning duplicate or orphan BLE write loops. | None |

---

## 4. Hook/Service I/O Registry

The mathematical functions and hooks export strict type contracts:

### A. Hooks Registry

#### 1. `useStreetMode`
- **Input Parameters**:
  - `activeMode`: `string` (Checks if `'STREET'`)
  - `writeToDevice`: `(payload: Uint8Array) => Promise<void>` (BLE injection helper)
  - `hwSettings`: `HardwareSettings | null`
  - `points`: `number` (LED count per segment)
  - `activeProduct`: `ProductDetails | null`
  - `brightness`: `number` (0–100)
  - `speed`: `number` (0–100)
  - `gpsSpeed`: `number` (fallback speed tracking)
- **Output Signatures**:
  - `streetSensitivity`: `number` (1–10)
  - `setStreetSensitivity`: `(val: number) => void`
  - `streetCruiseColor`: `string` (Hex)
  - `setStreetCruiseColor`: `(val: string) => void`
  - `streetBrakeColor`: `string` (Hex)
  - `setStreetBrakeColor`: `(val: string) => void`
  - `isStreetBraking`: `boolean`
  - `motionState`: `MotionState` (`'STOPPED' | 'ACCELERATING' | 'CRUISING' | 'SLOWING_DOWN' | 'HARD_BRAKING'`)
  - `applyStreetPattern`: `() => Promise<void>`
- **Side-Effects**: Registers an accelerometer update subscription at 12.5Hz (80ms sampling window) when `activeMode === 'STREET'`. Triggers rapid BLE `0x59` updates on FSM state transitions.

#### 2. `useAppMicrophone`
- **Input Parameters**:
  - `activeMode`: `string` (Checks if `'MUSIC'`)
  - `micSource`: `'DEVICE' | 'APP'` (Must be `'APP'`)
  - `isPoweredOn`: `boolean`
  - `writeToDevice`: `(payload: Uint8Array) => Promise<void>`
- **Output Signatures**:
  - `audioMagnitude`: `number` (Normalized 0.0 – 1.0)
  - `hasMicPermission`: `boolean | null`
  - `requestMicPermission`: `() => Promise<boolean>`
  - `recording`: `AudioRecorder | null` (Expo Audio object)
- **Side-Effects**: Requests system mic permissions. Spins up native recording session when app-mic is active. Sets a `50ms` (20Hz) timer that calculates volume magnitude using an exponential moving average (EMA) filter and dispatches `0x74` command packets to BLE.

#### 3. `useMusicMode`
- **Input Parameters**:
  - Config inputs: `musicPatternId` (number), `micSensitivity` (number), `brightness` (number), `micSource` ('DEVICE' | 'APP'), `musicPrimaryColor`, `musicSecondaryColor`, `musicMatrixStyle`.
- **Output Signatures**:
  - `handleMusicChange`: `(params: Partial<MusicParams>) => void`
- **Side-Effects**: Fires `0x73` config commands to set the onboard audio capture and pattern index. Sends an exit packet (`isOn: false`) when the mode is changed.

---

### B. Core Services Registry

#### 1. `PatternEngine.buildPatternPayload`
- **Inputs**: `patternId: number`, `fg: RGB`, `bg: RGB`, `speed: number`, `direction: boolean`, `brightness: number`, `options: PatternOptions`, `points: number`
- **Outputs**: `Uint8Array` (Wrapped BLE Command Packet)
- **Behavior**: Pure compiler. Automatically routes to `0x59` for spatial, `0x51` compact for test patterns 201-233, or `0x51` temporal schedulers for Jumps/Breaths.

#### 2. `SpatialEngine.generateArray`
- **Inputs**: `patternId: number`, `fgColor: RGB`, `bgColor: RGB`, `tick: number`, `speed: number`, `direction: boolean`, `options: PatternOptions`, `ledCount: number`
- **Outputs**: `RGB[]` (Linear Color Array)
- **Behavior**: Evaluates mathematical coordinate/wave functions (e.g., Meteor tail decay, Marquee stripes, Rainbow flow) over a dynamic spatial canvas.

---

## 5. OS Variance Matrix

The platform-specific branching matrix is mapped below:

| Feature / File | iOS Platform Behavior | Android Platform Behavior | Web Fallback |
| :--- | :--- | :--- | :--- |
| **Sensor Updates** (`useStreetMode.ts`) | Relies on CoreMotion sensors. Sampling is capped at `80ms` (12.5Hz). Requires user verification if prompt dialog appears. | Standard Android sensor library. Background operations require foreground service limits to prevent sensor freeze when screen locks. | Returns early. Accelerometer is disabled. |
| **Audio Recording** (`useAppMicrophone.ts`) | Requires `NSMicrophoneUsageDescription` in info.plist. Metering dBFS scales to positive float via `(db + 160) / 160`. | Requires `android.permission.RECORD_AUDIO` permission. Normalization behaves identically. | Bypasses native recording. Returns 0.0 magnitude. |
| **BLE Write Pacing** (`BleWriteDispatcher.ts`) | High-throughput support. Can write with shorter margins (~25ms). | Strict Android BLE stack limit (1 outstanding write). Enforces a 50ms gap between multi-device frames to avoid GATT `status 133` buffer saturations. | Mock BLE channel simulation. |

---

## 6. Dynamic Pipeline Sequence Diagrams

### A. App-Microphone Streaming (20Hz Pipeline)

```mermaid
sequenceDiagram
    autonumber
    actor User as User Activity
    participant DC as DockedController
    participant AM as useAppMicrophone
    participant EA as Expo Audio
    participant PE as PatternEngine
    participant WQ as BleWriteQueue
    participant BLE as Bluetooth GATT

    User->>DC: Tap "Music Mode" (Source: APP)
    DC->>AM: Mount with activeMode='MUSIC' & source='APP'
    AM->>EA: requestPermissionsAsync()
    EA-->>AM: Permission Granted
    AM->>EA: startRecording()
    AM->>AM: Set Interval (50ms / 20Hz)
    
    loop Every 50ms
        AM->>EA: getMetering()
        EA-->>AM: dBFS value (-160 to 0)
        AM->>AM: Normalize (0 to 150) & Apply EMA Filter
        AM->>PE: buildMusicStream(magnitude)
        PE-->>AM: Opcode 0x74 packet: [0x74, magnitude, check]
        AM->>WQ: enqueueWrite(0x74)
        WQ->>BLE: Write Characteristic (FF01)
    end

    User->>DC: Exit Music Mode / Power Off
    DC->>AM: Unmount / Mode changed
    AM->>EA: stopRecording()
    AM->>AM: clearInterval()
```

### B. Accelerometer Street Mode (Motion FSM & Brake Light Override)

```mermaid
sequenceDiagram
    autonumber
    participant Sens as Expo Sensors (Accelerometer)
    participant SM as useStreetMode FSM
    participant PE as PatternEngine
    participant DC as DockedController
    participant WQ as BleWriteQueue
    participant BLE as Bluetooth GATT

    DC->>SM: Enter Mode 'STREET'
    SM->>Sens: setUpdateInterval(80ms)
    SM->>Sens: addListener()

    loop Every 80ms
        Sens->>SM: Accelerometer Event {x, y, z}
        SM->>SM: Filter Gravity G-Force Vector Delta
        alt Vector Delta < Threshold (Still)
            SM->>SM: Transition State -> STOPPED
            SM->>PE: buildPatternPayload(ID: 101 - Red Accent Solid)
        else Vector Delta > Cruiser (Skating)
            SM->>SM: Transition State -> CRUISING
            SM->>PE: buildPatternPayload(ID: 102 - Cruise Blue Chase)
        else Sudden Negative Delta (Braking Event)
            SM->>SM: Transition State -> HARD_BRAKING (Brake Override Active)
            SM->>PE: buildPatternPayload(ID: 103 - Solid Bright Red 0x59)
        end
        PE-->>SM: Wrapped [0x59] payload array
        SM->>WQ: enqueueWrite(0x59 payload)
        WQ->>BLE: Dispatch to physical Skates (FF01)
    end
```

---

## 7. Pattern Template Catalog (`SK8LYTZ_TEMPLATES`)

The SK8Lytz registry is partitioned into mathematical, street-specific, and legacy categories:

### A. Color Customization Categories
- **`FG_BG`**: Full two-color picker layout showing primary and secondary selectors (e.g., *Split Colors, Comet Chase, Bold Stripes, Cyberpunk Shift*).
- **`FG_ONLY`**: One-color layout displaying foreground selector over a black canvas (e.g., *Solid Color, Center-Out Marquee, Strobe Flash*).
- **`GENERATIVE`**: Algorithmic rainbow hues that ignore static color pickers (e.g., *True Rainbow Flow, Rainbow Comet, Aurora Borealis*).

### B. Animation Tiers
1. **Tier 1 (Legacy Intercepts)**: Mirroring the original Zengge presets (`ge.*` effects) but compiled client-side as `0x59` cascade arrays to support custom speeds and colors.
2. **Tier 2 (Programs Reversals)**: Replicated patterns from older firmware (e.g., *Dot Chase, Meteor Shower, Theater Chase*).
3. **Tier 3 (SK8Lytz Originals)**: Tailored spatial effects optimized for skates (e.g., *Sine Pulse Wave, Cyberpunk Shift, Lightning Strike, Street Mode FSM Overrides*).

---

## 8. Stale Documentation Audit

Stale sections identified in `tools/SK8Lytz_App_Master_Reference.md` are tagged below:

1. **`0x41` Settled Mode Table Row**:
   * *Stale Section*: Master Reference "Opcode Map" section referencing `0x41` Settled Mode for custom dual colors.
   * *Rationale*: `PatternEngine.ts` explicitly intercepts test pattern IDs 201-233 and dispatches them via `0x51` compact (ZenggeProtocol.setCustomModeExtendedCompact) to bypass physical hardware state lockups caused by `0x41` on the `0xA3` chipset.

2. **`0x43` Multi-Effect Sequence Row**:
   * *Stale Section*: Master Reference description of `0x43` Multi-Effect Sequence supporting up to 50 effects.
   * *Rationale*: Physical testing confirms that sending `0x43` crashes the `0xA3` controller's state machine. The official ZENGGE app uses `0x51` multi-step slots instead.

---

## 9. Architectural Impact Flags

- `[IMPACTS_USER_JOURNEY]` — The 20Hz App-Microphone streaming pipeline relies on real-time magnitude writes. Hardware saturation is strictly defended by clamping values to 150 in `useAppMicrophone.ts` to avoid physical EEPROM/GATT lockout.
- `[IMPACTS_STATE_CHART]` — The Street Mode FSM inside `useStreetMode.ts` maps accelerometer vector transitions. Future modifications to threshold sensitivities or brake overrides directly impact this state graph.

---
*Document Compiled by Scout — Reyes (2026-06-11)*

<!-- CARTOGRAPHER_END: PATTERN_ENGINE -->

### Domain: CLOUD_FUNCTIONS
<!-- CARTOGRAPHER_START: CLOUD_FUNCTIONS -->

# CLOUD_FUNCTIONS Domain Cartography Report

This cartography report details the file manifest, imports/exports (Blast Radius), React Context dependencies, API parameters, OS-specific differences, runtime synchronization pipeline sequence, security hardening audits, and stale documentation targets for the `CLOUD_FUNCTIONS` domain in SK8Lytz.

---

## 1. File Manifest

The `CLOUD_FUNCTIONS` domain consists of PostgreSQL migrations, Deno Edge Functions, and client-side integration services that bridge offline storage buffers to the Supabase backend.

### Server-Side Functions & Database Migrations

| File / Migration Name | Location | Architectural Purpose |
| :--- | :--- | :--- |
| `notify-crew-session/index.ts` | `supabase/functions/notify-crew-session/index.ts` | Deno-based Edge Function that verifies caller identity via GoTrue JWT validation, checks crew membership, and dispatches batch push notifications (up to 100 per chunk) to crew members via the Expo Push API. |
| `20260413_hardening_sweep.sql` | `supabase/migrations/20260413_hardening_sweep.sql` | Executes the initial database hardening sweep, establishing base Row-Level Security (RLS) policies across core user and session tables. |
| `20260414_account_deletion_rpc.sql` | `supabase/migrations/20260414_account_deletion_rpc.sql` | Declares the `delete_account()` RPC as a `SECURITY DEFINER` function, allowing authenticated users to delete their own record from `auth.users`, cascading deletions to clear all profile, session, and telemetry records. |
| `20260418061000_admin_user_management.sql` | `supabase/migrations/20260418061000_admin_user_management.sql` | Defines the `admin_audit_logs` schema and administrative functions to promote, demote, or ban user accounts with cryptographic log validation. |
| `20260506000001_god_tier_telemetry.sql` | `supabase/migrations/20260506000001_god_tier_telemetry.sql` | Introduces the consolidated telemetry aggregation system and the high-volume `flush_telemetry` RPC to process batch uploads of diagnostics, preventing connection pooling overhead. |
| `20260512180000_fix_admin_revoke...sql` | `supabase/migrations/20260512180000_..._security.sql` | Closes privilege escalation vectors in the admin promotion trigger flow by validating the role of the promoting user. |
| `20260526190000_supabase_security_hardening.sql` | `supabase/migrations/20260526190000_..._hardening.sql` | Hardens PL/pgSQL function execution by setting `search_path = public` to prevent search path injection, drops public write permissions on `skate_spots`, and tightens telemetry access. |
| `20260608000000_sk8lytz_security_hardening.sql` | `supabase/migrations/20260608000000_..._hardening.sql` | Enforces Row-Level Security defensively on all active application tables (22 tables enabled) and updates the update policies on `parsed_session_stats`. |
| `20260609050000_get_all_devices_rpc.sql` | `supabase/migrations/20260609050000_..._rpc.sql` | Implements `get_all_registered_devices()` as a `SECURITY DEFINER` function to let command center panels bypass RLS to list active hardware. |
| `20260609175500_restore_domain_admin_promotion.sql` | `supabase/migrations/20260609175500_..._promotion.sql` | Adjusts auto-promotion logic during signups, mapping users with corporate domains to administrative tiers. |

### Client-Side Integration & Orchestration Layer

| File Name | Location | Architectural Purpose |
| :--- | :--- | :--- |
| `supabaseClient.ts` | `src/services/supabaseClient.ts` | Configures and instantiates the `@supabase/supabase-js` client. Integrates the custom `SecureStoreAdapter` for auth token storage and implements a complete offline fallback stub to prevent crashes when network layers are offline. |
| `AuthContext.tsx` | `src/context/AuthContext.tsx` | Manages global authentication states (`session`, `user`, `isAuthenticated`, `isOfflineMode`) and coordinates auto-refresh tokens. |
| `useOfflineSyncWorker.ts` | `src/hooks/cloud/useOfflineSyncWorker.ts` | The offline-first background synchronizer. Listens to network connectivity and AppState transitions to coordinate serial uploads of logs, local presets, and cached skate sessions. |
| `useTelemetryLedger.ts` | `src/hooks/useTelemetryLedger.ts` | Collects UI/BLE event heartbeats into in-memory buffers and dispatches batch updates via the database `flush_telemetry` RPC. |
| `AppLogger.ts` | `src/services/AppLogger.ts` | Coordinates system logs and crash events, storing offline log buffers in `AsyncStorage` and flushing batches to `telemetry_snapshots`. Bypasses buffers to fast-lane critical exceptions directly to `telemetry_errors`. |
| `ScenesService.ts` | `src/services/ScenesService.ts` | Synchronizes user-created patterns to `user_saved_presets` and community scenes to `shared_scenes` using local cache queues. |
| `SpeedTrackingService.ts` | `src/services/SpeedTrackingService.ts` | Computes live speed metrics during skate sessions, caches them locally, and pushes completed session snapshots to the cloud `skate_sessions` table. |

---

## 2. Blast Radius

The CLOUD_FUNCTIONS domain sits at the boundary between React Native execution and cloud persistence. Modifications to schema structures, database permissions, or client configuration parameters have broad ramifications:

```
                          ┌────────────────────────┐
                          │   Supabase Client /    │
                          │   Auth & RLS Schemas   │
                          └───────────┬────────────┘
                                      │
           ┌──────────────────────────┼──────────────────────────┐
           ▼                          ▼                          ▼
 ┌───────────────────┐      ┌───────────────────┐      ┌───────────────────┐
 │   AppLogger.ts    │      │  ScenesService    │      │  SpeedTracking    │
 │ (System telemetry │      │  (Custom patterns │      │  (Skate sessions  │
 │  & error dumps)   │      │   & scene sharing)│      │   & stats sync)   │
 └─────────┬─────────┘      └─────────┬─────────┘      └─────────┬─────────┘
           │                          │                          │
           └──────────────────────────┼──────────────────────────┘
                                      ▼
                        ┌──────────────────────────┐
                        │ useOfflineSyncWorker.ts  │
                        │ (Queue processing loop)  │
                        └─────────────┬────────────┘
                                      │
                                      ▼
                        ┌──────────────────────────┐
                        │   AsyncStorage Buffers   │
                        │  (Local Offline State)   │
                        └──────────────────────────┘
```

### Imports (Incoming Dependencies)
* **Authentication Storage**: `expo-secure-store` (`SecureStore` used by `supabaseClient.ts` to encrypt and store auth JWTs).
* **Native Polyfills**: `react-native-url-polyfill/auto` (injected to support standard URL parsing in React Native).
* **Network & AppState Lifecycle**: `react-native` (`AppState`, `Platform`, `PermissionsAndroid`).
* **Offline Buffering**: `@react-native-async-storage/async-storage` (`AsyncStorage` used to queue scenes, sessions, and log payloads).

### Exports (Outgoing Consumers)
* **UI Views**: `DashboardScreen.tsx`, `AccountModal.tsx`, `CrewModal.tsx` (all read profile metrics, group hierarchies, and saved presets synced by the background worker).
* **BLE Core Controllers**: `DockedController.tsx` (consumes `useControllerAnalytics` to log user state transitions, speeds, and presets to telemetry).
* **Location Systems**: `LocationService.ts` (pulls surrounding public skate sessions within coordinates synced to `public_sessions`).
* **Health Framework**: `HealthSyncService.ts` (consumes session results to write workouts back to Apple HealthKit or Google Health Connect).

---

## 3. Context Matrix

The integration layer exports and consumes React contexts to drive UI views based on active credentials and connection availability:

| React Context | Consumed / Provided | Purpose |
| :--- | :--- | :--- |
| `AuthContext` (`useAuth`) | **Provided** by `AuthProvider` | Exposes the current Supabase `session`, `user` object, and indicators like `isAuthenticated` and `isOfflineMode`. This eliminates redundant calls to `supabase.auth.getUser()`. |
| `SessionContext` (`useSession`) | **Consumed** by Speed Tracking | Integrates live location coordinates and health metrics with the `SpeedTrackingService` before final session packaging and DB upload. |

---

## 4. Hook/Service I/O Registry

The interfaces governing client-side transactions with Supabase tables and Edge endpoints operate under strict input/output bounds:

### `AuthContext.tsx`
#### `useAuth()`
* **Inputs**: None.
* **Outputs**: `{ session: Session | null, user: User | null, isOfflineMode: boolean, isAuthenticated: boolean }`
* **Side-Effects**: Subscribes to `supabase.auth.onAuthStateChange` to update context values. Falls back to offline-mode states if initialized during a network timeout or local-only session.

---

### `useTelemetryLedger.ts`
#### `useTelemetryLedger(deviceId: string | null)`
* **Inputs**: `deviceId: string | null`
* **Outputs**: None.
* **Side-Effects**: Registers a 15-minute background timer and listens to `AppState` changes. When triggered, it flushes the in-memory array of BLE commands and UI interactions to the `flush_telemetry` RPC.

---

### `useOfflineSyncWorker.ts`
#### `useOfflineSyncWorker()`
* **Inputs**: None.
* **Outputs**: None.
* **Side-Effects**: Hooks into NetInfo state updates and `AppState` transitions. Launches serial queue execution blocks:
  1. Spools cached logs from `AsyncStorage` to the telemetry API.
  2. Syncs pending patterns from `@Sk8lytz_Scene_Sync_Queue` to `user_saved_presets`.
  3. Flushes recorded sessions from `@Sk8lytz_Pending_Sessions` to `skate_sessions`.

---

### `AppLogger.ts`
#### `AppLogger.log(eventType: string, metadata: object)`
* **Inputs**: `eventType: string`, `metadata: object`
* **Outputs**: `Promise<void>`
* **Side-Effects**: Appends a timestamped payload into `@sk8lytz_log_buffer` in `AsyncStorage`. If the buffer exceeds 50 entries, it spawns an asynchronous flush to `telemetry_snapshots`.

#### `AppLogger.error(tag: string, message: string, context?: object)`
* **Inputs**: `tag: string`, `message: string`, `context?: object`
* **Outputs**: `Promise<void>`
* **Side-Effects**: Evaluates exception severity. Critical errors (`PROTOCOL_ERROR`, `CRITICAL_EXCEPTION`) bypass the offline buffer entirely and are posted immediately to `telemetry_errors` via an isolated `try/catch` block.

---

### `ScenesService.ts`
#### `ScenesService.syncPreset(preset: IPresetPayload)`
* **Inputs**: `preset: IPresetPayload`
* **Outputs**: `Promise<{ success: boolean }>`
* **Side-Effects**: Writes the pattern data to local storage. If online, upserts the pattern into `user_saved_presets`. If offline, queues the operation in `@Sk8lytz_Scene_Sync_Queue` for the background worker.

---

### `SpeedTrackingService.ts`
#### `SpeedTrackingService.saveCompletedSession(snapshot: ISessionSnapshot)`
* **Inputs**: `snapshot: ISessionSnapshot`
* **Outputs**: `Promise<void>`
* **Side-Effects**: Attempts to write the skate session record directly to `skate_sessions`. On failure or if the app is offline, stores the payload in `@Sk8lytz_Pending_Sessions` and triggers a background sync check.

---

## 5. OS Variance Matrix

Although PostgreSQL migrations and Deno Edge functions execute entirely in server-side environments, the client-side integration layer relies on platform-specific APIs to cache credentials and synchronize device settings:

```
                            ┌──────────────────────────┐
                            │  Supabase Client / Auth  │
                            └─────────────┬────────────┘
                                          │
                        ┌─────────────────┴─────────────────┐
                        ▼                                   ▼
             [ Platform.OS === 'ios' ]          [ Platform.OS === 'android' ]
          ┌─────────────────────────────┐    ┌─────────────────────────────┐
          │ - SecureStore: Encrypts JWT │    │ - SecureStore: Encrypts JWT │
          │ - Push: Native Expo token   │    │ - Push: Requires explicit   │
          │ - Health: Apple HealthKit   │    │   channelId declarations    │
          │   (Gated read verification  │    │ - Health: Health Connect    │
          │    due to type privacy)     │    │   (Requires activity rec    │
          └─────────────────────────────┘    │    and coroutine init)      │
                                             └─────────────────────────────┘
```

### 1. Authentication Secure Storage
* **Native iOS / Android**: The `SecureStoreAdapter` (defined in `supabaseClient.ts`) wraps `expo-secure-store` (`SecureStore.setItemAsync`, `SecureStore.getItemAsync`, `SecureStore.deleteItemAsync`) to encrypt and isolate the session JWT from other apps.
* **Web (Fallbacks)**: Reverts storage calls directly to `localStorage` (`localStorage.setItem`, `localStorage.getItem`, `localStorage.removeItem`).

### 2. Push Notification Channels (`notify-crew-session`)
* **Android**: The edge function payload forces the injection of the `channelId: 'crew-alerts'` parameter. In Android 8.0+ (Oreo) and above, push notifications fail to render unless a matching channel ID is declared on the client.
* **iOS**: Ignores `channelId` arrays; push alerts are routed natively through the Apple Push Notification service (APNs) badge and sound engine.

### 3. Workout Synchronization
* **iOS**: Invokes Apple HealthKit (`react-native-health`) to log a workout with the type `SkatingSports`.
* **Android**: Invokes Google Health Connect (`react-native-health-connect`) to write records of `recordType: 'ExerciseSession'` with `exerciseType: 60` (representing Skating).

---

## 6. Offline Sync Pipeline Sequence Diagram

The following sequence diagram illustrates how local edits, logs, and sessions are buffered inside `AsyncStorage` when offline, and subsequently synchronized to Supabase tables and RPCs by the `useOfflineSyncWorker` upon reconnection:

```mermaid
sequenceDiagram
    autonumber
    actor U as User
    participant C as UI Dashboard / Controller
    participant S as Feature Service (Scenes/Speed/Logger)
    participant AS as AsyncStorage (Buffer Queues)
    participant OSW as useOfflineSyncWorker
    participant N as NetInfo / AppState
    participant DB as Supabase DB & RPCs

    %% Local Cache Sequence (Offline Mode)
    Note over U, AS: Offline Data Caching Phase
    U->>C: Interacts (Saves Custom Scene / Ends Skate Session)
    C->>S: saveSession(payload) / savePreset(preset)
    S->>DB: Check connection / Attempt write
    DB-->>S: Return network error / Timeout
    S->>C: Display optimistic success ("Saved locally")
    S->>AS: Queue item in [@Sk8lytz_Scene_Sync_Queue / @Sk8lytz_Pending_Sessions]
    
    %% Synchronization Sequence (Network Restored)
    Note over N, DB: Synchronization Phase (Connection Restored)
    N->>OSW: Emit Connection State Change (isConnected = true)
    OSW->>AS: Read pending queues (Logs, Scenes, Sessions)
    AS-->>OSW: Return cached array payloads
    
    alt Scene Queue contains pending entries
        loop For each queued scene
            OSW->>DB: Upsert into "user_saved_presets"
            DB-->>OSW: Confirm write success (201 Created)
            OSW->>AS: Remove scene from "@Sk8lytz_Scene_Sync_Queue"
        end
    end

    alt Session Queue contains pending entries
        loop For each queued session
            OSW->>DB: Insert into "skate_sessions"
            DB-->>OSW: Confirm write success (201 Created)
            OSW->>AS: Remove session from "@Sk8lytz_Pending_Sessions"
        end
    end

    alt Logger Buffer contains pending entries
        OSW->>DB: Call flush_telemetry(batch_array) RPC
        DB-->>OSW: Confirm batch process execution
        OSW->>AS: Clear "@sk8lytz_log_buffer"
    end
```

---

## 7. Security / Vulnerability Risk Analysis

An audit of the Supabase SQL migrations, RLS policies, and RPC definitions highlights the following security designs and remaining architectural exposures:

### 1. PL/pgSQL Search Path Hijacking Safeguard
In PostgreSQL, functions executing as `SECURITY DEFINER` run with the privileges of the owner (typically the database administrator). If the function does not declare a target schema search path, a malicious user could write a custom operator or function in a user-accessible schema (like `public`) and trigger it via the admin function, escalating privileges.
* **Hardening Measure**: Migration `20260526190000_supabase_security_hardening.sql` enforces:
  ```sql
  ALTER FUNCTION public.admin_revoke_sessions(p_target_user_id uuid) SET search_path = public, auth;
  ALTER FUNCTION public.delete_account() SET search_path = public;
  ```
  This guarantees that all tables and helper functions are evaluated strictly within the public/auth namespaces, eliminating the Search Path Hijacking vector.

### 2. Cascading Account Deletion
Apple App Store and Google Play guidelines mandate that applications that allow users to create accounts must also allow users to request account deletion.
* **Mechanism**: The `delete_account()` RPC (defined in `20260414_account_deletion_rpc.sql`) uses:
  ```sql
  DELETE FROM auth.users WHERE id = auth.uid();
  ```
  Because the `auth.users` table holds foreign keys to application tables configured with `ON DELETE CASCADE` constraints (e.g., `user_profiles.user_id`, `skate_sessions.user_id`), this single statement completely and securely erases the user's data across the entire schema.

### 3. Crew Notifications Authenticated Verification
The Deno Edge function `notify-crew-session` runs server-side and does not share React Native's client auth context.
* **Hardening Measure**: It parses the incoming authorization header (`Bearer <JWT>`) and calls `supabase.auth.getUser(JWT)` using the service role key. This performs an out-of-band JWT signature verification. The function then checks the `crew_memberships` table using the user's ID to ensure they are a member of the crew before dispatching messages, preventing unauthorized push notification spam.

### 4. Admin Role Escalation Guard
In migration `20260512180000_fix_admin_revoke_and_promotion_security.sql`, functions managing user promotion (`admin_promote_user`) are locked down to prevent users from promoting themselves. The database validates the role of the caller in `user_profiles` before applying the update.

---

## 8. Archival Target Registry

During the codebase audit, the following stale documentation entries were identified:

* **File**: `tools/SK8Lytz_App_Master_Reference.md`
* **Line Range**: 946
* **Reason**: This hook has been fully deprecated and removed. Session tracking is now managed dynamically through the `SessionContext` and `SpeedTrackingService`.

* **File**: `tools/SK8Lytz_App_Master_Reference.md`
* **Line Range**: 958
* **Reason**: The `useDeviceFleet` hook was replaced by query logic utilizing junction tables (`device_group_members`) to support many-to-many device grouping.

* **File**: `tools/SK8Lytz_App_Master_Reference.md`
* **Line Range**: 965
* **Reason**: Replaced by direct modal state calls inside `ProgrammerModal` to prevent state syncing lag.

<!-- CARTOGRAPHER_END: CLOUD_FUNCTIONS -->

### Domain: THEME_&_ASSETS
<!-- CARTOGRAPHER_START: THEME_&_ASSETS -->

# Architectural Cartography — THEME_&_ASSETS Domain

This document provides a comprehensive read-only architectural audit of the `THEME_&_ASSETS` domain within the SK8Lytz codebase, covering static theme tokens, component style adaptors, utility registries, storage constants, asset folders, runtime theme contexts, and platform variance.

---

## 1. File Manifest

Every file within the `THEME_&_ASSETS` scope (`src/theme/*`, `src/styles/*`, `src/constants/*`, `src/assets/*`) is categorized below with its architectural purpose:

| File / Folder Path | Type | Architectural Purpose |
| :--- | :--- | :--- |
| `src/theme/theme.ts` | Code (`.ts`) | Centralizes static design tokens (colors, spacing, typography, layout) and platform-specific shadow/glow generators for light and dark palettes. |
| `src/styles/DashboardStyles.ts` | Code (`.ts`) | Exports responsive styling configurations for the primary `DashboardScreen` and its nested slabs, along with a helper that maps pattern names to specific gradient colors. |
| `src/constants/AppConstants.ts` | Code (`.ts`) | Defines top-level application constants, including the local AsyncStorage key prefix (`@Sk8lytz_`) and the hardware speed limit ceiling (`HW_SPEED_MAX`). |
| `src/constants/ControlsRegistry.ts` | Code (`.ts`) | Specifies configuration objects, risk thresholds, and confirmation alert metadata for features within the Administrator Command Center. |
| `src/constants/bleTimingConstants.ts` | Code (`.ts`) | Centralizes empirical connection handshakes, settle timers, retry backoffs, and inter-write gaps derived for the ZENGGE `0xA3` hardware adapter chipsets. |
| `src/constants/storageKeys.ts` | Code (`.ts`) | Houses the global index of AsyncStorage keys to eliminate string literal duplication and prevent namespace key collisions. |
| `src/constants/ProductCatalog.ts` | Code (`.ts`) | *[EXCEPTED FROM SCOPE]* (Houses metadata and specifications for the SOULZ, HALOZ, and RAILZ physical adapters). |
| `src/assets/images/music_modes/` | Assets | Stores standard PNG banners representing native audio frequency mapping visuals. |
| `src/assets/images/zengge_patterns/` | Assets | Holds reference JPEG and PNG images for scene visualizers, custom gradients, and preset backdrops. |

---

## 2. Blast Radius (Dependency Graph)

The ingress (imports) and egress (exports) mapping of this domain reveals its dependency footprints:

### Inward Imports (Dependencies)
The `THEME_&_ASSETS` domain is designed to be highly decoupled, importing only fundamental framework types:
* **`react-native`**: Imports `Platform`, `StyleSheet`, `ViewStyle`, and `TextStyle` to resolve native platform selects and compile-time styling rules.
* **Local Imports**: `DashboardStyles.ts` references the base `ThemePalette`, `Layout`, and `Spacing` tokens from `../theme/theme.ts`.

### Outward Consumers (Exports & Usage)
* **`src/theme/theme.ts`**: Consumed by over 70 visual modules, screens, and modals (e.g. `DockedController.tsx`, `AccountModal.tsx`, `TacticalSlider.tsx`, `VisualizerUnit.tsx`) to pull static styling attributes (`Spacing`, `Colors`, `Typography`, `Shadows`).
* **`src/styles/DashboardStyles.ts`**: Imported by the parent `DashboardScreen.tsx` to instantiate responsive, screen-size-dependent styles dynamically.
* **`src/constants/storageKeys.ts`**: Widely referenced across all core application layers:
  * **Auth Context & Profile Hooks**: Pulls credentials flags and email cache markers.
  * **BLE Services**: Resolves device cache ledgers and background telemetry queues.
  * **Sync Services**: Accesses scenes, spots, and pending session queues.
* **`src/constants/bleTimingConstants.ts`**: Imported by `ConnectService.ts`, `RecoveryService.ts`, `BleWriteDispatcher.ts`, and `Sk8LytzProgrammer.tsx` to align write throttles and retry backoffs.
* **`src/constants/ControlsRegistry.ts`**: Consumed by the admin `AppManager.tsx` to compile the interactive diagnostics settings tree.
* **`src/constants/AppConstants.ts`**: Imported in `NormalizationUtils.ts` to bound user speed inputs.

---

## 3. Context Matrix

While the files inside this domain export static data, they directly feed the runtime state management governed by `ThemeContext.tsx` (`src/context/ThemeContext.tsx`):

* **Provided Contexts**:
  * `ThemeContext`: Provides dynamic theme values and states (`Colors`, `isDark` boolean, `toggleTheme()`, `controlUITheme` select, and `toggleControlUITheme()`).
* **Consumed Contexts**:
  * None.
* **Storage Interlock**:
  * The context reads and writes the following keys:
    * `@Sk8lytz_ThemeMode` (persisted theme state: `'dark' | 'light'`)
    * `@Sk8lytz_ControlUITheme` (persisted layout variant: `'CLASSIC' | 'MODERN' | 'DOCKED'`)

---

## 4. Hook/Service I/O Registry

Although this domain lacks stateful React Hooks or Class-based Services, it exposes critical dynamic utility methods that compute design values:

### `getPatternColors` (defined in `DashboardStyles.ts`)
* **Input**:
  * `patternName` (`string?`): The name of the active pattern to match.
  * `Colors` (`ThemePalette?`): The active color theme mapping.
* **Output**:
  * `string[]` (A size-2 array containing CSS/React Native hex string colors for gradients).
* **Behavior/Side Effects**:
  * Scans the normalized lowercase pattern string for keyword matches (e.g., `'fire'` $\rightarrow$ Orange/Gold, `'water'` $\rightarrow$ Blue/Cyan, `'matrix'` $\rightarrow$ Lime/Green). Defaults to `[Colors.primary, Colors.secondary]` if no keywords match.

### `createDashboardStyles` (defined in `DashboardStyles.ts`)
* **Input**:
  * `Colors` (`ThemePalette`): Active colors injected from context.
  * `windowHeight` (`number`, default: 800)
  * `windowWidth` (`number`, default: 400)
* **Output**:
  * `StyleSheet` object containing structural layout keys (`safeArea`, `card`, `skateCardInner`, etc.) optimized for the target viewport.
* **Behavior/Side Effects**:
  * Dynamically modifies margins, padding, and spacers based on height criteria (`windowHeight < 720` for short viewports, `windowHeight < 640` for very short viewports).

### `Shadows.glow` (defined in `theme.ts`)
* **Input**:
  * `color` (`string`): Target neon glow color.
* **Output**:
  * `ViewStyle` shadow layout properties.

### `TextShadows.glow` (defined in `theme.ts`)
* **Input**:
  * `color` (`string`): Target neon text color.
  * `radius` (`number`, default: 10): Blur spread radius.
* **Output**:
  * `TextStyle` text shadow properties.

---

## 5. OS Variance Matrix

The domain implements structural adjustments to accommodate native platform rendering quirks and API characteristics:

| Code Constant | iOS Path | Android Path | Web Fallback / Default |
| :--- | :--- | :--- | :--- |
| `Shadows.soft` / `Shadows.medium` | Resolves to shadow object: `shadowColor`, `shadowOffset`, `shadowOpacity`, `shadowRadius`. | Maps directly to native elevation integers: `elevation: 3` / `elevation: 5`. | Returns `{}` (empty object) to prevent unsupported platform crashes. |
| `Shadows.glow(color)` | Standard shadow object: `shadowColor: color`, `shadowOffset: { width: 0, height: 0 }`, `shadowOpacity: 0.8`, `shadowRadius: 8`. | Maps to: `shadowColor: color`, `elevation: 8`. | Returns `{}`. |
| `TextShadows.glow(color, radius)` | Defaults to mobile shadow: `textShadowColor: color`, `textShadowRadius: radius`, `textShadowOffset: { width: 0, height: 0 }`. | Defaults to mobile shadow: `textShadowColor: color`, `textShadowRadius: radius`, `textShadowOffset: { width: 0, height: 0 }`. | Translates to string-based CSS property: `textShadow: "0 0 [radius]px [color]"`. |
| `bleTimingConstants.ts` $\rightarrow$ `MTU_RETRY_SETTLE_MS` | Unused (iOS CoreBluetooth manages MTU negotiations internally). | Empirically required: Android-only negotiation retry loop throttle (200ms gap) to prevent concurrent write collisions on Qualcomm/MediaTek adapters. | N/A |

---

## 6. Design System & Token Manifest

The design tokens codified in `src/theme/theme.ts` establish the "Righteous Neon" visual identity of the app:

### Typography Scale
Every typography preset enforces the brand-defining font family: **`Righteous`**.
* **Header**: `fontSize: 24`, `letterSpacing: 2`, `textTransform: 'uppercase'`, `fontFamily: 'Righteous'`
* **Title**: `fontSize: 16`, `letterSpacing: 0.5`, `fontFamily: 'Righteous'`
* **Body**: `fontSize: 14`, `fontFamily: 'Righteous'`
* **Caption**: `fontSize: 11`, `fontFamily: 'Righteous'`

### Spacing Scale
* `xxs`: 2px
* `xs`: 4px
* `sm`: 8px
* `md`: 12px
* `lg`: 16px
* `xl`: 24px
* `xxl`: 32px
* `xxxl`: 40px
* `huge`: 48px
* `giant`: 64px

### Layout Metrics
* `Layout.padding`: `Spacing.lg` (16px)
* `Layout.borderRadius`: `Spacing.xl` (24px)

### Colors Palette Manifest

```json
{
  "DarkColors": {
    "background": "#1B4279",
    "surface": "#245596",
    "surfaceHighlight": "#3172C9",
    "primary": "#FF5A00",
    "secondary": "#FFB800",
    "accent": "#FF3300",
    "text": "#FFFFFF",
    "textMuted": "#A0B4CF",
    "textDim": "#6B85A0",
    "border": "#2E5FA3",
    "success": "#00E88F",
    "error": "#FF3D71",
    "warning": "#FFB800",
    "isDark": true
  },
  "LightColors": {
    "background": "#EAEFF5",
    "surface": "#CBD6E2",
    "surfaceHighlight": "#DDE5EE",
    "primary": "#FF5A00",
    "secondary": "#FFB800",
    "accent": "#1B4279",
    "text": "#0A1C38",
    "textMuted": "#5C7491",
    "textDim": "#8A9EB5",
    "border": "#B0C0D0",
    "success": "#00C476",
    "error": "#FF3D71",
    "warning": "#E07A00",
    "isDark": false
  }
}
```

---

## 7. Sequence Diagram

This sequence diagram illustrates the initialization of the static theme values inside the dynamic React app structure, user swap events, and AsyncStorage persistence:

```mermaid
sequenceDiagram
    autonumber
    actor User as Skater
    participant App as App.tsx
    participant Provider as ThemeProvider (ThemeContext.tsx)
    participant Storage as AsyncStorage
    participant Styles as DashboardStyles.ts
    participant Screen as DashboardScreen.tsx

    Note over App,Provider: App Initialization & Mount
    App->>Provider: Mount ThemeProvider
    activate Provider
    Provider->>Storage: getItem("@Sk8lytz_ThemeMode")
    Storage-->>Provider: return "dark" or "light" (or null)
    Provider->>Storage: getItem("@Sk8lytz_ControlUITheme")
    Storage-->>Provider: return "CLASSIC" | "MODERN" | "DOCKED"
    Provider-->>Provider: Set state: themeMode & controlUITheme
    deactivate Provider

    Note over Provider,Screen: Rendering Flow
    Screen->>Provider: useTheme() (injects Colors, isDark, controlUITheme)
    Provider-->>Screen: return colors (DarkColors or LightColors)
    Screen->>Styles: createDashboardStyles(Colors, height, width)
    Styles-->>Screen: return style Sheet
    Screen->>Screen: Render with computed responsive styles

    Note over User,Storage: Runtime Interactions
    User->>Screen: Toggle Theme Mode button
    Screen->>Provider: toggleTheme()
    activate Provider
    Provider-->>Provider: Update themeMode state (e.g. "dark" -> "light")
    Provider->>Storage: setItem("@Sk8lytz_ThemeMode", "light")
    Provider-->>Screen: propagate new Colors (LightColors)
    deactivate Provider
    Screen->>Styles: createDashboardStyles(LightColors, height, width)
    Styles-->>Screen: return light stylesheet
    Screen->>Screen: Re-render UI with Light Colors
```

---

## 8. Stale Documentation & Key Registry Drift

During our audit, we identified documentation key name drift in `tools/SK8Lytz_App_Master_Reference.md`:

### 🚨 Key Registry Drift Detected
* The **Master Reference** Key Registry lists:
  * `@sk8lytz_theme` (linked to `ThemeContext`)
  * `@sk8lytz_control_theme` (linked to `ThemeContext`)
* The **Actual Code** (`ThemeContext.tsx`) loads and saves:
  * `@Sk8lytz_ThemeMode` (for theme state)
  * `@Sk8lytz_ControlUITheme` (for layout presets)

### 🧹 Archival Tags Applied
Stale document keys in `tools/SK8Lytz_App_Master_Reference.md` are marked for replacement:

<!-- CARTOGRAPHER_END: THEME_&_ASSETS -->

### Domain: SIMULATION_&_MOCKS
<!-- CARTOGRAPHER_START: SIMULATION_&_MOCKS -->

# 🗺️ SIMULATION_&_MOCKS Domain Cartography

This document is the **Canonical Architectural Map** for the simulation, mocking, and unit testing subsystems in the SK8Lytz application. It catalogs Jest hardware mocks, Expo Web shims, the offline Virtual BLE Daemon simulator, and all service-level unit test structures.

---

## 1. File Manifest

Every file in the `SIMULATION_&_MOCKS` domain is mapped below with its precise architectural purpose:

| File Path | Type | Architectural Purpose |
| :--- | :--- | :--- |
| `src/__mocks__/LocationService.ts` | Jest Mock | Mocks foreground permission requests and silent GPS coordinates fetching to allow location-dependent testing in Jest without actual device positioning modules. |
| `src/__mocks__/expo-audio.ts` | Jest Mock | Stub for the Expo Audio module permission requests, returning mock granted status in unit tests. |
| `src/__mocks__/expo-location.ts` | Jest Mock | Stubs Expo Location APIs (coordinates, Accuracy, foreground permissions, reverse geocoding) to mock GPS interactions for unit tests. |
| `src/__mocks__/sk8lytz-watch-bridge.ts` | Jest Mock | Jest mock replacing the native watch bridge module, enabling assertions on watch state sync and health/metric listener callbacks in unit tests without physical watch hardware. |
| `src/mocks/react-native-vision-camera-worklets.web.js` | Web Shim | Web stub exporting an empty object to prevent runtime crashes on Web platforms due to native Vision Camera frame processor imports. |
| `src/mocks/react-native-worklets.web.js` | Web Shim | Web stub for `react-native-worklets-core` to prevent `TurboModuleRegistry` crashes on Web by mocking `useSharedValue`, `useAnimatedStyle`, `runOnJS`, and `runOnUI`. |
| `src/hooks/ble/__tests__/ble-simulator.test.ts` | Unit Test | Runs integration and regression tests for the Virtual BLE simulator (`ble_simulator.js`), verifying V2 wrapping, command parsing, and safety bounds like the 12-pixel static colorful buffer lockout check. |
| `tools/ble-simulator/ble_simulator.js` | Dev Tool | An offline native Node.js HTTP server acting as a Virtual BLE daemon that parses and mimics Neogleamz / Zengge Symphony hardware GATT opcodes and connection behaviors. |
| `__tests__/services/SpeedTrackingService.offline.test.ts` | Unit Test | Verifies offline session queue persistence, optimistic local writes via AsyncStorage, deduplicated flush routines with re-entrancy protection, and auth-sync behavior. |
| `src/components/__tests__/components.test.ts` | Static Test | Formulates static import verification checks on key UI controller and dashboard files (`DockedController.tsx`, `DashboardScreen.tsx`) to catch broken relative references. |
| `src/hooks/__tests__/useControllerDispatch.test.ts` | Unit Test | Validates UI dispatcher behavior, actions, state changes, and logging. |
| `src/hooks/__tests__/useDashboardAutoConnect.test.ts` | Unit Test | Tests background reconnection logic, AsyncStorage cache hits, SecureStore integration, and auto-connection trigger cascades. |
| `src/hooks/__tests__/useDeviceStateLedger.test.ts` | Unit Test | Tests the ledger that tracks device properties, connection states, and battery levels. |
| `src/hooks/ble/__tests__/useBLEBatterySweep.test.ts` | Unit Test | Verifies polling/sweep cycles for battery telemetry aggregation across multiple connected BLE devices. |
| `src/hooks/ble/__tests__/useBLERSSIMonitor.test.ts` | Unit Test | Verifies RSSI strength monitoring and connection drop thresholds. |
| `src/protocols/__tests__/BanlanxAdapter.test.ts` | Unit Test | Verifies correct byte compilation of BanlanX SP621E hardware commands, value-overflow boundaries, and FFE0 advertisement matching rules. |
| `src/protocols/__tests__/ControllerRegistry.test.ts` | Unit Test | Assures service UUID mapping, priority sorting (BanlanX first), fallback protocols (Zengge default), and duplicate registration guards. |
| `src/protocols/__tests__/PatternEngine.test.ts` | Unit Test | Assures math translation logic and byte generation for native patterns (0x51 compact mode) and street patterns (0x59 spatial arrays). |
| `src/protocols/__tests__/ZenggeProtocol.test.ts` | Unit Test | Assures Zengge Symphony v2 header framing, checksum mathematical accuracy, time-sync formatting, 0x59 colors array padding, RF remote state mapping, and packet-chunking rules. |
| `src/screens/__tests__/HardwareSetupWizardScreen.test.tsx` | Unit Test | Validates the registration payload structure to enforce the multi-group array contract (`group_ids` and `group_names`) and reject the legacy scalar `group_id`. |
| `src/services/__tests__/AppLogger.test.ts` | Unit Test | Tests AppLogger telemetry queue, batch upload limits, and offline local cache behavior. |
| `src/services/__tests__/GroupRepository.test.ts` | Unit Test | Assures AsyncStorage persistence and Supabase synchronization of group records. |
| `src/services/ble/__tests__/BleMachine.test.ts` | Unit Test | Validates xstate BLE machine lifecycle states (scanning, connecting, ready, recovering, disconnecting) and organic connection loss response. |
| `src/services/ble/__tests__/ConnectService.test.ts` | Unit Test | Tests GATT connection routines, Android MTU negotiation with fallback limits, and GATT 133 error recovery. |
| `src/services/ble/__tests__/HeartbeatService.test.ts` | Unit Test | Validates periodic 45s heartbeat polling, command write query queries, and RSSI fallbacks. |
| `src/services/ble/__tests__/InterrogatorService.test.ts` | Unit Test | Tests hardware capabilities probing, response timeout mechanisms, FTUE/standard queue delays, and localStorage cache loads. |
| `src/services/ble/__tests__/RecoveryService.test.ts` | Unit Test | Tests auto-reconnect backoff cycles, write queue purging, and Phase 3 polling. |
| `src/utils/__tests__/kMeansPalette.test.ts` | Unit Test | Tests color palette clustering algorithm for LED pattern extraction. |
| `tools/observatory/__tests__/dedup.test.js` | Tool Test | Statically tests the task deduplication scripts inside the sentinel toolchain. |
| `tools/observatory/__tests__/scoring.test.js` | Tool Test | Statically tests the sentinel script scoring metrics for task priority grading. |
| `tools/observatory/__tests__/task_generator.test.js` | Tool Test | Tests the task generation script for compliance with templates. |

---

## 2. Blast Radius (Imports/Exports)

### 2.1 Native Module Mocks (`src/__mocks__/*`)
* **Imports**: Standard TypeScript types and mock configurations.
* **Exports/Consumers**: Substituted transparently by the Jest test runner during unit testing via mapping rules configured in `jest.config.js`:
  ```javascript
  moduleNameMapper: {
    '^expo-location$': '<rootDir>/src/__mocks__/expo-location.ts',
    '^expo-audio$': '<rootDir>/src/__mocks__/expo-audio.ts',
    '^sk8lytz-watch-bridge$': '<rootDir>/src/__mocks__/sk8lytz-watch-bridge.ts',
  }
  ```

### 2.2 Web platform Shims (`src/mocks/*`)
* **Imports**: None (zero-dependency no-op code).
* **Exports/Consumers**: React Native Web bundler (`metro.config.js`) resolving targets dynamically. Prevents white-screen crashes on non-native platform builds:
  ```javascript
  const WEB_SHIMS = {
    'react-native-worklets-core': path.resolve(__dirname, 'src/mocks/react-native-worklets.web.js'),
    'react-native-vision-camera-worklets': path.resolve(__dirname, 'src/mocks/react-native-vision-camera-worklets.web.js'),
  };
  ```

### 2.3 Virtual BLE Daemon (`tools/ble-simulator/ble_simulator.js`)
* **Imports**: Native Node.js modules: `http`, `path`. Written in **100% dependency-free JavaScript** following the project's strict Dependency Diet Protocol.
* **Exports/Consumers**: Spelled out as an HTTP listener. Spawned as a child process by `ble-simulator.test.ts` during local testing. Can also be launched standalone to allow offline frontend layout/visualizer development on Web builders where hardware is physically unavailable.

---

## 3. Context Matrix

The `SIMULATION_&_MOCKS` domain operates outside the React component lifecycle:
* **Context Consumption**: None. Mocks and tests run in raw Node/V8 environments.
* **Context Mocking**: Unit tests that target UI components (e.g. `HardwareSetupWizardScreen.test.tsx`) deliberately mock or bypass state contexts (such as `AuthContext` or `ThemeContext`) to prevent crashes caused by rendering mismatches in React Native testing libraries.
* **XState Machine Context**: In xstate-specific tests (e.g., `BleMachine.test.ts`), the tests mock and assert on the private **XState Machine Context** (e.g. `targetMacs`, `connectedDevices`, `ghostedDeviceIds`) rather than React Contexts.

---

## 4. Hook/Service I/O Registry

### 4.1 `ble_simulator.js` (Virtual BLE Lab Daemon)
* **Inputs**:
  * `HTTP GET /state`: Read current internal mock hardware state.
  * `HTTP POST /state`: Force mock states (e.g. change battery level, disconnect status).
  * `HTTP GET /adv`: Discovers advertising packet structure (UUIDs, name, productId).
  * `HTTP POST /connect`: Triggers connection flag override.
  * `HTTP POST /disconnect`: Simulates organic connection drop to test recovery hooks.
  * `HTTP POST /write`: Accepts packet bytes in hex, array, or base64 format.
* **Outputs**:
  * JSON payload corresponding to the state of the virtual hardware chipset (e.g. power status, active LED points, segments count, color sorting registry).
* **Side-effects**: 
  * Writes diagnostic log traces to stdout.
  * Evaluates array bounds: if a `0x59` spatial color array packet is dispatched with fewer than 10 RGB pixels, triggers a buffer warning simulating the `0xA3` hardware EEPROM buffer lockout constraint.

### 4.2 `LocationService` Mock (`src/__mocks__/LocationService.ts`)
* **Inputs**: None.
* **Outputs**:
  * `getSilentLocation`: Resolved mock returning `null`.
  * `requestLocationPermissions`: Resolved mock returning `false`.
* **Side-effects**: None (read-only stub).

### 4.3 `expo-audio` Mock (`src/__mocks__/expo-audio.ts`)
* **Inputs**: None.
* **Outputs**:
  * `requestRecordingPermissionsAsync`: Resolved mock returning `{ status: 'granted' }`.
  * `getRecordingPermissionsAsync`: Resolved mock returning `{ status: 'granted' }`.
* **Side-effects**: None.

### 4.4 `expo-location` Mock (`src/__mocks__/expo-location.ts`)
* **Inputs**: None.
* **Outputs**:
  * `requestForegroundPermissionsAsync`: Resolved mock returning `{ status: 'granted' }`.
  * `getForegroundPermissionsAsync`: Resolved mock returning `{ status: 'granted' }`.
  * `getCurrentPositionAsync` / `getLastKnownPositionAsync`: Resolved mock coordinates: `{ latitude: 38.90, longitude: -94.60, accuracy: 10 }`.
  * `reverseGeocodeAsync`: Mock array: `[{ city: 'Overland Park', region: 'KS', name: 'SkateCity OP' }]`.
* **Side-effects**: None.

### 4.5 `sk8lytz-watch-bridge` Mock (`src/__mocks__/sk8lytz-watch-bridge.ts`)
* **Inputs**: None.
* **Outputs**:
  * `syncSessionState` / `sendMetricUpdate`: Resolved mock returning `undefined`.
  * `isWatchReachable`: Resolved mock returning `false`.
  * `addWatchCommandListener` / `addWatchHealthListener`: Mock registering callbacks, returning unsubscribe function.
* **Side-effects**: None.

### 4.6 `react-native-worklets` Web Shim (`src/mocks/react-native-worklets.web.js`)
* **Inputs**: Callbacks, share values.
* **Outputs**:
  * `useSharedValue`: Mock returning `{ value: null }`.
  * `useAnimatedStyle`: Mock returning empty object `{}`.
  * `runOnJS` / `runOnUI`: Identity function passing through input closures directly on the JS main thread.
* **Side-effects**: None.

---

## 5. OS Variance Matrix

Mocking configurations implement specific iOS/Android and Web runtime branching behaviors:

### 5.1 Web Platform Bundling Bypass
Native React Native dependencies (`react-native-worklets-core`, `react-native-vision-camera`) are completely bypassed on Web builds via Metro aliases (`src/mocks/*.web.js`). This avoids bundler execution errors caused by missing TurboModule implementations.

### 5.2 Android-Specific MTU Negotiation Modeling
In `ConnectService.test.ts`, the test suite models Android-specific MTU requests:
* **Android Path**: Explicitly invokes `requestMTU(512)`. The test verifies fallback pathways: if MTU negotiation glitches and returns the minimal `23` payload limit, the system renegotiates and rolls back to `186` bytes. If it fails altogether, it falls back to the safe `186` ceiling.
* **iOS Path**: The MTU is managed transparently at the OS layer (omitted from direct BLE execution calls).

---

## 6. Sequence Diagrams

### 6.1 Virtual BLE Simulator Protocol Validation
The following sequence details how unit tests boot the virtual HTTP daemon to validate byte-level commands offline:

```mermaid
sequenceDiagram
    autonumber
    actor T as Jest Unit Test
    participant S as Child Process (ble_simulator.js)
    participant H as Node HTTP Server (Port 18081)
    participant D as Device State Registry
    
    T->>S: Spawn Node child process
    S->>H: Start HTTP server on 18081
    H-->>T: Server Ready Listener
    
    T->>H: POST /connect (Initialize connection)
    H->>D: Set isConnected = true
    H-->>T: 200 OK (State: Connected)
    
    T->>H: POST /write (Send 0x59 Colors Array - 8 pixels)
    H->>D: Process payload (length < 10 pixels check)
    Note over H,D: Warnings added to registry:<br/>"EEPROM_BUFFER_OVERFLOW_RISK"
    H->>D: Update colors registry array
    H-->>T: 200 OK (Payload ACK with Warning flag)
    
    T->>H: GET /state (Verify hardware registry)
    H->>D: Query current values
    D-->>H: Return registry map
    H-->>T: 200 OK (Current state payload)
    
    T->>S: Send SIGTERM
    S->>H: Shutdown Server
    H-->>T: Process Terminated (Clean Exit)
```

### 6.2 Android MTU Negotiation and Fallback Pipeline
This sequence maps how `ConnectService.ts` executes MTU adjustments on Android, including renegotiation retry branches:

```mermaid
sequenceDiagram
    autonumber
    participant C as ConnectService
    participant B as BleManager (RN BLE PLX)
    participant D as Peripheral Device (GATT Server)
    participant M as MTU Map Reference
    
    C->>B: requestMTUForDevice(deviceId, 512)
    B->>D: GATT MTU Request (512)
    
    alt Happy Path (MTU negotiated successfully)
        D-->>B: Return negotiated MTU (e.g. 512)
        B-->>C: Resolved MTU (512)
        C->>M: Store in mtuMapRef -> Map(deviceId: 512)
    else Glitch Case (Negotiated MTU drops to minimal 23)
        D-->>B: Return MTU (23)
        B-->>C: Resolved MTU (23)
        Note over C: Detect Glitch (mtu === 23)<br/>Trigger Android Settle & Retry
        C->>B: requestMTUForDevice(deviceId, 186)
        B->>D: GATT MTU Request (186)
        D-->>B: Return negotiated MTU (186)
        B-->>C: Resolved MTU (186)
        C->>M: Store in mtuMapRef -> Map(deviceId: 186)
    else Failure Case (Request fails / times out)
        D-->>B: Timeout / Connection Error
        B-->>C: Reject requestMTU
        Note over C: Catch Error<br/>Apply stable fallback
        C->>M: Store in mtuMapRef -> Map(deviceId: 186)
    end
```

---

## 7. Architectural Impact Flags

The mocking and simulation infrastructure has structural dependencies that govern feature releases:

* `[IMPACTS_USER_JOURNEY]` — UI mocks (like `HardwareSetupWizardScreen.test.tsx`) enforce group registration structures. Any change to the wizard registration payload must be tested against these mocks to prevent regressions in setup flows.
* `[IMPACTS_STATE_CHART]` — `BleMachine.test.ts` and `RecoveryService.test.ts` verify the transitions of the XState BLE machine. Modifications to scanning, connection timing, or recovery loops will directly affect state machine tests.
* `[IMPACTS_C4_CONTEXT]` — The virtual BLE daemon (`ble_simulator.js`) and database mocks reflect external hardware APIs. Changes in OEM protocol payloads must be matched by updates in the simulator logic.

<!-- CARTOGRAPHER_END: SIMULATION_&_MOCKS -->

### Domain: BUILD_CONFIG
<!-- CARTOGRAPHER_START: BUILD_CONFIG -->

# SK8Lytz Build Configuration Cartography Audit

_Domain Marker: **BUILD_CONFIG** | Last Updated: 2026-06-11_

This document presents a read-only architectural audit of the configuration, transpilation, compilation, package management, and validation pipeline within the SK8Lytz repository.

---

## 1. File Manifest

The `BUILD_CONFIG` domain comprises files governing package dependency, bundling, native compilation targets, testing configurations, and CI/git hooks.

| File Path | Architectural Purpose |
| :--- | :--- |
| [`package.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json) | Primary NPM project manifest defining versioning, script entry points, runtime dependencies, devDependencies, and resolution overrides. |
| [`app.config.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js) | Dynamic Expo application configuration detailing app metadata, native permissions (iOS/Android), config plugins, and environment-variable-backed API keys. |
| [`eas.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/eas.json) | Configuration for Expo Application Services (EAS) CLI, builds (profiles: development, preview, production), and submissions. |
| [`metro.config.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/metro.config.js) | Metro Bundler configuration that integrates a custom resolver to swap native-only modules (like `react-native-worklets`) for web-platform shims during Web builds to prevent runtime crashes. |
| [`babel.config.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/babel.config.js) | Babel configuration presets and plugins (e.g., `babel-preset-expo` and the `react-native-worklets/plugin` plugin) for syntax transformation. |
| [`tsconfig.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tsconfig.json) | TypeScript configuration extending Expo's base tsconfig, enabling strict type checks, and defining local path mappings (such as `sk8lytz-watch-bridge`). |
| [`jest.config.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/jest.config.js) | Jest testing framework configuration setting up `ts-jest` node environment, ignoring e2e and local-builder dirs, and mapping mocked modules for headless testing. |
| [`.husky/pre-commit`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.husky/pre-commit) | Worktree-aware Git pre-commit hook that verifies Babel syntax, runs ESLint on staged files, checks types with TSC, and triggers unit tests in a shared workspace structure. |
| [`.husky/pre-push`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.husky/pre-push) | Git pre-push hook serving as a zero-bypass gate that cryptographically verifies the QA checklist and runs vulnerability scans (`npm audit`). |
| [`plugins/withWearOsModule.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/plugins/withWearOsModule.js) | Custom Expo configuration plugin that dynamically injects Wear OS Gradle dependencies and links the `:sk8lytzWear` native module into the generated Android configuration during Expo prebuild. |
| [`targets/watch/expo-target.config.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/expo-target.config.js) | SwiftUI Apple Watch targets configuration defining bundleId, deployment target, HealthKit entitlements, and CLKComplication Principal Class maps. |
| [`modules/sk8lytz-watch-bridge/expo-module.config.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/expo-module.config.json) | Configuration JSON for the local watch companion Expo module, specifying iOS Swift and Android Kotlin module interfaces. |
| [`modules/sk8lytz-watch-bridge/package.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/package.json) | Local watch bridge NPM manifest declaring dependencies and TypeScript build script. |
| [`modules/sk8lytz-watch-bridge/android/build.gradle`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/android/build.gradle) | Android library gradle setup for the watch bridge, targeting SDK 36 and resolving Google Play Services Wearable APIs. |
| [`.detoxrc.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.detoxrc.js) | Configuration for Detox end-to-end testing, defining local Android emulator configurations and build scripts for E2E testing. |
| [`e2e/jest.config.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/e2e/jest.config.js) | Jest configuration specifically optimized for running Detox E2E tests in a sequential runner environment. |
| [`.gitignore`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.gitignore) | Git configuration excluding built targets, temporary dev structures, and environment configuration files, with negation exceptions for Wear OS and specific adapter protocols. |
| [`tools/verifiable-check-runner.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/verifiable-check-runner.js) | Master QA test script that executes full TSC, Jest, AST, BLE architecture, organic disconnect, and type safety checks, generating a cryptographically signed attestation file (`.test-attestation.json`). |
| [`tools/blast-radius-scanner.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/blast-radius-scanner.js) | CI pipeline check script ensuring developers modify corresponding test and dependent files when modifying core architectural components. |
| [`scripts/babel-syntax-gate.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/scripts/babel-syntax-gate.js) | Check script parsing files with Babel parser to verify AST syntactical correctness and catch bundler-breaking errors early. |

---

## 2. Blast Radius

The `BUILD_CONFIG` domain operates as the global scaffolding of the codebase, meaning its modifications have substantial structural ramifications.

### Inbound Dependencies (What it Imports)
- **Tooling Scripts**: Refers to custom local validation engines like `tools/blast-radius-scanner.js`, `tools/verifiable-check-runner.js`, `scripts/babel-syntax-gate.js`, and `plugins/withWearOsModule.js`.
- **NPM Package Modules**: Integrates dependencies, config plugins, and tools (`expo/metro-config`, `@expo/config-plugins`, `ts-jest`, `@bacons/apple-targets`).
- **Shared Secrets / Env**: Dynamically reads environment configurations (`process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY`) to inject keys into native settings.

### Outbound Dependencies & Exports (What imports it / What it impacts)
- **Global Compilation**: `metro.config.js`, `babel.config.js`, and `tsconfig.json` govern how **every source file** in `/src` is resolved, transpiled, and compiled.
- **Developer Workflows**: Git Hooks (`.husky/pre-commit` and `.husky/pre-push`) capture developer commands globally, enforcing static code gates on any file changes.
- **Native OS Output**: `app.config.js` and `plugins/withWearOsModule.js` export dynamic app configurations that directly generate `/ios` and `/android` directories during prebuild. These set permissions (Health, BLE, location, camera), configure SDK compiles, inject custom ProGuard rules, and bundle Wear OS companion APKs.

---

## 3. Context Matrix

As the build configuration layer is non-executable at React runtime, it does not provide or consume React Contexts directly. 

However, it establishes the build environment and module mocks mapping (e.g. mapping `sk8lytz-watch-bridge` to test mocks in `jest.config.js`) that allows context components (like `SessionContext` and `AuthContext`) to compile cleanly in test or web settings.

---

## 4. Hook/Service I/O Registry

Because this domain does not contain standard hooks or runtime services, we map the input, output, and side-effects of our custom build tools, plugins, and configurations:

### `withWearOsModule(config)` (Expo Config Plugin)
- **Inputs**: Expo `config` object.
- **Outputs**: Expo `config` object with injected Gradle configurations.
- **Side-Effects**: 
  - Modifies `settings.gradle` to append `:sk8lytzWear`.
  - Modifies `app/build.gradle` to append the `wearApp` project dependency (bundling the watch app with the main APK).
  - Modifies `AndroidManifest.xml` to inject the Notifee `ForegroundService` with locations/health/connectedDevice/shortService/dataSync foreground service types.

### `metro.config.js` Custom Resolver
- **Inputs**: `context` (Metro bundler context), `moduleName` (imported package path), `platform` (target build platform).
- **Outputs**: Resolver object mapping to a source file or calling default resolution.
- **Side-Effects**: Re-routes native-only imports (`react-native-worklets` and `react-native-vision-camera-worklets`) to zero-op stub mocks (`src/mocks/*.web.js`) ONLY during web builds, avoiding JSI/TurboModule initialization crashes.

### `verifiable-check-runner.js` Execution Runner
- **Inputs**: `--verify` (boolean flag to verify existing attestation).
- **Outputs**: Output exit code 0 or 1.
- **Side-Effects**: 
  - Dynamically creates `node_modules` Windows junctions in worktrees if absent.
  - Spawns `tsc --noEmit`, `jest` unit tests, `web-console-harvester.js` headless browser logs, Sentinel `ast_knowledge_compiler.js` checks, BLE architectural sweeps (restricting `startDeviceScan`), `useBLE.ts` organic disconnect checks, and production `as any` scan.
  - Generates `.test-attestation.json` with a cryptographic SHA-256 HMAC signature anchored to the HEAD commit.

### `blast-radius-scanner.js`
- **Inputs**: `--cached`, `--worktree`, or `--branch <branch_name>`.
- **Outputs**: Exit code 0 (pass) or 1 (violation).
- **Side-Effects**: Validates changed files against `tools/ARCH_DEPENDENCY_MAP.json` to ensure code changes in core services are paired with updates to their respective UI wrappers or interfaces.

---

## 5. OS Variance Matrix

The configuration layer branches extensively across target operating platforms:

| Feature / Setting | iOS Target Config | Android Target Config | Web Build Fallback |
| :--- | :--- | :--- | :--- |
| **Permissions Injected** | Info.plist keys: camera, microphone, locations, and HealthKit (`NSHealthShareUsageDescription`, `NSHealthUpdateUsageDescription`). | Manifest permissions: fine/coarse location, camera, audio recording, bluetooth scan/connect, notification posts, activity recognition, and foreground services. | N/A (Permissions handled via standard browser prompts). |
| **Native Bundling Target** | SwiftUI watchOS Companion (`targets/watch/`) compiled via `@bacons/apple-targets`. | Kotlin Wear OS Companion (`android/sk8lytzWear/`) injected via custom settings/build gradle config plugin. | N/A (Builds clean single-page application). |
| **SDK & Compiling Rules** | Handled natively by Xcode via iOS deployment target 10.0+ for watch OS targets. | Target/Compile SDK 36, Min SDK 26, enable Jetifier (`enableJetifier: true`). | Bundled through customized Metro resolver substituting web-platform mocks. |
| **Compilation Obfuscation** | Standard Swift/Obj-C compilation rules. | Android-specific ProGuard rules keeping BLE (`com.polidea`), Camera (`com.mrousavy.camera`), and Nitro (`com.mrousavy.nitro`) classes intact. | Webpack/Metro minification rules. |
| **Developer Hooks OS Compatibility** | Hook executes Unix-compliant bash scripts directly. | Hook executes Unix-compliant bash scripts directly. | Hook executes Unix-compliant bash scripts directly. |
| **Worktree Junctions** | Native symlinks (ln -s) in Unix environments. | Windows-specific command fallback: `cmd.exe /c "mklink /j node_modules ..."` to create directories junctions. | N/A. |

---

## 6. Sequence Diagram

Below is the **Pre-Push Zero-Bypass QA Gate & Cryptographic Attestation Pipeline** indicating how build scripts protect the repository during commits and pushes:

```mermaid
sequenceDiagram
    autonumber
    actor Developer
    participant Git as Git Client (Push Hook)
    participant Runner as verifiable-check-runner.js
    participant HMAC as HMAC Signer
    participant GitRemote as remote master

    Developer->>Git: git push origin main
    activate Git
    Git->>Runner: Execute pre-push hook (--verify)
    activate Runner
    Note over Runner: Read .test-attestation.json & shared salt
    Runner->>HMAC: Recalculate signature & verify age (<15m)
    alt Attestation invalid or missing
        HMAC-->>Runner: Signature Mismatch / Stale
        Runner-->>Git: Exit 1 (Reject Push)
        Git-->>Developer: Reject Push: Run "npm run verify" to generate a fresh attestation.
    else Attestation is valid
        HMAC-->>Runner: Verification Success
        Runner->>Git: Exit 0 (Pass Gate)
        Git->>GitRemote: Push commits to remote
        GitRemote-->>Git: Push Accepted
        Git-->>Developer: Success
    end
    deactivate Runner
    deactivate Git
```

---

## 7. Master Reference Archival Ledger

The following sections in the Master Reference (`tools/SK8Lytz_App_Master_Reference.md`) are stale and tagged for relocation:


---

_Report compiled by SDE Cartographer Node. Architectural surface has been verified. Git-Safety policies checked._

<!-- CARTOGRAPHER_END: BUILD_CONFIG -->

### Domain: OS_PERMISSIONS
<!-- CARTOGRAPHER_START: OS_PERMISSIONS -->

# OS_PERMISSIONS Domain Cartography Report

This cartography report details the file manifest, imports/exports (Blast Radius), React Context dependencies, API parameters, OS-specific differences, runtime permission request sequence, and security discrepancies for the `OS_PERMISSIONS` domain in SK8Lytz.

---

## 1. File Manifest

The `OS_PERMISSIONS` domain consists of the following key configuration and source files:

| File Name | Location | Architectural Purpose |
| :--- | :--- | :--- |
| `AndroidManifest.xml` | `android/app/src/main/AndroidManifest.xml` | Defines the main Android application package metadata, activity structures, intent filters, and requests all Android application-level permissions. |
| `AndroidManifest.xml` | `android/sk8lytzWear/src/main/AndroidManifest.xml` | Declares Wear OS watch-specific companion features and runtime body sensor permissions. |
| `app.config.js` | `app.config.js` | The root Expo managed configuration script detailing iOS Info.plist usage descriptions, Android permissions array, and native config plugins. |
| `Info.plist` | `targets/watch/Info.plist` | Empty property list configuration reserved for Apple Watch companion targets. |
| `withWearOsModule.js` | `plugins/withWearOsModule.js` | Custom Expo config plugin injecting settings/dependencies for the `:sk8lytzWear` module and patching the Notifee Foreground Service types in AndroidManifest.xml. |
| `PermissionService.ts` | `src/services/PermissionService.ts` | The core service containing check, request, and app-level soft-revocation logic for all permissions. |
| `BluetoothGuard.tsx` | `src/providers/BluetoothGuard.tsx` | App startup context guard preventing mounting of core dashboards until Bluetooth permissions and radio are enabled. |
| `PermissionsOnboardingScreen.tsx` | `src/screens/Onboarding/PermissionsOnboardingScreen.tsx` | Onboarding view that registers telemetry and automatically attempts Bluetooth permission access to eliminate friction. |
| `GranularPermissionsList.tsx` | `src/components/permissions/GranularPermissionsList.tsx` | Tabled view letting users granularly view, request, or toggle soft-revokes (opt-outs) on permissions. |

---

## 2. Blast Radius

The permissions domain forms a foundational layer. Changing manifest parameters or the permission APIs impacts the compilation config, third-party libraries, and UI modes:

### Imports (Incoming Dependencies)
* **Standard React Native Hooks & Components**: `react-native` (`Platform`, `PermissionsAndroid`, `DeviceEventEmitter`, `AppState`, `Linking`, `Alert`).
* **Expo APIs**: `expo-audio` (recording controls), `expo-location` (foreground coordinates), `expo-notifications` (inbound messages).
* **Native Ecosystems**: `react-native-health` (Apple HealthKit SDK), `react-native-health-connect` (Android Health Connect API).
* **Storage Utilities**: `@react-native-async-storage/async-storage` (`AsyncStorage`).
* **Config Plugins**: `@expo/config-plugins` (Gradle/Manifest mutation utilities).

### Exports (Outgoing Consumers)
* **Main Application Entry**: `App.tsx` (wraps main stack in `BluetoothGuard`).
* **Feature Handlers**: 
  * `CameraTracker.tsx` (consumes `PermissionService` to request camera access).
  * `DockedController.tsx` (consumes `PermissionService` to check location/camera status, handles soft fallback to offline modes if denied).
* **Onboarding & Settings UX**:
  * `PermissionsOnboardingScreen.tsx` (requests BLE permissions automatically on load).
  * `AccountTabSecurity.tsx` (exposes `GranularPermissionsList` to let users toggle permissions inside Settings).

---

## 3. Context Matrix

The domain consumes several contexts to enforce permission gates and theme styling, but does not publish contexts itself:

| React Context | Consumed By | Purpose |
| :--- | :--- | :--- |
| `BLEContext` (`useSharedBLE`) | `BluetoothGuard.tsx` | Accesses `isBluetoothEnabled` and `isBluetoothSupported` states to intercept app startup and calls `startSweeper()` on success. |
| `ThemeContext` (`useTheme`) | `BluetoothGuard.tsx`, `PermissionsOnboardingScreen.tsx`, `GranularPermissionsList.tsx` | Evaluates dynamic theme styles (`Colors.background`, `Colors.primary`, `Colors.textMuted`) to display modal dialogs and settings toggles. |

---

## 4. Hook/Service I/O Registry

The functional APIs exposed by `PermissionService.ts` and UI wrappers map as follows:

### PermissionService.ts Functions

#### `openGlobalPermissionsModal()`
* **Inputs**: None.
* **Outputs**: `Promise<void>`
* **Side-Effects**: Publishes `SHOW_GLOBAL_PERMISSIONS_EVENT` to open a global modal overlay; resolves once the `GLOBAL_PERMISSIONS_CLOSED_EVENT` event fires.

#### `getOptOutLedger()`
* **Inputs**: None.
* **Outputs**: `Promise<Record<PermissionType, boolean>>`
* **Side-Effects**: Reads the app-level permission opt-out mapping stored in `AsyncStorage` under `@sk8lytz_permissions_optout`.

#### `setPermissionOptOut(type: PermissionType, isOptedOut: boolean)`
* **Inputs**: `type: PermissionType` ('CAMERA' | 'MIC' | 'LOCATION' | 'NOTIFICATIONS' | 'BLUETOOTH' | 'HEALTH'), `isOptedOut: boolean`.
* **Outputs**: `Promise<void>`
* **Side-Effects**: Updates local storage, registers a `PERMISSION_OPT_IN` or `PERMISSION_OPT_OUT` event to `AppLogger`, and broadcasts `PERMISSION_STATUS_CHANGED_EVENT`.

#### `requestPermission(type: PermissionType)`
* **Inputs**: `type: PermissionType`.
* **Outputs**: `Promise<boolean>` (resolves to true if granted by OS/user).
* **Side-Effects**:
  * Triggers OS native dialog popup.
  * Android 12+ requests `BLUETOOTH_SCAN` and `BLUETOOTH_CONNECT` as a batch without location coordinates.
  * Android < 12 requests `ACCESS_FINE_LOCATION`.
  * Health Connect (Android) requests activity recognition first, then runs native `initialize()` initialization on a coroutine, and prompts for Health Connect metrics.
  * iOS HealthKit launches the Apple Health permissions interface.
  * Decoupled initialization of `NotificationService` occurs when notifications are allowed.

#### `checkPermission(type: PermissionType)`
* **Inputs**: `type: PermissionType`.
* **Outputs**: `Promise<boolean>` (resolves to true if allowed both in the app-level ledger and by the native OS).
* **Side-Effects**: None.

---

## 5. OS Variance Matrix

Handling permissions requires strict platform branches to comply with App Store guidelines, Google Play sandbox rules, and hardware API configurations:

```
                               ┌───────────────┐
                               │  Permissions  │
                               └───────┬───────┘
                                       │
                     ┌─────────────────┴─────────────────┐
                     ▼                                   ▼
                [ Platform.OS === 'ios' ]          [ Platform.OS === 'android' ]
             ┌───────────────────────────────┐   ┌───────────────────────────────┐
             │ - Camera: Checked implicitly  │   │ - Camera: Manual runtime request│
             │ - Bluetooth: System handles   │   │ - Bluetooth:                  │
             │ - Health: Apple HealthKit     │   │   - API >= 31: Scan & Connect │
             │   (Returns true due to        │   │     (neverForLocation)        │
             │    individual read privacy)   │   │   - API < 31: Location required│
             └───────────────────────────────┘   │ - Health:                     │
                                                 │   - Activity Recognition first│
                                                 │   - Health Connect (initHC    │
                                                 │     required before query)    │
                                                 └───────────────────────────────┘
```

### 1. Camera Permission (`CAMERA`)
* **Android**: Explicitly requests `PermissionsAndroid.PERMISSIONS.CAMERA`. Requires declaration in `AndroidManifest.xml`.
* **iOS**: Handled natively on first use; verification checks return `true` immediately in `PermissionService`. Requires description `NSCameraUsageDescription` in `app.config.js` (`Info.plist`).

### 2. Bluetooth Permission (`BLUETOOTH`)
* **Android 12+ (API >= 31)**: Batches permissions `BLUETOOTH_SCAN` and `BLUETOOTH_CONNECT`. Injects `neverForLocation` flags to bypass location settings entirely.
* **Android 11 and below (API < 31)**: Triggers permission requests for `ACCESS_FINE_LOCATION` to execute Bluetooth scans.
* **iOS**: Prompted natively on first scan. Uses the `react-native-ble-plx` plugin to append `NSBluetoothAlwaysUsageDescription` and `NSBluetoothPeripheralUsageDescription` to `Info.plist`.

### 3. Health & Biometrics (`HEALTH`)
* **Android (Health Connect)**: 
  * Requires a two-step query sequence. First, request `android.permission.ACTIVITY_RECOGNITION`. 
  * If granted, call the `initialize()` function on the native `react-native-health-connect` module before attempting queries. Failing to run initialization causes a native coroutine thread to crash due to an `UninitializedPropertyAccessException`.
  * Explicitly requires a custom intent action `androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE` inside the `AndroidManifest.xml` launcher activity to handle health permission rationale redirects.
* **iOS (Apple HealthKit)**:
  * Entitled with `com.apple.developer.healthkit` and `com.apple.developer.healthkit.background-delivery`.
  * Requires `NSHealthShareUsageDescription` and `NSHealthUpdateUsageDescription` keys.
  * **Privacy Gating (RISK-4)**: iOS deliberately conceals individual type permissions for read actions (always returns "not determined"). Thus, the check API pragmatically defaults to `true`, allowing actual fetch routines to return empty results gracefully without triggering crashes.

### 4. Background Services (Android Only)
* **Android 14+ (API >= 34) Compatibility**: Foreground services must register specific foreground types.
* **Discrepancy (API 36 Target)**: The application targets Android API `36` (targetSdkVersion: 36) in `app.config.js`, but does not declare all required foreground permissions (see Section 7).

---

## 6. Runtime Permission Sequence Diagram

The following diagram details the actor-to-actor flow when checking and requesting permissions, highlighting the integration between user-level settings and OS status:

```mermaid
sequenceDiagram
    autonumber
    actor U as User
    participant C as UI Component
    participant P as PermissionService
    participant AS as AsyncStorage (Ledger)
    participant OS as OS Native API
    
    %% Check permission flow
    Note over C, P: Check Permission Flow
    C->>P: checkPermission(type)
    P->>AS: getOptOutLedger()
    AS-->>P: returns ledger Record
    alt User has opted out in App (Soft-Revoke)
        P-->>C: returns false (early exit)
    else User is opted in
        P->>OS: checkNativePermissionStatus()
        OS-->>P: returns granted / denied
        P-->>C: returns boolean status
    end
    
    %% Request permission flow
    Note over C, P: Request Permission Flow
    C->>P: requestPermission(type)
    alt Android 12+ and type == BLUETOOTH
        P->>OS: requestMultiple(SCAN, CONNECT)
    else Android < 12 and type == BLUETOOTH
        P->>OS: request(ACCESS_FINE_LOCATION)
    else type == HEALTH (Android)
        P->>OS: request(ACTIVITY_RECOGNITION)
        Note over P, OS: If granted, initialize Health Connect
        P->>OS: requestHealthConnectPermission(records)
    else standard type
        P->>OS: requestNativePermission(type)
    end
    OS->>U: Display System Permission Modal
    U-->>OS: User choice (Grant / Deny)
    OS-->>P: returns system status (granted / denied)
    alt System Granted
        P->>AS: setPermissionOptOut(type, false)
        P->>P: Log to Cloud (PERMISSION_OPT_IN)
        P-->>C: returns true
    else System Denied
        P->>P: Log to Cloud (PERMISSION_OPT_OUT)
        P-->>C: returns false
        C->>U: Show "Open Settings" alert dialog
        U->>OS: Opens OS settings page manually
    end
```

---

## 7. Security / Vulnerability Risk Analysis

An audit of the manifest configurations reveals critical discrepancies:

### ⚠️ Android 14+ Foreground Service API Target Lockout (High Risk)
In `app.config.js`, the target SDK is compiled for Android 16 (API 36):
```json
"compileSdkVersion": 36,
"targetSdkVersion": 36,
```
However, the Custom Expo plugin `plugins/withWearOsModule.js` injects the Notifee foreground service configuration with the following types:
```xml
<service android:name="app.notifee.core.ForegroundService" android:foregroundServiceType="location|health|connectedDevice|shortService|dataSync" tools:replace="android:foregroundServiceType"/>
```
Starting with Android 14 (API 34), services using specific foreground types must declare their respective permissions in the manifest. The app requests:
* `android.permission.FOREGROUND_SERVICE_LOCATION` (for `location`)
* `android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE` (for `connectedDevice`)

**Critical Failure**: The app is missing the required permissions:
* `android.permission.FOREGROUND_SERVICE_HEALTH` (for `health` type)
* `android.permission.FOREGROUND_SERVICE_DATA_SYNC` (for `dataSync` type)

Because the app targets API 36, attempting to start the foreground service for health or data sync will cause the app to crash with a `SecurityException` at runtime on Android 14+.

### 🔒 Gitignored Google Maps API Key Exposure (Low Risk)
The Google Maps API key `AIzaSyBfvwN5fcyDbzUZp2Q7c2OfMLPFajVRPwA` is generated inside the local gitignored `AndroidManifest.xml` and is pulled from `process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY` in `app.config.js`. This is correctly handled locally and git-quarantined. However, because it is located in the local workspace directory, developers must avoid staging generated native `android` or `ios` outputs.

---

## 8. Archival Target Registry

During the codebase audit, the following stale reference was identified:

* **File**: `tools/SK8Lytz_App_Master_Reference.md`
* **Line Range**: 1182 (and 1573 in comments)
* **Stale Text**: `- **OS Sync**: syncSystemPermissions() runs on boot/foreground to reconcile the ledger with native OS settings. If OS is "Denied", App ledger is forced to "Opt-Out".`
* **Reason**: This behavior was explicitly deprecated inside `PermissionService.ts` (lines 60-65) because "aggressive sweeping of OS permissions locks users out on fresh installs before they can be prompted." The Master Reference text has not been updated to reflect the deprecation.

<!-- CARTOGRAPHER_END: OS_PERMISSIONS -->

### Domain: ADMIN_&_TELEMETRY
<!-- CARTOGRAPHER_START: ADMIN_&_TELEMETRY -->

# 🏛️ ADMIN_&_TELEMETRY Domain Cartography

_SDE Cartographer Node Read-Only Audit | Compiled: 2026-06-11 | Domain: `src/components/admin/*`, `src/services/AppLogger.ts`, `src/services/AppSettingsService.ts`, `src/hooks/useAdminSettings.ts`, `src/hooks/useAdminTelemetry.ts`, `src/hooks/useDiagnosticLog.ts`._

---

## 1. File Manifest

This domain contains 34 files representing the system administration portals, remote configuration engines, and logging infrastructure:

| Filename | Type | Architectural Purpose |
|:---|:---:|:---|
| `src/services/AppLogger.ts` | Service | Central telemetry singleton capturing, throttling, obfuscating PII, caching, and batch-uploading log data to Supabase (with a dedicated VIP Error Fast-Lane). |
| `src/services/AppSettingsService.ts` | Service | Configuration client managing application-wide settings and feature flags, offering instant local caches and background Supabase syncing. |
| `src/hooks/useAdminSettings.ts` | Hook | Manages local UI states for global app controls with optimistic updates, background retries, and automatic rollback on synchronization failures. |
| `src/hooks/useAdminTelemetry.ts` | Hook | Translates raw log event types into visual dashboard metadata (icons/labels) and controls upload, local clearing, and sharing interactions. |
| `src/hooks/useDiagnosticLog.ts` | Hook | Orchestrates the low-level Bluetooth Diagnostic Lab logs, command transmission wrappers, and verification verdicts for the Protocol Oracle. |
| `src/components/admin/adminStyles.ts` | Styling | Consolidates common high-contrast visual styles, layout variables, and typography definitions for the admin subsystem. |
| `src/components/admin/AdminTab.tsx` | UI | Renders the tab index page directing developers to hardware programming, diagnostics, and remote settings sub-panels. |
| `src/components/admin/AdminToolsModal.tsx` | UI | Command Center entry point wrapping the tab-navigation shell, virtualized log viewer, and statistics dashboard under a memoized architecture. |
| `src/components/admin/AdvancedHardwareModal.tsx` | UI | Debug Modal presenting peripheral chip register specifications and EEPROM configuration data. |
| `src/components/admin/ConfirmDeleteModal.tsx` | UI | Simple validation modal prompting confirmation before executing log deletion scripts. |
| `src/components/admin/DeviceTab.tsx` | UI | Aggregates connection logs to extract and list MAC addresses, firmware versions, and RSSI tracking charts. |
| `src/components/admin/StatsTab.tsx` | UI | Summarizes local system parameters (battery, total RAM, memory limits, and storage sizes) and app usage metrics. |
| `src/components/admin/tools/AdminAuditLogViewer.tsx` | UI | Displays the history trail of security actions taken by administrators. |
| `src/components/admin/tools/AdminPicksScheduler.tsx` | UI | Editor modal for scheduling spotlight picks, integrating date pickers, and building custom positional gradient arrays. |
| `src/components/admin/tools/AdminRosterPanel.tsx` | UI | Management interface to view, add, or delete registered administrators. |
| `src/components/admin/tools/AppManager.tsx` | UI | Configures settings and legal policies (EULA) dynamically mapped via the application-wide `ControlsRegistry`. |
| `src/components/admin/tools/FeatureFlagsPanel.tsx` | UI | Exposes feature flags for runtime A/B testing and staggered feature rollouts. |
| `src/components/admin/tools/GlobalAnalyticsPanel.tsx` | UI | Panel consolidating fleet-wide performance, error rates, and user session metrics. |
| `src/components/admin/tools/HardwareBlacklistPanel.tsx` | UI | Portal to restrict and manage blacklisted BLE peripheral MAC addresses. |
| `src/components/admin/tools/ProductManager.tsx` | UI | Catalog editor validating and upserting Neogleamz product models and segment default profiles. |
| `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx` | UI | Diagnostic wrapper orchestrating color testing, transitions, frame-builders, and the Protocol Oracle test harness. |
| `src/components/admin/tools/Sk8LytzProgrammer.tsx` | UI | Flashing tool providing command arrays for native over-the-air firmware updates and EEPROM modifications. |
| `src/components/admin/tools/UserManagementPanel.tsx` | UI | Administrative dashboard to search, review, ban, or unlock user profiles. |
| `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx` | UI | Multi-mode layout assembler visualizing byte annotations and allowing raw transmission. |
| `src/components/admin/tools/tabs/DiagnosticLabColorTab.tsx` | UI | Interface for running automated color routing tests (RGB vs. GRB) on active devices. |
| `src/components/admin/tools/tabs/DiagnosticLabConstants.ts` | Data | Static data maps defining lab categories, layout parameters, and opcodes. |
| `src/components/admin/tools/tabs/DiagnosticLabDevicesTab.tsx` | UI | Connection manager tracing physical BLE states and handling pairing workflows in the lab. |
| `src/components/admin/tools/tabs/DiagnosticLabHwBadge.tsx` | UI | Status component displaying connected device IC specifications and segment layouts. |
| `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx` | UI | Interactive test execution log certifying protocol opcodes and reporting to the coverage matrix. |
| `src/components/admin/tools/tabs/DiagnosticLabQuickColorGrid.tsx` | UI | Simple color palette selector dispatching commands instantly. |
| `src/components/admin/tools/tabs/DiagnosticLabSnifferTab.tsx` | UI | Live scrolling stream mapping RX/TX hex payloads with packet descriptions. |
| `src/components/admin/tools/tabs/DiagnosticLabStyles.ts` | Styling | Modular styling sheet for diagnostic panels and console screens. |
| `src/components/admin/tools/tabs/DiagnosticLabTransitionTab.tsx` | UI | UI controls to test fade, breath, flow, and strobe transition modes. |
| `src/components/admin/tools/tabs/DiagnosticLabTypes.ts` | Types | TypeScript interface definitions for lab statuses and test entries. |

---

## 2. Blast Radius

```
Imports (Inflow)                           ADMIN_&_TELEMETRY Domain                         Exports (Outflow)
┌──────────────────────────────────────┐   ┌────────────────────────────────────────────┐   ┌──────────────────────────────────────────────┐
│ - react, react-native, expo-device   │   │ - AppLogger.ts (Telemetry core)            │   │ - Globals: imported by virtually all hooks, │
│ - expo-battery                       │──&gt;│ - AppSettingsService.ts (Remote settings)  │──&gt;│   services, and screen files to report       │
│ - @react-native-async-storage        │   │ - useAdminTelemetry / useAdminSettings.ts  │   │   app events &amp; catch exceptions.             │
│ - @react-native-community/datetimeptr│   │ - useDiagnosticLog.ts                      │   │ - useBLE.ts &amp; BleMachine.ts: reports connection│
│ - react-native-ble-plx (types)       │   │ - AdminToolsModal.tsx                      │   │   events, queues, and recovery lifecycles.   │
│ - Supabase client (Client)           │   │ - Sk8LytzDiagnosticLab.tsx / Programmer.tsx│   │ - DashboardScreen.tsx: mounts AdminToolsModal│
│ - useProtocolDispatch.ts             │   └────────────────────────────────────────────┘   │   via a hidden 10-tap gesture.              │
└──────────────────────────────────────┘                                                    └──────────────────────────────────────────────┘
```

### Imports and Internal Dependencies
* **Sensors & Platform API**: Consumes `expo-device` and `expo-battery` to append device statistics, battery levels, memory specs, and operating system build identifiers to logs.
* **Storage & Sync**: Relies on `@react-native-async-storage/async-storage` for persistence, and Supabase client `src/services/supabaseClient.ts` for database uploads and remote settings fetches.
* **Date Manipulation**: Pulls `@react-native-community/datetimepicker` for spotlight visibility scheduling.
* **GATT & Dispatch**: Leverages the application's dispatch hooks (`useProtocolDispatch`, `useRegistration`) to run raw diagnostic commands.

### Exports & Consumers
* **Global Instrumentation**: `AppLogger` is the primary crash and analytics collection gateway. Every custom hook, BLE lifecycle listener, location service, and database mutation catch block calls `AppLogger.error()` or `AppLogger.log()` to record breadcrumbs and exception events.
* **Secure Access Modal**: `AdminToolsModal` acts as the master developer HUD, which is mounted at the root of `DashboardScreen.tsx` and protected by a passcode passcode input field (`0000` sequence).

---

## 3. Context Matrix

* **ThemeContext (`useTheme`)**: Consumed by the admin tools shell and sub-tabs (`AdminToolsModal`, `Sk8LytzDiagnosticLab`, `AdminPicksScheduler`, `AdminTab`, `DeviceTab`, `StatsTab`) to apply Neogleamz standard dark/light design parameters.
* **Safe Area Context (`useSafeAreaInsets`)**: Consumed by the modal views (`Sk8LytzDiagnosticLab`, `Sk8LytzProgrammer`, `AdminPicksScheduler`) to dynamically compute safe boundaries for notch viewports across iOS devices.
* **Singleton Services**: The domain does not expose a React Context provider; instead, it offers raw service hooks (`useAdminTelemetry`, `useAdminSettings`, `useDiagnosticLog`) which tap into the global `AppLogger` and `AppSettingsService` instances.

---

## 4. Hook/Service I/O Registry

### `AppLogger` (Service)
* **Inputs**:
  * `log(event: EventType, rawPayload?: Record<string, any>): Promise<void>`
  * `debug(message: string, context?: Record<string, any>): void`
  * `info(message: string, context?: Record<string, any>): void`
  * `warn(message: string, context?: unknown): void`
  * `error(message: string, errorObj?: unknown, context?: Record<string, unknown>): void`
  * `updateKnownDevices(devices: any[]): void`
  * `setLastTxPayload(hex: string): void`
* **Outputs**:
  * `getLogs(): Promise<LogEntry[]>` (returned in reverse-chronological order)
  * `getStats(): Promise<TelemetryStats>` (calculates storage footprint, app launch times, and mode usage statistics)
  * `exportJSON(): Promise<string>` (compiles full device logs and stats into a structured JSON string)
* **Side-Effects**:
  * Enforces a 500ms throttle limit on user interaction events (`COLOR_CHANGED`, `SPEED_CHANGED`, `BRIGHTNESS_CHANGED`).
  * Triggers a debounced local filesystem write (500ms debounce interval) to AsyncStorage.
  * Deterministically scrubs keys matching PII key lists (`email`, `password`, `token`, `latitude`, etc.) using recursive key traversal to protect skater privacy.
  * Dispatches critical diagnostics immediately to Supabase `telemetry_errors` and `crash_telemetry` tables (VIP Fast-Lane).
  * Uploads standard logs in batches of 500 records to `telemetry_snapshots` before trimming local arrays and clearing synced storage.

### `AppSettingsService` (Service)
* **Inputs**:
  * `updateSetting(key: AppSettingKey, value: unknown): Promise<boolean>`
* **Outputs**:
  * `fetchAllSettings(): Promise<AppSettingsMap>`
* **Side-Effects**:
  * Reads the local AsyncStorage cache for offline boot immediately, and triggers a background query to Supabase `sk8lytz_app_settings` to update local tables.
  * Commits upserts to remote database and updates storage caches upon calling `updateSetting`.

### `useAdminSettings` (Hook)
* **Inputs**:
  * `visible: boolean`
* **Outputs**:
  * `appSettings: AppSettingsMap`
  * `isLoading: boolean`
  * `loadSettings: () => Promise<void>`
  * `updateSetting: (key: string, value: AppSettingsValue) => Promise<void>`
* **Side-Effects**:
  * Loads remote configurations from `AppSettingsService` when `visible` transitions to true.
  * Optimistically updates state on modification and triggers an automatic rollback/reload sequence if the cloud update fails.

### `useAdminTelemetry` (Hook)
* **Inputs**:
  * `visible: boolean`
* **Outputs**:
  * `logs: LogEntry[]`
  * `stats: TelemetryStats | null`
  * `isUploading: boolean`
  * `load: () => Promise<void>`
  * `clearLogs: () => Promise<void>`
  * `uploadLogs: () => Promise<void>`
  * `exportLogs: () => Promise<void>`
* **Side-Effects**:
  * Fetches in-memory stats and timeline logs when active.
  * Triggers database deletions, logs uploads, and opens OS-level share drawers for logs exports.

### `useDiagnosticLog` (Hook)
* **Inputs**:
  * `{ visible, liveRxPayload, targetDeviceId }`
* **Outputs**:
  * `logs: BleLog[]` (stores the last 200 packets)
  * `lastSent: string`
  * `lastNote: string`
  * `testLog: TestLogEntry[]`
  * `coverage: Record<TrackedOpcode, OpcodeStatus>`
  * `transmit: (bytes: number[], note?: string, opcode?: string) => Promise<void>`
  * `sendRawHex: (hexStr: string, note?: string, opcode?: string) => Promise<void>`
  * `setVerdict: (entryId: string, opcode: string, verdict: TestVerdict) => void`
  * `setLastVerdict: (opcode: string, verdict: TestVerdict) => void`
  * `clearLogs: () => void`
  * `clearTestLog: () => void`
* **Side-Effects**:
  * Syncs and loads the diagnostic coverage registry from `@sk8lytz_diag_test_log` on instantiation.
  * Triggers raw Bluetooth writes to the target BLE device via the dispatch client.
  * Captures live notifications (`liveRxPayload`), processes EEPROM packets, and associates incoming packets with the active test verdict entries.

---

## 5. OS Variance Matrix

* **Android FlatList Optimizations**:
  * Android devices process the Admin Timeline with `removeClippedSubviews={Platform.OS === 'android'}` configured. This reduces native layout hierarchies during high-frequency log updates.
* **DatePicker Dialog Layout (iOS vs. Android)**:
  * Android automatically dismisses the calendar picker dialog as soon as a date value changes (`onDateChanged`). iOS requires mounting a custom visual overlay (`iosDatePickerOverlay`) containing a manual "Done" confirmation button.
* **Diagnostic Code Fonts**:
  * Diagnostic and sniffer panels select `fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace'` to align hexadecimal bytes inside grids.
* **Optical Simulation Mode (Web/Simulator Fallback)**:
  * `Sk8LytzProgrammer.tsx` branches `Platform.OS === 'web'` to enable simulator modes. This allows web builds to mock BLE operations and test log-correlation mechanics.
* **Haptic Feedback Gating**:
  * Telemetry-producing BLE hooks (such as `useOptimisticBLE.ts`) check `Platform.OS !== 'web'` to suppress haptic calls on non-native environments.

---

## 6. Sequence Diagrams

### standard Logging Pipeline
This flow represents standard UI and system events being throttled, correlated with BLE write queues, debounced, and synced to Supabase.

```mermaid
sequenceDiagram
    autonumber
    actor Hook as React Hook / Component
    participant AL as AppLogger Service
    participant IMB as InMemory Buffer
    participant AS as AsyncStorage
    participant SB as Supabase database
    participant FR as FlightRecorder

    Hook->>AL: log("SPEED_CHANGED", { value: 75 })
    Note over AL: Check high-frequency throttle<br/>(Last log > 500ms ago?)
    AL-->>Hook: Allow log (Proceed)
    
    AL->>AL: formatPayload(rawPayload)
    Note over AL: 1. Inject Device Context (RSSI, battery)<br/>2. Scrub PII keys (mac, lat/lng, emails)<br/>3. Deterministic FNV-1a scrubbing
    
    AL->>FR: leaveBreadcrumb("ACTION", "SPEED_CHANGED", cleanPayload)
    AL->>IMB: push(LogEntry)
    
    Note over AL: Debounced persist() triggers<br/>(500ms timeout)
    AL->>AS: setItem("@Sk8lytz_logs", JSON.stringify(buffer))
    
    Note over Hook: Developer triggers "Sync Logs"
    Hook->>AL: uploadLogsToSupabase()
    AL->>AS: getItem("@sk8lytz_app_settings")
    Note over AL: Confirm global_telemetry_enabled === true
    
    AL->>SB: Get authenticated user details (.auth.getUser())
    
    loop Chunked upload (CHUNK_SIZE = 500)
        AL->>SB: Insert into table "telemetry_snapshots"
        SB-->>AL: Confirm batch insert success
    end
    
    AL->>IMB: slice(successfulCount) (Trims uploaded delta)
    AL->>AS: setItem("@Sk8lytz_logs", JSON.stringify(buffer))
```

### VIP Error Fast-Lane Pipeline
This diagram displays critical crashes or protocol errors bypassing local queue buffers to dispatch dual writes to Supabase instantly.

```mermaid
sequenceDiagram
    autonumber
    actor SRE as SRE / System Exception
    participant AL as AppLogger Service
    participant IMB as InMemory Buffer
    participant AS as AsyncStorage
    participant SB as Supabase database
    participant FR as FlightRecorder

    SRE->>AL: log("ERROR_CAUGHT", { error: "GATT 133 Connection Timeout" })
    AL->>AL: formatPayload(rawPayload)
    Note over AL: Obfuscate PII & extract context
    AL->>FR: leaveBreadcrumb("ERROR", "ERROR_CAUGHT", cleanPayload)
    
    par Dual Direct Uploads (VIP Fast-Lane)
        AL->>SB: Insert details into table "telemetry_errors"
        AL->>SB: Insert error + breadcrumb trail into table "crash_telemetry"
    end
    
    Note over AL: Concurrently persist locally to prevent data loss if offline
    AL->>IMB: push(LogEntry)
    AL->>AS: setItem("@Sk8lytz_logs", JSON.stringify(buffer))
    
    SB-->>AL: Confirm VIP insertions (non-blocking)
```

---

## 7. AppLogger Telemetry Pipeline Detail

1. **Event Capture & Throttling**:
   Events represent React UI interactions, BLE connection states, and hardware transmissions. High-frequency events (`BRIGHTNESS_CHANGED`, `SPEED_CHANGED`, `COLOR_CHANGED`) are throttled to a maximum rate of 1 write per 500ms.
2. **PII Obfuscation**:
   Log entries pass through `formatPayload` to sanitize private credentials. Strings associated with PII keys (including `password`, `token`, `email`, `lat`/`lng` coordinates, and `mac` addresses) are replaced with `[REDACTED]`. The algorithm uses a JavaScript `WeakSet` to traverse object trees, preventing circular reference lockups.
3. **Queue Correlation**:
   To diagnose packet delivery, hardware commands (such as brightness adjustments) are delayed in a `pendingLogQueue` for up to 100ms. If a BLE write dispatch (`txPayloadQueue`) occurs within 250ms of the command, the raw hex payload and write status are injected directly into the user event log entry before it is saved.
4. **Debounced Local Persistence**:
   Writing to local disk (AsyncStorage key `@Sk8lytz_app_settings_logger` / `@sk8lytz_logs`) is deferred via a 500ms timer. This batches multiple rapid log statements into a single file save, protecting mobile write operations from performance bottlenecks.
5. **VIP Fast-Lane Insertion**:
   Crash logs (`ERROR_CAUGHT`, `PROTOCOL_ERROR`, `BLE_WRITE_ERROR`, `BLE_CONNECTION_ERROR`, `CREW_ERROR`) bypass the standard local queues. The logger initiates immediate, parallel inserts to Supabase tables `telemetry_errors` and `crash_telemetry`, appending the current `FlightRecorder` breadcrumb trail.
6. **Supabase Ingestion (Sync)**:
   When syncing, the logger checks the `@sk8lytz_app_settings` cache to verify that telemetry has not been disabled. If settings are enabled, logs are compiled, associated with the active Supabase user ID, and uploaded to `telemetry_snapshots` in chunks of 500 rows. If telemetry has been disabled, the local buffer is wiped immediately.
7. **Log Clearing**:
   Invoking `clearLogs()` purges the local database cache, resets active device lists, and calls Supabase storage buckets to delete remote JSON logs matching the device's host or BLE hardware identifiers.

---

## 8. Architectural Impact Flags

* **`[IMPACTS_USER_JOURNEY]`**: The Admin Modal is protected by a hidden 10-tap gesture and a passcode. The date pickers and color diagnostic selectors within the modal adapt dynamically to Android and iOS layout constraints.
* **`[IMPACTS_C4_CONTEXT]`**: The logging pipeline writes to six remote PostgreSQL tables inside Supabase (`telemetry_snapshots`, `telemetry_errors`, `crash_telemetry`, etc.) and performs operations on Supabase's `sk8lytz-logs` object storage bucket.
* **`[IMPACTS_STATE_CHART]`**: The diagnostics suite incorporates the Protocol Oracle, which monitors active opcodes (`0x41`, `0x42`, `0x43`, `0x51`, `0x59`, `0x63`, `0x74`, etc.) and records pass/fail verdicts to verify hardware integrity.

<!-- CARTOGRAPHER_END: ADMIN_&_TELEMETRY -->

### Domain: DEPENDENCY_AUDIT
<!-- CARTOGRAPHER_START: DEPENDENCY_AUDIT -->

# Architectural Cartography: DEPENDENCY_AUDIT

This document provides a comprehensive architectural map of the **DEPENDENCY_AUDIT** domain, analyzing the configurations, scripts, and runtime implications of dependencies defined in the core manifests.

---

## 1. File Manifest

| File | Architectural Purpose |
|:---|:---|
| [`package.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json) | Main workspace package manifest detailing the dependency tree, build scripts, development tools, overrides, and packages. |
| [`package-lock.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package-lock.json) | Detailed dependency lock file ensuring exact sub-dependency version resolution and hash validation across developer machines. |
| [`modules/sk8lytz-watch-bridge/package.json`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/package.json) | Manifest for the local watchOS/WearOS bridging module, identifying its peer Expo dependencies. |
| [`plugins/withWearOsModule.js`](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/plugins/withWearOsModule.js) | Custom Expo configuration plugin that automates native Android project modifications (settings.gradle, build.gradle, AndroidManifest.xml) for Wear OS companion apps and Notifee foreground service types. |

---

## 2. Blast Radius

The packages defined in the dependency manifests dictate the entire execution surface of the application. Below is the mapping of imported packages (external modules) and what they export/affect in the codebase (internal blast radius):

### Imported Modules (Core Dependencies)
* **Core React & Expo Engine** (`react`, `react-native`, `expo`, `@expo/metro-runtime`, `react-native-web`): Forms the foundation of the hybrid runtime and UI views on native mobile platforms and fallback web views.
* **BLE Transport Pipeline** (`react-native-ble-plx` & `@config-plugins/react-native-ble-plx`): The central hardware interface. Integrates the native Bluetooth stacks and provides the low-level GATT socket commands wrapped by `ZenggeProtocol.ts`, `BleWriteQueue.ts`, and `BleMachine.ts`.
* **State Chart FSM Engine** (`xstate`, `@xstate/react`): Orchestrates state lifecycles. Powering the multi-state BLE connection flow (`IDLE → SCANNING → CONNECTING → READY → RECOVERING → DISCONNECTING`) in `BleMachine.ts`, ensuring race conditions and duplicate scans are structurally prevented.
* **Background Notifications & Foreground Service HUD** (`@notifee/react-native` & `expo-notifications`): Manages active background notification alerts and foreground service hooks. Configures Android foreground services (`location|health|connectedDevice|shortService|dataSync`) enabling background BLE scanning and fitness telemetry tracking.
* **High-Performance JSI Bridges** (`react-native-nitro-modules`, `react-native-nitro-image`, `react-native-worklets`, `react-native-vision-camera`, `react-native-vision-camera-resizer`): Direct C++ JSI bindings for high-frequency operations. Powers the camera color-sampler (`CameraTracker.tsx`) by executing 5Hz GPU frame-scaling and client-side K-Means palette extraction entirely in JSI worklet threads.
* **Database Client Pipeline** (`@supabase/supabase-js`): Connects the local app to Supabase tables (`registered_devices`, `skate_spots`, `skate_sessions`). Powering real-time Crew Hub subscriptions, profile updates, and offline sync spooling.
* **Fitness Telemetry Interfaces** (`react-native-health` & `react-native-health-connect`): Captures skater health telemetry metrics (heart rate, active calories, workout duration) to display on-wrist and store in session snapshots.
* **Local Companion Native Bridge** (`sk8lytz-watch-bridge`): Coordinates continuous session data replication and remote session start/stop signals with Wear OS and watchOS companions.

### Exported Targets (Scripts and Tooling Hooks)
* **`npm run verify`**: Combines architectural blast radius checks (`blast-radius-scanner.js`) with compilation and automated test gates (`verifiable-check-runner.js`).
* **`patch-package`**: Automatically injects local postinstall fixes into the `node_modules/` directory during dependency installation.
* **Husky Git hooks**: Enforces pre-commit linting checks and Husky `npm run verify` gates before pushing.

---

## 3. Context Matrix

The application's context architecture coordinates dependency status and session states across component layers:

```
                  ┌──────────────────────────────────────────┐
                  │               AuthProvider               │
                  │  (Supabase client auth state & profile)  │
                  └────────────────────┬─────────────────────┘
                                       │
                  ┌────────────────────▼─────────────────────┐
                  │              SessionProvider             │
                  │  (Syncs telemetry and watchOS/WearOS via  │
                  │       sk8lytz-watch-bridge module)       │
                  └────────────────────┬─────────────────────┘
                                       │
                  ┌────────────────────▼─────────────────────┐
                  │              BleMachineProvider          │
                  │ (XState BLE client FSM, write dispatcher)│
                  └──────────────────────────────────────────┘
```

* **`AuthContext`** (Provides: user profile data, credentials validation, and `isOfflineMode` state derived from `@supabase/supabase-js` auth sessions).
* **`SessionContext`** (Provides: active skating session duration, GPS distance, peak speed, and continuous health telemetry. Integrates with `sk8lytz-watch-bridge` to relay active state to the watch companions).
* **`BleMachineContext`** (Provides: the centralized XState `BleMachine` context, exposing connected device arrays, scanning indicators, GATT mutex status, and auto-recovery state).
* **`ThemeContext`** (Provides: strict FSM theme states `themeMode: 'dark' | 'light'` with backward-compatible boolean getters).

---

## 4. Hook/Service I/O Registry

The following table documents the input, output, and side-effects of the primary APIs introduced by core external and local dependencies:

| API / Hook | Input Parameters | Output Signature | Side-Effects & Core Behavior |
|:---|:---|:---|:---|
| **`useMachine`**<br>(from `@xstate/react`) | `BleMachine: StateMachine`, `options?` | `[state, send, actorRef]` | Instantiates the core BLE state engine; spawns XState actors (`ConnectService`, `RecoveryService`, `HeartbeatService`) to serialize radio writes and connects. |
| **`BleManager`**<br>(from `react-native-ble-plx`) | `restoreStateIdentifier?: string`, `restoreStateCallback?: function` | Singleton API interface | Interacts with native iOS CoreBluetooth and Android BluetoothAdapter stacks; opens and destroys physical GATT connection handles. |
| **`useHealthTelemetry`**<br>(local wrapper using health APIs) | None | `{ heartRate: number, calories: number, duration: number, isGranted: boolean }` | Triggers OS system permissions; subscribes to workout sessions on iOS (HealthKit) and queries health datastores on Android (Health Connect). |
| **`WatchBridge`**<br>(from local module bridge) | Event listener callbacks, message payloads | Native event emitters and message senders | Initializes `WCSession` (iOS) or Google Play Services `DataClient` (Android) to establish bidirectional sockets with watch companion apps. |
| **`useOfflineSyncWorker`**<br>(local database sync coordinator) | None | `{ queueLength: number, isFlushing: boolean, flushQueue: function }` | Reads locally spooled sessions from `AsyncStorage`; performs authenticated Postgres bulk uploads via `@supabase/supabase-js`; removes successfully synced cache records. |

---

## 5. OS Variance Matrix

Due to the heavy utilization of native Bluetooth, health tracking, and companion APIs, the dependency behavior branches significantly between iOS and Android:

### iOS Specific Paths
* **Health Tracking**: Utilizes the `react-native-health` dependency linked to the native Apple HealthKit framework.
  * **Info.plist Keys**: Requires `NSHealthShareUsageDescription` and `NSHealthUpdateUsageDescription` keys.
  * **Entitlements**: Requires `com.apple.developer.healthkit` and `com.apple.developer.healthkit.background-delivery` entitlements.
* **Watch Companion**: Integrates with watchOS via Apple's native `WCSession` (WatchConnectivity framework) bridged inside `modules/sk8lytz-watch-bridge`. iOS companion compilation is managed via `@bacons/apple-targets`.
* **Bluetooth Gating**: Requires `NSBluetoothAlwaysUsageDescription` key in the `Info.plist`.

### Android Specific Paths
* **Health Tracking**: Utilizes the `react-native-health-connect` dependency linked to Android's Google Health Connect API.
  * **Permissions**: Requires `android.permission.ACTIVITY_RECOGNITION` and `android.permission.FOREGROUND_SERVICE_HEALTH` in the Manifest.
* **Watch Companion**: Bundles the `sk8lytzWear` Kotlin Compose project into the final APK. The custom Expo plugin `plugins/withWearOsModule.js` injects compile bindings (`include ':sk8lytzWear'`) inside `settings.gradle` and bundles it via `wearApp project(':sk8lytzWear')` inside the app's `build.gradle`.
* **Background Notifications**: Notifee foreground services types are explicitly compiled in `AndroidManifest.xml` as `location|health|connectedDevice|shortService|dataSync` to prevent Android OS process kills during active background sessions.
* **Bluetooth Permissions**: Requires explicit permissions: `android.permission.BLUETOOTH`, `android.permission.BLUETOOTH_ADMIN`, `android.permission.BLUETOOTH_CONNECT`, `android.permission.BLUETOOTH_SCAN`.
* **Proguard Obfuscation Rules**: Configured via `expo-build-properties` to keep native structures intact and prevent reflection failures:
  ```proguard
  -keep class com.polidea.reactnativeble.** { *; }
  -keep class com.polidea.rxandroidble2.** { *; }
  -keep class com.mrousavy.camera.** { *; }
  -keep class com.mrousavy.nitro.** { *; }
  ```

---

## 6. App Startup Dependency & Service Bootstrap Pipeline

The diagram below details the sequential lifecycle bootstrap flow of core native dependencies and services on application launch:

```mermaid
sequenceDiagram
    autonumber
    participant App as App.tsx (JS Root)
    participant Auth as AuthProvider (Supabase)
    participant BleM as BleMachine (XState FSM)
    participant Bridge as WatchBridge (Native Bridge)
    participant Health as Health Telemetry APIs
    participant OS as Native OS (iOS / Android)

    App->>Auth: Initialize Supabase Client
    Auth-->>App: Auth session hydration completed (isOfflineMode resolved)
    
    App->>Bridge: Mount WatchBridge & register Session listeners
    Bridge->>OS: Initialize WCSession / Google DataClient Socket
    OS-->>Bridge: Native companion transport channel ready
    
    App->>BleM: Boot BleMachine (Transition from null to IDLE state)
    BleM->>OS: Initialize react-native-ble-plx (BleManager instance created)
    
    alt User has active session cached
        App->>Health: Trigger useHealthTelemetry synchronization
        Health->>OS: Query iOS HealthKit / Android Health Connect data stores
        OS-->>Health: Returns raw telemetry (HR, active calories)
        Health-->>App: State hydrations mapped to SessionContext
    end

    Note over App,OS: App Bootstrap Sequence Completed. Dashboard Interface rendered.
```

---

## 7. Cited Truth
* **Root Manifest Dependencies**: `package.json:L17-70` defines core runtime versions (`react: 19.2.6`, `react-native: 0.83.2`, `expo: ~55.0.8`, `xstate: ^5.32.0`, `react-native-ble-plx: ^3.5.1`, `@supabase/supabase-js: ^2.100.0`).
* **Android/iOS Build Configs**: `app.config.js:L15-88` documents specific plist strings, Android permissions, Proguard configurations, and the `withWearOsModule` Gradle plugin injection mapping.
* **Custom Config Plugin**: `plugins/withWearOsModule.js:L15-79` details the specific settings.gradle and app/build.gradle line modifications that compile the Wear OS APK companion.
* **Architectural Dependency Rules**: `tools/ARCH_DEPENDENCY_MAP.json:L59-62` binds `modules/sk8lytz-watch-bridge/src/` changes with updates in `useHealthTelemetry.ts` and `SessionContext.tsx`.

---

No architectural surface changes were introduced in this audit. Therefore, no `[IMPACTS_USER_JOURNEY]`, `[IMPACTS_C4_CONTEXT]`, or `[IMPACTS_STATE_CHART]` flags are appended.

<!-- CARTOGRAPHER_END: DEPENDENCY_AUDIT -->

## 13. 🪦 The Graveyard

### Cartographer Graveyard Deposits (2026-06-10T18:04:46.108Z)

### Cartographer Graveyard Deposits (2026-06-11T05:28:24.447Z)
- **[BLE_CORE]**: * **§3.8.3 "Dashboard UI Layout (4-Slab Architecture)"**: `` — Replaced by newer fluid tabs and setup steps.
- **[BLE_CORE]**: * **§3.8.5 "One-Screen Setup Policy"**: `` — Setup has migrated to a Multi-Step Wizard component.
- **[BLE_CORE]**: * **§3.10.3 "writeChunked — 0x51 Extended Payload Framing"**: `` — Re-architected to delegate framing logic strictly to `ZenggeProtocol` in compliance with R-19.
- **[GROUP_SYNC]**: * "Automatic `_MM/DD` suffix enforced in `CrewModal.handleCreate` " (line 1116)
- **[GROUP_SYNC]**: * **Section 12.2 Auto-Compiled Domain Architecture / Domain: GROUP_SYNC** (line 1464-1466) — The section is empty and stale references on lines 1564/1579-1648/2145-2157 describing legacy offline queues should be marked with ``.
- **[UI_SCREENS]**: - **[UI_SCREENS]**: - `### Dashboard UI Layout (4-Slab Architecture) `
- **[UI_SCREENS]**: - **[UI_SCREENS]**: - `### UI Design patterns & Branding` -> `One-Screen Setup Policy `
- **[UI_SCREENS]**: - **[UI_SCREENS]**: - `### writeChunked — 0x51 Extended Payload Framing `
- **[UI_MODALS]**: ## 9. Archival Ledger (``)
- **[UI_MODALS]**: - **Dashboard UI Layout (4-Slab Architecture)**: Tagged with `` on lines 308 and 1553. The current dashboard uses tabbed nested controllers rather than a single vertical 4-slab layout.
- **[UI_MODALS]**: - **One-Screen Setup Policy**: Tagged with `` on lines 321 and 1554. Multi-device onboarding steps are split across a dedicated modal wizard rather than squeezed into a single scroll-disabled layout.
- **[UI_MODALS]**: - **AccountModal Domain stale registers**: Stale hooks (`useDeviceFleet`) tagged with `` on line 958. Device registrations are handled by shared state properties passed down from the root layout instead of independent Supabase triggers inside the modal.
- **[UTILS]**: Visualizer: `src/utils/RbmSimulator.ts` (pixel-perfect frame generation).  (Note: RbmSimulator has been deleted/refactored, visualizer frame generation has been migrated to protocols/SymphonyEngine.ts)
- **[UTILS]**: Visualizer: `src/utils/RbmSimulator.ts` → `getRbmMusicFrame()`.  (Note: migrated to protocols/SymphonyEngine.ts -> getMusicVisualizerFrame)
- **[SESSION_TRACKING]**: | `src/hooks/useSessionTracking.ts` | **``** - *Legacy File (Deleted)*. Stale hook that previously drove session FSM logic; its functionality has been completely integrated into `SessionContext.tsx`, `useGlobalTelemetry.ts`, and `SpeedTrackingService.ts`. |
- **[PROTOCOL_CORE]**: - **Verdict**: ``
- **[PROTOCOL_CORE]**: - **Verdict**: ``
- **[PATTERN_ENGINE]**: * *Status*: ``
- **[PATTERN_ENGINE]**: * *Status*: ``
- **[CLOUD_FUNCTIONS]**: ### ``
- **[CLOUD_FUNCTIONS]**: * **Stale Text**: `| useSessionTracking (stale) | DockedController |  - Session FSM (IDLE → RECORDING → SUMMARY), duration, distance, peak speed, session summary modal |`
- **[CLOUD_FUNCTIONS]**: * **Stale Text**: `| useDeviceFleet | AccountModal | registered_devices Supabase fetch, fleet display list  |`
- **[CLOUD_FUNCTIONS]**: * **Stale Text**: `| useProtocolBuilder | Sk8LytzDiagnosticLab |  - Stale owner Sk8LytzProgrammerModal replaced. FSM-based payload generation for 0x51, 0x59, 0x62, 0x63, 0x73 |`
- **[THEME_&_ASSETS]**: * `| @sk8lytz_theme | ThemeContext | ...` `` $\rightarrow$ update to `@Sk8lytz_ThemeMode`
- **[THEME_&_ASSETS]**: * `| @sk8lytz_control_theme | ThemeContext | ...` `` $\rightarrow$ update to `@Sk8lytz_ControlUITheme`
- **[BUILD_CONFIG]**: - **Section 11.7 Future Watch Enhancements (Planned)**: Tagged with ``. Features like the "Session Duration Timer" and "watchOS Complications" are fully shipped and configured in `expo-target.config.js`.
- **[BUILD_CONFIG]**: - **Section 2.3 Dashboard UI Layout (4-Slab Architecture)**: Tagged with ``. The UI has transitioned away from the strict 4-slab layout, making this paragraph stale.
- **[BUILD_CONFIG]**: - **Section 2.4 One-Screen Setup Policy**: Tagged with ``. Superseded by setup refactoring.
- **[BUILD_CONFIG]**: - **Section 2.5 writeChunked - 0x51 Extended Payload Framing**: Tagged with ``. Standard 0x51 writes are performed via compact 9B structures.
- **[OS_PERMISSIONS]**: ### ``

- **[BUILD_CONFIG]**: **[MOVE_TO_ARCHIVE]**
- **[CLOUD_FUNCTIONS]**: 1435: ### 12.1 Identity & Auth [MOVE_TO_ARCHIVE]
- **[DEPENDENCY_AUDIT]**: Any documentation in `tools/SK8Lytz_App_Master_Reference.md` referencing legacy pure-JS image processing (via `jpeg-js`) or older state management libraries superseded by `xstate` should be archived.
- **[IDENTITY]**: - Documentation pertaining to Auth/Profile systems inside `tools/SK8Lytz_App_Master_Reference.md` has been reviewed. Current implementations reflect modern domain architectures (like offline-skip mechanisms). Older references to user state may require `[MOVE_TO_ARCHIVE]` tagging if monolithic `useAuth` refs surface.
- **[NATIVE_&_WATCH]**: I reviewed `tools/SK8Lytz_App_Master_Reference.md` but the file was heavily truncated before Section 11 (Wearable Companion Architecture) could be fully analyzed. If there are any stale payload constants or obsolete bridging logic in Section 11, please append `[MOVE_TO_ARCHIVE]` to those sections.
- **[NOTIFICATIONS_&_ROUTING]**: **[MOVE_TO_ARCHIVE]**: The existing master reference documentation for `NOTIFICATIONS & ROUTING` located at line 2559 of `tools/SK8Lytz_App_Master_Reference.md` is considered stale and has been tagged for archiving by this cartography pass.
- **[OS_PERMISSIONS]**: - **`tools/SK8Lytz_App_Master_Reference.md`**: Section `9.1 Legal Hardening (The Compliance Shield)` documents the `OS_PERMISSIONS` domain. As per archival instructions, this should be tagged with `[MOVE_TO_ARCHIVE]` as this cartography report supersedes its technical documentation.
- **[PATTERN_ENGINE]**: If `tools/SK8Lytz_App_Master_Reference.md` contains stale documentation regarding Pattern Engine intercept mappings or legacy firmware bindings, tag those sections with `[MOVE_TO_ARCHIVE]`.
- **[SESSION_TRACKING]**: **[MOVE_TO_ARCHIVE]**: The file `tools/SK8Lytz_App_Master_Reference.md` contains stale documentation asserting that `useSessionTracking.ts` is an active hook (e.g., line 4589 explicitly states session logic was refactored into `useSessionTracking.ts`). Because `useSessionTracking.ts` has been deleted entirely, all references to it in the Master Reference must be immediately tagged and archived.
- **[SIMULATION_&_MOCKS]**: * **Stale Documentation**: The following sections in `tools/SK8Lytz_App_Master_Reference.md` contain stale documentation for this domain and must be tagged with `[MOVE_TO_ARCHIVE]`:
- **[THEME_&_ASSETS]**: - `### Dashboard UI Layout (4-Slab Architecture) [MOVE_TO_ARCHIVE]`
- **[THEME_&_ASSETS]**: - `- **One-Screen Setup Policy** [MOVE_TO_ARCHIVE]`
- **[UI_DOCKED_CONTROLLER]**: **[MOVE_TO_ARCHIVE] Tags identified for `tools/SK8Lytz_App_Master_Reference.md`:**
- **[UI_MODALS]**: > **[MOVE_TO_ARCHIVE]**
- **[UI_SCREENS]**: - `### Dashboard UI Layout (4-Slab Architecture) [MOVE_TO_ARCHIVE]`
- **[UI_SCREENS]**: - `### UI Design patterns & Branding` -> `One-Screen Setup Policy [MOVE_TO_ARCHIVE]`
- **[UI_SCREENS]**: - `### writeChunked — 0x51 Extended Payload Framing [MOVE_TO_ARCHIVE]`
- **[UI_VISUALIZER]**: > No significantly stale documentation regarding the `UI_VISUALIZER` domain was identified in the active `SK8Lytz_App_Master_Reference.md`. The documentation sections for `VisualizerUnit` and `CameraTracker` accurately match the current codebase implementation.

### Batch 2026-06-07T04:05:25.387Z
- **Domain: IDENTITY**: The `SK8Lytz_App_Master_Reference.md` likely contains stale references describing `ProfileService` as a monolithic God-object. These references must be archived and updated to reflect the "Meal 1: ProfileService split" where it is strictly a barrel re-export facade over `AuthProfileService`, `CrewProfileService`, and `PushTokenService`.
- **Domain: GROUP_SYNC**: Stale `GROUP_SYNC` architecture documentation discovered in `tools/SK8Lytz_App_Master_Reference.md` (specifically lines 1579-1648 and 2145-2157 regarding older offline sync mechanisms and references). This should be reconciled with the newly analyzed file architecture.
- **Domain: DATA_LAYER**: This file does not exist. The architecture is natively hook-driven (`useGlobalTelemetry`, `useAdminTelemetry`). Documentation referencing it is stale.
- **Domain: NATIVE_&_WATCH**: - Master Reference Section 12.6 NATIVE_&_WATCH (Stale: Missing Wear OS & Bridge)
- **Domain: NOTIFICATIONS_&_ROUTING**: The legacy documentation for NOTIFICATIONS_&_ROUTING found in `tools/SK8Lytz_App_Master_Reference.md` is stale and should be archived. It incorrectly lists `PushTokenService` as part of this exact directory group and misses the state-based routing elements like `App.tsx`, `BluetoothGuard`, and `ComplianceGate`.
- **Domain: HARDWARE_PROTOCOLS**: The Master Reference sections referencing `0x41 Settled Mode`, `0x42 RBM Programs Mode`, and `0x43` Multi-Sequence Mode should be flagged. The protocol codebase has explicitly marked them as `@deprecated Since v2.8.0` or `@HARDWARE-DANGER` due to state machine crashes and testing limitations, being fully superseded by `0x51` Pattern Engine and `0x59` Spatial routines.
- **Domain: THEME_&_ASSETS**: The "Dashboard UI Layout (4-Slab Architecture)" and "UI Design Patterns & Branding" sections located in `SK8Lytz_App_Master_Reference.md` are tagged as stale documentation drift and should be archived or fully relocated to `DashboardStyles.ts` and `theme.ts`.
- **Domain: SIMULATION_&_MOCKS**: The existing documentation for this domain in `SK8Lytz_App_Master_Reference.md` is stale and should be archived.
- **Domain: DEPENDENCY_AUDIT**: Any documentation in `SK8Lytz_App_Master_Reference.md` referencing legacy pure-JS image processing (via `jpeg-js`) or older state management libraries superseded by `xstate` should be archived.
- **Domain: DEPENDENCY_AUDIT**: Any legacy documentation concerning Web fallbacks for BLE (Optical Simulation Mode for Expo Web). Remove any lingering workflow references or offline caches regarding @react-native-voice/voice.
- **Domain: OS_PERMISSIONS**: OS Sync: `syncSystemPermissions()` runs on boot/foreground to reconcile the ledger with native OS settings. (This contradicts the actual implementation in PermissionService.ts, where aggressive sweeping was deprecated).
- **Domain: NATIVE_&_WATCH**: Stale Reference: Master Reference Section 11.7 Future Watch Enhancements (Planned) lists "Session Duration Timer" and "watchOS Complications" as planned features. Both are fully shipped and active.
- **Domain: THEME_&_ASSETS**: Mention of "Master Reference §2 — FTUE Threshold Classification" in ProductCatalog.ts. "Dashboard UI Layout (4-Slab Architecture)" and "UI Design Patterns & Branding" located in SK8Lytz_App_Master_Reference.md.
- **Domain: HARDWARE_PROTOCOLS**: The entry in the "Condemned Opcodes" table: `0x41` Settled Mode (Symphony Effects). Cartographer Audit Reality: PatternEngine.ts explicitly intercepts test pattern IDs 201-233 and fires them via ZenggeProtocol.setCustomModeExtendedCompact() (which is a 0x51 opcode pipeline). The Master Reference directly contradicts itself later on line 398 warning against 0x41 usage, confirming the table row is stale legacy text.
- **Domain: SESSION_TRACKING**: Section 7 (Session Telemetry Architecture) contains a stale skate_sessions schema missing fields like avg_bpm, peak_gforce, crew_session_id, and has no documentation of the PENDING_SESSION_QUEUE_KEY offline fallback architecture.

### Hook Registry Updates
- useWebDemoConsoleBridge: Web Demo specific hook to pipe console logs to Command Center.

### 🚨 SDE Autonomous Fuzzer Discoveries (Auto-Documented)
- **Opcode**: `0x59` (Static Colorful)
- **Constraint**: Array sizes between 2 and 9 elements cause physical EEPROM buffer lockout on the `0xA3` chipset.
- **Rule**: Minimum safe payload length is 12 RGB pixels. (See Rule: Surgical Buffer Overflow Defense in agent-behavior.md).

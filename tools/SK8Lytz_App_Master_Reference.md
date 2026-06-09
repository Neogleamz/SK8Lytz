# SK8Lytz App Master Reference

_Last Updated: 2026-06-03 | **Wearable Companion Architecture SHIPPED** — watchOS + Wear OS companion apps, Expo native bridge module (sk8lytz-watch-bridge), watch-preferred health priority system, bidirectional phoneâ†”watch session sync, Speed push to watch, VS-002 gitignore fix. v3.8.2 | Source of Truth: modules/sk8lytz-watch-bridge/src/index.ts, src/hooks/useHealthTelemetry.ts, src/context/SessionContext.tsx_

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

### Dashboard UI Layout (4-Slab Architecture)

The primary dashboard uses a **Vertical Slab (No-Scroll)** layout to maximize glanceability and touch accuracy.

1. **Slab 1: Dynamic Header**: Logo, user profile, and active polling/telemetry indicator.
2. **Slab 2: Crew Hub**: Active session discovery and quick-join pills.
3. **Slab 3: My Skates / Groups**: High-impact cards for grouped hardware with global power controls.
4. **Slab 4: Hardware Fleet**: List of all registered devices with a "TAP TO ADD" quick-access wizard link.

### UI Design Patterns & Branding

- **Tucked-in Attribution**: Credit links (e.g., "by neogleamz.com") must be placed discreetly within header containers, aligned with the visual boundary of the primary logo (e.g., `marginRight: '16%'` for a 300px logo) and using `fontSize: 9` with `fontWeight: '800'` muted text.
- **Fluid Component Scaling**: Components (Builders, Camera Viewers) must NOT use hardcoded heights. They must utilize available `flex` space between the `ProductVisualizer` and the bottom dock to ensure responsiveness across all aspect ratios.
- **One-Screen Setup Policy**: The Hardware Setup Wizard must minimize vertical occupancy. For naming and registration (Step 3), all primary controls (Fleet Name, Device Labels, Type, Position) must be visible on a single standard mobile viewport (e.g. iPhone SE) without requiring a vertical scroll for a standard 2-skate setup. Use horizontal inlining and 8pt grid proximity instead of explicit labels where possible.

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
#### ðŸ“ Auto-Compiled Zengge Protocol Constants (AST Compiler)

##### ðŸ”Œ BLE UUIDs
- **Service UUID**: `0000ffff-0000-1000-8000-00805f9b34fb` (`ZENGGE_SERVICE_UUID`)
- **Write Characteristic UUID**: `0000ff01-0000-1000-8000-00805f9b34fb` (`ZENGGE_CHARACTERISTIC_UUID`)
- **Notification Characteristic UUID**: `0000ff02-0000-1000-8000-00805f9b34fb` (`ZENGGE_NOTIFY_UUID`)

##### ðŸ› ï¸ Hardware Constraints
| Constraint | Value | Description |
|:---|:---:|:---|
| `maxPoints` | 300 | Maximum addressable points per segment |
| `maxSegments` | 2048 | Maximum physical segment duplicates |
| `maxPxS` | 2048 | Max points * segments limit |
| `maxMicPoints` | 150 | Maximum points when microphone is active |
| `maxMicPxS` | 960 | Max micPoints * micSegments limit |
| `defaultPoints` | 30 | Fallback default point count |
| `defaultSegments` | 10 | Fallback default segment count |

##### ðŸ“Ÿ IC Chip Types (`IC_TYPES`)
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

##### ðŸŽ¨ Color Sorting RGB (`COLOR_SORTING_RGB`)
| Key | RGB Order |
|:---:|:---|
| 0 | RGB |
| 1 | RBG |
| 2 | GRB |
| 3 | GBR |
| 4 | BRG |
| 5 | BGR |

<!-- AST_COMPILER_END: ZENGGE_CONSTANTS -->

### writeChunked — 0x51 Extended Payload Framing

Required for 323-byte 0x51 Extended Scene Builder payloads (32 steps Ã— 10B + 3B header).

- **Function**: `useBLE.writeChunked(payload: number[], chunkSize = 20): Promise<void>`
- **Framing**: `[0x40, seqByte, 0x00, 0x00, 0x01, 0x43, 0xBD, 0x0B, ...data]`
- **12 bytes data per 20-byte BLE chunk** (8-byte header overhead)
- **20ms inter-chunk delay** — prevents BLE TX buffer overflow on Android
- **âš ï¸ Framing signature `[0x01, 0x43, 0xBD, 0x0B]` needs Oracle Lab HCI sniff** before wiring to production Scene Builder UI
- Exported in `BluetoothLowEnergyApi` interface (commit `fdc0ff3`)

### BLE Stability Constraints & GATT Error Prevention

> [!CAUTION]
> React Native BLE PLX and the Android native `BluetoothAdapter` suffer from extreme race conditions. To avoid GATT 133 exceptions, UI freezes, and buffer overflows, all logic must follow these architectural constraints:

1. **Global Connection Gate (`bleGateRef`):** A `BleStateMachine` FSM ref with phases `IDLE | SCANNING | CONNECTING | DISCONNECTING | RECOVERING`. ALL BLE operations must check/acquire the gate before touching the radio. Only one operation class at a time. The gate auto-syncs to React state via `addListener` for re-renders.
2. **GATT Mutex with 4-Tier Priority (`useBLEGattMutex`):** Fine-grained GATT operation serialization. Priority tiers: `P1_CRITICAL` (power, user writes), `P2_RECOVERY` (auto-reconnect — preempts lower tiers via AbortController), `P3_INTERROGATION` (EEPROM probes), `P4_MAINTENANCE` (heartbeat, RSSI polls). Higher priority requests abort in-progress lower-priority locks. 15s deadlock watchdog auto-releases orphaned locks and logs telemetry.
3. **The GATT 133 Exponential Backoff:** `connectToDevice` is wrapped in a 3-attempt retry loop with exponential delays `[500ms, 1500ms, 4000ms]` + `refreshGatt: 'OnConnected'` on each retry to silently absorb Android RF congestion. _(Previously: 2-attempt, flat 200ms delay.)_
4. **Connection Priority Downgrade after Handshake:** On Android, `requestConnectionPriority(HIGH)` fires immediately on connect for fast MTU/handshake. After the first successful write, priority is downgraded to `BALANCED` — saves 2—3Ã— battery on fire-and-forget traffic. _(Previously: stayed at HIGH permanently.)_
5. **Pre-Lock Gate Check:** `connectToDevices` now checks `bleGateRef !== IDLE` _before_ acquiring the GATT lock. If the gate is already busy (scanning, recovering), the connect attempt is skipped immediately instead of blocking in an 8s polling loop.
6. **Lean Connection Loops:** `connectToDevices` strictly establishes MTU (request 512 bytes) and notification pipes. Do NOT execute 600ms latency buffers, firmware loads, or 0x63 hardware settings queries during the connection stack.
7. **50ms Inter-Device Write Gap:** All multi-device group writes in `BleWriteDispatcher` enforce a 50ms pause between per-device GATT writes. Prevents silent GATT drops on Qualcomm Snapdragon 665/675 and MediaTek Helio chipsets. _(Previously: 20ms — insufficient for budget chipsets.)_
8. **Priority FIFO Write Queue (`BleWriteQueue.ts`):** All BLE writes are serialized through a priority queue with backpressure. Critical writes (power, time sync) bypass the debounce. Pattern writes are deduplicated by generation counter. Queue depth is capped to prevent memory pressure.
9. **Parallel Writes and Teardowns (`Promise.all`):** Group-wide commands (sliders) and teardowns (`cancelDeviceConnection`) MUST be wrapped in `Promise.all` loops to eliminate staggered latency.

### The Transport Wrapper (`wrapCommand`)

Every inner protocol payload must be wrapped using the standard 8-byte Zengge V2 framing:
`[0x00, SequenceNum, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]`

### Auto-Recovery System (3-Phase, Gate-Coordinated)

_Refactored: 2026-06-05 | Lives in: `src/hooks/ble/useBLEAutoRecovery.ts`_

The **Auto-Recovery** system monitors GATT-connected devices for organic disconnects and stale-link heartbeat failures. When a device drops, recovery automatically attempts reconnection through a 3-phase escalation strategy with gate coordination.

#### 3-Phase Recovery Architecture

| Phase | Name | Duration | Backoff | GATT Lock | Behavior |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Phase 1** | Aggressive | 0—2 min | `1500ms Ã— 1.5^attempt` + jitter(0—1500ms), capped 30s | Acquires `P2_RECOVERY` | Rapid reconnect. Best chance of success while device is nearby. |
| **Phase 2** | Moderate | 2—10 min | Same formula, longer natural gaps | Acquires `P2_RECOVERY` | Reduced frequency. Device may have moved out of range temporarily. |
| **Phase 3** | Passive | 10 min+ | **No active polling** | **No GATT lock** | Zero-cost watch mode. Delegates to Sweeper — if the device reappears in scan results, recovery is re-initiated from Phase 1. |

#### Group Dropout Coordinator

_Lives in: `useBLEAutoRecovery.ts` → `onGroupDropout` callback_

When 2+ devices disconnect within a 1.5s debounce window (common when a user powers off both skates), the coordinator batches them into a single `connectToDevices([...devices])` call instead of spawning N competing recovery loops. Eliminates the "stampeding herd" race condition that caused cascading GATT 133 errors.

#### Recovery Properties

| Property | Value |
| :--- | :--- |
| **Trigger** | Organic `onDisconnected` event from BLE PLX, OR `useBLEHeartbeat` stale-link detection |
| **Gate coordination** | Uses GATT mutex `P2_RECOVERY` priority — preempts interrogation/maintenance, yields to critical writes |
| **Retry backoff** | `1500ms Ã— 1.5^attempt` + random jitter `[0, 1500ms]`, ceiling 30s |
| **Cancellation** | AbortController-style token — incrementing counter instantly breaks all active loops |
| **Ghosting** | Failed recovery (all phases exhausted) adds device to `ghostedDeviceIds` — UI dims card, `writeToDevice` skips it |
| **Auto-Recovery Summary** | `AUTO_RECOVERY_SUMMARY` telemetry event with lifetime success rate, avg recovery time, and per-phase stats aggregated per device |

**Telemetry Events:**
- `AUTO_RECOVERY_STARTED` — emitted when recovery loop begins for a device
- `AUTO_RECOVERY_SUCCESS` — device reconnected and services restored
- `AUTO_RECOVERY_FAILED` — all phases exhausted, device is now ghosted
- `AUTO_RECOVERY_CANCELLED` — recovery loop cancelled (user-initiated disconnect)
- `AUTO_RECOVERY_GATE_WAIT` — recovery attempt skipped because gate is busy
- `AUTO_RECOVERY_SUMMARY` — per-device aggregate telemetry (success rate, avg time, phase breakdown)

> [!NOTE]
> **History:** The legacy `useBLEWatchdog.ts` (flat 30s polling + silentRelatch) was deleted 2026-04-17 because it caused GATT collisions during active user writes. The new `useBLEHeartbeat` (added 2026-06-06) solves the same problem correctly — it uses the GATT mutex at `P4_MAINTENANCE` priority, sends a lightweight 0x63 query, and only fires every 45s. Stale links detected by heartbeat are routed to `autoRecovery.initiateRecovery(mac)`.

### Connection Health Heartbeat

_Added: 2026-06-06 | Lives in: `src/hooks/ble/useBLEHeartbeat.ts`_

Pings every connected device every 45s via a 0x63 EEPROM query to detect stale GATT handles early. Samsung Galaxy A-series can hold stale handles alive for minutes after the physical device powers off — without heartbeat, the stale link is only discovered on the next user write.

| Property | Value |
| :--- | :--- |
| **Interval** | 45s (`HEARTBEAT_INTERVAL_MS`) |
| **Probe** | `0x63` hardware query via adapter — same query used by EEPROM interrogation |
| **Fallback** | If adapter doesn't support `0x63` (BanlanX), falls back to `readRSSIForDevice` |
| **On failure** | Drops device from `connectedDevices`, immediately calls `autoRecovery.initiateRecovery(mac)` |
| **GATT priority** | `P4_MAINTENANCE` — yields to all user writes, recovery, and interrogation |
| **Testability** | `pingConnectedDevice()` exported as pure async fn — tested without React context |

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
| **Badge component** | `ConnectionStrengthBadge` — 3-bar signal icon using pure View rectangles (no SVG). 4-tier colour: green (â‰¥-60), amber (-60 to -75), orange (-75 to -82), red (<-82). Hidden when rssi is null. |
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
| **MTU Platform Guard** | `BleConnectionManager.ts` | `requestMTU()` wrapped in `Platform.OS === 'android'` block — iOS negotiates MTU automatically during GATT connection. Calling `requestMTU` on iOS throws. On iOS, `conn.mtu` is read directly (typed `number` in `react-native-ble-plx`). |
| **UUID Filter in `startDeviceScan`** | `useBLEBatterySweep.ts/useBLEInterrogator.ts` | `startDeviceScan(null, ...)` replaced with `startDeviceScan([ZENGGE_SERVICE_UUID], ...)`. Enables iOS background scanning mode (CBCentralManager requires a service UUID filter when `allowDuplicates: false`). Also reduces Android scan noise. |

### Android Platform Guards

_Added: 2026-06-05 (AND-02, AND-03, AND-04)_

| Guard | File | Fix |
| :--- | :--- | :--- |
| **Connection Priority Downgrade** | `BleConnectionManager.ts` | After handshake, `requestConnectionPriority(BALANCED)` fired to save 2—3Ã— battery. Only on Android (iOS manages its own priority). |
| **50ms Inter-Device Write Gap** | `BleWriteDispatcher.ts` | Increased from 20ms → 50ms. Fixes silent GATT drops on Qualcomm Snapdragon 665/675 and MediaTek Helio chipsets. |
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
Visualizer: `src/utils/RbmSimulator.ts` (pixel-perfect frame generation).
Protocol: `0x42` (`setCustomRbm`) or `0x61` (legacy APK path — same pattern table).

#### Music Mode Patterns (46 Profiles)

Source of truth: `src/utils/MusicDictionary.ts` — music-reactive patterns keyed to protocol IDs depending on Mode Type (Light Bar = 16, Light Screen = 30).
Visualizer: `src/utils/RbmSimulator.ts` → `getRbmMusicFrame()`.
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
> **DDA Refactor Shipped: 2026-04-14** — The architecture was refactored from a monolithic component model to a Hook-First domain model. **BLE Engine Refactor: 2026-06-05** — `useBLE.ts` decomposed into 6 domain sub-hooks (`useBLEScanner`, `useBLEBatterySweep + useBLEInterrogator`, `useBLEAutoRecovery`, `useBLEGattMutex`, `useBLEHeartbeat`, `useBLERSSIMonitor`) + 6 extracted services (`BleConnectionManager`, `BleWriteDispatcher`, `BleWriteQueue`, `BleLifecycleManager`, `BlePingService`, `BleStateMachine`). `useBLE.ts` is now a thin orchestrator (~600 lines).

To ensure scalability and maintain UI performance, the SK8Lytz app enforces a **Hook-First** architecture. Complex business logic, hardware protocols, and Supabase data fetching must be extracted from UI components into decoupled domain hooks. UI components must focus strictly on rendering.

---

### âš¡ Critical Architectural Constraint: BLE Co-location

> [!CAUTION]
> **BLE state (`connectedDevices`, `writeToDevice`, `setOnDataReceived`) MUST remain co-located in `DashboardScreen.tsx`.** Do NOT move these into any domain hook. The BLE lifecycle manager is a singleton with hardware-level race conditions (GATT 133). Distributing it across multiple hook contexts would create multiple competing subscribers which cause silent write failures and GATT exceptions. All domain hooks receive BLE context via **prop injection** only.

---

### ðŸ—ºï¸ Complete Hook & Service Registry

#### BLE Engine Domain (`src/hooks/ble/`, `src/services/`)

_All BLE sub-hooks are orchestrated by `useBLE.ts` (the thin orchestrator). They are NEVER consumed directly by UI components._

| Hook / Service | File | Owns |
| :--- | :--- | :--- |
| `useBLEScanner` | `src/hooks/ble/useBLEScanner.ts` | Peripheral discovery, RSSI proximity gating, pending registrations |
| `useBLEBatterySweep + useBLEInterrogator` | `src/hooks/ble/useBLEBatterySweep.ts/useBLEInterrogator.ts` | Silent background LowPower scan, Interrogator Queue, `hwCache`, 3-tier battery-adaptive throttling (BAT-01) |
| `useBLEAutoRecovery` | `src/hooks/ble/useBLEAutoRecovery.ts` | 3-phase reconnect (Aggressive/Moderate/Passive), group dropout coordinator, `ghostedDeviceIds`, per-device telemetry aggregation |
| `useBLEGattMutex` | `src/hooks/ble/useBLEGattMutex.ts` | 4-tier GATT operation serialization (P1—P4), 15s deadlock watchdog, AbortController preemption |
| `useBLEHeartbeat` | `src/hooks/ble/useBLEHeartbeat.ts` | 45s connection health ping via 0x63 query, stale-link detection → recovery (MISS-03) |
| `useBLERSSIMonitor` | `src/hooks/ble/useBLERSSIMonitor.ts` | 30s post-connect RSSI polling, `rssiMap`, proactive reconnect at -82 dBm (BAT-02) |
| `BleStateMachine` | `src/services/BleStateMachine.ts` | FSM gate (`IDLEâ”‚SCANNINGâ”‚CONNECTINGâ”‚DISCONNECTINGâ”‚RECOVERING`), listener-based React state sync |
| `BleConnectionManager` | `src/services/BleConnectionManager.ts` | GATT connect flow: MTU negotiation, adapter resolution, notification wiring, priority downgrade |
| `BleWriteDispatcher` | `src/services/BleWriteDispatcher.ts` | Serialized group writes with 50ms inter-device gap, debounce, generation counter |
| `BleWriteQueue` | `src/services/BleWriteQueue.ts` | Priority FIFO write queue with backpressure (WRITE-01) |
| `BleLifecycleManager` | `src/services/BleLifecycleManager.ts` | Keepalive timer (60s), `realDisconnect`, `forceDisconnect` |
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
| `useSessionTracking`       | `DockedController` | Session FSM (`IDLE → RECORDING → SUMMARY`), duration, distance, peak speed, session summary modal                                     |
| `useMusicMode`             | `DockedController` | Owns 0x73 music config dispatch, pattern names, pattern navigation.                                                                   |
| `useCuratedPicks`          | `DockedController` | Fetches and caches SK8Lytz Picks (curated presets) from Supabase.                                                                     |
| `useAppMicrophone`         | `DockedController` | Manages the expo-av Audio.Recording lifecycle for APP MIC mode. Streams normalized magnitude (0—1).                                   |
| `useControllerAnalytics`   | `DockedController` | Debounced telemetry logging for mode, pattern, color, brightness, speed changes.                                                      |

#### AccountModal Domain (`src/hooks/`)

| Hook                 | Consumer       | Owns                                                            |
| :------------------- | :------------- | :-------------------------------------------------------------- |
| `useAccountOverview` | `AccountModal` | Supabase profile read/write, avatar upload, display name update |
| `useSkateStats`      | `AccountModal` | Aggregate session stats fetch, totals calculation               |
| `useDeviceFleet`     | `AccountModal` | `registered_devices` Supabase fetch, fleet display list         |

#### Admin Domain (`src/hooks/`)

| Hook                 | Consumer                 | Owns                                                                     |
| :------------------- | :----------------------- | :----------------------------------------------------------------------- |
| `useDiagnosticLog`   | `Sk8LytzDiagnosticLab`   | BLE RX/TX log buffer, `targetDeviceId` targeting, raw hex transmission   |
| `useProtocolBuilder` | `Sk8LytzProgrammerModal` | FSM-based payload generation for `0x51`, `0x59`, `0x62`, `0x63`, `0x73`  |
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

Automatic `_MM/DD` suffix enforced in `CrewModal.handleCreate`.

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
- Session summary (`useSessionTracking`)
- Crew Hub session telemetry
- Street mode (G-force: phone accelerometer only — watch does NOT relay G-force)
- `skate_sessions` Supabase writes

> [!IMPORTANT]
> **GPS Speed and G-Force remain phone-only.** The watch does NOT relay GPS or accelerometer data to save battery. Speed comes from `expo-location` on the phone. G-force comes from `expo-sensors` on the phone. The phone pushes speed TO the watch for display only.

---

### 11.7 Future Watch Enhancements (Planned)

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

### 12.1 Identity & Auth
<!-- CARTOGRAPHER_START: IDENTITY -->

[🕵️ Scout — Reyes | Cartographer Analysis | IDENTITY-domain | warm]

<!-- CARTOGRAPHER_START: IDENTITY -->

## 1. File Manifest
- **`src/context/AuthContext.tsx`**: Centralized authentication state provider managing Supabase sessions, deep links, and offline mode gating.
  - **`AuthContextValue` API** (exported via `useAuth()`):
    - State: `session`, `user`, `isOfflineMode`, `isAuthenticated`, `sessionLoaded`, `sessionExpired`
    - Setters: `setIsOfflineMode(value)`, `clearOfflineMode()`
    - **Auth Actions (added fix/auth-context-bypass @ `1fc96fb9`)**: `signIn(email, password)`, `signUp(email, password, options?)`, `resetPassword(email, redirectTo?)`, `signOut()`
  - **Rule**: All UI components and hooks MUST call these context methods. Direct `supabase.auth.*` calls in the UI layer are forbidden. Zero bypasses confirmed post-merge.

- **`src/services/AuthProfileService.ts`**: Handles user profile CRUD, auto-creation/self-healing on missing metadata, and fetching session history.
- **`src/services/CrewProfileService.ts`**: Manages permanent crew lifecycle (CRUD), multi-owner member management, search, and session statistics.
- **`src/services/ProfileService.ts`**: Barrel re-export facade preserving the legacy monolithic API surface while delegating to specialized Auth, Crew, and PushToken services.
- **`src/services/ProfileService.types.ts`**: Shared type contracts (e.g., `UserProfile`, `PermanentCrew`) to prevent circular dependencies between decoupled profile services.
- **`src/components/account/AccountTabCrewz.tsx`**: UI tab for users to create, join (via invite code), manage, or leave permanent crews.
- **`src/components/account/AccountTabDevices.tsx`**: UI tab displaying registered hardware fleet, grouping, firmware/hardware stats, and renaming/forgetting devices.
- **`src/components/account/AccountTabProfile.tsx`**: UI tab for updating user display name, username, avatar color hue, and profile photo.
- **`src/components/account/AccountTabSecurity.tsx`**: UI tab for updating email, password, and granular privacy permissions.
- **`src/components/account/AccountTabSettings.tsx`**: UI tab for global app preferences including push notifications, dark mode, auto-pause, health sync, and account deletion.
- **`src/components/account/AccountTabStats.tsx`**: UI tab providing a high-level summa
<truncated 3290 bytes>
OS` checks** exist within the domain files. The domain largely relies on platform-agnostic React Native abstractions.


## 7. Sequence Diagram
### AuthContext Cold-Start Initialization Flow
```mermaid
sequenceDiagram
    participant App
    participant AuthContext
    participant SecureStore
    participant AsyncStorage
    participant Supabase Auth
    
    App->>AuthContext: Mounts AuthProvider
    AuthContext->>SecureStore: migrateAuthTokensToSecureStore()
    AuthContext->>AsyncStorage: getItem(STORAGE_OFFLINE_SKIP)
    
    alt is offlineSkip == 'true'
        AsyncStorage-->>AuthContext: 'true'
        AuthContext->>AuthContext: setIsOfflineMode(true)
        AuthContext->>AuthContext: setSessionLoaded(true)
    else is normal auth
        AsyncStorage-->>AuthContext: null
        AuthContext->>Supabase Auth: getSession()
        
        alt session exists
            Supabase Auth-->>AuthContext: Session Data
            AuthContext->>AuthContext: setSession(existing)
        else no session
            Supabase Auth-->>AuthContext: null
            AuthContext->>AsyncStorage: getItem('@Sk8lytz_auth_last_email')
            
            alt last email exists
                AsyncStorage-->>AuthContext: 'user@email.com'
                AuthContext->>AuthContext: setSessionExpired(true)
            else first time user
                AsyncStorage-->>AuthContext: null
            end
        end
        AuthContext->>AuthContext: setSessionLoaded(true)
    end
```

<!-- CARTOGRAPHER_END: IDENTITY -->

<!-- CARTOGRAPHER_END: IDENTITY -->

### 12.2 BLE Protocol Core
<!-- CARTOGRAPHER_START: BLE_CORE -->

[🕵️ Scout — Reyes | research | BLE_CORE cartography | cold]

I have completed the structural audit of the `BLE_CORE` domain. Here is the requested markdown payload ready for injection into the `<!-- CARTOGRAPHER_START: BLE_CORE -->` block.

### 🗂️ File Manifest

*   **`src/services/ble/BleMachine.ts`**: XState definitions outlining the 6 global BLE operational phases (IDLE, SCANNING, CONNECTING, READY, DISCONNECTING, RECOVERING).
*   **`src/services/ble/BleMachine.types.ts`**: TypeScript strict types and event interfaces guarding the BleMachine FSM transitions.
*   **`src/hooks/useBLE.ts`**: The thin orchestrator hook that exposes the unified `BluetoothLowEnergyApi` by composing all underlying `ble/` sub-hooks and dispatching to services.
*   **`src/hooks/ble/useBLEAutoRecovery.ts`**: A 3-phase exponential-backoff engine mapping out recovery logic for organic GATT disconnects, with a group-dropout debounce coordinator.
*   **`src/hooks/ble/useBLEGattMutex.ts`**: A robust module-level async mutex (4-tier priority map) serializing all GATT radio traffic to prevent Android `GATT 133` race conditions.
*   **`src/hooks/ble/useBLEHeartbeat.ts`**: Connection health monitor that pings active devices every 45s (via `0x63` EEPROM query or RSSI) to sniff out and flush stale Android handles.
*   **`src/hooks/ble/useBLERSSIMonitor.ts`**: Post-connect signal quality scanner polling device RSSI every 30s to trigger live UI badge metrics and proactive channel reconnections.
*   **`src/hooks/ble/useBLEScanner.ts`**: Legacy peripheral discovery loop providing RSSI proximity gating and populating pending registrations for the FTUE Setup Wizard.
*   **`src/hooks/ble/useBLEBatterySweep.ts/useBLEInterrogator.ts`**: An always-on, battery-adaptive background listener that 
<truncated 4153 bytes>
)
    participant Q as Interrogator Queue
    participant M as GATT Mutex
    participant BM as BleManager
    participant DB as AsyncStorage

    S->>BM: startDeviceScan([ZENGGE, BANLANX])
    BM-->>S: Unseen Device Discovered (MAC_X)
    
    S->>Q: probeQueue.push(MAC_X)
    Q->>Q: Wait 2000ms (PROBE_QUEUE_DELAY_MS)
    
    Q->>M: acquireGattLock(P3_INTERROGATION)
    M-->>Q: Lock Granted (signal, release)
    
    Q->>BM: createGattSession(MAC_X)
    BM-->>Q: Connected + Adapter Resolved
    
    Q->>BM: monitorCharacteristicForDevice()
    Q->>BM: writeCharacteristicWithoutResponse (0x63 HW Query)
    Q->>BM: writeCharacteristicWithoutResponse (RF Query)
    
    BM-->>Q: Notifications Received (EEPROM State)
    
    Q->>DB: setItem(@sk8_hw_MAC_X, hwConfig)
    Q->>S: setHwCache(hwConfig)
    
    Q->>BM: cancelDeviceConnection(MAC_X)
    Q->>M: release()
```

### ⚙️ State Machine (FSM) Map: BleMachine.ts

```mermaid
stateDiagram-v2
    [*] --> IDLE
    
    IDLE --> SCANNING : SCAN_START
    IDLE --> CONNECTING : CONNECT_REQUEST
    IDLE --> DISCONNECTING : DISCONNECT_REQUEST
    IDLE --> RECOVERING : RECOVERY_START
    
    SCANNING --> IDLE : SCAN_STOP
    SCANNING --> CONNECTING : CONNECT_REQUEST
    SCANNING --> DISCONNECTING : DISCONNECT_REQUEST
    
    CONNECTING --> READY : CONNECT_SUCCESS
    CONNECTING --> IDLE : CONNECT_FAIL
    CONNECTING --> RECOVERING : RECOVERY_START
    CONNECTING --> DISCONNECTING : DISCONNECT_REQUEST
    
    READY --> DISCONNECTING : DISCONNECT_REQUEST
    READY --> RECOVERING : RECOVERY_START
    READY --> IDLE : UPDATE_CONNECTED_DEVICES [devices.length === 0]
    
    DISCONNECTING --> IDLE : DISCONNECT_COMPLETE
    
    RECOVERING --> READY : RECOVERY_COMPLETE [devices.length > 0]
    RECOVERING --> IDLE : RECOVERY_COMPLETE [devices.length === 0]
    RECOVERING --> CONNECTING : CONNECT_REQUEST
    RECOVERING --> IDLE : RECOVERY_FAIL

    note right of IDLE
      Global Escape Hatch:
      FORCE_IDLE transitions ANY state to IDLE
    end note
```

<!-- CARTOGRAPHER_END: BLE_CORE -->

### 12.3 Group Sync & Swarm
<!-- CARTOGRAPHER_START: GROUP_SYNC -->

Here is the SDE Cartographer Node markdown payload for the `GROUP_SYNC` domain.

<!-- CARTOGRAPHER_START: GROUP_SYNC -->

# 🗺️ Domain Architecture: GROUP_SYNC & CREW HUB

## 1. File Manifest
*   **`src/services/GroupRepository.ts`**: The single source of truth for custom group persistence, handling offline-first local state and atomic Supabase RPC synchronization.
*   **`src/services/GroupSyncService.ts`**: **[MISSING]** — Does not exist in the file system (Functionality is handled internally by `GroupRepository` and `CrewService`).
*   **`src/context/CrewContext.tsx`**: The primary React Context provider orchestrating UI state, form fields, and modal steps for the Crew Hub lifecycle.
*   **`src/components/crew/CrewLandingScreen.tsx`**: The main entry point for the Crew Hub, listing user crews, active nearby sessions, and dispatching quick-join flows.
*   **`src/components/crew/CrewControllerScreen.tsx`**: The active session HUD, providing real-time telemetry, leader handoff toggles, and session termination controls.
*   **`src/components/crew/CrewDetailScreen.tsx`**: Administrative view for a specific crew showing statistics, member rosters, and edit functionality.
*   **`src/components/crew/CrewManageScreen.tsx`**: The creation and configuration wizard for establishing new permanent crews and defining their visibility.
*   **`src/components/crew/CrewCreateScreen.tsx`**: Interface for initiating a live session from a selected crew.
*   **`src/components/crew/CrewJoinScreen.tsx`**: Specialized interface for discovering and entering active live sessions or permanent crews.
*   **`src/components/crew/CrewLandingMap.tsx`**: Native map implementation rendering geographic markers for nearby sessions and spots.
*   **`src/components/
<truncated 1630 bytes>
abase `upsert_group_with_devices` RPC.
*   **`useCrewContext` (via `useCrewHub`, `useCrewManage`, `useCrewSession`)**:
    *   **Inputs**: User input (Invite Codes, Radius filters, Crew metadata).
    *   **Outputs**: UI State (loading flags, error messages, active step, member lists).
    *   **Side-Effects**: BLE device updates via session synchronization, triggers map rerenders.

## 5. OS Variance Matrix
*   **Typography**: Invite codes in `CrewLandingScreen` and `CrewManageScreen` use `Platform.OS === 'ios' ? 'Menlo' : 'monospace'` to ensure cross-platform monospaced alignment.
*   **Map Implementation**: Geographic features explicitly branch at the file-resolver level (`CrewLandingMap.tsx` vs `CrewLandingMap.web.tsx`) to support Expo Web compilation without crashing on native map dependencies.


## 7. Sequence Diagram
```mermaid
sequenceDiagram
    participant UI as CrewManageScreen
    participant Repo as GroupRepository
    participant Storage as AsyncStorage
    participant Delegate as DeviceRepository
    participant DB as Supabase Cloud

    UI->>Repo: saveGroupTransactional(groupId, name, macs)
    Repo->>Storage: Optimistic Save (@Sk8lytz_custom_groups)
    Repo->>Delegate: updateDevicesInBulk(updatedDevices)
    Delegate-->>Repo: Acknowledge Cache Update
    Repo->>Delegate: notifySubscribers()
    
    alt Network Available & No Pending Syncs
        Repo->>DB: rpc('upsert_group_with_devices')
        DB-->>Repo: Success
    else Offline or Pending Devices
        Repo->>Storage: Queue Sync (@Sk8lytz_pending_group_sync)
        Storage-->>Repo: Queued
    end
    Repo-->>UI: return success(true)
```
<!-- CARTOGRAPHER_START: GROUP_SYNC -->

<!-- CARTOGRAPHER_END: GROUP_SYNC -->

### 12.4 Control Surfaces
<!-- CARTOGRAPHER_START: UI_CONTROLS -->

[🕵️ Scout — Reyes | Cartographer | UI_CONTROLS | cold]

Here is the Elite Architecture Markdown Payload for the `UI_CONTROLS` domain.

<!-- CARTOGRAPHER_START: UI_CONTROLS -->
# 🗺️ UI_CONTROLS Domain Architecture

## 1. File Manifest
- `src/screens/AuthScreen.tsx`: Manages user authentication, magic links, offline mode toggling, and UI states for login/signup/forgot password.
- `src/screens/DashboardScreen.tsx`: The monolithic root application screen orchestrating BLE lifecycle, fleet management, session context, and routing hardware notifications.
- `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`: Multi-step FTUE orchestrator for discovering, blinking, claiming, and configuring physical skate hardware.
- `src/screens/Onboarding/PermissionsOnboardingScreen.tsx`: Explains and auto-requests essential permissions (BLE, Location, Camera) before allowing entry to the app.
- `src/components/DockedController.tsx`: The primary routing shell for LED control, managing the mode FSM, BLE write bus, and delegating sub-panel rendering.

## 2. Blast Radius
- **Imports (Consumes)**:
  - **Contexts**: `ThemeContext`, `BLEContext`, `SessionContext`, `FavoritesContext`
  - **Domain Hooks**: `useRegistration`, `useDashboardProfile`, `useDashboardGroups`, `useDashboardAutoConnect`, `useDeviceStateLedger`, `useProtocolDispatch`, `useDockedControllerState`, `useStreetMode`, `useControllerAnalytics`, `useSharedFavorites`, `useOptimisticBLE`, etc.
  - **Protocols/Utils**: `ZenggeProtocol`, `PatternEngine`, `ColorUtils`, `NamingUtils`, `ProductCatalog`, `AppLogger`
  - **Sub-components**: `DockedDock`, `ProEffectsPanel`, `BuilderPanel`, `MusicPanel`, `AuthHeader`, `DeviceItem`, `LiveTelemetryHUD`
- **Exports (Consumed By)**: 
  - `AuthScreen
<truncated 2377 bytes>
rdScreen.tsx`**: `KeyboardAvoidingView` behavior is `'padding'` on iOS, `undefined` on Android. Uses `Platform.OS === 'ios' ? 0 : 12` for footer padding.

## 6. Complex Sequence Flow: Hardware Setup Wizard (Atomic Ping)
```mermaid
sequenceDiagram
    participant User
    participant Wizard as HardwareSetupWizardScreen
    participant BLE as pingDevice (BLEContext)
    participant HW as Skate Hardware

    User->>Wizard: Taps "BLINK" on device card
    Wizard->>BLE: pingDevice(mac, blinkPayload)
    BLE->>HW: Connect to GATT
    BLE->>HW: Write Blink Payload (0x59 Green)
    BLE->>HW: Write Probe Request (0x63)
    HW-->>BLE: Return EEPROM Config (points, segments, ic)
    BLE->>HW: Write Off Command
    BLE->>HW: Disconnect GATT
    BLE-->>Wizard: Return hwConfig
    Wizard->>Wizard: Update PendingRegistration state with probed points
    Wizard-->>User: Visual feedback (Blink completed)
```

## 7. Design System & Token Manifest
- **Spacings & Layouts**: Uses `Spacing.md`, `Spacing.lg`, `Spacing.xl` extensively. Relies on `Layout.borderRadius`.
- **Typographic Tokens**: Titles use `fontSize: 24-32`, `fontWeight: '900'`. Accents use `letterSpacing: 1-1.5`. Relies heavily on `Typography.title`.
- **Colors**: Leverages `Colors.primary` (`#00f0ff` natively) for CTAs. Destructive/Errors use `#ff4444`. Surface elements rely on `Colors.surface` and `Colors.surfaceHighlight`. Muted text uses `Colors.textMuted`.
- **Iconography**: `MaterialCommunityIcons` is the standard throughout.

---
### 🗄️ Archival Notice
I identified stale documentation in `tools/SK8Lytz_App_Master_Reference.md` regarding the `useFavorites` hook implementation. I have mapped it for archiving.

**Stale Doc found in Master Reference:**
`| useFavorites

<!-- CARTOGRAPHER_END: UI_CONTROLS -->

### 12.5 Data & Telemetry
<!-- CARTOGRAPHER_START: DATA_LAYER -->

🎯 PM — Jordan | Data & Telemetry Mapping | DATA_LAYER | cold

Here is the finalized Cartographer payload for the `DATA_LAYER` domain.


## 1. File Manifest
*   **`src/services/DeviceRepository.ts`**: Single Source of Truth (SSOT) for Device & Group Persistence. Manages AsyncStorage (local-first) and Supabase (cloud-second), tombstone pattern to prevent resurrection, offline pending queues, and local-wins config merging.
*   **`src/services/TelemetryService.ts`**: Utility class for BLE error context extraction. Extracts standard context (payload size, operation type) and forces GATT status 133 extraction.
*   **`src/types/supabase.ts`**: Autogenerated TypeScript definitions for the PostgREST schema, typing tables like `registered_devices`, `registered_groups`, `admin_audit_logs`, `telemetry_snapshots`, and junction tables.
*   **`src/services/supabaseClient.ts`**: Initializes the Supabase client mapped to the `Database` type using `expo-secure-store` for token persistence and provides an offline fallback mock if env vars are missing or if the client operates offline.

## 2. Blast Radius
*   **`DeviceRepository.ts`**: Edits affect all hardware bindings, fleet persistence, React state syncing, offline queueing, and Supabase RPCs (`upsert_group_with_devices`). Returning mutated ar
<truncated 2952 bytes>
ilter cloudRows against local tombstones
    DeviceRepository->>DeviceRepository: Local-first smart merge with local devices
    DeviceRepository->>DeviceRepository: Append offline-only local devices
    DeviceRepository->>AsyncStorage: setItem(DEVICES_KEY, finalDevices)
    DeviceRepository->>App: Notify listeners
    DeviceRepository->>AsyncStorage: getItem(PENDING_KEY)
    AsyncStorage-->>DeviceRepository: pending sync queue
    loop For each pending device
        DeviceRepository->>Supabase: from('registered_groups').upsert()
        DeviceRepository->>Supabase: from('registered_devices').upsert()
    end
    DeviceRepository->>AsyncStorage: removeItem(PENDING_KEY)
    DeviceRepository->>Supabase: Flush pending tombstones (delete from 'registered_devices')
    DeviceRepository-->>App: finalDevices
```

## 7. Database Schema & RLS Policies
*   **Schema Map**:
    *   `registered_devices`: Primary device table mapped by `user_id` and `device_mac`. Stores hardware configs (`led_points`, `segments`, `strip_type`, `sorting`).
    *   `registered_groups`: Fleet management mapped by `id` and `user_id`.
    *   `device_group_members`: Join table for device-to-group relation.
    *   `telemetry_snapshots`: Unified event ingestion mapped by `id`, `user_id`, `device_id`, `event_type`, and `metadata` (JSONB).
*   **Security & RLS Rules** (Inferred & Enforced by Client Architecture):
    *   **User Isolation**: All DML operations strictly filter by `user_id = auth.uid()` natively via Supabase RLS. The repository mirrors this logic locally (e.g., `eq('user_id', user.id)`).
    *   **Atomic Upserts**: Groups and Devices are upserted transactionally via the RPC `upsert_group_with_devices` to prevent orphaned devices during RLS checks.

## 8. Environment/Secrets Manifest
*   `EXPO_PUBLIC_SUPABASE_URL` *(Required)*: The endpoint URL for the Supabase project.
*   `EXPO_PUBLIC_SUPABASE_ANON_KEY` *(Required)*: The public anonymous API key for client-side Supabase requests.

<!-- CARTOGRAPHER_END: DATA_LAYER -->

<!-- CARTOGRAPHER_END: DATA_LAYER -->

### 12.6 Utilities & Types
<!-- CARTOGRAPHER_START: UTILS -->

[🕵️ Scout — Reyes | Cartographer Analysis | UTILS | cold]

Here is the SDE Cartographer Payload for the **Utilities & Types** domain. 

> [!NOTE]
> `
## 1. File Manifest
**`src/utils/` (Pure Utilities & Parsers)**
- `BlePayloadParser.ts`: Protocol Gatekeeper. Safely parses ZENGGE hardware V1/V2 settings and RF payload byte arrays.
- `ColorUtils.ts`: Pure color conversion math (hex/hue/RGB). Owns the critical `boostForLED` algorithm that maximizes HSV saturation for WS2812B vibrancy.
- `presetColorUtils.ts`: Shared utility for rendering UI gradients and glow colors for Preset/Group cards (determines `GENERATIVE` rainbow vs `FG_ONLY` solid color).
- `MusicDictionary.ts`: Authoritative registry defining the 46 hardware-native music profiles across Light Bar (0x26) and Light Screen (0x27) matrix types.
- `classifyBLEDevice.ts`: Core mapping logic translating raw BLE `Device` data into a structured `PendingRegistration`, employing EEPROM hwCache and catalog fallbacks.
- `NamingUtils.ts`: Deterministic fallback generator for device and group names to prevent UI drift (`getDefaultGroupName`, `getDefaultDeviceName`).
- `NormalizationUtils.ts`: Hardware constraint normalizer (maps UI 0-100 speed into hardware 1-31 boundaries).
- `kMeansPalette.ts`: JSI-optimized K-Means clustering algorithm for extracting dominant palettes from ambient RGB frame data.
- `migrateAuthTokens.ts`: Migration script migratin
<truncated 3607 bytes>
| Handled via empty stubs/shims in `react-native-worklets.web.js`. |
| Parsing Math | Pure JS number/bitwise ops (Consistent execution). | Pure JS number/bitwise ops (Consistent execution). | Pure JS execution. |

## 6. Complex Workflow: K-Means Dominant Palette Extraction
```mermaid
sequenceDiagram
    participant Caller (Camera/UI)
    participant KMeans as kMeansPalette
    
    Caller->>KMeans: extractKMeansPalette(pixels, k=3, maxIter=5)
    alt pixels array is empty
        KMeans-->>Caller: Return [Red, Green, Blue] defaults
    end
    
    KMeans->>KMeans: Initialize 3 Centroids (Evenly spaced samples)
    
    loop Up to maxIter (5)
        KMeans->>KMeans: Clear assigned clusters
        
        loop For each pixel
            KMeans->>KMeans: Euclidean Dist: Assign to nearest Centroid
        end
        
        KMeans->>KMeans: Recalculate Centroids (Mean R, G, B of clusters)
        
        alt Centroids stabilized (No change)
            KMeans->>KMeans: Break loop early
        end
    end
    
    KMeans->>KMeans: Sort Centroids by dominance (Cluster size)
    KMeans-->>Caller: Return Dominant RGB Array (k=3)
```

## 7. Design System & Token Manifest (Elite Directive)
- **Generative Palette Tokens** (`presetColorUtils.ts`):
  - `GENERATIVE_RAINBOW`: `['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#00BFFF', '#0000FF', '#8B00FF']` *(Visualizes algorithmic hardware patterns)*
- **Preset Color Map** (`ColorUtils.ts`):
  - `COLOR_PRESET_PALETTE`: `['#FF0000', '#FF8000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#800080', '#FF00FF', '#FFFFFF', '#000000']`
  - `PRESET_HUE_MAP`: Instantly locks hue sliders for primary colors (e.g., `#00FFFF` -> 180, `#FF00FF` -> 300).
- **Vibrancy Mathematical Tokens** (`boostForLED`):
  - Neutral Suppression Gate: `Saturation (S) < 0.20` *(Passes through true grays/whites)*
  - Boost Coefficient: Maximum target `Saturation (S) = 1.0`, `Value (V) = 1.0` *(Overrides dull camera captures for pure LED brilliance)*

<!-- CARTOGRAPHER_END: UTILS -->
```

<!-- CARTOGRAPHER_END: UTILS -->

### NATIVE & WATCH\n<!-- CARTOGRAPHER_START: NATIVE_&_WATCH -->

Here is the requested Markdown payload for the `NATIVE_&_WATCH` domain, ready for injection into the `<!-- CARTOGRAPHER_START: NATIVE_&_WATCH -->` block.

```markdown
## Domain: NATIVE & WATCH

### 1. File Manifest
* **`targets/watch/ComplicationController.swift`**: Provides watchOS complications (e.g., speed gauge) that update in real-time.
* **`targets/watch/ContentView.swift`**: Main SwiftUI UI for Apple Watch, rendering idle, active, and summary session states.
* **`targets/watch/HealthManager.swift`**: Manages `HKWorkoutSession` for watchOS, fetching live HR/calories and preventing app sleep.
* **`targets/watch/WatchConnectivityManager.swift`**: Central watchOS singleton handling bidirectional `WCSession` updates with the iOS host.
* **`android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/MainActivity.kt`**: Wear OS entry point managing Ambient mode and `FLAG_KEEP_SCREEN_ON` policies.
* **`android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/DashboardScreen.kt`**: Jetpack Compose Wear OS UI displaying session telemetry and interactive buttons.
* **`android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt`**: Android utility wrapping `MessageClient` to dispatch watch-to-phone commands and throttled health updates.
* **`android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/HealthTracker.kt`**: Interfaces with Android `HealthServices` (`ExerciseClient`) for live HR/calorie metrics (`INLINE_SKATING`).
* **`android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/OngoingActivityManager.kt`**: Creates a Wear OS `OngoingActivity` notification to keep the session alive in the background.
* **`android/sk8lytzWear/src/main/kotlin/com/neogle
<truncated 2919 bytes>
| **Health Tracking** | `HKWorkoutSession` via HealthKit | `HealthServices` `ExerciseClient` (`INLINE_SKATING`) |
| **Background Execution**| `HKWorkoutSession` keeps app active implicitly | `OngoingActivity` + `Notification` explicitly required |
| **App UI Framework** | `SwiftUI` (`ContentView.swift`) | Jetpack Compose for Wear OS (`DashboardScreen.kt`) |
| **Screen Wake Policy** | System-managed during active workout | `FLAG_KEEP_SCREEN_ON` + `AmbientModeSupport` manually toggled |

### 6. Archival Status
**ARCHIVAL TAGGING:** Audited `tools/SK8Lytz_App_Master_Reference.md` §11 (Wearable Companion Architecture). The documentation is perfectly aligned with the v3.8.2 implementation (Health Services API, DataClient, SwiftUI, WatchBridge). No `

<!-- CARTOGRAPHER_END: NATIVE_&_WATCH -->\n\n### NOTIFICATIONS & ROUTING\n<!-- CARTOGRAPHER_START: NOTIFICATIONS_&_ROUTING -->

[🕵️ Scout — Reyes | Cartographer Node | Notifications & Routing Domain | cold]

I have completed the analysis for the Notifications & Routing domain across the designated files (`App.tsx`, `src/navigation/*`, `src/providers/*`, `src/services/Notification*`).

### Architectural Findings
- `src/navigation/` does not exist. SK8Lytz employs a flat, **100% state-driven routing architecture** governed primarily by `App.tsx` and auth context, utilizing conditional component rendering and Modals instead of a dedicated library like `react-navigation` or `expo-router`.
- Hardware compliance is enforced via gatekeeper providers (`BluetoothGuard`, `ComplianceGate`) at the root level.
- Push notifications are fully decoupled from initial launch to prevent OS prompt fatigue, integrating explicitly via `NotificationService`.

Here is the final Cartographer payload.

***

### NOTIFICATIONS_&_ROUTING

**1. File Manifest**
- `App.tsx`: The architectural root. Handles global error catching (`SafeErrorBoundary`), telemetry monkey-patching, top-level context orchestration (Auth, BLE, Session, Theme), and the state-based routing switch between `AuthScreen` and `DashboardScreen`.
- `src/providers/BluetoothGuard.tsx`: The hardware gatekeeper. Intercepts the rendering pipeline if OS Bluetooth permissions or the physical Bluetooth adapter are disabled, ensuring no downstream UI or BLE logic executes blindly.
- `src/providers/ComplianceGate.tsx`: The legal gatekeeper. Enforces EULA acceptance (`required_eula_version`) by conditionally rendering the `EulaModal`. Integrates deeply with `isOfflineMode` to save local compliance to `AsyncStorage` when cloud synchronization is unavailable.
- `src/services/NotificationService.ts`: Core wrapper for `expo-notific
<truncated 3135 bytes>
 UninitializedPropertyAccessException`.
- **Web Build Avoidance**: Push notification logic explicitly short-circuits (`if (Platform.OS === 'web') return false;`) because Expo Web Push is not utilized in this flow.

### Sequence Diagram: App Initialization & State-Driven Routing

```mermaid
sequenceDiagram
    participant OS as OS / Expo
    participant App as App.tsx
    participant Auth as AuthContext
    participant Gate1 as ComplianceGate
    participant Gate2 as BluetoothGuard
    participant UI as Screens (Auth/Dashboard)

    OS->>App: Launch App & Mount Providers
    App->>Auth: Initialize AuthContext
    Auth-->>App: Return (sessionLoaded, isAuthenticated, isOfflineMode)
    
    alt !sessionLoaded
        App->>UI: return null (Splash Screen holds)
    else sessionLoaded == true
        alt !isAuthenticated && !isOfflineMode
            App->>UI: Render <AuthScreen />
        else isAuthenticated || isOfflineMode
            App->>Gate1: Render <ComplianceGate>
            Gate1->>Gate1: Check required_eula_version (Supabase / AsyncStorage)
            alt EULA needed
                Gate1->>UI: Render <EulaModal> (Blocks App)
            else EULA OK
                Gate1->>Gate2: Render <BluetoothGuard>
                Gate2->>Gate2: Check OS BLE Permission & Adapter State
                alt BLE missing / disabled
                    Gate2->>UI: Render Warning ("Power Up SK8Lytz")
                else BLE OK
                    Gate2->>UI: Render <DashboardScreen />
                end
            end
        end
    end
```

<!-- CARTOGRAPHER_END: NOTIFICATIONS_&_ROUTING -->\n\n### SESSION TRACKING\n<!-- CARTOGRAPHER_START: SESSION_TRACKING -->

[🕵️ Scout — Reyes | Cartographer Node Execution | SESSION_TRACKING domain | warm]

I have completed the discovery of the `SESSION_TRACKING` domain and constructed the required 5 Elite Architecture sections, along with the Mermaid sequence diagram mapping the session lifecycle. 

I have verified that `src/services/SessionTrackingService.ts` **does not exist** in the codebase (its responsibilities are actively handled by `SpeedTrackingService.ts` and `useSessionTracking.ts`). The Master Reference was reviewed; no stale documentation for `SessionContext` was found to tag for archival, as Section 11 accurately describes the Wearable Companion Architecture.

Here is the final payload ready for injection:

<!-- CARTOGRAPHER_START: SESSION_TRACKING -->
### 1. File Manifest
- **`src/context/SessionContext.tsx`**: Manages the global React state for active skate sessions, coordinating hardware telemetry, auto-pause logic, persistent phase tracking, and synchronizing real-time metrics with Native OS Foreground Services and the WatchBridge.
- **`src/services/SessionTrackingService.ts`**: **[FILE NOT FOUND]** — Session persistence and queue flushing are structurally implemented via `SpeedTrackingService.ts` and the `useSessionTracking.ts` hook.

### 2. Blast Radius
- **Imports**: `@notifee/react-native`, `expo-location`, `@react-native-async-storage/async-storage`, `useGlobalTelemetry`, `useHealthTelemetry`, `AppLogger`, `sk8lytz-watch-bridge`.
- **Imported By**: Core App providers (`App.tsx` / `index.tsx`) to supply the React tree, and consumed by dashboard components (e.g., `DockedController`) via the `useSession()` hook.

### 3. Context Matrix
- **Provided**: `SessionContext` exposes `isSkateSessionActive`, `sessionPhase` (`IDLE` | `AC
<truncated 559 bytes>
ide-Effects**: Writes resilient session phase to `AsyncStorage` (`@sk8lytz_session_phase`), syncs session state downstream to watches (`WatchBridge.syncSessionState`), and binds native OS notification actions.

### 5. OS Variance Matrix
- **Android**: Uses `notifee.createChannel()` for `sk8lytz-session`. Implements a strict Foreground Service (FGS) with `FOREGROUND_SERVICE_TYPE_LOCATION`. *Critical Guard*: On Android 14+, the FGS is only launched if `AppState` is `active` and location permissions are granted, otherwise it crashes with a `SecurityException`. Uses `notifee.stopForegroundService()` to tear down.
- **iOS**: Configures interactive notification categories (`session-actions`) with a foreground-capable `End Session` button. Uses `notifee.cancelNotification()` to tear down. Relies on standard background notification presentation without the Android FGS paradigm.

### Session Lifecycle Sequence
```mermaid
sequenceDiagram
    actor User
    participant App as SessionContext
    participant Watch as WatchBridge
    participant Notifee as Native OS Notification
    participant Storage as AsyncStorage

    User->>App: startSession(externalStartTimeMs)
    App->>Storage: persistSessionPhase('active')
    App->>Watch: syncSessionState({ status: 'ACTIVE' })
    App->>Notifee: displayNotification (FGS on Android)
    
    loop Every 5s
        App->>Notifee: displayNotification (Update Distance/Speed)
    end
    
    alt GPS Speed < 0.2 mph
        App->>App: 10s auto-pause timer
        App->>Storage: persistSessionPhase('paused', pauseTimeMs)
        App->>Watch: syncSessionState({ status: 'PAUSED' })
    end
    
    User->>App: endSession()
    App->>Storage: persistSessionPhase('idle')
    App->>Watch: syncSessionState({ status: 'SUMMARY', metrics })
    App->>Notifee: stopForegroundService() / cancelNotification()
    
    note right of App: 10s delay to allow watch to show SUMMARY card
    App->>Watch: syncSessionState({ status: 'STOPPED' })
```
<!-- CARTOGRAPHER_END: SESSION_TRACKING -->

<!-- CARTOGRAPHER_END: SESSION_TRACKING -->\n\n### HARDWARE PROTOCOLS\n<!-- CARTOGRAPHER_START: HARDWARE_PROTOCOLS -->

The following is a <SYSTEM_MESSAGE> not actually sent by the user. It is provided by the system as important information to pay attention to.

<SYSTEM_MESSAGE>
[Message] timestamp=2026-06-07T19:22:09Z sender=6c136951-5abf-4c93-8081-bed72765fbba priority=MESSAGE_PRIORITY_HIGH content=Here is the extracted Elite Architecture Payload for the HARDWARE_PROTOCOLS domain (`src/protocols/*`).

## 1. File Manifest
- **`IControllerProtocol.ts`**: Defines the Hardware Abstraction Layer (HAL) interface for all LED controllers, providing unified request/response typings (`ProtocolResult`).
- **`ControllerRegistry.ts`**: Maintains the runtime registry of hardware adapters (BanlanX, Zengge) and resolves the active protocol matching a given BLE device advertisement.
- **`BanlanxAdapter.ts`**: Implements `IControllerProtocol` for BanlanX SP621E hardware, mapping app intentions to `0xA0` prefixed opcodes.
- **`ZenggeAdapter.ts`**: Implements `IControllerProtocol` for Zengge/MagicHome hardware, orchestrating the translation to `0x10`, `0x51`, `0x59`, and `0x62`/`0x63` protocol frames.
- **`ZenggeProtocol.ts`**: The SSOT raw byte payload compiler for all Zengge commands, converting arrays into checksum-wrapped packets (`[0x00, Seq, 0x80, ... 0x0B]`).
- **`PatternEngine.ts`**: The centralized template registry (`SK8LYTZ_TEMPLATES`) and master dispatcher that routes pattern IDs to math generators.
- **`SpatialEngine.ts`**: Math synthesizer that builds multi-color RGB arrays across the physical LED canvas (spatial and generative effects).
- **`SymphonyEngine.ts`**: Dedicated visualizer generators that build pixel arrays specifically tailored to Music Mode reactivity and test native hardware sequences.
- **`VisualizerEngine.ts`**: Bridges the pattern generators with the UI `ProductVisualizer`, managing simulated tick rotations and array mirroring.
- **`PositionalMathBuffer.ts`**: Generates mathematically interpolated RGB LED buffers from spatial percentage-based nodes, rendering complex gradients bypassing hardware constraints.
- **`REA
<truncated 3915 bytes>
ct(effectId=1, speed=10, brightness=100)
    BLE->>Adapter: buildEffect(1, 10, 100)
    Note over Adapter: Generates [0xA0, 0x53, ...] and [0xA0, 0x54, ...]
    Adapter-->>BLE: ProtocolResult (2 packets, 20ms delay)
    
    BLE->>Adapter: prepareForTransmission(result, MTU)
    Adapter-->>BLE: Unchanged (BanlanX < 20 bytes)
    
    BLE->>HW: Write 0x53 (Effect Selection)
    Note over BLE: Wait 20ms delay (MANDATORY)
    BLE->>HW: Write 0x54 (Speed Selection)
```

## 8. Domain Specific: Hardware Capabilities & Array Byte Maps
- **Zengge Hardware (`0x59` Animation Opcodes)**:
  - Header: `0x59, totalLen_hi, totalLen_lo`
  - Body: RGB per pixel for 12 to 54 LEDs maximum.
  - Footer: `numPoints_hi, numPoints_lo, transitionType, speed(1-100), direction(1/0), checksum`
  - `transitionType` mapped mathematically: `0x01` (Static), `0x02` (Running scroll), `0x03` (Strobe flash), `0x04` (Hard jump), `0x05` (Breathe fade).
  - EEPROM `0x62`: Set configuration `[0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, chksum]`.
- **Zengge Hardware (`0x51` Compact/Extended Opcodes)**:
  - Custom sequence packets are prefixed with `0x51`.
  - The compact format generates up to 18 custom steps natively fitting under standard MTU without `0x40` packet chunking: `[0x51, 0xF0, mode, speed, R1,G1,B1, R2,G2,B2, 0x0F, checksum]`.
- **BanlanX SP621E Adapter (`0xA0` Base Family)**:
  - Commands use the `[0xA0, cmd, length, ...payload]` format.
  - No sequence counters or dynamic length bytes natively wrapped compared to Zengge.
  - Power: `0xA0 0x50 0x01 0x01` (ON) / `0xA0 0x50 0x01 0x00` (OFF).
  - Solid Color: `0xA0 0x52 0x03 R G B`.
  - Multi-Packet Effects: Dispatch `0xA0 0x53 0x01 effectId`, enforce strict **20ms mandatory inter-packet delay**, dispatch `0xA0 0x54 0x01 speed`. Speed mapping must clamp app scale (1-100) to hardware scale (1-10).
  - Audio: BanlanX does not need external App FFTs (`0x74` magnitudes). Music mode natively delegates onboard via `0x59` input and `0x5A` gain packets.
</SYSTEM_MESSAGE>

<!-- CARTOGRAPHER_END: HARDWARE_PROTOCOLS -->\n\n### CLOUD FUNCTIONS\n<!-- CARTOGRAPHER_START: CLOUD_FUNCTIONS -->

🎯 PM — Jordan | Cartography Task | CLOUD_FUNCTIONS | cold

Here is the Elite Architecture Markdown Payload for the domain, ready for injection.

I have completed the Cartographer Scan for the Cloud Functions domain (`supabase/functions/*` and `supabase/migrations/*`) and compiled the requested 5 Elite Architecture base sections, the Sequence Diagram for the session notification lifecycle, and applied the `

<!-- CARTOGRAPHER_END: CLOUD_FUNCTIONS -->\n\n### THEME & ASSETS\n<!-- CARTOGRAPHER_START: THEME_&_ASSETS -->

[🕵️ Scout — Reyes | Cartography Task | THEME_&_ASSETS | cold]

<!-- CARTOGRAPHER_START: THEME_&_ASSETS -->

## 1. File Manifest
- **`src/theme/theme.ts`**: Central design token repository establishing the core typography, layout constants, and color palette structures (Dark/Light) utilized across the app.
- **`src/styles/DashboardStyles.ts`**: Dynamic StyleSheet factory mapping `ThemePalette` and window dimensions into the 4-Slab dashboard layout, fluid typography sizing, and premium gradient arrays.
- **`src/constants/AppConstants.ts`**: Fundamental app configuration primitives (e.g., `STORAGE_PREFIX`, `HW_SPEED_MAX`).
- **`src/constants/ControlsRegistry.ts`**: Global configuration dictionary defining administrative safety limits, feature toggles, and their associated UI risk levels (`normal`, `warning`, `danger`).
- **`src/constants/ProductCatalog.ts`**: Offline-safe SSOT fallback catalog for all hardware profiles (HALOZ, SOULZ, RAILZ), defining physical capacities, threshold rules, and visualizer metadata.
- **`src/constants/storageKeys.ts`**: Constant dictionary defining canonical `AsyncStorage` keys to prevent namespace drift.

## 2. Blast Radius
- **Imports Within Domain**: `DashboardStyles.ts` imports `ThemePalette`, `Layout`, and `Spacing` from `theme.ts`.
- **External Consumers**:
  - `theme.ts` is consumed globally by all UI components.
  - `ProductCatalog.ts` is consumed by `DeviceRepository`, `VisualizerUnit`, and Bluetooth setup layers (e.g., matching LED points to hardware type).
  - Constants and `storageKeys.ts` are tightly coupled to caching services, Supabase synchronization hooks, and administrative interfaces (`AdminToolsModal`).

## 3. Context Matrix
- **`ThemeContext`** (External Consumer): Though not defi
<truncated 1014 bytes>
atrix
- **View Dimensions**: `createDashboardStyles` dynamically scales typography, padding, and layout geometries based on viewport boundaries (`isShort < 720px`, `isVeryShort < 640px`, `isNarrow < 360px`) to normalize UI fidelity between standard Android aspect ratios and constrained iOS devices (e.g., iPhone SE).


## 7. SEQUENCE DIAGRAM
```mermaid
sequenceDiagram
    participant App as Application Boot
    participant Local as ProductCatalog (Local)
    participant Cloud as Supabase
    participant Cache as AsyncStorage
    
    App->>Local: Mount & Load LOCAL_PRODUCT_CATALOG
    Local-->>App: Return Offline-Safe Hardware Profiles
    App->>Cloud: Background Fetch `product_catalog` (is_active = true)
    alt Cloud Available
        Cloud-->>App: Return Remote Profiles
        App->>Cache: Merge & Cache (Cloud Wins ID Conflicts)
    else Cloud Unavailable
        App->>Cache: Rely on Local / Cached Data Only
    end
```

## 8. DOMAIN SPECIFIC: Design System & Token Manifest

### Color Palette (SK8Lytz Brand Identity)
- **Primary**: `#FF5A00` (Orange)
- **Secondary**: `#FFB800` (Amber)
- **Dark Background/Surface**: `#1B4279` / `#245596`
- **Light Background/Surface**: `#EAEFF5` / `#CBD6E2`
- **Utility Status**: Success `#00E88F`, Error `#FF3D71`, Warning `#FFB800`

### Typography (Righteous Engine)
- **Header**: 24px, uppercase, 2px letter spacing
- **Title**: 16px, 0.5px letter spacing
- **Body**: 14px
- **Caption**: 11px

### Baseline Spacing Architecture
- Defined iteratively in an 8-point increment grid framework (post-`xs`):
  `xxs: 2`, `xs: 4`, `sm: 8`, `md: 12`, `lg: 16`, `xl: 24`, `xxl: 32`, `huge: 48`, `giant: 64`

<!-- CARTOGRAPHER_END: THEME_&_ASSETS -->

<!-- CARTOGRAPHER_END: THEME_&_ASSETS -->\n\n### SIMULATION & MOCKS\n<!-- CARTOGRAPHER_START: SIMULATION_&_MOCKS -->

Here is the final Markdown Payload for the SIMULATION_&_MOCKS domain. It includes the 5 Elite Architecture base sections, the Sequence Diagram for the Offline Queue Flush process, and the Archival Instruction for the Master Reference.

<!-- CARTOGRAPHER_START: SIMULATION_&_MOCKS -->
### 12.10 Simulation & Mocks

#### File Manifest
- **`src/mocks/react-native-vision-camera-worklets.web.js`**: Web stub to prevent module crash on web loads for camera vision processing.
- **`src/mocks/react-native-worklets.web.js`**: No-op shim aliased for metro bundler when on web to prevent `react-native-worklets-core` from crashing due to `TurboModuleRegistry.getEnforcing` missing on web.
- **`src/__mocks__/sk8lytz-watch-bridge.ts`**: Jest mock for the native Expo watch bridge module, replacing native functions to prevent test crashes and allow asserting bridge payloads without a physical device.
- **`e2e/init.ts`**: Detox initialization hook ensuring the app is launched fresh before all tests and React Native is reloaded before each individual test.
- **`e2e/jest.config.js`**: Detox Jest configuration setting up the test environment, runners, and reporter to target end-to-end smoke tests.
- **`e2e/smoke.test.ts`**: Core Detox end-to-end test validating the basic app lifecycle by ensuring the dashboard screen becomes visible upon launch.
- **`__tests__/services/SpeedTrackingService.offline.test.ts`**: Comprehensive Jest unit test suite covering offline session queue logic, authentication state handling, flush happy paths, and re-entrancy guards for `SpeedTrackingService`.

#### Blast Radius
- **Imports (What this domain consumes):**
  - **External:** `@react-native-async-storage/async-storage`, `detox`, `react-native`, `jest` framework functions.
  - *
<truncated 1330 bytes>
ed on mocked `mockGetUser`/`mockGetSession`.
  - *Side-effects*: Flushes data to mock Supabase DB, stores serialized offline queues in mocked `AsyncStorage`, presents `Alert.alert` prompts.

#### OS Variance Matrix
- **iOS/Android (E2E):** Detox E2E configuration (`jest.config.js`, `init.ts`, `smoke.test.ts`) abstracts native app behavior, relying on Detox to natively interface with the respective OS emulator.
- **Web (`*.web.js` mocks):** Explicit code branching that completely bypasses `react-native-worklets-core` and `react-native-vision-camera` logic strictly when compiled for the web environment, returning no-op or stubbed objects.

#### Sequence Diagram (Offline Queue Flush)
```mermaid
sequenceDiagram
    participant User
    participant SpeedTrackingService
    participant AsyncStorage
    participant Supabase

    User->>SpeedTrackingService: saveSession(snapshot, null)
    Note over SpeedTrackingService: User is unauthenticated (Offline)
    SpeedTrackingService->>AsyncStorage: setItem(PENDING_SESSION_QUEUE_KEY, snapshot)
    SpeedTrackingService-->>User: Alert.alert("Session Saved Locally")

    User->>SpeedTrackingService: flushPendingSessionQueue(userId)
    SpeedTrackingService->>AsyncStorage: getItem(PENDING_SESSION_QUEUE_KEY)
    AsyncStorage-->>SpeedTrackingService: returns queued sessions
    Note over SpeedTrackingService: Re-entrancy guard activated
    SpeedTrackingService->>Supabase: from('skate_sessions').insert(payload_with_userId)
    Supabase-->>SpeedTrackingService: success
    SpeedTrackingService->>AsyncStorage: setItem(PENDING_SESSION_QUEUE_KEY, empty_array)
    Note over SpeedTrackingService: Re-entrancy guard released
```

>

<!-- CARTOGRAPHER_END: SIMULATION_&_MOCKS -->\n\n### BUILD CONFIG & OTA\n<!-- CARTOGRAPHER_START: BUILD_CONFIG_&_OTA -->

[🕵️ Reyes | SDE Cartographer Node | BUILD_CONFIG_&_OTA | cold]
<!-- CARTOGRAPHER_START: BUILD_CONFIG_&_OTA -->
## 1. File Manifest
- `app.config.js`: Centralized Expo configuration defining app metadata, platform-specific settings, plugins, and EAS build project references.
- `eas.json`: Defines the build profiles (development, preview, production) and submission targets for Expo Application Services (EAS).
- `metro.config.js`: Metro bundler configuration that injects web-platform no-op shims for native-only modules to prevent web build crashes.
- `babel.config.js`: Defines the Babel compiler presets and plugins, specifically configuring the `react-native-worklets/plugin` for frame processing.
- `package.json`: NPM manifest detailing all dependencies, dev dependencies, build scripts, and engine requirements.

## 2. Blast Radius
- **Imports/Depends On**:
  - `react-native-worklets`, `react-native-vision-camera`, `react-native-ble-plx`, `@react-native-async-storage/async-storage` (node_modules).
  - Expo plugins (`@config-plugins/detox`, `expo-build-properties`, `react-native-ble-plx`, `@bacons/apple-targets`, `./plugins/withWearOsModule`).
  - Native module stub mocks for web (`src/mocks/*.web.js`).
- **Imported By**:
  - Expo/Metro CLI and EAS CLI during local development, build, and OTA update steps.
  - CI/CD pipelines relying on `package.json` scripts (`npm run verify`, `blast-radius-scanner.js`).

## 3. Context Matrix
- **Provided**: None (configuration domain operates outside the React component tree).
- **Consumed**: None.

## 4. Hook/Service I/O Registry
- **npm scripts**:
  - `start` (Input: None, Output: Expo dev server).
  - `android`/`ios` (Input: Native config, Output: Compiled local apps).
  - `web` (Input: `metro.co
<truncated 1043 bytes>
t: true`).
  - **EAS Build**: Simulator build is explicitly enabled for the `preview` profile (`simulator: true`).

## 6. Domain Specific (EAS, OTA, & Native Modules)
- **EAS Configurations**:
  - **CLI Config**: Enforces EAS CLI version `>= 16.0.0`, uses `remote` appVersionSource, and demands `requireCommit: true`.
  - **Build Profiles**:
    - `development`: internal distribution, dev client, apk.
    - `preview`: internal distribution, simulator support, apk.
    - `production`: standard submission profile, aab.
- **Native Module Requirements**:
  - Extends standard React Native via `@bacons/apple-targets` (watchOS) and `./plugins/withWearOsModule` (Wear OS companion).
  - `react-native-ble-plx` background execution is enabled with `peripheral` and `central` modes.
  - Native module integration enforces the use of Jetifier on Android due to legacy dependency constraints.

## 7. Sequence Diagram
```mermaid
sequenceDiagram
    participant User as Developer
    participant AppConfig as app.config.js
    participant EAS as EAS CLI / eas.json
    participant Cloud as Expo Application Services
    participant BuildScript as npm scripts (verify)

    User->>BuildScript: Run `npm run verify`
    BuildScript-->>User: Verification Success
    User->>EAS: Execute `eas build --profile <profile>`
    EAS->>AppConfig: Evaluate app.config.js & Plugins
    AppConfig-->>EAS: Inject Native properties (SDK 36, Wear OS, BLE)
    EAS->>Cloud: Dispatch Build Job (requireCommit: true)
    Cloud->>Cloud: Run Build Pipeline (Android/iOS)
    Cloud-->>User: Return Artifact (APK / AAB / Simulator Build)
```

<!-- CARTOGRAPHER_END: BUILD_CONFIG_&_OTA -->\n\n## 13. Historical Archive (The Graveyard)

### Archived from IDENTITY
`
> The existing `IDENTITY` section in `tools/SK8Lytz_App_Master_Reference.md` (lines 1432-1460) is stale. It contains references to `ProfileService` as a monolithic God-object. These references must be archived to reflect the "Meal 1: ProfileService split" where it is strictly a barrel re-export facade. Furthermore, it lacks the tracking for `ProfileService.types.ts` and `src/components/account/*.tsx` as defined in current architecture constraints.


### Archived from GROUP_SYNC
**: Any legacy documentation in `SK8Lytz_App_Master_Reference.md` referencing scalar `group_id` constraints or old `ng_custom_groups` keys must be formally archived. The migration to `group_ids` (array) and `GroupRepository` via junction tables has superseded the legacy 1:1 hardware group relationship.


### Archived from UI_CONTROLS
| DockedController | favorites[], quickPresets[], save/delete/load operations, prompt FSM |`
*(Reason: `useFavorites` was superseded by `useSharedFavorites` injected from `FavoritesContext` via `../context/FavoritesContext`)*
<!-- CARTOGRAPHER_END: UI_CONTROLS -->


### Archived from DATA_LAYER
`
> *(The service DOES exist and is currently handling BLE error extraction.)*

---

<!-- CARTOGRAPHER_START: DATA_LAYER -->


### Archived from UTILS
` The existing "12.6 Utilities & Types" documentation in `tools/SK8Lytz_App_Master_Reference.md` is stale and missing key details (like OS Variance Matrix and the K-Means Sequence Diagram). It should be archived to make room for this updated architecture manifest.

### UTILS

```markdown
<!-- CARTOGRAPHER_START: UTILS -->


### Archived from NATIVE_&_WATCH
` tags required.

### 7. Sequence Diagram
```mermaid
sequenceDiagram
    participant P as Phone (SessionContext)
    participant B as WatchBridge (Native Module)
    participant W as Watch App (Wear OS / watchOS)
    participant H as Health Sensors (HealthKit/HealthServices)

    %% Session Initiation
    note over P, H: Watch-Initiated Session Start
    W->>W: User taps "Start"
    W->>B: command: "START_SESSION" (MessageClient / WCSession)
    B->>P: onWatchCommandReceived("START_SESSION")
    P->>P: Boot Phone Session & GPS
    P->>B: syncSessionState({ status: 'ACTIVE', startTime })
    B->>W: Push State (DataClient / updateApplicationContext)
    W->>H: startWorkout() / startTracking()
    
    %% Phone -> Watch Telemetry Loop
    note over P, H: 1Hz UI Telemetry Loop (Phone → Watch)
    loop Every 1 second
        P->>B: sendMetricUpdate({ speed: 12.5 })
        B->>W: MessageClient / sendMessage
        W->>W: Update Speed HUD & Complications
    end
    
    %% Watch -> Phone Health Relay Loop
    note over P, H: 5s Health Relay Loop (Watch → Phone)
    loop Every 5 seconds
        H-->>W: onHealthUpdate(HR, Calories)
        W->>B: sendHealthUpdate(HR, Calories)
        B->>P: onWatchHealthUpdate(HR, Calories)
    end
```
```


### Archived from NOTIFICATIONS_&_ROUTING
`, as this document now fully captures the absence of `src/navigation` and correctly maps the state-driven router in `App.tsx` and the `BluetoothGuard`/`ComplianceGate` boundaries.


### Archived from CLOUD_FUNCTIONS
` tag to the legacy references.

<!-- CARTOGRAPHER_START: CLOUD_FUNCTIONS -->

### 🗂️ File Manifest (CLOUD_FUNCTIONS)
- `supabase/functions/notify-crew-session/index.ts`: Deno Edge Function that authenticates users, queries `crew_memberships`, and dispatches Expo Push Notifications in batches when a session starts.
- `supabase/migrations/20260414_account_deletion_rpc.sql`: Creates a secure `delete_account()` RPC utilizing Postgres `ON DELETE CASCADE` to fulfill App Store GDPR/account deletion requirements.
- `supabase/migrations/20260418061000_admin_user_management.sql`: Defines the `user_role` ENUM, sets up the `admin_audit_logs` table, and deploys security-definer RPCs (`admin_ban_user`, `admin_revoke_ban`, `admin_soft_delete_user`).
- `supabase/migrations/20260419*_scraper_*.sql` (Multiple): Provisions the Control Plane and schema for the Cultural Enrichment Puppeteer Daemon.
- `supabase/migrations/202605*_telemetry.sql` (Multiple): Configures tables for god-tier hardware tracking and system health telemetry.
- `supabase/migrations/20260607*_fix_telemetry_schema.sql` (Multiple): Final type safety patches and constraint fixes for verified telemetry.
*(Note: 40 total migration files meticulously orchestrate the backend state mapping).*

### 💥 Blast Radius
- **Imports**: `notify-crew-session` imports `@s
<truncated 1054 bytes>
nput**: Implicit via `auth.uid()`.
  - **Side Effects**: Permanently `DELETE`s row from `auth.users`, leveraging DB cascades to wipe all related telemetry, sessions, and profile data natively.
- **`admin_ban_user(p_target_user_id, p_reason)` (Postgres RPC)**
  - **Input**: Target UUID and reason string.
  - **Side Effects**: Flips `is_banned` bit in `user_profiles` and creates an audit entry in `admin_audit_logs`.

### 📱 OS Variance Matrix
- **Push Notification Formatting (`notify-crew-session`)**: The payload explicitly shapes OS behavior by transmitting `sound: "default"` and `channelId: "crew-alerts"`. This bridges OS variance where iOS leverages the default APNs chime while Android relies on matching the specific `crew-alerts` Notification Channel configured natively in the Sk8Lytz Android wrapper.

### 🔄 Sequence Diagram: Crew Session Notifications
```mermaid
sequenceDiagram
    participant App as Mobile App (Leader)
    participant Func as Edge Function (notify-crew-session)
    participant DB as Supabase DB
    participant Expo as Expo Push API
    participant Member as Crew Member Device

    App->>Func: POST /notify-crew-session {crewId, sessionId...} + JWT
    Func->>DB: auth.getUser(JWT)
    DB-->>Func: Return authenticated User
    Func->>DB: SELECT user_id FROM crew_memberships WHERE crew_id = target AND user_id != caller
    DB-->>Func: Return Array of member_ids
    Func->>DB: SELECT token FROM push_tokens WHERE user_id IN (member_ids)
    DB-->>Func: Return Array of Expo Push Tokens
    
    loop Every 100 Tokens (Batch Size)
        Func->>Expo: POST https://exp.host/--/api/v2/push/send
        Expo-->>Func: HTTP 200 OK
    end
    
    Func-->>App: { sent: totalCount }
    Expo->>Member: OS Push Notification: "Crew is Live!"
```

### 🗄️ Archival Instruction
`[MOVE_TO_ARCHIVE]` - As requested, the stale migrations count references and deprecated DB metrics in the Master Reference should be relocated to the Archive Graveyard.

<!-- CARTOGRAPHER_END: CLOUD_FUNCTIONS -->


### Archived from THEME_&_ASSETS
**: Any legacy references in `tools/SK8Lytz_App_Master_Reference.md` or elsewhere describing hardcoded `switch(type)` UI heuristics or `ng_*` storage keys. As of the Dynamic Catalog Migration (2026-04-11), all visualization themes and device rules strictly use `LOCAL_PRODUCT_CATALOG`.


### Archived from SIMULATION_&_MOCKS
- 2026-06-07 (Cartographer Sweep)
> **Domain:** SIMULATION_&_MOCKS
> Stale Reference: The previous cartographer block for `SIMULATION_&_MOCKS` in `SK8Lytz_App_Master_Reference.md` (lines 1500-1506) is stale and should be archived or overwritten by this updated Cartographer payload.
<!-- CARTOGRAPHER_END: SIMULATION_&_MOCKS -->


### Archived from BUILD_CONFIG_&_OTA
** The documentation in `tools/SK8Lytz_App_Master_Reference.md` (Line 278) under "Android Build Requirements" which states: `- **SDK Versions**: Project currently targets SDK 34 (\`compileSdk\`, \`targetSdk\`).` This is stale and conflicts with `app.config.js` which defines `compileSdkVersion: 36` and `targetSdkVersion: 36`.
<!-- CARTOGRAPHER_END: BUILD_CONFIG_&_OTA -->


### Archived from OS_PERMISSIONS
` tag, as the current Legal Hardening section aligns perfectly with the codebase reality).*
<!-- CARTOGRAPHER_END: OS_PERMISSIONS -->
```


### Archived from DEPENDENCY_AUDIT
`: The `### Third-Party Library Patches` section referencing `@react-native-voice/voice` is fully deprecated dead code.
- `[MOVE_TO_ARCHIVE]`: The `Optical Simulation Mode (Web Fallback)` documentation in the Admin Tools Hub section. Maintaining `react-native-web` compatibility actively conflicts with heavy native packages present in `package.json` (like `react-native-nitro-modules` and `react-native-vision-camera-worklets`).
<!-- CARTOGRAPHER_END: DEPENDENCY_AUDIT -->


### Archived from GROUP_SYNC
` as instructed. My task here is complete.


### Archived from HARDWARE_PROTOCOLS
` tags for the deprecated opcode sections in the Master Reference.


### Archived from CLOUD_FUNCTIONS
` tag for the stale migrations count in the Master Reference back to the main agent.


### Archived from THEME_&_ASSETS
`.


### Archived from BUILD_CONFIG_&_OTA
` tag, and delivered the final payload directly to the main agent using the `BUILD_CONFIG_&_OTA` marker.


### Archived from OS_PERMISSIONS
`, and the Mermaid sequence diagram for Android HealthConnect initialization.


> [!NOTE]
> This section is strictly managed by ?? Docs � Avery. It contains condemned opcodes, legacy architectural decisions, and features that have been removed from the live codebase. 
> 
> **THE PRIME DIRECTIVE:** We never delete history. If a Cartographer agent finds that a documented feature no longer exists in src/, they move the documentation block here instead of deleting it. This preserves the "Why" long after the "What" is gone.



> [MOVE_TO_ARCHIVE] - 2026-06-07 (Cartographer Sweep)
> **Domain:** UTILS
> Stale Reference: `src/utils/RbmDictionary.ts` and `src/utils/RbmSimulator.ts` no longer exist in the working directory tree.

> [MOVE_TO_ARCHIVE] - 2026-06-07 (Cartographer Sweep)
> **Domain:** BLE_CORE
> Stale Reference: Priority 4 `P4_MAINTENANCE` regarding GATT Mutex for liveness checks. The code shows neither `useBLEHeartbeat.ts` nor `useBLERSSIMonitor.ts` acquire the GATT mutex at any priority.

> [MOVE_TO_ARCHIVE] - 2026-06-07 (Cartographer Sweep)
> **Domain:** UI_CONTROLS
> Stale Reference: `BleStateMachine` class. This was ripped out for XState v5.

> [MOVE_TO_ARCHIVE] - 2026-06-07 (Cartographer Sweep)
> **Domain:** GROUP_SYNC
> Stale Reference: "Automatic _MM/DD suffix enforced in CrewModal.handleCreate". `CrewModal` no longer directly handles creation logic. All references to `GroupSyncService.ts` should be `CrewService.ts`.

### Archived on 2026-06-06 (V2 Data Audit)

- **Domain: IDENTITY** 
  - The existing `IDENTITY` section in `tools/SK8Lytz_App_Master_Reference.md` (lines 1432-1460) is stale. It lacks file tracking for `src/services/ProfileService.types.ts` and all component files within `src/components/account/*.tsx`. It also fails to provide the rigorous `Context Matrix` and `Blast Radius` schemas required by Cartographer node specifications.

- **Domain: BLE_CORE**
  - *Stale Claim:* "Priority tiers: ... `P4_MAINTENANCE` (heartbeat, RSSI polls)."
  - *Truth:* `useBLEGattMutex.ts` clearly labels Priority 4 as "SWEEPER" (Passive Scan). Neither `useBLEHeartbeat.ts` nor `useBLERSSIMonitor.ts` acquire the `GattMutex` at any priority (Heartbeat calls `writeCharacteristicWithoutResponse`, RSSI calls `readRSSIForDevice` directly). Line 1610 previously flagged this partially, but Line 472 remains stale.

- **Domain: GROUP_SYNC**
  - *Stale Reference:* "Automatic _MM/DD suffix enforced in CrewModal.handleCreate". CrewModal no longer directly handles creation logic. All references to GroupSyncService.ts should be CrewService.ts.

- **Domain: UI_CONTROLS**
  - *Stale Reference:* `pingDevice: Orchestrates an atomic connection: GATT -> green blink -> EEPROM probe -> sever GATT.`
  - *Reason:* Dashboard `runAutoProvisioning` was deleted; Setup Wizard now exclusively owns all device claiming, rendering this reference stale in the core `DashboardScreen` UI_CONTROLS block.

- **Domain: DATA_LAYER**
  - Any architectural documentation or plans referencing `src/services/TelemetryService.ts`. The codebase explicitly notes that this file does not exist, as telemetry is natively hook-driven.


### 15. OS_PERMISSIONS
<!-- CARTOGRAPHER_START: OS_PERMISSIONS -->

Here is the Cartographer Payload for the `OS_PERMISSIONS` domain:

```markdown
<!-- CARTOGRAPHER_START: OS_PERMISSIONS -->
### 12.X OS & Native Permissions Architecture

#### 1. File Manifest
*   **`android/app/src/main/AndroidManifest.xml`**: Defines critical Android system boundaries, foreground service typings (`location|health|connectedDevice|shortService|dataSync`), and exact granular hardware permissions required for BLE and health tracking.
*   **`app.config.js` (`ios.infoPlist`)**: Acts as the centralized Expo Prebuild configuration, dynamically injecting iOS Usage Descriptions (`NSMicrophoneUsageDescription`, `NSCameraUsageDescription`, `NSHealthShareUsageDescription`) into the ephemeral `Info.plist`.

#### 2. Blast Radius
*   **Imports**: None (Native configuration files).
*   **Imported By**: React Native Build System (Expo Prebuild, Gradle, CocoaPods).
*   **Impact**: Any modification to these configuration structures requires a full native rebuild. Missing or misaligned declarations will result in silent OS-level hardware denial or immediate app crashes (e.g., `SecurityException` for undeclared Foreground Service types), severing functionality for `BluetoothGuard`, `useHealthTelemetry`, `CameraTracker`, and `useAppMicrophone`.

#### 3. Context Matrix
*   While native config files do not consume React Context directly, their configuration dictates the truth state for the **App-Level Opt-Out Ledger** (`@sk8lytz_permissions_optout`).
*   This ledger is reconciled with OS native permission states and consumed by React Native via `GlobalPermissionsModal`, `BluetoothGuard`, and `PermissionsOnboardingScreen`.

#### 4. Hook/Service I/O Registry
*   **Inputs**: OS-level Authorization Prompts (User Consent).
*   **Outputs**: Native
<truncated 379 bytes>
ndroid**: Enforces a strict, granular permission model (`BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, `BLUETOOTH_ADMIN`). Crucially, background stability requires explicit `<service>` declarations for `app.notifee.core.ForegroundService` with exact types (`location|health|connectedDevice`). Max SDK versions are enforced on legacy storage permissions (`READ_EXTERNAL_STORAGE` max=32).
*   **iOS**: Enforces a strict user-intent model via `infoPlist` usage strings. Background execution for BLE is handled holistically via the `react-native-ble-plx` Expo plugin (`isBackgroundEnabled: true`, `bluetoothAlwaysPermission`). The `ios` native directory is completely ephemeral/gitignored (Expo Prebuild environment).

#### 6. Sequence Diagram: Legal Hardening & Permission Reconciliation
```mermaid
sequenceDiagram
    participant OS as Native OS (iOS/Android)
    participant App as App Lifecycle (syncSystemPermissions)
    participant Ledger as AsyncStorage (@sk8lytz_permissions_optout)
    participant UI as GlobalPermissionsModal

    App->>OS: Query Current Hardware Permission State
    OS-->>App: Returns State (Granted / Denied)
    App->>Ledger: Read Soft-Revoke Status
    Ledger-->>App: Returns Ledger State (Opt-In / Opt-Out)
    
    alt OS is Denied
        App->>Ledger: Force state to 'Opt-Out' (OS Authority)
    else OS is Granted, but Ledger is Opt-Out
        App->>App: Suppress Hardware API Access (Soft-Revoke honored)
    else OS is Granted, Ledger is Opt-In
        App->>App: Authorize Hardware API Access
    end

    App->>UI: Hydrate UI Toggle States
    
    opt User Toggles Permission in App
        UI->>Ledger: Update Soft-Revoke State
        UI->>OS: Dispatch Telemetry Event (PERMISSION_OPT_IN / OUT)
    end
```

*(Note: No stale documentation directly contradicting this domain was found in `tools/SK8Lytz_App_Master_Reference.md` requiring the `

<!-- CARTOGRAPHER_END: OS_PERMISSIONS -->
```

<!-- CARTOGRAPHER_END: OS_PERMISSIONS -->

### 16. DEPENDENCY_AUDIT
<!-- CARTOGRAPHER_START: DEPENDENCY_AUDIT -->

I have completed the Cartographer Scan for the `DEPENDENCY_AUDIT` domain. The Elite Architecture markdown payload, including the File Manifest, Blast Radius, Context Matrix, Hook/Service I/O Registry, OS Variance Matrix, and the Mermaid sequence diagram for the `verify` script gating pipeline, has been generated. I also identified stale documentation in the Master Reference that needs archival.

<!-- CARTOGRAPHER_START: DEPENDENCY_AUDIT -->
### 1. File Manifest
- **`package.json`**: The core dependency ledger and build script manifest for the SK8Lytz application, governing native module resolutions (Expo SDK 55, React Native 0.83.2), custom local bridges (`sk8lytz-watch-bridge`), and strict CI/CD pipeline scripts (`verify`, `blast-radius`).

### 2. Blast Radius
- **Imports (External/Local Dependencies):** Defines the entire application's external ecosystem, importing React/Expo core, BLE transport (`react-native-ble-plx`), native camera/worklet engines (`react-native-vision-camera`, `react-native-worklets`, `nitro-modules`), state management (`xstate`), spatial clustering (`supercluster`), and the local custom bridge `sk8lytz-watch-bridge`.
- **Imported By:** Acted upon by the Node package manager (`npm`/`yarn`/`bun`), the Expo CLI pipeline (`eas build`, `expo run`), CI/CD gating scripts, and ultimately defines the module resolution graph for every source file in the project.

### 3. Context Matrix
- While `package.json` itself is not a React component, it explicitly provisions the critical dependency providers that back all application contexts: `@supabase/supabase-js` (Auth/Session Contexts), `react-native-ble-plx` (BLE Device Contexts), `@xstate/react` (FSM Contexts), and `react-native-safe-area-context` (Layout Contexts).

### 4. 
<truncated 676 bytes>
un:android`), `ios` (`expo run:ios`), and `web` (`expo start --web`).
- **Dependencies:** Provisions OS-specific linking constraints, such as `@bacons/apple-targets` exclusively for watchOS companion building, and splits health kit capabilities between `@notifee/react-native` / `react-native-health` (iOS) and `react-native-health-connect` (Android).

### 6. Architectural Sequence Diagram (Verification Pipeline)
```mermaid
sequenceDiagram
    participant Dev as Developer
    participant NPM as package.json
    participant BRS as tools/blast-radius-scanner.js
    participant VCR as tools/verifiable-check-runner.js
    participant GK as fortress-gatekeeper.ps1
    
    Dev->>NPM: npm run verify
    activate NPM
    NPM->>BRS: node blast-radius-scanner.js --worktree
    activate BRS
    Note right of BRS: Verifies worktree isolation rules
    BRS-->>NPM: Exit Code (0=Pass, 1=Collision)
    deactivate BRS
    alt Blast Radius Fails
        NPM-->>Dev: Abort Build/Merge
    else Blast Radius Passes
        NPM->>VCR: node verifiable-check-runner.js
        activate VCR
        Note right of VCR: Runs TSC, Jest, AST TypeSafety
        VCR-->>NPM: Exit Code (0=Pass, 1=Fail)
        deactivate VCR
        alt Verifiable Check Fails
            NPM-->>Dev: Abort Build/Merge
        else Verifiable Check Passes
            NPM-->>Dev: Verification ✅
            Dev->>GK: run fortress-gatekeeper.ps1
            Note right of GK: Fast-forward merge to master
        end
    end
    deactivate NPM
```

### 7. Archival Tags
- `

<!-- CARTOGRAPHER_END: DEPENDENCY_AUDIT -->

### UI Screens
<!-- CARTOGRAPHER_START: UI_SCREENS -->
<!-- CARTOGRAPHER_END: UI_SCREENS -->

### UI Docked Controller
<!-- CARTOGRAPHER_START: UI_DOCKED_CONTROLLER -->
<!-- CARTOGRAPHER_END: UI_DOCKED_CONTROLLER -->

### UI Modals
<!-- CARTOGRAPHER_START: UI_MODALS -->
<!-- CARTOGRAPHER_END: UI_MODALS -->

### UI Visualizer
<!-- CARTOGRAPHER_START: UI_VISUALIZER -->
<!-- CARTOGRAPHER_END: UI_VISUALIZER -->

### Protocol Core
<!-- CARTOGRAPHER_START: PROTOCOL_CORE -->
<!-- CARTOGRAPHER_END: PROTOCOL_CORE -->

### Pattern Engine
<!-- CARTOGRAPHER_START: PATTERN_ENGINE -->
<!-- CARTOGRAPHER_END: PATTERN_ENGINE -->

### Build Config
<!-- CARTOGRAPHER_START: BUILD_CONFIG -->
<!-- CARTOGRAPHER_END: BUILD_CONFIG -->

### Admin & Telemetry
<!-- CARTOGRAPHER_START: ADMIN_&_TELEMETRY -->
<!-- CARTOGRAPHER_END: ADMIN_&_TELEMETRY -->

## 13. 🪦 The Graveyard

### Batch 2026-06-07T04:05:25.387Z
- **Domain: IDENTITY**: ` The `SK8Lytz_App_Master_Reference.md` likely contains stale references describing `ProfileService` as a monolithic God-object. These references must be archived and updated to reflect the "Meal 1: ProfileService split" where it is strictly a barrel re-export facade over `AuthProfileService`, `CrewProfileService`, and `PushTokenService`.

- **Domain: GROUP_SYNC**: Stale `GROUP_SYNC` architecture documentation discovered in `tools/SK8Lytz_App_Master_Reference.md` (specifically lines 1579-1648 and 2145-2157 regarding older offline sync mechanisms and references). This should be reconciled with the newly analyzed file architecture.

- **Domain: DATA_LAYER**: ** This file does not exist. The architecture is natively hook-driven (`useGlobalTelemetry`, `useAdminTelemetry`). Documentation referencing it is stale.
- `src/types/supabase.ts`: Autogenerated TypeScript definitions enforcing strict type parity with the Supabase PostgREST schema (tables, views, enums).
- `src/services/supabaseClient.ts`: Supabase client initializer wrapping `expo-secure-store` for Auth state persistence, equipped with built-in mock fallbacks for seamless offline-mode execution.

- **Domain: NATIVE_&_WATCH**: - Master Reference Section 12.6 NATIVE_&_WATCH (Stale: Missing Wear OS & Bridge)`

---

- **Domain: NOTIFICATIONS_&_ROUTING**: ` The legacy documentation for NOTIFICATIONS_&_ROUTING found in `tools/SK8Lytz_App_Master_Reference.md` (lines 1837-1873) is stale and should be archived. It incorrectly lists `PushTokenService` as part of this exact directory group and misses the state-based routing elements like `App.tsx`, `BluetoothGuard`, and `ComplianceGate`.

- **Domain: SESSION_TRACKING**: ` required for the Master Reference.

- **Domain: HARDWARE_PROTOCOLS**: The Master Reference sections referencing `0x41 Settled Mode`, `0x42 RBM Programs Mode`, and `0x43` Multi-Sequence Mode should be flagged. The protocol codebase has explicitly marked them as `@deprecated Since v2.8.0` or `@HARDWARE-DANGER` due to state machine crashes and testing limitations, being fully superseded by `0x51` Pattern Engine and `0x59` Spatial routines.

- **Domain: CLOUD_FUNCTIONS**: ` was found in the Master Reference.

- **Domain: THEME_&_ASSETS**: The "Dashboard UI Layout (4-Slab Architecture)" and "UI Design Patterns & Branding" sections located in `SK8Lytz_App_Master_Reference.md` are tagged as stale documentation drift and should be archived or fully relocated to `DashboardStyles.ts` and `theme.ts`.

- **Domain: SIMULATION_&_MOCKS**: The existing documentation for this domain in `SK8Lytz_App_Master_Reference.md` (lines 2039-2080) is stale (it is missing the `useAnimatedStyle` hook in the worklets mock and lacks the flow diagram for the offline syncing logic) and should be archived.

---

- **Domain: BUILD_CONFIG_&_OTA**: ` tagging was unnecessary. Since OTA is not implemented (missing `expo-updates`), a sequence diagram for updates is not applicable.

- **Domain: OS_PERMISSIONS**: ` below.

Here is the Elite Architecture Markdown Payload for the domain:

```markdown

- **Domain: OS_PERMISSIONS**: Note: Stale documentation for OS_PERMISSIONS exists in `tools/SK8Lytz_App_Master_Reference.md` (lines 2168-2230).

- **Domain: DEPENDENCY_AUDIT**: **: Any documentation in `SK8Lytz_App_Master_Reference.md` referencing legacy pure-JS image processing (via `jpeg-js`) or older state management libraries superseded by `xstate` should be archived.

---

- **Domain: DEPENDENCY_AUDIT**
  - Any legacy documentation concerning Web fallbacks for BLE (Optical Simulation Mode for Expo Web). While the Master Reference cites it as a developer tool, maintaining react-native-web compatibility alongside heavy native packages like react-native-nitro-modules often leads to extreme configuration friction.
  - Remove any lingering workflow references or offline caches regarding @react-native-voice/voice. The package was confirmed deleted, so all bridging stubs relating to it should be eliminated.

- **Domain: OS_PERMISSIONS**
  - OS Sync: `syncSystemPermissions()` runs on boot/foreground to reconcile the ledger with native OS settings. If OS is "Denied", App ledger is forced to "Opt-Out". (This contradicts the actual implementation in PermissionService.ts, where aggressive sweeping was deprecated because it falsely locked out fresh installs).


- **Domain: NATIVE_&_WATCH**
  - Stale Reference: Master Reference Section 11.7 Future Watch Enhancements (Planned) lists "Session Duration Timer" and "watchOS Complications" as planned features. Both are fully shipped and active in targets/watch/ContentView.swift and targets/watch/ComplicationController.swift.

- **Domain: THEME_&_ASSETS**
  - Mention of "Master Reference §2 — FTUE Threshold Classification" in ProductCatalog.ts (this section is missing or stale in the current Master Reference).
  - "Dashboard UI Layout (4-Slab Architecture)" and "UI Design Patterns & Branding" located in SK8Lytz_App_Master_Reference.md (layout details should be strictly contained within DashboardStyles.ts and theme.ts to prevent domain drift).

- **Domain: HARDWARE_PROTOCOLS**
  - The entry in the "Condemned Opcodes" table: `0x41` Settled Mode (Symphony Effects). Cartographer Audit Reality: PatternEngine.ts explicitly intercepts test pattern IDs 201-233 and fires them via ZenggeProtocol.setCustomModeExtendedCompact() (which is a 0x51 opcode pipeline). The Master Reference directly contradicts itself later on line 398 warning against 0x41 usage, confirming the table row is stale legacy text.

- **Domain: SESSION_TRACKING**
  - Section 7 (Session Telemetry Architecture) contains a stale skate_sessions schema missing fields like avg_bpm, peak_gforce, crew_session_id, and has no documentation of the PENDING_SESSION_QUEUE_KEY offline fallback architecture.


### Hook Registry Updates
- useWebDemoConsoleBridge: Web Demo specific hook to pipe console logs to Command Center.

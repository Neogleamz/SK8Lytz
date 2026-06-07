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
| `@Sk8lytz_Favorites`                | useFavorites                    | Dictionary of user-defined lighting presets (Name, Palette, Mode)                                                                               |
| `@sk8lytz_permissions_optout`       | PermissionService               | App-Level Opt-Out Ledger. User toggles that override OS permissions for legal/privacy reasons.                                                  |
| `@Sk8lytz_voice_tutorial_dismissed` | boolean                         | Gating for the Voice Command onboarding modal                                                                                                   |
| `@sk8lytz_app_settings`             | AppSettingsService / useBLEScanner | App-wide admin feature flags. Key `hw_setup_rssi_threshold` (default -70 dBm) controls the RSSI gate during Setup Wizard device discovery. Loaded once on scanner mount. |

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
- **Gate check** — skips connection when `bleGateRef â‰  IDLE`
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
| **UUID Filter in `startDeviceScan`** | `useBLESweeper.ts` | `startDeviceScan(null, ...)` replaced with `startDeviceScan([ZENGGE_SERVICE_UUID], ...)`. Enables iOS background scanning mode (CBCentralManager requires a service UUID filter when `allowDuplicates: false`). Also reduces Android scan noise. |

### Android Platform Guards

_Added: 2026-06-05 (AND-02, AND-03, AND-04)_

| Guard | File | Fix |
| :--- | :--- | :--- |
| **Connection Priority Downgrade** | `BleConnectionManager.ts` | After handshake, `requestConnectionPriority(BALANCED)` fired to save 2—3Ã— battery. Only on Android (iOS manages its own priority). |
| **50ms Inter-Device Write Gap** | `BleWriteDispatcher.ts` | Increased from 20ms → 50ms. Fixes silent GATT drops on Qualcomm Snapdragon 665/675 and MediaTek Helio chipsets. |
| **Scan Budget Guard** | `useBLESweeper.ts` | Tracks `startDeviceScan` calls against Android 12+'s 4-per-30s budget. If exhausted, defers the scan start until the budget window resets. Prevents silent throttling where Android OS stops delivering scan results with zero error feedback. |

### Battery-Adaptive Sweeper (The Silent Sweeper)

_Added: 2026-06-05 | Lives in: `src/hooks/ble/useBLESweeper.ts` (BAT-01)_

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
> **DDA Refactor Shipped: 2026-04-14** — The architecture was refactored from a monolithic component model to a Hook-First domain model. **BLE Engine Refactor: 2026-06-05** — `useBLE.ts` decomposed into 6 domain sub-hooks (`useBLEScanner`, `useBLESweeper`, `useBLEAutoRecovery`, `useBLEGattMutex`, `useBLEHeartbeat`, `useBLERSSIMonitor`) + 6 extracted services (`BleConnectionManager`, `BleWriteDispatcher`, `BleWriteQueue`, `BleLifecycleManager`, `BlePingService`, `BleStateMachine`). `useBLE.ts` is now a thin orchestrator (~600 lines).

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
| `useBLESweeper` | `src/hooks/ble/useBLESweeper.ts` | Silent background LowPower scan, Interrogator Queue, `hwCache`, 3-tier battery-adaptive throttling (BAT-01) |
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

## 1. File Manifest
- `src/context/AuthContext.tsx`: Centralized authentication state provider that manages Supabase sessions, user objects, offline mode, and deep-link auth callbacks.
- `src/services/ProfileService.ts`: A unified facade that re-exports all profile-related CRUD methods from sub-services to preserve backward compatibility for legacy consumers.
- `src/services/ProfileService.types.ts`: Defines shared type contracts (e.g., `UserProfile`, `PermanentCrew`, `SessionHistoryItem`) to prevent circular dependencies between the split profile sub-services.
- `src/services/AuthProfileService.ts`: Sub-service that handles `user_profiles` CRUD operations, auto-heals missing auth metadata, and fetches user session histories.
- `src/services/CrewProfileService.ts`: Sub-service that orchestrates all permanent crew lifecycle operations, memberships, stats aggregation, and active session teardowns.
- `src/components/account/AccountTabCrewz.tsx`: UI component for browsing, creating, and joining (via code) permanent crews.
- `src/components/account/AccountTabDevices.tsx`: UI component for listing, renaming, grouping, and managing registered hardware devices.
- `src/components/account/AccountTabProfile.tsx`: UI component for updating core identity settings, such as display name, username, and avatar color.
- `src/components/account/AccountTabSecurity.tsx`: UI component for sensitive account operations like password changes, email updates, and granular privacy settings.
- `src/components/account/AccountTabSettings.tsx`: UI component for app-wide settings including push notifications, health integrations, and theme toggles.
- `src/components/account/AccountTabStats.tsx`: UI component displaying a dashboard of lifetime metrics, recent session history, and skater statistics.
- `src/components/account/SkaterStatsPanel.tsx`: A visual component that aggregates offline-first local cache data to generate dynamic user personas and "Wrapped" statistics.

## 2. Blast Radius
- **Imports In:**
  - `@react-native-async-storage/async-storage` (Token & profile caching, offline mode flag, local telemetry caching)
  - `@supabase/supabase-js` and `../services/supabaseClient` (Backend BaaS for Auth/DB sync)
  - `expo-linking` (Deep link parsing for auth callbacks)
  - `@expo/vector-icons`, `../../theme/theme`, `../CustomSlider` (UI/Theming)
  - `../services/AppLogger` (Telemetry and error logging)
- **Exports Out:**
  - Exposes `AuthContext` (`useAuth`) and `profileService` methods to the rest of the app.
  - Used heavily by any screen/feature needing user profile access, crew data, or authentication state enforcement.

## 3. Context Matrix
- **Provided Contexts:**
  - `AuthContext`: Provides `{ session, user, isOfflineMode, isAuthenticated, sessionLoaded, sessionExpired, setIsOfflineMode, clearOfflineMode }` to the entire application subtree.
- **Consumed Contexts:**
  - `AuthContext`: Consumed locally by `SkaterStatsPanel.tsx` (via `useAuth()`) to determine the current user before querying cached or remote lifetime stats.

## 4. Hook/Service I/O Registry
- **`useAuth()`**
  - **Inputs:** None.
  - **Outputs:** `AuthContextValue` (session, user, boolean auth states, and offline mode modifiers).
  - **Side-Effects:** Subscribes to Supabase auth state changes, parses deep links for OAuth tokens, and persists offline mode toggles to AsyncStorage.
- **`AuthProfileService.fetchOrCreateProfile(user)`**
  - **Inputs:** `user` object from Auth context.
  - **Outputs:** `Promise<UserProfile | null>`.
  - **Side-Effects:** Mutates `user_profiles` to self-heal missing metadata or create default profiles derived from auth claims.
- **`AuthProfileService.updateProfile(userId, fields)`**
  - **Inputs:** `userId` (string), `fields` (Partial<UserProfile>).
  - **Outputs:** `Promise<void>`.
  - **Side-Effects:** Updates `user_profiles` database table and enforces unique constraint rules (e.g., username availability).
- **`AuthProfileService.getSessionHistory(userId)`**
  - **Inputs:** `userId` (string).
  - **Outputs:** `Promise<SessionHistoryItem[]>`.
  - **Side-Effects:** Queries `crew_members` and its relation to `crew_sessions` to aggregate history.
- **`CrewProfileService` (Key Methods)**
  - **Inputs:** Various (e.g., `crewId`, `name`, `opts`, `userIds`).
  - **Outputs:** Models like `PermanentCrew[]`, `CrewMemberFull[]`, or aggregated stats object.
  - **Side-Effects:** Mutates `crews`, `crew_memberships`. Handles complex teardowns (e.g., actively broadcasting `session_ended` messages to Realtime channels before tearing down a crew in `deleteCrew`).
- **Account Tab Components (`AccountTab*.tsx`)**
  - **Inputs:** Highly prop-driven (Callbacks: `handleCreateCrew`, `handleSaveProfile`, `handleSignOut`; State properties: `savingProfile`, `isDark`, `crews`, `devices`).
  - **Outputs:** Rendered React View/ScrollView hierarchies.
  - **Side-Effects:** Limited directly; mutations and network calls are elevated to the parent orchestrator via prop callbacks. `SkaterStatsPanel.tsx` is an exception, fetching and caching stats directly in `useEffect`.

<!-- CARTOGRAPHER_END: IDENTITY -->

### 12.2 BLE Protocol Core
<!-- CARTOGRAPHER_START: BLE_CORE -->

### 1. File Manifest
*   **Orchestrator**: 
    *   `src/hooks/useBLE.ts` (Thin Orchestrator API)
*   **State Machine**:
    *   `src/services/ble/BleMachine.ts` (XState V5 Engine)
    *   `src/services/ble/BleMachine.types.ts`
*   **BLE Sub-Hooks**:
    *   `src/hooks/ble/useBLEAutoRecovery.ts` (Organic disconnect backoff loops)
    *   `src/hooks/ble/useBLEScanner.ts` (Active user-initiated discovery)
    *   `src/hooks/ble/useBLESweeper.ts` (Battery-adaptive background listener)
    *   `src/hooks/ble/useBLEHeartbeat.ts` (45s connection liveness queries)
    *   `src/hooks/ble/useBLERSSIMonitor.ts` (30s post-connect signal tracker)
    *   `src/hooks/ble/useBLEGattMutex.ts` (4-Tier Async GATT operation serializer)

### 2. Blast Radius
*   **`useBLE.ts`** serves as the public API `BluetoothLowEnergyApi` for UI layers (`DashboardScreen`, `HardwareSetupWizardScreen`). Modifying this hook directly affects all BLE UX interactions.
*   **`useBLEGattMutex`** globally regulates radio traffic. Breaking the preemption chain (AbortControllers) here will instantly cascade into Android GATT 133 contention errors across the app.
*   **`useBLEAutoRecovery`** coordinates with `BleSessionFactory` and `useBLEGattMutex` (Priority 2). It also relies heavily on `onGroupDropout` mapping; failing to batch here recreates stampeding-herd reconnect storms.
*   **`useBLESweeper`** manages `hwCache` (EEPROM cache mapped in `AsyncStorage`). Changes to caching here impact the Setup Wizard's ability to render hardware shapes correctly without latency.

### 3. Context Matrix
*   **Concurrency Model**: Serialized Async via `useBLEGattMutex.ts`. P1 (User Action) > P2 (Auto-Recovery) > P3 (Background EEPROM Probe) > P4 (Passive Sweep). P1 actively aborts P2/P3 flights via `AbortController`.
*   **State Distribution**: 
    *   *High-Level Phase Limits:* `BleMachine.ts` dictates discrete state allowed (e.g., cannot CONNECT while DISCONNECTING). 
    *   *Low-Level Volatility:* Stored entirely in module-level `useRef` elements (like `mtuMapRef`, `adapterMapRef`, `connectedDevicesRef`) bypassing React state to guarantee cycle-accurate callbacks and zero render-thrashing.
*   **Event Handling**: Organic drops are captured by stable ref callbacks passed to `bleManager.onDeviceDisconnected`, forwarding immediately to `autoRecovery.initiateRecovery(deviceId)` bypassing UI thread delays.

### 4. Hook/Service I/O Registry
*   **`useBLE.ts`**:
    *   *IN:* `registeredMacs`
    *   *OUT:* `connectToDevices`, `writeToDevice`, `pingDevice`, `startSweeper`, `connectedDevices`, `rssiMap`
*   **`useBLEAutoRecovery`**:
    *   *IN:* `onGroupDropout`, `connectedDevicesRef`
    *   *OUT:* `initiateRecovery`, `cancelAllRecoveries`, `ghostedDeviceIds`
*   **`useBLESweeper`**:
    *   *IN:* `registeredMacs`
    *   *OUT:* `hwCache` (EEPROM payload), `startSweeper`/`stopSweeper`/`burstScan`
*   **`useBLEHeartbeat` & `useBLERSSIMonitor`**:
    *   *IN:* `connectedDevicesRef`
    *   *OUT:* `onStaleLinkDetected` (Heartbeat), `rssiMap` & `onCriticalSignal` (RSSI Monitor)
*   **`useBLEGattMutex`**:
    *   *IN:* `priority` (1-4), `timeoutMs`
    *   *OUT:* `GattLockHandle` (`{ release, signal }`)

### 5. State Machine (FSM) Map
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
    READY --> IDLE : UPDATE_CONNECTED_DEVICES (0 devices)
    
    DISCONNECTING --> IDLE : DISCONNECT_COMPLETE
    
    RECOVERING --> READY : RECOVERY_COMPLETE (devices > 0)
    RECOVERING --> IDLE : RECOVERY_COMPLETE (0 devices)
    RECOVERING --> CONNECTING : CONNECT_REQUEST
    RECOVERING --> IDLE : RECOVERY_FAIL

    note left of IDLE : FORCE_IDLE forces transition to IDLE from ANY state
```

<!-- CARTOGRAPHER_END: BLE_CORE -->

### 12.3 Group Sync & Swarm
<!-- CARTOGRAPHER_START: GROUP_SYNC -->

## 1. File Manifest
- **`src/services/CrewService.ts`**: The real-time engine. Manages `crew_sessions` DB writes, Auto-Rejoin via `AsyncStorage` TTLs, and handles `Supabase Realtime` channel broadcasts.
- **`src/services/GroupRepository.ts`**: The persistence SSOT. Extracted from `DeviceRepository.ts` (Phase 3.5), it orchestrates offline-first local state and transactional cloud syncs via junction table RPC.
- **`src/context/CrewContext.tsx`**: The orchestrator. Combines `useCrewHub`, `useCrewManage`, and `useCrewSession` into a single injectable interface (`CrewContextValue`).
- **`src/components/crew/CrewLandingScreen.tsx`**: The main Hub UI. Renders "Live Near You" map (via `CrewLandingMap`), handles active session browsing, and coordinates public/private joining patterns.
- **`src/components/crew/CrewDetailScreen.tsx`**: The granular inspection UI. Exposes crew stats, ownership transfer, invite codes, and inline editing workflows.

## 2. Blast Radius
- **Supabase Realtime**: Edits to `CrewService.ts` directly impact the `crew:{sessionId}` channels, breaking the 150ms throttled `broadcastScene` loop or `session_ended` teardowns.
- **`DeviceRepository` Delegate Map**: Modifying `GroupRepository.ts` can fracture the `GroupDeviceDelegate` injection pattern, leading to infinite Metro loops or desynced `<RegisteredDevice[]>` group derivations.
- **`AsyncStorage` TTL Pipeline**: Real-time reconnection relies strictly on `ng_crew_last_session_id` / `ng_crew_last_session_exp`. Modifying auto-rejoin logic impacts session recovery after backgrounding.

## 3. Context Matrix
- **`CrewContext` (State Trio)**
  - `hub`: Managed by `useCrewHub`. Owns geo-radius discovery, map filtering, and active vs. permanent session polling.
  - `manage`: Managed by `useCrewManage`. Owns administrative flows: roster editing, ownership assignment (`makeOwnerFor`), kicking (`isRemovingUserFor`), and search lookups.
  - `session`: Managed by `useCrewSession`. Owns active user-state during a session (`executeLeaveSession`, `executeEndSession`, `isHandoffMode`).
- **`GroupDeviceDelegate`**: The crucial injection context that prevents circular imports. Allows `GroupRepository` to instruct `DeviceRepository` to bulk-update `group_ids` on device config edits.

## 4. Hook/Service I/O Registry
- **`CrewService`**:
  - `createSession(name, displayName, opts)` → `Promise<CrewSession>`
  - `joinSession(inviteCode, displayName)` / `joinSessionById(...)` → `Promise<CrewSession>`
  - `endSession(explicitSessionId?)` → RLS-safe DB termination and `session_ended` network broadcast.
  - `broadcastScene(scene)` → Throttled (150ms debounce) payload pusher + 5s throttled `last_scene` DB sync.
- **`GroupRepository`**:
  - `saveGroupTransactional(groupId, groupName, deviceMacs)` → Atomically upserts the group and applies bulk device membership updates.
  - `flushPendingGroups(userId, currentDevices)` → Flushes the `@Sk8lytz_pending_group_sync` offline queue upon reconnection.

<!-- CARTOGRAPHER_END: GROUP_SYNC -->

### 12.4 Control Surfaces
<!-- CARTOGRAPHER_START: UI_CONTROLS -->

## 1. File Manifest
* **`src/components/DockedController.tsx`**: Primary routing shell and UI layout engine for LED control (Hollow Shell v3). Delegates panel rendering via `DockedBus`.
* **`src/screens/DashboardScreen.tsx`**: Root application monolith co-locating BLE connection lifecycle, 4-slab layout, and domain-driven hooks.
* **`src/screens/AuthScreen.tsx`**: Authentication gateway with Offline mode, Sandbox toggles, and Session Expiry states.
* **`src/screens/Onboarding/*`**: FTUE wizards for hardware setup (BLE discovery) and OS compliance gates.

## 2. Blast Radius
* **Dependencies**: `useOptimisticBLE`, `useProtocolDispatch`, `useStreetMode`, `useDashboardGroups`, `useDashboardAutoConnect`, `useDashboardProfile`, `useDashboardCrew`, `useControllerAnalytics`, `useSessionTracking`, `useAppMicrophone`, `useDeviceStateLedger`.
* **Dependents**: `App.tsx` (mounts screens), `DashboardScreen` (mounts `DockedController` and dashboard slabs/modals).

## 3. Context Matrix
* **Consumed Contexts**: `ThemeContext` (Colors, Spacing, Typography), `BLEContext` (GATT connections, discovery sweeps), `FavoritesContext` (preset states), `SessionContext` (telemetry/tracking).

## 4. Hook/Service I/O Registry
* **`useOptimisticBLE`**: Immediately fires haptics and updates UI local state before dispatching BLE payload (Ghost Standard).
* **`useStreetMode`**: Automatically intercepts BLE payloads when G-forces cross thresholds via `DashboardScreen` telemetry.
* **`useDashboardGroups`**: Bridges UI selections to physical fleet config, handling selection state and group CRUD.
* **`useDashboardAutoConnect`**: Spawns staggered background routines to auto-connect to hardware and manages the 500ms debounce queue.
* **`useDashboardCrew`**: Orchestrates Crew Hub state, auto-rejoin, and crew mode summary broadcasts.

## 5. Design System & Token Manifest
* **Colors (Dark / Light)**:
  * `background`: `#1B4279` / `#EAEFF5`
  * `surface`: `#245596` / `#CBD6E2`
  * `surfaceHighlight`: `#3172C9` / `#DDE5EE`
  * `primary`: `#FF5A00`
  * `secondary`: `#FFB800`
  * `accent`: `#FF3300` / `#1B4279`
* **Spacing**:
  * `xxs`: 2, `xs`: 4, `sm`: 8, `md`: 12, `lg`: 16, `xl`: 24, `xxl`: 32, `xxxl`: 40, `huge`: 48, `giant`: 64
* **Typography**:
  * `header`: 24px uppercase (Righteous, 2px tracking)
  * `title`: 16px (Righteous, 0.5px tracking)
  * `body`: 14px (Righteous)
  * `caption`: 11px (Righteous)
* **Layout Constants**:
  * `padding`: `Spacing.lg` (16)
  * `borderRadius`: `Spacing.xl` (24)

<!-- CARTOGRAPHER_END: UI_CONTROLS -->

### 12.5 Data & Telemetry
<!-- CARTOGRAPHER_START: DATA_LAYER -->

### 1. File Manifest
*   **`src/services/DeviceRepository.ts`**: Single Source of Truth (SSOT) for Device & Group Persistence. Manages AsyncStorage (local-first) and Supabase (cloud-second), including offline queueing and a tombstone pattern to prevent deleted devices from being resurrected by the cloud.
*   **`src/services/TelemetryService.ts`**: **[DOES NOT EXIST]** The architecture is natively hook-driven (e.g., `useGlobalTelemetry`, `useAdminTelemetry`, `useTelemetryLedger`). See the stale docs section below.
*   **`src/types/supabase.ts`**: Autogenerated TypeScript definitions for the PostgREST schema, strictly typing tables like `registered_devices`, `registered_groups`, and `admin_audit_logs`.
*   **`src/services/supabaseClient.ts`**: Initializes the Supabase client mapped to the `Database` type. Injects a `SecureStoreAdapter` (`expo-secure-store`) for React Native token persistence and provides an offline fallback mock if env vars are missing.

### 2. Blast Radius
*   **Device Lifecycle & State**: Changes to `DeviceRepository` instantly affect hooks like `useRegistration` and `useDashboardGroups`, which subscribe to its observer pattern.
*   **Offline/Cloud Sync Loop**: The sync engine (`syncFromCloud`, `_flushPendingSync`) tightly couples local AsyncStorage keys (`@Sk8lytz_registered_devices`, `@Sk8lytz_pending_sync`, `@Sk8lytz_deleted_macs`) with Supabase RPC calls. Modifying the data shape risks breaking the offline sync queue or the tombstone filter.
*   **Authentication & RLS**: The `supabaseClient.ts` initialization dictates how session tokens are stored. Breaking this breaks RLS policies and user authentication across the entire app.

### 3. Context Matrix
*   **Domain**: `DATA_LAYER`
*   **State Model**: Singleton (`DeviceRepository.getInstance()`) using a custom pub/sub observer model (`subscribe`, `getVersion`) designed for React's `useSyncExternalStore`.
*   **Persistence Strategy**: Local-First / Optimistic UI. 
    *   **Writes**: Saved to AsyncStorage immediately, then async-pushed to Supabase.
    *   **Deletes**: Uses a Tombstone pattern (`@Sk8lytz_deleted_macs`) locally before attempting network deletion to survive offline states.
*   **Reactivity**: Returns shallow copies of state arrays (`[...this.devices]`) to guarantee React reference-identity re-renders.

### 4. Hook/Service I/O Registry
*   **`DeviceRepository.getInstance()`**
    *   `saveDevice(device)` ➔ `Promise<boolean>` *(Upserts local + cloud, handles fallback to pending queue)*
    *   `deleteDevice(mac)` ➔ `Promise<void>` *(Appends to tombstone, removes local, deletes cloud)*
    *   `syncFromCloud()` ➔ `Promise<RegisteredDevice[]>` *(Pulls cloud, filters by tombstones, merges via local-first rules, flushes queues)*
    *   `checkDeviceClaimed(mac, fingerprint?)` ➔ `'unclaimed' | 'claimed_by_self' | 'claimed_by_other' | 'offline_unknown'`
*   **`supabase`** (from `supabaseClient.ts`)
    *   `createClient<Database>()` ➔ Fully typed Supabase client with offline mode bypass.

### 5. Database Schema & RLS Policies
*   **Schema Map**:
    *   `registered_devices`: Primary device table mapped by `user_id` and `device_mac`. Stores hardware configs (`led_points`, `segments`, `strip_type`, `sorting`).
    *   `registered_groups`: Fleet management mapped by `id` and `user_id`.
    *   `device_group_members`: Join table for device-to-group relation.
*   **Security & RLS Rules** (Inferred & Enforced by Client Architecture):
    *   **User Isolation**: All DML operations strictly filter by `user_id = auth.uid()` natively via Supabase RLS. The repository mirrors this logic locally (e.g., `eq('user_id', user.id)`).
    *   **Atomic Upserts**: Groups and Devices are upserted transactionally via the RPC `upsert_group_with_devices` to prevent orphaned devices during RLS checks.

### 6. Environment/Secrets Manifest
*   `EXPO_PUBLIC_SUPABASE_URL` *(Required)*: The endpoint URL for the Supabase project.
*   `EXPO_PUBLIC_SUPABASE_ANON_KEY` *(Required)*: The public anonymous API key for client-side Supabase requests.

<!-- CARTOGRAPHER_END: DATA_LAYER -->

### 12.6 Utilities & Types
<!-- CARTOGRAPHER_START: UTILS -->

## 1. File Manifest
**src/utils/**
- `BlePayloadParser.ts`: Pure Utility Gatekeeper for BLE Protocol Context Parsing. Deterministically parses LED/RF payload bytes from the hardware.
- `ColorUtils.ts`: Pure color conversion math (hex/hue/RGB) and the critical `boostForLED` algorithm that maximizes HSV saturation for WS2812B vibrancy.
- `presetColorUtils.ts`: Shared Color Resolution Utilities for rendering UI gradient and glow colors for Preset and Group cards (handling `GENERATIVE` vs `FG_ONLY` edge cases).
- `MusicDictionary.ts`: Authoritative registry for the 46 hardware-native music profiles across Light Bar (0x26) and Light Screen (0x27) matrices.
- `classifyBLEDevice.ts`: Central logic mapping raw BLE device properties to a standard `PendingRegistration` object using EEPROM and advertisement heuristics.
- `NamingUtils.ts`, `NormalizationUtils.ts`, `kMeansPalette.ts`, `migrateAuthTokens.ts`: Small utility helpers for naming generation, clustering, and data mapping.

**src/types/**
- `ProductCatalog.ts`: Defines `ProductProfile` and `VizShape` types, establishing the catalog schemas for HALOZ, SOULZ, and RAILZ products.
- `ble.types.ts`: Domain-specific BLE pipeline types (`BleConnectionRequest`, `RegisteredDeviceRow`, `RegisteredGroup`) and re-exported `react-native-ble-plx` types to prevent `any` casts.
- `dashboard.types.ts`: Shared Domain Type Contracts. Houses the core data models (`DevicePatternState`, `PingResult`, `DeviceSettings`) and State Machine definitions (`BleConnectionState`, `SessionState`, `GroupModalState`).
- `supabase.ts`: Autogenerated Database types.

## 2. Blast Radius
- **`src/types/dashboard.types.ts` (CRITICAL)**: The backbone of the application state. Modifying FSM shapes or data models here requires refactoring across all UI views and React hooks.
- **`src/types/ble.types.ts` (HIGH)**: Shapes the inputs to the core BLE Connection Manager. Changes to `BleConnectionRequest` will impact the initialization of the entire BLE pipeline.
- **`src/utils/ColorUtils.ts` & `presetColorUtils.ts` (HIGH)**: Modifying the conversion math or `GENERATIVE_RAINBOW` resolutions directly affects the visual rendering of the UI across dashboards, preset cards, and color sliders.
- **`src/types/ProductCatalog.ts` (MEDIUM)**: Modifying this impacts FTUE (First Time User Experience) classification thresholds and Visualizer geometries.
- **`src/utils/BlePayloadParser.ts` & `classifyBLEDevice.ts` (MEDIUM)**: Controls data mapping from the BLE adapter to the app layer. Bugs here will result in missing hardware telemetry or improper setup wizards.

## 3. Context Matrix
- **Hardware Config & Provisioning**: `classifyBLEDevice.ts` relies on `ProductCatalog.ts` (for auto-detection thresholds) and generates objects defined in `dashboard.types.ts`.
- **UI Rendering Pipeline**: `presetColorUtils.ts` relies heavily on `dashboard.types.ts` (`IFavoriteState`, `GroupPatternSnapshot`) to resolve the exact arrays consumed by `LinearGradient` components on the dashboard.
- **BLE Telemetry Pipeline**: `BlePayloadParser.ts` normalizes incoming byte arrays for ingestion into the Supabase `device_diagnostics` table and local `PingResult` states.

## 4. Hook/Service I/O Registry
- **ColorUtils**
  - `boostForLED(r: number, g: number, b: number) => { r, g, b }`
  - `hexToHue(hex: string) => number`
  - `hueToHex(hue: number) => string`
- **BlePayloadParser**
  - `parseLedPayload(payload: number[]) => ParsedLedConfig | null`
  - `parseRfPayload(payload: number[]) => ParsedRfConfig | null`
- **presetColorUtils**
  - `resolveGlowColor(fav: IFavoriteState, fallback: string) => string`
  - `resolveGradientColors(fav: IFavoriteState, glow: string) => string[]`
  - `resolveGroupCardColors(snapshot: GroupPatternSnapshot, fallback: string[]) => string[]`
- **classifyBLEDevice**
  - `resolveProductType(device: BLERawDevice, hwCache?) => string`
  - `mapDeviceToRegistration(device, index, hwCache?, productType?) => PendingRegistration`
- **MusicDictionary**
  - `getMusicProfiles(modeType: number) => MusicProfile[]`
  - `getActiveMusicProfile(modeType: number, patternId: number) => MusicProfile`

## 5. Design System & Token Manifest
- **Color Palette Constants**:
  - `COLOR_PRESET_PALETTE`: 10-color standard palette used in the color grid (`'#FF0000', '#FF8000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#800080', '#FF00FF', '#FFFFFF', '#000000'`).
  - `PRESET_HUE_MAP`: Fixed map of preset hex colors to hue values (0-360) for instant slider synchronization.
  - `GENERATIVE_RAINBOW`: 7-stop vibrant gradient (`'#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#00BFFF', '#0000FF', '#8B00FF'`) used to visually represent generative patterns (e.g., Rainbow, Aurora) where fixed colors are dynamically generated by hardware.
- **Color Processing Engine Tokens**:
  - *Saturation Boost Thresholds* (`boostForLED`): Neutral gate skips boosting if `S < 0.20`. Boost maximizes saturation to `1.0` and value to `1.0` to force maximum vividness on WS2812B addressable LEDs.

<!-- CARTOGRAPHER_END: UTILS -->

### NATIVE & WATCH\n<!-- CARTOGRAPHER_START: NATIVE_&_WATCH -->

## 1. File Manifest
**Android Core**
- `android/app/src/main/java/com/neogleamz/sk8lytz/MainActivity.kt`: React Native Activity entry point. Coordinates splash screen, Fabric architecture, Android S back-button behavior, and delegates HealthConnect permissions.
- `android/app/src/main/java/com/neogleamz/sk8lytz/MainApplication.kt`: Core ReactApplication class. Sets up `ExpoReactHostFactory`, application lifecycle dispatcher, and React Native package registry.

**watchOS Companion (targets/watch/)**
- `targets/watch/ContentView.swift`: Main session dashboard UI. Renders live speed, heart rate, calories, and an elapsed session timer anchored to a phone-authoritative ISO-8601 timestamp.
- `targets/watch/WatchConnectivityManager.swift`: Single source of truth for `WCSession` state. Handles bidirectional communication, receiving active session status from the phone and relaying health telemetry back on a 5s heartbeat.
- `targets/watch/HealthManager.swift`: Manages `HKWorkoutSession` (.skatingSports) and `HKLiveWorkoutBuilder`. Captures live heart rate and active energy burned via HealthKit sensors during active sessions.
- `targets/watch/ComplicationController.swift`: Provides real-time watchOS complications (Graphic Circular, Modular Small, Graphic Corner) driven by `WatchConnectivityManager` speed state.

## 2. Blast Radius
- **`WatchConnectivityManager.swift` (HIGH)**: Modifying payload schemas (`command`, `status`, `speed`, `healthUpdate`) or `WCSession` lifecycle directly affects the `sk8lytz-watch-bridge` module on iOS. Breakages desync the watch from the phone, halting telemetry and remote session controls.
- **`HealthManager.swift` (HIGH)**: Changes to `HKWorkoutSession` lifecycle or HealthKit entitlement scopes require parallel changes in iOS entitlements. Misconfiguration leads to missing HR/calorie data or background session termination.
- **`MainActivity.kt` (MEDIUM)**: Edits to `HealthConnectPermissionDelegate` or React Activity lifecycles could break Android Health Connect authorization flows or Expo module initialization.

## 3. Context Matrix
- **Data Flow**: The phone is the authoritative master for session state and GPS speed. The watch acts as a slave display for speed/duration, while acting as the authoritative real-time sensor for heart rate and calories (relay mode).
- **Time Syncing**: Elapsed session duration on the watch is not computed via local independent ticks. It is strictly anchored to the phone-authoritative UTC `startTime` pushed via `WCSession` to ensure perfect parity.
- **Health Pipeline Priority**: Telemetry emitted from `HealthManager.swift` (via `WCSession` relay) is ingested by the phone's `useHealthTelemetry` hook, which uses a 15s expiry timeout to prioritize real-time watch data over staler OS-level HealthKit polling.

## 4. Hook/Service I/O Registry
- **`WatchConnectivityManager` (iOS/watchOS)**
  - *IN (from Phone)*: `didReceiveApplicationContext` / `didReceiveMessage` → `{ status, startTime, speed, calories, ...summary }`
  - *OUT (to Phone)*: `send(["command": "START_SESSION"])`, `send(["command": "STOP_SESSION"])`, `send(["healthUpdate": true, "heartRate": Int, "calories": Int])`
- **`HealthManager` (watchOS)**
  - *IN*: `startWorkout()`, `stopWorkout()`
  - *OUT*: `currentHeartRate`, `activeCalories` (published to `WatchConnectivityManager`)
- **`MainActivity` (Android)**
  - *IN*: App Launch
  - *OUT*: React Native initialization, `HealthConnectPermissionDelegate.setPermissionDelegate(this)`

<!-- CARTOGRAPHER_END: NATIVE_&_WATCH -->\n\n### NOTIFICATIONS & ROUTING\n<!-- CARTOGRAPHER_START: NOTIFICATIONS_&_ROUTING -->

## 1. File Manifest
- `App.tsx`: Root entry point. Sets up the global provider tree, handles conditional routing (`DashboardScreen` vs `AuthScreen`), and initializes crash/error boundaries (`SafeErrorBoundary`).
- `src/providers/BluetoothGuard.tsx`: Strict interceptor gate that blocks access to the app content if Bluetooth permissions are missing or if Bluetooth hardware is disabled. 
- `src/providers/ComplianceGate.tsx`: Interceptor provider wrapping `DashboardScreen` that checks if the user has accepted the most recent EULA version, displaying `EulaModal` if non-compliant. Supports both Cloud and Offline modes via AsyncStorage bridging.
- `src/services/NotificationService.ts`: Core wrapper for `expo-notifications`. Manages push token generation, local notification scheduling (Session Reminders, Crew Invites), Android notification channels, and app-foregrounding response handlers.
- `src/services/PushTokenService.ts`: Responsible for persisting the device's Expo push token into the `push_tokens` table in Supabase.

*(Note: `src/navigation/` directory does not exist. The app utilizes a flat navigation architecture relying on conditional component rendering in `App.tsx` and state-driven Modals rather than a dedicated navigator library like React Navigation).*

## 2. Blast Radius
- **App.tsx**: Modifications here affect the entire component lifecycle, global error capture (white-screen prevention), offline-mode hydration logic, and provider hierarchy.
- **BluetoothGuard.tsx**: Critical failure point. Breaking this component will hard-lock skaters out of the app with an un-dismissible Bluetooth requirement screen.
- **ComplianceGate.tsx**: Changes could trap users in an infinite EULA loop or bypass legal compliance gating entirely.
- **NotificationService.ts / PushTokenService.ts**: Breakages will cause missed Session Start events and Crew Invites. Failures in Expo Token retrieval must be handled gracefully to prevent blocking app initialization.

## 3. Context Matrix
- **AuthContext** (`useAuth`): Controls the primary routing pivot in `App.tsx` via `isAuthenticated`, `isOfflineMode`, and `sessionLoaded`.
- **ThemeContext** (`useTheme`): Consumed universally across `App.tsx`, `BluetoothGuard`, and `ComplianceGate` to enforce correct Dark/Light mode color palettes.
- **BLEContext** (`useSharedBLE`): Read by `BluetoothGuard` to continuously monitor `isBluetoothEnabled` and `isBluetoothSupported` states to correctly block/allow entry.

## 4. Hook/Service I/O Registry
### NotificationService
- `init(autoRequest: boolean) => Promise<string | null>`: Bootstraps Android channels, requests Expo push token permissions, and registers the token via `PushTokenService`.
- `setJoinHandler(handler: JoinHandler) => void`: Wires the response listener invoked when a Session/Crew notification is tapped.
- `sendCrewInviteNotification(opts) => Promise<void>`: Dispatches an immediate local push notification for crew joins.
- `sendSessionStartingSoon(opts) => Promise<string | null>`: Schedules a local notification trigger 15 minutes prior to a session's scheduled time.
- `sendSessionLiveAlert(opts) => Promise<void>`: Dispatches an immediate local push notification when a session goes live.
- `cleanup() => Promise<void>`: Purges notification listeners and triggers push token unregistration.

### PushTokenService
- `registerPushToken(token: string, platform: 'ios'|'android'|'web', userId: string | null) => Promise<void>`: Upserts token to Supabase `push_tokens` table.
- `unregisterPushToken(token: string, userId: string | null) => Promise<void>`: Deletes token from Supabase `push_tokens` table.

<!-- CARTOGRAPHER_END: NOTIFICATIONS_&_ROUTING -->\n\n### SESSION TRACKING\n<!-- CARTOGRAPHER_START: SESSION_TRACKING -->

## 1. File Manifest
*   **`src/context/SessionContext.tsx`** (The Orchestrator): Manages the high-level session state machine (`IDLE`, `ACTIVE`, `PAUSED`, `ENDING`). Handles Notifee foreground services (for Android 14+ background GPS survival), Apple Watch/Wear OS sync via `WatchBridge`, auto-pause/resume logic, and AsyncStorage crash recovery.
*   **`src/hooks/useGlobalTelemetry.ts`** (The Sensor Engine): Hooks into `expo-location` and `expo-sensors` (Accelerometer). Accumulates `sessionDistanceMiles`, `sessionDurationSec`, `gpsSpeed`, `peakSpeed`, and `peakGForce`. Decoupled from BLE connections to prevent dropouts from ruining session metrics.
*   **`src/services/SpeedTrackingService.ts`** (The Persistence Layer): Handles saving completed sessions to Supabase (`skate_sessions`), computing lifetime aggregates, estimating MET-based calories (if no watch HR is present), and managing an offline queue.
*   **`src/hooks/useHealthTelemetry.ts`** (The Vitals Injector): Pulls real-time BPM and calories from `sk8lytz-watch-bridge` and injects them into the session context.

## 2. Blast Radius (Coupling & Risks)
If the Session Tracking domain is modified, the following systems are at risk:
*   **Wearable Sync (`sk8lytz-watch-bridge`)**: Tightly coupled. `SessionContext` pushes phase changes (`START`, `STOP`, `SUMMARY`) and `SpeedTrackingService` pushes live throttled metrics (speed, hr, calories). Altering state shapes will break the watch HUD.
*   **Background Survival (`@notifee/react-native`)**: Android 14 FGS (Foreground Service) strictly requires the user to be in the app with Location permissions granted when `ACTIVE` triggers. Modifying the lifecycle risks `SecurityException` crashes.
*   **Crew Hub (`CrewService.ts`)**: `useGlobalTelemetry` directly mutates `crewService.sessionTelemetry` (live distance/speed sharing) when in a crew.
*   **HealthKit/Health Connect (`HealthSyncService.ts`)**: SpeedTrackingService delegates to HealthSyncService to close the user's activity rings after a successful save.
*   **Crash Recovery (`AsyncStorage`)**: Changes to the FSM phase names will break the `@sk8lytz_session_phase` recovery checks on app boot.

## 3. Context Matrix (State & Lifecycles)
*   **State Machine**: `IDLE` → `ACTIVE` ↔ `PAUSED` → `ENDING` → `IDLE`.
*   **Crash Recovery**: `SessionContext` reads `SESSION_PHASE_KEY` and `@sk8lytz_pending_bg_end` on app mount/foreground. If the app is killed while `ACTIVE`, it resumes seamlessly or fires a deferred teardown.
*   **Auto-Pause**: Managed by `SessionContext`. If `telemetry.gpsSpeed < 0.2 mph` for 10s, it transitions to `PAUSED`. Resumes instantly on movement.
*   **Offline First**: Unauthenticated or offline sessions are serialized by `SpeedTrackingService` into `PENDING_SESSION_QUEUE_KEY` and flushed via `useOfflineSyncWorker` every 60s once online.

## 4. Hook/Service I/O Registry
*   **`useSession(): SessionContextValue`**
    *   **Returns**: `{ isSkateSessionActive: boolean, sessionPhase: string, startSession: fn, endSession: fn, telemetry: GlobalTelemetryState, health: HealthTelemetry }`
*   **`useGlobalTelemetry(sessionPhase, healthMetrics, externalStartTimeMs)`**
    *   **Returns**: `{ gpsSpeed, peakGForce, sessionDistanceMiles, sessionDurationSec, sessionPeakSpeed, sessionAvgSpeed }`
*   **`SpeedTrackingService.saveSession(snapshot: ISessionSnapshot, userId: string | null)`**
    *   **Behavior**: Inserts into `skate_sessions` or writes to local offline queue if `userId` is null.
    *   **Returns**: `Promise<string | null>` (the session UUID).
*   **`SpeedTrackingService.pushSpeedToWatch(speedMph, calories, hr)`**
    *   **Behavior**: Throttled 3-second fire-and-forget payload over `WatchBridge.sendMetricUpdate`.

<!-- CARTOGRAPHER_END: SESSION_TRACKING -->\n\n### HARDWARE PROTOCOLS\n<!-- CARTOGRAPHER_START: HARDWARE_PROTOCOLS -->

## 1. File Manifest
* **IControllerProtocol.ts**: Hardware Abstraction Layer (HAL) defining uniform interface for BLE controllers. Declares the universal `ProtocolResult`.
* **ZenggeAdapter.ts**: `IControllerProtocol` implementation for Zengge hardware (0xA3 chipset). Owns independent `messageCounter` and handles 0x40 MTU chunking logic.
* **ZenggeProtocol.ts**: Low-level packet builder for all Zengge opcodes (0x59, 0x51, 0x73, 0x62, 0x63).
* **BanlanxAdapter.ts**: `IControllerProtocol` implementation for BanlanX SP621E. Employs native hardware FFT (`requiresSoftwareFFT = false`) and 0xA0 prefix payloads.
* **ControllerRegistry.ts**: Runtime protocol resolver. Dispatches adapters based on BLE advertisement data (FFE0 prioritizing BanlanX, FFFF prioritizing Zengge).
* **PatternEngine.ts**: Source of truth for 81 pattern templates (`SK8LYTZ_TEMPLATES`). The master dispatcher deciding between TS math → `0x59` spatial arrays vs. `0x51` native temporal intercepts.
* **SpatialEngine.ts**: 100% pure TS math engine generating spatial frame arrays (`RGB[]`) for spatial/wave/generative groups.
* **SymphonyEngine.ts**: Frame generator for native 0x51 Symphony effects and logic for the 13 Audio-Reactive Music modes.
* **PositionalMathBuffer.ts**: Engine for absolute positional node calculations (generates deterministic gradient sweeps bridging positional arrays).
* **VisualizerEngine.ts**: UI-facing array processor (handles continuous scroll rotations for spatial arrays).

## 2. Blast Radius
* **Protocol Output Breaking**: Modifying the `ProtocolResult` interface impacts the fundamental `useBLE.ts` transport layers.
* **Hardware EEPROM Breaking**: Modifying `IC_TYPES` or `COLOR_SORTING_RGB` arrays directly damages the payload logic in the `Sk8LytzProgrammerModal` and hardware setup flows.
* **Visualizer UI Parity Breaking**: Mutating math generator arrays in `SpatialEngine.ts` immediately impacts the `ProductVisualizer` UI canvas, as it relies on `getVisualizerFrame()` producing identical arrays to what hardware receives.
* **GATT Overflow Breaking**: Adjusting `safeMtu` chunking math (3-byte header overhead) in `ZenggeAdapter.prepareForTransmission` risks fatal GATT buffer overflows across budget Android chipsets.

## 3. Context Matrix
* **UI Dispatch** ➔ **PatternEngine** (`buildPatternPayload` orchestrates the target strategy).
* **TypeScript Math Layer** ➔ **SpatialEngine / SymphonyEngine** (builds exact `RGB[]` spatial array).
* **Transport Strategy Resolution** ➔ **ControllerRegistry** (returns active device `ZenggeAdapter` or `BanlanxAdapter`).
* **Byte Array Formatting** ➔ **ZenggeProtocol / BanlanxAdapter** (turns `RGB[]` into raw commands: `0x59` continuous cascade array vs. `0x51` multi-step array).
* **MTU Payload Slicing** ➔ **ZenggeAdapter (`prepareForTransmission`)** (applies 0x40 framing logic recursively across payloads).
* **Hardware Delivery** ➔ **BLE GATT Transport** (`useBLE.ts` writes priority queue packets).

## 4. Hook/Service I/O Registry
* **ControllerRegistry.resolveProtocol**: `(serviceUUIDs: string[], manufacturerData?: string) -> IControllerProtocol | null`
* **ZenggeAdapter.prepareForTransmission**: `(result: ProtocolResult, mtu: number) -> ProtocolResult` (slices > MTU payloads into `0x40` chunks with 3-byte prefix framing).
* **BanlanxAdapter.prepareForTransmission**: Passthrough (SP621E payloads natively fit inside standard MTU bounding, zero 0x40 chunking required).
* **PatternEngine.buildPatternPayload**: `(patternId, fg, bg, numLEDs, speed, direction, brightness, options, hardwareLedPoints) -> number[] | null` (Routes Group 9/10 to 0x51 compact, all else to 0x59 array).

## 5. Byte Offsets & Hardware Capabilities

### 1. Zengge Engine (0xA3 Hardware) Capabilities
* **Software FFT Engine:** `requiresSoftwareFFT = true`. App handles AudioContext and streams magnitudes via `0x74` array.
* **MTU Chunking (0x40):** Active. Payloads > (MTU - 3) fragmented. `[0x40, chunkIndex, totalChunks, ...chunkData]`.
* **Hardware Byte Maps:**
  * **0x59 (MultiColor Continuous Cascade):** 
    `[0x59, totalLenHi, totalLenLo, (R,G,B per px)..., ptsHi, ptsLo, transitionType(0x01-0x06), speed(1-100), dir(1/0), checksum]`
  * **0x51 (Compact Temporal Mode - 9 Bytes/slot):** 
    `[0x51, (0xF0, mode, speed(1-100), R1,G1,B1, R2,G2,B2)..., 0x0F, checksum]` (Max 18 steps safely fits default MTU)
  * **0x51 (Extended Compact Mode - 10 Bytes/slot):** 
    `[0x51, (0xF0, mode, speed, R1,G1,B1, R2,G2,B2, dirFlag)..., 0x0F, checksum]`. `dirFlag` = `0x80 | (dir === 1 ? 0x01 : 0x00)`.
  * **0x73 (Audio Reactivity Config - 13 Bytes):** 
    `[0x73, isOn(1/0), modeType(0x26/0x27), patternId, DropColor(R,G,B), ColumnColor(R,G,B), sensitivity(0-255), brightness, checksum]`
  * **0x62 (Hardware Config Write):**
    `[0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, checksum]`
  * **0x63 (Hardware Query Dual Support):**
    Supports JSON wrapper unpeeling (`[0]=0x00, [1]=0x63...`) or classic 12-byte payload.
  * **Power Commands:** ON = `[0x71, 0x23]` | OFF = `[0x71, 0x24]` (Replaced deprecated `0x56 0xAA` Scene ops).

### 2. BanlanX Engine (SP621E) Capabilities
* **Software FFT Engine:** `requiresSoftwareFFT = false`. SP621E has integrated math via `libwled_lfx.so`.
* **MTU Chunking:** Disabled. Packets are small enough to stream naturally.
* **Hardware Byte Maps (0xA0 Command Baseline):**
  * **Power Commands:** ON = `[0xA0, 0x50, 0x01, 0x01]` | OFF = `[0xA0, 0x50, 0x01, 0x00]`
  * **Solid Spatial Color:** `[0xA0, 0x52, 0x03, R, G, B]`
  * **Temporal Hardware Effect:** Requires 2 discrete packets with strict `20ms` delay gap:
    1. Select Engine: `[0xA0, 0x53, 0x01, effectId(1-142)]`
    2. Speed Injection: `[0xA0, 0x54, 0x01, speed(1-10)]` (Down-scaled from 1-100 logic)
  * **Audio Native Routing:** Requires 2 configuration packets:
    1. Input Hardware (Internal Mic): `[0xA0, 0x59, 0x01, 0x00]`
    2. Software Gain: `[0xA0, 0x5A, 0x01, gain(1-16)]`

<!-- CARTOGRAPHER_END: HARDWARE_PROTOCOLS -->\n\n### CLOUD FUNCTIONS\n<!-- CARTOGRAPHER_START: CLOUD_FUNCTIONS -->

## 1. File Manifest
- `supabase/functions/notify-crew-session/index.ts`: Deno Edge Function responsible for dispatching Expo push notifications to crew members when a leader starts a session.
- `supabase/migrations/*.sql`: 36 incremental SQL migrations defining the Postgres schema, RLS policies, custom RPCs (e.g., `delete_account`), telemetry configurations, and the Cultural Daemon/Scraper tables. Latest update: `20260606205739_add_notif_preferences_to_user_profiles.sql`.

## 2. Blast Radius
- **Edge Functions**: Modifications to `notify-crew-session` directly impact outbound push notifications via `exp.host`. A failure here disrupts crew alerts but will not crash the core client application, as the network call is decoupled from hardware opcodes.
- **Migrations**: Modifying established migration files will break the Supabase deployment pipeline. Altering existing schema signatures (like `registered_devices` or `push_tokens`) without running the client-side type generator will cause silent data-layer failures or fatal crashes in the PostgREST API layer.

## 3. Context Matrix
- **Auth Context**: The `notify-crew-session` function enforces strict authentication by validating the `Authorization: Bearer <JWT>` header via `supabase.auth.getUser()`.
- **Data Context**: Depends on `crew_memberships` (to resolve peer user IDs excluding the caller) and `push_tokens` (to extract physical device targets). It utilizes the `SUPABASE_SERVICE_ROLE_KEY` to bypass RLS for peer token resolution.
- **Trigger**: Fired by the client application when a crew leader transitions their session state to ACTIVE.

## 4. Hook/Service I/O Registry
### Edge Function: `notify-crew-session`
- **Auth**: Requires valid JWT Bearer token.
- **Inputs (POST JSON)**: 
  `{ crewId: string, sessionId: string, sessionName: string, leaderName: string }`
- **Outputs (JSON)**: 
  Success (200): `{ sent: number }` (Total pushes successfully handed off to Expo)
  Warning (200): `{ sent: 0, reason: "No members" | "No tokens" }`
  Error (400/401/405): Text response with HTTP error code.
- **External I/O**: `https://exp.host/--/api/v2/push/send` (Batches up to 100 messages per payload).

<!-- CARTOGRAPHER_END: CLOUD_FUNCTIONS -->\n\n### THEME & ASSETS\n<!-- CARTOGRAPHER_START: THEME_&_ASSETS -->

## 1. File Manifest
- `src/theme/theme.ts`: Core design system definitions, light/dark palettes, typography scale (`Righteous` font), and layout constants.
- `src/styles/DashboardStyles.ts`: Granular stylesheet implementing the 4-Slab Dashboard architecture. Contains `getPatternColors` utility for premium gradient resolution.
- `src/constants/AppConstants.ts`: Global application constants (`STORAGE_PREFIX`, `HW_SPEED_MAX`).
- `src/constants/ControlsRegistry.ts`: Definition of `CONTROLS_REGISTRY` encompassing feature flags and admin toggles (Governance, Hardware, Behavior, DangerZone).
- `src/constants/ProductCatalog.ts`: Offline-safe product profile catalog (`LOCAL_PRODUCT_CATALOG`) and device classification logic (`getLocalProfileById`, `getLocalProfileByPoints`).
- `src/constants/storageKeys.ts`: Master list of AsyncStorage persistence keys.

## 2. Blast Radius
- **`theme.ts`**: High risk. Modifications to `ThemePalette`, `Spacing`, or `Layout` affect every UI component globally.
- **`DashboardStyles.ts`**: High risk to Dashboard UI. Modifying `skateCardWrapper` or 4-slab container padding directly alters the structural layout of the dashboard view matrix.
- **`ProductCatalog.ts`**: CRITICAL risk. Altering LED array points (`defaultLedPoints`, `defaultSegments`) or visual thresholds directly breaks the 0x59 pattern rendering and visualizer hardware parity.
- **`ControlsRegistry.ts`**: Low to Medium risk. Altering keys affects Admin Tools rendering and available application feature flags.
- **`storageKeys.ts`**: Critical risk for data persistence. Changing a key without a migration script causes total offline data loss for that domain.

## 3. Context Matrix
- **Theme/Styles**: Tightly coupled with `DashboardScreen` and standard UI components. The `DashboardStyles` relies directly on `ThemePalette` values.
- **ProductCatalog**: Ingested by `useDashboardGroups`, hardware scanning heuristics, `ProductVisualizer.tsx`, and BLE Pattern Engines to ensure physical parity.
- **ControlsRegistry**: Consumed directly by `AdminToolsModal` and settings state management systems.

## 4. Hook/Service I/O Registry
- **`getPatternColors(patternName?: string, Colors?: ThemePalette)`**: Returns `[string, string]`. Infers 2-stop gradient colors from semantic names.
- **`getLocalProfileById(id: string)`**: Returns `ProductProfile | undefined`. Performs a case-insensitive product lookup.
- **`getLocalProfileByPoints(ledPoints: number)`**: Returns `ProductProfile`. Classifies devices based on point counts utilizing threshold checks (defaults to `SOULZ`).

## 5. Design System & Token Manifest
- **Palettes**:
  - `DarkColors`: Background `#1B4279`, Primary `#FF5A00`, Secondary `#FFB800`.
  - `LightColors`: Background `#EAEFF5`, Primary `#FF5A00`, Accent `#1B4279`.
- **Typography**: `Righteous` font family standard across all scales (Header: 24px, Title: 16px, Body: 14px, Caption: 11px).
- **Spacing Scale**: Granular increments from `xxs` (2) to `giant` (64).
- **Layout Tokens**: `padding` (16px), `borderRadius` (24px).
- **Visual Styling**: Glassmorphism overlays (`rgba(255,255,255,0.03)`), intense box shadows (`elevation: 10`, `shadowOpacity: 0.1` up to `0.3`), gradient patterns (`getPatternColors` maps strings like "fire", "water" to specific hex pairs).

<!-- CARTOGRAPHER_END: THEME_&_ASSETS -->\n\n### SIMULATION & MOCKS\n<!-- CARTOGRAPHER_START: SIMULATION_&_MOCKS -->

## 1. File Manifest
**src/mocks/**
- `react-native-worklets.web.js`: Web stub for `react-native-worklets-core`. Safely bypasses native worklet dependencies (e.g., `TurboModuleRegistry.getEnforcing`) during web compilation. Exports no-op shim functions (`runOnJS`, `runOnUI`, `useSharedValue`).
- `react-native-vision-camera-worklets.web.js`: Empty web stub bypassing native vision camera worklets in the browser bundle.

**src/__mocks__/**
- `sk8lytz-watch-bridge.ts`: Jest mock for the native Expo module (`sk8lytz-watch-bridge`). Ensures that unit tests invoking native watch commands (e.g., `WatchBridge.syncSessionState`) do not crash and allows assertion of bridge payloads.

**e2e/**
- `init.ts`: Detox global initialization and teardown hook. Launches the app (`newInstance: true`) before all tests and reloads React Native before each test.
- `jest.config.js`: Detox E2E Jest configuration specifying the test environment, runners, and timeouts.
- `smoke.test.ts`: Detox E2E test verifying the core app launch sequence and `dashboard-screen` visibility.

**__tests__/services/**
- `SpeedTrackingService.offline.test.ts`: Jest test suite validating offline session persistence behavior. Tests queue writes, happy-path flushing, re-entrancy guards (INSERT block), and missing auth sessions.

## 2. Blast Radius
- **`src/__mocks__/sk8lytz-watch-bridge.ts` (HIGH)**: Modifying this mock directly impacts all test suites that touch the watch bridge or health telemetry. Missing exports will break the CI `npm run verify` pipeline.
- **`src/mocks/react-native-worklets.web.js` (MEDIUM)**: Any changes to mocked functions (`runOnJS`, `runOnUI`) will impact web build stability and potentially crash camera-related layout code that relies on these exports during web simulation.
- **`e2e/jest.config.js` (LOW)**: Impacts only the Detox testing pipeline execution. Modifying it does not affect native or web builds.

## 3. Context Matrix
- **Testing Environment Integration**: `jest.config.js` configures Detox which consumes `init.ts` for app state management during E2E testing to ensure a clean state between test runs.
- **Native Module Interception**: Tests running in Node.js (Jest) intercept native calls via `src/__mocks__/sk8lytz-watch-bridge.ts` to prevent "Native module not found" crashes while simulating native Watch communication.
- **Web Simulation Isolation**: Both files in `src/mocks/` are aliased in the Metro config specifically to isolate and bypass `react-native-worklets-core` initialization issues when compiling for web platforms, as these libraries rely on `TurboModuleRegistry` which is undefined on web.

## 4. Hook/Service I/O Registry
- **Mock: sk8lytz-watch-bridge**
  - `syncSessionState(state) => Promise<undefined>` (Mocked)
  - `sendMetricUpdate(update) => Promise<undefined>` (Mocked)
  - `isWatchReachable() => Promise<false>` (Mocked)
  - `addWatchCommandListener() => function` (Mocked teardown)
  - `addWatchHealthListener() => function` (Mocked teardown)
- **Mock: react-native-worklets**
  - `useSharedValue() => { value: null }` (Mocked)
  - `runOnJS(fn) => fn` (Mocked)
  - `runOnUI(fn) => fn` (Mocked)

<!-- CARTOGRAPHER_END: SIMULATION_&_MOCKS -->\n\n### BUILD CONFIG & OTA\n<!-- CARTOGRAPHER_START: BUILD_CONFIG_&_OTA -->

## 1. File Manifest
*   **`app.config.js`**: Core Expo configuration. Defines app metadata, iOS/Android native permissions, custom config plugins (BLE, Health Connect, Apple Targets, Wear OS), and links to the EAS project ID.
*   **`eas.json`**: Expo Application Services build configurations. Specifies CLI rules (`appVersionSource: "remote"`) and defines three build profiles (`development`, `preview`, `production`) mapping artifact types (`apk`, `app-bundle`, simulator).
*   **`metro.config.js`**: Custom Metro bundler configuration. Implements a web-platform resolver to shim native-only modules (like `react-native-worklets` and `react-native-vision-camera-worklets`) with no-op stubs, preventing Expo Web compilation crashes.
*   **`babel.config.js`**: Babel compiler configuration. Registers `babel-preset-expo` and the `react-native-worklets/plugin` for native multi-threading capabilities.
*   **`package.json`**: NPM dependencies and script runner configurations. Notable for the `verify` and `blast-radius` validation scripts, reliance on Expo SDK 55, and custom linked workspace dependencies like `sk8lytz-watch-bridge`.

## 2. Blast Radius
*   **Native Rebuild Triggers**: Any changes to `app.config.js` (plugins, permissions, scheme), `package.json` (adding native dependencies), or `eas.json` require a full native rebuild via EAS because they modify the underlying iOS/Android project files. 
*   **JS Bundle Triggers**: Modifying `babel.config.js` or `metro.config.js` impacts the JavaScript pipeline. These changes require clearing the Metro cache and can cause bundle crashes if misconfigured.
*   **Web Fallback Integrity**: `metro.config.js` modifications directly impact Expo Web compilation. Removing the web-shims for worklets will immediately break web platform compilation with `TurboModuleRegistry.getEnforcing()` crashes.

## 3. Context Matrix
*   **Domain**: `BUILD_CONFIG_&_OTA`
*   **Build Pipeline Integration**: 
    *   `eas.json` profiles dictate the artifact type (`apk` for internal testing, `app-bundle` for the Play Store) mapping to Google Play requirements.
    *   `app.config.js` bridges configurations injected by native plugins (`@config-plugins/detox`, `withWearOsModule`) natively into `AndroidManifest.xml` and `Info.plist`.
*   **Metro & Babel Relationship**: `babel.config.js` transpiles worklets using the Reanimated/Vision Camera plugin, while `metro.config.js` actively mocks them out for web targets to safely run the UI without crashing the browser.

## 4. Hook/Service I/O Registry
*   *(No Hooks/Services apply directly to this configuration domain, as it manages infra)*
*   **EAS Build I/O**:
    *   *Input:* `eas build --profile <development|preview|production>`
    *   *Output:* Internal App (APK/Simulator) or Production App Bundle.
*   **Metro Resolver (`resolveRequest`)**:
    *   *Input:* Module Import Request (`moduleName`, `platform`)
    *   *Output:* If `platform === 'web'` and module is in `WEB_SHIMS` ➔ returns local source mock file. Else ➔ standard Expo Metro resolution.

## 5. Domain-Specific Directive (Release Channels, EAS Update & Native Modules)
*   **Release Channel Configurations**: `eas.json` configures three distinct profiles:
    *   `development`: Distributes `apk` internally, built with `developmentClient: true` to support Expo Dev Client.
    *   `preview`: Distributes `apk` internally, plus an iOS `simulator` target.
    *   `production`: Distributes standard `app-bundle` for Android Play Store submission. (Leverages remote app versioning `appVersionSource: "remote"`).
*   **EAS Update / OTA Logic**: **There is no OTA update mechanism currently implemented.** The project lacks `expo-updates` in `package.json` and has no configured `updates.url` in `app.config.js`. Updates are distributed strictly via native App Store/Play Store binary releases.
*   **Native Module Requirements**:
    *   **BLE**: `react-native-ble-plx` plugin requires heavy Android permissions (`BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN`, `ACCESS_FINE_LOCATION`) and background execution configurations.
    *   **Health**: `react-native-health` and `react-native-health-connect` require fitness read/write permissions.
    *   **Wearables**: The custom `./plugins/withWearOsModule` and `@bacons/apple-targets` plugins integrate the standalone watchOS/Wear OS binaries into the phone APK/IPA.
    *   **Android Build Props**: Enforces SDK 36 `targetSdkVersion` and SDK 26 `minSdkVersion`, with `enableJetifier: true` required for legacy AndroidX support.

<!-- CARTOGRAPHER_END: BUILD_CONFIG_&_OTA -->\n\n## 13. Historical Archive (The Graveyard)

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

### 1. File Manifest
* `android/app/src/main/AndroidManifest.xml`: Core native Android permission definitions (Bluetooth, Location, Camera, Audio, Notifications, Foreground Services, Activity Recognition).
* `app.config.js`: Cross-platform configuration defining Expo's Android permission arrays, iOS `Info.plist` usage descriptions (e.g., `NSMicrophoneUsageDescription`), and plugin configurations (e.g., `react-native-ble-plx` `neverForLocation` flag).
* `src/services/PermissionService.ts`: The unified abstraction layer for OS normalization, legacy fixes, and the user-level `@sk8lytz_permissions_optout` ledger.

### 2. Blast Radius
* **`src/components/DockedController.tsx`**: Highly reactive. Dynamically mounts/unmounts `CAMERA` and `STREET` visual modes based on live permission checks during `AppState` changes.
* **`src/components/CameraTracker.tsx`**: Dependent on `CAMERA` permissions. Utilizes `react-native-vision-camera` to render a fallback "GRANT PERMISSION" UI state.
* **`src/services/NotificationService.ts`**: Initialization is decoupled and depends on explicit user grant of `NOTIFICATIONS` to prevent rogue OS prompts blocking the app launch.
* **`src/hooks/useHealthTelemetry.ts`**: Depends on `HEALTH` permissions (Apple HealthKit / Android Health Connect) to synchronize physiological metrics.

### 3. Context Matrix (Domain-Specific Directive)
*Matrix to prevent App Store / Google Play rejections by strictly correlating requested permissions to skater-centric features.*

| OS Permission | App Feature Justification | Store Compliance Strategy |
| ------------- | ------------------------- | ------------------------- |
| **Location (`ACCESS_COARSE/FINE_LOCATION`)** | Required by Android <12 exclusively for BLE peripheral discovery. | Android 12+ utilizes `neverForLocation: true` in `app.config.js` to bypass location prompts entirely. Privacy policy must explicitly declare Location is only for hardware pairing, not mapping. |
| **Bluetooth (`BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN`)** | The essential transport layer for controller opcodes and state sync. | Hard-gated by `BluetoothGuard.tsx`. App is unusable without it. iOS uses automatic prompt on first GATT activity. |
| **Microphone (`RECORD_AUDIO`)** | Powers "Vibe Reactivity" (Music/Symphony mode) to sync skate LEDs to ambient sound. | iOS `Info.plist` strings declare: *"SK8Lytz needs microphone access to synchronize your lights to ambient music."* |
| **Camera (`CAMERA`)** | Powers the Vibe Catcher / Sniper mode to sample real-world colors and extract dynamic palettes. | Handled gracefully. If denied, the mode is dynamically removed from the `DockedController` to prevent broken UX. |
| **Activity (`ACTIVITY_RECOGNITION`) & Health** | Enables Wear OS/watchOS companions to act as a session HUD (HR, Calories). | We only request specific records (`HeartRate`, `ActiveCaloriesBurned`). Explicitly decoupled from core hardware features. |

### 4. Hook/Service I/O Registry
**`PermissionService.ts`**
* **I/O State**: Reads from native OS and `@sk8lytz_permissions_optout` (`AsyncStorage`).
* **`checkPermission(type)`**: 2-layer evaluation. First checks the soft opt-out ledger. If user opted-out, preempts the OS and returns `false`. Otherwise, polls native OS status.
* **`requestPermission(type)`**: Dispatches to correct OS bridge. Includes specific Android 12+ BLE location-skip logic and Health Connect complex initialization logic.
* **`setPermissionOptOut(type, boolean)`**: Triggers `DeviceEventEmitter.emit(PERMISSION_STATUS_CHANGED_EVENT)` to force reactive UI refreshes.

### 5. Sequence Diagram: Android Health Connect Request Flow
*Note: This flow includes a critical initialization requirement to prevent coroutine thread crashes.*

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Component as UI Component
    participant PS as PermissionService
    participant PA as PermissionsAndroid
    participant HC as react-native-health-connect
    
    Component->>PS: requestPermission('HEALTH')
    PS->>PA: request(ACTIVITY_RECOGNITION)
    PA-->>PS: Granted
    
    rect rgb(60, 20, 20)
    note right of HC: CRITICAL: initialize() must be called to register the<br/>ActivityResultLauncher before requesting permissions.
    PS->>HC: initialize()
    end
    
    PS->>HC: requestPermission([HeartRate, Calories, etc.])
    HC->>User: Displays OS Health Connect Prompt
    User-->>HC: Approves Read/Write
    HC-->>PS: Returns granted array
    PS->>PS: Validates HeartRate & Calories present
    PS-->>Component: Returns true
```

<!-- CARTOGRAPHER_END: OS_PERMISSIONS -->
```

<!-- CARTOGRAPHER_END: OS_PERMISSIONS -->

### 16. DEPENDENCY_AUDIT
<!-- CARTOGRAPHER_START: DEPENDENCY_AUDIT -->

## 1. File Manifest
- `package.json`

## 2. Blast Radius
- **Scope**: The entire Expo/React Native application runtime, configuration, and build pipeline.
- **Direct Consumers**: Expo Metro Bundler, EAS Build pipeline, GitHub Actions, `tools/verifiable-check-runner.js`, `tools/blast-radius-scanner.js`.

## 3. Context Matrix
| Dependency Domain | Packages | Role |
| --- | --- | --- |
| **Core Framework** | `react` (19.2.6), `react-native` (0.83.2), `expo` (~55.0.8) | Core React Native and Expo SDK infrastructure. |
| **BLE & Hardware** | `react-native-ble-plx`, `sk8lytz-watch-bridge` | Device communication and wearable integration. |
| **State & Data** | `xstate`, `@xstate/react`, `@supabase/supabase-js`, `@react-native-async-storage/async-storage` | Application state FSMs, cloud syncing, local offline caching. |
| **Vision & Image** | `react-native-vision-camera` (v5), `react-native-nitro-image`, `jpeg-js`, `expo-image-manipulator` | Camera Vibe Catcher and image processing pipeline. |
| **Location & Maps**| `react-native-maps`, `supercluster`, `react-native-map-clustering` | Crew Hub mapping and geographical grouping. |
| **Health**         | `react-native-health`, `react-native-health-connect` | Wearable health & biometric telemetry integration. |

## 4. Hook/Service I/O Registry (Script Run Pipeline)
| Script | Input | Output / Effect |
| --- | --- | --- |
| `verify` | Worktree state | Runs `blast-radius-scanner.js --worktree`, then `verifiable-check-runner.js`. Halts commit/push on failure. |
| `postinstall` | `node_modules` | Executes `patch-package` to apply custom native/JS module fixes after npm install. |
| `prepare` | Git Repo / Env | Initializes `husky` to enforce pre-commit/pre-push Sentinel gates. |

---

## 5. Domain-Specific Directive: Technical Debt & Bundle Risk Analysis

### ⚠️ Heavy Dependencies / Bundle Size Risks
1. **`jpeg-js` (^0.4.4)**
   - **Risk**: Pure JavaScript implementation of a JPEG encoder/decoder. Highly computationally expensive on the React Native JS thread.
   - **Debt**: Since `react-native-nitro-image`, `expo-image-manipulator`, and `react-native-vision-camera-resizer` are installed and leverage native/GPU acceleration, retaining `jpeg-js` is redundant and poses a frame-drop risk during Camera Mode processing.
2. **`string-similarity` (^4.0.4)**
   - **Risk**: String matching algorithms (e.g., Dice's Coefficient). If invoked frequently on large data sets (e.g., searching skaters or devices on the UI thread), it could lead to JS thread blocking.
3. **`xmldom` / `xml2js` (in `overrides`)**
   - **Risk**: XML parsing in JS is notoriously slow and heavy. Their presence in the `overrides` block indicates an attempt to patch legacy XML vulnerabilities in sub-dependencies.

### 🧟 Potential Zombie Packages
- **`buffer` (^6.0.3) & `base64-arraybuffer` (^1.0.2)**: Frequently manually installed to polyfill legacy RN versions or older BLE data transmission handlers. Given the app is on `react-native` 0.83+ and uses modern `expo-crypto`/`expo-file-system`, these might be entirely redundant dead weight and should be audited for removal.

---

## 6. Sequence Diagram: Verification & Linking Pipeline

```mermaid
sequenceDiagram
    participant Dev as Developer
    participant NPM as NPM/Yarn
    participant Husky as Husky Hooks
    participant BRS as Blast Radius Scanner
    participant VCR as Verify Check Runner
    participant PP as Patch-Package

    Note over Dev, NPM: Install & Setup Phase
    Dev->>NPM: npm install
    NPM->>PP: postinstall hook
    PP-->>NPM: apply node_modules patches
    NPM->>Husky: prepare hook
    Husky-->>NPM: bind git hooks (pre-push)

    Note over Dev, VCR: Validation Phase (Sentinel Gates)
    Dev->>NPM: npm run verify
    NPM->>BRS: node tools/blast-radius-scanner.js --worktree
    BRS-->>NPM: exit 0 (scope verified)
    NPM->>VCR: node tools/verifiable-check-runner.js
    VCR-->>NPM: exit 0 (TSC, Jest, AST OK)
    NPM-->>Dev: Pipeline Green ✅
```

<!-- CARTOGRAPHER_END: DEPENDENCY_AUDIT -->

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


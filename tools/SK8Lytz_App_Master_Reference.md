# SK8Lytz App Master Reference

_Last Updated: 2026-04-23 | **BATCH:P2 COMPLETE** — UnifiedPatternPicker, Scene Builder (32-slot), GradientBuilder corrections, LEDStripPreview, pattern-favorites-v2 (full BUILDER state persistence). Hotfix: color-picker snap-back (stale-closure regression). Visualizer simMode engine removed. TSC clean. v2.7.0 | Source of Truth: `src/protocols/ZenggeProtocol.ts`_

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
| **3** | Physical LEDs | Total real LEDs in the world (`ledPoints × segments`, or × wiring factor) | Not stored — derived only |

> **Golden Rule**: All pixel arrays (`0x59`, `0x31`) MUST be built using `ledPoints` (Layer 1). Segments and wiring are the hardware's job, not the app's.

#### Confirmed Product Defaults

| Product | `ledPoints` | `segments` | Physical LEDs | Adjustable? | Architecture |
|:--------|:-----------:|:----------:|:-------------:|:-----------:|:-------------|
| **HALOZ** | **8** | **2** | 16 | ❌ Fixed | Ring. Hardware **auto-mirrors** the 8-point pattern to a 2nd segment. Always send 8-element arrays. |
| **SOULZ** | **43** | **1** | 86* | ✅ Yes | Strip. No hardware mirroring. Controller drives one 43-point canvas. Physical doubling from Y-wire is transparent. |
| **RAILZ** | **30** | **2** | 60 | ✅ Yes | Dual rail. Placeholder — confirm with hardware before shipping. |

*SOULZ physical reality: 43 LEDs on LEFT skate (outside boot) + 43 LEDs on RIGHT skate (inside boot), both Y-wired to the same controller output. The controller is **oblivious to the doubling**.

#### SOULZ — User-Adjustable `ledPoints`

SOULZ strips are cut-to-length. If a user physically cuts the strip shorter, they **must** update `ledPoints` in the HW Setup Wizard to match the physical count. Example: cut from 43→36 → set `ledPoints=36`. The LED Points adjuster in the wizard (`hardwareAllowsCustomPoints: true`) exists for exactly this reason.

Every pixel array builder (`PatternEngine`, `applyEmergencyPattern`, etc.) must read `hwSettings.ledPoints` dynamically — NEVER hardcode 43.

#### ⚠️ Previous Bug (Fixed 2026-04-22)

`ProductCatalog.ts` previously had `HALOZ.defaultLedPoints = 16, segments = 1`. This was **wrong** — it caused:
1. `applyEmergencyPattern` sending 16-element arrays to an 8-point device, bypassing the hardware segment mirror engine
2. Any EEPROM probe (`0x63`) returning `ledPoints=8` would have caused a mismatch with stored defaults

Fixed: `HALOZ.defaultLedPoints = 8, segments = 2`.

---

**Core Philosophies (The 3 Pillars):**

1. **Bulletproof BLE Transport:** The connection to Neogleamz hardware MUST be instantaneous and nearly sentient. Reconnects and pairing must handle GATT exceptions and MTU drift invisibly. "It just works, immediately."
2. **Tactile, Glanceable UI:** High-contrast, Neogleamz standard aesthetics. Massive touch targets (>44px) for skaters in gear. One-tap access to Symphony effects and App-mic visualization.
3. **No-Compromise Offline Flow:** Hardware control is a fundamental right. basic lighting and EEPROM configuration (0x62/0x63) never require cloud authentication.

**Anti-Goals (What we ruthlessly reject):**

- **Bloated Developer Logic in Prod:** We use strict `__DEV__` elimination to keep the binary lean and free of testing debris.
- **Complex UI Micro-Management:** Skaters want to skate. We provide stunning Pro Effects and high-precision HUDs (Speed/Brightness), not frame-by-frame animation editors.
- **Hardware-Cloud Gating:** We never lock essential local hardware features behind an internet authentication wall.
- **Hardcoded Hardware Heuristics:** The UI layer must NEVER use explicit string literals (e.g. `type === 'HALOZ'`) or hardcoded binary logic to render products. All hardware metadata (shape, icons, colors) must be dynamically derived from `LOCAL_PRODUCT_CATALOG` (`src/constants/ProductCatalog.ts`) to ensure scalable, zero-code support for new OEM devices.

### ❌ Condemned Opcodes — Never Use in Production

> [!CAUTION]
> The following BLE opcodes are PERMANENTLY CONDEMNED for production UI use.
> They cause a fundamental visualizer-parity gap: the hardware controls the animation internally,
> so the ProductVisualizer cannot know what the hardware is showing. This breaks our core parity promise.

| Opcode | Name | Why Condemned | What Replaced It |
|:-------|:-----|:--------------|:-----------------|
| **`0x41`** | Settled Mode (Symphony Effects) | Hardware runs one of 33 `ge.*` animations internally. App cannot know the pixel state. | 33 `ge.*` effects reverse-engineered as PatternEngine TypeScript, fired via `0x59` |
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
| **Tier 1** | ge.* Java class reversal | 33 | Settled Mode effects. Each `ge.*` class is read for its math → reimplemented in TypeScript. `0x41` is NEVER called. |
| **Tier 2** | Programs Mode reversal | ~28 | Standard LED strip effects. Each Programs effect is reimplemented in TypeScript. `0x42` is NEVER called. |
| **Tier 3** | SK8Lytz originals | ∞ | Effects only possible because we own the payload. Positional gradients, reactive splits, sport sequences, etc. |

**Total target**: ~75+ patterns across all tiers, all in one unified picker.

#### Pattern Template Schema

Every pattern in `CustomEffects.ts` has this structure:

```typescript
interface SK8LytzTemplate {
  id: number;                          // Unique, never reuse. 1-28 = existing. 29+ = new.
  name: string;                        // User-facing name in picker
  icon: string;                        // Emoji icon for picker card
  colorMode: 'FG_BG' | 'FG_ONLY' | 'GENERATIVE';  // Which color pickers to show
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
- `GENERATIVE` — Neither picker shown (e.g. Rainbow Flow: hue is computed by math, not user-set)

> Note: The pattern ALWAYS receives both `fg` and `bg` arguments — the gate is purely a UI affordance.

#### Implementation Contract for Every New Pattern

```
1. Read source math (ge.* Java class, or Programs effect behavior)
2. Write TypeScript: buildXyz(fg, bg, numLEDs, tick, direction): RGB[]
3. Add case to PatternEngine.ts getVisualizerFrame()
4. Add entry to CustomEffects.ts SK8LYTZ_TEMPLATES with correct colorMode/tier/sourceRef
5. Verify: ProductVisualizer shows the effect ← identical to hardware via 0x59
6. Hardware test on HALOZ: tap pattern → LED ring matches visualizer
```

---

## 2. System Architecture & Local Storage

### AsyncStorage Key Registry

| Key                                 | Owner                           | Contents                                                                                                                                        |
| :---------------------------------- | :------------------------------ | :---------------------------------------------------------------------------------------------------------------------------------------------- |
| `@sk8lytz_logs`                     | AppLogger                       | Compact telemetry event buffer array                                                                                                            |
| `@Sk8lytz_auth_username`            | DashboardScreen                 | Local cache of Supabase display_name for instant UI feedback. Synced via Reactive Context Pattern (Load Cache -> Hydrate Profile -> Update UI). |
| `@Sk8lytz_registered_devices`       | DeviceRepository                | Primary SSOT ledger of all claimed/bound hardware keyed by BLE MAC. Supersedes ad-hoc device configurations.                                    |
| `@Sk8lytz_device_configs`           | useDashboardGroups / AppLogger  | Dict keyed by **BLE MAC** containing `{ name, type, points, segments, sorting, stripType, groupId }`                                           |
| `@Sk8lytz_custom_groups`            | useDashboardGroups              | Array of `{ id, name, isGroup, deviceIds }` — group memberships                                                                                |
| `@sk8_hw_<deviceId>`                | Sk8LytzProgrammerModal          | Per-device EEPROM hardware settings cache                                                                                                       |
| `@sk8lytz_theme`                    | ThemeContext                    | `dark` or `light`                                                                                                                               |
| `@sk8lytz_control_theme`            | ThemeContext                    | Control color theme name                                                                                                                        |
| `@Sk8lytz_Favorites`                | useFavorites                    | Dictionary of user-defined lighting presets (Name, Palette, Mode)                                                                               |
| `@sk8lytz_permissions_optout`       | PermissionService               | App-Level Opt-Out Ledger. User toggles that override OS permissions for legal/privacy reasons.                                                  |
| `@Sk8lytz_voice_tutorial_dismissed` | boolean                         | Gating for the Voice Command onboarding modal                                                                                                   |

> [!CAUTION]
> **PURGED KEYS (2026-04-17):** The following legacy `ng_*` keys are fully deprecated and MUST NOT be used anywhere in the codebase. They caused split-brain bugs due to namespace drift:
> - ~~`ng_device_configs`~~ → migrated to `@Sk8lytz_device_configs`
> - ~~`ng_custom_groups`~~ → migrated to `@Sk8lytz_custom_groups`
> - ~~`ng_processed_devices`~~ → DELETED (one-shot cleanup on boot)

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

The BLE write path uses an **Optimistic UI** architecture to eliminate perceived 80–500ms hardware latency:

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

---

## 3. BLE Protocol Library

> [!IMPORTANT]
> **Dynamic Catalog Migration (2026-04-11)**: All hardware profile logic—including default LED counts, visualization themes, and discovery categorization—is now handled strictly via `LOCAL_PRODUCT_CATALOG` (`src/constants/ProductCatalog.ts`).

All byte definitions below represent the inner payload _before_ the V2 BLE packet wrapper is applied.

### Confirmed Hardware Identity (APK-Verified 2026-04-21)

> [!IMPORTANT]
> All 3 physical SK8Lytz devices confirmed as **`Ctrl_Mini_RGB_Symphony_new_0xA3`** (product_id: **163 = 0xA3**). Confirmed from `discovered_devices_telemetry` across MACs `08:65:F0:9A:C2:3C`, `08:65:F0:9A:5E:06`, `08:65:F0:5F:03:B1`. Firmware: v45–46, BLE: 5, LED version: 3.
>
> **Key implications of 0xA3 vs 0xA2:**
> - `0x59` Static Colorful tab **IS available** on 0xA3 (not available on 0xA2) ✅
> - `0x51` Custom Scene — **9B compact format (291B) WORKS** on 0xA3 via our standard `wrapCommand` ✅
> - `0x51` 10B extended format (323B) does NOT work via our wrapper — requires ZENGGE chunked framing header (see Protocol Bible Section 11)
> - `0x42` effect ceiling: **1–100** (same as 0xA2). Effect 101 plays an undocumented effect (ceiling is soft).
> - `0x43` Multi-Sequence: **DO NOT USE** — Oracle test caused hardware LED shutoff (state machine crash). ZENGGE app uses `0x51` for multi-step effects, not `0x43`.
> - `0x41` Settled Mode, `0x53` Live Pixel Stream: **Unconfirmed** — our wrapper format appears wrong for these opcodes.
> - Source: Oracle Lab + live BLE HCI sniff (2026-04-22), `ZENGGE_PROTOCOL_BIBLE.md` Section 11

### BLE Connection Handshake (2026-04-22)

Every GATT connection fires this sequence before the device is added to React state:

1. **MTU Negotiation** — `requestMTUForDevice(conn.id, 512)`
2. **0x10 Session Time Sync** — `ZenggeProtocol.setSessionTime()` → written directly to `ZENGGE_CHARACTERISTIC_UUID`. Format: `[0x10, year-2000, month(1-12), day, hour, min, sec, weekday(0=Sun), checksum]`. Source: `TimeControllerFragment.java` APK decompile. Non-fatal — wrapped in try/catch.
3. **React state update** — `setConnectedDevices()` fires _after_ GATT is booted to prevent UI from blasting payloads during MTU queries.

> [!IMPORTANT]
> `setSessionTime()` was missing from `ZenggeProtocol.ts` entirely (line 742 was `queryHardwareConfig` — the plan was stale). The method was implemented from scratch (commit `fdc0ff3`).

### writeChunked — 0x51 Extended Payload Framing

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

1. **Global Connection Gate (`bleGateRef`):** A `useRef` semaphore with states `IDLE | SCANNING | CONNECTING | DISCONNECTING | RECOVERING`. ALL BLE operations must check/acquire the gate before touching the radio. Only one operation class at a time.
2. **Parallel Writes and Teardowns (`Promise.all`):** Previous sequential constraints caused massive UI lag. Group-wide commands (sliders) and teardowns (`cancelDeviceConnection`) MUST be wrapped in `Promise.all` loops to eliminate staggered latency.
3. **The GATT 133 Retry Bumper:** `connectToDevice` MUST be wrapped in a 2-attempt retry loop that explicitly catches `133` routing errors and applies a 200ms thread-sleep before the second attempt to silently absorb Android RF congestion.
4. **High-Priority Channel Escalation:** Upon resolving connection on Android, the `requestConnectionPriorityForDevice(conn.id, 1)` command must be instantly fired. This throttles the kernel polling interval to ~11.25ms to defend against crowded RF environments.
5. **Lean Connection Loops:** `connectToDevices` strictly establishes MTU (request 512 bytes) and notification pipes. Do NOT execute 600ms latency buffers, firmware loads, or 0x63 hardware settings queries during the connection stack, as this artificially bloats the boot sequence by 2.5s per device.

### The Transport Wrapper (`wrapCommand`)

Every inner protocol payload must be wrapped using the standard 8-byte Zengge V2 framing:
`[0x00, SequenceNum, 0x80, 0x00, LenHi, LenLo, Len+1, 0x0B, ...innerPayload]`

### Auto-Recovery System (Gate-Coordinated)

_Refactored: 2026-04-17 | Lives in: `src/hooks/ble/useBLEAutoRecovery.ts`_

The **Auto-Recovery** system monitors GATT-connected devices for organic disconnects (dropout events). When a device drops, recovery automatically attempts reconnection with gate coordination.

| Property | Value |
| :--- | :--- |
| **Trigger** | Organic `onDisconnected` event from BLE PLX |
| **Gate coordination** | Skips recovery attempts when `bleGateRef ≠ IDLE` (e.g., during manual connections) |
| **Retry sequence** | 1. Set gate → `RECOVERING` → 2. `connectToDevice` (3500ms timeout) → 3. Discover services → 4. Re-register FF02 monitor → 5. Release gate → `IDLE` |
| **Cancellation** | AbortController-style token — incrementing counter instantly breaks all active loops |
| **Max retries** | `5` with exponential delay: `2s, 4s, 8s, 16s, 30s` |
| **Ghosting** | Failed recovery adds device to `ghostedDeviceIds` — UI dims card, `writeToDevice` skips it |

**Telemetry Events:**
- `AUTO_RECOVERY_STARTED` — emitted when recovery loop begins for a device
- `AUTO_RECOVERY_SUCCESS` — device reconnected and services restored
- `AUTO_RECOVERY_FAILED` — all retries exhausted, device is now ghosted
- `AUTO_RECOVERY_CANCELLED` — recovery loop cancelled (user-initiated disconnect)
- `AUTO_RECOVERY_GATE_WAIT` — recovery attempt skipped because gate is busy

> [!IMPORTANT]
> **DELETED (2026-04-17):** The legacy `useBLEWatchdog.ts` heartbeat system has been completely removed. Its functionality (30s polling + silentRelatch) is replaced by the reactive `onDisconnected` observer in `useBLEAutoRecovery`. The watchdog created GATT collisions by issuing writes (`0x63`) during active user operations.

### Auto-Connect Observer (Debounced)

_Lives in: `src/hooks/useDashboardAutoConnect.ts`_

The dashboard auto-connect observer watches `allDevices` for registered peripherals that appear during passive scanning. It is hardened with:
- **500ms debounce** — batches devices discovered within 500ms into a single `connectToDevices` call
- **Gate check** — skips connection when `bleGateRef ≠ IDLE`
- **Prevents stampeding herd** — no concurrent auto-connect attempts

---

### LED Modes & Math Synthesizer Engine

> [!IMPORTANT]
> **Math Synthesizer Refactor (2026-04-21)**: The legacy "Fixed Mode" (10 hardcoded behaviors) and firmware-dependent RBM logic have been entirely superseded by a deterministic, client-side mathematical synthesizer. All lighting visualizations and sub-protocols are now driven by 28 customizable `SK8LYTZ_TEMPLATES`.

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

28 mathematical schemas define the structure (Comet, Breathing, Double Meteor) using a primary foreground (`FG`) and secondary background (`BG`) palette.
Dispatch chain: `useControllerDispatch.ts` → `PatternEngine.ts` (Synthesizer) → `ZenggeProtocol.ts` (BLE bytes).

**Archetypes & Auto-Routing:**
- **Spatial Mode (`0x59` CASCADE/FREEZE):** Synthesizes full 300-pixel RGB arrays client-side using waveform math (sine waves, pulse trains, alternating grids). Automatically routed to `<ProductVisualizer>` and `<CustomEffectVisualizer>` without duplicative business logic.
- **Temporal Mode (`0x51` STEP_JUMP/GRADUAL):** For patterns that require sub-millisecond fade interpolations (e.g., `Breath`, `Strobe`), the engine automatically routes to the `0x51` 32-step hardware scheduler to prevent BLE bus saturation.

> [!NOTE]
> The legacy `Fixed` UI tab was completely eliminated. The `MULTIMODE` hub now acts as a unified portal for the 28 spatial/temporal mathematical templates.

#### RBM Built-in Patterns (100 Modes)

Source of truth: `src/utils/RbmDictionary.ts` — IDs 1–100, mapped 1:1 to Zengge `SymphonyBuild` string table.
Visualizer: `src/utils/RbmSimulator.ts` (pixel-perfect frame generation).
Protocol: `0x42` (`setCustomRbm`) or `0x61` (legacy APK path — same pattern table).

#### Music Mode Patterns (13 Profiles)

Source of truth: `src/utils/MusicDictionary.ts` — 13 music-reactive patterns keyed to protocol IDs.
Visualizer: `src/utils/RbmSimulator.ts` → `getRbmMusicFrame()`.
Protocol: `0x73` (`setMusicConfig`) + `0x74` (App Mic magnitude stream).

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
> **`points` ≠ total LEDs.** `points` = LEDs per segment. `segments` = number of parallel mirrors.
> Total physical LEDs = `points × segments`. The hardware's segment engine mirrors the pattern automatically.
> **HALOZ example**: 22 bulbs = 11 points × 2 segments. All pattern commands use 11, not 22.
> The `0x51` slot `flags=0x80` byte enables segment mirroring ("section toggle"). `flags=0x00` disables it.
> Full model documented in `ZENGGE_PROTOCOL_BIBLE.md` under `0x62`.

---

### Command: Segmented Multi-Color Layout Array (0x59)

_Primary command for all IC-strip patterns. Sends a per-pixel RGB array that the hardware loops autonomously._

> [!IMPORTANT]
> **SEGMENT MODEL — Array Length Must Use `ledPoints`, NOT Total LEDs.**
> The ZENGGE hardware segment engine automatically mirrors the `ledPoints` pattern across all segments.
> For HALOZ (22 bulbs = 11 points × 2 segments), send an array of **11** pixels, not 22.
> Sending `ledPoints × segments` pixels bypasses the hardware mirror and fills both segments manually.
> Source: BLE sniff observation (2026-04-22) — ZENGGE Multi-Color creator uses `points` exclusively.

- **Format:** `[0x59, totalLenHi, totalLenLo, [R1,G1,B1...], numLEDsHi, numLEDsLo, transitionType, speed, direction, checksum]`
- **Source of Truth:** `ZenggeProtocol.setMultiColor()` — _do NOT replicate this logic elsewhere._
- **Minimum Payload:** 12 pixels. Payloads <10 cause **hardware memory lock glitching**.
- **TransitionType Bytes (Hardware-Confirmed Apr 2026):**

| Byte | HW Label | Builder Chip Name | Behavior |
|:---|:---|:---|:---|
| `0x00` | CASCADE | **STATIC** (freeze) | Pixel array locked in place — solid/static |
| `0x01` | FREEZE | **FLOW** (scroll) | Continuous hardware scroll — use for animated patterns |
| `0x02` | STROBE | **STROBE** (flash) | Flashing segments |
| `0x03` | Running Water | **WATER** (wave) | Hard jumping marquee — one-shot trigger per command |

> [!IMPORTANT]
> Builder chip IDs (0x00–0x03) corrected 2026-04-23 to match APK-proven `PositionalGradientBuilder.tsx` output. The HW label "CASCADE" = byte `0x00` = UI chip **STATIC**. The mapping is intentional — both represent the same hardware freeze behavior. Source of truth: commit `423f45e`.

- **Speed:** UI 0–100 → HW 1–31. Formula: `max(1, min(31, round(uiSpeed / 100 × 30) + 1))`. Source: APK `Protocol/n.java: ad.e.a(f, 1, 31)`.
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
- `effectId`: SymphonyEffect ID 1–33
- `speed`: 0–100 (direct, no scaling)
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
| `0x01`–`0x21` | Custom Effects 1–33 | Hardware `SymphonyEffect` IDs (advanced per-pixel effects) |

- **Speed:** Full 1–100 range valid (unlike `0x59` which is capped at 31).
- **Max slots:** 32 active steps.
- **Source of Truth:** `ZenggeProtocol.setCustomMode()` — current 9B format is production-safe.

---

### Basic Control Commands

- **Power ON (0x71):** `[0x71, 0x23, 0x0F, 0xA3]` — checksum `0xA3` = sum of first 3 bytes ✅
- **Power OFF (0x71):** `[0x71, 0x24, 0x0F, 0xA4]` — checksum `0xA4` = sum of first 3 bytes ✅
- **Source:** `C14184b.m4796M()` via `C7780q.m20873a()` — 0xA3 is NOT a legacy device → always uses `0x71`, never `0x3B`.

### Command: Settled Mode — FG + BG Dual Color (0x41)

_Triggers one of 33 Symphony effects with explicit foreground and background colors._

- **Format:** `[0x41, effectId, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, speed, direction, 0x00, 0xF0, checksum]` (13 bytes)
- **effectId range:** 1–33 (SymphonyEffect IDs)
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

- **Format:** `[0x42, patternId(1–100), speed(1–100), brightness(1–100), checksum]`
- **Source of Truth:** `ZenggeProtocol.setCustomRbm()`
- **Pattern IDs:** 1–100 mapped 1:1 to Zengge `SymphonyBuild` string table. Full dictionary in `src/utils/RbmDictionary.ts`.

### Command: Symphony Multi-Color / RBM Legacy (0x61)

_Legacy/alternative opcode for triggering RBM patterns. Present in Zengge APK code paths and the SK8Lytz Diagnostic Lab._

- **Format:** `[0x61, patternId, speed, brightness, checksum]` — identical structure to `0x42`.
- **Relationship to 0x42:** Both target the same on-chip RBM pattern table. The `0x61` opcode appears in older Zengge firmware revisions and specific APK UI code paths (`symphony_SymphonyBuild_*`). The production dispatch path uses `0x42` via `setCustomRbm()`, while the Diagnostic Lab UI labels it `0x61` for APK parity.
- **SK8Lytz Usage:** Exposed in Admin Diagnostic Lab (`useProtocolBuilder.ts`, `Sk8LytzDiagnosticLab.tsx`) for protocol testing.

### Command: Music Configuration (0x73)

_Configures the hardware's music-reactive mode with mic source, pattern, and dual colors._

> [!CAUTION]
> **micSource byte values were previously documented incorrectly.** APK-verified values from `MusicModeFragment` line 752 and `C7789z.java`:
> - `0x26` (38) = **App/phone mic** (magnitude driven by `0x74` commands)
> - `0x27` (39) = **Device mic** (hardware processes audio autonomously)
> The old `0x00`/`0x01` values are WRONG and must not be used.

- **Format (13 bytes):** `[0x73, isOn, micSource, effectId, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, sensitivity, brightness, checksum]`
- **isOn:** `0x01` = music mode on, `0x00` = off
- **micSource:** `0x26` = App mic (phone), `0x27` = Device mic (hardware)
- **effectId:** 1–13 music-reactive pattern IDs (mapped in `MusicDictionary.ts`)
- **sensitivity / brightness:** 0–255
- **Source of Truth:** `ZenggeProtocol.setMusicConfig()` — **must be updated to use `0x26`/`0x27` and include `isOn` byte.**
- **APK Source:** `C7789z.java`,  `MusicModeFragment.java` line 752

### Command: App Mic Magnitude (0x74)

_Streams real-time audio magnitude from the app's microphone to drive hardware music-reactive LEDs._

- **Format:** `[0x74, magnitude(0–255), checksum]` (3 bytes)
- **Used when:** `micSource = 0x26` (App mic) in the `0x73` music config.
- **Source:** `C7788y.java` → `m20863a()`, `useAppMicrophone.ts` → `ZenggeProtocol.sendMusicMagnitude()`

### Command: Live Pixel Stream — Frame-by-Frame (0x53)

_Streams one row of real-time pixel data per call. Used for live bitmap/image projection onto LEDs._

- **Format (variable):** `[0x53, totalLen_hi, totalLen_lo, R, G, B, ...(numLEDs × RGB)..., numLEDs_hi, numLEDs_lo, checksum]`
- **totalLen:** `(numLEDs × 3) + 6`
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
// sceneIndex: 0–9 for specific slot, 0xFF (-1 as byte) to replay ALL
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
> **DDA Refactor Shipped: 2026-04-14** — The architecture was refactored from a monolithic component model to a Hook-First domain model. All 18 domain hooks are live on `master`. The audit resolved 4 bugs (2x P0, 2x P1). TSC exit 0.

To ensure scalability and maintain UI performance, the SK8Lytz app enforces a **Hook-First** architecture. Complex business logic, hardware protocols, and Supabase data fetching must be extracted from UI components into decoupled domain hooks. UI components must focus strictly on rendering.

---

### ⚡ Critical Architectural Constraint: BLE Co-location

> [!CAUTION]
> **BLE state (`connectedDevices`, `writeToDevice`, `setOnDataReceived`) MUST remain co-located in `DashboardScreen.tsx`.** Do NOT move these into any domain hook. The BLE lifecycle manager is a singleton with hardware-level race conditions (GATT 133). Distributing it across multiple hook contexts would create multiple competing subscribers which cause silent write failures and GATT exceptions. All domain hooks receive BLE context via **prop injection** only.

---

### 🗺️ Complete Hook Registry (All 18 Hooks)

#### Dashboard Screen Domain (`src/hooks/`)

| Hook                  | Consumer          | Owns                                                               |
| :-------------------- | :---------------- | :----------------------------------------------------------------- |
| `useDashboardProfile` | `DashboardScreen` | User profile, `displayName`, `avatarUrl`, Supabase profile fetch   |
| `useDashboardGroups`  | `DashboardScreen` | `customGroups`, `deviceConfigs` AsyncStorage load/save, group CRUD |
| `useDashboardVoice`   | `DashboardScreen` | Voice command engine, mic permissions, command resolution          |

#### DockedController Domain (`src/hooks/`)

| Hook                       | Consumer           | Owns                                                                                                                                  |
| :------------------------- | :----------------- | :------------------------------------------------------------------------------------------------------------------------------------ |
| `useDockedControllerState` | `DockedController` | All LED control FSM state: `activeMode`, `selectedColor`, `brightness`, `speed`, `multiColors`, `builderNodes`, scene capture/restore |
| `useFavorites`             | `DockedController` | `favorites[]`, `quickPresets[]`, save/delete/load operations, prompt FSM                                                              |
| `useStreetMode`            | `DockedController` | Accelerometer subscription, G-force calculation, brake/cruise color dispatch, GPS speed sampling                                      |
| `useSessionTracking`       | `DockedController` | Session FSM (`IDLE → RECORDING → SUMMARY`), duration, distance, peak speed, session summary modal                                     |
| `useMusicMode`             | `DockedController` | Owns 0x73 music config dispatch, pattern names, pattern navigation.                                                                   |
| `useCuratedPicks`          | `DockedController` | Fetches and caches SK8Lytz Picks (curated presets) from Supabase.                                                                     |
| `useAppMicrophone`         | `DockedController` | Manages the expo-av Audio.Recording lifecycle for APP MIC mode. Streams normalized magnitude (0–1).                                   |
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

---

### 📐 Shared Type Contract

All FSM states and shared interfaces live in **`src/types/dashboard.types.ts`**. Never re-declare these types in individual hooks or components.

| Type                  | Values                                                                        |
| :-------------------- | :---------------------------------------------------------------------------- |
| `ModeType`            | `'FAVORITES' \| 'FIXED' \| 'MULTIMODE' \| 'MUSIC' \| 'STREET' \| 'CAMERA'` |
| `FixedSubMode`        | `'PATTERN' \| 'BUILDER'`                                                      |
| `MicSource`           | `'APP' \| 'DEVICE'`                                                           |
| `MusicColorFocus`     | `'PRIMARY' \| 'SECONDARY'`                                                    |
| `DeviceSettingsState` | FSM: `'IDLE' \| 'LOADING' \| 'READY' \| 'ERROR'`                              |
| `IDeviceConfigEntry`  | `{ name, type, points, segments, sorting, stripType, groupId }`               |

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
| `group_id`                  | TEXT       | Fleet group assignment                               |
| `group_name`                | TEXT       | Human-readable group name                            |
| `registered_at`             | TIMESTAMPTZ| First registration timestamp                         |
| `updated_at`                | TIMESTAMPTZ| Last modification timestamp                          |
| `rssi_at_register`          | INT        | Signal strength at registration                      |

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

### Wave 3: The AI Detective (Local Llama-3 Pipeline)
A localized, schema-driven AI extraction pipeline that processes raw text dumps from deep-crawled websites.
- **The Brain:** Ollama running locally (Llama-3 model).
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

---

> [!IMPORTANT]
> To remain active, every rule file MUST contain the `trigger: always_on` YAML frontmatter.


---

## 10. ZENGGE PROTOCOL BIBLE (APK DECOMPILED)

# ZENGGE PROTOCOL BIBLE
## Authoritative Hardware Reference — Derived from Decompiled ZENGGE 1.5.0 APK

> **Source Authority**: All entries in this document are traced to exact Java class files in
> `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\ZENGGE_APK\ZENGGE_DECOMPILED\sources\`
> No community docs, no guesses. Every byte is APK-verified.

> **Last Updated**: 2026-04-22 (Oracle Hardware Validation Session + Live BLE Sniff)
> **Confirmed Hardware**: `Ctrl_Mini_RGB_Symphony_new_0xA3` (product_id: 163 = 0xA3)
> **Source Files**: `tc/C14184b.java`, `tc/C14187d.java`, `com/zengge/wifi/COMM/Protocol/C77*.java`,
>   `com/zengge/wifi/activity/NewSymphony/fragment/*.java`

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
- **Speed range**: 0–100 (full range, different from 0x59 which caps at 31)
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
- **Transition Types** (hardware-confirmed):

| Byte | Name | Behavior |
|:-----|:-----|:---------|
| `0x00` | CASCADE | Continuous hardware scroll |
| `0x01` | FREEZE | Static locked in place |
| `0x02` | STROBE | Flash (implementation varies) |
| `0x03` | RUNNING_WATER | One-shot trigger, hard jump marquee |

- **Speed**: 0–100 UI → 1–31 HW. Formula: `max(1, min(31, round(uiSpeed/100 × 30) + 1))`
- **Direction**: `0x01` = forward, `0x00` = reverse
- **Minimum pixels**: 12 (hardware glitches below 10)
- **LED count branch** in `C7760a` line 23: `if (mo20320T() == 167)` → different LED count constant (for 0xA7 variant). Our 0xA3 (163) takes the ELSE branch = standard LED count path.
- **SK8Lytz relevance**: **CRITICAL** — primary pixel array command. ✅ Available on 0xA3.

---

### 0x62 — IC Config Write (EEPROM)
- **Builder**: `C14184b.m4807B()`, `m4806C()`, `m4804E()`
- **Format (full, 13 bytes)**:
```
[0x62, ptsHi, ptsLo, segHi, segLo, icType, sorting, micPts, micSegs, 0xF0, checksum]
```
- **Endianness**: Big-Endian (`ptsHi = points >> 8`, `ptsLo = points & 0xFF`)
- **SK8Lytz relevance**: CRITICAL — hardware provisioning (LED count, IC type, strip config)

> **🔬 SEGMENT MODEL DISCOVERY (2026-04-22 — BLE Sniff Observation)**
>
> `points` and `segments` are NOT equivalent to total LED count. The hardware treats them as:
> - **`points`** = number of addressable LEDs **per segment** (the design canvas)
> - **`segments`** = number of identical physical copies that mirror the pattern in parallel
> - **Total physical LEDs** = `points × segments`
>
> **Example — HALOZ (22 bulbs, 11 points, 2 segments):**
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
- **Format (13 bytes)**:
```
[0x73, isOn(1=on/0=off), micSource, effectId,
 FG.R, FG.G, FG.B, BG.R, BG.G, BG.B,
 sensitivity, brightness, checksum]
```
- **micSource**: `0x26` (38) = phone/app mic, `0x27` (39) = device mic
- **Confirmed from MusicModeFragment line 752**: `bool z2 = bArr[2] == 38` (38=phone, 39=device)

> **CORRECTION FROM MASTER REFERENCE**: The Master Reference lists `micSource: 0x01 = Device, 0x00 = App`.
> **APK TRUTH**: `0x26` (38) = app/phone mic, `0x27` (39) = device mic.
> The 0x00/0x01 values are WRONG in the current codebase/reference.

---

### 0x74 — Music Magnitude Stream ✅ OUR DEVICE
- **Builder**: `C7788y.java` → `m20863a(int magnitude, device)`
- **Format**: 3 bytes
```
[0x74, magnitude(0-255), checksum]
```
- **Used when**: micSource = `0x26` (app mic) in the `0x73` config
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
Mic Magnitude:  MusicModeFragment/useAppMic → C7788y → [0x74, mag, chk]
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

### Music Mode (0x73) — ✅ SMOKING GUN CONFIRMED
| Test | Result |
|:-----|:-------|
| `MIC=0x26`, `isOn=0x01` (APK App Mic) | ✅ PASS — Enters music mode, reacts to phone mic. `0x74` auto-stream fires but `useAppMicrophone` background hook was already streaming, masking it. |
| `MIC=0x27`, `isOn=0x01` (APK Device Mic) | ✅ PASS — Strip reacts to hardware control box mic. `0x74` auto-stream had secondary effect. |
| `isOn=0x00` | ✅ PASS — Music mode disengaged (confirmed `isOn` byte is live) |
| `MIC=0x00` (old legacy value) | ❌ FAIL — Blank/no response. Confirms old codebase was broken. |
| 12B format (missing isOn) | ❌ FAIL — Identical to 0x00 failure. Hardware rejects malformed packet. |
| `0x74` single magnitude | AMBIGUOUS — Works when mic=0x26 active, but `useAppMicrophone` background interference. |

> **KEY FINDING**: The `useAppMicrophone` hook is running in the background even during Oracle testing, continuously sending `0x74` packets. This means the auto-stream toggle in the lab appears to have no extra effect in MIC=0x26 mode because the hook is already streaming. This is a diagnostic consideration, not a bug.

### Custom Scene (0x51) — ✅ 9B COMPACT WORKS
| Test | Result |
|:-----|:-------|
| 9B compact format (current production) | ✅ PASS — Red/blue animated pattern fires correctly |
| 10B extended format (via our wrapCommand) | ❌ FAIL — Does nothing |

> **SEE BUG-1 UPDATE**: 10B extended fails due to our wrapper mismatch, not the hardware rejecting 10B slots.

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


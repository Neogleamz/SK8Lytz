/**
 * dashboard.types.ts — Shared Domain Type Contracts
 *
 * Single source of truth for all types shared across the SK8Lytz
 * domain hooks introduced by the domain-driven architecture refactor.
 *
 * Previously these were inlined inside DashboardScreen.tsx and
 * DockedController.tsx as local interfaces.
 */

// ─── Device & Group Contracts ───────────────────────────────────────────────

/**
 * DevicePatternState — the canonical per-device ledger entry.
 * Stored in AsyncStorage under `@SK8Lytz_DeviceState_v2_{MAC}`.
 * In-memory cache in useDeviceStateLedger for synchronous reads.
 */
export interface DevicePatternState {
  // Identity
  deviceMac: string;          // Always: MAC.toUpperCase(), no composite keys
  groupId?: string;

  // Structured (for UI pre-warm and dashboard preview)
  mode: ModeType;
  patternId?: number;
  patternLabel: string;        // e.g. "Pro Effects – Crimson"
  fgColor?: string;
  bgColor?: string;
  speed: number;
  brightness: number;
  builderNodes?: Array<{ id: string; position: number; colorHex: string }>;
  builderFillMode?: 'GRADIENT' | 'SOLID';
  builderTransitionType?: number;

  // Raw (for immediate BLE hardware replay on reconnect)
  rawPayload: number[];

  // Metadata
  ts: number;                  // Date.now() of last write
}

// Auto-classify result — fed into FirstTimeSetupModal
export interface PendingRegistration {
  device_mac: string;
  device_name: string;
  product_type: 'HALOZ' | 'SOULZ' | 'RAILZ' | 'UNKNOWN';
  position: 'Left' | 'Right';
  group_name: string;
  led_points: number;
  segments: number;
  ic_type: string;
  color_sorting: string;
  rssi: number;
  factory_name?: string;
  manufacturer_data?: string;
  ble_version?: number;
  firmware_ver?: number;
  led_version?: number;
  product_id?: number;
  rf_mode?: string;
  rf_paired_count?: number;
}

/**
 * PingResult — the structured EEPROM probe result from executePingDevice / pingDevice.
 * Extends HardwareSettingsResult (parseSettingsResponse return type) with optional
 * RF state fields accumulated during the wizard ping flow.
 * Returns null if the probe timed out before any telemetry was received.
 *
 * Source of truth: IControllerProtocol.ts HardwareSettingsResult §EEPROM
 */
export interface PingResult {
  // From HardwareSettingsResult (parseSettingsResponse)
  ledPoints: number;
  segments: number;
  icType: number;
  icName: string;
  colorSorting: number;
  colorSortingName: string;
  firmwareVer?: number;
  ledVersion?: number;
  bleVersion?: number;
  detected: boolean;
  // RF state (from parseRfRemoteState, accumulated separately)
  rfMode?: string;
  rfPairedCount?: number;
  // Extra fields from firmware advertisement
  productId?: number;
  [key: string]: unknown;
}

/**
 * Type guard for PingResult — validates the minimum EEPROM fields are present.
 * Use instead of `as unknown as PingResult` to safely narrow Partial<PingResult>.
 */
export function isPingResult(p: Partial<PingResult> | null | undefined): p is PingResult {
  return (
    p != null &&
    typeof p.ledPoints === 'number' &&
    typeof p.icType === 'number' &&
    typeof p.icName === 'string' &&
    p.detected === true
  );
}


export interface DeviceSettings {
  name: string;
  /** Widened from 'HALOZ' | 'SOULZ' to support all catalog product types (e.g. RAILZ). */
  type: string;
  provenance?: 'PROBED' | 'MANUALLY_CONFIGURED' | 'UNCONFIGURED';
  points?: number;
  segments?: number;
  stripType?: string;
  sorting?: string;
  grouped: boolean;
  groupIds?: string[];
  groupNames?: string[];
  /** Firmware version string as reported by the device — e.g. 'v2.0.1' or 'Unknown'. */
  firmware?: string;
  /**
   * ISO timestamp of the last explicit user-initiated Save in the Settings Modal.
   * When present, this config has USER priority and must not be overwritten by cloud
   * defaults or BLE probe results unless the probe returns a strictly higher timestamp.
   */
  userConfiguredAt?: string;
  rfMode?: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED';
  rfRemotes?: string[];
  /** ZENGGE hardware product ID reported during BLE pairing (0xA3 = 163 for all SK8Lytz hardware). */
  productId?: number;
  [key: string]: unknown;
}



export interface CustomGroup {
  id: string;
  name: string;
  isGroup: boolean;
  deviceIds: string[];
  type?: string;
  /** Standardized persistence field — last pattern name broadcast to this group. */
  lastPatternName?: string;
}

/**
 * GroupPatternSnapshot — lean color snapshot stored in lastGroupPatterns per group.
 * Replaces the legacy plain string so group cards can render accurate colors.
 * patternId → SK8LYTZ_TEMPLATES lookup drives GENERATIVE vs FG_BG vs FG_ONLY rendering.
 */
export interface GroupPatternSnapshot {
  /** Human-readable label shown on the card pill (e.g. "Pro Effects – Crimson") */
  patternLabel: string;
  /** Pattern template ID — used to look up colorMode (GENERATIVE/FG_BG/FG_ONLY) */
  patternId?: number;
  /** Active mode at time of dispatch */
  mode: string;
  /** Foreground color hex (undefined for GENERATIVE patterns) */
  fgColor?: string;
  /** Background color hex */
  bgColor?: string;
  /** Builder mode node colors */
  multiColors?: string[];
}

// ─── UI State FSMs (replaces scattered boolean flags) ───────────────────────

/** FSM for the group creation / rename modal. Replaces isGroupModalVisible + groupModalMode booleans. */
export type GroupModalState = 'HIDDEN' | 'CREATE' | 'RENAME';

/** FSM for the favorites / quick-preset naming prompt. Replaces isFavPromptVisible + isQuickPromptVisible. */
export type FavoritesPromptState = 'HIDDEN' | 'NAMING_FAVORITE' | 'NAMING_PRESET';

/** FSM for the GPS skate session recording lifecycle. Replaces sessionActive: boolean. */
export type SessionState = 'IDLE' | 'RECORDING' | 'COMPLETE';

/** FSM for the accelerometer-derived vehicle motion classification. */
export type MotionState =
  | 'STOPPED'
  | 'ACCELERATING'
  | 'CRUISING'
  | 'SLOWING_DOWN'
  | 'HARD_BRAKING';

/** FSM for the BLE connection lifecycle, replacing isScanning / isConnecting booleans. */
export type BleConnectionState =
  | 'IDLE'
  | 'SCANNING'
  | 'CONNECTING'
  | 'PROBING'
  | 'READY'
  | 'DISCONNECTING'
  | 'ERROR';

/** Per-device connection state for individual badges/indicators. */
export type DeviceConnectionState =
  | 'connected'
  | 'connecting'
  | 'reconnecting'
  | 'disconnected'
  | 'out_of_range';

/** FSM for the primary Dashboard view router. */
export type DashboardViewState =
  | 'LOADING_REGS'
  | 'SETUP_WIZARD'
  | 'DASHBOARD'
  | 'CREW_HUB'
  | 'OFFLINE';

export type ModeType =
  | 'FAVORITES'
  | 'MULTIMODE'
  | 'BUILDER'
  | 'MUSIC'
  | 'STREET'
  | 'CAMERA';


/** Sub-mode for the consolidated MultiMode / Fixed tab in DockedController. */
export type FixedSubMode = 'PATTERN' | 'BUILDER';

/** Pattern state for the Fixed Mode tab. */
export type FixedModePattern = 'STATIC' | 'STROBE' | 'BLINK';

// ─── Hardware Settings Interface ─────────────────────────────────────────────

/**
 * Typed replacement for the `hwSettings?: any` prop on DockedController.
 * Maps directly to the 0x63 EEPROM response fields parsed by ZenggeProtocol.
 */
export interface IHardwareSettings {
  ledPoints?: number;
  segments?: number;
  icType?: number;
  icName?: string;
  colorSorting?: number;
  colorSortingName?: string;
  detected?: boolean;
  [key: string]: unknown;
}

// ─── DockedController Domain Types (migrated from DockedController.tsx) ───────

/** Mic audio source for Music mode. */
export type MicSource = 'APP' | 'DEVICE';

/** Color focus selector for Music mode dual-color picker. */
export type MusicColorFocus = 'PRIMARY' | 'SECONDARY';

/** A connected BLE device visible to the controller. */
export interface IDeviceState {
  id: string;
  name: string;
  points?: number;
  segments?: number;
  sorting?: 'RGB' | 'GRB' | 'BRG' | 'RBG' | 'BGR' | 'GBR';
  [key: string]: unknown; // safe loose fallback for undocumented BLE peripheral keys
}

/**
 * DisplayDevice — the enriched shape of a connected BLE device after the
 * displayConnectedDevices useMemo in DashboardScreen injects DeviceConfigs
 * and RegisteredDevices data on top of the raw react-native-ble-plx Device.
 *
 * Fields come from three sources:
 *   1. BLE Device (react-native-ble-plx): id, name, rssi, mtu
 *   2. deviceConfigs spread: points, segments, sorting, stripType, detected, firmware
 *   3. displayConnectedDevices injector: type, groupId
 */
export interface DisplayDevice {
  // ── From BLE Device ───────────────────────────────────────
  id: string;
  name: string | null;
  rssi?: number | null;
  mtu?: number;
  // ── From deviceConfigs spread ─────────────────────────────
  points?: number;
  segments?: number;
  sorting?: string;
  colorSorting?: number;
  colorSortingName?: string;
  stripType?: string;
  icType?: number;
  icName?: string;
  detected?: boolean;
  firmware?: string;
  ledPoints?: number;
  groupIds?: string[];
  // ── Injected by displayConnectedDevices resolver ──────────
  /** Product type resolved from deviceConfigs → BLE → registeredDevices chain */
  type?: string;
  // ── Safe catch-all for undocumented BLE / config fields ───
  [key: string]: unknown;
}

/** A saved light preset (user favorite or SK8Lytz Pick). */
export interface IFavoriteState {
  id: string;
  name: string;
  customName?: string;
  mode: string;
  color?: string;
  patternId?: number;
  speed: number;
  brightness: number;
  fixedColorMode?: 'FOREGROUND' | 'BACKGROUND';
  fixedFgColor?: string;
  fixedBgColor?: string;
  fixedHue?: number;
  multiColors?: string[];
  multiTransition?: number;
  multiLength?: number;
  musicPrimaryColor?: string;
  musicSecondaryColor?: string;
  micSensitivity?: number;
  micSource?: MicSource;
  musicMatrixStyle?: number;
  // ── BUILDER mode persistence (feat/pattern-favorites-v2) ─────────────
  /** Saved gradient node array — each node: { id, position 0–100, colorHex } */
  builderNodes?: Array<{ id: string; position: number; colorHex: string }>;
  builderFillMode?: 'GRADIENT' | 'SOLID';
  /** commandType byte */
  builderTransitionType?: number;
  builderDirection?: number;
  // ── SCENE mode persistence ─────────────
  sceneSteps?: unknown[];
}

/** A quick color preset for the Builder sub-mode. */
export interface IQuickPreset {
  name: string;
  colors: string[];
  type: number;
}

// ─── DockedBus — Panel Communication Contract ─────────────────────────────────

/**
 * DockedBus — the typed contract between DockedController and its mode panels.
 *
 * Panels receive this as a single `bus` prop and MUST NOT reach outside it
 * for state or write operations. This enforces the Hollow Shell isolation boundary.
 *
 * The bus object is stabilized via `useMemo` inside DockedController so that
 * React.memo panels only re-render when their specific slice of data changes.
 */
export interface DockedBus {
  // ── Write pipeline ────────────────────────────────────────────────────────
  /** Debounced + mutex-guarded BLE write. Use for all non-critical writes. */
  writeToDevice: (payload: number[], writeTypeOrOverride?: 'Response' | 'NoResponse' | Record<string, unknown>) => Promise<void>;
  /** Current BLE write status — drives the status indicator dot in the visualizer. */
  writeStatus: import('../hooks/useOptimisticBLE').BLEWriteStatus;
  // ── Shared display parameters ─────────────────────────────────────────────
  brightness: number;
  speed: number;
  selectedColor: string;
  points?: number;
  hwSettings?: IHardwareSettings;
  // ── ProEffects / MULTIMODE panel ─────────────────────────────────────────
  fixedPatternId: number;
  setFixedPatternId: (id: number) => void;
  fixedFgColor: string;
  fixedBgColor: string;
  fixedDirection: number;
  applyFixedPattern: (patternId: number, fg: string, bg: string, spd?: number, brt?: number, dir?: number) => void;
}

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

export interface DeviceSettings {
  name: string;
  /** Widened from 'HALOZ' | 'SOULZ' to support all catalog product types (e.g. RAILZ). */
  type: string;
  points: number;
  segments: number;
  stripType: string;
  sorting: string;
  grouped: boolean;
  groupId?: string;
  groupName?: string;
  /** Firmware version string as reported by the device — e.g. 'v2.0.1' or 'Unknown'. */
  firmware?: string;
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

/** FSM for the primary Dashboard view router. */
export type DashboardViewState =
  | 'LOADING_REGS'
  | 'SETUP_WIZARD'
  | 'DASHBOARD'
  | 'CREW_HUB'
  | 'OFFLINE';

/** FSM for the DockedController primary mode selector. */
export type ModeType =
  | 'FAVORITES'
  | 'MULTIMODE'
  | 'PROGRAMS'
  | 'MUSIC'
  | 'STREET'
  | 'CAMERA';

/** Sub-mode for the consolidated MultiMode / Fixed tab in DockedController. */
export type FixedSubMode = 'PATTERN' | 'BUILDER';

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
}

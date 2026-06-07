/**
 * ble.types.ts — Shared BLE pipeline type definitions.
 *
 * Centralizes all BleManager, Device, and connection-related types so the
 * BLE pipeline can be fully typed without any `any` casts.
 *
 * Re-exports the official react-native-ble-plx types alongside our own
 * domain-specific interfaces.
 */
import type React from 'react';
import type { BleManager, Characteristic, Device, Subscription } from 'react-native-ble-plx';
import type { BLEPhaseTag } from '../services/ble/BleMachine.types';
import type { IControllerProtocol } from '../protocols/IControllerProtocol';

// Re-export library types so consumers import from one place
export type { BleManager, Characteristic, Device, Subscription };

// ── Supabase row shapes ────────────────────────────────────────────────────────

/** Row shape from the `registered_groups` Supabase table */
export interface RegisteredGroup {
  id: string;
  group_name: string;
  created_at: string;
  user_id?: string;
  /** Offline-only: MACs belonging to this group (not a DB column) */
  deviceIds?: string[];
}

/** Row shape from the `registered_devices` Supabase table (columns used in auto-connect) */
export interface RegisteredDeviceRow {
  id: string;
  device_mac: string;
  user_id: string;
  /** Many-to-many: array of group IDs this device belongs to */
  group_ids?: string[];
  /** Legacy scalar group_id (pre-migration) — MIGRATION-SHIM: remove at v3.9.0 */
  group_id?: string;
}

// ── Duck-typed BLE sub-system handles ─────────────────────────────────────────
// These interfaces let BleConnectionManager depend only on the specific
// methods it calls, without importing the full hook return type (circular).

/** Sweeper handle — only methods used by BleConnectionManager */
export interface BleSweeperHandle {
  isSweeperActive: boolean;
  stopSweeper(): void;
  startSweeper(): void;
}

/** Scanner handle — only method used by BleConnectionManager */
export interface BleScannerHandle {
  stopScanner(): void;
}

/** Auto-recovery handle — kept in request shape for future cancel-on-connect use */
export interface BleAutoRecoveryHandle {
  cancelAllRecoveries(): Promise<void>;
}

// ── Primary connection contract ────────────────────────────────────────────────

/**
 * BleConnectionRequest — replaces the 13 positional `any` parameters of
 * executeConnectToDevices() with a single typed options object.
 *
 * Grouping logically related refs/callbacks makes call sites self-documenting
 * and lets TypeScript catch wrong argument types at compile time.
 */
export interface BleConnectionRequest {
  /** Devices to connect (from allDevices scan list) */
  devices: Device[];
  /** The react-native-ble-plx BleManager singleton */
  bleManager: BleManager;
  /** Ref holding currently connected Device objects */
  connectedDevicesRef: React.MutableRefObject<Device[]>;
  /** MACs (uppercase) that are hardware-blacklisted */
  blacklistedMacsRef: React.MutableRefObject<string[]>;
  /** Keepalive timer ref — cancelled on reconnect */
  keepaliveTimerRef: React.MutableRefObject<ReturnType<typeof setTimeout> | null>;
  /** Per-device disconnect subscription map */
  disconnectListeners: React.MutableRefObject<Record<string, Subscription>>;
  /** Sweeper handle — stopped before connect, restarted after */
  sweeper: BleSweeperHandle;
  /** Scanner handle — stopped before connect */
  scanner: BleScannerHandle;
  /** Auto-recovery handle (reserved for future cancel-on-connect pattern) */
  autoRecovery: BleAutoRecoveryHandle;
  /** Getter for the current BLE phase tag */
  getGate: () => BLEPhaseTag;
  /** Per-device negotiated MTU map */
  mtuMapRef: React.MutableRefObject<Map<string, number>>;
  /** Per-device resolved protocol adapter map */
  adapterMapRef: React.MutableRefObject<Map<string, IControllerProtocol>>;
  /** Raw notification data callback (wired to monitor) */
  dataReceivedCallbackRef: React.MutableRefObject<((deviceId: string, data: number[]) => void) | undefined>;
  /**
   * Notification handler for GATT characteristic changes.
   * `error` is null on success; `characteristic` is null on error.
   */
  handleNotificationRef: React.MutableRefObject<(
    error: Error | null,
    characteristic: Characteristic | null,
    deviceId: string,
  ) => void>;
  /** Called on organic (unexpected) GATT disconnect */
  handleOrganicDisconnect: (error: Error | null, deviceId: string) => void;
  /** React state setter for connected devices */
  setConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  /** Gate phase setter */
  setGate: (phase: BLEPhaseTag) => void;
}

/**
 * ble.types.ts — Shared BLE pipeline type definitions.
 *
 * Centralizes all BleManager, Device, and connection-related types so the
 * BLE pipeline can be fully typed without any `any` casts.
 *
 * Re-exports the official react-native-ble-plx types alongside our own
 * domain-specific interfaces.
 */
import type { BleManager, Device, Subscription } from 'react-native-ble-plx';

// Re-export library types so consumers import from one place
export type { BleManager, Device, Subscription };

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
// NOTE: BleConnectionRequest, BleSweeperHandle, BleScannerHandle, and
// BleAutoRecoveryHandle have been removed. They were scaffolding for
// BleConnectionManager.ts which was replaced by ConnectService.ts (Phase 3).
// Connection is now fully owned by the XState BleMachine.

import type { CustomGroup, DeviceSettings } from '../../types/dashboard.types';
import type { TablesInsert } from '../../types/supabase';

// ─── RegisteredDevice ─────────────────────────────────────────────────────────
// Moved here (from hooks/useRegistration) to break the barrel back-edge cycle.
// hooks/useRegistration re-exports this for backward compatibility.
export interface RegisteredDevice {
  id?: string;
  user_id?: string;
  device_mac: string;
  device_name: string;
  custom_name?: string;
  /** Product type — must match a `ProductProfile.id` in LOCAL_PRODUCT_CATALOG. */
  product_type: 'HALOZ' | 'SOULZ' | 'RAILZ' | string;
  position: 'Left' | 'Right' | null;
  group_names?: string[];
  group_ids?: string[];
  /** @deprecated MIGRATION-SHIM: Legacy scalar field. Use group_ids[] instead. Remove at v3.9.0 */
  group_id?: string;
  /** @deprecated MIGRATION-SHIM: Legacy scalar field. Use group_names[] instead. Remove at v3.9.0 */
  group_name?: string;
  led_points?: number;
  segments?: number;
  ic_type?: string;
  color_sorting?: string;
  rssi_at_register?: number;
  // Secondary fingerprint for MAC-rotation fallback
  firmware_ver?: number;
  led_version?: number;
  ble_version?: number;
  product_id?: number;
  product_id_confirmed_at?: string | null;
  rf_mode?: string;
  rf_paired_count?: number;
  factory_name?: string;
  manufacturer_data?: string;
  // Location natively captured during registration
  last_lat?: number;
  last_lng?: number;
  // Offline sync state
  is_pending_sync?: boolean;
  registered_at?: string;
  updated_at?: string;
}

// ─── Local Supabase Insert Type Aliases ───────────────────────────────────────
// Derived directly from generated types — stay in sync with schema automatically.
export type DeviceInsertRow = TablesInsert<'registered_devices'>;
export type GroupInsert     = Omit<TablesInsert<'registered_groups'>, 'created_at'>;

export interface DeviceRepositorySnapshot {
  devices: RegisteredDevice[];
  configs: Record<string, DeviceSettings>;
  groups: CustomGroup[];
  tombstones: string[];
}

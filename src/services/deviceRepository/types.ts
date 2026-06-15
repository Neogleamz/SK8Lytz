import type { RegisteredDevice } from '../../hooks/useRegistration';
import type { CustomGroup, DeviceSettings } from '../../types/dashboard.types';
import type { TablesInsert } from '../../types/supabase';

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

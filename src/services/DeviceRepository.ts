/**
 * DeviceRepository.ts — Single Source of Truth for Device & Group Persistence
 *
 * Phase 3 of the Device Pipeline Stabilization epic.
 *
 * This singleton service owns ALL device + group persistence:
 *   - AsyncStorage reads/writes (local-first)
 *   - Supabase sync (cloud second)
 *   - Tombstone management (anti-resurrection)
 *   - Config merging (local-wins for user-configured, cloud-wins for metadata)
 *
 * Hooks (useRegistration, useDashboardGroups) delegate to this service
 * but keep their own React state for rendering. The Repository is the
 * canonical write path — hooks are read projections.
 *
 * Identity invariant: All MAC keys are UPPERCASE.
 */

import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import { supabase } from './supabaseClient';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import type { RegisteredDevice } from '../hooks/useRegistration';
import type { CustomGroup, DeviceSettings } from '../types/dashboard.types';

// ─── Storage Keys ─────────────────────────────────────────────────────────────
const DEVICES_KEY     = '@Sk8lytz_registered_devices';
const CONFIGS_KEY     = '@Sk8lytz_device_configs';
const GROUPS_KEY      = '@Sk8lytz_custom_groups';
const TOMBSTONE_KEY   = '@Sk8lytz_deleted_macs';
const PENDING_KEY     = '@Sk8lytz_pending_sync';
const PROCESSED_KEY   = '@Sk8lytz_processed_devices';
const PATTERNS_KEY    = '@Sk8lytz_last_group_patterns';

// ─── Listener Type ────────────────────────────────────────────────────────────
type Listener = () => void;

// ─── Snapshot Type ────────────────────────────────────────────────────────────
export interface DeviceRepositorySnapshot {
  devices: RegisteredDevice[];
  configs: Record<string, DeviceSettings>;
  groups: CustomGroup[];
  tombstones: string[];
}

/**
 * Singleton service that owns all device fleet persistence.
 *
 * Usage from hooks:
 *   const repo = DeviceRepository.getInstance();
 *   await repo.saveDevice(device);
 *   const devices = repo.getDevices();
 */
class DeviceRepository {
  private static instance: DeviceRepository;

  // ── In-memory state ─────────────────────────────────────────────────────────
  private devices: RegisteredDevice[] = [];
  private configs: Record<string, DeviceSettings> = {};
  private groups: CustomGroup[] = [];
  private tombstones: string[] = [];
  private isInitialized = false;
  private initPromise: Promise<void> | null = null;

  // ── Subscriber pattern for useSyncExternalStore ─────────────────────────────
  private listeners: Set<Listener> = new Set();
  private version = 0;

  private constructor() {}

  static getInstance(): DeviceRepository {
    if (!DeviceRepository.instance) {
      DeviceRepository.instance = new DeviceRepository();
    }
    return DeviceRepository.instance;
  }

  // ── Initialization ──────────────────────────────────────────────────────────

  /**
   * Load all persisted state from AsyncStorage.
   * Safe to call multiple times — subsequent calls return the same Promise.
   */
  async initialize(): Promise<void> {
    if (this.isInitialized) return;
    if (this.initPromise) return this.initPromise;

    this.initPromise = this._loadFromStorage();
    await this.initPromise;
    this.isInitialized = true;
  }

  private async _loadFromStorage(): Promise<void> {
    try {
      const [devicesRaw, configsRaw, groupsRaw, tombRaw] = await Promise.all([
        AsyncStorage.getItem(DEVICES_KEY),
        AsyncStorage.getItem(CONFIGS_KEY),
        AsyncStorage.getItem(GROUPS_KEY),
        AsyncStorage.getItem(TOMBSTONE_KEY),
      ]);

      if (devicesRaw) {
        try { this.devices = JSON.parse(devicesRaw); } catch { this.devices = []; }
      }
      if (configsRaw) {
        try { this.configs = JSON.parse(configsRaw); } catch { this.configs = {}; }
      }
      if (groupsRaw) {
        try { this.groups = JSON.parse(groupsRaw); } catch { this.groups = []; }
      }
      if (tombRaw) {
        try { this.tombstones = JSON.parse(tombRaw); } catch { this.tombstones = []; }
      }

      AppLogger.warn('[DeviceRepository] initialized', {
        event: 'initialized',
        deviceCount: this.devices.length,
        groupCount: this.groups.length,
        tombstoneCount: this.tombstones.length,
      });
    } catch (e) {
      AppLogger.warn('[DeviceRepository] Storage load failed:', e);
    }
  }

  // ── Read Accessors ──────────────────────────────────────────────────────────

  getDevices(): RegisteredDevice[] { return this.devices; }
  getConfigs(): Record<string, DeviceSettings> { return this.configs; }
  getGroups(): CustomGroup[] { return this.groups; }
  getTombstones(): string[] { return this.tombstones; }

  getSnapshot(): DeviceRepositorySnapshot {
    return {
      devices: this.devices,
      configs: this.configs,
      groups: this.groups,
      tombstones: this.tombstones,
    };
  }

  /**
   * Find a device by its MAC address (case-insensitive).
   */
  findDevice(mac: string): RegisteredDevice | undefined {
    const normalized = mac.toUpperCase();
    return this.devices.find(d => d.device_mac.toUpperCase() === normalized);
  }

  /**
   * Get config for a specific device MAC.
   */
  getConfig(mac: string): DeviceSettings | undefined {
    return this.configs[mac.toUpperCase()];
  }

  // ── Device Mutations ────────────────────────────────────────────────────────

  /**
   * Save (upsert) a device to local storage and optionally to Supabase.
   * This is the canonical write path — all hooks delegate here.
   */
  async saveDevice(device: Partial<RegisteredDevice> & { device_mac: string }): Promise<boolean> {
    try {
      const { data: { user } } = await supabase.auth.getUser();
      const now = new Date().toISOString();
      const normalizedMac = device.device_mac.toUpperCase();
      const deviceId = device.id || `${normalizedMac.replace(/:/g, '')}-${user?.id?.slice(0, 8) || 'offline'}`;
      const groupId = device.group_id || device.group_name?.toLowerCase().replace(/\s+/g, '-') || 'default-fleet';

      const fullDevice: RegisteredDevice = {
        device_name: device.device_name || 'Unknown Device',
        product_type: device.product_type || LOCAL_PRODUCT_CATALOG[0].id,
        group_name: device.group_name || 'Default Fleet',
        position: device.position || null,
        ...device,
        device_mac: normalizedMac,
        id: deviceId,
        group_id: groupId,
        user_id: user?.id,
        updated_at: now,
        registered_at: device.registered_at || now,
        is_pending_sync: false,
      };

      // 1. Update in-memory state
      const idx = this.devices.findIndex(d => d.device_mac.toUpperCase() === normalizedMac);
      if (idx >= 0) this.devices[idx] = fullDevice;
      else this.devices.push(fullDevice);

      // 2. Persist to AsyncStorage
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices));

      // 3. Notify subscribers
      this._notifyListeners();

      // 4. Sync to Supabase
      if (user) {
        // Phase 5: Atomic group upsert via server-side RPC (single transaction)
        try {
          await supabase.rpc('upsert_group_with_devices', {
            p_group_id:   fullDevice.group_id,
            p_group_name: fullDevice.group_name || 'Default Fleet',
            p_type:       'device-fleet',
            p_device_ids: [deviceId],
          });
        } catch (_rpc) {
          AppLogger.warn('[DeviceRepository] upsert_group_with_devices RPC failed, falling back:', _rpc);
          try {
            await supabase.from('registered_groups').upsert({
              id: fullDevice.group_id,
              group_name: fullDevice.group_name || 'Default Fleet',
              type: 'device-fleet',
              user_id: user.id
            } as any, { onConflict: 'id' } as any);
          } catch (_fk) {
            AppLogger.warn('[DeviceRepository] Group FK fallback also failed:', _fk);
          }
        }

        // Spread-if-valid: only include hardware fields with real data
        const validPoints   = fullDevice.led_points && fullDevice.led_points > 0   ? fullDevice.led_points   : undefined;
        const validSegments = fullDevice.segments   && fullDevice.segments   > 0   ? fullDevice.segments     : undefined;
        const validIcType   = fullDevice.ic_type    && fullDevice.ic_type !== 'UNKNOWN' ? fullDevice.ic_type : undefined;
        const validSorting  = fullDevice.color_sorting && fullDevice.color_sorting !== 'UNKNOWN' ? fullDevice.color_sorting : undefined;

        const dbRow = {
          device_mac:      fullDevice.device_mac,
          device_name:     fullDevice.device_name,
          product_type:    fullDevice.product_type,
          position:        fullDevice.position,
          group_name:      fullDevice.group_name,
          group_id:        fullDevice.group_id,
          custom_name:     fullDevice.device_name,
          user_id:         user.id,
          id:              fullDevice.id || deviceId,
          updated_at:      now,
          registered_at:   fullDevice.registered_at,
          rssi_at_register: fullDevice.rssi_at_register,
          firmware_ver:    fullDevice.firmware_ver,
          led_version:     fullDevice.led_version,
          product_id:      fullDevice.product_id,
          rf_mode:         fullDevice.rf_mode,
          rf_paired_count: fullDevice.rf_paired_count,
          ...(validPoints   !== undefined ? { points: validPoints,   led_points: validPoints }   : {}),
          ...(validSegments !== undefined ? { segments: validSegments }                           : {}),
          ...(validIcType   !== undefined ? { ic_type: validIcType,  strip_type: validIcType }   : {}),
          ...(validSorting  !== undefined ? { color_sorting: validSorting, sorting: validSorting } : {}),
        };

        const { error } = await supabase
          .from('registered_devices')
          .upsert(dbRow as any, { onConflict: 'user_id,device_mac' } as any);

        if (error) throw error;
      } else {
        await this._queuePendingSync(fullDevice);
      }

      return true;
    } catch (e) {
      AppLogger.warn('[DeviceRepository] Save failed, queuing offline:', e);
      await this._queuePendingSync(device);
      return false;
    }
  }

  /**
   * Save multiple devices atomically (used by wizard completion).
   */
  async saveAllDevices(devices: RegisteredDevice[]): Promise<boolean> {
    let allOk = true;
    for (const d of devices) {
      const ok = await this.saveDevice(d);
      if (!ok) allOk = false;
    }
    return allOk;
  }

  /**
   * Delete a device by MAC address. Writes tombstone, removes locally, then syncs to cloud.
   */
  async deleteDevice(deviceMac: string): Promise<void> {
    const normalizedMac = deviceMac.toUpperCase();

    try {
      const { data: { user } } = await supabase.auth.getUser();

      // Step 1: Write tombstone (prevents cloud resurrection)
      if (!this.tombstones.includes(normalizedMac)) {
        this.tombstones.push(normalizedMac);
        await AsyncStorage.setItem(TOMBSTONE_KEY, JSON.stringify(this.tombstones)).catch(() => {});
      }

      // Step 2: Remove from in-memory devices
      this.devices = this.devices.filter(d => d.device_mac.toUpperCase() !== normalizedMac);
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices));

      // Step 3: Scrub device config
      if (this.configs[normalizedMac]) {
        delete this.configs[normalizedMac];
        await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch(() => {});
      }

      // Step 4: Notify subscribers
      this._notifyListeners();

      // Step 5: Remove from Supabase
      if (user) {
        AppLogger.warn('[DeviceRepository] DEREGISTER_ATTEMPT', { mac: normalizedMac, userId: user.id });
        const { error, count } = await supabase
          .from('registered_devices')
          .delete({ count: 'exact' })
          .eq('user_id', user.id)
          .ilike('device_mac', normalizedMac);

        if (error) {
          AppLogger.warn('[DeviceRepository] DB delete error (RLS?)', { mac: normalizedMac, error: error.message });
          throw error;
        }

        AppLogger.warn('[DeviceRepository] DEREGISTER_RESULT', { mac: normalizedMac, rowsDeleted: count });
      }
    } catch (e: any) {
      AppLogger.warn('[DeviceRepository] Deregister failed:', e);
      // Tombstone still prevents resurrection even if Supabase delete fails
    }
  }

  // ── Config Mutations ────────────────────────────────────────────────────────

  /**
   * Update device config by MAC. Merges with existing config.
   */
  async updateConfig(mac: string, patch: Partial<DeviceSettings>): Promise<void> {
    const key = mac.toUpperCase();
    this.configs[key] = { ...(this.configs[key] || {} as DeviceSettings), ...patch };
    await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch(() => {});
    this._notifyListeners();
  }

  /**
   * Replace entire configs map (used by hardware notification handlers).
   */
  async setConfigs(configs: Record<string, DeviceSettings>): Promise<void> {
    this.configs = configs;
    await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch(() => {});
    this._notifyListeners();
  }

  /**
   * Delete a config entry by MAC.
   */
  async deleteConfig(mac: string): Promise<void> {
    const key = mac.toUpperCase();
    if (this.configs[key]) {
      delete this.configs[key];
      await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch(() => {});
      this._notifyListeners();
    }
  }

  // ── Group Mutations ─────────────────────────────────────────────────────────

  /**
   * Set groups directly (used by group derivation logic in useDashboardGroups).
   */
  async setGroups(groups: CustomGroup[]): Promise<void> {
    this.groups = groups;
    await AsyncStorage.setItem(GROUPS_KEY, JSON.stringify(this.groups)).catch(() => {});
    this._notifyListeners();
  }

  /**
   * Remove a group by ID from local state and cloud (atomic via RPC).
   * Phase 5: Uses delete_group_cascade RPC to clear device group_ids and
   * delete the group row in a single server-side transaction.
   */
  async deleteGroup(groupId: string): Promise<void> {
    // 1. Remove group row
    this.groups = this.groups.filter(g => g.id !== groupId);
    await AsyncStorage.setItem(GROUPS_KEY, JSON.stringify(this.groups)).catch(() => {});

    // 2. Clear group assignments from in-memory devices
    let devicesChanged = false;
    this.devices = this.devices.map(d => {
      if (d.group_id === groupId) {
        devicesChanged = true;
        return { ...d, group_id: 'default-fleet', group_name: 'Default Fleet' };
      }
      return d;
    });
    if (devicesChanged) await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices));

    // 3. Notify
    this._notifyListeners();

    // Cloud cleanup via atomic RPC
    try {
      const { error } = await supabase.rpc('delete_group_cascade', {
        p_group_id: groupId,
      });
      if (error) throw error;
    } catch (e) {
      AppLogger.warn('[DeviceRepository] delete_group_cascade RPC failed, falling back:', e);
      // Graceful fallback: direct row delete (membership cleared on next sync)
      try {
        const { data: { session } } = await supabase.auth.getSession();
        if (session?.user) {
          await supabase.from('registered_groups').delete()
            .eq('id', groupId)
            .eq('user_id', session.user.id);
        }
      } catch (fe) {
        AppLogger.warn('[DeviceRepository] Cloud group delete fallback also failed:', fe);
      }
    }
  }

  /**
   * Transactional group save: atomically upserts the group and bulk-assigns
   * device membership in one RPC call (prevents partial-write corruption).
   *
   * Called by useDashboardGroups.saveGroup() for explicit group CRUD operations.
   */
  async saveGroupTransactional(
    groupId: string,
    groupName: string,
    deviceMacs: string[],  // Array of MAC addresses (frontend convention)
    type = 'device-fleet'
  ): Promise<boolean> {
    // 1. Update local group state immediately (optimistic)
    const existingIdx = this.groups.findIndex(g => g.id === groupId);
    const updatedGroup: CustomGroup = existingIdx >= 0
      ? { ...this.groups[existingIdx], id: groupId, name: groupName, deviceIds: deviceMacs }
      : { id: groupId, name: groupName, color: undefined, deviceIds: deviceMacs } as any;

    if (existingIdx >= 0) this.groups[existingIdx] = updatedGroup;
    else this.groups.push(updatedGroup);
    await AsyncStorage.setItem(GROUPS_KEY, JSON.stringify(this.groups)).catch(() => {});

    // In-memory mapping: convert frontend MAC addresses to Supabase registered_devices PKs
    // AND update in-memory devices for immediate UI rendering.
    const normalizedMacs = deviceMacs.map(m => m.toUpperCase());
    let devicesChanged = false;
    const dbDeviceIds: string[] = [];

    this.devices = this.devices.map(d => {
      const mac = d.device_mac.toUpperCase();
      // Case A: Part of the new group
      if (normalizedMacs.includes(mac)) {
        if (d.id) dbDeviceIds.push(d.id);
        if (d.group_id !== groupId || d.group_name !== groupName) {
          devicesChanged = true;
          return { ...d, group_id: groupId, group_name: groupName };
        }
      } 
      // Case B: Booted out of this group
      else if (d.group_id === groupId) {
        devicesChanged = true;
        return { ...d, group_id: 'default-fleet', group_name: 'Default Fleet' };
      }
      return d;
    });

    if (devicesChanged) await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices));
    this._notifyListeners();

    // 2. Cloud: atomic RPC transaction
    try {
      const { error } = await supabase.rpc('upsert_group_with_devices', {
        p_group_id:   groupId,
        p_group_name: groupName,
        p_type:       type,
        p_device_ids: dbDeviceIds,
      });
      if (error) throw error;
      return true;
    } catch (e) {
      AppLogger.warn('[DeviceRepository] saveGroupTransactional RPC failed:', e);
      return false;
    }
  }

  // ── Cloud Sync ──────────────────────────────────────────────────────────────

  /**
   * Sync registered devices from Supabase, applying tombstone filter
   * and local-first smart merge.
   */
  async syncFromCloud(): Promise<RegisteredDevice[]> {
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return this.devices;

      const { data, error } = await supabase
        .from('registered_devices')
        .select('*')
        .eq('user_id', user.id)
        .order('registered_at', { ascending: true });

      if (error) throw error;
      if (!data) return this.devices;

      // Tombstone filter
      const cloudRows = data.filter((row: any) =>
        !this.tombstones.includes(row.device_mac?.toUpperCase?.())
      );

      if (cloudRows.length < data.length) {
        AppLogger.warn('[DeviceRepository] syncFromCloud tombstone filtered', {
          skipped: data.length - cloudRows.length,
        });
      }

      // Local-first smart merge
      const merged: RegisteredDevice[] = cloudRows.map((row: Record<string, any>) => {
        const cloud = { ...row, is_pending_sync: false } as RegisteredDevice;
        const local = this.devices.find(
          l => l.device_mac.toUpperCase() === cloud.device_mac.toUpperCase()
        );

        if (!local) return cloud;

        const localHasPendingChanges = !!local.is_pending_sync;
        const localHasValidPoints    = (local.led_points ?? 0) > 0;
        const localHasValidSegments  = (local.segments ?? 0) > 0;
        const localHasValidIcType    = local.ic_type && local.ic_type !== 'UNKNOWN' && local.ic_type !== 'WS2812B';
        const localHasValidSorting   = local.color_sorting && local.color_sorting !== 'UNKNOWN';

        return {
          ...cloud,
          led_points:    (localHasPendingChanges || localHasValidPoints)    ? local.led_points    : cloud.led_points,
          segments:      (localHasPendingChanges || localHasValidSegments)  ? local.segments      : cloud.segments,
          ic_type:       (localHasPendingChanges || localHasValidIcType)    ? local.ic_type       : cloud.ic_type,
          color_sorting: (localHasPendingChanges || localHasValidSorting)   ? local.color_sorting : cloud.color_sorting,
          group_id:      cloud.group_id,
          group_name:    cloud.group_name,
          device_name:   cloud.device_name || local.device_name,
          is_pending_sync: localHasPendingChanges,
        };
      });

      // Update in-memory state
      this.devices = merged;
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(merged));
      this._notifyListeners();

      // Flush any pending offline registrations
      await this._flushPendingSync(user.id);

      return merged;
    } catch (e) {
      AppLogger.warn('[DeviceRepository] Cloud sync failed (offline?):', e);
      return this.devices;
    }
  }

  // ── Claim Check ─────────────────────────────────────────────────────────────

  /**
   * Check whether a device MAC is claimed by the current user, another user, or unclaimed.
   */
  async checkDeviceClaimed(
    deviceMac: string,
    fingerprint?: { firmwareVer?: number; ledVersion?: number; productId?: number }
  ): Promise<'unclaimed' | 'claimed_by_self' | 'claimed_by_other' | 'offline_unknown'> {
    try {
      // Fast path: check local
      const normalized = deviceMac.toUpperCase();
      if (this.devices.some(d => d.device_mac.toUpperCase() === normalized)) {
        return 'claimed_by_self';
      }

      // Network path: check Supabase
      const { data: { user } } = await supabase.auth.getUser();
      const { data, error } = await supabase
        .from('registered_devices')
        .select('user_id')
        .ilike('device_mac', deviceMac)
        .maybeSingle();

      if (error) throw error;

      if (!data) {
        // Fingerprint fallback
        if (fingerprint?.firmwareVer && fingerprint?.productId) {
          const { data: fpData } = await supabase
            .from('registered_devices')
            .select('user_id')
            .eq('firmware_ver', fingerprint.firmwareVer)
            .eq('product_id', fingerprint.productId)
            .maybeSingle();
          if (fpData) {
            return fpData.user_id === user?.id ? 'claimed_by_self' : 'claimed_by_other';
          }
        }
        return 'unclaimed';
      }

      return data.user_id === user?.id ? 'claimed_by_self' : 'claimed_by_other';
    } catch (e) {
      AppLogger.warn('[DeviceRepository] Claim check failed (offline?):', e);
      return 'offline_unknown';
    }
  }

  /**
   * Check if user has ANY registered devices (cloud or local).
   */
  async hasRegistrations(): Promise<boolean> {
    if (this.devices.length > 0) return true;

    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return false;

      const { count } = await supabase
        .from('registered_devices')
        .select('id', { count: 'exact', head: true })
        .eq('user_id', user.id);

      return (count ?? 0) > 0;
    } catch {
      return false;
    }
  }

  // ── Swap Positions ──────────────────────────────────────────────────────────

  async swapPositions(mac1: string, mac2: string): Promise<void> {
    const d1 = this.findDevice(mac1);
    const d2 = this.findDevice(mac2);
    if (!d1 || !d2) return;

    const tmp = d1.position;
    d1.position = d2.position;
    d2.position = tmp;

    await this.saveDevice(d1);
    await this.saveDevice(d2);
  }

  // ── Subscribe / Notify (for useSyncExternalStore) ───────────────────────────

  subscribe = (listener: Listener): (() => void) => {
    this.listeners.add(listener);
    return () => { this.listeners.delete(listener); };
  };

  getVersion(): number { return this.version; }

  private _notifyListeners(): void {
    this.version++;
    this.listeners.forEach(l => l());
  }

  // ── Internal: Pending Sync Queue ────────────────────────────────────────────

  private async _queuePendingSync(device: Partial<RegisteredDevice> & { device_mac: string }): Promise<void> {
    try {
      const raw = await AsyncStorage.getItem(PENDING_KEY);
      const queue: RegisteredDevice[] = raw ? JSON.parse(raw) : [];
      const idx = queue.findIndex(d => d.device_mac.toUpperCase() === device.device_mac.toUpperCase());
      const marked: RegisteredDevice = {
        device_name: device.device_name || 'Unknown Device',
        product_type: device.product_type || LOCAL_PRODUCT_CATALOG[0].id,
        group_name: device.group_name || 'Default Fleet',
        position: device.position || null,
        group_id: device.group_id || 'default-fleet',
        ...device,
        is_pending_sync: true,
      };
      if (idx >= 0) queue[idx] = marked; else queue.push(marked);
      await AsyncStorage.setItem(PENDING_KEY, JSON.stringify(queue));
    } catch (e) {
      AppLogger.warn('[DeviceRepository] Queue failed:', e);
    }
  }

  private async _flushPendingSync(userId: string): Promise<void> {
    try {
      const raw = await AsyncStorage.getItem(PENDING_KEY);
      if (!raw) return;
      const queue: RegisteredDevice[] = JSON.parse(raw);
      if (queue.length === 0) return;

      for (const device of queue) {
        const groupId = device.group_id || device.group_name?.toLowerCase().replace(/\s+/g, '-') || 'default-fleet';
        try {
          await supabase.from('registered_groups').upsert({
            id: groupId,
            group_name: device.group_name || 'Default Fleet',
            type: 'device-fleet',
            user_id: userId
          } as any, { onConflict: 'id' } as any);
        } catch (_fk) {}

        const dbRow = {
          device_mac:      device.device_mac,
          device_name:     device.device_name,
          product_type:    device.product_type,
          position:        device.position,
          group_id:        groupId,
          custom_name:     device.device_name,
          points:          device.led_points || 0,
          led_points:      device.led_points,
          segments:        device.segments || 1,
          ic_type:         device.ic_type,
          strip_type:      device.ic_type || 'WS2812B',
          color_sorting:   device.color_sorting,
          sorting:         device.color_sorting || 'GRB',
          rssi_at_register: device.rssi_at_register,
          firmware_ver:    device.firmware_ver,
          led_version:     device.led_version,
          product_id:      device.product_id,
          rf_mode:         device.rf_mode,
          rf_paired_count: device.rf_paired_count,
          user_id:         userId,
          id:              device.id || `${device.device_mac.toUpperCase().replace(/:/g, '')}-${userId.slice(0, 8)}`,
          updated_at:      new Date().toISOString(),
          registered_at:   device.registered_at,
        };

        const { error } = await supabase
          .from('registered_devices')
          .upsert(dbRow as any, { onConflict: 'user_id,device_mac' } as any);
        if (error) AppLogger.warn('[DeviceRepository] Flush error for ' + device.device_mac, { error: error.message });
      }

      await AsyncStorage.removeItem(PENDING_KEY);
    } catch (e) {
      AppLogger.warn('[DeviceRepository] Flush failed:', e);
    }
  }
}

export default DeviceRepository;

// TODO: [R-23] File exceeds 30KB limit (39417 bytes) — mandatory component extraction required before architectural refactor
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
import { AppLogger } from './appLogger';
import { supabase } from './supabaseClient';
import { scrubPII } from '../utils/piiScrubber';
import { locationService } from './LocationService';

import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import type { RegisteredDevice } from '../hooks/useRegistration';
import type { CustomGroup, DeviceSettings } from '../types/dashboard.types';
import type { TablesInsert } from '../types/supabase';
import GroupRepository from './GroupRepository';

// ─── Local Supabase Insert Type Aliases ───────────────────────────────────────
// Derived directly from generated types — stay in sync with schema automatically.
type DeviceInsertRow = TablesInsert<'registered_devices'>;
type GroupInsert     = Omit<TablesInsert<'registered_groups'>, 'created_at'>;

import { STORAGE_DELETED_MACS, STORAGE_PENDING_SYNC, STORAGE_REGISTERED_DEVICES, CONFIGS_KEY as GLOBAL_CONFIGS_KEY } from '../constants/storageKeys';

// ─── Storage Keys ─────────────────────────────────────────────────────────────
const DEVICES_KEY     = STORAGE_REGISTERED_DEVICES;
const CONFIGS_KEY     = GLOBAL_CONFIGS_KEY;
const TOMBSTONE_KEY   = STORAGE_DELETED_MACS;
const PENDING_KEY     = STORAGE_PENDING_SYNC;
// NOTE: '@Sk8lytz_last_group_patterns' is intentionally managed by useDashboardGroups
// as a UI-local concern (last pattern picked per group). It is NOT part of this repo's SSOT.

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
  private tombstones: string[] = [];
  private isInitialized = false;
  private initPromise: Promise<void> | null = null;

  // ── Subscriber pattern for useSyncExternalStore ─────────────────────────────
  private listeners: Set<Listener> = new Set();
  private version = 0;

  private constructor() {
    GroupRepository.getInstance().setDeviceDelegate({
      getCurrentDevices: () => this.devices,
      updateDevicesInBulk: async (updated) => {
        this.devices = updated;
        await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'DEVICES_KEY', error: e instanceof Error ? e.message : String(e)  }));
      },
      notifySubscribers: () => this._notifyListeners(),
      getCurrentConfigs: () => this.configs,
      updateConfigsInBulk: async (configs) => {
        this.configs = configs;
        await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'CONFIGS_KEY', error: e instanceof Error ? e.message : String(e)  }));
      },
    });
  }

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

    this.initPromise = (async () => {
      await Promise.all([
        this._loadFromStorage(),
        GroupRepository.getInstance().initialize(),
      ]);
    })();
    await this.initPromise;
    this.isInitialized = true;
  }

  private async _loadFromStorage(): Promise<void> {
    try {
      const [devicesRaw, configsRaw, tombRaw] = await Promise.all([
        AsyncStorage.getItem(DEVICES_KEY),
        AsyncStorage.getItem(CONFIGS_KEY),
        AsyncStorage.getItem(TOMBSTONE_KEY),
      ]);

      if (devicesRaw) {
        try { this.devices = JSON.parse(devicesRaw); } catch { this.devices = []; }
      }
      if (configsRaw) {
        try { this.configs = JSON.parse(configsRaw); } catch { this.configs = {}; }
      }
      if (tombRaw) {
        try { this.tombstones = JSON.parse(tombRaw); } catch { this.tombstones = []; }
      }

      AppLogger.warn('[DeviceRepository] initialized', {
        event: 'initialized',
        deviceCount: this.devices.length,
        tombstoneCount: this.tombstones.length,
      });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Storage load failed:', { error: msg });
    }
  }

  // ── Read Accessors ──────────────────────────────────────────────────────────

  // CRITICAL: Return shallow copies so React detects state changes via reference identity.
  // Returning the raw `this.devices` reference caused React to skip re-renders after mutations
  // (push/index-set don't change Object.is identity), breaking group derivation and AccountModal.
  getDevices(): RegisteredDevice[] { return [...this.devices]; }
  getConfigs(): Record<string, DeviceSettings> { return { ...this.configs }; }
  getGroups(): CustomGroup[] { return GroupRepository.getInstance().getGroups(); }
  getTombstones(): string[] { return this.tombstones; }

  getSnapshot(): DeviceRepositorySnapshot {
    return {
      devices: this.devices,
      configs: this.configs,
      groups: GroupRepository.getInstance().getGroups(),
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
  async saveDevice(device: Partial<RegisteredDevice> & { device_mac: string }, userId?: string): Promise<boolean> {
    try {
      const user = userId ? { id: userId } : null;
      const now = new Date().toISOString();
      const normalizedMac = device.device_mac.toUpperCase();
      const deviceId = device.id || `${normalizedMac.replace(/:/g, '')}-${user?.id?.slice(0, 8) || 'offline'}`;
      const groupIds = device.group_ids || (device.group_names ? device.group_names.map(n => n.toLowerCase().replace(/\s+/g, '-')) : ['default-fleet']);

      let lat = device.last_lat;
      let lng = device.last_lng;
      if (lat === undefined || lng === undefined) {
        const loc = await locationService.getSilentLocation();
        if (loc) {
          lat = loc.lat;
          lng = loc.lng;
        }
      }

      const fullDevice: RegisteredDevice = {
        device_name: device.device_name || 'Unknown Device',
        product_type: device.product_type || LOCAL_PRODUCT_CATALOG[0].id,
        group_names: device.group_names || ['Default Fleet'],
        position: device.position || null,
        ...device,
        device_mac: normalizedMac,
        id: deviceId,
        group_ids: groupIds,
        user_id: user?.id,
        last_lat: lat,
        last_lng: lng,
        updated_at: now,
        registered_at: device.registered_at || now,
        is_pending_sync: false,
      };

      // 1. Update in-memory state (IMMUTABLE — new array reference for React change detection)
      const idx = this.devices.findIndex(d => d.device_mac.toUpperCase() === normalizedMac);
      if (idx >= 0) {
        this.devices = this.devices.map((d, i) => i === idx ? fullDevice : d);
      } else {
        this.devices = [...this.devices, fullDevice];
      }

      // 1.5. Purge from tombstone on re-add (Fix: BUG-14 — permanent tombstone lock)
      if (this.tombstones.includes(normalizedMac)) {
        this.tombstones = this.tombstones.filter(t => t !== normalizedMac);
        await AsyncStorage.setItem(TOMBSTONE_KEY, JSON.stringify(this.tombstones)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'TOMBSTONE_KEY', error: e instanceof Error ? e.message : String(e)  }));
        AppLogger.warn('[DeviceRepository] Tombstone cleared on re-add', { deviceId: scrubPII(normalizedMac) });
      }

      // 2. Persist to AsyncStorage
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'DEVICES_KEY/saveDevice', error: e instanceof Error ? e.message : String(e)  }));

      // 3. Notify subscribers
      this._notifyListeners();

      // 4. Sync to Supabase
      if (user) {
        // Phase 5: Atomic group upsert via server-side RPC (single transaction)
        try {
          await supabase.rpc('upsert_group_with_devices', {
            p_group_id:   fullDevice.group_ids && fullDevice.group_ids[0] ? fullDevice.group_ids[0] : 'default-fleet',
            p_group_name: fullDevice.group_names && fullDevice.group_names[0] ? fullDevice.group_names[0] : 'Default Fleet',
            p_type:       'device-fleet',
            p_device_ids: [deviceId],
          });
        } catch (_rpc: unknown) {
          const msg = _rpc instanceof Error ? _rpc.message : String(_rpc);
          AppLogger.warn('[DeviceRepository] upsert_group_with_devices RPC failed, falling back:', { error: msg });
          try {
            await supabase.from('registered_groups').upsert({
              id: fullDevice.group_ids && fullDevice.group_ids[0] ? fullDevice.group_ids[0] : 'default-fleet',
              group_name: fullDevice.group_names && fullDevice.group_names[0] ? fullDevice.group_names[0] : 'Default Fleet',
              type: 'device-fleet',
              user_id: user.id
            } satisfies GroupInsert, { onConflict: 'id' });
          } catch (_fk: unknown) {
            const msg = _fk instanceof Error ? _fk.message : String(_fk);
            AppLogger.warn('[DeviceRepository] Group FK fallback also failed:', { error: msg });
          }
        }

        // Spread-if-valid: only include hardware fields with real data
        const validPoints   = fullDevice.led_points && fullDevice.led_points > 0   ? fullDevice.led_points   : undefined;
        const validSegments = fullDevice.segments   && fullDevice.segments   > 0   ? fullDevice.segments     : undefined;
        const validIcType   = fullDevice.ic_type    && fullDevice.ic_type !== 'UNKNOWN' ? fullDevice.ic_type : undefined;
        const validSorting  = fullDevice.color_sorting && fullDevice.color_sorting !== 'UNKNOWN' ? fullDevice.color_sorting : undefined;

        const dbRow: DeviceInsertRow = {
          device_mac:      fullDevice.device_mac,
          device_name:     fullDevice.device_name,
          product_type:    fullDevice.product_type || null,
          position:        fullDevice.position || null,
          group_name:      fullDevice.group_names ? fullDevice.group_names[0] : null,
          group_id:        fullDevice.group_ids ? fullDevice.group_ids[0] : 'default-fleet',
          custom_name:     fullDevice.device_name || '',
          user_id:         user.id,
          id:              fullDevice.id || deviceId,
          updated_at:      now,
          registered_at:   fullDevice.registered_at || null,
          last_lat:        fullDevice.last_lat ?? null,
          last_lng:        fullDevice.last_lng ?? null,
          rssi_at_register: fullDevice.rssi_at_register ?? null,
          firmware_ver:    fullDevice.firmware_ver ?? null,
          led_version:     fullDevice.led_version ?? null,
          product_id:      fullDevice.product_id ?? null,
          rf_mode:         fullDevice.rf_mode ?? null,
          rf_paired_count: fullDevice.rf_paired_count ?? null,
          // Required non-null fields — use validated values or safe defaults
          points:          validPoints   ?? 0,
          led_points:      validPoints   ?? null,
          segments:        validSegments ?? 1,
          sorting:         validSorting  ?? 'GRB',
          strip_type:      validIcType   ?? 'WS2812B',
          ic_type:         validIcType   ?? null,
          color_sorting:   validSorting  ?? null,
          // Unused hardware metadata fields — not tracked at registration time
          ble_version:     fullDevice.ble_version ?? null,
          factory_name:    fullDevice.factory_name ?? null,
          manufacturer_data: fullDevice.manufacturer_data ?? null,
          is_pending_sync: false,
          product_id_confirmed_at: null,
        };

        const { error } = await supabase
          .from('registered_devices')
          .upsert(dbRow, { onConflict: 'user_id,device_mac' });

        if (error) throw error;
      } else {
        await this._queuePendingSync(fullDevice);
      }

      return true;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Save failed, queuing offline:', { error: msg });
      await this._queuePendingSync(device);
      return false;
    }
  }

  /**
   * Save multiple devices atomically (used by wizard completion).
   */
  async saveAllDevices(devices: RegisteredDevice[], userId?: string): Promise<boolean> {
    let allOk = true;
    for (const d of devices) {
      const ok = await this.saveDevice(d, userId);
      if (!ok) allOk = false;
    }
    return allOk;
  }

  /**
   * Delete a device by MAC address. Writes tombstone, removes locally, then syncs to cloud.
   */
  async deleteDevice(deviceMac: string, userId?: string): Promise<void> {
    const normalizedMac = deviceMac.toUpperCase();

    try {
      const user = userId ? { id: userId } : null;

      // Step 1: Write tombstone (prevents cloud resurrection)
      if (!this.tombstones.includes(normalizedMac)) {
        this.tombstones.push(normalizedMac);
        await AsyncStorage.setItem(TOMBSTONE_KEY, JSON.stringify(this.tombstones)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'TOMBSTONE_KEY', error: e instanceof Error ? e.message : String(e)  }));
      }

      // Step 2: Remove from in-memory devices
      this.devices = this.devices.filter(d => d.device_mac.toUpperCase() !== normalizedMac);
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'DEVICES_KEY/deleteDevice', error: e instanceof Error ? e.message : String(e)  }));

      // Step 3: Scrub device config
      if (this.configs[normalizedMac]) {
        delete this.configs[normalizedMac];
        await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'CONFIGS_KEY', error: e instanceof Error ? e.message : String(e)  }));
      }

      // Step 4: Notify subscribers
      this._notifyListeners();

      // Step 5: Remove from Supabase
      if (user) {
        AppLogger.warn('[DeviceRepository] DEREGISTER_ATTEMPT', { deviceId: scrubPII(normalizedMac) });
        const { error, count } = await supabase
          .from('registered_devices')
          .delete({ count: 'exact' })
          .eq('user_id', user.id)
          .ilike('device_mac', normalizedMac);

        if (error) {
          AppLogger.warn('[DeviceRepository] DB delete error (RLS?)', { deviceId: scrubPII(normalizedMac), error: error instanceof Error ? error.message : String(error)  });
          throw error;
        }

        AppLogger.warn('[DeviceRepository] DEREGISTER_RESULT', { deviceId: scrubPII(normalizedMac), rowsDeleted: count });
      }
    } catch (e: unknown) {
      const errorMsg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Deregister failed:', errorMsg);
      // Tombstone still prevents resurrection even if Supabase delete fails
    }
  }

  // ── Config Mutations ────────────────────────────────────────────────────────

  /**
   * Update device config by MAC. Merges with existing config.
   */
  async updateConfig(mac: string, patch: Partial<DeviceSettings>): Promise<void> {
    const key = mac.toUpperCase();
    
    // BUG-05 Fix: Automatically stamp userConfiguredAt when hardware topology (points) 
    // is successfully probed/written. This prevents the initial Setup Wizard results 
    // from being immediately overwritten by stale cloud state (which defaults to 0).
    const incomingPoints = patch.points ?? (patch as Record<string, unknown>).ledPoints as number | undefined;
    if (incomingPoints !== undefined && incomingPoints > 0 && !patch.userConfiguredAt) {
      patch.userConfiguredAt = new Date().toISOString();
    }

    this.configs[key] = { ...(this.configs[key] || {} as DeviceSettings), ...patch };
    await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'CONFIGS_KEY/updateConfig', error: e instanceof Error ? e.message : String(e)  }));
    this._notifyListeners();
  }

  /**
   * Replace entire configs map (used by hardware notification handlers).
   */
  async setConfigs(configs: Record<string, DeviceSettings>): Promise<void> {
    this.configs = configs;
    await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'CONFIGS_KEY/setConfigs', error: e instanceof Error ? e.message : String(e)  }));;
    this._notifyListeners();
  }

  /**
   * Delete a config entry by MAC.
   */
  async deleteConfig(mac: string): Promise<void> {
    const key = mac.toUpperCase();
    if (this.configs[key]) {
      delete this.configs[key];
      await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(this.configs)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'CONFIGS_KEY/deleteConfig', error: e instanceof Error ? e.message : String(e)  }));
      this._notifyListeners();
    }
  }

  // ── Group Mutations ─────────────────────────────────────────────────────────

  /**
   * Set groups directly (used by group derivation logic in useDashboardGroups).
   */
  async setGroups(groups: CustomGroup[]): Promise<void> {
    await GroupRepository.getInstance().setGroups(groups);
  }

  /**
   * Remove a group by ID from local state and cloud.
   */
  async deleteGroup(groupId: string, userId?: string): Promise<void> {
    await GroupRepository.getInstance().deleteGroup(groupId, userId);
  }

  /**
   * Transactional group save: atomically upserts the group and bulk-assigns
   * device membership.
   */
  async saveGroupTransactional(
    groupId: string,
    groupName: string,
    deviceMacs: string[],  // Array of MAC addresses (frontend convention)
    type = 'device-fleet',
    userId?: string
  ): Promise<boolean> {
    return GroupRepository.getInstance().saveGroupTransactional(groupId, groupName, deviceMacs, type, userId);
  }

  // ── Cloud Sync ──────────────────────────────────────────────────────────────

  /**
   * Sync registered devices from Supabase, applying tombstone filter
   * and local-first smart merge.
   */
  async syncFromCloud(userId?: string): Promise<RegisteredDevice[]> {
    try {
      if (!userId) return this.devices;
      const user = { id: userId };

      const { data, error } = await supabase
        .from('registered_devices')
        .select('*')
        .eq('user_id', user.id)
        .order('registered_at', { ascending: true });

      if (error) throw error;
      if (!data) return this.devices;

      // Tombstone filter
      const cloudRows = data.filter((row: Record<string, unknown>) =>
        !this.tombstones.includes((row.device_mac as string)?.toUpperCase?.())
      );

      if (cloudRows.length < data.length) {
        AppLogger.warn('[DeviceRepository] syncFromCloud tombstone filtered', { skipped: data.length - cloudRows.length,
        });
      }

      // Local-first smart merge
      const merged: RegisteredDevice[] = cloudRows.map((row: Record<string, unknown>) => {
        const cloud = { ...row, is_pending_sync: false } as RegisteredDevice;
        const local = this.devices.find(
          l => l.device_mac.toUpperCase() === cloud.device_mac.toUpperCase()
        );

        if (!local) {
          // BUG FIX: Reconstruct array group fields from scalar DB columns.
          // Supabase registered_devices has group_id (scalar) but NOT group_ids (array).
          // Without this, fresh installs lose group membership and cards vanish.
          const scalarGroupId   = (row as Record<string, unknown>).group_id;
          const scalarGroupName = (row as Record<string, unknown>).group_name;
          return {
            ...cloud,
            group_ids:   scalarGroupId   ? [String(scalarGroupId)]   : [],
            group_names: scalarGroupName ? [String(scalarGroupName)]  : [],
          };
        }

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
          // MIGRATION-SHIM: cloud row type predates group_ids — access safely. Remove at v3.9.0.
          group_ids:     cloud.group_ids ?? local.group_ids ?? [],
          group_names:   cloud.group_names ?? local.group_names ?? [],
          device_name:   cloud.device_name || local.device_name,
          is_pending_sync: localHasPendingChanges,
        };
      });

      // Pure-local preservation (Fix: BUG-13 — offline device wiping)
      // Retain any locally-registered (pending_sync) devices not yet in cloud
      const offlineLocalOnly = this.devices.filter(
        localD =>
          !!localD.is_pending_sync &&
          !cloudRows.some((cloudD: Record<string, unknown>) => (cloudD.device_mac as string)?.toUpperCase() === localD.device_mac.toUpperCase())
      );

      const finalDevices = [...merged, ...offlineLocalOnly];

      // Update in-memory state
      this.devices = finalDevices;
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(finalDevices)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'DEVICES_KEY/syncFromCloud', error: e instanceof Error ? e.message : String(e)  }));
      this._notifyListeners();

      // Flush pending offline device registrations, group sync, and tombstone deletions
      await this._flushPendingSync(user.id);
      await GroupRepository.getInstance().flushPendingGroups(user.id, this.devices);
      await this._flushPendingTombstones(user.id);

      return finalDevices;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Cloud sync failed (offline?):', { error: msg });
      return this.devices;
    }
  }

  // ── Product ID Confirmation ───────────────────────────────────────────────────

  /**
   * Called when a 0x63 EEPROM query successfully identifies the product ID.
   * Persists the confirmation timestamp locally and syncs to Supabase.
   */
  async confirmProductId(deviceMac: string): Promise<void> {
    const normalizedMac = deviceMac.toUpperCase();
    const device = this.devices.find(d => d.device_mac.toUpperCase() === normalizedMac);
    
    // Only confirm if registered and not already confirmed
    if (!device || device.product_id_confirmed_at) return;

    const now = new Date().toISOString();
    device.product_id_confirmed_at = now;
    
    // Update local SSOT and notify listeners
    await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(this.devices)).catch(e => 
      AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'DEVICES_KEY/confirmProductId', error: e instanceof Error ? e.message : String(e) })
    );
    this._notifyListeners();

    try {
      if (!device.user_id) return;
      const { error } = await supabase
        .from('registered_devices')
        .update({ product_id_confirmed_at: now })
        .eq('user_id', device.user_id)
        .ilike('device_mac', normalizedMac);
        
      if (error) throw error;
      AppLogger.log('PRODUCT_CONFIRMED', { deviceId: scrubPII(normalizedMac) });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Failed to confirm product ID', { error: msg });
    }
  }

  // ── Claim Check ─────────────────────────────────────────────────────────────

  /**
   * Check whether a device MAC is claimed by the current user, another user, or unclaimed.
   */
  async checkDeviceClaimed(
    deviceMac: string,
    fingerprint?: { firmwareVer?: number; ledVersion?: number; productId?: number },
    userId?: string
  ): Promise<'unclaimed' | 'claimed_by_self' | 'claimed_by_other' | 'offline_unknown'> {
    try {
      // Fast path: check local
      const normalized = deviceMac.toUpperCase();
      if (this.devices.some(d => d.device_mac.toUpperCase() === normalized)) {
        return 'claimed_by_self';
      }

      // Network path: check Supabase
      const user = userId ? { id: userId } : null;
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
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Claim check failed (offline?):', { error: msg });
      return 'offline_unknown';
    }
  }

  /**
   * Check if user has ANY registered devices (cloud or local).
   */
  async hasRegistrations(userId?: string): Promise<boolean> {
    if (this.devices.length > 0) return true;

    try {
      if (!userId) return false;
      const user = { id: userId };

      const { count } = await supabase
        .from('registered_devices')
        .select('id', { count: 'exact', head: true })
        .eq('user_id', user.id);

      return (count ?? 0) > 0;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] hasRegistrations failed', { error: msg });
      return false;
    }
  }

  // ── Swap Positions ──────────────────────────────────────────────────────────

  async swapPositions(mac1: string, mac2: string): Promise<void> {
    try {
      const d1 = this.findDevice(mac1);
      const d2 = this.findDevice(mac2);
      if (!d1 || !d2) return;

      const tmp = d1.position;
      d1.position = d2.position;
      d2.position = tmp;

      await this.saveDevice(d1);
      await this.saveDevice(d2);
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[DeviceRepository] swapPositions failed', { error: msg , payload_size: 0, ssi: 0 });
    }
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
        group_names: device.group_names || ['Default Fleet'],
        position: device.position || null,
        group_ids: device.group_ids || ['default-fleet'],
        ...device,
        is_pending_sync: true,
      };
      if (idx >= 0) queue[idx] = marked; else queue.push(marked);
      await AsyncStorage.setItem(PENDING_KEY, JSON.stringify(queue)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'PENDING_KEY', error: e instanceof Error ? e.message : String(e)  }));
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Queue failed:', { error: msg });
    }
  }

  private async _flushPendingSync(userId: string): Promise<void> {
    try {
      const raw = await AsyncStorage.getItem(PENDING_KEY);
      if (!raw) return;
      const queue: RegisteredDevice[] = JSON.parse(raw);
      if (queue.length === 0) return;

      for (const device of queue) {
        const groupId = device.group_ids && device.group_ids[0] ? device.group_ids[0] : (device.group_names && device.group_names[0]?.toLowerCase().replace(/\s+/g, '-') || 'default-fleet');
        try {
          await supabase.from('registered_groups').upsert({
            id: groupId,
            group_name: device.group_names && device.group_names[0] ? device.group_names[0] : 'Default Fleet',
            type: 'device-fleet',
            user_id: userId
          } satisfies GroupInsert, { onConflict: 'id' });
        } catch (_fk: unknown) {
          const msg = _fk instanceof Error ? _fk.message : String(_fk);
          AppLogger.error('[DeviceRepository] FK fallback failed', { error: msg , payload_size: 0, ssi: 0 });
        }

        const dbRow: DeviceInsertRow = {
          device_mac:      device.device_mac,
          device_name:     device.device_name || null,
          product_type:    device.product_type || null,
          position:        device.position || null,
          group_id:        groupId,
          group_name:      device.group_names ? device.group_names[0] : null,
          custom_name:     device.device_name || '',
          points:          device.led_points || 0,
          led_points:      device.led_points ?? null,
          segments:        device.segments || 1,
          ic_type:         device.ic_type ?? null,
          strip_type:      device.ic_type || 'WS2812B',
          color_sorting:   device.color_sorting ?? null,
          sorting:         device.color_sorting || 'GRB',
          rssi_at_register: device.rssi_at_register ?? null,
          firmware_ver:    device.firmware_ver ?? null,
          led_version:     device.led_version ?? null,
          product_id:      device.product_id ?? null,
          rf_mode:         device.rf_mode ?? null,
          rf_paired_count: device.rf_paired_count ?? null,
          user_id:         userId,
          id:              device.id || `${device.device_mac.toUpperCase().replace(/:/g, '')}-${userId.slice(0, 8)}`,
          updated_at:      new Date().toISOString(),
          registered_at:   device.registered_at ?? null,
          last_lat:        device.last_lat ?? null,
          last_lng:        device.last_lng ?? null,
          // Unused hardware metadata fields — not tracked at registration time
          ble_version:     device.ble_version ?? null,
          factory_name:    device.factory_name ?? null,
          manufacturer_data: device.manufacturer_data ?? null,
          is_pending_sync: true,
          product_id_confirmed_at: null,
        };

        const { error } = await supabase
          .from('registered_devices')
          .upsert(dbRow, { onConflict: 'user_id,device_mac' });
        if (error) AppLogger.warn('[DeviceRepository] Flush error', { deviceId: scrubPII(device.device_mac), error: error.message });
      }

      await AsyncStorage.removeItem(PENDING_KEY).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage remove failed', { key: 'PENDING_KEY', error: e instanceof Error ? e.message : String(e)  }));
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Flush failed:', { error: msg });
    }
  }



  /**
   * Push any locally-tombstoned MACs to Supabase on login.
   * Clears synced tombstones to prevent permanent accumulation.
   * Fix: BUG — offline device deletion leaves orphaned cloud rows.
   */
  private async _flushPendingTombstones(userId: string): Promise<void> {
    if (this.tombstones.length === 0) return;
    const synced: string[] = [];

    for (const mac of this.tombstones) {
      try {
        const { error } = await supabase
          .from('registered_devices')
          .delete()
          .eq('user_id', userId)
          .ilike('device_mac', mac);

        if (!error) {
          synced.push(mac);
          AppLogger.warn('[DeviceRepository] Tombstone synced to cloud', { deviceId: scrubPII(mac) });
        } else {
          AppLogger.warn('[DeviceRepository] Tombstone cloud delete failed', { deviceId: scrubPII(mac), error: error instanceof Error ? error.message : String(error)  });
        }
      } catch (e: unknown) {
        AppLogger.warn('[DeviceRepository] Tombstone flush error', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e)  });
      }
    }

    if (synced.length > 0) {
      // Tombstones that were successfully deleted from cloud can be cleared locally.
      // We KEEP any we failed to sync so the next boot retries them.
      this.tombstones = this.tombstones.filter(t => !synced.includes(t));
      await AsyncStorage.setItem(TOMBSTONE_KEY, JSON.stringify(this.tombstones)).catch((e: unknown) => AppLogger.warn('[DeviceRepository] AsyncStorage write failed', { key: 'TOMBSTONE_KEY/flushPendingTombstones', error: e instanceof Error ? e.message : String(e)  }));
    }
  }
}


export default DeviceRepository;

import { AppLogger } from '../appLogger';
import { supabase } from '../supabaseClient';
import { scrubPII } from '../../utils/piiScrubber';
import { locationService } from '../LocationService';
import { LOCAL_PRODUCT_CATALOG } from '../../constants/ProductCatalog';
import type { RegisteredDevice } from './types';
import type { CustomGroup, DeviceSettings } from '../../types/dashboard.types';
import GroupRepository from '../GroupRepository';
import { DeviceStorage } from './DeviceStorage';
import { DeviceStateManagement } from './DeviceStateManagement';
import { DeviceCloudSync } from './DeviceCloudSync';
import type { DeviceRepositorySnapshot, DeviceInsertRow, GroupInsert } from './types';

/**
 * Singleton service that owns all device fleet persistence.
 */
export default class DeviceRepositoryService {
  private static instance: DeviceRepositoryService;

  private state = new DeviceStateManagement();
  private isInitialized = false;
  private initPromise: Promise<void> | null = null;

  private constructor() {
    GroupRepository.getInstance().setDeviceDelegate({
      getCurrentDevices: () => this.state.devices,
      updateDevicesInBulk: async (updated) => {
        this.state.devices = updated;
        await DeviceStorage.saveDevices(this.state.devices, 'updateDevicesInBulk');
      },
      notifySubscribers: () => this.state.notifyListeners(),
      getCurrentConfigs: () => this.state.configs,
      updateConfigsInBulk: async (configs) => {
        this.state.configs = configs;
        await DeviceStorage.saveConfigs(this.state.configs, 'updateConfigsInBulk');
      },
    });
  }

  static getInstance(): DeviceRepositoryService {
    if (!DeviceRepositoryService.instance) {
      DeviceRepositoryService.instance = new DeviceRepositoryService();
    }
    return DeviceRepositoryService.instance;
  }

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
        DeviceStorage.loadDevices(),
        DeviceStorage.loadConfigs(),
        DeviceStorage.loadTombstones(),
      ]);

      if (devicesRaw) this.state.devices = devicesRaw;
      if (configsRaw) this.state.configs = configsRaw;
      if (tombRaw) this.state.tombstones = tombRaw;

      AppLogger.warn('[DeviceRepository] initialized', {
        event: 'initialized',
        deviceCount: this.state.devices.length,
        tombstoneCount: this.state.tombstones.length,
      });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Storage load failed:', { error: msg });
    }
  }

  getDevices(): RegisteredDevice[] { return [...this.state.devices]; }
  getConfigs(): Record<string, DeviceSettings> { return { ...this.state.configs }; }
  getGroups(): CustomGroup[] { return GroupRepository.getInstance().getGroups(); }
  getTombstones(): string[] { return this.state.tombstones; }

  getSnapshot(): DeviceRepositorySnapshot {
    return {
      devices: this.state.devices,
      configs: this.state.configs,
      groups: GroupRepository.getInstance().getGroups(),
      tombstones: this.state.tombstones,
    };
  }

  findDevice(mac: string): RegisteredDevice | undefined {
    const normalized = mac.toUpperCase();
    return this.state.devices.find(d => d.device_mac.toUpperCase() === normalized);
  }

  getConfig(mac: string): DeviceSettings | undefined {
    return this.state.configs[mac.toUpperCase()];
  }

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

      const idx = this.state.devices.findIndex(d => d.device_mac.toUpperCase() === normalizedMac);
      if (idx >= 0) {
        this.state.devices = this.state.devices.map((d, i) => i === idx ? fullDevice : d);
      } else {
        this.state.devices = [...this.state.devices, fullDevice];
      }

      if (this.state.tombstones.includes(normalizedMac)) {
        this.state.tombstones = this.state.tombstones.filter(t => t !== normalizedMac);
        await DeviceStorage.saveTombstones(this.state.tombstones, 'saveDevice');
        AppLogger.warn('[DeviceRepository] Tombstone cleared on re-add', { deviceId: scrubPII(normalizedMac) });
      }

      await DeviceStorage.saveDevices(this.state.devices, 'saveDevice');
      this.state.notifyListeners();

      if (user) {
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
          points:          validPoints   ?? 0,
          led_points:      validPoints   ?? null,
          segments:        validSegments ?? 1,
          sorting:         validSorting  ?? 'GRB',
          strip_type:      validIcType   ?? 'WS2812B',
          ic_type:         validIcType   ?? null,
          color_sorting:   validSorting  ?? null,
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

  async saveAllDevices(devices: RegisteredDevice[], userId?: string): Promise<boolean> {
    let allOk = true;
    for (const d of devices) {
      const ok = await this.saveDevice(d, userId);
      if (!ok) allOk = false;
    }
    return allOk;
  }

  async deleteDevice(deviceMac: string, userId?: string): Promise<void> {
    const normalizedMac = deviceMac.toUpperCase();

    try {
      const user = userId ? { id: userId } : null;

      if (!this.state.tombstones.includes(normalizedMac)) {
        this.state.tombstones.push(normalizedMac);
        await DeviceStorage.saveTombstones(this.state.tombstones, 'deleteDevice');
      }

      this.state.devices = this.state.devices.filter(d => d.device_mac.toUpperCase() !== normalizedMac);
      await DeviceStorage.saveDevices(this.state.devices, 'deleteDevice');

      if (this.state.configs[normalizedMac]) {
        delete this.state.configs[normalizedMac];
        await DeviceStorage.saveConfigs(this.state.configs, 'deleteDevice');
      }

      this.state.notifyListeners();

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
    }
  }

  async updateConfig(mac: string, patch: Partial<DeviceSettings>): Promise<void> {
    const key = mac.toUpperCase();
    
    const incomingPoints = patch.points ?? (patch as Record<string, unknown>).ledPoints as number | undefined;
    if (incomingPoints !== undefined && incomingPoints > 0 && !patch.userConfiguredAt) {
      patch.userConfiguredAt = new Date().toISOString();
    }

    this.state.configs[key] = { ...(this.state.configs[key] || {} as DeviceSettings), ...patch };
    await DeviceStorage.saveConfigs(this.state.configs, 'updateConfig');
    this.state.notifyListeners();
  }

  async setConfigs(configs: Record<string, DeviceSettings>): Promise<void> {
    this.state.configs = configs;
    await DeviceStorage.saveConfigs(this.state.configs, 'setConfigs');
    this.state.notifyListeners();
  }

  async deleteConfig(mac: string): Promise<void> {
    const key = mac.toUpperCase();
    if (this.state.configs[key]) {
      delete this.state.configs[key];
      await DeviceStorage.saveConfigs(this.state.configs, 'deleteConfig');
      this.state.notifyListeners();
    }
  }

  async setGroups(groups: CustomGroup[]): Promise<void> {
    await GroupRepository.getInstance().setGroups(groups);
  }

  async deleteGroup(groupId: string, userId?: string): Promise<void> {
    await GroupRepository.getInstance().deleteGroup(groupId, userId);
  }

  async saveGroupTransactional(
    groupId: string,
    groupName: string,
    deviceMacs: string[],
    type = 'device-fleet',
    userId?: string
  ): Promise<boolean> {
    return GroupRepository.getInstance().saveGroupTransactional(groupId, groupName, deviceMacs, type, userId);
  }

  async syncFromCloud(userId?: string): Promise<RegisteredDevice[]> {
    try {
      if (!userId) return this.state.devices;
      const user = { id: userId };

      const { data, error } = await supabase
        .from('registered_devices')
        .select('*')
        .eq('user_id', user.id)
        .order('registered_at', { ascending: true });

      if (error) throw error;
      if (!data) return this.state.devices;

      const finalDevices = DeviceCloudSync.mergeCloudAndLocal(data, this.state.devices, this.state.tombstones);

      this.state.devices = finalDevices;
      await DeviceStorage.saveDevices(this.state.devices, 'syncFromCloud');
      this.state.notifyListeners();

      await this._flushPendingSync(user.id);
      await GroupRepository.getInstance().flushPendingGroups(user.id, this.state.devices);
      await this._flushPendingTombstones(user.id);

      return finalDevices;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Cloud sync failed (offline?):', { error: msg });
      return this.state.devices;
    }
  }

  async confirmProductId(deviceMac: string): Promise<void> {
    const normalizedMac = deviceMac.toUpperCase();
    const device = this.state.devices.find(d => d.device_mac.toUpperCase() === normalizedMac);
    
    if (!device || device.product_id_confirmed_at) return;

    const now = new Date().toISOString();
    device.product_id_confirmed_at = now;
    
    await DeviceStorage.saveDevices(this.state.devices, 'confirmProductId');
    this.state.notifyListeners();

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

  async checkDeviceClaimed(
    deviceMac: string,
    fingerprint?: { firmwareVer?: number; ledVersion?: number; productId?: number },
    userId?: string
  ): Promise<'unclaimed' | 'claimed_by_self' | 'claimed_by_other' | 'offline_unknown'> {
    return DeviceCloudSync.checkDeviceClaimed(deviceMac, this.state.devices, fingerprint, userId);
  }

  async hasRegistrations(userId?: string): Promise<boolean> {
    return DeviceCloudSync.hasRegistrations(this.state.devices, userId);
  }

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

  subscribe = (listener: () => void): (() => void) => {
    return this.state.subscribe(listener);
  };

  getVersion(): number {
    return this.state.getVersion();
  }

  private async _queuePendingSync(device: Partial<RegisteredDevice> & { device_mac: string }): Promise<void> {
    try {
      const queue = await DeviceStorage.loadPendingQueue();
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
      await DeviceStorage.savePendingQueue(queue);
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Queue failed:', { error: msg });
    }
  }

  private async _flushPendingSync(userId: string): Promise<void> {
    try {
      const queue = await DeviceStorage.loadPendingQueue();
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

      await DeviceStorage.clearPendingQueue();
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceRepository] Flush failed:', { error: msg });
    }
  }

  private async _flushPendingTombstones(userId: string): Promise<void> {
    if (this.state.tombstones.length === 0) return;
    const synced: string[] = [];

    for (const mac of this.state.tombstones) {
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
      this.state.tombstones = this.state.tombstones.filter(t => !synced.includes(t));
      await DeviceStorage.saveTombstones(this.state.tombstones, 'flushPendingTombstones');
    }
  }
}

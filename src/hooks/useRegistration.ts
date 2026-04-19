/**
 * useRegistration.ts — SK8Lytz Device Ownership Registry
 *
 * Local-first architecture:
 *   1. On mount → load from DeviceRepository (instant, no network needed)
 *   2. If online → DeviceRepository syncs from Supabase
 *   3. All writes → DeviceRepository (AsyncStorage first, then Supabase)
 *   4. On reconnect → DeviceRepository flushes pending offline registrations
 *
 * Phase 3 refactor: This hook is now a thin React facade over DeviceRepository.
 * It maintains its own React state for rendering, but delegates all persistence
 * to the singleton DeviceRepository service.
 *
 * A "registered device" means a specific BLE controller MAC address is
 * associated with a Supabase user account. This is soft-ownership (no
 * hardware binding is possible with Zengge controllers).
 */

import { useCallback, useEffect, useState } from 'react';
import { Alert } from 'react-native';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { AppLogger } from '../services/AppLogger';
import DeviceRepository from '../services/DeviceRepository';
import { getDefaultDeviceName } from '../utils/NamingUtils';

// ─── Types ────────────────────────────────────────────────────────────────────

export interface RegisteredDevice {
  id?: string;
  user_id?: string;
  device_mac: string;
  device_name: string;
  custom_name?: string;
  /** Product type — must match a `ProductProfile.id` in LOCAL_PRODUCT_CATALOG. */
  product_type: 'HALOZ' | 'SOULZ' | 'RAILZ' | string;
  position: 'Left' | 'Right' | null;
  group_name: string;
  group_id: string;
  led_points?: number;
  segments?: number;
  ic_type?: string;
  color_sorting?: string;
  rssi_at_register?: number;
  // Secondary fingerprint for MAC-rotation fallback
  firmware_ver?: number;
  led_version?: number;
  product_id?: number;
  rf_mode?: string;
  rf_paired_count?: number;
  // Offline sync state
  is_pending_sync?: boolean;
  registered_at?: string;
  updated_at?: string;
}

export type ClaimStatus =
  | 'unclaimed'           // No row in DB — safe to register
  | 'claimed_by_self'     // Row exists, same user
  | 'claimed_by_other'    // Row exists, different user — cannot claim
  | 'offline_unknown';    // No network — cannot verify


// ─── Hook ─────────────────────────────────────────────────────────────────────

export function useRegistration() {
  const [registeredDevices, setRegisteredDevices] = useState<RegisteredDevice[]>([]);
  const [isLoading, setIsLoading]                 = useState(true);
  const [hasPendingSync, setHasPendingSync]       = useState(false);

  const repo = DeviceRepository.getInstance();

  // ── Boot: initialize repo, load local, then sync from cloud ─────────────────
  useEffect(() => {
    const boot = async () => {
      await repo.initialize();
      setRegisteredDevices(repo.getDevices());
      setIsLoading(false);

      // Cloud sync — updates repo in-memory, then we pull fresh state
      const merged = await repo.syncFromCloud();
      setRegisteredDevices(merged);
    };
    boot();

    // Subscribe to repo changes (from other callers)
    const unsub = repo.subscribe(() => {
      setRegisteredDevices(repo.getDevices());
    });
    return unsub;
  }, []);


  // ── Save (upsert) a device ───────────────────────────────────────────────────
  /**
   * Persists a device registration to local storage and Supabase.
   * Delegates to DeviceRepository for all persistence logic.
   */
  const saveRegisteredDevice = async (device: Partial<RegisteredDevice> & { device_mac: string }) => {
    try {
      const ok = await repo.saveDevice(device);
      setRegisteredDevices(repo.getDevices());
      if (!ok) setHasPendingSync(true);
      return ok;
    } catch (e) {
      AppLogger.warn('[Registration] Save failed:', e);
      setHasPendingSync(true);
      return false;
    }
  };

  // ── Save multiple devices at once (first-time wizard) ───────────────────────
  const saveAllRegisteredDevices = useCallback(async (devices: RegisteredDevice[]): Promise<boolean> => {
    const ok = await repo.saveAllDevices(devices);
    setRegisteredDevices(repo.getDevices());
    if (!ok) setHasPendingSync(true);
    return ok;
  }, []);

  // ── Check claim status of a specific device MAC ──────────────────────────────
  const checkDeviceClaimed = useCallback(async (
    deviceMac: string,
    fingerprint?: { firmwareVer?: number; ledVersion?: number; productId?: number }
  ): Promise<ClaimStatus> => {
    return repo.checkDeviceClaimed(deviceMac, fingerprint);
  }, []);

  // ── Deregister (release ownership) ───────────────────────────────────────────
  const deregisterDevice = useCallback(async (deviceMac: string): Promise<void> => {
    try {
      await repo.deleteDevice(deviceMac);
      setRegisteredDevices(repo.getDevices());
    } catch (e: any) {
      AppLogger.warn('[Registration] Deregister failed:', e);
      Alert.alert('Delete Failed', `Could not remove device: ${e.message || String(e)}`);
    }
  }, []);


  // ── Swap positions of two paired devices ─────────────────────────────────────
  const swapDevicePositions = useCallback(async (mac1: string, mac2: string): Promise<void> => {
    const d1 = repo.findDevice(mac1);
    const d2 = repo.findDevice(mac2);
    if (!d1 || !d2) return;

    // Swap positions
    const tmp = d1.position;
    d1.position = d2.position;
    d2.position = tmp;

    // FIX: Use canonical NamingUtils format
    d1.device_name = `${getDefaultDeviceName(d1.device_mac)}${d1.position ? ` ${d1.position}` : ''}`;
    d2.device_name = `${getDefaultDeviceName(d2.device_mac)}${d2.position ? ` ${d2.position}` : ''}`;

    await repo.saveDevice(d1);
    await repo.saveDevice(d2);
    setRegisteredDevices(repo.getDevices());
  }, []);

  // ── Check if user has ANY registered devices (cloud or local) ────────────────
  const hasCloudRegistrations = useCallback(async (): Promise<boolean> => {
    return repo.hasRegistrations();
  }, []);

  // ── Migrate legacy local groups into registered_devices ──────────────────────
  const migrateLegacyGroups = useCallback(async (
    allDevices: any[],
    deviceConfigs: Record<string, any>
  ): Promise<RegisteredDevice[]> => {
    // Legacy migration path — builds RegisteredDevice objects from old AsyncStorage groups.
    // DeviceRepository doesn't own this because it's a one-time migration helper.
    const migrated: RegisteredDevice[] = [];
    try {
      const AsyncStorage = (await import('@react-native-async-storage/async-storage')).default;
      const raw = await AsyncStorage.getItem('@Sk8lytz_custom_groups');
      if (!raw) return migrated;
      const groups: any[] = JSON.parse(raw);

      for (const group of groups) {
        for (const mac of group.deviceIds) {
          const device = allDevices.find(d => d.id === mac);
          const config = deviceConfigs[mac] || {};
          if (!device) continue;

          const rd: RegisteredDevice = {
            device_mac:     mac,
            device_name:    config.name || device.name || mac,
            product_type:   config.type || LOCAL_PRODUCT_CATALOG[0].id,
            position:       config.name?.includes('Left') ? 'Left' :
                           config.name?.includes('Right') ? 'Right' : null,
            group_name:     group.name,
            group_id:       group.id || group.name,
            led_points:     config.points,
            segments:       config.segments,
            ic_type:        config.stripType,
            color_sorting:  config.sorting,
            is_pending_sync: true,
          };
          migrated.push(rd);
        }
      }
    } catch (e) {
      AppLogger.warn('[Registration] Legacy migration failed:', e);
    }
    return migrated;
  }, []);

  // ── Cloud re-sync (exposed for manual refresh) ──────────────────────────────
  const syncFromCloud = useCallback(async () => {
    const merged = await repo.syncFromCloud();
    setRegisteredDevices(merged);
  }, []);

  return {
    registeredDevices,
    isLoading,
    hasPendingSync,
    saveRegisteredDevice,
    saveAllRegisteredDevices,
    checkDeviceClaimed,
    deregisterDevice,
    swapDevicePositions,
    hasCloudRegistrations,
    migrateLegacyGroups,
    syncFromCloud,
  };
}

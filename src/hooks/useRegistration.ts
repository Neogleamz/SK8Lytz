/**
 * useRegistration.ts — SK8Lytz Device Ownership Registry
 *
 * Local-first architecture:
 *   1. On mount → load from AsyncStorage (instant, no network needed)
 *   2. If online → fetch from Supabase, overwrite local cache
 *   3. All writes → AsyncStorage first, then Supabase (or queue if offline)
 *   4. On reconnect → flush pending offline registrations
 *
 * A "registered device" means a specific BLE controller MAC address is
 * associated with a Supabase user account. This is soft-ownership (no
 * hardware binding is possible with Zengge controllers).
 */

import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useRef, useState } from 'react';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';
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

const LOCAL_KEY        = '@Sk8lytz_registered_devices';
const PENDING_SYNC_KEY = '@Sk8lytz_pending_sync';

// ─── Hook ─────────────────────────────────────────────────────────────────────

export function useRegistration() {
  const [registeredDevices, setRegisteredDevices] = useState<RegisteredDevice[]>([]);
  const [isLoading, setIsLoading]                 = useState(true);
  const [hasPendingSync, setHasPendingSync]       = useState(false);
  const isSyncing = useRef(false);

  // ── Boot: load local cache, then sync from Supabase ─────────────────────────
  useEffect(() => {
    loadLocal().then(() => syncFromCloud());
  }, []);

  const loadLocal = async () => {
    try {
      const raw = await AsyncStorage.getItem(LOCAL_KEY);
      if (raw) {
        setRegisteredDevices(JSON.parse(raw));
      }
    } catch (e) {
      AppLogger.warn('[Registration] Local load failed:', e);
    } finally {
      setIsLoading(false);
    }
  };

  const syncFromCloud = async () => {
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;

      const { data, error } = await supabase
        .from('registered_devices')
        .select('*')
        .eq('user_id', user.id)
        .order('registered_at', { ascending: true });

      if (error) throw error;
      if (!data) return;

      // Cast via `as RegisteredDevice` — Supabase Row type infers a precise shape
      // that doesn't fully overlap with RegisteredDevice's optional fields at compile time,
      // but the data contract is guaranteed by the schema.
      const devices: RegisteredDevice[] = data.map((row: Record<string, any>) => ({
        ...row,
        is_pending_sync: false,
      } as RegisteredDevice));

      setRegisteredDevices(devices);
      await AsyncStorage.setItem(LOCAL_KEY, JSON.stringify(devices));

      // Flush any offline-queued registrations
      await flushPendingSync(user.id);
    } catch (e) {
      AppLogger.warn('[Registration] Cloud sync failed (offline?):', e);
    }
  };

  // ── Save (upsert) a device ───────────────────────────────────────────────────
  /**
   * Persists a device registration to local storage and Supabase.
   * Uses "Hardened Column Mapping" to ensure only valid database schema fields
   * are sent to Supabase, protecting against schema mismatch crashes.
   * Auto-generates mandatory UI-required fields like 'id' and 'group_id'.
   */
  const saveRegisteredDevice = async (device: Partial<RegisteredDevice> & { device_mac: string }) => {
    try {
      const { data: { user } } = await supabase.auth.getUser();
      const now = new Date().toISOString();
      const deviceId = device.id || `${device.device_mac.replace(/:/g, '')}-${user?.id?.slice(0, 8) || 'offline'}`;
      const groupId = device.group_id || device.group_name?.toLowerCase().replace(/\s+/g, '-') || 'default-fleet';
      
      const fullDevice: RegisteredDevice = {
        device_name: device.device_name || 'Unknown Device',
        product_type: device.product_type || LOCAL_PRODUCT_CATALOG[0].id,
        group_name: device.group_name || 'Default Fleet',
        position: device.position || null,
        ...device,
        id: deviceId,
        group_id: groupId,
        user_id: user?.id,
        updated_at: now,
        registered_at: device.registered_at || now,
        is_pending_sync: false,
      };

      // 1. Write local immediately (offline-safe)
      const current = await getLocalDevices();
      const idx = current.findIndex(d => d.device_mac === device.device_mac);
      if (idx >= 0) current[idx] = fullDevice;
      else current.push(fullDevice);
      await AsyncStorage.setItem(LOCAL_KEY, JSON.stringify(current));
      setRegisteredDevices([...current]);

        // 2. Try Supabase upsert - Explicitly pick valid columns
        if (user) {
          // PRE-FLIGHT: Ensure the group exists in 'registered_groups' to satisfy the 'registered_devices_group_id_fkey' constraint.
          try {
            await supabase.from('registered_groups').upsert({
              id: fullDevice.group_id,
              group_name: fullDevice.group_name || 'Default Fleet',
              type: 'device-fleet',
              user_id: user.id
            } as any, { onConflict: 'id' } as any);
          } catch (fkError) {
            AppLogger.warn('[Registration] Could not establish group FK pre-flight:', fkError);
          }

          const dbRow = {
            device_mac:      fullDevice.device_mac,
            device_name:     fullDevice.device_name,
            product_type:    fullDevice.product_type,
            position:        fullDevice.position,
            group_name:      fullDevice.group_name,
            group_id:        fullDevice.group_id,
            custom_name:     fullDevice.device_name,
            points:          fullDevice.led_points || 0,
            led_points:      fullDevice.led_points,
            segments:        fullDevice.segments || 1,
            ic_type:         fullDevice.ic_type,
            strip_type:      fullDevice.ic_type || 'WS2812B',
            color_sorting:   fullDevice.color_sorting,
            sorting:         fullDevice.color_sorting || 'GRB',
            rssi_at_register: fullDevice.rssi_at_register,
            firmware_ver:    fullDevice.firmware_ver,
            led_version:     fullDevice.led_version,
            product_id:      fullDevice.product_id,
            user_id:         user.id,
            id:              fullDevice.id || deviceId,
            updated_at:      now,
            registered_at:   fullDevice.registered_at,
          };

          // Cast as `any` — dbRow contains valid DB columns (points, strip_type) that exist
          // in the schema but the auto-generated Insert type doesn't perfectly capture.
          const { error } = await supabase
            .from('registered_devices')
            .upsert(dbRow as any, { onConflict: 'user_id,device_mac' } as any);

          if (error) throw error;
        } else {
        // No session — queue for later sync
        await queuePendingSync(fullDevice);
        setHasPendingSync(true);
      }

      return true;
    } catch (e) {
      AppLogger.warn('[Registration] Save failed, queuing offline:', e);
      await queuePendingSync(device);
      setHasPendingSync(true);
      return false; // saved locally, pending cloud
    }
  };

  // ── Save multiple devices at once (first-time wizard) ───────────────────────
  const saveAllRegisteredDevices = useCallback(async (devices: RegisteredDevice[]): Promise<boolean> => {
    let allOk = true;
    for (const d of devices) {
      const ok = await saveRegisteredDevice(d);
      if (!ok) allOk = false;
    }
    return allOk;
  }, [saveRegisteredDevice]);

  // ── Check claim status of a specific device MAC ──────────────────────────────
  const checkDeviceClaimed = useCallback(async (
    deviceMac: string,
    fingerprint?: { firmwareVer?: number; ledVersion?: number; productId?: number }
  ): Promise<ClaimStatus> => {
    try {
      // 1. FAST PATH: Check local storage first. If we already hold it locally, it's ours.
      const localDevices = await getLocalDevices();
      if (localDevices.some(d => d.device_mac.toLowerCase() === deviceMac.toLowerCase())) {
        return 'claimed_by_self';
      }

      // 2. NETWORK PATH: Check Supabase to see if someone else owns it
      const { data: { user } } = await supabase.auth.getUser();

      // Query DB for this device_mac
      const { data, error } = await supabase
        .from('registered_devices')
        .select('user_id')
        .eq('device_mac', deviceMac)
        .maybeSingle();

      if (error) throw error;

      if (!data) {
        // Try fingerprint fallback if MAC returned nothing
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
      AppLogger.warn('[Registration] Claim check failed (offline?):', e);
      return 'offline_unknown';
    }
  }, []);

  // ── Deregister (release ownership) ───────────────────────────────────────────
  const deregisterDevice = useCallback(async (deviceMac: string): Promise<void> => {
    try {
      const { data: { user } } = await supabase.auth.getUser();

      // Remove from local
      const current = await getLocalDevices();
      const filtered = current.filter(d => d.device_mac !== deviceMac);
      await AsyncStorage.setItem(LOCAL_KEY, JSON.stringify(filtered));
      setRegisteredDevices(filtered);

      // Remove from Supabase
      if (user) {
        await supabase
          .from('registered_devices')
          .delete()
          .eq('user_id', user.id)
          .eq('device_mac', deviceMac);
      }
    } catch (e) {
      AppLogger.warn('[Registration] Deregister failed:', e);
    }
  }, []);

  // ── Swap positions of two paired devices ─────────────────────────────────────
  const swapDevicePositions = useCallback(async (mac1: string, mac2: string): Promise<void> => {
    const current = await getLocalDevices();
    const d1 = current.find(d => d.device_mac === mac1);
    const d2 = current.find(d => d.device_mac === mac2);
    if (!d1 || !d2) return;

    // Swap positions
    const tmp = d1.position;
    d1.position = d2.position;
    d2.position = tmp;

    // FIX: Use canonical NamingUtils format instead of `${productType} ${position}`
    // which produced non-standard strings like "HALOZ Left" that never matched
    // anything resolved via getDefaultDeviceName elsewhere in the app.
    d1.device_name = `${getDefaultDeviceName(d1.device_mac)}${d1.position ? ` ${d1.position}` : ''}`;
    d2.device_name = `${getDefaultDeviceName(d2.device_mac)}${d2.position ? ` ${d2.position}` : ''}`;

    await saveRegisteredDevice(d1);
    await saveRegisteredDevice(d2);
  }, [saveRegisteredDevice]);

  // ── Check if user has ANY registered devices (cloud or local) ────────────────
  const hasCloudRegistrations = useCallback(async (): Promise<boolean> => {
    try {
      // 1. Always check local first because it reflects the true current state
      const local = await getLocalDevices();
      if (local.length > 0) return true;

      // 2. Fallback to Supabase if local is empty (e.g., fresh install with existing cloud data)
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
  }, []);

  // ── Migrate legacy local groups into registered_devices ──────────────────────
  const migrateLegacyGroups = useCallback(async (
    allDevices: any[],
    deviceConfigs: Record<string, any>
  ): Promise<RegisteredDevice[]> => {
    const migrated: RegisteredDevice[] = [];
    try {
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
            product_type:   config.type || LOCAL_PRODUCT_CATALOG[0].id, // Local-first fallback: use first catalog entry
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

  // ── Internals ────────────────────────────────────────────────────────────────

  const getLocalDevices = async (): Promise<RegisteredDevice[]> => {
    try {
      const raw = await AsyncStorage.getItem(LOCAL_KEY);
      return raw ? JSON.parse(raw) : [];
    } catch { return []; }
  };

  const queuePendingSync = async (device: Partial<RegisteredDevice> & { device_mac: string }) => {
    try {
      const raw = await AsyncStorage.getItem(PENDING_SYNC_KEY);
      const queue: RegisteredDevice[] = raw ? JSON.parse(raw) : [];
      const idx = queue.findIndex(d => d.device_mac === device.device_mac);
      const marked: RegisteredDevice = { 
        device_name: device.device_name || 'Unknown Device',
        product_type: device.product_type || LOCAL_PRODUCT_CATALOG[0].id,
        group_name: device.group_name || 'Default Fleet',
        position: device.position || null,
        group_id: device.group_id || 'default-fleet',
        ...device, 
        is_pending_sync: true 
      };
      if (idx >= 0) queue[idx] = marked; else queue.push(marked);
      await AsyncStorage.setItem(PENDING_SYNC_KEY, JSON.stringify(queue));
    } catch (e) { AppLogger.warn('[Registration] Queue failed:', e); }
  };

  const flushPendingSync = async (userId: string) => {
    if (isSyncing.current) return;
    isSyncing.current = true;
    try {
      const raw = await AsyncStorage.getItem(PENDING_SYNC_KEY);
      if (!raw) return;
      const queue: RegisteredDevice[] = JSON.parse(raw);
      if (queue.length === 0) return;

      for (const device of queue) {
        // PRE-FLIGHT: Ensure the group exists in 'registered_groups'
        const groupId = device.group_id || device.group_name.toLowerCase().replace(/\s+/g, '-') || 'default-fleet';
        try {
          await supabase.from('registered_groups').upsert({
            id: groupId,
            group_name: device.group_name || 'Default Fleet',
            type: 'device-fleet',
            user_id: userId
          } as any, { onConflict: 'id' } as any);
        } catch (fkError) {
          AppLogger.warn('[Registration] Flush pre-flight group FK error:', fkError);
        }

        const dbRow = {
          device_mac:      device.device_mac,
          device_name:     device.device_name,
          product_type:    device.product_type,
          position:        device.position,
          group_id:        device.group_id || device.group_name.toLowerCase().replace(/\s+/g, '-') || 'default-fleet',
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
          user_id:         userId,
          id:              device.id || `${device.device_mac.replace(/:/g, '')}-${userId.slice(0, 8)}`,
          updated_at:      new Date().toISOString(),
          registered_at:   device.registered_at,
        };

        const { error } = await supabase
          .from('registered_devices')
          .upsert(dbRow as any, { onConflict: 'user_id,device_mac' } as any);
        if (error) AppLogger.warn('[Registration] Flush error for ' + device.device_mac, { error: error.message });
      }

      await AsyncStorage.removeItem(PENDING_SYNC_KEY);
      setHasPendingSync(false);
    } catch (e) {
      AppLogger.warn('[Registration] Flush failed:', e);
    } finally {
      isSyncing.current = false;
    }
  };

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


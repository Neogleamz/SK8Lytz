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

import { useCallback, useEffect, useRef, useState } from 'react';
import { Alert } from 'react-native';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { AppLogger } from '../services/appLogger';
import DeviceRepository from '../services/deviceRepository';
import { getDefaultDeviceName } from '../utils/NamingUtils';
import { useAuth } from '../context/AuthContext';
import type { ViewState } from '../types/ViewState';

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

export type ClaimStatus =
  | 'unclaimed'           // No row in DB — safe to register
  | 'claimed_by_self'     // Row exists, same user
  | 'claimed_by_other'    // Row exists, different user — cannot claim
  | 'offline_unknown';    // No network — cannot verify


// ─── Hook ─────────────────────────────────────────────────────────────────────

export function useRegistration() {
  const [registeredDevices, setRegisteredDevices] = useState<RegisteredDevice[]>([]);
  /** 4-state FSM: idle → loading → success/empty/error */
  const [viewState, setViewState] = useState<ViewState>('loading');
  const [errorMsg, setErrorMsg] = useState('');
  const [hasPendingSync, setHasPendingSync]       = useState(false);
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => {
      isMountedRef.current = false;
    };
  }, []);

  const { session } = useAuth();
  const userId = session?.user?.id;

  const repo = DeviceRepository.getInstance();

  // ── Boot: initialize repo, load local, then sync from cloud ─────────────────
  useEffect(() => {
    let isActive = true;
    const boot = async () => {
      try {
        await repo.initialize();
        if (!isActive) return;
        setRegisteredDevices(repo.getDevices());

        // Cloud sync — updates repo in-memory, then we pull fresh state
        const merged = await repo.syncFromCloud(userId);
        if (!isActive) return;
        setRegisteredDevices(merged);
        setViewState(merged.length > 0 ? 'success' : 'empty');
      } catch (e: unknown) {
        if (!isActive) return;
        AppLogger.warn('[useRegistration] Boot initialization or cloud sync failed', {
          error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0
        });
        setErrorMsg(e instanceof Error ? e.message : String(e));
        setViewState('error');
      } finally {
        // loading state handled
      }
    };
    boot();

    // Subscribe to repo changes (from other callers)
    const unsub = repo.subscribe(() => {
      if (!isActive) return;
      setRegisteredDevices(repo.getDevices());
    });
    return () => {
      isActive = false;
      unsub();
    };
  }, [userId]);


  // ── Save (upsert) a device ───────────────────────────────────────────────────
  /**
   * Persists a device registration to local storage and Supabase.
   * Delegates to DeviceRepository for all persistence logic.
   */
  const saveRegisteredDevice = async (device: Partial<RegisteredDevice> & { device_mac: string }) => {
    try {
      const ok = await repo.saveDevice(device, userId);
      if (!isMountedRef.current) return ok;
      setRegisteredDevices(repo.getDevices());
      if (!ok) setHasPendingSync(true);
      return ok;
    } catch (e: unknown) {
      if (!isMountedRef.current) return false;
      AppLogger.warn('[Registration] Save failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      setHasPendingSync(true);
      return false;
    }
  };

  // ── Save multiple devices at once (first-time wizard) ───────────────────────
  const saveAllRegisteredDevices = useCallback(async (devices: RegisteredDevice[]): Promise<boolean> => {
    try {
      const ok = await repo.saveAllDevices(devices, userId);
      if (!isMountedRef.current) return ok;
      setRegisteredDevices(repo.getDevices());
      if (!ok) setHasPendingSync(true);
      return ok;
    } catch (e: unknown) {
      if (!isMountedRef.current) return false;
      AppLogger.warn('[Registration] Save all devices failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      setHasPendingSync(true);
      return false;
    }
  }, [userId]);

  // ── Check claim status of a specific device MAC ──────────────────────────────
  const checkDeviceClaimed = useCallback(async (
    deviceMac: string,
    fingerprint?: { firmwareVer?: number; ledVersion?: number; productId?: number }
  ): Promise<ClaimStatus> => {
    try {
      return await repo.checkDeviceClaimed(deviceMac, fingerprint, userId);
    } catch (e: unknown) {
      AppLogger.warn('[Registration] Check device claimed failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      return 'offline_unknown';
    }
  }, [userId]);

  // ── Deregister (release ownership) ───────────────────────────────────────────
  const deregisterDevice = useCallback(async (deviceMac: string): Promise<void> => {
    try {
      await repo.deleteDevice(deviceMac, userId);
      if (!isMountedRef.current) return;
      setRegisteredDevices(repo.getDevices());
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      AppLogger.warn('[Registration] Deregister failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      Alert.alert('Delete Failed', `Could not remove device: ${(e instanceof Error ? e.message : String(e))}`);
    }
  }, [userId]);


  // ── Swap positions of two paired devices ─────────────────────────────────────
  const swapDevicePositions = useCallback(async (mac1: string, mac2: string): Promise<void> => {
    try {
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

      await repo.saveDevice(d1, userId);
      await repo.saveDevice(d2, userId);
      if (!isMountedRef.current) return;
      setRegisteredDevices(repo.getDevices());
    } catch (e: unknown) {
      AppLogger.warn('[Registration] Swap positions failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
    }
  }, [userId]);

  // ── Check if user has ANY registered devices (cloud or local) ────────────────
  const hasCloudRegistrations = useCallback(async (): Promise<boolean> => {
    try {
      return await repo.hasRegistrations(userId);
    } catch (e: unknown) {
      AppLogger.warn('[Registration] Check registrations failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      return false;
    }
  }, [userId]);



  // ── Cloud re-sync (exposed for manual refresh) ──────────────────────────────
  const syncFromCloud = useCallback(async () => {
    try {
      const merged = await repo.syncFromCloud(userId);
      if (!isMountedRef.current) return;
      setRegisteredDevices(merged);
    } catch (e: unknown) {
      AppLogger.warn('[Registration] Sync from cloud failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
    }
  }, [userId]);

  return {
    registeredDevices,
    viewState,
    errorMsg,
    // Legacy support for unmodified consumers
    isLoading: viewState === 'loading',
    hasPendingSync,
    saveRegisteredDevice,
    saveAllRegisteredDevices,
    checkDeviceClaimed,
    deregisterDevice,
    swapDevicePositions,
    hasCloudRegistrations,
    syncFromCloud,
  };
}

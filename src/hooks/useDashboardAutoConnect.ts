/**
 * useDashboardAutoConnect.ts — Dashboard BLE Auto-Connection Orchestrator
 *
 * Owns the cloud-sync-and-auto-connect startup sequence:
 *  1. On BLE ready: queries Supabase for the user's registered groups
 *  2. Falls back to AsyncStorage if offline
 *  3. Queues target device MACs into autoConnectIdsRef
 *  4. Runs a continuous observer that connects any queued device the moment
 *     it appears in the BLE scan list
 *
 * BLE transport calls (`connectToDevices`, `scanForPeripherals`) are passed
 * in as parameters, keeping them co-located in DashboardScreen per the
 * Master Reference constraint.
 *
 * Extracted from DashboardScreen.tsx (Phase 2 — God Object Surgery).
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useEffect, useRef } from 'react';
import type { RegisteredDevice } from '../hooks/useRegistration';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';

/** Minimal device shape needed from useBLE */
interface BLEDevice {
  id: string;
  name?: string | null;
}

interface UseDashboardAutoConnectOptions {
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  isActuallyConnected: boolean;
  allDevices: BLEDevice[];
  connectedDevices: BLEDevice[];
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  connectToDevices: (devices: any[]) => Promise<void>;
  scanForPeripherals: () => void;
  requestPermissions: () => Promise<boolean>;
  refreshProfile: () => Promise<void>;
  registeredDevices: RegisteredDevice[];
}

/**
 * Fires the cloud-sync → group-resolution → BLE auto-connect sequence
 * once per app launch, after BLE is confirmed ready.
 * Returns nothing — all managed via side-effects internally.
 */
export function useDashboardAutoConnect({
  isBluetoothSupported,
  isBluetoothEnabled,
  isActuallyConnected,
  allDevices,
  connectedDevices,
  connectToDevices,
  scanForPeripherals,
  requestPermissions,
  refreshProfile,
}: UseDashboardAutoConnectOptions): void {

  const hasAutoConnectedRef = useRef(false);
  const autoConnectIdsRef = useRef<string[]>([]);

  // ── Continuous observer: connect queued devices as they appear in scan ───
  useEffect(() => {
    if (autoConnectIdsRef.current.length === 0) return;

    const pendingToConnect = allDevices.filter(d =>
      autoConnectIdsRef.current.includes(d.id) &&
      !connectedDevices.some(c => c.id === d.id)
    );

    if (pendingToConnect.length > 0) {
      // Drain queue for this batch to prevent reconnection loop
      autoConnectIdsRef.current = autoConnectIdsRef.current.filter(
        id => !pendingToConnect.some(p => p.id === id)
      );
      AppLogger.log('BLE_STATE_CHANGE', {
        event: 'auto_connect_observer',
        devices: pendingToConnect.map(d => d.name ?? d.id),
      });
      connectToDevices(pendingToConnect as any);
    }
  }, [allDevices, connectedDevices]);

  // ── One-shot cloud sync + auto-connect trigger on BLE ready ─────────────
  useEffect(() => {
    async function syncCloudAndAutoConnect() {
      if (hasAutoConnectedRef.current || !isBluetoothSupported || !isBluetoothEnabled) return;
      hasAutoConnectedRef.current = true;

      let groupsToProcess: any[] = [];
      let isOffline = true;
      let cloudUserId: string | null = null;

      if (supabase) {
        const { data: { session } } = await supabase.auth.getSession();
        if (session?.user) {
          cloudUserId = session.user.id;

          try {
            await refreshProfile();
          } catch (e) {
            AppLogger.warn('Failed to refresh profile on dashboard load', { error: String(e) });
          }

          let groups: any[] | null = null;
          try {
            const result = await supabase
              .from('registered_groups')
              .select('*')
              .eq('user_id', cloudUserId);
            groups = result.data as any[];
            isOffline = !!result.error;
          } catch {
            isOffline = true;
          }

          if (!isOffline && groups && groups.length > 0) {
            groupsToProcess = groups;
          }
        }
      }

      if (isOffline || groupsToProcess.length === 0) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_offline_fallback' });
        const localDevicesStr = await AsyncStorage.getItem('@Sk8lytz_registered_devices');
        if (localDevicesStr) {
          try {
            const parsed = JSON.parse(localDevicesStr);
            const offlineGroupMap = new Map<string, any>();
            parsed.forEach((d: any) => {
              if (d.group_id && d.group_id !== 'default-fleet') {
                if (!offlineGroupMap.has(d.group_id)) {
                  offlineGroupMap.set(d.group_id, {
                    id: d.group_id,
                    group_name: d.group_name || d.group_id,
                    created_at: new Date().toISOString(),
                    deviceIds: [],
                  });
                }
                offlineGroupMap.get(d.group_id).deviceIds.push(d.device_mac);
              }
            });
            groupsToProcess = Array.from(offlineGroupMap.values());
          } catch (e) {
            AppLogger.warn('Failed to parse offline registered_devices', { error: String(e) });
          }
        }
      }

      if (groupsToProcess.length > 0) {
        const granted = await requestPermissions();
        if (granted && !isActuallyConnected) {
          // NOTE: scan is intentionally deferred until AFTER autoConnectIdsRef is
          // populated below. If scan starts first, devices appear in allDevices
          // before the ref is set — the observer fires, sees an empty queue,
          // and skips them. Starting scan after setting the ref guarantees every
          // discovered device is evaluated against a populated target list.

          let presentGroups: any[] = [];
          let idsToConnect: string[] = [];

          if (!isOffline && supabase && cloudUserId) {
            const { data: devices } = await supabase
              .from('registered_devices')
              .select('*')
              .eq('user_id', cloudUserId);
            if (devices) {
              presentGroups = groupsToProcess.sort(
                (a: any, b: any) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
              );
              if (presentGroups.length > 0) {
                idsToConnect = devices
                  .filter((d: any) => d.group_id === presentGroups[0].id)
                  .map((d: any) => d.device_mac || d.id);
              }
            }
          } else {
            presentGroups = groupsToProcess.sort(
              (a: any, b: any) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
            );
            if (presentGroups.length > 0) {
              idsToConnect = presentGroups[0].deviceIds;
            }
          }

          if (idsToConnect.length > 0) {
            AppLogger.log('BLE_STATE_CHANGE', {
              event: 'auto_connect_queued',
              fleet: presentGroups[0]?.group_name,
              count: idsToConnect.length,
            });
            autoConnectIdsRef.current = idsToConnect;
            // Start scan NOW — ref is populated, observer will catch every device
            scanForPeripherals();
          }
        }
      }
    }

    // Slight delay to allow Bluetooth stack to fully initialize
    const timerId = setTimeout(syncCloudAndAutoConnect, 1500);
    return () => clearTimeout(timerId);
  }, [isBluetoothSupported, isBluetoothEnabled]);
}

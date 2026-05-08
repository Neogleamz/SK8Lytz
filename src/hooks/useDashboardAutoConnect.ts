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
  scanForPeripherals: (options?: { disableProbing?: boolean }) => void;
  requestPermissions: () => Promise<boolean>;
  refreshProfile: () => Promise<void>;
  registeredDevices: RegisteredDevice[];
  /** Global connection gate semaphore — observer only connects when IDLE */
  bleGateRef: React.MutableRefObject<string>;
  /** Gate the observer if setup wizard is active */
  isWizardActive?: boolean;
  /** Trigger a high-power active scan (from useBLESweeper) */
  burstScan?: (durationMs: number) => Promise<void>;
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
  bleGateRef,
  isWizardActive,
  burstScan,
}: UseDashboardAutoConnectOptions): { clearAutoConnectQueue: () => void; retriggerAutoConnect: () => void } {

  const hasAutoConnectedRef = useRef(false);
  const autoConnectIdsRef = useRef<string[]>([]);
  // Stable ref to the cloud sync function — allows retriggerAutoConnect to call it
  // directly without needing to re-subscribe to the useEffect dependency array.
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const syncCloudAndAutoConnectRef = useRef<(isRetrigger?: boolean) => Promise<void>>(async () => {});

  // ── Continuous observer: connect queued devices as they appear in scan ───
  // Debounce: batch devices that appear within 500ms into a single connectToDevices call.
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const pendingBatchRef = useRef<BLEDevice[]>([]);

  useEffect(() => {
    if (autoConnectIdsRef.current.length === 0) return;
    if (isWizardActive) return;

    const pendingToConnect = allDevices.filter(d =>
      autoConnectIdsRef.current.includes(d.id) &&
      !connectedDevices.some(c => c.id === d.id)
    );

    if (pendingToConnect.length > 0) {
      // Accumulate into batch
      for (const d of pendingToConnect) {
        if (!pendingBatchRef.current.some(p => p.id === d.id)) {
          pendingBatchRef.current.push(d);
        }
      }

      // Clear existing debounce timer and set a new one
      if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);

      const attemptConnection = () => {
        // ── GATE CHECK: Only connect when no other BLE operation is in-flight ──
        if (bleGateRef.current !== 'IDLE') {
          AppLogger.log('BLE_STATE_CHANGE', {
            event: 'auto_connect_observer_gate_blocked_retrying',
            gate: bleGateRef.current,
            batchSize: pendingBatchRef.current.length,
          });
          // BUG-02 Fix: Do not wipe pendingBatchRef. Instead, retry in 1000ms.
          debounceTimerRef.current = setTimeout(attemptConnection, 1000);
          return;
        }

        const batch = [...pendingBatchRef.current];
        pendingBatchRef.current = [];

        // Drain queue for this batch to prevent reconnection loop
        autoConnectIdsRef.current = autoConnectIdsRef.current.filter(
          id => !batch.some(p => p.id === id)
        );

        AppLogger.log('BLE_STATE_CHANGE', {
          event: 'auto_connect_observer',
          devices: batch.map(d => d.name ?? d.id),
        });

        connectToDevices(batch as any).finally(() => {
          if (autoConnectIdsRef.current.length > 0) {
            AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_resume_scan' });
            scanForPeripherals({ disableProbing: true });
          }
        });
      };

      debounceTimerRef.current = setTimeout(attemptConnection, 500); // 500ms debounce window
    }
  }, [allDevices, connectedDevices]);

  // ── One-shot cloud sync + auto-connect trigger on BLE ready ─────────────
  useEffect(() => {
    async function syncCloudAndAutoConnect(isRetrigger: boolean = false) {
      if ((hasAutoConnectedRef.current && !isRetrigger) || !isBluetoothSupported || !isBluetoothEnabled) return;
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
        
        const processLocalDevices = (devicesArray: any[]) => {
          const offlineGroupMap = new Map<string, any>();
          devicesArray.forEach((d: any) => {
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
        };

        if (registeredDevices && registeredDevices.length > 0) {
          processLocalDevices(registeredDevices);
        } else {
          const localDevicesStr = await AsyncStorage.getItem('@Sk8lytz_registered_devices');
          if (localDevicesStr) {
            try {
              const parsed = JSON.parse(localDevicesStr);
              processLocalDevices(parsed);
            } catch (e) {
              AppLogger.warn('Failed to parse offline registered_devices', { error: String(e) });
            }
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
            // ── Overwatch: NO manual scan needed here ──────────────────────────
            // useBLESweeper (Silent Sweeper) is already running in the background,
            // continuously populating allDevices. The observer effect above will
            // fire connectToDevices() the moment Fleet MACs appear in the list.
            // Starting a manual scan here was a duplicate trigger that raced
            // against the Sweeper and caused double-connection attempts.

            // BUG-04 Fix: If this is a post-wizard retrigger, we don't want to wait 
            // for the passive sweeper's next slow cycle (which could take 30s+ in low power).
            // We fire a targeted 8-second burst scan to instantly discover the device.
            if (isRetrigger && burstScan) {
              AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_burst_scan_triggered' });
              burstScan(8000).catch(() => {});
            }
          }
        }
      }
    }

    // Keep the ref current so retriggerAutoConnect can call this function
    // after the Wizard completes without needing to re-run the useEffect.
    syncCloudAndAutoConnectRef.current = syncCloudAndAutoConnect;

    // Slight delay to allow Bluetooth stack to fully initialize
    const timerId = setTimeout(() => syncCloudAndAutoConnect(false), 1500);
    return () => clearTimeout(timerId);
  }, [isBluetoothSupported, isBluetoothEnabled]);

  return {
    clearAutoConnectQueue: () => {
      autoConnectIdsRef.current = [];
    },
    // FIX: Allow DashboardScreen to re-trigger auto-connect after Setup Wizard completes.
    // Resets the one-shot gate so the cloud sync runs again and queues the newly
    // registered device for connection the moment it appears in the BLE scan.
    retriggerAutoConnect: () => {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_retriggered' });
      hasAutoConnectedRef.current = false;
      autoConnectIdsRef.current = [];
      // The useEffect watching isBluetoothSupported/isBluetoothEnabled won't re-fire
      // since those haven't changed. Call the async function directly with isRetrigger=true.
      syncCloudAndAutoConnectRef.current(true);
    },
  };
}

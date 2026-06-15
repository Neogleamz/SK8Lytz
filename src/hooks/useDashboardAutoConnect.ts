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
import DeviceRepository from '../services/DeviceRepository';
import { useEffect, useRef } from 'react';
import type { Device } from 'react-native-ble-plx';
import type { RegisteredDevice } from '../hooks/useRegistration';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';
import { useAuth } from '../context/AuthContext';
import { jitteredDelay } from '../utils/backoff';
import type { RegisteredGroup, RegisteredDeviceRow } from '../types/ble.types';
import { scrubPII } from '../utils/piiScrubber';

interface UseDashboardAutoConnectOptions {
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  isActuallyConnected: boolean;
  allDevices: Device[];
  connectedDevices: Device[];
  connectToDevices: (devices: Device[]) => Promise<void>;
  scanForPeripherals: (options?: { disableProbing?: boolean }) => void;
  requestPermissions: () => Promise<boolean>;
  refreshProfile: () => Promise<void>;
  registeredDevices: RegisteredDevice[];
  /** Global connection gate semaphore — observer only connects when IDLE */
  getGate: () => string;
  /** Gate the observer if setup wizard is active */
  isWizardActive?: boolean;
  /** Trigger a high-power active scan (from useBLESweeper) */
  burstScan?: (durationMs?: number) => void | Promise<void>;
}

/**
 * Pure function: maps an array of registered devices (new or legacy format) into
 * a group map suitable for the offline auto-connect sequence.
 *
 * Handles BOTH the new many-to-many format (group_ids[]) and the legacy scalar
 * format (group_id) from pre-migration cache rows.
 *
 * @param devicesArray - Array of RegisteredDevice objects (may be pre-migration scalar format)
 * @returns Array of group objects with { id, group_name, deviceIds[] }
 *
 * MIGRATION-SHIM: The group_id scalar fallback can be removed at v3.9.0
 * once all users have re-registered their devices through the Setup Wizard.
 */
export function buildOfflineGroupMap(devicesArray: RegisteredDevice[]): Array<{
  id: string;
  group_name: string;
  created_at: string;
  deviceIds: string[];
}> {
  const offlineGroupMap = new Map<string, { id: string; group_name: string; created_at: string; deviceIds: string[] }>();
  const now = new Date().toISOString();

  devicesArray.forEach((d) => {
    // Many-to-many migration: prefer group_ids array, fall back to legacy scalar group_id
    // MIGRATION-SHIM: Remove scalar fallback at v3.9.0
    const gIds: string[] = d.group_ids ?? (d.group_id ? [d.group_id] : []);
    const gNames: string[] = d.group_names ?? (d.group_name ? [d.group_name] : []);
    gIds.forEach((gId: string, idx: number) => {
      if (gId && gId !== 'default-fleet') {
        if (!offlineGroupMap.has(gId)) {
          offlineGroupMap.set(gId, { id: gId, group_name: gNames[idx] || gId, created_at: now, deviceIds: [] });
        }
        const entry = offlineGroupMap.get(gId)!;
        if (!entry.deviceIds.includes(d.device_mac)) {
          entry.deviceIds.push(d.device_mac);
        }
      }
    });
  });

  return Array.from(offlineGroupMap.values());
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
  registeredDevices,
  getGate,
  isWizardActive,
  burstScan,
}: UseDashboardAutoConnectOptions): { clearAutoConnectQueue: () => void; retriggerAutoConnect: () => void } {

  const { session } = useAuth();
  const sessionRef = useRef(session);
  useEffect(() => {
    sessionRef.current = session;
  }, [session]);
  
  const hasAutoConnectedRef = useRef(false);
  const autoConnectIdsRef = useRef<string[]>([]);
  /** Tracks retry counts per MAC for failed auto-connect attempts. RC-02 */
  const autoConnectRetriesRef = useRef<Map<string, number>>(new Map());
  /** Max retries before permanently abandoning a MAC. */
  const MAX_AUTO_CONNECT_RETRIES = 3;
  /** Base backoff in ms between retries (multiplied by attempt count). */
  const AUTO_CONNECT_RETRY_BACKOFF_MS = 3000;
  // Stable ref to the cloud sync function — allows retriggerAutoConnect to call it
  // directly without needing to re-subscribe to the useEffect dependency array.
  const syncCloudAndAutoConnectRef = useRef<(isRetrigger?: boolean) => Promise<void>>(async () => {});
  const lastRetriggerRef = useRef<number>(0);

  // ── Continuous observer: connect queued devices as they appear in scan ───
  // Debounce: batch devices that appear within 500ms into a single connectToDevices call.
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const retryTimersRef = useRef<ReturnType<typeof setTimeout>[]>([]);
  const retriggerTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    return () => {
      retryTimersRef.current.forEach(clearTimeout);
      retryTimersRef.current = [];
      if (retriggerTimerRef.current) clearTimeout(retriggerTimerRef.current);
    };
  }, []);

  const pendingBatchRef = useRef<Device[]>([]);
  const gateRetryCountRef = useRef(0);

  // Stable ref-forwarding: connectToDevices and scanForPeripherals are passed as
  // options and may change identity on re-render. The observer useEffect only depends
  // on [allDevices, connectedDevices], so it would capture a stale closure. The ref
  // always holds the latest version. (Same pattern as connectToDevicesRef in useBLE.ts.)
  const connectToDevicesRef = useRef(connectToDevices);
  connectToDevicesRef.current = connectToDevices;
  const scanForPeripheralsRef = useRef(scanForPeripherals);
  scanForPeripheralsRef.current = scanForPeripherals;

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
        if (getGate() !== 'IDLE') {
          AppLogger.log('BLE_STATE_CHANGE', {
            event: 'auto_connect_observer_gate_blocked_retrying',
            gate: getGate(),
            batchSize: pendingBatchRef.current.length,
            payload_size: 0,
            ssi: 0,
          });
          // BUG-02 Fix: Do not wipe pendingBatchRef. Instead, retry with jittered exponential backoff.
          gateRetryCountRef.current += 1;
          const backoffMs = Math.min(1000 * Math.pow(1.5, gateRetryCountRef.current), 10000);
          const delay = jitteredDelay(backoffMs, backoffMs * 0.25);
          debounceTimerRef.current = setTimeout(attemptConnection, delay);
          return;
        }

        gateRetryCountRef.current = 0;
        const batch = [...pendingBatchRef.current];
        pendingBatchRef.current = [];

        // Drain queue for this batch to prevent reconnection loop
        autoConnectIdsRef.current = autoConnectIdsRef.current.filter(
          id => !batch.some(p => p.id === id)
        );

        AppLogger.log('BLE_STATE_CHANGE', {
          event: 'auto_connect_observer',
          devices: batch.map(d => scrubPII(d.name ?? d.id)),
          payload_size: 0,
          ssi: 0,
        });

        connectToDevicesRef.current(batch)
          .then(() => {
            // On success: clear retry counters for devices that connected
            for (const d of batch) {
              autoConnectRetriesRef.current.delete(d.id);
            }
          })
          .catch((e: unknown) => {
            AppLogger.warn('[AutoConnect] Batch connection failed — re-queueing', {
              macs: batch.map(d => scrubPII(d.id)),
              error: String(e),
              payload_size: 0,
              ssi: 0,
            });

            // Re-queue MACs that aren't already connected and haven't exceeded retries
            const failedIds = batch
              .filter(d => !connectedDevices.some(c => c.id === d.id))
              .map(d => d.id);

            for (const id of failedIds) {
              const retries = (autoConnectRetriesRef.current.get(id) ?? 0) + 1;
              autoConnectRetriesRef.current.set(id, retries);

              if (retries <= MAX_AUTO_CONNECT_RETRIES) {
                const backoff = jitteredDelay(AUTO_CONNECT_RETRY_BACKOFF_MS * retries, 500);
                AppLogger.log('BLE_STATE_CHANGE', {
                  event: 'auto_connect_requeued',
                  deviceId: scrubPII(id),
                  retry: retries,
                  backoffMs: backoff,
                  payload_size: 0,
                  ssi: 0,
                });
                const retryTimerId = setTimeout(() => {
                  if (!autoConnectIdsRef.current.includes(id)) {
                    autoConnectIdsRef.current = [...autoConnectIdsRef.current, id];
                  }
                  retryTimersRef.current = retryTimersRef.current.filter(t => t !== retryTimerId);
                }, backoff);
                retryTimersRef.current.push(retryTimerId);
              } else {
                AppLogger.warn('[AutoConnect] Max retries exceeded — abandoning', { deviceId: scrubPII(id), retries, payload_size: 0, ssi: 0 });
                autoConnectRetriesRef.current.delete(id);
              }
            }
          })
          .finally(() => {
            if (autoConnectIdsRef.current.length > 0) {
              AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_resume_scan', payload_size: 0, ssi: 0 });
              scanForPeripheralsRef.current({ disableProbing: true });
            }
          });
      };

      debounceTimerRef.current = setTimeout(attemptConnection, 500); // 500ms debounce window
    }

    return () => {
      if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [allDevices, connectedDevices, isWizardActive]);

  // ── One-shot cloud sync + auto-connect trigger on BLE ready ─────────────
  useEffect(() => {
    async function syncCloudAndAutoConnect(isRetrigger: boolean = false) {
      if ((hasAutoConnectedRef.current && !isRetrigger) || !isBluetoothSupported || !isBluetoothEnabled) return;
      hasAutoConnectedRef.current = true;

      // ── 1. Immediate Local/Offline Resolution ──
      AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_local_resolution', payload_size: 0, ssi: 0 });
      let groupsToProcess: RegisteredGroup[] = [];
      
      const processLocalDevices = (devicesArray: RegisteredDevice[]) => {
        groupsToProcess = buildOfflineGroupMap(devicesArray);
      };

      if (registeredDevices && registeredDevices.length > 0) {
        processLocalDevices(registeredDevices);
      } else {
        try {
          const localDevices = DeviceRepository.getInstance().getDevices();
          if (localDevices && localDevices.length > 0) {
            processLocalDevices(localDevices);
          }
        } catch (e: unknown) {
          const msg = e instanceof Error ? e.message : String(e);
          AppLogger.warn('Failed to read or parse offline registered_devices', { error: msg, payload_size: 0, ssi: 0 });
        }
      }

      let idsToConnect: string[] = [];
      const presentGroups = groupsToProcess.sort(
        (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
      );
      
      const macSet = new Set<string>();
      for (const group of presentGroups) {
        for (const mac of group.deviceIds ?? []) {
          macSet.add(mac);
        }
      }
      idsToConnect = Array.from(macSet);

      if (idsToConnect.length > 0) {
        let granted = false;
        try {
          granted = await requestPermissions();
        } catch (e: unknown) {
          AppLogger.error('[AutoConnect] requestPermissions failed', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
        }
        if (granted && !isActuallyConnected) {
          AppLogger.log('BLE_STATE_CHANGE', {
            event: 'auto_connect_queued',
            fleets: presentGroups.map(g => scrubPII(g.group_name)),
            groupCount: presentGroups.length,
            count: idsToConnect.length,
            payload_size: 0,
            ssi: 0,
          });
          autoConnectIdsRef.current = idsToConnect;

          // BUG-04 Fix: Burst scan
          if (isRetrigger && burstScan) {
            AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_burst_scan_triggered', payload_size: 0, ssi: 0 });
            try {
              const scanResult = burstScan(8000);
              if (scanResult && typeof scanResult.catch === 'function') {
                scanResult.catch((e: unknown) => {
                  AppLogger.warn('Auto-connect burst scan failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
                });
              }
            } catch (e: unknown) {
              AppLogger.warn('Auto-connect burst scan threw error', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
            }
          }
        }
      }

      // ── 2. Background Cloud Sync ──
      const cloudUserId = sessionRef.current?.user?.id;
      if (supabase && cloudUserId) {
        (async () => {
          try {
            await refreshProfile();
          } catch (e: unknown) {
            const msg = e instanceof Error ? e.message : String(e);
            AppLogger.warn('Failed to refresh profile on dashboard load', { error: msg, payload_size: 0, ssi: 0 });
          }

          try {
            const { data: groups, error: groupsError } = await supabase
              .from('registered_groups')
              .select('*')
              .eq('user_id', cloudUserId);

            if (groupsError) throw groupsError;

            if (groups && groups.length > 0) {
              const { data: devices, error: devError } = await supabase
                .from('registered_devices')
                .select('*')
                .eq('user_id', cloudUserId);

              if (devError) throw devError;

              if (devices) {
                const cloudGroups = groups.sort(
                  (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
                );
                const allGroupIds = new Set(cloudGroups.map(g => g.id));
                const cloudMacSet = new Set<string>();
                for (const d of devices as RegisteredDeviceRow[]) {
                  const dGroupIds: string[] = d.group_ids || (d.group_id ? [d.group_id] : []);
                  if (dGroupIds.some(gId => allGroupIds.has(gId))) {
                    cloudMacSet.add(d.device_mac || d.id);
                  }
                }
                const cloudIds = Array.from(cloudMacSet);
                
                if (cloudIds.length > 0) {
                  const currentSet = new Set(autoConnectIdsRef.current);
                  let addedNew = false;
                  for (const id of cloudIds) {
                    if (!currentSet.has(id)) {
                      currentSet.add(id);
                      addedNew = true;
                    }
                  }
                  if (addedNew) {
                    autoConnectIdsRef.current = Array.from(currentSet);
                    AppLogger.log('BLE_STATE_CHANGE', {
                      event: 'auto_connect_cloud_synced',
                      count: autoConnectIdsRef.current.length,
                      payload_size: 0,
                      ssi: 0,
                    });
                  }
                }
              }
            }
          } catch (e: unknown) {
            const msg = e instanceof Error ? e.message : String(e);
            AppLogger.warn('Failed to query registered groups/devices from Supabase in background', { error: msg, payload_size: 0, ssi: 0 });
          }
        })();
      }
    }

    // Keep the ref current so retriggerAutoConnect can call this function
    // after the Wizard completes without needing to re-run the useEffect.
    syncCloudAndAutoConnectRef.current = syncCloudAndAutoConnect;

    // Slight delay to allow Bluetooth stack to fully initialize
    const timerId = setTimeout(() => {
      syncCloudAndAutoConnect(false).catch((e: unknown) => {
        AppLogger.error('[AutoConnect] syncCloudAndAutoConnect failed', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
      });
    }, 1500);
    return () => clearTimeout(timerId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isBluetoothSupported, isBluetoothEnabled]);

  return {
    clearAutoConnectQueue: () => {
      autoConnectIdsRef.current = [];
    },
    // FIX: Allow DashboardScreen to re-trigger auto-connect after Setup Wizard completes or AppState wakes up.
    // Includes a 5-second throttle and a 1.5-second OS BLE stack wakeup delay.
    retriggerAutoConnect: () => {
      const now = Date.now();
      if (lastRetriggerRef.current && now - lastRetriggerRef.current < 5000) {
        return; // Throttle 5 seconds to prevent spamming burst scans on rapid minimize/maximize
      }
      lastRetriggerRef.current = now;

      AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_retriggered', payload_size: 0, ssi: 0 });
      hasAutoConnectedRef.current = false;
      autoConnectIdsRef.current = [];
      autoConnectRetriesRef.current.clear();
      
      // Delay to allow Android/iOS Bluetooth stack to fully transition from suspended to active
      // before blasting a high-power burst scan.
      if (retriggerTimerRef.current) clearTimeout(retriggerTimerRef.current);
      retriggerTimerRef.current = setTimeout(() => {
        syncCloudAndAutoConnectRef.current(true).catch((e: unknown) => {
          AppLogger.error('[AutoConnect] retrigger syncCloudAndAutoConnect failed', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
        });
      }, 1500);
    },
  };
}

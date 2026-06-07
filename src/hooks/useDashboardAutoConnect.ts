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
import React, { useEffect, useRef } from 'react';
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
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const syncCloudAndAutoConnectRef = useRef<(isRetrigger?: boolean) => Promise<void>>(async () => {});
  const lastRetriggerRef = useRef<number>(0);

  // ── Continuous observer: connect queued devices as they appear in scan ───
  // Debounce: batch devices that appear within 500ms into a single connectToDevices call.
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
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
          devices: batch.map(d => d.name ?? d.id),
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
              macs: batch.map(d => d.id),
              error: String(e),
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
                });
                setTimeout(() => {
                  if (!autoConnectIdsRef.current.includes(id)) {
                    autoConnectIdsRef.current = [...autoConnectIdsRef.current, id];
                  }
                }, backoff);
              } else {
                AppLogger.warn('[AutoConnect] Max retries exceeded — abandoning', { deviceId: scrubPII(id), retries });
                autoConnectRetriesRef.current.delete(id);
              }
            }
          })
          .finally(() => {
            if (autoConnectIdsRef.current.length > 0) {
              AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_resume_scan' });
              scanForPeripheralsRef.current({ disableProbing: true });
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

      let groupsToProcess: RegisteredGroup[] = [];
      let isOffline = true;
      let cloudUserId: string | null = null;

      if (supabase) {
        if (sessionRef.current?.user) {
          cloudUserId = sessionRef.current.user.id;

          try {
            await refreshProfile();
          } catch (e) {
            AppLogger.warn('Failed to refresh profile on dashboard load', { error: String(e) });
          }

          let groups: RegisteredGroup[] | null = null;
          try {
            const result = await supabase
              .from('registered_groups')
              .select('*')
              .eq('user_id', cloudUserId);
            groups = result.data;
            isOffline = !!result.error;
          } catch (e) {
            AppLogger.warn('Failed to query registered groups from Supabase, entering offline mode', e);
            isOffline = true;
          }

          if (!isOffline && groups && groups.length > 0) {
            groupsToProcess = groups;
          }
        }
      }

      if (isOffline || groupsToProcess.length === 0) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_offline_fallback' });
        
        const processLocalDevices = (devicesArray: RegisteredDevice[]) => {
          groupsToProcess = buildOfflineGroupMap(devicesArray);
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

          let presentGroups: RegisteredGroup[] = [];
          let idsToConnect: string[] = [];

          if (!isOffline && supabase && cloudUserId) {
            const { data: devices } = await supabase
              .from('registered_devices')
              .select('*')
              .eq('user_id', cloudUserId);
            if (devices) {
              presentGroups = groupsToProcess.sort(
                (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
              );
              // Aggregate ALL device MACs across ALL groups (deduplicated) — RC-07
              const allGroupIds = new Set(presentGroups.map(g => g.id));
              const macSet = new Set<string>();
              for (const d of devices as RegisteredDeviceRow[]) {
                const dGroupIds: string[] = d.group_ids || (d.group_id ? [d.group_id] : []);
                if (dGroupIds.some(gId => allGroupIds.has(gId))) {
                  macSet.add(d.device_mac || d.id);
                }
              }
              idsToConnect = Array.from(macSet);
            }
          } else {
            presentGroups = groupsToProcess.sort(
              (a, b) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime()
            );
            // Aggregate ALL device MACs across ALL offline groups (deduplicated) — RC-07
            const macSet = new Set<string>();
            for (const group of presentGroups) {
              for (const mac of group.deviceIds ?? []) {
                macSet.add(mac);
              }
            }
            idsToConnect = Array.from(macSet);
          }

          if (idsToConnect.length > 0) {
            AppLogger.log('BLE_STATE_CHANGE', {
              event: 'auto_connect_queued',
              fleets: presentGroups.map(g => g.group_name),
              groupCount: presentGroups.length,
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
              const scanResult = burstScan(8000);
              if (scanResult && typeof (scanResult as Promise<void>).catch === 'function') {
                (scanResult as Promise<void>).catch((e: unknown) => {
                  AppLogger.warn('Auto-connect burst scan failed', e instanceof Error ? e.message : String(e));
                });
              }
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
    // FIX: Allow DashboardScreen to re-trigger auto-connect after Setup Wizard completes or AppState wakes up.
    // Includes a 5-second throttle and a 1.5-second OS BLE stack wakeup delay.
    retriggerAutoConnect: () => {
      const now = Date.now();
      if (lastRetriggerRef.current && now - lastRetriggerRef.current < 5000) {
        return; // Throttle 5 seconds to prevent spamming burst scans on rapid minimize/maximize
      }
      lastRetriggerRef.current = now;

      AppLogger.log('BLE_STATE_CHANGE', { event: 'auto_connect_retriggered' });
      hasAutoConnectedRef.current = false;
      autoConnectIdsRef.current = [];
      autoConnectRetriesRef.current.clear();
      
      // Delay to allow Android/iOS Bluetooth stack to fully transition from suspended to active
      // before blasting a high-power burst scan.
      setTimeout(() => {
        syncCloudAndAutoConnectRef.current(true);
      }, 1500);
    },
  };
}

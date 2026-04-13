/**
 * useHardwareNotifications.ts — BLE Hardware Data Callback Orchestrator
 *
 * Owns both BLE data-received and hardware-probed callback registrations:
 *
 *  1. `setOnDataReceived` — fires on every BLE RX notification from a connected device:
 *     - Stamps raw hex into `lastRawNotification` (feeds the Sniffer UI in DiagnosticLab)
 *     - Fire-and-forget Supabase diagnostics upload
 *     - Parses v2/v1 hardware config from the payload
 *     - Merges config into `allDevices` state + `deviceConfigs` state + AsyncStorage
 *
 *  2. `setOnHardwareProbed` — fires once per device after a scan probe response:
 *     - Merges parsed hardware config into `deviceConfigs` + AsyncStorage
 *     - Updates `allDevices` so the device list shows hardware info before connecting
 *
 * BLE callback registration primitives (`setOnDataReceived`, `setOnHardwareProbed`)
 * are passed in as parameters — they remain co-located in DashboardScreen per the
 * "Stability-First BLE Lifecycle" constraint from the Master Reference.
 *
 * Extracted from DashboardScreen.tsx (Phase 5 — God Object Surgery).
 */
import { useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/AppLogger';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';

// ─── Types ────────────────────────────────────────────────────────────────────

/** Minimal device shape — just enough for the notification callback to resolve name. */
interface BLEDeviceMinimal {
  id: string;
  name?: string | null;
}

interface UseHardwareNotificationsOptions {
  /** Registers the BLE data-received callback. From useBLE(). */
  setOnDataReceived: (cb: (deviceId: string, payload: number[]) => void) => void;
  /** Registers the scan-probe-completed callback. From useBLE(). */
  setOnHardwareProbed: (cb: (deviceId: string, cfg: any) => void) => void;
  /** BLE scanned device list — used to resolve device names for diagnostics. */
  allDevices: BLEDeviceMinimal[];
  /** State updater for allDevices — merges parsed hardware config into BLE list. */
  setAllDevices: (updater: (prev: any[]) => any[]) => void;
  /** State updater for deviceConfigs — persists hw settings per device. */
  setDeviceConfigs: (updater: (prev: Record<string, any>) => Record<string, any>) => void;
  /** State setter for the raw BLE payload — feeds the Sniffer UI. */
  setLastRawNotification: (val: { deviceId: string; payloadHex: string } | null) => void;
}

// ─── Hook ─────────────────────────────────────────────────────────────────────

/**
 * Registers both BLE hardware callbacks once per mount.
 * Returns nothing — all managed via side-effects internally.
 */
export function useHardwareNotifications({
  setOnDataReceived,
  setOnHardwareProbed,
  allDevices,
  setAllDevices,
  setDeviceConfigs,
  setLastRawNotification,
}: UseHardwareNotificationsOptions): void {

  // ── 1. BLE data-received: sniffer stamp + diagnostics upload + config merge ─
  useEffect(() => {
    setOnDataReceived((deviceId: string, payload: number[]) => {
      const payloadHex = payload
        .map(b => b.toString(16).padStart(2, '0').toUpperCase())
        .join(' ');
      // Feed raw hex to the Sniffer UI
      setLastRawNotification({ deviceId, payloadHex });

      // ── Fire-and-forget diagnostics upload ──────────────────────────────────
      // Never blocks the UI or BLE pipeline.
      const v2ConfigForUpload = ZenggeProtocol.parseHardwareSettingsResponse(payload);
      const v1ConfigForUpload = ZenggeProtocol.parseHardwareConfig(payload);
      const parsedOk = !!(v2ConfigForUpload || v1ConfigForUpload);
      const pts      = v2ConfigForUpload?.ledPoints ?? v1ConfigForUpload?.points;
      const ict      = v2ConfigForUpload?.icType;
      const icn      = v2ConfigForUpload?.icName ?? v1ConfigForUpload?.stripType;
      const cs       = v2ConfigForUpload?.colorSorting ?? undefined;
      const co       = v2ConfigForUpload?.colorSortingName ?? v1ConfigForUpload?.sorting;
      const deviceName = allDevices.find((d: BLEDeviceMinimal) => d.id === deviceId)?.name ?? null;

      supabase.from('device_diagnostics').insert({
        device_id:     deviceId,
        device_name:   deviceName,
        payload_hex:   payloadHex,
        payload_bytes: payload.length,
        byte_0:        payload[0] ?? null,
        byte_2:        payload[2] ?? null,
        parsed_ok:     parsedOk,
        points:        pts ?? null,
        ic_type:       ict ?? null,
        ic_name:       icn ?? null,
        color_sorting: cs ?? null,
        color_order:   co ?? null,
      }).then(({ error }: any) => {
        if (error) AppLogger.warn('[Diagnostics] upload failed:', error.message);
      });
      // ────────────────────────────────────────────────────────────────────────

      // ── Parse hardware config from the notification payload ──────────────────
      const v2Config = ZenggeProtocol.parseHardwareSettingsResponse(payload);
      const v1Config = ZenggeProtocol.parseHardwareConfig(payload);

      let configPoints: number | undefined;
      let configSegments: number | undefined;
      let configStripType: string | undefined;
      let configSorting: string | undefined;
      let configSortingIdx: number | undefined;
      let configIcType: number | undefined;

      if (v2Config) {
        configPoints     = v2Config.ledPoints;
        configSegments   = v2Config.segments;
        configStripType  = v2Config.icName;
        configSorting    = v2Config.colorSortingName;
        configSortingIdx = v2Config.colorSorting;   // numeric index — critical for hwSettings
        configIcType     = v2Config.icType;
      } else if (v1Config) {
        configPoints    = v1Config.points;
        configSegments  = v1Config.segments;
        configStripType = v1Config.stripType;
        configSorting   = v1Config.sorting;
        // Derive numeric index from string for v1 (defaults to GRB=2 if unknown)
        configSortingIdx = ['RGB', 'RBG', 'GRB', 'GBR', 'BRG', 'BGR'].indexOf(configSorting ?? 'GRB');
        if (configSortingIdx < 0) configSortingIdx = 2;
      }

      if (configPoints !== undefined && configSorting !== undefined) {
        // Prevent telemetry flooding — only update on actual hardware config packets
        setAllDevices(prev => prev.map(d => {
          if (d.id !== deviceId) return d;
          const newD = {
            ...d,
            points:           configPoints,
            sorting:          configSorting,
            colorSorting:     configSortingIdx,  // numeric index propagated
            colorSortingName: configSorting,
            stripType:        configStripType,
            icType:           configIcType,
            segments:         configSegments,
            detected:         true,              // flag that this came from real hardware
          } as any as typeof d;

          // Mirror securely to persistent memory
          AsyncStorage.getItem('ng_device_configs').then(str => {
            const p = JSON.parse(str || '{}');
            p[deviceId] = { ...p[deviceId], ...newD };
            AsyncStorage.setItem('ng_device_configs', JSON.stringify(p));
          }).catch(() => {});

          setDeviceConfigs(prevConfigs => ({
            ...prevConfigs,
            [deviceId]: { ...(prevConfigs[deviceId] || {}), ...newD },
          }));

          return newD;
        }));
      }
    });
  }, [setOnDataReceived, setAllDevices]);

  // ── 2. Hardware probe callback: merge scanned config before first connect ───
  useEffect(() => {
    setOnHardwareProbed((deviceId: string, cfg: any) => {
      setDeviceConfigs(prev => {
        const merged = { ...(prev[deviceId] || {}), ...cfg };
        const next = { ...prev, [deviceId]: merged };
        // Persist immediately so the next app launch has the config ready
        AsyncStorage.setItem('ng_device_configs', JSON.stringify(next)).catch(() => {});
        return next;
      });
      // Update allDevices so the list shows detected config before user connects
      setAllDevices(prev => prev.map(d =>
        (d as any).id === deviceId
          ? {
              ...d,
              points:      cfg.ledPoints,
              segments:    cfg.segments,
              stripType:   cfg.icName,
              sorting:     cfg.colorSortingName,
              colorSorting: cfg.colorSorting,
              icType:      cfg.icType,
              detected:    true,
            } as any
          : d
      ));
    });
  }, [setOnHardwareProbed]);
}

/**
 * useHardwareNotifications.ts — BLE Hardware Data Callback Orchestrator
 * (Refactored to Mailroom Architecture)
 *
 * Owns both BLE data-received and hardware-probed callback registrations.
 * Gated and throttled:
 * 1. Debounces duplicate packets.
 * 2. Diagnostics (Sniffer + Supabase) are locked behind isDiagnosticsMode.
 * 3. Uses pure stateless parser (BlePayloadParser).
 * 4. Checks Delta before mutating State/AsyncStorage.
 *
 * fix/hw-notifications-ssot-bypass: All config persistence is now routed
 * through DeviceRepository (the designated SSOT for @Sk8lytz_device_configs).
 * No direct AsyncStorage writes remain in this hook.
 */
import { useEffect, useRef } from 'react';
import { AppLogger } from '../services/AppLogger';
import DeviceRepository from '../services/DeviceRepository';
import { BlePayloadParser } from '../utils/BlePayloadParser';

// ─── Types ────────────────────────────────────────────────────────────────────

/** Minimal device shape — just enough for the notification callback to resolve name. */
interface BLEDeviceMinimal {
  id: string;
  name?: string | null;
}

interface UseHardwareNotificationsOptions {
  /** Enables heavy diagnostic features (Sniffer UI logging, Supabase upload) */
  isDiagnosticsMode?: boolean;
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
  /** Current device configurations for delta checks */
  deviceConfigs: Record<string, any>;
  /** State setter for the raw BLE payload — feeds the Sniffer UI. */
  setLastRawNotification: (val: { deviceId: string; payloadHex: string } | null) => void;
}

// ─── Hook ─────────────────────────────────────────────────────────────────────

export function useHardwareNotifications({
  isDiagnosticsMode = false,
  setOnDataReceived,
  setOnHardwareProbed,
  allDevices,
  setAllDevices,
  setDeviceConfigs,
  deviceConfigs,
  setLastRawNotification,
}: UseHardwareNotificationsOptions): void {

  // Singleton repo — canonical write path for @Sk8lytz_device_configs
  const repo = DeviceRepository.getInstance();

  // Maintain refs to prevent dependency cycles in useEffect closures
  const deviceConfigsRef = useRef(deviceConfigs);
  useEffect(() => {
    deviceConfigsRef.current = deviceConfigs;
  }, [deviceConfigs]);

  const allDevicesRef = useRef(allDevices);
  useEffect(() => {
    allDevicesRef.current = allDevices;
  }, [allDevices]);

  // Maintain a throttle cache of the last packet seen to drop duplicates
  const lastPacketCacheRef = useRef<Record<string, string>>({});

  // ── 1. BLE data-received: Mailroom Handler ──────────────────────────────────
  useEffect(() => {
    setOnDataReceived((deviceId: string, payload: number[]) => {
      const payloadHex = payload
        .map(b => b.toString(16).padStart(2, '0').toUpperCase())
        .join(' ');
        
      // ── [Gatekeeper] Throttle identical back-to-back packets (Debouncer) ────
      if (lastPacketCacheRef.current[deviceId] === payloadHex) {
        return; // Drop identical repetitive packets to save UI thread
      }
      lastPacketCacheRef.current[deviceId] = payloadHex;

      // ── [Diag-Gate] Diagnostics upload & Sniffer UI update ─────────────────
      if (isDiagnosticsMode) {
        setLastRawNotification({ deviceId, payloadHex });
        
        // Pure parser call
        const parsed = BlePayloadParser.parseLedPayload(payload);
        
        AppLogger.log('RAW_PAYLOAD', {
           dir: 'RX',
           deviceId: deviceId,
           hex: payloadHex,
           bytes: payload.length,
           parsedOk: parsed?.parsedOk ?? false,
           diagnostics: {
               byte_0: payload[0] ?? null,
               byte_2: payload[2] ?? null,
               ...parsed?.rawUploadData
           }
        });
      }

      // ── [RF Remote Config] ────────────────────────────────────────────────
      const rfConfig = BlePayloadParser.parseRfPayload(payload);
      if (rfConfig && rfConfig.parsedOk) {
        const rfPatch = {
          rfMode: rfConfig.rfMode,
          rfRemotes: rfConfig.rfRemotes,
        };
        // Update React state
        setDeviceConfigs(prevConfigs => ({
          ...prevConfigs,
          [deviceId]: { ...(prevConfigs[deviceId] || {}), ...rfPatch },
        }));
        // Persist via SSOT — DeviceRepository owns @Sk8lytz_device_configs
        repo.updateConfig(deviceId, rfPatch).catch((e: unknown) =>
          AppLogger.warn('[HWNotif] RF config repo write failed', { deviceId, error: String(e) })
        );
        return; // Handled the RF packet, exit mailroom
      }

      // ── [Pure Utility] Parse hardware config ────────────────────────────────
      const ledConfig = BlePayloadParser.parseLedPayload(payload);
      if (!ledConfig || !ledConfig.parsedOk || ledConfig.points === undefined || ledConfig.sorting === undefined) {
        return;
      }

      // ── [Delta Check] Only update state/disk if data fundamentally changed ──
      const existingCfg = deviceConfigsRef.current[deviceId] || {};
      const isDirty = 
        existingCfg.points !== ledConfig.points || 
        existingCfg.colorSorting !== ledConfig.colorSortingIdx ||
        existingCfg.segments !== ledConfig.segments ||
        existingCfg.detected !== true; // Ensure fallback 'detected' flag triggers 1st write

      if (!isDirty) return; // Deduplicated — prevents 5+ disk writes per connect!
      
      // Update state and persist via SSOT
      setAllDevices((prev: any[]) => prev.map(d => {
        if (d.id !== deviceId) return d;
        const newD = {
          ...d,
          points:           ledConfig.points,
          sorting:          ledConfig.sorting,
          colorSorting:     ledConfig.colorSortingIdx,
          colorSortingName: ledConfig.sorting,
          stripType:        ledConfig.stripType,
          icType:           ledConfig.icType,
          segments:         ledConfig.segments,
          detected:         true,
        };

        // Mirror securely to persistent memory via SSOT (replaces direct AsyncStorage write)
        repo.updateConfig(deviceId, newD).catch((e: unknown) =>
          AppLogger.warn('[HWNotif] LED config repo write failed', { deviceId, error: String(e) })
        );

        setDeviceConfigs(prevConfigs => ({
          ...prevConfigs,
          [deviceId]: { ...(prevConfigs[deviceId] || {}), ...newD },
        }));

        return newD;
      }));
    });
  }, [setOnDataReceived, setAllDevices, setDeviceConfigs, isDiagnosticsMode, repo]);

  // ── 2. Hardware probe callback: merge scanned config before first connect ───
  useEffect(() => {
    setOnHardwareProbed((deviceId: string, cfg: any) => {
      setDeviceConfigs(prev => {
        const merged = { ...(prev[deviceId] || {}), ...cfg };
        const next = { ...prev, [deviceId]: merged };
        // Persist entire configs map via SSOT (replaces direct AsyncStorage write)
        repo.setConfigs(next).catch((e: unknown) =>
          AppLogger.warn('[HWNotif] Hardware probe repo write failed', { deviceId, error: String(e) })
        );
        return next;
      });
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
  }, [setOnHardwareProbed, setDeviceConfigs, setAllDevices, repo]);
}

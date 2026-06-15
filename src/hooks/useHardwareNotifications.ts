/**
 * useHardwareNotifications.ts — BLE Hardware Data Callback Orchestrator
 * (Refactored to Mailroom Architecture)
 *
 * Owns both BLE data-received and hardware-probed callback registrations.
 * Gated and throttled:
 * 1. Debounces duplicate packets.
 * 2. Diagnostics (Sniffer + Supabase) are locked behind isDiagnosticsMode.
 * 3. Uses pure stateless parser (BlePayloadParser).
 * 4. Checks Delta before mutating state — all persistence via DeviceRepository SSOT.
 */
import { useEffect, useRef } from 'react';
import { AppLogger } from '../services/appLogger';
import DeviceRepository from '../services/DeviceRepository';
import { BlePayloadParser } from '../utils/BlePayloadParser';
import { scrubPII } from '../utils/piiScrubber';

// ─── Types ────────────────────────────────────────────────────────────────────

/** Minimal device shape — just enough for the notification callback to resolve name. */
export interface BLEDeviceMinimal {
  id: string;
  name?: string | null;
  points?: number;
  ledPoints?: number;
  sorting?: string;
  colorSorting?: number;
  colorSortingName?: string;
  stripType?: string;
  icType?: number;
  segments?: number;
  detected?: boolean;
  firmware?: string;
}

export interface ProbedHardwareConfig {
  ledPoints?: number;
  segments?: number;
  icName?: string;
  colorSortingName?: string;
  colorSorting?: number;
  icType?: number;
  [key: string]: unknown;
}

interface UseHardwareNotificationsOptions {
  /** Enables heavy diagnostic features (Sniffer UI logging, Supabase upload) */
  isDiagnosticsMode?: boolean;
  /** Registers the BLE data-received callback. From useBLE(). */
  setOnDataReceived: (cb: (deviceId: string, payload: number[]) => void) => void;
  /** Registers the scan-probe-completed callback. From useBLE(). */
  setOnHardwareProbed: (cb: (deviceId: string, cfg: ProbedHardwareConfig) => void) => void;
  /** BLE scanned device list — used to resolve device names for diagnostics. */
  allDevices: BLEDeviceMinimal[];
  /** State updater for allDevices — merges parsed hardware config into BLE list. */
  setAllDevices: (updater: (prev: BLEDeviceMinimal[]) => BLEDeviceMinimal[]) => void;
  /** State updater for deviceConfigs — persists hw settings per device. */
  setDeviceConfigs: (updater: (prev: Record<string, Record<string, unknown>>) => Record<string, Record<string, unknown>>) => void;
  /** Current device configurations for delta checks */
  deviceConfigs: Record<string, Record<string, unknown>>;
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
  
  // Synchronous cache for led config writes to prevent re-entrancy
  const syncConfigCacheRef = useRef<Record<string, Record<string, unknown>>>({});

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
           deviceId: scrubPII(deviceId),
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
        setDeviceConfigs(prevConfigs => {
          const updated = {
            ...(prevConfigs[deviceId] || {}),
            rfMode: rfConfig.rfMode,
            rfRemotes: rfConfig.rfRemotes
          };
          // Persist RF config update via DeviceRepository SSOT (tombstone-safe merge)
          DeviceRepository.getInstance().updateConfig(deviceId, { rfMode: rfConfig.rfMode, rfRemotes: rfConfig.rfRemotes }).catch(e => AppLogger.warn('Failed to persist RF config', { error: e instanceof Error ? e.message : String(e), payload_size: payload.length, ssi: 0 }));
          return { ...prevConfigs, [deviceId]: updated };
        });
        // BUG-03 Fix: Removed early return. Compound notifications contain both RF and LED configs.
        // Continuing execution allows the payload to be evaluated by parseLedPayload.
      }

      // ── [Pure Utility] Parse hardware config ────────────────────────────────
      const ledConfig = BlePayloadParser.parseLedPayload(payload);
      if (!ledConfig || !ledConfig.parsedOk || ledConfig.points === undefined || ledConfig.sorting === undefined) {
        return;
      }

      // EEPROM parsing was successful — lock the product identification
      DeviceRepository.getInstance().confirmProductId(deviceId).catch(e => AppLogger.warn('Confirm failed', { error: e instanceof Error ? e.message : String(e), payload_size: payload.length, ssi: 0 }));

      // ── [Delta Check] Only update state/disk if data fundamentally changed ──
      const existingCfg = syncConfigCacheRef.current[deviceId] || deviceConfigsRef.current[deviceId] || {};
      const isDirty = 
        existingCfg.points !== ledConfig.points || 
        existingCfg.colorSorting !== ledConfig.colorSortingIdx ||
        existingCfg.segments !== ledConfig.segments ||
        existingCfg.detected !== true; // Ensure fallback 'detected' flag triggers 1st write

      if (!isDirty) return; // Deduplicated — prevents 5+ disk writes per connect!
      
      // Update synchronous cache immediately to prevent re-entrancy
      syncConfigCacheRef.current[deviceId] = {
        ...existingCfg,
        points: ledConfig.points,
        colorSorting: ledConfig.colorSortingIdx,
        segments: ledConfig.segments,
        detected: true,
      };
      
      // Update state — mirror all hardware fields into allDevices so DeviceSettingsModal
      // receives a fully-populated initialSettings object without any extra queries.
      setAllDevices((prev: BLEDeviceMinimal[]) => prev.map(d => {
        if (d.id !== deviceId) return d;
        const newD = {
          ...d,
          name:             d.name ?? undefined,
          points:           ledConfig.points,
          ledPoints:        ledConfig.points,  // Mirror for activeHwSettings (checks ledPoints first)
          sorting:          ledConfig.sorting,
          colorSorting:     ledConfig.colorSortingIdx,
          colorSortingName: ledConfig.sorting,
          stripType:        ledConfig.stripType,
          icType:           ledConfig.icType,
          segments:         ledConfig.segments,
          detected:         true,
          // Carry firmware forward from BLE scan advertisement data if already present.
          // parseLedPayload does not contain firmware — it lives on the raw Device object
          // from the scan phase. We preserve it here to avoid overwriting with undefined.
          firmware:         d.firmware,
        };

        // Mirror securely to persistent memory via DeviceRepository SSOT
        DeviceRepository.getInstance().updateConfig(deviceId, newD).catch(e => AppLogger.warn('Failed to persist LED config', { error: e instanceof Error ? e.message : String(e), payload_size: payload.length, ssi: 0 }));

        setDeviceConfigs(prevConfigs => ({
          ...prevConfigs,
          [deviceId]: { ...(prevConfigs[deviceId] || {}), ...newD },
        }));

        return newD;
      }));
    });

    return () => {
      setOnDataReceived(() => {});
    };
  }, [setOnDataReceived, setAllDevices, setDeviceConfigs, isDiagnosticsMode, setLastRawNotification]);

  // ── 2. Hardware probe callback: merge scanned config before first connect ───
  useEffect(() => {
    setOnHardwareProbed((deviceId: string, cfg: ProbedHardwareConfig) => {
      setDeviceConfigs(prev => {
        const merged = { ...(prev[deviceId] || {}), ...cfg };
        const next = { ...prev, [deviceId]: merged };
        // Persist probe config via DeviceRepository SSOT (updateConfig merges internally)
        DeviceRepository.getInstance().updateConfig(deviceId, cfg).catch(e => AppLogger.warn('Failed to persist probed config', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 }));
        DeviceRepository.getInstance().confirmProductId(deviceId).catch(e => AppLogger.warn('Confirm failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 }));
        return next;
      });
      setAllDevices(prev => prev.map(d =>
        d.id === deviceId
          ? {
              ...d,
              points:      cfg.ledPoints,
              ledPoints:   cfg.ledPoints,  // Mirror for activeHwSettings (checks ledPoints first)
              segments:    cfg.segments,
              stripType:   cfg.icName,
              sorting:     cfg.colorSortingName,
              colorSorting: cfg.colorSorting,
              icType:      cfg.icType,
              detected:    true,
            }
          : d
      ));
    });

    return () => {
      setOnHardwareProbed(() => {});
    };
  }, [setOnHardwareProbed, setDeviceConfigs, setAllDevices]);
}

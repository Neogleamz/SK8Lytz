/**
 * InterrogatorService.ts — Pure BLE hardware interrogation logic.
 *
 * Extracted from useBLEInterrogator hook. Contains the core probe execution,
 * queue management, and AsyncStorage caching — no React hooks.
 * Consumed by useBLEInterrogator (thin wrapper) and testable standalone.
 *
 * Responsibilities:
 *   - Open a GATT session, fire HW query (0x63) + RF remote state query
 *   - Parse response notifications into a PingResult
 *   - Persist result to AsyncStorage keyed by MAC
 *   - Manage a debounced probe queue to prevent GATT saturation
 */
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import type { BleManager, BleError, Characteristic } from 'react-native-ble-plx';
import { AppLogger } from '../AppLogger';
import { createGattSession } from '../BleSessionFactory';
import { enqueueWrite, enqueueDelay } from '../BleWriteQueue';
import { type PingResult, isPingResult } from '../../types/dashboard.types';
import { scrubPII } from '../../utils/piiScrubber';

const HW_CACHE_KEY = (mac: string) => `@sk8_hw_${mac.toUpperCase()}`;
const PROBE_TIMEOUT_MS = 3500;
export const PROBE_QUEUE_DELAY_MS = 2000;
export const PROBE_QUEUE_DELAY_MS_FTUE = 500;
export const BLE_INTERROGATION_STAGGER_MS = 500;

/**
 * loadHWCacheFromStorage — Load all persisted hardware profiles on startup.
 * Returns a map of MAC → PingResult from AsyncStorage.
 */
export async function loadHWCacheFromStorage(): Promise<Record<string, PingResult>> {
  const result: Record<string, PingResult> = {};
  try {
    const keys = await AsyncStorage.getAllKeys();
    const hwKeys = keys.filter(k => k.startsWith('@sk8_hw_'));
    if (hwKeys.length === 0) return result;
    const pairs = await AsyncStorage.multiGet(hwKeys);
    for (const [key, val] of pairs) {
      if (!val) continue;
      const mac = key.replace('@sk8_hw_', '').toUpperCase();
      try {
        result[mac] = JSON.parse(val);
      } catch (e: unknown) {
        AppLogger.warn('[InterrogatorService] Malformed HW cache entry', { mac: '[REDACTED]', error: e instanceof Error ? e.message : String(e) });
      }
    }
  } catch (e: unknown) {
    AppLogger.warn('[InterrogatorService] Failed to load HW cache from storage', { error: String(e) });
  }
  return result;
}

/**
 * interrogateDevice — Perform a full GATT hardware probe on a single MAC.
 *
 * Opens a short-lived GATT session, fires 0x63 HW query + RF remote state query,
 * waits up to PROBE_TIMEOUT_MS for notification response, and caches the result.
 *
 * Returns the PingResult if successful, null otherwise. Never throws.
 */
export async function interrogateDevice(
  mac: string,
  bleManager: BleManager,
  probingMacsRef: { current: Set<string> },
  hwCacheRef: { current: Record<string, PingResult> },
  onDeviceInterrogated: () => void,
): Promise<PingResult | null> {
  if (Platform.OS === 'web' || !bleManager) return null;
  if (probingMacsRef.current.has(mac)) return null;
  if (hwCacheRef.current[mac]) return null;

  probingMacsRef.current.add(mac);
  AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_start', mac: scrubPII(mac) });

  try {
    const { adapter: interrogatorAdapter } = await createGattSession(bleManager, mac, {
      timeout: 6000,
      retries: 2,
      context: 'interrogateDevice',
    });

    const hwConfig = await new Promise<PingResult | null>(resolve => {
      let accumulated: Partial<PingResult> | null = null;
      const timer = setTimeout(() => {
        sub.remove();
        resolve(isPingResult(accumulated) ? accumulated : null);
      }, PROBE_TIMEOUT_MS);

      const sub = bleManager.monitorCharacteristicForDevice(
        mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.notifyCharacteristicUUID,
        (err: BleError | null, char: Characteristic | null) => {
          if (err || !char?.value) return;
          try {
            const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
            const hwParsed = interrogatorAdapter.parseSettingsResponse(raw);
            if (hwParsed) accumulated = { ...accumulated, ...hwParsed };
            const rfParsed = interrogatorAdapter.parseRfRemoteState(raw);
            if (rfParsed) accumulated = { ...accumulated, rfMode: rfParsed.mode, rfPairedCount: rfParsed.pairedCount };
            if (accumulated?.detected && accumulated?.rfMode) {
              clearTimeout(timer);
              sub.remove();
              if (isPingResult(accumulated)) resolve(accumulated);
            }
          } catch (e: unknown) {
            AppLogger.warn('[InterrogatorService] Protocol parse failed', { mac: scrubPII(mac), error: e instanceof Error ? e.message : String(e) });
          }
        }
      );

      enqueueDelay('normal', 400);

      const hwQuery = interrogatorAdapter.buildQuerySettings(false);
      if (hwQuery.packets.length > 0) {
        const b64HW = Buffer.from(hwQuery.packets[0]).toString('base64');
        enqueueWrite('normal', async () => {
          await bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64HW
          );
          return true;
        }).catch((e: unknown) => AppLogger.warn('[InterrogatorService] HW query write failed', { error: String(e) }));
      }

      enqueueDelay('normal', 200);

      const rfQuery = interrogatorAdapter.buildQueryRfRemoteState();
      if (rfQuery.packets.length > 0) {
        const b64RF = Buffer.from(rfQuery.packets[0]).toString('base64');
        enqueueWrite('normal', async () => {
          await bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64RF
          );
          return true;
        }).catch((e: unknown) => AppLogger.warn('[InterrogatorService] RF query write failed', { error: String(e) }));
      }
    });

    if (hwConfig) {
      await AsyncStorage.setItem(HW_CACHE_KEY(mac), JSON.stringify(hwConfig))
        .catch(e => AppLogger.warn('[InterrogatorService] Failed to persist HW cache', { error: String(e) }));
      hwCacheRef.current[mac] = hwConfig;
      AppLogger.log('DEVICE_DISCOVERED', { event: 'interrogator_complete', mac: scrubPII(mac), ledPoints: hwConfig.ledPoints });
      onDeviceInterrogated();
    }

    return hwConfig;
  } catch (err: unknown) {
    AppLogger.warn(`[InterrogatorService] Probe failed for ${scrubPII(mac)}`, { error: String(err) });
    return null;
  } finally {
    probingMacsRef.current.delete(mac);
    await bleManager.cancelDeviceConnection(mac)
      .catch((e: unknown) => AppLogger.warn('[InterrogatorService] Disconnect after probe failed', { error: String(e) }));
  }
}

/**
 * createProbeQueue — Returns a queue manager for serializing MAC probes.
 *
 * Debounces probe execution to prevent flooding GATT during rapid scan.
 * FTUE mode (no registered MACs) uses a shorter delay for faster first-use setup.
 */
export function createProbeQueue(params: {
  bleManager: BleManager;
  probingMacsRef: { current: Set<string> };
  hwCacheRef: { current: Record<string, PingResult> };
  getRegisteredMacsCount: () => number;
  onDeviceInterrogated: () => void;
}) {
  const probeQueueRef: { current: string[] } = { current: [] };
  const probeQueueTimerRef: { current: ReturnType<typeof setTimeout> | null } = { current: null };
  const isProcessingRef = { current: false };

  function processQueue() {
    if (isProcessingRef.current) return;
    if (probeQueueTimerRef.current) clearTimeout(probeQueueTimerRef.current);
    const delay = params.getRegisteredMacsCount() === 0 ? PROBE_QUEUE_DELAY_MS_FTUE : PROBE_QUEUE_DELAY_MS;
    probeQueueTimerRef.current = setTimeout(async () => {
      isProcessingRef.current = true;
      try {
        while (probeQueueRef.current.length > 0) {
          const mac = probeQueueRef.current.shift()!;
          await interrogateDevice(mac, params.bleManager, params.probingMacsRef, params.hwCacheRef, params.onDeviceInterrogated);
          await new Promise(r => setTimeout(r, BLE_INTERROGATION_STAGGER_MS));
        }
      } finally {
        isProcessingRef.current = false;
      }
    }, delay);
  }

  function queueDeviceForInterrogation(mac: string) {
    if (
      !params.hwCacheRef.current[mac] &&
      !params.probingMacsRef.current.has(mac) &&
      !probeQueueRef.current.includes(mac)
    ) {
      probeQueueRef.current.push(mac);
      processQueue();
    }
  }

  function cleanup() {
    if (probeQueueTimerRef.current) clearTimeout(probeQueueTimerRef.current);
  }

  return { queueDeviceForInterrogation, cleanup };
}

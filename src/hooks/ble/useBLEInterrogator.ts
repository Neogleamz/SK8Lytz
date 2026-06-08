import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import type { BleManager, BleError, Characteristic } from 'react-native-ble-plx';
import { AppLogger } from '../../services/AppLogger';
import { createGattSession } from '../../services/BleSessionFactory';
import { acquireGattLock } from './useBLEGattMutex';
import { enqueueWrite } from '../../services/BleWriteQueue';
import { type PingResult, isPingResult } from '../../types/dashboard.types';

const HW_CACHE_KEY = (mac: string) => `@sk8_hw_${mac.toUpperCase()}`;
const PROBE_TIMEOUT_MS = 3500;
const PROBE_QUEUE_DELAY_MS = 2000;
const PROBE_QUEUE_DELAY_MS_FTUE = 500;

export interface UseBLEInterrogatorProps {
  bleManager: BleManager | null;
  registeredMacs: string[];
  onDeviceInterrogated: () => void;
}

export function useBLEInterrogator({ bleManager, registeredMacs, onDeviceInterrogated }: UseBLEInterrogatorProps) {
  const [hwCache, setHwCache] = useState<Record<string, PingResult>>({});
  const hwCacheRef = useRef<Record<string, PingResult>>({});

  const probingMacsRef = useRef<Set<string>>(new Set());
  const probeQueueRef = useRef<string[]>([]);
  const probeQueueTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    if (Platform.OS === 'web') return;
    AsyncStorage.getAllKeys().then(keys => {
      const hwKeys = keys.filter(k => k.startsWith('@sk8_hw_'));
      if (hwKeys.length === 0) return;
      AsyncStorage.multiGet(hwKeys).then(pairs => {
        const loaded: Record<string, any> = {};
        for (const [key, val] of pairs) {
          if (!val) continue;
          const mac = key.replace('@sk8_hw_', '').toUpperCase();
          try {
            loaded[mac] = JSON.parse(val);
          } catch (e: unknown) {
      const safeErr = e instanceof Error ? e : new Error(String(e));
            AppLogger.warn('[useBLEInterrogator] Malformed HW cache', { mac, error: e instanceof Error ? e.message : String(e) });
          }
        }
        if (Object.keys(loaded).length > 0) {
          hwCacheRef.current = { ...hwCacheRef.current, ...loaded };
          setHwCache(prev => ({ ...prev, ...loaded }));
          AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_cache_loaded', count: Object.keys(loaded).length });
        }
      });
    }).catch(e => AppLogger.warn('[useBLEInterrogator] Failed to load sweeper hardware cache', { error: String(e) }));
  }, []);

  const interrogateDevice = useCallback(async (mac: string) => {
    if (Platform.OS === 'web' || !bleManager) return;
    if (probingMacsRef.current.has(mac)) return;
    if (hwCacheRef.current[mac]) return;

    if (registeredMacs.map(m => m.toUpperCase()).includes(mac.toUpperCase())) return;

    const lockHandle = await acquireGattLock(3);
    if (!lockHandle) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_yield_p1_lock', mac });
      if (!probeQueueRef.current.includes(mac)) probeQueueRef.current.push(mac);
      return;
    }

    const { release, signal } = lockHandle;
    probingMacsRef.current.add(mac);
    AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_start', mac });

    try {
      if (signal.aborted) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_preempted_pre_connect', mac });
        return;
      }

      const { adapter: interrogatorAdapter } = await createGattSession(bleManager, mac, {
        timeout: 6000,
        retries: 2,
        signal,
        context: 'interrogateDevice',
      });

      if (signal.aborted) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_preempted_post_discover', mac });
        return;
      }

      const hwConfig = await new Promise<PingResult | null>(resolve => {
        let accumulated: Partial<PingResult> | null = null;
        const timer = setTimeout(() => {
          sub.remove();
          resolve(isPingResult(accumulated) ? accumulated : null);
        }, PROBE_TIMEOUT_MS);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.notifyCharacteristicUUID,
          (err: BleError | null, char: Characteristic | null) => {
            if (signal.aborted) {
              clearTimeout(timer);
              sub.remove();
              resolve(null);
              return;
            }
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
      const safeErr = e instanceof Error ? e : new Error(String(e));
              AppLogger.warn('[useBLEInterrogator] Protocol parse failed', { mac, error: e instanceof Error ? e.message : String(e) });
            }
          }
        );

        setTimeout(() => {
          if (signal.aborted) { clearTimeout(timer); sub.remove(); resolve(null); return; }
          const hwQuery = interrogatorAdapter.buildQuerySettings(false);
          if (hwQuery.packets.length > 0) {
            const b64HW = Buffer.from(hwQuery.packets[0]).toString('base64');
            enqueueWrite('normal', async () => {
              await bleManager.writeCharacteristicWithoutResponseForDevice(
                mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64HW
              );
              return true;
            }).catch((e: unknown) => AppLogger.warn('[useBLEInterrogator] HW query failed', { error: String(e) }));
          }
          setTimeout(() => {
            if (signal.aborted) return;
            const rfQuery = interrogatorAdapter.buildQueryRfRemoteState();
            if (rfQuery.packets.length > 0) {
              const b64RF = Buffer.from(rfQuery.packets[0]).toString('base64');
              enqueueWrite('normal', async () => {
                await bleManager.writeCharacteristicWithoutResponseForDevice(
                  mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64RF
                );
                return true;
              }).catch((e: unknown) => AppLogger.warn('[useBLEInterrogator] RF query failed', { error: String(e) }));
            }
          }, 200);
        }, 400);
      });

      if (hwConfig && !signal.aborted) {
        AsyncStorage.setItem(HW_CACHE_KEY(mac), JSON.stringify(hwConfig)).catch(e => AppLogger.warn('[useBLEInterrogator] Failed to cache hw config', { error: String(e) }));
        hwCacheRef.current[mac] = hwConfig;
        setHwCache(prev => ({ ...prev, [mac]: hwConfig }));
        AppLogger.log('DEVICE_DISCOVERED', { event: 'interrogator_complete', mac, ledPoints: hwConfig.ledPoints });
        
        onDeviceInterrogated();
      }
    } catch (err: unknown) {
      const safeErr = err instanceof Error ? err : new Error(String(err));
      if (!signal.aborted) {
        AppLogger.warn(`[Interrogator] Failed for ${mac}`, { error: String(err) });
      }
    } finally {
      probingMacsRef.current.delete(mac);
      release();
      await bleManager.cancelDeviceConnection(mac).catch((e: unknown) => AppLogger.warn('[useBLEInterrogator] Disconnect failed', { error: String(e) }));
    }
  }, [bleManager, registeredMacs, onDeviceInterrogated]);

  const processProbeQueue = useCallback(() => {
    if (probeQueueTimerRef.current) clearTimeout(probeQueueTimerRef.current);
    const delay = registeredMacs.length === 0 ? PROBE_QUEUE_DELAY_MS_FTUE : PROBE_QUEUE_DELAY_MS;
    probeQueueTimerRef.current = setTimeout(async () => {
      while (probeQueueRef.current.length > 0) {
        const mac = probeQueueRef.current.shift()!;
        await interrogateDevice(mac);
        await new Promise(r => setTimeout(r, 500));
      }
    }, delay);
  }, [interrogateDevice, registeredMacs.length]);

  const queueDeviceForInterrogation = useCallback((mac: string) => {
    if (!hwCacheRef.current[mac] && !probingMacsRef.current.has(mac) && !probeQueueRef.current.includes(mac)) {
      probeQueueRef.current.push(mac);
      processProbeQueue();
    }
  }, [processProbeQueue]);

  return { hwCache, hwCacheRef, queueDeviceForInterrogation };
}

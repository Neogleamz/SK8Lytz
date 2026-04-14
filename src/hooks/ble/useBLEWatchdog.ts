import { Buffer } from 'buffer';
import { useEffect, useRef } from 'react';
import { Platform } from 'react-native';
import type { Device, Subscription } from 'react-native-ble-plx';
import { ZENGGE_CHARACTERISTIC_UUID, ZENGGE_NOTIFY_UUID, ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { AppLogger } from '../../services/AppLogger';

export interface UseBLEWatchdogProps {
  bleManager: any;
  connectedDevicesRef: React.MutableRefObject<Device[]>;
  setConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  setDroppedOutDeviceIds: React.Dispatch<React.SetStateAction<string[]>>;
  disconnectListeners: React.MutableRefObject<Record<string, Subscription>>;
  handleNotification: (error: any, characteristic: any, deviceId: string) => void;
}

export function useBLEWatchdog({
  bleManager,
  connectedDevicesRef,
  setConnectedDevices,
  setDroppedOutDeviceIds,
  disconnectListeners,
  handleNotification
}: UseBLEWatchdogProps) {
  const isWatchdogRecovering = useRef<boolean>(false);
  const watchdogMissCountRef = useRef<Record<string, number>>({});
  const watchdogIntervalRef = useRef<ReturnType<typeof setInterval> | null>(null);

  const pingDeviceForLiveness = async (deviceId: string): Promise<boolean> => {
    if (Platform.OS === 'web' || !bleManager) return true;
    try {
      const result = await new Promise<boolean>((resolve) => {
        const timeout = setTimeout(() => { sub.remove(); resolve(false); }, 2500);
        const sub = bleManager.monitorCharacteristicForDevice(
          deviceId, ZENGGE_SERVICE_UUID, ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            if (err || !char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              if (raw[0] === 0x63 || raw[8] === 0x63) {
                clearTimeout(timeout);
                sub.remove();
                resolve(true);
              }
            } catch { } // silently ignore parse errors
          }
        );
        const qp = ZenggeProtocol.queryHardwareSettings(false);
        bleManager.writeCharacteristicWithoutResponseForDevice(
          deviceId, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID,
          Buffer.from(qp).toString('base64')
        ).catch(() => {});
      });
      return result;
    } catch {
      return false;
    }
  };

  const silentRelatch = async (device: Device): Promise<boolean> => {
    try {
      AppLogger.log('WATCHDOG_RELATCH', { deviceId: device.id, name: device.name, action: 'begin' });

      if (disconnectListeners.current[device.id]) {
        disconnectListeners.current[device.id].remove();
        delete disconnectListeners.current[device.id];
      }
      await bleManager.cancelDeviceConnection(device.id).catch(() => {});
      await new Promise(r => setTimeout(r, 250));

      const conn = await bleManager.connectToDevice(device.id, { timeout: 5000 });
      await conn.discoverAllServicesAndCharacteristics();

      disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
        AppLogger.log('DEVICE_DISCONNECTED', { id: conn.id, reason: 'dropout_post_relatch', error: error?.message });
        setDroppedOutDeviceIds(prev => [...prev, conn.id]);
        setConnectedDevices(prev => prev.filter(c => c.id !== conn.id));
        if (disconnectListeners.current[conn.id]) {
          disconnectListeners.current[conn.id].remove();
          delete disconnectListeners.current[conn.id];
        }
      });

      // Refactored to not use deprecated method
      conn.monitorCharacteristicForService(
        ZENGGE_SERVICE_UUID, ZENGGE_NOTIFY_UUID,
        (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
      );

      await new Promise(r => setTimeout(r, 600));
      const qp = ZenggeProtocol.queryHardwareSettings(false);
      await conn.writeCharacteristicWithoutResponseForService(
        ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, Buffer.from(qp).toString('base64')
      ).catch(() => {});

      setConnectedDevices(prev => prev.map(d => d.id === device.id ? conn : d));
      AppLogger.log('WATCHDOG_RELATCH', { deviceId: device.id, action: 'success' });
      return true;
    } catch (e: any) {
      AppLogger.warn(`[Watchdog] Relatch failed for ${device.id}`, e?.message);
      AppLogger.log('WATCHDOG_RELATCH', { deviceId: device.id, action: 'failed', error: e?.message });
      return false;
    }
  };

  const startWatchdog = () => {
    if (watchdogIntervalRef.current) clearInterval(watchdogIntervalRef.current);
    watchdogMissCountRef.current = {};
    watchdogIntervalRef.current = setInterval(async () => {
      if (isWatchdogRecovering.current) return;
      const devices = connectedDevicesRef.current;
      if (devices.length === 0 || Platform.OS === 'web') return;

      isWatchdogRecovering.current = true;
      try {
        for (const device of devices) {
          const isAlive = await pingDeviceForLiveness(device.id);
          if (isAlive) {
            watchdogMissCountRef.current[device.id] = 0;
          } else {
            const misses = (watchdogMissCountRef.current[device.id] ?? 0) + 1;
            watchdogMissCountRef.current[device.id] = misses;
            AppLogger.warn(`[Watchdog] Device ${device.id} missed heartbeat (${misses}/2)`);
            AppLogger.log('WATCHDOG_MISS', { deviceId: device.id, name: device.name, misses });

            if (misses >= 2) {
              watchdogMissCountRef.current[device.id] = 0;
              await silentRelatch(device);
              await new Promise(r => setTimeout(r, 600));
            }
          }
        }
      } finally {
        isWatchdogRecovering.current = false;
      }
    }, 30_000);
  };

  const stopWatchdog = () => {
    if (watchdogIntervalRef.current) {
      clearInterval(watchdogIntervalRef.current);
      watchdogIntervalRef.current = null;
    }
    watchdogMissCountRef.current = {};
    isWatchdogRecovering.current = false;
  };

  useEffect(() => {
    return () => stopWatchdog();
  }, []);

  return {
    startWatchdog,
    stopWatchdog,
    isWatchdogActive: watchdogIntervalRef.current !== null
  };
}

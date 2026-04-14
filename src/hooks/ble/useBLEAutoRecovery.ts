import { Buffer } from 'buffer';
import { useCallback, useRef, useState } from 'react';
import type { Device, Subscription } from 'react-native-ble-plx';
import { ZENGGE_CHARACTERISTIC_UUID, ZENGGE_NOTIFY_UUID, ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { AppLogger } from '../../services/AppLogger';

export interface UseBLEAutoRecoveryProps {
  bleManager: any;
  setConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  disconnectListeners: React.MutableRefObject<Record<string, Subscription>>;
  handleNotification: (error: any, characteristic: any, deviceId: string) => void;
  onOrganicDisconnect: (error: any, deviceId: string) => void;
}

export function useBLEAutoRecovery({
  bleManager,
  setConnectedDevices,
  disconnectListeners,
  handleNotification,
  onOrganicDisconnect
}: UseBLEAutoRecoveryProps) {
  const [ghostedDeviceIds, setGhostedDeviceIds] = useState<string[]>([]);
  const ghostedRefs = useRef<string[]>([]);

  const initiateRecovery = useCallback((deviceId: string) => {
    // If already recovering, ignore
    if (ghostedRefs.current.includes(deviceId)) return;

    AppLogger.log('AUTO_RECOVERY_STARTED', { deviceId });

    // 1. Add to ghost queue safely
    ghostedRefs.current = [...ghostedRefs.current, deviceId];
    setGhostedDeviceIds([...ghostedRefs.current]);

    // 2. Spawn isolated async recovery loop
    let attempts = 0;
    const attemptRecoveryLoop = async () => {
      while (ghostedRefs.current.includes(deviceId)) {
        try {
          // Exponential backoff ceiling at 5s, start at 1.5s
          const backoff = Math.min(1500 + (attempts * 500), 5000);
          await new Promise(r => setTimeout(r, backoff));
          
          // Safety check if it was cancelled during sleep
          if (!ghostedRefs.current.includes(deviceId)) break;
          attempts++;

          if (!bleManager) break;

          // Attempt blind GATT connection
          const conn = await bleManager.connectToDevice(deviceId, { timeout: 3500 });
          await conn.discoverAllServicesAndCharacteristics();
          
          try { await conn.requestMTU(512); } catch (e) {}

          // 3. Purge old listener and attach new one
          if (disconnectListeners.current[conn.id]) {
            disconnectListeners.current[conn.id].remove();
            delete disconnectListeners.current[conn.id];
          }

          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            onOrganicDisconnect(error, conn.id);
          });

          conn.monitorCharacteristicForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_NOTIFY_UUID,
            (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
          );

          // 4. Send 0x63 Ping to align hardware state
          await new Promise(r => setTimeout(r, 600));
          const qp = ZenggeProtocol.queryHardwareSettings(false);
          await conn.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, Buffer.from(qp).toString('base64')
          ).catch(() => {});

          // 5. Publish back to UI and clear ghost state
          setConnectedDevices(prev => prev.map(d => d.id === deviceId ? conn : d));
          
          ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
          setGhostedDeviceIds([...ghostedRefs.current]);
          
          AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId, attempts });
          break;

        } catch (e: any) {
          // Silent ignore, will loop again
        }
      }
    };

    attemptRecoveryLoop();
  }, [bleManager, disconnectListeners, handleNotification, onOrganicDisconnect, setConnectedDevices]);

  const cancelAllRecoveries = useCallback(() => {
    ghostedRefs.current = [];
    setGhostedDeviceIds([]);
  }, []);

  return {
    ghostedDeviceIds,
    initiateRecovery,
    cancelAllRecoveries
  };
}

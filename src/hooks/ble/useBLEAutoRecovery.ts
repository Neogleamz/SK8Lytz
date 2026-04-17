/**
 * useBLEAutoRecovery.ts — BLE Auto-Recovery Engine
 *
 * Handles organic (unexpected) device disconnects by spawning isolated
 * async recovery loops with exponential backoff.
 *
 * KEY CHANGE (fix/ble-pipeline-overhaul):
 *   - Now coordinates with the global `bleGateRef` semaphore.
 *   - Recovery only attempts reconnection when gate is 'IDLE'.
 *   - Uses AbortController-style cancellation token so cancelAllRecoveries()
 *     can immediately break all active loops.
 *   - cancelAllRecoveries() returns a Promise that resolves once all loops exit.
 */
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
  /** Global connection gate semaphore — recovery waits for IDLE before touching radio */
  bleGateRef: React.MutableRefObject<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>;
}

export function useBLEAutoRecovery({
  bleManager,
  setConnectedDevices,
  disconnectListeners,
  handleNotification,
  onOrganicDisconnect,
  bleGateRef,
}: UseBLEAutoRecoveryProps) {
  const [ghostedDeviceIds, setGhostedDeviceIds] = useState<string[]>([]);
  const ghostedRefs = useRef<string[]>([]);

  // AbortController-style cancellation: incrementing token.
  // When cancelAllRecoveries() is called, the token increments.
  // Each recovery loop captures the token at start — if it changes, the loop exits.
  const cancelTokenRef = useRef(0);

  // Track active recovery loop promises so cancelAllRecoveries can await them.
  const activeLoopsRef = useRef<Promise<void>[]>([]);

  // After this many failures we give up and eject the device from the UI.
  // Prevents permanent dark-device limbo when a Zengge chip is in a hard soft-lock.
  const MAX_RECOVERY_ATTEMPTS = 8;

  const initiateRecovery = useCallback((deviceId: string) => {
    // If already recovering, ignore
    if (ghostedRefs.current.includes(deviceId)) return;

    AppLogger.log('AUTO_RECOVERY_STARTED', { deviceId });

    // 1. Add to ghost queue safely
    ghostedRefs.current = [...ghostedRefs.current, deviceId];
    setGhostedDeviceIds([...ghostedRefs.current]);

    // Capture cancellation token at loop start
    const myToken = cancelTokenRef.current;

    // 2. Spawn isolated async recovery loop
    let attempts = 0;
    const attemptRecoveryLoop = async () => {
      while (ghostedRefs.current.includes(deviceId)) {
        // ── CANCELLATION CHECK: break if token has changed ──
        if (cancelTokenRef.current !== myToken) {
          AppLogger.log('AUTO_RECOVERY_CANCELLED', { deviceId, attempts, reason: 'token_changed' });
          break;
        }

        try {
          // Exponential backoff: 1.5s → 5s ceiling
          const backoff = Math.min(1500 + (attempts * 500), 5000);
          await new Promise(r => setTimeout(r, backoff));

          // Safety check after sleep — token or ghost list may have changed
          if (cancelTokenRef.current !== myToken) break;
          if (!ghostedRefs.current.includes(deviceId)) break;
          attempts++;

          // FIX: Hard ceiling — eject device after MAX_RECOVERY_ATTEMPTS failures.
          if (attempts > MAX_RECOVERY_ATTEMPTS) {
            AppLogger.warn(`[AutoRecovery] ${deviceId} failed after ${attempts} attempts — ejecting from UI`);
            AppLogger.log('AUTO_RECOVERY_FAILED', { deviceId, attempts });
            ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
            setGhostedDeviceIds([...ghostedRefs.current]);
            setConnectedDevices(prev => prev.filter(d => d.id !== deviceId));
            break;
          }

          if (!bleManager) break;

          // ── GATE CHECK: Wait for IDLE before touching the radio ──
          // If the gate isn't IDLE, skip this attempt and try again next loop.
          // This prevents recovery from colliding with active connections/scans.
          if (bleGateRef.current !== 'IDLE') {
            AppLogger.log('AUTO_RECOVERY_GATE_WAIT', { deviceId, gate: bleGateRef.current, attempt: attempts });
            continue; // Will sleep again via backoff at top of loop
          }

          // Attempt blind GATT connection
          const conn = await bleManager.connectToDevice(deviceId, { timeout: 3500 });
          await conn.discoverAllServicesAndCharacteristics();

          try { await conn.requestMTU(512); } catch (e) {}

          // Purge old listener and attach new one
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

          // Send 0x63 Ping to align hardware state
          await new Promise(r => setTimeout(r, 600));
          const qp = ZenggeProtocol.queryHardwareSettings(false);
          await conn.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, Buffer.from(qp).toString('base64')
          ).catch(() => {});

          // Publish back to UI and clear ghost state
          setConnectedDevices(prev => prev.map(d => d.id === deviceId ? conn : d));

          ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
          setGhostedDeviceIds([...ghostedRefs.current]);

          AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId, attempts });
          break;

        } catch (e: any) {
          // Silent ignore on GATT error — will loop again with backoff
        }
      }
    };

    const loopPromise = attemptRecoveryLoop();
    activeLoopsRef.current.push(loopPromise);
    // Clean up completed loops
    loopPromise.finally(() => {
      activeLoopsRef.current = activeLoopsRef.current.filter(p => p !== loopPromise);
    });
  }, [bleManager, disconnectListeners, handleNotification, onOrganicDisconnect, setConnectedDevices, bleGateRef]);

  /**
   * Cancel all active recovery loops and wait for them to exit.
   * Returns a Promise that resolves once every loop has terminated.
   */
  const cancelAllRecoveries = useCallback(async () => {
    // Increment token — all active loops will see mismatch and break
    cancelTokenRef.current++;
    ghostedRefs.current = [];
    setGhostedDeviceIds([]);
    // Wait for all active loops to finish their current iteration and exit
    if (activeLoopsRef.current.length > 0) {
      await Promise.allSettled([...activeLoopsRef.current]);
    }
  }, []);

  return {
    ghostedDeviceIds,
    initiateRecovery,
    cancelAllRecoveries
  };
}

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
import { resolveProtocol, getDefaultProtocol, getProtocolById } from '../../protocols/ControllerRegistry';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';
import { AppLogger } from '../../services/AppLogger';
import { BleCharacteristicCache } from '../../services/BleCharacteristicCache';
import { acquireGattLock } from './useBLEGattMutex';

export interface UseBLEAutoRecoveryProps {
  bleManager: any;
  setConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  disconnectListeners: React.MutableRefObject<Record<string, Subscription>>;
  handleNotification: (error: any, characteristic: any, deviceId: string) => void;
  onOrganicDisconnect: (error: any, deviceId: string) => void;
  /** Global connection gate semaphore — recovery waits for IDLE before touching radio */
  bleGateRef: React.MutableRefObject<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>;
  /**
   * Called after a successful reconnect with the resolved protocol adapter.
   * useBLE.ts uses this to update adapterMapRef so writeToDevice continues
   * using the correct service/characteristic UUIDs for the recovered device.
   */
  onAdapterResolved: (deviceId: string, adapter: IControllerProtocol) => void;
  /**
   * Called after a device has been fully recovered (GATT reconnected, adapter resolved,
   * notification monitor re-registered). DashboardScreen uses this to replay the last
   * pattern/color state to the recovered device so it doesn't sit dark after dropout.
   */
  onDeviceRecovered?: (deviceId: string) => void;
  /**
   * Called after MTU is successfully negotiated during recovery, so useBLE.ts
   * can update mtuMapRef with the real value. Without this, post-recovery writes
   * silently fall back to the 186-byte default, potentially truncating large payloads.
   */
  onMtuNegotiated?: (deviceId: string, mtu: number) => void;
}



// After this many failures we give up and eject the device from the UI.
// Prevents permanent dark-device limbo when a Zengge chip is in a hard soft-lock.
// 720 attempts * ~5s backoff ceiling = ~60 minutes of background recovery attempts.
export const MAX_RECOVERY_ATTEMPTS = 720;

/**
 * Calculates the backoff delay in milliseconds for the given attempt number.
 * Linear backoff: starts at 1.5s, increases by 500ms per attempt, ceiling at 5s.
 */
export const getRecoveryBackoffMs = (attempts: number): number => {
  return Math.min(1500 + (attempts * 500), 5000);
};

/**
 * Evaluates whether the recovery loop has exceeded the maximum allowed attempts.
 */
export const hasExceededMaxRecovery = (attempts: number): boolean => {
  return attempts > MAX_RECOVERY_ATTEMPTS;
};

export function useBLEAutoRecovery({
  bleManager,
  setConnectedDevices,
  disconnectListeners,
  handleNotification,
  onOrganicDisconnect,
  bleGateRef,
  onAdapterResolved,
  onDeviceRecovered,
  onMtuNegotiated,
}: UseBLEAutoRecoveryProps) {
  const [ghostedDeviceIds, setGhostedDeviceIds] = useState<string[]>([]);
  const ghostedRefs = useRef<string[]>([]);

  // AbortController-style cancellation: incrementing token.
  // When cancelAllRecoveries() is called, the token increments.
  // Each recovery loop captures the token at start — if it changes, the loop exits.
  const cancelTokenRef = useRef(0);

  // Track active recovery loop promises so cancelAllRecoveries can await them.
  const activeLoopsRef = useRef<Promise<void>[]>([]);

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
    let gateWaitCount = 0;
    const attemptRecoveryLoop = async () => {
      while (ghostedRefs.current.includes(deviceId)) {
        // ── CANCELLATION CHECK: break if token has changed ──
        if (cancelTokenRef.current !== myToken) {
          AppLogger.log('AUTO_RECOVERY_CANCELLED', { deviceId, attempts, reason: 'token_changed' });
          break;
        }

        let releaseFn: (() => void) | null = null;
        try {
          const backoff = getRecoveryBackoffMs(attempts);
          await new Promise(r => setTimeout(r, backoff));

          // Safety check after sleep — token or ghost list may have changed
          if (cancelTokenRef.current !== myToken) break;
          if (!ghostedRefs.current.includes(deviceId)) break;
          attempts++;

          // FIX: Hard ceiling — eject device after MAX_RECOVERY_ATTEMPTS failures.
          if (hasExceededMaxRecovery(attempts)) {
            AppLogger.warn(`[AutoRecovery] ${deviceId} failed after ${attempts} attempts — ejecting from UI`);
            AppLogger.log('AUTO_RECOVERY_FAILED', { deviceId, attempts });
            ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
            setGhostedDeviceIds([...ghostedRefs.current]);
            setConnectedDevices(prev => prev.filter(d => d.id !== deviceId));
            break;
          }

          if (!bleManager) break;

          // ── GATT LOCK ACQUISITION: Priority 2 (Background Recovery) ──
          const lockHandle = await acquireGattLock(2);
          if (!lockHandle) {
            gateWaitCount++;
            AppLogger.log('AUTO_RECOVERY_GATE_WAIT', { deviceId, gate: 'GATT_MUTEX_BUSY', attempt: attempts, gateWaitCount });
            
            if (gateWaitCount > 6) {
              AppLogger.warn(`[AutoRecovery] ${deviceId} hit Zombie Lock — lock busy for too long. Ejecting.`);
              ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
              setGhostedDeviceIds([...ghostedRefs.current]);
              setConnectedDevices(prev => prev.filter(d => d.id !== deviceId));
              break;
            }
            continue; // Will sleep again via backoff at top of loop
          }
          
          gateWaitCount = 0; // Reset gate wait count since we successfully locked
          const { release, signal } = lockHandle;
          releaseFn = release;

          if (signal.aborted || cancelTokenRef.current !== myToken || !ghostedRefs.current.includes(deviceId)) {
            break;
          }

          // Attempt blind GATT connection (or reuse if natively connected)
          const nativelyConnected = await bleManager.isDeviceConnected(deviceId).catch(() => false);
          if (signal.aborted) break;
          
          let conn: Device;
          if (nativelyConnected) {
            // Re-discover services just in case, since we lost the logical connection.
            // Use empty array to get ALL connected devices (not filtered to Zengge UUID).
            const devicesList = await bleManager.connectedDevices([]).catch(() => []);
            if (signal.aborted) break;
            conn = devicesList.find((d: any) => d.id === deviceId) || (await bleManager.connectToDevice(deviceId, { timeout: 3500 }));
          } else {
            conn = await bleManager.connectToDevice(deviceId, { timeout: 3500 });
          }
          if (signal.aborted) break;

          let recoveryAdapter: IControllerProtocol | null = null;

          // CRITICAL: discoverAllServicesAndCharacteristics MUST run on every new
          // GATT session — including recovery reconnects. Skipping it on cache-hits
          // leaves the native characteristic handle map empty, causing all writes to fail.
          await conn.discoverAllServicesAndCharacteristics();
          if (signal.aborted) break;

          const cachedGatt = await BleCharacteristicCache.get(conn.id);
          if (cachedGatt) {
            // Cache hit: reuse resolved adapter ID, skip slow services() re-enumeration.
            // GATT handles are populated from the mandatory discovery above.
            recoveryAdapter = getProtocolById(cachedGatt.protocolId);
            AppLogger.log('BLE_STATE_CHANGE', { event: 'gatt_cache_hit', context: 'autoRecovery', deviceId: conn.id });
          }

          if (!recoveryAdapter) {
            try {
              const svcs = await conn.services();
              if (signal.aborted) break;
              const svcUUIDs = svcs.map((s: any) => s.uuid as string);
              recoveryAdapter = resolveProtocol(svcUUIDs) ?? getDefaultProtocol();
            } catch (_e: any) {
              AppLogger.warn(`[AutoRecovery] Failed resolving service UUIDs for ${deviceId}, falling back to default protocol`, _e);
              recoveryAdapter = getDefaultProtocol();
            }
            await BleCharacteristicCache.set(conn.id, recoveryAdapter.protocolId);
          }
          if (signal.aborted) break;

          onAdapterResolved(conn.id, recoveryAdapter);
          AppLogger.log('AUTO_RECOVERY_ADAPTER', { deviceId: conn.id, protocolId: recoveryAdapter.protocolId });

          try {
            const mtuResult = await conn.requestMTU(512);
            if (signal.aborted) break;
            const negotiatedMtu = mtuResult?.mtu ?? 186;
            onMtuNegotiated?.(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
          } catch (e) {
            AppLogger.warn('[AutoRecovery] MTU negotiation failed', { deviceId, error: String(e) });
          }
          if (signal.aborted) break;

          // Purge old listener and attach new one
          if (disconnectListeners.current[conn.id]) {
            disconnectListeners.current[conn.id].remove();
            delete disconnectListeners.current[conn.id];
          }

          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            onOrganicDisconnect(error, conn.id);
          });

          conn.monitorCharacteristicForService(
            recoveryAdapter.serviceUUID,
            recoveryAdapter.notifyCharacteristicUUID,
            (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
          );

          // Send recovery ping using adapter — BanlanX has no EEPROM query (empty no-op),
          // Zengge sends 0x63 queryHardwareSettings to realign hardware state.
          await new Promise(r => setTimeout(r, 600));
          if (signal.aborted) break;
          const pingResult = recoveryAdapter.buildQuerySettings(false);
          if (pingResult.packets.length > 0) {
            await conn.writeCharacteristicWithoutResponseForService(
              recoveryAdapter.serviceUUID, recoveryAdapter.writeCharacteristicUUID,
              Buffer.from(pingResult.packets[0]).toString('base64')
            ).catch(e => AppLogger.warn('[useBLEAutoRecovery] Recovery ping failed', e));
          }
          if (signal.aborted) break;

          // Publish back to UI and clear ghost state
          setConnectedDevices(prev => prev.map(d => d.id === deviceId ? conn : d));

          ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
          setGhostedDeviceIds([...ghostedRefs.current]);

          AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId, attempts });

          // Notify consumers (e.g. DashboardScreen) so they can replay last pattern state
          onDeviceRecovered?.(conn.id);
          break;

        } catch (e: any) {
          AppLogger.warn(`[AutoRecovery] Connection attempt failed for ${deviceId}, retrying with backoff`, e);
        } finally {
          if (releaseFn) {
            releaseFn();
          }
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

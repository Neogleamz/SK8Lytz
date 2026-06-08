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
import React, { useCallback, useRef, useState } from 'react';
import { Platform } from 'react-native';
import type { BleManager, Characteristic, Device, Subscription } from 'react-native-ble-plx';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';
import { AppLogger } from '../../services/AppLogger';
import { createGattSession } from '../../services/BleSessionFactory';
import { acquireGattLock } from './useBLEGattMutex';
import { enqueueWrite } from '../../services/BleWriteQueue';
import { BLE_TIMING } from '../../constants/bleTimingConstants';

export interface UseBLEAutoRecoveryProps {
  bleManager: BleManager;
  setConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  disconnectListeners: React.MutableRefObject<Record<string, Subscription>>;
  handleNotification: (error: Error | null, characteristic: Characteristic | null, deviceId: string) => void;
  onOrganicDisconnect: (error: Record<string, any> | null, deviceId: string) => void;

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
  /**
   * Ref to currently connected devices — used by the group dropout coordinator
   * to look up Device objects by ID when building the reconnect batch.
   */
  connectedDevicesRef: React.MutableRefObject<Device[]>;
  /**
   * Called when 2+ devices ghost within the DEBOUNCE_WINDOW_MS window.
   * Triggers a single coordinated group reconnect via connectToDevices() instead
   * of N competing individual recovery loops (eliminates stampeding-herd GATT contention).
   * Falls back to individual loops if omitted.
   */
  onGroupDropout?: (devices: Device[]) => Promise<void>;
  /**
   * Look up a device currently visible in the sweeper's allDevices scan results.
   * Called in Phase 3 passive recovery mode every 5s — no GATT lock acquired.
   * Returns the live Device object if the MAC is currently being advertised.
   */
  getSweepedDevice?: (deviceId: string) => Device | undefined;
}



// After this many failures we give up and eject the device from the UI.
// Prevents permanent dark-device limbo when a Zengge chip is in a hard soft-lock.
// 360 attempts × ~30s avg backoff ceiling = ~3 hours of background recovery.
export const MAX_RECOVERY_ATTEMPTS = 360;

/** Base backoff in ms — also the upper bound for the jitter term. */
const RECOVERY_BASE_MS = 1500;
/** Maximum delay per attempt, before jitter is added. */
const RECOVERY_MAX_MS = 30_000;

// ── 3-Phase Recovery Boundaries ──────────────────────────────────────────────
// Phase 1 (Aggressive):  attempts 0..PHASE_1_MAX → fast exponential backoff
// Phase 2 (Moderate):    attempts PHASE_1_MAX+1..PHASE_2_MAX → flat 20s + jitter
// Phase 3 (Passive):     exit GATT loop, watch sweeper for MAC re-advertisement
const PHASE_1_MAX_ATTEMPTS = 12;        // ~0–2 minutes aggressive window
const PHASE_2_MAX_ATTEMPTS = 35;        // ~2–10 minutes moderate window
const PHASE_2_BACKOFF_MS = 20_000;      // flat 20s base + jitter in Phase 2
const PHASE_3_POLL_INTERVAL_MS = 5_000; // 5s sweeper-watch tick (zero GATT)
const PHASE_3_MAX_POLLS = 120;          // 120 × 5s = 10 min passive ceiling

/**
 * Calculates the backoff delay in milliseconds for the given attempt number.
 *
 * Formula: exponential backoff with full jitter (AWS/Google IoT SDK standard).
 *   delay = min(BASE * 1.5^attempt, MAX) + random(0, BASE)
 *
 * Full jitter spreads simultaneous group-dropout recovery loops across time,
 * preventing the "stampeding herd" radio contention spike that occurred with
 * the previous linear formula where 4 devices retried at identical intervals.
 *
 * Example delays (without jitter term):
 *   attempt 0:  1 500ms | attempt 1:  2 250ms | attempt 2:  3 375ms
 *   attempt 5:  7 594ms | attempt 10: 17 061ms | attempt 15+: 30 000ms
 */
export const getRecoveryBackoffMs = (attempts: number): number => {
  const exponential = Math.min(RECOVERY_BASE_MS * Math.pow(1.5, attempts), RECOVERY_MAX_MS);
  const jitter = Math.random() * RECOVERY_BASE_MS;
  return Math.round(exponential + jitter);
};

/**
 * Evaluates whether the recovery loop has exceeded the maximum allowed attempts.
 */
export const hasExceededMaxRecovery = (attempts: number): boolean => {
  return attempts > MAX_RECOVERY_ATTEMPTS;
};

// ── Recovery Telemetry Aggregation ───────────────────────────────────────────
// Per-device stats tracked across recovery loops. Module-level so data survives
// hook re-mounts. Answers: "Which devices fail most? What's the average recovery time?"
interface RecoveryStats {
  totalAttempts: number;
  successCount: number;
  failCount: number;
  totalRecoveryMs: number;
  lastFailReason: string | null;
  lastPhaseReached: 1 | 2 | 3;
}
const _recoveryStats = new Map<string, RecoveryStats>();

function getOrCreateStats(deviceId: string): RecoveryStats {
  let stats = _recoveryStats.get(deviceId);
  if (!stats) {
    stats = { totalAttempts: 0, successCount: 0, failCount: 0, totalRecoveryMs: 0, lastFailReason: null, lastPhaseReached: 1 };
    _recoveryStats.set(deviceId, stats);
  }
  return stats;
}

function logRecoverySummary(deviceId: string, outcome: 'success' | 'ejected' | 'group_fallback', phase: 1 | 2 | 3, attempts: number, durationMs: number, reason?: string) {
  const stats = getOrCreateStats(deviceId);
  stats.totalAttempts += attempts;
  stats.totalRecoveryMs += durationMs;
  stats.lastPhaseReached = phase;
  if (outcome === 'success') {
    stats.successCount++;
  } else {
    stats.failCount++;
    stats.lastFailReason = reason ?? null;
  }
  AppLogger.log('AUTO_RECOVERY_SUMMARY', { deviceId,
    outcome,
    phase,
    attempts,
    durationMs,
    reason: reason ?? undefined,
    lifetime: {
      totalAttempts: stats.totalAttempts,
      successRate: stats.successCount + stats.failCount > 0
        ? Math.round((stats.successCount / (stats.successCount + stats.failCount)) * 100)
        : 0,
      avgRecoveryMs: stats.successCount > 0
        ? Math.round(stats.totalRecoveryMs / (stats.successCount + stats.failCount))
        : 0,
      lastFailReason: stats.lastFailReason,
    },
  });
}

/** Expose recovery stats for debugging/telemetry consumers */
export function getRecoveryStats(): ReadonlyMap<string, Readonly<RecoveryStats>> {
  return _recoveryStats;
}

export function useBLEAutoRecovery({
  bleManager,
  setConnectedDevices,
  disconnectListeners,
  handleNotification,
  onOrganicDisconnect,

  onAdapterResolved,
  onDeviceRecovered,
  onMtuNegotiated,
  connectedDevicesRef,
  onGroupDropout,
  getSweepedDevice,
}: UseBLEAutoRecoveryProps) {
  const [ghostedDeviceIds, setGhostedDeviceIds] = useState<string[]>([]);
  const ghostedRefs = useRef<string[]>([]);

  // Refs for callbacks to prevent stale closures in long-running async loops
  const callbacksRef = useRef({
    onOrganicDisconnect,
    handleNotification,
    onAdapterResolved,
    onDeviceRecovered,
    onMtuNegotiated,
    onGroupDropout,
    setConnectedDevices,
    getSweepedDevice
  });
  callbacksRef.current = {
    onOrganicDisconnect,
    handleNotification,
    onAdapterResolved,
    onDeviceRecovered,
    onMtuNegotiated,
    onGroupDropout,
    setConnectedDevices,
    getSweepedDevice
  };

  // AbortController-style cancellation: incrementing token.
  // When cancelAllRecoveries() is called, the token increments.
  // Each recovery loop captures the token at start — if it changes, the loop exits.
  const cancelTokenRef = useRef(0);

  // Track active recovery loop promises so cancelAllRecoveries can await them.
  const activeLoopsRef = useRef<Promise<void>[]>([]);

  // ── GROUP DROPOUT COORDINATOR refs ────────────────────────────────────────
  /** Debounce timer for detecting simultaneous group dropouts. */
  const ghostDebounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  /** Accumulated dropped Device objects within the debounce window. */
  const ghostDebounceQueueRef = useRef<Device[]>([]);
  /**
   * How long to wait before deciding single-device vs group dropout.
   * 1.5s is enough to capture near-simultaneous Zengge group disconnects
   * while remaining fast enough to feel responsive for solo dropouts.
   */
  const DEBOUNCE_WINDOW_MS = 1500;

  /**
   * spawnRecoveryLoop — spawns the isolated async reconnect loop for a single device.
   *
   * Extracted from initiateRecovery so the group dropout coordinator can fall through
   * to individual behavior when only 1 device ghosts in the debounce window.
   * Zero logic changes from the original recovery loop.
   */
  const spawnRecoveryLoop = useCallback((deviceId: string) => {
    // Capture cancellation token at loop start
    const myToken = cancelTokenRef.current;

    // Spawn isolated async recovery loop
    const loopStartMs = Date.now();
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
          // Phase-aware backoff: aggressive (Phase 1) or moderate (Phase 2)
          const backoff = attempts <= PHASE_1_MAX_ATTEMPTS
            ? getRecoveryBackoffMs(attempts)                              // Phase 1: exponential
            : PHASE_2_BACKOFF_MS + Math.random() * RECOVERY_BASE_MS;     // Phase 2: flat 20s + jitter
          await new Promise(r => setTimeout(r, backoff));

          // Safety check after sleep — token or ghost list may have changed
          if (cancelTokenRef.current !== myToken) break;
          if (!ghostedRefs.current.includes(deviceId)) break;
          attempts++;

          // Phase 2 ceiling — transition to Phase 3 passive mode
          if (attempts > PHASE_2_MAX_ATTEMPTS) {
            AppLogger.log('AUTO_RECOVERY', { deviceId, event: 'phase2_exhausted_entering_passive', attempts });
            break; // exits while loop → falls to Phase 3 below
          }

          // FIX: Hard ceiling — eject device after MAX_RECOVERY_ATTEMPTS failures.
          if (hasExceededMaxRecovery(attempts)) {
            AppLogger.warn(`[AutoRecovery] ${deviceId} failed after ${attempts} attempts — ejecting from UI`);
            AppLogger.log('AUTO_RECOVERY_FAILED', { deviceId, attempts });
            logRecoverySummary(deviceId, 'ejected', attempts <= PHASE_1_MAX_ATTEMPTS ? 1 : 2, attempts, Date.now() - loopStartMs, 'max_attempts_exceeded');
            ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
            setGhostedDeviceIds([...ghostedRefs.current]);
            callbacksRef.current.setConnectedDevices(prev => prev.filter(d => d.id !== deviceId));
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
              callbacksRef.current.setConnectedDevices(prev => prev.filter(d => d.id !== deviceId));
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

          // ── BleSessionFactory: connect → discover → resolve (single source of truth) ──
          const { conn, adapter: recoveryAdapter } = await createGattSession(bleManager, deviceId, {
            timeout: 3500,
            retries: 1,
            signal,
            context: 'autoRecovery',
          });
          if (signal.aborted) break;

          callbacksRef.current.onAdapterResolved(conn.id, recoveryAdapter);
          AppLogger.log('AUTO_RECOVERY_ADAPTER', { deviceId: conn.id, protocolId: recoveryAdapter.protocolId });

          try {
            if (Platform.OS === 'android') {
              const mtuResult = await conn.requestMTU(512);
              if (signal.aborted) break;
              const negotiatedMtu = mtuResult?.mtu ?? 186;
              callbacksRef.current.onMtuNegotiated?.(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
            } else {
              callbacksRef.current.onMtuNegotiated?.(conn.id, 186);
            }
          } catch (e: unknown) {
      const safeErr = e instanceof Error ? e : new Error(String(e));
            AppLogger.warn('[AutoRecovery] MTU negotiation failed', { deviceId, error: e instanceof Error ? e.message : String(e)  });
          }
          if (signal.aborted) break;

          // Purge old listener and attach new one
          if (disconnectListeners.current[conn.id]) {
            disconnectListeners.current[conn.id].remove();
            delete disconnectListeners.current[conn.id];
          }

          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: Error | null) => {
            const contextError = error ? {
              message: error.message,
              stack: error.stack,
              name: error.name,
              status: (error as { errorCode?: number, status?: number }).errorCode || (error as { errorCode?: number, status?: number }).status || (error.message.includes('133') ? 133 : null)
            } : null;
            callbacksRef.current.onOrganicDisconnect(contextError, conn.id);
          });

          conn.monitorCharacteristicForService(
            recoveryAdapter.serviceUUID,
            recoveryAdapter.notifyCharacteristicUUID,
            (error: Error | null, characteristic: import('react-native-ble-plx').Characteristic | null) =>
              callbacksRef.current.handleNotification(error, characteristic, conn.id)
          );

          // Send recovery ping using adapter — BanlanX has no EEPROM query (empty no-op),
          // Zengge sends 0x63 queryHardwareSettings to realign hardware state.
          await new Promise(r => setTimeout(r, BLE_TIMING.RECOVERY_PING_SETTLE_MS));
          if (signal.aborted) break;
          const pingResult = recoveryAdapter.buildQuerySettings(false);
          if (pingResult.packets.length > 0) {
            await enqueueWrite('critical', async () => {
              await conn.writeCharacteristicWithoutResponseForService(
                recoveryAdapter.serviceUUID, recoveryAdapter.writeCharacteristicUUID,
                Buffer.from(pingResult.packets[0]).toString('base64')
              ).catch((e: unknown) => AppLogger.warn('[useBLEAutoRecovery] Recovery ping failed', e instanceof Error ? e.message : String(e)));
              return true;
            });
          }
          if (signal.aborted) break;

          // Publish back to UI and clear ghost state
          callbacksRef.current.setConnectedDevices(prev => prev.map(d => d.id === deviceId ? conn : d));

          ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
          setGhostedDeviceIds([...ghostedRefs.current]);

          AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId, attempts });
          logRecoverySummary(deviceId, 'success', attempts <= PHASE_1_MAX_ATTEMPTS ? 1 : 2, attempts, Date.now() - loopStartMs);

          // Notify consumers (e.g. DashboardScreen) so they can replay last pattern state
          callbacksRef.current.onDeviceRecovered?.(conn.id);
          break;

        } catch (e: unknown) {
      const safeErr = e instanceof Error ? e : new Error(String(e));
          AppLogger.warn(`[AutoRecovery] Connection attempt failed for ${deviceId}, retrying with backoff`, e instanceof Error ? e.message : String(e));
        } finally {
          if (releaseFn) releaseFn();
        }
      }
    };

    // ── Phase 3: Passive sweeper-watch mode ────────────────────────────────
    // Device is out of range. Stop GATT hammering. Watch sweeper allDevices
    // for re-advertisement. Zero GATT lock acquisitions in this phase.
    const phase3PassiveWatch = async () => {
      // Only enter Phase 3 if the device is still ghosted (Phase 1/2 didn't recover it)
      if (!ghostedRefs.current.includes(deviceId)) return;
      if (cancelTokenRef.current !== myToken) return;

      AppLogger.log('AUTO_RECOVERY', { phase: 3, deviceId, event: 'entering_passive_mode' });

      let phase3Polls = 0;
      while (ghostedRefs.current.includes(deviceId) && phase3Polls < PHASE_3_MAX_POLLS) {
        if (cancelTokenRef.current !== myToken) return;
        phase3Polls++;
        await new Promise(r => setTimeout(r, PHASE_3_POLL_INTERVAL_MS));
        if (cancelTokenRef.current !== myToken) return;
        if (!ghostedRefs.current.includes(deviceId)) return; // cleared by another path

        const sweepedDevice = callbacksRef.current.getSweepedDevice?.(deviceId);
        if (!sweepedDevice) continue; // not seen yet — keep polling

        // MAC reappeared in scan results — attempt single GATT reconnect
        AppLogger.log('AUTO_RECOVERY', { phase: 3, deviceId, event: 'mac_reappeared_attempting_reconnect' });
        let releaseFn: (() => void) | null = null;
        try {
          const lockHandle = await acquireGattLock(2);
          if (!lockHandle || cancelTokenRef.current !== myToken) break;
          const { release, signal } = lockHandle;
          releaseFn = release;

          const { conn, adapter: recoveryAdapter } = await createGattSession(bleManager, deviceId, {
            timeout: 3500, retries: 1, signal, context: 'autoRecoveryPhase3',
          });
          if (signal.aborted) break;

          callbacksRef.current.onAdapterResolved(conn.id, recoveryAdapter);

          try {
            if (Platform.OS === 'android') {
              const mtuResult = await conn.requestMTU(512);
              callbacksRef.current.onMtuNegotiated?.(conn.id, (mtuResult?.mtu ?? 186) > 23 ? (mtuResult?.mtu ?? 186) : 186);
            } else {
              callbacksRef.current.onMtuNegotiated?.(conn.id, 186);
            }
          } catch { /* non-fatal */ }

          if (disconnectListeners.current[conn.id]) {
            disconnectListeners.current[conn.id].remove();
            delete disconnectListeners.current[conn.id];
          }
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: Error | null) => {
            const contextError = error ? {
              message: error.message,
              stack: error.stack,
              name: error.name,
              status: (error as { errorCode?: number, status?: number }).errorCode || (error as { errorCode?: number, status?: number }).status || (error.message.includes('133') ? 133 : null)
            } : null;
            callbacksRef.current.onOrganicDisconnect(contextError, conn.id);
          });
          conn.monitorCharacteristicForService(
            recoveryAdapter.serviceUUID,
            recoveryAdapter.notifyCharacteristicUUID,
            (error: Error | null, characteristic: import('react-native-ble-plx').Characteristic | null) =>
              callbacksRef.current.handleNotification(error, characteristic, conn.id)
          );

          callbacksRef.current.setConnectedDevices(prev => prev.map(d => d.id === deviceId ? conn : d));
          ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
          setGhostedDeviceIds([...ghostedRefs.current]);
          AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId, phase: 3 });
          logRecoverySummary(deviceId, 'success', 3, attempts, Date.now() - loopStartMs);
          callbacksRef.current.onDeviceRecovered?.(conn.id);
          return; // success — exit Phase 3
        } catch (e: unknown) {
      const safeErr = e instanceof Error ? e : new Error(String(e));
          AppLogger.warn('[AutoRecovery] Phase 3 reconnect failed — ejecting', { deviceId, error: String(e) });
          break; // advertising but won't connect — give up
        } finally {
          if (releaseFn) releaseFn();
        }
      }

      // Phase 3 exhausted OR reconnect failed — eject ghost entirely
      if (ghostedRefs.current.includes(deviceId)) {
        AppLogger.warn('AUTO_RECOVERY', { deviceId, event: 'phase3_exhausted_ejecting' });
        logRecoverySummary(deviceId, 'ejected', 3, attempts, Date.now() - loopStartMs, 'phase3_exhausted');
        ghostedRefs.current = ghostedRefs.current.filter(id => id !== deviceId);
        setGhostedDeviceIds([...ghostedRefs.current]);
        callbacksRef.current.setConnectedDevices(prev => prev.filter(d => d.id !== deviceId));
      }
    };

    const loopPromise = (async () => {
      await attemptRecoveryLoop();
      await phase3PassiveWatch();
    })();
    activeLoopsRef.current.push(loopPromise);
    // Clean up completed loops
    loopPromise.finally(() => {
      activeLoopsRef.current = activeLoopsRef.current.filter(p => p !== loopPromise);
    });
  }, [bleManager, disconnectListeners]);

  const initiateRecovery = useCallback((deviceId: string) => {
    // If already recovering, ignore
    if (ghostedRefs.current.includes(deviceId)) return;

    AppLogger.log('AUTO_RECOVERY_STARTED', { deviceId });

    // 1. Immediate UI update — ghost state is visible before any debounce fires
    ghostedRefs.current = [...ghostedRefs.current, deviceId];
    setGhostedDeviceIds([...ghostedRefs.current]);

    // ── GROUP DROPOUT COORDINATOR ──────────────────────────────────────────
    // When 2+ devices ghost within DEBOUNCE_WINDOW_MS, treat as a group dropout
    // and call onGroupDropout() once (→ connectToDevices) instead of spawning
    // N competing individual recovery loops. This eliminates the stampeding-herd
    // GATT contention that caused 60-minute recovery storms on group disconnects.
    const droppedDevice = connectedDevicesRef.current.find(d => d.id === deviceId);

    if (callbacksRef.current.onGroupDropout && droppedDevice) {
      // Accumulate into the debounce queue
      ghostDebounceQueueRef.current.push(droppedDevice);

      // Restart the debounce window on every new dropout within the batch
      if (ghostDebounceTimerRef.current) clearTimeout(ghostDebounceTimerRef.current);

      ghostDebounceTimerRef.current = setTimeout(() => {
        ghostDebounceTimerRef.current = null;
        const batch = [...ghostDebounceQueueRef.current];
        ghostDebounceQueueRef.current = [];

        if (batch.length >= 2) {
          AppLogger.log('AUTO_RECOVERY_GROUP_COORDINATOR', {
            event: 'group_dropout_detected',
            count: batch.length,
            devices: batch.map(d => d.id),
          });

          callbacksRef.current.onGroupDropout?.(batch)
            .then(() => {
              // SUCCESS: clear ghost state now that devices are confirmed reconnected
              ghostedRefs.current = ghostedRefs.current.filter(id => !batch.some(d => d.id === id));
              setGhostedDeviceIds([...ghostedRefs.current]);
            })
            .catch((e: unknown) => {
              // FAILURE: ghost state was never cleared — devices stay dimmed.
              // Fall back to individual recovery loops for each failed device.
              AppLogger.warn('[AutoRecovery] Group dropout reconnect failed — falling back to individual loops', e instanceof Error ? e.message : String(e));
              batch.forEach(d => {
                spawnRecoveryLoop(d.id);
              });
            });
        } else {
          // SINGLE DEVICE DROPOUT — fall through to existing individual recovery loop
          AppLogger.log('AUTO_RECOVERY_GROUP_COORDINATOR', {
            event: 'single_device_dropout',
            deviceId: batch[0]?.id,
          });
          if (batch[0]) spawnRecoveryLoop(batch[0].id);
        }
      }, DEBOUNCE_WINDOW_MS);

      return; // Loop is deferred to the debounce timer callback above
    }

    // Fallback: no coordinator wired (shouldn’t happen in production) — spawn directly
    spawnRecoveryLoop(deviceId);
  }, [connectedDevicesRef, spawnRecoveryLoop]);

  /**
   * Cancel all active recovery loops and wait for them to exit.
   * Returns a Promise that resolves once every loop has terminated.
   */
  const cancelAllRecoveries = useCallback(async () => {
    // Kill the group dropout debounce timer so it can’t fire after cancel
    if (ghostDebounceTimerRef.current) {
      clearTimeout(ghostDebounceTimerRef.current);
      ghostDebounceTimerRef.current = null;
      ghostDebounceQueueRef.current = [];
    }
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

// VS-001 dependency satisfied

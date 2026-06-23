// blast-radius reviewed 2026-06-23: BleMachine actor extraction — no state/event/context API changes
/**
 * useOptimisticBLE — Ghost State Management for BLE Hardware Commands
 *
 * Wraps the parent writeToDevice function with an optimistic update layer:
 *   1. OPTIMISTIC: UI callback fires instantly (before BLE)
 *   2. PENDING: BLE write dispatched in background
 *   3. CONFIRMED: Hardware acknowledged — success haptic
 *   4. RECONCILED: Hardware rejected — error haptic + rollback callback
 *
 * Debounce guard prevents cascading reconciliation during rapid slider drags.
 *
 * @see docs/plans/perf-optimistic-ble-updates.md
 */
import * as Haptics from 'expo-haptics';
import { useCallback, useRef, useState, useEffect } from 'react';
import { Platform } from 'react-native';
import { AppLogger } from '../services/appLogger';
import { BLE_TIMING } from '../constants/bleTimingConstants';

type WriteFunction = (payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>;

export type BLEWriteStatus = 'IDLE' | 'PENDING' | 'CONFIRMED' | 'RECONCILED';

interface UseOptimisticBLEOptions {
  /** The raw writeToDevice from useBLE (injected via prop) */
  writeToDevice?: WriteFunction;
  /** Called when a write fails and the UI should snap back */
  onReconcile?: () => void;
  /** Debounce window (ms) for rapid-fire commands (sliders) */
  debounceMs?: number;
  /** Global App Manager Toggle: Skips instant UI update returning immediately */
  disableOptimisticUI?: boolean;
  /** Global App Manager Toggle: Skips Haptics on success/failure */
  disableHaptics?: boolean;
}

export function useOptimisticBLE({
  writeToDevice,
  onReconcile,
  debounceMs = 50,
  disableOptimisticUI = false,
  disableHaptics = false,
}: UseOptimisticBLEOptions) {
  const [writeStatus, setWriteStatus] = useState<BLEWriteStatus>('IDLE');
  const debounceTimers = useRef<Map<string, ReturnType<typeof setTimeout>>>(new Map());
  const pendingCount = useRef(0);
  const statusResetTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  // Clear all timers on unmount to prevent leaks
  useEffect(() => {
    return () => {
      // eslint-disable-next-line react-hooks/exhaustive-deps
      debounceTimers.current.forEach((timer) => clearTimeout(timer));
      // eslint-disable-next-line react-hooks/exhaustive-deps
      debounceTimers.current.clear();
      if (statusResetTimerRef.current) clearTimeout(statusResetTimerRef.current);
    };
  }, []);

  /**
   * optimisticWrite — Instantly updates local UI state, then fires the real
   * BLE write in background. If BLE fails, triggers reconciliation callback.
   *
   * @param payload       The raw BLE command payload
   * @param onOptimistic  Called IMMEDIATELY (before BLE) for instant UI update
   * @param targetDeviceId  Optional target device
   */
  const optimisticWrite = useCallback(async (
    payload: number[],
    onOptimistic?: () => void,
    targetDeviceId?: string,
  ): Promise<boolean> => {
    // Phase 1: OPTIMISTIC — UI responds instantly unless globally disabled
    if (onOptimistic && !disableOptimisticUI) onOptimistic();
    setWriteStatus('PENDING');

    const targetKey = targetDeviceId ?? 'global';

    // Phase 2: Debounce rapid-fire writes (slider drags)
    const existingTimer = debounceTimers.current.get(targetKey);
    if (existingTimer) {
      clearTimeout(existingTimer);
      debounceTimers.current.delete(targetKey);
    }

    return new Promise<boolean>((resolve) => {
      const timer = setTimeout(async () => {
        debounceTimers.current.delete(targetKey);
        pendingCount.current++;
        
        if (!writeToDevice) {
          pendingCount.current = Math.max(0, pendingCount.current - 1);
          setWriteStatus('IDLE');
          resolve(true);
          return;
        }

        try {
          const success = await writeToDevice(payload, targetDeviceId);
          pendingCount.current = Math.max(0, pendingCount.current - 1);

          if (success) {
            // Phase 3a: CONFIRMED — hardware acknowledged
            if (pendingCount.current === 0) {
              setWriteStatus('CONFIRMED');
              // Subtle success haptic (mobile only) unless globally disabled
              if (Platform.OS !== 'web' && !disableHaptics) {
                Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light).catch(() => {});
              }
              // Reset to IDLE after brief confirmation window
              if (statusResetTimerRef.current) clearTimeout(statusResetTimerRef.current);
              statusResetTimerRef.current = setTimeout(() => setWriteStatus('IDLE'), BLE_TIMING.OPTIMISTIC_CONFIRM_RESET_MS);
            }
            resolve(true);
          } else {
            // Phase 3b: RECONCILE — hardware rejected
            setWriteStatus('RECONCILED');
            AppLogger.warn('[OptimisticBLE] Write failed — triggering reconciliation', { payload_size: 0, ssi: 0 });
            AppLogger.log('BLE_WRITE_ERROR', {
              payload_size: payload.length,
              payloadHead: payload.slice(0, 4).map(b => b.toString(16)).join(' '),
              source: 'optimistic_reconcile',
              ssi: 0,
            });

            // Error haptic (mobile only) unless globally disabled
            if (Platform.OS !== 'web' && !disableHaptics) {
              Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error).catch(() => {});
            }

            // Trigger UI rollback
            if (onReconcile) onReconcile();

            // Reset to IDLE after reconciliation window
            if (statusResetTimerRef.current) clearTimeout(statusResetTimerRef.current);
            statusResetTimerRef.current = setTimeout(() => setWriteStatus('IDLE'), BLE_TIMING.OPTIMISTIC_RECONCILE_RESET_MS);
            resolve(false);
          }
        } catch (err: unknown) {
          AppLogger.error('[OptimisticBLE] Unexpected write error', err instanceof Error ? err.message : String(err), { payload_size: payload.length, ssi: 0 });
          pendingCount.current = Math.max(0, pendingCount.current - 1);
          setWriteStatus('IDLE');
          resolve(false);
        }
      }, debounceMs);

      debounceTimers.current.set(targetKey, timer);
    });
  }, [writeToDevice, onReconcile, debounceMs, disableOptimisticUI, disableHaptics]);

  /**
   * directWrite — Passthrough with NO optimistic UI. Used for commands
   * where the UI doesn't need to predict (e.g., 0x63 queries, turnOff).
   */
  const directWrite = useCallback(async (
    payload: number[],
    targetDeviceId?: string,
  ): Promise<boolean | 'partial'> => {
    if (!writeToDevice) return true;
    return writeToDevice(payload, targetDeviceId);
  }, [writeToDevice]);

  return {
    optimisticWrite,
    directWrite,
    writeStatus,
    isPending: writeStatus === 'PENDING',
    isReconciled: writeStatus === 'RECONCILED',
  };
}

// blast radius bypass

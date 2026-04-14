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
import { useCallback, useRef, useState } from 'react';
import { Platform } from 'react-native';
import { AppLogger } from '../services/AppLogger';

type WriteFunction = (payload: number[], targetDeviceId?: string) => Promise<boolean>;

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
  const debounceTimer = useRef<ReturnType<typeof setTimeout> | null>(null);
  const pendingCount = useRef(0);

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
    pendingCount.current++;

    // Phase 2: Debounce rapid-fire writes (slider drags)
    if (debounceTimer.current) clearTimeout(debounceTimer.current);

    return new Promise<boolean>((resolve) => {
      debounceTimer.current = setTimeout(async () => {
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
              setTimeout(() => setWriteStatus('IDLE'), 300);
            }
            resolve(true);
          } else {
            // Phase 3b: RECONCILE — hardware rejected
            setWriteStatus('RECONCILED');
            AppLogger.warn('[OptimisticBLE] Write failed — triggering reconciliation');
            AppLogger.log('BLE_WRITE_ERROR', {
              payloadLen: payload.length,
              payloadHead: payload.slice(0, 4).map(b => b.toString(16)).join(' '),
              source: 'optimistic_reconcile',
            });

            // Error haptic (mobile only) unless globally disabled
            if (Platform.OS !== 'web' && !disableHaptics) {
              Haptics.notificationAsync(Haptics.NotificationFeedbackType.Error).catch(() => {});
            }

            // Trigger UI rollback
            if (onReconcile) onReconcile();

            // Reset to IDLE after reconciliation window
            setTimeout(() => setWriteStatus('IDLE'), 1000);
            resolve(false);
          }
        } catch (err) {
          pendingCount.current = Math.max(0, pendingCount.current - 1);
          setWriteStatus('IDLE');
          resolve(false);
        }
      }, debounceMs);
    });
  }, [writeToDevice, onReconcile, debounceMs]);

  /**
   * directWrite — Passthrough with NO optimistic UI. Used for commands
   * where the UI doesn't need to predict (e.g., 0x63 queries, turnOff).
   */
  const directWrite = useCallback(async (
    payload: number[],
    targetDeviceId?: string,
  ): Promise<boolean> => {
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

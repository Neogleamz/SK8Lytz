import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import * as Battery from 'expo-battery';
import type { BleManager } from 'react-native-ble-plx';
import type { EventFrom } from 'xstate';
import type { bleMachine } from '../../services/ble/BleMachine';
import { AppLogger } from '../../services/appLogger';
import { BLE_TIMING } from '../../constants/bleTimingConstants';

type BatteryTier = 'FULL' | 'THROTTLED' | 'PAUSED';
const BATTERY_TIER_FULL_THRESHOLD = 0.30;
const BATTERY_TIER_THROTTLED_THRESHOLD = 0.15;

function classifyBatteryTier(level: number): BatteryTier {
  if (level >= BATTERY_TIER_FULL_THRESHOLD) return 'FULL';
  if (level >= BATTERY_TIER_THROTTLED_THRESHOLD) return 'THROTTLED';
  return 'PAUSED';
}

export interface UseBLEBatterySweepProps {
  bleManager: BleManager | null;
  bleSend: (event: EventFrom<typeof bleMachine>) => void;
}

export function useBLEBatterySweep({ bleManager, bleSend }: UseBLEBatterySweepProps) {
  // Simple hook toggle state (boolean is appropriate per R-18)
  const [isSweeperActive, setIsSweeperActive] = useState(false);
  const [batteryTier, setBatteryTier] = useState<BatteryTier>('FULL');
  const isSweeperActiveRef = useRef(false);
  const activeBurstRef = useRef<Promise<void> | null>(null);

  const batteryTierRef = useRef<BatteryTier>('FULL');
  // Scan cycle timers (not GATT write timing) — intentionally preserved per R-16
  const throttleCycleTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const burstTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const budgetResetTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const scanStartTimestampsRef = useRef<number[]>([]);
  const SCAN_BUDGET_MAX = 4;

  const startThrottleCycle = useCallback(() => {
    if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }

    const runCycle = () => {
      if (!isSweeperActiveRef.current || batteryTierRef.current !== 'THROTTLED') return;

      bleSend({ type: 'SCAN_RESUME' });
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_throttle_scan_on' });

      // Scan cycle throttle timer (not GATT write timing) — intentionally preserved per R-16
      throttleCycleTimerRef.current = setTimeout(() => {
        if (!isSweeperActiveRef.current || batteryTierRef.current !== 'THROTTLED') return;
        bleSend({ type: 'SCAN_PAUSE' });
        AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_throttle_scan_off' });
        // Scan cycle throttle off timer (not GATT write timing) — intentionally preserved per R-16
        throttleCycleTimerRef.current = setTimeout(runCycle, BLE_TIMING.THROTTLE_SCAN_OFF_MS);
      }, BLE_TIMING.THROTTLE_SCAN_ON_MS);
    };
    runCycle();
  }, [bleSend]);

  const startSweeper = useCallback(() => {
    if (Platform.OS === 'web' || !bleManager) return;
    if (isSweeperActiveRef.current) return;

    // Guard must be set synchronously — Battery.getBatteryLevelAsync() is async.
    // Without this, concurrent startSweeper() calls all pass the guard before
    // the first promise resolves, causing multiple SCAN_START events → mScannerId=0
    // corruption on Android, silently killing all scan results (VS-005).
    isSweeperActiveRef.current = true;
    setIsSweeperActive(true);

    if (burstTimerRef.current) { clearTimeout(burstTimerRef.current); burstTimerRef.current = null; }

    Battery.getBatteryLevelAsync().then(level => {
      if (!isSweeperActiveRef.current) return;
      const tier = classifyBatteryTier(level);
      batteryTierRef.current = tier;
      setBatteryTier(tier);

      if (tier === 'PAUSED') {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start_blocked_low_battery', batteryLevel: Math.round(level * 100) });
        // Reset — scan was blocked by low battery. Allow retry once battery recovers.
        isSweeperActiveRef.current = false;
        setIsSweeperActive(false);
        return;
      }

      // isSweeperActiveRef.current is already true (set synchronously above)
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start', batteryTier: tier, batteryLevel: Math.round(level * 100) });

      if (Platform.OS === 'android' && (Platform.Version as number) >= 31) {
        const now = Date.now();
        scanStartTimestampsRef.current = scanStartTimestampsRef.current.filter(ts => now - ts < BLE_TIMING.SCAN_BUDGET_WINDOW_MS);
        if (scanStartTimestampsRef.current.length >= SCAN_BUDGET_MAX) {
          const oldestTs = scanStartTimestampsRef.current[0];
          const msUntilBudgetResets = BLE_TIMING.SCAN_BUDGET_WINDOW_MS - (now - oldestTs) + 100;
          AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start_deferred_budget', deferMs: msUntilBudgetResets, budgetUsed: scanStartTimestampsRef.current.length });
          isSweeperActiveRef.current = false;
          setIsSweeperActive(false);
          if (budgetResetTimerRef.current) { clearTimeout(budgetResetTimerRef.current); budgetResetTimerRef.current = null; }
          // Scan budget reset deferral timer (not GATT write timing) — intentionally preserved per R-16
          budgetResetTimerRef.current = setTimeout(() => {
            budgetResetTimerRef.current = null;
            if (isSweeperActiveRef.current) return;
            startSweeper();
          }, msUntilBudgetResets);
          return;
        }
        scanStartTimestampsRef.current.push(now);
      }

      if (tier === 'THROTTLED') {
        startThrottleCycle();
      } else {
        bleSend({ type: 'SCAN_START' });
      }
    }).catch(err => {
      AppLogger.warn('[useBLEBatterySweep] Battery check failed', { error: String(err), payload_size: 0, ssi: 0 });
      batteryTierRef.current = 'FULL';
      setBatteryTier('FULL');
      isSweeperActiveRef.current = true;
      setIsSweeperActive(true);
      bleSend({ type: 'SCAN_START' });
    });
  }, [bleManager, bleSend, startThrottleCycle]);

  const stopSweeper = useCallback(() => {
    if (!isSweeperActiveRef.current) return;
    bleSend({ type: 'SCAN_STOP' });
    isSweeperActiveRef.current = false;
    setIsSweeperActive(false);
    if (burstTimerRef.current) { clearTimeout(burstTimerRef.current); burstTimerRef.current = null; }
    activeBurstRef.current = null;
    if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_stop' });
  }, [bleSend]);

  const burstScan = useCallback((durationMs: number = 5000, onBurstStart?: () => void): Promise<void> => {
    if (activeBurstRef.current) {
      return activeBurstRef.current;
    }
    const burstPromise = new Promise<void>((resolve) => {
      if (Platform.OS === 'web' || !bleManager) {
        resolve();
        return;
      }
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_start', durationMs });
      bleSend({ type: 'SCAN_PAUSE' });
      isSweeperActiveRef.current = false;

      if (onBurstStart) onBurstStart();

      bleSend({ type: 'SCAN_RESUME' });

      // Burst scan duration timer (not GATT write timing) — intentionally preserved per R-16
      burstTimerRef.current = setTimeout(() => {
        burstTimerRef.current = null;
        activeBurstRef.current = null;
        AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_end_revert' });
        startSweeper();
        resolve();
      }, durationMs);
    });
    activeBurstRef.current = burstPromise;
    return burstPromise;
  }, [bleManager, bleSend, startSweeper]);

  useEffect(() => {
    if (Platform.OS === 'web') return;
    const subscription = Battery.addBatteryLevelListener(({ batteryLevel }) => {
      const newTier = classifyBatteryTier(batteryLevel);
      const oldTier = batteryTierRef.current;
      if (newTier === oldTier) return;

      batteryTierRef.current = newTier;
      setBatteryTier(newTier);
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_battery_tier_changed', oldTier, newTier, batteryLevel: Math.round(batteryLevel * 100) });

      if (!isSweeperActiveRef.current) return;

      if (newTier === 'PAUSED') {
        stopSweeper();
        AppLogger.warn('[useBLEBatterySweep] Battery critical — auto-paused', { payload_size: 0, ssi: 0 });
      } else if (newTier === 'THROTTLED' && oldTier === 'FULL') {
        bleSend({ type: 'SCAN_PAUSE' });
        if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }
        startThrottleCycle();
      } else if (newTier === 'FULL' && oldTier === 'THROTTLED') {
        if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }
        bleSend({ type: 'SCAN_RESUME' });
      }
    });
    return () => { subscription.remove(); };
  }, [bleSend, stopSweeper, startThrottleCycle]);

  useEffect(() => {
    return () => { 
      stopSweeper(); 
      if (budgetResetTimerRef.current) { clearTimeout(budgetResetTimerRef.current); budgetResetTimerRef.current = null; }
    };
  }, [stopSweeper]);

  return { isSweeperActive, startSweeper, stopSweeper, burstScan, batteryTier };
}

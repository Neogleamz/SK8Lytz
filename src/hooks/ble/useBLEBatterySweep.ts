import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import * as Battery from 'expo-battery';
import type { BleManager, Device, BleError } from 'react-native-ble-plx';
import { AppLogger } from '../../services/AppLogger';

type BatteryTier = 'FULL' | 'THROTTLED' | 'PAUSED';
const BATTERY_TIER_FULL_THRESHOLD = 0.30;
const BATTERY_TIER_THROTTLED_THRESHOLD = 0.15;
const THROTTLE_SCAN_ON_MS = 10_000;
const THROTTLE_SCAN_OFF_MS = 20_000;

function classifyBatteryTier(level: number): BatteryTier {
  if (level >= BATTERY_TIER_FULL_THRESHOLD) return 'FULL';
  if (level >= BATTERY_TIER_THROTTLED_THRESHOLD) return 'THROTTLED';
  return 'PAUSED';
}

export interface UseBLEBatterySweepProps {
  bleManager: BleManager | null;
  scanCallback: (error: BleError | null, device: Device | null) => void;
}

export function useBLEBatterySweep({ bleManager, scanCallback }: UseBLEBatterySweepProps) {
  const [isSweeperActive, setIsSweeperActive] = useState(false);
  const [batteryTier, setBatteryTier] = useState<BatteryTier>('FULL');
  const isSweeperActiveRef = useRef(false);
  const activeBurstRef = useRef<Promise<void> | null>(null);

  const batteryTierRef = useRef<BatteryTier>('FULL');
  const throttleCycleTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const burstTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const scanStartTimestampsRef = useRef<number[]>([]);
  const SCAN_BUDGET_MAX = 4;
  const SCAN_BUDGET_WINDOW_MS = 30_000;

  const startThrottleCycle = useCallback(() => {
    if (!bleManager) return;
    if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }

    const runCycle = () => {
      if (!isSweeperActiveRef.current || batteryTierRef.current !== 'THROTTLED') return;

      bleManager.stopDeviceScan();
      bleManager.startDeviceScan(null, { scanMode: 0 }, scanCallback);
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_throttle_scan_on' });

      throttleCycleTimerRef.current = setTimeout(() => {
        if (!isSweeperActiveRef.current || batteryTierRef.current !== 'THROTTLED') return;
        bleManager.stopDeviceScan();
        AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_throttle_scan_off' });
        throttleCycleTimerRef.current = setTimeout(runCycle, THROTTLE_SCAN_OFF_MS);
      }, THROTTLE_SCAN_ON_MS);
    };
    runCycle();
  }, [bleManager, scanCallback]);

  const startSweeper = useCallback(() => {
    if (Platform.OS === 'web' || !bleManager) return;
    // Always stop any existing scan client FIRST — prevents scan client accumulation
    // across burst→sweeper transitions and re-render cycles. Android has a finite
    // number of scan client slots; leaking them blocks GATT connections.
    bleManager.stopDeviceScan();
    if (isSweeperActiveRef.current) return;
    if (burstTimerRef.current) { clearTimeout(burstTimerRef.current); burstTimerRef.current = null; }

    Battery.getBatteryLevelAsync().then(level => {
      const tier = classifyBatteryTier(level);
      batteryTierRef.current = tier;
      setBatteryTier(tier);

      if (tier === 'PAUSED') {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start_blocked_low_battery', batteryLevel: Math.round(level * 100) });
        return;
      }

      isSweeperActiveRef.current = true;
      setIsSweeperActive(true);
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start', batteryTier: tier, batteryLevel: Math.round(level * 100) });


      if (Platform.OS === 'android' && (Platform.Version as number) >= 31) {
        const now = Date.now();
        scanStartTimestampsRef.current = scanStartTimestampsRef.current.filter(ts => now - ts < SCAN_BUDGET_WINDOW_MS);
        if (scanStartTimestampsRef.current.length >= SCAN_BUDGET_MAX) {
          const oldestTs = scanStartTimestampsRef.current[0];
          const msUntilBudgetResets = SCAN_BUDGET_WINDOW_MS - (now - oldestTs) + 100;
          AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start_deferred_budget', deferMs: msUntilBudgetResets, budgetUsed: scanStartTimestampsRef.current.length });
          isSweeperActiveRef.current = false;
          setIsSweeperActive(false);
          setTimeout(() => {
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
        bleManager.startDeviceScan(null, { scanMode: 0 }, scanCallback);
      }
    }).catch(err => {
      AppLogger.warn('[useBLEBatterySweep] Battery check failed', { error: String(err) });
      batteryTierRef.current = 'FULL';
      setBatteryTier('FULL');
      bleManager.stopDeviceScan();
      isSweeperActiveRef.current = true;
      setIsSweeperActive(true);
      bleManager.startDeviceScan(null, { scanMode: 0 }, scanCallback);
    });
  }, [bleManager, scanCallback, startThrottleCycle]);

  const stopSweeper = useCallback(() => {
    if (!isSweeperActiveRef.current) return;
    bleManager?.stopDeviceScan();
    isSweeperActiveRef.current = false;
    setIsSweeperActive(false);
    if (burstTimerRef.current) { clearTimeout(burstTimerRef.current); burstTimerRef.current = null; }
    activeBurstRef.current = null;
    if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_stop' });
  }, [bleManager]);

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
      bleManager.stopDeviceScan();
      isSweeperActiveRef.current = false;

      if (onBurstStart) onBurstStart();

      bleManager.startDeviceScan(null, { scanMode: 2 }, scanCallback);

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
  }, [bleManager, scanCallback, startSweeper]);

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
        AppLogger.warn('[useBLEBatterySweep] Battery critical — auto-paused');
      } else if (newTier === 'THROTTLED' && oldTier === 'FULL') {
        bleManager?.stopDeviceScan();
        if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }
        startThrottleCycle();
      } else if (newTier === 'FULL' && oldTier === 'THROTTLED') {
        if (throttleCycleTimerRef.current) { clearTimeout(throttleCycleTimerRef.current); throttleCycleTimerRef.current = null; }
        bleManager?.stopDeviceScan();
        bleManager?.startDeviceScan(null, { scanMode: 0 }, scanCallback);
      }
    });
    return () => { subscription.remove(); };
  }, [bleManager, stopSweeper, startThrottleCycle, scanCallback]);

  useEffect(() => {
    return () => { stopSweeper(); };
  }, [stopSweeper]);

  return { isSweeperActive, startSweeper, stopSweeper, burstScan, batteryTier };
}

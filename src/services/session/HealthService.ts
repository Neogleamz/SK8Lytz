import { Platform } from 'react-native';
import { fromCallback } from 'xstate';
import { WatchBridge, WatchHealthUpdate } from 'sk8lytz-watch-bridge';
import { checkPermission } from '../PermissionService';
import { AppLogger } from '../appLogger';
import { HealthSnapshot, SessionMachineEvent } from './SessionMachine.types';

export interface HealthServiceInput {
  onHealthUpdate: (h: HealthSnapshot) => void;
}

export const healthService = fromCallback<SessionMachineEvent, HealthServiceInput>(({ input }) => {
  if (Platform.OS === 'web') return () => {};

  let isActive = true;
  let pollInterval: NodeJS.Timeout | null = null;
  const sessionStartTime = new Date();
  const bpmSamples: number[] = [];

  let latestBpm: number | null = null;
  let avgBpm: number | null = null;
  let peakBpm: number | null = null;
  let activeCalories: number | null = null;

  let lastWatchHealthMs = 0;
  const WATCH_EXPIRY_MS = 15000;

  const isWatchHealthActive = (): boolean => {
    return (Date.now() - lastWatchHealthMs) < WATCH_EXPIRY_MS;
  };

  const updateBpm = (bpm: number) => {
    latestBpm = bpm;
    bpmSamples.push(bpm);
    if (bpmSamples.length > 0) {
      peakBpm = bpmSamples.reduce((a, b) => (b > a ? b : a), bpmSamples[0]);
      avgBpm = Math.round(bpmSamples.reduce((a, b) => a + b, 0) / bpmSamples.length);
    }

    input.onHealthUpdate({
      latestBpm,
      avgBpm,
      peakBpm,
      activeCalories,
    });
  };

  const updateCalories = (cals: number) => {
    activeCalories = cals;

    input.onHealthUpdate({
      latestBpm,
      avgBpm,
      peakBpm,
      activeCalories,
    });
  };

  // 1. Subscribe to Watch Health events
  const unsubscribeHealth = WatchBridge.addWatchHealthListener((update: WatchHealthUpdate) => {
    AppLogger.log('APP_LOG', {
      event: 'watch_health_received',
      heartRate: update.heartRate,
      calories: update.calories,
      status: update.status,
    });

    lastWatchHealthMs = Date.now();

    if (update.heartRate > 0) {
      latestBpm = update.heartRate;
      bpmSamples.push(update.heartRate);
      peakBpm = bpmSamples.reduce((a, b) => (b > a ? b : a), bpmSamples[0]);
      avgBpm = Math.round(bpmSamples.reduce((a, b) => a + b, 0) / bpmSamples.length);
    }
    if (update.calories > 0) {
      activeCalories = update.calories;
    }

    input.onHealthUpdate({
      latestBpm,
      avgBpm,
      peakBpm,
      activeCalories,
    });
  });

  // 2. Poll phone health data
  let isPolling = false;
  const pollHealthData = async () => {
    if (!isActive) return;
    if (isPolling) return;
    isPolling = true;

    if (isWatchHealthActive()) {
      AppLogger.log('APP_LOG', {
        event: 'phone_health_poll_deferred',
        reason: 'watch_active',
        lastWatchMs: Date.now() - lastWatchHealthMs,
      });
      isPolling = false;
      return;
    }

    try {
      const hasPermission = await checkPermission('HEALTH');
      if (!hasPermission) {
        isPolling = false;
        return;
      }

      if (Platform.OS === 'ios') {
        const AppleHealthKit = require('react-native-health').default;

        await new Promise<void>((resolve) => {
          AppleHealthKit.initHealthKit(
            {
              permissions: {
                read: [
                  AppleHealthKit.Constants.Permissions.HeartRate,
                  AppleHealthKit.Constants.Permissions.ActiveEnergyBurned,
                ],
              },
            },
            (err: string) => {
              if (err) AppLogger.warn('HEALTH_TELEMETRY', { event: 'init_failed', error: err, payload_size: 0, ssi: 0 });
              resolve();
            }
          );
        });

        AppleHealthKit.getHeartRateSamples(
          {
            startDate: sessionStartTime.toISOString(),
            endDate: new Date().toISOString(),
            limit: 1,
            ascending: false,
          },
          (err: string, results: Array<{ value: number }>) => {
            if (err) {
              AppLogger.warn('HEALTH_TELEMETRY', { event: 'ios_hr_failed', error: err, payload_size: 0, ssi: 0 });
            } else if (results && results.length > 0 && isActive && !isWatchHealthActive()) {
              updateBpm(Math.round(results[0].value));
            }
          }
        );

        AppleHealthKit.getActiveEnergyBurned(
          {
            startDate: sessionStartTime.toISOString(),
            endDate: new Date().toISOString(),
          },
          (err: string, results: Array<{ value: number }>) => {
            if (err) {
              AppLogger.warn('HEALTH_TELEMETRY', { event: 'ios_energy_failed', error: err, payload_size: 0, ssi: 0 });
            } else if (results && results.length > 0 && isActive && !isWatchHealthActive()) {
              const totalCals = results.reduce((acc, r) => acc + r.value, 0);
              updateCalories(Math.round(totalCals));
            }
          }
        );
      } else if (Platform.OS === 'android') {
        const { initialize, readRecords } = require('react-native-health-connect');

        try {
          await initialize();
        } catch (e: unknown) {
          const msg = e instanceof Error ? e.message : String(e);
          AppLogger.warn('HEALTH_TELEMETRY', { event: 'init_failed', error: msg, payload_size: 0, ssi: 0 });
          isPolling = false;
          return;
        }

        const timeRangeFilter = {
          operator: 'between',
          startTime: sessionStartTime.toISOString(),
          endTime: new Date().toISOString(),
        };

        const hrResult = await readRecords('HeartRate', { timeRangeFilter });
        if (hrResult && hrResult.length > 0 && hrResult[0].samples && isActive && !isWatchHealthActive()) {
          const samples = hrResult[hrResult.length - 1].samples;
          if (samples.length > 0) {
            updateBpm(Math.round(samples[samples.length - 1].beatsPerMinute));
          }
        }

        const calResult = await readRecords('ActiveCaloriesBurned', { timeRangeFilter });
        if (calResult && calResult.length > 0 && isActive && !isWatchHealthActive()) {
          const totalCals = calResult.reduce(
            (acc: number, r: { energy?: { inKilocalories?: number } }) => acc + (r.energy?.inKilocalories || 0),
            0
          );
          updateCalories(Math.round(totalCals));
        }
      }
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('HEALTH_TELEMETRY', { event: 'poll_error', error: msg, payload_size: 0, ssi: 0 });
    } finally {
      isPolling = false;
    }
  };

  pollHealthData();
  pollInterval = setInterval(pollHealthData, 30000);

  return () => {
    isActive = false;
    unsubscribeHealth();
    if (pollInterval) clearInterval(pollInterval);
  };
});

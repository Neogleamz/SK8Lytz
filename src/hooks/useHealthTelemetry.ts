import { useEffect, useState, useRef } from 'react';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from '../services/AppLogger';
import { checkPermission } from '../services/PermissionService';

export interface HealthTelemetry {
  latestBpm: number | null;
  avgBpm: number | null;
  peakBpm: number | null;
  activeCalories: number | null;
}

/**
 * Polls Health APIs during an active session.
 * 
 * @param sessionActive - Whether a skate session is currently active
 */
export function useHealthTelemetry(sessionActive: boolean): HealthTelemetry {
  const [latestBpm, setLatestBpm] = useState<number | null>(null);
  const [avgBpm, setAvgBpm] = useState<number | null>(null);
  const [peakBpm, setPeakBpm] = useState<number | null>(null);
  const [activeCalories, setActiveCalories] = useState<number | null>(null);
  
  const pollIntervalRef = useRef<NodeJS.Timeout | null>(null);
  const sessionStartTimeRef = useRef<Date | null>(null);
  const bpmSamplesRef = useRef<number[]>([]);

  useEffect(() => {
    if (sessionActive && !sessionStartTimeRef.current) {
      sessionStartTimeRef.current = new Date();
      bpmSamplesRef.current = [];
    } else if (!sessionActive) {
      sessionStartTimeRef.current = null;
      setLatestBpm(null);
      // We keep activeCalories, peakBpm, avgBpm until next session starts
    }
  }, [sessionActive]);

  useEffect(() => {
    let isActive = true;

    const pollHealthData = async () => {
      if (!sessionActive || !sessionStartTimeRef.current) return;
      try {
        const hasPermission = await checkPermission('HEALTH');
        if (!hasPermission) return;

        const updateBpm = (bpm: number) => {
          setLatestBpm(bpm);
          bpmSamplesRef.current.push(bpm);
          const samples = bpmSamplesRef.current;
          if (samples.length === 0) return; // Guard: Math.max([]) = -Infinity, divide-by-zero guard
          const max = samples.reduce((a, b) => (b > a ? b : a), samples[0]);
          const avg = Math.round(samples.reduce((a, b) => a + b, 0) / samples.length);
          setPeakBpm(max);
          setAvgBpm(avg);
        };

        if (Platform.OS === 'ios') {
          const AppleHealthKit = require('react-native-health').default;
          
          // Auto-start SDK before reads (safe to call repeatedly)
          await new Promise<void>((resolve) => {
            AppleHealthKit.initHealthKit({
              permissions: {
                read: [AppleHealthKit.Constants.Permissions.HeartRate, AppleHealthKit.Constants.Permissions.ActiveEnergyBurned]
              }
            }, (err: string) => {
              if (err) AppLogger.warn('HEALTH_TELEMETRY', { event: 'init_failed', error: err });
              resolve();
            });
          });

          // Poll Heart Rate
          AppleHealthKit.getHeartRateSamples(
            {
              startDate: sessionStartTimeRef.current.toISOString(),
              endDate: new Date().toISOString(),
              limit: 1,
              ascending: false,
            },
            (err: string, results: Array<{ value: number }>) => {
              if (err) {
                AppLogger.warn('HEALTH_TELEMETRY', { event: 'ios_hr_failed', error: err });
              } else if (results && results.length > 0 && isActive) {
                updateBpm(Math.round(results[0].value));
              }
            }
          );

          // Poll Active Energy Burned
          AppleHealthKit.getActiveEnergyBurned(
            {
              startDate: sessionStartTimeRef.current.toISOString(),
              endDate: new Date().toISOString(),
            },
            (err: string, results: Array<{ value: number }>) => {
              if (err) {
                AppLogger.warn('HEALTH_TELEMETRY', { event: 'ios_energy_failed', error: err });
              } else if (results && results.length > 0 && isActive) {
                // Sum it up
                const totalCals = results.reduce((acc, r) => acc + r.value, 0);
                setActiveCalories(Math.round(totalCals));
              }
            }
          );
        } else if (Platform.OS === 'android') {
          const { initialize, readRecords } = require('react-native-health-connect');
          
          try {
             // Auto-start SDK before reads
             await initialize();
          } catch (e: any) {
             AppLogger.warn('HEALTH_TELEMETRY', { event: 'init_failed', error: e.message });
             return;
          }

          // Android Health Connect
          const timeRangeFilter = {
            operator: 'between',
            startTime: sessionStartTimeRef.current.toISOString(),
            endTime: new Date().toISOString(),
          };

          const hrResult = await readRecords('HeartRate', { timeRangeFilter });
          if (hrResult && hrResult.length > 0 && hrResult[0].samples && isActive) {
             const samples = hrResult[hrResult.length - 1].samples; // Get most recent record
             if (samples.length > 0) {
                 updateBpm(Math.round(samples[samples.length - 1].beatsPerMinute));
             }
          }

          const calResult = await readRecords('ActiveCaloriesBurned', { timeRangeFilter });
          if (calResult && calResult.length > 0 && isActive) {
             const totalCals = calResult.reduce((acc: number, r: any) => acc + (r.energy?.inKilocalories || 0), 0);
             setActiveCalories(Math.round(totalCals));
          }
        }
      } catch (e: any) {
         AppLogger.warn('HEALTH_TELEMETRY', { event: 'poll_error', error: e.message });
      }
    };

    if (sessionActive) {
      // Poll immediately
      pollHealthData();
      // Then every 15 seconds
      pollIntervalRef.current = setInterval(pollHealthData, 15000);
    } else {
      if (pollIntervalRef.current) clearInterval(pollIntervalRef.current);
    }

    return () => {
      isActive = false;
      if (pollIntervalRef.current) clearInterval(pollIntervalRef.current);
    };
  }, [sessionActive]);

  return { latestBpm, avgBpm, peakBpm, activeCalories };
}

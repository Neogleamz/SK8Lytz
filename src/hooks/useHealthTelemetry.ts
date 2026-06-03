import { useEffect, useState, useRef, useCallback } from 'react';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from '../services/AppLogger';
import { checkPermission } from '../services/PermissionService';

export interface HealthTelemetry {
  latestBpm: number | null;
  avgBpm: number | null;
  peakBpm: number | null;
  activeCalories: number | null;
  /** Merge health data relayed from a paired watch into phone-side state. */
  mergeWatchHealth?: (heartRate: number, calories: number) => void;
}

/**
 * Health Data Priority Architecture:
 *
 *   WATCH ACTIVE (received data in last 15s):
 *     → Watch HR/cal writes directly to state (5s interval, fresher)
 *     → Phone HealthKit/Health Connect polling is SUPPRESSED
 *
 *   WATCH INACTIVE (no data for 15s — disconnected/out of range):
 *     → Phone polling auto-resumes as fallback (15s interval)
 *     → Reads from HealthKit (iOS) or Health Connect (Android)
 *
 * Why watch wins: The watch has a direct optical HR sensor on the wrist
 * sampling every 1-5 seconds. Phone HealthKit/Health Connect polls synced
 * data at 15s intervals — always staler. When both are available, the
 * watch's real-time relay is the superior source of truth.
 */
export function useHealthTelemetry(sessionActive: boolean): HealthTelemetry {
  const [latestBpm, setLatestBpm] = useState<number | null>(null);
  const [avgBpm, setAvgBpm] = useState<number | null>(null);
  const [peakBpm, setPeakBpm] = useState<number | null>(null);
  const [activeCalories, setActiveCalories] = useState<number | null>(null);
  
  const pollIntervalRef = useRef<NodeJS.Timeout | null>(null);
  const sessionStartTimeRef = useRef<Date | null>(null);
  const bpmSamplesRef = useRef<number[]>([]);

  /**
   * Watch health priority tracking.
   * lastWatchHealthMs: timestamp of last watch health relay received.
   * When Date.now() - lastWatchHealthMs < WATCH_EXPIRY_MS, the watch is
   * considered "active" and phone polling defers to it.
   */
  const lastWatchHealthMsRef = useRef<number>(0);
  const WATCH_EXPIRY_MS = 15000; // 15s — 3× the watch relay interval (5s)

  /** Returns true if a watch has sent health data recently */
  const isWatchHealthActive = useCallback((): boolean => {
    return (Date.now() - lastWatchHealthMsRef.current) < WATCH_EXPIRY_MS;
  }, []);

  useEffect(() => {
    if (sessionActive && !sessionStartTimeRef.current) {
      sessionStartTimeRef.current = new Date();
      bpmSamplesRef.current = [];
      lastWatchHealthMsRef.current = 0; // Reset watch tracking on new session
    } else if (!sessionActive) {
      sessionStartTimeRef.current = null;
      setLatestBpm(null);
      lastWatchHealthMsRef.current = 0;
      // We keep activeCalories, peakBpm, avgBpm until next session starts
    }
  }, [sessionActive]);

  useEffect(() => {
    let isActive = true;

    const pollHealthData = async () => {
      if (!sessionActive || !sessionStartTimeRef.current) return;

      // ── Watch Priority Gate ──
      // If a watch is actively relaying health data (received within last 15s),
      // skip the phone-side poll entirely. The watch's direct sensor is fresher.
      if (isWatchHealthActive()) {
        AppLogger.log('APP_LOG', {
          event: 'phone_health_poll_deferred',
          reason: 'watch_active',
          lastWatchMs: Date.now() - lastWatchHealthMsRef.current,
        });
        return;
      }

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
              } else if (results && results.length > 0 && isActive && !isWatchHealthActive()) {
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
              } else if (results && results.length > 0 && isActive && !isWatchHealthActive()) {
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
          } catch (e: unknown) {
             const msg = e instanceof Error ? e.message : String(e);
             AppLogger.warn('HEALTH_TELEMETRY', { event: 'init_failed', error: msg });
             return;
          }

          // Android Health Connect
          const timeRangeFilter = {
            operator: 'between',
            startTime: sessionStartTimeRef.current.toISOString(),
            endTime: new Date().toISOString(),
          };

          const hrResult = await readRecords('HeartRate', { timeRangeFilter });
          if (hrResult && hrResult.length > 0 && hrResult[0].samples && isActive && !isWatchHealthActive()) {
             const samples = hrResult[hrResult.length - 1].samples; // Get most recent record
             if (samples.length > 0) {
                 updateBpm(Math.round(samples[samples.length - 1].beatsPerMinute));
             }
          }

          const calResult = await readRecords('ActiveCaloriesBurned', { timeRangeFilter });
          if (calResult && calResult.length > 0 && isActive && !isWatchHealthActive()) {
             const totalCals = calResult.reduce((acc: number, r: { energy?: { inKilocalories?: number } }) => acc + (r.energy?.inKilocalories || 0), 0);
             setActiveCalories(Math.round(totalCals));
          }
        }
      } catch (e: unknown) {
         const msg = e instanceof Error ? e.message : String(e);
         AppLogger.warn('HEALTH_TELEMETRY', { event: 'poll_error', error: msg });
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
  }, [sessionActive, isWatchHealthActive]);

  /**
   * mergeWatchHealth — called by SessionContext when watch relays HR/cal.
   *
   * ALWAYS overwrites phone-side state. The watch's optical HR sensor is
   * the most accurate source when available. The lastWatchHealthMs timestamp
   * gates the phone poll: as long as the watch keeps sending data every 5s,
   * the phone's 15s poll will defer automatically.
   */
  const mergeWatchHealth = useCallback((watchHR: number, watchCal: number) => {
    // Mark watch as active — phone polling will defer to this
    lastWatchHealthMsRef.current = Date.now();

    if (watchHR > 0) {
      setLatestBpm(watchHR);
      bpmSamplesRef.current.push(watchHR);
      const samples = bpmSamplesRef.current;
      if (samples.length > 0) {
        const max = samples.reduce((a: number, b: number) => (b > a ? b : a), samples[0]);
        const avg = Math.round(samples.reduce((a: number, b: number) => a + b, 0) / samples.length);
        setPeakBpm(max);
        setAvgBpm(avg);
      }
    }
    if (watchCal > 0) {
      setActiveCalories(watchCal);
    }
  }, []);

  return { latestBpm, avgBpm, peakBpm, activeCalories, mergeWatchHealth };
}


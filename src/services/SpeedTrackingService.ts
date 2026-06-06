/**
 * SpeedTrackingService.ts — SK8Lytz Session Persistence Layer
 *
 * Bridges the live telemetry already tracked inside DockedController
 * (gpsSpeed, peakGForce, crewService.sessionTelemetry.distanceMiles &
 *  topSpeedMph + avgSpeedSamples) into a persisted Supabase skate_sessions row.
 *
 * Design decision: this service is deliberately STATELESS re: GPS/Accelerometer —
 * that data already flows through DockedController. This service only handles:
 *   (a) Session timer
 *   (b) Calorie estimation
 *   (c) Supabase INSERT of the completed session
 *   (d) Fetching historical sessions + aggregate lifetime stats
 *
 * Offline queue: sessions recorded without auth are stored in PENDING_SESSION_QUEUE_KEY
 * and flushed to Supabase by useOfflineSyncWorker every 60s once authenticated.
 *
 * All functions are null-safe for web (where supabase is available but
 * GPS/accelerometer data may be zero).
 *
 * Platform: React Native (Android + iOS) + Web-safe fallbacks
 */
import { Alert, Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import { supabase } from './supabaseClient';
import { WatchBridge } from 'sk8lytz-watch-bridge';

// ── Types ──────────────────────────────────────────────────────────────────────

/** Live snapshot passed from DockedController on session end. */
export interface ISessionSnapshot {
  durationSec: number;
  distanceMiles: number;
  avgSpeedMph: number;
  peakSpeedMph: number;
  peakGForce: number;
  locationLabel?: string;
  crewSessionId?: string;
  healthBpm?: number;
  healthPeakBpm?: number;
  healthCalories?: number;
}

/**
 * A session record queued for Supabase sync when the user was offline/unauthenticated.
 * user_id is NOT stored here — it is stamped at flush time from the live auth session.
 * calories is pre-computed at queue time so we don't re-derive it later.
 */
export interface PendingSessionRecord extends ISessionSnapshot {
  calories: number;
  queued_at: string; // ISO timestamp
}

/** AsyncStorage key for the offline session queue. */
export const PENDING_SESSION_QUEUE_KEY = '@SK8Lytz_PendingSession_Queue';

/** Soft cap threshold — warn when queue exceeds this (but never discard). */
const PENDING_QUEUE_SOFT_CAP = 50;

/** A single historic skate session row returned from Supabase. */
export interface ISkateSession {
  id: string;
  sessionDate: string;
  durationSec: number;
  distanceMiles: number;
  avgSpeedMph: number;
  peakSpeedMph: number;
  peakGForce: number | null;
  calories: number | null;
  avgBpm: number | null;
  peakBpm: number | null;
  locationLabel: string | null;
}

/** Aggregated lifetime stats derived from all stored sessions. */
export interface ILifetimeStats {
  totalSessions: number;
  totalDistanceMiles: number;
  totalDurationSec: number;
  lifetimePeakSpeedMph: number;
  lifetimeAvgSpeedMph: number;
  lifetimePeakGForce: number;
  lifetimeCalories: number;
  lifetimePeakBpm: number | null;
}

// ── Helpers ────────────────────────────────────────────────────────────────────

/**
 * Estimates calories burned skating using a MET-based formula.
 * MET for inline skating is ~7.5 (moderate) to ~12 (vigorous).
 * Approximation: 70 kg base weight, MET scaled by avg speed.
 *   calories = MET × weight_kg × duration_hours
 */
function estimateCalories(avgSpeedMph: number, durationSec: number): number {
  const MET = avgSpeedMph > 12 ? 12 : avgSpeedMph > 8 ? 9 : 7;
  const hours = durationSec / 3600;
  const weightKg = 70; // canonical approximation — no personal data stored
  return Math.round(MET * weightKg * hours);
}

// ── Service ────────────────────────────────────────────────────────────────────

class SpeedTrackingServiceClass {
  /** Re-entrancy guard — INSERT is non-idempotent, prevents double-row on slow networks. */
  private _isFlushingSessionQueue = false;
  /** Timestamp of last watch metric push — used to throttle to max once/3s */
  private lastWatchSyncMs = 0;
  private readonly WATCH_SYNC_THROTTLE_MS = 3000;
  /**
   * Saves a completed skating session to Supabase.
   * Called by DockedController when user taps "Save Session" in Street Mode.
   *
   * @returns The inserted session ID on success, null on failure.
   */
  async saveSession(snapshot: ISessionSnapshot): Promise<string | null> {
    if (!supabase) {
      AppLogger.log('ERROR_CAUGHT', { message: '[SpeedTrackingService] Supabase not configured.' });
      return null;
    }

    // Do not persist zero-data sessions (e.g. user tapped save immediately)
    if (snapshot.distanceMiles < 0.01 && snapshot.durationSec < 10) {
      return null;
    }

    try {
      const { data: { user } } = await supabase.auth.getUser();

      if (!user) {
        // ── Offline Queue ────────────────────────────────────────────────────────
        // User is unauthenticated. Serialize this session to the local queue so
        // useOfflineSyncWorker can flush it to Supabase once auth is available.
        const calories = snapshot.healthCalories !== undefined && snapshot.healthCalories !== null
          ? snapshot.healthCalories
          : estimateCalories(snapshot.avgSpeedMph, snapshot.durationSec);

        try {
          const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY);
          const queue: PendingSessionRecord[] = raw ? JSON.parse(raw) : [];

          if (queue.length >= PENDING_QUEUE_SOFT_CAP) {
            AppLogger.warn('[SpeedTrackingService] Pending session queue at capacity', { count: queue.length });
          }

          const record: PendingSessionRecord = { ...snapshot, calories, queued_at: new Date().toISOString() };
          queue.push(record);
          await AsyncStorage.setItem(PENDING_SESSION_QUEUE_KEY, JSON.stringify(queue));

          AppLogger.info('[SpeedTrackingService] Session queued for offline sync', { queueLength: queue.length });
        } catch (queueErr: unknown) {
          AppLogger.log('ERROR_CAUGHT', { message: `[SpeedTrackingService] Failed to queue offline session: ${String(queueErr)}` });
        }

        Alert.alert(
          'Session Saved Locally',
          "You're offline — this session will sync automatically when you sign in.",
        );
        return null;
        // ── End Offline Queue ────────────────────────────────────────────────────
      }

      const calories = snapshot.healthCalories !== undefined && snapshot.healthCalories !== null
        ? snapshot.healthCalories
        : estimateCalories(snapshot.avgSpeedMph, snapshot.durationSec);

      const { data, error } = await supabase
        .from('skate_sessions')
        .insert({
          user_id: user.id,
          duration_sec: Math.round(snapshot.durationSec),
          distance_miles: parseFloat(snapshot.distanceMiles.toFixed(3)),
          avg_speed_mph: parseFloat(snapshot.avgSpeedMph.toFixed(2)),
          peak_speed_mph: parseFloat(snapshot.peakSpeedMph.toFixed(2)),
          peak_gforce: parseFloat(snapshot.peakGForce.toFixed(2)),
          calories,
          avg_bpm: snapshot.healthBpm ?? null,
          peak_bpm: snapshot.healthPeakBpm ?? null,
          location_label: snapshot.locationLabel ?? null,
          crew_session_id: snapshot.crewSessionId ?? null,
        })
        .select('id')
        .single();

      if (error) {
        AppLogger.log('ERROR_CAUGHT', { message: `[SpeedTrackingService] Save failed: ${error.message}` });
        return null;
      }

      AppLogger.log('SESSION_SAVED', {
        sessionId: data.id,
        distanceMiles: snapshot.distanceMiles,
        peakSpeedMph: snapshot.peakSpeedMph,
        durationSec: snapshot.durationSec,
      });

      // --- TWO-WAY HEALTH SYNC (Close the Rings) ---
      try {
        const { HealthSyncService } = require('./HealthSyncService');
        const enrichedSnapshot: ISessionSnapshot = {
          ...snapshot,
          healthCalories: calories,
        };
        await HealthSyncService.saveWorkout(enrichedSnapshot);
      } catch (healthErr: any) {
        AppLogger.warn('HEALTH_TELEMETRY', { event: 'health_sync_delegation_failed', error: healthErr.message });
      }

      return data.id;
    } catch (err: any) {
      AppLogger.log('ERROR_CAUGHT', { message: `[SpeedTrackingService] Exception: ${err.message}` });
      return null;
    }
  }

  /**
   * Flushes the offline session queue to Supabase.
   * Called by useOfflineSyncWorker every 60s.
   *
   * Design notes:
   * - user_id is stamped at flush time from the live auth session (not stored in queue)
   * - INSERT is non-idempotent: _isFlushingSessionQueue guard prevents double-rows
   * - Failed records stay in queue for the next flush cycle
   * - No session → return silently, keep queue intact
   */
  async flushPendingSessionQueue(): Promise<void> {
    if (!supabase) return;
    if (this._isFlushingSessionQueue) return;
    this._isFlushingSessionQueue = true;

    try {
      const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY);
      const queue: PendingSessionRecord[] = raw ? JSON.parse(raw) : [];
      if (queue.length === 0) return;

      // Auth check — mirror ScenesService.flushSyncQueue() L342-346 pattern exactly
      const { data: sessionData } = await supabase.auth.getSession();
      if (!sessionData?.session?.user) {
        // Not authenticated — keep queue, retry on next cycle
        return;
      }

      const userId = sessionData.session.user.id;
      const remainingQueue: PendingSessionRecord[] = [];
      let successCount = 0;

      for (const record of queue) {
        try {
          const { error } = await supabase
            .from('skate_sessions')
            .insert({
              user_id: userId,
              duration_sec: Math.round(record.durationSec),
              distance_miles: parseFloat(record.distanceMiles.toFixed(3)),
              avg_speed_mph: parseFloat(record.avgSpeedMph.toFixed(2)),
              peak_speed_mph: parseFloat(record.peakSpeedMph.toFixed(2)),
              peak_gforce: parseFloat(record.peakGForce.toFixed(2)),
              calories: record.calories,
              avg_bpm: record.healthBpm ?? null,
              peak_bpm: record.healthPeakBpm ?? null,
              location_label: record.locationLabel ?? null,
              crew_session_id: record.crewSessionId ?? null,
            });

          if (!error) {
            successCount++;
          } else {
            remainingQueue.push(record);
          }
        } catch {
          // Swallow per-record error to allow remaining records to flush
          remainingQueue.push(record);
        }
      }

      await AsyncStorage.setItem(PENDING_SESSION_QUEUE_KEY, JSON.stringify(remainingQueue));

      if (successCount > 0) {
        AppLogger.info('[SpeedTrackingService] Flushed pending sessions', {
          successCount,
          remaining: remainingQueue.length,
        });
      }
    } catch (e: unknown) {
      AppLogger.log('ERROR_CAUGHT', { message: `[SpeedTrackingService] flushPendingSessionQueue error: ${String(e)}` });
    } finally {
      this._isFlushingSessionQueue = false;
    }
  }

  /**
   * Pushes a live speed update to connected watches.
   * Throttled to max once per 3 seconds to protect watch battery.
   * Call this from DockedController on every GPS location update.
   */
  pushSpeedToWatch(speedMph: number, calories?: number, heartRateBpm?: number): void {
    const now = Date.now();
    if (now - this.lastWatchSyncMs < this.WATCH_SYNC_THROTTLE_MS) return;
    this.lastWatchSyncMs = now;

    WatchBridge.sendMetricUpdate({
      speed:     speedMph,
      calories:  calories,
      heartRate: heartRateBpm,
    }).catch((err: unknown) =>
      AppLogger.warn('WATCH_BRIDGE', { event: 'metric_push_failed', error: String(err) })
    );
  }

  /**
   * Fetches the N most recent sessions for the current user.
   * Returns an empty array on error or if unauthenticated.
   */
  async fetchRecentSessions(limit = 20): Promise<ISkateSession[]> {
    if (!supabase) return [];
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return [];

      const { data, error } = await supabase
        .from('skate_sessions')
        .select('*')
        .eq('user_id', user.id)
        .order('session_date', { ascending: false })
        .limit(limit);

      if (error || !data) return [];

      // Boy Scout: type the mapped row to avoid Record<string, any>
      type SkateSessionRow = {
        id: string; session_date: string; duration_sec: number; distance_miles: number;
        avg_speed_mph: number; peak_speed_mph: number; peak_gforce: number | null;
        calories: number | null; avg_bpm: number | null; peak_bpm: number | null;
        location_label: string | null;
      };
      return (data as unknown as SkateSessionRow[]).map((r) => ({
        id: r.id,
        sessionDate: r.session_date,
        durationSec: r.duration_sec,
        distanceMiles: Number(r.distance_miles),
        avgSpeedMph: Number(r.avg_speed_mph),
        peakSpeedMph: Number(r.peak_speed_mph),
        peakGForce: r.peak_gforce !== null ? Number(r.peak_gforce) : null,
        calories: r.calories,
        avgBpm: r.avg_bpm ?? null,
        peakBpm: r.peak_bpm ?? null,
        locationLabel: r.location_label,
      }));
    } catch {
      return [];
    }
  }

  /**
   * Aggregates lifetime statistics from all stored sessions for the current user.
   */
  async fetchLifetimeStats(): Promise<ILifetimeStats> {
    const empty: ILifetimeStats = {
      totalSessions: 0, totalDistanceMiles: 0, totalDurationSec: 0,
      lifetimePeakSpeedMph: 0, lifetimeAvgSpeedMph: 0, lifetimePeakGForce: 0, lifetimeCalories: 0,
      lifetimePeakBpm: null,
    };
    if (!supabase) return empty;

    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return empty;

      const { data, error } = await supabase
        .from('skate_sessions')
        .select('duration_sec, distance_miles, avg_speed_mph, peak_speed_mph, peak_gforce, calories, peak_bpm')
        .eq('user_id', user.id);

      if (error || !data || data.length === 0) return empty;

      // Boy Scout: type the aggregate row to avoid Record<string, any>
      type AggRow = {
        duration_sec: number; distance_miles: number; avg_speed_mph: number;
        peak_speed_mph: number; peak_gforce: number | null; calories: number | null; peak_bpm: number | null;
      };
      const rows = data as unknown as AggRow[];
      const totalSessions = rows.length;
      const totalDistanceMiles = rows.reduce((s, r) => s + Number(r.distance_miles), 0);
      const totalDurationSec = rows.reduce((s, r) => s + Number(r.duration_sec), 0);
      const lifetimePeakSpeedMph = Math.max(...rows.map((r) => Number(r.peak_speed_mph)));
      const lifetimeAvgSpeedMph = rows.reduce((s, r) => s + Number(r.avg_speed_mph), 0) / totalSessions;
      const lifetimePeakGForce = Math.max(...rows.map((r) => Number(r.peak_gforce ?? 0)));
      const lifetimeCalories = rows.reduce((s, r) => s + (r.calories ?? 0), 0);
      const lifetimePeakBpm = Math.max(...rows.map((r) => Number(r.peak_bpm ?? 0)));

      return {
        totalSessions,
        totalDistanceMiles: parseFloat(totalDistanceMiles.toFixed(2)),
        totalDurationSec,
        lifetimePeakSpeedMph: parseFloat(lifetimePeakSpeedMph.toFixed(1)),
        lifetimeAvgSpeedMph: parseFloat(lifetimeAvgSpeedMph.toFixed(1)),
        lifetimePeakGForce: parseFloat(lifetimePeakGForce.toFixed(1)),
        lifetimeCalories,
        lifetimePeakBpm: lifetimePeakBpm > 0 ? lifetimePeakBpm : null,
      };
    } catch {
      return empty;
    }
  }
}

export const SpeedTrackingService = new SpeedTrackingServiceClass();

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
import { PENDING_SESSION_QUEUE_KEY } from '../constants/storageKeys';

// ── Types ──────────────────────────────────────────────────────────────────────

/** Live snapshot passed from DockedController on session end. */
export interface ISessionSnapshot {
  durationSec: number;
  distanceMiles: number;
  avgSpeedMph: number;
  peakSpeedMph: number;
  peakGForce: number;
  locationLabel?: string;
  locationCoords?: { lat: number; lng: number };
  startCoords?: { lat: number; lng: number };
  endCoords?: { lat: number; lng: number };
  pathCoords?: Array<{ lat: number; lng: number }>;
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
  async saveSession(snapshot: ISessionSnapshot, userId: string | null): Promise<string | null> {
    if (!supabase) {
      AppLogger.log('ERROR_CAUGHT', { message: '[SpeedTrackingService] Supabase not configured.' });
      return null;
    }

    // Do not persist zero-data sessions (e.g. user tapped save immediately)
    if (snapshot.distanceMiles < 0.01 && snapshot.durationSec < 10) {
      return null;
    }

    try {
      if (!userId) {
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
          AppLogger.warn('[SpeedTrackingService] Failed to queue offline session', {
            error: queueErr instanceof Error ? queueErr.message : String(queueErr),
          });
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
          user_id: userId,
          duration_sec: Math.round(snapshot.durationSec),
          distance_miles: parseFloat(snapshot.distanceMiles.toFixed(3)),
          avg_speed_mph: parseFloat(snapshot.avgSpeedMph.toFixed(2)),
          peak_speed_mph: parseFloat(snapshot.peakSpeedMph.toFixed(2)),
          peak_gforce: parseFloat(snapshot.peakGForce.toFixed(2)),
          calories,
          avg_bpm: snapshot.healthBpm ?? null,
          peak_bpm: snapshot.healthPeakBpm ?? null,
          location_label: snapshot.locationLabel ?? null,
          location_coords: snapshot.locationCoords ?? null,
          start_coords: snapshot.startCoords ?? null,
          end_coords: snapshot.endCoords ?? null,
          path_coords: snapshot.pathCoords ?? null,
          crew_session_id: snapshot.crewSessionId ?? null,
        })
        .select('id')
        .single();

      if (error) {
        AppLogger.log('ERROR_CAUGHT', { message: `[SpeedTrackingService] Save failed: ${error instanceof Error ? error.message : String(error)}` });
        try {
          const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY);
          const queue: PendingSessionRecord[] = raw ? JSON.parse(raw) : [];
          if (queue.length >= PENDING_QUEUE_SOFT_CAP) {
            AppLogger.warn('[SpeedTrackingService] Pending session queue at capacity', { count: queue.length });
          }
          const record: PendingSessionRecord = { ...snapshot, calories, queued_at: new Date().toISOString() };
          queue.push(record);
          await AsyncStorage.setItem(PENDING_SESSION_QUEUE_KEY, JSON.stringify(queue));
          AppLogger.info('[SpeedTrackingService] Session queued for offline sync after failed save', { queueLength: queue.length });
        } catch (queueErr: unknown) {
          AppLogger.warn('[SpeedTrackingService] Failed to queue offline session after save failure', {
            error: queueErr instanceof Error ? queueErr.message : String(queueErr),
          });
        }
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
      } catch (healthErr: unknown) {
        AppLogger.warn('HEALTH_TELEMETRY', { event: 'health_sync_delegation_failed', error: healthErr instanceof Error ? healthErr.message : String(healthErr) });
      }

      // --- UPDATE LIFETIME STATS (DRIFT FIX) ---
      try {
        if (userId && (snapshot.distanceMiles > 0 || snapshot.peakSpeedMph > 0)) {
          const { data: profile } = await supabase
            .from('user_profiles')
            .select('lifetime_top_speed_mph, lifetime_distance_miles')
            .eq('user_id', userId)
            .single();
          if (profile) {
            const newDistance = (profile.lifetime_distance_miles || 0) + snapshot.distanceMiles;
            const newTopSpeed = Math.max((profile.lifetime_top_speed_mph || 0), snapshot.peakSpeedMph);
            if (newDistance > (profile.lifetime_distance_miles || 0) || newTopSpeed > (profile.lifetime_top_speed_mph || 0)) {
               await supabase.from('user_profiles').update({
                 lifetime_distance_miles: parseFloat(newDistance.toFixed(3)),
                 lifetime_top_speed_mph: parseFloat(newTopSpeed.toFixed(2))
               }).eq('user_id', userId);
            }
          }
        }
      } catch (statsErr: unknown) {
        AppLogger.warn('STATS_TELEMETRY', { event: 'lifetime_stats_update_failed', error: statsErr instanceof Error ? statsErr.message : String(statsErr) });
      }

      return data.id;
    } catch (err: unknown) {
      AppLogger.warn('[SpeedTrackingService] saveSession exception', {
        error: err instanceof Error ? err.message : String(err),
      });
      if (userId) {
        try {
          const calories = snapshot.healthCalories !== undefined && snapshot.healthCalories !== null
            ? snapshot.healthCalories
            : estimateCalories(snapshot.avgSpeedMph, snapshot.durationSec);
          const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY);
          const queue: PendingSessionRecord[] = raw ? JSON.parse(raw) : [];
          if (queue.length >= PENDING_QUEUE_SOFT_CAP) {
            AppLogger.warn('[SpeedTrackingService] Pending session queue at capacity', { count: queue.length });
          }
          const record: PendingSessionRecord = { ...snapshot, calories, queued_at: new Date().toISOString() };
          queue.push(record);
          await AsyncStorage.setItem(PENDING_SESSION_QUEUE_KEY, JSON.stringify(queue));
          AppLogger.info('[SpeedTrackingService] Session queued for offline sync after save exception', { queueLength: queue.length });
        } catch (queueErr: unknown) {
          AppLogger.warn('[SpeedTrackingService] Failed to queue offline session after save exception', {
            error: queueErr instanceof Error ? queueErr.message : String(queueErr),
          });
        }
      }
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
  async flushPendingSessionQueue(userId: string | null): Promise<void> {
    if (!supabase) return;
    if (this._isFlushingSessionQueue) return;
    this._isFlushingSessionQueue = true;

    try {
      const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY);
      const queue: PendingSessionRecord[] = raw ? JSON.parse(raw) : [];
      if (queue.length === 0) return;

      if (!userId) {
        // Not authenticated — keep queue, retry on next cycle
        return;
      }
      const remainingQueue: PendingSessionRecord[] = [];
      let successCount = 0;
      let totalFlushedDistance = 0;
      let maxFlushedTopSpeed = 0;

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
              location_coords: record.locationCoords ?? null,
              start_coords: record.startCoords ?? null,
              end_coords: record.endCoords ?? null,
              path_coords: record.pathCoords ?? null,
              crew_session_id: record.crewSessionId ?? null,
            });

          if (!error) {
            successCount++;
            totalFlushedDistance += record.distanceMiles;
            if (record.peakSpeedMph > maxFlushedTopSpeed) {
              maxFlushedTopSpeed = record.peakSpeedMph;
            }
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
        
        // --- UPDATE LIFETIME STATS (DRIFT FIX) ---
        if (userId && (totalFlushedDistance > 0 || maxFlushedTopSpeed > 0)) {
          try {
            const { data: profile } = await supabase
              .from('user_profiles')
              .select('lifetime_top_speed_mph, lifetime_distance_miles')
              .eq('user_id', userId)
              .single();
            if (profile) {
              const newDistance = (profile.lifetime_distance_miles || 0) + totalFlushedDistance;
              const newTopSpeed = Math.max((profile.lifetime_top_speed_mph || 0), maxFlushedTopSpeed);
              if (newDistance > (profile.lifetime_distance_miles || 0) || newTopSpeed > (profile.lifetime_top_speed_mph || 0)) {
                 await supabase.from('user_profiles').update({
                   lifetime_distance_miles: parseFloat(newDistance.toFixed(3)),
                   lifetime_top_speed_mph: parseFloat(newTopSpeed.toFixed(2))
                 }).eq('user_id', userId);
              }
            }
          } catch (e: unknown) {
            AppLogger.warn('[SpeedTrackingService] Lifetime stats update failed during queue flush', {
              error: e instanceof Error ? e.message : String(e),
            });
          }
        }
      }
    } catch (e: unknown) {
      AppLogger.warn('[SpeedTrackingService] flushPendingSessionQueue error', {
        error: e instanceof Error ? e.message : String(e),
      });
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
      AppLogger.warn('WATCH_BRIDGE', { event: 'metric_push_failed', error: (err instanceof Error ? err.message : String(err)) })
    );
  }

  /**
   * Fetches the N most recent sessions for the current user.
   * Returns an empty array on error or if unauthenticated.
   */
  async fetchRecentSessions(userId: string | null, limit = 20): Promise<ISkateSession[]> {
    if (!supabase) return [];
    try {
      if (!userId) return [];

      // Boy Scout: type the mapped row to avoid Record<string, any>
      type SkateSessionRow = {
        id: string; session_date: string; duration_sec: number; distance_miles: number;
        avg_speed_mph: number; peak_speed_mph: number; peak_gforce: number | null;
        calories: number | null; avg_bpm: number | null; peak_bpm: number | null;
        location_label: string | null;
      };
      
      const { data, error } = await supabase
        .from('skate_sessions')
        .select('*')
        .eq('user_id', userId)
        .order('session_date', { ascending: false })
        .limit(limit)
        .returns<SkateSessionRow[]>();

      if (error || !data) return this.getCachedRecentSessions(userId);

      const mapped = data.map((r) => ({
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
      if (mapped.length > 0) {
        try {
          await AsyncStorage.setItem(`@sk8lytz_recent_sessions_${userId}`, JSON.stringify(mapped));
        } catch (e) {}
      }
      return mapped.length > 0 ? mapped : await this.getCachedRecentSessions(userId);
    } catch (e: unknown) {
      AppLogger.warn('[SpeedTrackingService] fetchRecentSessions failed — falling back to offline queue', {
        error: e instanceof Error ? e.message : String(e),
      });
      return userId ? this.getCachedRecentSessions(userId) : this._getOfflineFallbackSessions();
    }
  }

  async getCachedRecentSessions(userId: string): Promise<ISkateSession[]> {
    try {
      const raw = await AsyncStorage.getItem(`@sk8lytz_recent_sessions_${userId}`);
      const cached: ISkateSession[] = raw ? JSON.parse(raw) : [];
      const pending = await this._getOfflineFallbackSessions();
      const combined = [...pending, ...cached].sort((a, b) => new Date(b.sessionDate).getTime() - new Date(a.sessionDate).getTime());
      return combined;
    } catch {
      return this._getOfflineFallbackSessions();
    }
  }

  // NOTE: Requires PLAN-fix-offline-session-persistence-queue to fully work.
  private async _getOfflineFallbackSessions(): Promise<ISkateSession[]> {
    try {
      const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY);
      if (!raw) return [];
      const queue: PendingSessionRecord[] = JSON.parse(raw);
      return queue.map(r => ({
        id: `offline_${r.queued_at}`,
        sessionDate: r.queued_at,
        durationSec: r.durationSec,
        distanceMiles: r.distanceMiles,
        avgSpeedMph: r.avgSpeedMph,
        peakSpeedMph: r.peakSpeedMph,
        peakGForce: r.peakGForce,
        calories: r.calories,
        avgBpm: r.healthBpm ?? null,
        peakBpm: r.healthPeakBpm ?? null,
        locationLabel: r.locationLabel ?? null
      }));
    } catch (e: unknown) {
      AppLogger.warn('[SpeedTrackingService] _getOfflineFallbackSessions: failed to parse offline queue', {
        error: e instanceof Error ? e.message : String(e),
      });
      return [];
    }
  }

  /**
   * Aggregates lifetime statistics from all stored sessions for the current user.
   */
  async fetchLifetimeStats(userId: string | null): Promise<ILifetimeStats> {
    const empty: ILifetimeStats = {
      totalSessions: 0, totalDistanceMiles: 0, totalDurationSec: 0,
      lifetimePeakSpeedMph: 0, lifetimeAvgSpeedMph: 0, lifetimePeakGForce: 0, lifetimeCalories: 0,
      lifetimePeakBpm: null,
    };
    if (!supabase) return empty;

    try {
      if (!userId) return empty;

      // Boy Scout: type the aggregate row to avoid Record<string, any>
      type AggRow = {
        duration_sec: number; distance_miles: number; avg_speed_mph: number;
        peak_speed_mph: number; peak_gforce: number | null; calories: number | null; peak_bpm: number | null;
      };
      
      const { data, error } = await supabase
        .from('skate_sessions')
        .select('duration_sec, distance_miles, avg_speed_mph, peak_speed_mph, peak_gforce, calories, peak_bpm')
        .eq('user_id', userId)
        .returns<AggRow[]>();

      // Fetch cached lifetime stats to prevent drift when sessions are archived/deleted
      let cachedDistance = 0;
      let cachedTopSpeed = 0;
      try {
        const { data: profile } = await supabase
          .from('user_profiles')
          .select('lifetime_distance_miles, lifetime_top_speed_mph')
          .eq('user_id', userId)
          .single();
        if (profile) {
          cachedDistance = profile.lifetime_distance_miles || 0;
          cachedTopSpeed = profile.lifetime_top_speed_mph || 0;
        }
      } catch (e: unknown) {
        AppLogger.warn('[SpeedTrackingService] fetchLifetimeStats: user_profiles cache read failed', {
          error: e instanceof Error ? e.message : String(e),
        });
      }

      if (error || !data || data.length === 0) {
        const cached = await this.getCachedLifetimeStats(userId);
        if (cached) return cached;
        return {
          ...empty,
          totalDistanceMiles: cachedDistance,
          lifetimePeakSpeedMph: cachedTopSpeed
        };
      }

      const rows = data;
      const totalSessions = rows.length;
      const computedDistance = rows.reduce((s, r) => s + Number(r.distance_miles), 0);
      const totalDurationSec = rows.reduce((s, r) => s + Number(r.duration_sec), 0);
      const computedTopSpeed = Math.max(...rows.map((r) => Number(r.peak_speed_mph)));
      const lifetimeAvgSpeedMph = rows.reduce((s, r) => s + Number(r.avg_speed_mph), 0) / totalSessions;
      const lifetimePeakGForce = Math.max(...rows.map((r) => Number(r.peak_gforce ?? 0)));
      const lifetimeCalories = rows.reduce((s, r) => s + (r.calories ?? 0), 0);
      const lifetimePeakBpm = Math.max(...rows.map((r) => Number(r.peak_bpm ?? 0)));

      const result = {
        totalSessions,
        totalDistanceMiles: parseFloat(Math.max(computedDistance, cachedDistance).toFixed(2)),
        totalDurationSec,
        lifetimePeakSpeedMph: parseFloat(Math.max(computedTopSpeed, cachedTopSpeed).toFixed(1)),
        lifetimeAvgSpeedMph: parseFloat(lifetimeAvgSpeedMph.toFixed(1)),
        lifetimePeakGForce: parseFloat(lifetimePeakGForce.toFixed(1)),
        lifetimeCalories,
        lifetimePeakBpm: lifetimePeakBpm > 0 ? lifetimePeakBpm : null,
      };

      try {
        await AsyncStorage.setItem(`@sk8lytz_lifetime_stats_${userId}`, JSON.stringify(result));
      } catch (e) {}

      return result;
    } catch (e: unknown) {
      AppLogger.warn('[SpeedTrackingService] fetchLifetimeStats failed', {
        error: e instanceof Error ? e.message : String(e),
      });
      const cached = userId ? await this.getCachedLifetimeStats(userId) : null;
      if (cached) return cached;
      return empty;
    }
  }

  async getCachedLifetimeStats(userId: string): Promise<ILifetimeStats | null> {
    try {
      const raw = await AsyncStorage.getItem(`@sk8lytz_lifetime_stats_${userId}`);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }
}

export const SpeedTrackingService = new SpeedTrackingServiceClass();

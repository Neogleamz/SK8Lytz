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
 * All functions are null-safe for web (where supabase is available but
 * GPS/accelerometer data may be zero).
 *
 * Platform: React Native (Android + iOS) + Web-safe fallbacks
 */
import { supabase } from './supabaseClient';
import { AppLogger } from './AppLogger';

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
}

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
  locationLabel: string | null;
}

/** Aggregated lifetime stats derived from all stored sessions. */
export interface ILifetimeStats {
  totalSessions: number;
  totalDistanceMiles: number;
  totalDurationSec: number;
  lifetimePeakSpeedMph: number;
  lifetimeAvgSpeedMph: number;
  lifetimeCalories: number;
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
      if (!user) return null;

      const calories = estimateCalories(snapshot.avgSpeedMph, snapshot.durationSec);

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

      return data.id;
    } catch (err: any) {
      AppLogger.log('ERROR_CAUGHT', { message: `[SpeedTrackingService] Exception: ${err.message}` });
      return null;
    }
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

      return data.map((r: Record<string, any>) => ({
        id: r.id,
        sessionDate: r.session_date,
        durationSec: r.duration_sec,
        distanceMiles: Number(r.distance_miles),
        avgSpeedMph: Number(r.avg_speed_mph),
        peakSpeedMph: Number(r.peak_speed_mph),
        peakGForce: r.peak_gforce !== null ? Number(r.peak_gforce) : null,
        calories: r.calories,
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
      lifetimePeakSpeedMph: 0, lifetimeAvgSpeedMph: 0, lifetimeCalories: 0,
    };
    if (!supabase) return empty;

    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return empty;

      const { data, error } = await supabase
        .from('skate_sessions')
        .select('duration_sec, distance_miles, avg_speed_mph, peak_speed_mph, calories')
        .eq('user_id', user.id);

      if (error || !data || data.length === 0) return empty;

      const totalSessions = data.length;
      const totalDistanceMiles = data.reduce((s: number, r: Record<string, any>) => s + Number(r.distance_miles), 0);
      const totalDurationSec = data.reduce((s: number, r: Record<string, any>) => s + Number(r.duration_sec), 0);
      const lifetimePeakSpeedMph = Math.max(...data.map((r: Record<string, any>) => Number(r.peak_speed_mph)));
      const lifetimeAvgSpeedMph = data.reduce((s: number, r: Record<string, any>) => s + Number(r.avg_speed_mph), 0) / totalSessions;
      const lifetimeCalories = data.reduce((s: number, r: Record<string, any>) => s + (r.calories ?? 0), 0);

      return {
        totalSessions,
        totalDistanceMiles: parseFloat(totalDistanceMiles.toFixed(2)),
        totalDurationSec,
        lifetimePeakSpeedMph: parseFloat(lifetimePeakSpeedMph.toFixed(1)),
        lifetimeAvgSpeedMph: parseFloat(lifetimeAvgSpeedMph.toFixed(1)),
        lifetimeCalories,
      };
    } catch {
      return empty;
    }
  }
}

export const SpeedTrackingService = new SpeedTrackingServiceClass();

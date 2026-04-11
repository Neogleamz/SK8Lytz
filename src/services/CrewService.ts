/**
 * CrewService.ts — SK8Lytz Crew Sync
 *
 * Manages real-time crew session lifecycle:
 *   - Create / Join / Leave crew sessions via Supabase DB
 *   - Broadcast scene changes (leader → members) via Supabase Realtime
 *   - Auto-rejoin last active session on app relaunch (AsyncStorage TTL)
 *
 * Channel format:  crew:{sessionId}
 * Broadcast event: scene_update
 * Payload:         { scene: CapturedScene, leader_id: string, ts: number }
 */

import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import type { RealtimeChannel } from '@supabase/supabase-js';
import { AppLogger } from './AppLogger';

// ─── Types ───────────────────────────────────────────────────────────────────

export interface CrewSession {
  id: string;
  invite_code: string;
  name: string;
  leader_user_id: string;
  created_at: string;
  expires_at: string;
  is_active: boolean;
  last_scene?: Record<string, any> | null;
  member_count?: number;      // populated by fetchActiveSessions
  // Migration 006 fields
  status?: 'active' | 'scheduled' | 'ended';
  location_label?: string;
  location_coords?: { lat: number; lng: number };
  scheduled_at?: string;
  ended_at?: string;
  // Migration 007 fields
  is_public?: boolean;  // true = anyone nearby can join without a code
  // Migration 008 fields
  crew_id?: string | null;  // FK to permanent crews table — enables reliable crew↔session matching
}

export interface CrewMember {
  id: string;
  session_id: string;
  user_id: string;
  display_name: string;
  joined_at: string;
}

export interface CrewScenePayload {
  scene: Record<string, any>;
  leader_id: string;
  ts: number;
}

export type CrewRole = 'leader' | 'member' | null;

export interface SessionTelemetryData {
  distanceMiles: number;
  topSpeedMph: number;
  avgSpeedSamples: number[];
}

// ─── AsyncStorage Keys ───────────────────────────────────────────────────────

const STORAGE_LAST_SESSION_ID  = 'ng_crew_last_session_id';
const STORAGE_LAST_SESSION_EXP = 'ng_crew_last_session_exp';
const MAX_MEMBERS_PER_SESSION  = 20;

// ─── Service ─────────────────────────────────────────────────────────────────

class CrewService {
  private channel: RealtimeChannel | null = null;
  private broadcastTimer: ReturnType<typeof setTimeout> | null = null;
  /** The session ID this service instance is currently tracking. */
  public currentSessionId: string | null = null;
  /** The role of the signed-in user in the current session. Set by createSession/joinSession. */
  public currentRole: CrewRole = null;
  /** Temporary in-memory stats aggregated by DockedController */
  public sessionTelemetry: SessionTelemetryData = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };

  // ── Session management ────────────────────────────────────────────────────

  /** Create a new crew session with optional geo location. Caller becomes the leader. */
  async createSession(
    name: string,
    displayName: string,
    opts?: { locationLabel?: string; locationCoords?: { lat: number; lng: number }; scheduledAt?: string; isPublic?: boolean; crewId?: string }
  ): Promise<CrewSession> {
    const user = (await supabase.auth.getUser()).data.user;
    if (!user) throw new Error('Must be signed in to create a crew');

    // Proactively end any previous active sessions by this leader
    await this.cleanupLegacySessions(user.id);

    const insertData: Record<string, any> = {
      name,
      leader_user_id: user.id,
      status: opts?.scheduledAt ? 'scheduled' : 'active',
    };
    if (opts?.locationLabel)  insertData.location_label  = opts.locationLabel;
    if (opts?.locationCoords) insertData.location_coords = opts.locationCoords;
    if (opts?.scheduledAt)    insertData.scheduled_at    = opts.scheduledAt;
    if (opts?.isPublic !== undefined) insertData.is_public = opts.isPublic;
    if (opts?.crewId)         insertData.crew_id         = opts.crewId;

    const { data, error } = await supabase
      .from('crew_sessions')
      .insert(insertData)
      .select()
      .single();

    if (error) throw error;
    const session = data as CrewSession;

    // Auto-join as leader member
    await supabase.from('crew_members').insert({
      session_id: session.id,
      user_id: user.id,
      display_name: displayName || 'Leader',
    });

    await this._persistSession(session);
    this.currentSessionId = session.id;
    this.currentRole = 'leader';
    this.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
    return session;
  }

  /** Proactively deactivate any existing active sessions by this user. */
  async cleanupLegacySessions(userId: string): Promise<void> {
    const { error } = await supabase
      .from('crew_sessions')
      .update({
        is_active: false,
        status: 'ended',
        ended_at: new Date().toISOString(),
      })
      .eq('leader_user_id', userId)
      .eq('is_active', true);

    if (error) {
      console.warn('[CrewService] cleanupLegacySessions failed:', error.message);
      AppLogger.log('CREW_ERROR', { action: 'cleanupLegacySessions', error: error.message });
    }
  }

  /** Join an existing session by 6-char invite code. */
  async joinSession(inviteCode: string, displayName: string): Promise<CrewSession> {
    const user = (await supabase.auth.getUser()).data.user;
    if (!user) throw new Error('Must be signed in to join a crew');

    const code = inviteCode.trim().toUpperCase();

    const { data: session, error: sessionErr } = await supabase
      .from('crew_sessions')
      .select('*')
      .eq('invite_code', code)
      .eq('is_active', true)
      .gt('expires_at', new Date().toISOString())
      .single();

    if (sessionErr || !session) throw new Error('Crew not found or session expired');

    // Check capacity
    const { count } = await supabase
      .from('crew_members')
      .select('id', { count: 'exact', head: true })
      .eq('session_id', session.id);

    if ((count ?? 0) >= MAX_MEMBERS_PER_SESSION) {
      throw new Error('Crew is full (max 20 members)');
    }

    // Upsert membership (idempotent re-join)
    const { error: memberErr } = await supabase.from('crew_members').upsert({
      session_id: session.id,
      user_id: user.id,
      display_name: displayName || 'Skater',
    }, { onConflict: 'session_id,user_id' });

    if (memberErr) throw memberErr;

    await this._persistSession(session);
    this.currentSessionId = session.id;
    this.currentRole = user.id === session.leader_user_id ? 'leader' : 'member';
    this.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
    return session as CrewSession;
  }

  /** Join a session directly by ID (from the active sessions browser). */
  async joinSessionById(sessionId: string, displayName: string): Promise<CrewSession> {
    const user = (await supabase.auth.getUser()).data.user;
    if (!user) throw new Error('Must be signed in to join a crew');

    const { data: session, error } = await supabase
      .from('crew_sessions')
      .select('*')
      .eq('id', sessionId)
      .eq('is_active', true)
      .gt('expires_at', new Date().toISOString())
      .single();

    if (error || !session) throw new Error('Session not found or expired');

    const { count } = await supabase
      .from('crew_members')
      .select('id', { count: 'exact', head: true })
      .eq('session_id', sessionId);
    if ((count ?? 0) >= MAX_MEMBERS_PER_SESSION) throw new Error('Crew is full');

    await supabase.from('crew_members').upsert({
      session_id: sessionId,
      user_id: user.id,
      display_name: displayName,
    }, { onConflict: 'session_id,user_id' });

    await this._persistSession(session);
    this.currentSessionId = sessionId;
    this.currentRole = user.id === session.leader_user_id ? 'leader' : 'member';
    this.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
    return session as CrewSession;
  }

  /**
   * Fetch all currently active crew sessions (for browse UI).
   * Also returns location_label and status with each session.
   */
  async fetchActiveSessions(): Promise<CrewSession[]> {
    const { data, error } = await supabase
      .from('crew_sessions')
      .select('*, crew_members(count)')
      .eq('is_active', true)
      .gt('expires_at', new Date().toISOString())
      .order('created_at', { ascending: false })
      .limit(20);

    if (error || !data) return [];

    return data.map((s: CrewSession & { crew_members: { count: number }[] }) => ({
      ...s,
      member_count: s.crew_members?.[0]?.count ?? 0,
    } as CrewSession));
  }

  /**
   * Fetch PUBLIC sessions only (open to anyone, no invite code needed).
   * Used for location-based discovery browse. Requires migration 007.
   */
  async fetchPublicSessions(): Promise<CrewSession[]> {
    try {
      // Try the public_sessions view first (migration 007)
      const { data, error } = await supabase
        .from('public_sessions')
        .select('*')
        .limit(30);

      if (!error && data) {
        return data.map((s: any) => ({
          ...s,
          is_public: true,
        })) as CrewSession[];
      }
    } catch {}

    // Fallback: filter from crew_sessions directly
    const { data } = await supabase
      .from('crew_sessions')
      .select('*, crew_members(count)')
      .eq('is_active', true)
      .eq('is_public', true)
      .gt('expires_at', new Date().toISOString())
      .order('created_at', { ascending: false })
      .limit(20);

    return (data ?? []).map((s: CrewSession & { crew_members: { count: number }[] }) => ({
      ...s,
      member_count: s.crew_members?.[0]?.count ?? 0,
    } as CrewSession));
  }

  /**
   * End the session (leader only).
   * Accepts an explicit sessionId override in case the singleton state is stale
   * (e.g. modal was rebuilt after a navigation change).
   */
  async endSession(explicitSessionId?: string): Promise<void> {
    const user = (await supabase.auth.getUser()).data.user;
    if (!user) throw new Error('Not authenticated');

    const sessionId = explicitSessionId ?? this.currentSessionId;
    if (!sessionId) throw new Error('No active session to end');

    console.log('[CrewService] endSession called', { sessionId, userId: user.id });

    // ── Single update filtered by id AND leader_user_id ──────────────────────
    // RLS-safe: Supabase only matches rows the policy allows.
    // If count === 0, this user is not the leader (or session already ended).
    // We try the full update (needs migration 006: status + ended_at columns).
    const avgSpeed = this.sessionTelemetry.avgSpeedSamples.length
      ? this.sessionTelemetry.avgSpeedSamples.reduce((a, b) => a + b, 0) / this.sessionTelemetry.avgSpeedSamples.length
      : 0;

    const { error: fullError, data: fullData } = await supabase
      .from('crew_sessions')
      .update({
        status: 'ended',
        is_active: false,
        ended_at: new Date().toISOString(),
        top_speed_mph: this.sessionTelemetry.topSpeedMph || 0,
        avg_speed_mph: avgSpeed || 0,
        total_distance_miles: this.sessionTelemetry.distanceMiles || 0,
      })
      .eq('id', sessionId)
      .eq('leader_user_id', user.id)   // ← RLS-safe leader gate
      .select('id');

    if (fullError) {
      console.warn('[CrewService] Full update failed, trying fallback:', fullError.message);
      // Fallback: just flip is_active — works even without migration 006
      const { error: fallbackError, data: fallbackData } = await supabase
        .from('crew_sessions')
        .update({ is_active: false })
        .eq('id', sessionId)
        .eq('leader_user_id', user.id)
        .select('id');

      if (fallbackError) throw new Error(`Could not end session: ${fallbackError.message}`);
      if (!fallbackData || fallbackData.length === 0) {
        throw new Error('Only the session leader can end the session');
      }
    } else if (!fullData || fullData.length === 0) {
      // 0 rows → either not leader or session doesn't exist
      // Try fallback before giving up (handles column-missing scenario)
      const { error: fbErr, data: fbData } = await supabase
        .from('crew_sessions')
        .update({ is_active: false })
        .eq('id', sessionId)
        .eq('leader_user_id', user.id)
        .select('id');
      if (fbErr || !fbData || fbData.length === 0) {
        throw new Error('Only the session leader can end the session');
      }
    }

    console.log('[CrewService] DB update succeeded, broadcasting session_ended');

    // Broadcast END event so members know the session is over.
    // IMPORTANT: Do NOT call unsubscribe() immediately after send() — the Realtime
    // channel needs ~500ms for the broadcast to propagate before the socket closes.
    this.channel?.send({
      type: 'broadcast',
      event: 'session_ended',
      payload: { sessionId },
    });

    // Clean up all crew_members rows for this session (no CASCADE in schema)
    // Fire-and-forget: not critical if this fails — records expire naturally
    supabase
      .from('crew_members')
      .delete()
      .eq('session_id', sessionId)
      .then(() => console.log('[CrewService] crew_members cleaned up for session', sessionId))
      .catch((e: any) => console.warn('[CrewService] crew_members cleanup failed:', e.message));

    // Delay channel teardown so the session_ended broadcast can propagate to members
    const channelRef = this.channel;
    this.channel = null;
    this.currentSessionId = null;
    this.currentRole = null;
    setTimeout(() => {
      if (channelRef) supabase.removeChannel(channelRef);
      console.log('[CrewService] channel torn down after broadcast delay');
    }, 600);

    if (this.broadcastTimer) { clearTimeout(this.broadcastTimer); this.broadcastTimer = null; }
    await AsyncStorage.multiRemove([STORAGE_LAST_SESSION_ID, STORAGE_LAST_SESSION_EXP]);
    AppLogger.log('CREW_SESSION_ENDED', { reason: 'leader_ended', sessionId });
    console.log('[CrewService] endSession complete');
  }

  /** Fetch the last scene snapshot from a session (for late-arrival sync). */
  async fetchLastScene(sessionId: string): Promise<Record<string, any> | null> {
    const { data } = await supabase
      .from('crew_sessions')
      .select('last_scene')
      .eq('id', sessionId)
      .single();
    return data?.last_scene ?? null;
  }

  /** Leave current session and unsubscribe. */
  async leaveSession(): Promise<void> {
    const user = (await supabase.auth.getUser()).data.user;
    if (!user || !this.currentSessionId) return;

    await supabase
      .from('crew_members')
      .delete()
      .eq('session_id', this.currentSessionId)
      .eq('user_id', user.id);

    this.unsubscribe();
    await AsyncStorage.multiRemove([STORAGE_LAST_SESSION_ID, STORAGE_LAST_SESSION_EXP]);
    this.currentSessionId = null;
    this.currentRole = null;
  }

  /** Transfer leadership to another crew member. Leader only. */
  async transferLeadership(newLeaderId: string): Promise<void> {
    if (!this.currentSessionId || this.currentRole !== 'leader') return;

    const { error } = await supabase
      .from('crew_sessions')
      .update({ leader_user_id: newLeaderId })
      .eq('id', this.currentSessionId);

    if (error) throw error;
    this.currentRole = 'member';
  }

  /** Fetch all members for a session (for lobby UI). */
  async fetchMembers(sessionId: string): Promise<CrewMember[]> {
    const { data, error } = await supabase
      .from('crew_members')
      .select('*')
      .eq('session_id', sessionId)
      .order('joined_at', { ascending: true });

    if (error) throw error;
    return (data ?? []) as CrewMember[];
  }

  // ── Auto-rejoin ───────────────────────────────────────────────────────────

  /**
   * Check if there's a persisted active session from a previous launch.
   * Returns the session if still valid, null otherwise.
   */
  async tryAutoRejoin(displayName: string): Promise<{ session: CrewSession; role: CrewRole } | null> {
    try {
      const [savedId, savedExp] = await AsyncStorage.multiGet([
        STORAGE_LAST_SESSION_ID,
        STORAGE_LAST_SESSION_EXP,
      ]);
      const sessionId  = savedId[1];
      const expiresAt  = savedExp[1];

      if (!sessionId || !expiresAt) return null;
      if (new Date(expiresAt) < new Date()) {
        await AsyncStorage.multiRemove([STORAGE_LAST_SESSION_ID, STORAGE_LAST_SESSION_EXP]);
        return null;
      }

      const user = (await supabase.auth.getUser()).data.user;
      if (!user) return null;

      const { data: session } = await supabase
        .from('crew_sessions')
        .select('*')
        .eq('id', sessionId)
        .eq('is_active', true)
        .single();

      if (!session) return null;

      // Ensure membership still exists
      const { data: membership } = await supabase
        .from('crew_members')
        .select('id')
        .eq('session_id', sessionId)
        .eq('user_id', user.id)
        .single();

      if (!membership) {
        // Re-join as member
        await supabase.from('crew_members').upsert({
          session_id: sessionId,
          user_id: user.id,
          display_name: displayName,
        }, { onConflict: 'session_id,user_id' });
      }

      this.currentSessionId = sessionId;
      this.currentRole = user.id === session.leader_user_id ? 'leader' : 'member';
      this.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
      return { session: session as CrewSession, role: this.currentRole };
    } catch {
      return null;
    }
  }

  // ── Realtime ──────────────────────────────────────────────────────────────

  /**
   * Subscribe as LEADER: listens for member presence events (for lobby count).
   * Scene broadcasting is done via broadcastScene().
   */
  subscribeAsLeader(
    sessionId: string,
    onMemberChange: (members: CrewMember[]) => void,
  ): void {
    this._ensureUnsubscribed();

    this.channel = supabase
      .channel(`crew:${sessionId}`, { config: { broadcast: { self: false } } })
      .on('broadcast', { event: 'member_update' }, () => {
        this.fetchMembers(sessionId).then(onMemberChange);
      })
      .subscribe();
  }

  /**
   * Subscribe as MEMBER: receives scene_update broadcasts from the leader
   * and calls onScene with the payload.
   */
  subscribeAsMember(
    sessionId: string,
    onScene: (scene: Record<string, any>) => void,
    onSessionEnded?: () => void,
  ): void {
    this._ensureUnsubscribed();

    this.channel = supabase
      .channel(`crew:${sessionId}`, { config: { broadcast: { self: false } } })
      .on('broadcast', { event: 'scene_update' }, (payload: { payload: CrewScenePayload }) => {
        const crewPayload = payload.payload as CrewScenePayload;
        if (crewPayload?.scene) onScene(crewPayload.scene);
      })
      .on('broadcast', { event: 'session_ended' }, () => {
        AppLogger.log('CREW_SESSION_LEFT', { reason: 'leader_ended_session', role: 'member' });
        this.unsubscribe();
        this.currentSessionId = null;
        this.currentRole = null;
        AsyncStorage.multiRemove([STORAGE_LAST_SESSION_ID, STORAGE_LAST_SESSION_EXP]);
        onSessionEnded?.();
      })
      .subscribe();
  }

  /**
   * Broadcast the current scene to all crew members.
   * Debounced 150ms to avoid flooding on slider drags.
   * Also persists last_scene to DB (throttled 5s) for late-arrival sync.
   */
  broadcastScene(scene: Record<string, any>): void {
    if (!this.channel || this.currentRole !== 'leader') return;

    if (this.broadcastTimer) clearTimeout(this.broadcastTimer);
    this.broadcastTimer = setTimeout(async () => {
      const { data: { user } } = await supabase.auth.getUser();
      const leaderId = user?.id ?? '';
      this.channel?.send({
        type: 'broadcast',
        event: 'scene_update',
        payload: {
          scene,
          leader_id: leaderId,
          ts: Date.now(),
        } satisfies CrewScenePayload,
      });
      // Persist last_scene for late-arrival sync (throttled)
      this._persistLastScene(scene);
    }, 150);
  }

  private _lastScenePersistTimer: ReturnType<typeof setTimeout> | null = null;

  /** Write last_scene to crew_sessions row, throttled to once per 5 seconds. */
  private _persistLastScene(scene: Record<string, any>): void {
    if (!this.currentSessionId || this.currentRole !== 'leader') return;
    if (this._lastScenePersistTimer) return; // already scheduled
    this._lastScenePersistTimer = setTimeout(async () => {
      this._lastScenePersistTimer = null;
      if (!this.currentSessionId) return;
      await supabase
        .from('crew_sessions')
        .update({ last_scene: scene })
        .eq('id', this.currentSessionId);
    }, 5000);
  }

  /** Unsubscribe from current channel and clean up timer. */
  unsubscribe(): void {
    if (this.broadcastTimer) {
      clearTimeout(this.broadcastTimer);
      this.broadcastTimer = null;
    }
    if (this.channel) {
      supabase.removeChannel(this.channel);
      this.channel = null;
    }
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  get sessionId() { return this.currentSessionId; }
  get role() { return this.currentRole; }
  get isInCrew() { return this.currentSessionId !== null; }

  private async _persistSession(session: CrewSession): Promise<void> {
    await AsyncStorage.multiSet([
      [STORAGE_LAST_SESSION_ID,  session.id],
      [STORAGE_LAST_SESSION_EXP, session.expires_at],
    ]);
  }

  private _ensureUnsubscribed(): void {
    if (this.channel) {
      supabase.removeChannel(this.channel);
      this.channel = null;
    }
  }
}

export const crewService = new CrewService();

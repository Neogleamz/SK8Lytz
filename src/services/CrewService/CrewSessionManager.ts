import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../supabaseClient';
import { AppLogger } from '../appLogger';
import { CrewSession, CrewMember } from './types';
import type { Database } from '../../types/supabase';
import type { CrewService } from './CrewService';

// ── Timing constants (R-16: no magic numbers) ─────────────────────────────
const CHANNEL_TEARDOWN_DELAY_MS = 600;

export const MAX_MEMBERS_PER_SESSION = 20;

export class CrewSessionManager {
  constructor(private service: CrewService) {}

  async createSession(
    name: string,
    displayName: string,
    opts?: { locationLabel?: string; locationCoords?: { lat: number; lng: number }; scheduledAt?: string; isPublic?: boolean; crewId?: string; skateSpotId?: string },
    userId?: string
  ): Promise<CrewSession> {
    try {
      if (!userId) throw new Error('Must be signed in to create a crew');

      await this.cleanupLegacySessions(userId);

      const insertData: Partial<Database['public']['Tables']['crew_sessions']['Insert']> & Record<string, unknown> = {
        name,
        leader_user_id: userId,
        status: opts?.scheduledAt ? 'scheduled' : 'active',
      };
      if (opts?.locationLabel)  insertData.location_label  = opts.locationLabel;
      if (opts?.locationCoords) insertData.location_coords = opts.locationCoords;
      if (opts?.scheduledAt)    insertData.scheduled_at    = opts.scheduledAt;
      if (opts?.isPublic !== undefined) insertData.is_public = opts.isPublic;
      if (opts?.crewId)         insertData.crew_id         = opts.crewId;
      if (opts?.skateSpotId)    insertData.skate_spot_id   = opts.skateSpotId;

      const { data, error } = await supabase
        .from('crew_sessions')
        .insert(insertData as Database['public']['Tables']['crew_sessions']['Insert'])
        .select()
        .single();

      if (error) throw error;
      const session = data as CrewSession;

      await supabase.from('crew_members').insert({
        session_id: session.id,
        user_id: userId,
        display_name: displayName || 'Leader',
      });

      await this.service._persistSession(session);
      this.service.currentSession = session;
      this.service.currentSessionId = session.id;
      this.service.currentRole = 'leader';
      this.service.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
      this.service.emit();
      return session;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] createSession failed', { error: msg, payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  async cleanupLegacySessions(userId: string): Promise<void> {
    try {
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
        AppLogger.warn('[CrewService] cleanupLegacySessions failed', { error: error instanceof Error ? error.message : String(error)  });
        AppLogger.log('CREW_ERROR', { action: 'cleanupLegacySessions', error: error instanceof Error ? error.message : String(error)  });
      }
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] cleanupLegacySessions threw exception', { error: msg });
    }
  }

  async cleanupExpiredSessions(): Promise<void> {
    try {
      const now = new Date().toISOString();
      const { error } = await supabase
        .from('crew_sessions')
        .update({
          is_active: false,
          status: 'ended',
          ended_at: now,
        })
        .eq('is_active', true)
        .lt('expires_at', now);

      if (error) {
        AppLogger.warn('[CrewService] cleanupExpiredSessions failed', { error: error instanceof Error ? error.message : String(error)  });
      } else {
        AppLogger.log('CREW_CLEANUP', { action: 'cleanupExpiredSessions', status: 'complete' });
      }
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] cleanupExpiredSessions threw exception', { error: msg });
    }
  }

  async joinSession(inviteCode: string, displayName: string, userId?: string): Promise<CrewSession> {
    try {
      if (!userId) throw new Error('Must be signed in to join a crew');

      const code = inviteCode.trim().toUpperCase();

      const { data: _sessionData, error: sessionErr } = await supabase
        .from('crew_sessions')
        .select('*')
        .eq('invite_code', code)
        .eq('is_active', true)
        .gt('expires_at', new Date().toISOString())
        .single();
      const session = _sessionData as CrewSession;

      if (sessionErr || !session) throw new Error('Crew not found or session expired');

      const { count } = await supabase
        .from('crew_members')
        .select('id', { count: 'exact', head: true })
        .eq('session_id', session.id);

      if ((count ?? 0) >= MAX_MEMBERS_PER_SESSION) {
        throw new Error('Crew is full (max 20 members)');
      }

      const { error: memberErr } = await supabase.from('crew_members').upsert({
        session_id: session.id,
        user_id: userId,
        display_name: displayName || 'Skater',
      }, { onConflict: 'session_id,user_id' });

      if (memberErr) throw memberErr;

      await this.service._persistSession(session);
      this.service.currentSession = session as CrewSession;
      this.service.currentSessionId = session.id;
      this.service.currentRole = userId === session.leader_user_id ? 'leader' : 'member';
      this.service.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
      this.service.emit();
      return session as CrewSession;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] joinSession failed', { error: msg, payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  async joinSessionById(sessionId: string, displayName: string, userId?: string): Promise<CrewSession> {
    try {
      if (!userId) throw new Error('Must be signed in to join a crew');

      const { data: _sessionDataById, error } = await supabase
        .from('crew_sessions')
        .select('*')
        .eq('id', sessionId)
        .eq('is_active', true)
        .gt('expires_at', new Date().toISOString())
        .single();
      const session = _sessionDataById as CrewSession;

      if (error || !session) throw new Error('Session not found or expired');

      const { count } = await supabase
        .from('crew_members')
        .select('id', { count: 'exact', head: true })
        .eq('session_id', sessionId);
      if ((count ?? 0) >= MAX_MEMBERS_PER_SESSION) throw new Error('Crew is full');

      await supabase.from('crew_members').upsert({
        session_id: sessionId,
        user_id: userId,
        display_name: displayName,
      }, { onConflict: 'session_id,user_id' });

      await this.service._persistSession(session);
      this.service.currentSession = session as CrewSession;
      this.service.currentSessionId = sessionId;
      this.service.currentRole = userId === session.leader_user_id ? 'leader' : 'member';
      this.service.sessionTelemetry = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };
      this.service.emit();
      return session as CrewSession;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] joinSessionById failed', { error: msg, payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  async fetchActiveSessions(updatedSince?: string): Promise<CrewSession[]> {
    try {
      await this.cleanupExpiredSessions();

      let query = supabase
        .from('crew_sessions')
        .select('*, crew_members(count)')
        .eq('is_active', true)
        .gt('expires_at', new Date().toISOString());

      if (updatedSince) {
        query = query.gt('updated_at', updatedSince);
      }

      const { data, error } = await query
        .order('created_at', { ascending: false })
        .limit(20);

      if (error || !data) return [];

      return data.map((s: Record<string, unknown>) => ({
        ...s,
        member_count: (s.crew_members as { count?: number }[])?.[0]?.count ?? 0,
      } as CrewSession));
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] fetchActiveSessions failed', { error: msg });
      return [];
    }
  }

  async fetchPublicSessions(updatedSince?: string): Promise<CrewSession[]> {
    try {
      let pubQuery = supabase
        .from('public_sessions')
        .select('*');
        
      if (updatedSince) {
        pubQuery = pubQuery.gt('updated_at', updatedSince);
      }
      
      const { data, error } = await pubQuery.limit(30);

      if (!error && data) {
        const rows = data as Record<string, unknown>[];
        return rows.map((s) => ({
          id: (s.id as string) ?? '',
          name: (s.crew_name as string) ?? (s.id as string) ?? '',
          invite_code: (s.invite_code as string) ?? '',
          location_label: (s.location_label as string) ?? null,
          location_coords: (s.location_coords as Record<string, unknown>) ?? null,
          scheduled_at: (s.scheduled_at as string) ?? null,
          created_at: (s.created_at as string) ?? new Date().toISOString(),
          is_public: true,
          crew_id: (s.crew_id as string) ?? null,
          is_active: true,
          expires_at: (s.expires_at as string) ?? new Date().toISOString(),
          leader_user_id: '',
          status: (s.status as string) ?? 'active',
          updated_at: (s.updated_at as string) ?? null,
          ended_at: null,
          total_distance_miles: 0,
          top_speed_mph: 0,
          avg_speed_mph: 0,
          last_scene: null,
          skate_spot_id: null,
          member_count: (s.member_count as number) ?? 0,
        }) as CrewSession);
      }
    } catch (e: unknown) {
      AppLogger.warn('[CrewService] public_sessions view failed, using fallback', { error: e instanceof Error ? e.message : String(e)  });
    }

    try {
      let query = supabase
        .from('crew_sessions')
        .select('*, crew_members(count)')
        .eq('is_active', true)
        .eq('is_public', true)
        .gt('expires_at', new Date().toISOString());

      if (updatedSince) {
        query = query.gt('updated_at', updatedSince);
      }

      const { data } = await query
        .order('created_at', { ascending: false })
        .limit(20);

      return (data ?? []).map((s: Record<string, unknown>) => ({
        ...s,
        member_count: (s.crew_members as { count?: number }[])?.[0]?.count ?? 0,
      } as CrewSession));
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] fetchPublicSessions fallback failed', { error: msg });
      return [];
    }
  }

  async endSession(explicitSessionId?: string, userId?: string): Promise<void> {
    try {
      if (!userId) throw new Error('Not authenticated');

      const sessionId = explicitSessionId ?? this.service.currentSessionId;
      if (!sessionId) throw new Error('No active session to end');

      AppLogger.log('CREW_END_SESSION', { sessionId });

      const avgSpeed = this.service.sessionTelemetry.avgSpeedSamples.length
        ? this.service.sessionTelemetry.avgSpeedSamples.reduce((a: number, b: number) => a + b, 0) / this.service.sessionTelemetry.avgSpeedSamples.length
        : 0;

      const { error: fullError, data: fullData } = await supabase
        .from('crew_sessions')
        .update({
          status: 'ended',
          is_active: false,
          ended_at: new Date().toISOString(),
          top_speed_mph: this.service.sessionTelemetry.topSpeedMph || 0,
          avg_speed_mph: avgSpeed || 0,
          total_distance_miles: this.service.sessionTelemetry.distanceMiles || 0,
        })
        .eq('id', sessionId)
        .eq('leader_user_id', userId)
        .select('id');

      if (fullError) {
        AppLogger.warn('[CrewService] Full update failed, trying fallback', { error: fullError.message });
        const { error: fallbackError, data: fallbackData } = await supabase
          .from('crew_sessions')
          .update({ is_active: false })
          .eq('id', sessionId)
          .eq('leader_user_id', userId)
          .select('id');

        if (fallbackError) throw new Error(`Could not end session: ${fallbackError.message}`);
        if (!fallbackData || fallbackData.length === 0) {
          throw new Error('Only the session leader can end the session');
        }
      } else if (!fullData || fullData.length === 0) {
        const { error: fbErr, data: fbData } = await supabase
          .from('crew_sessions')
          .update({ is_active: false })
          .eq('id', sessionId)
          .eq('leader_user_id', userId)
          .select('id');
        if (fbErr || !fbData || fbData.length === 0) {
          throw new Error('Only the session leader can end the session');
        }
      }

      AppLogger.log('CREW_SESSION_ENDED', { reason: 'db_update_succeeded', action: 'broadcasting_ended' });

      this.service.channel?.send({
        type: 'broadcast',
        event: 'session_ended',
        payload: { sessionId },
      });

      supabase
        .from('crew_members')
        .delete()
        .eq('session_id', sessionId)
        .then(
          () => AppLogger.log('CREW_CLEANUP', { action: 'crew_members_deleted', sessionId }),
          (err: unknown) => AppLogger.warn('[CrewService] crew_members delete failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 })
        );

      const channelRef = this.service.channel;
      this.service.channel = null;
      this.service.currentSession = null;
      this.service.currentSessionId = null;
      this.service.currentRole = null;
      this.service.emit();
      setTimeout(() => {
        try {
          if (channelRef) supabase.removeChannel(channelRef).catch((err: unknown) => AppLogger.warn('[CrewService] deferred removeChannel failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }));
          AppLogger.log('CREW_SESSION_ENDED', { action: 'channel_torn_down' });
        } catch (err: unknown) {
          AppLogger.warn('[CrewService] deferred removeChannel failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
        }
      }, CHANNEL_TEARDOWN_DELAY_MS);

      if (this.service.broadcastTimer) { clearTimeout(this.service.broadcastTimer); this.service.broadcastTimer = null; }
      try {
        await AsyncStorage.multiRemove([this.service.STORAGE_LAST_SESSION_ID, this.service.STORAGE_LAST_SESSION_EXP]);
      } catch (err: unknown) {
        AppLogger.warn('[CrewService] Failed to multiRemove on endSession', { error: err instanceof Error ? err.message : String(err)  });
      }
      AppLogger.log('CREW_SESSION_ENDED', { reason: 'leader_ended', sessionId });
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] endSession failed', { error: msg });
      throw new Error(msg);
    }
  }

  async fetchLastScene(sessionId: string): Promise<Record<string, any> | null> {
    try {
      const { data } = await supabase
        .from('crew_sessions')
        .select('last_scene')
        .eq('id', sessionId)
        .single();
      return (data?.last_scene as Record<string, any>) ?? null;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] fetchLastScene failed', { error: msg , payload_size: 0, ssi: 0 });
      return null;
    }
  }

  async leaveSession(userId?: string): Promise<void> {
    try {
      if (!userId || !this.service.currentSessionId) return;

      await supabase
        .from('crew_members')
        .delete()
        .eq('session_id', this.service.currentSessionId)
        .eq('user_id', userId);

      this.service.unsubscribe();
      try {
        await AsyncStorage.multiRemove([this.service.STORAGE_LAST_SESSION_ID, this.service.STORAGE_LAST_SESSION_EXP]);
      } catch (err: unknown) {
        AppLogger.warn('[CrewService] Failed to multiRemove on leaveSession', { error: err instanceof Error ? err.message : String(err)  });
      }
      this.service.currentSession = null;
      this.service.currentSessionId = null;
      this.service.currentRole = null;
      this.service.emit();
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] leaveSession failed', { error: msg, payload_size: 0, ssi: 0 });
    }
  }

  async transferLeadership(newLeaderId: string): Promise<void> {
    try {
      if (!this.service.currentSessionId || this.service.currentRole !== 'leader') return;

      const { error } = await supabase
        .from('crew_sessions')
        .update({ leader_user_id: newLeaderId })
        .eq('id', this.service.currentSessionId);

      if (error) throw error;
      this.service.currentRole = 'member';
      if (this.service.currentSession) {
        this.service.currentSession.leader_user_id = newLeaderId;
      }
      this.service.emit();
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] transferLeadership failed', { error: msg , payload_size: 0, ssi: 0 });
      throw new Error(msg);
    }
  }

  async fetchMembers(sessionId: string): Promise<CrewMember[]> {
    try {
      const { data, error } = await supabase
        .from('crew_members')
        .select('*')
        .eq('session_id', sessionId)
        .order('joined_at', { ascending: true });

      if (error) throw error;
      return (data ?? []) as CrewMember[];
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] fetchMembers failed', { error: msg , payload_size: 0, ssi: 0 });
      return [];
    }
  }
}

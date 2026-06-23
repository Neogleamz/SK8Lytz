import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../supabaseClient';
import { AppLogger } from '../appLogger';
import { CrewSession, CrewRole, CrewMember, CrewScenePayload } from './types';
import type { RealtimeChannel } from '@supabase/supabase-js';

interface ICrewRealtimeDependencies {
  _ensureUnsubscribed(): void;
  unsubscribe(): void;
  fetchMembers(sessionId: string): Promise<CrewMember[]>;
  channel: RealtimeChannel | null;
  currentSession: CrewSession | null;
  currentSessionId: string | null;
  currentRole: CrewRole;
  broadcastTimer: ReturnType<typeof setTimeout> | null;
  STORAGE_LAST_SESSION_ID: string;
  STORAGE_LAST_SESSION_EXP: string;
  emit(): void;
}

// ── Timing constants (R-16: no magic numbers) ─────────────────────────────
const BROADCAST_DEBOUNCE_MS = 150;
const HEARTBEAT_INTERVAL_MS = 60_000;
const PERSIST_PAYLOAD_DELAY_MS = 5_000;

export class CrewRealtime {
  constructor(private service: ICrewRealtimeDependencies) {}

  subscribeAsLeader(
    sessionId: string,
    onMemberChange: (members: CrewMember[]) => void,
  ): (() => void) {
    this.service._ensureUnsubscribed();
    this.startHeartbeat(sessionId);

    this.service.channel = supabase
      .channel(`crew:${sessionId}`, { config: { broadcast: { self: false } } })
      .on('broadcast', { event: 'member_update' }, () => {
        this.service.fetchMembers(sessionId).then(onMemberChange);
      })
      .subscribe();
      
    return () => this.service.unsubscribe();
  }

  subscribeAsMember(
    sessionId: string,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onSceneOrPayload: (data: any) => void,
    onSessionEnded?: () => void,
  ): (() => void) {
    this.service._ensureUnsubscribed();

    this.service.channel = supabase
      .channel(`crew:${sessionId}`, { config: { broadcast: { self: false } } })
      .on('broadcast', { event: 'scene_update' }, (payload: { payload: CrewScenePayload }) => {
        const crewPayload = payload.payload;
        if (crewPayload?.payload) onSceneOrPayload(crewPayload.payload);
      })
      .on('broadcast', { event: 'session_ended' }, () => {
        (async () => {
          AppLogger.log('CREW_SESSION_LEFT', { reason: 'leader_ended_session', role: 'member' });
          this.service.unsubscribe();
          this.service.currentSession = null;
          this.service.currentSessionId = null;
          this.service.currentRole = null;
          this.service.emit();
          try {
            await AsyncStorage.multiRemove([this.service.STORAGE_LAST_SESSION_ID, this.service.STORAGE_LAST_SESSION_EXP]);
          } catch (err: unknown) {
            AppLogger.warn('[CrewService] Failed to multiRemove on session_ended broadcast', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
          }
          onSessionEnded?.();
        })().catch((err: unknown) => {
          AppLogger.warn('[CrewService] unhandled error in session_ended', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
        });
      })
      .subscribe();
      
    return () => this.service.unsubscribe();
  }

  broadcastPayload(payload: number[], userId?: string): void {
    if (!this.service.channel || this.service.currentRole !== 'leader') return;

    if (this.service.broadcastTimer) clearTimeout(this.service.broadcastTimer);
    this.service.broadcastTimer = setTimeout(async () => {
      const leaderId = userId ?? '';
      this.service.channel?.send({
        type: 'broadcast',
        event: 'scene_update',
        payload: {
          payload,            // runtime byte array
          leader_id: leaderId,
          ts: Date.now(),
        } satisfies CrewScenePayload,
      });
      this._persistLastPayload(payload);
    }, BROADCAST_DEBOUNCE_MS);
  }

  private _heartbeatTimer: ReturnType<typeof setInterval> | null = null;

  startHeartbeat(sessionId: string): void {
    if (this._heartbeatTimer) clearInterval(this._heartbeatTimer);
    this._heartbeatTimer = setInterval(async () => {
      try {
        await supabase
          .from('crew_sessions')
          .update({ updated_at: new Date().toISOString() })
          .eq('id', sessionId);
      } catch (err: unknown) {
        AppLogger.warn('[CrewRealtime] heartbeat update failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
      }
    }, HEARTBEAT_INTERVAL_MS);
  }

  private _lastScenePersistTimer: ReturnType<typeof setTimeout> | null = null;

  private _persistLastPayload(payload: number[]): void {
    if (!this.service.currentSessionId || this.service.currentRole !== 'leader') return;
    if (this._lastScenePersistTimer) return; // already scheduled
    this._lastScenePersistTimer = setTimeout(async () => {
      try {
        this._lastScenePersistTimer = null;
        if (!this.service.currentSessionId) return;
        await supabase
          .from('crew_sessions')
          .update({ last_scene: payload })
          .eq('id', this.service.currentSessionId);
      } catch (e: unknown) {
        const msg = e instanceof Error ? e.message : String(e);
        AppLogger.error('[CrewRealtime] _persistLastPayload failed', { error: msg, payload_size: 0, ssi: 0 });
      }
    }, PERSIST_PAYLOAD_DELAY_MS);
  }

  unsubscribeRealtime(): void {
    if (this._lastScenePersistTimer) {
      clearTimeout(this._lastScenePersistTimer);
      this._lastScenePersistTimer = null;
    }
    if (this._heartbeatTimer) {
      clearInterval(this._heartbeatTimer);
      this._heartbeatTimer = null;
    }
  }
}

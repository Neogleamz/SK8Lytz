import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../supabaseClient';
import { AppLogger } from '../appLogger';
import { CrewSession, CrewRole, CrewMember, CrewScenePayload } from './types';
import type { CrewService } from './CrewService';

export class CrewRealtime {
  constructor(private service: CrewService) {}

  subscribeAsLeader(
    sessionId: string,
    onMemberChange: (members: CrewMember[]) => void,
  ): (() => void) {
    this.service._ensureUnsubscribed();

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
    onScene: (scene: Record<string, any>) => void,
    onSessionEnded?: () => void,
  ): (() => void) {
    this.service._ensureUnsubscribed();

    this.service.channel = supabase
      .channel(`crew:${sessionId}`, { config: { broadcast: { self: false } } })
      .on('broadcast', { event: 'scene_update' }, (payload: { payload: CrewScenePayload }) => {
        const crewPayload = payload.payload as CrewScenePayload;
        if (crewPayload?.scene) onScene(crewPayload.scene);
      })
      .on('broadcast', { event: 'session_ended' }, () => {
        AppLogger.log('CREW_SESSION_LEFT', { reason: 'leader_ended_session', role: 'member' });
        this.service.unsubscribe();
        this.service.currentSession = null;
        this.service.currentSessionId = null;
        this.service.currentRole = null;
        this.service.emit();
        try {
          AsyncStorage.multiRemove([this.service.STORAGE_LAST_SESSION_ID, this.service.STORAGE_LAST_SESSION_EXP]);
        } catch (err: unknown) {
          AppLogger.warn('[CrewService] Failed to multiRemove on session_ended broadcast', { error: err instanceof Error ? err.message : String(err)  });
        }
        onSessionEnded?.();
      })
      .subscribe();
      
    return () => this.service.unsubscribe();
  }

  broadcastScene(scene: Record<string, any>, userId?: string): void {
    if (!this.service.channel || this.service.currentRole !== 'leader') return;

    if (this.service.broadcastTimer) clearTimeout(this.service.broadcastTimer);
    this.service.broadcastTimer = setTimeout(async () => {
      const leaderId = userId ?? '';
      this.service.channel?.send({
        type: 'broadcast',
        event: 'scene_update',
        payload: {
          scene,
          leader_id: leaderId,
          ts: Date.now(),
        } satisfies CrewScenePayload,
      });
      this._persistLastScene(scene);
    }, 150);
  }

  private _lastScenePersistTimer: ReturnType<typeof setTimeout> | null = null;

  private _persistLastScene(scene: Record<string, any>): void {
    if (!this.service.currentSessionId || this.service.currentRole !== 'leader') return;
    if (this._lastScenePersistTimer) return; // already scheduled
    this._lastScenePersistTimer = setTimeout(async () => {
      try {
        this._lastScenePersistTimer = null;
        if (!this.service.currentSessionId) return;
        await supabase
          .from('crew_sessions')
          .update({ last_scene: scene })
          .eq('id', this.service.currentSessionId);
      } catch (e: unknown) {
        const msg = e instanceof Error ? e.message : String(e);
        AppLogger.error('[CrewService] _persistLastScene failed', { error: msg , payload_size: 0, ssi: 0 });
      }
    }, 5000);
  }

  unsubscribeRealtime(): void {
    if (this._lastScenePersistTimer) {
      clearTimeout(this._lastScenePersistTimer);
      this._lastScenePersistTimer = null;
    }
  }
}

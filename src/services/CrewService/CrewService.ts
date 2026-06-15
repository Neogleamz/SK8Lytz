import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../supabaseClient';
import { AppLogger } from '../appLogger';
import { CrewSession, CrewRole, CrewMember, CrewScenePayload, SessionTelemetryData } from './types';
import { CrewSessionManager } from './CrewSessionManager';
import { CrewRealtime } from './CrewRealtime';
import { CrewAutoRejoin } from './CrewAutoRejoin';
import type { RealtimeChannel } from '@supabase/supabase-js';

type Listener = () => void;

export class CrewService {
  public STORAGE_LAST_SESSION_ID = 'sk8lytz:last_session_id';
  public STORAGE_LAST_SESSION_EXP = 'sk8lytz:last_session_exp';

  public currentSession: CrewSession | null = null;
  public currentSessionId: string | null = null;
  public currentRole: CrewRole = null;
  public channel: RealtimeChannel | null = null;
  public broadcastTimer: ReturnType<typeof setTimeout> | null = null;
  public sessionTelemetry: SessionTelemetryData = { distanceMiles: 0, topSpeedMph: 0, avgSpeedSamples: [] };

  private listeners = new Set<Listener>();

  private manager: CrewSessionManager;
  private realtime: CrewRealtime;
  private autoRejoin: CrewAutoRejoin;

  // Delegated Methods (Manager)
  public createSession: CrewSessionManager['createSession'];
  public cleanupLegacySessions: CrewSessionManager['cleanupLegacySessions'];
  public cleanupExpiredSessions: CrewSessionManager['cleanupExpiredSessions'];
  public joinSession: CrewSessionManager['joinSession'];
  public joinSessionById: CrewSessionManager['joinSessionById'];
  public fetchActiveSessions: CrewSessionManager['fetchActiveSessions'];
  public fetchPublicSessions: CrewSessionManager['fetchPublicSessions'];
  public endSession: CrewSessionManager['endSession'];
  public fetchLastScene: CrewSessionManager['fetchLastScene'];
  public leaveSession: CrewSessionManager['leaveSession'];
  public transferLeadership: CrewSessionManager['transferLeadership'];
  public fetchMembers: CrewSessionManager['fetchMembers'];

  // Delegated Methods (Realtime)
  public subscribeAsLeader: CrewRealtime['subscribeAsLeader'];
  public subscribeAsMember: CrewRealtime['subscribeAsMember'];
  public broadcastScene: CrewRealtime['broadcastScene'];

  // Delegated Methods (Auto-Rejoin)
  public tryAutoRejoin: CrewAutoRejoin['tryAutoRejoin'];

  constructor() {
    this.manager = new CrewSessionManager(this);
    this.realtime = new CrewRealtime(this);
    this.autoRejoin = new CrewAutoRejoin(this);

    this.createSession = this.manager.createSession.bind(this.manager);
    this.cleanupLegacySessions = this.manager.cleanupLegacySessions.bind(this.manager);
    this.cleanupExpiredSessions = this.manager.cleanupExpiredSessions.bind(this.manager);
    this.joinSession = this.manager.joinSession.bind(this.manager);
    this.joinSessionById = this.manager.joinSessionById.bind(this.manager);
    this.fetchActiveSessions = this.manager.fetchActiveSessions.bind(this.manager);
    this.fetchPublicSessions = this.manager.fetchPublicSessions.bind(this.manager);
    this.endSession = this.manager.endSession.bind(this.manager);
    this.fetchLastScene = this.manager.fetchLastScene.bind(this.manager);
    this.leaveSession = this.manager.leaveSession.bind(this.manager);
    this.transferLeadership = this.manager.transferLeadership.bind(this.manager);
    this.fetchMembers = this.manager.fetchMembers.bind(this.manager);

    this.subscribeAsLeader = this.realtime.subscribeAsLeader.bind(this.realtime);
    this.subscribeAsMember = this.realtime.subscribeAsMember.bind(this.realtime);
    this.broadcastScene = this.realtime.broadcastScene.bind(this.realtime);

    this.tryAutoRejoin = this.autoRejoin.tryAutoRejoin.bind(this.autoRejoin);
  }

  public get isInCrew(): boolean {
    return !!this.currentSessionId && !!this.currentSession;
  }

  // --- Listener Logic ---
  subscribe(listener: Listener): () => void {
    this.listeners.add(listener);
    return () => this.listeners.delete(listener);
  }
  
  public emit() {
    this.listeners.forEach(l => l());
  }

  public _ensureUnsubscribed() {
    if (this.channel) {
      supabase.removeChannel(this.channel);
      this.channel = null;
    }
  }

  public unsubscribe() {
    this._ensureUnsubscribed();
    this.realtime.unsubscribeRealtime();
  }

  public async _persistSession(session: CrewSession) {
    if (!session?.id || !session.expires_at) return;
    try {
      await AsyncStorage.multiSet([
        [this.STORAGE_LAST_SESSION_ID, session.id],
        [this.STORAGE_LAST_SESSION_EXP, session.expires_at],
      ]);
    } catch (e: unknown) {
      AppLogger.warn('[CrewService] failed to persist session ID', { error: e instanceof Error ? e.message : String(e)  });
    }
  }
}

export const crewService = new CrewService();
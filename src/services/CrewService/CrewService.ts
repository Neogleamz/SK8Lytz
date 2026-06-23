import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../supabaseClient';
import { AppLogger } from '../appLogger';
import { CrewSession, CrewRole, CrewMember, SessionTelemetryData } from './types';

interface ICrewSessionManager {
  createSession(name: string, displayName: string, opts?: Record<string, unknown>, userId?: string): Promise<CrewSession>;
  cleanupLegacySessions(userId: string): Promise<void>;
  cleanupExpiredSessions(): Promise<void>;
  joinSession(inviteCode: string, displayName: string, userId?: string): Promise<CrewSession>;
  joinSessionById(sessionId: string, displayName: string, userId?: string): Promise<CrewSession>;
  fetchActiveSessions(updatedSince?: string): Promise<CrewSession[]>;
  fetchPublicSessions(updatedSince?: string): Promise<CrewSession[]>;
  endSession(explicitSessionId?: string, userId?: string): Promise<void>;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  fetchLastScene(sessionId: string): Promise<Record<string, any> | null>;
  leaveSession(userId?: string): Promise<void>;
  transferLeadership(newLeaderId: string): Promise<void>;
  fetchMembers(sessionId: string): Promise<CrewMember[]>;
}
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

  private manager: ICrewSessionManager;
  private realtime: CrewRealtime;
  private autoRejoin: CrewAutoRejoin;

  // Delegated Methods (Manager)
  public createSession: ICrewSessionManager['createSession'];
  public cleanupLegacySessions: ICrewSessionManager['cleanupLegacySessions'];
  public cleanupExpiredSessions: ICrewSessionManager['cleanupExpiredSessions'];
  public joinSession: ICrewSessionManager['joinSession'];
  public joinSessionById: ICrewSessionManager['joinSessionById'];
  public fetchActiveSessions: ICrewSessionManager['fetchActiveSessions'];
  public fetchPublicSessions: ICrewSessionManager['fetchPublicSessions'];
  public endSession: ICrewSessionManager['endSession'];
  public fetchLastScene: ICrewSessionManager['fetchLastScene'];
  public leaveSession: ICrewSessionManager['leaveSession'];
  public transferLeadership: ICrewSessionManager['transferLeadership'];
  public fetchMembers: ICrewSessionManager['fetchMembers'];

  // Delegated Methods (Realtime)
  public subscribeAsLeader: CrewRealtime['subscribeAsLeader'];
  public subscribeAsMember: CrewRealtime['subscribeAsMember'];
  public broadcastPayload: CrewRealtime['broadcastPayload'];

  // Delegated Methods (Auto-Rejoin)
  public tryAutoRejoin: CrewAutoRejoin['tryAutoRejoin'];

  constructor() {
    const { CrewSessionManager } = require('./CrewSessionManager');
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
    this.broadcastPayload = this.realtime.broadcastPayload.bind(this.realtime);

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
      supabase.removeChannel(this.channel).catch((err: unknown) => AppLogger.warn('[CrewService] removeChannel failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }));
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
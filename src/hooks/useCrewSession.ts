import { useCallback, useEffect, useRef, useState } from 'react';
import { AppLogger } from '../services/appLogger';
import { CrewMember, CrewRole, crewService, CrewSession } from '../services/CrewService';
import { useAuth } from '../context/AuthContext';
import { scrubPII } from '../utils/piiScrubber';

export function useCrewSession(
  // TODO(feat/crewz-resilience): The plan requested updating onScene to onPayload here and passing to BleWriteQueue, 
  // but that logic resides in useDashboardCrew.ts and DashboardCrewPanel.tsx which are Out of Scope.
  onSessionReady: (session: CrewSession, role: CrewRole, lastScene: Record<string, unknown> | null) => void,
  onSessionLeft: () => void,
  onSessionEnded: () => void,
  refreshNearby: () => void,
  goToLanding: () => void,
  setErrorMsg: (msg: string) => void
) {
  const { user } = useAuth();
  const [currentSession, setCurrentSession] = useState<CrewSession | null>(crewService.currentSession);
  const [currentRole, setCurrentRole] = useState<CrewRole>(crewService.currentRole);
  const [members, setMembers] = useState<CrewMember[]>([]);
  const [isHandoffMode, setIsHandoffMode] = useState(false);
  const loadMembersTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    return () => {
      if (loadMembersTimerRef.current) clearTimeout(loadMembersTimerRef.current);
    };
  }, []);

  useEffect(() => {
    return crewService.subscribe(() => {
      setCurrentSession(crewService.currentSession);
      setCurrentRole(crewService.currentRole);
    });
  }, []);

  // Live presence: leader realtime subscriptions publish refreshed member lists
  // here whenever a `member_update` broadcast is received (join/leave).
  useEffect(() => {
    return crewService.subscribeMembers((m: CrewMember[]) => {
      setMembers(m);
    });
  }, []);

  const loadMembers = useCallback(async () => {
    if (!currentSession) return;
    const m = await crewService.fetchMembers(currentSession.id).catch(() => []);
    setMembers(m);
  }, [currentSession]);

  const handleSessionJoined = useCallback(async (session: CrewSession) => {
    try {
      // Determine the role
      const role: CrewRole = crewService.currentRole ?? (session.leader_user_id === user?.id ? 'leader' : 'member');
      
      // Load members
      const m = await crewService.fetchMembers(session.id).catch(() => []);
      setMembers(m);

      const lastScene = role === 'member'
        ? await crewService.fetchLastScene(session.id).catch(() => null)
        : null;

      onSessionReady(session, role, lastScene);
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.log('CREW_ERROR', { action: 'join_session', error: msg, payload_size: 0, ssi: 0 });
      setErrorMsg(msg);
    }
  }, [user?.id, onSessionReady, setErrorMsg]);


  const executeEndSession = async () => {
    const currentSessionId = currentSession?.id;
    AppLogger.log('CREW_SESSION_ENDED', { sessionId: currentSessionId ? scrubPII(currentSessionId) : undefined, crewName: currentSession?.name ? scrubPII(currentSession?.name) : undefined, role: 'leader', reason: 'explicit_end', payload_size: 0, ssi: 0 });

    // R-05: Optimistic UI — navigate away immediately, persist to cloud in background
    setIsHandoffMode(false);
    onSessionEnded();
    goToLanding();

    if (currentSessionId) {
      // Fire-and-forget: telemetry + session end (non-blocking)
      (async () => {
        try {
          await crewService.endSession(currentSessionId, user?.id);
          refreshNearby();
        } catch (e: unknown) {
          const msg = e instanceof Error ? e.message : 'Could not end session';
          AppLogger.error('[useCrewSession] background endSession failed', { error: msg, payload_size: 0, ssi: 0 });
        }
      })();
    }
  };

  const executeLeaveSession = async () => {
    AppLogger.log('CREW_SESSION_LEFT', { sessionId: currentSession?.id ? scrubPII(currentSession.id) : undefined, role: currentRole, payload_size: 0, ssi: 0 });
    try {
      await crewService.leaveSession(user?.id);
      refreshNearby();
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.log('CREW_ERROR', { action: 'leave_session', error: msg, payload_size: 0, ssi: 0 });
      setErrorMsg(msg);
    } finally {
      setIsHandoffMode(false);
      onSessionLeft();
      goToLanding();
    }
  };

  const handleHandoffLeadership = async (member: CrewMember): Promise<boolean> => {
    try {
      await crewService.transferLeadership(member.user_id);
      AppLogger.log('CREW_LEADERSHIP_TRANSFERRED', { transferred: true, payload_size: 0, ssi: 0 });
      setIsHandoffMode(false);
      if (loadMembersTimerRef.current) clearTimeout(loadMembersTimerRef.current);
      loadMembersTimerRef.current = setTimeout(loadMembers, 500);
      return true;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[useCrewSession] handleHandoffLeadership failed', { error: msg, payload_size: 0, ssi: 0 });
      return false;
    }
  };

  return {
    currentSession,
    currentRole,
    members, setMembers, loadMembers,
    isHandoffMode, setIsHandoffMode,
    handleSessionJoined,
    executeEndSession,
    executeLeaveSession,
    handleHandoffLeadership
  };
}

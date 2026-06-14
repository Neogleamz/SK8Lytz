import { useCallback, useEffect, useRef, useState } from 'react';
import { AppLogger } from '../services/AppLogger';
import { CrewMember, CrewRole, crewService, CrewSession } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';
import { useAuth } from '../context/AuthContext';

export function useCrewSession(
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
      AppLogger.log('CREW_ERROR', { action: 'join_session', error: msg });
      setErrorMsg(msg);
    }
  }, [user?.id, onSessionReady, setErrorMsg]);


  const executeEndSession = async () => {
    const currentSessionId = currentSession?.id;
    try {
      AppLogger.log('CREW_SESSION_ENDED', { sessionId: currentSessionId, crewName: currentSession?.name, role: 'leader', reason: 'explicit_end' });
      if (currentSessionId) {
        // Telemetry
        // TODO: Consolidate the profile stats update logic into a single method (e.g., SpeedTrackingService.updateLifetimeStats)
        // to share between useCrewSession and SpeedTrackingService, eliminating this duplication.
        if (user && (crewService.sessionTelemetry.distanceMiles > 0 || crewService.sessionTelemetry.topSpeedMph > 0)) {
          try {
            const { data: profile } = await supabase
              .from('user_profiles')
              .select('lifetime_top_speed_mph, lifetime_distance_miles')
              .eq('user_id', user.id)
              .single();
            if (profile) {
              const newDistance = (profile.lifetime_distance_miles || 0) + crewService.sessionTelemetry.distanceMiles;
              const newTopSpeed = Math.max((profile.lifetime_top_speed_mph || 0), crewService.sessionTelemetry.topSpeedMph);
              if (newDistance > (profile.lifetime_distance_miles || 0) || newTopSpeed > (profile.lifetime_top_speed_mph || 0)) {
                 const { error } = await supabase.from('user_profiles').update({
                   lifetime_distance_miles: parseFloat(newDistance.toFixed(3)),
                   lifetime_top_speed_mph: parseFloat(newTopSpeed.toFixed(2))
                 }).eq('user_id', user.id);
                 if (error) AppLogger.warn('[useCrewSession] Telemetry sync failed', { error: error.message, payload_size: 0, ssi: 0 });
              }
            }
          } catch (e: unknown) {
            AppLogger.warn('[useCrewSession] Telemetry sync exception', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
          }
        }
        await crewService.endSession(currentSessionId, user?.id);
        refreshNearby();
      }
      setIsHandoffMode(false);
      onSessionEnded();
      goToLanding();
    } catch (e: unknown) {
      const msg = e instanceof Error ? (e instanceof Error ? e.message : String(e)) : 'Could not end session';
      AppLogger.log('CREW_ERROR', { action: 'end_session', error: msg });
      setErrorMsg(msg);
    }
  };

  const executeLeaveSession = async () => {
    AppLogger.log('CREW_SESSION_LEFT', { sessionId: currentSession?.id, role: currentRole });
    try {
      await crewService.leaveSession(user?.id);
      refreshNearby();
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.log('CREW_ERROR', { action: 'leave_session', error: msg });
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
      AppLogger.log('CREW_LEADERSHIP_TRANSFERRED', { transferred: true });
      setIsHandoffMode(false);
      if (loadMembersTimerRef.current) clearTimeout(loadMembersTimerRef.current);
      loadMembersTimerRef.current = setTimeout(loadMembers, 500);
      return true;
    } catch (e: unknown) { 
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

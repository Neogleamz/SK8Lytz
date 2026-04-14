import { useCallback, useEffect, useState } from 'react';
import { AppLogger } from '../services/AppLogger';
import { CrewMember, CrewRole, crewService, CrewSession } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';

export function useCrewSession(
  activeSession: CrewSession | null,
  activeRole: CrewRole,
  currentUserId: string,
  onSessionReady: (session: CrewSession, role: CrewRole, lastScene: Record<string, any> | null) => void,
  onSessionLeft: () => void,
  onSessionEnded: () => void,
  refreshNearby: () => void,
  goToLanding: () => void,
  setErrorMsg: (msg: string) => void
) {
  const [currentSession, setCurrentSession] = useState<CrewSession | null>(activeSession);
  const [currentRole, setCurrentRole] = useState<CrewRole>(activeRole);
  const [members, setMembers] = useState<CrewMember[]>([]);
  const [isHandoffMode, setIsHandoffMode] = useState(false);

  useEffect(() => {
    if (activeSession) {
      setCurrentSession(activeSession);
      setCurrentRole(activeRole);
    }
  }, [activeSession, activeRole]);

  const loadMembers = useCallback(async () => {
    if (!currentSession) return;
    const m = await crewService.fetchMembers(currentSession.id).catch(() => []);
    setMembers(m);
  }, [currentSession]);

  const handleSessionJoined = useCallback(async (session: CrewSession) => {
    // Determine the role
    const role: CrewRole = crewService.currentRole ?? (session.leader_user_id === currentUserId ? 'leader' : 'member');
    setCurrentSession(session);
    setCurrentRole(role);
    
    // Load members
    const m = await crewService.fetchMembers(session.id).catch(() => []);
    setMembers(m);

    const lastScene = role === 'member'
      ? await crewService.fetchLastScene(session.id).catch(() => null)
      : null;

    onSessionReady(session, role, lastScene);
  }, [currentUserId, onSessionReady]);


  const executeEndSession = async () => {
    const currentSessionId = currentSession?.id;
    try {
      AppLogger.log('CREW_SESSION_ENDED', { sessionId: currentSessionId, crewName: currentSession?.name, role: 'leader', reason: 'explicit_end' });
      if (currentSessionId) {
        // Telemetry
        const { data: { user } } = await supabase.auth.getUser();
        if (user && (crewService.sessionTelemetry.distanceMiles > 0 || crewService.sessionTelemetry.topSpeedMph > 0)) {
          const { data: profile } = await supabase
            .from('user_profiles')
            .select('lifetime_top_speed_mph, lifetime_distance_miles')
            .eq('user_id', user.id)
            .single();
          if (profile) {
            const newDistance = (profile.lifetime_distance_miles || 0) + crewService.sessionTelemetry.distanceMiles;
            const newTopSpeed = Math.max((profile.lifetime_top_speed_mph || 0), crewService.sessionTelemetry.topSpeedMph);
            if (newDistance > (profile.lifetime_distance_miles || 0) || newTopSpeed > (profile.lifetime_top_speed_mph || 0)) {
               await supabase.from('user_profiles').update({
                 lifetime_distance_miles: newDistance,
                 lifetime_top_speed_mph: newTopSpeed
               }).eq('user_id', user.id);
            }
          }
        }
        await crewService.endSession(currentSessionId);
        refreshNearby();
      }
      setCurrentSession(null); 
      setCurrentRole(null);
      setIsHandoffMode(false);
      onSessionEnded();
      goToLanding();
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : 'Could not end session';
      AppLogger.log('CREW_ERROR', { action: 'end_session', error: msg });
      setErrorMsg(msg);
    }
  };

  const executeLeaveSession = async () => {
    AppLogger.log('CREW_SESSION_LEFT', { sessionId: currentSession?.id, role: currentRole });
    await crewService.leaveSession();
    refreshNearby();
    setCurrentSession(null); 
    setCurrentRole(null);
    setIsHandoffMode(false);
    onSessionLeft();
    goToLanding();
  };

  const handleHandoffLeadership = async (member: CrewMember): Promise<boolean> => {
    try {
      await crewService.transferLeadership(member.user_id);
      AppLogger.log('CREW_LEADERSHIP_TRANSFERRED', { newLeaderName: member.display_name });
      setCurrentRole('member');
      setIsHandoffMode(false);
      setTimeout(loadMembers, 500);
      return true;
    } catch (e: any) { 
      return false; 
    }
  };

  return {
    currentSession, setCurrentSession,
    currentRole, setCurrentRole,
    members, setMembers, loadMembers,
    isHandoffMode, setIsHandoffMode,
    handleSessionJoined,
    executeEndSession,
    executeLeaveSession,
    handleHandoffLeadership
  };
}

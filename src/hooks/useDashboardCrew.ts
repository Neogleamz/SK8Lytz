/**
 * useDashboardCrew.ts — Crew Hub Domain Hook
 *
 * Owns crew session state (crewSession, crewRole, modal visibility) and the
 * auto-rejoin lifecycle. Surfaces an `onApplyScene` callback to let the
 * DashboardScreen bridge member scene updates to the DockedController ref
 * without this hook needing a direct ref dependency.
 *
 * Extracted from DashboardScreen.tsx (chore/refactor-dashboard-monolith).
 */
import React, { useEffect, useState } from 'react';
import { CrewRole, crewService, CrewSession } from '../services/CrewService';
import { AppLogger } from '../services/AppLogger';
import { useAuth } from '../context/AuthContext';

interface UseDashboardCrewOptions {
  /** Called when the crew pushes a live scene update (member role only). */
  onApplyScene: (scene: Record<string, any>) => void;
}

export interface UseDashboardCrewResult {
  crewSession: CrewSession | null;
  crewRole: CrewRole;
  isCrewModalVisible: boolean;
  setIsCrewModalVisible: (v: boolean) => void;
  crewModeSummary: string | undefined;
  setCrewModeSummary: (s: string | undefined) => void;
  lastLeaderScene: Record<string, any> | null;
  setLastLeaderScene: (s: Record<string, any> | null) => void;
  pendingJoinCrewId: string | null;
  setPendingJoinCrewId: (id: string | null) => void;
}

export function useDashboardCrew({
  onApplyScene,
}: UseDashboardCrewOptions): UseDashboardCrewResult {
  const [crewSession, setCrewSession] = useState<CrewSession | null>(crewService.currentSession);
  const [crewRole, setCrewRole] = useState<CrewRole>(crewService.currentRole);
  const [isCrewModalVisible, setIsCrewModalVisible] = useState(false);
  const [crewModeSummary, setCrewModeSummary] = useState<string | undefined>(undefined);
  const [lastLeaderScene, setLastLeaderScene] = useState<Record<string, any> | null>(null);
  const [pendingJoinCrewId, setPendingJoinCrewId] = useState<string | null>(null);

  useEffect(() => {
    return crewService.subscribe(() => {
      setCrewSession(crewService.currentSession);
      setCrewRole(crewService.currentRole);
    });
  }, []);

  const { user, sessionLoaded } = useAuth();
  const hasTriedRejoinRef = React.useRef(false);
  const isRejoiningRef = React.useRef(false);

  // ── Auto-rejoin on launch ──────────────────────────────────────────────────
  useEffect(() => {
    if (!sessionLoaded || !user || hasTriedRejoinRef.current || isRejoiningRef.current) return;

    let isMounted = true;
    let unsub: (() => void) | undefined;

    const tryRejoin = async () => {
      isRejoiningRef.current = true;
      try {
        hasTriedRejoinRef.current = true;
        const displayName = user.email?.split('@')[0] || 'Skater';
        const result = await crewService.tryAutoRejoin(displayName, user.id);
        
        if (!isMounted) return;
        if (!result) return;

        const { session, role } = result;

        if (role === 'leader') {
          const u = await crewService.subscribeAsLeader(session.id, () => {});
          if (!isMounted) {
            u();
          } else {
            unsub = u;
          }
        } else {
          const u = crewService.subscribeAsMember(session.id, (scene) => {
            if (isMounted) onApplyScene(scene);
          });
          
          if (!isMounted) {
            u();
            return;
          }
          unsub = u;

          // Apply last known scene immediately on rejoin
          const lastScene = await crewService.fetchLastScene(session.id).catch(() => null);
          if (isMounted && lastScene) {
            onApplyScene(lastScene);
          }
        }
      } catch (e: unknown) {
        if (isMounted) {
          AppLogger.warn('[useDashboardCrew] auto-rejoin failed', {
            error: (e instanceof Error ? e.message : String(e)),
            payload_size: 0,
            ssi: 0
          });
        }
      } finally {
        isRejoiningRef.current = false;
      }
    };

    tryRejoin();

    return () => {
      isMounted = false;
      if (unsub) unsub();
    };
    // onApplyScene is a stable callback — intentionally excluded from deps
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user, sessionLoaded]);

  return {
    crewSession,
    crewRole,
    isCrewModalVisible,
    setIsCrewModalVisible,
    crewModeSummary,
    setCrewModeSummary,
    lastLeaderScene,
    setLastLeaderScene,
    pendingJoinCrewId,
    setPendingJoinCrewId,
  };
}

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
import { useEffect, useState } from 'react';
import { CrewRole, crewService, CrewSession } from '../services/CrewService';
import { supabase } from '../services/supabaseClient';

interface UseDashboardCrewOptions {
  /** Called when the crew pushes a live scene update (member role only). */
  onApplyScene: (scene: Record<string, any>) => void;
}

export interface UseDashboardCrewResult {
  crewSession: CrewSession | null;
  setCrewSession: (s: CrewSession | null) => void;
  crewRole: CrewRole;
  setCrewRole: (r: CrewRole) => void;
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
  const [crewSession, setCrewSession] = useState<CrewSession | null>(null);
  const [crewRole, setCrewRole] = useState<CrewRole>(null);
  const [isCrewModalVisible, setIsCrewModalVisible] = useState(false);
  const [crewModeSummary, setCrewModeSummary] = useState<string | undefined>(undefined);
  const [lastLeaderScene, setLastLeaderScene] = useState<Record<string, any> | null>(null);
  const [pendingJoinCrewId, setPendingJoinCrewId] = useState<string | null>(null);

  // ── Auto-rejoin on launch ──────────────────────────────────────────────────
  useEffect(() => {
    const tryRejoin = async () => {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;
      const displayName = user.email?.split('@')[0] || 'Skater';
      const result = await crewService.tryAutoRejoin(displayName);
      if (!result) return;

      const { session, role } = result;
      setCrewSession(session);
      setCrewRole(role);

      if (role === 'leader') {
        crewService.subscribeAsLeader(session.id, () => {});
      } else {
        crewService.subscribeAsMember(session.id, (scene) => {
          onApplyScene(scene);
        });
        // Apply last known scene immediately on rejoin
        const lastScene = await crewService.fetchLastScene(session.id).catch(() => null);
        if (lastScene) {
          setTimeout(() => onApplyScene(lastScene), 500);
        }
      }
    };

    // Delay 2 s to let auth session restore before querying
    const t = setTimeout(tryRejoin, 2000);
    return () => clearTimeout(t);
    // onApplyScene is a stable callback — intentionally excluded from deps
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return {
    crewSession,
    setCrewSession,
    crewRole,
    setCrewRole,
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

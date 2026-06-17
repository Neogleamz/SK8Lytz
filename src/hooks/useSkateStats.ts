import { useEffect, useState } from 'react';
import { ILifetimeStats, ISkateSession, SpeedTrackingService } from '../services/SpeedTrackingService';
import { AppLogger } from '../services/appLogger';
import { useAuth } from '../context/AuthContext';
import type { ViewState } from '../types/ViewState';

export function useSkateStats(visible: boolean) {
  const { user } = useAuth();
  const userId = user?.id || null;
  const [lifetimeStats, setLifetimeStats] = useState<ILifetimeStats | null>(null);
  const [recentSessions, setRecentSessions] = useState<ISkateSession[]>([]);
  /** 4-state FSM: idle → loading → success/empty/error */
  const [viewState, setViewState] = useState<ViewState>('idle');
  const [errorMsg, setErrorMsg] = useState('');

  useEffect(() => {
    let active = true;
    if (visible) {
      setViewState('loading');
      setErrorMsg('');

      const targetId = userId || 'anonymous';
      // Load from cache first for instant UI
      Promise.all([
        SpeedTrackingService.getCachedLifetimeStats(targetId),
        SpeedTrackingService.getCachedRecentSessions(targetId),
      ]).then(([cachedStats, cachedSessions]) => {
        if (!active) return;
        if (cachedStats) setLifetimeStats(cachedStats);
        if (cachedSessions && cachedSessions.length > 0) setRecentSessions(cachedSessions);
      });

      Promise.all([
        SpeedTrackingService.fetchLifetimeStats(userId),
        SpeedTrackingService.fetchRecentSessions(userId, 10),
      ])
        .then(([stats, sessions]) => {
          if (!active) return;
          setLifetimeStats(stats);
          setRecentSessions(sessions);
          const isEmpty = (!stats || stats.totalDistanceMiles === 0) && (!sessions || sessions.length === 0);
          setViewState(isEmpty ? 'empty' : 'success');
        })
        .catch((err: unknown) => {
          if (!active) return;
          AppLogger.error('[useSkateStats] Failed to fetch stats', err instanceof Error ? err.message : String(err), { payload_size: 0, ssi: 0 });
          setErrorMsg(err instanceof Error ? err.message : String(err));
          setViewState('error');
        });
    }
    return () => {
      active = false;
    };
  }, [visible, userId]);

  return {
    lifetimeStats,
    recentSessions,
    viewState,
    errorMsg,
    // Legacy support for unmodified consumers
    statsLoading: viewState === 'loading',
  };
}

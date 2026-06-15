import { useEffect, useState } from 'react';
import { ILifetimeStats, ISkateSession, SpeedTrackingService } from '../services/SpeedTrackingService';
import { AppLogger } from '../services/appLogger';
import { useAuth } from '../context/AuthContext';

export function useSkateStats(visible: boolean) {
  const { user } = useAuth();
  const userId = user?.id || null;
  const [lifetimeStats, setLifetimeStats] = useState<ILifetimeStats | null>(null);
  const [recentSessions, setRecentSessions] = useState<ISkateSession[]>([]);
  const [statsLoading, setStatsLoading] = useState(false);

  useEffect(() => {
    let active = true;
    if (visible) {
      setStatsLoading(true);

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
        })
        .catch((err: unknown) => {
          if (!active) return;
          AppLogger.error('[useSkateStats] Failed to fetch stats', err instanceof Error ? err.message : String(err), { payload_size: 0, ssi: 0 });
        })
        .finally(() => {
          if (active) {
            setStatsLoading(false);
          }
        });
    }
    return () => {
      active = false;
    };
  }, [visible, userId]);

  return {
    lifetimeStats,
    recentSessions,
    statsLoading,
  };
}

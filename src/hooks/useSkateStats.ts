import { useEffect, useState } from 'react';
import { ILifetimeStats, ISkateSession, SpeedTrackingService } from '../services/SpeedTrackingService';
import { AppLogger } from '../services/AppLogger';
import { useAuth } from '../context/AuthContext';

export function useSkateStats(visible: boolean) {
  const { user } = useAuth();
  const userId = user?.id || null;
  const [lifetimeStats, setLifetimeStats] = useState<ILifetimeStats | null>(null);
  const [recentSessions, setRecentSessions] = useState<ISkateSession[]>([]);
  const [statsLoading, setStatsLoading] = useState(false);

  useEffect(() => {
    if (visible && userId) {
      setStatsLoading(true);
      Promise.all([
        SpeedTrackingService.fetchLifetimeStats(userId),
        SpeedTrackingService.fetchRecentSessions(userId, 10),
      ])
        .then(([stats, sessions]) => {
          setLifetimeStats(stats);
          setRecentSessions(sessions);
        })
        .catch((err) => {
          AppLogger.error('[useSkateStats] Failed to fetch stats', err instanceof Error ? err.message : String(err));
        })
        .finally(() => setStatsLoading(false));
    }
  }, [visible]);

  return {
    lifetimeStats,
    recentSessions,
    statsLoading,
  };
}

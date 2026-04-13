import { useState, useEffect } from 'react';
import { SpeedTrackingService, ILifetimeStats, ISkateSession } from '../services/SpeedTrackingService';

export function useSkateStats(visible: boolean) {
  const [lifetimeStats, setLifetimeStats] = useState<ILifetimeStats | null>(null);
  const [recentSessions, setRecentSessions] = useState<ISkateSession[]>([]);
  const [statsLoading, setStatsLoading] = useState(false);

  useEffect(() => {
    if (visible) {
      setStatsLoading(true);
      Promise.all([
        SpeedTrackingService.fetchLifetimeStats(),
        SpeedTrackingService.fetchRecentSessions(10),
      ])
        .then(([stats, sessions]) => {
          setLifetimeStats(stats);
          setRecentSessions(sessions);
        })
        .catch((err) => {
          console.warn('[useSkateStats] Failed to fetch stats:', err);
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

import { useEffect, useRef } from 'react';
import { AppLogger } from '../../services/AppLogger';
import { ScenesService } from '../../services/ScenesService';
import { SpeedTrackingService } from '../../services/SpeedTrackingService';

const SYNC_INTERVAL_MS = 60000; // 60 seconds

/**
 * useOfflineSyncWorker
 * 
 * A background loop that continuously runs in the main app entrypoint.
 * It periodically flushes the AppLogger telemetry queue and the SceneSyncQueue
 * to Supabase. If the app is offline, these flushes gracefully fail and retry
 * on the next interval.
 */
import { useAuth } from '../../context/AuthContext';

export function useOfflineSyncWorker() {
  const { user } = useAuth();
  const syncIntervalRef = useRef<NodeJS.Timeout | null>(null);
  const userRef = useRef(user);
  const _isFlushingSyncRef = useRef(false);

  useEffect(() => {
    userRef.current = user;
  }, [user]);

  useEffect(() => {
    AppLogger.debug('[OfflineSyncWorker] Started background sync loop');

    const runSync = async () => {
      if (_isFlushingSyncRef.current) return;
      _isFlushingSyncRef.current = true;
      try {
        if (!userRef.current) return; // Prevent flush loop if unauthenticated
        await ScenesService.flushSyncQueue(userRef.current.id);
        await SpeedTrackingService.flushPendingSessionQueue(userRef.current.id);
        await AppLogger.uploadLogsToSupabase();
      } catch (e: unknown) {
        // We catch everything here to prevent the worker loop from crashing the app
        AppLogger.warn('[OfflineSyncWorker] Sync cycle encountered an error', { error: (e instanceof Error ? e.message : String(e)) });
      } finally {
        _isFlushingSyncRef.current = false;
      }
    };

    // Run once immediately on mount
    runSync();

    // Set up polling interval
    syncIntervalRef.current = setInterval(runSync, SYNC_INTERVAL_MS);

    return () => {
      if (syncIntervalRef.current) {
        clearInterval(syncIntervalRef.current);
      }
      AppLogger.debug('[OfflineSyncWorker] Stopped background sync loop');
    };
  }, []);
}

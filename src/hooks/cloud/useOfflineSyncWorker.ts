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
export function useOfflineSyncWorker() {
  const syncIntervalRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    AppLogger.debug('[OfflineSyncWorker] Started background sync loop');

    const runSync = async () => {
      try {
        await ScenesService.flushSyncQueue();
        await SpeedTrackingService.flushPendingSessionQueue();
        await AppLogger.uploadLogsToSupabase();
      } catch (e) {
        // We catch everything here to prevent the worker loop from crashing the app
        AppLogger.warn('[OfflineSyncWorker] Sync cycle encountered an error', { error: String(e) });
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

import { fromPromise } from 'xstate';
import { WatchBridge } from 'sk8lytz-watch-bridge';
import { AppLogger } from '../appLogger';
import { SpeedTrackingService, ISessionSnapshot } from '../SpeedTrackingService';
import { TelemetrySnapshot, HealthSnapshot } from './SessionMachine.types';

export interface SessionCommitServiceInput {
  startTimeMs: number | null;
  pausedMsAccum: number;
  onSessionSaved: () => void;
  telemetryRef: { current: TelemetrySnapshot };
  healthRef: { current: HealthSnapshot };
  userIdRef: { current: string | null };
}

export const sessionCommitService = fromPromise<void, SessionCommitServiceInput>(async ({ input }) => {
  const startTime = input.startTimeMs || Date.now();
  const finalDurationSec = (Date.now() - startTime - input.pausedMsAccum) / 1000;
  const finalDistanceMiles = input.telemetryRef.current.sessionDistanceMiles ?? 0;
  const finalAvgSpeed =
    finalDistanceMiles > 0 && finalDurationSec > 0
      ? finalDistanceMiles / (finalDurationSec / 3600)
      : 0;
  const finalCalories = input.healthRef.current.activeCalories ?? 0;
  const finalPeakHR = input.healthRef.current.peakBpm ?? 0;

  AppLogger.log('APP_LOG', { event: 'session_ending' });

  // 1. Push SUMMARY to watches (Wear OS and iOS)
  // Distance field confirmed as 'distance' in Wave 0 spike
  try {
    await WatchBridge.syncSessionState({
      status: 'SUMMARY',
      totalDuration: finalDurationSec,
      distance: finalDistanceMiles,
      avgSpeed: finalAvgSpeed,
      calories: finalCalories,
      peakHR: finalPeakHR,
    });
  } catch (err: unknown) {
    AppLogger.warn('WATCH_BRIDGE', {
      event: 'summary_push_failed',
      error: err instanceof Error ? err.message : String(err),
      payload_size: 0,
      ssi: 0,
    });
  }

  // 2. Build session snapshot payload and save via SpeedTrackingService
  if (finalDistanceMiles > 0.1 || finalDurationSec > 60) {
    const snapshot: ISessionSnapshot = {
      durationSec: finalDurationSec,
      distanceMiles: finalDistanceMiles,
      peakSpeedMph: input.telemetryRef.current.sessionPeakSpeed ?? 0,
      avgSpeedMph: parseFloat(finalAvgSpeed.toFixed(2)),
      peakGForce: input.telemetryRef.current.peakGForce ?? 1.0,
      healthBpm: input.healthRef.current.avgBpm ?? undefined,
      healthPeakBpm: input.healthRef.current.peakBpm ?? undefined,
      healthCalories: input.healthRef.current.activeCalories ?? undefined,
      startCoords: input.telemetryRef.current.startCoords,
      endCoords: input.telemetryRef.current.endCoords,
      locationCoords: input.telemetryRef.current.startCoords,
      pathCoords: input.telemetryRef.current.pathCoords,
    };

    try {
      await SpeedTrackingService.saveSession(snapshot, input.userIdRef.current);
      AppLogger.log('GLOBAL_SESSION_SAVED', {
        action: 'AUTO_SAVED_TO_DB',
        durationSec: finalDurationSec,
        distanceMiles: finalDistanceMiles,
      });
      input.onSessionSaved();
    } catch (err: unknown) {
      AppLogger.error(
        '[SessionCommitService] Failed to persist session',
        err instanceof Error ? err.message : String(err),
        { payload_size: 0, ssi: 0 }
      );
      // Notify consumer even on DB failure so the session phase exits ENDING cleanly
      input.onSessionSaved();
    }
  } else {
    AppLogger.log('GLOBAL_SESSION_DISCARDED', {
      reason: 'insufficient distance/duration',
      distanceMiles: finalDistanceMiles,
      durationSec: finalDurationSec,
    });
    // Call onSessionSaved even on discard so the consumer knows saving phase is complete
    input.onSessionSaved();
  }
});

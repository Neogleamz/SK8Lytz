jest.mock('sk8lytz-watch-bridge', () => ({
  WatchBridge: {
    syncSessionState: jest.fn().mockResolvedValue(undefined),
  },
}));

jest.mock('../../../services/AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
  },
}));

jest.mock('../../../services/SpeedTrackingService', () => ({
  SpeedTrackingService: {
    saveSession: jest.fn().mockResolvedValue(undefined),
  },
}));

import { sessionCommitService } from '../SessionCommitService';
import { SpeedTrackingService } from '../../../services/SpeedTrackingService';
import { WatchBridge } from 'sk8lytz-watch-bridge';

describe('SessionCommitService test suite', () => {
  let mockInput: any;

  beforeEach(() => {
    jest.clearAllMocks();

    mockInput = {
      startTimeMs: Date.now() - 120000, // 2 minutes ago
      pausedMsAccum: 20000, // 20 seconds of pause
      onSessionSaved: jest.fn(),
      telemetryRef: {
        current: {
          gpsSpeed: 0,
          peakGForce: 1.5,
          sessionDistanceMiles: 0.5,
          sessionDurationSec: 100,
          sessionPeakSpeed: 12.0,
          sessionAvgSpeed: 10.0,
        },
      },
      healthRef: {
        current: {
          latestBpm: 120,
          avgBpm: 110,
          peakBpm: 140,
          activeCalories: 150,
        },
      },
      userIdRef: {
        current: 'user-123',
      },
    };
  });

  const getPromise = (input: any) => (sessionCommitService as any).config({ input });

  it('1. Syncs SUMMARY state to watch and saves session to DB', async () => {
    const promise = getPromise(mockInput);
    await promise;

    // Expect SUMMARY push
    expect(WatchBridge.syncSessionState).toHaveBeenCalledWith(
      expect.objectContaining({
        status: 'SUMMARY',
        distance: 0.5,
        calories: 150,
        peakHR: 140,
      })
    );

    // Expect Save to DB
    expect(SpeedTrackingService.saveSession).toHaveBeenCalledWith(
      expect.objectContaining({
        distanceMiles: 0.5,
        peakSpeedMph: 12.0,
        peakGForce: 1.5,
        healthCalories: 150,
      }),
      'user-123'
    );

    expect(mockInput.onSessionSaved).toHaveBeenCalled();
  });

  it('2. Discards session if distance and duration are below thresholds', async () => {
    // 5 seconds ago, 0 miles
    mockInput.startTimeMs = Date.now() - 5000;
    mockInput.pausedMsAccum = 0;
    mockInput.telemetryRef.current.sessionDistanceMiles = 0;

    const promise = getPromise(mockInput);
    await promise;

    expect(SpeedTrackingService.saveSession).not.toHaveBeenCalled();
    expect(mockInput.onSessionSaved).toHaveBeenCalled(); // Calls onSessionSaved even on discard
  });
});

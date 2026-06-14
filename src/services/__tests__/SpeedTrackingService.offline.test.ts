/**
 * SpeedTrackingService.offline.test.ts
 *
 * Tests the offline session queue behaviour:
 *   A) Queue write when user is unauthenticated
 *   B) Flush happy path — user_id stamped at flush time
 *   C) Re-entrancy guard — INSERT called exactly once on concurrent flush calls
 *   D) No auth session — queue preserved intact
 */

import AsyncStorage from '@react-native-async-storage/async-storage';
import { Alert } from 'react-native';

// ── Mocks ─────────────────────────────────────────────────────────────────────

jest.mock('@react-native-async-storage/async-storage', () =>
  require('@react-native-async-storage/async-storage/jest/async-storage-mock')
);

jest.mock('react-native', () => ({
  Alert: { alert: jest.fn() },
  Platform: { OS: 'android', select: jest.fn((obj: Record<string, unknown>) => obj.android) },
}));

jest.mock('sk8lytz-watch-bridge', () => ({
  WatchBridge: { sendMetricUpdate: jest.fn().mockResolvedValue(undefined) },
}));

const mockInsert = jest.fn();
const mockFrom = jest.fn((_table: string) => ({ insert: mockInsert }));
const mockGetUser = jest.fn();
const mockGetSession = jest.fn();

jest.mock('../supabaseClient', () => ({
  supabase: {
    auth: {
      getUser: () => mockGetUser(),
      getSession: () => mockGetSession(),
    },
    from: (table: string) => mockFrom(table),
  },
}));

jest.mock('../AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    info: jest.fn(),
    debug: jest.fn(),
    error: jest.fn(),
    uploadLogsToSupabase: jest.fn().mockResolvedValue(undefined),
  },
}));

// ── Import after mocks ────────────────────────────────────────────────────────

import {
  SpeedTrackingService,
  type PendingSessionRecord,
  type ISessionSnapshot,
} from '../SpeedTrackingService';
import { PENDING_SESSION_QUEUE_KEY } from '../../constants/storageKeys';

// ── Fixtures ──────────────────────────────────────────────────────────────────
const mockUserId = 'user_offline_mock';

const mockSnapshot: ISessionSnapshot = {
  durationSec: 1800,
  distanceMiles: 5.2,
  avgSpeedMph: 10.4,
  peakSpeedMph: 18.7,
  peakGForce: 2.1,
  locationLabel: 'Venice Beach',
  crewSessionId: undefined,
  healthBpm: 142,
  healthPeakBpm: 178,
  healthCalories: 420,
};

const mockSessionUser = { id: 'user-uuid-abc123' };

// ── Tests ─────────────────────────────────────────────────────────────────────

beforeEach(async () => {
  await AsyncStorage.clear().catch(() => {});
  jest.clearAllMocks();
  // Reset the re-entrancy guard between tests by using the service singleton
  // (the guard resets in the finally block, but we need a clean state)
});

// ── Test A: Queue write when unauthenticated ──────────────────────────────────

describe('saveSession() — offline queue', () => {
  it('A: queues session to AsyncStorage when user is null', async () => {
    mockGetUser.mockResolvedValueOnce({ data: { user: null } });

    const result = await SpeedTrackingService.saveSession(mockSnapshot, null);

    // Returns null (did not save to Supabase)
    expect(result).toBeNull();

    // Should NOT have called supabase.from (no cloud insert)
    expect(mockFrom).not.toHaveBeenCalled();

    // Should show user feedback
    expect(Alert.alert).toHaveBeenCalledWith(
      'Session Saved Locally',
      expect.stringContaining('offline'),
    );

    // Should have written to the queue key
    const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY).catch(() => null);
    expect(raw).not.toBeNull();

    const queue: PendingSessionRecord[] = JSON.parse(raw!);
    expect(queue).toHaveLength(1);

    const entry = queue[0];
    // All snapshot fields present
    expect(entry.durationSec).toBe(mockSnapshot.durationSec);
    expect(entry.distanceMiles).toBe(mockSnapshot.distanceMiles);
    expect(entry.peakSpeedMph).toBe(mockSnapshot.peakSpeedMph);
    // Pre-computed calories present (used healthCalories since it was set)
    expect(entry.calories).toBe(420);
    // Timestamp present and parseable
    expect(new Date(entry.queued_at).getTime()).not.toBeNaN();
    // NO user_id stored in queue record
    expect('user_id' in entry).toBe(false);
  });
});

// ── Test B: Flush happy path ──────────────────────────────────────────────────

describe('flushPendingSessionQueue() — happy path', () => {
  it('B: stamps user_id at flush time and clears flushed entries', async () => {
    // Pre-populate queue
    const pending: PendingSessionRecord = {
      ...mockSnapshot,
      calories: 420,
      queued_at: new Date().toISOString(),
    };
    await AsyncStorage.setItem(PENDING_SESSION_QUEUE_KEY, JSON.stringify([pending])).catch(() => {});

    // Auth session present
    mockGetSession.mockResolvedValueOnce({
      data: { session: { user: mockSessionUser } },
    });

    // Supabase insert succeeds
    mockInsert.mockResolvedValueOnce({ error: null });

    await SpeedTrackingService.flushPendingSessionQueue(mockSessionUser.id);

    // Insert was called exactly once
    expect(mockFrom).toHaveBeenCalledWith('skate_sessions');
    expect(mockInsert).toHaveBeenCalledTimes(1);

    // user_id was stamped from the session (not from the queue record)
    const insertPayload = mockInsert.mock.calls[0][0];
    expect(insertPayload.user_id).toBe(mockSessionUser.id);
    expect(insertPayload.duration_sec).toBe(Math.round(pending.durationSec));
    expect(insertPayload.location_label).toBe(pending.locationLabel);

    // Queue cleared after successful flush
    const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY).catch(() => null);
    const remaining: PendingSessionRecord[] = JSON.parse(raw!);
    expect(remaining).toHaveLength(0);
  });
});

// ── Test C: Re-entrancy guard ─────────────────────────────────────────────────

describe('flushPendingSessionQueue() — re-entrancy guard', () => {
  it('C: concurrent flush calls result in exactly one Supabase insert', async () => {
    const pending: PendingSessionRecord = {
      ...mockSnapshot,
      calories: 420,
      queued_at: new Date().toISOString(),
    };
    await AsyncStorage.setItem(PENDING_SESSION_QUEUE_KEY, JSON.stringify([pending])).catch(() => {});

    mockGetSession.mockResolvedValue({
      data: { session: { user: mockSessionUser } },
    });

    // Make insert take a moment so the guard has time to block the second call
    mockInsert.mockImplementation(
      () => Promise.resolve().then(() => ({ error: null }))
    );

    // Fire both without await — second should be blocked by guard
    const [, ] = await Promise.all([
      SpeedTrackingService.flushPendingSessionQueue(mockSessionUser.id),
      SpeedTrackingService.flushPendingSessionQueue(mockSessionUser.id),
    ]);

    expect(mockInsert).toHaveBeenCalledTimes(1);
  });
});

// ── Test D: No auth session — queue preserved ─────────────────────────────────

describe('flushPendingSessionQueue() — no auth session', () => {
  it('D: leaves queue intact when no active session', async () => {
    const pending: PendingSessionRecord = {
      ...mockSnapshot,
      calories: 420,
      queued_at: new Date().toISOString(),
    };
    await AsyncStorage.setItem(PENDING_SESSION_QUEUE_KEY, JSON.stringify([pending])).catch(() => {});

    // No auth session
    mockGetSession.mockResolvedValueOnce({
      data: { session: null },
    });

    await SpeedTrackingService.flushPendingSessionQueue(null);

    // Insert NOT called
    expect(mockInsert).not.toHaveBeenCalled();

    // Queue still has the record
    const raw = await AsyncStorage.getItem(PENDING_SESSION_QUEUE_KEY).catch(() => null);
    const remaining: PendingSessionRecord[] = JSON.parse(raw!);
    expect(remaining).toHaveLength(1);
  });
});

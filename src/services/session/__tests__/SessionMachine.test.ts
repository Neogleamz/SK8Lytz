(global as unknown as { __DEV__: boolean }).__DEV__ = true;

jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn().mockResolvedValue(null),
  setItem: jest.fn().mockResolvedValue(undefined),
  removeItem: jest.fn().mockResolvedValue(undefined),
  multiSet: jest.fn().mockResolvedValue(undefined),
}));

jest.mock('expo-secure-store', () => ({
  getItemAsync: jest.fn().mockResolvedValue(null),
  setItemAsync: jest.fn().mockResolvedValue(undefined),
  deleteItemAsync: jest.fn().mockResolvedValue(undefined),
}));

jest.mock('expo-device', () => ({
  brand: 'MockBrand',
  modelName: 'MockModel',
}));

jest.mock('expo-battery', () => ({
  isAvailableAsync: jest.fn().mockResolvedValue(true),
  getBatteryLevelAsync: jest.fn().mockResolvedValue(1),
  getBatteryStateAsync: jest.fn().mockResolvedValue(1),
}));

jest.mock('react-native-url-polyfill/auto', () => ({}));

jest.mock('@notifee/react-native', () => ({
  default: {
    displayNotification: jest.fn().mockResolvedValue(undefined),
    createChannel: jest.fn().mockResolvedValue(undefined),
    stopForegroundService: jest.fn().mockResolvedValue(undefined),
    cancelNotification: jest.fn().mockResolvedValue(undefined),
  },
  AndroidImportance: { HIGH: 4, LOW: 2 },
  AndroidForegroundServiceType: { FOREGROUND_SERVICE_TYPE_LOCATION: 8 },
}));

jest.mock('react-native/Libraries/vendor/emitter/EventEmitter', () => {
  return {};
}, { virtual: true });

jest.mock('sk8lytz-watch-bridge', () => ({
  WatchBridge: {
    syncSessionState: jest.fn().mockResolvedValue(undefined),
    addWatchCommandListener: jest.fn().mockReturnValue(() => {}),
    addWatchHealthListener: jest.fn().mockReturnValue(() => {}),
  },
}));

jest.mock('../../AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
    info: jest.fn(),
  },
}));

jest.mock('expo-sensors', () => ({
  Accelerometer: {
    addListener: jest.fn().mockReturnValue({ remove: jest.fn() }),
    setUpdateInterval: jest.fn(),
  },
}));

jest.mock('expo-location', () => ({
  Accuracy: { Balanced: 3 },
  watchPositionAsync: jest.fn().mockResolvedValue({ remove: jest.fn() }),
  getForegroundPermissionsAsync: jest.fn().mockResolvedValue({ status: 'granted' }),
}));

jest.mock('react-native', () => ({
  Platform: {
    OS: 'android',
    select: jest.fn((objs) => objs.android || objs.default),
  },
  Alert: {
    alert: jest.fn(),
  },
  DeviceEventEmitter: {
    addListener: jest.fn().mockReturnValue({ remove: jest.fn() }),
  },
  AppState: {
    addEventListener: jest.fn().mockReturnValue({ remove: jest.fn() }),
  },
}));

import { createActor, fromPromise, fromCallback } from 'xstate';
import { sessionMachine } from '../SessionMachine';
import { SessionMachineContext } from '../SessionMachine.types';

describe('SessionMachine test suite', () => {
  let mockCommitResolve: () => void;
  let mockCommitPromise: Promise<void>;
  let actorsList: ReturnType<typeof createActor>[] = [];

  const testSessionMachine = sessionMachine.provide({
    actors: {
      sensorService: fromCallback(() => {}),
      autoPauseService: fromCallback(() => {}),
      healthService: fromCallback(() => {}),
      notificationService: fromCallback(() => {}),
      sessionCommitService: fromPromise(() => {
        mockCommitPromise = new Promise((resolve) => {
          mockCommitResolve = resolve;
        });
        return mockCommitPromise;
      }),
    },
  });

  let mockInput: Omit<SessionMachineContext, 'sessionPhase' | 'startTimeMs' | 'pauseStartTimeMs' | 'pausedMsAccum'>;

  beforeEach(() => {
    jest.clearAllMocks();
    actorsList = [];

    mockInput = {
      autoPauseEnabled: true,
      gpsSpeedRef: { current: 0 },
      telemetryRef: { current: { gpsSpeed: 0, peakGForce: 1, sessionDistanceMiles: 0, sessionDurationSec: 0, sessionPeakSpeed: 0, sessionAvgSpeed: 0 } },
      healthRef: { current: { latestBpm: null, avgBpm: null, peakBpm: null, activeCalories: null } },
      userIdRef: { current: 'user-1' },
      onTelemetryUpdate: jest.fn(),
      onHealthUpdate: jest.fn(),
      onSessionSaved: jest.fn(),
    };
  });

  afterEach(() => {
    actorsList.forEach((actor) => {
      try {
        actor.stop();
      } catch {}
    });
  });

  const createTestActor = () => {
    const actor = createActor(testSessionMachine, { input: mockInput });
    actorsList.push(actor);
    return actor;
  };

  it('1. IDLE -> ACTIVE on START', () => {
    const actor = createTestActor();
    actor.start();
    expect(actor.getSnapshot().value).toBe('IDLE');

    actor.send({ type: 'START' });
    expect(actor.getSnapshot().value).toBe('ACTIVE');
    expect(actor.getSnapshot().context.sessionPhase).toBe('ACTIVE');
    expect(actor.getSnapshot().context.startTimeMs).not.toBeNull();
  });

  it('2. ACTIVE -> PAUSED on PAUSE', () => {
    const actor = createTestActor();
    actor.start();
    actor.send({ type: 'START' });
    expect(actor.getSnapshot().value).toBe('ACTIVE');

    actor.send({ type: 'PAUSE' });
    expect(actor.getSnapshot().value).toBe('PAUSED');
    expect(actor.getSnapshot().context.sessionPhase).toBe('PAUSED');
    expect(actor.getSnapshot().context.pauseStartTimeMs).not.toBeNull();
  });

  it('3. PAUSED -> ACTIVE on RESUME', () => {
    const actor = createTestActor();
    actor.start();
    actor.send({ type: 'START' });
    actor.send({ type: 'PAUSE' });
    expect(actor.getSnapshot().value).toBe('PAUSED');

    actor.send({ type: 'RESUME' });
    expect(actor.getSnapshot().value).toBe('ACTIVE');
    expect(actor.getSnapshot().context.sessionPhase).toBe('ACTIVE');
    expect(actor.getSnapshot().context.pauseStartTimeMs).toBeNull();
  });

  it('4. ACTIVE -> ENDING on END', () => {
    const actor = createTestActor();
    actor.start();
    actor.send({ type: 'START' });
    expect(actor.getSnapshot().value).toBe('ACTIVE');

    actor.send({ type: 'END' });
    expect(actor.getSnapshot().value).toBe('ENDING');
    expect(actor.getSnapshot().context.sessionPhase).toBe('ENDING');
  });

  it('5. PAUSED -> ENDING on END', () => {
    const actor = createTestActor();
    actor.start();
    actor.send({ type: 'START' });
    actor.send({ type: 'PAUSE' });
    expect(actor.getSnapshot().value).toBe('PAUSED');

    actor.send({ type: 'END' });
    expect(actor.getSnapshot().value).toBe('ENDING');
    expect(actor.getSnapshot().context.sessionPhase).toBe('ENDING');
  });

  it('6. ENDING -> IDLE on sessionCommitService done', async () => {
    const actor = createTestActor();
    actor.start();
    actor.send({ type: 'START' });
    actor.send({ type: 'END' });
    expect(actor.getSnapshot().value).toBe('ENDING');

    mockCommitResolve();
    await mockCommitPromise;
    await new Promise((res) => setTimeout(res, 0));

    expect(actor.getSnapshot().value).toBe('IDLE');
    expect(actor.getSnapshot().context.sessionPhase).toBe('IDLE');
  });
});

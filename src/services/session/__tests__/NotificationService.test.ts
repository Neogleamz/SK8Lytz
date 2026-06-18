import { notificationService } from '../NotificationService';
import notifee from '@notifee/react-native';
import * as Location from 'expo-location';
import { Platform, AppState } from 'react-native';
import type { CallbackServiceActor } from '../../../__tests__/test-env';

jest.mock('react-native', () => ({
  Platform: { OS: 'android' },
  AppState: { currentState: 'active', addEventListener: jest.fn() }
}));

jest.mock('@notifee/react-native', () => ({
  __esModule: true,
  default: {
    displayNotification: jest.fn(),
    createChannel: jest.fn(),
    stopForegroundService: jest.fn(),
    cancelNotification: jest.fn()
  },
  AndroidImportance: { LOW: 2 },
  AndroidForegroundServiceType: { FOREGROUND_SERVICE_TYPE_LOCATION: 8 }
}));

jest.mock('expo-location', () => ({
  getForegroundPermissionsAsync: jest.fn().mockResolvedValue({ status: 'granted' })
}));

jest.mock('../../appLogger', () => ({
  AppLogger: { error: jest.fn(), warn: jest.fn() }
}));

describe('NotificationService test suite', () => {
  let telemetryRef: { current: { sessionDistanceMiles: number; gpsSpeed: number } };

  beforeEach(() => {
    telemetryRef = { current: { sessionDistanceMiles: 1.5, gpsSpeed: 10.2 } };
    jest.useFakeTimers();
    (notifee.displayNotification as jest.Mock).mockClear();
    (notifee.stopForegroundService as jest.Mock).mockClear();
    Platform.OS = 'android';
    AppState.currentState = 'active';
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  const getCallback = () => (notificationService as unknown as CallbackServiceActor).config;

  it('1. ACTIVE phase: notification displays PAUSE + END action buttons', async () => {
    const callback = getCallback();
    const cleanup = callback({
      input: {
        sessionPhase: 'ACTIVE',
        startTimeMs: Date.now(),
        pausedMsAccum: 0,
        telemetryRef
      }
    });

    // flush promises
    for (let i = 0; i < 5; i++) await Promise.resolve();
    
    expect(notifee.displayNotification).toHaveBeenCalled();
    const args = (notifee.displayNotification as jest.Mock).mock.calls[0][0];
    
    expect(args.title).toContain('Skate Session Active');
    expect(args.android.actions).toHaveLength(3);
    expect(args.android.actions[0].title).toContain('END');
    expect(args.android.actions[1].title).toContain('MUSIC');
    expect(args.android.actions[2].title).toContain('FAVORITE');
    cleanup?.();
  });

  it('2. PAUSED phase: notification displays RESUME + END action buttons', async () => {
    const callback = getCallback();
    const cleanup = callback({
      input: {
        sessionPhase: 'PAUSED',
        startTimeMs: Date.now(),
        pausedMsAccum: 1000,
        telemetryRef
      }
    });

    for (let i = 0; i < 5; i++) await Promise.resolve();

    expect(notifee.displayNotification).toHaveBeenCalled();
    const args = (notifee.displayNotification as jest.Mock).mock.calls[0][0];
    
    expect(args.title).toContain('Paused');
    expect(args.android.actions).toHaveLength(3);
    expect(args.android.actions[0].title).toContain('END');
    expect(args.android.actions[1].title).toContain('MUSIC');
    expect(args.android.actions[2].title).toContain('FAVORITE');
    cleanup?.();
  });

  it('3. ENDING phase: notification displays NO action buttons', async () => {
    const callback = getCallback();
    const cleanup = callback({
      input: {
        sessionPhase: 'ENDING',
        startTimeMs: Date.now(),
        pausedMsAccum: 0,
        telemetryRef
      }
    });

    for (let i = 0; i < 5; i++) await Promise.resolve();

    expect(notifee.displayNotification).toHaveBeenCalled();
    const args = (notifee.displayNotification as jest.Mock).mock.calls[0][0];
    
    expect(args.title).toContain('Saving Session');
    expect(args.android.actions).toHaveLength(0); // NO actions during ENDING
    cleanup?.();
  });

  it('4. Cleanup calls cancelNotification', async () => {
    const callback = getCallback();
    const cleanup = callback({
      input: {
        sessionPhase: 'ACTIVE',
        startTimeMs: Date.now(),
        pausedMsAccum: 0,
        telemetryRef
      }
    });

    for (let i = 0; i < 5; i++) await Promise.resolve();
    cleanup?.();
    for (let i = 0; i < 5; i++) await Promise.resolve();

    expect(notifee.cancelNotification).toHaveBeenCalled();
  });
});

import { healthService } from '../HealthService';
import { WatchBridge } from 'sk8lytz-watch-bridge';
import { checkPermission } from '../../PermissionService';
import { Platform } from 'react-native';

jest.mock('react-native', () => ({
  Platform: { OS: 'ios' }
}));

jest.mock('sk8lytz-watch-bridge', () => ({
  WatchBridge: {
    addWatchHealthListener: jest.fn()
  }
}));

jest.mock('../../PermissionService', () => ({
  checkPermission: jest.fn()
}));

jest.mock('../../AppLogger', () => ({
  AppLogger: { log: jest.fn(), warn: jest.fn(), error: jest.fn() }
}));

describe('HealthService test suite', () => {
  let onHealthUpdateMock: jest.Mock;
  
  beforeEach(() => {
    onHealthUpdateMock = jest.fn();
    jest.useFakeTimers();
    (checkPermission as jest.Mock).mockResolvedValue(true);
    (WatchBridge.addWatchHealthListener as jest.Mock).mockClear();
    Platform.OS = 'ios'; // Default to iOS for testing the interval
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  const getCallback = () => (healthService as any).config;

  it('1. addWatchHealthListener callback fires -> onHealthUpdate called', () => {
    let watchListener: any;
    const unsubscribeMock = jest.fn();
    (WatchBridge.addWatchHealthListener as jest.Mock).mockImplementation((cb) => {
      watchListener = cb;
      return unsubscribeMock;
    });

    const callback = getCallback();
    const cleanup = callback({
      input: {
        onHealthUpdate: onHealthUpdateMock
      }
    });

    expect(watchListener).toBeDefined();

    // Trigger watch update
    watchListener({
      heartRate: 150,
      calories: 300,
      status: 'active'
    });

    expect(onHealthUpdateMock).toHaveBeenCalledWith(
      expect.objectContaining({
        latestBpm: 150,
        activeCalories: 300
      })
    );

    cleanup();
  });

  it('2. 30s poll interval fires -> phone health data check runs', async () => {
    let watchListener: any;
    (WatchBridge.addWatchHealthListener as jest.Mock).mockImplementation((cb) => {
      watchListener = cb;
      return jest.fn();
    });

    // We will just verify that the checkPermission mock is hit when the 30s timer triggers
    const callback = getCallback();
    const cleanup = callback({
      input: {
        onHealthUpdate: onHealthUpdateMock
      }
    });

    // We must flush promises so the initial pollHealthData() completes
    // and resets isPolling to false, otherwise the 30s interval
    // will just exit early!
    for (let i = 0; i < 5; i++) await Promise.resolve();

    (checkPermission as jest.Mock).mockClear();

    // Advance 30 seconds
    jest.advanceTimersByTime(30000);
    
    // Flush promises again to let the interval's pollHealthData complete
    for (let i = 0; i < 5; i++) await Promise.resolve();

    // pollHealthData should be called
    expect(checkPermission).toHaveBeenCalledWith('HEALTH');

    cleanup();
  });

  it('3. Cleanup calls unsubscribeHealth and stops polling', () => {
    const unsubscribeMock = jest.fn();
    (WatchBridge.addWatchHealthListener as jest.Mock).mockReturnValue(unsubscribeMock);

    const callback = getCallback();
    const cleanup = callback({
      input: {
        onHealthUpdate: onHealthUpdateMock
      }
    });

    cleanup();

    expect(unsubscribeMock).toHaveBeenCalled();

    (checkPermission as jest.Mock).mockClear();
    // Advance 30 seconds to ensure timer was cleared
    jest.advanceTimersByTime(30000);
    expect(checkPermission).not.toHaveBeenCalled();
  });
});

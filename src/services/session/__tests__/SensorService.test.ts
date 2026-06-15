import { sensorService } from '../SensorService';
import * as Location from 'expo-location';
import { Accelerometer } from 'expo-sensors';
import { checkPermission } from '../../PermissionService';
import { Platform } from 'react-native';

jest.mock('react-native', () => ({
  Platform: { OS: 'ios' }
}));

jest.mock('expo-location', () => ({
  watchPositionAsync: jest.fn(),
  Accuracy: { Balanced: 2 }
}));

jest.mock('expo-sensors', () => ({
  Accelerometer: {
    setUpdateInterval: jest.fn(),
    addListener: jest.fn()
  }
}));

jest.mock('../../PermissionService', () => ({
  checkPermission: jest.fn(),
  openGlobalPermissionsModal: jest.fn()
}));

jest.mock('../../appLogger', () => ({
  AppLogger: { error: jest.fn() }
}));

jest.mock('../../CrewService', () => ({
  crewService: { currentSessionId: null }
}));

jest.mock('../../SpeedTrackingService', () => ({
  SpeedTrackingService: { pushSpeedToWatch: jest.fn() }
}));

describe('SensorService test suite', () => {
  let onTelemetryUpdateMock: jest.Mock;
  let gpsSpeedRef: { current: number };

  beforeEach(() => {
    onTelemetryUpdateMock = jest.fn();
    gpsSpeedRef = { current: 0 };
    jest.useFakeTimers();
    (checkPermission as jest.Mock).mockResolvedValue(true);
    (Location.watchPositionAsync as jest.Mock).mockClear();
    (Accelerometer.addListener as jest.Mock).mockClear();
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  const getCallback = () => (sensorService as any).config;

  it('1. GPS speed tick updates telemetry via onTelemetryUpdate', async () => {
    let watchCallback: any;
    (Location.watchPositionAsync as jest.Mock).mockImplementation((options, cb) => {
      watchCallback = cb;
      return Promise.resolve({ remove: jest.fn() });
    });

    const callback = getCallback();
    const cleanup = callback({
      input: {
        onTelemetryUpdate: onTelemetryUpdateMock,
        gpsSpeedRef,
        startTimeMs: Date.now() - 10000,
      },
    });

    // Let the async location setup resolve
    await Promise.resolve();

    expect(watchCallback).toBeDefined();

    // Trigger GPS update
    watchCallback({
      coords: { speed: 5, latitude: 10, longitude: 20 },
      timestamp: Date.now()
    });

    expect(gpsSpeedRef.current).toBe(5 * 2.23694); // 11.1847 mph
    expect(onTelemetryUpdateMock).toHaveBeenCalledWith(
      expect.objectContaining({
        gpsSpeed: gpsSpeedRef.current,
        sessionPeakSpeed: gpsSpeedRef.current
      })
    );

    cleanup();
  });

  it('2. Accelerometer high-G tick updates peakGForce', () => {
    let accelCallback: any;
    (Accelerometer.addListener as jest.Mock).mockImplementation((cb) => {
      accelCallback = cb;
      return { remove: jest.fn() };
    });

    const callback = getCallback();
    const cleanup = callback({
      input: {
        onTelemetryUpdate: onTelemetryUpdateMock,
        gpsSpeedRef,
        startTimeMs: Date.now(),
      },
    });

    expect(accelCallback).toBeDefined();

    // Trigger high-G event (simulate 2G total vector)
    // x=1.15, y=1.15, z=1.15 -> sqrt = ~2.0
    accelCallback({ x: 1.15, y: 1.15, z: 1.15 });

    // The decayed G will be calculated, peak should be higher than 1.0
    expect(onTelemetryUpdateMock).toHaveBeenCalledWith(
      expect.objectContaining({
        peakGForce: expect.any(Number)
      })
    );
    const lastCallArg = onTelemetryUpdateMock.mock.calls[onTelemetryUpdateMock.mock.calls.length - 1][0];
    expect(lastCallArg.peakGForce).toBeGreaterThan(1.0);

    cleanup();
  });

  it('3. Cleanup removes both subscriptions (location + accelerometer)', async () => {
    const locRemoveMock = jest.fn();
    (Location.watchPositionAsync as jest.Mock).mockResolvedValue({ remove: locRemoveMock });

    const accelRemoveMock = jest.fn();
    (Accelerometer.addListener as jest.Mock).mockReturnValue({ remove: accelRemoveMock });

    const callback = getCallback();
    const cleanup = callback({
      input: {
        onTelemetryUpdate: onTelemetryUpdateMock,
        gpsSpeedRef,
        startTimeMs: Date.now(),
      },
    });

    for (let i = 0; i < 5; i++) await Promise.resolve();

    cleanup();

    expect(locRemoveMock).toHaveBeenCalled();
    expect(accelRemoveMock).toHaveBeenCalled();
  });

  it('4. onTelemetryUpdate NOT called after cleanup via the 1s interval', () => {
    const callback = getCallback();
    const cleanup = callback({
      input: {
        onTelemetryUpdate: onTelemetryUpdateMock,
        gpsSpeedRef,
        startTimeMs: Date.now(),
      },
    });

    onTelemetryUpdateMock.mockClear();
    
    cleanup();
    
    // Advance 5 seconds
    jest.advanceTimersByTime(5000);
    
    expect(onTelemetryUpdateMock).not.toHaveBeenCalled();
  });
});

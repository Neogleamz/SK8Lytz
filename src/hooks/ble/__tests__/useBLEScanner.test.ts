import React from 'react';
import { Platform } from 'react-native';
import { useBLEScanner } from '../useBLEScanner';
import { ZENGGE_SERVICE_UUID } from '../../../protocols/ZenggeProtocol';
import type { BleManager, Device } from 'react-native-ble-plx';
import type { GlobalWithDev, MutablePlatform } from '../../../__tests__/test-env';

// Mock React Hooks to inspect returned functions of useBLEScanner directly
jest.mock('react', () => {
  const actual = jest.requireActual('react');
  return {
    ...actual,
    useState: jest.fn((init) => [init, jest.fn()]),
    useRef: jest.fn((init) => ({ current: init })),
    useEffect: jest.fn(),
    useCallback: jest.fn((fn) => fn),
  };
});

// Mock other dependencies
jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn().mockResolvedValue(null),
  setItem: jest.fn().mockResolvedValue(undefined),
}));

jest.mock('react-native', () => ({
  Platform: {
    OS: 'android',
    select: jest.fn((objs) => objs.android || objs.default),
  },
  InteractionManager: {
    runAfterInteractions: jest.fn((fn) => fn()),
  },
}));

jest.mock('../../../services/AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
  },
}));

jest.mock('../../../services/supabaseClient', () => ({
  supabase: null,
}));

jest.mock('../../../services/LocationService', () => ({
  locationService: {
    getSilentLocation: jest.fn().mockResolvedValue(null),
  },
}));

jest.mock('../../../utils/classifyBLEDevice', () => ({
  mapDeviceToRegistration: jest.fn().mockReturnValue({}),
}));

const mockStartSweeper = jest.fn();
const mockStopSweeper = jest.fn();
const mockBurstScan = jest.fn();

jest.mock('../useBLEBatterySweep', () => ({
  useBLEBatterySweep: jest.fn(() => ({
    isSweeperActive: false,
    startSweeper: mockStartSweeper,
    stopSweeper: mockStopSweeper,
    burstScan: mockBurstScan,
    batteryTier: 'HIGH',
  })),
}));

jest.mock('../useBLEInterrogator', () => ({
  useBLEInterrogator: jest.fn(() => ({
    hwCache: {},
    hwCacheRef: { current: {} },
    queueDeviceForInterrogation: jest.fn(),
  })),
}));

describe('useBLEScanner unit tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    jest.useFakeTimers();
    (global as GlobalWithDev).__DEV__ = true;
    (Platform as unknown as MutablePlatform).OS = 'android';
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  it('asserts that when registeredMacs.length === 0, scanForPeripherals calls startSweeper unconditionally', () => {
    const mockBleSend = jest.fn();
    const mockSetAllDevices = jest.fn();

    const scanner = useBLEScanner({
      bleManager: {} as unknown as BleManager,
      allDevices: [],
      setAllDevices: mockSetAllDevices,
      bleSend: mockBleSend,
      registeredMacs: [], // FTUE state: no registered devices
      isSandboxEnabled: false,
    });

    scanner.scanForPeripherals();

    // In FTUE, startSweeper must run directly to avoid async battery level checks racing
    expect(mockStartSweeper).toHaveBeenCalledTimes(1);
    // Standard manual scan events should not fire in FTUE persistent sweeper mode
    expect(mockBleSend).not.toHaveBeenCalledWith({ type: 'SCAN_START' });
  });

  it('asserts sandbox mock devices are discovered when sandbox is enabled', () => {
    const mockBleSend = jest.fn();
    const mockSetAllDevices = jest.fn();

    // Force Platform.OS to web to guarantee isSandboxMocking evaluates to true
    (Platform as unknown as MutablePlatform).OS = 'web';

    const scanner = useBLEScanner({
      bleManager: {} as unknown as BleManager,
      allDevices: [],
      setAllDevices: mockSetAllDevices,
      bleSend: mockBleSend,
      registeredMacs: [],
      isSandboxEnabled: true,
    });

    scanner.scanForPeripherals();

    // Web sandbox starts scan
    expect(mockBleSend).toHaveBeenCalledWith({ type: 'SCAN_START' });

    // Fast-forward 1 second to fire the discovery timer
    jest.advanceTimersByTime(1000);

    // Fast-forward to 5 seconds to fire the stop timer
    jest.advanceTimersByTime(4000);
    expect(mockBleSend).toHaveBeenCalledWith({ type: 'SCAN_STOP' });
  });
});

/* eslint-disable no-undef */
(global as unknown as { __DEV__: boolean }).__DEV__ = true;

jest.mock('expo-battery', () => ({
  isAvailableAsync: jest.fn().mockResolvedValue(true),
  getBatteryLevelAsync: jest.fn().mockResolvedValue(1),
  getBatteryStateAsync: jest.fn().mockResolvedValue(1),
}));

jest.mock('expo-device', () => ({
  brand: 'MockBrand',
  modelName: 'MockModel',
}));

jest.mock('expo-secure-store', () => ({
  getItemAsync: jest.fn().mockResolvedValue(null),
  setItemAsync: jest.fn().mockResolvedValue(undefined),
  deleteItemAsync: jest.fn().mockResolvedValue(undefined),
}));

jest.mock('react-native-url-polyfill/auto', () => ({}));

jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn().mockResolvedValue(null),
  setItem: jest.fn().mockResolvedValue(undefined),
  removeItem: jest.fn().mockResolvedValue(undefined),
  getAllKeys: jest.fn().mockResolvedValue([]),
  multiGet: jest.fn().mockResolvedValue([]),
}));

jest.mock('react-native', () => ({
  Platform: {
    OS: 'android',
    select: jest.fn((objs: Record<string, unknown>) => objs.android ?? objs.default),
  },
  Alert: { alert: jest.fn() },
  DeviceEventEmitter: {
    addListener: jest.fn().mockReturnValue({ remove: jest.fn() }),
  },
  AppState: {
    addEventListener: jest.fn().mockReturnValue({ remove: jest.fn() }),
  },
}));

// Silence AppLogger to prevent ENOBUFS buffer overflow in verifiable-check-runner.js
jest.mock('../../appLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
    info: jest.fn(),
    flushQueues: jest.fn(),
  },
}));

jest.mock('../../BleWriteQueue', () => ({
  enqueueWrite: jest.fn(async (_priority: any, op: () => Promise<any>) => op()),
  enqueueDelay: jest.fn(async (_priority: any, delay: number) => new Promise(res => setTimeout(res, delay))),
}));

jest.mock('../../BleSessionFactory', () => ({
  createGattSession: jest.fn(),
}));

import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import type { BleManager, Subscription } from 'react-native-ble-plx';
import {
  interrogateDevice,
  createProbeQueue,
  loadHWCacheFromStorage,
  PROBE_QUEUE_DELAY_MS,
  PROBE_QUEUE_DELAY_MS_FTUE,
} from '../InterrogatorService';
import { createGattSession } from '../../BleSessionFactory';
import { enqueueWrite } from '../../BleWriteQueue';
import type { PingResult } from '../../../types/dashboard.types';

describe('InterrogatorService test suite', () => {
  let mockBleManager: {
    writeCharacteristicWithoutResponseForDevice: jest.Mock;
    monitorCharacteristicForDevice: jest.Mock;
    cancelDeviceConnection: jest.Mock;
  };
  let mockAdapter: {
    serviceUUID: string;
    writeCharacteristicUUID: string;
    notifyCharacteristicUUID: string;
    buildQuerySettings: jest.Mock;
    buildQueryRfRemoteState: jest.Mock;
    parseSettingsResponse: jest.Mock;
    parseRfRemoteState: jest.Mock;
  };
  let mockSubscription: Subscription;
  let mockProbingMacsRef: { current: Set<string> };
  let mockHwCacheRef: { current: Record<string, PingResult> };
  let mockOnDeviceInterrogated: jest.Mock;

  // Captures the notification callback registered by monitorCharacteristicForDevice
  let capturedNotificationCb: ((err: any, char: any) => void) | null = null;

  const MOCK_MAC = 'AA:BB:CC:DD:EE:FF';

  const makeMockPingResult = (): PingResult => ({
    detected: true,
    ledPoints: 90,
    segments: 3,
    icType: 6,
    icName: 'WS2812B',
    colorSorting: 0,
    colorSortingName: 'RGB',
    rfMode: 'single',
    rfPairedCount: 2,
  });

  beforeEach(() => {
    jest.clearAllMocks();
    jest.useFakeTimers();
    (Platform as unknown as { OS: string }).OS = 'android';

    capturedNotificationCb = null;
    mockSubscription = { remove: jest.fn() } as unknown as Subscription;

    mockAdapter = {
      serviceUUID: 'uuid-service',
      writeCharacteristicUUID: 'uuid-write',
      notifyCharacteristicUUID: 'uuid-notify',
      buildQuerySettings: jest.fn().mockReturnValue({ packets: [[0x63, 0x12]] }),
      buildQueryRfRemoteState: jest.fn().mockReturnValue({ packets: [[0x87, 0x00]] }),
      parseSettingsResponse: jest.fn().mockReturnValue(null),
      parseRfRemoteState: jest.fn().mockReturnValue(null),
    };

    mockBleManager = {
      writeCharacteristicWithoutResponseForDevice: jest.fn().mockResolvedValue(undefined),
      monitorCharacteristicForDevice: jest.fn().mockImplementation(
        (_mac: string, _serviceUUID: string, _charUUID: string, cb: (err: any, char: any) => void) => {
          capturedNotificationCb = cb;
          return mockSubscription;
        }
      ),
      cancelDeviceConnection: jest.fn().mockResolvedValue(undefined),
    };

    (createGattSession as jest.Mock).mockResolvedValue({
      conn: { id: MOCK_MAC },
      adapter: mockAdapter,
    });

    mockProbingMacsRef = { current: new Set() };
    mockHwCacheRef = { current: {} };
    mockOnDeviceInterrogated = jest.fn();
  });

  afterEach(() => {
    jest.clearAllTimers();
    jest.useRealTimers();
  });

  // ─── Group A: interrogateDevice Happy Path ─────────────────────────────────

  it('1. Successful probe → onDeviceInterrogated called with parsed PingResult', async () => {
    const pingResult = makeMockPingResult();

    // Set up adapter to parse both HW and RF response from notification
    mockAdapter.parseSettingsResponse.mockReturnValue({
      detected: true,
      ledPoints: 90,
      segments: 3,
      icType: 6,
      icName: 'WS2812B',
      colorSorting: 0,
      colorSortingName: 'RGB',
    });
    mockAdapter.parseRfRemoteState.mockReturnValue({
      mode: 'single',
      pairedCount: 2,
    });

    const probePromise = interrogateDevice(
      MOCK_MAC,
      mockBleManager as unknown as BleManager,
      mockProbingMacsRef,
      mockHwCacheRef,
      mockOnDeviceInterrogated,
    );

    // Advance past HW query delay (400ms) to trigger the enqueueWrite
    jest.advanceTimersByTime(400);
    await Promise.resolve();

    // Fire the notification callback with a mock characteristic
    const mockChar = { value: Buffer.from([0x63, 0x12]).toString('base64') };
    expect(capturedNotificationCb).not.toBeNull();
    capturedNotificationCb!(null, mockChar);

    // Let the probe promise resolve
    await jest.runAllTimersAsync();
    const result = await probePromise;

    expect(result).not.toBeNull();
    expect(mockOnDeviceInterrogated).toHaveBeenCalledTimes(1);
    expect(mockHwCacheRef.current[MOCK_MAC]).toBeDefined();
  });

  it('2. cancelDeviceConnection called in finally block (success path)', async () => {
    mockAdapter.parseSettingsResponse.mockReturnValue({
      detected: true, ledPoints: 90, segments: 3, icType: 6, icName: 'WS2812B',
      colorSorting: 0, colorSortingName: 'RGB',
    });
    mockAdapter.parseRfRemoteState.mockReturnValue({ mode: 'single', pairedCount: 2 });

    const probePromise = interrogateDevice(
      MOCK_MAC,
      mockBleManager as unknown as BleManager,
      mockProbingMacsRef,
      mockHwCacheRef,
      mockOnDeviceInterrogated,
    );

    jest.advanceTimersByTime(400);
    await Promise.resolve();
    capturedNotificationCb!(null, { value: Buffer.from([0x63]).toString('base64') });

    await jest.runAllTimersAsync();
    await probePromise;

    // cancelDeviceConnection must be called in finally regardless of outcome
    expect(mockBleManager.cancelDeviceConnection).toHaveBeenCalledWith(MOCK_MAC);
  });

  it('3. MAC removed from probingMacsRef in finally block', async () => {
    const probePromise = interrogateDevice(
      MOCK_MAC,
      mockBleManager as unknown as BleManager,
      mockProbingMacsRef,
      mockHwCacheRef,
      mockOnDeviceInterrogated,
    );

    // MAC is added immediately when probe starts
    expect(mockProbingMacsRef.current.has(MOCK_MAC)).toBe(true);

    // Let probe timeout and finish
    await jest.runAllTimersAsync();
    await probePromise;

    // MAC must be removed in finally
    expect(mockProbingMacsRef.current.has(MOCK_MAC)).toBe(false);
  });

  // ─── Group B: interrogateDevice Error Paths ────────────────────────────────

  it('4. createGattSession throws → cancelDeviceConnection still called in finally', async () => {
    (createGattSession as jest.Mock).mockRejectedValue(new Error('GATT session failed'));

    const result = await interrogateDevice(
      MOCK_MAC,
      mockBleManager as unknown as BleManager,
      mockProbingMacsRef,
      mockHwCacheRef,
      mockOnDeviceInterrogated,
    );

    expect(result).toBeNull();
    expect(mockBleManager.cancelDeviceConnection).toHaveBeenCalledWith(MOCK_MAC);
    expect(mockOnDeviceInterrogated).not.toHaveBeenCalled();
  });

  it('5. enqueueWrite (0x63 write) throws → probe exits gracefully, cancelDeviceConnection called', async () => {
    (enqueueWrite as jest.Mock).mockRejectedValue(new Error('Write failed'));

    const probePromise = interrogateDevice(
      MOCK_MAC,
      mockBleManager as unknown as BleManager,
      mockProbingMacsRef,
      mockHwCacheRef,
      mockOnDeviceInterrogated,
    );

    // Advance past write delay and let timeout fire
    await jest.runAllTimersAsync();
    const result = await probePromise;

    // Write failure is non-fatal — probe completes with null (timeout)
    expect(result).toBeNull();
    expect(mockBleManager.cancelDeviceConnection).toHaveBeenCalledWith(MOCK_MAC);
  });

  it('6. Notification timeout (no response) → probe returns null, cancelDeviceConnection called', async () => {
    // Do not fire any notification — let PROBE_TIMEOUT_MS elapse
    const probePromise = interrogateDevice(
      MOCK_MAC,
      mockBleManager as unknown as BleManager,
      mockProbingMacsRef,
      mockHwCacheRef,
      mockOnDeviceInterrogated,
    );

    // Advance past PROBE_TIMEOUT_MS (3500ms)
    await jest.runAllTimersAsync();
    const result = await probePromise;

    expect(result).toBeNull();
    expect(mockBleManager.cancelDeviceConnection).toHaveBeenCalledWith(MOCK_MAC);
    expect(mockOnDeviceInterrogated).not.toHaveBeenCalled();
  });

  // ─── Group C: FTUE vs Standard Delay ──────────────────────────────────────

  it('7. getRegisteredMacsCount() returns 0 → queue delay is FTUE (500ms)', async () => {
    const { queueDeviceForInterrogation } = createProbeQueue({
      bleManager: mockBleManager as unknown as BleManager,
      probingMacsRef: mockProbingMacsRef,
      hwCacheRef: mockHwCacheRef,
      getRegisteredMacsCount: () => 0, // FTUE: no registered MACs
      onDeviceInterrogated: mockOnDeviceInterrogated,
    });

    queueDeviceForInterrogation(MOCK_MAC);

    // Not yet fired before FTUE delay
    jest.advanceTimersByTime(PROBE_QUEUE_DELAY_MS_FTUE - 1);
    expect(createGattSession).not.toHaveBeenCalled();

    // Fires at exactly FTUE delay
    await jest.runAllTimersAsync();
    expect(createGattSession).toHaveBeenCalled();
  });

  it('8. getRegisteredMacsCount() returns 3 → queue delay is standard (2000ms)', async () => {
    const { queueDeviceForInterrogation } = createProbeQueue({
      bleManager: mockBleManager as unknown as BleManager,
      probingMacsRef: mockProbingMacsRef,
      hwCacheRef: mockHwCacheRef,
      getRegisteredMacsCount: () => 3, // standard: has registered MACs
      onDeviceInterrogated: mockOnDeviceInterrogated,
    });

    queueDeviceForInterrogation(MOCK_MAC);

    // Not fired during FTUE window
    jest.advanceTimersByTime(PROBE_QUEUE_DELAY_MS_FTUE + 100);
    expect(createGattSession).not.toHaveBeenCalled();

    // Not fired before standard delay
    jest.advanceTimersByTime(PROBE_QUEUE_DELAY_MS - PROBE_QUEUE_DELAY_MS_FTUE - 200);
    expect(createGattSession).not.toHaveBeenCalled();

    // Fires at standard delay
    await jest.runAllTimersAsync();
    expect(createGattSession).toHaveBeenCalled();
  });

  // ─── Group D: loadHWCacheFromStorage ──────────────────────────────────────

  it('9. Valid JSON in AsyncStorage → parsed and returned correctly', async () => {
    const pingResult = makeMockPingResult();
    const mac = 'AA:BB:CC:DD:EE:FF';
    const key = `@sk8_hw_${mac}`;

    (AsyncStorage.getAllKeys as jest.Mock).mockResolvedValue([key]);
    (AsyncStorage.multiGet as jest.Mock).mockResolvedValue([[key, JSON.stringify(pingResult)]]);

    const result = await loadHWCacheFromStorage();

    expect(result[mac]).toEqual(pingResult);
  });

  it('10. Malformed JSON for one MAC → that entry skipped, other MACs loaded successfully', async () => {
    const pingResult = makeMockPingResult();
    const goodKey = '@sk8_hw_AA:BB:CC:DD:EE:FF';
    const badKey = '@sk8_hw_11:22:33:44:55:66';

    (AsyncStorage.getAllKeys as jest.Mock).mockResolvedValue([goodKey, badKey]);
    (AsyncStorage.multiGet as jest.Mock).mockResolvedValue([
      [goodKey, JSON.stringify(pingResult)],
      [badKey, 'NOT_VALID_JSON{{{'],
    ]);

    const result = await loadHWCacheFromStorage();

    // Good entry loaded, bad entry skipped (no crash)
    expect(result['AA:BB:CC:DD:EE:FF']).toEqual(pingResult);
    expect(result['11:22:33:44:55:66']).toBeUndefined();
  });

  it('11. AsyncStorage.getAllKeys throws → error caught, empty cache returned (no crash)', async () => {
    (AsyncStorage.getAllKeys as jest.Mock).mockRejectedValue(new Error('Storage unavailable'));

    const result = await loadHWCacheFromStorage();

    expect(result).toEqual({});
  });

  // ─── Group E: Duplicate Guard ──────────────────────────────────────────────

  it('12. MAC already in probingMacsRef → interrogateDevice returns null early, no GATT attempted', async () => {
    // Mark MAC as already being probed
    mockProbingMacsRef.current.add(MOCK_MAC);

    const result = await interrogateDevice(
      MOCK_MAC,
      mockBleManager as unknown as BleManager,
      mockProbingMacsRef,
      mockHwCacheRef,
      mockOnDeviceInterrogated,
    );

    expect(result).toBeNull();
    expect(createGattSession).not.toHaveBeenCalled();
    expect(mockBleManager.cancelDeviceConnection).not.toHaveBeenCalled();
  });
});

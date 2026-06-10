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

// enqueueWrite is the only BleWriteQueue export HeartbeatService uses.
// Path: from __tests__/ go up two levels to reach src/services/BleWriteQueue.ts
jest.mock('../../BleWriteQueue', () => ({
  enqueueWrite: jest.fn(async (_priority: any, op: () => Promise<any>) => op()),
}));

// Silence AppLogger to prevent console noise from overflowing verifiable-check-runner.js pipe
jest.mock('../../AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
    info: jest.fn(),
    flushQueues: jest.fn(),
  },
}));

import type { Device } from 'react-native-ble-plx';
import type { IControllerProtocol } from '../../../protocols/IControllerProtocol';
import { heartbeatService, type HeartbeatServiceInput } from '../HeartbeatService';
import { enqueueWrite } from '../../BleWriteQueue';

const HEARTBEAT_INTERVAL_MS = 45_000;

/**
 * Flush the microtask queue (Promise chains) without advancing the fake clock.
 *
 * CRITICAL: Do NOT use jest.runAllTimersAsync() for setInterval actors — it
 * repeatedly fires the interval until Jest's 100k-timer safeguard aborts the test.
 * Instead, flush only the async callbacks that the current clock position triggered.
 * 8 rounds covers all nested awaits: setInterval → enqueueWrite → writeCharacteristic.
 */
const flushAsyncQueue = async (rounds = 8) => {
  for (let i = 0; i < rounds; i++) {
    await Promise.resolve();
  }
};

describe('HeartbeatService test suite', () => {
  let mockBleManager: {
    writeCharacteristicWithoutResponseForDevice: jest.Mock;
    readRSSIForDevice: jest.Mock;
    cancelDeviceConnection: jest.Mock;
  };
  let mockDevice: Device;
  let mockAdapter: IControllerProtocol & { buildQuerySettings: jest.Mock };
  let mockInput: HeartbeatServiceInput;

  /**
   * Invoke the fromCallback actor logic directly with a mock sendBack.
   * XState v5 stores the raw callback in actorLogic.config.
   * Returns the cleanup function (clearInterval wrapper).
   */
  const callHeartbeatService = (input: HeartbeatServiceInput, mockSendBack: jest.Mock): () => void => {
    const fn = (heartbeatService as any).config as (params: {
      input: HeartbeatServiceInput;
      sendBack: (event: any) => void;
      receive: (listener: any) => void;
      self: any;
      system: any;
    }) => () => void;
    return fn({
      input,
      sendBack: mockSendBack,
      receive: jest.fn(),
      self: {} as any,
      system: {} as any,
    });
  };

  beforeEach(() => {
    jest.clearAllMocks();
    jest.useFakeTimers();

    mockBleManager = {
      writeCharacteristicWithoutResponseForDevice: jest.fn().mockResolvedValue(undefined),
      readRSSIForDevice: jest.fn().mockResolvedValue(-60),
      cancelDeviceConnection: jest.fn().mockResolvedValue(undefined),
    };

    mockAdapter = {
      serviceUUID: 'uuid-service',
      writeCharacteristicUUID: 'uuid-write',
      notifyCharacteristicUUID: 'uuid-notify',
      protocolId: 'zengge',
      buildQuerySettings: jest.fn().mockReturnValue({ packets: [[0x63, 0x12, 0x21, 0x0f, 0xa5]] }),
    } as unknown as IControllerProtocol & { buildQuerySettings: jest.Mock };

    mockDevice = {
      id: 'MAC1',
      name: 'Skate L',
    } as unknown as Device;

    mockInput = {
      bleManager: mockBleManager,
      connectedDevices: [mockDevice],
      adapterMap: new Map([['MAC1', mockAdapter]]),
    };
  });

  afterEach(() => {
    // Clear any pending fake intervals (e.g. the 45s HeartbeatService setInterval)
    // BEFORE restoring real timers to prevent AppLogger flush queue from leaking
    jest.clearAllTimers();
    jest.useRealTimers();
  });

  // ─── Group A: Tick Interval ────────────────────────────────────────────────

  it('1. setInterval registered with 45,000ms — interval fires at exactly 45s', async () => {
    const mockSendBack = jest.fn();
    callHeartbeatService(mockInput, mockSendBack);

    // No tick before 45s
    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS - 1);
    expect(enqueueWrite).not.toHaveBeenCalled();

    // Tick fires at exactly 45s — flush its async callback chain
    jest.advanceTimersByTime(1);
    await flushAsyncQueue();

    expect(enqueueWrite).toHaveBeenCalledTimes(1);
  });

  it('2. Heartbeat does NOT fire immediately — waits for first 45s interval', () => {
    const mockSendBack = jest.fn();
    callHeartbeatService(mockInput, mockSendBack);

    // No write immediately after starting
    expect(enqueueWrite).not.toHaveBeenCalled();
    expect(mockBleManager.readRSSIForDevice).not.toHaveBeenCalled();
    expect(mockSendBack).not.toHaveBeenCalled();
  });

  // ─── Group B: Zengge Device Happy Path ────────────────────────────────────

  it('3. Zengge adapter present → enqueueWrite called with "normal" priority', async () => {
    const mockSendBack = jest.fn();
    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    expect(enqueueWrite).toHaveBeenCalledTimes(1);
    expect(enqueueWrite).toHaveBeenCalledWith('normal', expect.any(Function));
  });

  it('4. Zengge adapter → 0x63 opcode bytes sent via writeCharacteristicWithoutResponseForDevice', async () => {
    const mockSendBack = jest.fn();
    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    expect(mockBleManager.writeCharacteristicWithoutResponseForDevice).toHaveBeenCalledWith(
      'MAC1',
      'uuid-service',
      'uuid-write',
      expect.any(String), // base64-encoded packet
    );

    // Verify the base64 encodes the correct 0x63 opcode bytes
    const callArgs = mockBleManager.writeCharacteristicWithoutResponseForDevice.mock.calls[0];
    const b64 = callArgs[3] as string;
    const decoded = Buffer.from(b64, 'base64');
    expect(decoded[0]).toBe(0x63); // First byte must be 0x63 opcode
  });

  // ─── Group C: Fallback (BanlanX / Unknown Adapter) ────────────────────────

  it('5. No adapter in adapterMap → readRSSIForDevice called directly (NOT via enqueueWrite)', async () => {
    const mockSendBack = jest.fn();
    // Remove adapter from map so device falls through to RSSI fallback
    mockInput.adapterMap = new Map();

    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    expect(enqueueWrite).not.toHaveBeenCalled();
    expect(mockBleManager.readRSSIForDevice).toHaveBeenCalledWith('MAC1');
  });

  // ─── Group D: HEARTBEAT_FAIL ───────────────────────────────────────────────

  it('6. enqueueWrite throws GATT error → sendBack HEARTBEAT_FAIL for that device', async () => {
    (enqueueWrite as jest.Mock).mockRejectedValueOnce(new Error('GATT write failed'));
    const mockSendBack = jest.fn();

    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    expect(mockSendBack).toHaveBeenCalledWith({
      type: 'HEARTBEAT_FAIL',
      deviceId: 'MAC1',
    });
  });

  it('7. cancelDeviceConnection called after GATT error — before sendBack', async () => {
    (enqueueWrite as jest.Mock).mockRejectedValueOnce(new Error('GATT write failed'));
    const callOrder: string[] = [];

    mockBleManager.cancelDeviceConnection.mockImplementation(async () => {
      callOrder.push('cancel');
    });
    const mockSendBack = jest.fn().mockImplementation(() => {
      callOrder.push('sendBack');
    });

    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    expect(callOrder).toEqual(['cancel', 'sendBack']);
    expect(mockBleManager.cancelDeviceConnection).toHaveBeenCalledWith('MAC1');
  });

  it('8. readRSSIForDevice throws → sendBack HEARTBEAT_FAIL for fallback device', async () => {
    mockInput.adapterMap = new Map(); // no adapter → RSSI fallback
    mockBleManager.readRSSIForDevice.mockRejectedValueOnce(new Error('RSSI read failed'));
    const mockSendBack = jest.fn();

    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    expect(mockSendBack).toHaveBeenCalledWith({
      type: 'HEARTBEAT_FAIL',
      deviceId: 'MAC1',
    });
  });

  // ─── Group E: Multi-device ─────────────────────────────────────────────────

  it('9. Two connected devices → heartbeat sent to both sequentially in same tick', async () => {
    const mockDevice2 = { id: 'MAC2', name: 'Skate R' } as unknown as Device;
    const mockAdapter2 = {
      ...mockAdapter,
      buildQuerySettings: jest.fn().mockReturnValue({ packets: [[0x63, 0x12]] }),
    } as unknown as IControllerProtocol & { buildQuerySettings: jest.Mock };

    mockInput.connectedDevices = [mockDevice, mockDevice2];
    mockInput.adapterMap = new Map([
      ['MAC1', mockAdapter],
      ['MAC2', mockAdapter2],
    ]);

    const mockSendBack = jest.fn();
    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    // enqueueWrite called once per device
    expect(enqueueWrite).toHaveBeenCalledTimes(2);
    expect(mockSendBack).not.toHaveBeenCalled(); // both succeeded
  });

  it('10. First device GATT fails → HEARTBEAT_FAIL for MAC1, second device still receives heartbeat', async () => {
    const mockDevice2 = { id: 'MAC2', name: 'Skate R' } as unknown as Device;
    const mockAdapter2 = {
      ...mockAdapter,
      buildQuerySettings: jest.fn().mockReturnValue({ packets: [[0x63, 0x12]] }),
    } as unknown as IControllerProtocol & { buildQuerySettings: jest.Mock };

    mockInput.connectedDevices = [mockDevice, mockDevice2];
    mockInput.adapterMap = new Map([
      ['MAC1', mockAdapter],
      ['MAC2', mockAdapter2],
    ]);

    // First device fails, second succeeds
    (enqueueWrite as jest.Mock)
      .mockRejectedValueOnce(new Error('MAC1 GATT fail'))
      .mockResolvedValueOnce(undefined);

    const mockSendBack = jest.fn();
    callHeartbeatService(mockInput, mockSendBack);

    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS);
    await flushAsyncQueue();

    // HEARTBEAT_FAIL only for MAC1
    expect(mockSendBack).toHaveBeenCalledTimes(1);
    expect(mockSendBack).toHaveBeenCalledWith({ type: 'HEARTBEAT_FAIL', deviceId: 'MAC1' });

    // Both devices attempted — enqueueWrite called twice
    expect(enqueueWrite).toHaveBeenCalledTimes(2);
  });

  // ─── Group F: Cleanup ──────────────────────────────────────────────────────

  it('11. Cleanup function calls clearInterval — timer does not fire after cleanup', () => {
    const mockSendBack = jest.fn();
    const cleanup = callHeartbeatService(mockInput, mockSendBack);

    // clearInterval is called — the setInterval is removed from fake timer registry
    cleanup();

    // Advance well past the interval — nothing should fire
    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS * 3);

    expect(enqueueWrite).not.toHaveBeenCalled();
    expect(mockBleManager.readRSSIForDevice).not.toHaveBeenCalled();
  });

  it('12. No sendBack called after cleanup — orphaned timer cannot fire HEARTBEAT_FAIL', async () => {
    // Setup failing write so that if timer fires, sendBack would be called
    (enqueueWrite as jest.Mock).mockRejectedValue(new Error('GATT fail'));
    const mockSendBack = jest.fn();

    const cleanup = callHeartbeatService(mockInput, mockSendBack);

    // Clean up immediately — clearInterval removes the timer
    cleanup();

    // Advance well past interval — interval is gone, nothing fires
    jest.advanceTimersByTime(HEARTBEAT_INTERVAL_MS * 2);
    await flushAsyncQueue();

    // Timer was cleared — sendBack must never fire
    expect(mockSendBack).not.toHaveBeenCalled();
  });
});

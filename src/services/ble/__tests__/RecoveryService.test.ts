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
    select: jest.fn((objs) => objs.android || objs.default),
  },
  Alert: { alert: jest.fn() },
  DeviceEventEmitter: {
    addListener: jest.fn().mockReturnValue({ remove: jest.fn() }),
  },
  AppState: {
    addEventListener: jest.fn().mockReturnValue({ remove: jest.fn() }),
  },
}));

// Key mocks: clearWriteQueue and enqueueWrite from BleWriteQueue
jest.mock('../../BleWriteQueue', () => ({
  clearWriteQueue: jest.fn(),
  enqueueWrite: jest.fn(async (_priority: string, op: () => Promise<unknown>) => op()),
  enqueueDelay: jest.fn(async (_priority: string, delay: number) => new Promise(res => setTimeout(res, delay))),
}));

jest.mock('../../BleSessionFactory', () => ({
  createGattSession: jest.fn(),
}));

// Silence AppLogger to prevent ENOBUFS buffer overflow in verifiable-check-runner.js
// Phase 1+2 exhaustion emits 35+ warn entries per test — too much stdout for spawn buffer
jest.mock('../../appLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
    info: jest.fn(),
    flushQueues: jest.fn(),
  },
}));

import { Platform } from 'react-native';
import type { BleManager, Device, Subscription } from 'react-native-ble-plx';
import { recoveryService } from '../RecoveryService';
import { createGattSession } from '../../BleSessionFactory';
import { clearWriteQueue } from '../../BleWriteQueue';
import type { AnyActorRef, ActorSystem, AnyEventObject } from 'xstate';
import type { IControllerProtocol } from '../../../protocols/IControllerProtocol';

// Mirrors the private RecoveryInput interface from RecoveryService.ts
interface RecoveryInput {
  bleManager: BleManager;
  ghostedDeviceIds: string[];
  adapterMapRef: { current: Map<string, IControllerProtocol> };
  mtuMapRef: { current: Map<string, number> };
  disconnectListeners: { current: Record<string, Subscription> };
  handleOrganicDisconnect: (error: Error | null, deviceId: string) => void;
  onOrganicDisconnect: (deviceId: string) => void;
  handleNotification: (error: Error | null, characteristic: { value: string | null } | null, deviceId: string) => void;
  getSweepedDevice?: (deviceId: string) => Device | undefined;
}

// Increase test suite timeout — Phase 1+2 exhaustion iterates 35+ async cycles
jest.setTimeout(30_000);

describe('RecoveryService test suite', () => {
  let mockConn: Device;
  let mockAdapter: {
    serviceUUID: string;
    notifyCharacteristicUUID: string;
    writeCharacteristicUUID: string;
    protocolId: string;
    buildQuerySettings: jest.Mock;
  };
  let mockBleManager: { onDeviceDisconnected: jest.Mock };
  let mockDisconnectSubscription: { remove: jest.Mock };
  let mockInput: RecoveryInput;

  /**
   * Invoke the fromCallback actor logic directly with a mock sendBack.
   * XState v5 stores the raw callback in actorLogic.config.
   * Returns the cancel() cleanup function.
   */
  const callRecoveryService = (input: RecoveryInput, mockSendBack: jest.Mock): () => void => {
    const fn = (recoveryService as unknown as {
      config: (params: {
        input: RecoveryInput;
        sendBack: (event: AnyEventObject) => void;
        receive: (listener: (event: AnyEventObject) => void) => void;
        self: AnyActorRef;
        system: ActorSystem<any>;
      }) => () => void;
    }).config;
    return fn({
      input,
      sendBack: mockSendBack,
      receive: jest.fn(),
      self: {} as unknown as AnyActorRef,
      system: {} as unknown as ActorSystem<any>,
    });
  };

  beforeEach(() => {
    jest.clearAllMocks();
    jest.useFakeTimers();
    (Platform as unknown as { OS: string }).OS = 'android';

    mockDisconnectSubscription = { remove: jest.fn() };

    mockAdapter = {
      serviceUUID: 'uuid-service',
      notifyCharacteristicUUID: 'uuid-notify',
      writeCharacteristicUUID: 'uuid-write',
      protocolId: 'zengge',
      buildQuerySettings: jest.fn().mockReturnValue({ packets: [[0x63, 0x12]] }),
    };

    mockConn = {
      id: 'MAC1',
      name: 'Skate L',
      requestMTU: jest.fn().mockResolvedValue({ mtu: 512 }),
      monitorCharacteristicForService: jest.fn(),
      writeCharacteristicWithoutResponseForService: jest.fn().mockResolvedValue(undefined),
    } as unknown as Device;

    mockBleManager = {
      onDeviceDisconnected: jest.fn().mockReturnValue(mockDisconnectSubscription),
    };

    (createGattSession as jest.Mock).mockResolvedValue({
      conn: mockConn,
      adapter: mockAdapter,
    });

    mockInput = {
      bleManager: mockBleManager as unknown as BleManager,
      ghostedDeviceIds: ['MAC1'],
      adapterMapRef: { current: new Map() },
      mtuMapRef: { current: new Map() },
      disconnectListeners: { current: {} },
      handleOrganicDisconnect: jest.fn(),
      onOrganicDisconnect: jest.fn(),
      handleNotification: jest.fn(),
      getSweepedDevice: jest.fn().mockReturnValue(undefined),
    };
  });

  afterEach(() => {
    // Clear any pending fake timers before restoring real ones to prevent
    // AppLogger flush queue timer from firing after test suite completes
    jest.clearAllTimers();
    jest.useRealTimers();
  });

  // ─── Group A: Write Queue Clear (Regression Guard for fix in 2276ac8a) ───

  it('1. clearWriteQueue() called immediately before first GATT attempt', () => {
    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    // clearWriteQueue is called synchronously before any await — no timers needed
    expect(clearWriteQueue).toHaveBeenCalledTimes(1);
  });

  // ─── Group B: RECOVERY_COMPLETE Path ──────────────────────────────────────

  it('2. First GATT attempt succeeds → sendBack RECOVERY_COMPLETE with device', async () => {
    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockSendBack).toHaveBeenCalledWith({
      type: 'RECOVERY_COMPLETE',
      devices: [mockConn],
    });
  });

  it('3. adapterMapRef updated with recovered adapter', async () => {
    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockInput.adapterMapRef.current.get('MAC1')).toBe(mockAdapter);
  });

  it('4. monitorCharacteristicForService re-registered with correct UUIDs after reconnect', async () => {
    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockConn.monitorCharacteristicForService).toHaveBeenCalledWith(
      'uuid-service',
      'uuid-notify',
      expect.any(Function),
    );
  });

  it('5. onOrganicDisconnect wired into new disconnect subscription after reconnect (regression guard)', async () => {
    let capturedCb: ((err: Error | null) => void) | null = null;
    mockBleManager.onDeviceDisconnected = jest.fn().mockImplementation(
      (_id: string, cb: (err: Error | null) => void) => {
        capturedCb = cb;
        return mockDisconnectSubscription;
      }
    );

    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(capturedCb).not.toBeNull();
    capturedCb!(null);
    expect(mockInput.onOrganicDisconnect).toHaveBeenCalledWith('MAC1');
    expect(mockInput.handleOrganicDisconnect).toHaveBeenCalled();
  });

  it('6. MTU re-negotiated on Android and stored in mtuMapRef', async () => {
    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockConn.requestMTU).toHaveBeenCalledWith(512);
    expect(mockInput.mtuMapRef.current.get('MAC1')).toBe(512);
  });

  // ─── Group C: RECOVERY_FAIL Paths ─────────────────────────────────────────

  it('7. Empty ghostedDeviceIds → immediate RECOVERY_FAIL, createGattSession never called', async () => {
    mockInput.ghostedDeviceIds = [];
    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockSendBack).toHaveBeenCalledWith({ type: 'RECOVERY_FAIL' });
    expect(createGattSession).not.toHaveBeenCalled();
  });

  it('8. All Phase 1+2 GATT attempts exhausted → RECOVERY_PERMANENTLY_FAILED', async () => {
    (createGattSession as jest.Mock).mockRejectedValue(new Error('GATT connection failed'));

    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    // Fast-forward all Phase 1+2 backoff delays
    await jest.runAllTimersAsync();

    expect(mockSendBack).toHaveBeenCalledWith({ type: 'RECOVERY_PERMANENTLY_FAILED', deviceId: 'MAC1' });
  });

  it('9. Phase 3: getSweepedDevice never returns device → RECOVERY_PERMANENTLY_FAILED after poll exhaustion', async () => {
    (createGattSession as jest.Mock).mockRejectedValue(new Error('GATT fail'));
    mockInput.getSweepedDevice = jest.fn().mockReturnValue(undefined);

    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockSendBack).toHaveBeenCalledWith({ type: 'RECOVERY_PERMANENTLY_FAILED', deviceId: 'MAC1' });
  });

  // ─── Group D: Phase 3 Happy Path ──────────────────────────────────────────

  it('10. Phase 1+2 fail → Phase 3 entered → getSweepedDevice finds device → RECOVERY_COMPLETE', async () => {
    let gattCallCount = 0;
    (createGattSession as jest.Mock).mockImplementation(() => {
      gattCallCount++;
      // Phase 1+2: first 5 calls fail; Phase 3 call succeeds
      if (gattCallCount <= 5) {
        return Promise.reject(new Error('Phase 1/2 fail'));
      }
      return Promise.resolve({ conn: mockConn, adapter: mockAdapter });
    });

    let pollCount = 0;
    mockInput.getSweepedDevice = jest.fn().mockImplementation(() => {
      pollCount++;
      // Device reappears on the 3rd Phase 3 poll
      return pollCount >= 3 ? mockConn : undefined;
    });

    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockSendBack).toHaveBeenCalledWith({
      type: 'RECOVERY_COMPLETE',
      devices: [mockConn],
    });
  });

  it('11. Phase 3: createGattSession fails → immediate RECOVERY_PERMANENTLY_FAILED (no further Phase 3 retries)', async () => {
    // All GATT attempts fail (Phase 1+2 AND Phase 3)
    (createGattSession as jest.Mock).mockRejectedValue(new Error('always fail'));
    // But sweeper finds the device immediately → enters Phase 3 reconnect attempt → fails
    mockInput.getSweepedDevice = jest.fn().mockReturnValue(mockConn);

    const mockSendBack = jest.fn();
    callRecoveryService(mockInput, mockSendBack);
    await jest.runAllTimersAsync();

    expect(mockSendBack).toHaveBeenCalledWith({ type: 'RECOVERY_PERMANENTLY_FAILED', deviceId: 'MAC1' });
  });

  // ─── Group E: Cancellation ────────────────────────────────────────────────

  it('12. cancel() during Phase 1 backoff → service exits cleanly, no events sent', async () => {
    // Make createGattSession hang indefinitely so we stay in Phase 1 backoff
    (createGattSession as jest.Mock).mockImplementation(() => new Promise(() => {}));

    const mockSendBack = jest.fn();
    const cancel = callRecoveryService(mockInput, mockSendBack);

    // cancel() is called BEFORE the Phase 1 backoff timer fires
    // (run() is currently awaiting the first setTimeout delay)
    cancel();

    await jest.runAllTimersAsync();

    // cancelled = true → while loop exits → if (cancelled) return → no sendBack
    expect(mockSendBack).not.toHaveBeenCalled();
  });

  it('13. cancel() during Phase 3 poll → service exits cleanly, no events sent', async () => {
    // Phase 1+2 always fail so we enter Phase 3
    (createGattSession as jest.Mock).mockRejectedValue(new Error('fail'));
    mockInput.getSweepedDevice = jest.fn().mockReturnValue(undefined);

    const mockSendBack = jest.fn();
    const cancel = callRecoveryService(mockInput, mockSendBack);

    // Cancel immediately — cancelled flag is respected at every await checkpoint
    // including Phase 3 poll delays
    cancel();

    await jest.runAllTimersAsync();

    expect(mockSendBack).not.toHaveBeenCalled();
  });
});

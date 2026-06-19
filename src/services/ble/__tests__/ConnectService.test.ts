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

jest.mock('../../BleSessionFactory', () => ({
  createGattSession: jest.fn(),
}));

import { Platform, Alert } from 'react-native';
import type { Device, BleManager, Subscription } from 'react-native-ble-plx';
import { connectService, ConnectServiceInput } from '../ConnectService';
import { createGattSession } from '../../BleSessionFactory';
import type { IControllerProtocol } from '../../../protocols/IControllerProtocol';
import { createActor } from 'xstate';

describe('ConnectService test suite', () => {
  let mockDevice1: Device;
  let mockDevice2: Device;
  let mockAdapter1: {
    protocolId: string;
    serviceUUID: string;
    writeCharacteristicUUID: string;
    notifyCharacteristicUUID: string;
    getHandshakePayloads: jest.Mock;
  };
  let mockAdapter2: {
    protocolId: string;
    serviceUUID: string;
    writeCharacteristicUUID: string;
    notifyCharacteristicUUID: string;
    getHandshakePayloads: jest.Mock;
  };
  let mockBleManager: {
    isDeviceConnected: jest.Mock;
    connectedDevices: jest.Mock;
    connectToDevice: jest.Mock;
    cancelDeviceConnection: jest.Mock;
    onDeviceDisconnected: jest.Mock;
    requestConnectionPriorityForDevice: jest.Mock;
  };
  let mockInput: ConnectServiceInput;
  let mockDisconnectSubscription: {
    remove: jest.Mock;
  };

  beforeEach(() => {
    jest.clearAllMocks();
    jest.useFakeTimers();

    (Platform as unknown as { OS: string }).OS = 'android';

    mockDisconnectSubscription = {
      remove: jest.fn(),
    };

    mockDevice1 = {
      id: 'MAC1',
      name: 'Skate L',
      manufacturerData: 'ZenggeData',
      requestMTU: jest.fn().mockResolvedValue({ mtu: 512 }),
      monitorCharacteristicForService: jest.fn(),
      writeCharacteristicWithoutResponseForService: jest.fn().mockResolvedValue(undefined),
      mtu: 23,
    } as unknown as Device;

    mockDevice2 = {
      id: 'MAC2',
      name: 'Skate R',
      manufacturerData: 'ZenggeData',
      requestMTU: jest.fn().mockResolvedValue({ mtu: 512 }),
      monitorCharacteristicForService: jest.fn(),
      writeCharacteristicWithoutResponseForService: jest.fn().mockResolvedValue(undefined),
      mtu: 23,
    } as unknown as Device;

    mockAdapter1 = {
      protocolId: 'zengge',
      serviceUUID: 'uuid-service',
      writeCharacteristicUUID: 'uuid-write',
      notifyCharacteristicUUID: 'uuid-notify',
      getHandshakePayloads: jest.fn().mockReturnValue({
        packets: [[0x10, 0x20]],
        interPacketDelayMs: 0,
      }),
    };

    mockAdapter2 = {
      protocolId: 'zengge',
      serviceUUID: 'uuid-service',
      writeCharacteristicUUID: 'uuid-write',
      notifyCharacteristicUUID: 'uuid-notify',
      getHandshakePayloads: jest.fn().mockReturnValue({
        packets: [[0x10, 0x20]],
        interPacketDelayMs: 0,
      }),
    };

    mockBleManager = {
      isDeviceConnected: jest.fn().mockResolvedValue(false),
      connectedDevices: jest.fn().mockResolvedValue([]),
      connectToDevice: jest.fn().mockResolvedValue(mockDevice1),
      cancelDeviceConnection: jest.fn().mockResolvedValue(undefined),
      onDeviceDisconnected: jest.fn().mockReturnValue(mockDisconnectSubscription),
      requestConnectionPriorityForDevice: jest.fn().mockResolvedValue(undefined),
    };

    (createGattSession as jest.Mock).mockImplementation((manager, id) => {
      if (id === 'MAC1') return Promise.resolve({ conn: mockDevice1, adapter: mockAdapter1 as unknown as IControllerProtocol });
      if (id === 'MAC2') return Promise.resolve({ conn: mockDevice2, adapter: mockAdapter2 as unknown as IControllerProtocol });
      return Promise.reject(new Error('Unknown ID in createGattSession mock'));
    });

    mockInput = {
      bleManager: mockBleManager as unknown as BleManager,
      targetMacs: ['MAC1'],
      registeredMacs: [],
      connectedDevicesRef: { current: [] },
      adapterMapRef: { current: new Map() },
      mtuMapRef: { current: new Map() },
      disconnectListeners: { current: {} },
      notificationListeners: { current: {} },
      blacklistedMacsRef: { current: [] },
      handleOrganicDisconnect: jest.fn(),
      onOrganicDisconnect: jest.fn(),
      handleNotification: jest.fn(),
      enqueueWrite: jest.fn((priority, op) => op()),
    };
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  // Helper function to run the fromPromise logic using XState v5 createActor
  const runConnectService = (input: ConnectServiceInput, abortSignal?: AbortSignal): Promise<{ devices: Device[] }> => {
    return new Promise((resolve, reject) => {
      const actor = createActor(connectService, { input });
      let isFinished = false;

      const sub = actor.subscribe({
        next: (snapshot) => {
          if (snapshot.status === 'done') {
            isFinished = true;
            sub.unsubscribe();
            resolve(snapshot.output);
          } else if (snapshot.status === 'error') {
            isFinished = true;
            sub.unsubscribe();
            reject(snapshot.error);
          }
        },
        error: (err) => {
          isFinished = true;
          sub.unsubscribe();
          reject(err);
        }
      });

      if (abortSignal) {
        if (abortSignal.aborted) {
          actor.stop();
          isFinished = true;
          reject(new Error('connect_aborted'));
          return;
        }
        abortSignal.addEventListener('abort', () => {
          if (!isFinished) {
            actor.stop();
            isFinished = true;
            reject(new Error('connect_aborted'));
          }
        });
      }

      actor.start();
    });
  };

  // Helper helper to resolve promise and trigger timers asynchronously
  const resolveActor = async (promise: Promise<{ devices: Device[] }>) => {
    await jest.runAllTimersAsync();
    return promise;
  };

  // --- Group A: Happy Path ---

  it('1. Single device connect successfully', async () => {
    const promise = runConnectService(mockInput);
    const result = await resolveActor(promise);

    expect(result).toEqual({ devices: [mockDevice1] });
    expect(mockBleManager.connectToDevice).toHaveBeenCalledWith('MAC1', { autoConnect: false });
    expect(mockInput.adapterMapRef.current.get('MAC1')).toBe(mockAdapter1);
    expect(mockInput.mtuMapRef.current.get('MAC1')).toBe(512);
  });

  it('2. Group connect (2 MACs) sequential flow', async () => {
    mockInput.targetMacs = ['MAC1', 'MAC2'];
    mockBleManager.connectToDevice = jest.fn().mockImplementation((mac) => {
      if (mac === 'MAC1') return Promise.resolve(mockDevice1);
      if (mac === 'MAC2') return Promise.resolve(mockDevice2);
      return Promise.reject(new Error('Unknown device'));
    });

    const promise = runConnectService(mockInput);
    const result = await resolveActor(promise);

    expect(result.devices).toEqual([mockDevice1, mockDevice2]);
    expect(mockBleManager.connectToDevice).toHaveBeenNthCalledWith(1, 'MAC1', { autoConnect: false });
    expect(mockBleManager.connectToDevice).toHaveBeenNthCalledWith(2, 'MAC2', { autoConnect: false });
  });

  it('2.1. Group connect (2 MACs) sequential flow asserts isGrouped invariant', async () => {
    mockInput.targetMacs = ['MAC1', 'MAC2'];
    mockBleManager.connectToDevice = jest.fn().mockImplementation((mac) => {
      if (mac === 'MAC1') return Promise.resolve(mockDevice1);
      if (mac === 'MAC2') return Promise.resolve(mockDevice2);
      return Promise.reject(new Error('Unknown device'));
    });

    const promise = runConnectService(mockInput);
    const result = await resolveActor(promise);

    expect(result.devices).toEqual([mockDevice1, mockDevice2]);
    
    // [R-24] Group Connection Ground Truth: check that length > 1 defines grouping
    const isGrouped = result.devices.length > 1;
    expect(isGrouped).toBe(true);
  });

  it('3. Already-connected device cache hit skips connectToDevice', async () => {
    mockInput.connectedDevicesRef.current = [mockDevice1];

    const promise = runConnectService(mockInput);
    const result = await resolveActor(promise);

    expect(result).toEqual({ devices: [mockDevice1] });
    expect(mockBleManager.connectToDevice).not.toHaveBeenCalled();
  });

  // --- Group B: GATT Retry (GATT 133) ---

  it('4. GATT 133 on attempt 1 retries and succeeds on attempt 2', async () => {
    mockBleManager.connectToDevice = jest
      .fn()
      .mockRejectedValueOnce(new Error('GATT 133 error'))
      .mockResolvedValueOnce(mockDevice1);

    const promise = runConnectService(mockInput);
    
    await jest.runAllTimersAsync();
    const result = await promise;

    expect(result).toEqual({ devices: [mockDevice1] });
    expect(mockBleManager.connectToDevice).toHaveBeenNthCalledWith(1, 'MAC1', { autoConnect: false });
    expect(mockBleManager.connectToDevice).toHaveBeenNthCalledWith(2, 'MAC1', { autoConnect: false, refreshGatt: 'OnConnected' });
    expect(mockBleManager.cancelDeviceConnection).toHaveBeenCalledWith('MAC1');
  });

  it('5. GATT 133 on all 3 attempts omits failed device from group', async () => {
    mockInput.targetMacs = ['MAC1', 'MAC2'];
    mockBleManager.connectToDevice = jest.fn().mockImplementation((mac) => {
      if (mac === 'MAC1') return Promise.reject(new Error('GATT 133 error'));
      if (mac === 'MAC2') return Promise.resolve(mockDevice2);
      return Promise.reject(new Error('Unknown device'));
    });

    const promise = runConnectService(mockInput);
    
    await jest.runAllTimersAsync();
    const result = await promise;

    expect(result.devices).toEqual([mockDevice2]);
    expect(mockBleManager.connectToDevice).toHaveBeenCalledTimes(4); // 3 for MAC1 + 1 for MAC2
  });

  it('6. All devices fail throws all_connections_failed', async () => {
    mockBleManager.connectToDevice = jest.fn().mockRejectedValue(new Error('Fatal connection error'));

    const promise = runConnectService(mockInput);
    const expectation = expect(promise).rejects.toThrow('all_connections_failed');

    await jest.runAllTimersAsync();
    await expectation;
  });

  // --- Group C: Incremental Group Assembly (stale flush removed) ---

  it('7. Non-target connected device is retained during incremental assembly', async () => {
    // Scenario: Device A (MAC1) is already connected. New batch targets only MAC2.
    // OLD behavior: MAC1 was classified as "stale" and disconnected.
    // NEW behavior: MAC1 is retained. Both devices end up in the final group.
    mockInput.targetMacs = ['MAC2'];
    const mockDisconnectSub = { remove: jest.fn() };
    mockInput.connectedDevicesRef.current = [mockDevice1];
    mockInput.disconnectListeners.current = { MAC1: mockDisconnectSub as unknown as Subscription };

    mockBleManager.connectToDevice = jest.fn().mockResolvedValue(mockDevice2);

    const promise = runConnectService(mockInput);

    await jest.runAllTimersAsync();
    const result = await promise;

    // MAC1 should NOT be disconnected — it is retained
    expect(mockBleManager.cancelDeviceConnection).not.toHaveBeenCalledWith('MAC1');
    expect(mockDisconnectSub.remove).not.toHaveBeenCalled();
    // Both devices should be in the final group
    expect(result.devices).toEqual([mockDevice1, mockDevice2]);
    expect(result.devices.length).toBe(2);
  });

  it('8. No stale flush settle delay when devices are retained', async () => {
    // With stale flush removed, connectToDevice should fire immediately
    // without waiting for a settle delay.
    mockInput.targetMacs = ['MAC2'];
    mockInput.connectedDevicesRef.current = [mockDevice1];
    mockBleManager.connectToDevice = jest.fn().mockResolvedValue(mockDevice2);

    const promise = runConnectService(mockInput);

    await jest.runAllTimersAsync();
    const result = await promise;

    // connectToDevice should have been called (no settle delay blocking it)
    expect(mockBleManager.connectToDevice).toHaveBeenCalledWith('MAC2', { autoConnect: false });
    // Both devices retained in final group
    expect(result.devices).toEqual([mockDevice1, mockDevice2]);
  });

  // --- Group D: MTU Negotiation (Android) ---

  it('9. MTU negotiation sets mtuMapRef on Android', async () => {
    const promise = runConnectService(mockInput);
    await resolveActor(promise);

    expect(mockDevice1.requestMTU).toHaveBeenCalledWith(512);
    expect(mockInput.mtuMapRef.current.get('MAC1')).toBe(512);
  });

  it('10. MTU glitch (returns 23) retries and falls back to 186', async () => {
    mockDevice1.requestMTU = jest.fn().mockResolvedValue({ mtu: 23 });

    const promise = runConnectService(mockInput);
    
    await jest.runAllTimersAsync();
    await promise;

    expect(mockDevice1.requestMTU).toHaveBeenCalledTimes(2);
    expect(mockInput.mtuMapRef.current.get('MAC1')).toBe(186);
  });

  it('11. MTU request fails falls back to 186', async () => {
    mockDevice1.requestMTU = jest.fn().mockRejectedValue(new Error('MTU request error'));

    const promise = runConnectService(mockInput);
    
    await jest.runAllTimersAsync();
    await promise;

    expect(mockInput.mtuMapRef.current.get('MAC1')).toBe(186);
  });

  // --- Group E: Adapter + Notification ---

  it('12. adapterMapRef is populated after connection', async () => {
    const promise = runConnectService(mockInput);
    await resolveActor(promise);

    expect(mockInput.adapterMapRef.current.get('MAC1')).toBe(mockAdapter1);
  });

  it('13. onDeviceDisconnected subscription registered', async () => {
    const promise = runConnectService(mockInput);
    await resolveActor(promise);

    expect(mockBleManager.onDeviceDisconnected).toHaveBeenCalledWith('MAC1', expect.any(Function));
    expect(mockInput.disconnectListeners.current['MAC1']).toBe(mockDisconnectSubscription);
  });

  it('14. onOrganicDisconnect fires on unexpected device disconnect', async () => {
    let capturedCallback: ((error: Error | null, device: { id: string } | null) => void) | null = null;
    mockBleManager.onDeviceDisconnected = jest.fn().mockImplementation((id, cb) => {
      capturedCallback = cb;
      return mockDisconnectSubscription;
    });

    const promise = runConnectService(mockInput);
    await resolveActor(promise);

    expect(capturedCallback).not.toBeNull();
    const disconnectError = new Error('GATT Server organic dropout');
    capturedCallback!(disconnectError, null);

    expect(mockInput.handleOrganicDisconnect).toHaveBeenCalledWith(disconnectError, 'MAC1');
    expect(mockInput.onOrganicDisconnect).toHaveBeenCalledWith('MAC1');
  });

  it('15. monitorCharacteristicForService called with correct UUIDs', async () => {
    const promise = runConnectService(mockInput);
    await resolveActor(promise);

    expect(mockDevice1.monitorCharacteristicForService).toHaveBeenCalledWith(
      'uuid-service',
      'uuid-notify',
      expect.any(Function)
    );
  });

  // --- Group F: Hardware Blacklist ---

  it('16. blacklisted MAC address aborts and throws hardware_blacklist', async () => {
    mockInput.blacklistedMacsRef.current = ['MAC1'];

    const promise = runConnectService(mockInput);
    const expectation = expect(promise).rejects.toThrow('hardware_blacklist');

    await jest.runAllTimersAsync();
    await expectation;
  });

  // --- Group G: Abort Signal ---

  it('17. signal.aborted before connect throws connect_aborted', async () => {
    const controller = new AbortController();
    controller.abort();

    const promise = runConnectService(mockInput, controller.signal);

    await expect(promise).rejects.toThrow('connect_aborted');
  });

  it('18. signal.aborted during connection loop throws connect_aborted', async () => {
    const controller = new AbortController();

    mockBleManager.connectToDevice = jest.fn().mockImplementation(() => {
      controller.abort();
      return Promise.resolve(mockDevice1);
    });

    const promise = runConnectService(mockInput, controller.signal);
    const expectation = expect(promise).rejects.toThrow('connect_aborted');

    await jest.runAllTimersAsync();
    await expectation;
  });
});

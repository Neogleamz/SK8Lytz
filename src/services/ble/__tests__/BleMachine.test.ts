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

import { createActor, fromPromise, fromCallback } from 'xstate';
import { bleMachine } from '../BleMachine';
import type { Device } from 'react-native-ble-plx';

const mockBleManager = {
  startDeviceScan: jest.fn(),
  stopDeviceScan: jest.fn(),
  cancelDeviceConnection: jest.fn().mockResolvedValue(undefined),
};

describe('BleMachine test suite', () => {
  let mockConnectResolve: (val: { devices: Device[] }) => void;
  let mockConnectReject: (err: any) => void;
  let mockConnectPromise: Promise<{ devices: Device[] }>;

  const testBleMachine = bleMachine.provide({
    actors: {
      connectService: fromPromise(() => {
        mockConnectPromise = new Promise((resolve, reject) => {
          mockConnectResolve = resolve;
          mockConnectReject = reject;
        });
        return mockConnectPromise;
      }),
      recoveryService: fromCallback(() => {}),
      heartbeatService: fromCallback(() => {}),
    },
  });

  let mockInput: any;

  beforeEach(() => {
    jest.clearAllMocks();

    mockInput = {
      bleManager: mockBleManager,
      scanCallback: jest.fn(),
      scanMode: 1,
      scanServiceUUIDs: ['uuid1'],
      adapterMapRef: { current: new Map() },
      mtuMapRef: { current: new Map() },
      disconnectListeners: { current: {} },
      blacklistedMacsRef: { current: [] },
      handleOrganicDisconnect: jest.fn(),
      onOrganicDisconnect: jest.fn(),
      handleNotification: jest.fn(),
      enqueueWrite: jest.fn(),
    };
  });

  // Helper helper to transition actor to READY state
  const walkToReady = async (actor: ReturnType<typeof createActor>) => {
    actor.start();
    actor.send({ type: 'CONNECT_REQUEST', targetMacs: ['MAC1'] });
    const mockDevice = { id: 'MAC1', name: 'Mock Skates' } as unknown as Device;
    mockConnectResolve({ devices: [mockDevice] });
    await mockConnectPromise;
    // Wait for microtasks
    await new Promise((res) => setTimeout(res, 0));
  };

  // --- Group A: State Transitions ---

  it('1. IDLE -> SCANNING on SCAN_START', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    expect(actor.getSnapshot().value).toBe('IDLE');

    actor.send({ type: 'SCAN_START', sweeperId: 42 });
    expect(actor.getSnapshot().value).toBe('SCANNING');
    expect(actor.getSnapshot().context.sweeperId).toBe(42);
    expect(mockBleManager.startDeviceScan).toHaveBeenCalled();
  });

  it('2. IDLE -> CONNECTING on CONNECT_REQUEST', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'CONNECT_REQUEST', targetMacs: ['MAC1'] });
    expect(actor.getSnapshot().value).toBe('CONNECTING');
    expect(actor.getSnapshot().context.targetMacs).toEqual(['MAC1']);
  });

  it('3. IDLE -> RECOVERING on RECOVERY_START', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'RECOVERY_START', ghostedMacs: ['MAC1'] });
    expect(actor.getSnapshot().value).toBe('RECOVERING');
    expect(actor.getSnapshot().context.ghostedDeviceIds).toEqual(['MAC1']);
  });

  it('4. SCANNING -> IDLE on SCAN_STOP', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'SCAN_START', sweeperId: 10 });
    expect(actor.getSnapshot().value).toBe('SCANNING');

    actor.send({ type: 'SCAN_STOP' });
    expect(actor.getSnapshot().value).toBe('IDLE');
    expect(actor.getSnapshot().context.sweeperId).toBeUndefined();
    expect(mockBleManager.stopDeviceScan).toHaveBeenCalled();
  });

  it('5. SCANNING -> CONNECTING on CONNECT_REQUEST', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'SCAN_START', sweeperId: 10 });
    expect(actor.getSnapshot().value).toBe('SCANNING');

    actor.send({ type: 'CONNECT_REQUEST', targetMacs: ['MAC1'] });
    expect(actor.getSnapshot().value).toBe('CONNECTING');
    expect(actor.getSnapshot().context.sweeperId).toBeUndefined();
    expect(actor.getSnapshot().context.targetMacs).toEqual(['MAC1']);
  });

  it('6. CONNECTING -> READY on connectService success (xstate.done.actor)', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'CONNECT_REQUEST', targetMacs: ['MAC1'] });
    expect(actor.getSnapshot().value).toBe('CONNECTING');

    const mockDevice = { id: 'MAC1', name: 'Mock Skates' } as unknown as Device;
    mockConnectResolve({ devices: [mockDevice] });
    await mockConnectPromise;
    await new Promise((res) => setTimeout(res, 0));

    expect(actor.getSnapshot().value).toBe('READY');
    expect(actor.getSnapshot().context.connectedDevices).toEqual([mockDevice]);
  });

  it('7. CONNECTING -> IDLE on connectService error', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'CONNECT_REQUEST', targetMacs: ['MAC1'] });
    expect(actor.getSnapshot().value).toBe('CONNECTING');

    mockConnectReject(new Error('Connection Failed'));
    try {
      await mockConnectPromise;
    } catch (e) {}
    await new Promise((res) => setTimeout(res, 0));

    expect(actor.getSnapshot().value).toBe('IDLE');
  });

  it('8. READY -> RECOVERING on HEARTBEAT_FAIL', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    await walkToReady(actor);
    expect(actor.getSnapshot().value).toBe('READY');

    actor.send({ type: 'HEARTBEAT_FAIL', deviceId: 'MAC1' });
    expect(actor.getSnapshot().value).toBe('RECOVERING');
    expect(actor.getSnapshot().context.ghostedDeviceIds).toEqual(['MAC1']);
  });

  it('9. READY -> DISCONNECTING on DISCONNECT_REQUEST', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    await walkToReady(actor);
    expect(actor.getSnapshot().value).toBe('READY');

    actor.send({ type: 'DISCONNECT_REQUEST' });
    expect(actor.getSnapshot().value).toBe('DISCONNECTING');
  });

  it('10. RECOVERING -> READY on RECOVERY_COMPLETE', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'RECOVERY_START', ghostedMacs: ['MAC1'] });
    expect(actor.getSnapshot().value).toBe('RECOVERING');

    const mockDevice = { id: 'MAC1', name: 'Recovered Skates' } as unknown as Device;
    actor.send({ type: 'RECOVERY_COMPLETE', devices: [mockDevice] });
    expect(actor.getSnapshot().value).toBe('READY');
    expect(actor.getSnapshot().context.connectedDevices).toEqual([mockDevice]);
  });

  it('11. RECOVERING -> IDLE on RECOVERY_FAIL', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'RECOVERY_START', ghostedMacs: ['MAC1'] });
    expect(actor.getSnapshot().value).toBe('RECOVERING');

    actor.send({ type: 'RECOVERY_FAIL' });
    expect(actor.getSnapshot().value).toBe('IDLE');
    expect(actor.getSnapshot().context.ghostedDeviceIds).toEqual([]);
  });

  it('12. DISCONNECTING -> IDLE on DISCONNECT_COMPLETE', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'DISCONNECT_REQUEST' });
    expect(actor.getSnapshot().value).toBe('DISCONNECTING');

    actor.send({ type: 'DISCONNECT_COMPLETE' });
    expect(actor.getSnapshot().value).toBe('IDLE');
    expect(actor.getSnapshot().context.connectedDevices).toEqual([]);
  });

  it('13. ANY -> IDLE on FORCE_IDLE (does not clear connectedDevices)', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    await walkToReady(actor);
    expect(actor.getSnapshot().value).toBe('READY');
    expect(actor.getSnapshot().context.connectedDevices.length).toBe(1);

    actor.send({ type: 'FORCE_IDLE' });
    expect(actor.getSnapshot().value).toBe('IDLE');
    expect(actor.getSnapshot().context.connectedDevices.length).toBe(1);
  });

  // --- Group B: Organic Disconnect Regression Guard (P0) ---

  it('14. onOrganicDisconnect triggers RECOVERY_START and transitions to RECOVERING', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    
    // Simulate useBLE.ts wiring for onOrganicDisconnect
    const onOrganicDisconnectWrapper = (deviceId: string) => {
      const snapshot = actor.getSnapshot();
      if (snapshot.value !== 'DISCONNECTING') {
        actor.send({ type: 'RECOVERY_START', ghostedMacs: [deviceId] });
      }
    };

    mockInput.onOrganicDisconnect = onOrganicDisconnectWrapper;

    await walkToReady(actor);
    expect(actor.getSnapshot().value).toBe('READY');

    // Trigger organic drop out
    onOrganicDisconnectWrapper('MAC1');
    expect(actor.getSnapshot().value).toBe('RECOVERING');
    expect(actor.getSnapshot().context.ghostedDeviceIds).toEqual(['MAC1']);
  });

  it('15. onOrganicDisconnect is suppressed during DISCONNECTING', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    
    const onOrganicDisconnectWrapper = (deviceId: string) => {
      const snapshot = actor.getSnapshot();
      if (snapshot.value !== 'DISCONNECTING') {
        actor.send({ type: 'RECOVERY_START', ghostedMacs: [deviceId] });
      }
    };

    mockInput.onOrganicDisconnect = onOrganicDisconnectWrapper;

    await walkToReady(actor);
    expect(actor.getSnapshot().value).toBe('READY');

    // Request disconnect, transitioning to DISCONNECTING state
    actor.send({ type: 'DISCONNECT_REQUEST' });
    expect(actor.getSnapshot().value).toBe('DISCONNECTING');

    // Trigger organic drop out during disconnect
    onOrganicDisconnectWrapper('MAC1');
    // Ensure state remains DISCONNECTING and did NOT enter RECOVERING
    expect(actor.getSnapshot().value).toBe('DISCONNECTING');
    expect(actor.getSnapshot().context.ghostedDeviceIds).toEqual([]);
  });

  // --- Group C: Context Assertions ---

  it('16. ghostedDeviceIds populated correctly on RECOVERY_START', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'RECOVERY_START', ghostedMacs: ['MAC1', 'MAC2'] });
    expect(actor.getSnapshot().context.ghostedDeviceIds).toEqual(['MAC1', 'MAC2']);
  });

  it('17. connectedDevices cleared on DISCONNECT_COMPLETE, but not on FORCE_IDLE', async () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    await walkToReady(actor);
    expect(actor.getSnapshot().context.connectedDevices.length).toBe(1);

    actor.send({ type: 'FORCE_IDLE' });
    expect(actor.getSnapshot().context.connectedDevices.length).toBe(1);

    // Go back to ready
    actor.send({ type: 'CONNECT_REQUEST', targetMacs: ['MAC1'] });
    const mockDevice = { id: 'MAC1', name: 'Mock Skates' } as unknown as Device;
    mockConnectResolve({ devices: [mockDevice] });
    await mockConnectPromise;
    await new Promise((res) => setTimeout(res, 0));
    expect(actor.getSnapshot().value).toBe('READY');

    // Request formal disconnect
    actor.send({ type: 'DISCONNECT_REQUEST' });
    actor.send({ type: 'DISCONNECT_COMPLETE' });
    expect(actor.getSnapshot().value).toBe('IDLE');
    expect(actor.getSnapshot().context.connectedDevices).toEqual([]);
  });

  it('18. targetMacs set correctly on CONNECT_REQUEST', () => {
    const actor = createActor(testBleMachine, { input: mockInput });
    actor.start();
    actor.send({ type: 'CONNECT_REQUEST', targetMacs: ['MAC1', 'MAC2'] });
    expect(actor.getSnapshot().context.targetMacs).toEqual(['MAC1', 'MAC2']);
  });
});

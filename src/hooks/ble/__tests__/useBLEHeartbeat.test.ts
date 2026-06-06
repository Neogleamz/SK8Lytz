import { renderHook, act } from '@testing-library/react-hooks';
import { useBLEHeartbeat } from '../useBLEHeartbeat';

jest.useFakeTimers();

const HEARTBEAT_MS = 45_000;

describe('useBLEHeartbeat', () => {
  const MAC = 'AA:BB:CC:DD:EE:FF';
  const mockDevice = { id: MAC };

  const mockAdapter = {
    serviceUUID: '0000FFE0-0000-1000-8000-00805F9B34FB',
    writeCharacteristicUUID: '0000FFE1-0000-1000-8000-00805F9B34FB',
    buildQuerySettings: jest.fn().mockReturnValue({
      packets: [[0x7e, 0x00, 0x63, 0x12, 0x21, 0x0f, 0x00, 0xef]],
      interPacketDelayMs: 0,
      isRateLimited: false,
    }),
  };

  let bleManager: Record<string, jest.Mock>;
  let connectedDevicesRef: { current: typeof mockDevice[] };
  let adapterMapRef: { current: Map<string, typeof mockAdapter> };
  let onStaleLinkDetected: jest.Mock;

  beforeEach(() => {
    bleManager = {
      writeCharacteristicWithoutResponseForDevice: jest.fn().mockResolvedValue(undefined),
      readRSSIForDevice: jest.fn().mockResolvedValue(-65),
      cancelDeviceConnection: jest.fn().mockResolvedValue(undefined),
    };
    connectedDevicesRef = { current: [mockDevice] };
    adapterMapRef = { current: new Map([[MAC, mockAdapter]]) };
    onStaleLinkDetected = jest.fn();
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.clearAllTimers();
  });

  // ── Happy path ──────────────────────────────────────────────────────────────

  it('sends 0x63 hardware query ping to Zengge device after 45s', async () => {
    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    await act(async () => {
      jest.advanceTimersByTime(HEARTBEAT_MS + 1);
      await Promise.resolve();
    });

    expect(bleManager.writeCharacteristicWithoutResponseForDevice).toHaveBeenCalledTimes(1);
    expect(bleManager.writeCharacteristicWithoutResponseForDevice).toHaveBeenCalledWith(
      MAC,
      mockAdapter.serviceUUID,
      mockAdapter.writeCharacteristicUUID,
      expect.any(String), // base64-encoded packet
    );
    expect(onStaleLinkDetected).not.toHaveBeenCalled();
  });

  it('falls back to readRSSIForDevice when device has no adapter', async () => {
    adapterMapRef.current.clear();

    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    await act(async () => {
      jest.advanceTimersByTime(HEARTBEAT_MS + 1);
      await Promise.resolve();
    });

    expect(bleManager.readRSSIForDevice).toHaveBeenCalledWith(MAC);
    expect(bleManager.writeCharacteristicWithoutResponseForDevice).not.toHaveBeenCalled();
    expect(onStaleLinkDetected).not.toHaveBeenCalled();
  });

  // ── Failure / stale link path ───────────────────────────────────────────────

  it('cancels connection and calls onStaleLinkDetected on GATT 133 write error', async () => {
    bleManager.writeCharacteristicWithoutResponseForDevice.mockRejectedValueOnce(
      new Error('BleError: Device GATT Error (code: 133)')
    );

    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    await act(async () => {
      jest.advanceTimersByTime(HEARTBEAT_MS + 1);
      // Two ticks: one for the write rejection, one for the cancel
      await Promise.resolve();
      await Promise.resolve();
    });

    expect(bleManager.cancelDeviceConnection).toHaveBeenCalledWith(MAC);
    expect(onStaleLinkDetected).toHaveBeenCalledWith(MAC);
  });

  it('cancels connection and calls onStaleLinkDetected on RSSI read error (BanlanX path)', async () => {
    adapterMapRef.current.clear();
    bleManager.readRSSIForDevice.mockRejectedValueOnce(
      new Error('BleError: Device disconnected (code: 8)')
    );

    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    await act(async () => {
      jest.advanceTimersByTime(HEARTBEAT_MS + 1);
      await Promise.resolve();
      await Promise.resolve();
    });

    expect(bleManager.cancelDeviceConnection).toHaveBeenCalledWith(MAC);
    expect(onStaleLinkDetected).toHaveBeenCalledWith(MAC);
  });

  it('does not call onStaleLinkDetected if cancelDeviceConnection itself throws', async () => {
    // cancelDeviceConnection fails but stale detection should still fire
    bleManager.writeCharacteristicWithoutResponseForDevice.mockRejectedValueOnce(new Error('GATT 133'));
    bleManager.cancelDeviceConnection.mockRejectedValueOnce(new Error('already disconnected'));

    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    await act(async () => {
      jest.advanceTimersByTime(HEARTBEAT_MS + 1);
      await Promise.resolve();
      await Promise.resolve();
      await Promise.resolve();
    });

    // onStaleLinkDetected must still be called even if cancel throws
    expect(onStaleLinkDetected).toHaveBeenCalledWith(MAC);
  });

  // ── Guard conditions ────────────────────────────────────────────────────────

  it('does not ping when connectedDevices is empty', async () => {
    connectedDevicesRef.current = [];

    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    await act(async () => {
      jest.advanceTimersByTime(HEARTBEAT_MS + 1);
      await Promise.resolve();
    });

    expect(bleManager.writeCharacteristicWithoutResponseForDevice).not.toHaveBeenCalled();
    expect(bleManager.readRSSIForDevice).not.toHaveBeenCalled();
  });

  it('does not set up an interval on web platform', () => {
    const originalPlatform = require('react-native').Platform.OS;
    Object.defineProperty(require('react-native').Platform, 'OS', { get: () => 'web' });

    const setIntervalSpy = jest.spyOn(global, 'setInterval');

    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    expect(setIntervalSpy).not.toHaveBeenCalled();

    Object.defineProperty(require('react-native').Platform, 'OS', { get: () => originalPlatform });
    setIntervalSpy.mockRestore();
  });

  it('cleans up interval on unmount', () => {
    const clearIntervalSpy = jest.spyOn(global, 'clearInterval');

    const { unmount } = renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    unmount();
    expect(clearIntervalSpy).toHaveBeenCalled();
    clearIntervalSpy.mockRestore();
  });

  it('pings multiple devices independently and reports only the stale one', async () => {
    const MAC2 = 'FF:EE:DD:CC:BB:AA';
    const device2 = { id: MAC2 };
    connectedDevicesRef.current = [mockDevice, device2];
    adapterMapRef.current.set(MAC2, mockAdapter);

    // Only the second device's write fails
    bleManager.writeCharacteristicWithoutResponseForDevice
      .mockResolvedValueOnce(undefined)   // device 1: OK
      .mockRejectedValueOnce(new Error('GATT 133')); // device 2: stale

    renderHook(() =>
      useBLEHeartbeat({ bleManager, connectedDevicesRef, adapterMapRef, onStaleLinkDetected })
    );

    await act(async () => {
      jest.advanceTimersByTime(HEARTBEAT_MS + 1);
      await Promise.resolve();
      await Promise.resolve();
    });

    expect(bleManager.cancelDeviceConnection).toHaveBeenCalledWith(MAC2);
    expect(bleManager.cancelDeviceConnection).not.toHaveBeenCalledWith(MAC);
    expect(onStaleLinkDetected).toHaveBeenCalledWith(MAC2);
    expect(onStaleLinkDetected).not.toHaveBeenCalledWith(MAC);
  });
});

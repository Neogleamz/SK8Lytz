/**
 * useBLEHeartbeat.test.ts — Stale GATT Link Detector Unit Tests
 *
 * Tests pingConnectedDevice (the pure async liveness probe) directly.
 * No React renderer or fiber context required — pingConnectedDevice is a
 * plain async function exported specifically to enable this testing pattern.
 *
 * The useBLEHeartbeat hook itself is just a thin setInterval orchestrator;
 * its registration behaviour is verified via the 'registers setInterval' test.
 */

// Must be before imports — jest.mock() is hoisted by Babel.
jest.mock('react-native', () => ({
  Platform: { OS: 'android' },
}));
jest.mock('react-native-ble-plx', () => ({}));
jest.mock('../../../services/AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
    setLastTxPayload: jest.fn(),
    updateKnownDevices: jest.fn(),
  },
}));

import type { IControllerProtocol } from '../../../protocols/IControllerProtocol';
import { pingConnectedDevice } from '../useBLEHeartbeat';

jest.mock('../../../services/BleWriteQueue', () => ({
  enqueueWrite: jest.fn((priority, operation) => operation()),
}));

// ── Shared mocks ─────────────────────────────────────────────────────────────
const MAC = 'AA:BB:CC:DD:EE:FF';

const mockAdapter = {
  serviceUUID: '0000FFE0-0000-1000-8000-00805F9B34FB',
  writeCharacteristicUUID: '0000FFE1-0000-1000-8000-00805F9B34FB',
  buildQuerySettings: jest.fn().mockReturnValue({
    packets: [[0x7e, 0x00, 0x63, 0x12, 0x21, 0x0f, 0x00, 0xef]],
    interPacketDelayMs: 0,
    isRateLimited: false,
  }),
} as unknown as IControllerProtocol;

// Typed accessor to avoid casting at every call site in assertions
const adapter = mockAdapter as unknown as {
  serviceUUID: string;
  writeCharacteristicUUID: string;
  buildQuerySettings: jest.Mock;
};

describe('pingConnectedDevice', () => {
  let bleManager: {
    writeCharacteristicWithoutResponseForDevice: jest.Mock;
    readRSSIForDevice: jest.Mock;
    cancelDeviceConnection: jest.Mock;
  };
  let onStaleLinkDetected: jest.Mock;

  beforeEach(() => {
    bleManager = {
      writeCharacteristicWithoutResponseForDevice: jest.fn().mockResolvedValue(undefined),
      readRSSIForDevice: jest.fn().mockResolvedValue(-65),
      cancelDeviceConnection: jest.fn().mockResolvedValue(undefined),
    };
    onStaleLinkDetected = jest.fn();
  });

  afterEach(() => {
    jest.clearAllMocks();
    adapter.buildQuerySettings.mockClear();
  });

  // ── Happy path ──────────────────────────────────────────────────────────────

  it('sends 0x63 hardware query to Zengge device when adapter is present', async () => {
    await pingConnectedDevice(MAC, bleManager, mockAdapter, onStaleLinkDetected);

    expect(bleManager.writeCharacteristicWithoutResponseForDevice).toHaveBeenCalledTimes(1);
    expect(bleManager.writeCharacteristicWithoutResponseForDevice).toHaveBeenCalledWith(
      MAC,
      adapter.serviceUUID,
      adapter.writeCharacteristicUUID,
      expect.any(String), // base64-encoded 0x63 packet
    );
    expect(onStaleLinkDetected).not.toHaveBeenCalled();
  });

  it('falls back to readRSSIForDevice when no adapter is provided (BanlanX path)', async () => {
    await pingConnectedDevice(MAC, bleManager, undefined, onStaleLinkDetected);

    expect(bleManager.readRSSIForDevice).toHaveBeenCalledWith(MAC);
    expect(bleManager.writeCharacteristicWithoutResponseForDevice).not.toHaveBeenCalled();
    expect(onStaleLinkDetected).not.toHaveBeenCalled();
  });

  it('falls back to readRSSIForDevice when adapter buildQuerySettings returns empty packets', async () => {
    adapter.buildQuerySettings.mockReturnValueOnce({ packets: [], interPacketDelayMs: 0, isRateLimited: false });
    await pingConnectedDevice(MAC, bleManager, mockAdapter, onStaleLinkDetected);

    expect(bleManager.readRSSIForDevice).toHaveBeenCalledWith(MAC);
    expect(onStaleLinkDetected).not.toHaveBeenCalled();
  });

  // ── Stale link detection ────────────────────────────────────────────────────

  it('cancels connection and calls onStaleLinkDetected on GATT 133 write error', async () => {
    bleManager.writeCharacteristicWithoutResponseForDevice.mockRejectedValueOnce(
      new Error('BleError: Device GATT Error (code: 133)'),
    );

    await pingConnectedDevice(MAC, bleManager, mockAdapter, onStaleLinkDetected);

    expect(bleManager.cancelDeviceConnection).toHaveBeenCalledWith(MAC);
    expect(onStaleLinkDetected).toHaveBeenCalledWith(MAC);
  });

  it('cancels connection and calls onStaleLinkDetected on RSSI error (BanlanX path)', async () => {
    bleManager.readRSSIForDevice.mockRejectedValueOnce(
      new Error('BleError: Device disconnected (code: 8)'),
    );

    await pingConnectedDevice(MAC, bleManager, undefined, onStaleLinkDetected);

    expect(bleManager.cancelDeviceConnection).toHaveBeenCalledWith(MAC);
    expect(onStaleLinkDetected).toHaveBeenCalledWith(MAC);
  });

  it('still calls onStaleLinkDetected even if cancelDeviceConnection itself throws', async () => {
    bleManager.writeCharacteristicWithoutResponseForDevice.mockRejectedValueOnce(new Error('GATT 133'));
    bleManager.cancelDeviceConnection.mockRejectedValueOnce(new Error('already disconnected'));

    await pingConnectedDevice(MAC, bleManager, mockAdapter, onStaleLinkDetected);

    // The .catch(() => undefined) in pingConnectedDevice swallows the cancel error,
    // so onStaleLinkDetected must still fire.
    expect(onStaleLinkDetected).toHaveBeenCalledWith(MAC);
  });

  it('does not call onStaleLinkDetected when ping succeeds', async () => {
    await pingConnectedDevice(MAC, bleManager, mockAdapter, onStaleLinkDetected);
    expect(onStaleLinkDetected).not.toHaveBeenCalled();
  });
});

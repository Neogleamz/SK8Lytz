/**
 * useBLERSSIMonitor.test.ts — Post-Connect RSSI Monitor Unit Tests
 *
 * Tests readDeviceRSSI (the pure async liveness + signal probe) directly.
 * No React renderer or fiber context required — same pattern as
 * useBLEHeartbeat's pingConnectedDevice tests.
 */

jest.mock('react-native', () => ({ Platform: { OS: 'android' } }));
jest.mock('react-native-ble-plx', () => ({}));
jest.mock('../../../services/AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
    error: jest.fn(),
  },
}));

import {
  readDeviceRSSI,
  RSSI_WEAK_THRESHOLD,
  RSSI_CRITICAL_THRESHOLD,
  RSSI_POLL_INTERVAL_MS,
} from '../useBLERSSIMonitor';

const MAC = 'AA:BB:CC:DD:EE:FF';

describe('readDeviceRSSI', () => {
  let bleManager: { readRSSIForDevice: jest.Mock };

  beforeEach(() => {
    bleManager = {
      readRSSIForDevice: jest.fn(),
    };
  });

  afterEach(() => jest.clearAllMocks());

  // ── Happy path ──────────────────────────────────────────────────────────────

  it('returns the rssi number from the Device object on success', async () => {
    bleManager.readRSSIForDevice.mockResolvedValueOnce({ id: MAC, rssi: -62 });

    const result = await readDeviceRSSI(MAC, bleManager);

    expect(result).toBe(-62);
    expect(bleManager.readRSSIForDevice).toHaveBeenCalledWith(MAC);
  });

  it('returns null when device.rssi is null (device returned but no RSSI field)', async () => {
    bleManager.readRSSIForDevice.mockResolvedValueOnce({ id: MAC, rssi: null });

    const result = await readDeviceRSSI(MAC, bleManager);

    expect(result).toBeNull();
  });

  // ── Error handling ──────────────────────────────────────────────────────────

  it('returns null on GATT 133 error — does not throw', async () => {
    bleManager.readRSSIForDevice.mockRejectedValueOnce(
      new Error('BleError: Device GATT Error (code: 133)'),
    );

    const result = await readDeviceRSSI(MAC, bleManager);

    expect(result).toBeNull();
  });

  it('returns null on disconnection error (code 8) — does not throw', async () => {
    bleManager.readRSSIForDevice.mockRejectedValueOnce(
      new Error('BleError: Device disconnected (code: 8)'),
    );

    const result = await readDeviceRSSI(MAC, bleManager);

    expect(result).toBeNull();
  });

  it('returns null when bleManager itself is null — does not throw', async () => {
    const result = await readDeviceRSSI(MAC, null as unknown as Parameters<typeof readDeviceRSSI>[1]);

    expect(result).toBeNull();
  });

  // ── Threshold constants ─────────────────────────────────────────────────────

  it('exports correct RSSI threshold constants', () => {
    expect(RSSI_WEAK_THRESHOLD).toBe(-75);
    expect(RSSI_CRITICAL_THRESHOLD).toBe(-82);
    expect(RSSI_POLL_INTERVAL_MS).toBe(30_000);
  });

  it('reads excellent signal correctly', async () => {
    bleManager.readRSSIForDevice.mockResolvedValueOnce({ id: MAC, rssi: -55 });
    const result = await readDeviceRSSI(MAC, bleManager);
    expect(result).toBe(-55);
    expect(result! > RSSI_WEAK_THRESHOLD).toBe(true);
  });

  it('reads weak-but-not-critical signal correctly', async () => {
    bleManager.readRSSIForDevice.mockResolvedValueOnce({ id: MAC, rssi: -78 });
    const result = await readDeviceRSSI(MAC, bleManager);
    expect(result).toBe(-78);
    expect(result! < RSSI_WEAK_THRESHOLD).toBe(true);
    expect(result! > RSSI_CRITICAL_THRESHOLD).toBe(true);
  });

  it('reads critical signal correctly', async () => {
    bleManager.readRSSIForDevice.mockResolvedValueOnce({ id: MAC, rssi: -85 });
    const result = await readDeviceRSSI(MAC, bleManager);
    expect(result).toBe(-85);
    expect(result! < RSSI_CRITICAL_THRESHOLD).toBe(true);
  });
});

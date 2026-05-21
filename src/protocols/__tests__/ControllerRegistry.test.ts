/**
 * ControllerRegistry.test.ts — Unit Tests for ControllerRegistry
 *
 * Verifies: resolveProtocol() advertisement matching, getDefaultProtocol()
 * returns Zengge (not BanlanX), resolveProtocolForDevice() adapter map
 * lookup, and registerProtocol() duplicate guard.
 */

import {
  resolveProtocol,
  getDefaultProtocol,
  resolveProtocolForDevice,
  registerProtocol,
  getRegisteredProtocolIds,
} from '../ControllerRegistry';
import type { IControllerProtocol } from '../IControllerProtocol';

// ─── resolveProtocol ──────────────────────────────────────────────────────────

describe('resolveProtocol', () => {
  it('returns BanlanxAdapter for FFE0 service UUID', () => {
    const adapter = resolveProtocol(['0000ffe0-0000-1000-8000-00805f9b34fb']);
    expect(adapter).not.toBeNull();
    expect(adapter?.protocolId).toBe('banlanx');
  });

  it('returns ZenggeAdapter for FFFF service UUID', () => {
    const adapter = resolveProtocol(['0000ffff-0000-1000-8000-00805f9b34fb']);
    expect(adapter).not.toBeNull();
    expect(adapter?.protocolId).toBe('zengge');
  });

  it('returns BanlanxAdapter for FFE0 even when FFFF is also present (BanlanX is checked first)', () => {
    // BanlanX is registered first in the registry — it wins on FFE0
    const adapter = resolveProtocol([
      '0000ffe0-0000-1000-8000-00805f9b34fb',
      '0000ffff-0000-1000-8000-00805f9b34fb',
    ]);
    expect(adapter?.protocolId).toBe('banlanx');
  });

  it('returns null for an unrecognized service UUID', () => {
    const adapter = resolveProtocol(['0000abcd-0000-1000-8000-00805f9b34fb']);
    expect(adapter).toBeNull();
  });

  it('returns null for an empty UUID list', () => {
    const adapter = resolveProtocol([]);
    expect(adapter).toBeNull();
  });

  it('BanlanX matches MFR ID 0x5053 (SP reversed) via manufacturerData', () => {
    // BanlanX secondary match: manufacturer data bytes [0x53, 0x50, ...]
    const mfrBuffer = Buffer.from([0x53, 0x50, 0x00, 0x00]).toString('base64');
    const adapter = resolveProtocol([], mfrBuffer);
    expect(adapter?.protocolId).toBe('banlanx');
  });
});

// ─── getDefaultProtocol ───────────────────────────────────────────────────────

describe('getDefaultProtocol', () => {
  it('returns ZenggeAdapter — NOT BanlanxAdapter', () => {
    // REGRESSION CHECK: BanlanxAdapter is registry[0] for priority matching
    // but getDefaultProtocol() must always return the production default (Zengge).
    const defaultAdapter = getDefaultProtocol();
    expect(defaultAdapter.protocolId).toBe('zengge');
    expect(defaultAdapter.protocolId).not.toBe('banlanx');
  });

  it('default adapter has requiresSoftwareFFT = true (Zengge needs app FFT)', () => {
    expect(getDefaultProtocol().requiresSoftwareFFT).toBe(true);
  });

  it('default adapter service UUID is the Zengge FFFF UUID', () => {
    expect(getDefaultProtocol().serviceUUID).toBe('0000ffff-0000-1000-8000-00805f9b34fb');
  });
});

// ─── resolveProtocolForDevice ─────────────────────────────────────────────────

describe('resolveProtocolForDevice', () => {
  it('returns the adapter from the map when device is found', () => {
    const banlanx = resolveProtocol(['0000ffe0-0000-1000-8000-00805f9b34fb'])!;
    const adapterMap = new Map<string, IControllerProtocol>([
      ['AA:BB:CC:DD:EE:FF', banlanx],
    ]);
    const resolved = resolveProtocolForDevice('AA:BB:CC:DD:EE:FF', adapterMap);
    expect(resolved.protocolId).toBe('banlanx');
  });

  it('falls back to getDefaultProtocol() (Zengge) when device is NOT in map', () => {
    const adapterMap = new Map<string, IControllerProtocol>();
    const resolved = resolveProtocolForDevice('00:11:22:33:44:55', adapterMap);
    expect(resolved.protocolId).toBe('zengge');
  });
});

// ─── registerProtocol ────────────────────────────────────────────────────────

describe('registerProtocol', () => {
  it('does not register a duplicate protocol ID', () => {
    const idsBefore = getRegisteredProtocolIds();
    const zenggeCount = idsBefore.filter(id => id === 'zengge').length;

    // Try to register a fake adapter with the same protocolId as ZenggeAdapter
    const fakeZengge: IControllerProtocol = {
      protocolId: 'zengge',
      serviceUUID: 'fake',
      writeCharacteristicUUID: 'fake',
      notifyCharacteristicUUID: 'fake',
      requiresSoftwareFFT: false,
      matchesAdvertisement: () => false,
      parseFirmwareFromAdvertisement: () => null,
      getHandshakePayloads: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildQuerySettings: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      parseSettingsResponse: () => null,
      buildWriteSettings: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildQueryRfRemoteState: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      parseRfRemoteState: () => null,
      buildPowerOn: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildPowerOff: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildSolidColor: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildMultiColor: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildCustomMode: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildCustomModeExtended: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildEffect: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildCandleMode: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildStreamPixelFrame: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildMusicConfig: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      buildMusicMagnitude: () => ({ packets: [], interPacketDelayMs: 0, isRateLimited: false }),
      prepareForTransmission: (r) => r,
    };

    registerProtocol(fakeZengge);

    const idsAfter = getRegisteredProtocolIds();
    const zenggeCountAfter = idsAfter.filter(id => id === 'zengge').length;

    // Must still be exactly 1 Zengge adapter (no duplicate added)
    expect(zenggeCountAfter).toBe(zenggeCount);
  });
});

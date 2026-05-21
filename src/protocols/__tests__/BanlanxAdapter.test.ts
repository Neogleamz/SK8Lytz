/**
 * BanlanxAdapter.test.ts — Unit Tests for BanlanxAdapter
 *
 * TDD Mandate: Verifies byte-exact protocol output for all critical
 * BanlanX SP621E opcodes per BANLANX_PROTOCOL_BIBLE.md.
 */

import { BanlanxAdapter } from '../BanlanxAdapter';

describe('BanlanxAdapter', () => {
  const adapter = new BanlanxAdapter();

  // ─── Identity ──────────────────────────────────────────────────────────────

  it('has correct protocolId', () => {
    expect(adapter.protocolId).toBe('banlanx');
  });

  it('has requiresSoftwareFFT = false (native hardware FFT)', () => {
    expect(adapter.requiresSoftwareFFT).toBe(false);
  });

  // ─── Power ────────────────────────────────────────────────────────────────

  it('buildPowerOn returns [0xA0, 0x50, 0x01, 0x01]', () => {
    // BANLANX_PROTOCOL_BIBLE.md §3 — 0x50 Power ON
    const result = adapter.buildPowerOn();
    expect(result.packets).toEqual([[0xA0, 0x50, 0x01, 0x01]]);
    expect(result.packets.length).toBe(1);
    expect(result.interPacketDelayMs).toBe(0);
    expect(result.isRateLimited).toBe(false);
  });

  it('buildPowerOff returns [0xA0, 0x50, 0x01, 0x00]', () => {
    // BANLANX_PROTOCOL_BIBLE.md §3 — 0x50 Power OFF
    const result = adapter.buildPowerOff();
    expect(result.packets).toEqual([[0xA0, 0x50, 0x01, 0x00]]);
    expect(result.packets.length).toBe(1);
  });

  // ─── Color ────────────────────────────────────────────────────────────────

  it('buildSolidColor returns [0xA0, 0x52, 0x03, R, G, B]', () => {
    // BANLANX_PROTOCOL_BIBLE.md §3 — 0x52 Set Solid Color
    const result = adapter.buildSolidColor(255, 128, 0);
    expect(result.packets).toEqual([[0xA0, 0x52, 0x03, 255, 128, 0]]);
    expect(result.isRateLimited).toBe(false);
  });

  it('buildSolidColor masks values to 0xFF (overflow protection)', () => {
    // Values above 255 should be masked — no hardware lockup from out-of-range bytes
    const result = adapter.buildSolidColor(256, 0, 0);
    expect(result.packets[0][3]).toBe(0); // 256 & 0xFF = 0
  });

  // ─── Effect ───────────────────────────────────────────────────────────────

  it('buildEffect returns 2 packets with 20ms interPacketDelayMs', () => {
    // BANLANX_PROTOCOL_BIBLE.md §3, §7 — 0x53 effect + 0x54 speed, 20ms required
    const result = adapter.buildEffect(0x01, 50, 100);
    expect(result.packets.length).toBe(2);
    expect(result.packets[0]).toEqual([0xA0, 0x53, 0x01, 0x01]); // effect select
    expect(result.packets[1][1]).toBe(0x54);                      // speed opcode
    expect(result.interPacketDelayMs).toBe(20);
  });

  it('buildEffect clamps speed to 1-10 BanlanX scale', () => {
    // Speed 100 → Math.round(100/10) = 10 (max)
    const maxResult = adapter.buildEffect(0x01, 100, 100);
    expect(maxResult.packets[1][3]).toBe(10);

    // Speed 1 → Math.round(1/10) = 0 → clamped to 1 (min)
    const minResult = adapter.buildEffect(0x01, 1, 100);
    expect(minResult.packets[1][3]).toBe(1);

    // Speed 0 → clamped to 1
    const zeroResult = adapter.buildEffect(0x01, 0, 100);
    expect(zeroResult.packets[1][3]).toBe(1);
  });

  // ─── Handshake ────────────────────────────────────────────────────────────

  it('getHandshakePayloads returns empty packets (no handshake)', () => {
    // BANLANX_PROTOCOL_BIBLE.md §2 — no session time sync required
    const result = adapter.getHandshakePayloads();
    expect(result.packets).toEqual([]);
    expect(result.packets.length).toBe(0);
  });

  // ─── Music ────────────────────────────────────────────────────────────────

  it('buildMusicMagnitude returns empty no-op (native FFT)', () => {
    // BANLANX_PROTOCOL_BIBLE.md §10 — hardware FFT, no software magnitude stream
    const result = adapter.buildMusicMagnitude(128);
    expect(result.packets).toEqual([]);
  });

  it('buildMusicConfig sends internal mic (0x59) + sensitivity (0x5A)', () => {
    // BANLANX_PROTOCOL_BIBLE.md §10 — 0x59 audio input + 0x5A sensitivity
    const result = adapter.buildMusicConfig({
      micSensitivity: 127, // ~50% → gain ~8
      matrixStyle: 0,
      patternId: 0,
      color1: { r: 0, g: 0, b: 0 },
      color2: { r: 0, g: 0, b: 0 },
      speed: 5,
      brightness: 100,
    });
    expect(result.packets.length).toBe(2);
    expect(result.packets[0]).toEqual([0xA0, 0x59, 0x01, 0x00]); // internal mic
    expect(result.packets[1][1]).toBe(0x5A);                       // sensitivity opcode
    expect(result.packets[1][3]).toBeGreaterThanOrEqual(1);
    expect(result.packets[1][3]).toBeLessThanOrEqual(16);
  });

  // ─── Transport ────────────────────────────────────────────────────────────

  it('prepareForTransmission is a passthrough (no chunking needed)', () => {
    // BANLANX_PROTOCOL_BIBLE.md §2 — all packets < 20 bytes
    const input = { packets: [[0xA0, 0x52, 0x03, 1, 2, 3]], interPacketDelayMs: 0, isRateLimited: false };
    expect(adapter.prepareForTransmission(input, 64)).toEqual(input);
    expect(adapter.prepareForTransmission(input, 23)).toEqual(input);
  });

  // ─── Advertisement Matching ───────────────────────────────────────────────

  it('matchesAdvertisement matches FFE0 service UUID (primary)', () => {
    // BANLANX_PROTOCOL_BIBLE.md §2 — 0000ffe0-0000-1000-8000-00805f9b34fb
    expect(adapter.matchesAdvertisement(['0000ffe0-0000-1000-8000-00805f9b34fb'])).toBe(true);
  });

  it('matchesAdvertisement does NOT match Zengge FFFF UUID', () => {
    // BANLANX_PROTOCOL_BIBLE.md §1 — Zengge uses FFFF, BanlanX uses FFE0
    expect(adapter.matchesAdvertisement(['0000ffff-0000-1000-8000-00805f9b34fb'])).toBe(false);
  });

  it('matchesAdvertisement matches MFR ID 5053 (0x53, 0x50 LE) as secondary', () => {
    // BANLANX_PROTOCOL_BIBLE.md §1 — Manufacturer ID 0x5053 = "PS" (SP reversed)
    const mfrBuffer = Buffer.from([0x53, 0x50, 0x00, 0x00]).toString('base64');
    expect(adapter.matchesAdvertisement([], mfrBuffer)).toBe(true);
  });

  // ─── No-ops / Stubs ───────────────────────────────────────────────────────

  it('buildQuerySettings returns empty (no EEPROM query in Phase 1)', () => {
    expect(adapter.buildQuerySettings(false).packets).toEqual([]);
  });

  it('parseSettingsResponse returns null (no EEPROM parsing in Phase 1)', () => {
    expect(adapter.parseSettingsResponse([0xA0, 0x70, 0x00])).toBeNull();
  });

  it('buildQueryRfRemoteState returns empty (no RF remote on SP621E)', () => {
    expect(adapter.buildQueryRfRemoteState().packets).toEqual([]);
  });

  it('parseRfRemoteState returns null', () => {
    expect(adapter.parseRfRemoteState([0x00, 0x00])).toBeNull();
  });
});

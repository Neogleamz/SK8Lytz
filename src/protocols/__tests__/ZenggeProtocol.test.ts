import { ZenggeProtocol } from '../ZenggeProtocol';
import { ZenggeAdapter } from '../ZenggeAdapter';
import type { ProtocolResult } from '../IControllerProtocol';

// ─── ZenggeProtocol (Static Facade Backward Compatibility) ───────────────────
// Verifies that all 25 legacy consumers can still call ZenggeProtocol.method()
// statically and receive correct results. The static methods delegate to the
// singleton instance (_instance) — these tests prove that still works.

describe('ZenggeProtocol', () => {
  describe('calculateChecksum', () => {
    it('should correctly sum and mask to 8 bits', () => {
      expect(ZenggeProtocol.calculateChecksum([0x71, 0x23, 0x0f])).toBe(0xa3);
      expect(ZenggeProtocol.calculateChecksum([0xFF, 0x02])).toBe(0x01); // 257 & 0xFF
    });
  });

  describe('wrapCommand (static facade)', () => {
    it('should correctly build the header envelope via the static singleton', () => {
      // The static facade delegates to _instance. We spy on the prototype's
      // instance method (getSequenceCounter) to make the test deterministic.
      const proto = ZenggeProtocol.prototype as any;
      const spy = jest.spyOn(proto, 'getSequenceCounter').mockReturnValue(0);

      const payload = [0x71, 0x23, 0x0f, 0xa3];
      const result = ZenggeProtocol.wrapCommand(payload);

      // Expected header: [0x00, 0x00, 0x80, 0x00, 0x00, 0x04, 0x05, 0x0b]
      expect(result.slice(0, 8)).toEqual([0x00, 0x00, 0x80, 0x00, 0x00, 0x04, 0x05, 0x0b]);
      // Appended payload
      expect(result.slice(8)).toEqual([0x71, 0x23, 0x0f, 0xa3]);

      spy.mockRestore();
    });
  });

  describe('queryHardwareSettings', () => {
    it('should generate correct query packet for no-mic', () => {
      const proto = ZenggeProtocol.prototype as any;
      const spy = jest.spyOn(proto, 'getSequenceCounter').mockReturnValue(0);
      const result = ZenggeProtocol.queryHardwareSettings(false);

      // raw: [0x63, 0x12, 0x21, 0x0F] -> cs: 0xA5
      expect(result.slice(8)).toEqual([0x63, 0x12, 0x21, 0x0F, 0xA5]);
      spy.mockRestore();
    });
  });

  describe('setMultiColor', () => {
    it('should construct correct 0x59 payload and respect constraints', () => {
      const proto = ZenggeProtocol.prototype as any;
      const spy = jest.spyOn(proto, 'getSequenceCounter').mockReturnValue(0);

      const colors = [
        { r: 255, g: 0, b: 0 },
        { r: 0, g: 255, b: 0 },
      ]; // Only 2 colors, will be padded to 12

      const result = ZenggeProtocol.setMultiColor(colors, 50, 10, 1, 0x02);

      // Payload starts at index 8 after wrapper
      expect(result[8]).toBe(0x59);

      // Check total length calculation: (12 points * 3) + 9 = 45 (0x2D)
      expect(result[9]).toBe(0x00); // len_hi
      expect(result[10]).toBe(0x2D); // len_lo

      // Check first pixel
      expect(result[11]).toBe(255);
      expect(result[12]).toBe(0);
      expect(result[13]).toBe(0);

      // Check hardware led points (50 = 0x00 0x32)
      // Colors take 3 * 12 = 36 bytes. Index 11 + 36 = 47
      expect(result[47]).toBe(0x00); // pts_hi
      expect(result[48]).toBe(0x32); // pts_lo

      spy.mockRestore();
    });
  });

  describe('RF Remote settings mapping', () => {
    it('should generate correct write payloads for each mode', () => {
      // ALLOW_ALL: modeByte=0x01
      const allowAll = ZenggeProtocol.setRfRemoteState('ALLOW_ALL', false);
      expect(allowAll[8]).toBe(0x2A);
      expect(allowAll[9]).toBe(0x01); // modeByte
      expect(allowAll[15]).toBe(0x00); // clearByte

      // ALLOW_NONE: modeByte=0x02
      const allowNone = ZenggeProtocol.setRfRemoteState('ALLOW_NONE', false);
      expect(allowNone[9]).toBe(0x02); // modeByte

      // ALLOW_PAIRED: modeByte=0x03
      const allowPaired = ZenggeProtocol.setRfRemoteState('ALLOW_PAIRED', false);
      expect(allowPaired[9]).toBe(0x03); // modeByte

      // Clear Pairing: clearByte=0x01
      const clear = ZenggeProtocol.setRfRemoteState('ALLOW_PAIRED', true);
      expect(clear[9]).toBe(0x03); // modeByte
      expect(clear[15]).toBe(0x01); // clearByte
    });

    it('should parse 0x2B remote responses correctly', () => {
      // Mock response: [0x2B, modeByte, pairedCount, ID_bytes...]
      // 0x01 -> ALLOW_ALL
      const resAll = ZenggeProtocol.parseRfRemoteState([0x2B, 0x01, 0x00]);
      expect(resAll).not.toBeNull();
      expect(resAll!.mode).toBe('ALLOW_ALL');

      // 0x02 -> ALLOW_NONE
      const resNone = ZenggeProtocol.parseRfRemoteState([0x2B, 0x02, 0x00]);
      expect(resNone!.mode).toBe('ALLOW_NONE');

      // 0x03 -> ALLOW_PAIRED
      const resPaired = ZenggeProtocol.parseRfRemoteState([0x2B, 0x03, 0x01, 0xAA, 0xBB, 0xCC, 0xDD]);
      expect(resPaired!.mode).toBe('ALLOW_PAIRED');
      expect(resPaired!.pairedCount).toBe(1);
      expect(resPaired!.pairedRemoteIds).toEqual(['AABBCCDD']);
    });
  });
});

// ─── ZenggeAdapter (HAL Layer Tests) ─────────────────────────────────────────
// Verifies the adapter's IControllerProtocol implementation — ProtocolResult
// wrapping, corrected power opcodes, handshake, and transmission chunking.

describe('ZenggeAdapter', () => {
  let adapter: ZenggeAdapter;

  beforeEach(() => {
    adapter = new ZenggeAdapter();
  });

  describe('identity', () => {
    it('should have correct protocol ID and service UUID', () => {
      expect(adapter.protocolId).toBe('zengge');
      expect(adapter.serviceUUID).toBe('0000ffff-0000-1000-8000-00805f9b34fb');
      expect(adapter.requiresSoftwareFFT).toBe(true);
    });
  });

  describe('matchesAdvertisement', () => {
    it('should match on ffff service UUID', () => {
      expect(adapter.matchesAdvertisement(['0000ffff-0000-1000-8000-00805f9b34fb'])).toBe(true);
    });
    it('should NOT match on ffe0 service UUID (BanlanX)', () => {
      expect(adapter.matchesAdvertisement(['0000ffe0-0000-1000-8000-00805f9b34fb'])).toBe(false);
    });
    it('should return false for empty UUID list', () => {
      expect(adapter.matchesAdvertisement([])).toBe(false);
    });
  });

  describe('buildPowerOn', () => {
    it('should return 0x71 0x23 opcode — NOT the old 0x56 0xAA bug', () => {
      const result: ProtocolResult = adapter.buildPowerOn();
      expect(result.packets).toHaveLength(1);
      expect(result.interPacketDelayMs).toBe(0);
      // After the 8-byte wrapper, payload starts at index 8
      const payload = result.packets[0];
      // The 0x71 opcode confirms correct power-on
      // Payload inner bytes: [0x71, 0x23, 0x0f, 0xa3]
      expect(payload[8]).toBe(0x71);
      expect(payload[9]).toBe(0x23);
      // REGRESSION CHECK: 0x56 was the old wrong opcode (Scene Delete)
      expect(payload[8]).not.toBe(0x56);
    });
  });

  describe('buildPowerOff', () => {
    it('should return 0x71 0x24 opcode — NOT the old 0x56 0xAB bug', () => {
      const result: ProtocolResult = adapter.buildPowerOff();
      expect(result.packets).toHaveLength(1);
      const payload = result.packets[0];
      expect(payload[8]).toBe(0x71);
      expect(payload[9]).toBe(0x24);
      expect(payload[8]).not.toBe(0x56);
    });
  });

  describe('getHandshakePayloads', () => {
    it('should return a 0x10 time sync packet', () => {
      const result: ProtocolResult = adapter.getHandshakePayloads();
      expect(result.packets).toHaveLength(1);
      expect(result.interPacketDelayMs).toBe(0);
      // Inner payload byte 0 = 0x10 (time sync opcode)
      const payload = result.packets[0];
      expect(payload[8]).toBe(0x10);
    });
  });

  describe('buildMultiColor', () => {
    it('should return isRateLimited=true for pattern writes', () => {
      const result = adapter.buildMultiColor(
        [{ r: 255, g: 0, b: 0 }], 12, 16, 1, 0x02
      );
      expect(result.isRateLimited).toBe(true);
      expect(result.packets).toHaveLength(1);
      expect(result.packets[0][8]).toBe(0x59); // 0x59 opcode
    });
  });

  describe('buildQuerySettings', () => {
    it('should return a 0x63 query packet', () => {
      const result = adapter.buildQuerySettings(false);
      expect(result.packets).toHaveLength(1);
      expect(result.packets[0][8]).toBe(0x63);
    });
  });

  describe('buildMusicMagnitude', () => {
    it('should return a 0x74 magnitude packet and NOT be rate-limited', () => {
      const result = adapter.buildMusicMagnitude(128);
      expect(result.packets).toHaveLength(1);
      expect(result.isRateLimited).toBe(false);
      // Inner opcode
      expect(result.packets[0][8]).toBe(0x74);
    });
  });

  describe('prepareForTransmission', () => {
    it('should pass through packets unchanged (chunking is now handled by BleWriteDispatcher)', () => {
      const largePayload = new Array(300).fill(0xCC);
      const large: ProtocolResult = {
        packets: [largePayload],
        interPacketDelayMs: 20,
        isRateLimited: true,
      };
      const prepared = adapter.prepareForTransmission(large, 186);
      expect(prepared.packets).toHaveLength(1);
      expect(prepared.packets[0]).toEqual(largePayload);
      expect(prepared.interPacketDelayMs).toBe(20);
    });
  });
});

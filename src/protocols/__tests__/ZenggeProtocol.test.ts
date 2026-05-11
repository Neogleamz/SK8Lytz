import { ZenggeProtocol } from '../ZenggeProtocol';

describe('ZenggeProtocol', () => {
  describe('calculateChecksum', () => {
    it('should correctly sum and mask to 8 bits', () => {
      expect(ZenggeProtocol.calculateChecksum([0x71, 0x23, 0x0f])).toBe(0xa3);
      expect(ZenggeProtocol.calculateChecksum([0xFF, 0x02])).toBe(0x01); // 257 & 0xFF
    });
  });

  describe('wrapCommand', () => {
    it('should correctly build the header envelope', () => {
      // Mock getSequenceCounter to 0 to make test deterministic
      const spy = jest.spyOn(ZenggeProtocol as any, 'getSequenceCounter').mockReturnValue(0);
      
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
      const spy = jest.spyOn(ZenggeProtocol as any, 'getSequenceCounter').mockReturnValue(0);
      const result = ZenggeProtocol.queryHardwareSettings(false);
      
      // raw: [0x63, 0x12, 0x21, 0x0F] -> cs: 0xA5
      expect(result.slice(8)).toEqual([0x63, 0x12, 0x21, 0x0F, 0xA5]);
      spy.mockRestore();
    });
  });

  describe('setMultiColor', () => {
    it('should construct correct 0x59 payload and respect constraints', () => {
      const spy = jest.spyOn(ZenggeProtocol as any, 'getSequenceCounter').mockReturnValue(0);
      
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
});

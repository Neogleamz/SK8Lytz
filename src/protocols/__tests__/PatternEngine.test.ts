import { buildPatternPayload } from '../PatternEngine';

describe('PatternEngine', () => {
  describe('buildPatternPayload', () => {
    const fg = { r: 255, g: 0, b: 0 };
    const bg = { r: 0, g: 255, b: 0 };

    it('should generate a valid payload for a SOLID pattern (ID: 1)', () => {
      const payload = buildPatternPayload(1, fg, bg, 30, 50);
      
      expect(payload).toBeDefined();
      if (!payload) return; // For TypeScript
      
      expect(payload.length).toBeGreaterThan(0);
      
      // Should be wrapped correctly (starts with 0x00 and 0x80 envelope or 0x59 raw depending on if it's chunked)
      // Since it's a SOLID color, it proxies to setMultiColor which wraps it
      expect(payload[0]).toBe(0x00);
      
      // 0x59 inside the wrapper
      expect(payload[8]).toBe(0x59);
    });

    it('should generate a valid payload for a TEST pattern (ID: 201)', () => {
      // Test pattern goes to setCustomModeExtended (0x51 interception)
      const payload = buildPatternPayload(201, fg, bg, 30, 50);
      
      expect(payload).toBeDefined();
      if (!payload) return;
      
      // 323 byte array
      expect(payload.length).toBe(323);
      expect(payload[0]).toBe(0x51);
    });
  });
});

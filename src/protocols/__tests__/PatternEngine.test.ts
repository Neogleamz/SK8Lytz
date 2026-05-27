import { buildPatternPayload } from '../PatternEngine';

describe('PatternEngine', () => {
  describe('buildPatternPayload', () => {
    const fg = { r: 255, g: 0, b: 0 };
    const bg = { r: 0, g: 255, b: 0 };

    it('should generate a valid 0x41 payload for a NATIVE pattern (ID: 1)', () => {
      const payload = buildPatternPayload(1, fg, bg, 30, 50);
      
      expect(payload).toBeDefined();
      if (!payload) return; // For TypeScript
      
      expect(payload.length).toBeGreaterThan(0);
      
      // Since it's a Settled Mode 0x41, it proxies to setSettledMode which wraps it
      expect(payload[0]).toBe(0x00);
      
      // 0x41 is the 9th byte inside the BLE wrapper envelope (index 8)
      expect(payload[8]).toBe(0x41);
    });

    it('should generate a valid 0x59 payload for a STREET pattern (ID: 101)', () => {
      // Test pattern goes to buildMultiColorPayload (0x59 interception)
      const payload = buildPatternPayload(101, fg, bg, 30, 50);
      
      expect(payload).toBeDefined();
      if (!payload) return;
      
      expect(payload.length).toBeGreaterThan(0);
      
      // 0x59 is the 9th byte inside the BLE wrapper envelope (index 8)
      expect(payload[8]).toBe(0x59);
    });
  });
});

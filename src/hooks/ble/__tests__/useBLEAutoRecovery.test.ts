jest.mock('react-native', () => ({
  Platform: { OS: 'android' }
}));

import { getRecoveryBackoffMs, hasExceededMaxRecovery, MAX_RECOVERY_ATTEMPTS } from '../useBLEAutoRecovery';

jest.mock('../../../services/AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
  }
}));

jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn(),
  setItem: jest.fn(),
}));

describe('useBLEAutoRecovery Math Helpers', () => {
  describe('getRecoveryBackoffMs', () => {
    // Formula: round(min(1500 * 1.5^n, 30_000) + random(0, 1500))
    // Output range per attempt: [exponential, exponential + 1500]
    const BASE = 1500;
    const MAX_EXP = 30_000;

    const exponential = (attempt: number) =>
      Math.min(BASE * Math.pow(1.5, attempt), MAX_EXP);

    it('returns a value within the valid jitter range for attempt 0', () => {
      const result = getRecoveryBackoffMs(0);
      const exp = exponential(0); // 1500
      expect(result).toBeGreaterThanOrEqual(Math.round(exp));
      expect(result).toBeLessThanOrEqual(Math.round(exp + BASE));
    });

    it('returns increasing values for attempts 1, 2, 3 (within valid range)', () => {
      for (const attempt of [1, 2, 3]) {
        const result = getRecoveryBackoffMs(attempt);
        const exp = exponential(attempt);
        expect(result).toBeGreaterThanOrEqual(Math.round(exp));
        expect(result).toBeLessThanOrEqual(Math.round(exp + BASE));
      }
    });

    it('caps exponential term at RECOVERY_MAX_MS; total never exceeds MAX + BASE', () => {
      // At attempt 100, exponential saturates at 30_000; jitter adds up to 1500
      for (const attempt of [20, 50, 100, MAX_RECOVERY_ATTEMPTS]) {
        const result = getRecoveryBackoffMs(attempt);
        expect(result).toBeGreaterThanOrEqual(MAX_EXP);
        expect(result).toBeLessThanOrEqual(MAX_EXP + BASE);
      }
    });
  });

  describe('hasExceededMaxRecovery', () => {
    it('returns false for attempts within the allowed limit', () => {
      expect(hasExceededMaxRecovery(0)).toBe(false);
      expect(hasExceededMaxRecovery(100)).toBe(false);
      expect(hasExceededMaxRecovery(MAX_RECOVERY_ATTEMPTS - 1)).toBe(false);
    });

    it('returns false when attempts exactly equals the limit', () => {
      expect(hasExceededMaxRecovery(MAX_RECOVERY_ATTEMPTS)).toBe(false);
    });

    it('returns true when attempts exceed the limit', () => {
      expect(hasExceededMaxRecovery(MAX_RECOVERY_ATTEMPTS + 1)).toBe(true);
      expect(hasExceededMaxRecovery(MAX_RECOVERY_ATTEMPTS + 10)).toBe(true);
    });
  });
});
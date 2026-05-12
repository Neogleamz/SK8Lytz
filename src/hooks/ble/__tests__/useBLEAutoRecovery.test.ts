import { getRecoveryBackoffMs, hasExceededMaxRecovery, MAX_RECOVERY_ATTEMPTS } from '../useBLEAutoRecovery';

describe('useBLEAutoRecovery Math Helpers', () => {
  describe('getRecoveryBackoffMs', () => {
    it('starts at 1500ms on the 0th attempt', () => {
      expect(getRecoveryBackoffMs(0)).toBe(1500);
    });

    it('increments 500ms linearly per attempt', () => {
      expect(getRecoveryBackoffMs(1)).toBe(2000);
      expect(getRecoveryBackoffMs(2)).toBe(2500);
      expect(getRecoveryBackoffMs(3)).toBe(3000);
    });

    it('caps strictly at 5000ms', () => {
      // 1500 + 7 * 500 = 5000
      expect(getRecoveryBackoffMs(7)).toBe(5000);
      expect(getRecoveryBackoffMs(8)).toBe(5000);
      expect(getRecoveryBackoffMs(100)).toBe(5000);
      expect(getRecoveryBackoffMs(MAX_RECOVERY_ATTEMPTS)).toBe(5000);
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

import AsyncStorage from '@react-native-async-storage/async-storage';
import { renderHook, act } from '@testing-library/react-hooks';
import { isStale, normalizeMac, useDeviceStateLedger, warmLedgerCache } from '../useDeviceStateLedger';

jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  getAllKeys: jest.fn(),
  multiGet: jest.fn(),
}));

describe('useDeviceStateLedger', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    // Use fake timers to control the 500ms debounce
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  describe('normalizeMac', () => {
    it('strips composite suffixes (e.g. Supabase user IDs)', () => {
      expect(normalizeMac('AA:BB:CC:DD:EE:FF_user123')).toBe('AA:BB:CC:DD:EE:FF');
    });

    it('forces uppercase letters', () => {
      expect(normalizeMac('aa:bb:cc:dd:ee:ff')).toBe('AA:BB:CC:DD:EE:FF');
    });

    it('passes clean MACs through unchanged', () => {
      expect(normalizeMac('11:22:33:AA:BB:CC')).toBe('11:22:33:AA:BB:CC');
    });
  });

  describe('isStale', () => {
    it('returns false for timestamps under 24 hours', () => {
      const now = Date.now();
      const twelveHoursAgo = now - 12 * 60 * 60 * 1000;
      expect(isStale(twelveHoursAgo)).toBe(false);
    });

    it('returns true for timestamps over 24 hours', () => {
      const now = Date.now();
      const twentyFiveHoursAgo = now - 25 * 60 * 60 * 1000;
      expect(isStale(twentyFiveHoursAgo)).toBe(true);
    });
  });

  describe('save() and loadSync() in-memory caching', () => {
    it('updates in-memory cache immediately so loadSync can read it without awaiting AsyncStorage', () => {
      const { result } = renderHook(() => useDeviceStateLedger());
      
      const mockState = {
        patternIndex: 1,
        mode: 'FIXED',
        speed: 50,
        brightness: 100,
        timestamp: Date.now()
      } as any;

      act(() => {
        result.current.save('AA:BB:CC', mockState);
      });

      // Synchronous read immediately after save should succeed
      const readState = result.current.loadSync('aa:bb:cc_suffix');
      expect(readState).toBeDefined();
      expect(readState?.patternIndex).toBe(1);
      expect(readState?.deviceMac).toBe('AA:BB:CC'); // the key is injected by save()

      // The async storage write should have been queued via setTimeout (500ms debounce)
      expect(AsyncStorage.setItem).not.toHaveBeenCalled();

      // Fast-forward time to trigger the debounced write
      jest.advanceTimersByTime(500);

      expect(AsyncStorage.setItem).toHaveBeenCalledWith(
        '@SK8Lytz_DeviceState_v2_AA:BB:CC',
        expect.any(String)
      );
    });
  });
});

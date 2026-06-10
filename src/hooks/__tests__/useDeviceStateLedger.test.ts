import AsyncStorage from '@react-native-async-storage/async-storage';
(global as any).__DEV__ = true;
import { isStale, normalizeMac, useDeviceStateLedger, warmLedgerCache } from '../useDeviceStateLedger';

jest.mock('react-native', () => ({
  AppState: {
    addEventListener: jest.fn(() => ({ remove: jest.fn() })),
    currentState: 'active',
  },
}));

jest.mock('../../services/AppLogger', () => ({
  AppLogger: {
    log: jest.fn(),
    warn: jest.fn(),
  }
}));

jest.mock('react', () => ({
  ...jest.requireActual('react'),
  useCallback: (fn: any) => fn,
  useEffect: jest.fn((fn: any) => fn()),
}));

jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn().mockResolvedValue(null),
  setItem: jest.fn().mockResolvedValue(undefined),
  removeItem: jest.fn().mockResolvedValue(undefined),
  getAllKeys: jest.fn().mockResolvedValue([]),
  multiGet: jest.fn().mockResolvedValue([]),
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
      const ledger = useDeviceStateLedger();
      
      const mockState = {
        patternId: 1,
        mode: 'FIXED',
        speed: 50,
        brightness: 100,
        timestamp: Date.now()
      } as any;

      ledger.save('AA:BB:CC', mockState);

      // Synchronous read immediately after save should succeed
      const readState = ledger.loadSync('aa:bb:cc_suffix');
      expect(readState).toBeDefined();
      expect(readState?.patternId).toBe(1);
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

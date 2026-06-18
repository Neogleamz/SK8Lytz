import '@react-native-async-storage/async-storage';
import type { GlobalWithDev } from '../../__tests__/test-env';

// Define __DEV__ globally for React Native environment
/* eslint-disable-next-line no-undef */
(global as GlobalWithDev).__DEV__ = true;

jest.mock('@react-native-async-storage/async-storage', () =>
  require('@react-native-async-storage/async-storage/jest/async-storage-mock')
);
jest.mock('expo-battery', () => ({
  isAvailableAsync: jest.fn().mockResolvedValue(false),
  getBatteryLevelAsync: jest.fn().mockResolvedValue(1),
  isLowPowerModeEnabledAsync: jest.fn().mockResolvedValue(false),
  getBatteryStateAsync: jest.fn().mockResolvedValue(0)
}));
jest.mock('expo-device', () => ({
  osInternalBuildId: 'test-device',
  modelId: 'test-model',
  brand: 'TestBrand',
  manufacturer: 'TestMfg'
}));
jest.mock('../supabaseClient', () => ({
  supabase: {
    from: jest.fn().mockReturnValue({
      insert: jest.fn().mockReturnValue({
        then: jest.fn().mockReturnValue({
          catch: jest.fn()
        })
      }),
      remove: jest.fn().mockResolvedValue({})
    }),
    storage: {
      from: jest.fn().mockReturnValue({
        remove: jest.fn().mockResolvedValue({})
      })
    }
  }
}));

const { AppLogger } = require('../appLogger');

describe('AppLogger PII Scrubbing', () => {
  beforeEach(async () => {
    await AppLogger.clearLogs();
  });

  it('redacts mac, deviceId, and peripheral_id fields from logs', async () => {
    const payload = { 
      mac: 'AA:BB:CC:DD:EE:FF', 
      deviceId: 'abc123', 
      peripheral_id: 'per_999',
      message: 'test' 
    };
    
    await AppLogger.log('APP_LOG', payload);

    const logs = await AppLogger.getLogs();
    expect(logs.length).toBeGreaterThan(0);
    const entry = logs[0];

    expect(entry.d.mac).toBe('[REDACTED]');
    expect(entry.d.deviceId).toBe('[REDACTED]');
    expect(entry.d.peripheral_id).toBe('[REDACTED]');
    expect(entry.d.message).toBe('test');
  });
});

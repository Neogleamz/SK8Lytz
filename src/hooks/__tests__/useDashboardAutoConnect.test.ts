/**
 * useDashboardAutoConnect.test.ts
 *
 * Tests the offline group map builder for the auto-connect fallback path.
 * This covers the BUG-1 regression: the offline path must handle BOTH the
 * new many-to-many format (group_ids[]) and the legacy scalar format (group_id)
 * from pre-migration cache rows.
 *
 * Motivation: BUG-1 was a silent offline regression where devices registered
 * via the new Setup Wizard had group_ids[] only — the old scalar group_id
 * was undefined — causing the offline fallback to produce an empty group map,
 * silently skipping auto-connect. This test suite locks that path down.
 */

(global as any).__DEV__ = true;

// ── Module mocks — all native/expo deps pulled transitively by the hook ──────
jest.mock('@react-native-async-storage/async-storage', () => ({
  getItem: jest.fn().mockResolvedValue(null),
  setItem: jest.fn().mockResolvedValue(undefined),
}));

jest.mock('../../services/AppLogger', () => ({
  AppLogger: { log: jest.fn(), warn: jest.fn(), error: jest.fn() },
  getAppLogger: () => ({ log: jest.fn(), warn: jest.fn(), error: jest.fn() }),
}));

jest.mock('expo-linking', () => ({
  addEventListener: jest.fn(() => ({ remove: jest.fn() })),
  getInitialURL: jest.fn().mockResolvedValue(null),
}));

jest.mock('expo-secure-store', () => ({
  getItemAsync: jest.fn().mockResolvedValue(null),
  setItemAsync: jest.fn().mockResolvedValue(null),
  deleteItemAsync: jest.fn().mockResolvedValue(null),
}));

jest.mock('react-native', () => ({
  Platform: { OS: 'ios' },
  DeviceEventEmitter: { addListener: jest.fn() },
  PermissionsAndroid: { request: jest.fn(), check: jest.fn(), PERMISSIONS: {} },
  NativeModules: {},
}));

jest.mock('../../services/supabaseClient', () => ({
  supabase: { auth: { getUser: jest.fn().mockResolvedValue({ data: { user: null } }) } },
}));

jest.mock('react', () => ({
  ...jest.requireActual('react'),
  useEffect: jest.fn(),
  useRef: jest.fn(() => ({ current: null })),
}));

import { buildOfflineGroupMap } from '../useDashboardAutoConnect';
import type { RegisteredDevice } from '../useRegistration';

// ── Helpers ─────────────────────────────────────────────────────────────────

const makeDevice = (overrides: Partial<RegisteredDevice> & { device_mac: string }): RegisteredDevice => ({
  device_name: 'Test Device',
  product_type: 'HALOZ',
  position: null,
  ...overrides,
});

// ── Tests ────────────────────────────────────────────────────────────────────

describe('buildOfflineGroupMap', () => {
  it('returns empty array when given an empty device list', () => {
    const result = buildOfflineGroupMap([]);
    expect(result).toHaveLength(0);
  });

  it('correctly maps new-format devices (group_ids array)', () => {
    const devices: RegisteredDevice[] = [
      makeDevice({
        device_mac: 'AA:BB:CC:DD:EE:01',
        group_ids: ['my-sk8lytz-haloz'],
        group_names: ['My SK8Lytz HALOZ'],
      }),
      makeDevice({
        device_mac: 'AA:BB:CC:DD:EE:02',
        group_ids: ['my-sk8lytz-haloz'],
        group_names: ['My SK8Lytz HALOZ'],
      }),
    ];

    const result = buildOfflineGroupMap(devices);

    expect(result).toHaveLength(1);
    expect(result[0].id).toBe('my-sk8lytz-haloz');
    expect(result[0].group_name).toBe('My SK8Lytz HALOZ');
    expect(result[0].deviceIds).toEqual(['AA:BB:CC:DD:EE:01', 'AA:BB:CC:DD:EE:02']);
  });

  it('correctly maps legacy-format devices (scalar group_id — MIGRATION-SHIM)', () => {
    // Simulates pre-migration cache rows that only have the scalar group_id field
    const devices: RegisteredDevice[] = [
      makeDevice({
        device_mac: 'AA:BB:CC:DD:EE:03',
        group_id: 'legacy-fleet',
        group_name: 'Legacy Fleet',
        // group_ids and group_names are deliberately absent
      }),
    ];

    const result = buildOfflineGroupMap(devices);

    expect(result).toHaveLength(1);
    expect(result[0].id).toBe('legacy-fleet');
    expect(result[0].group_name).toBe('Legacy Fleet');
    expect(result[0].deviceIds).toContain('AA:BB:CC:DD:EE:03');
  });

  it('produces the same result from both scalar and array inputs for the same group', () => {
    const newFormatDevice = makeDevice({
      device_mac: 'AA:BB:CC:DD:EE:04',
      group_ids: ['fleet-alpha'],
      group_names: ['Fleet Alpha'],
    });

    const legacyDevice = makeDevice({
      device_mac: 'AA:BB:CC:DD:EE:05',
      group_id: 'fleet-alpha',
      group_name: 'Fleet Alpha',
    });

    const resultNew    = buildOfflineGroupMap([newFormatDevice]);
    const resultLegacy = buildOfflineGroupMap([legacyDevice]);

    expect(resultNew[0].id).toBe(resultLegacy[0].id);
    expect(resultNew[0].group_name).toBe(resultLegacy[0].group_name);
  });

  it('handles a device in multiple groups (many-to-many)', () => {
    const devices: RegisteredDevice[] = [
      makeDevice({
        device_mac: 'AA:BB:CC:DD:EE:06',
        group_ids: ['crew-alpha', 'crew-beta'],
        group_names: ['Crew Alpha', 'Crew Beta'],
      }),
    ];

    const result = buildOfflineGroupMap(devices);

    expect(result).toHaveLength(2);
    const ids = result.map(g => g.id);
    expect(ids).toContain('crew-alpha');
    expect(ids).toContain('crew-beta');
    result.forEach(g => expect(g.deviceIds).toContain('AA:BB:CC:DD:EE:06'));
  });

  it('excludes devices assigned to default-fleet (they have no real group)', () => {
    const devices: RegisteredDevice[] = [
      makeDevice({
        device_mac: 'AA:BB:CC:DD:EE:07',
        group_ids: ['default-fleet'],
        group_names: ['Default Fleet'],
      }),
    ];

    const result = buildOfflineGroupMap(devices);
    expect(result).toHaveLength(0);
  });

  it('does not duplicate a device already in the group', () => {
    const devices: RegisteredDevice[] = [
      makeDevice({ device_mac: 'AA:BB:CC:DD:EE:08', group_ids: ['my-fleet'], group_names: ['My Fleet'] }),
      makeDevice({ device_mac: 'AA:BB:CC:DD:EE:08', group_ids: ['my-fleet'], group_names: ['My Fleet'] }), // duplicate
    ];

    const result = buildOfflineGroupMap(devices);

    expect(result).toHaveLength(1);
    expect(result[0].deviceIds).toHaveLength(1);
  });

  it('handles mixed legacy and new-format devices in the same group', () => {
    const devices: RegisteredDevice[] = [
      makeDevice({
        device_mac: 'AA:BB:CC:DD:EE:09',
        group_ids: ['crew-mix'],
        group_names: ['Crew Mix'],
      }),
      makeDevice({
        device_mac: 'AA:BB:CC:DD:EE:10',
        group_id: 'crew-mix',
        group_name: 'Crew Mix',
      }),
    ];

    const result = buildOfflineGroupMap(devices);

    expect(result).toHaveLength(1);
    expect(result[0].deviceIds).toContain('AA:BB:CC:DD:EE:09');
    expect(result[0].deviceIds).toContain('AA:BB:CC:DD:EE:10');
  });
});

/**
 * GroupRepository.test.ts
 *
 * Comprehensive unit test suite for the newly extracted GroupRepository singleton.
 * Covers initialization, local group CRUD operations, dynamic delegate updates,
 * offline queue caching, and Supabase synchronization logic.
 */

(global as any).__DEV__ = true;

import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../supabaseClient';
import GroupRepository, { GroupDeviceDelegate } from '../GroupRepository';
import type { RegisteredDevice } from '../../hooks/useRegistration';
import type { CustomGroup, DeviceSettings } from '../../types/dashboard.types';

// ── Mock Dependencies ────────────────────────────────────────────────────────
jest.mock('@react-native-async-storage/async-storage', () => {
  const store: Record<string, string> = {};
  return {
    getItem: jest.fn(async (key: string) => store[key] || null),
    setItem: jest.fn(async (key: string, val: string) => {
      store[key] = val;
    }),
    removeItem: jest.fn(async (key: string) => {
      delete store[key];
    }),
    clear: jest.fn(async () => {
      for (const k of Object.keys(store)) delete store[k];
    }),
  };
});

jest.mock('../AppLogger', () => ({
  AppLogger: { log: jest.fn(), warn: jest.fn(), error: jest.fn() },
}));

jest.mock('../supabaseClient', () => {
  const rpcMock = jest.fn().mockResolvedValue({ data: null, error: null });
  const fromMock = jest.fn().mockReturnValue({
    upsert: jest.fn().mockResolvedValue({ error: null }),
    update: jest.fn().mockReturnValue({
      in: jest.fn().mockResolvedValue({ error: null }),
    }),
    delete: jest.fn().mockReturnValue({
      eq: jest.fn().mockReturnValue({
        eq: jest.fn().mockResolvedValue({ error: null }),
      }),
    }),
  });

  return {
    supabase: {
      auth: {
        getUser: jest.fn().mockResolvedValue({ data: { user: { id: 'test-user-id' } } }),
        getSession: jest.fn().mockResolvedValue({ data: { session: { user: { id: 'test-user-id' } } } }),
      },
      rpc: rpcMock,
      from: fromMock,
    },
  };
});

describe('GroupRepository', () => {
  let repository: GroupRepository;
  let mockDelegate: jest.Mocked<GroupDeviceDelegate>;
  let mockDevices: RegisteredDevice[];

  beforeEach(async () => {
    jest.clearAllMocks();
    await AsyncStorage.clear();

    repository = GroupRepository.getInstance();

    mockDevices = [
      {
        id: 'dev-1',
        device_mac: '08:65:F0:9A:C2:3C',
        device_name: 'Haloz Left',
        product_type: 'HALOZ',
        position: 'Left',
        group_ids: ['default-fleet'],
        group_names: ['Default Fleet'],
      },
      {
        id: 'dev-2',
        device_mac: '08:65:F0:9A:5E:06',
        device_name: 'Haloz Right',
        product_type: 'HALOZ',
        position: 'Right',
        group_ids: ['default-fleet'],
        group_names: ['Default Fleet'],
      },
    ];

    mockDelegate = {
      getCurrentDevices: jest.fn(() => mockDevices),
      updateDevicesInBulk: jest.fn(async (updated) => {
        mockDevices = updated;
      }),
      notifySubscribers: jest.fn(),
      getCurrentConfigs: jest.fn(() => ({})),
      updateConfigsInBulk: jest.fn(async (configs: Record<string, DeviceSettings>) => {}),
    };

    repository.setDeviceDelegate(mockDelegate);

    // Reset private state after delegate injection
    await repository.setGroups([]);

    // Clear delegate notifications triggered during boot reset
    mockDelegate.notifySubscribers.mockClear();
  });

  describe('Initialization', () => {
    it('successfully loads cached groups from storage', async () => {
      const cachedGroups: CustomGroup[] = [
        { id: 'group-1', name: 'Elite Crew', isGroup: true, deviceIds: ['08:65:F0:9A:C2:3C'] },
      ];
      await AsyncStorage.setItem('@Sk8lytz_custom_groups', JSON.stringify(cachedGroups));

      // Re-initialize a fresh init state
      (repository as any).isInitialized = false;
      (repository as any).initPromise = null;
      await repository.initialize();

      expect(repository.getGroups()).toEqual(cachedGroups);
    });
  });

  describe('Local CRUD Mutations', () => {
    it('sets groups and triggers notifications', async () => {
      const newGroups: CustomGroup[] = [
        { id: 'group-2', name: 'Street Griders', isGroup: true, deviceIds: [] },
      ];

      await repository.setGroups(newGroups);

      expect(repository.getGroups()).toEqual(newGroups);
      expect(mockDelegate.notifySubscribers).toHaveBeenCalledTimes(1);
      const stored = await AsyncStorage.getItem('@Sk8lytz_custom_groups');
      expect(JSON.parse(stored!)).toEqual(newGroups);
    });

    it('deletes a group and updates mapped devices through the delegate', async () => {
      const initialGroups: CustomGroup[] = [
        { id: 'group-to-delete', name: 'Obsolete Group', isGroup: true, deviceIds: ['08:65:F0:9A:C2:3C'] },
      ];
      await repository.setGroups(initialGroups);

      // Setup device in that group
      mockDevices[0].group_ids = ['group-to-delete'];
      mockDevices[0].group_names = ['Obsolete Group'];

      await repository.deleteGroup('group-to-delete');

      // Verify group removed
      expect(repository.getGroups()).toHaveLength(0);

      // Verify device group cleared back to default-fleet
      expect(mockDelegate.updateDevicesInBulk).toHaveBeenCalled();
      const updatedFirstDevice = mockDelegate.updateDevicesInBulk.mock.calls[0][0][0];
      expect(updatedFirstDevice.group_ids).toEqual(['default-fleet']);
      expect(updatedFirstDevice.group_names).toEqual(['Default Fleet']);

      // Verify delegate notification
      expect(mockDelegate.notifySubscribers).toHaveBeenCalled();

      // Verify Supabase delete called
      expect(supabase.rpc).toHaveBeenCalledWith('delete_group_cascade', {
        p_group_id: 'group-to-delete',
      });
    });

    it('performs transactional save and updates bi-directional references', async () => {
      const deviceMacs = ['08:65:F0:9A:C2:3C', '08:65:F0:9A:5E:06'];

      const success = await repository.saveGroupTransactional('new-group-id', 'Neogleamz Fleet', deviceMacs);

      expect(success).toBe(true);

      // Verify group in-memory state
      const groups = repository.getGroups();
      expect(groups).toHaveLength(1);
      expect(groups[0].id).toBe('new-group-id');
      expect(groups[0].name).toBe('Neogleamz Fleet');
      expect(groups[0].deviceIds).toEqual(deviceMacs);

      // Verify device objects got updated with the new group
      expect(mockDelegate.updateDevicesInBulk).toHaveBeenCalled();
      const updatedDevicesList = mockDelegate.updateDevicesInBulk.mock.calls[0][0];
      expect(updatedDevicesList[0].group_ids).toContain('new-group-id');
      expect(updatedDevicesList[1].group_ids).toContain('new-group-id');

      // Verify Supabase RPC upsert called
      expect(supabase.rpc).toHaveBeenCalledWith('upsert_group_with_devices', {
        p_group_id: 'new-group-id',
        p_group_name: 'Neogleamz Fleet',
        p_type: 'device-fleet',
        p_device_ids: ['dev-1', 'dev-2'],
      });
    });
  });

  describe('Offline Queue & Synchronization', () => {
    it('queues group sync when user is offline or RPC fails', async () => {
      // Force auth getUser to return null to simulate offline/guest mode
      (supabase.auth.getUser as jest.Mock).mockResolvedValueOnce({ data: { user: null } });

      const deviceMacs = ['08:65:F0:9A:C2:3C'];
      const success = await repository.saveGroupTransactional('offline-group', 'Offline Group', deviceMacs);

      // Should succeed locally and return true
      expect(success).toBe(true);

      // Verify entry added to pending offline sync queue
      const queuedRaw = await AsyncStorage.getItem('@Sk8lytz_pending_group_sync');
      expect(queuedRaw).not.toBeNull();
      const queue = JSON.parse(queuedRaw!);
      expect(queue).toHaveLength(1);
      expect(queue[0].groupId).toBe('offline-group');
      expect(queue[0].deviceMacs).toEqual(deviceMacs);
    });

    it('flushes pending groups sequentially when triggered', async () => {
      const offlineEntry = {
        groupId: 'queued-group',
        groupName: 'Queued Group',
        deviceMacs: ['08:65:F0:9A:C2:3C'],
        type: 'device-fleet',
      };
      await AsyncStorage.setItem('@Sk8lytz_pending_group_sync', JSON.stringify([offlineEntry]));

      await repository.flushPendingGroups('test-user-id', mockDevices);

      // Verify RPC fired with queued params
      expect(supabase.rpc).toHaveBeenCalledWith('upsert_group_with_devices', {
        p_group_id: 'queued-group',
        p_group_name: 'Queued Group',
        p_type: 'device-fleet',
        p_device_ids: ['dev-1'],
      });

      // Verify pending queue cleared
      const queuedRaw = await AsyncStorage.getItem('@Sk8lytz_pending_group_sync');
      expect(queuedRaw).toBeNull();
    });
  });
});

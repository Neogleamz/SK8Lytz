import { STORAGE_CUSTOM_GROUPS, STORAGE_PENDING_GROUP_SYNC } from '../constants/storageKeys';
/**
 * GroupRepository.ts — Single Source of Truth for Custom Group Persistence & Sync
 *
 * Extracted from DeviceRepository.ts (Phase 3.5 — Tech Debt cleanup).
 *
 * This singleton service owns all group-related persistence:
 *   - AsyncStorage reads/writes (local-first)
 *   - Supabase sync (cloud second via junction table RPC)
 *   - Group-to-device mapping updates delegated to DeviceRepository.
 *
 * Circular imports are prevented by injecting a delegate dynamically at boot time.
 */

import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './appLogger';
import { supabase } from './supabaseClient';
import type { RegisteredDevice } from './deviceRepository/types';
import type { CustomGroup, DeviceSettings } from '../types/dashboard.types';
import type { TablesInsert } from '../types/supabase';

type GroupInsert = Omit<TablesInsert<'registered_groups'>, 'created_at'>;

const GROUPS_KEY = STORAGE_CUSTOM_GROUPS;
const PENDING_GROUP_KEY = STORAGE_PENDING_GROUP_SYNC;

/**
 * Delegate interface implemented by DeviceRepository to decouple
 * bi-directional state synchronization and notify React subscribers.
 */
export interface GroupDeviceDelegate {
  getCurrentDevices: () => RegisteredDevice[];
  updateDevicesInBulk: (updated: RegisteredDevice[]) => Promise<void>;
  notifySubscribers: () => void;
  updateConfigsInBulk: (configs: Record<string, DeviceSettings>) => Promise<void>;
  getCurrentConfigs: () => Record<string, DeviceSettings>;
}

class GroupRepository {
  private static instance: GroupRepository;

  private groups: CustomGroup[] = [];
  private isInitialized = false;
  private initPromise: Promise<void> | null = null;
  private delegate: GroupDeviceDelegate | null = null;
  private groupListeners: Set<() => void> = new Set();

  private constructor() {}

  static getInstance(): GroupRepository {
    if (!GroupRepository.instance) {
      GroupRepository.instance = new GroupRepository();
    }
    return GroupRepository.instance;
  }

  /**
   * Inject the DeviceRepository delegate at runtime to avoid Metro require loops.
   */
  setDeviceDelegate(delegate: GroupDeviceDelegate): void {
    this.delegate = delegate;
  }

  private getDelegate(): GroupDeviceDelegate {
    if (!this.delegate) {
      throw new Error('[GroupRepository] GroupDeviceDelegate was not injected prior to usage.');
    }
    return this.delegate;
  }

  subscribeGroups(cb: () => void): () => void {
    this.groupListeners.add(cb);
    return () => this.groupListeners.delete(cb);
  }

  private notifyGroupSubscribers(): void {
    this.groupListeners.forEach((cb) => cb());
  }

  async initialize(): Promise<void> {
    if (this.isInitialized) return;
    if (this.initPromise) return this.initPromise;

    this.initPromise = this._loadFromStorage();
    await this.initPromise;
    this.isInitialized = true;
  }

  private async _loadFromStorage(): Promise<void> {
    try {
      const groupsRaw = await AsyncStorage.getItem(GROUPS_KEY);
      if (groupsRaw) {
        try {
          this.groups = JSON.parse(groupsRaw);
        } catch {
          this.groups = [];
        }
      }
      AppLogger.warn('[GroupRepository] initialized', { event: 'initialized',
        groupCount: this.groups.length, payload_size: 0, ssi: 0
      });
    } catch (e: unknown) {
      AppLogger.warn('[GroupRepository] Storage load failed:', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
    }
  }

  // ─── Read API (C14: R-21 consolidation) ────────────────────────────────────
  // ALL group reads must flow through these methods. No consumer should read
  // AsyncStorage directly for groups — subscribe via subscribeGroups() instead.

  /** Returns a defensive copy of all in-memory groups. */
  getGroups(): CustomGroup[] {
    return [...this.groups];
  }

  /** Returns the group matching the given ID, or undefined if not found. */
  getGroupById(groupId: string): CustomGroup | undefined {
    return this.groups.find(g => g.id === groupId);
  }

  /** Case-insensitive lookup by group name. */
  getGroupByName(name: string): CustomGroup | undefined {
    return this.groups.find(g => g.name.toLowerCase() === name.toLowerCase());
  }

  /** Returns all groups that include the given MAC address (normalized to uppercase). */
  getGroupsByDeviceId(mac: string): CustomGroup[] {
    const normalizedMac = mac.toUpperCase();
    return this.groups.filter(g => g.deviceIds?.map(m => m.toUpperCase()).includes(normalizedMac));
  }

  async setGroups(groups: CustomGroup[]): Promise<void> {
    this.groups = groups;
    await AsyncStorage.setItem(GROUPS_KEY, JSON.stringify(this.groups)).catch((e) =>
      AppLogger.warn('[GroupRepository] AsyncStorage write failed', { key: 'GROUPS_KEY/setGroups', error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0  })
    );
    this.getDelegate().notifySubscribers();
    this.notifyGroupSubscribers();
  }

  /**
   * Remove a group by ID from local state and cloud.
   */
  async deleteGroup(groupId: string, userId?: string): Promise<void> {
    // 1. Remove group row
    this.groups = this.groups.filter((g) => g.id !== groupId);
    await AsyncStorage.setItem(GROUPS_KEY, JSON.stringify(this.groups)).catch((e) =>
      AppLogger.warn('[GroupRepository] AsyncStorage write failed', { key: 'GROUPS_KEY/deleteGroup', error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0  })
    );

    // 2. Clear group assignments from in-memory devices via delegate
    const delegate = this.getDelegate();
    let devices = delegate.getCurrentDevices();
    let devicesChanged = false;

    const updatedDevices = devices.map((d) => {
      if (d.group_ids?.includes(groupId)) {
        devicesChanged = true;
        let newIds = d.group_ids.filter((id) => id !== groupId);
        let newNames = (d.group_names || []).filter((n) => n !== groupId);
        if (newIds.length === 0) {
          newIds = ['default-fleet'];
          newNames = ['Default Fleet'];
        }
        return { ...d, group_ids: newIds, group_names: newNames };
      }
      return d;
    });

    if (devicesChanged) {
      await delegate.updateDevicesInBulk(updatedDevices);
    }

    // 3. Notify subscribers
    delegate.notifySubscribers();
    this.notifyGroupSubscribers();

    // 4. Cloud cleanup via atomic RPC
    try {
      const { error } = await supabase.rpc('delete_group_cascade', {
        p_group_id: groupId,
      });
      if (error) throw error;
    } catch (e: unknown) {
      AppLogger.warn('[GroupRepository] delete_group_cascade RPC failed, falling back:', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0  });
      try {
        if (userId) {
          await supabase.from('registered_groups').delete()
            .eq('id', groupId)
            .eq('user_id', userId);
        }
      } catch (fe: unknown) {
        AppLogger.warn('[GroupRepository] Cloud group delete fallback failed:', { error: fe instanceof Error ? fe.message : String(fe), payload_size: 0, ssi: 0 });
      }
    }
  }

  /**
   * Transactional group save: atomically upserts the group and bulk-assigns
   * device membership.
   */
  async saveGroupTransactional(
    groupId: string,
    groupName: string,
    deviceMacs: string[],
    type = 'device-fleet',
    userId?: string
  ): Promise<boolean> {
    // 1. Update local group state immediately (optimistic)
    const existingIdx = this.groups.findIndex((g) => g.id === groupId);
    const updatedGroup: CustomGroup =
      existingIdx >= 0
        ? { ...this.groups[existingIdx], id: groupId, name: groupName, deviceIds: deviceMacs }
        : { id: groupId, name: groupName, isGroup: true, deviceIds: deviceMacs };

    if (existingIdx >= 0) this.groups[existingIdx] = updatedGroup;
    else this.groups.push(updatedGroup);
    await AsyncStorage.setItem(GROUPS_KEY, JSON.stringify(this.groups)).catch((e) =>
      AppLogger.warn('[GroupRepository] AsyncStorage write failed', { key: 'GROUPS_KEY/saveGroupTransactional', error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0  })
    );

    const delegate = this.getDelegate();
    let devices = delegate.getCurrentDevices();
    const normalizedMacs = deviceMacs.map((m) => m.toUpperCase());
    let devicesChanged = false;
    const dbDeviceIds: string[] = [];

    const updatedDevices = devices.map((d) => {
      const mac = d.device_mac.toUpperCase();
      const currentIds: string[] = d.group_ids ?? (d.group_id ? [d.group_id] : []);
      const currentNames: string[] = d.group_names ?? (d.group_name ? [d.group_name] : []);

      if (normalizedMacs.includes(mac)) {
        if (d.id) dbDeviceIds.push(d.id);
        if (!currentIds.includes(groupId)) {
          devicesChanged = true;
          const newIds = currentIds.filter((id) => id !== 'default-fleet').concat(groupId);
          const newNames = currentNames.filter((n) => n !== 'Default Fleet').concat(groupName);
          return { ...d, group_ids: newIds, group_names: newNames };
        }
      } else if (currentIds.includes(groupId)) {
        devicesChanged = true;
        let newIds = currentIds.filter((id) => id !== groupId);
        let newNames = currentNames.filter((n) => n !== groupName);
        if (newIds.length === 0) {
          newIds = ['default-fleet'];
          newNames = ['Default Fleet'];
        }
        return { ...d, group_ids: newIds, group_names: newNames };
      }
      return d;
    });

    if (devicesChanged) {
      await delegate.updateDevicesInBulk(updatedDevices);
    }
    delegate.notifySubscribers();
    this.notifyGroupSubscribers();

    // 2. Cloud: atomic RPC transaction
    try {
      const hasPendingDevices = updatedDevices.some(
        (d) => normalizedMacs.includes(d.device_mac.toUpperCase()) && d.is_pending_sync
      );

      if (!userId || hasPendingDevices) {
        await this._queuePendingGroupSync(groupId, groupName, deviceMacs, type);
        AppLogger.warn('[GroupRepository] Offline or Pending Devices — group queued for sync', { groupId,
          hasPendingDevices, payload_size: 0, ssi: 0
        });
        return true;
      }

      const { error } = await supabase.rpc('upsert_group_with_devices', {
        p_group_id: groupId,
        p_group_name: groupName,
        p_type: type,
        p_device_ids: dbDeviceIds,
      });
      if (error) throw error;

      if (dbDeviceIds.length > 0) {
        (async () => {
          try {
            const { error: updateErr } = await supabase
              .from('registered_devices')
              .update({ group_id: groupId, group_name: groupName })
              .in('id', dbDeviceIds);
            if (updateErr) {
              AppLogger.warn('[GroupRepository] group_id stamp failed (non-fatal)', {
                error: updateErr.message, payload_size: 0, ssi: 0
              });
            }
          } catch (e: unknown) {
            AppLogger.warn('[GroupRepository] group_id stamp network error', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
          }
        })();
      }

      return true;
    } catch (e: unknown) {
      AppLogger.warn('[GroupRepository] saveGroupTransactional RPC failed, queuing:', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0  });
      await this._queuePendingGroupSync(groupId, groupName, deviceMacs, type);
      return false;
    }
  }

  private async _queuePendingGroupSync(
    groupId: string,
    groupName: string,
    deviceMacs: string[],
    type: string
  ): Promise<void> {
    try {
      const raw = await AsyncStorage.getItem(PENDING_GROUP_KEY);
      const queue: Array<{ groupId: string; groupName: string; deviceMacs: string[]; type: string }> = raw
        ? JSON.parse(raw)
        : [];
      const idx = queue.findIndex((q) => q.groupId === groupId);
      const entry = { groupId, groupName, deviceMacs, type };
      if (idx >= 0) queue[idx] = entry;
      else queue.push(entry);
      await AsyncStorage.setItem(PENDING_GROUP_KEY, JSON.stringify(queue));
    } catch (e: unknown) {
      AppLogger.warn('[GroupRepository] Group queue failed:', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0  });
    }
  }

  /**
   * Flushes pending offline groups. Called dynamically from DeviceRepository during syncFromCloud.
   */
  async flushPendingGroups(userId: string, currentDevices: RegisteredDevice[]): Promise<void> {
    try {
      const raw = await AsyncStorage.getItem(PENDING_GROUP_KEY);
      if (!raw) return;
      const queue: Array<{ groupId: string; groupName: string; deviceMacs: string[]; type: string }> = JSON.parse(raw);
      if (queue.length === 0) return;

      for (const entry of queue) {
        const dbDeviceIds = currentDevices
          .filter((d) => entry.deviceMacs.map((m) => m.toUpperCase()).includes(d.device_mac.toUpperCase()))
          .map((d) => d.id)
          .filter(Boolean) as string[];

        try {
          const { error } = await supabase.rpc('upsert_group_with_devices', {
            p_group_id: entry.groupId,
            p_group_name: entry.groupName,
            p_type: entry.type,
            p_device_ids: dbDeviceIds,
          });
          if (error) throw error;
        } catch (rpcErr: unknown) {
          AppLogger.warn('[GroupRepository] Group flush RPC failed, fallback:', { error: rpcErr instanceof Error ? rpcErr.message : String(rpcErr), payload_size: 0, ssi: 0 });
          try {
            await supabase.from('registered_groups').upsert({
              id: entry.groupId,
              group_name: entry.groupName,
              type: entry.type,
              user_id: userId,
            } satisfies GroupInsert, { onConflict: 'id' });
          } catch (fbErr: unknown) {
            AppLogger.warn('[GroupRepository] Group flush fallback failed:', { error: fbErr instanceof Error ? fbErr.message : String(fbErr), payload_size: 0, ssi: 0 });
          }
        }
      }

      await AsyncStorage.removeItem(PENDING_GROUP_KEY);
      AppLogger.warn('[GroupRepository] Pending group sync flushed', { count: queue.length, payload_size: 0, ssi: 0 });
    } catch (e: unknown) {
      AppLogger.warn('[GroupRepository] Group flush failed:', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0  });
    }
  }
}

export default GroupRepository;

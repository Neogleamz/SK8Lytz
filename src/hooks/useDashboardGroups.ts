/**
 * useDashboardGroups.ts — Dashboard Device Fleet & Group Management Domain Hook
 *
 * Owns all device group state, device configs persistence, power state map,
 * last-group-pattern tracking, and the group modal FSM.
 *
 * Receives BLE/Registration primitives from DashboardScreen as options
 * since BLE state is intentionally co-located in DashboardScreen per
 * the Master Reference constraint.
 *
 * Extracted from DashboardScreen.tsx (Phase 1 — Domain-Driven Refactor).
 *
 * Depends on: AsyncStorage (UI-local patterns only), useRegistration (via options), custom types
 */
import React, { useEffect, useRef, useState } from 'react';
import { Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

import type { RegisteredDevice } from '../hooks/useRegistration';
import { AppLogger } from '../services/AppLogger';
import DeviceRepository from '../services/DeviceRepository';
// NOTE: Direct supabase import removed — all cloud writes go through DeviceRepository SSOT.
import type { CustomGroup, DeviceSettings, GroupModalState, GroupPatternSnapshot } from '../types/dashboard.types';


interface UseDashboardGroupsOptions {
  registeredDevices: RegisteredDevice[];
  /** Injected from DashboardScreen's useRegistration hook.
   * Saves the full device list to cloud + local after FTUE wizard.
   * Return value (boolean | void) is intentionally discarded.
   */
  saveAllRegisteredDevices: (devices: RegisteredDevice[]) => Promise<boolean | void>;
  /** Saves a single registered device to cloud + local. Used by group CRUD. */
  saveRegisteredDevice: (device: any) => Promise<any>;
  migrateLegacyGroups: (allDevices: any[], deviceConfigs: Record<string, DeviceSettings>) => Promise<RegisteredDevice[]>;
  clearPendingRegistrations: () => void;
  /** Called after FTUE setup completes to hide the SetupWizard. */
  onRegistrationComplete: () => void;
  /** Provides real-time access to BLE-scanned devices for provisioning. */
  getAllScannedDevices: () => any[];
  /** Mirror config-back into useBLE's allDevices state after provisioning. */
  setAllDevices: (updater: (prev: any[]) => any[]) => void;
  /** Ref that mirrors allDevices for stale-closure-safe access inside callbacks. */
  allDevicesRef: React.MutableRefObject<any[]>;
  deregisterDevice: (mac: string) => Promise<void>;
}

export interface UseDashboardGroupsResult {
  customGroups: CustomGroup[];
  setCustomGroups: React.Dispatch<React.SetStateAction<CustomGroup[]>>;
  customGroupsRef: React.MutableRefObject<CustomGroup[]>;
  deviceConfigs: Record<string, DeviceSettings>;
  setDeviceConfigs: React.Dispatch<React.SetStateAction<Record<string, DeviceSettings>>>;
  powerStates: Record<string, boolean>;
  setPowerState: (deviceIds: string[], forceState?: boolean) => void;
  lastGroupPatterns: Record<string, GroupPatternSnapshot>;
  setLastGroupPattern: (groupId: string, snapshot: GroupPatternSnapshot) => Promise<void>;
  // ─── Group modal FSM ──────────────────────────────────────────────────────
  groupModalState: GroupModalState;
  editingGroupId: string | null;
  openGroupCreate: () => void;
  openGroupRename: (groupId: string) => void;
  closeGroupModal: () => void;
  // ─── Selection mode ────────────────────────────────────────────────────────
  selectedIds: string[];
  isSelectionMode: boolean;
  toggleDeviceSelection: (id: string) => void;
  clearSelection: () => void;
  // ─── Collapse toggles ─────────────────────────────────────────────────────
  isDeviceListCollapsed: boolean;
  setIsDeviceListCollapsed: (v: boolean) => void;
  isRegisteredCollapsed: boolean;
  setIsRegisteredCollapsed: (v: boolean) => void;
  // ─── FTUE registration handler ────────────────────────────────────────────
  handleRegistrationComplete: (devices: RegisteredDevice[], allBleDevices: any[]) => Promise<void>;
  // ─── Provisioning & CRUD (Phase 2–3 migration) ────────────────────────────
  saveGroup: (name: string, deviceIds: string[]) => Promise<void>;
  handleGroupDelete: (id: string) => Promise<void>;
}

export function useDashboardGroups({
  registeredDevices,
  saveAllRegisteredDevices,
  saveRegisteredDevice: _saveRegisteredDevice,
  migrateLegacyGroups: _migrateLegacyGroups,
  clearPendingRegistrations,
  onRegistrationComplete,
  getAllScannedDevices: _getAllScannedDevices,
  setAllDevices: _setAllDevices,
  allDevicesRef: _allDevicesRef,
  deregisterDevice,
}: UseDashboardGroupsOptions): UseDashboardGroupsResult {

  const repo = DeviceRepository.getInstance();

  // ─── Group state (derived from useRegistration, kept as display SSOT) ─────
  const [customGroups, setCustomGroups] = useState<CustomGroup[]>([]);
  /** Stable ref so callbacks (e.g. handleRegistrationComplete) always read current groups. */
  const customGroupsRef = useRef<CustomGroup[]>([]);
  useEffect(() => { customGroupsRef.current = customGroups; }, [customGroups]);

  // Derive groups and hardware configs from registeredDevices whenever cloud sync updates them.
  // INVARIANT: deviceIds always contains UPPERCASE MACs — matching BLE d.id.toUpperCase().
  //
  // CRITICAL async-trap fix: setDeviceConfigs uses a functional updater that React runs
  // asynchronously during reconciliation. groupMap must be populated BEFORE the updater
  // fires — so we iterate registeredDevices TWICE:
  //   Pass 1 (sync): Build groupMap → setCustomGroups (always has data)
  //   Pass 2 (async functional updater): Merge hardware config fields → setDeviceConfigs
  useEffect(() => {
    // ── Pass 1: Group derivation (synchronous) ───────────────────────────────
    const groupMap: Record<string, CustomGroup> = {};
    registeredDevices.forEach(rd => {
      const mac = rd.device_mac.toUpperCase();
      // MIGRATION-SHIM: Remove after all devices re-registered via wizard (target: v3.9.0)
      // New devices use group_ids[]. Legacy cached devices may still have scalar group_id.
      const ids: string[] = rd.group_ids ?? (rd.group_id ? [rd.group_id] : []);
      const names: string[] = rd.group_names ?? (rd.group_name ? [rd.group_name] : []);
      
      ids.forEach((gId, idx) => {
        const gName = names[idx] || gId;
        if (gId && gName && gId !== 'default-fleet') {
          if (!groupMap[gId]) {
            groupMap[gId] = { id: gId, name: gName, isGroup: true, deviceIds: [] };
          }
          if (!groupMap[gId].deviceIds.includes(mac)) {
            groupMap[gId].deviceIds.push(mac);
          }
        }
      });
    });
    // setCustomGroups is called synchronously — groupMap is fully populated here
    setCustomGroups(Object.values(groupMap));

    // ── Pass 2: Hardware config merge (functional updater, async-safe) ───────
    // NOTE: This config layer only handles hardware topology (points/segments/groups).
    // Live pattern/color dispatch state is tracked in DeviceStateLedger, NOT here
    // and definitely NOT in ControllerPersistence (which is purely for UI widget state).
    setDeviceConfigs(prevConfigs => {
      let nextConfigs = { ...prevConfigs };
      let configsChanged = false;

      registeredDevices.forEach(rd => {
        const mac = rd.device_mac.toUpperCase();

        // Identify truthy values from the cloud that should replace missing or stale local state.
        // LOCAL WINS rule: if local has an explicit userConfiguredAt stamp, hardware fields are
        // protected from cloud overwrite regardless of what Supabase returns.
        // We also strictly require points/segments > 0 to prevent cloud defaults (0) from wiping valid cache.
        const existing = nextConfigs[mac] || {} as DeviceSettings;
        const localIsUserConfigured = !!existing.userConfiguredAt;

        const canSyncPoints    = !localIsUserConfigured && rd.led_points !== undefined && rd.led_points > 0 && rd.led_points !== existing.points;
        const canSyncSegments  = !localIsUserConfigured && rd.segments   !== undefined && rd.segments   > 0 && rd.segments   !== existing.segments;
        const canSyncSorting   = !localIsUserConfigured && !!rd.color_sorting   && rd.color_sorting   !== 'UNKNOWN' && rd.color_sorting   !== existing.sorting;
        const canSyncStrip     = !localIsUserConfigured && !!rd.ic_type          && rd.ic_type          !== 'UNKNOWN' && rd.ic_type          !== existing.stripType;
        // Name and group metadata always sync from cloud (cross-device authoritative state)
        const canSyncName  = !!rd.device_name && rd.device_name !== existing.name;
        const canSyncGroup = !!rd.group_ids && !rd.group_ids.includes('default-fleet') && JSON.stringify(rd.group_ids) !== JSON.stringify(existing.groupIds);

        if (canSyncPoints || canSyncSegments || canSyncSorting || canSyncStrip || canSyncName || canSyncGroup) {
          nextConfigs[mac] = {
            ...existing,
            points:    canSyncPoints    ? rd.led_points!    : existing.points,
            segments:  canSyncSegments  ? rd.segments!      : existing.segments,
            sorting:   canSyncSorting   ? rd.color_sorting! : existing.sorting,
            stripType: canSyncStrip     ? rd.ic_type!       : existing.stripType,
            name:      canSyncName      ? rd.device_name!   : existing.name,
            groupIds:  canSyncGroup     ? rd.group_ids!      : existing.groupIds,
            groupNames:rd.group_names   || existing.groupNames,
            grouped:   !!(rd.group_ids && rd.group_ids.length > 0 && !rd.group_ids.includes('default-fleet')),
            // Preserve the userConfiguredAt stamp — never erase it via cloud sync
            userConfiguredAt: existing.userConfiguredAt,
          };
          configsChanged = true;
        }
      });

      if (configsChanged) {
        // Persist via DeviceRepository singleton (canonical write path)
        repo.setConfigs(nextConfigs).catch((e) => AppLogger.warn('PERSISTENCE', { event: 'repo_set_configs_failed', error: String(e) }));
        return nextConfigs;
      }
      return prevConfigs;
    });
  }, [registeredDevices]);



  // ─── Device configs — 0x63 probe results + user overrides ────────────────
  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, DeviceSettings>>({});

  // Load persisted configs from DeviceRepository on mount, then subscribe to
  // all future repo emissions (e.g. from useHardwareNotifications probe writes).
  // This is the canonical subscriber pattern — repo is the SSOT, React state is
  // a read-only mirror. Eliminates the dual-write race between probe callbacks
  // and the mount-time load from AsyncStorage.
  useEffect(() => {
    repo.initialize().then(() => {
      const configs = repo.getConfigs();
      if (Object.keys(configs).length > 0) {
        setDeviceConfigs(configs);
      }
    }).catch((e) => AppLogger.warn('PERSISTENCE', { event: 'repo_initialize_failed', error: String(e) }));

    // Subscribe: re-read configs on every repo mutation (saveDevice, updateConfig, etc.)
    const unsubscribe = repo.subscribe(() => {
      const fresh = repo.getConfigs();
      setDeviceConfigs(prev => {
        // Identity check: only trigger re-render if something actually changed
        const prevKeys = Object.keys(prev);
        const freshKeys = Object.keys(fresh);
        if (prevKeys.length !== freshKeys.length) return fresh;
        for (const k of freshKeys) {
          if (prev[k] !== fresh[k]) return fresh;
        }
        return prev;
      });
    });

    return unsubscribe;
  }, []);

  // ─── Power states map ──────────────────────────────────────────────────────
  const [powerStates, setPowerStates] = useState<Record<string, boolean>>({});

  /**
   * Toggle or force power state for one or more device IDs.
   * DashboardScreen is responsible for firing the actual BLE write after calling this.
   */
  const setPowerState = (deviceIds: string[], forceState?: boolean) => {
    const targetState = forceState !== undefined ? forceState : !(powerStates[deviceIds[0]] ?? true);
    const newStates = { ...powerStates };
    deviceIds.forEach(id => { newStates[id] = targetState; });
    setPowerStates(newStates);
  };

  // ─── Last group patterns — persisted color snapshot per group ───────────────
  const [lastGroupPatterns, setLastGroupPatterns] = useState<Record<string, GroupPatternSnapshot>>({});

  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_last_group_patterns').then(saved => {
      if (saved) {
        try { setLastGroupPatterns(JSON.parse(saved)); } catch (e) { AppLogger.warn('[Groups] Failed to parse last group patterns', { error: String(e) }); }
      }
    }).catch((e) => AppLogger.warn('PERSISTENCE', { key: '@Sk8lytz_last_group_patterns', event: 'load_failed', error: String(e) }));
  }, []);

  const setLastGroupPattern = async (groupId: string, snapshot: GroupPatternSnapshot): Promise<void> => {
    const updated = { ...lastGroupPatterns, [groupId]: snapshot };
    setLastGroupPatterns(updated);
    await AsyncStorage.setItem('@Sk8lytz_last_group_patterns', JSON.stringify(updated)).catch((e) => AppLogger.warn('PERSISTENCE', { key: '@Sk8lytz_last_group_patterns', event: 'save_failed', error: String(e) }));
  };

  // ─── Group modal FSM ──────────────────────────────────────────────────────
  const [groupModalState, setGroupModalState] = useState<GroupModalState>('HIDDEN');
  const [editingGroupId, setEditingGroupId] = useState<string | null>(null);

  const openGroupCreate = () => setGroupModalState('CREATE');
  const openGroupRename = (groupId: string) => {
    setEditingGroupId(groupId);
    setGroupModalState('RENAME');
  };
  const closeGroupModal = () => {
    setGroupModalState('HIDDEN');
    setEditingGroupId(null);
  };

  // ─── Selection mode ────────────────────────────────────────────────────────
  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const [isSelectionMode, setIsSelectionMode] = useState(false);

  const toggleDeviceSelection = (id: string) => {
    if (selectedIds.includes(id)) {
      const next = selectedIds.filter(i => i !== id);
      setSelectedIds(next);
      if (next.length === 0) setIsSelectionMode(false);
    } else {
      setSelectedIds([...selectedIds, id]);
      setIsSelectionMode(true);
    }
  };

  const clearSelection = () => {
    setSelectedIds([]);
    setIsSelectionMode(false);
  };

  // ─── Collapse toggles ──────────────────────────────────────────────────────
  const [isDeviceListCollapsed, setIsDeviceListCollapsed] = useState(true);
  const [isRegisteredCollapsed, setIsRegisteredCollapsed] = useState(true);

  // ─── FTUE registration completion handler ─────────────────────────────────
  /**
   * Called by HardwareSetupWizardScreen.onSetupComplete.
   * Saves devices to cloud, builds the initial fleet group in local state,
   * then notifies DashboardScreen to hide the wizard.
   *
   * @param devices - Devices returned by the wizard
   * @param allBleDevices - Current BLE discovered devices (from DashboardScreen's allDevices)
   */
  const handleRegistrationComplete = async (
    devices: RegisteredDevice[],
    _allBleDevices: any[]
  ): Promise<void> => {
    // [Ghost Injection Fix]: We completely remove `migrateLegacyGroups` which was re-injecting 
    // mismatched lower-case MACs from local cache alongside fresh upper-case MACs, bypassing DB UNIQUE constraints.
    await saveAllRegisteredDevices(devices);

    // Groups are derived automatically from registeredDevices via the useEffect derivation loop.
    // Devices carry the correct group_id from the wizard — no manual group creation needed.
    clearPendingRegistrations();
    onRegistrationComplete();
  };

  // ─── Group CRUD: save and delete handlers ───────────────────────────────────

  /**
   * Prompts the user: Forget Group Only or Deregister Hardware?
   */
  const handleGroupDelete = async (id: string): Promise<void> => {
    const groupToDelete = customGroups.find(g => g.id === id);
    if (!groupToDelete) { closeGroupModal(); return; }

    Alert.alert(
      "Delete Group",
      "Do you want to just forget the group organization, or permanently deregister the physical hardware inside it?",
      [
        { text: "Cancel", style: "cancel", onPress: () => closeGroupModal() },
        { 
          text: "Forget Group Only", 
          onPress: async () => {
            // Phase 5: Atomic RPC-backed backend sync and instantaneous RAM cleanup
            await repo.deleteGroup(id);
            await _scrubGhostGroupFromLocal(groupToDelete);
            closeGroupModal();
          }
        },
        {
          text: "Deregister Hardware",
          style: "destructive",
          onPress: async () => {
            const devsToDelete = registeredDevices.filter(d => d.group_ids && d.group_ids.includes(id));
            for (const d of devsToDelete) {
              await deregisterDevice(d.device_mac);
            }
            // Phase 5: Atomic RPC-backed cleanup follows the device removal
            await repo.deleteGroup(id);
            await _scrubGhostGroupFromLocal(groupToDelete);
            closeGroupModal();
          }
        }
      ]
    );
  };

  const _scrubGhostGroupFromLocal = async (groupToDelete: CustomGroup) => {
    if (groupToDelete.deviceIds) {
      try {
        const configs = { ...repo.getConfigs() };
        let configsChanged = false;
        for (const mac of groupToDelete.deviceIds) {
          if (configs[mac]) {
            const currentIds = configs[mac].groupIds || [];
            const currentNames = configs[mac].groupNames || [];
            const newIds = currentIds.filter(gid => gid !== groupToDelete.id);
            const newNames = currentNames.filter(n => n !== groupToDelete.name);
            configs[mac].groupIds = newIds;
            configs[mac].groupNames = newNames;
            configs[mac].grouped = newIds.length > 0 && !newIds.includes('default-fleet');
            configsChanged = true;
          }
        }
        if (configsChanged) {
          await repo.setConfigs(configs);
          setDeviceConfigs(configs);
        }
      } catch (e) {
        AppLogger.warn('Failed to scrub ghost group from device configs', { error: String(e) });
      }
    }
  };

  /**
   * Creates or renames a custom group. If renaming with zero devices, deletes
   * the group. Persists group membership changes atomically via DeviceRepository RPC.
   */
  const saveGroup = async (name: string, deviceIds: string[]): Promise<void> => {
    let finalGroupId = `group-${Date.now()}`;
    let previousDeviceIds: string[] = [];

    if (groupModalState === 'CREATE') {
      const existing = customGroups.find(g => g.name.toLowerCase() === name.toLowerCase());
      if (existing) {
        finalGroupId = existing.id;
        previousDeviceIds = existing.deviceIds || [];
      }
      clearSelection();
    } else if (groupModalState === 'RENAME' && editingGroupId) {
      if (deviceIds.length === 0) {
        await handleGroupDelete(editingGroupId);
        return;
      }
      finalGroupId = editingGroupId;
      const existing = customGroups.find(g => g.id === editingGroupId);
      if (existing) previousDeviceIds = existing.deviceIds || [];
    }

    try {
      const configs = { ...repo.getConfigs() };
      let configsChanged = false;

      // Phase 5 RPC Call: Atomically write new configuration 
      // (Handles both additions and removals dynamically)
      await repo.saveGroupTransactional(finalGroupId, name, deviceIds);

      // Local UI configs map cleanup (for disconnected state overlays)
      const removedIds = previousDeviceIds.filter(id => !deviceIds.includes(id));
      for (const mac of removedIds) {
        if (configs[mac]) {
          const currentIds = configs[mac].groupIds || [];
          const currentNames = configs[mac].groupNames || [];
          const newIds = currentIds.filter(id => id !== finalGroupId);
          const newNames = currentNames.filter(n => n !== name);
          configs[mac].groupIds = newIds;
          configs[mac].groupNames = newNames;
          configs[mac].grouped = newIds.length > 0 && !newIds.includes('default-fleet');
          configsChanged = true;
        }
      }

      for (const mac of deviceIds) {
        if (!configs[mac]) configs[mac] = {} as DeviceSettings;
        const currentIds = configs[mac].groupIds || [];
        const currentNames = configs[mac].groupNames || [];
        const newIds = currentIds.includes(finalGroupId) ? currentIds : [...currentIds.filter(id => id !== 'default-fleet'), finalGroupId];
        const newNames = currentNames.includes(name) ? currentNames : [...currentNames.filter(n => n !== 'Default Fleet'), name];
        configs[mac] = { ...configs[mac], groupIds: newIds, groupNames: newNames, grouped: true };
        configsChanged = true;
      }

      if (configsChanged) {
        await repo.setConfigs(configs);
        setDeviceConfigs(configs);
        closeGroupModal();
      }
    } catch (e) {
      AppLogger.warn('Failed to sync group cache changes', { error: String(e) });
    }
  };

  return {
    customGroups,
    setCustomGroups,
    customGroupsRef,
    deviceConfigs,
    setDeviceConfigs,
    powerStates,
    setPowerState,
    lastGroupPatterns,
    setLastGroupPattern,
    groupModalState,
    editingGroupId,
    openGroupCreate,
    openGroupRename,
    closeGroupModal,
    selectedIds,
    isSelectionMode,
    toggleDeviceSelection,
    clearSelection,
    isDeviceListCollapsed,
    setIsDeviceListCollapsed,
    isRegisteredCollapsed,
    setIsRegisteredCollapsed,
    handleRegistrationComplete,
    saveGroup,
    handleGroupDelete,
  };
}

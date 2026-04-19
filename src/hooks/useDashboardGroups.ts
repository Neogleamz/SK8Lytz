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
 * Depends on: AsyncStorage, useRegistration (via options), custom types
 */
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { getLocalProfileByPoints, LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import type { RegisteredDevice } from '../hooks/useRegistration';
import { AppLogger } from '../services/AppLogger';
import DeviceRepository from '../services/DeviceRepository';
import { supabase } from '../services/supabaseClient';
import type { CustomGroup, DeviceSettings, GroupModalState } from '../types/dashboard.types';
import { getDefaultDeviceName, getDefaultGroupName } from '../utils/NamingUtils';

interface UseDashboardGroupsOptions {
  registeredDevices: RegisteredDevice[];
  /** Injected from DashboardScreen's useRegistration hook.
   * Saves the full device list to cloud + local after FTUE wizard.
   * Return value (boolean | void) is intentionally discarded.
   */
  saveAllRegisteredDevices: (devices: RegisteredDevice[]) => Promise<boolean | void>;
  /** Saves a single registered device to cloud + local. Used by group CRUD. */
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
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
  lastGroupPatterns: Record<string, string>;
  setLastGroupPattern: (groupId: string, patternName: string) => Promise<void>;
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
  runAutoProvisioning: () => Promise<void>;
  isProvisioningTriggered: React.MutableRefObject<boolean>;
  saveGroup: (name: string, deviceIds: string[]) => Promise<void>;
  handleGroupDelete: (id: string) => Promise<void>;
}

export function useDashboardGroups({
  registeredDevices,
  saveAllRegisteredDevices,
  saveRegisteredDevice,
  migrateLegacyGroups,
  clearPendingRegistrations,
  onRegistrationComplete,
  getAllScannedDevices,
  setAllDevices,
  allDevicesRef,
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
      if (rd.group_id && rd.group_name && rd.group_id !== 'default-fleet') {
        if (!groupMap[rd.group_id]) {
          groupMap[rd.group_id] = { id: rd.group_id, name: rd.group_name, isGroup: true, deviceIds: [] };
        }
        if (!groupMap[rd.group_id].deviceIds.includes(mac)) {
          groupMap[rd.group_id].deviceIds.push(mac);
        }
      }
    });
    // setCustomGroups is called synchronously — groupMap is fully populated here
    setCustomGroups(Object.values(groupMap));

    // ── Pass 2: Hardware config merge (functional updater, async-safe) ───────
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
        const canSyncGroup = !!rd.group_id && rd.group_id !== 'default-fleet' && rd.group_id !== existing.groupId;

        if (canSyncPoints || canSyncSegments || canSyncSorting || canSyncStrip || canSyncName || canSyncGroup) {
          nextConfigs[mac] = {
            ...existing,
            points:    canSyncPoints    ? rd.led_points!    : existing.points,
            segments:  canSyncSegments  ? rd.segments!      : existing.segments,
            sorting:   canSyncSorting   ? rd.color_sorting! : existing.sorting,
            stripType: canSyncStrip     ? rd.ic_type!       : existing.stripType,
            name:      canSyncName      ? rd.device_name!   : existing.name,
            groupId:   canSyncGroup     ? rd.group_id!      : existing.groupId,
            groupName: rd.group_name    || existing.groupName,
            grouped:   !!(rd.group_id && rd.group_id !== 'default-fleet'),
            // Preserve the userConfiguredAt stamp — never erase it via cloud sync
            userConfiguredAt: existing.userConfiguredAt,
          };
          configsChanged = true;
        }
      });

      if (configsChanged) {
        // Persist via DeviceRepository singleton (canonical write path)
        repo.setConfigs(nextConfigs).catch(() => {});
        return nextConfigs;
      }
      return prevConfigs;
    });
  }, [registeredDevices]);



  // ─── Device configs — 0x63 probe results + user overrides ────────────────
  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, DeviceSettings>>({});

  // Load persisted configs from DeviceRepository on mount
  useEffect(() => {
    repo.initialize().then(() => {
      const configs = repo.getConfigs();
      if (Object.keys(configs).length > 0) {
        setDeviceConfigs(configs);
      }
    }).catch(() => {});
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

  // ─── Last group patterns — persisted LED pattern per group ────────────────
  const [lastGroupPatterns, setLastGroupPatterns] = useState<Record<string, string>>({});

  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_last_group_patterns').then(saved => {
      if (saved) {
        try { setLastGroupPatterns(JSON.parse(saved)); } catch (e) { AppLogger.warn('[Groups] Failed to parse last group patterns', { error: String(e) }); }
      }
    }).catch(() => {});
  }, []);

  const setLastGroupPattern = async (groupId: string, patternName: string): Promise<void> => {
    const updated = { ...lastGroupPatterns, [groupId]: patternName };
    setLastGroupPatterns(updated);
    await AsyncStorage.setItem('@Sk8lytz_last_group_patterns', JSON.stringify(updated)).catch(() => {});
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
    allBleDevices: any[]
  ): Promise<void> => {
    // [Ghost Injection Fix]: We completely remove `migrateLegacyGroups` which was re-injecting 
    // mismatched lower-case MACs from local cache alongside fresh upper-case MACs, bypassing DB UNIQUE constraints.
    await saveAllRegisteredDevices(devices);

    // Groups are derived automatically from registeredDevices via the useEffect derivation loop.
    // Devices carry the correct group_id from the wizard — no manual group creation needed.
    clearPendingRegistrations();
    onRegistrationComplete();
  };

  // ─── Auto-provisioning: catalog-driven group inference after scan ─────────
  /**
   * Trigger flag set by handleScan() in DashboardScreen.
   * `runAutoProvisioning` reads and clears it to ensure single-execution.
   */
  const isProvisioningTriggered = useRef(false);

  /**
   * Classifies all scanned BLE devices by product type, auto-groups pairs,
   * persists to AsyncStorage, and syncs to Supabase.
   * Called by DashboardScreen's handleScan after the scan timer completes.
   */
  const runAutoProvisioning = useCallback(async () => {
    if (!isProvisioningTriggered.current) return;
    isProvisioningTriggered.current = false;

    AppLogger.log('BLE_STATE_CHANGE', { event: 'provisioning_started' });
    const currentDevices = getAllScannedDevices();
    if (currentDevices.length === 0) return;

    let updatedGroups = [...customGroupsRef.current];
    let didUpdateGroups = false;
    let didUpdateConfigs = false;

    const [resConfigs, resProcessed] = await Promise.all([
      AsyncStorage.getItem('@Sk8lytz_device_configs'),
      AsyncStorage.getItem('@Sk8lytz_processed_devices'),
    ]);

    let configs: any = {};
    if (resConfigs) { try { configs = JSON.parse(resConfigs); } catch (e) { AppLogger.warn('[Groups] Failed to parse stored configs during migration', { error: String(e) }); } }

    let processed: string[] = [];
    if (resProcessed) { try { processed = JSON.parse(resProcessed); } catch (e) { AppLogger.warn('[Groups] Failed to parse processed devices', { error: String(e) }); } }
    let didUpdateProcessed = false;

    const checkAndGroup = (
      devicesToProcess: any[],
      targetGroupName: string,
      typeVal: string,
      pointsVal: number,
    ) => {
      const unprocessed = devicesToProcess.filter(d =>
        !processed.includes(d.id.toUpperCase()) &&
        !updatedGroups.some(g => g.deviceIds.includes(d.id.toUpperCase()))
      );
      if (unprocessed.length >= 2) {
        const leftId = unprocessed[0].id.toUpperCase();
        const rightId = unprocessed[1].id.toUpperCase();
        processed.push(leftId, rightId);
        didUpdateProcessed = true;

        const existingIdx = updatedGroups.findIndex(g => g.name === targetGroupName);
        if (existingIdx > -1) {
          const target = updatedGroups[existingIdx];
          if (!target.deviceIds.includes(leftId) || !target.deviceIds.includes(rightId)) {
            updatedGroups[existingIdx] = {
              ...target,
              deviceIds: Array.from(new Set([...target.deviceIds, leftId, rightId])),
            };
            didUpdateGroups = true;
          }
        } else {
          updatedGroups.push({
            id: `group-${Date.now()}-${typeVal}-${Math.floor(Math.random() * 1000)}`,
            name: targetGroupName,
            deviceIds: [leftId, rightId],
            type: typeVal,
            isGroup: true,
          });
          didUpdateGroups = true;
        }

        [leftId, rightId].forEach((id, idx) => {
          if (!configs[id]) {
            configs[id] = { name: getDefaultDeviceName(id), type: typeVal, points: pointsVal } as any;
            didUpdateConfigs = true;
          }
        });
      }
    };

    // Catalog-driven classification — groups by resolved product type
    const devicesByType = new Map<string, typeof currentDevices>();
    for (const d of currentDevices) {
      const pts = (d as any).points ?? 0;
      const profile = getLocalProfileByPoints(pts);
      if (!devicesByType.has(profile.id)) devicesByType.set(profile.id, []);
      devicesByType.get(profile.id)!.push(d);
    }
    for (const [typeKey, devices] of devicesByType.entries()) {
      const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === typeKey) ?? LOCAL_PRODUCT_CATALOG[0];
      // FIX: Route through NamingUtils so auto-provisioned group names are consistent
      // with names produced by HardwareSetupWizard and other manual flows.
      // Before: `${profile.displayName} SK8Lytz` → "HALOZ SK8Lytz"
      // After:  getDefaultGroupName(typeKey)      → "My SK8Lytz HALOZ"
      checkAndGroup(devices, getDefaultGroupName(typeKey), typeKey, profile.defaultLedPoints);
    }


    const storagePromises: Promise<void>[] = [];
    if (didUpdateProcessed)
      storagePromises.push(AsyncStorage.setItem('@Sk8lytz_processed_devices', JSON.stringify(processed)));
    if (didUpdateGroups)
      storagePromises.push(repo.setGroups(updatedGroups));
    if (didUpdateConfigs)
      storagePromises.push(repo.setConfigs(configs));
    if (storagePromises.length > 0) await Promise.all(storagePromises);

    if (didUpdateGroups) {
      customGroupsRef.current = updatedGroups;
    }

    // Sync to Supabase if authenticated
    if ((didUpdateGroups || didUpdateConfigs) && supabase) {
      try {
        const { data: { session } } = await supabase.auth.getSession();
        if (session?.user) {
          const userId = session.user.id;
          for (const group of updatedGroups) {
            try {
              await supabase.from('registered_groups').upsert({
                id: group.id, user_id: userId, group_name: group.name,
                type: (group as any).type || 'device-fleet',
                created_at: new Date().toISOString(),
              }, { onConflict: 'id' });
            } catch (_ge) { /* best-effort */ }

            for (const deviceId of group.deviceIds) {
              const c = configs[deviceId];
              if (c) {
                try {
                  await supabase.from('registered_devices').upsert({
                    id: deviceId, user_id: userId, group_id: group.id,
                    custom_name: c.name || 'Unknown', points: c.points || 0,
                    segments: c.segments || 0, sorting: c.sorting || 'GRB',
                    strip_type: c.stripType || 'UNKNOWN',
                  }, { onConflict: 'id' });
                } catch (_de) { /* best-effort */ }
              }
            }
          }
        }
      } catch (e) {
        AppLogger.warn('Supabase sync error during provisioning', { error: String(e) });
      }
    }

    if (didUpdateConfigs) {
      setAllDevices(prev => {
        let morphed = false;
        const next = prev.map(d => {
          const c = configs[d.id];
          if (c && (d.name !== c.name || (d as any).sorting !== c.sorting || (d as any).stripType !== c.stripType)) {
            morphed = true;
            return { ...d, name: c.name, points: c.points, sorting: c.sorting, stripType: c.stripType } as any;
          }
          return d;
        });
        if (morphed) allDevicesRef.current = next;
        return morphed ? next : prev;
      });
    }

    // Collapse device list after provisioning to surface grouped controls
    setIsDeviceListCollapsed(true);
    AppLogger.log('BLE_STATE_CHANGE', { event: 'provisioning_complete' });
  }, [getAllScannedDevices, setAllDevices, allDevicesRef]);

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
            const devsToDelete = registeredDevices.filter(d => d.group_id === id);
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
            delete configs[mac].groupId;
            delete configs[mac].groupName;
            configs[mac].grouped = false;
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
          delete configs[mac].groupId;
          delete configs[mac].groupName;
          configs[mac].grouped = false;
          configsChanged = true;
        }
      }

      for (const mac of deviceIds) {
        if (!configs[mac]) configs[mac] = {} as DeviceSettings;
        configs[mac] = { ...configs[mac], groupId: finalGroupId, groupName: name, grouped: true };
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
    runAutoProvisioning,
    isProvisioningTriggered,
    saveGroup,
    handleGroupDelete,
  };
}

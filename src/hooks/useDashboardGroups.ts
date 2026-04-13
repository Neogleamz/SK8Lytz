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
import { useState, useEffect, useRef } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import type { CustomGroup, GroupModalState, DeviceSettings } from '../types/dashboard.types';
import type { RegisteredDevice } from '../hooks/useRegistration';

interface UseDashboardGroupsOptions {
  registeredDevices: RegisteredDevice[];
  /** Injected from DashboardScreen's useRegistration hook.
   * Saves the full device list to cloud + local after FTUE wizard.
   * Return value (boolean | void) is intentionally discarded.
   */
  saveAllRegisteredDevices: (devices: RegisteredDevice[]) => Promise<boolean | void>;
  migrateLegacyGroups: (allDevices: any[], deviceConfigs: Record<string, DeviceSettings>) => Promise<RegisteredDevice[]>;
  clearPendingRegistrations: () => void;
  /** Called after FTUE setup completes to hide the SetupWizard. */
  onRegistrationComplete: () => void;
}

export interface UseDashboardGroupsResult {
  customGroups: CustomGroup[];
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
}

export function useDashboardGroups({
  registeredDevices,
  saveAllRegisteredDevices,
  migrateLegacyGroups,
  clearPendingRegistrations,
  onRegistrationComplete,
}: UseDashboardGroupsOptions): UseDashboardGroupsResult {

  // ─── Group state (derived from useRegistration, kept as display SSOT) ─────
  const [customGroups, setCustomGroups] = useState<CustomGroup[]>([]);
  /** Stable ref so callbacks (e.g. handleRegistrationComplete) always read current groups. */
  const customGroupsRef = useRef<CustomGroup[]>([]);
  useEffect(() => { customGroupsRef.current = customGroups; }, [customGroups]);

  // Derive groups from registeredDevices whenever cloud sync updates them
  useEffect(() => {
    const groupMap: Record<string, CustomGroup> = {};
    registeredDevices.forEach(rd => {
      if (rd.group_id && rd.group_name && rd.group_id !== 'default-fleet') {
        if (!groupMap[rd.group_id]) {
          groupMap[rd.group_id] = { id: rd.group_id, name: rd.group_name, isGroup: true, deviceIds: [] };
        }
        if (!groupMap[rd.group_id].deviceIds.includes(rd.device_mac)) {
          groupMap[rd.group_id].deviceIds.push(rd.device_mac);
        }
      }
    });
    setCustomGroups(Object.values(groupMap));
  }, [registeredDevices]);

  // ─── Device configs — 0x63 probe results + user overrides ────────────────
  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, DeviceSettings>>({});

  // Load persisted configs from AsyncStorage on mount
  useEffect(() => {
    AsyncStorage.getItem('ng_device_configs').then(raw => {
      if (raw) {
        try { setDeviceConfigs(JSON.parse(raw)); } catch (e) {}
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
        try { setLastGroupPatterns(JSON.parse(saved)); } catch (e) {}
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
    const legacyDevices = await migrateLegacyGroups(allBleDevices, deviceConfigs);
    const allToRegister = [
      ...devices,
      ...legacyDevices.filter(l => !devices.find(d => d.device_mac === l.device_mac)),
    ];
    await saveAllRegisteredDevices(allToRegister);

    const macs = devices.map(d => d.device_mac);
    if (macs.length > 0) {
      const newGroupId = `fleet_${Date.now()}`;
      const groupName = devices[0].group_name || 'My Skates';
      const newGroup: CustomGroup = { id: newGroupId, name: groupName, isGroup: true, deviceIds: macs };
      const updatedGroups = [...customGroupsRef.current, newGroup];
      setCustomGroups(updatedGroups);
      AsyncStorage.setItem('ng_custom_groups', JSON.stringify(updatedGroups)).catch(() => {});
    }

    clearPendingRegistrations();
    onRegistrationComplete();
  };

  return {
    customGroups,
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
  };
}

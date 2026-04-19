/**
 * useDashboardDeviceConfig.ts — Device Config Mutation Hook
 *
 * Encapsulates the `saveSettings` logic that was inline in DashboardScreen.tsx —
 * group-id resolution, optimistic device-state update, AsyncStorage persist,
 * and AppLogger audit trail for renames.
 *
 * Extracted from DashboardScreen.tsx (chore/refactor-dashboard-monolith).
 */
import { MutableRefObject } from 'react';
import { AppLogger } from '../services/AppLogger';
import DeviceRepository from '../services/DeviceRepository';
import type { DeviceSettings } from '../types/dashboard.types';

interface Group {
  id: string;
  name: string;
  deviceIds: string[];
}

interface RegisteredDevice {
  device_mac: string;
  group_id?: string;
  group_name?: string;
  is_pending_sync?: boolean;
  [key: string]: any;
}

interface UseDashboardDeviceConfigOptions {
  selectedDeviceForSettings: { id: string; name: string | null; [key: string]: any } | null;
  customGroups: Group[];
  registeredDevices: RegisteredDevice[];
  saveRegisteredDevice: (rd: any) => Promise<any>;
  setAllDevices: (updater: (prev: any[]) => any[]) => void;
  allDevicesRef: MutableRefObject<any[]>;
  setUpdateTrigger: (updater: (prev: number) => number) => void;
  setIsSettingsVisible: (v: boolean) => void;
}

export interface UseDashboardDeviceConfigResult {
  saveSettings: (settings: DeviceSettings) => Promise<void>;
}

export function useDashboardDeviceConfig({
  selectedDeviceForSettings,
  customGroups,
  registeredDevices,
  saveRegisteredDevice,
  setAllDevices,
  allDevicesRef,
  setUpdateTrigger,
  setIsSettingsVisible,
}: UseDashboardDeviceConfigOptions): UseDashboardDeviceConfigResult {

  const saveSettings = async (settings: DeviceSettings): Promise<void> => {
    if (!selectedDeviceForSettings) return;

    const targetMac = (selectedDeviceForSettings.device_mac || selectedDeviceForSettings.id).toUpperCase();

    // ── Group-ID resolution ────────────────────────────────────────────────
    let finalGroupId = settings.groupId;

    if (settings.grouped && settings.groupName && !settings.groupId) {
      // Implicit group association from the settings modal name field
      const existingGroup = customGroups.find(
        g => g.name.toLowerCase() === settings.groupName?.toLowerCase()
      );
      finalGroupId = existingGroup ? existingGroup.id : `group-${Date.now()}`;
    } else if (!settings.grouped) {
      finalGroupId = 'default-fleet';
    }

    // ── Registration SSOT sync ─────────────────────────────────────────────
    const rd = registeredDevices.find(r => r.device_mac?.toUpperCase() === targetMac);
    if (rd) {
      const targetGroupName = settings.grouped
        ? (settings.groupName || rd.group_name)
        : undefined;
      saveRegisteredDevice({
        ...rd,
        device_name: settings.name,
        group_id: finalGroupId,
        group_name: targetGroupName,
        led_points: settings.points,
        segments: settings.segments,
        ic_type: settings.stripType,
        color_sorting: settings.sorting,
        is_pending_sync: true,
      }).catch(AppLogger.warn);
    }

    // ── Optimistic device-list update ──────────────────────────────────────
    setAllDevices((prev: any[]) => {
      const next = prev.map(d => {
        const dMac = (d.device_mac || d.id).toUpperCase();
        return dMac === targetMac
          ? {
              ...d,
              name:      settings.name,
              type:      settings.type,
              points:    settings.points,
              segments:  settings.segments,
              sorting:   settings.sorting,
              stripType: settings.stripType,
              groupId:   finalGroupId,
            }
          : d
      });
      allDevicesRef.current = next as any;
      return next;
    });

    setUpdateTrigger(prev => prev + 1);

    // ── Device config persist via DeviceRepository ───────────────────────────────────────
    try {
      const repo = DeviceRepository.getInstance();
      // USER-EXPLICIT SAVE: stamp with current timestamp so this config has
      // the highest priority in the local/cloud merge arbitration.
      // The userConfiguredAt field prevents cloud sync from ever overwriting
      // these settings unless the user explicitly saves again.
      const userConfiguredAt = new Date().toISOString();
      await repo.updateConfig(targetMac, { ...settings, groupId: finalGroupId, userConfiguredAt });


      AppLogger.log('HARDWARE_CONFIG_CHANGED', {
        deviceId:  targetMac,
        name:      settings.name,
        type:      settings.type,
        points:    settings.points,
        segments:  settings.segments,
        sorting:   settings.sorting,
        stripType: settings.stripType,
      });

      // Separate audit event for device renames
      const previousName = selectedDeviceForSettings.name;
      if (settings.name && settings.name !== previousName) {
        AppLogger.log('DEVICE_RENAMED', {
          deviceId: targetMac,
          oldName:  previousName || 'Unknown',
          newName:  settings.name,
        });
      }
    } catch (e) {
      AppLogger.error('Failed to persist settings', e);
    }

    setIsSettingsVisible(false);
  };

  return { saveSettings };
}

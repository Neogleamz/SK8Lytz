import { supabase } from '../supabaseClient';
import { AppLogger } from '../appLogger';
import type { Tables } from '../../types/supabase';
import type { RegisteredDevice } from './types';

/** Supabase row shape for registered_devices — device_mac is nullable from the DB schema. */
type CloudDeviceRow = Tables<'registered_devices'>;

export class DeviceCloudSync {
  /**
   * Helper to perform the local-first smart merge logic.
   */
  static mergeCloudAndLocal(
    cloudRows: CloudDeviceRow[],
    localDevices: RegisteredDevice[],
    tombstones: string[]
  ): RegisteredDevice[] {
    // Tombstone filter
    const filteredCloud = cloudRows.filter((row) =>
      !tombstones.includes(row.device_mac?.toUpperCase?.() ?? '')
    );

    if (filteredCloud.length < cloudRows.length) {
      AppLogger.warn('[DeviceCloudSync] syncFromCloud tombstone filtered', {
        skipped: cloudRows.length - filteredCloud.length,
      });
    }

    // Local-first smart merge
    const merged: RegisteredDevice[] = filteredCloud.map((row) => {
      const cloud = { ...row, is_pending_sync: false } as RegisteredDevice;
      const local = localDevices.find(
        (l) => l.device_mac.toUpperCase() === cloud.device_mac.toUpperCase()
      );

      if (!local) {
        const scalarGroupId = row.group_id;
        const scalarGroupName = row.group_name;
        return {
          ...cloud,
          group_ids: scalarGroupId ? [String(scalarGroupId)] : [],
          group_names: scalarGroupName ? [String(scalarGroupName)] : [],
        };
      }

      const localHasPendingChanges = !!local.is_pending_sync;
      const localHasValidPoints = (local.led_points ?? 0) > 0;
      const localHasValidSegments = (local.segments ?? 0) > 0;
      const localHasValidIcType =
        local.ic_type && local.ic_type !== 'UNKNOWN' && local.ic_type !== 'WS2812B';
      const localHasValidSorting =
        local.color_sorting && local.color_sorting !== 'UNKNOWN';

      return {
        ...cloud,
        led_points:
          localHasPendingChanges || localHasValidPoints
            ? local.led_points
            : cloud.led_points,
        segments:
          localHasPendingChanges || localHasValidSegments
            ? local.segments
            : cloud.segments,
        ic_type:
          localHasPendingChanges || localHasValidIcType
            ? local.ic_type
            : cloud.ic_type,
        color_sorting:
          localHasPendingChanges || localHasValidSorting
            ? local.color_sorting
            : cloud.color_sorting,
        group_ids: cloud.group_ids ?? local.group_ids ?? [],
        group_names: cloud.group_names ?? local.group_names ?? [],
        device_name: cloud.device_name || local.device_name,
        is_pending_sync: localHasPendingChanges,
      };
    });

    // Pure-local preservation
    const offlineLocalOnly = localDevices.filter(
      (localD) =>
        !!localD.is_pending_sync &&
        !filteredCloud.some(
          (cloudD) =>
            cloudD.device_mac?.toUpperCase() === localD.device_mac.toUpperCase()
        )
    );

    return [...merged, ...offlineLocalOnly];
  }

  /**
   * Check whether a device MAC is claimed by the current user, another user, or unclaimed.
   */
  static async checkDeviceClaimed(
    deviceMac: string,
    localDevices: RegisteredDevice[],
    fingerprint?: { firmwareVer?: number; ledVersion?: number; productId?: number },
    userId?: string
  ): Promise<'unclaimed' | 'claimed_by_self' | 'claimed_by_other' | 'offline_unknown'> {
    try {
      const normalized = deviceMac.toUpperCase();
      if (localDevices.some((d) => d.device_mac.toUpperCase() === normalized)) {
        return 'claimed_by_self';
      }

      const user = userId ? { id: userId } : null;
      const { data, error } = await supabase
        .from('registered_devices')
        .select('user_id')
        .ilike('device_mac', deviceMac)
        .maybeSingle();

      if (error) throw error;

      if (!data) {
        if (fingerprint?.firmwareVer && fingerprint?.productId) {
          const { data: fpData } = await supabase
            .from('registered_devices')
            .select('user_id')
            .eq('firmware_ver', fingerprint.firmwareVer)
            .eq('product_id', fingerprint.productId)
            .maybeSingle();
          if (fpData) {
            return fpData.user_id === user?.id ? 'claimed_by_self' : 'claimed_by_other';
          }
        }
        return 'unclaimed';
      }

      return data.user_id === user?.id ? 'claimed_by_self' : 'claimed_by_other';
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceCloudSync] Claim check failed (offline?):', { error: msg });
      return 'offline_unknown';
    }
  }

  /**
   * Check if user has at least one registered device.
   */
  static async hasRegistrations(localDevices: RegisteredDevice[], userId?: string): Promise<boolean> {
    if (localDevices.length > 0) return true;

    try {
      if (!userId) return false;
      const user = { id: userId };

      const { count } = await supabase
        .from('registered_devices')
        .select('id', { count: 'exact', head: true })
        .eq('user_id', user.id);

      return (count ?? 0) > 0;
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[DeviceCloudSync] hasRegistrations failed', { error: msg });
      return false;
    }
  }
}

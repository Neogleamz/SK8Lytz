import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from '../appLogger';
import {
  STORAGE_DELETED_MACS,
  STORAGE_PENDING_SYNC,
  STORAGE_REGISTERED_DEVICES,
  CONFIGS_KEY as GLOBAL_CONFIGS_KEY,
} from '../../constants/storageKeys';
import type { RegisteredDevice } from '../../hooks/useRegistration';
import type { DeviceSettings } from '../../types/dashboard.types';

// ─── Storage Keys ─────────────────────────────────────────────────────────────
export const DEVICES_KEY   = STORAGE_REGISTERED_DEVICES;
export const CONFIGS_KEY   = GLOBAL_CONFIGS_KEY;
export const TOMBSTONE_KEY = STORAGE_DELETED_MACS;
export const PENDING_KEY   = STORAGE_PENDING_SYNC;

/**
 * Handles all direct AsyncStorage reading/writing for the device repository.
 */
export class DeviceStorage {
  static async loadDevices(): Promise<RegisteredDevice[] | null> {
    try {
      const raw = await AsyncStorage.getItem(DEVICES_KEY);
      if (raw) return JSON.parse(raw);
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] loadDevices failed', { error: e instanceof Error ? e.message : String(e) });
    }
    return null;
  }

  static async saveDevices(devices: RegisteredDevice[], context: string): Promise<void> {
    try {
      await AsyncStorage.setItem(DEVICES_KEY, JSON.stringify(devices));
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] AsyncStorage write failed', { key: `DEVICES_KEY/${context}`, error: e instanceof Error ? e.message : String(e) });
    }
  }

  static async loadConfigs(): Promise<Record<string, DeviceSettings> | null> {
    try {
      const raw = await AsyncStorage.getItem(CONFIGS_KEY);
      if (raw) return JSON.parse(raw);
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] loadConfigs failed', { error: e instanceof Error ? e.message : String(e) });
    }
    return null;
  }

  static async saveConfigs(configs: Record<string, DeviceSettings>, context: string): Promise<void> {
    try {
      await AsyncStorage.setItem(CONFIGS_KEY, JSON.stringify(configs));
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] AsyncStorage write failed', { key: `CONFIGS_KEY/${context}`, error: e instanceof Error ? e.message : String(e) });
    }
  }

  static async loadTombstones(): Promise<string[] | null> {
    try {
      const raw = await AsyncStorage.getItem(TOMBSTONE_KEY);
      if (raw) return JSON.parse(raw);
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] loadTombstones failed', { error: e instanceof Error ? e.message : String(e) });
    }
    return null;
  }

  static async saveTombstones(tombstones: string[], context: string): Promise<void> {
    try {
      await AsyncStorage.setItem(TOMBSTONE_KEY, JSON.stringify(tombstones));
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] AsyncStorage write failed', { key: `TOMBSTONE_KEY/${context}`, error: e instanceof Error ? e.message : String(e) });
    }
  }

  static async loadPendingQueue(): Promise<RegisteredDevice[]> {
    try {
      const raw = await AsyncStorage.getItem(PENDING_KEY);
      if (raw) return JSON.parse(raw);
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] loadPendingQueue failed', { error: e instanceof Error ? e.message : String(e) });
    }
    return [];
  }

  static async savePendingQueue(queue: RegisteredDevice[]): Promise<void> {
    try {
      await AsyncStorage.setItem(PENDING_KEY, JSON.stringify(queue));
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] AsyncStorage write failed', { key: 'PENDING_KEY', error: e instanceof Error ? e.message : String(e) });
    }
  }

  static async clearPendingQueue(): Promise<void> {
    try {
      await AsyncStorage.removeItem(PENDING_KEY);
    } catch (e: unknown) {
      AppLogger.warn('[DeviceStorage] AsyncStorage remove failed', { key: 'PENDING_KEY', error: e instanceof Error ? e.message : String(e) });
    }
  }
}

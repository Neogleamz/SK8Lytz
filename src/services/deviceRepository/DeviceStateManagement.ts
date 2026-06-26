import type { RegisteredDevice } from './types';
import type { DeviceSettings } from '../../types/dashboard.types';

export type Listener = () => void;

/**
 * Isolates the in-memory state arrays, the listeners Set, and the
 * useSyncExternalStore subscription logic.
 */
export class DeviceStateManagement {
  private _devices: RegisteredDevice[] = [];
  private _configs: Record<string, DeviceSettings> = {};
  private _tombstones: string[] = [];

  private _listeners: Set<Listener> = new Set();
  private _version = 0;

  get devices(): RegisteredDevice[] {
    return this._devices;
  }

  set devices(value: RegisteredDevice[]) {
    this._devices = value;
  }

  get configs(): Record<string, DeviceSettings> {
    return this._configs;
  }

  set configs(value: Record<string, DeviceSettings>) {
    this._configs = value;
  }

  get tombstones(): string[] {
    return this._tombstones;
  }

  set tombstones(value: string[]) {
    this._tombstones = value;
  }

  subscribe = (listener: Listener): (() => void) => {
    this._listeners.add(listener);
    return () => {
      this._listeners.delete(listener);
    };
  };

  getVersion(): number {
    return this._version;
  }

  notifyListeners(): void {
    this._version++;
    this._listeners.forEach((l) => l());
  }
}

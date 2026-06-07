/**
 * ControllerRegistry.ts — Runtime Protocol Resolver
 *
 * Maintains a registry of all known IControllerProtocol implementations.
 * At scan time, resolveProtocol() is called with a device's BLE advertisement
 * data to determine which protocol adapter should handle it.
 *
 * SINGLE SOURCE OF TRUTH:
 * For architectural taxonomy and protocol routing rules, refer strictly to:
 * `tools/SK8Lytz_App_Master_Reference.md`
 */

import type { IControllerProtocol } from './IControllerProtocol';
import { BanlanxAdapter } from './BanlanxAdapter';
import { ZenggeAdapter } from './ZenggeAdapter';

// Lazy AppLogger — ControllerRegistry may load before native modules are ready
let _appLogger: typeof import('../services/AppLogger').AppLogger | typeof console | undefined;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../services/AppLogger').AppLogger; } catch { _appLogger = console; }
  }
  return _appLogger!;
}

// ─── Registry ─────────────────────────────────────────────────────────────────

// The registry is ordered: BanlanX FIRST (FFE0 UUID is unambiguous — must not be masked
// by Zengge's FFFF match). ZenggeAdapter is the production default fallback.
const _banlanxAdapter = new BanlanxAdapter();
const _zenggeAdapter = new ZenggeAdapter();

const registry: IControllerProtocol[] = [
  // BanlanX checked FIRST — FFE0 service UUID is unambiguous for SP621E.
  // ZenggeAdapter is the fallback for FFFF / MFR-data-matched devices.
  _banlanxAdapter,
  _zenggeAdapter,
];

/**
 * Resolve the correct protocol adapter for a BLE device based on its
 * advertised service UUIDs and optional manufacturer data.
 *
 * @returns The matching protocol adapter, or null if no protocol matches.
 */
export function resolveProtocol(
  serviceUUIDs: string[],
  manufacturerData?: string
): IControllerProtocol | null {
  return registry.find(p => p.matchesAdvertisement(serviceUUIDs, manufacturerData)) ?? null;
}

/**
 * Register a new protocol adapter at runtime.
 * This is the extension point for adding new controller hardware.
 */
export function registerProtocol(protocol: IControllerProtocol): void {
  // Prevent duplicate registrations
  if (registry.some(p => p.protocolId === protocol.protocolId)) {
    getAppLogger().warn(`[ControllerRegistry] Protocol '${protocol.protocolId}' already registered — skipping`);
    return;
  }
  registry.push(protocol);
}

/**
 * Get the default protocol (Zengge — the primary production hardware).
 * BanlanxAdapter is registry[0] for advertisement priority, but Zengge
 * is the correct fallback for devices where no protocol is resolved.
 */
export function getDefaultProtocol(): IControllerProtocol {
  return _zenggeAdapter;
}

/**
 * Get all registered protocol IDs (for diagnostics/admin tools).\
 */
export function getRegisteredProtocolIds(): string[] {
  return registry.map(p => p.protocolId);
}

/**
 * Retrieve a specific protocol adapter by its ID.
 */
export function getProtocolById(protocolId: string): IControllerProtocol | null {
  return registry.find(p => p.protocolId === protocolId) ?? null;
}

/**
 * Resolve the adapter for a specific already-connected device from an
 * adapter map (e.g. useBLE.ts adapterMapRef). Falls back to the default
 * protocol if the device is not in the map.
 *
 * This avoids re-running advertisement matching for devices already connected.
 *
 * @param deviceId   The BLE device ID (MAC on Android, UUID on iOS).
 * @param adapterMap The per-device adapter map from useBLE.ts adapterMapRef.
 */
export function resolveProtocolForDevice(
  deviceId: string,
  adapterMap: Map<string, IControllerProtocol>
): IControllerProtocol {
  return adapterMap.get(deviceId) ?? getDefaultProtocol();
}

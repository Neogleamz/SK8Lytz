/**
 * ControllerRegistry.ts — Runtime Protocol Resolver
 *
 * Maintains a registry of all known IControllerProtocol implementations.
 * At scan time, resolveProtocol() is called with a device's BLE advertisement
 * data to determine which protocol adapter should handle it.
 *
 * Usage:
 *   import { resolveProtocol } from '../protocols/ControllerRegistry';
 *   const protocol = resolveProtocol(device.serviceUUIDs, device.manufacturerData);
 *   if (protocol) {
 *     const query = protocol.buildQuerySettingsPayload();
 *     // ...
 *   }
 *
 * To register a new controller type:
 *   import { registerProtocol } from '../protocols/ControllerRegistry';
 *   registerProtocol(new Esp32SpiAdapter());
 */

import type { IControllerProtocol } from './IControllerProtocol';
import { BanlanxAdapter } from './BanlanxAdapter';
import { ZenggeAdapter } from './ZenggeAdapter';

// Lazy AppLogger — ControllerRegistry may load before native modules are ready
let _appLogger: any;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../services/AppLogger').AppLogger; } catch (_e) { _appLogger = console; }
  }
  return _appLogger;
}

// ─── Registry ─────────────────────────────────────────────────────────────────

const registry: IControllerProtocol[] = [
  // BanlanX checked FIRST — FFE0 service UUID is unambiguous for SP621E.
  // ZenggeAdapter is the fallback for FFFF / MFR-data-matched devices.
  new BanlanxAdapter(),
  new ZenggeAdapter(),
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
 * Get the default protocol (first registered).
 * Used as a fallback when no specific protocol is resolved.
 */
export function getDefaultProtocol(): IControllerProtocol {
  return registry[0];
}

/**
 * Get all registered protocol IDs (for diagnostics/admin tools).\
 */
export function getRegisteredProtocolIds(): string[] {
  return registry.map(p => p.protocolId);
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

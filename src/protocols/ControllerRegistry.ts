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
import { ZenggeAdapter } from './ZenggeAdapter';

// ─── Registry ─────────────────────────────────────────────────────────────────

const registry: IControllerProtocol[] = [
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
    console.warn(`[ControllerRegistry] Protocol '${protocol.protocolId}' already registered — skipping`);
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
 * Get all registered protocol IDs (for diagnostics/admin tools).
 */
export function getRegisteredProtocolIds(): string[] {
  return registry.map(p => p.protocolId);
}

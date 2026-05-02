/**
 * classifyBLEDevice.ts — Shared BLE Device Classification Utility
 *
 * Single source of truth for mapping a raw BLE Device + optional EEPROM
 * hwCache entry into a PendingRegistration object.
 *
 * Previously duplicated in:
 *  - useBLEScanner.classifyProbeResults (mapToRegistration)
 *  - useBLESweeper.classifyForRegistrations
 *
 * Priority chain for each field:
 *   1. Real EEPROM data from hwCache (probed by Interrogator)
 *   2. Advertisement-derived properties (name heuristics, mfData parse)
 *   3. LOCAL_PRODUCT_CATALOG defaults for the resolved profile
 */

import type { Device } from 'react-native-ble-plx';
import { getLocalProfileByPoints, LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import type { PendingRegistration } from '../types/dashboard.types';
import { getDefaultGroupName } from './NamingUtils';

/**
 * Resolve the product type string for a device using catalog-driven point matching.
 * Falls back to advertisement data, then 'UNKNOWN'.
 */
export function resolveProductType(device: any, hwCache?: Record<string, any>): string {
  const mac = (device.id || '').toUpperCase();
  const cached = hwCache?.[mac];
  const points = cached?.ledPoints ?? (device as any).hwPoints ?? 0;
  const profile = getLocalProfileByPoints(points);
  // Only trust catalog resolution if we have real probe data
  if (cached?.detected || points > 0) return profile.id;
  return (device as any).product_type || 'UNKNOWN';
}

/**
 * Map a raw BLE Device + optional hwCache entry into a PendingRegistration.
 *
 * @param device       The raw BLE peripheral from react-native-ble-plx
 * @param index        Position in the scan results (used for Left/Right assignment)
 * @param hwCache      In-memory EEPROM cache from useBLESweeper — enriches output if available
 * @param productType  Optional pre-resolved product type string (from classifyProbeResults)
 */
export function mapDeviceToRegistration(
  device: any,
  index: number,
  hwCache: Record<string, any> = {},
  productType?: string,
): PendingRegistration {
  const mac = (device.id || '').toUpperCase();
  const deviceIdShort = mac.replace(/:/g, '').slice(-4);
  const pos = index % 2 === 0 ? 'Left' : 'Right';

  // ── Priority 1: EEPROM cache (Interrogator result) ──────────────────────
  const cached = hwCache[mac];

  // ── Priority 2: Resolve product type ────────────────────────────────────
  const resolvedType = productType ?? resolveProductType(device, hwCache);
  const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === resolvedType) ?? LOCAL_PRODUCT_CATALOG[0];

  return {
    device_mac:         mac,
    device_name:        `SK8Lytz-${deviceIdShort}`,
    factory_name:       device.name || 'Unknown',
    manufacturer_data:  device.manufacturerData,
    ble_version:        device.bleVersion,
    product_type:       resolvedType as any,
    position:           pos,
    group_name:         getDefaultGroupName(resolvedType),
    // ── Hardware fields — EEPROM > advertisement > profile default ──────────
    led_points:         cached?.ledPoints        ?? device.hwPoints        ?? profile.vizDefaultPoints,
    segments:           cached?.segments         ?? device.hwSegments      ?? profile.defaultSegments,
    ic_type:            cached?.icName           ?? device.hwStripType     ?? (profile.defaultIcType === 1 ? 'WS2812B' : 'SM16703'),
    color_sorting:      cached?.colorSortingName ?? device.hwSorting       ?? (profile.defaultColorSorting === 2 ? 'GRB' : 'RGB'),
    rssi:               device.rssi              ?? -99,
    firmware_ver:       device.firmwareVer,
    led_version:        device.ledVersion,
    product_id:         device.productId,
    rf_mode:            cached?.rfMode           ?? device.hwRfMode,
    rf_paired_count:    cached?.rfPairedCount    ?? device.hwRfPairedCount,
  };
}

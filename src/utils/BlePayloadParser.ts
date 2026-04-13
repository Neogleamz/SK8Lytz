/**
 * BlePayloadParser.ts — Pure Utility Gatekeeper for BLE Protocol Context Parsing
 *
 * This module extracts parsing from the BLE notification handler into a strict,
 * stateless utility function. It acts as the Mailroom's structural gate, ensuring
 * that invalid arrays or corrupted protocol packets do not crash the React Native
 * UI thread.
 */

import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';

export interface ParsedLedConfig {
  points?: number;
  segments?: number;
  stripType?: string;
  sorting?: string;
  colorSortingIdx?: number;
  icType?: number;
  parsedOk: boolean;
  // Raw mapped values ensuring parity with the Supabase `device_diagnostics` upload
  rawUploadData: {
    points: number | null;
    icType: number | null;
    icName: string | null;
    colorSorting: number | null;
    colorOrder: string | null;
  };
}

export const BlePayloadParser = {
  /**
   * Deterministically parses Zengge V1 and V2 hardware configurations from raw bytes.
   * Will never throw an error; returns null on failure to prevent notification crashes.
   */
  parseLedPayload(payload: number[]): ParsedLedConfig | null {
    try {
      if (!Array.isArray(payload) || payload.length === 0) return null;

      const v2Config = ZenggeProtocol.parseHardwareSettingsResponse(payload);
      const v1Config = ZenggeProtocol.parseHardwareConfig(payload);

      const parsedOk = !!(v2Config || v1Config);
      if (!parsedOk) return null;

      let configPoints: number | undefined;
      let configSegments: number | undefined;
      let configStripType: string | undefined;
      let configSorting: string | undefined;
      let configSortingIdx: number | undefined;
      let configIcType: number | undefined;

      if (v2Config) {
        configPoints = v2Config.ledPoints;
        configSegments = v2Config.segments;
        configStripType = v2Config.icName;
        configSorting = v2Config.colorSortingName;
        configSortingIdx = v2Config.colorSorting;
        configIcType = v2Config.icType;
      } else if (v1Config) {
        configPoints = v1Config.points;
        configSegments = v1Config.segments;
        configStripType = v1Config.stripType;
        configSorting = v1Config.sorting;
        // Derive numeric index from string for v1 (defaults to GRB=2 if unknown)
        configSortingIdx = ['RGB', 'RBG', 'GRB', 'GBR', 'BRG', 'BGR'].indexOf(configSorting ?? 'GRB');
        if (configSortingIdx < 0) configSortingIdx = 2;
      }

      return {
        points: configPoints,
        segments: configSegments,
        stripType: configStripType,
        sorting: configSorting,
        colorSortingIdx: configSortingIdx,
        icType: configIcType,
        parsedOk,
        rawUploadData: {
          points: v2Config?.ledPoints ?? v1Config?.points ?? null,
          icType: v2Config?.icType ?? null,
          icName: v2Config?.icName ?? v1Config?.stripType ?? null,
          colorSorting: v2Config?.colorSorting ?? null,
          colorOrder: v2Config?.colorSortingName ?? v1Config?.sorting ?? null,
        },
      };
    } catch (error) {
      AppLogger.warn('[BlePayloadParser] Malformed payload dropped', error);
      return null;
    }
  },
};

/**
 * ZenggeAdapter.ts — Zengge Protocol Adapter (IControllerProtocol Implementation)
 *
 * Thin adapter that delegates to the existing ZenggeProtocol static class,
 * implementing the IControllerProtocol interface for the HAL.
 *
 * This is the ONLY file that should import ZenggeProtocol directly. All other
 * files should go through the ControllerRegistry or ControllerContext.
 *
 * NOTE: For this initial HAL phase, the existing 15 files still import
 * ZenggeProtocol directly. Those will be migrated file-by-file in a follow-up
 * once the interface is proven stable.
 *
 * All 4 stubs from the initial HAL phase are now fully wired:
 *   - buildMusicConfig → ZenggeProtocol.setMusicConfig (0x73)
 *   - buildMusicMagnitude → ZenggeProtocol.sendMusicMagnitude (0x74)
 *   - buildPowerOn → 0x56 0xAA wrapped
 *   - buildPowerOff → 0x56 0xAB wrapped
 */

import type {
  CustomModeStep,
  HardwareSettingsResult,
  IControllerProtocol,
  MusicConfig,
  RGB,
} from './IControllerProtocol';
import {
  ZENGGE_CHARACTERISTIC_UUID,
  ZENGGE_NOTIFY_UUID,
  ZENGGE_SERVICE_UUID,
  ZenggeProtocol,
} from './ZenggeProtocol';

export class ZenggeAdapter implements IControllerProtocol {
  // ─── Identity ──────────────────────────────────────────────────────
  readonly protocolId = 'zengge';
  readonly serviceUUID = ZENGGE_SERVICE_UUID;
  readonly writeCharacteristicUUID = ZENGGE_CHARACTERISTIC_UUID;
  readonly notifyCharacteristicUUID = ZENGGE_NOTIFY_UUID;

  // ─── Discovery ─────────────────────────────────────────────────────
  matchesAdvertisement(serviceUUIDs: string[], _manufacturerData?: string): boolean {
    return serviceUUIDs?.includes(ZENGGE_SERVICE_UUID) ?? false;
  }

  // ─── Hardware Settings (EEPROM) ────────────────────────────────────
  buildQuerySettingsPayload(hasMic: boolean = false): number[] {
    return ZenggeProtocol.queryHardwareSettings(hasMic);
  }

  parseSettingsResponse(raw: number[]): HardwareSettingsResult | null {
    return ZenggeProtocol.parseHardwareSettingsResponse(raw);
  }

  buildWriteSettingsPayload(points: number, segments: number, icType: number, sorting: number): number[] {
    return ZenggeProtocol.writeHardwareSettings(points, segments, icType, sorting);
  }

  // ─── LED Commands ──────────────────────────────────────────────────
  buildSolidColor(r: number, g: number, b: number): number[] {
    // Single solid color = setMultiColor with 1 pixel, FREEZE transition
    return ZenggeProtocol.setMultiColor([{ r, g, b }], 1, 1, 0x01);
  }

  buildMultiColor(colors: RGB[], speed: number, direction: number, transition: number): number[] {
    return ZenggeProtocol.setMultiColor(colors, speed, direction, transition);
  }

  buildCustomMode(steps: CustomModeStep[]): number[] {
    return ZenggeProtocol.setCustomMode(steps);
  }

  buildMusicConfig(config: MusicConfig): number[] {
    return ZenggeProtocol.setMusicConfig(
      config.patternId,
      0x26,                // APP mic by default — caller can override via raw write
      true,                // isOn
      config.color1,
      config.color2,
      config.micSensitivity,
      config.brightness,
    );
  }

  buildMusicMagnitude(magnitude: number): number[] {
    return ZenggeProtocol.sendMusicMagnitude(magnitude);
  }

  // ─── Power ─────────────────────────────────────────────────────────
  buildPowerOn(): number[] {
    // Zengge ON = 0x56 0xAA 0x56 0xAA (wrapped with counter header)
    const raw = [0x56, 0xAA, 0x56, 0xAA];
    return ZenggeProtocol.wrapCommand(raw);
  }

  buildPowerOff(): number[] {
    // Zengge OFF = 0x56 0xAB 0x56 0xAB (wrapped with counter header)
    const raw = [0x56, 0xAB, 0x56, 0xAB];
    return ZenggeProtocol.wrapCommand(raw);
  }

  // ─── Utility ───────────────────────────────────────────────────────
  calculateChecksum(payload: number[]): number {
    return ZenggeProtocol.calculateChecksum(payload);
  }

  wrapCommand(rawPayload: number[]): number[] {
    return ZenggeProtocol.wrapCommand(rawPayload);
  }
}

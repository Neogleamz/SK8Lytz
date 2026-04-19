/**
 * useControllerDispatch.ts — BLE hardware dispatch functions for DockedController.
 *
 * Extracted from DockedController.tsx to isolate BLE-protocol translation logic
 * from the UI component. All functions are pure command builders that delegate
 * to ZenggeProtocol and write via the provided writeToDevice callback.
 *
 * Depends on: ZenggeProtocol, AppLogger, NormalizationUtils
 */
import { useCallback } from 'react';
import { getLocalProfileById } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { hexToRgb } from '../utils/ColorUtils';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';

type WriteFn = (payload: number[]) => Promise<boolean | 'partial' | void>;

interface UseControllerDispatchParams {
  writeToDevice?: WriteFn;
  hwSettings?: any;
  points?: number;
}

/**
 * Provides stable BLE dispatch functions for sending solid colors, patterns,
 * music configs, and emergency lighting to connected devices.
 */
export function useControllerDispatch({ writeToDevice, hwSettings, points }: UseControllerDispatchParams) {
  /** Resolve LED count from hw config or fallback */
  const numLEDs = Math.max(1, hwSettings?.ledPoints || points || 16);

  /**
   * Maps UI speed slider (0–100) to Zengge hardware speed range (1–31).
   * The APK enforces 1–31 for all 0x59 animated patterns.
   */
  const clampSpeed = useCallback(
    (uiSpeed: number): number => normalizeUISpeedToHardware(uiSpeed),
    []
  );

  /** Send a solid color instantly using setSymphonyColor (15-byte protocol) to escape the MTU limit */
  const sendColor = useCallback(
    async (r: number, g: number, b: number) => {
      if (!writeToDevice) return;
      // We use setSymphonyColor (0x41) because it generates a 15-byte payload overall.
      // This slides perfectly underneath any 23-byte MTU failure ceilings.
      await writeToDevice(ZenggeProtocol.setSymphonyColor(r, g, b));
    },
    [writeToDevice]
  );

  /**
   * Apply a fixed/custom pattern to devices.
   * Delegates to ZenggeProtocol.setCustomModeCompact — single source of truth.
   */
  const applyFixedPattern = useCallback(
    async (
      patternId: number,
      fg: string,
      bg: string,
      currentSpeed?: number,
      currentBrightness?: number
    ) => {
      if (!writeToDevice) return;

      // 0x51 Custom Engines expect pure, unmodified hexadecimal parameters.
      // The hardware engines interpret brightness separately natively.
      const fgRaw = hexToRgb(fg);
      const bgRaw = hexToRgb(bg);

      // Use compact format: only 1 active step (12 bytes raw, 20 bytes wrapped).
      // Fits in any BLE MTU.
      const payload = ZenggeProtocol.setCustomModeCompact([
        { mode: patternId, speed: currentSpeed ?? 50, color1: fgRaw, color2: bgRaw },
      ]);

      if (payload) writeToDevice(payload);
    },
    [writeToDevice]
  );

  /** Apply static/strobe/blink mode pattern */
  const applyStaticModePattern = useCallback(
    (
      pat: 'STATIC' | 'STROBE' | 'BLINK',
      selectedColor: string,
      speed: number,
      r?: number,
      g?: number,
      b?: number,
      spd?: number
    ) => {
      if (!writeToDevice) return;
      const tR = r !== undefined ? Math.max(0, Math.min(255, r | 0)) : parseInt(selectedColor.slice(1, 3), 16) || 255;
      const tG = g !== undefined ? Math.max(0, Math.min(255, g | 0)) : parseInt(selectedColor.slice(3, 5), 16) || 255;
      const tB = b !== undefined ? Math.max(0, Math.min(255, b | 0)) : parseInt(selectedColor.slice(5, 7), 16) || 255;
      const tSpd = normalizeUISpeedToHardware(spd !== undefined ? spd : speed);

      if (pat === 'STATIC') {
        sendColor(tR, tG, tB);
      } else if (pat === 'STROBE') {
        writeToDevice(ZenggeProtocol.setCustomModeCompact([
          { mode: ZenggeProtocol.STEP_STROBE, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }
        ]));
      } else if (pat === 'BLINK') {
        writeToDevice(ZenggeProtocol.setCustomModeCompact([
          { mode: ZenggeProtocol.STEP_JUMP, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }
        ]));
      }
    },
    [writeToDevice, sendColor]
  );

  /** Emergency (hazard) lighting pattern — red/yellow/white for HALOZ and SOULZ */
  const applyEmergencyPattern = useCallback(
    (spd: number, bright: number) => {
      if (!writeToDevice) return;
      const factor = bright / 100;
      const profile = getLocalProfileById(hwSettings?.type || '');
      const isRingShape = profile?.vizShape === 'RING';
      const hwSpd = Math.min(spd, ZenggeProtocol.ANIM_SPEED_MAX);

      const red = { r: Math.round(255 * factor), g: 0, b: 0 };
      const white = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: Math.round(255 * factor) };
      const yellow = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: 0 };
      const off = { r: 0, g: 0, b: 0 };

      let arr: { r: number; g: number; b: number }[];

      if (isRingShape) {
        // ── HALOZ 2-segment: 8-LED frame mirrored to 16 ──
        const frame8 = [red, red, yellow, off, yellow, off, white, white];
        const mirror8 = [...frame8].reverse();
        arr = [...frame8, ...mirror8];
      } else {
        // ── SOULZ linear: [rear RED×4][mid flash×8][front WHITE×4] ──
        arr = [
          red, red, red, red,
          yellow, off, yellow, off, yellow, off, yellow, off,
          white, white, white, white,
        ];
      }

      // 0x03 = RunningWater: hardware scrolls mid section natively
      writeToDevice(ZenggeProtocol.setMultiColor(arr, hwSpd, 1, 0x03));
    },
    [writeToDevice, hwSettings]
  );

  /** Send music mode configuration to hardware */
  const handleMusicChange = useCallback(
    (
      patternId: number,
      sens: number,
      bright: number,
      src: 'APP' | 'DEVICE',
      color1Hex: string,
      color2Hex: string,
      matrix: number
    ) => {
      if (!writeToDevice) return;

      const isDeviceMic = src === 'DEVICE';
      const c1 = hexToRgb(color1Hex);
      const c2 = hexToRgb(color2Hex);

      AppLogger.log("MUSIC_CONFIG_REQUESTED", { patternId, c1Hex: color1Hex, c2Hex: color2Hex, matrix });

      writeToDevice(ZenggeProtocol.setMusicConfig(
        isDeviceMic,
        matrix,
        patternId,
        c1,
        c2,
        sens,
        bright
      ));
    },
    [writeToDevice]
  );

  return {
    sendColor,
    applyFixedPattern,
    applyStaticModePattern,
    applyEmergencyPattern,
    handleMusicChange,
    clampSpeed,
  };
}

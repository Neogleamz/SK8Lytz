/**
 * useControllerDispatch.ts — BLE hardware dispatch functions for DockedController.
 *
 * Extracted from DockedController.tsx to isolate BLE-protocol translation logic
 * from the UI component. All functions are pure command builders that delegate
 * to ZenggeProtocol and write via the provided writeToDevice callback.
 *
 * Depends on: ZenggeProtocol, AppLogger, NormalizationUtils
 */
import { useCallback, useEffect, useRef } from 'react';
import { getLocalProfileById } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { startPatternAnimation, stopPatternAnimation } from '../protocols/PatternEngine';
import { hexToRgb } from '../utils/ColorUtils';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';
import { AppLogger } from '../services/AppLogger';

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
   * Keep a ref to writeToDevice so the animation pump interval always uses
   * the latest function reference without needing to restart the loop.
   */
  const writeRef = useRef(writeToDevice);
  useEffect(() => { writeRef.current = writeToDevice; }, [writeToDevice]);

  /** Stop pump on unmount / device disconnect */
  useEffect(() => {
    return () => { stopPatternAnimation(); };
  }, []);

  /**
   * Maps UI speed slider (0–100) to Zengge hardware speed range (1–31).
   * The APK enforces 1–31 for all 0x59 animated patterns.
   */
  const clampSpeed = useCallback(
    (uiSpeed: number): number => normalizeUISpeedToHardware(uiSpeed),
    []
  );

  /** Send a solid color instantly using setMultiColor (0x59 FREEZE) to bypass physical payload limits */
  const sendColor = useCallback(
    async (r: number, g: number, b: number) => {
      if (!writeToDevice) return;
      // 0x59 FREEZE is the true architectural ghost standard for Solid Replication without glitching/failing
      const arr = Array.from({ length: numLEDs }, () => ({ r, g, b }));
      await writeToDevice(ZenggeProtocol.setMultiColor(arr, 31, 1, 0x01)); // 0x01 = FREEZE
    },
    [writeToDevice, numLEDs]
  );

  /**
   * Apply a pattern to the hardware via the animation pump.
   *
   * - Static patterns (1–5): one-shot 0x59 FREEZE, no loop.
   * - Temporal patterns (20–22): one-shot 0x51, no loop.
   * - All others (6–19, 23–61): starts 15fps frame pump via startPatternAnimation().
   *
   * Always stops the previous pump before starting a new one.
   */
  const applyFixedPattern = useCallback(
    async (
      patternId: number,
      fg: string,
      bg: string,
      currentSpeed?: number,
      _currentBrightness?: number
    ) => {
      if (!writeToDevice) return;

      const fgRaw = hexToRgb(fg);
      const bgRaw = hexToRgb(bg);
      const speed = clampSpeed(currentSpeed ?? 50);

      // Solid Mode (Pattern 1) Override: Use standard 0x59 FREEZE via sendColor
      if (patternId === 1) {
        stopPatternAnimation();
        sendColor(fgRaw.r, fgRaw.g, fgRaw.b);
        return;
      }

      // startPatternAnimation handles static (2-5), temporal (20-22), and animated (6-19, 23-61)
      // It also calls stopPatternAnimation() internally before starting a new loop.
      startPatternAnimation(
        patternId,
        fgRaw,
        bgRaw,
        numLEDs,
        speed,
        1, // direction
        (payload) => { writeRef.current?.(payload); }
      );
    },
    [writeToDevice, sendColor, clampSpeed, numLEDs]
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
        stopPatternAnimation();
        sendColor(tR, tG, tB);
      } else if (pat === 'STROBE') {
        stopPatternAnimation();
        writeToDevice(ZenggeProtocol.setCustomModeCompact([
          { mode: ZenggeProtocol.STEP_STROBE, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }
        ]));
      } else if (pat === 'BLINK') {
        stopPatternAnimation();
        writeToDevice(ZenggeProtocol.setCustomModeCompact([
          { mode: ZenggeProtocol.STEP_JUMP, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }
        ]));
      }
    },
    [writeToDevice, sendColor]
  );

  /**
   * Emergency (hazard) lighting pattern — red/yellow/white for HALOZ and SOULZ.
   *
   * Hardware truth (confirmed 2026-04-22, Master Reference §1):
   * - HALOZ: ledPoints=8, segments=2. Send 8 elements — hardware auto-mirrors to segment 2.
   *   Sending 16 elements bypasses the segment mirror engine (each segment gets its own
   *   independent half), which is NOT a mirror. Fixed: send numLEDs (8) elements only.
   * - SOULZ: ledPoints=43 (user-adjustable for cut strips). All zones scale proportionally.
   *   Fixed: no hardcoded counts — use numLEDs throughout.
   */
  const applyEmergencyPattern = useCallback(
    (spd: number, bright: number) => {
      if (!writeToDevice) return;
      stopPatternAnimation();
      const factor = bright / 100;
      const profile = getLocalProfileById(hwSettings?.type || '');
      const isRingShape = profile?.vizShape === 'RING';
      const hwSpd = Math.min(spd, ZenggeProtocol.ANIM_SPEED_MAX);

      const red    = { r: Math.round(255 * factor), g: 0,                      b: 0                      };
      const white  = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: Math.round(255 * factor) };
      const yellow = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: 0                      };
      const off    = { r: 0,                         g: 0,                      b: 0                      };

      let arr: { r: number; g: number; b: number }[];

      if (isRingShape) {
        arr = [red, red, yellow, off, yellow, off, white, white].slice(0, numLEDs);
      } else {
        const n = numLEDs;
        const zone = Math.max(1, Math.floor(n / 3));
        const midLen = n - zone * 2;
        arr = [
          ...Array(zone).fill(red),
          ...Array.from({ length: midLen }, (_, i) => (i % 2 === 0 ? yellow : off)),
          ...Array(zone).fill(white),
        ];
      }

      // 0x00 = CASCADE: hardware scrolls the mid section natively
      writeToDevice(ZenggeProtocol.setMultiColor(arr, hwSpd, 1, 0x00));
    },
    [writeToDevice, hwSettings, numLEDs]
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
        patternId,               // musicMode 1-13
        isDeviceMic ? 0x27 : 0x26, // micSource byte
        true,                    // isOn
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

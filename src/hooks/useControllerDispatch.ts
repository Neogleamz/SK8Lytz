/**
 * useControllerDispatch.ts — BLE hardware dispatch functions for DockedController.
 *
 * Extracted from DockedController.tsx to isolate BLE-protocol translation logic
 * from the UI component. All functions are pure command builders that delegate
 * to ZenggeProtocol and write via the provided writeToDevice callback.
 *
 * IMPORTANT: writeToDevice is DockedController's optimistic wrapper, which
 * routes through parentWriteToDevice (DashboardScreen's useBLE instance).
 * Do NOT call useProtocolDispatch() here — it creates an orphan useBLE
 * instance with empty connectedDevices, silently dropping all writes.
 *
 * Depends on: ZenggeProtocol, PatternEngine, AppLogger, NormalizationUtils
 */
import { useCallback } from 'react';
import { getLocalProfileById } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { buildPatternPayload } from '../protocols/PatternEngine';
import { AppLogger } from '../services/AppLogger';
import { hexToRgb } from '../utils/ColorUtils';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';

/**
 * LRU Cache for pattern payloads to avoid re-running the Math Synthesizer on repeat taps.
 * Key format: {patternId}_{fg.r}_{fg.g}_{fg.b}_{bg.r}_{bg.g}_{bg.b}_{numLEDs}_{speed}_{brightness}
 * Capacity: 8
 */
const patternPayloadCache = new Map<string, number[]>();

type WriteFn = (payload: number[], override?: Record<string, any>) => Promise<boolean | 'partial' | void>;

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

  /** Send a solid color instantly using setMultiColor (0x59 FREEZE) to bypass physical payload limits */
  const sendColor = useCallback(
    async (r: number, g: number, b: number) => {
      if (!writeToDevice) {
        if (__DEV__) console.error("🔴 [BLE DEAD WIRE] sendColor called but writeToDevice is undefined — writes are silently dropping!");
        return;
      }
      // 0x59 FREEZE is the true architectural ghost standard for Solid Replication without glitching/failing
      const arr = Array.from({ length: numLEDs }, () => ({ r, g, b }));
      await writeToDevice(ZenggeProtocol.setMultiColor(arr, hwSettings?.ledPoints || points || 16, 31, 1, 0x01)); // 0x01 = FREEZE
    },
    [writeToDevice, numLEDs, hwSettings, points]
  );

  /**
   * Apply a fixed/custom pattern to devices.
   * Delegates to PatternEngine to bridge the Math Synthesizer logic with the hardware.
   */
  const applyFixedPattern = useCallback(
    async (
      patternId: number,
      fg: string,
      bg: string,
      currentSpeed?: number,
      currentBrightness?: number,
      currentDirection?: number
    ) => {
      if (!writeToDevice) {
        if (__DEV__) console.error("🔴 [BLE DEAD WIRE] applyFixedPattern called but writeToDevice is undefined — writes are silently dropping!");
        return;
      }

      const fgRaw = hexToRgb(fg);
      const bgRaw = hexToRgb(bg);

      // Solid Mode (Pattern 1) Override: Use standard 0x59 FREEZE
      if (patternId === 1) {
        sendColor(fgRaw.r, fgRaw.g, fgRaw.b);
        return;
      }

      // Instead of bypassing the Math Synthesizer directly to hardware, we use the
      // PatternEngine to derive identically matched 0x59 Temporal Array Payloads
      // for Spatial Patterns, OR 0x51 Temporal payloads for full-strip Temporal patterns.
      // Pattern Payload Memoization (perf: avoids 30-80ms Math Synthesizer run on repeat taps)
      const spd = clampSpeed(currentSpeed ?? 50);
      const brt = currentBrightness ?? 100;
      const dir = currentDirection ?? 1;
      const cacheKey = `${patternId}_${fgRaw.r}_${fgRaw.g}_${fgRaw.b}_${bgRaw.r}_${bgRaw.g}_${bgRaw.b}_${numLEDs}_${spd}_${brt}_${dir}`;

      let payload = patternPayloadCache.get(cacheKey);
      if (payload) {
        // Refresh LRU position
        patternPayloadCache.delete(cacheKey);
        patternPayloadCache.set(cacheKey, payload);
      } else {
        payload = buildPatternPayload(
          patternId,
          fgRaw,
          bgRaw,
          numLEDs,
          spd,
          dir,
          brt
        ) ?? undefined;
        if (payload) {
          patternPayloadCache.set(cacheKey, payload);
          // Evict oldest if exceeding capacity
          if (patternPayloadCache.size > 8) {
            const firstKey = patternPayloadCache.keys().next().value;
            if (firstKey) patternPayloadCache.delete(firstKey);
          }
        }
      }

      if (payload) writeToDevice(payload);
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
      if (!writeToDevice) {
        if (__DEV__) console.error("🔴 [BLE DEAD WIRE] applyStaticModePattern called but writeToDevice is undefined — writes are silently dropping!");
        return;
      }
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
      if (!writeToDevice) {
        if (__DEV__) console.error("🔴 [BLE DEAD WIRE] applyEmergencyPattern called but writeToDevice is undefined — writes are silently dropping!");
        return;
      }
      const factor = bright / 100;
      const profile = getLocalProfileById(hwSettings?.type || '');
      const isRingShape = profile?.vizShape === 'RING';
      const hwSpd = Math.min(spd, 31); // 31 is max anim speed for Zengge

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

      // 0x02 = Running: hardware scrolls the array natively (APK: StaticColorfulMode.Running)
      writeToDevice(ZenggeProtocol.setMultiColor(arr, hwSettings?.ledPoints || numLEDs, hwSpd, 1, 0x02));
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
      if (!writeToDevice) {
        if (__DEV__) console.error("🔴 [BLE DEAD WIRE] handleMusicChange called but writeToDevice is undefined — writes are silently dropping!");
        return;
      }

      const c1 = hexToRgb(color1Hex);
      const c2 = hexToRgb(color2Hex);

      AppLogger.log("MUSIC_CONFIG_REQUESTED", { patternId, src, c1Hex: color1Hex, c2Hex: color2Hex, matrix });

      writeToDevice(ZenggeProtocol.setMusicConfig(
        patternId,
        matrix === 0x27 ? 0x27 : 0x26,
        src === 'DEVICE',
        c1,
        c2,
        sens,
        bright
      ), { micSource: src });
    },
    [writeToDevice]
  );

  /** Send power on/off command */
  const setPower = useCallback(
    (isOn: boolean) => {
      if (!writeToDevice) {
        if (__DEV__) console.error("🔴 [BLE DEAD WIRE] setPower called but writeToDevice is undefined — writes are silently dropping!");
        return;
      }
      writeToDevice(isOn ? ZenggeProtocol.turnOn() : ZenggeProtocol.turnOff());
    },
    [writeToDevice]
  );

  /** Send multi-color array (used by BUILDER mode, favorites restore) */
  const setMultiColor = useCallback(
    (colors: { r: number; g: number; b: number }[], ledPoints: number, speed: number, direction: number, transitionType?: number) => {
      if (!writeToDevice) {
        if (__DEV__) console.error("🔴 [BLE DEAD WIRE] setMultiColor called but writeToDevice is undefined — writes are silently dropping!");
        return;
      }
      writeToDevice(ZenggeProtocol.setMultiColor(colors, ledPoints, speed, direction, transitionType));
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
    setPower,
    setMultiColor,
  };
}

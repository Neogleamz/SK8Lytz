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
import { IControllerProtocol } from '../protocols/IControllerProtocol';
import { AppLogger } from '../services/AppLogger';
import { hexToRgb } from '../utils/ColorUtils';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';
import type { IHardwareSettings } from '../types/dashboard.types';
import { BLE_TIMING } from '../constants/bleTimingConstants';

/**
 * LRU Cache for pattern payloads to avoid re-running the Math Synthesizer on repeat taps.
 * Key format: {patternId}_{fg.r}_{fg.g}_{fg.b}_{bg.r}_{bg.g}_{bg.b}_{numLEDs}_{speed}_{brightness}
 * Capacity: 8
 */
const patternPayloadCache = new Map<string, number[]>();

type WriteFn = (payload: number[], targetDeviceId?: string | Record<string, unknown>, override?: Record<string, unknown>) => Promise<boolean | 'partial' | void>;

interface UseControllerDispatchParams {
  writeToDevice?: WriteFn;
  hwSettings?: IHardwareSettings | null;
  points?: number;
  getAdapterForDevice?: (mac: string) => IControllerProtocol | undefined;
  primaryDeviceId?: string;
  connectedDevices?: { id: string }[];
}

/**
 * Provides stable BLE dispatch functions for sending solid colors, patterns,
 * music configs, and emergency lighting to connected devices.
 */
export function useControllerDispatch({ writeToDevice, hwSettings, points, getAdapterForDevice, primaryDeviceId, connectedDevices = [] }: UseControllerDispatchParams) {
  /** Resolve LED count from hw config or fallback */
  const numLEDs = Math.max(1, (hwSettings?.ledPoints as number | undefined) || points || 16);

  const safeWrite = useCallback(
    async (payload: number[], targetId?: string, override?: Record<string, unknown>) => {
      if (!writeToDevice) return Promise.resolve(false);
      try {
        const promise = override !== undefined
          ? writeToDevice(payload, targetId, override)
          : writeToDevice(payload, targetId);
        return await promise;
      } catch (e: unknown) {
        AppLogger.error('[useControllerDispatch] BLE write failed', e instanceof Error ? e.message : String(e), { payload_size: payload.length, ssi: 0 });
        return false;
      }
    },
    [writeToDevice]
  );

  // DEV diagnostic: Detect when numLEDs resolves from product defaults instead of EEPROM probe.
  // If this fires, the EEPROM 0x63 response hasn't populated hwSettings before the first dispatch.
  if (__DEV__ && !hwSettings?.detected && hwSettings?.ledPoints) {
    AppLogger.warn('[useControllerDispatch] numLEDs resolved WITHOUT EEPROM probe', 'using product defaults', {
      numLEDs, hwLedPoints: hwSettings?.ledPoints, propPoints: points, detected: hwSettings?.detected,
    });
  }

  /**
   * Maps UI speed slider (0–100) to Zengge hardware speed range (1–31).
   * The hardware natively accepts 1-100 on 0xA3 chips, but 0x59 transition speed 
   * logic enforces its own bounds. Refer to ZENGGE_PROTOCOL_BIBLE.md
   */
  const clampSpeed = useCallback(
    (uiSpeed: number): number => normalizeUISpeedToHardware(uiSpeed),
    []
  );

  /** Send a solid color instantly using setMultiColor (0x59 FREEZE) to bypass physical payload limits */
  const sendColor = useCallback(
    async (r: number, g: number, b: number) => {
      if (!writeToDevice) {
        if (__DEV__) AppLogger.error("BLE_DEAD_WIRE", "sendColor called but writeToDevice is undefined", { function: 'sendColor' });
        return;
      }
      const targets = connectedDevices.length > 0 ? connectedDevices : [{ id: primaryDeviceId ?? '' }];
      await Promise.all(targets.map(async (device) => {
        console.log('[DEBUG sendColor] device.id =', device.id, typeof device.id);
        const adapter = getAdapterForDevice?.(device.id);
        if (adapter) {
          const result = adapter.buildSolidColor(r, g, b);
          for (const p of result.packets) { await safeWrite(p, device.id); }
        } else {
          const arr = Array.from({ length: numLEDs }, () => ({ r, g, b }));
          await safeWrite(ZenggeProtocol.setMultiColor(arr, (hwSettings?.ledPoints as number | undefined) || points || 16, 31, 1, 0x01), device.id); // 0x01 = FREEZE
        }
      }));
    },
    [writeToDevice, safeWrite, numLEDs, hwSettings, points, connectedDevices, getAdapterForDevice, primaryDeviceId]
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
        if (__DEV__) AppLogger.error("BLE_DEAD_WIRE", "applyFixedPattern called but writeToDevice is undefined", { function: 'applyFixedPattern' });
        return;
      }

      const fgRaw = hexToRgb(fg);
      const bgRaw = hexToRgb(bg);

      // Solid Mode (Pattern 1) Override: Use standard 0x59 FREEZE
      if (patternId === 1) {
        await sendColor(fgRaw.r, fgRaw.g, fgRaw.b);
        return;
      }

      // Instead of bypassing the Math Synthesizer directly to hardware, we use the
      // PatternEngine to derive identically matched 0x59 Temporal Array Payloads
      // for Spatial Patterns, OR 0x51 Temporal payloads for full-strip Temporal patterns.
      // Pattern Payload Memoization (perf: avoids 30-80ms Math Synthesizer run on repeat taps)
      const spd = clampSpeed(currentSpeed ?? 50);
      const brt = currentBrightness ?? 100;
      const dir = currentDirection ?? 1;
      const targets = connectedDevices.length > 0 ? connectedDevices : [{ id: primaryDeviceId ?? '' }];
      
      await Promise.all(targets.map(async (device) => {
        const adapter = getAdapterForDevice?.(device.id);
        const protocolKey = adapter ? adapter.constructor.name : 'ZenggeProtocol';
        const cacheKey = `${protocolKey}_${patternId}_${fgRaw.r}_${fgRaw.g}_${fgRaw.b}_${bgRaw.r}_${bgRaw.g}_${bgRaw.b}_${numLEDs}_${spd}_${brt}_${dir}`;

        let payload = patternPayloadCache.get(cacheKey);
        if (payload) {
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
            brt,
            undefined, // options
            undefined, // hardwareLedPoints
            adapter    // protocol
          ) ?? undefined;
          if (payload) {
            patternPayloadCache.set(cacheKey, payload);
            if (patternPayloadCache.size > 8) {
              const firstKey = patternPayloadCache.keys().next().value;
              if (firstKey) patternPayloadCache.delete(firstKey);
            }
          }
        }

        if (payload) await safeWrite(payload, device.id);
      }));
    },
    [writeToDevice, safeWrite, sendColor, clampSpeed, numLEDs, getAdapterForDevice, primaryDeviceId, connectedDevices]
  );

  /** Apply static/strobe/blink mode pattern */
  const applyStaticModePattern = useCallback(
    async (
      pat: 'STATIC' | 'STROBE' | 'BLINK',
      selectedColor: string,
      speed: number,
      r?: number,
      g?: number,
      b?: number,
      spd?: number
    ) => {
      if (!writeToDevice) {
        if (__DEV__) AppLogger.error("BLE_DEAD_WIRE", "applyStaticModePattern called but writeToDevice is undefined", { function: 'applyStaticModePattern' });
        return;
      }
      const pR = parseInt(selectedColor.slice(1, 3), 16);
      const pG = parseInt(selectedColor.slice(3, 5), 16);
      const pB = parseInt(selectedColor.slice(5, 7), 16);
      const tR = r !== undefined ? Math.max(0, Math.min(255, r | 0)) : (isNaN(pR) ? 255 : pR);
      const tG = g !== undefined ? Math.max(0, Math.min(255, g | 0)) : (isNaN(pG) ? 255 : pG);
      const tB = b !== undefined ? Math.max(0, Math.min(255, b | 0)) : (isNaN(pB) ? 255 : pB);
      const tSpd = normalizeUISpeedToHardware(spd !== undefined ? spd : speed);

      const targets = connectedDevices.length > 0 ? connectedDevices : [{ id: primaryDeviceId ?? '' }];
      
      await Promise.all(targets.map(async (device) => {
        if (pat === 'STATIC') {
          await sendColor(tR, tG, tB);
        } else if (pat === 'STROBE') {
          const adapter = getAdapterForDevice?.(device.id);
          if (adapter) {
            const result = adapter.buildCustomMode([{ mode: ZenggeProtocol.STEP_STROBE, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }]);
            for (const p of result.packets) { await safeWrite(p, device.id); }
          } else {
            await safeWrite(ZenggeProtocol.setCustomModeCompact([
              { mode: ZenggeProtocol.STEP_STROBE, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }
            ]), device.id);
          }
        } else if (pat === 'BLINK') {
          const adapter = getAdapterForDevice?.(device.id);
          if (adapter) {
            const result = adapter.buildCustomMode([{ mode: ZenggeProtocol.STEP_JUMP, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }]);
            for (const p of result.packets) { await safeWrite(p, device.id); }
          } else {
            await safeWrite(ZenggeProtocol.setCustomModeCompact([
              { mode: ZenggeProtocol.STEP_JUMP, speed: tSpd, color1: { r: tR, g: tG, b: tB }, color2: { r: 0, g: 0, b: 0 } }
            ]), device.id);
          }
        }
      }));
    },
    [writeToDevice, safeWrite, sendColor, getAdapterForDevice, primaryDeviceId, connectedDevices]
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
    async (spd: number, bright: number) => {
      if (!writeToDevice) {
        if (__DEV__) AppLogger.error("BLE_DEAD_WIRE", "applyEmergencyPattern called but writeToDevice is undefined", { function: 'applyEmergencyPattern' });
        return;
      }
      const factor = bright / 100;
      const profile = getLocalProfileById((hwSettings?.type as string | undefined) || '');
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

      // 0x02 = Running: hardware scrolls the array natively
      const targets = connectedDevices.length > 0 ? connectedDevices : [{ id: primaryDeviceId ?? '' }];
      
      await Promise.all(targets.map(async (device) => {
        const adapter = getAdapterForDevice?.(device.id);
        if (adapter) {
          const result = adapter.buildMultiColor(arr, (hwSettings?.ledPoints as number | undefined) || numLEDs, hwSpd, 1, 0x02);
          for (const p of result.packets) { await safeWrite(p, device.id); }
        } else {
          await safeWrite(ZenggeProtocol.setMultiColor(arr, (hwSettings?.ledPoints as number | undefined) || numLEDs, hwSpd, 1, 0x02), device.id);
        }
      }));
    },
    [writeToDevice, safeWrite, hwSettings, numLEDs, getAdapterForDevice, primaryDeviceId, connectedDevices]
  );

  /** Send music mode configuration to hardware */
  const handleMusicChange = useCallback(
    async (
      patternId: number,
      sens: number,
      bright: number,
      src: 'APP' | 'DEVICE',
      color1Hex: string,
      color2Hex: string,
      matrix: number
    ) => {
      if (!writeToDevice) {
        if (__DEV__) AppLogger.error("BLE_DEAD_WIRE", "handleMusicChange called but writeToDevice is undefined", { function: 'handleMusicChange' });
        return;
      }

      const c1 = hexToRgb(color1Hex);
      const c2 = hexToRgb(color2Hex);

      AppLogger.log("MUSIC_CONFIG_REQUESTED", { patternId, src, c1Hex: color1Hex, c2Hex: color2Hex, matrix });

      // BUG FIX: Previously passed `src === 'DEVICE'` as the `isOn` byte, which
      // disabled music mode (isOn=false -> 0x00) whenever APP mic was selected.
      // Per ZENGGE_PROTOCOL_BIBLE.md § 0x73 § 11 Oracle (2026-04-22):
      //   isOn (byte 1): 0x01 = activate music mode, 0x00 = deactivate
      //   Mic routing is NOT controlled here — APP mic is activated by streaming
      //   0x74 magnitude packets. DEVICE mic listens natively when 0x74 is absent.
      //   isOn must be true ONLY when Device Mic is selected (built-in hardware mic).
      //   In App Mic mode, isOn must be false (0x00) so the onboard mic is disabled
      //   and the controller expects 0x74 magnitude packets.
      const targets = connectedDevices.length > 0 ? connectedDevices : [{ id: primaryDeviceId ?? '' }];
      
      for (const device of targets) {
        const adapter = getAdapterForDevice?.(device.id);
        if (adapter) {
          const result = adapter.buildMusicConfig({
            patternId,
            matrixStyle: matrix === 0x27 ? 0x27 : 0x26,
            isOn: src === 'DEVICE',
            color1: c1,
            color2: c2,
            micSensitivity: sens,
            brightness: bright,
            speed: 50
          });
          for (const p of result.packets) {
            await safeWrite(p, device.id, { micSource: src });
            await new Promise(r => setTimeout(r, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
          }
        } else {
          await safeWrite(ZenggeProtocol.setMusicConfig(
            patternId,
            matrix === 0x27 ? 0x27 : 0x26,
            src === 'DEVICE',
            c1,
            c2,
            sens,
            bright
          ), device.id, { micSource: src });
          await new Promise(r => setTimeout(r, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
        }
      }
    },
    [writeToDevice, safeWrite, getAdapterForDevice, primaryDeviceId, connectedDevices]
  );

  /** Send power on/off command */
  const setPower = useCallback(
    async (isOn: boolean) => {
      if (!writeToDevice) {
        if (__DEV__) AppLogger.error("BLE_DEAD_WIRE", "setPower called but writeToDevice is undefined", { function: 'setPower' });
        return;
      }
      const targets = connectedDevices.length > 0 ? connectedDevices : [{ id: primaryDeviceId ?? '' }];
      
      await Promise.all(targets.map(async (device) => {
        const adapter = getAdapterForDevice?.(device.id);
        if (adapter) {
          const result = isOn ? adapter.buildPowerOn() : adapter.buildPowerOff();
          for (const p of result.packets) { await safeWrite(p, device.id); }
        } else {
          await safeWrite(isOn ? ZenggeProtocol.turnOn() : ZenggeProtocol.turnOff(), device.id);
        }
      }));
    },
    [writeToDevice, safeWrite, getAdapterForDevice, primaryDeviceId, connectedDevices]
  );

  /** Send multi-color array (used by BUILDER mode, favorites restore) */
  const setMultiColor = useCallback(
    async (colors: { r: number; g: number; b: number }[], ledPoints: number, speed: number, direction: number, transitionType?: number) => {
      if (!writeToDevice) {
        if (__DEV__) AppLogger.error("BLE_DEAD_WIRE", "setMultiColor called but writeToDevice is undefined", { function: 'setMultiColor' });
        return;
      }
      const targets = connectedDevices.length > 0 ? connectedDevices : [{ id: primaryDeviceId ?? '' }];
      
      await Promise.all(targets.map(async (device) => {
        const adapter = getAdapterForDevice?.(device.id);
        if (adapter) {
          const result = adapter.buildMultiColor(colors, ledPoints, speed, direction, transitionType);
          for (const p of result.packets) { await safeWrite(p, device.id); }
        } else {
          await safeWrite(ZenggeProtocol.setMultiColor(colors, ledPoints, speed, direction, transitionType), device.id);
        }
      }));
    },
    [writeToDevice, safeWrite, getAdapterForDevice, primaryDeviceId, connectedDevices]
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

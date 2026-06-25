import { ZenggeProtocol, HardwareSettings, HW_CONSTRAINTS, IC_TYPES, COLOR_SORTING_RGB, icTypeIndex, colorSortingIndex } from '../ZenggeProtocol';

type AppLoggerLike = {
  log: (...args: unknown[]) => void;
  warn: (...args: unknown[]) => void;
  error: (...args: unknown[]) => void;
};
let _appLogger: AppLoggerLike | null = null;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../../services/AppLogger').AppLogger; } catch (_e) { _appLogger = console; }
  }
  return _appLogger;
}

/**
   * Build a 0x59 MultiColor packet (the primary command for all IC-strip patterns).
   *
   * Payload format:
   *   [0x59, totalLen_hi, totalLen_lo, R,G,B per pixel...,
   *    numPoints_hi, numPoints_lo, transitionType, speed, direction, checksum]
   *
   * @param colors       Full per-pixel array. Length = actual device LED count.
   *                     NO 32-pixel cap — hardware supports up to 300 LEDs.
   * @param speed        Clamp to 1–31 (hardware range). Unused for Static (0x01), send 1.
   * @param direction    1 = forward, 0 = reverse
   * @param transitionType (Refer to ZENGGE_PROTOCOL_BIBLE.md):
   *   0x01 = Static  — freeze in place (Group 1 solid patterns)
   *   0x02 = Running — continuous hardware scroll (ALL animated patterns)
   *   0x03 = Strobe  — flash
   *   0x04 = Jump    — hard color jump
   *   0x05 = Breathe — breathe fade
   *   0x06 = Twinkly — twinkle
   *   ⚠️ 0x00 is NOT valid — sends undefined commandType to hardware.
   */
  export function setMultiColor(protocol: ZenggeProtocol, 
    colors: { r: number, g: number, b: number }[],
    hardwareLedPoints: number,
    speed: number,
    direction: number,
    transitionType: number = 0x01  // Default: Static/freeze (safest fallback)
  ): number[] {
    // Determine how many pixels we can safely send in a single default MTU write packet.
    // The protocol builds the full payload; the transport layer (BleWriteDispatcher) handles MTU fragmentation via chunking.
    // Therefore, pixel cap is now set to hardware max (300).
    const MAX_PIXELS = HW_CONSTRAINTS.maxPoints;

    // Enforce an absolute minimum matrix length of 12! Master Reference explicitly warns that 0x59 payloads < 10 cause hardware memory lock glitching!
    const numPoints = Math.max(12, Math.min(MAX_PIXELS, colors.length));
    const safeLedPoints = Math.max(12, hardwareLedPoints);

    // Pad the physical array out to numPoints internally if the caller under-filled it
    const paddedColors = new Array(numPoints).fill(colors[0] || { r: 0, g: 0, b: 0 });
    for (let i = 0; i < Math.min(numPoints, colors.length); i++) paddedColors[i] = colors[i];

    // Speed: hardware range 1–100 on 0xA3 chipset (oracle-confirmed 2026-04-23, Protocol Bible §0x59).
    // Clamp applied below — ANIM_SPEED_MIN=1, ANIM_SPEED_MAX=100.
    const safeSpeed = Math.max(ZenggeProtocol.ANIM_SPEED_MIN, Math.min(ZenggeProtocol.ANIM_SPEED_MAX, Math.round(speed) || 16));
    const hwDir = direction === 1 ? 1 : 0;

    const totalLen = (numPoints * 3) + 9;
    const payload = new Array(totalLen).fill(0);
    payload[0] = 0x59;
    payload[1] = (totalLen >> 8) & 0xFF;
    payload[2] = totalLen & 0xFF;
    let idx = 3;
    for (const c of paddedColors) {
      payload[idx++] = Math.max(0, Math.min(255, c.r | 0));
      payload[idx++] = Math.max(0, Math.min(255, c.g | 0));
      payload[idx++] = Math.max(0, Math.min(255, c.b | 0));
    }
    payload[idx++] = (safeLedPoints >> 8) & 0xFF;
    payload[idx++] = safeLedPoints & 0xFF;
    payload[idx++] = transitionType & 0xFF;
    payload[idx++] = safeSpeed;
    payload[idx++] = hwDir & 0xFF;
    payload[idx] = protocol.calculateChecksum(payload.slice(0, totalLen - 1));
    return protocol.wrapCommand(payload);
  }

export function oracleSceneQuery(protocol: ZenggeProtocol): number[] { const p = [0x58, 0xF0]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }

export function oracleSceneActivate(protocol: ZenggeProtocol, slot: number): number[] { const p = [0x57, slot & 0xFF, 0x32, 0x64]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }

export function oracleSceneDelete(protocol: ZenggeProtocol, slot: number): number[] { const p = [0x56, slot & 0xFF, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }


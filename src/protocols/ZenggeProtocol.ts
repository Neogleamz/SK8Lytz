/**
 * Zengge BLE Controller Protocol Implementation
 * Supports Magic Home / LEDnetWF / Zengge Symphony controllers
 *
 * Hardware Settings Protocol (from APK reverse engineering - April 2026):
 *   QUERY:  0x63 0x12 0x21 [0xF0|0x0F] [checksum]  — 5 bytes
 *   RESPONSE: 12 bytes starting with 0x63: icType, timings, points (little-endian swapped), sorting
 *   WRITE:  0x62 [ptsHigh] [ptsLow] [segHigh] [segLow] [icType] [sorting] [micPts] [micSegs] 0xF0 [cs]
 */

export const ZENGGE_SERVICE_UUID = '0000ffff-0000-1000-8000-00805f9b34fb';
export const ZENGGE_CHARACTERISTIC_UUID = '0000ff01-0000-1000-8000-00805f9b34fb';

// ─── IC TYPE TABLE (0-indexed values sent in protocol) ───────────────────────
// Source: dd/i.java → m(boolean, int) — non-mic mode list (HALOZ/SOULZ hardware)
export const IC_TYPES: Record<number, string> = {
  1:  'WS2812B',
  2:  'SM16703',
  3:  'SM16704',
  4:  'WS2811',
  5:  'UCS1903',
  6:  'SK6812',
  7:  'SK6812RGBW',
  8:  'INK1003',
  9:  'UCS2904B',
  10: 'JY1903',
  11: 'WS2812E',
};

export const IC_TYPE_NAMES = Object.values(IC_TYPES);

// ─── COLOR SORTING TABLE (0-indexed values sent in protocol) ─────────────────
// Source: SymphonySettingForA3.java → b1() — non-RGBW mode
export const COLOR_SORTING_RGB: Record<number, string> = {
  0: 'RGB',
  1: 'RBG',
  2: 'GRB',
  3: 'GBR',
  4: 'BRG',
  5: 'BGR',
};

export const COLOR_SORTING_RGBW: Record<number, string> = {
  0: 'RGBW', 1: 'RBGW', 2: 'GRBW', 3: 'GBRW', 4: 'BRGW', 5: 'BGRW',
  6: 'WRGB',  7: 'WRBG',  8: 'WGRB',  9: 'WGBR',  10: 'WBRG', 11: 'WBGR',
};

// Reverse lookups: name → index
export function icTypeIndex(name: string): number {
  return parseInt(Object.keys(IC_TYPES).find(k => IC_TYPES[parseInt(k)] === name) || '1');
}
export function colorSortingIndex(name: string): number {
  return parseInt(Object.keys(COLOR_SORTING_RGB).find(k => COLOR_SORTING_RGB[parseInt(k)] === name) || '2');
}

// ─── HARDWARE CONSTRAINTS ─────────────────────────────────────────────────────
export const HW_CONSTRAINTS = {
  maxPoints: 300,
  maxSegments: 2048,
  maxPxS: 2048,           // points × segments ≤ 2048
  maxMicPoints: 150,
  maxMicPxS: 960,         // micPoints × micSegments ≤ 960
  defaultPoints: 30,
  defaultSegments: 10,
};

// ─── SK8LYTZ DEFAULTS ─────────────────────────────────────────────────────────
export const SK8_DEFAULTS = {
  HALOZ: { points: 16, segments: 1, icType: 1, icName: 'WS2812B', sorting: 2, sortingName: 'GRB' },
  SOULZ: { points: 43, segments: 1, icType: 2, icName: 'SM16703', sorting: 2, sortingName: 'GRB' },
};

// ─── HARDWARE SETTINGS TYPES ──────────────────────────────────────────────────
export interface HardwareSettings {
  ledPoints: number;
  segments: number;
  icType: number;           // index into IC_TYPES
  icName: string;           // e.g. "WS2812B"
  colorSorting: number;     // index into COLOR_SORTING_RGB/W
  colorSortingName: string; // e.g. "GRB"
  firmwareVer?: number;
  ledVersion?: number;
  bleVersion?: number;
  detected: boolean;        // whether response came from live hardware query
}

export class ZenggeProtocol {
  private static messageCounter = 0;

  private static getSequenceCounter(): number {
    this.messageCounter = (this.messageCounter + 1) % 256;
    return this.messageCounter;
  }

  public static calculateChecksum(payload: number[]): number {
    return payload.reduce((acc, val) => acc + val, 0) & 0xFF;
  }

  public static wrapCommand(rawPayload: number[], cmdFamily: number = 0x0b): number[] {
    const payloadLen = rawPayload.length;
    const seq = this.getSequenceCounter();
    const packet = [
      0x00,
      seq & 0xFF,
      0x80,
      0x00,
      (payloadLen >> 8) & 0xFF,
      payloadLen & 0xFF,
      (payloadLen + 1) & 0xFF,
      cmdFamily
    ];
    return [...packet, ...rawPayload];
  }

  // ─── HARDWARE SETTINGS: QUERY (0x63) ───────────────────────────────────────
  /**
   * Build 5-byte query packet for reading hardware config from device.
   * Source: tc/b.java → f0(boolean)
   * Send immediately on BLE connection before any pattern commands.
   *
   * @param hasMic true if device has built-in mic (always false for HALOZ/SOULZ)
   */
  public static queryHardwareSettings(hasMic: boolean = false): number[] {
    const raw = [0x63, 0x12, 0x21, hasMic ? 0xF0 : 0x0F];
    raw.push(this.calculateChecksum(raw));
    return this.wrapCommand(raw);
  }

  // ─── HARDWARE SETTINGS: PARSE RESPONSE (0x63) ──────────────────────────────
  /**
   * Parse a 12-byte hardware settings response from device.
   * Source: tc/b.java → static class a → g(byte[])
   *
   * CRITICAL: Points bytes are LITTLE-ENDIAN SWAPPED in the response:
   *   bytes[8] = Low byte, bytes[9] = High byte  (reversed from write command)
   *
   * Validation: bytes[0]==0x63 AND bytes[11]==checksum(bytes[0..10])
   */
  public static parseHardwareSettingsResponse(raw: number[]): HardwareSettings | null {
    // Strip V2 wrapper if present (8-byte header starting with 0x00, 0x__, 0x80, 0x00)
    let payload = raw;
    if (raw.length > 12 && raw[2] === 0x80) {
      payload = raw.slice(8);
    }

    if (payload.length < 12) return null;
    if (payload[0] !== 0x63) return null;

    // Validate checksum: sum(payload[0..10]) & 0xFF == payload[11]
    const expectedCs = payload.slice(0, 11).reduce((a, b) => (a + b) & 0xFF, 0);
    if (payload[11] !== expectedCs) return null;

    const icType = payload[3] & 0xFF;
    // BYTE SWAP: payload[8]=Low, payload[9]=High (little-endian pair)
    const ledPoints = ((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF);
    const colorSorting = payload[10] & 0xFF;

    return {
      ledPoints: ledPoints || HW_CONSTRAINTS.defaultPoints,
      segments: 1,    // segments not returned in 0x63 response, default 1
      icType,
      icName: IC_TYPES[icType] || 'WS2812B',
      colorSorting,
      colorSortingName: COLOR_SORTING_RGB[colorSorting] || 'GRB',
      detected: true,
    };
  }

  // ─── HARDWARE SETTINGS: WRITE (0x62) ───────────────────────────────────────
  /**
   * Build 11-byte write packet to set hardware config on device.
   * Source: tc/b.java → C(int, int, int, int, int, int)
   *
   * Constraints (from SymphonySettingForA3.java):
   *   points: 1-300, points × segments ≤ 2048
   *   segments: 1 to 2048/points
   *   micPoints: 1-150, micPoints × micSegments ≤ 960
   */
  public static writeHardwareSettings(
    points: number,
    segments: number,
    icType: number,
    sorting: number,
    micPoints?: number,
    micSegments?: number
  ): number[] {
    // Clamp values to safe ranges
    const safePoints = Math.max(1, Math.min(HW_CONSTRAINTS.maxPoints, points));
    const maxSeg = Math.floor(HW_CONSTRAINTS.maxPxS / safePoints);
    const safeSegments = Math.max(1, Math.min(maxSeg, segments));

    const mp = micPoints ?? safePoints;
    const ms = micSegments ?? safeSegments;
    const safeMicPts = Math.max(1, Math.min(HW_CONSTRAINTS.maxMicPoints, mp));
    const maxMicSeg = Math.floor(HW_CONSTRAINTS.maxMicPxS / safeMicPts);
    const safeMicSegs = Math.max(1, Math.min(maxMicSeg, ms));

    // Big-endian pairs for points and segments
    const pHigh = (safePoints >> 8) & 0xFF;
    const pLow  = safePoints & 0xFF;
    const sHigh = (safeSegments >> 8) & 0xFF;
    const sLow  = safeSegments & 0xFF;

    const raw = [0x62, pHigh, pLow, sHigh, sLow, icType, sorting, safeMicPts, safeMicSegs, 0xF0];
    raw.push(this.calculateChecksum(raw));
    return this.wrapCommand(raw);
  }

  // ─── FIRMWARE VERSION FROM ADVERTISEMENT DATA ───────────────────────────────
  /**
   * Parse firmware version from BLE manufacturer advertisement data.
   * Source: ZGHBDevice.java → setDeviceInfo(byte[])
   *
   * Call during BLE scan callback when manufacturerData is available.
   */
  public static parseFirmwareFromAdvertisement(manufacturerDataBase64: string): {
    firmwareVer: number;
    ledVersion: number;
    bleVersion: number;
    productId: number;
  } | null {
    try {
      const buffer = require('buffer').Buffer.from(manufacturerDataBase64, 'base64');
      if (buffer.length < 15) return null;

      const bleVersion = buffer[3] & 0xFF;
      const firmwareVer = bleVersion >= 6
        ? (buffer[12] & 0xFF) | (((buffer[14] & 0xFFFF) >> 2) << 8)
        : buffer[12] & 0xFF;
      const ledVersion = buffer[13] & 0xFF;
      const productId = ((buffer[10] & 0xFF) << 8) | (buffer[11] & 0xFF);

      return { firmwareVer, ledVersion, bleVersion, productId };
    } catch {
      return null;
    }
  }

  // ─── EXISTING COMMANDS ─────────────────────────────────────────────────────

  static setColor(r: number, g: number, b: number, speed: number = 100): number[] {
    return this.setMultiColor(Array(10).fill({ r, g, b }), speed, 1, 1);
  }

  static setSymphonyColor(r: number, g: number, b: number): number[] {
    const cmd = [0x41, r, g, b, 0x01, 0x01, 0xf0];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  static setCustomRbm(patternId: number, speed: number, brightness: number): number[] {
    const speedHex = Math.max(1, Math.min(100, speed));
    const brightnessHex = Math.max(1, Math.min(100, brightness));
    const cmd = [0x42, patternId, speedHex, brightnessHex];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  public static setCandleMode(r: number, g: number, b: number, speed: number, brightness: number, amplitude: number): number[] {
    const cleanR = Math.max(0, Math.min(255, Math.round(r)));
    const cleanG = Math.max(0, Math.min(255, Math.round(g)));
    const cleanB = Math.max(0, Math.min(255, Math.round(b)));
    const invertedSpeed = 101 - Math.max(1, Math.min(100, Math.round(speed)));
    const cleanBright = Math.max(1, Math.min(100, Math.round(brightness)));
    const cleanAmp = Math.max(1, Math.min(3, Math.round(amplitude)));
    const cmd = [0x39, 0xD1, cleanR, cleanG, cleanB, invertedSpeed, cleanBright, cleanAmp];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  static setMusicConfig(
    isDeviceMic: boolean, modeType: number, patternId: number,
    color1: {r: number, g: number, b: number},
    color2: {r: number, g: number, b: number},
    sensitivity: number, brightness: number
  ): number[] {
    const payload = [0x73, isDeviceMic ? 0x01 : 0x00, modeType, patternId,
      color1.r, color1.g, color1.b, color2.r, color2.g, color2.b, 0x20, sensitivity, brightness];
    const checksum = this.calculateChecksum(payload);
    return this.wrapCommand([...payload, checksum]);
  }

  static sendMusicMagnitude(magnitude: number): number[] {
    const payload = [0x74, magnitude];
    const checksum = this.calculateChecksum(payload);
    return this.wrapCommand([...payload, checksum]);
  }

  static setMultiColor(colors: {r: number, g: number, b: number}[], speed: number, direction: number, transitionType: number = 0x00): number[] {
    let expandedColors = [...colors];
    if (expandedColors.length > 0 && expandedColors.length < 10) {
      const basePattern = [...expandedColors];
      while (expandedColors.length < 10) {
        expandedColors.push(...basePattern);
      }
    }
    const safeColors = expandedColors.slice(0, 32);
    const numPoints = safeColors.length;
    const totalLen = (numPoints * 3) + 9;
    const payload = new Array(totalLen);
    payload[0] = 0x59;
    payload[1] = (totalLen >> 8) & 0xFF;
    payload[2] = totalLen & 0xFF;
    let idx = 3;
    for (const c of safeColors) {
      payload[idx++] = c.r;
      payload[idx++] = c.g;
      payload[idx++] = c.b;
    }
    payload[idx++] = (numPoints >> 8) & 0xFF;
    payload[idx++] = numPoints & 0xFF;
    payload[idx++] = transitionType;
    payload[idx++] = speed;
    payload[idx++] = direction;
    payload[idx] = this.calculateChecksum(payload.slice(0, totalLen - 1));
    return this.wrapCommand(payload);
  }

  static setCustomMode(steps: {mode: number, speed: number, color1: {r: number, g: number, b: number}, color2: {r: number, g: number, b: number}}[]): number[] {
    const safeSteps = steps.slice(0, 32);
    const payload = new Array(291).fill(0);
    payload[0] = 0x51;
    let idx = 1;
    for (let i = 0; i < 32; i++) {
      if (i < safeSteps.length) {
        const step = safeSteps[i];
        payload[idx++] = 0xf0;
        payload[idx++] = step.mode;
        payload[idx++] = step.speed;
        payload[idx++] = step.color1.r;
        payload[idx++] = step.color1.g;
        payload[idx++] = step.color1.b;
        payload[idx++] = step.color2.r;
        payload[idx++] = step.color2.g;
        payload[idx++] = step.color2.b;
      } else {
        payload[idx++] = 0x0f;
        idx += 8;
      }
    }
    payload[289] = 0x0f;
    payload[290] = this.calculateChecksum(payload.slice(0, 290));
    return this.wrapCommand(payload);
  }

  static turnOn(): number[] {
    return this.wrapCommand([0x71, 0x23, 0x0f, 0xa3]);
  }

  static turnOff(): number[] {
    return this.wrapCommand([0x71, 0x24, 0x0f, 0xa4]);
  }

  /**
   * @deprecated Use writeHardwareSettings() instead.
   * Legacy 0x81 command kept for backward compatibility.
   */
  public static setHardwareConfig(points: number, colorOrder: string, stripType: string, segments: number = 1): number[] {
    const pointsHigh = (points >> 8) & 0xFF;
    const pointsLow = points & 0xFF;
    const segmentsByte = segments & 0xFF;
    let orderByte = 2; // GRB default (0-based index)
    if (colorOrder === 'RGB') orderByte = 0;
    else if (colorOrder === 'RBG') orderByte = 1;
    else if (colorOrder === 'GRB') orderByte = 2;
    else if (colorOrder === 'GBR') orderByte = 3;
    else if (colorOrder === 'BRG') orderByte = 4;
    else if (colorOrder === 'BGR') orderByte = 5;
    let icByte = 1;
    if (stripType.includes('WS2812B')) icByte = 1;
    else if (stripType.includes('SM16703')) icByte = 2;
    else if (stripType.includes('SM16704')) icByte = 3;
    else if (stripType.includes('WS2811'))  icByte = 4;
    else if (stripType.includes('UCS1903')) icByte = 5;
    else if (stripType.includes('SK6812'))  icByte = 6;
    const cmd = [0x81, icByte, orderByte, pointsHigh, pointsLow, segmentsByte];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum], 0x0A);
  }

  static queryHardwareConfig(): number[] {
    return this.wrapCommand([0x10, 0x00, 0x00, 0x10]);
  }

  /**
   * @deprecated Use parseHardwareSettingsResponse() for 0x63 responses.
   * Legacy parser kept for backward compatibility with 0x81/0x10 responses.
   */
  static parseHardwareConfig(payload: number[]) {
    if (!payload || payload.length < 13) return null;
    if (payload[2] !== 0x80) return null;
    const cmdHeader = payload[7];
    if (cmdHeader !== 0x0A && cmdHeader !== 0x63 && cmdHeader !== 0x81) return null;
    const icType = payload[8];
    const sortingType = payload[9];
    const pointsHigh = payload[10];
    const pointsLow = payload[11];
    const segments = payload[12];
    const points = (pointsHigh << 8) | pointsLow;
    const sorting = COLOR_SORTING_RGB[sortingType] || 'GRB';
    const stripType = IC_TYPES[icType] || 'WS2812B';
    return { points, sorting, stripType, segments };
  }

  public static setRfRemoteState(authMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', clearRemotes: boolean = false): number[] {
    let modeByte = 0x03;
    if (authMode === 'ALLOW_NONE') modeByte = 0x01;
    else if (authMode === 'ALLOW_PAIRED') modeByte = 0x02;
    const cmd = [0x2A, modeByte, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0F];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }
}

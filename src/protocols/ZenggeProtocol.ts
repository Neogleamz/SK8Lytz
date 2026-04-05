/**
 * Zengge BLE Controller Protocol Implementation
 * Supports Magic Home / LEDnetWF / Zengge Symphony controllers
 *
 * Hardware Settings Protocol (from APK reverse engineering - April 2026):
 *   QUERY:  0x63 0x12 0x21 [0xF0|0x0F] [checksum]  — 5 bytes
 *   RESPONSE: 12 bytes starting with 0x63: icType, timings, points (little-endian swapped), sorting
 *   WRITE:  0x62 [ptsHigh] [ptsLow] [segHigh] [segLow] [icType] [sorting] [micPts] [micSegs] 0xF0 [cs]
 */

export const ZENGGE_SERVICE_UUID        = '0000ffff-0000-1000-8000-00805f9b34fb';
export const ZENGGE_CHARACTERISTIC_UUID = '0000ff01-0000-1000-8000-00805f9b34fb'; // WRITE
export const ZENGGE_NOTIFY_UUID         = '0000ff02-0000-1000-8000-00805f9b34fb'; // NOTIFY (responses)

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
  // ─── COLOR SORTING UTILITY ────────────────────────────────────────────────────
  public static applyColorSorting(r: number, g: number, b: number, sortingIndex: number = 2): { r: number; g: number; b: number } {
    // 0: RGB, 1: RBG, 2: GRB, 3: GBR, 4: BRG, 5: BGR
    const map = [
      [r, g, b],
      [r, b, g],
      [g, r, b],
      [g, b, r],
      [b, r, g],
      [b, g, r],
    ];
    let idx = sortingIndex;
    if (idx < 0 || idx > 5) idx = 2; // default GRB
    return { r: map[idx][0], g: map[idx][1], b: map[idx][2] };
  }

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
    // ── Step 1: Detect and unwrap JSON envelope ──────────────────────────────
    // Newer firmware sends: [8-byte BLE header][JSON: {"code":0,"payload":"<hex>"}]
    // where <hex> is the actual binary response encoded as a hex string.
    let payload = raw;
    let isJsonFormat = false;
    try {
      const jsonStart = raw.findIndex((b, i) => b === 0x7B && raw[i+1] === 0x22); // 0x7B = '{'
      if (jsonStart !== -1) {
        const jsonStr = String.fromCharCode(...raw.slice(jsonStart));
        const parsed = JSON.parse(jsonStr);
        if (parsed?.payload && typeof parsed.payload === 'string') {
          const hexStr: string = parsed.payload;
          const innerBytes: number[] = [];
          for (let i = 0; i < hexStr.length - 1; i += 2) {
            innerBytes.push(parseInt(hexStr.slice(i, i + 2), 16));
          }
          payload = innerBytes;
          isJsonFormat = true;
        }
      }
    } catch (e) {
      // Not JSON format — fall through to binary parse
    }

    // ── Step 2: Strip V2 binary wrapper if present ──────────────────────────
    if (!isJsonFormat && payload.length > 12 && payload[2] === 0x80) {
      payload = payload.slice(8);
    }

    // ── Step 3: Verify this is a 0x63 response packet ────────────────────────
    // JSON format: [0x00, 0x63, ...] — 0x63 at index 1
    // Classic format: [0x63, ...] — 0x63 at index 0
    const is63AtIndex0 = payload[0] === 0x63;
    const is63AtIndex1 = payload[1] === 0x63;
    if (!is63AtIndex0 && !is63AtIndex1) return null;
    if (payload.length < 10) return null;

    // ── Step 4: Extract fields from correct offsets ───────────────────────────
    // JSON-inner format (0x63 at index 1):
    //   [0]=0x00  [1]=0x63  [2]=0x00  [3]=icType  [4]=0x00
    //   [5]=segments  [6]=pts_hi  [7]=pts_lo  [8]=X  [9]=colorSorting
    // Classic format (0x63 at index 0):
    //   [0]=0x63  [1]=?  [2]=?  [3]=icType  [4]=?  [5]=?  [6]=?  [7]=?
    //   [8]=pts_lo  [9]=pts_hi  [10]=colorSorting  [11]=checksum
    let icType: number, ledPoints: number, colorSorting: number;

    if (is63AtIndex1 && isJsonFormat) {
      // JSON-inner format — verified against Zengge app ground truth:
      // [0]=0x00  [1]=0x63  [2]=0x00
      // [3]=points (single byte, e.g. 0x0B=11)
      // [4]=0x00  (reserved)
      // [5]=segments (e.g. 0x02=2)
      // [6]=icType (e.g. 0x01=WS2812B)
      // [7]=colorSorting (e.g. 0x02=GRB)
      // [8]=micPoints  [9]=micSegments  [10]=flags
      icType       = payload[6] & 0xFF;
      ledPoints    = payload[3] & 0xFF;
      colorSorting = payload[7] & 0xFF;
      const parsedSegments = payload[5] & 0xFF;

      return {
        ledPoints: (ledPoints > 0 && ledPoints <= 2048) ? ledPoints : HW_CONSTRAINTS.defaultPoints,
        segments:  (parsedSegments > 0) ? parsedSegments : 1,
        icType,
        icName: IC_TYPES[icType] || 'WS2812B',
        colorSorting,
        colorSortingName: COLOR_SORTING_RGB[colorSorting] || 'GRB',
        detected: true,
      };
    } else {
      // Classic 12-byte format
      icType       = payload[3] & 0xFF;
      ledPoints    = ((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF);
      colorSorting = payload[10] & 0xFF;
    }

    // Clamp sorting to defined range
    if (colorSorting > 5) colorSorting = colorSorting & 0x07;
    if (colorSorting > 5) colorSorting = 2; // default GRB

    return {
      ledPoints: (ledPoints > 0 && ledPoints <= 2048) ? ledPoints : HW_CONSTRAINTS.defaultPoints,
      segments: 1,
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

    // 0x62 write format — mirrors 0x63 response field order (verified vs. APK):
    //   [0x62][ptsHigh][ptsLow][segHigh][segLow][icType][sorting][micPts][micSegs][0xF0][checksum]
    //
    // Points/Segments are big-endian 16-bit pairs.
    // icType: index from IC_TYPES (1=WS2812B, 2=SM16703, 6=SK6812, etc.)
    // sorting: index from COLOR_SORTING_RGB (0=RGB, 2=GRB, etc.)
    //
    // Compare with 0x63 response inner payload (JSON format):
    //   [3]=points  [5]=segments  [6]=icType  [7]=sorting
    const pHigh = (safePoints >> 8) & 0xFF;
    const pLow  = safePoints & 0xFF;
    const sHigh = (safeSegments >> 8) & 0xFF;
    const sLow  = safeSegments & 0xFF;

    const raw = [0x62, pHigh, pLow, sHigh, sLow, icType & 0xFF, sorting & 0xFF, safeMicPts, safeMicSegs, 0xF0];
    raw.push(this.calculateChecksum(raw));
    return this.wrapCommand(raw);
  }

  /**
   * Convenience wrapper around writeHardwareSettings that accepts string names.
   * Resolves IC type and color sorting strings to their protocol indexes.
   *
   * @param points    LED point count (1-300)
   * @param segments  Segment count (clamped automatically)
   * @param icName    IC type string e.g. 'WS2812B', 'SM16703', 'SK6812'
   * @param sortingName Color order string e.g. 'GRB', 'RGB', 'BGR'
   */
  public static writeHardwareSettingsByName(
    points: number,
    segments: number,
    icName: string,
    sortingName: string
  ): number[] {
    const icTypeIdx = icTypeIndex(icName);
    const sortingIdx = colorSortingIndex(sortingName);
    return this.writeHardwareSettings(points, segments, icTypeIdx, sortingIdx);
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
   * @deprecated Use writeHardwareSettingsByName() instead — this uses the OLD 0x81 command
   * which is NOT recognized by newer firmware (LEDnetWF0200 series).
   * The correct write command is 0x62 via writeHardwareSettings() / writeHardwareSettingsByName().
   */
  public static setHardwareConfig(points: number, colorOrder: string, stripType: string, segments: number = 1): number[] {
    console.warn('[ZenggeProtocol] setHardwareConfig() is deprecated — use writeHardwareSettingsByName()');
    return this.writeHardwareSettingsByName(points, segments, stripType, colorOrder);
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

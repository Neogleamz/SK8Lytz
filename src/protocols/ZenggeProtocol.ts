/**
 * Zengge BLE Controller Protocol Implementation
 * Supports Magic Home / LEDnetWF / Zengge Symphony controllers
 *
 * SINGLE SOURCE OF TRUTH:
 * For all packet formats, byte mapping, and hardware behavior, refer strictly to:
 * `tools/ZENGGE_PROTOCOL_BIBLE.md`
 */

import { Buffer } from 'buffer';

// Lazy AppLogger to avoid circular/early-init crashes on Android release builds.
// ZenggeProtocol is evaluated at bundle load time — AppLogger depends on
// supabase + expo-device + AsyncStorage which may not be ready yet.
let _appLogger: typeof import('../services/AppLogger').AppLogger | typeof console | undefined;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../services/AppLogger').AppLogger; } catch (_e: unknown) { _appLogger = console; /* intentional: AppLogger not ready yet — console is the safe fallback */ }
  }
  return _appLogger!;
}

export const ZENGGE_SERVICE_UUID = '0000ffff-0000-1000-8000-00805f9b34fb';
export const ZENGGE_CHARACTERISTIC_UUID = '0000ff01-0000-1000-8000-00805f9b34fb'; // WRITE
export const ZENGGE_NOTIFY_UUID = '0000ff02-0000-1000-8000-00805f9b34fb'; // NOTIFY (responses)

// ─── IC TYPE TABLE (0-indexed values sent in protocol) ───────────────────────
export const IC_TYPES: Record<number, string> = {
  1: 'WS2812B',
  2: 'SM16703',
  3: 'SM16704',
  4: 'WS2811',
  5: 'UCS1903',
  6: 'SK6812',
  7: 'SK6812RGBW',
  8: 'INK1003',
  9: 'UCS2904B',
  10: 'JY1903',
  11: 'WS2812E',
};

export const IC_TYPE_NAMES = Object.values(IC_TYPES);

// ─── COLOR SORTING TABLE (0-indexed values sent in protocol) ─────────────────
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
  6: 'WRGB', 7: 'WRBG', 8: 'WGRB', 9: 'WGBR', 10: 'WBRG', 11: 'WBGR',
};

// Reverse lookups: name → index
export function icTypeIndex(name: string): number {
  return parseInt(Object.keys(IC_TYPES).find(k => IC_TYPES[parseInt(k, 10)] === name) || '1', 10);
}
export function colorSortingIndex(name: string): number {
  return parseInt(Object.keys(COLOR_SORTING_RGB).find(k => COLOR_SORTING_RGB[parseInt(k, 10)] === name) || '2', 10);
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
  // ─── Instance State ──────────────────────────────────────────────────────────
  // Each ZenggeProtocol instance has its own sequence counter so that a
  // ZenggeAdapter instance (used by the HAL) and legacy static callers (the 25
  // existing consumers) don't share and corrupt each other's sequence.

  private messageCounter = 0;

  // Module-level singleton — used by all static facade methods below.
  // This preserves 100% backward compatibility: ZenggeProtocol.setMultiColor()
  // still works exactly as before for all 25 legacy consumers.
  private static readonly _instance = new ZenggeProtocol();

  /**
   * Diagnostic mode gate — must be set to true by Sk8LytzDiagnosticLab before
   * calling any condemned opcode (e.g. 0x43). Resets to false on lab unmount.
   * NEVER set this to true in production code paths.
   */
  public static DIAGNOSTIC_MODE_ENABLED = false;

  // Instance method — used by this instance and by ZenggeAdapter
  private getSequenceCounter(): number {
    this.messageCounter = (this.messageCounter + 1) % 256;
    return this.messageCounter;
  }

  // ─── Instance Methods (used by ZenggeAdapter and internally) ────────────────

  public calculateChecksum(payload: number[]): number {
    return payload.reduce((acc, val) => acc + val, 0) & 0xFF;
  }

  public wrapCommand(rawPayload: number[], cmdFamily: number = 0x0b): number[] {
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

  // ─── Static Facades (backward compatibility — DO NOT REMOVE) ─────────────────
  // All 25 existing consumers call ZenggeProtocol.method() statically.
  // These facades delegate to the singleton instance with zero behavior change.



  // ─── HARDWARE SETTINGS: QUERY (0x63) ───────────────────────────────────────
  /**
   * Build 5-byte query packet for reading hardware config from device.
   * Send immediately on BLE connection before any pattern commands.
   *
   * @param hasMic true if device has built-in mic (always false for HALOZ/SOULZ)
   */
  public queryHardwareSettings(hasMic: boolean = false): number[] {
    const raw = [0x63, 0x12, 0x21, hasMic ? 0xF0 : 0x0F];
    raw.push(this.calculateChecksum(raw));
    return this.wrapCommand(raw);
  }

  // ─── HARDWARE SETTINGS: PARSE RESPONSE (0x63) ──────────────────────────────
  /**
   * Parse a 12-byte hardware settings response from device.
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
      const jsonStart = raw.findIndex((b, i) => b === 0x7B && raw[i + 1] === 0x22); // 0x7B = '{'
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
    } catch (_e: unknown) {
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
      icType = payload[6] & 0xFF;
      ledPoints = payload[3] & 0xFF;
      colorSorting = payload[7] & 0xFF;
      const parsedSegments = payload[5] & 0xFF;

      return {
        ledPoints: (ledPoints > 0 && ledPoints <= 2048) ? ledPoints : HW_CONSTRAINTS.defaultPoints,
        segments: (parsedSegments > 0) ? parsedSegments : 1,
        icType,
        icName: IC_TYPES[icType] || 'WS2812B',
        colorSorting,
        colorSortingName: COLOR_SORTING_RGB[colorSorting] || 'GRB',
        detected: true,
      };
    } else {
      // Classic 12-byte format
      icType = payload[3] & 0xFF;
      ledPoints = ((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF);
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

  /**
   * Parse ZENGGE Manufacturer Specific Data (type 0xFF) from raw byte array.
   *
   * This is the low-level decoder used when manufacturerData arrives as a
   * number[] from the BLE scanner's advertisement callback.
   * For base64-encoded strings (from the GATT probe path), use parseFirmwareFromAdvertisement.
   *
   * Advertisement byte map (Refer to ZENGGE_PROTOCOL_BIBLE.md):
   *   [0]  = 0xA8 (ZENGGE vendor prefix high byte)
   *   [1]  = 0x01 (ZENGGE vendor prefix low byte)
   *   [2]  = flags / device class
   *   [3]  = BLE Protocol Version (e.g. 0x04 = v4)
   *   [4]–[9]  = MAC Address (6 bytes, Big-Endian)
   *   [10] = Product ID high byte
   *   [11] = Product ID low byte
   *   [12] = Firmware Version major
   *   [13] = reserved / sub-build
   *   [14] = Firmware Version minor
   */
  public static parseFirmwareFromManufacturerBytes(
    manufacturerData: number[]
  ): Pick<HardwareSettings, 'firmwareVer' | 'bleVersion'> & { productId?: number; macAddress?: string } | null {
    if (!manufacturerData || manufacturerData.length < 15) return null;
    if (manufacturerData[0] !== 0xA8 || manufacturerData[1] !== 0x01) return null;

    const bleVersion = manufacturerData[3] & 0xFF;
    const productId = ((manufacturerData[10] & 0xFF) << 8) | (manufacturerData[11] & 0xFF);
    const firmwareMaj = manufacturerData[12] & 0xFF;
    const firmwareMin = manufacturerData[14] & 0xFF;
    const firmwareVer = firmwareMaj * 100 + firmwareMin;

    const macAddress = manufacturerData
      .slice(4, 10)
      .map(b => b.toString(16).toUpperCase().padStart(2, '0'))
      .join(':');

    return { firmwareVer, bleVersion, productId, macAddress };
  }

  // ─── HARDWARE SETTINGS: WRITE (0x62) ───────────────────────────────────────
  /**
   * Build 11-byte write packet to set hardware config on device.
   *
   * Constraints (Refer to ZENGGE_PROTOCOL_BIBLE.md):
   *   points: 1-300, points × segments ≤ 2048
   *   segments: 1 to 2048/points
   *   micPoints: 1-150, micPoints × micSegments ≤ 960
   */
  public writeHardwareSettings(
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

    // 0x62 write format:
    //   [0x62][ptsHigh][ptsLow][segHigh][segLow][icType][sorting][micPts][micSegs][0xF0][checksum]
    //
    // Points/Segments are big-endian 16-bit pairs.
    // icType: index from IC_TYPES (1=WS2812B, 2=SM16703, 6=SK6812, etc.)
    // sorting: index from COLOR_SORTING_RGB (0=RGB, 2=GRB, etc.)
    //
    // Compare with 0x63 response inner payload (JSON format):
    //   [3]=points  [5]=segments  [6]=icType  [7]=sorting
    const pHigh = (safePoints >> 8) & 0xFF;
    const pLow = safePoints & 0xFF;
    const sHigh = (safeSegments >> 8) & 0xFF;
    const sLow = safeSegments & 0xFF;

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
  public writeHardwareSettingsByName(
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
      const buffer = Buffer.from(manufacturerDataBase64, 'base64');
      if (buffer.length < 15) return null;

      const bleVersion = buffer[3] & 0xFF;
      const firmwareVer = bleVersion >= 6
        ? (buffer[12] & 0xFF) | (((buffer[14] & 0xFFFF) >> 2) << 8)
        : buffer[12] & 0xFF;
      const ledVersion = buffer[13] & 0xFF;
      const productId = ((buffer[10] & 0xFF) << 8) | (buffer[11] & 0xFF);

      return { firmwareVer, ledVersion, bleVersion, productId };
    } catch (e: unknown) {
      getAppLogger().warn('[ZenggeProtocol] parseFirmwareFromAdvertisement failed', {
        error: e instanceof Error ? e.message : String(e),
      });
      return null;
    }
  }

  // ─── PROTOCOL CONSTANTS ─────────────────────────────
  // 0x51 DIY step transition modes — must use these exact values
  static readonly STEP_JUMP = 0x3A; // Hard cut between colors
  static readonly STEP_GRADUAL = 0x3B; // Smooth cross-fade between colors
  static readonly STEP_STROBE = 0x3C; // Rapid flash between colors

  // 0x59 animated pattern speed range (ORACLE-CONFIRMED 2026-04-23: 1-100, NOT 1-31)
  // Physical hardware (0xA3) responds to the full 1-100 range on the 0x59 speed byte.
  static readonly ANIM_SPEED_MIN = 1;
  static readonly ANIM_SPEED_MAX = 100; // Hardware confirmed via Oracle lab 2026-04-23
  // User-facing speed slider is 0–100. Pass directly to hardware (clamped to 1–100).

  // ─── EXISTING COMMANDS ─────────────────────────────────────────────────────




  /**
   * 0x41 Settled Mode — dual-color animated Symphony effects.
   * Used by the Symphony tab (33 built-in hardware effects).
   *
   * Format (Refer to ZENGGE_PROTOCOL_BIBLE.md):
   * Format (13 bytes): [0x41, effectId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, speed, dir, 0x00, 0xF0, checksum]
   *
   * @DEPRECATED — not wired to any production UI. DiagnosticLab only.
   * Verify via Oracle Lab before enabling in production.
   *
   * @param effectId  1–33 (Symphony effect ID)
   * @param fg        Foreground color
   * @param bg        Background color
   * @param speed     1–255 (hardware native range)
   * @param direction 0=forward, 1=reverse
   */
  public setSettledMode(
    effectId: number,
    fg: { r: number; g: number; b: number },
    bg: { r: number; g: number; b: number },
    speed: number,
    direction: 0 | 1 = 0
  ): number[] {
    const raw = [
      0x41,
      Math.max(1, Math.min(44, effectId | 0)),
      Math.min(255, Math.max(0, fg.r | 0)),
      Math.min(255, Math.max(0, fg.g | 0)),
      Math.min(255, Math.max(0, fg.b | 0)),
      Math.min(255, Math.max(0, bg.r | 0)),
      Math.min(255, Math.max(0, bg.g | 0)),
      Math.min(255, Math.max(0, bg.b | 0)),
      Math.max(1, Math.min(255, speed | 0)),
      (direction === 1 ? 0 : 1) & 0x01,
      0x00, // static trailer byte 1
      0xF0, // static trailer byte 2
    ];
    raw.push(this.calculateChecksum(raw));
    getAppLogger().log('ZENGGE_SETTLED_MODE_0x41', { effectId, speed, direction });
    return this.wrapCommand(raw);
  }

  /**
   * @deprecated Since v2.8.0 — PROGRAMS mode retired. The 0x42 opcode (RBM channel player)
   * has been superseded by 0x59 PatternEngine. Kept for DiagnosticLab forensic use only.
   * Do NOT call from any production UI path.
   */
  public setCustomRbm(patternId: number, speed: number, brightness: number): number[] {
    // FIX: Clamp effectId to confirmed 0xA3 hardware range (1–100).
    // IDs >100 are outside verified range — behavior is undefined on 0xA3.
    const clampedId = Math.min(100, Math.max(1, Math.round(patternId)));
    if (clampedId !== patternId) {
      getAppLogger().warn('ZenggeProtocol.setCustomRbm: effectId clamped to valid range', { original: patternId, clamped: clampedId });
    }
    const speedHex = Math.max(1, Math.min(100, speed));
    const brightnessHex = Math.max(1, Math.min(100, brightness));
    const cmd = [0x42, clampedId, speedHex, brightnessHex];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /**
   * @HARDWARE-DANGER: 0x43 CONDEMNED for 0xA3 hardware.
   * Oracle Lab confirmed 2026-04-22: sending 0x43 causes the LED strip to go
   * dark immediately and requires a POWER CYCLE to recover. DO NOT SEND IN PRODUCTION.
   *
   * This method is ONLY callable from Sk8LytzDiagnosticLab when
   * ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED = true.
   *
   * @see tools/ZENGGE_PROTOCOL_BIBLE.md — 0x43 condemned section
   */
  public setEffectSequence(
    effectIds: number[],
    speed: number,
    brightness: number
  ): number[] {
    if (!ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED) {
      throw new Error(
        '[HARDWARE-DANGER] 0x43 is a condemned opcode on 0xA3 hardware. ' +
        'It causes firmware fault and requires power cycle to recover. ' +
        'Set ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED = true to use in DiagnosticLab only.'
      );
    }
    const safeIds = effectIds.slice(0, 50).map(id => Math.max(1, Math.min(100, id | 0)));
    const safeSpeed = Math.max(1, Math.min(100, speed));
    const safeBright = Math.max(1, Math.min(100, brightness));
    const cmd = [0x43, safeIds.length, ...safeIds, 0x00, safeSpeed, safeBright];
    cmd.push(this.calculateChecksum(cmd));
    getAppLogger().log('ZENGGE_EFFECT_SEQ_0x43_DIAGNOSTIC_ONLY', { count: safeIds.length, speed: safeSpeed });
    return this.wrapCommand(cmd);
  }

  public setCandleMode(r: number, g: number, b: number, speed: number, brightness: number, amplitude: number): number[] {
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



  /**
   * Build 0x73 Music Config packet — Oracle-verified 13-byte format for 0xA3 hardware.
   *
   * Refer to ZENGGE_PROTOCOL_BIBLE.md
   *
   * Format (13 bytes, wrapped):
   *   [0x73, isOn, modeType, patternId, FG.r, FG.g, FG.b, BG.r, BG.g, BG.b, sensitivity, brightness, checksum]
   *
   *   isOn:       0x01 = activate music mode, 0x00 = deactivate
   *   modeType:   0x26 = Light Bar matrix (16 patterns, IDs 1–16)
   *               0x27 = Light Screen matrix (30 patterns, IDs 1–30)
   *   patternId:  1–16 for 0x26, 1–30 for 0x27 (clamped per modeType below)
   *   FG (color1): Sound Column color
   *   BG (color2): Drop color (ignored by hardware for NONE/FG_ONLY modes)
   */
  public setMusicConfig(
    musicMode: number,
    modeType: 0x26 | 0x27,
    isOn: boolean,
    color1: { r: number; g: number; b: number },
    color2: { r: number; g: number; b: number },
    sensitivity: number,
    brightness: number
  ): number[] {
    // BUG FIX: Clamp patternId to the correct ceiling for each matrix.
    // Light Bar (0x26) has only 16 patterns; Light Screen (0x27) has 30.
    // Previously hardcoded to 30, allowing IDs 17-30 to be sent to a 0x26
    // matrix — undefined hardware behavior. (Bible §0x73 §11, 2026-04-22)
    const maxPatternId = modeType === 0x27 ? 30 : 16;

    // BUG FIX: 
    // The previous assumption that Bytes 4-6 = Sound Column and Bytes 7-9 = Drop Color was SWAPPED!
    // Mic Source definition:
    //  - sb_col (Bytes 7-9) = "sound column color" (`col_color` translation)
    //  - sb_point (Bytes 4-6) = "drop color" (`point_color` translation)
    // Additionally, for Light Bar (0x26), we send the exact same color to BOTH slots.
    const dropColor = modeType === 0x26 ? color1 : color2;
    const soundColumnColor = color1;

    const payload = [
      0x73,
      isOn ? 0x01 : 0x00,
      modeType,
      Math.max(1, Math.min(maxPatternId, musicMode | 0)),
      Math.max(0, Math.min(255, dropColor.r | 0)),
      Math.max(0, Math.min(255, dropColor.g | 0)),
      Math.max(0, Math.min(255, dropColor.b | 0)),
      Math.max(0, Math.min(255, soundColumnColor.r | 0)),
      Math.max(0, Math.min(255, soundColumnColor.g | 0)),
      Math.max(0, Math.min(255, soundColumnColor.b | 0)),
      Math.max(0, Math.min(255, sensitivity | 0)),
      Math.max(0, Math.min(255, brightness | 0)),
    ];
    getAppLogger().log('ZENGGE_MUSIC_CONFIG_13B', {
      musicMode, modeType: `0x${modeType.toString(16)}`, isOn,
      c1: `${color1.r},${color1.g},${color1.b}`,
    });
    payload.push(this.calculateChecksum(payload));
    return this.wrapCommand(payload);
  }

  public sendMusicMagnitude(magnitude: number): number[] {
    const payload = [0x74, magnitude & 0xFF];
    const checksum = this.calculateChecksum(payload);
    return this.wrapCommand([...payload, checksum]);
  }

  /**
   * [HYPOTHESIS] Stream a single pixel frame via 0x53.
   *
   * Used for real-time per-frame animation rendering. Sends one complete
   * frame of pixel data at the current FPS rate.
   *
   * Hypothesis format:
   *   [0x53, dataLen_hi, dataLen_lo, R1, G1, B1, R2, G2, B2, ..., checksum]
   *   dataLen = pixelCount * 3
   *
   * Analogous to 0x59 but without animation footer bytes.
   * Hardware-verify via Oracle Phase 2 panel.
   *
   * @param pixels  Array of RGB pixels. Max 54 (safe MTU limit).
   */
  public streamPixelFrame(pixels: { r: number; g: number; b: number }[]): number[] {
    const MAX_PX = 54;
    const safePx = pixels.slice(0, MAX_PX);
    const dataLen = safePx.length * 3;
    const raw: number[] = [
      0x53,
      (dataLen >> 8) & 0xFF,
      dataLen & 0xFF,
    ];
    for (const px of safePx) {
      raw.push(Math.max(0, Math.min(255, px.r | 0)));
      raw.push(Math.max(0, Math.min(255, px.g | 0)));
      raw.push(Math.max(0, Math.min(255, px.b | 0)));
    }
    raw.push(this.calculateChecksum(raw));
    return this.wrapCommand(raw);
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
  public setMultiColor(
    colors: { r: number, g: number, b: number }[],
    hardwareLedPoints: number,
    speed: number,
    direction: number,
    transitionType: number = 0x01  // Default: Static/freeze (safest fallback)
  ): number[] {
    // Determine how many pixels we can safely send in a single default MTU write packet.
    // Assuming default safe MTU of ~180 bytes, wrapper + 0x59 headers = 16 bytes.
    // 164 bytes / 3 bytes per pixel = 54 pixels max.
    const MAX_PIXELS = 54;

    // Enforce an absolute minimum matrix length of 12! Master Reference explicitly warns that 0x59 payloads < 10 cause hardware memory lock glitching!
    const numPoints = Math.max(12, Math.min(MAX_PIXELS, colors.length));
    const safeLedPoints = Math.max(12, hardwareLedPoints);

    // Pad the physical array out to numPoints internally if the caller under-filled it
    const paddedColors = new Array(numPoints).fill(colors[0] || { r: 0, g: 0, b: 0 });
    for (let i = 0; i < Math.min(numPoints, colors.length); i++) paddedColors[i] = colors[i];

    // Speed mapping: user-facing 0–100 → hardware 1–31.
    // Previously 0x00 (CASCADE) was HARDCODED to speed=1 — that was wrong, made animations look frozen.
    // Fix: pass speed through for ALL transition types, properly clamped to 1–31.
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
    payload[idx] = this.calculateChecksum(payload.slice(0, totalLen - 1));
    return this.wrapCommand(payload);
  }



  /**
   * Old fixed 32-step custom mode packet logic removed. 
   * Now proxies directly to setCustomModeCompact to ensure safe MTU size.
   */
  public setCustomMode(steps: {
    mode: number;  // 1-33 (0x01-0x21) for Custom Effects, or 0x3A, 0x3B, 0x3C for Standard
    speed: number; // 1–100
    color1: { r: number, g: number, b: number }; // foreground
    color2: { r: number, g: number, b: number }; // background
  }[]): number[] {
    return this.setCustomModeCompact(steps);
  }

  /**
   * Build a COMPACT 0x51 packet with only the provided active steps (no padding to 32 slots).
   *
   * Format: [0x51, step0(9), step1(9), ..., 0x0F, checksum]
   * For 1 step: 12 bytes raw → 20 bytes wrapped — fits ANY BLE MTU.
   */
  public setCustomModeCompact(steps: {
    mode: number;
    speed: number;
    color1: { r: number, g: number, b: number };
    color2: { r: number, g: number, b: number };
  }[]): number[] {
    // 18 steps * 9 bytes/step + 11 overhead bytes = 173 bytes. Fits safely within 180 byte MTU limit.
    const safeSteps = steps.slice(0, 18);
    // Variable-length: only active steps
    const raw: number[] = [0x51];
    for (const step of safeSteps) {
      const safeSpeed = Math.max(1, Math.min(100, Math.round(step.speed)));
      raw.push(0xF0); // active
      raw.push(step.mode & 0xFF);
      raw.push(safeSpeed);
      raw.push(Math.max(0, Math.min(255, step.color1.r | 0)));
      raw.push(Math.max(0, Math.min(255, step.color1.g | 0)));
      raw.push(Math.max(0, Math.min(255, step.color1.b | 0)));
      raw.push(Math.max(0, Math.min(255, step.color2.r | 0)));
      raw.push(Math.max(0, Math.min(255, step.color2.g | 0)));
      raw.push(Math.max(0, Math.min(255, step.color2.b | 0)));
    }
    raw.push(0x0F); // terminator
    raw.push(this.calculateChecksum(raw.slice(0, raw.length)));
    getAppLogger().log('ZENGGE_CUSTOM_MODE_COMPACT', { bytes: raw.length, steps: safeSteps.length });
    return this.wrapCommand(raw);
  }

  /**
   * Build a 10B-SLOT EXTENDED packet but send it as a COMPACT (variable length) payload.
   * This bypasses the broken 0x40 chunking protocol while still sending the 10th byte (direction flag).
   * 
   * Format: [0x51, step0(10), step1(10), ..., 0x0F, checksum]
   * For 1 step: 13 bytes raw -> 21 bytes wrapped — fits ANY BLE MTU.
   */
  public setCustomModeExtendedCompact(steps: {
    mode: number;
    speed: number;
    color1: { r: number, g: number, b: number };
    color2: { r: number, g: number, b: number };
    dir?: number;
  }[]): number[] {
    // 16 steps * 10 bytes/step + 11 overhead bytes = 171 bytes. Fits safely within 180 byte MTU limit.
    const safeSteps = steps.slice(0, 16);
    // Variable-length: only active steps
    const raw: number[] = [0x51];
    for (const step of safeSteps) {
      const safeSpeed = Math.max(1, Math.min(100, Math.round(step.speed)));
      raw.push(0xF0); // active
      raw.push(step.mode & 0xFF);
      raw.push(safeSpeed);
      raw.push(Math.max(0, Math.min(255, step.color1.r | 0)));
      raw.push(Math.max(0, Math.min(255, step.color1.g | 0)));
      raw.push(Math.max(0, Math.min(255, step.color1.b | 0)));
      raw.push(Math.max(0, Math.min(255, step.color2.r | 0)));
      raw.push(Math.max(0, Math.min(255, step.color2.g | 0)));
      raw.push(Math.max(0, Math.min(255, step.color2.b | 0)));
      raw.push((step.dir ?? 0x00) & 0xFF); // 10th byte: direction flag!
    }
    raw.push(0x0F); // terminator
    raw.push(this.calculateChecksum(raw.slice(0, raw.length)));
    getAppLogger().log('ZENGGE_CUSTOM_MODE_EXT_COMPACT', { bytes: raw.length, steps: safeSteps.length });
    return this.wrapCommand(raw);
  }

  /**
   * Build a 323-byte EXTENDED 0x51 packet required by 0xA3 hardware (product_id=163).
   *
   * Uses 32 fixed-slot format with 10 bytes
   * per slot (vs 9-byte compact format used by 0xA2 hardware).
   *
   * Format: [0x51, slot0(10B), slot1(10B), ..., slot31(10B), 0x0F, checksum]
   * Each slot (10 bytes): [active, mode, speed, R1, G1, B1, R2, G2, B2, dir]
   *   active: 0xF0 = active, 0x00 = inactive (empty slot)
   *   dir:    0x01 = forward, 0x00 = reverse (byte 9, ABSENT in 9B compact format)
   *
   * Total: 1 + 32×10 + 1 + 1 = 323 bytes.
   *
   * ⚠️ Phase 1 Smoking Gun B: if 0xA3 hardware accepts this but rejects the
   *    291B compact format, this is the source of truth for production.
   *
   * @param steps Active steps (1–32). Remaining slots zero-filled as inactive.
   * @param dir   Default direction byte for all slots. Default 0x01 (forward).
   */
  public setCustomModeExtended(steps: {
    mode: number;
    speed: number;
    color1: { r: number; g: number; b: number };
    color2: { r: number; g: number; b: number };
    dir?: number;
  }[], dir: number = 0x81): number[] { // 0x80 = segment mirroring, 0x01 = forward, 0x00 = reverse
    const TOTAL_SLOTS = 32;
    const raw: number[] = [0x51];

    for (let i = 0; i < TOTAL_SLOTS; i++) {
      const step = steps[i];
      if (step) {
        const safeSpeed = Math.max(1, Math.min(100, Math.round(step.speed)));
        raw.push(0xF0); // active
        raw.push(step.mode & 0xFF);
        raw.push(safeSpeed);
        raw.push(Math.max(0, Math.min(255, step.color1.r | 0)));
        raw.push(Math.max(0, Math.min(255, step.color1.g | 0)));
        raw.push(Math.max(0, Math.min(255, step.color1.b | 0)));
        raw.push(Math.max(0, Math.min(255, step.color2.r | 0)));
        raw.push(Math.max(0, Math.min(255, step.color2.g | 0)));
        raw.push(Math.max(0, Math.min(255, step.color2.b | 0)));
        raw.push((step.dir ?? dir) & 0xFF); // direction byte — required in 10B format
      } else {
        // Inactive slot — 10 bytes, first byte 0x0F
        raw.push(0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00);
      }
    }

    raw.push(0x0F); // terminator
    raw.push(this.calculateChecksum(raw));
    getAppLogger().log('ZENGGE_CUSTOM_MODE_323B', { bytes: raw.length, steps: steps.length });
    return raw; // Do NOT wrap - writeChunked handles the 0x40 chunk framing envelope
  }

  public turnOn(): number[] {
    return this.wrapCommand([0x71, 0x23, 0x0f, 0xa3]);
  }

  public turnOff(): number[] {
    return this.wrapCommand([0x71, 0x24, 0x0f, 0xa4]);
  }

  /**
   * Convenience alias — use in Diagnostic Lab and UI code for readability.
   * Delegates to turnOn() / turnOff().
   */
  public setPower(on: boolean): number[] {
    return on ? this.turnOn() : this.turnOff();
  }




  /**
   * Build a 0x10 session time sync packet.
   * ZENGGE app sends this on EVERY successful BLE connection handshake,
   * before any pattern commands. Without it, the hardware session clock
   * starts from epoch 0 — timing-sensitive effects may drift or misfire.
   *
   * Format (8 bytes + checksum):
   *   [0x10, year-2000, month(1-12), day(1-31), hour(0-23), min(0-59), sec(0-59), weekday(0=Sun), checksum]
   */
  public setSessionTime(): number[] {
    const now = new Date();
    const raw = [
      0x10,
      (now.getFullYear() - 2000) & 0xFF, // year offset from 2000
      now.getMonth() + 1,                 // 1–12
      now.getDate(),                      // 1–31
      now.getHours(),                     // 0–23
      now.getMinutes(),                   // 0–59
      now.getSeconds(),                   // 0–59
      now.getDay(),                       // 0=Sun, 1=Mon, ..., 6=Sat
    ];
    raw.push(this.calculateChecksum(raw));
    return this.wrapCommand(raw);
  }


  /**
   * @deprecated Use parseHardwareSettingsResponse() for 0x63 responses.
   * Legacy parser kept for backward compatibility with older command responses.
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

  // ─── RF REMOTE CONTROL (0x2A) ─────────────────────────────────────────────────
  //
  // Packet structure (inner payload, 15 bytes + checksum):
  //   [0x2A][modeByte][0xFF][0xFF][0xFF][0xFF][0xFF][clearByte][0x00...][0x0F]
  //
  // modeByte:  0x01 = ALLOW_ALL (any RF remote in range can control device)
  //            0x02 = ALLOW_NONE (block all RF remotes — also used when clearing)
  //            0x03 = ALLOW_PAIRED (locked to exclusively paired remote UID only)
  //
  // clearByte: 0x00 = keep existing paired remotes
  //            0x01 = clear/unpair all paired remotes
  //
  // NOTE: Physical pairing requires power-cycle + hold remote button during first
  // 5 seconds of boot — cannot be triggered over BLE. Only mode changes and
  // clearing are possible via BLE.
  //
  public setRfRemoteState(
    authMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED',
    clearRemotes: boolean = false
  ): number[] {
    let modeByte = 0x01; // ALLOW_ALL default
    if (authMode === 'ALLOW_NONE') modeByte = 0x02;
    else if (authMode === 'ALLOW_PAIRED') modeByte = 0x03;

    const clearByte = clearRemotes ? 0x01 : 0x00;
    const cmd = [0x2A, modeByte, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, clearByte, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0F];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /** Convenience: clear all paired remotes while keeping same auth mode */
  public clearRfRemotes(currentMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED' = 'ALLOW_PAIRED'): number[] {
    return this.setRfRemoteState(currentMode, true);
  }

  /** Query current RF remote auth mode — device responds with 0x2B packet */
  public queryRfRemoteState(): number[] {
    const cmd = [0x2B, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /**
   * Parses a 0x2B response packet to determine the current RF auth mode.
   * Inner payload byte[0] = 0x2B, byte[1] = modeByte
   */
  public static parseRfRemoteState(payload: number[]): {
    mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED';
    modeName: string;
    pairedCount: number;
    pairedRemoteIds: string[];
  } | null {
    // Response is wrapped — locate the 0x2B marker in the inner payload
    const innerStart = payload.findIndex(b => b === 0x2B);
    if (innerStart < 0 || innerStart + 1 >= payload.length) return null;
    const modeByte = payload[innerStart + 1];
    const pairedCount = payload[innerStart + 2] ?? 0;
    const modeMap: Record<number, 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'> = {
      0x01: 'ALLOW_ALL',
      0x02: 'ALLOW_NONE',
      0x03: 'ALLOW_PAIRED',
    };
    const mode = modeMap[modeByte] ?? 'ALLOW_ALL';
    const modeNames: Record<string, string> = {
      ALLOW_ALL: 'Allow All Remotes',
      ALLOW_NONE: 'Block All Remotes',
      ALLOW_PAIRED: 'Paired Remote Only',
    };

    const pairedRemoteIds: string[] = [];
    let offset = innerStart + 3;
    for (let i = 0; i < pairedCount; i++) {
      if (offset + 4 <= payload.length) {
        const idBytes = payload.slice(offset, offset + 4);
        const hexId = idBytes.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join('');
        pairedRemoteIds.push(hexId);
        offset += 4;
      }
    }

    return { mode, modeName: modeNames[mode], pairedCount, pairedRemoteIds };
  }

  // ─── DIAGNOSTIC ORACLE COMMANDS ──────────────────────────────────────────────
  public oracleMusicMic26(): number[] { return [0x73, 0x01, 0x26, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; }
  public oracleMusicMic27(): number[] { return [0x73, 0x01, 0x27, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; }
  public oracleMusicOff(): number[] { return [0x73, 0x01, 0x26, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; }
  public oracleMusicMic00(): number[] { return [0x73, 0x01, 0x00, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; }
  public oracleMusicMissingIsOn(): number[] { return [0x73, 0x00, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; }
  public oracleSceneQuery(): number[] { return [0x58, 0xF0]; }
  public oracleSceneActivate(slot: number): number[] { return [0x57, slot & 0xFF, 0x32, 0x64]; }
  public oracleSceneDelete(slot: number): number[] { return [0x56, slot & 0xFF, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]; }

  // ─── HAL ENCLOSURE: BUFFER LOCKOUT DEFENSE (R-19) ────────────────────────────

  /**
   * Pad a pre-built 0x59 Static Colorful payload to the hardware-minimum of 12 pixels.
   *
   * PROTOCOL BIBLE §0x59 MINIMUM PIXELS:
   *   Payloads below 10 pixels cause physical controller EEPROM buffer lockouts on the 0xA3 chipset.
   *   Minimum enforced = 12 pixels for a safety margin above that threshold.
   *
   * This method is the authoritative location for this padding logic (R-19 HAL Enclosure rule).
   * The BleWriteDispatcher MUST call this instead of duplicating the byte math inline.
   *
   * @param payload  Fully-constructed 0x59 payload (already checksummed, NOT yet V2-wrapped).
   * @returns        The same payload if numLEDs >= 12, or a new padded payload otherwise.
   *                 Returns the original payload unchanged if it is malformed or not a 0x59 packet.
   */
  public static padStaticColorfulPayload(payload: number[]): number[] {
    // Guard: must be 0x59 with enough bytes to read the length header
    if (payload[0] !== 0x59 || payload.length < 10) return payload;

    const totalLen = (payload[1] << 8) | payload[2];
    const numLEDs = Math.floor((totalLen - 9) / 3);

    if (numLEDs <= 0 || numLEDs >= 12) return payload; // already safe

    const paddingPixels = 12 - numLEDs;
    const paddingBytes = paddingPixels * 3;
    const newTotalLen = totalLen + paddingBytes;

    const newPayload = new Array(newTotalLen + 1).fill(0);
    newPayload[0] = 0x59;
    newPayload[1] = (newTotalLen >> 8) & 0xFF;
    newPayload[2] = newTotalLen & 0xFF;

    const existingRgbBytes = numLEDs * 3;
    // Copy existing RGB pixel bytes
    for (let i = 0; i < existingRgbBytes; i++) {
      newPayload[3 + i] = payload[3 + i];
    }
    // Padding bytes (zeros) already filled by Array.fill(0) above

    const footerStart = 3 + existingRgbBytes;
    // Safety: require at least 5 footer bytes (numLEDs_hi, numLEDs_lo, trans, spd, dir)
    if (footerStart + 4 >= payload.length) return payload;

    const origLedPoints = (payload[footerStart] << 8) | payload[footerStart + 1];
    const safeLedPoints = Math.max(12, origLedPoints);

    let dstIdx = 3 + existingRgbBytes + paddingBytes;
    newPayload[dstIdx++] = (safeLedPoints >> 8) & 0xFF;
    newPayload[dstIdx++] = safeLedPoints & 0xFF;
    newPayload[dstIdx++] = payload[footerStart + 2]; // transitionType
    newPayload[dstIdx++] = payload[footerStart + 3]; // speed
    newPayload[dstIdx++] = payload[footerStart + 4]; // direction

    // Recompute checksum over all bytes except the last slot
    let checksum = 0;
    for (let i = 0; i < dstIdx; i++) checksum += newPayload[i];
    newPayload[dstIdx] = checksum & 0xFF;

    return newPayload;
  }

  /**
   * Build the ZENGGE 0x40 chunked frame sequence for a large payload.
   *
   * PROTOCOL BIBLE §0x51 ZENGGE BLE Chunked Framing:
   *   [0x40, seqNum, offset_lo, offset_hi, totalLen_hi, totalLen_lo, dataLen+1, 0x0B, ...data]  (first chunk)
   *   [0x40, seqNum, indexWord_hi, indexWord_lo, dataLen, ...data]                                (subsequent)
   *   Last chunk has bit 15 of indexWord set (0x8000 OR).
   *
   * This method is the authoritative 0x40 frame builder (R-19 HAL Enclosure rule).
   * The BleWriteDispatcher MUST call this instead of building frames inline.
   *
   * @param payload    The inner payload bytes to chunk (e.g. a 323-byte 0x51 extended packet).
   * @param chunkSize  Maximum bytes per BLE write (derived from negotiated MTU - 3).
   * @param seqByte    Random sequence byte (0x00–0xFF) for this transmission session.
   * @returns          Array of ready-to-send BLE write buffers.
   */
  public static buildChunkedFrames(payload: number[], chunkSize: number, seqByte: number): number[][] {
    const totalLen = payload.length;
    const chunks: number[][] = [];
    let offset = 0;
    let segmentIndex = 0;

    while (offset < totalLen) {
      const isFirstChunk = segmentIndex === 0;
      const headerSize = isFirstChunk ? 8 : 5;
      const maxDataLen = chunkSize - headerSize;
      const dataLen = Math.min(maxDataLen, totalLen - offset);
      const isLastChunk = (offset + dataLen) >= totalLen;

      let indexWord = segmentIndex;
      if (isLastChunk) indexWord |= 0x8000;

      const chunk = [
        0x40,
        seqByte & 0xFF,
        (indexWord >> 8) & 0xFF,
        indexWord & 0xFF,
      ];

      if (isFirstChunk) {
        chunk.push((totalLen >> 8) & 0xFF);
        chunk.push(totalLen & 0xFF);
        chunk.push((dataLen + 1) & 0xFF); // ZENGGE counts the 0x0B byte in the data length
        chunk.push(0x0B);
      } else {
        chunk.push(dataLen & 0xFF);
      }

      chunk.push(...payload.slice(offset, offset + dataLen));
      chunks.push(chunk);
      offset += dataLen;
      segmentIndex++;
    }

    return chunks;
  }
}

export namespace ZenggeProtocol {
  const _shared = new ZenggeProtocol();

  export function calculateChecksum(payload: number[]): number { return _shared.calculateChecksum(payload); }
  export function wrapCommand(rawPayload: number[], cmdFamily: number = 0x0b): number[] { return _shared.wrapCommand(rawPayload, cmdFamily); }

  export function queryHardwareSettings(hasMic: boolean = false): number[] { return _shared.queryHardwareSettings(hasMic); }
  export function writeHardwareSettings(points: number, segments: number, icType: number, sorting: number, micPoints?: number, micSegments?: number): number[] { return _shared.writeHardwareSettings(points, segments, icType, sorting, micPoints, micSegments); }
  export function writeHardwareSettingsByName(points: number, segments: number, icName: string, sortingName: string): number[] { return _shared.writeHardwareSettingsByName(points, segments, icName, sortingName); }
  
  export function setSettledMode(effectId: number, fg: { r: number; g: number; b: number }, bg: { r: number; g: number; b: number }, speed: number, direction: 0 | 1 = 0): number[] { return _shared.setSettledMode(effectId, fg, bg, speed, direction); }
  export function setCustomRbm(patternId: number, speed: number, brightness: number): number[] { return _shared.setCustomRbm(patternId, speed, brightness); }
  export function setEffectSequence(effectIds: number[], speed: number, brightness: number): number[] { return _shared.setEffectSequence(effectIds, speed, brightness); }
  export function setCandleMode(r: number, g: number, b: number, speed: number, brightness: number, amplitude: number): number[] { return _shared.setCandleMode(r, g, b, speed, brightness, amplitude); }
  
  export function setMusicConfig(musicMode: number, modeType: 0x26 | 0x27, isOn: boolean, color1: { r: number; g: number; b: number }, color2: { r: number; g: number; b: number }, sensitivity: number, brightness: number): number[] { return _shared.setMusicConfig(musicMode, modeType, isOn, color1, color2, sensitivity, brightness); }
  export function sendMusicMagnitude(magnitude: number): number[] { return _shared.sendMusicMagnitude(magnitude); }
  export function streamPixelFrame(pixels: { r: number; g: number; b: number }[]): number[] { return _shared.streamPixelFrame(pixels); }
  
  export function setMultiColor(colors: { r: number, g: number, b: number }[], hardwareLedPoints: number, speed: number, direction: number, transitionType: number = 0x01): number[] { return _shared.setMultiColor(colors, hardwareLedPoints, speed, direction, transitionType); }
  export function setCustomMode(steps: { mode: number; speed: number; color1: { r: number, g: number, b: number }; color2: { r: number, g: number, b: number }; }[]): number[] { return _shared.setCustomMode(steps); }
  export function setCustomModeCompact(steps: { mode: number; speed: number; color1: { r: number, g: number, b: number }; color2: { r: number, g: number, b: number }; }[]): number[] { return _shared.setCustomModeCompact(steps); }
  export function setCustomModeExtendedCompact(steps: { mode: number; speed: number; color1: { r: number, g: number, b: number }; color2: { r: number, g: number, b: number }; dir?: number; }[]): number[] { return _shared.setCustomModeExtendedCompact(steps); }
  export function setCustomModeExtended(steps: { mode: number; speed: number; color1: { r: number; g: number; b: number }; color2: { r: number; g: number; b: number }; dir?: number; }[], dir: number = 0x81): number[] { return _shared.setCustomModeExtended(steps, dir); }
  
  export function turnOn(): number[] { return _shared.turnOn(); }
  export function turnOff(): number[] { return _shared.turnOff(); }
  export function setPower(on: boolean): number[] { return _shared.setPower(on); }
  export function setSessionTime(): number[] { return _shared.setSessionTime(); }
  
  export function setRfRemoteState(authMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', clearRemotes: boolean = false): number[] { return _shared.setRfRemoteState(authMode, clearRemotes); }
  export function clearRfRemotes(currentMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED' = 'ALLOW_PAIRED'): number[] { return _shared.clearRfRemotes(currentMode); }
  export function queryRfRemoteState(): number[] { return _shared.queryRfRemoteState(); }

  export function oracleMusicMic26(): number[] { return _shared.oracleMusicMic26(); }
  export function oracleMusicMic27(): number[] { return _shared.oracleMusicMic27(); }
  export function oracleMusicOff(): number[] { return _shared.oracleMusicOff(); }
  export function oracleMusicMic00(): number[] { return _shared.oracleMusicMic00(); }
  export function oracleMusicMissingIsOn(): number[] { return _shared.oracleMusicMissingIsOn(); }
  export function oracleSceneQuery(): number[] { return _shared.oracleSceneQuery(); }
  export function oracleSceneActivate(slot: number): number[] { return _shared.oracleSceneActivate(slot); }
  export function oracleSceneDelete(slot: number): number[] { return _shared.oracleSceneDelete(slot); }
  // Note: padStaticColorfulPayload and buildChunkedFrames are public static on the class.
  // Callers use ZenggeProtocol.padStaticColorfulPayload() / ZenggeProtocol.buildChunkedFrames() directly.
  // Do NOT add namespace re-exports for static methods — TypeScript merges class+namespace scope,
  // causing TS2300 duplicate identifier errors.
}


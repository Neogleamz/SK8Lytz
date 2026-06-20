// Acknowledged Monolith (S4): This file is a monolith > 30KB (approx. 53.4KB). Component extraction is flagged for future refactoring.
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
let _appLogger: typeof import('../services/appLogger').AppLogger | typeof console | undefined;
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
  public getSequenceCounter(): number {
    this.messageCounter = (this.messageCounter + 1) % 256;
    return this.messageCounter;
  }

  public static getNextChunkSeqByte(): number {
    return ZenggeProtocol.sharedInstance.getSequenceCounter();
  }

  /**
   * Returns the shared singleton instance used by all namespace facade methods.
   * Exposes _instance so the namespace and any static callers share ONE
   * sequence counter — prevents split-brain SeqNum corruption (PROTOCOL_CORE-004).
   *
   * Cited Truth: ZENGGE_PROTOCOL_BIBLE.md §2 V2 BLE Packet Framing:
   *   SeqNum must be a monotonically incrementing per-session value.
   *   Two independent counters on the same logical connection corrupt the sequence.
   */
  public static get sharedInstance(): ZenggeProtocol {
    return ZenggeProtocol._instance;
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

  
  public queryHardwareSettings(hasMic: boolean = false): number[] {
    return require('./handlers/hardwareSettingsHandler').queryHardwareSettings(this, hasMic);
  }

  
  public static parseHardwareSettingsResponse(raw: number[]): HardwareSettings | null {
    return require('./handlers/hardwareSettingsHandler').parseHardwareSettingsResponse(raw);
  }

  
  public static parseFirmwareFromManufacturerBytes(
    manufacturerData: number[]
  ): Pick<HardwareSettings, 'firmwareVer' | 'bleVersion'> & { productId?: number; macAddress?: string } | null {
    return require('./handlers/hardwareSettingsHandler').parseFirmwareFromManufacturerBytes(manufacturerData);
  }

  
  public writeHardwareSettings(
    points: number,
    segments: number,
    icType: number,
    sorting: number,
    micPoints?: number,
    micSegments?: number
  ): number[] {
    return require('./handlers/hardwareSettingsHandler').writeHardwareSettings(this, points, segments, icType, sorting, micPoints, micSegments);
  }

  
  public writeHardwareSettingsByName(
    points: number,
    segments: number,
    icName: string,
    sortingName: string
  ): number[] {
    return require('./handlers/hardwareSettingsHandler').writeHardwareSettingsByName(this, points, segments, icName, sortingName);
  }

  
  public static parseFirmwareFromAdvertisement(manufacturerDataBase64: string): {
    firmwareVer: number;
    ledVersion: number;
    bleVersion: number;
    productId: number;
  } | null {
    return require('./handlers/hardwareSettingsHandler').parseFirmwareFromAdvertisement(manufacturerDataBase64);
  }

  // ─── PROTOCOL CONSTANTS ─────────────────────────────
  // 0x51 DIY step transition modes — must use these exact values
  static readonly STEP_JUMP = 0x3A; // Hard cut between colors
  static readonly STEP_GRADUAL = 0x3B; // Smooth cross-fade between colors
  static readonly STEP_STROBE = 0x3C; // Rapid flash between colors

  // 0x59 animated pattern speed range (ORACLE-CONFIRMED 2026-04-23: 1-100, NOT 1-31)
  // Physical hardware (0xA3) responds to the full 1-100 range on the 0x59 speed byte.
  static readonly ANIM_SPEED_MIN = 1;
  static readonly ANIM_SPEED_MAX = 100; 
  public setSettledMode(
    effectId: number,
    fg: { r: number; g: number; b: number },
    bg: { r: number; g: number; b: number },
    speed: number,
    direction: 0 | 1 = 0
  ): number[] {
    return require('./handlers/dynamicEffectHandler').setSettledMode(this, effectId, fg, bg, speed, direction);
  }

  
  public setCustomRbm(patternId: number, speed: number, brightness: number): number[] {
    return require('./handlers/dynamicEffectHandler').setCustomRbm(this, patternId, speed, brightness);
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
    return require('./handlers/dynamicEffectHandler').setCandleMode(this, r, g, b, speed, brightness, amplitude);
  }



  
  public setMusicConfig(
    musicMode: number,
    modeType: 0x26 | 0x27,
    isOn: boolean,
    color1: { r: number; g: number; b: number },
    color2: { r: number; g: number; b: number },
    sensitivity: number,
    brightness: number
  ): number[] {
    return require('./handlers/musicModeHandler').setMusicConfig(this, musicMode, modeType, isOn, color1, color2, sensitivity, brightness);
  }

  public sendMusicMagnitude(magnitude: number): number[] {
    return require('./handlers/dynamicEffectHandler').sendMusicMagnitude(this, magnitude);
  }

  
  public streamPixelFrame(pixels: { r: number; g: number; b: number }[]): number[] {
    return require('./handlers/dynamicEffectHandler').streamPixelFrame(this, pixels);
  }

  
  public setMultiColor(
    colors: { r: number, g: number, b: number }[],
    hardwareLedPoints: number,
    speed: number,
    direction: number,
    transitionType: number = 0x01  // Default: Static/freeze (safest fallback)
  ): number[] {
    return require('./handlers/staticColorHandler').setMultiColor(this, colors, hardwareLedPoints, speed, direction, transitionType);
  }



  
  public setCustomMode(steps: {
    mode: number;  // 1-33 (0x01-0x21) for Custom Effects, or 0x3A, 0x3B, 0x3C for Standard
    speed: number; // 1–100
    color1: { r: number, g: number, b: number }; // foreground
    color2: { r: number, g: number, b: number }; // background
  }[]): number[] {
    return require('./handlers/dynamicEffectHandler').setCustomMode(this, steps);
  }

  
  public setCustomModeCompact(steps: {
    mode: number;
    speed: number;
    color1: { r: number, g: number, b: number };
    color2: { r: number, g: number, b: number };
  }[]): number[] {
    return require('./handlers/dynamicEffectHandler').setCustomModeCompact(this, steps);
  }

  
  public setCustomModeExtendedCompact(steps: {
    mode: number;
    speed: number;
    color1: { r: number, g: number, b: number };
    color2: { r: number, g: number, b: number };
    dir?: number;
  }[]): number[] {
    return require('./handlers/dynamicEffectHandler').setCustomModeExtendedCompact(this, steps);
  }

  
  public setCustomModeExtended(steps: {
    mode: number;
    speed: number;
    color1: { r: number; g: number; b: number };
    color2: { r: number; g: number; b: number };
    dir?: number;
  }[], dir: number = 0x81): number[] {
    return require('./handlers/dynamicEffectHandler').setCustomModeExtended(this, steps, dir);
  }

  public turnOn(): number[] {
    return require('./handlers/stateHandler').turnOn(this);
  }

  public turnOff(): number[] {
    return require('./handlers/stateHandler').turnOff(this);
  }

  
  public setPower(on: boolean): number[] {
    return require('./handlers/stateHandler').setPower(this, on);
  }




  
  public setSessionTime(): number[] {
    return require('./handlers/stateHandler').setSessionTime(this);
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

  
  public clearRfRemotes(currentMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED' = 'ALLOW_PAIRED'): number[] {
    return require('./handlers/stateHandler').clearRfRemotes(this, currentMode);
  }

  
  public queryRfRemoteState(): number[] {
    return require('./handlers/stateHandler').queryRfRemoteState(this);
  }

  
  public static parseRfRemoteState(payload: number[]): {
    mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED';
    modeName: string;
    pairedCount: number;
    pairedRemoteIds: string[];
  } | null {
    return require('./handlers/stateHandler').parseRfRemoteState(payload);
  }

  
  public oracleMusicMic26(): number[] {
    return require('./handlers/musicModeHandler').oracleMusicMic26(this);
  }
  public oracleMusicMic27(): number[] {
    return require('./handlers/musicModeHandler').oracleMusicMic27(this);
  }
  public oracleMusicOff(): number[] {
    return require('./handlers/musicModeHandler').oracleMusicOff(this);
  }
  public oracleMusicMic00(): number[] {
    return require('./handlers/musicModeHandler').oracleMusicMic00(this);
  }
  public oracleMusicMissingIsOn(): number[] {
    return require('./handlers/musicModeHandler').oracleMusicMissingIsOn(this);
  }
  public oracleSceneQuery(): number[] {
    return require('./handlers/staticColorHandler').oracleSceneQuery(this);
  }
  public oracleSceneActivate(slot: number): number[] {
    return require('./handlers/staticColorHandler').oracleSceneActivate(this, slot);
  }
  public oracleSceneDelete(slot: number): number[] {
    return require('./handlers/staticColorHandler').oracleSceneDelete(this, slot);
  }

  
  public static padStaticColorfulPayload(payload: number[]): number[] {
    return require('./handlers/legacyHandler').padStaticColorfulPayload(payload);
  }

  
  public static buildChunkedFrames(payload: number[], chunkSize: number, seqByte: number): number[][] {
    return require('./handlers/legacyHandler').buildChunkedFrames(payload, chunkSize, seqByte);
  }
}

export namespace ZenggeProtocol {
  // PROTOCOL_CORE-004 FIX: Reuse the class-level singleton (_instance via sharedInstance)
  // instead of creating a third independent ZenggeProtocol instance.
  // Previously, this _shared had its own messageCounter running independently of
  // ZenggeProtocol._instance (used by getNextChunkSeqByte). Result: legacy namespace
  // callers (ZenggeProtocol.wrapCommand) and chunked-frame sequence bytes diverged,
  // producing duplicate or mismatched SeqNum within the same BLE session.
  // Cited Truth: ZENGGE_PROTOCOL_BIBLE.md §2 — SeqNum must be monotonically incrementing.
  const _shared = ZenggeProtocol.sharedInstance;

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


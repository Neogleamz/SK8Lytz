import { ZenggeProtocol, HardwareSettings, HW_CONSTRAINTS, IC_TYPES, COLOR_SORTING_RGB, icTypeIndex, colorSortingIndex } from '../ZenggeProtocol';

let _appLogger: any;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../../services/AppLogger').AppLogger; } catch (_e) { _appLogger = console; }
  }
  return _appLogger;
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
  export function setMusicConfig(protocol: ZenggeProtocol, 
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
    payload.push(protocol.calculateChecksum(payload));
    return protocol.wrapCommand(payload);
  }

// ─── DIAGNOSTIC ORACLE COMMANDS ──────────────────────────────────────────────
  export function oracleMusicMic26(protocol: ZenggeProtocol): number[] { const p = [0x73, 0x01, 0x26, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }

export function oracleMusicMic27(protocol: ZenggeProtocol): number[] { const p = [0x73, 0x01, 0x27, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }

export function oracleMusicOff(protocol: ZenggeProtocol): number[] { const p = [0x73, 0x01, 0x26, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }

export function oracleMusicMic00(protocol: ZenggeProtocol): number[] { const p = [0x73, 0x01, 0x00, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }

export function oracleMusicMissingIsOn(protocol: ZenggeProtocol): number[] { const p = [0x73, 0x00, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64]; p.push(protocol.calculateChecksum(p)); return protocol.wrapCommand(p); }


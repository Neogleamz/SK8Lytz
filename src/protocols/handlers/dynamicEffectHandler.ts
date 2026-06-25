import { ZenggeProtocol, HardwareSettings, HW_CONSTRAINTS, IC_TYPES, COLOR_SORTING_RGB, icTypeIndex, colorSortingIndex } from '../ZenggeProtocol';

let _appLogger: any;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../../services/AppLogger').AppLogger; } catch (_e) { _appLogger = console; }
  }
  return _appLogger;
}

// Hardware confirmed via Oracle lab 2026-04-23
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
  export function setSettledMode(protocol: ZenggeProtocol, 
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
      direction & 0x01, // Protocol Bible §0x41: 1=reverse, 0=forward
      0x00, // static trailer byte 1
      0xF0, // static trailer byte 2
    ];
    raw.push(protocol.calculateChecksum(raw));
    getAppLogger().log('ZENGGE_SETTLED_MODE_0x41', { effectId, speed, direction });
    return protocol.wrapCommand(raw);
  }

/**
   * @deprecated Since v2.8.0 — PROGRAMS mode retired. The 0x42 opcode (RBM channel player)
   * has been superseded by 0x59 PatternEngine. Kept for DiagnosticLab forensic use only.
   * Do NOT call from any production UI path.
   */
  export function setCustomRbm(protocol: ZenggeProtocol, patternId: number, speed: number, brightness: number): number[] {
    // FIX: Clamp effectId to confirmed 0xA3 hardware range (1–100).
    // IDs >100 are outside verified range — behavior is undefined on 0xA3.
    const clampedId = Math.min(100, Math.max(1, Math.round(patternId)));
    if (clampedId !== patternId) {
      getAppLogger().warn('ZenggeProtocol.setCustomRbm: effectId clamped to valid range', { original: patternId, clamped: clampedId });
    }
    const speedHex = Math.max(1, Math.min(100, speed));
    const brightnessHex = Math.max(1, Math.min(100, brightness));
    const cmd = [0x42, clampedId, speedHex, brightnessHex];
    const checksum = protocol.calculateChecksum(cmd);
    return protocol.wrapCommand([...cmd, checksum]);
  }

export function setCandleMode(protocol: ZenggeProtocol, r: number, g: number, b: number, speed: number, brightness: number, amplitude: number): number[] {
    const cleanR = Math.max(0, Math.min(255, Math.round(r)));
    const cleanG = Math.max(0, Math.min(255, Math.round(g)));
    const cleanB = Math.max(0, Math.min(255, Math.round(b)));
    const invertedSpeed = 101 - Math.max(1, Math.min(100, Math.round(speed)));
    const cleanBright = Math.max(1, Math.min(100, Math.round(brightness)));
    const cleanAmp = Math.max(1, Math.min(3, Math.round(amplitude)));
    const cmd = [0x39, 0xD1, cleanR, cleanG, cleanB, invertedSpeed, cleanBright, cleanAmp];
    const checksum = protocol.calculateChecksum(cmd);
    return protocol.wrapCommand([...cmd, checksum]);
  }

export function sendMusicMagnitude(protocol: ZenggeProtocol, magnitude: number): number[] {
    const payload = [0x74, magnitude & 0xFF];
    const checksum = protocol.calculateChecksum(payload);
    return protocol.wrapCommand([...payload, checksum]);
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
   * @param pixels  Array of RGB pixels (up to HW_CONSTRAINTS.maxPoints = 300).
   *                Transport layer (BleWriteDispatcher) handles MTU chunking.
   *                NOTE: Oracle Lab 2026-04-22 found 0x53 produced NO response on 0xA3.
   *                See ZENGGE_PROTOCOL_BIBLE.md Section 11 Phase 2 Extended Panels.
   */
  export function streamPixelFrame(protocol: ZenggeProtocol, pixels: { r: number; g: number; b: number }[]): number[] {
    const safePx = pixels.slice(0, HW_CONSTRAINTS.maxPoints);
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
    raw.push(protocol.calculateChecksum(raw));
    return protocol.wrapCommand(raw);
  }

/**
   * Old fixed 32-step custom mode packet logic removed. 
   * Now proxies directly to setCustomModeCompact to ensure safe MTU size.
   */
  export function setCustomMode(protocol: ZenggeProtocol, steps: {
    mode: number;  // 1-33 (0x01-0x21) for Custom Effects, or 0x3A, 0x3B, 0x3C for Standard
    speed: number; // 1–100
    color1: { r: number, g: number, b: number }; // foreground
    color2: { r: number, g: number, b: number }; // background
  }[]): number[] {
    return setCustomModeCompact(protocol, steps);
  }

/**
   * Build a COMPACT 0x51 packet with only the provided active steps (no padding to 32 slots).
   *
   * Format: [0x51, step0(9), step1(9), ..., 0x0F, checksum]
   * For 1 step: 12 bytes raw → 20 bytes wrapped — fits ANY BLE MTU.
   */
  export function setCustomModeCompact(protocol: ZenggeProtocol, steps: {
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
    raw.push(protocol.calculateChecksum(raw.slice(0, raw.length)));
    getAppLogger().log('ZENGGE_CUSTOM_MODE_COMPACT', { bytes: raw.length, steps: safeSteps.length });
    return protocol.wrapCommand(raw);
  }

/**
   * Build a 10B-SLOT EXTENDED packet but send it as a COMPACT (variable length) payload.
   * This bypasses the broken 0x40 chunking protocol while still sending the 10th byte (direction flag).
   * 
   * Format: [0x51, step0(10), step1(10), ..., 0x0F, checksum]
   * For 1 step: 13 bytes raw -> 21 bytes wrapped — fits ANY BLE MTU.
   */
  export function setCustomModeExtendedCompact(protocol: ZenggeProtocol, steps: {
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
    raw.push(protocol.calculateChecksum(raw.slice(0, raw.length)));
    getAppLogger().log('ZENGGE_CUSTOM_MODE_EXT_COMPACT', { bytes: raw.length, steps: safeSteps.length });
    return protocol.wrapCommand(raw);
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
  export function setCustomModeExtended(protocol: ZenggeProtocol, steps: {
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
    raw.push(protocol.calculateChecksum(raw));
    getAppLogger().log('ZENGGE_CUSTOM_MODE_323B', { bytes: raw.length, steps: steps.length });
    return raw; // Do NOT wrap - writeChunked handles the 0x40 chunk framing envelope
  }


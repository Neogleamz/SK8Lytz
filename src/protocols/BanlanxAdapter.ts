/**
 * BanlanxAdapter.ts — BanlanX SP621E Protocol Adapter (IControllerProtocol Implementation)
 *
 * Implements the IControllerProtocol HAL interface for the BanlanX SP621E
 * Mini SPI RGB Controller.
 *
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * PROTOCOL TRUTH (BANLANX_PROTOCOL_BIBLE.md §2, §3, §7, §10):
 *
 *   Packet format:  [0xA0, cmd, dataLen, ...payload]
 *   - NO sequence counter
 *   - NO checksum
 *   - NO multi-segment chunking (all packets < 20 bytes)
 *   - NO connection handshake (no 0x10 time sync equivalent)
 *
 *   GATTs (from libapp.so string extraction):
 *   - Service:  0000ffe0-0000-1000-8000-00805f9b34fb
 *   - Write:    0000ffe1-0000-1000-8000-00805f9b34fb
 *   - Notify:   0000ff12-0000-1000-8000-00805f9b34fb
 *
 *   Key difference from Zengge:
 *   - buildEffect() returns TWO packets (0x53 select + 0x54 speed)
 *     with a mandatory 20ms interPacketDelayMs.
 *   - Music mode uses hardware-native FFT — buildMusicMagnitude() is a no-op.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */

import { Buffer } from 'buffer';
import type {
  CustomModeStep,
  FirmwareInfo,
  HardwareSettingsResult,
  IControllerProtocol,
  MusicConfig,
  ProtocolResult,
  RGB,
  RfRemoteState,
} from './IControllerProtocol';

// ─── BLE GATT Constants ────────────────────────────────────────────────────────
// Source: BANLANX_PROTOCOL_BIBLE.md §2 — libapp.so string extraction

/** Primary GATT service UUID for BanlanX SP621E */
export const BANLANX_SERVICE_UUID = '0000ffe0-0000-1000-8000-00805f9b34fb';

/** Write characteristic UUID — all control commands go here */
export const BANLANX_WRITE_UUID = '0000ffe1-0000-1000-8000-00805f9b34fb';

/** Notify characteristic UUID — device state push notifications */
export const BANLANX_NOTIFY_UUID = '0000ff12-0000-1000-8000-00805f9b34fb';

// ─── Helpers ──────────────────────────────────────────────────────────────────

/** Shared empty ProtocolResult for no-op methods */
const EMPTY_RESULT: ProtocolResult = {
  packets: [],
  interPacketDelayMs: 0,
  isRateLimited: false,
};

/**
 * Build a minimal BanlanX packet: [0xA0, cmd, dataLen, ...payload]
 * Source: BANLANX_PROTOCOL_BIBLE.md §2
 */
function buildPacket(cmd: number, payload: number[]): number[] {
  return [0xA0, cmd, payload.length, ...payload];
}

// ─── Adapter ──────────────────────────────────────────────────────────────────

export class BanlanxAdapter implements IControllerProtocol {
  // ─── Identity ────────────────────────────────────────────────────────────────
  readonly protocolId = 'banlanx';
  readonly serviceUUID = BANLANX_SERVICE_UUID;
  readonly writeCharacteristicUUID = BANLANX_WRITE_UUID;
  readonly notifyCharacteristicUUID = BANLANX_NOTIFY_UUID;

  /**
   * BanlanX SP621E has a native onboard FFT engine (libwled_lfx.so).
   * The phone does NOT need to run AudioContext or stream magnitude bytes.
   * Music mode is activated by sending the audio input source opcode (0x59),
   * after which the hardware handles all audio processing internally.
   * Source: BANLANX_PROTOCOL_BIBLE.md §10
   */
  readonly requiresSoftwareFFT = false;

  // ─── Discovery ───────────────────────────────────────────────────────────────

  /**
   * Returns true if the device's BLE advertisement indicates a BanlanX SP621E.
   *
   * Primary match: FFE0 service UUID (confirmed in libapp.so, BANLANX_PROTOCOL_BIBLE.md §2).
   * Secondary match: Manufacturer ID 0x5053 ("PS" = SP reversed, little-endian = [0x53, 0x50])
   * at the start of manufacturer data (BANLANX_PROTOCOL_BIBLE.md §1).
   *
   * Registered BEFORE ZenggeAdapter in ControllerRegistry so FFE0 match is
   * tested first (BanlanX advertises FFE0; Zengge advertises FFFF — no conflict).
   */
  matchesAdvertisement(serviceUUIDs: string[], manufacturerData?: string): boolean {
    // Primary: service UUID check (most reliable)
    const hasService = serviceUUIDs.some(u =>
      u.toLowerCase().replace(/-/g, '').includes('ffe0')
    );
    if (hasService) return true;

    // Secondary: Manufacturer ID 0x5053 (bytes [0x53, 0x50] little-endian)
    if (manufacturerData) {
      try {
        const buf = Buffer.from(manufacturerData, 'base64');
        if (buf.length >= 2 && buf[0] === 0x53 && buf[1] === 0x50) return true;
      } catch (_e) { /* malformed base64 — ignore */ }
    }

    return false;
  }

  /**
   * BanlanX advertisement manufacturer data does not carry firmware version
   * in a parsed format we can use. Returns null (Phase 1).
   */
  parseFirmwareFromAdvertisement(_manufacturerDataBase64: string): FirmwareInfo | null {
    return null;
  }

  // ─── Connection Lifecycle ────────────────────────────────────────────────────

  /**
   * BanlanX requires NO connection handshake.
   * Unlike Zengge (which needs a 0x10 time sync on every connect),
   * the SP621E is ready to accept commands immediately after GATT connection.
   * Source: BANLANX_PROTOCOL_BIBLE.md §2 — "No session time sync equivalent"
   */
  getHandshakePayloads(): ProtocolResult {
    return EMPTY_RESULT;
  }

  // ─── Hardware Settings (EEPROM) ──────────────────────────────────────────────

  /**
   * BanlanX state query (0x70). Phase 1: returns empty — we don't parse the response yet.
   * Full implementation deferred to Task 2B (hardware integration testing).
   * Source: BANLANX_PROTOCOL_BIBLE.md §3 — 0x70 State Query
   */
  buildQuerySettings(_hasMic?: boolean): ProtocolResult {
    return EMPTY_RESULT;
  }

  /** BanlanX EEPROM response parsing — not implemented in Phase 1. */
  parseSettingsResponse(_raw: number[]): HardwareSettingsResult | null {
    return null;
  }

  /**
   * BanlanX: pixel count via 0x55, driver type via 0x6A.
   * Phase 1 stub — settings write is a Phase 2 feature.
   */
  buildWriteSettings(
    _points: number,
    _segments: number,
    _icType: number,
    _sorting: number
  ): ProtocolResult {
    return EMPTY_RESULT;
  }

  buildWriteSettingsByName(
    _points: number,
    _segments: number,
    _stripTypeName: string,
    _sortingName: string
  ): ProtocolResult {
    return EMPTY_RESULT;
  }

  // ─── RF Remote ───────────────────────────────────────────────────────────────

  /**
   * BanlanX SP621E has no RF remote subsystem.
   * Source: BANLANX_PROTOCOL_BIBLE.md — no RF remote opcode found.
   */
  buildQueryRfRemoteState(): ProtocolResult {
    return EMPTY_RESULT;
  }

  buildSetRfRemoteState(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', autoSave: boolean): ProtocolResult {
    return EMPTY_RESULT;
  }

  buildClearRfRemotes(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'): ProtocolResult {
    return EMPTY_RESULT;
  }

  parseRfRemoteState(_raw: number[]): RfRemoteState | null {
    return null;
  }

  // ─── Power ───────────────────────────────────────────────────────────────────

  /**
   * Power ON — [0xA0, 0x50, 0x01, 0x01]
   * Source: BANLANX_PROTOCOL_BIBLE.md §3 — 0x50 Power ON/OFF
   * Byte math: cmd=0x50, dataLen=0x01, payload=0x01 (ON)
   */
  buildPowerOn(): ProtocolResult {
    return {
      packets: [buildPacket(0x50, [0x01])],
      interPacketDelayMs: 0,
      isRateLimited: false,
    };
  }

  /**
   * Power OFF — [0xA0, 0x50, 0x01, 0x00]
   * Source: BANLANX_PROTOCOL_BIBLE.md §3 — 0x50 Power ON/OFF
   * Byte math: cmd=0x50, dataLen=0x01, payload=0x00 (OFF)
   */
  buildPowerOff(): ProtocolResult {
    return {
      packets: [buildPacket(0x50, [0x00])],
      interPacketDelayMs: 0,
      isRateLimited: false,
    };
  }

  // ─── Color & Pattern Commands ─────────────────────────────────────────────────

  /**
   * Solid color — [0xA0, 0x52, 0x03, R, G, B]
   * Source: BANLANX_PROTOCOL_BIBLE.md §3 — 0x52 Set Solid Color
   * Byte math: cmd=0x52, dataLen=0x03, payload=[R, G, B] each 0x00–0xFF
   */
  buildSolidColor(r: number, g: number, b: number): ProtocolResult {
    return {
      packets: [buildPacket(0x52, [r & 0xFF, g & 0xFF, b & 0xFF])],
      interPacketDelayMs: 0,
      isRateLimited: false,
    };
  }

  /**
   * Multi-color spatial array.
   * BanlanX native pixel streaming (DiyPixel) is Phase 2 (opcode TBD via HCI sniff).
   * Phase 1: falls back to solid color using the first color in the array.
   */
  buildMultiColor(
    colors: RGB[],
    _ledPoints: number,
    _speed: number,
    _direction: number,
    _transitionType?: number
  ): ProtocolResult {
    const first = colors[0] ?? { r: 0, g: 0, b: 0 };
    return this.buildSolidColor(first.r, first.g, first.b);
  }

  /**
   * Custom mode — BanlanX has no equivalent concept.
   * Maps to the closest built-in effect (rainbow, effectId=0x01) as a placeholder.
   * Phase 2 will use the DIY gradient system (0x90).
   */
  buildCustomMode(_steps: CustomModeStep[]): ProtocolResult {
    return EMPTY_RESULT;
  }

  /** Extended custom mode — no BanlanX equivalent. No-op. */
  buildCustomModeExtended(_steps: CustomModeStep[], _direction?: number): ProtocolResult {
    return EMPTY_RESULT;
  }

  /**
   * Built-in effect select + speed — TWO packets required.
   *
   * Source: BANLANX_PROTOCOL_BIBLE.md §3:
   *   0x53: [0xA0, 0x53, 0x01, effectId]   — select effect (1–142)
   *   0x54: [0xA0, 0x54, 0x01, speed]       — set speed (1–10 scale)
   *
   * ⚠️ CRITICAL: 20ms interPacketDelayMs is MANDATORY.
   * Hardware drops the speed packet if it arrives before the effect settles.
   * Source: BANLANX_PROTOCOL_BIBLE.md §3 §7 — "20ms interPacketDelayMs required"
   *
   * Speed conversion: caller passes 1-100, BanlanX accepts 1-10.
   * Map: Math.round(speed / 10), clamped to [1, 10].
   */
  buildEffect(effectId: number, speed: number, _brightness: number): ProtocolResult {
    const banlanxSpeed = Math.max(1, Math.min(10, Math.round(speed / 10)));
    return {
      packets: [
        buildPacket(0x53, [effectId & 0xFF]),         // effect select
        buildPacket(0x54, [banlanxSpeed]),             // speed (1-10)
      ],
      interPacketDelayMs: 20,
      isRateLimited: false,
    };
  }

  /**
   * Candle mode — no direct BanlanX equivalent.
   * Phase 1: no-op. Phase 2 candidate: map to nearest flicker effect via 0x53.
   */
  buildCandleMode(
    _r: number,
    _g: number,
    _b: number,
    _speed: number,
    _brightness: number,
    _amplitude: number
  ): ProtocolResult {
    return EMPTY_RESULT;
  }

  /**
   * Real-time pixel frame streaming — Phase 2 (opcode TBD via HCI sniff).
   * DiyPixel class confirmed in libapp.so but exact BLE framing is unknown.
   * Source: BANLANX_PROTOCOL_BIBLE.md §8 — Critical Unknown
   */
  buildStreamPixelFrame(_pixels: RGB[]): ProtocolResult {
    return EMPTY_RESULT;
  }

  // ─── Music ───────────────────────────────────────────────────────────────────

  /**
   * Activate hardware-native music reactive mode.
   *
   * Source: BANLANX_PROTOCOL_BIBLE.md §10:
   *   0x59: [0xA0, 0x59, 0x01, 0x00]  — audio input: internal mic (0x00)
   *   0x5A: [0xA0, 0x5A, 0x01, gain]  — sensitivity (1–16)
   *
   * Sensitivity conversion: caller passes 0–255, BanlanX accepts 1–16.
   * Map: Math.round((sens / 255) * 16), clamped to [1, 16].
   *
   * After sending these two packets, the SP621E's onboard FFT takes over.
   * No further app-side audio processing is needed (requiresSoftwareFFT = false).
   */
  buildMusicConfig(config: MusicConfig): ProtocolResult {
    const gain = Math.max(1, Math.min(16, Math.round((config.micSensitivity / 255) * 16)));
    return {
      packets: [
        buildPacket(0x59, [0x00]),    // audio input: internal mic
        buildPacket(0x5A, [gain]),    // sensitivity 1-16
      ],
      interPacketDelayMs: 0,
      isRateLimited: false,
    };
  }

  /**
   * Software magnitude stream — NO-OP for BanlanX.
   *
   * Source: BANLANX_PROTOCOL_BIBLE.md §10:
   * "BanlanX native FFT — we do NOT send magnitude bytes."
   * The SP621E handles all audio processing onboard via libwled_lfx.so.
   *
   * Returning empty packets causes useBLE.ts to skip the write entirely,
   * saving radio bandwidth during music mode.
   */
  buildMusicMagnitude(_magnitude: number): ProtocolResult {
    return EMPTY_RESULT;
  }

  // ─── Transport Preparation ────────────────────────────────────────────────────

  /**
   * Passthrough — BanlanX packets are always small (< 20 bytes).
   * No 0x40 chunking is needed. The ProtocolResult is returned as-is.
   * Source: BANLANX_PROTOCOL_BIBLE.md §2 — "No multi-segment chunking protocol"
   */
  prepareForTransmission(result: ProtocolResult, _mtu: number): ProtocolResult {
    return result;
  }
}

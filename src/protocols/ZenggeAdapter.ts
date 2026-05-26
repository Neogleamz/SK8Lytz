/**
 * ZenggeAdapter.ts — Zengge Protocol Adapter (IControllerProtocol Implementation)
 *
 * Implements the IControllerProtocol HAL interface for Zengge/MagicHome/LEDnetWF
 * hardware. Owns its own ZenggeProtocol instance (independent sequence counter)
 * and wraps all protocol calls to return ProtocolResult.
 *
 * This is the ONLY file that should import ZenggeProtocol directly. All other
 * files should go through the ControllerRegistry or IControllerProtocol.
 *
 * NOTE: For backward compatibility, the 25 existing consumers still call
 * ZenggeProtocol.setMultiColor() etc. statically. Those static calls delegate
 * to ZenggeProtocol._instance (the shared singleton). ZenggeAdapter uses its
 * OWN instance so adapter writes have an independent sequence counter — this
 * prevents sequence number corruption when both paths are active simultaneously.
 *
 * Transport:
 *   - Chunking: 0x40 fragmentation is handled HERE in prepareForTransmission().
 *     useBLE.ts no longer contains any chunking math.
 *   - MTU: Negotiated MTU is received by prepareForTransmission(). Chunked
 *     packets use (mtu - 3) as the safe payload limit (3 bytes ATT overhead).
 *   - Handshake: Zengge requires a 0x10 session time sync immediately after
 *     GATT connection. Returned by getHandshakePayloads().
 */

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
import {
  ZENGGE_CHARACTERISTIC_UUID,
  ZENGGE_NOTIFY_UUID,
  ZENGGE_SERVICE_UUID,
  ZenggeProtocol,
} from './ZenggeProtocol';

export class ZenggeAdapter implements IControllerProtocol {
  // ─── Instance ──────────────────────────────────────────────────────────────
  // Own ZenggeProtocol instance with independent sequence counter.
  // DO NOT use ZenggeProtocol.static* methods from here — use this.protocol.*
  private readonly protocol = new ZenggeProtocol();

  // ─── Identity ──────────────────────────────────────────────────────────────
  readonly protocolId = 'zengge';
  readonly serviceUUID = ZENGGE_SERVICE_UUID;
  readonly writeCharacteristicUUID = ZENGGE_CHARACTERISTIC_UUID;
  readonly notifyCharacteristicUUID = ZENGGE_NOTIFY_UUID;

  /**
   * Zengge uses the app's software FFT engine (AudioContext + 0x74 magnitude
   * stream). The AudioContext must stay active during music mode.
   */
  readonly requiresSoftwareFFT = true;

  // ─── Helper ────────────────────────────────────────────────────────────────
  /** Wrap a single packet into a ProtocolResult. */
  private toResult(packet: number[], isRateLimited = false): ProtocolResult {
    return { packets: [packet], interPacketDelayMs: 0, isRateLimited };
  }

  /** Wrap an empty result (no-op command). */
  private noOp(): ProtocolResult {
    return { packets: [], interPacketDelayMs: 0, isRateLimited: false };
  }

  // ─── Discovery ─────────────────────────────────────────────────────────────
  matchesAdvertisement(serviceUUIDs: string[], _manufacturerData?: string): boolean {
    // Zengge advertises service UUID 0000ffff-...
    return serviceUUIDs?.some(uuid =>
      uuid.toLowerCase().includes('ffff') &&
      uuid.toLowerCase().startsWith('0000')
    ) ?? false;
  }

  parseFirmwareFromAdvertisement(manufacturerDataBase64: string): FirmwareInfo | null {
    const result = ZenggeProtocol.parseFirmwareFromAdvertisement(manufacturerDataBase64);
    if (!result) return null;
    return {
      firmwareVer: result.firmwareVer,
      ledVersion: result.ledVersion,
      bleVersion: result.bleVersion,
      productId: result.productId,
    };
  }

  // ─── Connection Lifecycle ──────────────────────────────────────────────────
  /**
   * Zengge requires a 0x10 session time sync on EVERY connection.
   * Without it, timing-sensitive effects may drift or misfire.
   * Source: TimeControllerFragment.java (APK analysis)
   */
  getHandshakePayloads(): ProtocolResult {
    return this.toResult(ZenggeProtocol.setSessionTime());
  }

  // ─── Hardware Settings (EEPROM) ────────────────────────────────────────────
  buildQuerySettings(hasMic: boolean = false): ProtocolResult {
    return this.toResult(ZenggeProtocol.queryHardwareSettings(hasMic));
  }

  parseSettingsResponse(raw: number[]): HardwareSettingsResult | null {
    return ZenggeProtocol.parseHardwareSettingsResponse(raw);
  }

  buildWriteSettings(
    points: number,
    segments: number,
    icType: number,
    sorting: number
  ): ProtocolResult {
    return this.toResult(ZenggeProtocol.writeHardwareSettings(points, segments, icType, sorting));
  }

  buildWriteSettingsByName(
    points: number,
    segments: number,
    stripTypeName: string,
    sortingName: string
  ): ProtocolResult {
    return this.toResult(ZenggeProtocol.writeHardwareSettingsByName(points, segments, stripTypeName, sortingName));
  }

  // ─── RF Remote ─────────────────────────────────────────────────────────────
  buildQueryRfRemoteState(): ProtocolResult {
    return this.toResult(ZenggeProtocol.queryRfRemoteState());
  }

  buildSetRfRemoteState(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', autoSave: boolean): ProtocolResult {
    return this.toResult(ZenggeProtocol.setRfRemoteState(mode, autoSave));
  }

  buildClearRfRemotes(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'): ProtocolResult {
    return this.toResult(ZenggeProtocol.clearRfRemotes(mode));
  }

  parseRfRemoteState(raw: number[]): RfRemoteState | null {
    return ZenggeProtocol.parseRfRemoteState(raw);
  }

  // ─── Power ─────────────────────────────────────────────────────────────────
  /**
   * Power ON — delegates to 0x71 0x23 (APK-proven opcode).
   *
   * ⚠️ CRITICAL BUG FIX: Prior adapter used 0x56 0xAA which is the SCENE DELETE
   * opcode, not power control. 0x71 is the APK-confirmed power toggle opcode.
   * Source: ZENGGE_PROTOCOL_BIBLE.md §4 — Power Opcodes.
   */
  buildPowerOn(): ProtocolResult {
    return this.toResult(ZenggeProtocol.turnOn());
  }

  /**
   * Power OFF — delegates to 0x71 0x24 (APK-proven opcode).
   *
   * ⚠️ CRITICAL BUG FIX: Prior adapter used 0x56 0xAB. See buildPowerOn().
   */
  buildPowerOff(): ProtocolResult {
    return this.toResult(ZenggeProtocol.turnOff());
  }

  // ─── Color & Pattern Commands ──────────────────────────────────────────────
  buildSolidColor(r: number, g: number, b: number): ProtocolResult {
    return this.toResult(ZenggeProtocol.setMultiColor([{ r, g, b }], 12, 1, 1, 0x01), false);
  }

  buildMultiColor(
    colors: RGB[],
    ledPoints: number,
    speed: number,
    direction: number,
    transitionType: number = 0x02
  ): ProtocolResult {
    return this.toResult(ZenggeProtocol.setMultiColor(colors, ledPoints, speed, direction, transitionType), true);
  }

  buildCustomMode(steps: CustomModeStep[]): ProtocolResult {
    return this.toResult(ZenggeProtocol.setCustomMode(steps), true);
  }

  /**
   * Extended 0x51 format (323 bytes) for 0xA3 hardware.
   * This REQUIRES chunked transmission — prepareForTransmission() will
   * automatically apply 0x40 fragmentation when the packet exceeds MTU.
   */
  buildCustomModeExtended(steps: CustomModeStep[], direction: number = 0x80): ProtocolResult {
    const extSteps = steps.map(s => ({ mode: s.mode, speed: s.speed, color1: s.color1, color2: s.color2, dir: s.dir ?? direction }));
    return this.toResult(ZenggeProtocol.setCustomModeExtended(extSteps, direction), true);
  }

  /**
   * 0x42 RBM effect (Zengge). Single packet: [effectId, speed, brightness].
   * Note: 0x42 is deprecated in production UI — DiagnosticLab only.
   */
  buildEffect(effectId: number, speed: number, brightness: number): ProtocolResult {
    return this.toResult(ZenggeProtocol.setCustomRbm(effectId, speed, brightness), false);
  }

  buildCandleMode(r: number, g: number, b: number, speed: number, brightness: number, amplitude: number): ProtocolResult {
    return this.toResult(ZenggeProtocol.setCandleMode(r, g, b, speed, brightness, amplitude), false);
  }

  buildStreamPixelFrame(pixels: RGB[]): ProtocolResult {
    return this.toResult(ZenggeProtocol.streamPixelFrame(pixels), false);
  }

  // ─── Music ─────────────────────────────────────────────────────────────────
  buildMusicConfig(config: MusicConfig): ProtocolResult {
    // BUG FIX: Previously hardcoded to 0x26 (Light Bar), ignoring config.matrixStyle.
    // This blocked all Light Screen (0x27) patterns from ever reaching hardware.
    // matrixStyle is set by useMusicMode based on the user's matrix toggle.
    // Source: ZENGGE_PROTOCOL_BIBLE.md §0x73 — modeType controls the pattern matrix.
    return this.toResult(
      ZenggeProtocol.setMusicConfig(
        config.patternId,
        config.matrixStyle as 0x26 | 0x27,
        config.isOn ?? true,
        config.color1,
        config.color2,
        config.micSensitivity,
        config.brightness
      ),
      false
    );
  }

  /**
   * 0x74 magnitude sample for real-time audio reactivity.
   * Called at the app's FFT frame rate (30-60fps) — NOT rate-limited.
   */
  buildMusicMagnitude(magnitude: number): ProtocolResult {
    return this.toResult(ZenggeProtocol.sendMusicMagnitude(magnitude), false);
  }

  // ─── Transport Preparation ─────────────────────────────────────────────────
  /**
   * Apply MTU-aware chunking to a ProtocolResult.
   *
   * Zengge 0x40 chunk framing:
   *   Packets larger than (mtu - 3) bytes are fragmented into chunks.
   *   Each chunk is prefixed with: [0x40, chunkIndex, totalChunks]
   *   followed by up to (safeMtu - 3) bytes of payload.
   *
   * This logic was previously inline in useBLE.ts writeChunked() (L747-832).
   * Moving it here means useBLE.ts has ZERO protocol knowledge.
   *
   * Example (323-byte 0x51 extended, MTU=186):
   *   safeMtu = 186 - 3 = 183 bytes per write
   *   chunkPayloadSize = 183 - 3 (chunk header) = 180 bytes of data per chunk
   *   Chunks = ceil(323 / 180) = 2 chunks
   *   chunk0: [0x40, 0x00, 0x02, ...180 bytes...]
   *   chunk1: [0x40, 0x01, 0x02, ...143 bytes...]
   *
   * @param result  ProtocolResult to prepare.
   * @param mtu     Negotiated BLE MTU for this device.
   */
  prepareForTransmission(result: ProtocolResult, mtu: number): ProtocolResult {
    const safeMtu = mtu - 3; // Subtract 3 bytes ATT overhead
    const CHUNK_HEADER_SIZE = 3; // [0x40, chunkIndex, totalChunks]
    const chunkPayloadSize = safeMtu - CHUNK_HEADER_SIZE;

    let needsChunking = false;
    for (const packet of result.packets) {
      if (packet.length > safeMtu) {
        needsChunking = true;
        break;
      }
    }

    if (!needsChunking) return result;

    const chunkedPackets: number[][] = [];

    for (const packet of result.packets) {
      if (packet.length <= safeMtu) {
        // Packet fits in one MTU — send as-is
        chunkedPackets.push(packet);
      } else {
        // Fragment into 0x40-prefixed chunks
        const totalChunks = Math.ceil(packet.length / chunkPayloadSize);
        for (let i = 0; i < totalChunks; i++) {
          const chunkData = packet.slice(i * chunkPayloadSize, (i + 1) * chunkPayloadSize);
          chunkedPackets.push([
            0x40,              // Zengge chunk framing opcode
            i & 0xFF,          // chunk index (0-based)
            totalChunks & 0xFF, // total chunk count
            ...chunkData,
          ]);
        }
      }
    }

    return {
      packets: chunkedPackets,
      // Add 8ms inter-chunk delay when we actually chunked something
      interPacketDelayMs: chunkedPackets.length > result.packets.length
        ? Math.max(8, result.interPacketDelayMs)
        : result.interPacketDelayMs,
      isRateLimited: result.isRateLimited,
    };
  }
}

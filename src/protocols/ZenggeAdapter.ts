/**
 * ZenggeAdapter.ts — Zengge Protocol Adapter (IControllerProtocol Implementation)
 *
 * Implements the IControllerProtocol HAL interface for Zengge/MagicHome/LEDnetWF
 * hardware. Uses ZenggeProtocol.sharedInstance — all adapters share one monotonic
 * sequence counter, eliminating the split-brain where per-adapter counters
 * (PROTOCOL_CORE-004) caused controllers to reject packets with out-of-order
 * sequence bytes. Wraps all protocol calls to return ProtocolResult.
 *
 * This is the ONLY file that should import ZenggeProtocol directly. All other
 * files should go through the ControllerRegistry or IControllerProtocol.
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
  // Uses the shared singleton instance to prevent sequence counter corruption
  // between the adapter and BleWriteDispatcher (PROTOCOL_CORE-004 fix).
  private get protocol() { return ZenggeProtocol.sharedInstance; }

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
   */
  getHandshakePayloads(): ProtocolResult {
    return this.toResult(this.protocol.setSessionTime());
  }

  // ─── Hardware Settings (EEPROM) ────────────────────────────────────────────
  buildQuerySettings(hasMic: boolean = false): ProtocolResult {
    return this.toResult(this.protocol.queryHardwareSettings(hasMic));
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
    return this.toResult(this.protocol.writeHardwareSettings(points, segments, icType, sorting));
  }

  buildWriteSettingsByName(
    points: number,
    segments: number,
    stripTypeName: string,
    sortingName: string
  ): ProtocolResult {
    return this.toResult(this.protocol.writeHardwareSettingsByName(points, segments, stripTypeName, sortingName));
  }

  // ─── RF Remote ─────────────────────────────────────────────────────────────
  buildQueryRfRemoteState(): ProtocolResult {
    return this.toResult(this.protocol.queryRfRemoteState());
  }

  buildSetRfRemoteState(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', autoSave: boolean): ProtocolResult {
    return this.toResult(this.protocol.setRfRemoteState(mode, autoSave));
  }

  buildClearRfRemotes(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'): ProtocolResult {
    return this.toResult(this.protocol.clearRfRemotes(mode));
  }

  parseRfRemoteState(raw: number[]): RfRemoteState | null {
    return ZenggeProtocol.parseRfRemoteState(raw);
  }

  // ─── Power ─────────────────────────────────────────────────────────────────
  /**
   * Power ON — delegates to 0x71 0x23.
   *
   * ⚠️ CRITICAL BUG FIX: Prior adapter used 0x56 0xAA which is the SCENE DELETE
   * opcode, not power control. 0x71 is the confirmed power toggle opcode.
   */
  buildPowerOn(): ProtocolResult {
    return this.toResult(this.protocol.turnOn());
  }

  /**
   * Power OFF — delegates to 0x71 0x24.
   *
   * ⚠️ CRITICAL BUG FIX: Prior adapter used 0x56 0xAB. See buildPowerOn().
   */
  buildPowerOff(): ProtocolResult {
    return this.toResult(this.protocol.turnOff());
  }

  // ─── Color & Pattern Commands ──────────────────────────────────────────────
  buildSolidColor(r: number, g: number, b: number): ProtocolResult {
    return this.toResult(this.protocol.setMultiColor([{ r, g, b }], 12, 1, 1, 0x01), false);
  }

  buildMultiColor(
    colors: RGB[],
    ledPoints: number,
    speed: number,
    direction: number,
    transitionType: number = 0x02
  ): ProtocolResult {
    return this.toResult(this.protocol.setMultiColor(colors, ledPoints, speed, direction, transitionType), true);
  }

  buildCustomMode(steps: CustomModeStep[]): ProtocolResult {
    return this.toResult(this.protocol.setCustomMode(steps), true);
  }

  /**
   * Extended 0x51 format (323 bytes) for 0xA3 hardware.
   * Returns a raw 323-byte 0x51 extended payload (NOT V2-wrapped, NOT 0x40-chunked).
   * Chunking responsibility by caller:
   *   - writeToDevice path → _executeWriteToDeviceInternal:155 detects 0x51 + length > 200
   *     and routes to executeWriteChunked automatically. ✅ SAFE.
   *   - executeProtocolResults path (_dispatchToDevices) → has MTU guard added in F-003.
   *     ✅ SAFE after fix/protocol-dispatch-mtu-guard merged.
   *   - prepareForTransmission() is a PASS-THROUGH — it does NOT chunk. Do not rely on it.
   */
  buildCustomModeExtended(steps: CustomModeStep[], direction: number = 0x80): ProtocolResult {
    const extSteps = steps.map(s => ({ mode: s.mode, speed: s.speed, color1: s.color1, color2: s.color2, dir: s.dir ?? direction }));
    return this.toResult(this.protocol.setCustomModeExtended(extSteps, direction), true);
  }

  /**
   * 0x42 RBM effect (Zengge). Single packet: [effectId, speed, brightness].
   * Note: 0x42 is deprecated in production UI — DiagnosticLab only.
   */
  buildEffect(effectId: number, speed: number, brightness: number): ProtocolResult {
    return this.toResult(this.protocol.setCustomRbm(effectId, speed, brightness), false);
  }

  buildCandleMode(r: number, g: number, b: number, speed: number, brightness: number, amplitude: number): ProtocolResult {
    return this.toResult(this.protocol.setCandleMode(r, g, b, speed, brightness, amplitude), false);
  }

  buildStreamPixelFrame(pixels: RGB[]): ProtocolResult {
    return this.toResult(this.protocol.streamPixelFrame(pixels), false);
  }

  // ─── Music ─────────────────────────────────────────────────────────────────
  buildMusicConfig(config: MusicConfig): ProtocolResult {
    // BUG FIX: Previously hardcoded to 0x26 (Light Bar), ignoring config.matrixStyle.
    // This blocked all Light Screen (0x27) patterns from ever reaching hardware.
    // matrixStyle is set by useMusicMode based on the user's matrix toggle.
    // Refer to ZENGGE_PROTOCOL_BIBLE.md
    return this.toResult(
      this.protocol.setMusicConfig(
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
    return this.toResult(this.protocol.sendMusicMagnitude(magnitude), false);
  }

  // ─── Transport Preparation ─────────────────────────────────────────────────
  /**
   * Pass-through transport preparation (PROTOCOL_CORE-001 fix).
   *
   * 0x40 chunking is handled EXCLUSIVELY by BleWriteDispatcher (Wave 2).
   * ZenggeAdapter's role is to produce correct flat protocol payloads.
   * It must NOT perform chunking here — doing so would create a split-brain
   * where both this adapter and BleWriteDispatcher independently attempt
   * to fragment the same payload, corrupting the 0x40 segment index sequence.
   *
   * Authoritative chunking implementation: ZenggeProtocol.buildChunkedFrames()
   * Authoritative chunking caller:         BleWriteDispatcher (Wave 2 scope)
   *
   * @param result  ProtocolResult to pass through unchanged.
   * @param _mtu    Negotiated BLE MTU (unused here; consumed by BleWriteDispatcher).
   */
  prepareForTransmission(result: ProtocolResult, _mtu: number): ProtocolResult {
    // 0x40 chunking is now handled exclusively by BleWriteDispatcher.
    // ZenggeAdapter should only emit flat, protocol-level payloads.
    return result;
  }
}

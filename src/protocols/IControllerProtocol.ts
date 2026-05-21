/**
 * IControllerProtocol.ts — Hardware Abstraction Layer (HAL) Interface
 *
 * Universal interface for all SK8Lytz LED controller hardware protocols.
 * This is the seam between "what the app wants to do" (set color, run pattern,
 * query settings) and "how the hardware does it" (Zengge packet format,
 * BanlanX format, custom silicon, etc).
 *
 * Current implementations:
 *   - ZenggeAdapter (wraps existing ZenggeProtocol)
 *
 * Future implementations:
 *   - BanlanxAdapter (SP621E native 0x5X opcodes)
 *   - SimulatorAdapter (testing without hardware)
 *
 * Architecture:
 *   App Layer → IControllerProtocol → ControllerRegistry → Adapter → BLE GATT
 *
 * Return type: All build* methods return ProtocolResult — a self-describing
 * struct that bundles the BLE packets to send, the timing between them, and
 * whether the write path should be rate-limited (debounced).
 */

// ─── Core Result Type ─────────────────────────────────────────────────────────

/**
 * The universal return type for all IControllerProtocol build* methods.
 *
 * - `packets`: Ordered array of BLE payloads. Each element = one GATT write.
 *   Single-packet commands have length 1. Multi-packet commands (e.g. BanlanX
 *   0x53 + 0x54 for effect + speed) have length > 1.
 *
 * - `interPacketDelayMs`: Milliseconds to wait BETWEEN packets. 0 = fire all
 *   immediately. BanlanX effect commands require 20ms to prevent hardware drops.
 *
 * - `isRateLimited`: If true, the transport layer should apply write debounce
 *   (e.g. for rapid color cycling from a slider). If false, fire immediately
 *   (e.g. power on/off, one-shot commands).
 */
export interface ProtocolResult {
  packets: number[][];
  interPacketDelayMs: number;
  isRateLimited: boolean;
}

// ─── Shared Types ─────────────────────────────────────────────────────────────

export interface RGB {
  r: number;
  g: number;
  b: number;
}

export interface HardwareSettingsResult {
  ledPoints: number;
  segments: number;
  icType: number;
  icName: string;
  colorSorting: number;
  colorSortingName: string;
  firmwareVer?: number;
  ledVersion?: number;
  bleVersion?: number;
  detected: boolean;
}

export interface CustomModeStep {
  mode: number;
  speed: number;
  color1: RGB;
  color2: RGB;
  dir?: number;
}

export interface MusicConfig {
  micSensitivity: number;
  matrixStyle: number;
  patternId: number;
  color1: RGB;
  color2: RGB;
  speed: number;
  brightness: number;
}

export interface FirmwareInfo {
  firmwareVer: number;
  ledVersion?: number;
  bleVersion?: number;
  productId?: number;
}

export interface RfRemoteState {
  mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED';
  modeName: string;
  pairedCount: number;
  pairedRemoteIds: string[];
}

// ─── Protocol Interface ───────────────────────────────────────────────────────

export interface IControllerProtocol {
  // ─── Identity ──────────────────────────────────────────────────────────────
  /** Unique protocol identifier (e.g., 'zengge', 'banlanx') */
  readonly protocolId: string;

  /** BLE GATT service UUID for this controller */
  readonly serviceUUID: string;

  /** BLE GATT characteristic UUID for WRITE operations */
  readonly writeCharacteristicUUID: string;

  /** BLE GATT characteristic UUID for NOTIFY (responses/telemetry) */
  readonly notifyCharacteristicUUID: string;

  /**
   * If true, the app's software FFT engine (AudioContext + magnitude stream)
   * must remain active for music mode. If false (e.g. BanlanX), the hardware
   * has its own native FFT — the app's AudioContext should be suspended to
   * save battery.
   */
  readonly requiresSoftwareFFT: boolean;

  // ─── Discovery ─────────────────────────────────────────────────────────────
  /**
   * Returns true if this protocol matches the given BLE advertisement.
   * Used by ControllerRegistry.resolveProtocol() during scan.
   * Primary: service UUID matching. Secondary: manufacturer data.
   */
  matchesAdvertisement(serviceUUIDs: string[], manufacturerData?: string): boolean;

  /**
   * Parse firmware version from BLE advertisement manufacturer data (base64).
   * Returns null if this adapter cannot parse the given advertisement.
   */
  parseFirmwareFromAdvertisement(manufacturerDataBase64: string): FirmwareInfo | null;

  // ─── Connection Lifecycle ──────────────────────────────────────────────────
  /**
   * Returns the ordered BLE packets to send immediately after GATT connection.
   * Zengge: sends 0x10 time sync. BanlanX: returns empty packets (no handshake).
   * useBLE.ts iterates these packets with interPacketDelayMs between each.
   */
  getHandshakePayloads(): ProtocolResult;

  // ─── Hardware Settings (EEPROM) ────────────────────────────────────────────
  /**
   * Build the query payload to read hardware settings from EEPROM.
   * Zengge: 0x63 query. BanlanX: 0x70 state query.
   */
  buildQuerySettings(hasMic?: boolean): ProtocolResult;

  /**
   * Parse a raw BLE notification response into structured hardware settings.
   * Returns null if the response does not match this protocol.
   */
  parseSettingsResponse(raw: number[]): HardwareSettingsResult | null;

  /**
   * Build the payload to write hardware settings to EEPROM.
   * Zengge: 0x62 write. BanlanX: 0x55 (pixel count) + 0x6A (driver type).
   */
  buildWriteSettings(
    points: number,
    segments: number,
    icType: number,
    sorting: number
  ): ProtocolResult;

  /**
   * Convenience wrapper around buildWriteSettings that accepts string names.
   */
  buildWriteSettingsByName(
    points: number,
    segments: number,
    stripTypeName: string,
    sortingName: string
  ): ProtocolResult;

  // ─── RF Remote ─────────────────────────────────────────────────────────────
  /**
   * Build the payload to set RF remote auth state.
   */
  buildSetRfRemoteState(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', autoSave: boolean): ProtocolResult;

  /**
   * Clear all paired RF remotes. The hardware requires the current mode as a parameter.
   */
  buildClearRfRemotes(mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'): ProtocolResult;

  /**
   * Build the payload to query RF remote auth state.
   * Zengge: 0x2B query. BanlanX: no-op (returns empty packets).
   */
  buildQueryRfRemoteState(): ProtocolResult;

  /**
   * Parse a raw BLE notification into RF remote state.
   * Returns null if unsupported or response doesn't match.
   */
  parseRfRemoteState(raw: number[]): RfRemoteState | null;

  // ─── Power ─────────────────────────────────────────────────────────────────
  /** Power on command. Zengge: 0x71 0x23. BanlanX: 0xA0 0x50 0x01 0x01. */
  buildPowerOn(): ProtocolResult;

  /** Power off command. Zengge: 0x71 0x24. BanlanX: 0xA0 0x50 0x01 0x00. */
  buildPowerOff(): ProtocolResult;

  // ─── Color & Pattern Commands ──────────────────────────────────────────────
  /** Set all LEDs to a single solid color. */
  buildSolidColor(r: number, g: number, b: number): ProtocolResult;

  /**
   * Set a multi-color pixel array.
   * Zengge: 0x59 with full pixel array + ledPoints footer.
   * BanlanX: 0x52 solid (multi-color streaming requires Phase 2).
   *
   * @param colors       Per-pixel RGB array. Length = hardware ledPoints.
   * @param ledPoints    Hardware LED point count (from EEPROM, NOT total LEDs).
   * @param speed        Animation speed 1-100.
   * @param direction    1 = forward, 0 = reverse.
   * @param transitionType  0x01=Static, 0x02=Running, etc. (Zengge-specific).
   */
  buildMultiColor(
    colors: RGB[],
    ledPoints: number,
    speed: number,
    direction: number,
    transitionType?: number
  ): ProtocolResult;

  /**
   * Build a compact custom mode packet (variable-length steps).
   * Zengge: 0x51 compact format (up to 18 steps, fits single MTU).
   */
  buildCustomMode(steps: CustomModeStep[]): ProtocolResult;

  /**
   * Build an extended custom mode packet (32 fixed slots, 323 bytes).
   * Zengge: 0x51 extended format — REQUIRES chunked transmission via prepareForTransmission.
   * BanlanX: maps to buildEffect (no extended mode concept).
   */
  buildCustomModeExtended(steps: CustomModeStep[], direction?: number): ProtocolResult;

  /**
   * Build an effect command.
   * Zengge: 0x42 (effectId + speed + brightness in one packet).
   * BanlanX: TWO packets — 0x53 (effectId) + 0x54 (speed) with 20ms delay.
   *
   * @param effectId   Hardware effect ID (1-100 for Zengge, 1-142 for BanlanX).
   * @param speed      Speed 1-100.
   * @param brightness Brightness 1-100.
   */
  buildEffect(effectId: number, speed: number, brightness: number): ProtocolResult;

  /**
   * Build a candle flicker mode command.
   * Zengge: 0x39. BanlanX: no direct equivalent (maps to nearest effect).
   */
  buildCandleMode(
    r: number,
    g: number,
    b: number,
    speed: number,
    brightness: number,
    amplitude: number
  ): ProtocolResult;

  /**
   * Stream a single pixel frame for real-time animation.
   * Zengge: 0x53 hypothesis (APK-unconfirmed). BanlanX: Phase 2 (TBD opcode).
   */
  buildStreamPixelFrame(pixels: RGB[]): ProtocolResult;

  // ─── Music ─────────────────────────────────────────────────────────────────
  /**
   * Configure music-reactive mode.
   * Zengge: 0x73 (13-byte config). BanlanX: 0x59 (source) + 0x5A (sensitivity).
   */
  buildMusicConfig(config: MusicConfig): ProtocolResult;

  /**
   * Send a single music magnitude sample for real-time audio reactivity.
   * Zengge: 0x74 (magnitude byte). BanlanX: no-op (native FFT handles it).
   * Returns empty packets if not applicable.
   */
  buildMusicMagnitude(magnitude: number): ProtocolResult;

  // ─── Transport Preparation ─────────────────────────────────────────────────
  /**
   * Apply MTU-aware transmission preparation to a ProtocolResult.
   *
   * Zengge: if any packet exceeds (mtu - 3) bytes, applies 0x40 chunking
   * fragmentation (splits into multiple smaller packets with chunk headers).
   *
   * BanlanX: passthrough — packets are always small (< 20 bytes).
   *
   * useBLE.ts calls this for EVERY outgoing write, after resolving the
   * per-device adapter and MTU from adapterMapRef / mtuMapRef.
   *
   * @param result  The ProtocolResult to prepare.
   * @param mtu     Negotiated MTU for the target device (from mtuMapRef).
   * @returns       A new ProtocolResult with chunked packets if needed.
   */
  prepareForTransmission(result: ProtocolResult, mtu: number): ProtocolResult;
}

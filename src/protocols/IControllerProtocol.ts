/**
 * IControllerProtocol.ts — Hardware Abstraction Layer (HAL) Interface
 *
 * Universal interface for all SK8Lytz LED controller hardware protocols.
 * This is the seam between "what the app wants to do" (set color, run pattern,
 * query settings) and "how the hardware does it" (Zengge packet format,
 * ESP32-SPI format, custom silicon, etc).
 *
 * Current implementations:
 *   - ZenggeAdapter (wraps existing ZenggeProtocol)
 *
 * Future implementations:
 *   - ESP32SPIAdapter (custom SPI silicon)
 *   - SimulatorAdapter (testing without hardware)
 *
 * Architecture:
 *   App Layer → IControllerProtocol → ControllerRegistry → Adapter → BLE GATT
 */

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
  version: string;
  raw?: string;
}

// ─── Protocol Interface ───────────────────────────────────────────────────────

export interface IControllerProtocol {
  // ─── Identity ──────────────────────────────────────────────────────
  /** Unique protocol identifier (e.g., 'zengge', 'esp32-spi') */
  readonly protocolId: string;

  /** BLE GATT service UUID for this controller */
  readonly serviceUUID: string;

  /** BLE GATT characteristic UUID for WRITE operations */
  readonly writeCharacteristicUUID: string;

  /** BLE GATT characteristic UUID for NOTIFY (responses/telemetry) */
  readonly notifyCharacteristicUUID: string;

  // ─── Discovery ─────────────────────────────────────────────────────
  /** Returns true if this protocol matches the given BLE advertisement */
  matchesAdvertisement(serviceUUIDs: string[], manufacturerData?: string): boolean;

  // ─── Hardware Settings (EEPROM) ────────────────────────────────────
  /** Build the query payload to read hardware settings (e.g., 0x63 for Zengge) */
  buildQuerySettingsPayload(hasMic?: boolean): number[];

  /** Parse a raw notification response into structured hardware settings */
  parseSettingsResponse(raw: number[]): HardwareSettingsResult | null;

  /** Build the payload to write hardware settings to EEPROM (e.g., 0x62 for Zengge) */
  buildWriteSettingsPayload(points: number, segments: number, icType: number, sorting: number): number[];

  // ─── LED Commands ──────────────────────────────────────────────────
  /** Set all LEDs to a single solid color */
  buildSolidColor(r: number, g: number, b: number): number[];

  /** Set a multi-color pixel array (e.g., 0x59 for Zengge) */
  buildMultiColor(colors: RGB[], speed: number, direction: number, transition: number): number[];

  /** Set a custom mode with fade/jump steps (e.g., 0x51 for Zengge) */
  buildCustomMode(steps: CustomModeStep[]): number[];

  /** Configure music-reactive mode */
  buildMusicConfig(config: MusicConfig): number[];

  /** Send a music magnitude sample for real-time audio reactivity */
  buildMusicMagnitude(magnitude: number): number[];

  /** Power on command */
  buildPowerOn(): number[];

  /** Power off command */
  buildPowerOff(): number[];

  // ─── Utility ───────────────────────────────────────────────────────
  /** Calculate checksum for a raw payload */
  calculateChecksum(payload: number[]): number;

  /** Wrap a raw command with protocol framing (e.g., counter header for Zengge) */
  wrapCommand(rawPayload: number[]): number[];
}

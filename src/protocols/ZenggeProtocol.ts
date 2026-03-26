/**
 * Zengge BLE Controller Protocol Implementation
 * Supports Magic Home / LEDnetWF / Zengge controllers
 */

export const ZENGGE_SERVICE_UUID = '0000ffff-0000-1000-8000-00805f9b34fb';
export const ZENGGE_CHARACTERISTIC_UUID = '0000ff01-0000-1000-8000-00805f9b34fb';

export class ZenggeProtocol {
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
      0x00,                      // Header: version 0, not segmented
      seq & 0xFF,                // Sequence number
      0x80,                      // Frag control high byte
      0x00,                      // Frag control low byte
      (payloadLen >> 8) & 0xFF,  // Total length high byte
      payloadLen & 0xFF,         // Total length low byte
      (payloadLen + 1) & 0xFF,   // Payload length + 1
      cmdFamily                  // cmdId
    ];
    
    return [...packet, ...rawPayload];
  }

  /**
   * Universal Rock-Solid Color State
   * REPLACES the glitchy 0x31 Basic RGB endpoint.
   * Testing confirmed generating a 10-length segmented array using Type 1 (Gradual) forces the hardware to natively lock a perfect solid light state.
   */
  static setColor(r: number, g: number, b: number, speed: number = 100): number[] {
    return this.setMultiColor(Array(10).fill({ r, g, b }), speed, 1, 1);
  }

  /**
   * Basic RGB Solid Color (0x31) - Legacy Unstable Fallback
   */
  static setLegacyColor0x31(r: number, g: number, b: number): number[] {
    const cmd = [0x31, r, g, b, 0x00, 0x00, 0x0f];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /**
   * Symphony Static Color (0x41)
   * Specifically for addressable SPI controllers to set a solid color on all pixels.
   */
  static setSymphonyColor(r: number, g: number, b: number): number[] {
    const cmd = [0x41, r, g, b, 0x01, 0x01, 0xf0];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /**
   * RBM Mode (Advanced 100 modes on Firmware 0x56)
   * Pattern 1-100, Speed 0-100, Brightness 0-100
   */
  static setRbmMode(pattern: number, speed: number, brightness: number = 100): number[] {
    const payload = [0x42, pattern, speed, brightness];
    const checksum = this.calculateChecksum(payload);
    return this.wrapCommand([...payload, checksum]);
  }

  /**
   * Music Sync Mode (Config)
   * @param isDeviceMic true for Device Mic, false for App Mic
   * @param modeType 0x26 for Light Bar, 0x27 for Light Screen
   * @param patternId 1-16
   */
  static setMusicConfig(
    isDeviceMic: boolean,
    modeType: number,
    patternId: number,
    color1: {r: number, g: number, b: number},
    color2: {r: number, g: number, b: number},
    sensitivity: number,
    brightness: number
  ): number[] {
    const payload = [
      0x73, 
      isDeviceMic ? 0x01 : 0x00, 
      modeType, 
      patternId, 
      color1.r, color1.g, color1.b, 
      color2.r, color2.g, color2.b, 
      0x20, 
      sensitivity, 
      brightness
    ];
    const checksum = this.calculateChecksum(payload);
    return this.wrapCommand([...payload, checksum]);
  }

  /**
   * Send Music Magnitude for App Mic (Live Data)
   */
  static sendMusicMagnitude(magnitude: number): number[] {
    const payload = [0x74, magnitude];
    const checksum = this.calculateChecksum(payload);
    return this.wrapCommand([...payload, checksum]);
  }

  /**
   * Multi-color / Segmented Mode (0x59)
   */
  static setMultiColor(colors: {r: number, g: number, b: number}[], speed: number, direction: number, transitionType: number = 0x00): number[] {
    const numPoints = colors.length;
    const totalLen = (numPoints * 3) + 9;
    const payload = new Array(totalLen);
    payload[0] = 0x59;
    payload[1] = (totalLen >> 8) & 0xFF;
    payload[2] = totalLen & 0xFF;
    let idx = 3;
    for (const c of colors) {
      payload[idx++] = c.r;
      payload[idx++] = c.g;
      payload[idx++] = c.b;
    }
    payload[idx++] = (numPoints >> 8) & 0xFF;
    payload[idx++] = numPoints & 0xFF;
    payload[idx++] = transitionType; // Type
    payload[idx++] = speed;
    payload[idx++] = direction;
    payload[idx] = this.calculateChecksum(payload.slice(0, totalLen - 1));

    return this.wrapCommand(payload);
  }

  /**
   * Custom DIY Pattern Mode (0x51)
   * Up to 32 steps. Each step: [active, mode, speed, r1, g1, b1, r2, g2, b2]
   */
  static setCustomMode(steps: {mode: number, speed: number, color1: {r: number, g: number, b: number}, color2: {r: number, g: number, b: number}}[]): number[] {
    const payload = new Array(291).fill(0);
    payload[0] = 0x51;
    let idx = 1;
    for (let i = 0; i < 32; i++) {
        if (i < steps.length) {
            const step = steps[i];
            payload[idx++] = 0xf0; // Active
            payload[idx++] = step.mode;
            payload[idx++] = step.speed;
            payload[idx++] = step.color1.r;
            payload[idx++] = step.color1.g;
            payload[idx++] = step.color1.b;
            payload[idx++] = step.color2.r;
            payload[idx++] = step.color2.g;
            payload[idx++] = step.color2.b;
        } else {
            payload[idx++] = 0x0f; // Inactive
            idx += 8; // Fill with 0
        }
    }
    payload[289] = 0x0f; // Final byte
    payload[290] = this.calculateChecksum(payload.slice(0, 290));

    return this.wrapCommand(payload);
  }

  /**
   * Legacy Pattern Command (0x25)
   */
  static setLegacyPattern(mode: number, speed: number): number[] {
    const cmd = [0x25, mode, speed];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /**
   * Turn device ON
   */
  static turnOn(): number[] {
    return this.wrapCommand([0x71, 0x23, 0x0f, 0xa3]);
  }

  /**
   * Turn device OFF
   */
  static turnOff(): number[] {
    return this.wrapCommand([0x71, 0x24, 0x0f, 0xa4]);
  }

  /**
   * Set Hardware Strip Configuration (IC Type, Pixels, Color Order) 0x81 command
   */
  public static setHardwareConfig(points: number, colorOrder: string, stripType: string, segments: number = 1): number[] {
    const pointsHigh = (points >> 8) & 0xFF;
    const pointsLow = points & 0xFF;
    
    const segmentsHigh = (segments >> 8) & 0xFF;
    const segmentsLow = segments & 0xFF;

    let orderByte = 3; // GRB default
    if (colorOrder.includes('RGB')) orderByte = 1;
    else if (colorOrder.includes('RBG')) orderByte = 2;
    else if (colorOrder.includes('GRB')) orderByte = 3;
    else if (colorOrder.includes('GBR')) orderByte = 4;
    else if (colorOrder.includes('BRG')) orderByte = 5;
    else if (colorOrder.includes('BGR')) orderByte = 6;

    let icByte = 3; // WS2812B default
    if (stripType.includes('SM16703')) icByte = 1;
    else if (stripType.includes('WS2811')) icByte = 2;
    else if (stripType.includes('SK6812')) icByte = 4;

    const cmd = [0x81, pointsHigh, pointsLow, orderByte, icByte, segmentsHigh, segmentsLow];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /**
   * Query Hardware Configuration (0x10)
   */
  static queryHardwareConfig(): number[] {
    const payload = [0x10, 0x00, 0x00, 0x10]; // Common query for v2 symphony
    return this.wrapCommand(payload);
  }

  /**
   * Parse Hardware Configuration Response
   */
  static parseHardwareConfig(payload: number[]) {
    if (payload[0] !== 0x10 || payload.length < 9) return null;
    
    const points = (payload[2] << 8) | payload[3];
    const orderBytes = ['UNK', 'RGB', 'RBG', 'GRB', 'GBR', 'BRG', 'BGR'];
    const sorting = orderBytes[payload[4]] || 'GRB';
    
    const icBytes: Record<number, string> = { 1: 'SM16703', 2: 'WS2811', 3: 'WS2812B', 4: 'SK6812' };
    const stripType = icBytes[payload[5]] || 'WS2812B';
    
    const segments = (payload[6] << 8) | payload[7];

    return { points, sorting, stripType, segments };
  }

  /**
   * Set 2.4G Physical RF Remote Authorization State & Pairing Memory
   * Mode: 0x01 (Allow None), 0x02 (Allow Paired Only), 0x03 (Allow All)
   */
  public static setRfRemoteState(authMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED', clearRemotes: boolean = false): number[] {
    let modeByte = 0x03; // Allow All
    if (authMode === 'ALLOW_NONE') modeByte = 0x01;
    else if (authMode === 'ALLOW_PAIRED') modeByte = 0x02;
    
    // Command 0x2A is the 2.4G configuration.
    // The 5 bytes of 0xFF effectively clear the paired 40-bit Remote ID when synchronized.
    const cmd = [
      0x2A, modeByte, 
      0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 
      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
      0x0F
    ];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }
}

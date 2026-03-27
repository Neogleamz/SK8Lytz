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
  static setCustomRbm(patternId: number, speed: number, brightness: number): number[] {
    const speedHex = Math.max(1, Math.min(100, speed));
    const brightnessHex = Math.max(1, Math.min(100, brightness));
    const cmd = [0x42, patternId, speedHex, brightnessHex];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum]);
  }

  /**
   * Hardware Native Candle Mode (0x39 / 0xD1)
   * Mimics physical flickering independently from the App's CPU.
   * @param r Red Channel (0-255)
   * @param g Green Channel (0-255)
   * @param b Blue Channel (0-255)
   * @param speed Flicker Speed (1-100)
   * @param brightness Baseline Brightness (1-100)
   * @param amplitude Intensity of flicker (1-3)
   */
  public static setCandleMode(r: number, g: number, b: number, speed: number, brightness: number, amplitude: number): number[] {
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
    // SECURITY: Dynamic Padding - The hardware native 0x59 parser WILL GLITCH if array length < 10.
    // We seamlessly loop the user's pattern over itself until it breaches the threshold.
    let expandedColors = [...colors];
    if (expandedColors.length > 0 && expandedColors.length < 10) {
       const basePattern = [...expandedColors];
       while (expandedColors.length < 10) {
          expandedColors.push(...basePattern);
       }
    }

    // SECURITY: Clamp the payload to absolute maximum of 32 color vectors to completely eliminate MTU fragmentation BLE packet crashing
    const safeColors = expandedColors.slice(0, 32);
    const numPoints = safeColors.length;
    const totalLen = (numPoints * 3) + 9;
    const payload = new Array(totalLen);
    payload[0] = 0x59;
    payload[1] = (totalLen >> 8) & 0xFF;
    payload[2] = totalLen & 0xFF;
    let idx = 3;
    for (const c of safeColors) {
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
    // SECURITY: Hard limit the 291 byte buffer exactly to the 32 Step MCU spec.
    const safeSteps = steps.slice(0, 32);
    const payload = new Array(291).fill(0);
    payload[0] = 0x51;
    let idx = 1;
    for (let i = 0; i < 32; i++) {
        if (i < safeSteps.length) {
            const step = safeSteps[i];
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
    
    // Per geminianswer.txt, Segments is a single integer byte in the config payload.
    const segmentsByte = segments & 0xFF;

    let orderByte = 1; // GRB default in the index logic
    if (colorOrder === 'RGB') orderByte = 0;
    else if (colorOrder === 'GRB') orderByte = 1;
    else if (colorOrder === 'BRG') orderByte = 2;
    // Map any outliers safely
    else if (colorOrder.includes('RGB')) orderByte = 0;
    else if (colorOrder.includes('GRB')) orderByte = 1;

    let icByte = 1; // WS2812B default
    if (stripType.includes('WS2812B')) icByte = 1;
    else if (stripType.includes('SM16703')) icByte = 2;
    else if (stripType.includes('SM16704')) icByte = 3;
    else if (stripType.includes('WS2811')) icByte = 4;
    else if (stripType.includes('SK6812')) icByte = 5;

    // Per geminianswer.txt: Pack them into the data section: [IC Type, Sorting, Points High, Points Low, Segments].
    // We prefix with 0x81 as the SET config specific data header.
    const cmd = [0x81, icByte, orderByte, pointsHigh, pointsLow, segmentsByte];
    const checksum = this.calculateChecksum(cmd);
    return this.wrapCommand([...cmd, checksum], 0x0A); // System settings often use cmdFamily 0x0A
  }

  /**
   * Universal Addressable LED Configuration Query Pings (Shotgun Dict)
   */
  static queryHardwareConfigDictionary(): number[][] {
    return [
      this.wrapCommand([0x81, 0x8A, 0x8B, 0x96]), // 1: Standard LedNet Status
      this.wrapCommand([0x10, 0x00, 0x00, 0x10]), // 2: Legacy MagicHome Ping
      this.wrapCommand([0x2B, 0x2C, 0x2D, 0x00]), // 3: RF Remote Config Ping
      this.wrapCommand([0x32, 0x3A, 0x3B, 0x0F]), // 4: Power State Boot Ping
      this.wrapCommand([0x63, 0x14, 0x00, 0x00]), // 5: Direct Addressable Config Probe
      this.wrapCommand([0x62, 0x00, 0x00, 0x00])  // 6: Direct Addressable Length Probe
    ];
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
    // Expected wrapped notification starting with 0x00, seq, 0x80, 0x00 ...
    // Minimum length for a 13-byte hardware response as defined in geminianswer.txt
    if (!payload || payload.length < 13) return null;
    
    // We check if it's a hardware response. The header starts usually with 0x80 at index 2.
    if (payload[2] !== 0x80) return null;
    
    // Index 7 is the Response Command Header (e.g., 0x0A or 0x63 or 0x81)
    const cmdHeader = payload[7];
    if (cmdHeader !== 0x0A && cmdHeader !== 0x63 && cmdHeader !== 0x81) return null;
    
    // Parsing logic exclusively mapping hardware characteristics based on offsets:
    const icType = payload[8];
    const sortingType = payload[9];
    const pointsHigh = payload[10];
    const pointsLow = payload[11];
    const segments = payload[12];
    
    // Mapping constants safely interpreting the extracted bytes
    const icBytes: Record<number, string> = { 1: 'WS2812B', 2: 'SM16703', 3: 'SM16704', 4: 'WS2811', 5: 'SK6812' };
    const orderBytes = ['RGB', 'GRB', 'BRG'];
    
    // Calculate total LED points via bitwise shift
    const points = (pointsHigh << 8) | pointsLow;
    const sorting = orderBytes[sortingType] || 'UNK';
    const stripType = icBytes[icType] || 'UNK';

    // Discard false positives where 0x81 ping isn't the config array (if bytes are radically out of expected ranges)
    if (sorting === 'UNK' && stripType === 'UNK') return null;

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

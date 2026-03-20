/**
 * Zengge BLE Controller Protocol Implementation
 * Supports Magic Home / LEDnetWF / Zengge controllers
 */

export const ZENGGE_SERVICE_UUID = '0000ffd5-0000-1000-8000-00805f9b34fb';
export const ZENGGE_CHARACTERISTIC_UUID = '0000ffd9-0000-1000-8000-00805f9b34fb';

export class ZenggeProtocol {
  private static messageCounter = 0;

  private static getCounterBytes(): number[] {
    this.messageCounter = (this.messageCounter + 1) % 0xFFFF;
    return [(this.messageCounter >> 8) & 0xFF, this.messageCounter & 0xFF];
  }

  private static calculateChecksum(payload: number[]): number {
    return payload.reduce((acc, val) => acc + val, 0) & 0xFF;
  }

  /**
   * Generates command to set RGB Color (Classic and V2)
   */
  static setColor(r: number, g: number, b: number): number[] {
    const cmd = [0x31, r, g, b, 0x00, 0x00, 0x0f];
    const checksum = this.calculateChecksum(cmd);
    return [...cmd, checksum];
  }

  /**
   * RBM Mode (Advanced 100 modes on Firmware 0x56)
   * Pattern 1-100, Speed 0-100, Brightness 0-100
   */
  static setRbmMode(pattern: number, speed: number, brightness: number = 100): number[] {
    const counter = this.getCounterBytes();
    // [counterHigh, counterLow, 0x80, 0x00, 0x00, 0x05, 0x06, 0x0b, 0x42, pattern, speed, brightness, checksum]
    const header = [...counter, 0x80, 0x00, 0x00, 0x05, 0x06, 0x0b];
    const payload = [0x42, pattern, speed, brightness];
    const checksum = this.calculateChecksum(payload);
    return [...header, ...payload, checksum];
  }

  /**
   * Music Sync Mode (Firmware 0x56)
   * Sensitivity 0-100, Brightness 0-100
   */
  static setMusicMode(sensitivity: number, brightness: number, colorType: number = 1): number[] {
    const counter = this.getCounterBytes();
    const header = [...counter, 0x80, 0x00, 0x00, 0x0d, 0x0e, 0x0b];
    // 73 00 26 [type] ff 00 00 ff 00 00 20 [sensitivity] [brightness]
    const payload = [0x73, 0x00, 0x26, colorType, 0xff, 0x00, 0x00, 0xff, 0x00, 0x00, 0x20, sensitivity, brightness];
    const checksum = this.calculateChecksum(payload);
    return [...header, ...payload, checksum];
  }

  /**
   * Legacy Pattern Command (0x25)
   */
  static setLegacyPattern(mode: number, speed: number): number[] {
    const cmd = [0x25, mode, speed];
    const checksum = this.calculateChecksum(cmd);
    return [...cmd, checksum];
  }

  /**
   * Turn device ON
   */
  static turnOn(): number[] {
    return [0x71, 0x23, 0x0f, 0xa3];
  }

  /**
   * Turn device OFF
   */
  static turnOff(): number[] {
    return [0x71, 0x24, 0x0f, 0xa4];
  }

  /**
   * Set Hardware Strip Configuration (IC Type, Pixels, Color Order) 0x81 command
   */
  static setHardwareConfig(points: number, colorOrder: string, stripType: string): number[] {
    const pointsHigh = (points >> 8) & 0xFF;
    const pointsLow = points & 0xFF;

    let orderByte = 3; // GRB default
    if (colorOrder.includes('RGB')) orderByte = 1;
    else if (colorOrder.includes('RBG')) orderByte = 2;
    else if (colorOrder.includes('GRB')) orderByte = 3;
    else if (colorOrder.includes('GBR')) orderByte = 4;
    else if (colorOrder.includes('BRG')) orderByte = 5;
    else if (colorOrder.includes('BGR')) orderByte = 6;

    let icByte = 3; // WS2812B default
    if (stripType.includes('WS2811')) icByte = 2;
    // Note: Other IC types use specific bytes (e.g., SK6812=4), standard defaults apply.

    const cmd = [0x81, pointsHigh, pointsLow, orderByte, icByte, 0x00, 0x00];
    const checksum = this.calculateChecksum(cmd);
    return [...cmd, checksum];
  }
}

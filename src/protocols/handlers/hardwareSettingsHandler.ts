import { ZenggeProtocol, HardwareSettings, HW_CONSTRAINTS, IC_TYPES, COLOR_SORTING_RGB, icTypeIndex, colorSortingIndex } from '../ZenggeProtocol';

let _appLogger: any;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../../services/AppLogger').AppLogger; } catch (_e) { _appLogger = console; }
  }
  return _appLogger;
}

// ─── Static Facades (backward compatibility — DO NOT REMOVE) ─────────────────
  // All 25 existing consumers call ZenggeProtocol.method() statically.
  // These facades delegate to the singleton instance with zero behavior change.



  // ─── HARDWARE SETTINGS: QUERY (0x63) ───────────────────────────────────────
  /**
   * Build 5-byte query packet for reading hardware config from device.
   * Send immediately on BLE connection before any pattern commands.
   *
   * @param hasMic true if device has built-in mic (always false for HALOZ/SOULZ)
   */
  export function queryHardwareSettings(protocol: ZenggeProtocol, hasMic: boolean = false): number[] {
    const raw = [0x63, 0x12, 0x21, hasMic ? 0xF0 : 0x0F];
    raw.push(protocol.calculateChecksum(raw));
    return protocol.wrapCommand(raw);
  }

// ─── HARDWARE SETTINGS: PARSE RESPONSE (0x63) ──────────────────────────────
  /**
   * Parse a 12-byte hardware settings response from device.
   *
   * CRITICAL: Points bytes are LITTLE-ENDIAN SWAPPED in the response:
   *   bytes[8] = Low byte, bytes[9] = High byte  (reversed from write command)
   *
   * Validation: bytes[0]==0x63 AND bytes[11]==checksum(bytes[0..10])
   */
  export function parseHardwareSettingsResponse(raw: number[]): HardwareSettings | null {
    // ── Step 1: Detect and unwrap JSON envelope ──────────────────────────────
    // Newer firmware sends: [8-byte BLE header][JSON: {"code":0,"payload":"<hex>"}]
    // where <hex> is the actual binary response encoded as a hex string.
    let payload = raw;
    let isJsonFormat = false;
    try {
      const jsonStart = raw.findIndex((b, i) => b === 0x7B && raw[i + 1] === 0x22); // 0x7B = '{'
      if (jsonStart !== -1) {
        const jsonStr = String.fromCharCode(...raw.slice(jsonStart));
        const parsed = JSON.parse(jsonStr);
        if (parsed?.payload && typeof parsed.payload === 'string') {
          const hexStr: string = parsed.payload;
          const innerBytes: number[] = [];
          for (let i = 0; i < hexStr.length - 1; i += 2) {
            innerBytes.push(parseInt(hexStr.slice(i, i + 2), 16));
          }
          payload = innerBytes;
          isJsonFormat = true;
        }
      }
    } catch (e: unknown) {
      // Not JSON format — fall through to binary parse
      const errorMsg = e instanceof Error ? e.message : String(e);
      getAppLogger().debug?.('[ZenggeProtocol] JSON parse skipped', { error: errorMsg });
    }

    // ── Step 2: Strip V2 binary wrapper if present ──────────────────────────
    if (!isJsonFormat && payload.length > 12 && payload[2] === 0x80) {
      payload = payload.slice(8);
    }

    // ── Step 3: Verify this is a 0x63 response packet ────────────────────────
    // JSON format: [0x00, 0x63, ...] — 0x63 at index 1
    // Classic format: [0x63, ...] — 0x63 at index 0
    const is63AtIndex0 = payload[0] === 0x63;
    const is63AtIndex1 = payload[1] === 0x63;
    if (!is63AtIndex0 && !is63AtIndex1) return null;
    if (payload.length < 10) return null;

    // ── Step 4: Extract fields from correct offsets ───────────────────────────
    // JSON-inner format (0x63 at index 1):
    //   [0]=0x00  [1]=0x63  [2]=0x00  [3]=icType  [4]=0x00
    //   [5]=segments  [6]=pts_hi  [7]=pts_lo  [8]=X  [9]=colorSorting
    // Classic format (0x63 at index 0):
    //   [0]=0x63  [1]=?  [2]=?  [3]=icType  [4]=?  [5]=?  [6]=?  [7]=?
    //   [8]=pts_lo  [9]=pts_hi  [10]=colorSorting  [11]=checksum
    let icType: number, ledPoints: number, colorSorting: number;

    if (is63AtIndex1 && isJsonFormat) {
      // JSON-inner format — verified against Zengge app ground truth:
      // [0]=0x00  [1]=0x63  [2]=0x00
      // [3]=points (single byte, e.g. 0x0B=11)
      // [4]=0x00  (reserved)
      // [5]=segments (e.g. 0x02=2)
      // [6]=icType (e.g. 0x01=WS2812B)
      // [7]=colorSorting (e.g. 0x02=GRB)
      // [8]=micPoints  [9]=micSegments  [10]=flags
      icType = payload[6] & 0xFF;
      ledPoints = payload[3] & 0xFF;
      colorSorting = payload[7] & 0xFF;
      const parsedSegments = payload[5] & 0xFF;

      return {
        ledPoints: (ledPoints > 0 && ledPoints <= 2048) ? ledPoints : HW_CONSTRAINTS.defaultPoints,
        segments: (parsedSegments > 0) ? parsedSegments : 1,
        icType,
        icName: IC_TYPES[icType] || 'WS2812B',
        colorSorting,
        colorSortingName: COLOR_SORTING_RGB[colorSorting] || 'GRB',
        detected: true,
      };
    } else {
      // Classic 12-byte format
      icType = payload[3] & 0xFF;
      ledPoints = ((payload[9] & 0xFF) << 8) | (payload[8] & 0xFF);
      colorSorting = payload[10] & 0xFF;
    }

    // Clamp sorting to defined range
    if (colorSorting > 5) colorSorting = colorSorting & 0x07;
    if (colorSorting > 5) colorSorting = 2; // default GRB

    return {
      ledPoints: (ledPoints > 0 && ledPoints <= 2048) ? ledPoints : HW_CONSTRAINTS.defaultPoints,
      segments: 1,
      icType,
      icName: IC_TYPES[icType] || 'WS2812B',
      colorSorting,
      colorSortingName: COLOR_SORTING_RGB[colorSorting] || 'GRB',
      detected: true,
    };
  }

/**
   * Parse ZENGGE Manufacturer Specific Data (type 0xFF) from raw byte array.
   *
   * This is the low-level decoder used when manufacturerData arrives as a
   * number[] from the BLE scanner's advertisement callback.
   * For base64-encoded strings (from the GATT probe path), use parseFirmwareFromAdvertisement.
   *
   * Advertisement byte map (Refer to ZENGGE_PROTOCOL_BIBLE.md):
   *   [0]  = 0xA8 (ZENGGE vendor prefix high byte)
   *   [1]  = 0x01 (ZENGGE vendor prefix low byte)
   *   [2]  = flags / device class
   *   [3]  = BLE Protocol Version (e.g. 0x04 = v4)
   *   [4]–[9]  = MAC Address (6 bytes, Big-Endian)
   *   [10] = Product ID high byte
   *   [11] = Product ID low byte
   *   [12] = Firmware Version major
   *   [13] = reserved / sub-build
   *   [14] = Firmware Version minor
   */
  export function parseFirmwareFromManufacturerBytes(
    manufacturerData: number[]
  ): Pick<HardwareSettings, 'firmwareVer' | 'bleVersion'> & { productId?: number; macAddress?: string } | null {
    if (!manufacturerData || manufacturerData.length < 15) return null;
    if (manufacturerData[0] !== 0xA8 || manufacturerData[1] !== 0x01) return null;

    const bleVersion = manufacturerData[3] & 0xFF;
    const productId = ((manufacturerData[10] & 0xFF) << 8) | (manufacturerData[11] & 0xFF);
    const firmwareMaj = manufacturerData[12] & 0xFF;
    const firmwareMin = manufacturerData[14] & 0xFF;
    const firmwareVer = firmwareMaj * 100 + firmwareMin;

    const macAddress = manufacturerData
      .slice(4, 10)
      .map(b => b.toString(16).toUpperCase().padStart(2, '0'))
      .join(':');

    return { firmwareVer, bleVersion, productId, macAddress };
  }

// ─── HARDWARE SETTINGS: WRITE (0x62) ───────────────────────────────────────
  /**
   * Build 11-byte write packet to set hardware config on device.
   *
   * Constraints (Refer to ZENGGE_PROTOCOL_BIBLE.md):
   *   points: 1-300, points × segments ≤ 2048
   *   segments: 1 to 2048/points
   *   micPoints: 1-150, micPoints × micSegments ≤ 960
   */
  export function writeHardwareSettings(protocol: ZenggeProtocol, 
    points: number,
    segments: number,
    icType: number,
    sorting: number,
    micPoints?: number,
    micSegments?: number
  ): number[] {
    // Clamp values to safe ranges
    const safePoints = Math.max(1, Math.min(HW_CONSTRAINTS.maxPoints, points));
    const maxSeg = Math.floor(HW_CONSTRAINTS.maxPxS / safePoints);
    const safeSegments = Math.max(1, Math.min(maxSeg, segments));

    const mp = micPoints ?? safePoints;
    const ms = micSegments ?? safeSegments;
    const safeMicPts = Math.max(1, Math.min(HW_CONSTRAINTS.maxMicPoints, mp));
    const maxMicSeg = Math.floor(HW_CONSTRAINTS.maxMicPxS / safeMicPts);
    const safeMicSegs = Math.max(1, Math.min(maxMicSeg, ms));

    // 0x62 write format:
    //   [0x62][ptsHigh][ptsLow][segHigh][segLow][icType][sorting][micPts][micSegs][0xF0][checksum]
    //
    // Points/Segments are big-endian 16-bit pairs.
    // icType: index from IC_TYPES (1=WS2812B, 2=SM16703, 6=SK6812, etc.)
    // sorting: index from COLOR_SORTING_RGB (0=RGB, 2=GRB, etc.)
    //
    // Compare with 0x63 response inner payload (JSON format):
    //   [3]=points  [5]=segments  [6]=icType  [7]=sorting
    const pHigh = (safePoints >> 8) & 0xFF;
    const pLow = safePoints & 0xFF;
    const sHigh = (safeSegments >> 8) & 0xFF;
    const sLow = safeSegments & 0xFF;

    const raw = [0x62, pHigh, pLow, sHigh, sLow, icType & 0xFF, sorting & 0xFF, safeMicPts, safeMicSegs, 0xF0];
    raw.push(protocol.calculateChecksum(raw));
    return protocol.wrapCommand(raw);
  }

/**
   * Convenience wrapper around writeHardwareSettings that accepts string names.
   * Resolves IC type and color sorting strings to their protocol indexes.
   *
   * @param points    LED point count (1-300)
   * @param segments  Segment count (clamped automatically)
   * @param icName    IC type string e.g. 'WS2812B', 'SM16703', 'SK6812'
   * @param sortingName Color order string e.g. 'GRB', 'RGB', 'BGR'
   */
  export function writeHardwareSettingsByName(protocol: ZenggeProtocol, 
    points: number,
    segments: number,
    icName: string,
    sortingName: string
  ): number[] {
    const icTypeIdx = icTypeIndex(icName);
    const sortingIdx = colorSortingIndex(sortingName);
    return protocol.writeHardwareSettings(points, segments, icTypeIdx, sortingIdx);
  }

// ─── FIRMWARE VERSION FROM ADVERTISEMENT DATA ───────────────────────────────
  /**
   * Parse firmware version from BLE manufacturer advertisement data.
   *
   * Call during BLE scan callback when manufacturerData is available.
   */
  export function parseFirmwareFromAdvertisement(manufacturerDataBase64: string): {
    firmwareVer: number;
    ledVersion: number;
    bleVersion: number;
    productId: number;
  } | null {
    try {
      const buffer = Buffer.from(manufacturerDataBase64, 'base64');
      if (buffer.length < 15) return null;

      const bleVersion = buffer[3] & 0xFF;
      const firmwareVer = bleVersion >= 6
        ? (buffer[12] & 0xFF) | (((buffer[14] & 0xFFFF) >> 2) << 8)
        : buffer[12] & 0xFF;
      const ledVersion = buffer[13] & 0xFF;
      const productId = ((buffer[10] & 0xFF) << 8) | (buffer[11] & 0xFF);

      return { firmwareVer, ledVersion, bleVersion, productId };
    } catch (e: unknown) {
      getAppLogger().warn('[ZenggeProtocol] parseFirmwareFromAdvertisement failed', {
        error: e instanceof Error ? e.message : String(e),
      });
      return null;
    }
  }


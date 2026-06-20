import { ZenggeProtocol, HardwareSettings, HW_CONSTRAINTS, IC_TYPES, COLOR_SORTING_RGB, icTypeIndex, colorSortingIndex } from '../ZenggeProtocol';

let _appLogger: any;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../../services/AppLogger').AppLogger; } catch (_e) { _appLogger = console; }
  }
  return _appLogger;
}

// ─── HAL ENCLOSURE: BUFFER LOCKOUT DEFENSE (R-19) ────────────────────────────

  /**
   * Pad a pre-built 0x59 Static Colorful payload to the hardware-minimum of 12 pixels.
   *
   * PROTOCOL BIBLE §0x59 MINIMUM PIXELS:
   *   Payloads below 10 pixels cause physical controller EEPROM buffer lockouts on the 0xA3 chipset.
   *   Minimum enforced = 12 pixels for a safety margin above that threshold.
   *
   * This method is the authoritative location for this padding logic (R-19 HAL Enclosure rule).
   * The BleWriteDispatcher MUST call this instead of duplicating the byte math inline.
   *
   * @param payload  Fully-constructed 0x59 payload (already checksummed, NOT yet V2-wrapped).
   * @returns        The same payload if numLEDs >= 12, or a new padded payload otherwise.
   *                 Returns the original payload unchanged if it is malformed or not a 0x59 packet.
   */
  export function padStaticColorfulPayload(payload: number[]): number[] {
    // Guard: must be 0x59 with enough bytes to read the length header
    if (payload[0] !== 0x59 || payload.length < 10) return payload;

    const totalLen = (payload[1] << 8) | payload[2];
    const numLEDs = Math.floor((totalLen - 9) / 3);

    if (numLEDs <= 0 || numLEDs >= 12) return payload; // already safe

    const paddingPixels = 12 - numLEDs;
    const paddingBytes = paddingPixels * 3;
    const newTotalLen = totalLen + paddingBytes;

    const newPayload = new Array(newTotalLen + 1).fill(0);
    newPayload[0] = 0x59;
    newPayload[1] = (newTotalLen >> 8) & 0xFF;
    newPayload[2] = newTotalLen & 0xFF;

    const existingRgbBytes = numLEDs * 3;
    // Copy existing RGB pixel bytes
    for (let i = 0; i < existingRgbBytes; i++) {
      newPayload[3 + i] = payload[3 + i];
    }
    // Padding bytes (zeros) already filled by Array.fill(0) above

    const footerStart = 3 + existingRgbBytes;
    // Safety: require at least 5 footer bytes (numLEDs_hi, numLEDs_lo, trans, spd, dir)
    if (footerStart + 4 >= payload.length) return payload;

    const origLedPoints = (payload[footerStart] << 8) | payload[footerStart + 1];
    const safeLedPoints = Math.max(12, origLedPoints);

    let dstIdx = 3 + existingRgbBytes + paddingBytes;
    newPayload[dstIdx++] = (safeLedPoints >> 8) & 0xFF;
    newPayload[dstIdx++] = safeLedPoints & 0xFF;
    newPayload[dstIdx++] = payload[footerStart + 2]; // transitionType
    newPayload[dstIdx++] = payload[footerStart + 3]; // speed
    newPayload[dstIdx++] = payload[footerStart + 4]; // direction

    // Recompute checksum over all bytes except the last slot
    let checksum = 0;
    for (let i = 0; i < dstIdx; i++) checksum += newPayload[i];
    newPayload[dstIdx] = checksum & 0xFF;

    return newPayload;
  }

/**
   * Build the ZENGGE 0x40 chunked frame sequence for a large payload.
   *
   * PROTOCOL BIBLE §0x51 ZENGGE BLE Chunked Framing:
   *   [0x40, seqNum, offset_lo, offset_hi, totalLen_hi, totalLen_lo, dataLen+1, 0x0B, ...data]  (first chunk)
   *   [0x40, seqNum, indexWord_hi, indexWord_lo, dataLen, ...data]                                (subsequent)
   *   Last chunk has bit 15 of indexWord set (0x8000 OR).
   *
   * This method is the authoritative 0x40 frame builder (R-19 HAL Enclosure rule).
   * The BleWriteDispatcher MUST call this instead of building frames inline.
   *
   * @param payload    The inner payload bytes to chunk (e.g. a 323-byte 0x51 extended packet).
   * @param chunkSize  Maximum bytes per BLE write (derived from negotiated MTU - 3).
   * @param seqByte    Random sequence byte (0x00–0xFF) for this transmission session.
   * @returns          Array of ready-to-send BLE write buffers.
   */
  export function buildChunkedFrames(payload: number[], chunkSize: number, seqByte: number): number[][] {
    const totalLen = payload.length;
    const chunks: number[][] = [];
    let offset = 0;
    let segmentIndex = 0;

    while (offset < totalLen) {
      const isFirstChunk = segmentIndex === 0;
      const headerSize = isFirstChunk ? 8 : 5;
      const maxDataLen = chunkSize - headerSize;
      const dataLen = Math.min(maxDataLen, totalLen - offset);
      const isLastChunk = (offset + dataLen) >= totalLen;

      let indexWord = segmentIndex;
      if (isLastChunk) indexWord |= 0x8000;

      const chunk = [
        0x40,
        seqByte & 0xFF,
        (indexWord >> 8) & 0xFF,
        indexWord & 0xFF,
      ];

      if (isFirstChunk) {
        chunk.push((totalLen >> 8) & 0xFF);
        chunk.push(totalLen & 0xFF);
        chunk.push((dataLen + 1) & 0xFF); // ZENGGE counts the 0x0B byte in the data length
        chunk.push(0x0B);
      } else {
        chunk.push(dataLen & 0xFF);
      }

      chunk.push(...payload.slice(offset, offset + dataLen));
      chunks.push(chunk);
      offset += dataLen;
      segmentIndex++;
    }

    return chunks;
  }


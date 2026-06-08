import { Platform } from 'react-native';
import { Buffer } from 'buffer';
import { AppLogger } from './AppLogger';
import { resolveProtocolForDevice } from '../protocols/ControllerRegistry';
import type { ProtocolResult } from '../protocols/IControllerProtocol';
import { enqueueWrite, resolveWritePriority, setWriteQueueGeneration } from './BleWriteQueue';
import { BLE_TIMING } from '../constants/bleTimingConstants';

export interface BleWriteStateRefs {
  writeGeneration: number;
  writeDebounceTimerRef: { current: ReturnType<typeof setTimeout> | null };
}

/**
 * executeWriteToDevice — Core BLE write function.
 * Manages debouncing for pattern/color writes and handles direct execution.
 */
export async function executeWriteToDevice(
  payload: number[],
  targetDeviceId: string | undefined,
  opts: { lowPriority?: boolean } | undefined,
  bleManager: any,
  connectedDevices: any[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: Map<string, any>,
  stateRefs: BleWriteStateRefs,
  setWriteGeneration: (gen: number) => void
): Promise<boolean | 'partial'> {
  const hexString = payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' ');
  AppLogger.setLastTxPayload(hexString);

  if (connectedDevices.length === 0 || Platform.OS === 'web') return true;

  const cmdByte = payload[0];
  const isPatternWrite = (cmdByte === 0x59 || cmdByte === 0x51 || cmdByte === 0x40);

  if (isPatternWrite) {
    const thisGeneration = opts?.lowPriority ? stateRefs.writeGeneration : stateRefs.writeGeneration + 1;
    if (!opts?.lowPriority) {
      setWriteGeneration(thisGeneration);
      setWriteQueueGeneration(thisGeneration);
    }

    return new Promise((resolve) => {
      if (stateRefs.writeDebounceTimerRef.current) {
        clearTimeout(stateRefs.writeDebounceTimerRef.current);
      }
      stateRefs.writeDebounceTimerRef.current = setTimeout(async () => {
        stateRefs.writeDebounceTimerRef.current = null;
        if (thisGeneration !== stateRefs.writeGeneration) {
          resolve(true);
          return;
        }
        const result = await _executeWriteToDeviceInternal(
          payload,
          targetDeviceId,
          thisGeneration,
          bleManager,
          connectedDevices,
          ghostedDeviceIds,
          mtuMap,
          adapterMap,
          stateRefs
        );
        resolve(result);
      }, BLE_TIMING.WRITE_DEBOUNCE_MS);
    });
  }

  return _executeWriteToDeviceInternal(
    payload,
    targetDeviceId,
    0, // Critical write bypasses generation check
    bleManager,
    connectedDevices,
    ghostedDeviceIds,
    mtuMap,
    adapterMap,
    stateRefs
  );
}

async function _executeWriteToDeviceInternal(
  payload: number[],
  targetDeviceId: string | undefined,
  capturedGeneration: number,
  bleManager: any,
  connectedDevices: any[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: Map<string, any>,
  stateRefs: BleWriteStateRefs
): Promise<boolean | 'partial'> {
  const targets = targetDeviceId
    ? connectedDevices.filter(d => d.id === targetDeviceId)
    : connectedDevices;

  if (targets.length === 0 && targetDeviceId) {
    AppLogger.warn(`Target device ${targetDeviceId} not found in connected devices`);
    return false;
  }

  const getDeviceMtu = (id: string) => mtuMap.get(id) ?? 186;
  const maxSafeSize = targetDeviceId ? getDeviceMtu(targetDeviceId) - 3 : Math.min(...targets.map(d => getDeviceMtu(d.id))) - 3;

  if (payload.length > maxSafeSize) {
    const cmdId = payload[0];
    if (cmdId === 0x51 && payload.length > 200) {
      AppLogger.warn('[BLE] 0x51 Payload exceeds safe MTU. Routing to writeChunked automatically.', { length: payload.length });
      await executeWriteChunked(payload, targetDeviceId, connectedDevices, ghostedDeviceIds, mtuMap, adapterMap);
      return true;
    }
    AppLogger.warn(`[BLE] PAYLOAD TOO LARGE: ${payload.length} bytes exceeds safe MTU window of ${maxSafeSize} bytes. Rejecting to prevent hardware lockup.`);
    return false;
  }

  const executeWrite = async (): Promise<boolean | 'partial'> => {
    if (capturedGeneration !== 0 && capturedGeneration !== stateRefs.writeGeneration) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'write_stale_dropped', capturedGeneration, currentGeneration: stateRefs.writeGeneration });
      return true;
    }

    const liveTargets = targets.filter(device => {
      if (ghostedDeviceIds.includes(device.id)) {
        AppLogger.warn(`[BLE] Write SKIPPED ghosted device ${device.id}`);
        return false;
      }
      return true;
    });

    const skippedGhosted = targets.length - liveTargets.length;
    if (liveTargets.length === 0) return skippedGhosted > 0 ? 'partial' : true;

    let allSucceeded = true;
    const base64Full = Buffer.from(payload).toString('base64');

    // FIX: 50ms inter-device gap prevents GATT 133 on Android multi-device groups.
    // writeCharacteristicWithoutResponse resolves when the write is SENT, not RECEIVED.
    // Without a gap, device 1's incoming GATT notification collides with device 2's
    // in-flight write, causing a buffer overflow → organic disconnect → auto-recovery cascade.
    for (const device of liveTargets) {
      const deviceAdapter = resolveProtocolForDevice(device.id, adapterMap);
      try {
        await device.writeCharacteristicWithoutResponseForService(
          deviceAdapter.serviceUUID,
          deviceAdapter.writeCharacteristicUUID,
          base64Full
        );
      } catch (writeError: unknown) {
        AppLogger.warn(`[BLE] Write failed for ${device.id}`, writeError instanceof Error ? writeError.message : String(writeError));
        allSucceeded = false;
      }
      await new Promise(res => setTimeout(res, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
    }

    if (skippedGhosted > 0 && allSucceeded) return 'partial';
    return allSucceeded;
  };

  // Route through priority queue — priority determined by first opcode byte
  const priority = capturedGeneration === 0
    ? resolveWritePriority(payload[0])   // critical path: use opcode classification
    : 'normal';                           // pattern write: always normal tier
  return enqueueWrite(priority, executeWrite, capturedGeneration);
}

/**
 * executeWriteChunked — Sends large payloads (>MTU) via sequential ZENGGE-framed chunks.
 */
export async function executeWriteChunked(
  payload: number[],
  targetDeviceId: string | undefined,
  connectedDevices: any[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: Map<string, any>
): Promise<void> {
  if (connectedDevices.length === 0 || Platform.OS === 'web') return;

  const targets = targetDeviceId
    ? connectedDevices.filter(d => d.id === targetDeviceId)
    : connectedDevices;

  if (targets.length === 0) return;

  const getDeviceMtu = (id: string) => mtuMap.get(id) ?? 186;
  const safeMtu = targetDeviceId ? getDeviceMtu(targetDeviceId) - 3 : Math.min(...targets.map(d => getDeviceMtu(d.id))) - 3;
  const chunkSize = Math.max(20, safeMtu);

  const totalLen = payload.length;
  const seqByte = Math.floor(Math.random() * 256) & 0xFF;

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
    if (isLastChunk) {
      indexWord |= 0x8000;
    }

    const chunk = [
      0x40,
      seqByte,
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

  AppLogger.log('BLE_CHUNKED_WRITE', { payloadLen: totalLen, numChunks: chunks.length, chunkSize });

  // Wrap entire chunk sequence as single 'bulk' queue entry — chunks must not
  // be interleaved with other writes (ordering requirement of ZENGGE 0x40 framing).
  await enqueueWrite('bulk', async () => {
    for (const chunk of chunks) {
      const b64 = Buffer.from(chunk).toString('base64');
      for (const device of targets) {
        if (ghostedDeviceIds.includes(device.id)) continue;
        const deviceAdapter = resolveProtocolForDevice(device.id, adapterMap);
        try {
          await device.writeCharacteristicWithoutResponseForService(
            deviceAdapter.serviceUUID, deviceAdapter.writeCharacteristicUUID, b64
          );
        } catch (e: unknown) {
          AppLogger.warn(`[BLE] writeChunked chunk failed for ${device.id}`, { error: e instanceof Error ? e.message : String(e) });
        }
        await new Promise(res => setTimeout(res, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
      }
      await new Promise(resolve => setTimeout(resolve, BLE_TIMING.WRITE_CHUNK_INTER_GAP_MS));
    }
    await new Promise(resolve => setTimeout(resolve, BLE_TIMING.WRITE_CHUNK_FINAL_SETTLE_MS));
    return true;
  });
}

/**
 * executeProtocolResults — Executes polymorphic adapter instructions.
 */
export async function executeProtocolResults(
  payloads: { targetDeviceId: string, result: ProtocolResult }[],
  opts: { lowPriority?: boolean } | undefined,
  bleManager: any,
  connectedDevices: any[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: Map<string, any>,
  stateRefs: BleWriteStateRefs,
  setWriteGeneration: (gen: number) => void
): Promise<boolean> {
  if (payloads.length === 0) return true;

  const isRateLimited = payloads.some(p => p.result.isRateLimited);
  const capturedGeneration = isRateLimited ? stateRefs.writeGeneration : 0;

  if (isRateLimited) {
    const thisGeneration = opts?.lowPriority ? stateRefs.writeGeneration : stateRefs.writeGeneration + 1;
    if (!opts?.lowPriority) {
      setWriteGeneration(thisGeneration);
      setWriteQueueGeneration(thisGeneration);
    }

    return new Promise((resolve) => {
      if (stateRefs.writeDebounceTimerRef.current) {
        clearTimeout(stateRefs.writeDebounceTimerRef.current);
      }
      stateRefs.writeDebounceTimerRef.current = setTimeout(async () => {
        stateRefs.writeDebounceTimerRef.current = null;
        if (thisGeneration !== stateRefs.writeGeneration) {
          resolve(true);
          return;
        }
        const res = await _executeProtocolResultsInternal(
          payloads,
          thisGeneration,
          connectedDevices,
          ghostedDeviceIds,
          mtuMap,
          adapterMap,
          stateRefs
        );
        resolve(res);
      }, BLE_TIMING.WRITE_DEBOUNCE_MS);
    });
  }

  return _executeProtocolResultsInternal(
    payloads,
    capturedGeneration,
    connectedDevices,
    ghostedDeviceIds,
    mtuMap,
    adapterMap,
    stateRefs
  );
}

async function _executeProtocolResultsInternal(
  payloads: { targetDeviceId: string, result: ProtocolResult }[],
  capturedGeneration: number,
  connectedDevices: any[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: Map<string, any>,
  stateRefs: BleWriteStateRefs
): Promise<boolean> {
  const executeWrite = async (): Promise<boolean> => {
    if (capturedGeneration !== 0 && capturedGeneration !== stateRefs.writeGeneration) {
      return true;
    }

    let allSucceeded = true;
    const getDeviceMtu = (id: string) => mtuMap.get(id) ?? 186;

    for (const { targetDeviceId, result } of payloads) {
      if (ghostedDeviceIds.includes(targetDeviceId)) continue;

      const device = connectedDevices.find(d => d.id === targetDeviceId);
      if (!device) continue;

      const adapter = resolveProtocolForDevice(targetDeviceId, adapterMap);
      const mtu = getDeviceMtu(targetDeviceId);
      const preparedResult = adapter.prepareForTransmission(result, mtu);

      for (let i = 0; i < preparedResult.packets.length; i++) {
        const base64 = Buffer.from(preparedResult.packets[i]).toString('base64');
        try {
          await device.writeCharacteristicWithoutResponseForService(
            adapter.serviceUUID,
            adapter.writeCharacteristicUUID,
            base64
          );
          if (i < preparedResult.packets.length - 1 && preparedResult.interPacketDelayMs > 0) {
            await new Promise(res => setTimeout(res, preparedResult.interPacketDelayMs));
          }
        } catch (e: unknown) {
          AppLogger.warn(`[BLE] executeProtocolResults failed for ${targetDeviceId}`, e instanceof Error ? e.message : String(e));
          allSucceeded = false;
          break;
        }
      }
      // FIX: 50ms inter-device gap prevents GATT 133 on Android multi-device groups.
      await new Promise(res => setTimeout(res, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
    }
    return allSucceeded;
  };

  // Route through priority queue
  const result = await enqueueWrite('normal', executeWrite as () => Promise<boolean | 'partial'>, capturedGeneration);
  return result !== false;
}

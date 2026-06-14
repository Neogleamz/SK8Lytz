import { Platform } from 'react-native';
import { Buffer } from 'buffer';
import { scrubPII } from '../utils/piiScrubber';
import { AppLogger } from './AppLogger';
import { resolveProtocolForDevice } from '../protocols/ControllerRegistry';
import type { IControllerProtocol, ProtocolResult } from '../protocols/IControllerProtocol';
import { enqueueWrite, resolveWritePriority, setWriteQueueGeneration } from './BleWriteQueue';
import { BLE_TIMING } from '../constants/bleTimingConstants';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';

export interface BleWriteStateRefs {
  writeGeneration: number;
  writeDebounceTimerRef: { current: ReturnType<typeof setTimeout> | null };
}


// R-08: bleManager and device objects come from react-native-ble-plx which does
// not re-export its Device class in a way that is importable without tight coupling.
// These structural aliases document the minimal interface we actually use.
type BleManagerLike = {
  writeCharacteristicWithoutResponseForDevice(
    deviceId: string,
    serviceUUID: string,
    charUUID: string,
    base64: string
  ): Promise<unknown>;
};
type BleDeviceLike = { id: string; writeCharacteristicWithoutResponseForService(s: string, c: string, b: string): Promise<unknown> };
// AdapterMapLike: the actual map in useBLE.ts holds full IControllerProtocol objects.
// We alias for clarity; callers pass Map<string, IControllerProtocol>.
type AdapterMapLike = Map<string, IControllerProtocol>;

/**
 * executeWriteToDevice — Core BLE write function.
 * Manages debouncing for pattern/color writes and handles direct execution.
 */
export async function executeWriteToDevice(
  payload: number[],
  targetDeviceId: string | undefined,
  opts: { lowPriority?: boolean } | undefined,
  bleManager: BleManagerLike,
  connectedDevices: BleDeviceLike[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: AdapterMapLike,
  stateRefs: BleWriteStateRefs,
  setWriteGeneration: (gen: number) => void
): Promise<boolean | 'partial'> {
  // BUFFER LOCKOUT DEFENSE (R-19): delegated to ZenggeProtocol.padStaticColorfulPayload()
  // Source: tools/ZENGGE_PROTOCOL_BIBLE.md §0x59 MINIMUM PIXELS
  payload = ZenggeProtocol.padStaticColorfulPayload(payload);

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
        try {
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
        } catch (e: unknown) {
          AppLogger.warn(`[BLE] Deferred write to device failed`, {
            error: e instanceof Error ? e.message : String(e),
            payload_size: payload.length,
            ssi: 0
          });
          resolve(false);
        }
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
  bleManager: BleManagerLike,
  connectedDevices: BleDeviceLike[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: AdapterMapLike,
  stateRefs: BleWriteStateRefs
): Promise<boolean | 'partial'> {
  const targets = targetDeviceId
    ? connectedDevices.filter(d => d.id === targetDeviceId)
    : connectedDevices;

  if (targets.length === 0 && targetDeviceId) {
    AppLogger.warn(`Target device '[REDACTED]' not found in connected devices`, { payload_size: payload.length, ssi: 0 });
    return false;
  }

  const getDeviceMtu = (id: string) => mtuMap.get(id) ?? 186;
  const maxSafeSize = targetDeviceId ? getDeviceMtu(targetDeviceId) - 3 : Math.min(...targets.map(d => getDeviceMtu(d.id))) - 3;

  if (payload.length > maxSafeSize) {
    const cmdId = payload[0];
    if (cmdId === 0x51 && payload.length > 200) {
      AppLogger.warn('[BLE] 0x51 Payload exceeds safe MTU. Routing to writeChunked automatically.', { payload_size: payload.length, ssi: 0 });
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
        AppLogger.warn(`[BLE] Write SKIPPED ghosted device ${scrubPII(device.id)}`);
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
    let index = 0;
    for (const device of liveTargets) {
      if (index > 0) {
        await new Promise(res => setTimeout(res, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
      }
      const deviceAdapter = resolveProtocolForDevice(device.id, adapterMap);
      try {
        await device.writeCharacteristicWithoutResponseForService(
          deviceAdapter.serviceUUID,
          deviceAdapter.writeCharacteristicUUID,
          base64Full
        );
      } catch (writeError: unknown) {
        AppLogger.warn(`[BLE] Write failed for '[REDACTED]'`, {
          error: writeError instanceof Error ? writeError.message : String(writeError),
          payload_size: payload.length,
          ssi: 0
        });
        allSucceeded = false;
      }
      index++;
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
  connectedDevices: BleDeviceLike[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: AdapterMapLike
): Promise<void> {
  if (connectedDevices.length === 0 || Platform.OS === 'web') return;

  const targets = targetDeviceId
    ? connectedDevices.filter(d => d.id === targetDeviceId)
    : connectedDevices;

  if (targets.length === 0) return;

  const getDeviceMtu = (id: string) => mtuMap.get(id) ?? 186;
  const safeMtu = targetDeviceId ? getDeviceMtu(targetDeviceId) - 3 : Math.min(...targets.map(d => getDeviceMtu(d.id))) - 3;
  const chunkSize = Math.max(20, safeMtu);

  const seqByte = Math.floor(Math.random() * 256) & 0xFF;
  // 0x40 frame construction delegated to ZenggeProtocol.buildChunkedFrames() (R-19)
  // Source: tools/ZENGGE_PROTOCOL_BIBLE.md §0x51 ZENGGE BLE Chunked Framing
  const chunks = ZenggeProtocol.buildChunkedFrames(payload, chunkSize, seqByte);

  AppLogger.log('BLE_CHUNKED_WRITE', { payloadLen: payload.length, numChunks: chunks.length, chunkSize });

  // Wrap entire chunk sequence as single 'bulk' queue entry — chunks must not
  // be interleaved with other writes (ordering requirement of ZENGGE 0x40 framing).
  await enqueueWrite('bulk', async () => {
    for (const chunk of chunks) {
      const b64 = Buffer.from(chunk).toString('base64');
      const liveTargetsChunk = targets.filter(d => !ghostedDeviceIds.includes(d.id));
      let index = 0;
      for (const device of liveTargetsChunk) {
        if (index > 0) {
          await new Promise(res => setTimeout(res, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
        }
        const deviceAdapter = resolveProtocolForDevice(device.id, adapterMap);
        try {
          await device.writeCharacteristicWithoutResponseForService(
            deviceAdapter.serviceUUID, deviceAdapter.writeCharacteristicUUID, b64
          );
        } catch (e: unknown) {
          AppLogger.warn(`[BLE] writeChunked chunk failed for '[REDACTED]'`, { 
            error: e instanceof Error ? e.message : String(e),
            payload_size: chunk.length,
            ssi: 0
          });
        }
        index++;
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
  bleManager: BleManagerLike,
  connectedDevices: BleDeviceLike[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: AdapterMapLike,
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
        try {
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
        } catch (e: unknown) {
          AppLogger.warn(`[BLE] Deferred execute protocol results failed`, {
            error: e instanceof Error ? e.message : String(e),
            payload_size: 0,
            ssi: 0
          });
          resolve(false);
        }
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
  connectedDevices: BleDeviceLike[],
  ghostedDeviceIds: string[],
  mtuMap: Map<string, number>,
  adapterMap: AdapterMapLike,
  stateRefs: BleWriteStateRefs
): Promise<boolean> {
  const executeWrite = async (): Promise<boolean> => {
    if (capturedGeneration !== 0 && capturedGeneration !== stateRefs.writeGeneration) {
      return true;
    }

    let allSucceeded = true;
    const getDeviceMtu = (id: string) => mtuMap.get(id) ?? 186;

    // Sequentialize per-device writes to prevent GATT 133 collisions on Android.
    // Packet ordering within a single device is preserved by the sequential inner loop.
    const livePayloads = payloads.filter(({ targetDeviceId }) => !ghostedDeviceIds.includes(targetDeviceId));
    let isFirstDevice = true;

    for (const { targetDeviceId, result } of livePayloads) {
      if (!isFirstDevice) {
        await new Promise(r => setTimeout(r, BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS));
      }
      isFirstDevice = false;

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
          AppLogger.warn(`[BLE] executeProtocolResults failed for ${scrubPII(targetDeviceId)}`, {
            error: e instanceof Error ? e.message : String(e),
            payload_size: preparedResult.packets[i].length,
            ssi: 0
          });
          allSucceeded = false;
        }
      }
    }
    return allSucceeded;
  };

  // Route through priority queue
  const result = await enqueueWrite('normal', executeWrite as () => Promise<boolean | 'partial'>, capturedGeneration);
  return result !== false;
}

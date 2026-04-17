import { useCallback, useEffect, useState } from 'react';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';

export interface BleLog {
  dir: 'TX' | 'RX';
  hex: string;
  t: number;
  dev?: string;
  note?: string;
}

interface UseDiagnosticLogProps {
  visible: boolean;
  liveRxPayload?: { deviceId: string; payloadHex: string; timestamp?: number } | null;
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void | boolean | 'partial'>;
  targetDeviceId: string | null;
}

export const useDiagnosticLog = ({
  visible,
  liveRxPayload,
  writeToDevice,
  targetDeviceId,
}: UseDiagnosticLogProps) => {
  const [logs, setLogs] = useState<BleLog[]>([]);
  const [lastSent, setLastSent] = useState<string>('');
  const [lastNote, setLastNote] = useState<string>('');

  // RX listener
  useEffect(() => {
    if (!visible || !liveRxPayload?.payloadHex) return;
    const entry: BleLog = {
      dir: 'RX',
      hex: liveRxPayload.payloadHex,
      t: liveRxPayload.timestamp || Date.now(),
      dev: liveRxPayload.deviceId,
    };
    const bytes = liveRxPayload.payloadHex.split(' ').map(h => parseInt(h, 16));
    const hw63 = ZenggeProtocol.parseHardwareSettingsResponse(bytes);
    if (hw63) {
      entry.note = `0x63 → LEDs:${hw63.ledPoints} ${hw63.icName} ${hw63.colorSortingName} sort:${hw63.colorSorting}`;
    }
    setLogs(prev => [entry, ...prev].slice(0, 200));
  }, [liveRxPayload, visible]);

  const transmit = useCallback(async (bytes: number[], note?: string) => {
    if (!writeToDevice) return;
    await writeToDevice(bytes, targetDeviceId ?? undefined).catch(e => AppLogger.error('[useDiagnosticLog] write failed', e));
    const hexStr = bytes.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    setLastSent(hexStr);
    setLastNote(note || '');
    setLogs(prev => [{ dir: 'TX' as const, hex: hexStr, t: Date.now(), note, dev: targetDeviceId ?? undefined }, ...prev].slice(0, 200));
    AppLogger.log('RAW_PAYLOAD', { dir: 'TX', hex: hexStr, note, deviceId: targetDeviceId ?? undefined });
  }, [writeToDevice, targetDeviceId]);

  const sendRawHex = useCallback(async (hexStr: string, note?: string) => {
    const bytes = hexStr.replace(/[^0-9A-Fa-f]/g, '').match(/.{1,2}/g)?.map(h => parseInt(h, 16)) || [];
    if (bytes.length === 0) return;
    await transmit(bytes, note);
  }, [transmit]);

  const clearLogs = () => setLogs([]);

  return {
    logs,
    lastSent,
    lastNote,
    transmit,
    sendRawHex,
    clearLogs,
  };
};

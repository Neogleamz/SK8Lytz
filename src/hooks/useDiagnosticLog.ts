import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useRef, useState } from 'react';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';

const VERDICT_LOG_KEY = '@sk8lytz_diag_test_log';
const VERDICT_LOG_MAX = 200;

/** Verdict for a single protocol test entry. */
export type TestVerdict = 'PASS' | 'FAIL' | 'AMBIGUOUS' | null;

/** Opcode coverage status — one per tracked command. */
export type OpcodeStatus = 'UNTESTED' | 'PASS' | 'FAIL' | 'AMBIGUOUS';

/** The 14 opcodes tracked by the Protocol Oracle coverage matrix. */
export const TRACKED_OPCODES = [
  '0x41', '0x42', '0x43', '0x51',
  '0x53', '0x56', '0x57', '0x58',
  '0x59', '0x62', '0x63', '0x71',
  '0x73', '0x74',
] as const;
export type TrackedOpcode = typeof TRACKED_OPCODES[number];

/** A persisted test log entry. */
export interface TestLogEntry {
  id: string;            // unique: `${opcode}-${timestamp}`
  opcode: string;        // e.g. '0x73'
  label: string;         // human-readable test name
  txHex: string;         // bytes sent
  rxHex?: string;        // bytes received (if any)
  verdict: TestVerdict;
  timestamp: number;
  note?: string;
}

export interface BleLog {
  dir: 'TX' | 'RX';
  hex: string;
  t: number;
  dev?: string;
  note?: string;
  entryId?: string; // links to TestLogEntry for verdict annotation
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

  // ── Verdict / Oracle state ────────────────────────────────────────────────
  const [testLog, setTestLog] = useState<TestLogEntry[]>([]);
  const [coverage, setCoverage] = useState<Record<TrackedOpcode, OpcodeStatus>>(
    Object.fromEntries(TRACKED_OPCODES.map(op => [op, 'UNTESTED'])) as Record<TrackedOpcode, OpcodeStatus>
  );
  // Track last TX entry id so verdict can be applied to it
  const lastEntryIdRef = useRef<string | null>(null);

  // Load persisted test log on mount
  useEffect(() => {
    AsyncStorage.getItem(VERDICT_LOG_KEY).then(raw => {
      try {
        if (!raw) return;
        const parsed: TestLogEntry[] = JSON.parse(raw);
        setTestLog(parsed);
        // Rebuild coverage from last verdict per opcode
        const cov = Object.fromEntries(TRACKED_OPCODES.map(op => [op, 'UNTESTED'])) as Record<TrackedOpcode, OpcodeStatus>;
        for (const entry of [...parsed].reverse()) {
          const op = entry.opcode as TrackedOpcode;
          if (TRACKED_OPCODES.includes(op) && cov[op] === 'UNTESTED' && entry.verdict) {
            cov[op] = entry.verdict as OpcodeStatus;
          }
        }
        setCoverage(cov);
      } catch (e) {
        AppLogger.warn('[useDiagnosticLog] Failed to parse verdict log', { error: String(e) });
      }
    }).catch(() => {});
  }, []);

  // ── RX listener ──────────────────────────────────────────────────────────
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
    // Attach RX to the last TX entry in testLog
    if (lastEntryIdRef.current) {
      setTestLog(prev => prev.map(e =>
        e.id === lastEntryIdRef.current ? { ...e, rxHex: liveRxPayload.payloadHex } : e
      ));
    }
    setLogs(prev => [entry, ...prev].slice(0, 200));
  }, [liveRxPayload, visible]);

  /**
   * Transmit bytes and create a pending TestLogEntry for verdict annotation.
   * @param opcode e.g. '0x73' — used to update coverage matrix
   */
  const transmit = useCallback(async (
    bytes: number[],
    note?: string,
    opcode?: string
  ) => {
    if (!writeToDevice) return;
    await writeToDevice(bytes, targetDeviceId ?? undefined).catch(e =>
      AppLogger.error('[useDiagnosticLog] write failed', e)
    );
    const hexStr = bytes.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    const entryId = `${opcode ?? 'raw'}-${Date.now()}`;
    lastEntryIdRef.current = entryId;

    setLastSent(hexStr);
    setLastNote(note || '');
    setLogs(prev => [{ dir: 'TX' as const, hex: hexStr, t: Date.now(), note, dev: targetDeviceId ?? undefined, entryId }, ...prev].slice(0, 200));
    AppLogger.log('RAW_PAYLOAD', { dir: 'TX', hex: hexStr, note, deviceId: targetDeviceId ?? undefined });

    if (opcode) {
      const newEntry: TestLogEntry = {
        id: entryId,
        opcode,
        label: note ?? opcode,
        txHex: hexStr,
        verdict: null,
        timestamp: Date.now(),
      };
      setTestLog(prev => [newEntry, ...prev].slice(0, VERDICT_LOG_MAX));
    }
  }, [writeToDevice, targetDeviceId]);

  const sendRawHex = useCallback(async (hexStr: string, note?: string, opcode?: string) => {
    const bytes = hexStr.replace(/[^0-9A-Fa-f]/g, '').match(/.{1,2}/g)?.map(h => parseInt(h, 16)) || [];
    if (bytes.length === 0) return;
    await transmit(bytes, note, opcode);
  }, [transmit]);

  /**
   * Annotate a test entry with a verdict and persist to AsyncStorage.
   * Also updates the opcode coverage matrix.
   */
  const setVerdict = useCallback((entryId: string, opcode: string, verdict: TestVerdict) => {
    setTestLog(prev => {
      const updated = prev.map(e => e.id === entryId ? { ...e, verdict } : e);
      AsyncStorage.setItem(VERDICT_LOG_KEY, JSON.stringify(updated)).catch(() => {});
      return updated;
    });
    if (TRACKED_OPCODES.includes(opcode as TrackedOpcode) && verdict) {
      setCoverage(prev => ({ ...prev, [opcode as TrackedOpcode]: verdict as OpcodeStatus }));
    }
  }, []);

  /** Set verdict on the most recently transmitted entry. */
  const setLastVerdict = useCallback((opcode: string, verdict: TestVerdict) => {
    if (!lastEntryIdRef.current) return;
    setVerdict(lastEntryIdRef.current, opcode, verdict);
  }, [setVerdict]);

  const clearLogs = () => setLogs([]);

  const clearTestLog = useCallback(() => {
    setTestLog([]);
    setCoverage(Object.fromEntries(TRACKED_OPCODES.map(op => [op, 'UNTESTED'])) as Record<TrackedOpcode, OpcodeStatus>);
    AsyncStorage.removeItem(VERDICT_LOG_KEY).catch(() => {});
  }, []);

  return {
    // BLE trace
    logs,
    lastSent,
    lastNote,
    transmit,
    sendRawHex,
    clearLogs,
    // Protocol Oracle
    testLog,
    coverage,
    setVerdict,
    setLastVerdict,
    clearTestLog,
  };
};

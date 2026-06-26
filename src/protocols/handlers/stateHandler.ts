import { ZenggeProtocol } from '../ZenggeProtocol';

type AppLoggerLike = {
  log: (...args: unknown[]) => void;
  warn: (...args: unknown[]) => void;
  error: (...args: unknown[]) => void;
};
let _appLogger: AppLoggerLike | null = null;
function getAppLogger() {
  if (!_appLogger) {
    try { _appLogger = require('../../services/AppLogger').AppLogger; } catch (_e) { _appLogger = console; }
  }
  return _appLogger;
}

export function turnOn(protocol: ZenggeProtocol): number[] {
    return protocol.wrapCommand([0x71, 0x23, 0x0f, 0xa3]);
  }

export function turnOff(protocol: ZenggeProtocol): number[] {
    return protocol.wrapCommand([0x71, 0x24, 0x0f, 0xa4]);
  }

/**
   * Convenience alias — use in Diagnostic Lab and UI code for readability.
   * Delegates to turnOn() / turnOff().
   */
  export function setPower(protocol: ZenggeProtocol, on: boolean): number[] {
    return on ? protocol.turnOn() : protocol.turnOff();
  }

/**
   * Build a 0x10 session time sync packet.
   * ZENGGE app sends this on EVERY successful BLE connection handshake,
   * before any pattern commands. Without it, the hardware session clock
   * starts from epoch 0 — timing-sensitive effects may drift or misfire.
   *
   * Format (8 bytes + checksum):
   *   [0x10, year-2000, month(1-12), day(1-31), hour(0-23), min(0-59), sec(0-59), weekday(0=Sun), checksum]
   */
  export function setSessionTime(protocol: ZenggeProtocol): number[] {
    const now = new Date();
    const raw = [
      0x10,
      (now.getFullYear() - 2000) & 0xFF, // year offset from 2000
      now.getMonth() + 1,                 // 1–12
      now.getDate(),                      // 1–31
      now.getHours(),                     // 0–23
      now.getMinutes(),                   // 0–59
      now.getSeconds(),                   // 0–59
      now.getDay(),                       // 0=Sun, 1=Mon, ..., 6=Sat
    ];
    raw.push(protocol.calculateChecksum(raw));
    return protocol.wrapCommand(raw);
  }

/** Convenience: clear all paired remotes while keeping same auth mode */
  export function clearRfRemotes(protocol: ZenggeProtocol, currentMode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED' = 'ALLOW_PAIRED'): number[] {
    return protocol.setRfRemoteState(currentMode, true);
  }

/** Query current RF remote auth mode — device responds with 0x2B packet */
  export function queryRfRemoteState(protocol: ZenggeProtocol): number[] {
    const cmd = [0x2B, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00];
    const checksum = protocol.calculateChecksum(cmd);
    return protocol.wrapCommand([...cmd, checksum]);
  }

/**
   * Parses a 0x2B response packet to determine the current RF auth mode.
   * Inner payload byte[0] = 0x2B, byte[1] = modeByte
   */
  export function parseRfRemoteState(payload: number[]): {
    mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED';
    modeName: string;
    pairedCount: number;
    pairedRemoteIds: string[];
  } | null {
    // Response is wrapped — locate the 0x2B marker in the inner payload
    const innerStart = payload.findIndex(b => b === 0x2B);
    if (innerStart < 0 || innerStart + 1 >= payload.length) return null;
    const modeByte = payload[innerStart + 1];
    const pairedCount = payload[innerStart + 2] ?? 0;
    const modeMap: Record<number, 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'> = {
      0x01: 'ALLOW_ALL',
      0x02: 'ALLOW_NONE',
      0x03: 'ALLOW_PAIRED',
    };
    const mode = modeMap[modeByte] ?? 'ALLOW_ALL';
    const modeNames: Record<string, string> = {
      ALLOW_ALL: 'Allow All Remotes',
      ALLOW_NONE: 'Block All Remotes',
      ALLOW_PAIRED: 'Paired Remote Only',
    };

    const pairedRemoteIds: string[] = [];
    let offset = innerStart + 3;
    for (let i = 0; i < pairedCount; i++) {
      if (offset + 4 <= payload.length) {
        const idBytes = payload.slice(offset, offset + 4);
        const hexId = idBytes.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join('');
        pairedRemoteIds.push(hexId);
        offset += 4;
      }
    }

    return { mode, modeName: modeNames[mode], pairedCount, pairedRemoteIds };
  }


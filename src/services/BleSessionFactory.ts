/**
 * BleSessionFactory.ts — Centralized GATT Session Creation
 *
 * Encapsulates the critical "connect → discover → resolve adapter" sequence
 * that was previously duplicated across 4 callsites:
 *   - handshakeDevice()     (useBLE.ts)
 *   - pingDevice()          (useBLE.ts)
 *   - interrogateDevice()   (useBLESweeper.ts)
 *   - initiateRecovery()    (useBLEAutoRecovery.ts)
 *
 * CRITICAL INVARIANT:
 *   discoverAllServicesAndCharacteristics() MUST always run on every new
 *   GATT session. The native BLE stack (Android + iOS) uses it to populate
 *   internal characteristic handle maps. Skipping it — even on a cache hit —
 *   leaves the maps empty, causing all write/monitor operations to silently fail.
 *   This bug was independently fixed in all 4 callsites; this factory ensures
 *   it can NEVER regress again.
 *
 * Design Decisions:
 *   - Module-level utility (NOT a React hook) — callable from any async context
 *   - AbortSignal passthrough for P1 preemption support
 *   - No MTU negotiation — caller-specific (handshake does it, ping doesn't)
 *   - No notification setup — caller-specific
 *   - Retry logic with GATT 133 / transient error handling baked in
 */
import type { BleManager, Device } from 'react-native-ble-plx';
import type { IControllerProtocol } from '../protocols/IControllerProtocol';
import { resolveProtocol, getDefaultProtocol, getProtocolById } from '../protocols/ControllerRegistry';
import { AppLogger } from './AppLogger';
import { BleCharacteristicCache } from './BleCharacteristicCache';
import { scrubPII } from '../utils/piiScrubber';
import { jitteredDelay } from '../utils/backoff';


/** Result of a successful GATT session creation */
export interface GattSessionResult {
  /** The connected + discovered device handle */
  conn: Device;
  /** Resolved protocol adapter (from cache or fresh service UUID enumeration) */
  adapter: IControllerProtocol;
  /** Whether the adapter was resolved from the BleCharacteristicCache */
  usedCache: boolean;
}

/** Options for createGattSession */
export interface CreateGattSessionOptions {
  /** Connection timeout in ms (default: 6000) */
  timeout?: number;
  /** Number of connection retry attempts (default: 2) */
  retries?: number;
  /** AbortSignal for P1 preemption — checked at each async boundary */
  signal?: AbortSignal;
  /** Logging context identifier (e.g. 'handshakeDevice', 'interrogateDevice') */
  context?: string;
  /**
   * Optional manufacturer data for adapter resolution heuristics.
   * handshakeDevice passes this from the BLE advertisement; other callers omit it.
   */
  manufacturerData?: string;
}

/**
 * Creates a fully-initialized GATT session with a BLE device.
 *
 * Sequence:
 *   1. Connect (with retry loop for GATT 133 / transient errors)
 *   2. discoverAllServicesAndCharacteristics() — ALWAYS (the invariant)
 *   3. Resolve adapter from cache or fresh service UUID enumeration
 *   4. Persist adapter to cache if freshly resolved
 *
 * @throws If connection fails after all retries, or if AbortSignal is triggered
 */
export async function createGattSession(
  bleManager: BleManager,
  mac: string,
  options: CreateGattSessionOptions = {},
): Promise<GattSessionResult> {
  const {
    timeout = 6000,
    retries = 2,
    signal,
    context = 'createGattSession',
    manufacturerData,
  } = options;

  // ── Step 1: Connect (with retry loop) ──────────────────────────────────
  let conn: Device | null = null;
  let lastErr: Error | null = null;

  // Exponential backoff delays for GATT 133 recovery (indexed by attempt-1).
  // refreshGatt: 'OnConnected' triggers BluetoothGatt.refresh() via reflection,
  // clearing the stale service table that causes Android GATT 133 (0x85) errors.
  const GATT_BACKOFF_MS = [500, 1500] as const;

  for (let attempt = 1; attempt <= retries; attempt++) {
    if (signal?.aborted) {
      throw new Error(`[BleSessionFactory] Aborted before connect (${context})`);
    }

    try {
      const isConnected = await bleManager.isDeviceConnected(mac);
      if (isConnected) {
        // Device is natively connected — reuse the handle via connectedDevices lookup
        const devicesList: Device[] = await bleManager.connectedDevices([]).catch(() => []);
        conn = devicesList.find((d) => d.id === mac) ?? null;
        if (!conn) {
          // Not found in connected list — connect fresh
          conn = await bleManager.connectToDevice(mac, { timeout });
        }
      } else {
        conn = await bleManager.connectToDevice(
          mac,
          attempt > 1 ? { timeout, refreshGatt: 'OnConnected' } : { timeout },
        );
      }
      break;
    } catch (e: unknown) {
      lastErr = e instanceof Error ? e : new Error(String(e));
      const errStr = String(e);
      const isTransient = errStr.includes('133') || errStr.includes('133 (0x85)')
        || errStr.includes('connection failed') || errStr.includes('timed out')
        || errStr.includes('Peer removed');

      if (isTransient && attempt < retries) {
        // R-03: jitteredDelay() randomizes within ±jitter ms to prevent reconnect storms.
        // GATT_BACKOFF_MS = [500, 1500] base delays; actual delay = base ± 500ms random.
        const delay = jitteredDelay(GATT_BACKOFF_MS[attempt - 1] ?? 1500, 500);
        AppLogger.warn(`[BleSessionFactory] GATT 133 on ${scrubPII(mac)}. Attempt ${attempt}/${retries} — retrying in ${delay}ms with refreshGatt`, { context, error: errStr });
        await bleManager.cancelDeviceConnection(mac).catch(() => {});
        await new Promise(resolve => setTimeout(resolve, delay));
      } else if (errStr.includes('already')) {
        // "already connected" — not an error, grab the handle from connectedDevices
        const devicesList: Device[] = await bleManager.connectedDevices([]).catch(() => []);
        conn = devicesList.find((d) => d.id === mac) ?? null;
        if (!conn) {
          conn = await bleManager.connectToDevice(mac, { timeout });
        }
        break;
      } else {
        // Non-transient or final attempt — don't retry
        break;
      }
    }
  }

  if (!conn) {
    throw lastErr ?? new Error(`[BleSessionFactory] Failed to connect to ${mac} after ${retries} attempts`);
  }

  // ── Step 2: CRITICAL — Discover all services and characteristics ───────
  // This MUST always run, even on cache hits. See module docstring for why.
  if (signal?.aborted) {
    throw new Error(`[BleSessionFactory] Aborted before discovery (${context})`);
  }
  await conn.discoverAllServicesAndCharacteristics();

  // ── Step 3: Resolve protocol adapter (cache-aware) ─────────────────────
  if (signal?.aborted) {
    throw new Error(`[BleSessionFactory] Aborted after discovery (${context})`);
  }

  let adapter: IControllerProtocol | null = null;
  let usedCache = false;

  const cachedGatt = await BleCharacteristicCache.get(mac);
  if (cachedGatt) {
    adapter = getProtocolById(cachedGatt.protocolId);
    if (adapter) {
      usedCache = true;
      AppLogger.log('BLE_STATE_CHANGE', { event: 'gatt_cache_hit', context, deviceId: scrubPII(mac) });
    }
  }

  if (!adapter) {
    try {
      const svcs = await conn.services();
      const svcUUIDs: string[] = svcs.map((s: { uuid: string }) => s.uuid);
      adapter = resolveProtocol(svcUUIDs, manufacturerData) ?? getDefaultProtocol();
    } catch (_e: unknown) {
      AppLogger.warn(`[BleSessionFactory] Failed resolving service UUIDs for ${scrubPII(mac)}, falling back to default protocol`, { context, error: _e instanceof Error ? _e.message : String(_e) });
      adapter = getDefaultProtocol();
    }
    await BleCharacteristicCache.set(mac, adapter.protocolId);
  }

  return { conn, adapter, usedCache };
}

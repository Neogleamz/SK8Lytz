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
import type { IControllerProtocol } from '../protocols/IControllerProtocol';
import { resolveProtocol, getDefaultProtocol, getProtocolById } from '../protocols/ControllerRegistry';
import { AppLogger } from './AppLogger';
import { BleCharacteristicCache } from './BleCharacteristicCache';

/** Result of a successful GATT session creation */
export interface GattSessionResult {
  /** The connected + discovered device handle */
  conn: any;
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
  bleManager: any,
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
  let conn: any = null;
  let lastErr: any = null;

  for (let attempt = 1; attempt <= retries; attempt++) {
    if (signal?.aborted) {
      throw new Error(`[BleSessionFactory] Aborted before connect (${context})`);
    }

    try {
      const isConnected = await bleManager.isDeviceConnected(mac);
      if (isConnected) {
        // Device is natively connected — reuse the handle
        // Try deviceForDevice first (lightweight), fall back to the device itself
        try {
          conn = await bleManager.deviceForDevice(mac);
        } catch {
          // deviceForDevice not available on all platforms — use connectedDevices
          const devicesList = await bleManager.connectedDevices([]).catch(() => []);
          conn = devicesList.find((d: any) => d.id === mac);
          if (!conn) {
            // Last resort: just connect fresh
            conn = await bleManager.connectToDevice(mac, { timeout });
          }
        }
      } else {
        conn = await bleManager.connectToDevice(mac, { timeout });
      }
      break;
    } catch (e: any) {
      lastErr = e;
      const errStr = String(e);
      const isTransient = errStr.includes('133') || errStr.includes('133 (0x85)')
        || errStr.includes('connection failed') || errStr.includes('timed out')
        || errStr.includes('Peer removed');

      if (isTransient) {
        AppLogger.warn(`[BleSessionFactory] Transient error linking ${mac}. Attempt ${attempt}/${retries}...`, { context, error: errStr });
        await bleManager.cancelDeviceConnection(mac).catch(() => {});
        await new Promise(resolve => setTimeout(resolve, 300));
      } else if (errStr.includes('already')) {
        // "already connected" — not an error, grab the handle
        try {
          conn = await bleManager.deviceForDevice(mac);
        } catch {
          conn = await bleManager.connectToDevice(mac, { timeout });
        }
        break;
      } else {
        // Non-transient, non-recoverable error — don't retry
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
      AppLogger.log('BLE_STATE_CHANGE', { event: 'gatt_cache_hit', context, mac });
    }
  }

  if (!adapter) {
    try {
      const svcs = await conn.services();
      const svcUUIDs = svcs.map((s: any) => s.uuid as string);
      adapter = resolveProtocol(svcUUIDs, manufacturerData) ?? getDefaultProtocol();
    } catch (_e: any) {
      AppLogger.warn(`[BleSessionFactory] Failed resolving service UUIDs for ${mac}, falling back to default protocol`, { context, error: String(_e) });
      adapter = getDefaultProtocol();
    }
    await BleCharacteristicCache.set(mac, adapter.protocolId);
  }

  return { conn, adapter, usedCache };
}

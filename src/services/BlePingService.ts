import { Platform } from 'react-native';
import { Buffer } from 'buffer';
import { AppLogger } from './appLogger';
import { createGattSession } from './BleSessionFactory';
import { type PingResult, isPingResult } from '../types/dashboard.types';
import { enqueueWrite, enqueueDelay } from './BleWriteQueue';
import { scrubPII } from '../utils/piiScrubber';

// R-08: react-native-ble-plx BleManager type is not importable without tight coupling.
// Structural alias for the minimal interface used by the ping flow.
type BleManagerPingLike = {
  writeCharacteristicWithoutResponseForDevice(id: string, svc: string, chr: string, b64: string): Promise<unknown>;
  cancelDeviceConnection(id: string): Promise<unknown>;
  monitorCharacteristicForDevice(
    id: string, svc: string, chr: string,
    cb: (err: Error | null, char: { value: string | null } | null) => void
  ): { remove(): void };
};

/**
 * executePingDevice — Wizard-exclusive atomic GATT session.
 * Connect → Blink → Probe EEPROM → Turn Off → Disconnect.
 * Designed for use in HardwareSetupWizardScreen only. Bypasses connectedDevices requirement.
 * Returns hwConfig (ledPoints, icName, etc.) or null if probe timed out.
 */
export async function executePingDevice(
  bleManager: BleManagerPingLike,
  mac: string,
  blinkPayload: number[],
  options?: {
    probe?: boolean;
    duration?: number;
    turnOffAtEnd?: boolean;
  }
): Promise<PingResult | null> {
  if (Platform.OS === 'web' || !bleManager) return null;

  const probe = options?.probe ?? true;
  const duration = options?.duration ?? 8000;
  const turnOffAtEnd = options?.turnOffAtEnd ?? true;

  try {
    // ── BleSessionFactory: connect → discover → resolve (single source of truth) ──
    // Cast justified: the caller always passes a real BleManager instance; BleManagerPingLike
    // is a structural subset type we defined to avoid tight coupling at the type level.
    const { conn: _pingConn, adapter: pingAdapter } = await createGattSession(
      bleManager as unknown as import('react-native-ble-plx').BleManager, mac, {
        timeout: 6000,
        retries: 2,
        context: 'pingDevice',
      });

    // ── Step 2: Write Blink (channel is now hot — no Phantom Blink) ───────────
    const b64Blink = Buffer.from(blinkPayload).toString('base64');
    await enqueueWrite('critical', async () => {
      await bleManager.writeCharacteristicWithoutResponseForDevice(
        mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID, b64Blink
      );
      return true;
    }).catch((e: unknown) => {
      AppLogger.warn('[BLE] pingDevice blink write failed (non-fatal)', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
    });

    let hwConfig: PingResult | null = null;

    if (probe) {
      // ── Step 3: Probe EEPROM (same GATT session — no collision) ──────────────
      hwConfig = await new Promise<PingResult | null>((resolve) => {
        let accumulatedTelemetry: Partial<PingResult> | null = null;

        const timer = setTimeout(() => {
          sub.remove();
          if (accumulatedTelemetry) {
            AppLogger.warn(`[BLE pingDevice] Partial telemetry for ${scrubPII(mac)}. Returning partial.`, { payload_size: 0, ssi: 0 });
            resolve(isPingResult(accumulatedTelemetry) ? accumulatedTelemetry : null);
          } else {
            AppLogger.warn(`[BLE pingDevice] Probe timed out for ${scrubPII(mac)} after 3500ms.`, { payload_size: 0, ssi: 0 });
            resolve(null);
          }
        }, 3500);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac,
          pingAdapter.serviceUUID,
          pingAdapter.notifyCharacteristicUUID,
          (err: Error | null, char: { value: string | null } | null) => {
            if (err) return;
            if (!char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              const hwParsed = pingAdapter.parseSettingsResponse(raw);
              if (hwParsed) accumulatedTelemetry = { ...accumulatedTelemetry, ...hwParsed };
              const rfParsed = pingAdapter.parseRfRemoteState(raw);
              if (rfParsed) {
                accumulatedTelemetry = {
                  ...accumulatedTelemetry,
                  rfMode: rfParsed.mode,
                  rfPairedCount: rfParsed.pairedCount
                };
              }
              if (accumulatedTelemetry?.detected && accumulatedTelemetry?.rfMode) {
                clearTimeout(timer);
                sub.remove();
                AppLogger.log('DEVICE_DISCOVERED', { context: 'pingDevice_probe_success', deviceId: scrubPII(mac) });
                if (isPingResult(accumulatedTelemetry)) resolve(accumulatedTelemetry);
              }
            } catch (e: unknown) {
              AppLogger.warn('[BLE] Parse error during pingDevice telemetry monitor', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
            }
          }
        );

        // Fire queries after giving the notification monitor 400ms to set up.
        // Uses adapter's polymorphic parse methods — Zengge parses EEPROM,
        // BanlanX returns null for both.
        enqueueDelay('critical', 400);

        const queryResult = pingAdapter.buildQuerySettings(false);
        if (queryResult.packets.length > 0) {
          const b64HW = Buffer.from(queryResult.packets[0]).toString('base64');
          enqueueWrite('critical', async () => {
            await bleManager.writeCharacteristicWithoutResponseForDevice(
              mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID, b64HW
            );
            return true;
          }).catch((e: unknown) => AppLogger.warn('[BLE pingDevice] HW query write failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 }));
        }

        enqueueDelay('critical', 200);

        const rfResult = pingAdapter.buildQueryRfRemoteState();
        if (rfResult.packets.length > 0) {
          const b64RF = Buffer.from(rfResult.packets[0]).toString('base64');
          enqueueWrite('critical', async () => {
            await bleManager.writeCharacteristicWithoutResponseForDevice(
              mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID, b64RF
            );
            return true;
          }).catch((e: unknown) => AppLogger.warn('[BLE pingDevice] RF query write failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 }));
        }
      });
    }

    // ── Step 4: Wait so user can see the blink ───────────────────────
    // R-16 note: this is a UX dwell delay (not a GATT write timing), so a
    // raw setTimeout is correct here — enqueueDelay is for inter-write gaps only.
    if (duration > 0) {
      await new Promise(r => setTimeout(r, duration));
    }

    // ── Step 5: Turn Off ─────────────────────────────────────────────────────
    if (turnOffAtEnd) {
      const offResult = pingAdapter.buildPowerOff();
      if (offResult.packets.length > 0) {
        await enqueueWrite('critical', async () => {
          await bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID,
            Buffer.from(offResult.packets[0]).toString('base64')
          );
          return true;
        }).catch((e: unknown) => {
          AppLogger.warn('[BLE] pingDevice turn-off write failed (non-fatal)', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
        });
      }
    }

    return hwConfig;
  } catch (err: unknown) {
    AppLogger.warn(`[BLE pingDevice] Failed for ${scrubPII(mac)}:`, { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
    return null;
  } finally {
    // ── Step 6: Always disconnect cleanly ────────────────────────────────────
    await bleManager.cancelDeviceConnection(mac).catch((e: unknown) => {
      AppLogger.warn('[BLE] pingDevice cancelDeviceConnection failed', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
    });
  }
}

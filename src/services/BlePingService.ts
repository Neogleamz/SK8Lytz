import { Platform } from 'react-native';
import { Buffer } from 'buffer';
import { AppLogger } from './AppLogger';
import { createGattSession } from './BleSessionFactory';
import { acquireGattLock } from '../hooks/ble/useBLEGattMutex';

/**
 * executePingDevice — Wizard-exclusive atomic GATT session.
 * Connect → Blink → Probe EEPROM → Turn Off → Disconnect.
 * Designed for use in HardwareSetupWizardScreen only. Bypasses connectedDevices requirement.
 * Returns hwConfig (ledPoints, icName, etc.) or null if probe timed out.
 */
export async function executePingDevice(
  bleManager: any,
  mac: string,
  blinkPayload: number[]
): Promise<any> {
  if (Platform.OS === 'web' || !bleManager) return null;

  const lockHandle = await acquireGattLock(1);
  if (!lockHandle) {
    AppLogger.warn(`[BLE] pingDevice rejected — could not acquire GATT lock`);
    return null;
  }
  const { release } = lockHandle;

  try {
    // ── BleSessionFactory: connect → discover → resolve (single source of truth) ──
    const { conn: _pingConn, adapter: pingAdapter } = await createGattSession(bleManager, mac, {
      timeout: 6000,
      retries: 2,
      context: 'pingDevice',
    });

    // ── Step 2: Write Blink (channel is now hot — no Phantom Blink) ───────────
    const b64Blink = Buffer.from(blinkPayload).toString('base64');
    await bleManager.writeCharacteristicWithoutResponseForDevice(
      mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID, b64Blink
    ).catch((e: any) => {
      AppLogger.warn('[BLE] pingDevice blink write failed (non-fatal)', { mac, error: e?.message });
    });

    // ── Step 3: Probe EEPROM (same GATT session — no collision) ──────────────
    const hwConfig = await new Promise<any>((resolve) => {
      let accumulatedTelemetry: any = null;

      const timer = setTimeout(() => {
        sub.remove();
        if (accumulatedTelemetry) {
          AppLogger.warn(`[BLE pingDevice] Partial telemetry for ${mac}. Returning partial.`);
          resolve(accumulatedTelemetry);
        } else {
          AppLogger.warn(`[BLE pingDevice] Probe timed out for ${mac} after 3500ms.`);
          resolve(null);
        }
      }, 3500);

      const sub = bleManager.monitorCharacteristicForDevice(
        mac,
        pingAdapter.serviceUUID,
        pingAdapter.notifyCharacteristicUUID,
        (err: any, char: any) => {
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
              AppLogger.log('DEVICE_DISCOVERED', { context: 'pingDevice_probe_success', deviceId: mac });
              resolve(accumulatedTelemetry);
            }
          } catch (e) {
            AppLogger.warn('[BLE] Parse error during pingDevice telemetry monitor', e);
          }
        }
      );

      // Fire queries after giving the notification monitor 400ms to set up.
      // Uses adapter's polymorphic parse methods — Zengge parses EEPROM,
      // BanlanX returns null for both.
      setTimeout(() => {
        const queryResult = pingAdapter.buildQuerySettings(false);
        if (queryResult.packets.length > 0) {
          const b64HW = Buffer.from(queryResult.packets[0]).toString('base64');
          bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID, b64HW
          ).catch((e: any) => AppLogger.warn('[BLE pingDevice] HW query write failed', { error: String(e) }));
        }

        setTimeout(() => {
          const rfResult = pingAdapter.buildQueryRfRemoteState();
          if (rfResult.packets.length > 0) {
            const b64RF = Buffer.from(rfResult.packets[0]).toString('base64');
            bleManager.writeCharacteristicWithoutResponseForDevice(
              mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID, b64RF
            ).catch((e: any) => AppLogger.warn('[BLE pingDevice] RF query write failed', { error: String(e) }));
          }
        }, 200);
      }, 400);
    });

    // ── Step 4: Wait so user can see the blink (probe ran concurrently) ───────
    await new Promise(r => setTimeout(r, 8000));

    // ── Step 5: Turn Off ─────────────────────────────────────────────────────
    const offResult = pingAdapter.buildPowerOff();
    if (offResult.packets.length > 0) {
      await bleManager.writeCharacteristicWithoutResponseForDevice(
        mac, pingAdapter.serviceUUID, pingAdapter.writeCharacteristicUUID,
        Buffer.from(offResult.packets[0]).toString('base64')
      ).catch((e: any) => {
        AppLogger.warn('[BLE] pingDevice turn-off write failed (non-fatal)', { mac, error: e?.message });
      });
    }

    return hwConfig;
  } catch (err: any) {
    AppLogger.warn(`[BLE pingDevice] Failed for ${mac}:`, { error: String(err) });
    return null;
  } finally {
    // ── Step 6: Always disconnect cleanly ────────────────────────────────────
    await bleManager.cancelDeviceConnection(mac).catch((e: any) => {
      AppLogger.warn('[BLE] pingDevice cancelDeviceConnection failed', { mac, error: e?.message });
    });
    release();
  }
}

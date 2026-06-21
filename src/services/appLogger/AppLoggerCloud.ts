import { supabase } from '../supabaseClient';
import { AppSettingsService } from '../AppSettingsService';
import * as Device from 'expo-device';
import { FlightRecorder } from '../../utils/FlightRecorder';
import { LogEntry, EventType } from './types';
import type { Device as BleDevice } from 'react-native-ble-plx';

export class AppLoggerCloud {
  static async pushFastLaneError(
    event: EventType,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    payload: Record<string, any>,
    sessionId: string
  ) {
    if (!supabase) return;

    let safeErrorString = 'Unknown error';
    try {
      safeErrorString = String(payload.message || payload.error || payload.errorMessage || JSON.stringify(payload));
    } catch {
      safeErrorString = '[Circular or Unparseable Error Object]';
    }

    supabase.from('telemetry_errors').insert({
      session_id: sessionId,
      event_type: event,
      error_message: safeErrorString.substring(0, 500),
      stack_trace: payload.stack || payload.stackTrace || null,
      raw_context: {
        ...payload,
        host_device_id: Device.osInternalBuildId || Device.modelId || 'unknown'
      },
      payload_size: payload.payload_size || null,
      operation_type: payload.operation_type || null
    }).then(
      ({ error }) => {
        if (error && __DEV__) console.warn('[AppLogger] VIP Fast-Lane failed:', error.message);
      },
      (e: unknown) => { if (__DEV__) console.warn('[AppLogger] VIP insert failed (network):', e instanceof Error ? e.message : String(e)); }
    );

      let normalizedSeverity = 'ERROR';
      if (payload.severity) {
        const rawSev = String(payload.severity).toUpperCase();
        if (rawSev === 'WARNING') normalizedSeverity = 'WARN';
        else if (rawSev === 'CRITICAL') normalizedSeverity = 'FATAL';
        else if (['FATAL', 'ERROR', 'WARN', 'INFO'].includes(rawSev)) {
          normalizedSeverity = rawSev;
        }
      }

      // JSON round-trip produces a plain JSON-serializable object that satisfies the Supabase Json type.
      // This avoids `as unknown as Json` laundering â€” if any field is unserializable it becomes null.
      const breadcrumbsJson = structuredClone(FlightRecorder.getBreadcrumbs()) as unknown as import('../../types/supabase').Json;
      const environmentStateJson = structuredClone({
        ...payload,
        host_device_id: Device.osInternalBuildId || Device.modelId || 'unknown',
        session_id: sessionId,
        event_type: event
      }) as unknown as import('../../types/supabase').Json;

      supabase.from('crash_telemetry').insert({
      error_signature: safeErrorString.substring(0, 500),
      stack_trace: payload.stack || payload.stackTrace || null,
      breadcrumbs: breadcrumbsJson,
      environment_state: environmentStateJson,
      severity: normalizedSeverity,
      app_version: Device.osVersion || null
    }).then(({ error }) => {
      if (error && __DEV__) console.warn('[AppLogger] Crash Telemetry dual-write failed:', error.message);
    }, (e: unknown) => {
      if (__DEV__) console.warn('[AppLogger] Crash Telemetry insert rejected (network):', e instanceof Error ? e.message : String(e));
    });
  }

  static async uploadTelemetrySnapshots(
    buffer: LogEntry[],
    sessionId: string,
    userId: string | undefined,
    onSuccess: (successfulCount: number) => void
  ) {
    if (!supabase) {
      if (__DEV__) console.log('[AppLogger] Supabase client not configured. Skipping snapshot ingestion.');
      return;
    }
    if (buffer.length === 0) return;

    try {
      // â”€â”€ [TELEMETRY MASTER GATE] â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
      // Route through AppSettingsService instead of direct AsyncStorage access (R-24).
      const settings = await AppSettingsService.fetchAllSettings();
      const telemetryEnabled = settings.global_telemetry_enabled !== false && settings.global_telemetry_enabled !== 'false';

      if (!telemetryEnabled) {
        if (__DEV__) console.log('[AppLogger] Global telemetry is DISABLED. Wiping buffer and aborting upload.');
        onSuccess(buffer.length); // Wipe it completely
        return;
      }
      // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

      const deviceId = Device.osInternalBuildId || Device.modelId || 'unknown';
      let successfulCount = 0;
      const CHUNK = 500;
      
      if (__DEV__) console.log(`[AppLogger] Pushing ${buffer.length} events to telemetry_snapshots...`);

      for (let i = 0; i < buffer.length; i += CHUNK) {
        const chunk = buffer.slice(i, i + CHUNK);
        const dbPayload = chunk.map((item: LogEntry) => {
          const isGroup = item.d?.target === 'group' || (item.d?.deviceIds && item.d.deviceIds.length > 1);
          const tgtDev = isGroup ? null : (item.d?.deviceId || null);

          return {
             session_id: sessionId,
             device_id: tgtDev,
             event_type: item.e,
             metadata: {
               timestamp_ms: item.t,
               host_device_id: deviceId,
               ...item.d
             },
             user_id: userId
          };
        });

        const { error } = await supabase.from('telemetry_snapshots').insert(dbPayload);
        if (error) {
           if (__DEV__) console.warn('[AppLogger] Chunk push failed:', error.message);
           break;
        }
        successfulCount += chunk.length;
      }

      if (successfulCount > 0) {
        if (__DEV__) console.log(`[AppLogger] Ingestion complete. ${successfulCount} items uploaded.`);
        onSuccess(successfulCount);
      } else {
        if (__DEV__) console.log('[AppLogger] Ingestion failed or no items uploaded. Buffer preserved.');
        onSuccess(0);
      }
    } catch (err: unknown) {
      const errorMsg = err instanceof Error ? err.message : String(err);
      if (__DEV__) console.warn('[AppLogger] Ingestion exception (safely caught):', errorMsg);
    }
  }

  static async clearCloudLogs(activeDevices: BleDevice[]) {
    if (!supabase) return;
    
    const pMac = activeDevices.length > 0 ? activeDevices[0].id : 'unpaired-host';
    const bleMac = pMac.replace(/[^a-zA-Z0-9_-]/g, '');
    
    const rawId = Device.osInternalBuildId || Device.modelId || 'unknown-device';
    const deviceId = rawId.replace(/[^a-zA-Z0-9_-]/g, '_');

    try {
      await supabase.storage.from('sk8lytz-logs').remove([`logs_${bleMac}.json`, `logs_${deviceId}.json`]);
    } catch(e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      if (__DEV__) console.warn('Failed cloud wipe', msg);
    }
  }
}

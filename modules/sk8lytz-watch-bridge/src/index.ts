import { requireNativeModule } from 'expo-modules-core';
import { Platform } from 'react-native';

// ── Types ─────────────────────────────────────────────────────────────────────

/**
 * The session state payload pushed from phone → watch.
 *
 * CONTRACT: Every status value MUST be handled by BOTH native companions:
 *   - watchOS: WatchConnectivityManager.swift → handlePayload()
 *   - Wear OS: WearableCommunicationService.kt → onDataChanged()
 * Adding a new status here requires updating both companions.
 * Last audit: 2026-06-06 — all 4 states verified in both platforms.
 */
export interface WatchSessionState {
  /** 'ACTIVE', 'PAUSED', 'SUMMARY' (post-session card, 10s), or 'STOPPED' */
  status: 'ACTIVE' | 'STOPPED' | 'PAUSED' | 'SUMMARY';
  /** Current GPS speed in mph */
  speed?: number;
  /** Live heart rate in bpm */
  heartRate?: number;
  /** Active calories burned */
  calories?: number;
  /** ISO 8601 session start timestamp */
  startTime?: string;
  // ── Summary-only fields (status === 'SUMMARY') ──────────────────────────────
  /** Total session duration in seconds */
  totalDuration?: number;
  /** Total distance skated in miles */
  distance?: number;
  /** Average speed across session in mph */
  avgSpeed?: number;
  /** Peak heart rate recorded during session in bpm */
  peakHR?: number;
}

/** Commands the watch can send back to the phone. */
export type WatchCommand =
  | { type: 'START_SESSION' | 'STOP_SESSION' }
  | { type: 'WRITE_COLOR'; r: number; g: number; b: number }
  | { type: 'EXECUTE_PATTERN'; patternId: number; fg?: number[]; bg?: number[]; speed?: number };

/** Health telemetry relayed from the watch's HealthKit sensors. */
export interface WatchHealthUpdate {
  heartRate: number;
  calories: number;
  status?: string;
  startTimeMs?: number;
}

/**
 * Subscription handle returned by NativeModule.addListener.
 * Defined locally to avoid importing the internal Subscription type.
 */
interface Subscription {
  remove(): void;
}

// ── Native Module Interface ───────────────────────────────────────────────────

/**
 * Raw interface for the native Expo module.
 * NativeModule base is NOT typed generically here — we use nativeModule.addListener
 * directly which is part of the NativeModule base class (no EventsMap constraint needed).
 */
interface Sk8lytzWatchBridgeNative {
  syncSessionState(state: WatchSessionState): Promise<void>;
  sendMetricUpdate(metrics: Pick<WatchSessionState, 'speed' | 'heartRate' | 'calories'>): Promise<void>;
  isWatchReachable(): Promise<boolean>;
  startListening(): void;
  /** Provided by the expo-modules-core NativeModule base — wraps native event subscription. */
  addListener(eventName: string, listener: (payload: unknown) => void): Subscription;
}

// ── Module Instantiation ──────────────────────────────────────────────────────

const nativeModule = Platform.OS !== 'web'
  ? requireNativeModule<Sk8lytzWatchBridgeNative>('Sk8lytzWatchBridge')
  : null;

// ── Public API ────────────────────────────────────────────────────────────────

export const WatchBridge = {
  /**
   * Push session state to all connected watches (fire-and-forget).
   * Safe to call even when no watch is paired — silently no-ops on native side.
   */
  syncSessionState: (state: WatchSessionState): Promise<void> => {
    if (!nativeModule) return Promise.resolve();
    return nativeModule.syncSessionState(state);
  },

  /**
   * Push a live metric snapshot to the watch.
   * Caller is responsible for throttling (max once per 3 seconds).
   */
  sendMetricUpdate: (
    metrics: Pick<WatchSessionState, 'speed' | 'heartRate' | 'calories'>
  ): Promise<void> => {
    if (!nativeModule) return Promise.resolve();
    return nativeModule.sendMetricUpdate(metrics);
  },

  /** Returns true if at least one watch is paired and reachable right now. */
  isWatchReachable: (): Promise<boolean> => {
    if (!nativeModule) return Promise.resolve(false);
    return nativeModule.isWatchReachable();
  },

  /**
   * Subscribe to commands sent from the watch (START_SESSION / STOP_SESSION).
   * Returns an unsubscribe function — call it in useEffect cleanup.
   *
   * The native payload is { command: WatchCommand }; we extract and type-narrow here.
   */
  addWatchCommandListener: (handler: (command: WatchCommand) => void): (() => void) => {
    if (!nativeModule) return () => {};
    nativeModule.startListening();
    const subscription = nativeModule.addListener(
      'onWatchCommandReceived',
      (payload: unknown) => {
        // Type-narrow from unknown payload — safe, no `as any`
        if (
          payload !== null &&
          typeof payload === 'object' &&
          'type' in payload &&
          typeof (payload as Record<string, unknown>).type === 'string'
        ) {
          const p = payload as Record<string, unknown>;
          const type = p.type as string;
          if (type === 'START_SESSION' || type === 'STOP_SESSION') {
            handler({ type } as WatchCommand);
          } else if (type === 'WRITE_COLOR') {
            handler({
              type: 'WRITE_COLOR',
              r: typeof p.r === 'number' ? p.r : 0,
              g: typeof p.g === 'number' ? p.g : 0,
              b: typeof p.b === 'number' ? p.b : 0,
            } as WatchCommand);
          } else if (type === 'EXECUTE_PATTERN') {
            handler({
              type: 'EXECUTE_PATTERN',
              patternId: typeof p.patternId === 'number' ? p.patternId : 0,
              fg: Array.isArray(p.fg) ? p.fg : undefined,
              bg: Array.isArray(p.bg) ? p.bg : undefined,
              speed: typeof p.speed === 'number' ? p.speed : undefined,
            } as WatchCommand);
          }
        }
      }
    );
    return () => subscription.remove();
  },

  /**
   * Subscribe to health telemetry relayed from the watch (heart rate, calories).
   * The watch sends updates every 5 seconds during an active session.
   * Returns an unsubscribe function — call it in useEffect cleanup.
   */
  addWatchHealthListener: (handler: (update: WatchHealthUpdate) => void): (() => void) => {
    if (!nativeModule) return () => {};
    nativeModule.startListening();
    const subscription = nativeModule.addListener(
      'onWatchHealthUpdate',
      (payload: unknown) => {
        if (
          payload !== null &&
          typeof payload === 'object' &&
          'heartRate' in payload &&
          'calories' in payload
        ) {
          const p = payload as Record<string, unknown>;
          handler({
            heartRate: typeof p.heartRate === 'number' ? p.heartRate : 0,
            calories: typeof p.calories === 'number' ? p.calories : 0,
            status: typeof p.status === 'string' ? p.status : undefined,
            startTimeMs: typeof p.startTimeMs === 'number' ? p.startTimeMs : undefined,
          });
        }
      }
    );
    return () => subscription.remove();
  },
};

/**
 * useSessionTracking.ts — Session Recording Domain Hook
 *
 * Manages the lifecycle of a single skate session recording within Street Mode.
 * Accumulates speed samples, distance, and G-force data via injected refs,
 * builds the final ISessionSnapshot on stop, and drives the SessionSummaryModal.
 *
 * Design notes:
 *  - Session refs (sessionStartTimeRef, sessionSpeedSamplesRef, etc.) are exposed
 *    so the GPS callback inside useStreetMode can write directly without re-subscription.
 *  - No BLE interaction occurs in this hook.
 *  - Depends on: SpeedTrackingService (Supabase), AppLogger
 */
import { useState, useRef, useCallback } from 'react';
import { SpeedTrackingService } from '../services/SpeedTrackingService';
import type { ISessionSnapshot } from '../services/SpeedTrackingService';
import { AppLogger } from '../services/AppLogger';

export type SessionState = 'IDLE' | 'RECORDING' | 'COMPLETE';

export interface UseSessionTrackingResult {
  /** FSM-based session state — replaces scattered boolean flags */
  sessionState: SessionState;
  /** Kick off a new recording session. Idempotent if already RECORDING. */
  startSession: () => void;
  /** Finalize session, build snapshot, transition to COMPLETE. */
  stopSession: () => void;
  /** Dismiss modal and reset to IDLE. */
  dismissModal: () => void;
  /** The completed session snapshot (only set when sessionState === 'COMPLETE') */
  sessionSummary: ISessionSnapshot | null;
  /** Controls SessionSummaryModal visibility */
  showSessionModal: boolean;
  setShowSessionModal: (v: boolean) => void;
  /** Persist the snapshot to Supabase via SpeedTrackingService. */
  saveSession: () => Promise<void>;

  // ── Accumulator refs exposed for GPS callback injection ──────────────────
  /** Unix ms timestamp when session started, null if not recording */
  sessionStartTimeRef: React.MutableRefObject<number | null>;
  /** Rolling array of GPS speed samples (mph) during the recording */
  sessionSpeedSamplesRef: React.MutableRefObject<number[]>;
  /** Accumulated distance in miles during the recording */
  sessionDistanceMilesRef: React.MutableRefObject<number>;
  /** Peak G-Force reading during the recording */
  sessionPeakGForceRef: React.MutableRefObject<number>;
  /** Peak GPS speed (mph) during the recording */
  sessionPeakSpeedRef: React.MutableRefObject<number>;
}

export function useSessionTracking(): UseSessionTrackingResult {
  const [sessionState, setSessionState] = useState<SessionState>('IDLE');
  const [sessionSummary, setSessionSummary] = useState<ISessionSnapshot | null>(null);
  const [showSessionModal, setShowSessionModal] = useState<boolean>(false);

  // Accumulator refs — stable across renders, safe for GPS callback injection
  const sessionStartTimeRef = useRef<number | null>(null);
  const sessionSpeedSamplesRef = useRef<number[]>([]);
  const sessionDistanceMilesRef = useRef<number>(0);
  const sessionPeakGForceRef = useRef<number>(1.0);
  const sessionPeakSpeedRef = useRef<number>(0);

  const startSession = useCallback(() => {
    if (sessionState === 'RECORDING') return; // Idempotent guard

    // Reset all accumulators for a fresh session
    sessionStartTimeRef.current = Date.now();
    sessionSpeedSamplesRef.current = [];
    sessionDistanceMilesRef.current = 0;
    sessionPeakGForceRef.current = 1.0;
    sessionPeakSpeedRef.current = 0;

    setSessionState('RECORDING');
    AppLogger.log('SESSION_SAVED', { action: 'START' });
  }, [sessionState]);

  const stopSession = useCallback(() => {
    if (sessionState !== 'RECORDING') return;

    // Build the snapshot from accumulated data
    const durationSec = sessionStartTimeRef.current
      ? (Date.now() - sessionStartTimeRef.current) / 1000
      : 0;

    const samples = sessionSpeedSamplesRef.current;
    const avgSpeed = samples.length > 0
      ? samples.reduce((acc, s) => acc + s, 0) / samples.length
      : 0;

    const snapshot: ISessionSnapshot = {
      durationSec,
      distanceMiles: sessionDistanceMilesRef.current,
      peakSpeedMph: sessionPeakSpeedRef.current,
      avgSpeedMph: parseFloat(avgSpeed.toFixed(2)),
      peakGForce: sessionPeakGForceRef.current,
    };

    // Clear start sentinel so GPS callback stops accumulating
    sessionStartTimeRef.current = null;

    setSessionSummary(snapshot);
    setSessionState('COMPLETE');
    setShowSessionModal(true);
  }, [sessionState]);

  const saveSession = useCallback(async () => {
    if (!sessionSummary) return;

    try {
      await SpeedTrackingService.saveSession(sessionSummary);
      AppLogger.log('SESSION_SAVED', { action: 'SAVED_TO_DB', durationSec: sessionSummary.durationSec });
    } catch (err) {
      console.warn('[useSessionTracking] Failed to persist session to Supabase:', err);
    }
  }, [sessionSummary]);

  const dismissModal = useCallback(() => {
    setShowSessionModal(false);
    setSessionSummary(null);
    setSessionState('IDLE');
  }, []);

  return {
    sessionState,
    startSession,
    stopSession,
    dismissModal,
    sessionSummary,
    showSessionModal,
    setShowSessionModal,
    saveSession,
    sessionStartTimeRef,
    sessionSpeedSamplesRef,
    sessionDistanceMilesRef,
    sessionPeakGForceRef,
    sessionPeakSpeedRef,
  };
}

export interface TelemetrySnapshot {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
  sessionPeakSpeed: number;
  sessionAvgSpeed: number;
  startCoords?: { lat: number; lng: number };
  endCoords?: { lat: number; lng: number };
  pathCoords?: Array<{ lat: number; lng: number }>;
}

export interface HealthSnapshot {
  latestBpm: number | null;
  avgBpm: number | null;
  peakBpm: number | null;
  activeCalories: number | null;
}

export type SessionPhase = 'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING';

export interface SessionMachineContext {
  sessionPhase: SessionPhase;
  startTimeMs: number | null;
  pauseStartTimeMs: number | null;
  pausedMsAccum: number;
  autoPauseEnabled: boolean;
  gpsSpeedRef: { current: number };
  telemetryRef: { current: TelemetrySnapshot };
  healthRef: { current: HealthSnapshot };
  userIdRef: { current: string | null };
  onTelemetryUpdate: (t: TelemetrySnapshot) => void;
  onHealthUpdate: (h: HealthSnapshot) => void;
  onSessionSaved: () => void;
  externalStartTimeMs?: number;
}

export type SessionMachineEvent =
  | { type: 'START'; externalStartTimeMs?: number }
  | { type: 'PAUSE' }
  | { type: 'RESUME' }
  | { type: 'END' }
  | { type: 'AUTO_PAUSE' }
  | { type: 'AUTO_RESUME' };

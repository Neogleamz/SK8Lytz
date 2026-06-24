// blast-radius reviewed 2026-06-24: sk8lytz-watch-bridge module restored verbatim from 82b18f14 — public API unchanged, no consumer interface updates required
import { useSession, HealthTelemetry } from '../context/SessionContext';

/**
 * useHealthTelemetry — Surfaces live health data from the Watch Bridge via SessionContext.
 * The Watch Bridge native module streams WearOS/Apple Watch BPM and calorie data into
 * the session machine's healthRef; this hook exposes the latest snapshot for UI consumers.
 */
export function useHealthTelemetry(): HealthTelemetry {
  const { health } = useSession();
  return health;
}

import { useState, useEffect } from 'react';

const API_BASE = 'http://localhost:5999';

export interface TelemetryOutCard {
  title: string;
  status: string;
  type: 'success' | 'rejected';
  data: [string, string, string][];
}

export interface DaemonTelemetry {
  active_job?: string;
  target?: string;
  in_q?: string[];
  rejected?: TelemetryOutCard[];
  success?: TelemetryOutCard[];
}

export interface PipelineTelemetry {
  scout?: DaemonTelemetry;
  spider?: DaemonTelemetry;
  detective?: DaemonTelemetry;
  photographer?: DaemonTelemetry;
  publisher?: DaemonTelemetry;
}

export function useScraperTelemetry(pollingIntervalMs: number = 2000) {
  const [telemetry, setTelemetry] = useState<PipelineTelemetry>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true;

    const fetchTelemetry = async () => {
      try {
        const response = await fetch(`${API_BASE}/api/pipeline/telemetry`);
        if (!response.ok) {
          throw new Error(`HTTP Error: ${response.status}`);
        }
        const data = await response.json();
        if (isMounted) {
          setTelemetry(data);
          setError(null);
        }
      } catch (err: any) {
        if (isMounted) {
          setError(err.message);
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    // Initial fetch
    fetchTelemetry();

    // Poll
    const interval = setInterval(fetchTelemetry, pollingIntervalMs);

    return () => {
      isMounted = false;
      clearInterval(interval);
    };
  }, [pollingIntervalMs]);

  return { telemetry, loading, error };
}

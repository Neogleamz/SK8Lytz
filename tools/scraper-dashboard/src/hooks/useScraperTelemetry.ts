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
  detective?: DaemonTelemetry;
  photographer?: DaemonTelemetry;
  publisher?: DaemonTelemetry;
}

export function useScraperTelemetry(pollingIntervalMs: number = 2000) {
  const [telemetry, setTelemetry] = useState<PipelineTelemetry>({});
  const [config, setConfig] = useState<any>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true;

    const fetchTelemetry = async () => {
      try {
        const [telRes, confRes] = await Promise.all([
          fetch(`${API_BASE}/api/pipeline/telemetry`),
          fetch(`${API_BASE}/config`).catch(() => ({ ok: true, json: () => ({}) } as any))
        ]);

        if (!telRes.ok) {
          throw new Error(`HTTP Error: ${telRes.status}`);
        }
        
        const data = await telRes.json();
        const confData = await confRes.json();

        if (isMounted) {
          setTelemetry(data);
          if (confData.config) setConfig(confData.config);
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

  return { telemetry, config, loading, error };
}

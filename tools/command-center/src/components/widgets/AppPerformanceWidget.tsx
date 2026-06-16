import { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

interface TelemetryRow {
  created_at: string | null;
  event_type: string;
  metadata: unknown;
  session_id: string;
}

interface ChartDataPoint {
  time: string;
  ttid: number;
  ttfd: number;
}

interface TelemetryMetadata {
  screen: string;
  ttidMs?: number;
  ttfdMs?: number;
}

function parseMetadata(metadata: unknown): TelemetryMetadata | null {
  if (metadata && typeof metadata === 'object' && !Array.isArray(metadata)) {
    const obj = metadata as Record<string, unknown>;
    if (typeof obj.screen === 'string') {
      return {
        screen: obj.screen,
        ttidMs: typeof obj.ttidMs === 'number' ? obj.ttidMs : undefined,
        ttfdMs: typeof obj.ttfdMs === 'number' ? obj.ttfdMs : undefined,
      };
    }
  }
  return null;
}

function getMedian(values: number[]): number {
  if (values.length === 0) return 0;
  const sorted = [...values].sort((a, b) => a - b);
  const half = Math.floor(sorted.length / 2);
  if (sorted.length % 2 !== 0) {
    return sorted[half];
  }
  return (sorted[half - 1] + sorted[half]) / 2.0;
}

function getAverageGap(points: ChartDataPoint[]): number {
  if (points.length === 0) return 0;
  const sum = points.reduce((acc, p) => acc + (p.ttfd - p.ttid), 0);
  return sum / points.length;
}

export default function AppPerformanceWidget() {
  const [rawTelemetry, setRawTelemetry] = useState<TelemetryRow[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchPerformanceData = async (silent = false) => {
    if (!silent) setLoading(true);
    const { data, error } = await supabase
      .from('telemetry_snapshots')
      .select('created_at, event_type, metadata, session_id')
      .in('event_type', ['SCREEN_LOAD_TTID', 'SCREEN_LOAD_TTFD'])
      .order('created_at', { ascending: true })
      .limit(500);

    if (data && !error) {
      setRawTelemetry(data as TelemetryRow[]);
    }
    if (!silent) setLoading(false);
  };

  useEffect(() => {
    fetchPerformanceData(false);
    const interval = setInterval(() => {
      fetchPerformanceData(true);
    }, 10000);
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return <div className="text-cyan-400">Loading App Performance Metrics...</div>;
  }

  // Parse and group telemetry data in-memory by screen
  const screenGroups: Record<string, { ttid: { val: number; time: string; session: string }[]; ttfd: { val: number; time: string; session: string }[] }> = {};

  for (const item of rawTelemetry) {
    const parsed = parseMetadata(item.metadata);
    if (!parsed) continue;

    const screen = parsed.screen;
    if (!screenGroups[screen]) {
      screenGroups[screen] = { ttid: [], ttfd: [] };
    }

    const timeStr = item.created_at || '';
    const session = item.session_id || '';

    if (item.event_type === 'SCREEN_LOAD_TTID' && parsed.ttidMs !== undefined) {
      screenGroups[screen].ttid.push({ val: parsed.ttidMs, time: timeStr, session });
    } else if (item.event_type === 'SCREEN_LOAD_TTFD' && parsed.ttfdMs !== undefined) {
      screenGroups[screen].ttfd.push({ val: parsed.ttfdMs, time: timeStr, session });
    }
  }

  // Correlate TTID and TTFD chronologically
  const pairedData: Record<string, ChartDataPoint[]> = {};

  for (const [screen, group] of Object.entries(screenGroups)) {
    const points: ChartDataPoint[] = [];
    const ttfdCandidates = [...group.ttfd];

    for (const ttidEntry of group.ttid) {
      let matchIdx = -1;

      // 1. Try matching by session first
      if (ttidEntry.session) {
        matchIdx = ttfdCandidates.findIndex(c => c.session === ttidEntry.session);
      }

      // 2. If no session match, find closest chronological match where TTFD is after TTID
      if (matchIdx === -1) {
        const ttidTime = new Date(ttidEntry.time).getTime();
        let minDiff = Infinity;
        for (let i = 0; i < ttfdCandidates.length; i++) {
          const candidateTime = new Date(ttfdCandidates[i].time).getTime();
          const diff = candidateTime - ttidTime;
          if (diff >= 0 && diff < 30000 && diff < minDiff) {
            minDiff = diff;
            matchIdx = i;
          }
        }
      }

      let ttfdVal = ttidEntry.val; // default to same value if not found
      if (matchIdx !== -1) {
        ttfdVal = ttfdCandidates[matchIdx].val;
        ttfdCandidates.splice(matchIdx, 1);
      }

      points.push({
        time: new Date(ttidEntry.time).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }),
        ttid: ttidEntry.val,
        ttfd: ttfdVal,
      });
    }

    points.sort((a, b) => a.time.localeCompare(b.time));
    pairedData[screen] = points;
  }

  const screens = Object.keys(pairedData);

  return (
    <div className="flex flex-col gap-6">
      <div className="flex justify-between items-center mb-2">
        <h2 className="text-2xl font-bold text-white">Screen Latency & Hydration Telemetry</h2>
      </div>

      {screens.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
          {screens.map(screen => {
            const points = pairedData[screen];
            const ttidValues = points.map(p => p.ttid);
            const ttfdValues = points.map(p => p.ttfd);
            const medianTtid = Math.round(getMedian(ttidValues));
            const medianTtfd = Math.round(getMedian(ttfdValues));
            const avgGap = Math.round(getAverageGap(points));

            return (
              <div key={screen} className="glass-panel p-4 rounded-xl border border-slate-800 hover:border-cyan-500/30 transition-all duration-300 flex flex-col relative">
                <h3 className="text-lg font-semibold text-white mb-3">{screen}</h3>

                <div className="grid grid-cols-3 gap-2 mb-4 text-center">
                  <div className="bg-slate-900/60 border border-slate-800/50 p-2 rounded-lg flex flex-col items-center justify-center">
                    <span className="text-[10px] text-slate-400 font-medium uppercase tracking-wider">Median TTID</span>
                    <span className="text-base font-bold text-cyan-400 mt-0.5">
                      {medianTtid}
                      <span className="text-[10px] font-normal text-slate-500 ml-0.5">ms</span>
                    </span>
                  </div>
                  <div className="bg-slate-900/60 border border-slate-800/50 p-2 rounded-lg flex flex-col items-center justify-center">
                    <span className="text-[10px] text-slate-400 font-medium uppercase tracking-wider">Median TTFD</span>
                    <span className="text-base font-bold text-emerald-400 mt-0.5">
                      {medianTtfd}
                      <span className="text-[10px] font-normal text-slate-500 ml-0.5">ms</span>
                    </span>
                  </div>
                  <div className="bg-slate-900/60 border border-slate-800/50 p-2 rounded-lg flex flex-col items-center justify-center">
                    <span className="text-[10px] text-slate-400 font-medium uppercase tracking-wider">Avg Gap</span>
                    <span className={`text-base font-bold mt-0.5 ${avgGap > 200 ? 'text-amber-400' : 'text-slate-300'}`}>
                      {avgGap}
                      <span className="text-[10px] font-normal text-slate-500 ml-0.5">ms</span>
                    </span>
                  </div>
                </div>

                <div className="w-full h-48">
                  <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={points} margin={{ top: 5, right: 5, left: -20, bottom: 0 }}>
                      <defs>
                        <linearGradient id={`colorTtid-${screen}`} x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#22d3ee" stopOpacity={0.6}/>
                          <stop offset="95%" stopColor="#22d3ee" stopOpacity={0}/>
                        </linearGradient>
                        <linearGradient id={`colorTtfd-${screen}`} x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#10b981" stopOpacity={0.4}/>
                          <stop offset="95%" stopColor="#10b981" stopOpacity={0}/>
                        </linearGradient>
                      </defs>
                      <XAxis dataKey="time" stroke="#64748b" tick={{ fill: '#64748b', fontSize: 10 }} />
                      <YAxis stroke="#64748b" tick={{ fill: '#64748b', fontSize: 10 }} />
                      <CartesianGrid strokeDasharray="3 3" stroke="#334155" vertical={false} />
                      <Tooltip
                        contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', color: '#fff', borderRadius: '8px', fontSize: 11 }}
                      />
                      <Area type="monotone" dataKey="ttfd" stroke="#10b981" fillOpacity={1} fill={`url(#colorTtfd-${screen})`} name="TTFD" />
                      <Area type="monotone" dataKey="ttid" stroke="#22d3ee" fillOpacity={1} fill={`url(#colorTtid-${screen})`} name="TTID" />
                    </AreaChart>
                  </ResponsiveContainer>
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        <div className="glass-panel p-8 rounded-xl border border-slate-800 flex flex-col items-center justify-center text-slate-500 min-h-[300px]">
          <span className="text-4xl mb-3">📈</span>
          <span className="text-lg font-medium text-slate-400">Awaiting Performance Telemetry</span>
          <p className="text-sm text-slate-600 mt-1">No screen load telemetry events have been recorded yet.</p>
        </div>
      )}
    </div>
  );
}

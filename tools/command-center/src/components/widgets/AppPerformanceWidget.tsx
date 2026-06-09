import React, { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, ScatterChart, Scatter, ZAxis } from 'recharts';

interface PerformanceData {
  created_at: string;
  average_load_time_ms: number;
  battery_level: number;
  os_name: string;
  is_low_power_mode: boolean;
}

export default function AppPerformanceWidget() {
  const [data, setData] = useState<PerformanceData[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPerformanceData();
  }, []);

  const fetchPerformanceData = async () => {
    setLoading(true);
    const { data, error } = await supabase
      .from('parsed_session_stats')
      .select('created_at, average_load_time_ms, battery_level, os_name, is_low_power_mode')
      .order('created_at', { ascending: true })
      .limit(100);
      
    if (data && !error) {
      setData(data as PerformanceData[]);
    }
    setLoading(false);
  };

  if (loading) {
    return <div className="text-cyan-400">Loading App Performance Metrics...</div>;
  }

  // Format data for timeline
  const timelineData = data.map(d => ({
    time: new Date(d.created_at).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    loadTime: d.average_load_time_ms,
    battery: d.battery_level * 100 // assuming 0-1
  }));

  return (
    <div className="flex flex-col gap-6">
      <div className="flex justify-between items-center mb-2">
        <h2 className="text-2xl font-bold text-white">App Performance Telemetry</h2>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="glass-panel p-4 rounded-xl border border-slate-800 flex flex-col">
          <h3 className="text-lg font-semibold text-white mb-4">Average App Load Time (ms)</h3>
          <div className="w-full h-64">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={timelineData} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorLoad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#22d3ee" stopOpacity={0.8}/>
                    <stop offset="95%" stopColor="#22d3ee" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <XAxis dataKey="time" stroke="#64748b" tick={{ fill: '#64748b' }} />
                <YAxis stroke="#64748b" tick={{ fill: '#64748b' }} />
                <CartesianGrid strokeDasharray="3 3" stroke="#334155" vertical={false} />
                <Tooltip 
                  contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', color: '#fff', borderRadius: '8px' }}
                />
                <Area type="monotone" dataKey="loadTime" stroke="#22d3ee" fillOpacity={1} fill="url(#colorLoad)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="glass-panel p-4 rounded-xl border border-slate-800 flex flex-col">
          <h3 className="text-lg font-semibold text-white mb-4">Battery Drain vs Load Time</h3>
          <div className="w-full h-64">
            <ResponsiveContainer width="100%" height="100%">
              <ScatterChart margin={{ top: 20, right: 20, bottom: 20, left: 20 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                <XAxis type="number" dataKey="battery" name="Battery %" unit="%" stroke="#64748b" tick={{ fill: '#64748b' }} />
                <YAxis type="number" dataKey="loadTime" name="Load Time" unit="ms" stroke="#64748b" tick={{ fill: '#64748b' }} />
                <Tooltip cursor={{ strokeDasharray: '3 3' }} contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155', borderRadius: '8px' }} />
                <Scatter name="Sessions" data={timelineData} fill="#f43f5e" />
              </ScatterChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}

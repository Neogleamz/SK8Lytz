import { useEffect, useState } from 'react';
import { supabase } from '../../services/supabase';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer, BarChart, Bar, XAxis, YAxis, CartesianGrid } from 'recharts';

// Suppress known Recharts false-positive warning in React 18 StrictMode
const originalWarn = console.warn;
console.warn = (...args) => {
  if (typeof args[0] === 'string' && args[0].includes('The width(-1) and height(-1) of chart should be greater than 0')) {
    return;
  }
  originalWarn(...args);
};

interface DeviceData {
  device_mac: string | null;
  firmware_ver: number | null;
  ble_version: number | null;
  product_type: string | null;
  updated_at?: string | null;
}

const COLORS = ['#22d3ee', '#3b82f6', '#6366f1', '#a855f7', '#ec4899', '#f43f5e'];

export default function FleetHealthWidget() {
  const [data, setData] = useState<DeviceData[]>([]);
  const [telemetry, setTelemetry] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [registeredDevices, setRegisteredDevices] = useState(0);
  const [registeredGroups, setRegisteredGroups] = useState(0);

  useEffect(() => {
    fetchDeviceData();
  }, []);

  const fetchDeviceData = async () => {
    setLoading(true);
    
    // Fetch telemetry and registered devices concurrently
    const [
      { data: telemetryData },
      { data: registeredData, count: devCount },
      { count: grpCount }
    ] = await Promise.all([
      supabase.from('discovered_devices_telemetry').select('device_mac, ble_version, firmware_ver, discovered_at'),
      supabase.from('registered_devices').select('device_mac, firmware_ver, ble_version, product_type, updated_at', { count: 'exact' }),
      supabase.from('registered_groups').select('*', { count: 'exact', head: true })
    ]);
      
    if (registeredData && telemetryData) {
      // Create a lookup map of the latest telemetry per MAC
      const telemetryMap = new Map();
      telemetryData.forEach(t => {
        if (t.device_mac) {
          telemetryMap.set(t.device_mac, t);
        }
      });

      // Enrich registered data with telemetry fallback
      const enrichedData = registeredData.map(d => {
        const t = d.device_mac ? telemetryMap.get(d.device_mac) : null;
        return {
          ...d,
          ble_version: d.ble_version || t?.ble_version || null,
          firmware_ver: d.firmware_ver || t?.firmware_ver || null,
        };
      });

      setData(enrichedData as DeviceData[]);
    }
    
    if (telemetryData) {
      setTelemetry(telemetryData);
    }
    setRegisteredDevices(devCount || 0);
    setRegisteredGroups(grpCount || 0);
    setLoading(false);
  };

  // KPI Calculations
  const totalDevices = telemetry.length; // Scanned Devices
  
  const now = new Date();
  const active24h = data.filter(d => {
    if (!d.updated_at) return false;
    const diff = now.getTime() - new Date(d.updated_at).getTime();
    return diff <= 24 * 60 * 60 * 1000;
  }).length;

  const getLatestFirmwareStr = () => {
    if (!data.length) return '0%';
    const versions = data.map(d => Number(d.firmware_ver) || 0);
    const maxVer = Math.max(...versions);
    const onLatest = data.filter(d => Number(d.firmware_ver) === maxVer).length;
    return `${Math.round((onLatest / data.length) * 100)}%`;
  };

  // Usage Trends (Bar Chart logic based on ALL telemetry)
  const generateUsageTrends = () => {
    const buckets: Record<string, number> = {};
    for (let i = 6; i >= 0; i--) {
      const d = new Date();
      d.setDate(d.getDate() - i);
      const dateStr = d.toISOString().split('T')[0];
      buckets[dateStr] = 0;
    }

    telemetry.forEach(d => {
      if (d.discovered_at) {
        const dateStr = d.discovered_at.split('T')[0];
        if (buckets[dateStr] !== undefined) {
          buckets[dateStr]++;
        }
      }
    });

    return Object.keys(buckets).map(date => ({
      date: date.substring(5),
      active: buckets[date]
    }));
  };

  const usageTrends = generateUsageTrends();

  // Fragmentation Aggregation (based on Registered Devices)
  const aggregateData = (key: keyof DeviceData) => {
    const counts = data.reduce((acc, curr) => {
      const val = curr[key] || 'Unknown';
      acc[val as string] = (acc[val as string] || 0) + 1;
      return acc;
    }, {} as Record<string, number>);

    return Object.keys(counts).map(name => ({
      name,
      value: counts[name]
    })).sort((a, b) => b.value - a.value);
  };

  const firmwareData = aggregateData('firmware_ver');
  const bleData = aggregateData('ble_version');
  const productData = aggregateData('product_type');

  if (loading) {
    return <div className="text-cyan-400 animate-pulse text-lg font-medium">Scanning Telemetry Systems...</div>;
  }

  const renderPieChart = (title: string, chartData: any[]) => (
    <div className="glass-panel p-4 rounded-xl border border-slate-800/60 bg-[#0f172a]/40 flex flex-col items-center relative">
      <h3 className="text-sm font-semibold text-slate-300 mb-2 uppercase tracking-widest">{title}</h3>
      <div className="w-full h-48">
        {chartData.length > 0 ? (
          <div style={{ width: '100%', height: 192 }}>
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
              <Pie
                data={chartData}
                innerRadius={40}
                outerRadius={60}
                paddingAngle={5}
                dataKey="value"
                stroke="none"
              >
                {chartData.map((_, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip 
                contentStyle={{ backgroundColor: 'rgba(15, 23, 42, 0.9)', borderColor: 'rgba(34, 211, 238, 0.3)', color: '#fff', borderRadius: '8px', backdropFilter: 'blur(8px)' }}
                itemStyle={{ color: '#22d3ee', fontWeight: 'bold' }}
              />
              <Legend verticalAlign="bottom" height={36} iconType="circle" wrapperStyle={{ fontSize: '12px', color: '#cbd5e1' }} />
            </PieChart>
          </ResponsiveContainer>
          </div>
        ) : (
          <div className="absolute inset-0 flex items-center justify-center flex-col text-slate-600 mt-6">
            <span className="text-xl mb-1">📊</span>
            <span className="text-xs">No Data</span>
          </div>
        )}
      </div>
    </div>
  );

  return (
    <div className="flex flex-col gap-6 pb-10 pr-2 overflow-y-auto h-full">
      <div className="flex justify-between items-center mb-2 shrink-0">
        <div>
          <h2 className="text-2xl font-bold text-white tracking-wide">Fleet Telemetry</h2>
          <p className="text-slate-400 text-sm mt-1">Live metrics from discovered devices and connection events.</p>
        </div>
      </div>
      
      {/* Tier 1: KPIs */}
      <div className="grid grid-cols-2 md:grid-cols-5 gap-4 shrink-0">
        <div className="glass-panel p-4 rounded-xl flex flex-col items-center justify-center text-center">
          <div className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-2">Scanned Devices</div>
          <div className="text-3xl font-black text-cyan-400">{totalDevices}</div>
        </div>
        <div className="glass-panel p-4 rounded-xl flex flex-col items-center justify-center text-center">
          <div className="text-slate-400 text-[10px] font-bold uppercase tracking-wider mb-2">Registered Skates</div>
          <div className="text-3xl font-black text-blue-400">{registeredDevices}</div>
        </div>
        <div className="glass-panel p-4 rounded-xl flex flex-col items-center justify-center text-center">
          <div className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-2">Active Groups</div>
          <div className="text-3xl font-black text-indigo-400">{registeredGroups}</div>
        </div>
        <div className="glass-panel p-4 rounded-xl flex flex-col items-center justify-center text-center">
          <div className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-2">Active (24h)</div>
          <div className="text-3xl font-black text-green-400">{active24h}</div>
        </div>
        <div className="glass-panel p-4 rounded-xl flex flex-col items-center justify-center text-center">
          <div className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-2">Latest FW Uptake</div>
          <div className="text-3xl font-black text-purple-400">{getLatestFirmwareStr()}</div>
        </div>
      </div>

      {/* Tier 2: Usage Trends */}
      <div className="glass-panel p-6 rounded-xl border border-slate-700/50 bg-[#0f172a]/60 shadow-xl shrink-0">
        <h3 className="text-lg font-bold text-white mb-6 tracking-wide">Fleet Detection Volume (7 Days)</h3>
        <div style={{ width: '100%', height: 256 }}>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={usageTrends} margin={{ top: 5, right: 20, bottom: 5, left: -20 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" vertical={false} />
              <XAxis dataKey="date" stroke="#64748b" tick={{ fill: '#94a3b8', fontSize: 12 }} tickLine={false} axisLine={false} />
              <YAxis stroke="#64748b" tick={{ fill: '#94a3b8', fontSize: 12 }} tickLine={false} axisLine={false} />
              <Tooltip 
                cursor={{ fill: 'rgba(34, 211, 238, 0.05)' }}
                contentStyle={{ backgroundColor: 'rgba(15, 23, 42, 0.95)', borderColor: 'rgba(34, 211, 238, 0.4)', borderRadius: '8px', color: '#fff' }}
                itemStyle={{ color: '#22d3ee', fontWeight: 'bold' }}
              />
              <Bar dataKey="active" name="Devices Discovered" fill="#22d3ee" radius={[4, 4, 0, 0]} barSize={40} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Tier 3: Fragmentation */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 shrink-0 mt-2">
        {renderPieChart('Firmware Distribution', firmwareData)}
        {renderPieChart('BLE Chipset Rollout', bleData)}
        {renderPieChart('Hardware Products', productData)}
      </div>
    </div>
  );
}
